package in.skylinelabs.Kym;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */

public class ChatMessage {
    private long id;
    private int tag;
    private String message;
    private String dateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int isMe) {
        this.tag = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }
}
