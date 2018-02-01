package ruben.netchat.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ChatServerThread extends Thread {

    protected DatagramSocket socket;
    protected String message;
    protected boolean isRunning = false;

    public ChatServerThread() {
        this("ChatServer");
    }

    public ChatServerThread(String name) {
        super(name);
        // Open a port to receive datagram from
        try {
            socket = new DatagramSocket(5412);
        } catch (SocketException e) {
            System.err.println("Failed to open port: 5412" );
            e.printStackTrace();
        }

        // Our response message to the client when they send us a packet
        message = "Hello there";
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            // The estimated size of a packet sent from the client
            // TODO(Ruben) : Make a class to hold this kind of information\
            try {
                byte[] buffer = new byte[2048];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                // Receive a packet from the client and store it in our array
                socket.receive(packet);

                //Did the client tell us to shutup?
                String received = new String(packet.getData(), 0, packet.getLength());
                if (received.contains("shutup")){
                    isRunning = false;
                    message = "How are you doing today?";
                }

                // Create our response
                buffer = message.getBytes();

                // Get the address of the client
                InetAddress address = packet.getAddress();
                // Get the port the client sent from
                int port = packet.getPort();
                packet = new DatagramPacket(buffer, buffer.length, address, port);
                socket.send(packet);
            } catch (IOException e) {
                isRunning = false;
                e.printStackTrace();
            }

        }
        socket.close();
    }

}
