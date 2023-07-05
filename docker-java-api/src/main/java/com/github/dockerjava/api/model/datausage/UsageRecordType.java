package com.github.dockerjava.api.model.datausage;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Build cache record type.
 * @see BuildCache
 */
public enum UsageRecordType {

    @JsonProperty("internal")
    INTERNAL,

    @JsonProperty("frontend")
    FRONTEND,

    @JsonProperty("source.local")
    LOCAL_SOURCE,

    @JsonProperty("source.git.checkout")
    GIT_CHECKOUT,

    @JsonProperty("exec.cachemount")
    CACHE_MOUNT,

    @JsonProperty("regular")
    REGULAR
}
