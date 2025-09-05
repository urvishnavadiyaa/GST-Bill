package com.exampl.GST.Bill.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    int custId;

    private String customer_name;
    private String mobile_number;
    private String product_name;
    private int product_count;
    private int product_amount;
    private double total_amount;
    private LocalDate localDate;

    public Customer(int custId, String customer_name, String mobile_number,
                    String product_name, Integer product_count, int product_amount,
                    double total_amount, LocalDate localDate) {
        this.custId = custId;
        this.customer_name = customer_name;
        this.mobile_number = mobile_number;
        this.product_name = product_name;
        this.product_count = product_count;
        this.product_amount = product_amount;
        this.total_amount = total_amount;
        this.localDate = localDate;
    }

    public Customer() {
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_count() {
        return product_count;
    }

    public void setProduct_count(int product_count) {
        this.product_count = product_count;
    }

    public int getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
