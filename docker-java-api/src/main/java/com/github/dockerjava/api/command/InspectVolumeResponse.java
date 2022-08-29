package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 *
 * @author Marcus Linke
 */
@EqualsAndHashCode
@ToString
public class InspectVolumeResponse extends DockerObject {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("Mountpoint")
    private String mountpoint;

    @JsonProperty("Options")
    private Map<String, String> options;

    @JsonProperty("UsageData")
    private UsageData usageData;

    public String getName() {
        return name;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public String getDriver() {
        return driver;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public UsageData getUsageData() {
        return usageData;
    }

    @EqualsAndHashCode
    @ToString
    public static class UsageData extends DockerObject {

        @JsonProperty("Size")
        private Long size;

        @JsonProperty("RefCount")
        private Long refCount;

        public Long getSize() {
            return size;
        }

        public Long getRefCount() {
            return refCount;
        }
    }

}
