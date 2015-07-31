package com.dsoft.entity;

import javax.persistence.*;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "product")
public class Product extends AbstractBaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "purchase_rate")
    private Double purchaseRate;

    @Column(name = "sale_rate")
    private Double saleRate;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name="product_group_id")
    private ProductGroup productGroup;

    @ManyToOne
    @JoinColumn(name="product_type_id")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name="unit_of_measure_id")
    private UnitOfMeasure unitOfMeasure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(UnitOfMeasure unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", purchaseRate=" + purchaseRate +
                ", saleRate=" + saleRate +
                ", company=" + company +
                ", productGroup=" + productGroup +
                ", productType=" + productType +
                ", unitOfMeasure=" + unitOfMeasure +
                '}';
    }
}
