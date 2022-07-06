package com.example.chat_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class profilechange extends AppCompatActivity {
    String number="",filename="";
    String time,date;
    ImageView pro;
    Button update,remove;
    byte imgdata[];
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilechange);
        number=getIntent().getStringExtra("ph_no");
        pro=findViewById(R.id.my_change_profile);
        update=findViewById(R.id.update_profile);
        remove=findViewById(R.id.remove_profile);

        ActivityCompat.requestPermissions(profilechange.this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        },200);

        StringRequest st=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/profile/get.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response=response.trim();
                Picasso.get().load(Uri.parse(response))
                        .resize(200,200)
                        .error(R.drawable.errorfor)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(pro);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msg("Error");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>pems=new Hashtable<>();
                pems.put("n",number);
                return pems;
            }
        };
        st.setShouldCache(false);
        RequestQueue q= Volley.newRequestQueue(getApplicationContext());
        q.add(st);
        q.getCache().clear();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"),200);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg("Please wait");
                StringRequest st=new StringRequest(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/profile/set.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        msg("done");
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error");
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>pems=new Hashtable<>();
                        pems.put("n",number);
                        pems.put("ct","r");
                        return pems;
                    }
                };
                st.setShouldCache(false);
                RequestQueue q= Volley.newRequestQueue(getApplicationContext());
                q.add(st);
                q.getCache().clear();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==200)&&(resultCode==RESULT_OK))
        {
            try {
                Bitmap bp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imgdata=getByte(bp);
                pro.setImageBitmap(bp);

                date=new SimpleDateFormat("ddMMyyyy").format(new Date());
                time=new SimpleDateFormat("HHmmss").format(new Date());

                filename=number+date+time+".jpg";

                pd= ProgressDialog.show(profilechange.this,"Updating","Please wait");
                VolleyMultipart vm=new VolleyMultipart(Request.Method.POST, "https://funworkshop603.000webhostapp.com/chat_app/profile/set.php", new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        pd.dismiss();
                        try {
                            msg(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        msg("Error try again...!");
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>pems=new Hashtable<>();
                        pems.put("ct","n");
                        pems.put("n",number);
                        return pems;
                    }
                    @Override
                    protected Map<String, VolleyMultipart.DataPart> getByteData() throws AuthFailureError {
                        Map<String, VolleyMultipart.DataPart> params = new HashMap<>();
                        params.put("file",new DataPart(filename,imgdata));
                        return params;
                    }
                };
                vm.setShouldCache(false);
                RequestQueue vq=Volley.newRequestQueue(profilechange.this);
                vq.add(vm);
                vq.getCache().clear();

            }
            catch (FileNotFoundException e)
            {
                msg("Error for access file");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
        else
        {
            msg("Please select a Image");
        }
    }

    byte[] getByte(Bitmap bitmap)
    {
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
        return b.toByteArray();
    }

    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
