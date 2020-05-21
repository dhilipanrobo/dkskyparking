package com.skyparking.admin.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Check_out extends Activity implements View.OnClickListener {
    long Delay = 4000;
    String  Dayout,Daytime;
    /* will enable user to enter any text to be printed */
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
    Float value1;

    byte[] readBuffer;
    TextView tv6,tv5,tv3,tv4;
    Button bt1;
    EditText et1;
    String am1,date,time,am6,no,amo6;
    String a,s,ams1,user;
    String Sno,Vno,Vty,Days,pri;
    int e=0,bno=0;
    DBhelper rd= new DBhelper(this);
    int tamo=0,day=0;
    DBhelper db1=new DBhelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_check_out);

        tv5=(TextView)findViewById(R.id.textView5);
        tv6=(TextView)findViewById(R.id.textView6);
        tv3=(TextView)findViewById(R.id.textView3);
        et1=(EditText) findViewById(R.id.editText2);
        bt1=(Button) findViewById(R.id.button4);

        bt1.setOnClickListener(this);

        DBhelper rd= new DBhelper(this);
        ArrayList<HashMap<String, String>> amap =rd.lastpri();

        ArrayAdapter<HashMap<String, String>> adap=new ArrayAdapter<HashMap<String, String>>(this,R.layout.lis,amap);

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

        tv5.setText(""+h+":"+mm+":"+am1);
        tv6.setText(""+d+"-"+m+"-"+y);
        a=""+y+"-"+m+"-"+d+" "+hh+":"+mm;



    }

    @Override
    public void onClick(View view) {




        date=tv6.getText().toString();
        time=tv5.getText().toString();
        no=et1.getText().toString();

        if(no.isEmpty())
        {
            Toast.makeText(this," FIELD IS EMITY",Toast.LENGTH_LONG).show();

        }
        else{

try {
    ArrayList<HashMap<String, String>> vty = rd.last2 ( no );
    String vty1 = vty.get ( 0 ).toString ( );
    String vty2 = vty1.replace ( "{=", "" );
    Vty = vty2.replace ( "}", "" );
}
catch (Exception e){
    Vty="";
    Toast.makeText(this,"Check No",Toast.LENGTH_LONG).show();}

            if(Vty.equals("Two wheeler"))
            {


                db1.updatesss1(no,a);
                db1.updatesss2(no,a);
                try{
                    DBhelper rd= new DBhelper(this);
                    ArrayList<HashMap<String, String>>amap =rd.last();

                    String sno1=amap.get(0).toString();



                    String sno2=sno1.replace("{=","");
                    Sno=sno2.replace("}","");


                    ArrayList<HashMap<String, String>>billno =rd.lastbill();

                    String bno1=billno.get(0).toString();



                    String bno2=bno1.replace("{=","");
                    String bno3 =bno2.replace("}","");

                    int Bno = Integer.parseInt(bno3);
                    bno=Bno+1;






                    ArrayList<HashMap<String, String>> Sumgst1 =db1.Selectroute(no,no);
                    ArrayList<HashMap<String, String>> amount =db1.Selectamount(no);

                    String am,amo1;
                    am=Sumgst1 .get(0).toString();
                    amo1=amount.get(0).toString();



                    String am2=am.replace("{","");
                    String am3=am2.replace("}","");
                    String am4=am3.replace("[","");
                    String am5=am4.replace("]","");
                    am6=am5.replace("=","");

                    String amo2=amo1.replace("{","");
                    String amo3=amo2.replace("}","");
                    String amo4=amo3.replace("[","");
                    String amo5=amo4.replace("]","");
                    amo6=amo5.replace("=","");

                    Float value = Float.parseFloat(am6);
                    value1 = Float.parseFloat(amo6);
                    int valu = Integer.parseInt(amo6);
                    e=Math.round(value);
                    Float w=value;
                    day=1+e;
                    tv3.setText(""+w+"e"+e);
                    tamo= (int) ((e*value1)+value1);
                    //    Toast.makeText(this," val"+value+" e "+e+" vno "+Vno,Toast.LENGTH_LONG).show();
                    //
                    //
                    db1.updatesss("close",date,time,no,a,value1,day,valu,bno,user);
                    db1.updatessssno("close",date,time,no,a,value1,day,valu,bno,user);
 //et1.setText("Total Amount:"+valu);




                    //  final Intent v=new Intent(this,MainActivity.class);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                closeBT();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                        }
                    }, 1500);

                    Intent v=new Intent(this,Home_Page.class);
                    startActivity(v);
                    finish();

                }


                catch(Exception e){
                    e.printStackTrace();

                    Toast.makeText(this,"Pls Check No",Toast.LENGTH_LONG).show();

                }

            }
            else {


            int id = view.getId();
            if (id== R.id.button4){
                try {

                    db1.updatesss1(no,a);
                    db1.updatesss2(no,a);

                }
                catch(Exception e){

                    e.printStackTrace();
                }
                try{
                    DBhelper rd= new DBhelper(this);
                    ArrayList<HashMap<String, String>>amap =rd.last();

                    String sno1=amap.get(0).toString();



                    String sno2=sno1.replace("{=","");
                    Sno=sno2.replace("}","");


                    ArrayList<HashMap<String, String>>billno =rd.lastbill();

                    String bno1=billno.get(0).toString();



                    String bno2=bno1.replace("{=","");
                    String bno3 =bno2.replace("}","");

                    int Bno = Integer.parseInt(bno3);
                    bno=Bno+1;






                    ArrayList<HashMap<String, String>> Sumgst1 =db1.Selectroute(no,no);
                    ArrayList<HashMap<String, String>> amount =db1.Selectamount(no);

                    String am,amo1;
                    am=Sumgst1 .get(0).toString();
                    amo1=amount.get(0).toString();



                    String am2=am.replace("{","");
                    String am3=am2.replace("}","");
                    String am4=am3.replace("[","");
                    String am5=am4.replace("]","");
                    am6=am5.replace("=","");

                    String amo2=amo1.replace("{","");
                    String amo3=amo2.replace("}","");
                    String amo4=amo3.replace("[","");
                    String amo5=amo4.replace("]","");
                    amo6=amo5.replace("=","");

                    Float value = Float.parseFloat(am6);
                     value1 = Float.parseFloat(amo6);
                    e=Math.round(value);
                    Float w=value;
                    day=1+e;
                    tv3.setText(""+w+"e"+e);
                    tamo= (int) ((e*value1)+value1);
                 //    Toast.makeText(this," val"+value+" e "+e+" vno "+Vno,Toast.LENGTH_LONG).show();
                    //
                    //
                    db1.updatesss("close",date,time,no,a,value1,day,tamo,bno,user);
                    db1.updatessssno("close",date,time,no,a,value1,day,tamo,bno,user);

              //      et1.setText("Total Amount:"+tamo);
                    try {
                        findBT();
                        openBT();
                        sendData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                  //  final Intent v=new Intent(this,MainActivity.class);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                closeBT();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }



                        }
                    }, 1500);

                    Intent v=new Intent(this,Home_Page.class);
                    startActivity(v);
                   finish();

                }


                catch(Exception e){
                    e.printStackTrace();

                    Toast.makeText(this,"Check No",Toast.LENGTH_LONG).show();

                }

            }}}}


    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if(mBluetoothAdapter == null) {   Toast.makeText(this," Bluetooth Not Found",Toast.LENGTH_LONG).show();

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

            ArrayList<HashMap<String, String>>amap =rd.lasttoken(no);

            String sno1=amap.get(0).toString();



            String sno2=sno1.replace("{=","");
            Sno=sno2.replace("}","");


            ArrayList<HashMap<String, String>>amap1 =rd.last1(no);
            String vno1=amap1.get(0).toString();
            String vno2=vno1.replace("{=","");
            Vno=vno2.replace("}","");



            ArrayList<HashMap<String, String>>vty =rd.last2(no);
            String vty1=vty.get(0).toString();
            String vty2=vty1.replace("{=","");
            Vty=vty2.replace("}","");

            ArrayList<HashMap<String, String>>days =rd.lastday(no);
            String day1=days.get(0).toString();
            String day2=day1.replace("{=","");
            Days=day2.replace("}","");

            ArrayList<HashMap<String, String>>dayout =rd.outdate(no);
            String day1out=dayout.get(0).toString();
            String day2out=day1out.replace("{=","");
            Dayout=day2out.replace("}","");

            ArrayList<HashMap<String, String>>timeout =rd.outtime(no);
            String day1time=timeout.get(0).toString();
            String day2time=day1time.replace("{=","");
            Daytime=day2time.replace("}","");


            // Toast.makeText(this,"open "+Sno+Vno,Toast.LENGTH_SHORT).show();
            //   String msg = myTextbox.getText().toString();
            Calendar cal= Calendar.getInstance();
            int y=cal.get(Calendar.YEAR);
            int m=cal.get(Calendar.MONTH)+1;
            int d=cal.get(Calendar.DATE);
            int h=cal.get(Calendar.HOUR);
            int mm=cal.get(Calendar.MINUTE);
            int am=cal.get(Calendar.AM_PM);




            if (am==0){ am1="am";}
            else{am1="pm";}

            String out="dd-MM-yy";
            SimpleDateFormat out1=new SimpleDateFormat(out);

            String msg="       Pay and Park \n" +
                    ""+" Moti Begumwadi, Salabatpura\n";
            msg += "----------------------------------";
            msg += "\n";
            msg +="         <<Parkout>>\n";
            msg += "----------------------------------";
            msg += "\n";

           // msg +="Date  :"+d+"/"+m+"/"+y+" "+"Time:"+""+h+":"+mm+":"+am1+"\n";
            msg +="Date  :"+Dayout+""+"Time:"+Daytime+"\n";
            msg +="Token No    :"+Sno+"    S:No:"+bno+"\n";
            msg +="Vehicle No  :"+Vno+"\n";
            msg +="No Of Hour  :"+Days+" X Rs"+value1+"\n";
            msg +="Vehicle Type:"+Vty+"\n"+"Amount      :"+tamo+"\n";
            msg += "User        :"+user+"  \n    ";


            msg += "     Vist Again  \n\n\n      ";
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
    void openBT() throws IOException {
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
    }
}
