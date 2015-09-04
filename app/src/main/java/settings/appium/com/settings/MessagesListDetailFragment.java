package settings.appium.com.settings;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.apache.commons.logging.Log;

import java.util.logging.Logger;

import settings.appium.com.settings.Manager.MessageContent;
import settings.appium.com.settings.Manager.SongContent;
import settings.appium.com.settings.handlers.MessagesManager;

/**
 * A fragment representing a single Messages_List detail screen.
 * This fragment is either contained in a {@link MessagesListListActivity}
 * in two-pane mode (on tablets) or a {@link MessagesListDetailActivity}
 * on handsets.
 */
public class MessagesListDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    /**
     * The dummy content this fragment is presenting.
     */
    private MessageContent.MessageItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessagesListDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = MessageContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messageslist_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.messageslist_detail)).setText(mItem.body);
            String SmsMessageId = mItem.id;
            MessagesManager.updateSMSReadStatus(getActivity(), true, SmsMessageId);
        }

        return rootView;
    }
}
