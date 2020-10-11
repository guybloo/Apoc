package com.example.apoc.types;

import java.io.Serializable;

/**
 * object which counts items
 */
public class ItemCount implements Serializable {
    private String name;
    private double amount;
    private double max;

    /**
     * copy constructor
     * @param name items name
     * @param amount the amount
     */
    public ItemCount(String name, double amount){
        this.name = name;
        this.amount = amount;
    }

    /**
     * constructor - new item
     * @param name items name
     */
    public ItemCount(String name){
        this.name = name;
        this.amount = 0;
    }

    /**
     * gets the items name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * sets the items amount
     * @param amount the new amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * gets the item amount
     * @return
     */
    public double getAmount() {
        return amount;
    }

    /**
     * increase the items amount
     */
    public void increase(){
        amount++;
    }

    /**
     * decrease the item amount
     */
    public void decrease(){
        if(amount > 0) {
            amount--;
        }
    }

    /**
     * sums the amount and max count
     * @param amount
     * @param max
     */
    public void sum(double amount, double max){
        this.amount += amount;
        this.max += max;
    }

    /**
     * set the maximum amount
     * @param max the value
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * gets the item amount precent from max
     * @return
     */
    public double getPrecentage(){
        return amount / max;
    }

    /**
     * gets the max value
     * @return
     */
    public double getMax() {
        return max;
    }
}
