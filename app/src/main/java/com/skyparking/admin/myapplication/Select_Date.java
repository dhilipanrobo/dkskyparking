package com.skyparking.admin.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Select_Date extends Activity implements AdapterView.OnItemClickListener{
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_select__date);
        lv=(ListView)findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
        lisr();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String dd=lv.getItemAtPosition(position).toString();
        Intent v=new Intent(this,User_report.class);

        v.putExtra("Dateref",dd);


        startActivity(v);


    }
    void lisr(){

        DBhelper rd= new DBhelper(this);
        SQLiteDatabase arg0=rd.getReadableDatabase();

        ArrayList<String> list =new ArrayList<String>();

        Cursor c=null;
        c=arg0.rawQuery(" SELECT  DISTINCT datein FROM sss  group by dateout ",null);


        if(c.moveToFirst())
        {
            do
            {

                list.add(""+c.getString(0));
                //Toast.makeText(this,"dd"+c.getString(2).toString(),Toast.LENGTH_LONG).show();
            }while(c.moveToNext());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this,R.layout.lis,list);
        lv.setAdapter(arrayAdapter);


    }
}
