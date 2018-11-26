import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

final class ChatClient {
    private static ObjectInputStream sInput;
    private static ObjectOutputStream sOutput;
    private static Socket socket;

    private final String server;
    private final String username;
    private final int port;

    private ChatClient(String server, int port, String username) {
        this.server = server;
        this.port = port;
        this.username = username;
    }

    /*
     * This starts the Chat Client
     */
    private boolean start() {
        // Create a socket
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create your input and output streams
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This thread will listen from the server for incoming messages
        Runnable r = new ListenFromServer();
        Thread t = new Thread(r);
        t.start();

        // After starting, send the clients username to the server.
        try {
            sOutput.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    /*
     * This method is used to send a ChatMessage Objects to the server
     */
    private void sendMessage(ChatMessage msg) {
        try {
            sOutput.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * To start the Client use one of the following command
     * > java ChatClient
     * > java ChatClient username
     * > java ChatClient username portNumber
     * > java ChatClient username portNumber serverAddress
     *
     * If the portNumber is not specified 1500 should be used
     * If the serverAddress is not specified "localHost" should be used
     * If the username is not specified "Anonymous" should be used
     */
    public static void main(String[] args) {
        try {
            // Get proper arguments and override defaults
            Scanner s = new Scanner(System.in);
            System.out.println("Enter username");
            String username = s.nextLine();
            System.out.println("Enter port number");
            String portnumber = s.nextLine();
            System.out.println("Enter server name");
            String server = s.nextLine();
            if (server.equals("")) {
                server = "localhost";
            }
            if (portnumber.equals("")) {
                portnumber = "1500";
            }
            if (username.equals("")) {
                username = "Anonymous";
            }
            // Create your client and start it
            ChatClient client = new ChatClient(server, Integer.parseInt(portnumber), username);
            client.start();
            System.out.println("Enter message to be sent");
            while (true) {
                String message = s.nextLine();
                if (message.equals("/logout")) {
                    client.sendMessage(new ChatMessage(username + " has logged out.", 1, null));
                    sOutput.flush();
                    sInput.close();
                    sOutput.close();
                    socket.close();
                    break;
                } else if (message.contains("/msg")) {
                    String[] mess = message.split(" ");
                    String recep = mess[1];
                    String messagetobesent = "";
                    for (int i = 2; i < mess.length; i++) {
                        if (i < mess.length - 1) {
                            messagetobesent += mess[i] + " ";
                        } else if (i == mess.length - 1) {
                            messagetobesent += mess[i];
                        }
                    }
                    client.sendMessage(new ChatMessage(messagetobesent, 3, recep));
                } else if (message.equals("/list")) {
                    client.sendMessage(new ChatMessage(message, 2, null));
                } else {
                    // Send an empty message to the server
                    client.sendMessage(new ChatMessage(message, 0, null));
                }
            }
        } catch (IOException e) {

        }
    }


    /*
     * This is a private class inside of the ChatClient
     * It will be responsible for listening for messages from the ChatServer.
     * ie: When other clients send messages, the server will relay it to the client.
     */
    private final class ListenFromServer implements Runnable {
        public void run() {
            try {
                while (true) {
                    String msg = (String) sInput.readObject();
                    System.out.print(msg);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
