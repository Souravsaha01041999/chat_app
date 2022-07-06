package com.example.chat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class chat_list extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView rv;
    GridLayoutManager glm;
    List<ContacctLists>cont;
    String tempno="",number;
    SharedPreferences sp;
    Adpt ad;
    Toolbar t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        fab=findViewById(R.id.chat_list_goall);
        rv=findViewById(R.id.lists_chat);
        t=findViewById(R.id.my_bar_fnt_tool);
        t.inflateMenu(R.menu.lists_option);
        setSupportActionBar(t);


        t.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                if (id==R.id.profile_change)
                {
                    startActivity(new Intent(chat_list.this,profilechange.class).putExtra("ph_no",sp.getString("chat_app_user_id","")));
                }
                return false;
            }
        });

        sp= PreferenceManager.getDefaultSharedPreferences(chat_list.this);
        number=sp.getString("chat_app_user_id","");

        cont=new ArrayList<>();

        glm=new GridLayoutManager(chat_list.this,1);
        rv.setLayoutManager(glm);
        ad=new Adpt(chat_list.this);

        ContentResolver cr=getContentResolver();
        Cursor cur=cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);
        if(cur.getCount()>0)
        {
            cur.moveToFirst();
            while(cur.moveToNext())
            {
                String id=cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor pcur=cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"=?",new String[]{id},null);
                while(pcur.moveToNext())
                {
                    if (!tempno.equals(pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ",""))) {
                        tempno=pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ","");
                        if (tempno.length()>10) {
                            cont.add(new ContacctLists(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), tempno.substring(tempno.length() - 10)));
                        }
                        else {
                            cont.add(new ContacctLists(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), tempno));
                        }
                    }
                }
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/get_chat_list.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.trim();
                        if (!response.equals("]")) {
                            try {
                                JSONArray ja = new JSONArray(response);
                                for (int i = 0; i < ja.length(); i++) {
                                    JSONObject jo = ja.getJSONObject(i);
                                    for (int ii=0;ii<cont.size();ii++){
                                        if(cont.get(ii).getContNumber().equals(jo.getString("chat_from")))
                                        {
                                            ad.add(new RcvData(cont.get(ii).getContName(),cont.get(ii).getContNumber(),jo.getString("image"),jo.getString("seen")));
                                        }
                                    }
                                }
                                rv.setAdapter(ad);
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
                        pems.put("number",number);
                        pems.put("cmd","n");
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(chat_list.this);
                q.add(sr);
                q.getCache().clear();
            }
        },1000);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(chat_list.this,all_cont.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists_option,menu);
        return super.onCreateOptionsMenu(menu);
    }



    void  msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
