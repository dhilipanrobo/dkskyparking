package com.skyparking.admin.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Date_select extends AppCompatActivity implements CalendarView.OnDateChangeListener,View.OnClickListener{
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    Button BTfrom,BTto,BTgo;
    EditText ETfrom,ETto;
    String STfromdt,STtodt;
    String stdate;
    String mdate;
    String alg,Rep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView ( R.layout.activity_date_select );
        BTfrom=findViewById ( R.id.button16 );
        BTto=findViewById ( R.id.button15);
        BTgo=findViewById ( R.id.button17);
        BTfrom.setOnClickListener ( this );
        BTto.setOnClickListener ( this );
        BTgo.setOnClickListener ( this );
        ETfrom=findViewById ( R.id.editText7 );
        ETto=findViewById ( R.id.editText6 );
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Rep=getIntent().getExtras().getString("report");
       // Toast.makeText ( this, "Rep"+Rep, Toast.LENGTH_SHORT ).show ( );
        //showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog (this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    public void setDate(View view) {
        showDialog(999);


    }


    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        //  Toast.makeText(getApplicationContext(),"date:"+dayOfMonth+":"+month+":"+year, Toast.LENGTH_SHORT).show();
        Intent v=new Intent(this,Date_select.class);
        int mon=month+1;
         stdate=String.format ( "%03d",mon);
        String date=dayOfMonth+"-"+stdate+"-"+year;
        v.putExtra("ref",stdate);

       // startActivity(v);
       // finish();

    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    date ( arg3,arg2+1,arg1 );
                    //showDate(arg1, arg2+1, arg3);
                }
            };
    void date(int day,int mon,int year){ Intent v=new Intent(this,Date_select.class);
        String stdatee=String.format ( "%02d",mon);
         mdate=day+"-"+stdatee+"-"+year;
         if (alg.equals ( "from" )){
        ETfrom.setText ( mdate.toString () );
         }
         else if(alg.equals ( "to" )){ ETto.setText ( mdate.toString () );}
        v.putExtra("ref",mdate);

        //startActivity(v);
       // finish();
        }

    @Override
    public void onClick(View view) {
        STfromdt=ETfrom.getText ().toString ();
        STtodt=ETto.getText ().toString ();

        int id = view.getId();
        if (id== R.id.button16){
            showDialog(999);
            alg="from";


        }


        else if(id== R.id.button15){

            showDialog(999);
      alg="to";

        }

        else if(id== R.id.button17){

            if(STfromdt.isEmpty ()){
                Toast.makeText ( this, "From Date Field Is Empty", Toast.LENGTH_SHORT ).show ( );
                ETfrom.requestFocus ();
            }
            else {

                if(STtodt.isEmpty ()){ Toast.makeText ( this, "TO Date Field Is Empty", Toast.LENGTH_SHORT ).show ( );
                    ETto.requestFocus ();}
                else { if(Rep.equals ( "cr" )){
                    Intent v=new Intent(this,Dailyreport.class);

                    v.putExtra("Date_from",ETfrom.getText ().toString ());
                    v.putExtra("Date_to",ETto.getText ().toString ());
                    startActivity(v);
                    finish ();
                }
                else if(Rep.equals ( "pr" )){
                    Intent v=new Intent(this,PendingVehicle.class);
                    v.putExtra("Date_from",ETfrom.getText ().toString ());
                    v.putExtra("Date_to",ETto.getText ().toString ());
                    startActivity(v);
                    finish ();}
                else if(Rep.equals ( "vr" )){
                    Intent v=new Intent(this,VehicleNo_Report.class);
                    v.putExtra("Date_from",ETfrom.getText ().toString ());
                    v.putExtra("Date_to",ETto.getText ().toString ());
                    startActivity(v);
                    finish ();}
                }
            }
            }




    }
}

