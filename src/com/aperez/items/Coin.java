package com.aperez.items;

/**
 * Represents the supported coins
 */
public enum Coin {
   NICKLE(5), DIME(10), QUARTER(25);

    private int value;

    Coin(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
