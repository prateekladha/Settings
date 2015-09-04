package settings.appium.com.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.*;
import android.graphics.*;
import android.view.View;
import com.facebook.network.connectionclass.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import settings.appium.com.settings.Manager.SongContent;
import settings.appium.com.settings.handlers.SongsManager;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "Main Activity";

    private ConnectionClassManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private TextView mTextView;
    private View mRunningBar;

    private String mURL = "http://connectionclass.parseapp.com/m100_hubble_4060.jpg";
    private int mTries = 0;
    public static ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
    private android.widget.ImageView img;
    private static android.graphics.Bitmap bmp;
    private DownloadImage mDownloader;

    public static List<String> songsMetaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*final IntentFilter mIntentFilter = new IntentFilter(MyAccessibilityService.Constants.ACTION_CATCH_NOTIFICATION);
        mIntentFilter.addAction(MyAccessibilityService.Constants.ACTION_CATCH_TOAST);
        registerReceiver(toastOrNotificationCatcherReceiver, mIntentFilter);
        Log.v(TAG, "Receiver registered.");*/

        Bundle extras = this.getIntent().getExtras();

        if(extras != null && !extras.isEmpty() && extras.size() > 0) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
            layout.setVisibility(View.VISIBLE);
            Iterator iter = extras.keySet().iterator();
            int i = 0;
            boolean flag = false;
            while (iter.hasNext()) {
                String name = (String) iter.next();
                if(!name.trim().equalsIgnoreCase("run")) {
                    Service service = ServicesFactory.getService(this, name);
                    if (service != null) {
                        String value = extras.getString(name);
                        updateView(i, name, value);
                        boolean status = (value.equalsIgnoreCase("on")) ? service.enable() : service.disable();
                    }
                }
                else{
                    flag = true;
                    String command = extras.getString(name).trim();
                    if(!command.equalsIgnoreCase("")){
                        String commandText = command;
                        Process process = null;
                        try {
                            process = Runtime.getRuntime().exec(commandText);
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String line;
                            StringBuilder sb = new StringBuilder();
                            while ((line = bufferedReader.readLine())!=null){
                                sb.append(line + "\n");
                            }
                            updateView(i, name, sb.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                i++;
            }
        }

        /*WebView myWebView = (WebView) findViewById(R.id.webView);
        myWebView.loadUrl("http://www.example.com");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        /*mConnectionClassManager = ConnectionClassManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        findViewById(R.id.test_btn).setOnClickListener(this);
        mTextView = (TextView)findViewById(R.id.connection_class);
        mTextView.setText(mConnectionClassManager.getCurrentBandwidthQuality().toString());
        mRunningBar = findViewById(R.id.runningBar);
        mRunningBar.setVisibility(View.GONE);
        mListener = new ConnectionChangedListener();
        img = (android.widget.ImageView) findViewById(R.id.image);
        img.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mConnectionClassManager.remove(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mConnectionClassManager.register(mListener);
    }

    /**
     * Listener to update the UI upon connectionclass change.
     */
    private class ConnectionChangedListener
            implements ConnectionClassManager.ConnectionClassStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(mConnectionClass.toString());
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        mDownloader = new DownloadImage(mURL, img, MainActivity.this, bmp, new DownloadImage.ImageLoaderListener(){
            public void onImageDownloaded(Bitmap bmp) {
                MainActivity.bmp = bmp;
            }
        }, mDeviceBandwidthSampler, mRunningBar, mTries);
        mDownloader.execute(mURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            SongContent.ITEM_MAP.clear();
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
        else if(id == R.id.ussd){
            android.content.Intent i = new android.content.Intent(getApplicationContext(), USSDCalling.class);
            this.startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void updateView(int index, String name, String value) {
        int viewId = this.getResources().getIdentifier("notice_" + index, "id", this.getPackageName());
        final TextView notice = (TextView) findViewById(viewId);

        if(!name.trim().equalsIgnoreCase("run")) {
            int stringId = this.getResources().getIdentifier(name + "_" + value, "string", this.getPackageName());
            notice.setText(getResources().getString(stringId));
        }
        else{
            notice.setText(value);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(toastOrNotificationCatcherReceiver);
    }

    /*private final BroadcastReceiver toastOrNotificationCatcherReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(TAG, "Received message");
            Log.v(TAG, "intent.getAction() :: " + intent.getAction());
            Log.v(TAG, "intent.getStringExtra(Constants.EXTRA_PACKAGE) :: " + intent.getStringExtra(MyAccessibilityService.Constants.EXTRA_PACKAGE));
            Log.v(TAG, "intent.getStringExtra(Constants.EXTRA_MESSAGE) :: " + intent.getStringExtra(MyAccessibilityService.Constants.EXTRA_MESSAGE));
        }
    };*/
}