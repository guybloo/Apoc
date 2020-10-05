package com.example.apoc.types;

import java.io.Serializable;

public class ItemCount implements Serializable {
    private String name;
    private double amount;
    private double max;

    public ItemCount(String name, double amount){
        this.name = name;
        this.amount = amount;
    }

    public ItemCount(String name){
        this.name = name;
        this.amount = 0;
    }

    public String getName() {
        return name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
    public void increase(){
        amount++;
    }
    public void decrease(){
        if(amount > 0) {
            amount--;
        }
    }
    public void sum(double amount){
        this.amount += amount;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getPrecentage(){
        return amount / max;
    }
}
