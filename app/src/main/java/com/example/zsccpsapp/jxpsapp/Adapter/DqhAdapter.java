package com.example.zsccpsapp.jxpsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zsccpsapp.jxpsapp.Entity.DqdEntity;
import com.example.zsccpsapp.jxpsapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 99zan on 2018/2/11.
 */

public class DqhAdapter extends BaseAdapter {

    private Context context;
    private List<DqdEntity> data = new ArrayList<>();
    private LayoutInflater inflater;

    public DqhAdapter(Context context, List<DqdEntity> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_orderitemnew, null);
            holder.createtime = convertView.findViewById(R.id.creattime);
            holder.dotime = convertView.findViewById(R.id.dotime);
            holder.shopname = convertView.findViewById(R.id.shopname);
            holder.run = convertView.findViewById(R.id.run);
            holder.shopaddress = convertView.findViewById(R.id.shopaddress);
            holder.disshop = convertView.findViewById(R.id.disshop);
            holder.take = convertView.findViewById(R.id.take);
            holder.buyeraddress = convertView.findViewById(R.id.buyeraddress);
            holder.discus = convertView.findViewById(R.id.discus);
            holder.send = convertView.findViewById(R.id.send);
            holder.sure = convertView.findViewById(R.id.sure);
            holder.sure2 = convertView.findViewById(R.id.sure2);
            holder.sure3 = convertView.findViewById(R.id.sure3);
            holder.sure4 = convertView.findViewById(R.id.sure4);
            holder.sure5 = convertView.findViewById(R.id.sure5);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sure.setVisibility(View.GONE);
        holder.sure2.setVisibility(View.VISIBLE);
        holder.sure3.setVisibility(View.VISIBLE);

        holder.sure2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(2, position);
            }
        });

        holder.sure3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(3, position);
            }
        });

        return convertView;
    }

    private class ViewHolder{
        TextView createtime;
        TextView dotime;
        TextView shopname;
        TextView run;
        TextView shopaddress;
        TextView disshop;
        TextView take;
        TextView buyeraddress;
        TextView discus;
        TextView send;
        TextView sure;
        TextView sure2;
        TextView sure3;
        TextView sure4;
        TextView sure5;
    }

    public interface OnItemClickListener {

        void onClick(int type, int i);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){

        this.onItemClickListener = onItemClickListener;

    }

}
