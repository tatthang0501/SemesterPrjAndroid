package com.example.semesterprojectnguyentatthangb17dccn563.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoneyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoneyFragment extends Fragment {
    private RecyclerMoneyAdapter adapter;
    private RecyclerView rev;
    private FloatingActionButton fabMoney;
    private TextView totalMoney, dateFrom, dateTo;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private Button btnGetDateFrom, btnGetDateTo, btnFilter, btnGetAll, btnViewStatic, btnSendEmail;
    private Spinner spMoneyType, spTimeType;
    List<Money> listMoney;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_money, container, false);
        rev = v.findViewById(R.id.revMoney);
        fabMoney = v.findViewById(R.id.fabMoney);
        totalMoney = v.findViewById(R.id.totalMoney);
        dateFrom = v.findViewById(R.id.tvDateFromMoney);
        dateTo = v.findViewById(R.id.tvDateToMoney);
        btnGetDateFrom = v.findViewById(R.id.btnGetDateFromMoney);
        btnGetDateTo = v.findViewById(R.id.btnGetDateToMoney);
        btnFilter = v.findViewById(R.id.btnMoneyFilter);
        btnSendEmail = v.findViewById(R.id.btnMoneySendEmail);
        spMoneyType = v.findViewById(R.id.spMoneyTypeStatic);
        spTimeType = v.findViewById(R.id.spTimeTypeStatic);
        btnViewStatic = v.findViewById(R.id.btnMoneyViewStatic);
        btnGetAll = v.findViewById(R.id.btnMoneyGetAll);
        adapter = new RecyclerMoneyAdapter(this.getContext());
        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false);
        rev.setLayoutManager(manager);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("money");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMoney = new ArrayList<Money>();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String email = user.getEmail();
                float total = 0;
                for(DataSnapshot item : snapshot.getChildren()){
                    if(item.child("email").getValue().toString().equals(email)){
                        Money money = item.getValue(Money.class);
                        float temp = Float.parseFloat(money.getMoney());
                        if(money.getType().equals("Tiền chi")) temp = -temp;
                        total += temp;
                        System.out.println("Tien day " + total);
                        listMoney.add(money);

                    }
                }
                totalMoney.setText("Tổng tiền: " + total);
                adapter.setListMoney(listMoney);
                rev.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        String email = user.getEmail();
                        float total = 0;
                        for(DataSnapshot item : snapshot.getChildren()){
                            if(item.child("email").getValue().toString().equals(email)){
                                Money money = item.getValue(Money.class);
                                float temp = Float.parseFloat(money.getMoney());
                                if(money.getType().equals("Tiền chi")) temp = -temp;
                                total += temp;
                                System.out.println("Tien day " + total);
                                listMoney.add(money);

                            }
                        }
                        totalMoney.setText("Tổng tiền đã thu: " + total);
                        adapter.setListMoney(listMoney);
                        rev.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        fabMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddActivityMoney.class);
                startActivity(intent);
            }
        });

        btnGetDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateTo.setText(dayOfMonth+"/"+(month+1)+"/"+(year));
                    }
                },y,m,d);
                dpd.show();
            }
        });

        btnGetDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateFrom.setText(dayOfMonth+"/"+(month+1)+"/"+(year));
                    }
                },y,m,d);
                dpd.show();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dFrom = dateFrom.getText().toString();
                String dTo = dateTo.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dateF = format.parse(dFrom);
                    Date dateT = format.parse(dTo);
                    if(dateF.after(dateT)){
                        Toast.makeText(getContext(), "Date (from) must be before date (to)!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<Money> listFilter = new ArrayList<>();
                    for(Money mi : listMoney){
                        Date temp = format.parse(mi.getDate());
                        if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                            listFilter.add(mi);
                        }
                    }
                    adapter.setListMoney(listFilter);
                    rev.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        btnViewStatic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Thống kế theo dữ liệu thổng hợp thu + chi
                if(spMoneyType.getSelectedItem().toString().equals("Tổng hợp") && spTimeType.getSelectedItem().toString().equals("Theo ngày")){
                    List<Money> listMoneySend = new ArrayList<>();
                    try{
                    Calendar curDate = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String tempDate = dateFormat.format(curDate.getTime());
                    String curDateString = "";
                    if(tempDate.charAt(0) == '0'){
                        for(int i = 1; i < tempDate.length(); i++){
                            curDateString+=tempDate.charAt(i);
                        }
                    }
                    else{
                        curDateString = tempDate;
                    }
                        System.out.println(curDateString);
                    for(Money m : listMoney) {
                        Date temp = dateFormat.parse(m.getDate());
                        System.out.println("MoneyDate dayyyy " + temp);
                        if (m.getDate().equals(curDateString)) {
                            listMoneySend.add(m);
                            }
                        }
                        System.out.println("List size dayyyy bro buh "+ listMoneySend.size());
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoney);
                        startActivity(intent);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else if(spMoneyType.getSelectedItem().toString().equals("Tổng hợp") && spTimeType.getSelectedItem().toString().equals("Theo tuần")){
                    Calendar curDate = Calendar.getInstance();
                    curDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateF = null;
                    Date dateT = null;
                    try {
                        dateF = dateFormat.parse(dateFormat.format(curDate.getTime()));
                        for(int i = 0; i <= 6; i++){
                            curDate.add(Calendar.DATE, 1);
                        }
                        dateT = dateFormat.parse(dateFormat.format(curDate.getTime()));
                        List<Money> listMoneySend = new ArrayList<>();
                        for(Money m : listMoney){
                            Date temp = dateFormat.parse(m.getDate());
                            if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                    || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                                listMoneySend.add(m);
                            }
                        }
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoneySend);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                else if(spMoneyType.getSelectedItem().toString().equals("Tổng hợp") && spTimeType.getSelectedItem().toString().equals("Theo tháng")){

                    try {
                        Calendar c = Calendar.getInstance();
                        String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        c.setTime(dateFormat.parse(date));
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                        Date dateT = c.getTime();
                        System.out.println(dateT);
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date dateF = c.getTime();
                        System.out.println(dateF);
                        List<Money> listMoneySend = new ArrayList<>();
                        for(Money m : listMoney){
                            Date temp = dateFormat.parse(m.getDate());
                            if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                    || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                                listMoneySend.add(m);
                            }
                        }
                        Toast.makeText(getContext(), "Listsize day " + listMoneySend.size(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoneySend);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //Thống kê theo dữ liệu chi
                else if(spMoneyType.getSelectedItem().toString().equals("Tiền chi") && spTimeType.getSelectedItem().toString().equals("Theo ngày")){
                    List<Money> listMoneySend = new ArrayList<>();
                    try{
                        Calendar curDate = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String tempDate = dateFormat.format(curDate.getTime());
                        String curDateString = "";
                        if(tempDate.charAt(0) == '0'){
                            for(int i = 1; i < tempDate.length(); i++){
                                curDateString+=tempDate.charAt(i);
                            }
                        }
                        else{
                            curDateString = tempDate;
                        }
                        for(Money m : listMoney) {
                            Date temp = dateFormat.parse(m.getDate());
                            if (m.getDate().equals(curDateString) && m.getType().equals("Tiền chi")) {
                                listMoneySend.add(m);
                            }
                        }
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoney);
                        startActivity(intent);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(spMoneyType.getSelectedItem().toString().equals("Tiền chi") && spTimeType.getSelectedItem().toString().equals("Theo tuần")){
                    Calendar curDate = Calendar.getInstance();
                    curDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateF = null;
                    Date dateT = null;
                    try {
                        dateF = dateFormat.parse(dateFormat.format(curDate.getTime()));
                        for(int i = 0; i <= 6; i++){
                            curDate.add(Calendar.DATE, 1);
                        }
                        dateT = dateFormat.parse(dateFormat.format(curDate.getTime()));
                        List<Money> listMoneySend = new ArrayList<>();
                        for(Money m : listMoney){
                            Date temp = dateFormat.parse(m.getDate());
                            if(m.getType() == "Tiền chi"){
                                if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                        || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                                    listMoneySend.add(m);
                                }
                            }
                        }
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoneySend);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                else if(spMoneyType.getSelectedItem().toString().equals("Tiền chi") && spTimeType.getSelectedItem().toString().equals("Theo tháng")){

                    try {
                        Calendar c = Calendar.getInstance();
                        String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        c.setTime(dateFormat.parse(date));
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                        Date dateT = c.getTime();
                        System.out.println(dateT);
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date dateF = c.getTime();
                        System.out.println(dateF);
                        List<Money> listMoneySend = new ArrayList<>();
                        for(Money m : listMoney){
                            Date temp = dateFormat.parse(m.getDate());
                            if(m.getType().equals("Tiền chi")){
                                if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                        || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                                    listMoneySend.add(m);
                                }
                            }
                        }
                        Toast.makeText(getContext(), "Listsize day " + listMoneySend.size(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoneySend);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //Thống kê dữ liệu thu
                else if(spMoneyType.getSelectedItem().toString().equals("Tiền thu") && spTimeType.getSelectedItem().toString().equals("Theo ngày")){
                    List<Money> listMoneySend = new ArrayList<>();
                    try{
                        Calendar curDate = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String tempDate = dateFormat.format(curDate.getTime());
                        String curDateString = "";
                        if(tempDate.charAt(0) == '0'){
                            for(int i = 1; i < tempDate.length(); i++){
                                curDateString+=tempDate.charAt(i);
                            }
                        }
                        else{
                            curDateString = tempDate;
                        }
                        for(Money m : listMoney) {
                            Date temp = dateFormat.parse(m.getDate());
                            if (m.getDate().equals(curDateString) && m.getType().equals("Tiền thu")) {
                                listMoneySend.add(m);
                            }
                        }
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoney);
                        startActivity(intent);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(spMoneyType.getSelectedItem().toString().equals("Tiền thu") && spTimeType.getSelectedItem().toString().equals("Theo tuần")){
                    Calendar curDate = Calendar.getInstance();
                    curDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateF = null;
                    Date dateT = null;
                    try {
                        dateF = dateFormat.parse(dateFormat.format(curDate.getTime()));
                        for(int i = 0; i <= 6; i++){
                            curDate.add(Calendar.DATE, 1);
                        }
                        dateT = dateFormat.parse(dateFormat.format(curDate.getTime()));
                        List<Money> listMoneySend = new ArrayList<>();
                        for(Money m : listMoney){
                            Date temp = dateFormat.parse(m.getDate());
                            if(m.getType() == "Tiền thu"){
                                if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                        || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                                    listMoneySend.add(m);
                                }
                            }
                        }
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoneySend);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                else if(spMoneyType.getSelectedItem().toString().equals("Tiền thu") && spTimeType.getSelectedItem().toString().equals("Theo tháng")){

                    try {
                        Calendar c = Calendar.getInstance();
                        String date = c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        c.setTime(dateFormat.parse(date));
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
                        Date dateT = c.getTime();
                        System.out.println(dateT);
                        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
                        Date dateF = c.getTime();
                        System.out.println(dateF);
                        List<Money> listMoneySend = new ArrayList<>();
                        for(Money m : listMoney){
                            Date temp = dateFormat.parse(m.getDate());
                            if(m.getType().equals("Tiền thu")){
                                if((temp.after(dateF) && temp.before(dateT)) || (temp.after(dateF) && temp.equals(dateT))
                                        || (temp.equals(dateF) && temp.equals(dateT)) || (temp.equals(dateF) && temp.before(dateT))){
                                    listMoneySend.add(m);
                                }
                            }
                        }
                        Toast.makeText(getContext(), "Listsize day " + listMoneySend.size(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), StaticMoney.class);
                        intent.putExtra("money", (Serializable) listMoneySend);
                        startActivity(intent);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}