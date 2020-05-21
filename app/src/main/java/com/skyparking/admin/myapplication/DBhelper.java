package com.skyparking.admin.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/16/2017.
 */

public class DBhelper extends SQLiteOpenHelper {	public DBhelper(Context context) {
    super(context,DATABASE_NAME, null,DATABASE_VERSION);     }

    private String TABLE_NAME ="sss";
    private String TABLE_NAME1 ="token2";
    private String TABLE_NAME10="printername";
    private String TABLE_NAME11="users";
  //  private String TABLE_NAME2="stock";

    //  private String TABLE_NAME8="address";

    static final String DATABASE_NAME="tokendk.db";
    private static final int DATABASE_VERSION=24;//
    private String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"(sno INTEGER PRIMARY KEY AUTOINCREMENT,vechicleno VARCHAR(20),datein  DATETIME default current_timestamp,dateout  DATETIME default current_timestamp,timein VARCHAR(20),timeout VARCHAR(20),amount VARCHAR(15),days VARCHAR(15),remark VARCHAR(15),dateoutt VARCHAR(15),dateinn VARCHAR(15),Total_Amount VARCHAR(15),vtype VARCHAR(15),bno VARCHAR(15),user VARCHAR(15),userout VARCHAR(15))";
    private String CREATE_TABLE1="CREATE TABLE "+TABLE_NAME1+"(sno INTEGER PRIMARY KEY AUTOINCREMENT,vechicleno VARCHAR(20),amount VARCHAR(20))";
   // private String CREATE_TABLE2="CREATE TABLE "+TABLE_NAME2+"(sno INTEGER PRIMARY KEY AUTOINCREMENT,item VARCHAR(20),stock VARCHAR(20))";
   private String CREATE_TABLE10="CREATE TABLE "+TABLE_NAME10+"(sno INTEGER PRIMARY KEY AUTOINCREMENT,printer  VARCHAR(20))";
    private String CREATE_TABLE11="CREATE TABLE "+TABLE_NAME11+"(sno INTEGER PRIMARY KEY AUTOINCREMENT,user VARCHAR(20))";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE10);
        db.execSQL(CREATE_TABLE11);
       // arg0.execSQL(CREATE_TABLE2);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL ("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL ("DROP TABLE IF EXISTS "+ TABLE_NAME1);
        db.execSQL ("DROP TABLE IF EXISTS "+ TABLE_NAME10);
        db.execSQL ("DROP TABLE IF EXISTS "+ TABLE_NAME11);
      //  arg0.execSQL("DROP TABLE"+TABLE_NAME2);

    }

    public long insertdb1(String Vno, String Datein, String Dateout, String Timein, String Timeout, String Amount, String Days, String Remark, String Dateinn, String Dateoutt, String Tamount, String Vtype, String Bno,String user,String userout)
    {



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("vechicleno",Vno);
        content.put("datein",Datein);
        content.put("dateout",Dateout);
        content.put("timein",Timein);
        content.put("timeout",Timeout);
        content.put("amount",Amount);
        content.put("days",Days);
        content.put("remark",Remark);
        content.put("dateinn",Dateinn);
        content.put("dateoutt",Dateoutt);
        content.put("Total_Amount",Tamount);
        content.put("vtype",Vtype);
        content.put("bno",Bno);
        content.put("user",user);
        content.put("userout",userout);




        return db.insert(TABLE_NAME,null,content);
    }

    public long insertdb2(String vtype,String  Amount)
    {



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("vechicleno",vtype);
        content.put("amount",Amount);




        return db.insert(TABLE_NAME1,null,content);
    }
    public long updatesss(String Remark, String Dateout, String Timeout, String vno, String Dateoutt, Float Amount, int Day, int tom,int Bno,String userout)
    {
        SQLiteDatabase arg0=this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("remark",Remark);
        content.put("timeout",Timeout);
        content.put("dateout",Dateout);
        content.put("dateoutt",Dateoutt);
        content.put("amount",Amount);
        content.put("days",Day);
        content.put("Total_Amount",tom);
        content.put("bno",Bno);
        content.put("userout",userout);
        // content.put("Phno",e);

        return arg0.update(TABLE_NAME  ,content,"vechicleno=?",new String[] {vno} );


    }


    public long updatessssno(String Remark, String Dateout, String Timeout, String vno, String Dateoutt, Float Amount, int Day, int tom,int Bno,String userout)
    {
        SQLiteDatabase arg0=this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("remark",Remark);
        content.put("timeout",Timeout);
        content.put("dateout",Dateout);
        content.put("dateoutt",Dateoutt);
        content.put("amount",Amount);
        content.put("days",Day);
        content.put("Total_Amount",tom);
        content.put("bno",Bno);
        content.put("userout",userout);
        // content.put("Phno",e);

        return arg0.update(TABLE_NAME  ,content,"sno=?",new String[] {vno} );


    }

    public long updatesss1(String vno,String Dateoutt)
    {
        SQLiteDatabase arg0=this.getWritableDatabase();
        ContentValues content=new ContentValues();

        content.put("dateoutt",Dateoutt);
        // content.put("Phno",e);

        return arg0.update(TABLE_NAME  ,content,"vechicleno=?",new String[] {vno} );


    }

    public long updatesss2(String vno,String Dateoutt)
    {
        SQLiteDatabase arg0=this.getWritableDatabase();
        ContentValues content=new ContentValues();

        content.put("dateoutt",Dateoutt);
        // content.put("Phno",e);

        return arg0.update(TABLE_NAME  ,content,"sno=?",new String[] {vno} );


    }

    public ArrayList Selectroute( String vnoo,String Vno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;

        c=arg0.rawQuery("SELECT  ((strftime('%s',dateoutt) - strftime('%s',dateinn))/60/60) FROM sss where remark =\"open\"  AND  ( vechicleno = "+ "'"+vnoo+"'" + " OR sno = "+"'"+vnoo+"'"+")" ,null);

        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));

                map.put("",c.getString(0));
                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList Selectamount( String vnoo)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;

        c=arg0.rawQuery("SELECT  amount FROM sss where remark =\"open\"  AND ( vechicleno = "+"'"+vnoo+"'" + " OR sno ="+"'"+vnoo+"'"+")" ,null);

        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));

                map.put("",c.getString(0));
                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList last()
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT sno,vechicleno FROM sss WHERE   sno = (SELECT MAX(sno)  FROM sss)",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList lastbill()
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT bno FROM sss WHERE   bno = (SELECT MAX(bno)  FROM sss)",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }


    public ArrayList lastbillsno()
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT sno FROM sss WHERE   bno = (SELECT MAX(bno)  FROM sss)",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList lasttamo( String Sno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT Total_Amount FROM sss WHERE   sno = "+"'"+Sno+"'" + " OR vechicleno = "+"'"+Sno+"'",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList last1( String Sno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT vechicleno FROM sss WHERE   sno = "+"'"+Sno+"'" + " OR vechicleno = "+"'"+Sno+"'",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList last2( String Sno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT vtype FROM sss WHERE  sno = "+"'"+Sno+"'" + " OR vechicleno = "+"'"+Sno+"'",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }



    public ArrayList outdate( String Sno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT dateout FROM sss WHERE  sno = "+"'"+Sno+"'" + " OR vechicleno = "+"'"+Sno+"'",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList outtime( String Sno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT timeout FROM sss WHERE  sno = "+"'"+Sno+"'" + " OR vechicleno = "+"'"+Sno+"'",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList lastday( String Sno)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT days FROM sss WHERE  sno = "+"'"+Sno+"'" + " OR vechicleno = "+"'"+Sno+"'",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList sumamount( String DateFrom,String Dateto)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT sum(Total_Amount) FROM sss WHERE remark = \"close\" AND dateout BETWEEN '" + DateFrom + "' AND '" + Dateto+ "' ORDER BY  dateout ASC",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList novlin(String DateFrom,String Dateto)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT  count(*) FROM sss WHERE   datein BETWEEN '" + DateFrom + "' AND '" + Dateto+ "' ORDER BY datein ASC",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList novlout( String DateFrom,String Dateto)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT  count(*) FROM sss WHERE remark = \"close\" AND dateout BETWEEN '" + DateFrom + "' AND '" + Dateto+ "' ORDER BY dateout ASC",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList lasttoken(String vnoo)
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT sno FROM sss WHERE  remark =\"close\"  AND ( vechicleno = "+"'"+vnoo+"'" + " OR sno ="+"'"+vnoo+"'"+")",null);

        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));




                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }


    public int delete( String vn) {
        SQLiteDatabase arg0=this.getReadableDatabase();
        return arg0.delete(TABLE_NAME1,"vechicleno ="+"'"+vn+"'",null);

    }

    public long insertstprienter(String Printer)
    {



        SQLiteDatabase arg0=this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("printer",Printer);




        return arg0.insert(TABLE_NAME10,null,content);
    }

    public long insertuser(String Printer)
    {



        SQLiteDatabase arg0=this.getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("user",Printer);




        return arg0.insert(TABLE_NAME11,null,content);
    }


    public ArrayList lastpri()
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT printer FROM printername WHERE   sno = (SELECT MAX(sno)  FROM printername)",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));



                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

    public ArrayList lastuser()
    {
        SQLiteDatabase arg0=this.getReadableDatabase();
        ArrayList amap = new ArrayList();

        Cursor c=null;
        c=arg0.rawQuery("SELECT user FROM users WHERE   sno = (SELECT MAX(sno)  FROM users)",null);
        //c=arg0.rawQuery("select * from "+TABLE_NAME,null);
        if(c.moveToFirst())
        {
            do
            {
                HashMap<String,String> map=new HashMap<String,String>();
                //.Log.e("Status",c.getString(0));
                map.put("",c.getString(0));



                //Log.e("Map",map.get("Name"));
                amap.add(map);
            }while(c.moveToNext());
        }
        //Log.e("ArrayList",amap.get();
        return amap;
    }

}
