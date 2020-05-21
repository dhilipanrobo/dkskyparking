package com.skyparking.admin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class User_report extends Activity implements View.OnClickListener {
    Button bt1,bt2,bt3,bt4,bt5,bt6;
    String Date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_user_report);

        bt2=(Button)findViewById(R.id.button2);
        bt2.setOnClickListener(this);

        Date=getIntent().getExtras().getString("Dateref");
        //Toast.makeText(this,"dd"+Date,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id== R.id.button){

            Intent v=new Intent(this,Dailyreport.class);

            v.putExtra("Dateref",Date);
            v.putExtra("user","user3");


            startActivity(v);
            finish();
        }

        else if(id== R.id.button2) {

            Intent v=new Intent(this,Dailyreport.class);

            v.putExtra("Dateref",Date);

            v.putExtra("user","user1");
            startActivity(v);

            finish();


        }
        else if(id== R.id.button3){
            Intent v=new Intent(this,Dailyreport.class);

            v.putExtra("Dateref",Date);
            v.putExtra("user","user2");

            startActivity(v);

            finish();


        }

    }
}
