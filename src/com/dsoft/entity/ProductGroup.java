package com.dsoft.entity;

import javax.persistence.*;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "product_group")
public class ProductGroup extends AbstractBaseEntity{

    @Column(name = "name")
    private String name;

    @Transient
    private Boolean saved;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    @Override
    public String toString() {
        return "ProductGroup{" +
                "name='" + name + '\'' +
                ", saved=" + saved +
                '}';
    }
}
