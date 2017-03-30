/* === UDPServer for P2b === */
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

class P2bServer {
   public static void main(String args[]) throws Exception {
      DatagramSocket serverSocket = new DatagramSocket(9090);
      System.out.println("Server Ready...");
      int loopNum = 0;

      while(true) {
         int rcvdNum = 0, unordNum = 0, prvNum = 0;
         DatagramPacket rcvdPkt = new DatagramPacket(new byte[4], 4);
         serverSocket.receive(rcvdPkt);
         int sleepSec = ByteBuffer.wrap(rcvdPkt.getData()).getInt();

         serverSocket.receive(rcvdPkt);
         int pktNum = ByteBuffer.wrap(rcvdPkt.getData()).getInt();
         while(pktNum <= 10000) {
            rcvdNum++;
            if(pktNum < prvNum) unordNum++;
            prvNum = pktNum;
            Thread.sleep(sleepSec);

            serverSocket.receive(rcvdPkt);
            pktNum = ByteBuffer.wrap(rcvdPkt.getData()).getInt();
         }

         InetAddress ip = rcvdPkt.getAddress();
         int lostNum = 10000 - rcvdNum, port = rcvdPkt.getPort();

         byte[] rcvdByte = ByteBuffer.allocate(4).putInt(rcvdNum).array();
         DatagramPacket sendPkt = new DatagramPacket(rcvdByte, 4, ip, port);
         serverSocket.send(sendPkt);
         byte[] lostByte = ByteBuffer.allocate(4).putInt(lostNum).array();
         sendPkt = new DatagramPacket(lostByte, 4, ip, port);
         serverSocket.send(sendPkt);
         byte[] unordByte = ByteBuffer.allocate(4).putInt(unordNum).array();
         sendPkt = new DatagramPacket(unordByte, 4, ip, port);
         serverSocket.send(sendPkt);

         System.out.println("UDP experiment # " + loopNum++ + "...");
         System.out.println("Received " + rcvdNum + " / 10000 packets." + '\n');
      }
   }
}
