import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

final class ChatServer {


    private static int uniqueId = 0;
    private final List<ClientThread> clients = new ArrayList<>();
    private final int port;
    String path = "";
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private ChatServer(int port) {
        this.port = port;
    }

    private ChatServer(int port, String path) {
        this.port = port;
        this.path = path;
    }

    /*
     * This is what starts the ChatServer.
     * Right now it just creates the socketServer and adds a new ClientThread to a list to be handled
     */
    private void start() {
        try {
            String chatTime = sdf.format(System.currentTimeMillis()) + " ";
            System.out.println(chatTime + " Server waiting for Clients on port " + port + ".");
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                Socket socket = serverSocket.accept();
                Runnable r = new ClientThread(socket, uniqueId++);
                Thread t = new Thread(r);
                clients.add((ClientThread) r);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     *  > java ChatServer
     *  > java ChatServer portNumber
     *  If the port number is not specified 1500 is used
     */
    public void chatPath(String str) {
        path = str;

    }

    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);
            String readLine = s.nextLine();
            String[] arry = readLine.split(" ");
            ChatServer server;
            if (arry.length == 2) {
                int porty = Integer.parseInt(arry[0]);
                server = new ChatServer(porty, arry[1]);
            } else if (arry.length == 1) {
                server = new ChatServer(Integer.parseInt(arry[0]));
            } else {
                server = new ChatServer(1500);
            }
            System.out.println("Banned words file: " + arry[1] + "\n");
            server.chatPath(arry[1]);
            System.out.println("Banned words: ");
            ChatFilter cf = new ChatFilter(arry[1]);
            for (int i = 0; i < cf.badWords.size(); i++) {
                System.out.println(cf.badWords.get(i));
            }
            System.out.println();
            server.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    /*
     * This is a private class inside of the ChatServer
     * A new thread will be created to run this every time a new client connects.
     */
    private final class ClientThread implements Runnable {
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        int id;
        String username;
        ChatMessage cm;

        private ClientThread(Socket socket, int id) {
            this.id = id;
            this.socket = socket;
            try {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                username = (String) sInput.readObject();
                String chatTime = sdf.format(System.currentTimeMillis()) + " ";
                System.out.println(chatTime + username + " just connected.");
                System.out.println(chatTime + " Server waiting for Clients on port " + port + ".");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        /*
         * This is what the client thread actually runs.
         */
        @Override
        public void run() {

            while (true) {

                // Read the username sent to you by client
                try {
                    cm = (ChatMessage) sInput.readObject();
                    if (cm.getType() == 1) {
                        sOutput.writeObject(cm.getMessage());
                        break;
                    } else if (cm.getType() == 0) {
                        broadcast(username + ": " + cm.getMessage() + "\n");
                    } else if (cm.getType() == 3) {
                        directMessage(cm.getMessage(), cm.getRecipient());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // Send message back to the client
            }
        }

        private void directMessage(String message, String username) {
            String[] to = cm.getMessage().split(" ");
            for (int i = 0; i < clients.size(); i++) {
                if (clients.get(i).username.equals(username)) {
                    System.out.println(cm.getMessage());
                    try {
                        clients.get(i).writeMessage(this.username + " -> " + username + ": " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        private String listUsers() {
            String listOfUsers = "";
            for (int i = 0; i < clients.size(); i++) {

                if (i < clients.size() - 1) {
                    listOfUsers += clients.get(i).username + "\n";
                } else {
                    listOfUsers += clients.get(i).username;
                }
            }
            return listOfUsers;
        }

        private synchronized void broadcast(String message) {
            try {
                String fullMessage = "";
                ChatFilter cf = new ChatFilter(path);
                for (int i = 0; i < clients.size(); i++) {
                    String chatTime = sdf.format(System.currentTimeMillis()) + " ";
                    fullMessage = chatTime + " " + cf.filter(message);
                    clients.get(i).writeMessage(fullMessage);
                    System.out.println(fullMessage);
                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean writeMessage(String msg) throws IOException {
            if (socket.isConnected()) {
                sOutput.writeObject(msg);
                sOutput.flush();
                return true;
            } else {
                return false;
            }
        }

        private synchronized void remove(int id) {
            clients.remove(id);
        }

        private void close() {
            try {
                sInput.close();
                sOutput.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
