package settings.appium.com.settings;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;

import settings.appium.com.settings.Manager.MessageContent;
import settings.appium.com.settings.Manager.SongContent;
import settings.appium.com.settings.handlers.MessagesManager;
import settings.appium.com.settings.handlers.SongsManager;


public class Messages extends ActionBarActivity implements View.OnClickListener {

    public static List<String> songsMetaData;
    public static List<String> messagesMetaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        findViewById(R.id.search1).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText address = (EditText)findViewById(R.id.editText1);
        EditText body = (EditText)findViewById(R.id.editText2);

        CheckBox status = (CheckBox)findViewById(R.id.checkBox);
        String read = "";
        if(status.isChecked()){
            read = "0";
        }
        else{
            read = "1";
        }

        messagesMetaData = MessagesManager.MessagesMetaData(this, address.getText().toString(), body.getText().toString(), read);
        MessageContent.ITEM_MAP.clear();
        MessageContent.ITEMS.clear();
        for(int i = 0; i < Messages.messagesMetaData.size(); i++){
            String[] data = Messages.messagesMetaData.get(i).split("\\|\\|");
            if(!MessageContent.ITEM_MAP.containsKey(data[0])) {
                MessageContent.addItem(new MessageContent.MessageItem(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13]));
            }
        }
        android.content.Intent i = new android.content.Intent(getApplicationContext(), MessagesListListActivity.class);
        this.startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            songsMetaData = SongsManager.SongsMetaData(this);
            if(SongContent.ITEM_MAP != null && SongContent.ITEM_MAP.size() > 0) {
                SongContent.ITEM_MAP.clear();
            }
            SongContent.ITEMS.clear();
            for(int i = 0; i < songsMetaData.size(); i++){
                String[] data = songsMetaData.get(i).split("\\|\\|");
                if(!SongContent.ITEM_MAP.containsKey(data[0])) {
                    SongContent.addItem(new SongContent.SongItem(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9], data[10], data[11], data[12], data[13], data[14], data[15]));
                }
            }
            android.content.Intent i = new android.content.Intent(getApplicationContext(), ItemListActivity.class);
            this.startActivity(i);
            return true;
        }
        else if(id == R.id.ussd){
            android.content.Intent i = new android.content.Intent(getApplicationContext(), USSDCalling.class);
            this.startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
