package machine;

import java.util.Scanner;

class Machine {
    int water, milk, beans, cups, money;
    Status currentStatus;

    public Machine() {
        this.water = 400;
        this.milk = 540;
        this.beans = 120;
        this.cups = 9;
        this.money = 550;
        this.currentStatus = Status.choosingAction;
    }
    public void printStatus() {
        System.out.println("The coffee machine has:");
        System.out.println(this.water + " of water");
        System.out.println(this.milk + " of milk");
        System.out.println(this.beans + " of coffee beans");
        System.out.println(this.cups + " of disposable cups");
        System.out.println(this.money + " of money");
    }

    enum Status {
        choosingAction,
        choosingCoffee,
        fillingBeans,
        fillingCups,
        fillingMilk,
        fillingWater
    }
    private void cookingCoffee(int buyOption) {
        switch (buyOption) {
            case 1: // espresso
                water -= 250;
                beans -= 16;
                cups--;
                money += 4;
                break;
            case 2: // latte
                water -= 350;
                milk -= 75;
                beans -= 20;
                cups--;
                money += 7;
                break;
            case 3: // cappuccino
                water -= 200;
                milk -= 100;
                beans -= 12;
                cups--;
                money += 6;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + buyOption);
        }
    }
    private void fillingWater(int value) {
        water += value;
    }
    private void fillingMilk(int value) {
        milk += value;
    }
    private void fillingBeans(int value) {
        beans += value;
    }
    private void fillingCups(int value) {
        cups += value;
    }
    private boolean checkStatus (int buyOption) {
        if (cups <= 0) {
            System.out.println();
        }

        int requestedWater;
        int requestedMilk;
        int requestedBeans;

        switch (buyOption) {
            case 1:
                requestedWater = 250;
                requestedBeans = 16;
                requestedMilk = 0;
                break;
            case 2:
                requestedWater = 350;
                requestedMilk = 75;
                requestedBeans = 20;
                break;
            case 3:
                requestedWater = 200;
                requestedMilk = 100;
                requestedBeans = 12;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + buyOption);
        }

        if (water - requestedWater < 0) {
            System.out.println("Sorry, not enough water!");
            return false;
        } else if (milk - requestedMilk < 0) {
            System.out.println("Sorry, not enough milk!");
            return false;
        } else if (beans - requestedBeans < 0) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        } else {
            System.out.println("I have enough resources, making you a coffee!");
            return true;
        }
    }
    private void giveMoney() {
        System.out.println("I gave you $" + money);
        money = 0;
    }
    public void printAction() {
        switch (currentStatus) {
            case choosingAction:
                System.out.println("Write action (buy, fill, take, remaining, exit): ");
                break;
            case choosingCoffee:
                System.out.println("What do you want to buy? 1 - espresso, " +
                        "2 - latte, 3 - cappuccino," +
                        " back - to main menu:");
                break;
            case fillingWater:
                System.out.println("Write how many ml of water do you want to add: ");
                break;
            case fillingMilk:
                System.out.println("Write how many ml of milk do you want to add:");
                break;
            case fillingBeans:
                System.out.println("Write how many grams of coffee beans do you want to add:");
                break;
            case fillingCups:
                System.out.println("Write how many disposable cups of coffee do you want to add:");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + currentStatus);
        }
    }
    public void request(String cmd) {
        switch (currentStatus) {
            case choosingAction:
                //System.out.println("Write action (buy, fill, take, remaining, exit): ");
                switch (cmd) {
                    case "buy":
                        currentStatus = Status.choosingCoffee;
                        break;
                    case "fill":
                        currentStatus = Status.fillingWater;
                        break;
                    case "take":
                        giveMoney();
                        break;
                    case "remaining":
                        printStatus();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + cmd);
                }
                break;
            case choosingCoffee:
                if (cmd.contains("back")) {
                    currentStatus = Status.choosingAction;
                    break;
                }
                else {
                    int buyOption = Integer.parseInt(cmd);
                    if (checkStatus(buyOption)) {
                        cookingCoffee(buyOption);
                    }
                }
                currentStatus = Status.choosingAction;
                break;
            case fillingBeans:
                fillingBeans(Integer.parseInt(cmd));
                currentStatus = Status.fillingCups;
                break;
            case fillingCups:
                fillingCups(Integer.parseInt(cmd));
                currentStatus = Status.choosingAction;
                break;
            case fillingMilk:
                fillingMilk(Integer.parseInt(cmd));
                currentStatus = Status.fillingBeans;
                break;
            case fillingWater:
                fillingWater(Integer.parseInt(cmd));
                currentStatus = Status.fillingMilk;
                break;
        }
    }

}

public class CoffeeMachine {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String action;
        Machine machine = new Machine();

        while (true) {
            machine.printAction();
            action = scanner.next();
            if (action.contains("exit")) {
                break;
            }
            machine.request(action);
        }
    }
}
