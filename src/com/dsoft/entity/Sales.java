package com.dsoft.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity

@Table(name = "sales")
public class Sales extends AbstractBaseEntity{

    public Sales(){
        this.salesReturn = false;
        this.unposted = true;
        this.totalAmount = 0d;
        this.discount = 0d;
    }

    @Column(name = "sales_token_no",unique=true)
    private String salesTokenNo;

    @Column(name = "sales_date")
    private Date salesDate;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "due")
    private Double due;

    @ManyToOne
    @JoinColumn(name="sale_by_id")
    private User user;


    @Column(name="sale_return")
    private Boolean salesReturn;

    @Column(name="unposted")
    private Boolean unposted;

    @Transient
    private List<SalesItem> salesItemList;

    public String getSalesTokenNo() {
        return salesTokenNo;
    }

    public void setSalesTokenNo(String salesTokenNo) {
        this.salesTokenNo = salesTokenNo;
    }

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDue() {
        return due;
    }

    public void setDue(Double due) {
        this.due = due;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(Boolean salesReturn) {
        this.salesReturn = salesReturn;
    }

    public List<SalesItem> getSalesItemList() {
        return salesItemList;
    }

    public void setSalesItemList(List<SalesItem> salesItemList) {
        this.salesItemList = salesItemList;
    }

    public Boolean getUnposted() {
        return unposted;
    }

    public void setUnposted(Boolean unposted) {
        this.unposted = unposted;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "salesTokenNo='" + salesTokenNo + '\'' +
                ", salesDate=" + salesDate +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", due=" + due +
                ", user=" + user +
                ", salesReturn=" + salesReturn +
                ", unposted=" + unposted +
                ", salesItemList=" + salesItemList +
                '}';
    }
}
