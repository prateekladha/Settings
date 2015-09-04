package settings.appium.com.settings;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import settings.appium.com.settings.Manager.SongContent;
import settings.appium.com.settings.handlers.SongsManager;


public class USSDCalling extends ActionBarActivity implements View.OnClickListener {

    public static List<String> songsMetaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ussdcalling);
        findViewById(R.id.search2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText ussdNumber = (EditText)findViewById(R.id.editText1);
        if(!ussdNumber.getText().toString().trim().equalsIgnoreCase("")){
            String ussdCode = ussdNumber.getText().toString().trim().replace("#", Uri.encode("#"));
            startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussdCode)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ussdcalling, menu);
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
        else if(id == R.id.messages){
            android.content.Intent i = new android.content.Intent(getApplicationContext(), Messages.class);
            this.startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
