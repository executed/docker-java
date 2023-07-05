package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.datausage.BuildCache;
import com.github.dockerjava.api.model.DockerObject;
import com.github.dockerjava.api.model.datausage.ContainerSummary;
import com.github.dockerjava.api.model.datausage.ImageSummary;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Get data usage information.
 * @author Dan Serbyn (dev.serbyn@gmail.com)
 */
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class GetDataUsageInfoResponse extends DockerObject implements Serializable {

    @JsonProperty("LayersSize")
    private Long layersSize;

    @JsonProperty("BuildCache")
    private BuildCache buildCache;

    @JsonProperty("Images")
    private List<ImageSummary> images;

    @JsonProperty("Containers")
    private List<ContainerSummary> containers;

    @JsonProperty("Volumes")
    private List<InspectVolumeResponse> volumes;

}
