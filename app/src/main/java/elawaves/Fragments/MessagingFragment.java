package elawaves.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.elastos.carrier.exceptions.CarrierException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import elawaves.Adapter.MessageAdapter;
import elawaves.Carrier.Friends.FriendManager;
import elawaves.Carrier.Messages.Message;
import elawaves.Carrier.Messages.MessageManager;
import elawaves.R;

public class MessagingFragment extends Fragment implements Observer, View.OnClickListener {

    private MessageAdapter adapter;
    private String address;
    private String friendName;
    private TextView friendNameView;
    private ListView messageListView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messaging,container,false);

        address = getArguments().getString("address");
        friendName = getArguments().getString("name");

        friendNameView = view.findViewById(R.id.recipientText);
        friendNameView.setText(friendName);

        messageListView =  view.findViewById(R.id.messagesList);
            messageListView.setDivider(null);

        MessageManager.getInstance().addObserver(this);

        adapter = new MessageAdapter(Objects.requireNonNull(getActivity()), MessageManager.getInstance().getMessages(this.address));
            messageListView.setAdapter(adapter);

        Button sendMessageButton = view.findViewById(R.id.sendMessageButton);
            sendMessageButton.setOnClickListener(this);

        scrollList();
        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        MessageManager.getInstance().deleteObserver(this);
    }

    public void sendMessage(View view, String message)
    {
        // split messages larger than maxLen
        int maxLen = 255;
        Timestamp time = new Timestamp(System.currentTimeMillis());
        ArrayList<String> messages = new ArrayList<String>();
        while(message.length() > maxLen) {
            messages.add(message.substring(0,maxLen));
            message = message.substring(maxLen);
        }
        messages.add(message);

        for(String messagePart : messages) {
            Message m = new Message(messagePart, false, address, time);
            try {
                MessageManager.getInstance().sendMessage(m);
            } catch (CarrierException e) {
                e.printStackTrace();
            }
        }
    }

    private void messagePopup(View view) {
        RelativeLayout rl = view.findViewById(R.id.messaging_popup_layout);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.messaging_popup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        popupWindow.setElevation(5.0f);
        ImageButton messagingPopupCloseButton = customView.findViewById(R.id.messaging_popup_close_button);
        messagingPopupCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(rl, Gravity.CENTER,0,0);
    }

    @Override
    public void onClick(View v) {

        EditText messageText = ((View) v.getParent()).findViewById(R.id.textInput);
        if(!messageText.getText().toString().equals("")) {
            this.sendMessage(v, messageText.getText().toString());
            messageText.setText("");
            scrollList();
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void scrollList() {
        messageListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                messageListView.setSelection(adapter.getCount() - 1);
            }
        });
    }
}
