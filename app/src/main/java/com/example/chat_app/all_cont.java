package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
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

import java.util.Hashtable;
import java.util.Map;

public class all_cont extends AppCompatActivity {
    String snd="{",tempno="0";
    int i=0;
    RecyclerView rv;
    GridLayoutManager glm;
    Adpt adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cont);

        rv=findViewById(R.id.all_cont_lists);
        glm=new GridLayoutManager(all_cont.this,1);
        rv.setLayoutManager(glm);

        ContentResolver cr=getContentResolver();
        adp=new Adpt(all_cont.this);
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
                        i++;
                        snd = snd + "\""+String.valueOf(i)+ "\":" + "\""+cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)) + "\",\"" + String.valueOf(i + 1) + "\":\"" + pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ","") + "\",";
                        i++;
                    }
                }
            }
        }
        snd=snd.substring(0,snd.length()-1);
        snd=snd+"}";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/cont_list.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response=response.trim();
                        if (!response.equals("no"))
                        {
                            try {
                                JSONArray ja = new JSONArray(response);
                                for (int i=0;i<ja.length();i++)
                                {
                                    JSONObject jo=ja.getJSONObject(i);
                                    adp.add(new RcvData(jo.getString("name"), jo.getString("no"),jo.getString("pic"),"1"));
                                }
                                rv.setAdapter(adp);
                            }
                            catch (JSONException e)
                            {

                            }
                        }
                        else
                        {
                            msg("no data");
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
                        pems.put("d",snd);
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(all_cont.this);
                q.add(sr);
                q.getCache().clear();
            }
        },2000);
    }
    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
