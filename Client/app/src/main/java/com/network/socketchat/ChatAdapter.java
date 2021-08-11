package com.network.socketchat;

import android.content.Context;
import android.os.AsyncTask;
import android.text.PrecomputedText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    private List<Chat> Chats;
    public static final int MSS_TYPE_RECEIVE = 0;
    public static final int MSS_TYPE_SEND = 1;

    public ChatAdapter(Context context, List<Chat> Chats){
        this.context = context;
        this.Chats = Chats;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView sendMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sendMessage = itemView.findViewById(R.id.message);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSS_TYPE_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.message_send, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.message_received, parent, false);
            return new ChatAdapter.ViewHolder(view);
        }
    }

    // 비동기 처리
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = Chats.get(position);
        holder.sendMessage.setText(chat.getMessage());
        Log.d("chats", chat.getMessage());
//        BackgroundTask task = new BackgroundTask(chat.getMessage(), holder);
//        task.execute();
//        task.cancel(false);
    }

    @Override
    public int getItemCount() {
        return Chats.size();
    }

    public int getItemViewType(int position){
        if(Chats.get(position).getUser() == 1){
            return MSS_TYPE_SEND;
        }
        else return MSS_TYPE_RECEIVE;
    }

//    class BackgroundTask extends AsyncTask<Integer, Integer, Integer>{
//
//        String msg;
//        ViewHolder holder;
//
//        public BackgroundTask(String msg, ViewHolder holder){
//            this.msg = msg;
//            this.holder = holder;
//        }
//
//        @Override
//        protected void onPreExecute(){
//            holder.sendMessage.setText(msg);
//            Log.d("chats", msg);
//        }
//
//        @Override
//        protected Integer doInBackground(Integer... value) {
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values){
//            //super.onProgressUpdate(values);
//            //publishProgress();
//        }
//    }
}
