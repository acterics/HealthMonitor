package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 20.05.17.
 */

public class Complaint {

    @SerializedName("description")
    private String description;

    @SerializedName("tags")
    private List<Tag> tags;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
