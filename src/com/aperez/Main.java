package com.aperez;

import com.aperez.items.Coin;
import com.aperez.items.Drink;
import com.aperez.vending.Stock;
import com.aperez.vending.VendingMachine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Stock<Coin> coinStock = new Stock<>();
        Stock<Drink> drinkStock = new Stock<>();
        if (0 < args.length) {
            String filename = args[0];
            File file = new File(filename);
            String readLine;
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                while ((readLine = br.readLine()) != null) {
                    String[] parameters = readLine.split(" ");
                    coinStock.put(Coin.QUARTER,Integer.parseInt(parameters[0]));
                    coinStock.put(Coin.DIME,Integer.parseInt(parameters[1]));
                    coinStock.put(Coin.NICKLE,Integer.parseInt(parameters[2]));
                    drinkStock.put(Drink.COKE,Integer.parseInt(parameters[3]));
                    drinkStock.put(Drink.WATER,Integer.parseInt(parameters[4]));
                    drinkStock.put(Drink.COFFEE,Integer.parseInt(parameters[5]));
                } // end while
            } // end try
            catch (IOException e) {
                System.err.println("Error happened: " + e);
            }
        } else {
            //Default initialization
            for (Coin coin : Coin.values()) {
                coinStock.put(coin, 7);
            }
            for (Drink drink : Drink.values()) {
                drinkStock.put(drink, 4);
            }
        }

        VendingMachine vendingMachine = new VendingMachine(coinStock, drinkStock);
        vendingMachine.start();
    }
}
