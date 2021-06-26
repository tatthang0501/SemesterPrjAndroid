package com.example.semesterprojectnguyentatthangb17dccn563.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.activity.AddActivityMoney;
import com.example.semesterprojectnguyentatthangb17dccn563.activity.StaticMoney;
import com.example.semesterprojectnguyentatthangb17dccn563.adapter.RecyclerMoneyAdapter;
import com.example.semesterprojectnguyentatthangb17dccn563.mailSender.MailSender;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyFragment extends Fragment {
    private RecyclerMoneyAdapter adapter;
    private RecyclerView rev;
    private Button btnGetAll, btnAdd;
    private TextView incomeMoney, spendMoney, balanceMoney, date, money;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    List<Money> listMoney;
    private CustomCalendar cc;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoneyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMoneyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoneyFragment newInstance(String param1, String param2) {
        MoneyFragment fragment = new MoneyFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_money, container, false);
        cc = v.findViewById(R.id.customCal);
        incomeMoney = v.findViewById(R.id.incomeMoney);
        spendMoney = v.findViewById(R.id.spendMoney);
        balanceMoney = v.findViewById(R.id.balanceMoney);
        date = v.findViewById(R.id.tvDayMoneyFrag);
        money = v.findViewById(R.id.tvMoneyMoneyFrag);
        rev = v.findViewById(R.id.revViewMoneyFrag);
        btnGetAll = v.findViewById(R.id.btnGetAll);
        btnAdd = v.findViewById(R.id.btnAddFragMoney);
        adapter = new RecyclerMoneyAdapter(this.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false);
        rev.setLayoutManager(manager);
        rev.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("money");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMoney = new ArrayList<Money>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = "";
                if(user != null){
                    email = user.getEmail();
                }
                float total = 0;
                float income = 0;
                float spend = 0;
                for(DataSnapshot item : snapshot.getChildren()){
                    if(item.child("email").getValue().toString().equals(email)){
                        Money money = item.getValue(Money.class);
                        float temp = Float.parseFloat(money.getMoney());
                        if(money.getType().equals("Tiền chi")){
                            spend += temp;
                            temp = -temp;
                        }
                        if(money.getType().equals("Tiền thu")){
                            income += temp;
                        }
                        total += temp;
                        listMoney.add(money);

                    }
                }
                date.setText("TẤT CẢ CÁC NGÀY");
                btnGetAll.setEnabled(false);
                money.setText("");
                if(total <= 0){
                    balanceMoney.setTextColor(Color.parseColor("#FFFF4444"));
                    balanceMoney.setText(total+"");
                }
                if(total > 0){
                    balanceMoney.setTextColor(Color.parseColor("#FF33B5E5"));
                    balanceMoney.setText("+" + total);
                }
                incomeMoney.setText("+" + income);
                spendMoney.setText("-" + spend);
                adapter.setListMoney(listMoney);
                rev.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        HashMap<Integer, Object> dateHashMap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        dateHashMap.put(calendar.get(Calendar.DAY_OF_MONTH),"current");
        cc.setDate(calendar, dateHashMap);
        cc.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String sDate = selectedDate.get(Calendar.DAY_OF_MONTH) + "/" + (selectedDate.get(Calendar.MONTH) + 1) + "/" + selectedDate.get(Calendar.YEAR);
                Toast.makeText(getContext(),"Xem danh sách thu chi ngày " + sDate, Toast.LENGTH_SHORT).show();
                date.setText(sDate);
                float total = 0;
                ArrayList<Money> listM = new ArrayList<>();
                for(Money money: listMoney){
                    if(money.getDate().equals(sDate)){
                        listM.add(money);
                        float temp = Float.parseFloat(money.getMoney());
                        if(money.getType().equals("Tiền chi")) temp = -temp;
                        total += temp;
                    }
                }
                if(total <= 0){
                    money.setTextColor(Color.parseColor("#FFFF4444"));
                    money.setText(total + "");
                }
                if(total > 0){
                    money.setTextColor(Color.parseColor("#FF33B5E5"));
                    money.setText("+" + total);
                }
                btnGetAll.setEnabled(true);
                adapter.setListMoney(listM);
                rev.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        btnGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference().child("money");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listMoney = new ArrayList<Money>();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = "";
                        if(user != null){
                            email = user.getEmail();
                        }
                        float total = 0;
                        float income = 0;
                        float spend = 0;
                        for(DataSnapshot item : snapshot.getChildren()){
                            if(item.child("email").getValue().toString().equals(email)){
                                Money money = item.getValue(Money.class);
                                float temp = Float.parseFloat(money.getMoney());
                                if(money.getType().equals("Tiền chi")){
                                    spend += temp;
                                    temp = -temp;
                                }
                                if(money.getType().equals("Tiền thu")){
                                    income += temp;
                                }
                                total += temp;
                                listMoney.add(money);

                            }
                        }
                        date.setText("TẤT CẢ CÁC NGÀY");
                        money.setText("");
                        if(total <= 0){
                            balanceMoney.setTextColor(Color.parseColor("#FFFF4444"));
                            balanceMoney.setText(total+"");
                        }
                        if(total > 0){
                            balanceMoney.setTextColor(Color.parseColor("#FF33B5E5"));
                            balanceMoney.setText("+" + total);
                        }
                        incomeMoney.setText("+" + income);
                        spendMoney.setText("-" + spend);
                        adapter.setListMoney(listMoney);
                        rev.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        btnGetAll.setEnabled(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddActivityMoney.class);
                startActivity(intent);
            }
        });
        return  v;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}