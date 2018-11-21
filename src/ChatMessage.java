import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

final class ChatMessage implements Serializable {
    private static final long serialVersionUID = 6898543889087L;
    private String message;
    private int type;
    private String recipient;

    public ChatMessage(String message, int type, String recipient) {
        this.message = message;
        this.type = type;
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public String getRecipient() {
        return recipient;
    }

    // Here is where you should implement the chat message object.
    // Variables, Constructors, Methods, etc.
}
