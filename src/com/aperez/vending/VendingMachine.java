package com.aperez.vending;

import com.aperez.exception.InsufficientDrinkStock;
import com.aperez.exception.InsufficientInsertedMoney;
import com.aperez.exception.NotEnoughChangeException;
import com.aperez.items.Coin;
import com.aperez.items.Drink;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Implementation of the vending machine
 */
public class VendingMachine {
    private Stock<Coin> coinStock;
    private Stock<Drink> drinkStock;
    private int balance = 0;

    public VendingMachine(Stock<Coin> coinStock, Stock<Drink> drinkStock) {
        this.coinStock = coinStock;
        this.drinkStock = drinkStock;

        calculateBalance();
    }

    /**
     * Shows the user the menu to operate the vending machine
     */
    private void showVendingMenu() {
        System.out.println("Select your drink: ");
        for (int i = 1; i <= Drink.values().length; i++) {
            Drink drink = Drink.values()[i - 1];
            System.out.println(i + ") " + drink.getName() + " " + drink.getFormattedPrice() + " (stock: " + drinkStock.getQuantity(drink) + ")");
        }
        System.out.println((Drink.values().length + 1) + ") QUIT");
    }

    /**
     * Calculates the change to be given to the user, based on current coin stock
     * @param totalMoney amount inserted by the user
     * @param productCost cost of the selected produc
     * @throws NotEnoughChangeException
     */
    private void getChangeForInsertedMoney(int totalMoney, int productCost) throws NotEnoughChangeException {
        int balance = totalMoney - productCost;
        int quartersChange = 0;
        int dimesChange = 0;
        int nickelsChange = 0;
        while (balance > 0) {
            if (balance >= Coin.QUARTER.getValue() && coinStock.getQuantity(Coin.QUARTER) > 0) {
                int needed = balance / Coin.QUARTER.getValue();
                if (needed <= coinStock.getQuantity(Coin.QUARTER)) {
                    quartersChange = needed;
                } else {
                    quartersChange = coinStock.getQuantity(Coin.QUARTER);
                }
                balance -= quartersChange * Coin.QUARTER.getValue();
            } else if (balance >= Coin.DIME.getValue() && coinStock.getQuantity(Coin.DIME) > 0) {
                int needed = balance / Coin.DIME.getValue();
                if (needed <= coinStock.getQuantity(Coin.DIME)) {
                    dimesChange = needed;
                } else {
                    dimesChange = coinStock.getQuantity(Coin.DIME);
                }
                balance -= dimesChange * Coin.DIME.getValue();
            } else if (balance >= Coin.NICKLE.getValue() && coinStock.getQuantity(Coin.NICKLE) > 0) {
                int needed = balance / Coin.NICKLE.getValue();
                if (needed <= coinStock.getQuantity(Coin.NICKLE)) {
                    nickelsChange = needed;
                } else {
                    nickelsChange = coinStock.getQuantity(Coin.NICKLE);
                }
                balance -= nickelsChange * Coin.NICKLE.getValue();
            } else {
                throw new NotEnoughChangeException("Not enough change!");
            }
            coinStock.deduct(Coin.QUARTER, quartersChange);
            coinStock.deduct(Coin.DIME, dimesChange);
            coinStock.deduct(Coin.NICKLE, nickelsChange);
        }
        if (quartersChange > 0 || dimesChange > 0 || nickelsChange > 0) {
            giveChange(quartersChange, dimesChange, nickelsChange);
            calculateBalance();
        }
    }

    /**
     * "Gives" the user the change base on the purchased product, the money inserted and the available coins on the machine
     * @param quartersChange quarters quantity
     * @param dimesChange dimes quantity
     * @param nickelsChange nickels quantity
     */
    private void giveChange(int quartersChange, int dimesChange, int nickelsChange) {
        System.out.println("Changed dispensed:");
        if (quartersChange > 0) {
            System.out.printf("Quarters: %d", quartersChange);
            System.out.println();
        }
        if (dimesChange > 0) {
            System.out.printf("Dimes: %d", dimesChange);
            System.out.println();
        }
        if (nickelsChange > 0) {
            System.out.printf("Nickels: %d", nickelsChange);
            System.out.println();
        }
        System.out.println("---------------------");
        int totalChange = quartersChange * Coin.QUARTER.getValue() + dimesChange * Coin.DIME.getValue() + nickelsChange * Coin.NICKLE.getValue();
        DecimalFormat df = new DecimalFormat("#.##");
        System.out.printf("Total : %s", df.format((float) totalChange / 100));
        System.out.println();
        System.out.println();
    }

    /**
     * Validates if there's enough stock before selling a drink
     * @param drink drink selected by the user
     * @throws InsufficientDrinkStock
     */
    private void verifyStock(Drink drink) throws InsufficientDrinkStock {
        if (drinkStock.getQuantity(drink) == 0) {
            throw new InsufficientDrinkStock("No stock available of " + drink.getName());
        }
    }

    /**
     * Starts the vending machine
     */
    public void start() {
        boolean running = true;
        Scanner command = new Scanner(System.in);
        while (running) {
            showVendingMenu();

            System.out.println("Enter your drink choice: ");
            int selectedDrink = command.nextInt();
            if (selectedDrink == Drink.values().length + 1) {
                running = false;
                continue;
            }
            Drink drink = Drink.values()[selectedDrink - 1];

            try {
                verifyStock(drink);
            } catch (InsufficientDrinkStock e) {
                System.out.println(e.getMessage());
                continue;
            }

            System.out.println("Enter the amount of money to insert: ");
            String money = command.next();
            money = money.replace(",", ".");
            int insertedMoney = (int) (Float.parseFloat(money) * 100);
            try {
                validateEnteredEnoughMoney(insertedMoney, drink.getPrice());
                validateRemainingChange(insertedMoney, drink.getPrice());
                getChangeForInsertedMoney(insertedMoney, drink.getPrice());
                drinkStock.deduct(drink, 1);
            } catch (NotEnoughChangeException e) {
                System.out.println(e.getMessage());
            } catch (InsufficientInsertedMoney e) {
                System.out.println(e.getMessage());
            }
        }
        command.close();
    }

    /**
     * Validates if the user entered enough money to pay for the drink
     * @param insertedMoney
     * @param price
     * @throws InsufficientInsertedMoney
     */
    private void validateEnteredEnoughMoney(int insertedMoney, int price) throws InsufficientInsertedMoney{
        if (insertedMoney < price) {
            throw new InsufficientInsertedMoney("Not enough money inserted!");
        }
    }

    /**
     * Validates if there's enough balance on the machine to give change to the user
     * @param money
     * @param insertedMoney
     * @throws NotEnoughChangeException
     */
    private void validateRemainingChange(int money, int insertedMoney) throws NotEnoughChangeException {
        int change = money - insertedMoney;
        if (change > balance) {
            throw new NotEnoughChangeException("Not enough change!");
        }
    }

    /**
     * Calculates the current balance of change in the machine
     */
    private void calculateBalance() {
        for (Coin coin : Coin.values()) {
            this.balance += coin.getValue() * coinStock.getQuantity(coin);
        }
    }
}
