package com.skyparking.admin.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Vehicle_report extends Activity implements View.OnClickListener{
    Button bt1,bt2,bt3,bt4,bt5,bt6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vehicle_report);
        bt1=(Button)findViewById(R.id.button7);
        bt1.setOnClickListener(this);
        bt2=(Button)findViewById(R.id.button8);
        bt2.setOnClickListener(this);
        bt3=(Button)findViewById(R.id.button14);
        bt3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id== R.id.button8){

            Intent v=new Intent(this,Date_select.class);
            v.putExtra("ref","bl");
            v.putExtra("report","vr");//cash report
            startActivity(v);
        }


        else if(id== R.id.button7){

        Intent v=new Intent(this,Date_select.class);
        v.putExtra("ref","bl");
            v.putExtra("report","cr");//cash report
        startActivity(v);

    }
        else if(id== R.id.button14){

            Intent v=new Intent(this,Date_select.class);
            v.putExtra("ref","bl");
            v.putExtra("report","pr");//cash report
            startActivity(v);

        }

    }
}
