package ru.crabgore.testapp.restModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestModel {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}