import java.net.*;
import java.io.*;

public class WarehouseServer {
    public static void main(String[] args) throws IOException {

        ServerSocket warehouseServerSocket = null;
        boolean listening = true;
        String warehouseServerName = "WarehouseServer";
        int warehousePort = 4545;

        int initialApples = 1000;
        int initialOranges = 1000;

        
        SharedWarehouseState sharedWarehouseState = new SharedWarehouseState(initialApples, initialOranges);

       
        try {
            warehouseServerSocket = new ServerSocket(warehousePort);
        } catch (IOException e) {
            System.err.println("Could not start " + warehouseServerName + " on specified port.");
            System.exit(-1);
        }

        System.out.println(warehouseServerName + " started with " + initialApples + " apples and " + initialOranges + " oranges");

        
        while (listening) {

            new WarehouseServerThread(warehouseServerSocket.accept(), "CustomerA-Thread", sharedWarehouseState).start();
            System.out.println("Customer A thread started.");

            new WarehouseServerThread(warehouseServerSocket.accept(), "CustomerB-Thread", sharedWarehouseState).start();
            System.out.println("Customer B thread started.");

            new WarehouseServerThread(warehouseServerSocket.accept(), "Supplier-Thread", sharedWarehouseState).start();
            System.out.println("Supplier thread started.");
        }

        warehouseServerSocket.close();
    }
}
