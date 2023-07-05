package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.GetDataUsageInfoCmd;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;

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
