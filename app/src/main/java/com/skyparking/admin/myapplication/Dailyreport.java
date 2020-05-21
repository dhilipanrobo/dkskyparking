package com.skyparking.admin.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class Dailyreport extends Activity implements View.OnClickListener  {
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
    String Date,am6,am1,amo1,vin1,vin6,vout6,vout1;;
    TextView tv4;
    TextView tv3;
    TextView tv7;
    Button bt1;
    DBhelper db1=new DBhelper(this);
    Check_in ch1=new Check_in();
    String pri,user;
    String Datefrom,Dateto;
    Button Btfilter,Btexcel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_dailyreport);
        Date=getIntent().getExtras().getString("Dateref");
        //Toast.makeText(this,":"+Date,Toast.LENGTH_LONG).show();
        tv3=(TextView)findViewById(R.id.textView3);
        tv4=(TextView)findViewById(R.id.textView4);
        tv7=(TextView)findViewById(R.id.textView7);
        bt1=(Button) findViewById(R.id.button10);
        Btexcel=findViewById ( R.id.button21);
        Btexcel.setOnClickListener ( this );
        Btfilter=findViewById ( R.id.button18 );
        Btfilter.setOnClickListener ( this );
        bt1.setOnClickListener(this);
        Date=getIntent().getExtras().getString("Dateref");
        user=getIntent().getExtras().getString("user");
        Datefrom=getIntent().getExtras().getString("Date_from");
        Dateto=getIntent().getExtras().getString("Date_to");
      //  Toast.makeText(this,"dd"+Date+"user"+user,Toast.LENGTH_LONG).show();

        DBhelper rd= new DBhelper(this);
        ArrayList<HashMap<String, String>> amap =rd.lastpri();

        ArrayAdapter<HashMap<String, String>> adap=new ArrayAdapter<HashMap<String, String>>(this,R.layout.lis,amap);

        try{
            String ams;
            ams=amap.toString();

            String ams4=ams.replace("[{=","");
            pri=ams4.replace("}]","");
        }
        catch (Exception e) {
            e.printStackTrace();
        }




        ArrayList<HashMap<String, String>> amount =db1.sumamount(Datefrom,Dateto);
        ArrayList<HashMap<String, String>> novin =db1.novlin(Datefrom,Dateto);
        ArrayList<HashMap<String, String>> novout =db1.novlout(Datefrom,Dateto);




        amo1=amount.get(0).toString();
        vin1=novin.get(0).toString();
        vout1=novout.get(0).toString();



        String am2=amo1.replace("{","");
        String am3=am2.replace("}","");
        String am4=am3.replace("[","");
        String am5=am4.replace("]","");
        am6=am5.replace("=","");

        String vin2=vin1.replace("{","");
        String vin3=vin2.replace("}","");
        String vin4=vin3.replace("[","");
        String vin5=vin4.replace("]","");
        vin6=vin5.replace("=","");

        String vout2=vout1.replace("{","");
        String vout3=vout2.replace("}","");
        String vout4=vout3.replace("[","");
        String vout5=vout4.replace("]","");
        vout6=vout5.replace("=","");

      //  Toast.makeText(this,":"+am6+"in "+vin6+"out "+vout6,Toast.LENGTH_LONG).show();

        tv3.setText(""+am6);
        tv4.setText(""+vin6);
        tv7.setText(""+vout6);


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

    void sendData() throws IOException, InterruptedException {



        try {


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
            msg +="         >>Report<<\n";
            msg += "----------------------------------";
            msg += "\n";

            msg +="Date  :"+d+"/"+m+"/"+y+" "+"Time:"+""+h  +":"+mm+":"+am1+"\n";
            msg +="Cash        :"+am6+"\n";
            msg +="Vehicle In  :"+vin6+"\n";
            msg +="Vehicle Out :"+vout6+"\n";



            msg += "\n";
            msg += "          Have Nice Day  \n\n\n      ";
            msg += "\n\n\n\n";
            format[2] =((byte)(0x6 | arrayOfByte1[2]));
            // format[2] =((byte)(0x10 | arrayOfByte1[2]));
            //        mmOutputStream.write(format);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id== R.id.button10)
        {

            try {
                findBT();
                openBT();
                sendData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
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

        else if(id== R.id.button18){

            Intent vv=new Intent(this,Date_select.class);

            vv.putExtra("report","cr");//cash report
            startActivity(vv);
            finish ();

        }

        else if(id== R.id.button21){

           // report_excel ();

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


                report3 ( filename,Datefrom,Dateto );


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
        c=arg0.rawQuery(" select( SELECT  count(*) , sum(Total_Amount) FROM sss where  remark = 'close' AND datein BETWEEN '" + date_from + "' AND '" + date_to+ "' ),( SELECT  count(*)  FROM sss where  remark = 'open' AND datein BETWEEN '\" + date_from + \"' AND '\" + date_to+ \"' )",null);
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

}