package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.command.GetDataUsageInfoCmd;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.command.SyncDockerCmd;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataUsageInfoCmdExec extends AbstrSyncDockerCmdExec<GetDataUsageInfoCmd, GetDataUsageInfoResponse> implements
        GetDataUsageInfoCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetDataUsageInfoCmdExec.class);

    public GetDataUsageInfoCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    @Override
    protected GetDataUsageInfoResponse execute(GetDataUsageInfoCmd command) {
        WebTarget webResource = getBaseResource().path("/system/df");
        LOGGER.trace("GET: {}", webResource);

        return webResource.request().accept(MediaType.APPLICATION_JSON).get(new TypeReference<GetDataUsageInfoResponse>() {});
    }
}
