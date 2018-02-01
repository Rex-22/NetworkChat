package ruben.netchat.client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientChat {

    private int port;
    private InetAddress address;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buffer = new byte[2048];

    public static void main(String args[]) {
//        if (args.length == 0) {
//            System.out.println("Usage: java ClientChat <host_address>");
//        }

        new ClientChat("127.0.0.1");
    }

    public ClientChat(String serverIP) {
        String message;

        Scanner input = new Scanner(System.in);
        System.out.println("Type a message");
        message = input.nextLine();

        try {
            try {
                for (int i = 0; i < message.length(); i++) {
                    buffer[i] = (byte) message.charAt(i);
                }
            }catch (Exception e) {
                System.err.println("Message is too long");
                e.printStackTrace();
            }

            socket = new DatagramSocket(5413);
            address = InetAddress.getByName(serverIP);
            port = 5412;
            packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);

            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Server: " + received);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
