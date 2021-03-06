package com.dsoft.entity;

import javax.persistence.*;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "purchase_item")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "purchase_rate")
    private Double purchaseRate;

    @Column(name = "sale_rate")
    private Double saleRate;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "rest_quantity")
    private Double restQuantity;

    @Transient
    private Double prevQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
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

    public Double getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(Double saleRate) {
        this.saleRate = saleRate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getRestQuantity() {
        return restQuantity;
    }

    public void setRestQuantity(Double restQuantity) {
        this.restQuantity = restQuantity;
    }

    public Double getPrevQuantity() {
        return prevQuantity;
    }

    public void setPrevQuantity(Double prevQuantity) {
        this.prevQuantity = prevQuantity;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
                "id=" + id +
                ", purchase=" + purchase +
                ", product=" + product +
                ", quantity=" + quantity +
                ", purchaseRate=" + purchaseRate +
                ", saleRate=" + saleRate +
                ", totalPrice=" + totalPrice +
                ", restQuantity=" + restQuantity +
                ", prevQuantity=" + prevQuantity +
                '}';
    }
}
