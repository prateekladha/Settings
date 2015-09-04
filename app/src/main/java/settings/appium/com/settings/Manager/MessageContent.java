package settings.appium.com.settings.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import settings.appium.com.settings.MainActivity;
import settings.appium.com.settings.Messages;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MessageContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<MessageItem> ITEMS = new ArrayList<MessageItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, MessageItem> ITEM_MAP = new HashMap<String, MessageItem>();

    static {
        for(int i = 0; i < Messages.messagesMetaData.size(); i++){
            String[] data = Messages.messagesMetaData.get(i).split("\\|\\|");
            MessageContent.addItem(new MessageContent.MessageItem(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13]));
        }
    }

    public static void addItem(MessageItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class MessageItem {
        public String id;
        public String address;
        public String person;
        public String date;
        public String protocol;
        public String read;
        public String status;
        public String type;
        public String subject;
        public String body;
        public String service_center;
        public String locked;
        public String error_code;
        public String seen;

        public MessageItem(String id, String address, String person, String date, String protocol, String read, String status, String type, String subject, String body, String service_center, String locked, String error_code, String seen) {
            this.id = id;
            this.address = address;
            this.person = person;
            this.date = date;
            this.protocol = protocol;
            this.read = read;
            this.status = status;
            this.type = type;
            this.subject = subject;
            this.body = body;
            this.service_center = service_center;
            this.locked = locked;
            this.error_code = error_code;
            this.seen = seen;
        }

        @Override
        public String toString() {
            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MMM-yyyy");
            String dateString = formatter.format(new java.util.Date(Long.parseLong(date)));
            return address + " : " + dateString;
        }
    }
}
