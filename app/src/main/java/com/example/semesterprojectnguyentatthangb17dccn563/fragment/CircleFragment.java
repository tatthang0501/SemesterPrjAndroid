package com.example.semesterprojectnguyentatthangb17dccn563.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.activity.MonthlyCompareActivity;
import com.example.semesterprojectnguyentatthangb17dccn563.activity.StaticMoney;
import com.example.semesterprojectnguyentatthangb17dccn563.adapter.CircleStaticAdapter;
import com.example.semesterprojectnguyentatthangb17dccn563.mailSender.MailSender;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;
import com.example.semesterprojectnguyentatthangb17dccn563.model.StaticMoneyModel;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CircleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CircleFragment extends Fragment {
    private NumberPicker monthPicker, yearPicker;
    private Button btnView, btnSendEmail;
    private AnyChartView chart;
    private RecyclerView rev;
    private CircleStaticAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private Spinner spType;
    private List<Money> listMoneyRead;
    private static final int MAX_YEAR = 2030;
    private static final int MIN_YEAR = 2015;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CircleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CircleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CircleFragment newInstance(String param1, String param2) {
        CircleFragment fragment = new CircleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v =  inflater.inflate(R.layout.fragment_circle, container, false);
        monthPicker = v.findViewById(R.id.month_picker);
        yearPicker = v.findViewById(R.id.year_picker);
        btnView = v.findViewById(R.id.btnViewStatic);
        btnSendEmail = v.findViewById(R.id.btnSendEmail);
        spType = v.findViewById(R.id.spTypeCircle);
        chart = v.findViewById(R.id.staticChartView);
        rev = v.findViewById(R.id.revCircle);
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false);
        adapter = new CircleStaticAdapter(this.getContext());
        Calendar cal = Calendar.getInstance();
        rev.setLayoutManager(manager);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(cal.get(Calendar.MONTH));

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(year);
        yearPicker.setMaxValue(MAX_YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setValue(year);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("money");
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), monthPicker.getValue() + "/" + yearPicker.getValue(), Toast.LENGTH_SHORT).show();
                readData(new FirebaseCallback() {
                    @Override
                    public void onCallBack(List<Money> listMoneyRead) {
                        ArrayList<Money> listMoneyReadIn = new ArrayList<>();
                        String type = spType.getSelectedItem().toString();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String chosenDate = monthPicker.getValue() + "/" + yearPicker.getValue();
                        for(Money m : listMoneyRead){
                            Date date = null;
                            try {
                                date = dateFormat.parse(m.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int month = date.getMonth() + 1;
                            int year = date.getYear() + 1900;
                            String moneyDate = month + "/" + year;
                            if(m.getType().equals(type) && moneyDate.equals(chosenDate)){
                                listMoneyReadIn.add(m);
                            }
                        }
                        ArrayList<String> listType = new ArrayList<>();
                        ArrayList<Integer> listMoney = new ArrayList<>();
                        ArrayList<StaticMoneyModel> listStaticMoney = new ArrayList<>();
                        for(Money mi : listMoneyReadIn){
                            if(!listType.contains(mi.getUseType())){
                                listType.add(mi.getUseType());
                            }
                        }
                        for(String s : listType){
                            listMoney.add(0);
                        }
                        for(Money mi : listMoneyReadIn){
                            if(listType.contains(mi.getUseType())){
                                int temp = listMoney.get(listType.indexOf(mi.getUseType()));
                                temp += Integer.parseInt(mi.getMoney());
                                listMoney.set(listType.indexOf(mi.getUseType()),temp);
                            }
                        }
                        List<DataEntry> dataEntries = new ArrayList<>();
                        for(int i = 0; i < listMoney.size(); i++){
                            StaticMoneyModel sm = new StaticMoneyModel();
                            sm.setMoney(listMoney.get(i));
                            sm.setType(listType.get(i));
                            listStaticMoney.add(sm);
                            dataEntries.add(new ValueDataEntry(listType.get(i), listMoney.get(i)));
                        }
                        System.out.println(dataEntries.size() + " so luong data");
                        Pie pie;
                        pie = AnyChart.pie();
                        pie.data(dataEntries);
                        chart.invalidate();
                        chart.setChart(pie);

                        adapter.setListMoney(listStaticMoney);
                        rev.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    Multipart m = new MimeMultipart();
                    List<Money> listSpending = new ArrayList<Money>();
                    List<Money> listIncoming = new ArrayList<Money>();
                    Calendar curDate = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String tempDate = dateFormat.format(curDate.getTime());
                    float total = 0;
                    String curDateString = "";
                    for(int i = 0; i < tempDate.length(); i++){
                        if(tempDate.charAt(i) == '0' && i == 0 || tempDate.charAt(i) == '0' && i == 3){
                            curDateString+="";
                        }
                        else curDateString += tempDate.charAt(i);
                    }
                    String incoming = "";
                    incoming+="Danh sách: \n";
                    for(StaticMoneyModel mo : adapter.getListMoney()){
                        total += mo.getMoney();
                        incoming += "\t" + mo.getType() + ": " + mo.getMoney() +"\n";
                    }
                    incoming+="Tổng: " + (int) total;
                    attachmentPart.setText(incoming);
                    m.addBodyPart(attachmentPart);

                    sendEmail(" " + spType.getSelectedItem().toString() + " tháng " + monthPicker.getValue() + "/" + yearPicker.getValue(),m);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    private void readData(CircleFragment.FirebaseCallback firebaseCallback){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMoneyRead = new ArrayList<Money>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = "";
                if(user != null){
                    email = user.getEmail();
                }
                for(DataSnapshot item : snapshot.getChildren()){
                    if(item.child("email").getValue().toString().equals(email)){
                        Money money = item.getValue(Money.class);
                        listMoneyRead.add(money);

                    }
                }
                firebaseCallback.onCallBack(listMoneyRead);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private interface FirebaseCallback{
        void onCallBack(List<Money> listMoney);
    }

    private void sendEmail(final String staticType, final Multipart attachment)
    {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    MailSender sender = new MailSender();
                    sender.sendMail("Báo cáo"+ staticType, attachment, sender.getUser(), email);
                    createSentToast();

                } catch (Exception e) {
                    createFailedToast();
                    Log.e("Sending email process", e.getMessage(), e);
                }
            }

        }).start();
    }
    private void createSentToast(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext() , "Gửi báo cáo qua email thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void createFailedToast(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), "Gửi báo cáo không thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}