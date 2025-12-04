import java.net.*;
import java.io.*;

public class WarehouseServerThread extends Thread {

    private Socket warehouseSocket = null;
    private SharedWarehouseState sharedWarehouseState;
    private String warehouseServerThreadName;
    private double warehouseSharedVariable;


    public WarehouseServerThread(Socket warehouseSocket, String warehouseServerThreadName, SharedWarehouseState sharedState) {

        super(warehouseServerThreadName);
        this.warehouseSocket = warehouseSocket;
        this.sharedWarehouseState = sharedState;
        this.warehouseServerThreadName = warehouseServerThreadName;
    }

    public void run() {
        try {
            System.out.println(warehouseServerThreadName + " initialising.");
            PrintWriter out = new PrintWriter(warehouseSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(warehouseSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {

                
                try {
                    sharedWarehouseState.acquireLock();
                    outputLine = sharedWarehouseState.processInput(warehouseServerThreadName, inputLine);
                    out.println(outputLine);
                    sharedWarehouseState.releaseLock();
                }
                catch (InterruptedException e) {
                    System.err.println("Failed to get lock when reading:" + e);
                }
            }

            out.close();
            in.close();
            warehouseSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

