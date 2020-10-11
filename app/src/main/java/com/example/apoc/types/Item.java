package com.example.apoc.types;

import com.example.apoc.DB.DBItem;
import com.example.apoc.Enums.Fears;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * class represents item
 */
public class Item implements DBItem, Serializable {
    private String name;
    private Map<Fears, Double> amount;

    /**
     * constructor
     * @param name item's name
     * @param amount how many items we got
     */
    public Item(String name, Map<Fears, Double> amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * empty item constructor
     * @param name items name
     */
    public Item(String name) {
        this.name = name;
        this.amount = new HashMap<>();
        amount.put(Fears.Pandemic, 0.0);
        amount.put(Fears.Zombies, 0.0);
        amount.put(Fears.Hurricane, 0.0);
        amount.put(Fears.War, 0.0);
        amount.put(Fears.Flood, 0.0);
        amount.put(Fears.Radioactive, 0.0);
    }

    /**
     * parse string to item constructor - separated by comas
     * @param line values
     * @param titles order of fears
     */
    public Item(String line, String titles) {
        String[] info = line.split(",");
        String[] title = titles.split(",");

        name = info[0];

        this.amount = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            switch (title[i]) {
                case ("Pandemic"): {
                    amount.put(Fears.Pandemic, Double.parseDouble(info[i]));
                    break;
                }
                case ("War"): {
                    amount.put(Fears.War, Double.parseDouble(info[i]));
                    break;
                }
                case ("Zombies"): {
                    amount.put(Fears.Zombies, Double.parseDouble(info[i]));
                    break;
                }
                case ("Radioactive"): {
                    amount.put(Fears.Radioactive, Double.parseDouble(info[i]));
                    break;
                }
                case ("Flood"): {
                    amount.put(Fears.Flood, Double.parseDouble(info[i]));
                    break;
                }
                case ("Hurricane"): {
                    amount.put(Fears.Hurricane, Double.parseDouble(info[i]));
                    break;
                }
            }
        }
    }

    /**
     * sets items amount
     * @param fear fear to change
     * @param amount new amount
     */
    public void setAmount(Fears fear, double amount) {
        this.amount.put(fear, amount);
    }

    /**
     * gets the item name
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get all item amounts
     * @return
     */
    public Map<Fears, Double> getAmount() {
        return amount;
    }

    /**
     * get item's amount by fear
     * @param fear the specific fear
     * @return the fears amount
     */
    public Double getAmount(Fears fear) {
        return amount.get(fear);
    }

    /**
     * gets item's id
     * @return
     */
    @Override
    public String getId() {
        return name;
    }

}
