package settings.appium.com.settings.handlers;

import java.util.*;

import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.MediaStore;
import android.database.Cursor;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by prateek on 4/13/15.
 */
public class SongsManager {
    //Some audio may be explicitly marked as not being music
    static String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

    static String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.TRACK,
            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.YEAR,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATE_MODIFIED,
            MediaStore.Audio.Media.MIME_TYPE
    };

    public static List<String> SongsMetaData(Context context){
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                new String[]{"DISTINCT _id, name"},
                "name is not null) or (name != ''",
                null,
                null
        );

        List<String> genres = new ArrayList<String>();

        cursor.moveToFirst();

        for(int i = 0 ; i < cursor.getCount(); i++){
            genres.add(cursor.getString(0) + "||" + cursor.getString(1));
            cursor.moveToNext();
        }

        cursor.close();

        List<List<String>> audio_genres = new ArrayList<List<String>>();

        for(int i = 0; i < genres.size(); i++){
            cursor = context.getContentResolver().query(
                    MediaStore.Audio.Genres.Members.getContentUri("external", Long.parseLong(genres.get(i).split("\\|\\|")[0].trim())),
                    new String[]{MediaStore.Audio.Genres.Members._ID, MediaStore.Audio.Genres.Members.TITLE},
                    MediaStore.Audio.Genres.Members.GENRE_ID + " = " + Long.parseLong(genres.get(i).split("\\|\\|")[0].trim()),
                    null,
                    null);

            List<String> AudioIds = new ArrayList<String>();
            cursor.moveToFirst();
            for(int j = 0 ; j < cursor.getCount(); j++){
                AudioIds.add(cursor.getString(0) + "||" + cursor.getString(1) + "||" + (genres.get(i).split("\\|\\|").length > 1 ? genres.get(i).split("\\|\\|")[1] : "NA"));
                cursor.moveToNext();
            }
            cursor.close();

            if(AudioIds.size() > 0) {
                audio_genres.add(AudioIds);
            }
        }

        cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        int count = cursor.getCount();

        List<String> songs = new ArrayList<String>();
        while(cursor.moveToNext()) {
            int i = cursor.getInt(0);
            int l = cursor.getString(1).length();
            if (l > 0) {
                String genre = "null";
                String audioId = cursor.getString(0);
                for(int m = 0; m < audio_genres.size(); m++){
                    for(int n = 0; n < audio_genres.get(m).size(); n++){
                        if(audio_genres.get(m).get(n).split("\\|\\|")[0].trim().equals(audioId)){
                            /*int flag = 0;
                            for(int u = 0 ; u < genres.get(m).split("\\|\\|").length; u++) {
                                if(u == 1) {
                                    flag = 1;
                                    genre = genres.get(m).split("\\|\\|")[u].trim();
                                    break;
                                }
                            }
                            if(flag == 0){
                                genre = "NA";
                                Log.d("SONG W/O GENRE", cursor.getString(2));
                            }*/
                            genre = audio_genres.get(m).get(n).split("\\|\\|")[2].trim();
                            break;
                        }
                    }
                }
                songs.add(cursor.getString(0) + "||"
                        + cursor.getString(1) + "||"
                        + cursor.getString(2) + "||"
                        + cursor.getString(3) + "||"
                        + cursor.getString(4) + "||"
                        + cursor.getString(5) + "||"
                        + cursor.getString(6) + "||"
                        + cursor.getString(7) + "||"
                        + cursor.getString(8) + "||"
                        + cursor.getString(9) + "||"
                        + cursor.getString(10) + "||"
                        + cursor.getString(11) + "||"
                        + cursor.getString(12) + "||"
                        + cursor.getString(13) + "||"
                        + genre + "||"
                        + count);
            }
            else{
                // delete any play-lists with a data length of '0'
                android.net.Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, i);
                context.getContentResolver().delete(uri, null, null);
            }
        }

        cursor.close();

        return songs;
    }
}
