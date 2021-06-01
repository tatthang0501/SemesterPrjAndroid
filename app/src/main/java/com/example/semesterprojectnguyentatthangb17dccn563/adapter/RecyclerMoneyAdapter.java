package com.example.semesterprojectnguyentatthangb17dccn563.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.activity.EditActivityMoney;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;

import java.util.ArrayList;
import java.util.List;

public class RecyclerMoneyAdapter extends RecyclerView.Adapter<RecyclerMoneyAdapter.IncomeViewHolder>{
    Context context;
    List<Money> listMoney;

    public RecyclerMoneyAdapter(Context context) {
        this.context = context;
        listMoney = new ArrayList<>();
    }

    public List<Money> getListMoney() {
        return listMoney;
    }

    public void setListMoney(List<Money> listMoney) {
        this.listMoney = listMoney;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.moneycard,parent,false);
        IncomeViewHolder ivh = new IncomeViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Money mi = listMoney.get(position);
        holder.money.setText(mi.getMoney());
        holder.date.setText(mi.getDate());
        holder.type.setText(mi.getType());
        holder.useType.setText(mi.getUseType());
        holder.description.setText(mi.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivityMoney.class);
                intent.putExtra("money",mi);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMoney.size();
    }

    public class  IncomeViewHolder extends RecyclerView.ViewHolder{
        TextView money, date, type, useType, description;
        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            money = itemView.findViewById(R.id.tvCardMoney);
            date = itemView.findViewById(R.id.tvCardDate);
            type = itemView.findViewById(R.id.tvCardType);
            useType = itemView.findViewById(R.id.tvCardUseType);
            description = itemView.findViewById(R.id.tvCardDescription);
        }
    }
}
