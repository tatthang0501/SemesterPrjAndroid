package com.example.semesterprojectnguyentatthangb17dccn563.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.model.StaticMoneyModel;

import java.util.ArrayList;
import java.util.List;

public class CircleStaticAdapter extends RecyclerView.Adapter<CircleStaticAdapter.StaticViewHolder>{
    Context context;
    List<StaticMoneyModel> listMoney;

    public CircleStaticAdapter(Context context) {
        this.context = context;
        listMoney = new ArrayList<>();
    }

    public List<StaticMoneyModel> getListMoney() {
        return listMoney;
    }

    public void setListMoney(List<StaticMoneyModel> listMoney) {
        this.listMoney = listMoney;
    }

    @NonNull
    @Override
    public StaticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.cardmoneybycategory,parent,false);
        StaticViewHolder ivh = new StaticViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull StaticViewHolder holder, int position) {
        StaticMoneyModel mi = listMoney.get(position);
        holder.type.setText(mi.getType());
        holder.money.setText(mi.getMoney()+"");
    }

    @Override
    public int getItemCount() {
        return listMoney.size();
    }

    public class  StaticViewHolder extends RecyclerView.ViewHolder{
        TextView type, money;
        public StaticViewHolder(@NonNull View itemView) {
            super(itemView);
            money = itemView.findViewById(R.id.tvCardMoneyCategory);
            type = itemView.findViewById(R.id.tvCardTypeCategory);
        }
    }
}