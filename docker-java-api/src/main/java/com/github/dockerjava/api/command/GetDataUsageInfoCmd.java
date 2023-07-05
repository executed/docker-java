package com.github.dockerjava.api.command;

/**
 * Inspect the details of a data usage.
 *
 * @author Dan Serbyn
 *
 */
public interface GetDataUsageInfoCmd extends SyncDockerCmd<GetDataUsageInfoResponse> {

    interface Exec extends DockerCmdSyncExec<GetDataUsageInfoCmd, GetDataUsageInfoResponse> {
    }
}
