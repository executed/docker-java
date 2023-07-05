package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum MountType {

    /**
     * A mount of a file or directory from the host into the container.
     */
    @JsonProperty("bind")
    BIND,

    @JsonProperty("volume")
    VOLUME,

    //@since 1.29
    @JsonProperty("tmpfs")
    TMPFS,

    /**
     * A named pipe from the host into the container.
     * @since 1.40
     */
    @JsonProperty("npipe")
    NPIPE

}
