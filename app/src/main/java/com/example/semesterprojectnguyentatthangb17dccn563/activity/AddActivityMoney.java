package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddActivityMoney extends AppCompatActivity {
    private EditText money, description;
    private TextView tvDate;
    private Spinner spType, spUseType;
    private Integer num = new Random().nextInt();
    private String key = Integer.toString(num);
    private Button btnAdd, btnBack, btnGetDate;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        init();

        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AddActivityMoney.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvDate.setText(dayOfMonth+"/"+(month+1)+"/"+(year));
                    }
                },y,m,d);
                dpd.show();
            }
        });
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = parent.getItemAtPosition(position).toString();
                System.out.println("Type dayyyyyyyyyyyyyy " + itemSelected );
                if(itemSelected.equals("Tiền thu")){
                    ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(AddActivityMoney.this, R.array.incomeOpt, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUseType.setAdapter(adapter);
                }
                if(itemSelected.equals("Tiền chi")){
                    ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(AddActivityMoney.this, R.array.spendingOpt, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUseType.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(AddActivityMoney.this, R.array.incomeOpt, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spUseType.setAdapter(adapter);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    reference = FirebaseDatabase.getInstance().getReference()
                            .child("money").child("Money" + key);
                    Map<String, Object> data = new HashMap<>();
                    data.put("key", key);
                    data.put("money", money.getText().toString());
                    data.put("date", tvDate.getText().toString());
                    data.put("type", spType.getSelectedItem().toString());
                    data.put("useType", spUseType.getSelectedItem().toString());
                    data.put("description", description.getText().toString());
                    data.put("email", email);
                    reference.setValue(data);
                    String [] time_spilt=tvDate.getText().toString().split("/");
                    int date=Integer.parseInt(time_spilt[0]);
                    int month=Integer.parseInt(time_spilt[1])-1;
                    int year=Integer.parseInt(time_spilt[2]);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(year,month,date);

                    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(AddActivityMoney.this, AddRecevier.class);
                    intent.putExtra("myAction", "mDoNotify");
                    intent.putExtra("money", money.getText().toString());
                    intent.putExtra("date", tvDate.getText().toString());
                    intent.putExtra("type", spType.getSelectedItem().toString());
                    intent.putExtra("useType", spUseType.getSelectedItem().toString());
                    intent.putExtra("description", description.getText().toString());
                    intent.putExtra("email", email);

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddActivityMoney.this,
                            0, intent, 0);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    finish();
                }

            }
        });
    }
    public void init(){
        money = findViewById(R.id.edtMoneyAddMoney);
        description = findViewById(R.id.edtDescriptionAddMoney);
        tvDate = findViewById(R.id.tvDateAddMoney);
        spType = findViewById(R.id.spTypeAdd);
        spUseType = findViewById(R.id.spUseTypeAdd);
        btnAdd = findViewById(R.id.btnAddAddMoney);
        btnBack = findViewById(R.id.btnBackAddMoney);
        btnGetDate = findViewById(R.id.btnGetDateAddMoney);
    }
}