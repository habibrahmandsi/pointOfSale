package com.dsoft.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

public class SearchBean {

    private String fromDateStr;
    private String toDateStr;
    private Date fromDate;
    private Date toDate;
    private Long userId;
    private List<User> userList;

    public String getFromDateStr() {
        return fromDateStr;
    }

    public void setFromDateStr(String fromDateStr) {
        this.fromDateStr = fromDateStr;
    }

    public String getToDateStr() {
        return toDateStr;
    }

    public void setToDateStr(String toDateStr) {
        this.toDateStr = toDateStr;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "fromDateStr='" + fromDateStr + '\'' +
                ", toDateStr='" + toDateStr + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", userId=" + userId +
                ", userList=" + userList +
                '}';
    }
}
