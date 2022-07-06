package com.example.chat_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText number,otp,email;
    Button login,send_otp;
    String otpvalue;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        number=findViewById(R.id.login_phone_number);
        login=findViewById(R.id.login_login_button);
        email=findViewById(R.id.login_email);
        otp=findViewById(R.id.login_otp);
        send_otp=findViewById(R.id.login_send_otp);

        sp= PreferenceManager.getDefaultSharedPreferences(login.this);
        spe=sp.edit();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date d=new Date();
                DateFormat df=new SimpleDateFormat("mmss");
                otpvalue=df.format(d);

                StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/for_otp.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        msg("Otp has been send in your mail check the otp");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                otpvalue = "";
                            }
                        },60000);
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
                        pems.put("rcvdata",otpvalue);
                        pems.put("email",email.getText().toString());
                        return pems;
                    }
                };
                sr.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(login.this);
                q.add(sr);
                q.getCache().clear();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!number.getText().toString().contains("+"))
                {
                    msg("enter +");
                }
                else if ((email.getText().toString().length()>0)&&(number.getText().toString().length()>0)&&(otp.getText().toString().length()>0))
                {
                    if (otp.getText().toString().equals(otpvalue))
                    {
                        StringRequest sr=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/login.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response=response.trim();
                                if (response.equals("1"))
                                {
                                    String t=number.getText().toString();
                                    t=t.substring(t.length()-10);
                                    spe.putString("chat_app_user_id",t);
                                    spe.apply();
                                    startActivity(new Intent(login.this,chat_list.class));
                                    finish();
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
                                pems.put("mno",number.getText().toString());
                                return pems;
                            }
                        };
                        sr.setShouldCache(false);
                        RequestQueue q= Volley.newRequestQueue(login.this);
                        q.add(sr);
                        q.getCache().clear();
                    }
                    else {
                        msg("False OTP");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Enter Data",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
