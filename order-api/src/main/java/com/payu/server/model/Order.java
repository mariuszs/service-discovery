package com.payu.server.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Order implements Serializable {
    private String what;
    private String amount;

    public Order() {
    }

    public Order(String amount, String what) {
        this.amount = amount;
        this.what = what;
    }

    public String getWhat() {
        return what;
    }

    public String getAmount() {
        return amount;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "what='" + what + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
