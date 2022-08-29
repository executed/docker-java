package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 *
 * @author Dan Serbyn
 */
@EqualsAndHashCode
@ToString
public class GetDataUsageInfoResponse extends DockerObject {

    @JsonProperty("LayersSize")
    private Long layersSize;

    @JsonProperty("Volumes")
    List<InspectVolumeResponse> volumes;

    public Long getLayersSize() {
        return layersSize;
    }

    public List<InspectVolumeResponse> getVolumes() {
        return volumes;
    }
}
