package com.aperez.items;

import java.text.DecimalFormat;

/**
 * Represents the supported drinks
 */
public enum Drink {
    COKE("Coke", 50), WATER("Water", 75), COFFEE("Coffee", 100);

    private String name;
    private int price;

    Drink(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "$" + df.format((float) price / 100);
    }
}
