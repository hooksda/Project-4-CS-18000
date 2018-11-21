
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


    private ChatServer(int port) {
        this.port = port;
    }
    /*
     * This is what starts the ChatServer.
     * Right now it just creates the socketServer and adds a new ClientThread to a list to be handled
     */
    private void start() {
        try {
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
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter port number");
        String portnumb = s.nextLine();
        if (portnumb.equals("")) {
            portnumb = "1500";
        }
        int porty = Integer.parseInt(portnumb);
        ChatServer server = new ChatServer(porty);
        System.out.println(portnumb);
        server.start();
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
                if (clients.size() < 2) {
                    System.out.println("Server waiting for Clients on port 1500.");
                }

                // Read the username sent to you by client
                try {
                    cm = (ChatMessage) sInput.readObject();
                    if (cm.getType() == 1) {
                        sOutput.writeObject(cm.getMessage());
                        break;
                    } else {
                        broadcast(username + ": " + cm.getMessage() + "\n");
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
                if (clients.get(i).username.equals(cm.getRecipient())) {
                    System.out.println(cm.getMessage());
                    try {
                        clients.get(i).writeMessage(cm.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private synchronized void broadcast(String message) {
            String fullMessage = "";
            for (int i = 0; i < clients.size(); i++) {
                try {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("America/Indiana/Indianapolis"));
                    String time = sdf.format(date);
                    fullMessage = time + " " + message;
                    clients.get(i).writeMessage(fullMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(fullMessage);
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
