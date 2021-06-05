package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;
import com.example.semesterprojectnguyentatthangb17dccn563.model.MonthlyMoney;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthlyCompareActivity extends AppCompatActivity {
    private Spinner spCompareType, spCompareYear;
    private Button btnCreate;
    private BarChart compareChart;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private List<Money> listMoney;
    private ArrayList<String> lableMonth;
    private ArrayList<BarEntry> barEntryArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_compare);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        init();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("money");
        readData(new FirebaseCallback() {
            @Override
            public void onCallBack(List<Money> listMoney) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                ArrayList<String> listYearAL = new ArrayList<>();
                for(Money m : listMoney){
                    try {
                        Date temp = dateFormat.parse(m.getDate());
                        String year = Integer.toString((temp.getYear() + 1900));
                        if(!listYearAL.contains(year)) listYearAL.add(year);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonthlyCompareActivity.this, android.R.layout.simple_spinner_item, listYearAL);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spCompareYear.setAdapter(adapter);
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readData(new FirebaseCallback() {
                    @Override
                    public void onCallBack(List<Money> listMoney) {
                        ArrayList<MonthlyMoney> listMonthlyMoney = new ArrayList<>();
                        ArrayList<Money> listChart = new ArrayList<>();
                        String type = spCompareType.getSelectedItem().toString();
                        int year = Integer.parseInt(spCompareYear.getSelectedItem().toString());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        for(Money m : listMoney){
                            try {
                                Date temp = dateFormat.parse(m.getDate());
                                if(temp.getYear()+1900 == year && m.getType().equals(type)) listChart.add(m);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        for(int i = 1; i <= 12; i++){
                            MonthlyMoney mm = new MonthlyMoney();
                            mm.setMoney(0);
                            mm.setMonth("T" + i);
                            listMonthlyMoney.add(mm);
                        }
                        for(Money m : listChart){
                            try {
                                Date moneyDate = dateFormat.parse(m.getDate());
                                int temp = moneyDate.getMonth() + 1;
                                MonthlyMoney mm = listMonthlyMoney.get(temp-1);
                                mm.setMoney(mm.getMoney() + Float.parseFloat(m.getMoney()));
                                listMonthlyMoney.set(temp-1, mm);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        lableMonth = new ArrayList<>();
                        barEntryArrayList = new ArrayList<>();
                        for (MonthlyMoney mm : listMonthlyMoney){
                            String month = mm.getMonth();
                            float money = mm.getMoney();
                            barEntryArrayList.add(new BarEntry(listMonthlyMoney.indexOf(mm),money));
                            lableMonth.add(month);
                        }
                        BarDataSet barDataSet = new BarDataSet(barEntryArrayList,type +" theo từng tháng");
                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        Description description = new Description();
                        description.setText("Tháng");
                        compareChart.setDescription(description);
                        BarData barData = new BarData(barDataSet);
                        barData.setValueTextSize(13);
                        barData.setBarWidth((float) 0.8);
                        compareChart.setData(barData);
                        XAxis xAxis = compareChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(lableMonth));
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setDrawAxisLine(false);
                        xAxis.setGranularity(1f);
                        xAxis.setTextSize(13);
                        xAxis.setLabelCount(lableMonth.size());
                        xAxis.setLabelRotationAngle(0);
                        compareChart.animateY(2000);
                        compareChart.invalidate();
                    }
                });
            }
        });

    }
    private void init(){
        spCompareType = findViewById(R.id.spCompareType);
        spCompareYear = findViewById(R.id.spCompareYear);
        btnCreate = findViewById(R.id.btnCreateCompare);
        compareChart = findViewById(R.id.compareBarChart);
    }
    private void readData(FirebaseCallback firebaseCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMoney = new ArrayList<Money>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = "";
                if(user != null){
                    email = user.getEmail();
                }
                        for(DataSnapshot item : snapshot.getChildren()){
                            if(item.child("email").getValue().toString().equals(email)){
                                Money money = item.getValue(Money.class);
                                listMoney.add(money);

                                }
                            }
                firebaseCallback.onCallBack(listMoney);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private interface FirebaseCallback{
        void onCallBack(List<Money> listMoney);
    }
}