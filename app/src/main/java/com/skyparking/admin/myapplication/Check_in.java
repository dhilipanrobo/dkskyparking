package com.skyparking.admin.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Check_in extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private static final Context This = null;
    long Delay = 4000;


    // will enable user to enter any text to be printed
    EditText myTextbox;

    int readBufferPosition;
    volatile boolean stopWorker;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] format = { 12, 16, 0 };  //27  33 0
    byte[] arrayOfByte1 = { 12, 16, 0 };


    byte[] readBuffer;
    TextView tv1;
    TextView tv2;
    String a,Amount,vtype,Vty;
    String user;
    String Sno,Vno,pri,datein,timein,am1,ams1;
    ListView lv1;
    int bno;
    Button bt1;

    EditText et1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.activity_check_in);

        try{DBhelper rd= new DBhelper(this);
            ArrayList<HashMap<String, String>> amap =rd.last();

            String sno1=amap.get(0).toString();



            String sno2=sno1.replace("{=","");
            Sno=sno2.replace("}","");

            int sno11= Integer.parseInt(Sno);

            if (sno11 >= 50){
                Toast.makeText(this,"trial version over you need Call +91 9488120489".toString(),Toast.LENGTH_LONG).show();
                finish();}
            //else {Toast.makeText(this,"else trial version over you need Call +91 9488120489".toString(),Toast.LENGTH_LONG).show();

            //  }
        }catch(Exception e){
            e.printStackTrace();
        }

        tv1=(TextView)findViewById(R.id.textView);
        tv2=(TextView)findViewById(R.id.textView2);
        et1=(EditText) findViewById(R.id.editText);

        lv1=(ListView)findViewById(R.id.listview);
        lv1.setOnItemClickListener(this);


        Calendar cal= Calendar.getInstance();
        String y= String.format("%02d",cal.get(Calendar.YEAR));
        String m= String.format("%02d",cal.get(Calendar.MONTH)+1);
        String d= String.format("%02d",cal.get(Calendar.DATE));
        String h= String.format("%02d",cal.get(Calendar.HOUR));
        String hh= String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));
        String mm= String.format("%02d",cal.get(Calendar.MINUTE));
        int am=cal.get(Calendar.AM_PM);



        if (am==0){ am1="am";}
        else{am1="pm";}

        tv1.setText(""+h+":"+mm+":"+am1);
        tv2.setText(""+d+"-"+m+"-"+y);

        a=""+y+"-"+m+"-"+d+" "+hh+":"+mm;

        datein=tv2.getText().toString();
        timein=tv1.getText().toString();

        DBhelper rd= new DBhelper(this);
        ArrayList<HashMap<String, String>>amap =rd.lastpri();



        try{
            String ams;
            ams=amap.toString();

            String ams4=ams.replace("[{=","");
            pri=ams4.replace("}]","");
            ArrayList<HashMap<String, String>>amap1 =rd.lastuser();
            ams1=amap1.toString();
            String ams5=ams1.replace("[{=","");
            user =ams5.replace("}]","");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        lisr();



}
    void lisr(){

        DBhelper rd= new DBhelper(this);
        SQLiteDatabase arg0=rd.getReadableDatabase();

        ArrayList<String> list =new ArrayList<String>();

        Cursor c=null;
        c=arg0.rawQuery(" SELECT vechicleno,amount FROM token2 ",null);


        if(c.moveToFirst())
        {
            do
            {

                list.add(""+c.getString(0)+"-"+c.getString(1));
                //Toast.makeText(this,"dd"+c.getString(2).toString(),Toast.LENGTH_LONG).show();
            }while(c.moveToNext());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this,R.layout.lis,list);
        lv1.setAdapter(arrayAdapter);


    }

    @Override
    public void onClick(final View v) {
        Vno=et1.getText().toString();

try{
        if(Vno.isEmpty())
        {
            Toast.makeText(this,"Vehicle NO Field Is Emity",Toast.LENGTH_LONG).show();

        }
        else{
        final Intent vv=new Intent(this,Home_Page.class);
        Vno=et1.getText().toString();
        DBhelper db1=new DBhelper(this);
        db1.insertdb1(Vno,datein,datein,timein,"0","10","0","close",a,"0","10","Two Wheeler","0",user,"0");
        try {

            findBT();
            openBT();
            sendData();
            et1.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                startActivity(vv);
                finish();

            }
        }, 2000);



    }}catch (Exception e) {
    e.printStackTrace();
}

    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Vno=et1.getText().toString();

        if(Vno.isEmpty())
        {
            Toast.makeText(this,"Vehicle NO FIELD IS EMITY",Toast.LENGTH_LONG).show();

        }
        else{


            String dd=lv1.getItemAtPosition(position).toString();
            String ds[]=dd.split("-");
            String ds1=dd.split("-")[1];
            String ds2=dd.split("-")[0];
            for (int y=0;y<ds.length;y++){

                Amount=ds1;


            }

            for (int y=0;y<ds.length;y++){

                vtype=ds2;


            }
            try{
                DBhelper rd= new DBhelper(this);
                ArrayList<HashMap<String, String>>amap =rd.last();

                String sno1=amap.get(0).toString();



                String sno2=sno1.replace("{=","");
                Sno=sno2.replace("}","");




                int Bno = Integer.parseInt(Sno);
                bno=Bno+1;}
            catch (Exception e) {
                e.printStackTrace();
            }
            final Intent v=new Intent(this,Home_Page.class);
            v.putExtra("ref","bl");
            DBhelper db1=new DBhelper(this);
            db1.insertdb1(Vno,datein,"0",timein,"0",Amount,"0","open",a,"0","0",vtype,"0",user,"0");

            try {
                lv1.getChildAt( position).setEnabled(false);
                findBT();
                openBT();
                sendData();
                et1.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }



            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        closeBT();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    startActivity(v);
                    finish();

                }
            }, 2000);
            lv1.getChildAt( position).setEnabled(true);
        }
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {
                Toast.makeText(this," Bluetooth Not Found",Toast.LENGTH_LONG).show();
            }

            if(!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if(pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals(pri)) {

                        mmDevice = device;
                        break;
                        // Printer001#DC:0D:30:20:46:90
                        //"BTprinter8269
                    }
                }
            }

            // myLabel.setText("Bluetooth device found.");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            boolean stopWorke = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //  myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendData() throws IOException {
        try {
            DBhelper rd= new DBhelper(this);
            ArrayList<HashMap<String, String>>amap =rd.last();

            String sno1=amap.get(0).toString();



            String sno2=sno1.replace("{=","");
            Sno=sno2.replace("}","");

            ArrayList<HashMap<String, String>>amap1 =rd.last1(Sno);
            String vno1=amap1.get(0).toString();
            String vno2=vno1.replace("{=","");
            Vno=vno2.replace("}","");


            ArrayList<HashMap<String, String>>vty =rd.last2(Sno);
            String vty1=vty.get(0).toString();
            String vty2=vty1.replace("{=","");
            Vty=vty2.replace("}","");


            // Toast.makeText(this,"open "+Sno+Vno,Toast.LENGTH_SHORT).show();
            //   String msg = myTextbox.getText().toString();
            Calendar cal= Calendar.getInstance();
            String y= String.format("%02d",cal.get(Calendar.YEAR));
            String m= String.format("%02d",cal.get(Calendar.MONTH)+1);
            String d= String.format("%02d",cal.get(Calendar.DATE));
            String h= String.format("%02d",cal.get(Calendar.HOUR));
            String hh= String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));
            String mm= String.format("%02d",cal.get(Calendar.MINUTE));
            int am=cal.get(Calendar.AM_PM);




            if (am==0){ am1="am";}
            else{am1="pm";}

            String out="dd-MM-yy";
            SimpleDateFormat out1=new SimpleDateFormat(out);

            String msg="       Pay and Park \n" +
                    ""+" Moti Begumwadi, Salabatpura\n";


            msg += "----------------------------------";
            msg += "\n";
            msg +="         >>Park In<<\n";
            msg += "----------------------------------";
            msg += "\n";

            msg +="Date  :"+d+"/"+m+"/"+y+" "+"Time:"+""+h  +":"+mm+":"+am1+"\n";
            msg +="Token No    :"+Sno+"\n\n";


            msg +="Vehicle No  :"+Vno+"\n\n";
            msg +="Vehicle Type:"+Vty+"\n";
           // msg += "User        :"+user+"  \n    ";

          /*   msg += "--------------------------------";
            msg += "\n";
            msg += "Item         GST Qty Rate Amount"+"\n";
            msg += "--------------------------------\n";
            for (int i=0;i<amap1.size();i++){
                String ams;
                ams=amap1.get(i).toString();


                String ams4=ams.replace("[","");
                String ams5=ams4.replace("]","");
                msg +=""+ams5+"\n\n";

                //    mmOutputStream.write(msg.getBytes());

            }
            msg += "--------------------------------";
            msg += "\n";

            msg +="                    Amount:" +am6+"\n";
            msg +="                      SGST:"+s+"\n";
            msg +="                      CGST:"+s+"\n";
            msg += "--------------------------------";
            msg +="                NETAmount:" +e+"\n";


            msg += "--------------------------------";
            msg += "\n";
            msg += "           GST Smmary\n";
            msg += "GST%       SGST      CGST   AMOUNT";*/



          /*  for (int i=0;i<amap2.size();i++){

                String ams;
                ams=amap2.get(i).toString();


                String ams4=ams.replace("[","");
                String ams5=ams4.replace("]","");
                msg +=""+ams5+"\n\n";
                msg +=""+amap2.get(i)+"\n";

                //     mmOutputStream.write(msg.getBytes());

            }*/
            msg += "\n";
            msg += "     Park At Owner Risk \n  \n\n   ";


            msg += "\n\n\n\n";
            format[2] =((byte)(0x6 | arrayOfByte1[2]));
            // format[2] =((byte)(0x10 | arrayOfByte1[2]));
            mmOutputStream.write(format);
            mmOutputStream.write(msg.getBytes());






        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //   bluetoothadapter.disable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();

            mmOutputStream = mmSocket.getOutputStream();
            Thread.sleep(1000);
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
