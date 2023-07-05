package com.github.dockerjava.api.model.datausage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerPort;
import com.github.dockerjava.api.model.DockerObject;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.NetworkSettings;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * ContainerSummary contains information about the data usage of the container.
 * Used for "Get Data Usage Info" request (/system/df)
 *
 * @author Dan Serbyn (dev.serbyn@gmail.com)
 * @see GetDataUsageInfoResponse
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class ContainerSummary extends DockerObject implements Serializable {

    /**
     * The ID of this container.
     */
    @JsonProperty("Id")
    private String id;
    /**
     * The names that this container has been given.
     */
    @JsonProperty("Names")
    private List<String> names;
    /**
     * The name of the image used when creating this container.
     */
    @JsonProperty("Image")
    private String image;
    /**
     * The ID of the image that this container was created from.
     */
    @JsonProperty("ImageID")
    private String imageID;
    /**
     * Command to run when starting the container.
     */
    @JsonProperty("Command")
    private String command;
    /**
     * When the container was created.
     */
    @JsonProperty("Created")
    private Instant created;
    /**
     * The ports exposed by this container.
     */
    @JsonProperty("Ports")
    private List<ContainerPort> ports;
    /**
     * The size of files (bytes) that have been created or changed by this container.
     * This doesn't consider data in the mounted volumes.
     */
    @JsonProperty("SizeRw")
    private Long sizeRw;
    /**
     * The total size of all the files in this container.
     */
    @JsonProperty("SizeRootFs")
    private Long sizeRootFs;
    /**
     * User-defined key/value metadata.
     * e.g. [["com.ex.label-1": "some-value"], ["com.ex.label-2": "other-value"]]
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    /**
     * The state of this container (e.g. "Exited").
     */
    @JsonProperty("State")
    private String state;

    /**
     * Additional human-readable status of this container (e.g. "Exit 0").
     */
    @JsonProperty("Status")
    private String status;

    /**
     * Container configuration that depends on the host we are running on.
     * Only network mode is populated for ContainerSummary.
     * @see HostConfig#getNetworkMode()
     */
    @JsonProperty("HostConfig")
    private HostConfig hostConfig;

    /**
     * A summary of the container's network settings.
     */
    @JsonProperty("NetworkSettings")
    private NetworkSettings networkSettings;

    /**
     * A list of mounts.
     * @see com.github.dockerjava.api.command.InspectContainerResponse.Mount
     */
    @JsonProperty("Mounts")
    private List<InspectContainerResponse.Mount> mounts;

    public String getId() {
        return id;
    }

    public List<String> getNames() {
        return names;
    }

    public String getImage() {
        return image;
    }

    public String getImageID() {
        return imageID;
    }

    public String getCommand() {
        return command;
    }

    public Instant getCreated() {
        return created;
    }

    public List<ContainerPort> getPorts() {
        return ports;
    }

    public Long getSizeRw() {
        return sizeRw;
    }

    public Long getSizeRootFs() {
        return sizeRootFs;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public String getState() {
        return state;
    }

    public String getStatus() {
        return status;
    }

    public HostConfig getHostConfig() {
        return hostConfig;
    }

    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }

    public List<InspectContainerResponse.Mount> getMounts() {
        return mounts;
    }
}
