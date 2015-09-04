package settings.appium.com.settings;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.MediaStore;
import android.net.Uri;
import android.graphics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import settings.appium.com.settings.Manager.SongContent;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    final Uri sourceUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    final Uri thumbUri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    final String thumb_DATA = MediaStore.Images.Thumbnails.DATA;
    final String thumb_IMAGE_ID = MediaStore.Images.Thumbnails.IMAGE_ID;

    /**
     * The dummy content this fragment is presenting.
     */
    private SongContent.SongItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = SongContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            Bundle bundle = getArguments();
            if(bundle != null){
                String detail;
                detail = bundle.getString("POSITION");
                int position = Integer.parseInt(detail);
            }
            String mediaId = mItem.id;
            ((TextView) rootView.findViewById(R.id.title)).setText(mItem.title);
            ((TextView) rootView.findViewById(R.id.album)).setText("ALBUM NAME :: " + mItem.album);
            ((TextView) rootView.findViewById(R.id.artist)).setText("ARTIST NAME :: " + mItem.artist);
            ((TextView) rootView.findViewById(R.id.data)).setText("PATH :: " + mItem.data);
            ((TextView) rootView.findViewById(R.id.displayName)).setText("DISPLAY NAME :: " + mItem.display_name);
            ((TextView) rootView.findViewById(R.id.duration)).setText("DURATION :: " + convertDuration(Long.parseLong(mItem.duration)));
            ((TextView) rootView.findViewById(R.id.track)).setText("TRACK :: " + mItem.track);
            ((TextView) rootView.findViewById(R.id.composer)).setText("COMPOSER :: " + mItem.composer);
            ((TextView) rootView.findViewById(R.id.year)).setText("YEAR :: " + mItem.year);

            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String dateString = formatter.format(new java.util.Date(Long.parseLong(mItem.date_added) * 1000));

            ((TextView) rootView.findViewById(R.id.dateAdded)).setText("DATE ADDED :: " + dateString);
            dateString = formatter.format(new java.util.Date(Long.parseLong(mItem.date_modified) * 1000));
            ((TextView) rootView.findViewById(R.id.dateModified)).setText("DATE MODIFIED :: " + dateString);
            ((TextView) rootView.findViewById(R.id.mimeType)).setText("MIME TYPE :: " + mItem.mime_type);
            android.widget.ImageView thumbView = (android.widget.ImageView) rootView.findViewById(R.id.thumbnail);
            Bitmap _bitmap = getThumbnail(Integer.parseInt(mItem.album_id), thumbView);
            if(_bitmap != null) {
                thumbView.setImageBitmap(_bitmap);
            }

            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
            Uri contentUri = android.content.ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, Long.parseLong(mItem.id));
            metaRetriever.setDataSource(getActivity(), contentUri);

            String bitRate = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
            if(bitRate != null) {
                ((TextView) rootView.findViewById(R.id.bitRate)).setText("BIT RATE :: " + Long.parseLong(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)) / 1000 + " kbps");
            }
            else{
                ((TextView) rootView.findViewById(R.id.bitRate)).setText("BIT RATE :: NA");
            }
            ((TextView) rootView.findViewById(R.id.genre)).setText("GENRE :: " + mItem.genre);
            ((TextView) rootView.findViewById(R.id.songsCount)).setText("COUNT :: " + mItem.count);
        }

        return rootView;
    }

    private Bitmap getThumbnail(int id, ImageView thumbView){

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = android.content.ContentUris.withAppendedId(sArtworkUri, Long.parseLong(java.lang.String.valueOf(id)));

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), albumArtUri);
            bitmap = Bitmap.createScaledBitmap(bitmap, 900, 671, true);

        } catch (java.io.FileNotFoundException exception) {
            exception.printStackTrace();
            //bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.audio_file);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        return bitmap;
    }

    public String convertDuration(long duration) {
        String out = null;
        long hours=0;
        try {
            hours = (duration / 3600000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return out;
        }
        long remaining_minutes = (duration - (hours * 3600000)) / 60000;
        String minutes = String.valueOf(remaining_minutes);
        if (minutes.equals(0)) {
            minutes = "00";
        }
        long remaining_seconds = (duration - (hours * 3600000) - (remaining_minutes * 60000));
        String seconds = String.valueOf(remaining_seconds);
        if (seconds.length() < 2) {
            seconds = "00";
        } else {
            seconds = seconds.substring(0, 2);
        }

        if (hours > 0) {
            out = hours + ":" + minutes + ":" + seconds;
        } else {
            out = minutes + ":" + seconds;
        }

        return out;

    }
}
