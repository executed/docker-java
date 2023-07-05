package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Config;
import com.github.dockerjava.api.model.InternetProtocol;
import com.github.dockerjava.api.model.MountType;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.datausage.BuildCache;
import com.github.dockerjava.api.model.datausage.ContainerSummary;
import com.github.dockerjava.api.model.datausage.ImageSummary;
import com.github.dockerjava.core.DockerRule;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.github.dockerjava.junit.PrivateRegistryRule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.api.model.ExposedPort.tcp;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.api.model.Ports.Binding.bindPort;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_27;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

public class GetDataUsageInfoCmdIT extends CmdIT {

    private static final String SH_CMD = "/bin/sh";
    private static final String TEST_CONTAINER_VOL_PATH = "/var/log";
    private static final String CONTAINER_NETWORK_MODE = "bridge";
    private static final String IMAGE_REPO_NAME = "docker-java-test";
    private static final String TEST_VOLUME_NAME = "test-volume";
    private static final String TEST_MOUNT_SOURCE = "/var/lib/docker/volumes/" + TEST_VOLUME_NAME + "/_data";
    private static final String LOCAL_DRIVER = "local";
    private static final String IMAGE_REPO_TAG = "tag1";
    private static final Map<String, String> testLabelsMap =
            Collections.singletonMap("com.github.dockerjava.usage", "test");

    @ClassRule
    public static PrivateRegistryRule REGISTRY = new PrivateRegistryRule();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("target/"));

    @Test
    public void testContainerDataUsageInfo() {
        final String containerName = this.createTestContainer(DockerRule.DEFAULT_IMAGE);
        // Data Usage
        GetDataUsageInfoResponse dataUsage = dockerRule.getClient().getDataUsageInfoCmd().exec();
        // Container summary
        ContainerSummary containerSummary = dataUsage.getContainers().stream()
            .filter(c -> c.getNames().stream().anyMatch(("/" + containerName)::equals))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("DataUsageInfo was not found for container " + containerName));

        assertFalse(containerSummary.getId().isEmpty());
        assertEquals(DockerRule.DEFAULT_IMAGE, containerSummary.getImage());
        assertFalse(containerSummary.getImageID().isEmpty());
        assertTrue(containerSummary.getCommand().startsWith(SH_CMD));
        assertTrue(ChronoUnit.MINUTES.between(containerSummary.getCreated(), Instant.now()) < 2);
        assertEquals("0.0.0.0", containerSummary.getPorts().get(0).getIp());
        assertEquals(Integer.valueOf(22), containerSummary.getPorts().get(0).getPrivatePort());
        assertEquals(Integer.valueOf(10022), containerSummary.getPorts().get(0).getPublicPort());
        assertEquals(InternetProtocol.TCP.toString(), containerSummary.getPorts().get(0).getType());
        // started container creates test_1.txt with content "123" = 3 bytes
        assertEquals(Long.valueOf(3), containerSummary.getSizeRw());
        assertTrue(containerSummary.getSizeRootFs() > 0);
        assertEquals(testLabelsMap, containerSummary.getLabels());
        assertEquals("running", containerSummary.getState());
        assertNotNull(containerSummary.getStatus());
        assertTrue(containerSummary.getStatus().toUpperCase(Locale.ROOT).contains("UP"));
        assertEquals(CONTAINER_NETWORK_MODE, containerSummary.getHostConfig().getNetworkMode());
        assertTrue(containerSummary.getNetworkSettings().getNetworks().containsKey(CONTAINER_NETWORK_MODE));
        // mounts
        assertEquals(TEST_VOLUME_NAME, containerSummary.getMounts().get(0).getName());
        assertEquals(MountType.VOLUME, containerSummary.getMounts().get(0).getType());
        assertEquals(TEST_CONTAINER_VOL_PATH, containerSummary.getMounts().get(0).getDestination().getPath());
        assertEquals(LOCAL_DRIVER, containerSummary.getMounts().get(0).getDriver());
        assertEquals(Boolean.TRUE, containerSummary.getMounts().get(0).getRW());
        assertEquals("rw", containerSummary.getMounts().get(0).getMode());
        assertEquals(TEST_MOUNT_SOURCE, containerSummary.getMounts().get(0).getSource());
        // InspectContainerResponse.Mount.propagation is OS-specific
    }

    @Test
    public void testImageDataUsageInfo() throws IOException, InterruptedException {
        // Build private registry image, so it has digest, labels, parent ID, etc.
        File baseDir = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
            .getResource("buildTests/ADD/file")).getFile());
        Collection<File> files = FileUtils.listFiles(baseDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        File tarFile = CompressArchiveUtil.archiveTARFiles(baseDir, files, UUID.randomUUID().toString());
        final String createdImageId = dockerRule.getClient().buildImageCmd()
            .withTarInputStream(new FileInputStream(tarFile))
            .withNoCache(false)
            .withLabels(testLabelsMap)
            .start()
            .awaitImageId();
        AuthConfig authConfig = REGISTRY.getAuthConfig();
        final String registryImgName = authConfig.getRegistryAddress() + "/" + IMAGE_REPO_NAME;
        dockerRule.getClient().authCmd().withAuthConfig(authConfig).exec();
        dockerRule.getClient().tagImageCmd(createdImageId, registryImgName, IMAGE_REPO_TAG)
                .withForce()
                .exec();
        dockerRule.getClient().pushImageCmd(registryImgName)
                .withAuthConfig(authConfig)
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS);
        // Create container based on image
        this.createTestContainer(createdImageId);
        // Image Data Usage
        GetDataUsageInfoResponse dataUsage = dockerRule.getClient().getDataUsageInfoCmd().exec();
        ImageSummary imageSummary = dataUsage.getImages().stream()
            .filter(is -> is.getRepoTags().stream().anyMatch((registryImgName + ":" + IMAGE_REPO_TAG)::equals))
            .findFirst()
            .orElseThrow(RuntimeException::new);
        // assertions
        assertTrue(imageSummary.getId().contains(createdImageId));
        assertTrue(StringUtils.isNotBlank(imageSummary.getParentId()));
        assertTrue(imageSummary.getRepoDigests().get(0).startsWith(registryImgName));
        assertTrue(ChronoUnit.MINUTES.between(imageSummary.getCreated(), Instant.now()) < 2);
        assertTrue(imageSummary.getSize() != null && imageSummary.getSize() > 0);
        assertTrue(imageSummary.getSharedSize() != null && imageSummary.getSharedSize() > 0);
        assertEquals(testLabelsMap, imageSummary.getLabels());
        assertEquals(Integer.valueOf(1), imageSummary.getContainers());
    }

    @Test
    public void testVolumeDataUsageInfo() {
        this.createTestContainer(DockerRule.DEFAULT_IMAGE);
        GetDataUsageInfoResponse dataUsage = dockerRule.getClient().getDataUsageInfoCmd().exec();
        InspectVolumeResponse volumeResponse = dataUsage.getVolumes().stream()
            .filter(v -> TEST_VOLUME_NAME.equals(v.getName()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("InspectVolumeResponse was not found for volume " + TEST_VOLUME_NAME));

        assertEquals(testLabelsMap, volumeResponse.getLabels());
        assertEquals(LOCAL_DRIVER, volumeResponse.getDriver());
        assertEquals(TEST_MOUNT_SOURCE, volumeResponse.getMountpoint());
        assertEquals(Long.valueOf(4), volumeResponse.getUsageData().getSize());
        assertEquals(Long.valueOf(1), volumeResponse.getUsageData().getRefCount());
    }

    @Test
    public void testBuildCacheDataUsageInfo() throws IOException {
        //this.createTestContainer(DockerRule.DEFAULT_IMAGE);

        //assumeThat(dockerRule, isGreaterOrEqual(VERSION_1_27));


        File dockerfile = folder.newFile("Dockerfile");
        String s =
            "# syntax=docker/dockerfile:1.2\n" +
            "FROM ubuntu:18.04\n" +
                "ENV DOCKER_BUILDKIT=1\n" +
                "ENV PIP_CACHE_DIR=/var/cache/buildkit/pip\n" +
                "RUN mkdir -p $PIP_CACHE_DIR\n" +
                "RUN rm -f /etc/apt/apt.conf.d/docker-clean\n" +
                "RUN --mount=type=cache,target=/var/cache/apt apt-get update && apt-get install -yqq --no-install-recommends && rm -rf /var/lib/apt/lists/*\n";
        FileUtils.writeStringToFile(dockerfile, s, true);

        File baseDir;
        InspectImageResponse inspectImageResponse;

//        baseDir = fileFromBuildTestResource("FROM/privateRegistry");
        baseDir = folder.getRoot();



        String imageId = dockerRule.getClient().buildImageCmd(baseDir)
                .withNoCache(false)
                .withBuildArg("DOCKER_BUILDKIT", "1")
                .start()
                .awaitImageId();

        inspectImageResponse = dockerRule.getClient().inspectImageCmd(imageId).exec();
        assertThat(inspectImageResponse, not(nullValue()));

        /*File baseDir = new File(Thread.currentThread().getContextClassLoader()
                .getResource("buildTests/CacheFrom/test3").getFile());
        String imageId = dockerRule.getClient().buildImageCmd(baseDir)
            .withTags(Set.of("new"))
            .withCacheFrom(Set.of("ubuntu:18.04"))
                .start()
                .awaitImageId();
        InspectImageResponse inspectImageResponse = dockerRule.getClient().inspectImageCmd(imageId).exec();
        assertNotNull(inspectImageResponse);

        GetDataUsageInfoResponse dataUsage = dockerRule.getClient().getDataUsageInfoCmd().exec();
        BuildCache buildCache = dataUsage.getBuildCache();
        assertNotNull(buildCache);*/
    }

    /**
     * Starts a test container with mounted volume with path defined in TEST_CONTAINER_VOL_PATH.
     * 3 bytes of data us written in container storage & 4 bytes of data into mounted volume.
     * Container port mapping - 10022:22. Network mode - bridge.
     * Both container & volume are created with labels defined in testLabelsMap.
     * @param image that is used to create a container.
     * @return started container name
     */
    private String createTestContainer(String image) {
        final String containerName = "generated_" + new SecureRandom().nextInt();
        // CREATE
        CreateVolumeResponse createVolumeResponse = dockerRule.getClient().execStartCmd()
            .createVolumeCmd()
            .withName(TEST_VOLUME_NAME)
            .withLabels(testLabelsMap)
            .withDriver(LOCAL_DRIVER)
            .exec();

        Volume volume = new Volume(TEST_CONTAINER_VOL_PATH);
        Bind volContainerBind = new Bind(createVolumeResponse.getName(), volume);

        CreateContainerResponse container = dockerRule.getClient()
            .createContainerCmd(image)
            .withName(containerName)
            // write 3 bytes of data to container & 4 bytes to mounted volume
            .withCmd(SH_CMD, "-c", "echo -n 123 > /test_1.txt && echo -n 1234 > " + TEST_CONTAINER_VOL_PATH + "/test_2.txt && sleep 9999")
            .withLabels(testLabelsMap)
            .withExposedPorts(tcp(22))
            .withHostConfig(newHostConfig()
                .withPortBindings(new Ports(tcp(22), bindPort(10022)))
                .withNetworkMode(CONTAINER_NETWORK_MODE)
                .withBinds(volContainerBind)
            )
            .exec();
        // Start container
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        return containerName;
    }
}
