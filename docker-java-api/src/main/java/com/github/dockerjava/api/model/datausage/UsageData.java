package com.github.dockerjava.api.model.datausage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.InspectVolumeResponse;

/**
 * Usage details about the volume. This information is used by the `GET /system/df`
 * endpoint, and omitted in other endpoints.
 *
 * @author Dan Serbyn (dev.serbyn@gmail.com)
 * @see InspectVolumeResponse
 */
public class UsageData {

    /**
     * Amount of disk space used by the volume (in bytes). This information is only available
     * for volumes created with the "local" volume driver. For volumes created with other
     * volume drivers, this field comes as "-1", but returned as null ("not available").
     * @see InspectVolumeResponse#getDriver()
     */
    @JsonProperty("Size")
    private Long size;

    /**
     * The number of containers referencing this volume. This field comes as "-1", but returned
     * as null if the reference-count is not available.
     */
    @JsonProperty("RefCount")
    private Long refCount;


    /**
     * @see #size
     */
    public Long getSize() {
        return size;
    }

    /**
     * @see #refCount
     */
    public Long getRefCount() {
        return refCount;
    }
}
