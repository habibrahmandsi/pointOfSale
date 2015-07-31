package com.dsoft.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: Md. Habibur Rahman on 25/07/15.
 */

@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @Column(name = "purchase_token_no",unique=true)
    private String purchaseTokenNo;

    @Column(name = "purchase_date")
    private Date purchaseDate;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "discount")
    private Double discount;

    @ManyToOne
    @JoinColumn(name="purchase_by_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="purchase_return")
    private Purchase purchaseReturn;


    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", purchaseTokenNo='" + purchaseTokenNo + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", totalAmount=" + totalAmount +
                ", discount=" + discount +
                ", user=" + user +
                ", purchaseReturn=" + purchaseReturn +
                '}';
    }
}
