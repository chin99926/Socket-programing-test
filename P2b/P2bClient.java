/* === UDPClient for P2b === */
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.Scanner;

class P2bClient {
   public static void main(String args[]) throws Exception {
      DatagramSocket clientSocket = new DatagramSocket();
      InetAddress serverIP = InetAddress.getByName("127.0.0.1");

      System.out.print("Set sleep time in Server loop to (msec) : ");
      Scanner scanner = new Scanner(System.in);
      int sleepSec = scanner.nextInt();
      byte[] sendSlp = ByteBuffer.allocate(4).putInt(sleepSec).array();
      DatagramPacket sleepPkt = new DatagramPacket(sendSlp, 4, serverIP, 9090);
      clientSocket.send(sleepPkt);

      System.out.println("Starts throwing packets to server...");
      for(int pktNum = 1; pktNum <= 10000; pktNum++) {
         byte[] sendByte = ByteBuffer.allocate(4).putInt(pktNum).array();
         DatagramPacket sendPkt =
            new DatagramPacket(sendByte, 4, serverIP, 9090);
         clientSocket.send(sendPkt);
      }
      Thread.sleep(10);
      byte[] endByte = ByteBuffer.allocate(4).putInt(10001).array();
      DatagramPacket endPkt = new DatagramPacket(endByte, 4, serverIP, 9090);
      clientSocket.send(endPkt);
      System.out.println("Finished sending packets.");

      DatagramPacket rcvdPkt = new DatagramPacket(new byte[4], 4);
      clientSocket.receive(rcvdPkt);
      int rcvdNum = ByteBuffer.wrap(rcvdPkt.getData()).getInt();
      clientSocket.receive(rcvdPkt);
      int lostNum = ByteBuffer.wrap(rcvdPkt.getData()).getInt();
      clientSocket.receive(rcvdPkt);
      int unordNum = ByteBuffer.wrap(rcvdPkt.getData()).getInt();
      System.out.println("During this UDP communication...");
      System.out.println("Received : " + rcvdNum + " packets.");
      System.out.println("Lost     : " + lostNum + " packets.");
      System.out.println("Unordered: " + unordNum + " packets." + '\n');

      clientSocket.close();
   }
}
