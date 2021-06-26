package com.example.semesterprojectnguyentatthangb17dccn563.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
        System.out.println(getItemCount() + " So luong day aaaaaa");
        String type = mi.getType();
        if(type.equals("Tiền chi")){
            holder.money.setTextColor(Color.parseColor("#FFFF4444"));
            holder.money.setText( "-" + mi.getMoney() + "");
        }
        if(type.equals("Tiền thu")){
            holder.money.setTextColor(Color.parseColor("#FF33B5E5"));
            holder.money.setText("+" + mi.getMoney());
        }

        holder.type.setText(mi.getUseType());
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
        TextView type, description, money;
        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            money = itemView.findViewById(R.id.tvCardMoney);
            type = itemView.findViewById(R.id.tvCardType);
            description = itemView.findViewById(R.id.tvCardDes);
        }
    }
}
