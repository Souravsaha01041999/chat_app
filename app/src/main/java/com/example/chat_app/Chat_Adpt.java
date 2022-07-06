package com.example.chat_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Chat_Adpt extends RecyclerView.Adapter<Chat_Adpt.MyHolder>
{
    List<ChatDet>data;
    Context context;
    String my_no;
    Chat_Adpt(Context c,String mn)
    {
        this.context=c;
        data=new ArrayList<>();
        this.my_no=mn;
    }
    void set(ChatDet cd)
    {
        data.add(cd);
    }
    void add(ChatDet cd)
    {
        this.data.add(cd);
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType==1)
        {
            v= LayoutInflater.from(context).inflate(R.layout.chatfrmdsg,parent,false);
        }
        else {
            v= LayoutInflater.from(context).inflate(R.layout.chattodsg,parent,false);
        }
        return new MyHolder(v,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.tv.setText(data.get(position).getChat());
    }
    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getChatfrm().equals(my_no))
        {
            return 1;
        }
        else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        TextView tv;
        public MyHolder(@NonNull View itemView,int type)
        {
            super(itemView);
            if (type==1)
            {
                tv=itemView.findViewById(R.id.frmchat);
            }
            else {
                tv=itemView.findViewById(R.id.tochat);
            }
        }
    }
}
