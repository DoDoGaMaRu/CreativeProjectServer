package network;

import java.io.*;
import java.net.*;
import java.util.Scanner;

//port : 5000
public class Server {
    private static final String IP = "127.0.0.1";
    private static final int PORT = 8080;
    private ServerSocket serverSocket;

    public static void main(String... args) {
        Server server = new Server();
        server.run();
    }

    public void run() {
        System.out.println("[INFO] Server run " + IP + ":" + PORT);

        try {
            InetAddress inetAddress = InetAddress.getByName(IP);
            Scanner sc = new Scanner(System.in);
            serverSocket = new ServerSocket(PORT, 50, inetAddress);

            ConnectThread connectThread = new ConnectThread(serverSocket);
            connectThread.setDaemon(true);

            connectThread.start();
            connectThread.join();
        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}