package settings.appium.com.settings.handlers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by prateek on 4/13/15.
 */
public class MessagesManager {

    static Logger log = Logger.getLogger(MessagesManager.class.getName());

    static String[] projection = {
            "_id",
            "address",
            "person",
            "date",
            "protocol",
            "read",
            "status",
            "type",
            "subject",
            "body",
            "service_center",
            "locked",
            "error_code",
            "seen"
    };

    public static int updateSMSReadStatus(Context context, boolean read, String smsId){
        ContentValues values = new ContentValues();
        String readStatus = "1";
        if(!read)
            readStatus = "0";
        values.put("read", readStatus);
        Uri uri = Uri.parse("content://sms/inbox");
        int count = context.getContentResolver().update(uri, values, "_id=" + smsId, null);
        log.info("SMS Id : " + smsId);
        log.info("Rows updated : " + count);

        return count;
    }

    public static List<String> MessagesMetaData(Context context, String address, String body, String read){
        String selection = null;
        if(address.trim().equalsIgnoreCase("") && body.trim().equalsIgnoreCase("")){
            selection = "read = " + read;
        }
        else if(!address.trim().equalsIgnoreCase("") && body.trim().equalsIgnoreCase("")){
            selection = "read = " + read + " and lower(address) like '%" + address.toLowerCase() + "%'";
        }
        else if(address.trim().equalsIgnoreCase("") && !body.trim().equalsIgnoreCase("")){
            selection = "read = " + read + " and lower(body) like '%" + body.toLowerCase() + "%'";
        }
        else{
            selection = "read = " + read + " and lower(address) like '%" + address.toLowerCase() + "%' and lower(body) like '%" + body.toLowerCase() + "%'";
        }

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms/inbox"),
                projection,
                selection,
                null,
                null);

        List<String> messages = new ArrayList<String>();
        while(cursor.moveToNext()) {
            int i = cursor.getInt(0);
            int l = cursor.getString(1).length();
            if (l > 0) {
                messages.add(cursor.getString(0) + "||"
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
                        + cursor.getString(13));
            }
            else{
                android.net.Uri uri = ContentUris.withAppendedId(Uri.parse("content://sms/inbox"), i);
                context.getContentResolver().delete(uri, null, null);
            }
        }

        return messages;
    }
}
