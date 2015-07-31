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
    private long id;

    @ManyToOne
    @JoinColumn(name="sales_id")
    private Sales sales;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "purchase_rate")
    private Double purchaseRate;

    @Column(name = "sales_rate")
    private Double salesRate;

    @Column(name = "total_price")
    private Double totalPrice;


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
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
                '}';
    }
}
