package com.dsoft.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "settings")
public class Settings extends AbstractBaseEntity {

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_address")
    private String shopAddress;


    @Column(name = "stock_limit_alarm_qty")
    private Double stockLimitAlarmQty;

    @Column(name = "expire_date_alarm_day")
    private Integer expireDateAlarmDay;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public Double getStockLimitAlarmQty() {
        return stockLimitAlarmQty;
    }

    public void setStockLimitAlarmQty(Double stockLimitAlarmQty) {
        this.stockLimitAlarmQty = stockLimitAlarmQty;
    }

    public Integer getExpireDateAlarmDay() {
        return expireDateAlarmDay;
    }

    public void setExpireDateAlarmDay(Integer expireDateAlarmDay) {
        this.expireDateAlarmDay = expireDateAlarmDay;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                ", stockLimitAlarmQty=" + stockLimitAlarmQty +
                ", expireDateAlarmDay=" + expireDateAlarmDay +
                '}';
    }
}
