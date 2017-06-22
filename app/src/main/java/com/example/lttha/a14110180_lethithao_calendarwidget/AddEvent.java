package com.example.lttha.a14110180_lethithao_calendarwidget;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddEvent extends AppCompatActivity {
    AlertDialog.Builder dialog;
    TextView txtTime1, txtTime2,txtSave, txtDate1,txtDate2;
    Calendar mcurrentTime = Calendar.getInstance();

    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        if(ab!=null){
            ab.setDisplayHomeAsUpEnabled(true);
        }

        txtSave=(TextView) findViewById(R.id.txtSave);
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEvent.this.finish();
            }
        });
        txtTime1=(TextView) findViewById(R.id.txtTime1);
        txtTime2=(TextView) findViewById(R.id.txtTime2);
        txtDate1=(TextView) findViewById(R.id.txtDate1);
        txtDate2=(TextView) findViewById(R.id.txtDate2);
        sw=(Switch) findViewById(R.id.switchButton);
        sw.setChecked(false);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    txtTime1.setVisibility(View.INVISIBLE);
                    txtTime2.setVisibility(View.INVISIBLE);
                }
                else
                {
                    txtTime1.setVisibility(View.VISIBLE);
                    txtTime2.setVisibility(View.VISIBLE);
                }

            }
        });

        dialog = new AlertDialog.Builder(AddEvent.this);
        dialog.setCancelable(true);

        txtTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Nếu giờ chọn nhỏ hơn 11 thì sẽ hiển thi text của btn là AM
                        if(selectedHour<11){
                            txtTime1.setText(selectedHour + ":" + selectedMinute + " AM");
                        }
                        else
                            txtTime1.setText(selectedHour + ":" + selectedMinute + " PM");

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set time");
                mTimePicker.show();

            }
        });

        txtTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddEvent.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //Nếu giờ chọn nhỏ hơn 11 thì sẽ hiển thi text của btn là AM
                        if(selectedHour<11){
                            txtTime2.setText(selectedHour + ":" + selectedMinute + " AM");
                        }
                        else
                            txtTime2.setText(selectedHour + ":" + selectedMinute + " PM");

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Set time");
                mTimePicker.show();

            }
        });

        txtDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog date = new DatePickerDialog(AddEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //Gán ngày tháng năm lên EditText
                                txtDate1.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        },
                        //Định dạng ngày tháng năm
                        mcurrentTime.get(Calendar.YEAR),
                        mcurrentTime.get(Calendar.MONTH),
                        mcurrentTime.get(Calendar.DAY_OF_MONTH));
                date.show();
            }
        });

        txtDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog date = new DatePickerDialog(AddEvent.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //Gán ngày tháng năm lên EditText
                                txtDate2.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        },
                        //Định dạng ngày tháng năm
                        mcurrentTime.get(Calendar.YEAR),
                        mcurrentTime.get(Calendar.MONTH),
                        mcurrentTime.get(Calendar.DAY_OF_MONTH));
                date.show();
            }
        });

    }


}
