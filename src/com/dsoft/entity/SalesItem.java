package com.dsoft.entity;

import javax.persistence.*;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "sales_item")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="sales_id")
    private Sales sales;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "purchase_rate")
    private Double purchaseRate;

    @Column(name = "sales_rate")
    private Double salesRate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "total_purchase_price")
    private Double totalPurchasePrice;

    @Column(name = "total_purchase_details")
    private String totalPurchaseDetails;


    @Column(name = "benefit")
    private Double benefit;

    @Column(name = "purchase_item_id")
    private Long purchaseItemId;

    @Transient
    private Double prevQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sales getSales() {
        return sales;
    }

    public void setSales(Sales sales) {
        this.sales = sales;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public void setTotalPurchasePrice(Double totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
    }

    public String getTotalPurchaseDetails() {
        return totalPurchaseDetails;
    }

    public void setTotalPurchaseDetails(String totalPurchaseDetails) {
        this.totalPurchaseDetails = totalPurchaseDetails;
    }

    public Double getBenefit() {
        return benefit;
    }

    public void setBenefit(Double benefit) {
        this.benefit = benefit;
    }

    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public Double getPrevQuantity() {
        return prevQuantity;
    }

    public void setPrevQuantity(Double prevQuantity) {
        this.prevQuantity = prevQuantity;
    }

    @Override
    public String toString() {
        return "SalesItem{" +
                "id=" + id +
                ", sales=" + sales +
                ", product=" + product +
                ", quantity=" + quantity +
                ", purchaseRate=" + purchaseRate +
                ", salesRate=" + salesRate +
                ", totalPrice=" + totalPrice +
                ", totalPurchasePrice=" + totalPurchasePrice +
                ", totalPurchaseDetails='" + totalPurchaseDetails + '\'' +
                ", benefit=" + benefit +
                ", purchaseItemId=" + purchaseItemId +
                ", prevQuantity=" + prevQuantity +
                '}';
    }
}
