// P2aClient.java
import java.io.*;
import java.net.Socket;

public class P2aClient {
   private Socket socket;
   private BufferedReader inFromServer;
   
   public void connectToServer() throws IOException {
      String serverAddress = "140.112.18.178";
      // serverAddress = "140.112.18.178";
      Socket socket = new Socket(serverAddress, 9090);
      this.socket = socket;
      inFromServer = new BufferedReader(
         new InputStreamReader(socket.getInputStream()));
      System.out.println("Connect to server at " + serverAddress + "..");
   }

   public void start() throws Exception {
      System.out.println(inFromServer.readLine());

      // TODO: finish it to communicate properly with the server..
      BufferedReader inFromUser =
         new BufferedReader(new InputStreamReader(System.in));
      DataOutputStream out2Server =
         new DataOutputStream(socket.getOutputStream());

      System.out.println(inFromServer.readLine());
      String name = inFromUser.readLine();
      out2Server.writeBytes(name + '\n');
      System.out.println(inFromServer.readLine());
      String id = inFromUser.readLine();
      out2Server.writeBytes(id + '\n');

      System.out.println(inFromServer.readLine());
      String check = inFromUser.readLine();
      out2Server.writeBytes(check + '\n');

      System.out.println(inFromServer.readLine());
   }

   /**
    * Runs the client application.
    */
   public static void main(String[] args) throws Exception {
      P2aClient client = new P2aClient();
      client.connectToServer();
      client.start();
      client.socket.close();
   }
}
