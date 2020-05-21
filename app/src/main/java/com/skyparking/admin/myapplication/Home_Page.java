package com.skyparking.admin.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Home_Page extends Activity implements View.OnClickListener,DialogInterface.OnClickListener {
    Button bt1,bt2,bt3,bt4,bt5,bt6;
    String am1;
    int effectivePrintWidth = 48;

    Button btnPrint, btnSetPrnType;
    ArrayList<String> printerList;
    String creditData;
    ProgressDialog m_WaitDialogue;

    int glbPrinterWidth;

    //private Context context;
    EditText editText,rfText;
    private PrintWriter printOut;
    private Socket socketConnection;
    private String txtIP="";
    Spinner spinner;
    String imgDecodableString;
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
    String no,pri;

    byte[] readBuffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home__page);
        bt1=(Button)findViewById(R.id.button3);
        bt1.setOnClickListener(this);
        bt2=(Button)findViewById(R.id.button);
        bt2.setOnClickListener(this);
        bt3=(Button)findViewById(R.id.button6);
        bt3.setOnClickListener(this);

        bt4=(Button)findViewById(R.id.button11);
        bt4.setOnClickListener(this);
        bt5=(Button)findViewById(R.id.button12);
        bt5.setOnClickListener(this);




        try{
            DBhelper rd= new DBhelper(this);
            ArrayList<HashMap<String, String>>amap =rd.lastpri();



            String ams,ams1;
            ams=amap.toString();


            String ams4=ams.replace("[{=","");
            pri=ams4.replace("}]","");
            ArrayList<HashMap<String, String>>amap1 =rd.lastuser();
            ams1=amap1.toString();
            String ams5=ams1.replace("[{=","");
            String user =ams5.replace("}]","");

        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id== R.id.button3){

            Intent v=new Intent(this,Check_in.class);
            v.putExtra("ref","bl");
            startActivity(v);
           // finish();
        }


        else if(id== R.id.button){

            Intent v=new Intent(this,Check_out.class);
            v.putExtra("ref","bl");
            startActivity(v);
          //  finish();

        }

        else if(id== R.id.button6){



            Intent v=new Intent(this,Vehicle_report.class);
            v.putExtra("ref","bl");
            startActivity(v);


        }



        else if(id== R.id.button12){

            try {
                findBT();
                openBT();
                sendData1();
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



                }
            }, 1500);

        }

        else if(id== R.id.button11){

            try {
                findBT();
                openBT();
                sendData();
            } catch (IOException e) {
                e.printStackTrace();
            }



            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                         closeBT();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                }
            }, 2000);
        }

        }



    void d1(){
        Intent v=new Intent(this,Add_Token.class);
        startActivity(v);
    }

    void d2(){
        Intent v=new Intent(this,Add_printer.class);
        startActivity(v);
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
            ArrayList<HashMap<String, String>> amap =rd.last();

            String sno1=amap.get(0).toString();



            String sno2=sno1.replace("{=","");
            String Sno=sno2.replace("}","");

            ArrayList<HashMap<String, String>>amap1 =rd.last1(Sno);
            String vno1=amap1.get(0).toString();
            String vno2=vno1.replace("{=","");
            String Vno=vno2.replace("}","");


            ArrayList<HashMap<String, String>>vty =rd.last2(Sno);
            String vty1=vty.get(0).toString();
            String vty2=vty1.replace("{=","");
            String    Vty=vty2.replace("}","");


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
            msg +="Token No    :"+Sno+"\n";
            msg +="Vehicle No  :"+Vno+"\n";
            msg +="Vehicle Type:"+Vty+"\n";


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
            msg += "          Have Nice Day  \n\n\n      ";
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

    void sendData1() throws IOException {
        try {



            DBhelper rd= new DBhelper(this);
            ArrayList<HashMap<String, String>>amap0 =rd.lastbillsno();


            ArrayList<HashMap<String, String>>billno =rd.lastbill();

            String bno1=billno.get(0).toString();



            String bno2=bno1.replace("{=","");
            String bno3 =bno2.replace("}","");

            String bsno1=amap0.get(0).toString();



            String bsno2=bsno1.replace("{=","");
            no=bsno2.replace("}","");



            ArrayList<HashMap<String, String>>amap =rd.lasttoken(no);

            String sno1=amap.get(0).toString();



            String sno2=sno1.replace("{=","");
            String    Sno=sno2.replace("}","");


            ArrayList<HashMap<String, String>>amap1 =rd.last1(no);
            String vno1=amap1.get(0).toString();
            String vno2=vno1.replace("{=","");
            String   Vno=vno2.replace("}","");



            ArrayList<HashMap<String, String>>vty =rd.last2(no);
            String vty1=vty.get(0).toString();
            String vty2=vty1.replace("{=","");
            String  Vty=vty2.replace("}","");

            ArrayList<HashMap<String, String>>days =rd.lastday(no);
            String day1=days.get(0).toString();
            String day2=day1.replace("{=","");
            String   Days=day2.replace("}","");


            ArrayList<HashMap<String, String>>amount =rd.lasttamo(no);
            String amount1=amount.get(0).toString();
            String amount2=day1.replace("{=","");
            String   amount3=amount2.replace("}","");


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

            String msg="          Jayesh C Ghael\n" +
                    "   4/2455, Ghael compound\n"+" Moti Begumwadi, Salabatpura\n"+"    SURAT. Pin no 395001. \n";
            msg += "----------------------------------";
            msg += "\n";
            msg +="         <<Parkout>>\n";
            msg += "----------------------------------";
            msg += "\n";

            msg +="Date  :"+d+"/"+m+"/"+y+" "+"Time:"+""+h+":"+mm+":"+am1+"\n";
            msg +="Token No    :"+Sno+"    S:No:"+bno3+"\n";
            msg +="Vehicle No  :"+Vno+"\n";
            msg +="No Of days  :"+Days+"\n";
            msg +="Vehicle Type:"+Vty+"\n"+"Amount      :"+amount3+"\n";


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
            msg += "            VistAgain  \n\n\n      ";
            msg += "\n\n\n\n";
            format[2] =((byte)(0x6 | arrayOfByte1[2]));
            // format[2] =((byte)(0x10 | arrayOfByte1[2]));
            mmOutputStream.write(format);
            mmOutputStream.write(msg.getBytes());






        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

