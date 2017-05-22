package com.acterics.healthmonitor.data.models.categories.complaint;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 20.05.17.
 */

public class Tag {
    @SerializedName("icon")
    private String icon;

    @SerializedName("title")
    private String title;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
