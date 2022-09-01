package elawaves.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import elawaves.Carrier.Messages.Message;
import elawaves.R;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(@NonNull Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    // Function to obtain and display messages between a friend
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        Message message = getItem(position);

        // Conditional to differentiate between sent and received messages
        if(message.isReceived()){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item_received,parent,false);
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_list_item_sent,parent,false);
        }

        TextView messageView = convertView.findViewById(R.id.messageTextView);
            messageView.setText(message.getMessage());

        return convertView;
    }
}
