package com.github.dockerjava.api.model.datausage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.time.Instant;

/**
 * BuildCache contains information about a build cache record.
 * Used for "Get Data Usage Info" request (/system/df).
 *
 * @author Dan Serbyn (dev.serbyn@gmail.com)
 * @see GetDataUsageInfoResponse
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class BuildCache extends DockerObject implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Unique ID of the build cache record.
     * Example:"ndlpt0hhvkqcdfkputsk4cq9c"
     */
    @JsonProperty("ID")
    private String id;
    /**
     * ID of the parent build cache record.
     * Comes as blank string, but returned as null if not available.
     * Example:"hw53o5aio51xtltp5xjp8v7fx"
     */
    @JsonProperty("Parent")
    private String parent;
    /**
     * Cache record type.
     * @see UsageRecordType
     */
    @JsonProperty("Type")
    private UsageRecordType type;
    /**
     * Description of the build-step that produced the build cache.
     * Comes as blank string, but returned as null if not available.
     * Example: "mount / from exec /bin/sh -c echo 'Binary::apt::APT::Keep-Downloaded-Packages \"true\";' > /etc/apt/apt.conf.d/keep-cache".
     */
    @JsonProperty("Description")
    private String description;
    /**
     * Indicates if the build cache is in use.
     */
    @JsonProperty("InUse")
    private Boolean inUse;
    /**
     * Indicates if the build cache is shared.
     */
    @JsonProperty("Shared")
    private Boolean shared;
    /**
     * Amount of disk space used by the build cache (in bytes).
     */
    @JsonProperty("Size")
    private Long size;
    /**
     * Date and time at which the build cache was created.
     */
    @JsonProperty("CreatedAt")
    private Instant createdAt;
    /**
     * Date and time at which the build cache was last used.
     */
    @JsonProperty("LastUsedAt")
    private Instant lastUsedAt;
    /**
     * Usage count.
     */
    @JsonProperty("UsageCount")
    private Integer usageCount;

    /**
     * @see #id
     */
    public String getId() {
        return id;
    }

    /**
     * @see #parent
     */
    @CheckForNull
    public String getParent() {
        return (this.parent == null || this.parent.isEmpty()) ? null : this.parent;
    }

    /**
     * @see #type
     */
    @CheckForNull
    public UsageRecordType getType() {
        return type;
    }

    /**
     * @see #description
     */
    @CheckForNull
    public String getDescription() {
        return (this.description == null || this.description.isEmpty()) ? null : this.description;
    }

    /**
     * @see #inUse
     */
    @CheckForNull
    public Boolean getInUse() {
        return inUse;
    }

    /**
     * @see #shared
     */
    @CheckForNull
    public Boolean getShared() {
        return shared;
    }

    /**
     * @see #size
     */
    @CheckForNull
    public Long getSize() {
        return size;
    }

    /**
     * @see #createdAt
     */
    @CheckForNull
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * @see #lastUsedAt
     */
    @CheckForNull
    public Instant getLastUsedAt() {
        return lastUsedAt;
    }

    /**
     * @see #usageCount
     */
    @CheckForNull
    public Integer getUsageCount() {
        return usageCount;
    }
}
