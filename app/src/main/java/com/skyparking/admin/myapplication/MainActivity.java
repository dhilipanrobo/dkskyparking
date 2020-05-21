package com.skyparking.admin.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends  AppCompatActivity implements View.OnClickListener{
    Button bt1,bt2,bt3,bt4,bt5,bt6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        bt2=(Button)findViewById(R.id.button2);
        bt2.setOnClickListener(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if(id== R.id.action_token){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enter Password");

            LayoutInflater inflater = getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.alb, null);
            final EditText userAnswer = (EditText) dialoglayout.findViewById(R.id.editText);
            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface arg0, int arg1) {



                    String pass=userAnswer.getText().toString();
                    if("13579".equals(pass)){
                        d1();

                    }
                    else{//vibrator.vibrate(500);
                    }







                }
            });




            builder.setView(dialoglayout);

            builder.show();

        }


        else if(id== R.id.action_printer){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enter Password");

            LayoutInflater inflater = getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.alb, null);
            final EditText userAnswer = (EditText) dialoglayout.findViewById(R.id.editText);
            builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface arg0, int arg1) {



                    String pass=userAnswer.getText().toString();
                    if("13579".equals(pass)){
                        d2();

                    }
                    else{//vibrator.vibrate(500);
                    }







                }
            });




            builder.setView(dialoglayout);

            builder.show();

        }


        return super.onOptionsItemSelected(item);
    }
    void d1(){
        Intent v=new Intent(this,Add_Token.class);
        startActivity(v);
    }

    void d2(){
        Intent v=new Intent(this,Add_printer.class);
        startActivity(v);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id== R.id.button){
            DBhelper db1=new DBhelper(this);
            db1.insertuser("user3");
            Intent v=new Intent(this,Home_Page.class);

            startActivity(v);
            finish();
        }

        else if(id== R.id.button2) {

            DBhelper db1=new DBhelper(this);
            db1.insertuser("user1");

            Intent v=new Intent(this,Home_Page.class);

            startActivity(v);
            finish();


        }
        else if(id== R.id.button3){
            DBhelper db1=new DBhelper(this);
            db1.insertuser("user2");
            Intent v=new Intent(this,Home_Page.class);

            startActivity(v);
            finish();


        }

    }
}
