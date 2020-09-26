package com.example.apoc.types;

import com.example.apoc.DB.DBItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Item implements DBItem, Serializable {
    private String name;
    private Map<Fears, Double> amount;

    public Item(String name, Map<Fears, Double> amount) {
        this.name = name;
        this.amount = amount;
    }
    public Item(String name) {
        this.name = name;
        this.amount = new HashMap<>();
        amount.put(Fears.Pandemic,0.0);
        amount.put(Fears.Zombies,0.0);
        amount.put(Fears.Hurricane,0.0);
        amount.put(Fears.War,0.0);
        amount.put(Fears.Flood,0.0);
        amount.put(Fears.Radioactive,0.0);
    }

    public void setAmount(Fears fear, double amount) {
        this.amount.put(fear, amount);
    }

    public String getName() {
        return name;
    }

    public Map<Fears, Double> getAmount() {
        return amount;
    }
    public Double getAmount(Fears fear) {
        return amount.get(fear);
    }

    @Override
    public String getId() {
        return name;
    }
}
