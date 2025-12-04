import java.net.*; 
import java.io.*;

public class SharedWarehouseState {
    
    private SharedWarehouseState sharedWarehouseObject;
    private String threadName;
    private int apples;
    private int oranges;
    private boolean accessing = false; 
    private int threadsWaiting = 0;    

    
    SharedWarehouseState(int initialApples, int initialOranges) {
        apples = initialApples;
        oranges = initialOranges;
    }

    
    public synchronized void acquireLock() throws InterruptedException {
        Thread me = Thread.currentThread(); 
        System.out.println(me.getName() + " is attempting to acquire a lock!");
        ++threadsWaiting;
        while (accessing) {
            System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
            wait(); 
        }
        --threadsWaiting;
        accessing = true;
        System.out.println(me.getName() + " got a lock!");
    }

    
    public synchronized void releaseLock() {
        accessing = false;
        notifyAll();
        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " released a lock!");
    }

   
    public synchronized String processInput(String threadName, String input) {
        System.out.println(threadName + " received " + input);
        String output = null;

        
        String[] parts = input.split(" ");
        String command = parts[0];

        
        if (command.equalsIgnoreCase("check_stock")) {
            output = "Stock: Apples=" + apples + ", Oranges=" + oranges;
            System.out.println(threadName + " checked stock: " + output);
        }
        else if (command.equalsIgnoreCase("add_apples")) {
            if (threadName.equals("Supplier-Thread")) {   
                if (parts.length > 1) {
                    int number = Integer.parseInt(parts[1]);
                    apples += number;
                    output = "Added " + number + " apples. Stock: Apples=" + apples + ", Oranges=" + oranges;
                    System.out.println(threadName + " added " + number + " apples. New stock: Apples=" + apples);
                } else {
                    output = "Error: add_apples requires a number";
                }
            } else {
                output = "Error: Only supplier can add apples";
                System.out.println(threadName + " tried to add apples but is not the supplier");
            }
        }
        else if (command.equalsIgnoreCase("add_oranges")) {
            if (threadName.equals("Supplier-Thread")) {   
                if (parts.length > 1) {
                    int number = Integer.parseInt(parts[1]);
                    oranges += number;
                    output = "Added " + number + " oranges. Stock: Apples=" + apples + ", Oranges=" + oranges;
                    System.out.println(threadName + " added " + number + " oranges. New stock: Oranges=" + oranges);
                } else {
                    output = "Error: add_oranges requires a number";
                }
            } else {
                output = "Error: Only supplier can add oranges";
                System.out.println(threadName + " tried to add oranges but is not the supplier");
            }
        }
        else if (command.equalsIgnoreCase("buy_apples")) {
            if (parts.length > 1) {
                int number = Integer.parseInt(parts[1]);
                if (apples >= number) {
                    apples -= number;
                    output = "Bought " + number + " apples. Stock: Apples=" + apples + ", Oranges=" + oranges;
                    System.out.println(threadName + " bought " + number + " apples. New stock: Apples=" + apples);
                } else {
                    output = "Error: Not enough apples in stock. Available: " + apples;
                    System.out.println(threadName + " tried to buy " + number + " apples but only " + apples + " available");
                }
            } else {
                output = "Error: buy_apples requires a number";
            }
        }
        else if (command.equalsIgnoreCase("buy_oranges")) {
            if (parts.length > 1) {
                int number = Integer.parseInt(parts[1]);
                if (oranges >= number) {
                    oranges -= number;
                    output = "Bought " + number + " oranges. Stock: Apples=" + apples + ", Oranges=" + oranges;
                    System.out.println(threadName + " bought " + number + " oranges. New stock: Oranges=" + oranges);
                } else {
                    output = "Error: Not enough oranges in stock. Available: " + oranges;
                    System.out.println(threadName + " tried to buy " + number + " oranges but only " + oranges + " available");
                }
            } else {
                output = "Error: buy_oranges requires a number";
            }
        }
        else {
            output = threadName + " received incorrect request - valid commands: check_stock, add_apples [n], add_oranges [n], buy_apples [n], buy_oranges [n]";
        }

        System.out.println(output);
        return output;
    }
}


