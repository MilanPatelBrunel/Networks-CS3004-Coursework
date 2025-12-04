import java.io.*;
import java.net.*;

public class SupplierClient {
    public static void main(String[] args) throws IOException {

        

        Socket warehouseClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int warehousePort = 4545;
        String warehouseServerName = "localhost";
        String clientID = "Supplier";

        try {
            warehouseClientSocket = new Socket(warehouseServerName, warehousePort);
            out = new PrintWriter(warehouseClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(warehouseClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + warehousePort);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + clientID + " client and IO connections");

        

        while (true) {

            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println(clientID + " sending " + fromUser + " to WarehouseServer");
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println(clientID + " received " + fromServer + " from WarehouseServer");
        }

        
    }
}
