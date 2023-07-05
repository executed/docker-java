package com.github.dockerjava.api.model.datausage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.GetDataUsageInfoResponse;
import com.github.dockerjava.api.model.DockerObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * ImageSummary contains information about the data usage of an image.
 * Used for "Get Data Usage Info" request (/system/df)
 *
 * @author Dan Serbyn (dev.serbyn@gmail.com)
 * @see GetDataUsageInfoResponse
 */
@ToString
@EqualsAndHashCode(callSuper = true)
public class ImageSummary extends DockerObject implements Serializable {

    /**
     * ID is the content-addressable ID of an image. This identifier is a content-addressable
     * digest calculated from the image's configuration (which includes the digests of layers
     * used by the image). Note that this digest differs from the `RepoDigests` below, which
     * holds digests of image manifests that reference the image.
     * e.g. sha256:ec3f0931a6e6b6855d76b2d7b0be30e81860baccd891b2e243280bf1cd8ad710
     */
    @JsonProperty("Id")
    private String id;
    /**
     * ID of the parent image. Depending on how the image was created, this field may be
     * empty (returned as null) and is only set for images that were built/created locally.
     * This field is null if the image was pulled from an image registry.
     */
    @JsonProperty("ParentId")
    private String parentId;
    /**
     * List of image names/tags in the local image cache that reference this image. Multiple
     * image tags can refer to the same image and this list may be empty if no tags
     * reference the image, in which case the image is "untagged", in which case it can still
     * be referenced by its ID.
     * e.g. ["example:1.0", "example:latest", "internal.registry.example.com:5000/example:1.0"]
     */
    @JsonProperty("RepoTags")
    private List<String> repoTags;
    /**
     * List of content-addressable digests of locally available image manifests that the image
     * is referenced from. Multiple manifests can refer to the same image. These digests are
     * usually only available if the image was either pulled from a registry, or if the image
     * was pushed to a registry, which is when the manifest is generated and its digest calculated.
     * e.g. ["example@sha256:afcc7...a0ccb", "internal.registry.example.com:5000/example@sha256:b699...f5578"]
     */
    @JsonProperty("RepoDigests")
    private List<String> repoDigests;
    /**
     * Date and time at which the image was created
     */
    @JsonProperty("Created")
    private Instant created;
    /**
     * Total size (bytes) of the image including all layers it is composed of.
     */
    @JsonProperty("Size")
    private Long size;
    /**
     * Total size of image layers that are shared between this image and other images.
     * This size is not calculated by default. Comes as "-1", but returned as null if the value
     * has not been set.
     */
    @JsonProperty("SharedSize")
    private Long sharedSize;
    /**
     * User-defined key/value metadata.
     * e.g. [["com.ex.label-1": "some-value"], ["com.ex.label-2": "other-value"]]
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;
    /**
     * Number of containers using this image. Includes both stopped and running containers.
     * This size is not calculated by default, and depends on which API endpoint is used.
     * Comes as "-1", but returned as null if the value has not been set / calculated.
     */
    @JsonProperty("Containers")
    private Integer containers;

    /**
     * @see #id
     */
    public String getId() {
        return this.id;
    }

    /**
     * @see #parentId
     */
    @CheckForNull
    public String getParentId() {
        return (this.parentId == null || this.parentId.isEmpty()) ? null : this.parentId;
    }

    /**
     * @see #repoTags
     */
    public List<String> getRepoTags() {
        return this.repoTags;
    }

    /**
     * @see #repoDigests
     */
    public List<String> getRepoDigests() {
        return this.repoDigests;
    }

    /**
     * @see #created
     */
    public Instant getCreated() {
        return this.created;
    }

    /**
     * @see #size
     */
    public Long getSize() {
        return this.size;
    }

    /**
     * @see #sharedSize
     */
    @CheckForNull
    public Long getSharedSize() {
        return (this.sharedSize == null || this.sharedSize == -1L) ? null : this.sharedSize;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #containers
     */
    @CheckForNull
    public Integer getContainers() {
        return (this.containers == null || this.containers == -1) ? null : this.containers;
    }
}
