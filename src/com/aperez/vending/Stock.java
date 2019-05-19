package com.aperez.vending;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an inventory, used for maintaining inventory inside a Vending Machine
 * @param <T>
 */
public class Stock<T> {
    private Map<T, Integer> inventory = new HashMap<>();

    public int getQuantity(T item){
        Integer value = inventory.get(item);
        return value == null? 0 : value ;
    }

    public void deduct(T item , int quantity) {
        if (hasItem(item)) {
            int count = inventory.get(item);
            inventory.put(item, count - quantity);
        }
    }

    public boolean hasItem(T item){
        return getQuantity(item) > 0;
    }

    public void put(T item, int quantity) {
        inventory.put(item, quantity);
    }
}
