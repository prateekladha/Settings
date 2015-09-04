package settings.appium.com.settings.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import settings.appium.com.settings.MainActivity;
import settings.appium.com.settings.Messages;
import settings.appium.com.settings.USSDCalling;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SongContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<SongItem> ITEMS = new ArrayList<SongItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, SongItem> ITEM_MAP = new HashMap<String, SongItem>();

    static {
        if(MainActivity.songsMetaData != null) {
            for (int i = 0; i < MainActivity.songsMetaData.size(); i++) {
                String[] data = MainActivity.songsMetaData.get(i).split("\\|\\|");
                SongContent.addItem(new SongContent.SongItem(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15]));
            }
        }
        else if(USSDCalling.songsMetaData != null){
            for (int i = 0; i < USSDCalling.songsMetaData.size(); i++) {
                String[] data = USSDCalling.songsMetaData.get(i).split("\\|\\|");
                SongContent.addItem(new SongContent.SongItem(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15]));
            }
        }
        else{
            for (int i = 0; i < Messages.songsMetaData.size(); i++) {
                String[] data = Messages.songsMetaData.get(i).split("\\|\\|");
                SongContent.addItem(new SongContent.SongItem(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15]));
            }
        }
    }

    public static void addItem(SongItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SongItem {
        public String id;
        public String artist;
        public String title;
        public String data;
        public String display_name;
        public String duration;
        public String album;
        public String album_id;
        public String track;
        public String composer;
        public String year;
        public String date_added;
        public String date_modified;
        public String mime_type;
        public String genre;
        public String count;

        public SongItem(String id, String artist, String title, String data, String display_name, String duration, String album, String album_id, String track, String composer, String year, String date_added, String date_modified, String mime_type, String genre, String count) {
            this.id = id;
            this.artist = artist;
            this.title = title;
            this.data = data;
            this.display_name = display_name;
            this.duration = duration;
            this.album = album;
            this.album_id = album_id;
            this.track = track;
            this.composer = composer;
            this.year = year;
            this.date_added = date_added;
            this.date_modified = date_modified;
            this.mime_type = mime_type;
            this.genre = genre;
            this.count = count;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
