package com.skyparking.admin.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Add_printer extends AppCompatActivity implements View.OnClickListener {
    Button bt1;
    EditText et1,et2,et3,et4,et5,et6;
    String pri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add__prienter);
        et1=(EditText)findViewById(R.id.editText5);
        bt1=(Button)findViewById(R.id.button13);
        bt1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        pri=et1.getText().toString();

        if(pri.isEmpty())
        {
            Toast.makeText(this," FIELD IS EMITY",Toast.LENGTH_LONG).show();

        }
        else {

            DBhelper db1=new DBhelper(this);
            db1.insertstprienter(pri);
            Toast.makeText(this,"Saved "+pri,Toast.LENGTH_LONG).show();

            et1.setText("");
        }

    }

    }

