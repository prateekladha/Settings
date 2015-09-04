package settings.appium.com.settings;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.network.connectionclass.ConnectionQuality;
import com.facebook.network.connectionclass.DeviceBandwidthSampler;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * AsyncTask for handling downloading and making calls to the timer.
 */
public class DownloadImage extends AsyncTask<String, Void, Void> {

    private static final String TAG = "ConnectionClass-Sample";
    private String url;
    private android.content.Context c;
    private ImageView img;
    private Bitmap bmp;
    private TextView percent;
    private ImageLoaderListener listener;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private View mRunningBar;
    private int mTries;

    public DownloadImage(String url, ImageView img, android.content.Context c, Bitmap bmp, ImageLoaderListener listener, DeviceBandwidthSampler mDeviceBandwidthSampler, View mRunningBar, int mTries) {
/*--- we need to pass some objects we are going to work with ---*/
        this.url = url;
        this.c = c;
        this.img = img;
        this.bmp = bmp;
        this.listener = listener;
        this.mDeviceBandwidthSampler = mDeviceBandwidthSampler;
        this.mRunningBar = mRunningBar;
        this.mTries = mTries;
    }

    public interface ImageLoaderListener {
        void onImageDownloaded(Bitmap bmp);
    }

    @Override
    protected void onPreExecute() {
        mDeviceBandwidthSampler.startSampling();
        mRunningBar.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... url) {
        /*String imageURL = url[0];
        try {
            // Open a stream to download the image from our URL.
            InputStream input = new URL(imageURL).openStream();
            try {
                byte[] buffer = new byte[1024];

                // Do some busy waiting while the stream is open.
                while (input.read(buffer) != -1) {
                }

                bmp = BitmapFactory.decodeStream(input);
            } finally {
                input.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error while downloading image.");
        }*/
        bmp = getBitmapFromURL(url[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        mDeviceBandwidthSampler.stopSampling();
        // Retry for up to 10 times until we find a ConnectionClass.
        if (MainActivity.mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
            mTries++;
            new DownloadImage(url, img, c, bmp, listener, mDeviceBandwidthSampler, mRunningBar, mTries).execute(url);
        }
        if (listener != null) {
            listener.onImageDownloaded(bmp);
        }
        if (!mDeviceBandwidthSampler.isSampling()) {
            mRunningBar.setVisibility(View.GONE);
            img.setImageBitmap(bmp);
        }

        super.onPostExecute(v);
    }

    public Bitmap getBitmapFromURL(String link) {
        try {
            URL url = new URL(link);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            return myBitmap;

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("getBmpFromUrl error: ", e.getMessage().toString());
            return null;
        }
    }
}
