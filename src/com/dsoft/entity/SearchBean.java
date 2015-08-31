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
    private Long companyId;
    private List<User> userList;
    private List totalSaleList;
    private List totalIncomeList;
    private int opt;
    private List totalPurchaseList;
    private List dateWiseGroupByList;
    private List companyList;
    private Double tpRate;
    private Double mrpRate;
    private Double tpRateReturn;
    private Double mrpRateReturn;
    private Long productId;
    private Double tpRateTotal;
    private Double mrpRateTotal;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List getTotalSaleList() {
        return totalSaleList;
    }

    public void setTotalSaleList(List totalSaleList) {
        this.totalSaleList = totalSaleList;
    }

    public List getTotalIncomeList() {
        return totalIncomeList;
    }

    public void setTotalIncomeList(List totalIncomeList) {
        this.totalIncomeList = totalIncomeList;
    }

    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    public List getTotalPurchaseList() {
        return totalPurchaseList;
    }

    public void setTotalPurchaseList(List totalPurchaseList) {
        this.totalPurchaseList = totalPurchaseList;
    }

    public List getDateWiseGroupByList() {
        return dateWiseGroupByList;
    }

    public void setDateWiseGroupByList(List dateWiseGroupByList) {
        this.dateWiseGroupByList = dateWiseGroupByList;
    }

    public List getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List companyList) {
        this.companyList = companyList;
    }

    public Double getTpRate() {
        return tpRate;
    }

    public void setTpRate(Double tpRate) {
        this.tpRate = tpRate;
    }

    public Double getMrpRate() {
        return mrpRate;
    }

    public void setMrpRate(Double mrpRate) {
        this.mrpRate = mrpRate;
    }

    public Double getTpRateReturn() {
        return tpRateReturn;
    }

    public void setTpRateReturn(Double tpRateReturn) {
        this.tpRateReturn = tpRateReturn;
    }

    public Double getMrpRateReturn() {
        return mrpRateReturn;
    }

    public void setMrpRateReturn(Double mrpRateReturn) {
        this.mrpRateReturn = mrpRateReturn;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getTpRateTotal() {
        return tpRateTotal;
    }

    public void setTpRateTotal(Double tpRateTotal) {
        this.tpRateTotal = tpRateTotal;
    }

    public Double getMrpRateTotal() {
        return mrpRateTotal;
    }

    public void setMrpRateTotal(Double mrpRateTotal) {
        this.mrpRateTotal = mrpRateTotal;
    }

    @Override
    public String toString() {
        return "SearchBean{" +
                "fromDateStr='" + fromDateStr + '\'' +
                ", toDateStr='" + toDateStr + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", userId=" + userId +
                ", companyId=" + companyId +
                ", userList=" + userList +
                ", totalSaleList=" + totalSaleList +
                ", totalIncomeList=" + totalIncomeList +
                ", opt=" + opt +
                ", totalPurchaseList=" + totalPurchaseList +
                ", dateWiseGroupByList=" + dateWiseGroupByList +
                ", companyList=" + companyList +
                ", tpRate=" + tpRate +
                ", mrpRate=" + mrpRate +
                ", tpRateReturn=" + tpRateReturn +
                ", mrpRateReturn=" + mrpRateReturn +
                ", productId=" + productId +
                ", tpRateTotal=" + tpRateTotal +
                ", mrpRateTotal=" + mrpRateTotal +
                '}';
    }
}
