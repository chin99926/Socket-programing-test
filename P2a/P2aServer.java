// P2aServer.java
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class P2aServer {
	public static void main (String args[]) throws IOException {
	 	System.out.println("The Grading server for HW2 is running..");
      int clientNumber = 0;
      ServerSocket listener = new ServerSocket(9090);
      RecordSystem.fw = new FileWriter("ta_record.csv", true);   
   	
      while (true) {
         try {
            /**
             * Runs RecordSystem for a new connected client on a new thread.
             * the method start() will implicitly create a new thread and call run() on it.
             */
            RecordSystem recSys = new RecordSystem(listener.accept(), clientNumber++);
            recSys.start();
         } catch (IOException e) {
            e.printStackTrace();
         }         
     	}
   }

   private static class RecordSystem extends Thread {
      private Socket socket;
      private int clientNumber;
      private String student_name;
      private String student_id;
      BufferedReader in;
      PrintWriter out;
      public static FileWriter fw;
      
      // constructor of class RecordSystem
      public RecordSystem(Socket socket, int clientNumber) {
         this.socket = socket;
         this.clientNumber = clientNumber;
         log("New connection # " + clientNumber + " at " + socket);       
      }

      // implicitly called by method start()
      public void run() {
         try {
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);   
	         askInformation();
            checkInformation();
            recordInformation();
	      } catch (IllegalStateException e) {
            log("Illegal state detected.");
         } catch (Exception e) {
            log("Error handling client# " + clientNumber + ": " + e);
            e.printStackTrace();
         } finally {
        	   closeSocket();
	      }
  		}

      private void askInformation() throws IOException {
         out.println("Welcome to HW2 P2-1 Local Server. Please give me your identity.");
         out.println("Your name (PinYin): ");
         student_name = in.readLine();
         out.println("Your student ID:");
         student_id = in.readLine();
         student_id = student_id.toUpperCase();

         if (student_name == null || student_id == null) {
            throw new IllegalStateException("");
         }
      }

      private void checkInformation() throws IOException {
         out.println("Hi " + student_name + 
            ", your student id is " + student_id +
            ". Is it correct? (Y/N)");
         String input = in.readLine();
         if (input == null) {
            throw new IllegalStateException("");
         } else if (!input.equals("Y") && !input.equals("y")) {
            out.println("Please retry it again.");
            throw new IllegalStateException("");
         }
      }
      private void recordInformation() throws IOException {
         out.println("Thanks. Your response has been recorded. " +
            "Please remeber to print-screen this execution, and have a nice day! " + 
            "(Session End)");
         try {
            synchronized(fw) {
               fw.write(student_id + "," + student_name + "," + socket + "\r\n");
               fw.flush();
            }
         } catch (IOException e) {
            log("Error writing record for " + student_id + ".");
         }
      }

      private void closeSocket() {
         try {
            socket.close();
         } catch (IOException e) {
            e.printStackTrace();
         } finally {
            log("Connection # " + clientNumber + " closed");   
         }
      }

      private void log(String message) {
      	System.out.println(message);
      }
   }
}


