package com.network.socketchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Socket socket;
    private List<Chat> chats;
    Context context;
    RecyclerView rc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        chats = new ArrayList<>();
        ClientThread thread = new ClientThread();
        thread.start();
        EditText ed = findViewById(R.id.input);
        Button send = findViewById(R.id.button2);

        rc = findViewById(R.id.recyclerview);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(context));

        ReceiveThread recT = new ReceiveThread();
        recT.start();
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SendThread sendT = new SendThread();
                sendT.start();
            }
        });
        ChatAdapter chatAdapter = new ChatAdapter(context, chats);
        rc.setAdapter(chatAdapter);
    }

    class ClientThread extends Thread {

        public void run(){
            try{
                String host = "192.168.0.3";
                int port = 9090;
                socket = new Socket(host, port);

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread{
        EditText ed = findViewById(R.id.input);

        public void run(){
            try{
                OutputStream ostream = socket.getOutputStream();
                PrintWriter pwrite = new PrintWriter(ostream, true);
                String msg = ed.getText().toString();
                //Log.d("msg", msg);
                if(msg != null) {
                    pwrite.println(msg);
                    pwrite.flush();
                    Log.d("Client", msg);
                    Chat chat_c = new Chat(1, msg);
                    chats.add(chat_c);
                    ed.setText("");
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    class ReceiveThread extends Thread{

        public void run(){
            try {
                InputStream istream = socket.getInputStream();
                BufferedReader read = new BufferedReader(new InputStreamReader(istream));

                while (true) {
                    String smsg = read.readLine();
                    if (smsg != null) {
                        Log.d("Server", smsg);
                        Chat chat_s = new Chat(0, smsg);
                        chats.add(chat_s);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}