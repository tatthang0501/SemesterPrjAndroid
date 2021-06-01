package com.example.semesterprojectnguyentatthangb17dccn563.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Money;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class EditActivityMoney extends AppCompatActivity {
    private EditText edtMoney, edtDes;
    private TextView tvDate;
    private Button btnGetDate, btnUpdate, btnDelete, btnBack;
    private Spinner spType, spUseType;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_money);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        init();

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = parent.getItemAtPosition(position).toString();
                System.out.println("Type dayyyyyyyyyyyyyy " + itemSelected );
                if(itemSelected.equals("Tiền thu")){
                    ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(EditActivityMoney.this, R.array.incomeOpt, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUseType.setAdapter(adapter);
                }
                if(itemSelected.equals("Tiền chi")){
                    ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(EditActivityMoney.this, R.array.spendingOpt, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spUseType.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(EditActivityMoney.this, R.array.incomeOpt, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spUseType.setAdapter(adapter);
            }
        });
        btnGetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(EditActivityMoney.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvDate.setText(dayOfMonth+"/"+(month+1)+"/"+(year));
                    }
                },y,m,d);
                dpd.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Intent intent = getIntent();
        if(intent.getSerializableExtra("money") != null){
            final Money money = (Money) intent.getSerializableExtra("money");
            edtMoney.setText(money.getMoney());
            edtDes.setText(money.getDescription());
            tvDate.setText(money.getDate());
            reference = FirebaseDatabase.getInstance().getReference().child("money")
                    .child("Money" + money.getKey());
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Money editMoney = new Money();
                    editMoney.setKey(money.getKey());
                    editMoney.setEmail(money.getEmail());
                    editMoney.setDate(tvDate.getText().toString());
                    editMoney.setDescription(edtDes.getText().toString());
                    editMoney.setType(spType.getSelectedItem().toString());
                    editMoney.setUseType(spUseType.getSelectedItem().toString());
                    editMoney.setMoney(edtMoney.getText().toString());
//                    Toast.makeText(EditActivityMoney.this, editIncome.getIncomeDate() + " "
//                            + editIncome.getIncomeMoney() + " " + editIncome.getEmail() + editIncome.getIncomeDescription(), Toast.LENGTH_SHORT).show();
                    reference.setValue(editMoney).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditActivityMoney.this, "Update success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditActivityMoney.this, "Failed to update value", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditActivityMoney.this, "Delete success", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditActivityMoney.this, "Delete failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    private void init(){
        edtMoney = findViewById(R.id.edtMoneyEditMoney);
        edtDes = findViewById(R.id.edtDescriptionEditMoney);
        tvDate = findViewById(R.id.tvDateEditMoney);
        btnGetDate = findViewById(R.id.btnGetDateEditMoney);
        btnUpdate = findViewById(R.id.btnUpdateEditMoney);
        btnDelete = findViewById(R.id.btnDeleteEditMoney);
        btnBack = findViewById(R.id.btnBackEditMoney);
        spType = findViewById(R.id.spTypeEdit);
        spUseType = findViewById(R.id.spUseTypeEdit);
    }
}