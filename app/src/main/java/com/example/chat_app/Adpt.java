package com.example.chat_app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adpt extends RecyclerView.Adapter<Adpt.MyHolder>
{
    List<RcvData>data;
    Context context;
    Adpt(Context c)
    {
        this.context=c;
        data=new ArrayList<>();
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.design_lists,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.name.setText(data.get(position).getName());
        if (data.get(position).getSeen().equals("1"))
        {
            holder.seen.setText("");
        }
        else {
            holder.seen.setText("New");
        }
        Picasso.get().load(Uri.parse(data.get(position).getImage()))
                .error(R.drawable.errorfor)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(holder.cv);

        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context,chat_section.class).putExtra("chat_to_number",data.get(position).getNumber()));
            }
        });
    }
    void add(RcvData rd)
    {
        data.add(rd);
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout rl;
        TextView name,seen;
        CircleImageView cv;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            rl=itemView.findViewById(R.id.clicklist);
            name=itemView.findViewById(R.id.user_name);
            seen=itemView.findViewById(R.id.seen);
            cv=itemView.findViewById(R.id.profile);
        }
    }
}
