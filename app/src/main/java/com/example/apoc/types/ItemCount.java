package com.example.apoc.types;

import java.io.Serializable;

public class ItemCount implements Serializable {
    private String name;
    private int amount;
    public ItemCount(String name, int amount){
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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
    public void increase(){
        amount++;
    }
    public void decrease(){
        amount--;
    }
}
