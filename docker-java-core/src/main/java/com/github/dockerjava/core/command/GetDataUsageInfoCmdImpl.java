package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.GetDataUsageInfoCmd;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;
import com.github.dockerjava.api.command.InspectVolumeCmd;
import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.exception.NotFoundException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Inspect the details of a data usage.
 */
public class GetDataUsageInfoCmdImpl extends AbstrDockerCmd<GetDataUsageInfoCmd, GetDataUsageInfoResponse> implements
        GetDataUsageInfoCmd {

    public GetDataUsageInfoCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public GetDataUsageInfoResponse exec() {
        return super.exec();
    }
}
