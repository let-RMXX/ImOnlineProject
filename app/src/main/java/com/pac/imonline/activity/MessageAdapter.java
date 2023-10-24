package com.pac.imonline.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pac.imonline.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENDER = 0;
    private static final int VIEW_TYPE_REPLY = 1;

    private List<Message> messageList;
    private MessageAdapterEventListener eventListener;

    public MessageAdapter(MessageAdapterEventListener eventListener){
        this.messageList = new ArrayList<>();
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_SENDER) {
            View view = inflater.inflate(R.layout.message_send_item, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.reply_item, parent, false);
            return new ReplyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Message message = this.messageList.get(position);

        if (holder instanceof SenderViewHolder) {
            SenderViewHolder senderViewHolder = (SenderViewHolder) holder;
            senderViewHolder.textViewSend.setText(message.getSender());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/ HH:mm", Locale.getDefault());
            String dateTime = dateFormat.format(calendar.getTime());
            TextView textViewTimestampSender = holder.itemView.findViewById(R.id.textViewTimestampSender);
            textViewTimestampSender.setText(dateTime);
            senderViewHolder.textViewTimestampSender.setText(message.getTimestamp());
        } else if (holder instanceof ReplyViewHolder) {
            ReplyViewHolder replyViewHolder = (ReplyViewHolder) holder;
            replyViewHolder.textViewReply.setText(message.getReply());
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/ HH:mm", Locale.getDefault());
            String dateTime = dateFormat.format(calendar.getTime());
            TextView textViewTimestampReply = holder.itemView.findViewById(R.id.textViewTimestampReply);
            textViewTimestampReply.setText(dateTime);
            replyViewHolder.textViewTimestampReply.setText(message.getTimestamp());

            //String avatarUrl = get
        }
    }

    //private String getAvatarUrlFromMessage(Message message)

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        if (message.isReply()) {
            return VIEW_TYPE_REPLY;
        } else {
            return VIEW_TYPE_SENDER;
        }
    }

    public void refreshList(List<Message> newMessageList) {
        this.messageList = newMessageList;
        notifyDataSetChanged();
    }

    public class ReplyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewReply;
        public ImageView imageViewReplyAvatar;
        public TextView textViewTimestampReply;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReply = itemView.findViewById(R.id.textViewReply);
            imageViewReplyAvatar = itemView.findViewById(R.id.imageViewReplyAvatar);
            textViewTimestampReply = itemView.findViewById(R.id.textViewTimestampReply);

        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewSend;
        public ImageView imageViewSendAvatar;
        public TextView textViewTimestampSender;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSend = itemView.findViewById(R.id.textViewSend);
            imageViewSendAvatar = itemView.findViewById(R.id.imageViewSendAvatar);
            textViewTimestampSender = itemView.findViewById(R.id.textViewTimestampSender);
        }
    }
    public interface MessageAdapterEventListener {
        void onMessageClicked(long messageId);
        void onMessageLongClicked(long messageId);
    }
}
