package com.dsoft.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "purchase")
public class Purchase {

    public Purchase(){
        this.purchaseReturn = false;
        this.unposted = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "purchase_token_no",unique=true)
    private String purchaseTokenNo;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "vat")
    private Double vat;

    @ManyToOne
    @JoinColumn(name="purchase_by_id")
    private User user;

    @Column(name = "purchase_return")
    private Boolean purchaseReturn;

    @Column(name = "unposted")
    private Boolean unposted;


    @Transient
    private List<PurchaseItem> purchaseItemList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurchaseTokenNo() {
        return purchaseTokenNo;
    }

    public void setPurchaseTokenNo(String purchaseTokenNo) {
        this.purchaseTokenNo = purchaseTokenNo;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
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

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(Boolean purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public List<PurchaseItem> getPurchaseItemList() {
        return purchaseItemList;
    }

    public void setPurchaseItemList(List<PurchaseItem> purchaseItemList) {
        this.purchaseItemList = purchaseItemList;
    }

    public Boolean getUnposted() {
        return unposted;
    }

    public void setUnposted(Boolean unposted) {
        this.unposted = unposted;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", purchaseTokenNo='" + purchaseTokenNo + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", vat=" + vat +
                ", user=" + user +
                ", purchaseReturn=" + purchaseReturn +
                ", unposted=" + unposted +
                ", purchaseItemList=" + purchaseItemList +
                '}';
    }
}
