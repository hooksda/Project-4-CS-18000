import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;
    private String message;
    private int type;
    public ChatMessage(String message, int type) {
        this.message = message;
        this.type = type;
    }
    private synchronized void broadcast(String message) {

    }
    private boolean writeMessage(String msg) throws IOException {
return true;

    }
    private void remove(int id) {

    }
    public void run() {

    }
    private void close() {

    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    // Here is where you should implement the chat message object.
    // Variables, Constructors, Methods, etc.
}
