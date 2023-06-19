package network;

import persistence.CEntityManagerFactory;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Server {
    private static final String IP = "192.168.0.8";
    private static final int PORT = 8080;
    private ServerSocket serverSocket;

    /* TODO 메인으로 이동 */
    public static void main(String... args) {
        /**
         *  INIT MODULE
         *
         *  CEntityManagerFactory   : create EntityManagerFactory
         */
        CEntityManagerFactory.initialization();

        /**
         * SET SERVER
         *
         *
         */
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