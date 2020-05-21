package com.skyparking.admin.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity implements View.OnClickListener {
 Button login;
 EditText ETname,ETpass;
 String STname,STpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        ActivityCompat.requestPermissions(login.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);


        setContentView ( R.layout.activity_login );
        ETname=findViewById ( R.id.editText8 );
        ETpass=findViewById ( R.id.editText9 );
        login = findViewById ( R.id.button9 );
        login.setOnClickListener ( this );}
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
    public void onClick(View v) {

        STname=ETname.getText ().toString ();
        STpass=ETpass.getText ().toString ();

        if(STname.isEmpty ())
        {
            Toast.makeText ( this, " User Name Field Is Empty ", Toast.LENGTH_SHORT ).show ( );
            ETname.requestFocus();
        }
        else if(STpass.isEmpty ()) {


                Toast.makeText ( this, " Password Field Is Empty ", Toast.LENGTH_SHORT ).show ( );
                ETpass.requestFocus();
            }
            else {
                if(STpass.equals ( "admin123" ) && STname.equals ( "admin" )){
                finish();

                Intent vv=new Intent(login.this,Home_Page.class);
                startActivity(vv);

            }
            else{
                    Toast.makeText ( this, "User Name Or Password Incorrect", Toast.LENGTH_SHORT ).show ( );
                }


        }
        }




    }

