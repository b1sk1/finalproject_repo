package com.SamsungProject.SamsungProject;

import java.io.Serializable;

public class FilterHeader implements Serializable {
    private String title;
    private String activeFilter;

    public FilterHeader(String title, String activeFilter) {
        this.title = title;
        this.activeFilter = activeFilter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActiveFilter() {
        return activeFilter;
    }

    public void setActiveFilter(String activeFilter) {
        this.activeFilter = activeFilter;
    }
}
