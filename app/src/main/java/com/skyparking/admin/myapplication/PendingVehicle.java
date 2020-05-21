package com.skyparking.admin.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PendingVehicle extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener {
    ListView lv;
    SearchView sv;
    String Datefrom,Dateto;
    Button Btfilter,Btexcel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate ( savedInstanceState );
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView ( R.layout.activity_pending_vehicle );
        Btfilter=findViewById ( R.id.button19 );
        Btfilter.setOnClickListener ( this );
        Btexcel=findViewById ( R.id.button21);
        Btexcel.setOnClickListener ( this );
        lv=(ListView)findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
        sv=(SearchView) findViewById(R.id.search);
        Datefrom=getIntent().getExtras().getString("Date_from");
        Dateto=getIntent().getExtras().getString("Date_to");
        lisr();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    void lisr(){

        DBhelper rd= new DBhelper(this);
        SQLiteDatabase arg0=rd.getReadableDatabase();

        ArrayList<String> list =new ArrayList<String>();

        Cursor c=null;
        c=arg0.rawQuery(" SELECT  DISTINCT vechicleno,timein,datein,dateout,timeout,days,amount FROM sss where remark='open'  AND datein BETWEEN '" + Datefrom + "' AND '" + Dateto+ "' ORDER BY datein ASC",null);


        if(c.moveToFirst())
        {
            do
            {

                list.add("V-NO:"+c.getString(0)+"\n"+""+"Intime\n"+c.getString(1)+"\t Date:"+c.getString(2));
                //Toast.makeText(this,"dd"+c.getString(2).toString(),Toast.LENGTH_LONG).show();
            }while(c.moveToNext());
        }
        if(list.isEmpty ()){
            Toast.makeText ( this, "Null Data  !!", Toast.LENGTH_SHORT ).show ( );
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this,R.layout.rep,list);
        lv.setAdapter(arrayAdapter);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter("V-NO:"+newText);
                return false;
            }
        });



    }



    @Override
    public void onClick(View view) {


        int id = view.getId();
        if (id== R.id.button19){

            Intent v=new Intent(this,Date_select.class);

            v.putExtra("report","pr");//cash report
            startActivity(v);
            fileList ();
        }


        else if(id== R.id.button21){

            report_excel ();

        }
    }

    void report_excel(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Enter The File Name");

        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.alt, null);
        final EditText userAnswer = (EditText) dialoglayout.findViewById(R.id.editText);

        builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                String filename=userAnswer.getText().toString();


                if (filename.isEmpty ())
                {
                    err ();
                }
                else
                {



                    report3 ( filename,Datefrom,Dateto );


                }


            }
        });




        builder.setView(dialoglayout);

        builder.show();

    }

    void report3(String file_name,String date_from,String date_to){
        Toast.makeText ( this, ""+file_name+".csv Saved", Toast.LENGTH_SHORT ).show ( );
        Calendar cal= Calendar.getInstance();
        int y=cal.get(Calendar.YEAR);
        int m=cal.get(Calendar.MONTH)+1;
        int d=cal.get(Calendar.DATE);
        String out="dd-MM-yy";
        SimpleDateFormat out1=new SimpleDateFormat(out);


        DBhelper rd= new DBhelper(this);
        SQLiteDatabase arg0=rd.getReadableDatabase();

        final ArrayList<String> list =new ArrayList<String>();
        Cursor c=null;
        //c=arg0.rawQuery("SELECT* FROM reg1 WHERE   sno = (SELECT MAX(sno)  FROM reg1)",null);
        c=arg0.rawQuery(" SELECT  DISTINCT vechicleno,timein,datein FROM sss where remark='open'  AND datein BETWEEN '" + date_from + "' AND '" + date_to+ "' ORDER BY datein ASC",null);


        try{
            int rowcount = 0;
            int colcount = 0;

            File sdCardDir = Environment.getExternalStorageDirectory();

            String filee=file_name.toString ()+".csv";
            String filename = filee;

            // the name of the file to export with

            File saveFile = new File(sdCardDir, filename);

            FileWriter fw = new FileWriter(saveFile);


            BufferedWriter bw = new BufferedWriter(fw);
            rowcount = c.getCount();

            colcount = c.getColumnCount();

            if (rowcount > 0) {

                c.moveToFirst();



                for (int i = 0; i < colcount; i++) {

                    if (i != colcount - 1) {



                        bw.write(c.getColumnName(i) + ",");



                    } else {



                        bw.write(c.getColumnName(i));



                    }

                }

                bw.newLine();



                for (int i = 0; i < rowcount; i++) {

                    c.moveToPosition(i);



                    for (int j = 0; j < colcount; j++) {

                        if (j != colcount - 1)

                            bw.write(c.getString(j) + ",");

                        else

                            bw.write(c.getString(j));

                    }

                    bw.newLine();

                }

                bw.flush();

            }

        } catch(Exception ex) {





        } finally{}

        if(c.moveToFirst())
        {
            do
            {


                list.add(""+c.getString(0)+"Rs:"+c.getString(1));




            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();


    }
    void err(){   Toast.makeText ( this, "From Date Field Is Empty", Toast.LENGTH_SHORT ).show ( );}

}


