package ru.crabgore.testapp.restModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cell {

    @SerializedName("sliderInterval")
    @Expose
    private Integer sliderInterval;
    @SerializedName("urls")
    @Expose
    private List<Url> urls = null;

    public Integer getSliderInterval() {
        return sliderInterval;
    }

    public void setSliderInterval(Integer sliderInterval) {
        this.sliderInterval = sliderInterval;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }
}