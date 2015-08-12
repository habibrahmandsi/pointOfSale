package com.dsoft.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by habib on 7/25/15.
 */
public class DataModelForTypeAhead {

    private Boolean  status;
    private String  error;
    private Product data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Product getData() {
        return data;
    }

    public void setData(Product data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataModelForTypeAhead{" +
                "status=" + status +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}
