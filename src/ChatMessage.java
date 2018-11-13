
import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;
    private String message;
    private int type;
    public ChatMessage(String message, int type) {
        this.message = message;
        this.type = type;
    }
    private void broadcast(String message) {

    }
    private boolean writeMessage(String msg) {

    }
    private void remove(int id) {

    }
    public void run() {

    }
    private void close() {

    }

    // Here is where you should implement the chat message object.
    // Variables, Constructors, Methods, etc.
}
