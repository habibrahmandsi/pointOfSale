package com.dsoft.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "sales")
public class Sales {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

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

    @ManyToOne
    @JoinColumn(name="sale_return")
    private Sales salesReturn;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Sales getSalesReturn() {
        return salesReturn;
    }

    public void setSalesReturn(Sales salesReturn) {
        this.salesReturn = salesReturn;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "id=" + id +
                ", salesTokenNo='" + salesTokenNo + '\'' +
                ", salesDate=" + salesDate +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", due=" + due +
                ", user=" + user +
                ", salesReturn=" + salesReturn +
                '}';
    }
}
