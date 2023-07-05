package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import com.github.dockerjava.api.model.datausage.UsageData;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Marcus Linke
 */
@EqualsAndHashCode
@ToString
public class InspectVolumeResponse extends DockerObject implements Serializable {

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

    /**
     * Populated in GetDataUsageInfoResponse ONLY (GetDataUsageInfoCmd)
     */
    @JsonProperty("UsageData")
    private UsageData usageData;

    /**
     * @see #name
     */
    public String getName() {
        return name;
    }

    /**
     * @see #labels
     */
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @see #mountpoint
     */
    public String getMountpoint() {
        return mountpoint;
    }

    /**
     * @see #options
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * @see #usageData
     */
    @CheckForNull
    public UsageData getUsageData() {
        return usageData;
    }
}
