package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class chat_section extends AppCompatActivity {
    String chat_to_number,datatemp;
    Button send;
    EditText chat;
    RecyclerView rv;
    GridLayoutManager glm;
    Chat_Adpt ca;
    MediaPlayer mp;
    CountDownTimer cdt;

    Date d;
    DateFormat df;
    DateFormat tf;
    String msgid,chat_frm_number;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_section);
        send=findViewById(R.id.chat_send_btn);
        chat=findViewById(R.id.chat_send_text);
        rv=findViewById(R.id.chat_all);
        mp=MediaPlayer.create(chat_section.this,R.raw.send_audio);

        glm=new GridLayoutManager(chat_section.this,1);
        rv.setLayoutManager(glm);

        sp= PreferenceManager.getDefaultSharedPreferences(chat_section.this);
        chat_frm_number=sp.getString("chat_app_user_id","");

        chat_to_number=getIntent().getStringExtra("chat_to_number");

        ca=new Chat_Adpt(chat_section.this,chat_frm_number);
        StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/get_chat.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response=response.trim();
                if (!response.equals("]")) {
                    try {
                        JSONArray ja = new JSONArray(response);
                        ca.data.clear();
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            ca.set(new ChatDet(jo.getString("msg_id"), jo.getString("msg"), jo.getString("chat_to"), jo.getString("chat_from"), jo.getString("seen")));
                        }
                        reFresh();
                        cdt.start();
                    } catch (JSONException e) {

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msg("Check your internet connection");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>pems=new Hashtable<>();
                pems.put("to",chat_to_number);
                pems.put("frm",chat_frm_number);
                pems.put("cmd","c");
                return pems;
            }
        };
        sr.setShouldCache(false);
        RequestQueue q= Volley.newRequestQueue(chat_section.this);
        q.add(sr);
        q.getCache().clear();
        cdt=new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/get_chat.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.trim();
                        if (!response.equals("]")) {
                            try {
                                JSONArray ja = new JSONArray(response);
                                ca.data.clear();
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);
                                    ca.set(new ChatDet(jo.getString("msg_id"), jo.getString("msg"), jo.getString("chat_to"), jo.getString("chat_from"), jo.getString("seen")));
                                }
                                reFresh();
                                cdt.start();
                            } catch (JSONException e) {

                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Check your internet connection");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>pems=new Hashtable<>();
                        pems.put("to",chat_to_number);
                        pems.put("frm",chat_frm_number);
                        pems.put("cmd","n");
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(chat_section.this);
                q.add(sr);
                q.getCache().clear();
            }

            @Override
            public void onFinish() {
                cdt.start();
            }
        };

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chat.getText().toString().length()>0)
                {
                    datatemp=chat.getText().toString();
                    chat.setText("");
                    d = new Date();
                    df = new SimpleDateFormat("ddMMyyyy");
                    tf = new SimpleDateFormat("HHmmss");

                    msgid=df.format(d)+tf.format(d)+chat_frm_number+chat_to_number;

                    StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/send_msg.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            mp.start();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            msg("Check your internet connection");
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String>pems=new Hashtable<>();
                            pems.put("mid",msgid);
                            pems.put("cto",chat_to_number);
                            pems.put("cf",chat_frm_number);
                            pems.put("msg",datatemp);
                            return pems;
                        }
                    };
                    sr.setShouldCache(false);
                    RequestQueue q= Volley.newRequestQueue(chat_section.this);
                    q.add(sr);
                    q.getCache().clear();
                    ca.add(new ChatDet(msgid,datatemp,chat_to_number,chat_frm_number,"1"));
                    reFresh();
                }
            }
        });

    }
    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }

    void reFresh()
    {
        rv.setAdapter(ca);
        rv.scrollToPosition(ca.data.size()-1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cdt.cancel();
    }
}
