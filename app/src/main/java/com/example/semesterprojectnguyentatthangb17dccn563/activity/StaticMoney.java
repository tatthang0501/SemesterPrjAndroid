package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;

import java.util.ArrayList;
import java.util.List;

public class StaticMoney extends AppCompatActivity {
    private AnyChartView anyChartView;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_money);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        ArrayList<Money> listIncome = (ArrayList<Money>) intent.getSerializableExtra("money");
        ArrayList<String> listType = new ArrayList<>();
        ArrayList<Integer> listMoney = new ArrayList<>();
        for(Money mi : listIncome){
            if(!listType.contains(mi.getUseType())){
                listType.add(mi.getUseType());
            }
        }
        for(String s : listType){
            listMoney.add(0);
        }
        for(Money mi : listIncome){
            if(listType.contains(mi.getUseType())){
                int temp = listMoney.get(listType.indexOf(mi.getUseType()));
                temp += Integer.parseInt(mi.getMoney());
                listMoney.set(listType.indexOf(mi.getUseType()),temp);
            }
        }

        anyChartView = findViewById(R.id.incomeChartView);
        btnBack = findViewById(R.id.btnStaticIncomeBack);
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i = 0; i < listMoney.size(); i++){
            dataEntries.add(new ValueDataEntry(listType.get(i), listMoney.get(i)));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}