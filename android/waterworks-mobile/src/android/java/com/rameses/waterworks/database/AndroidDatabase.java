package com.rameses.waterworks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AndroidDatabase extends SQLiteOpenHelper implements Database{
    
    public String ERROR = "";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "etracs_waterworks";
    
    public AndroidDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqld) {
        sqld.execSQL("CREATE TABLE setting ("
                + " name TEXT,"
                + " value TEXT)");
        
        sqld.execSQL("CREATE TABLE account ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " state VARCHAR(30),"
                + " dtstarted VARCHAR(50),"
                + " acctno VARCHAR(50), "
                + " applicationid VARCHAR(50), "
                + " name TEXT, "
                + " address TEXT, "
                + " cellphoneno VARCHAR(50), "
                + " meterid VARCHAR(50), "
                + " areaid VARCHAR(50), "
                + " area VARCHAR(50), "
                + " balance VARCHAR(50), "
                + " lasttxndate VARCHAR(50), "
                + " serialno VARCHAR(50), "
                + " reading VARCHAR(50),"
                + " assignee_objid VARCHAR(50),"
                + " assignee_name VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE reading ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " meterid VARCHAR(50), "
                + " reading VARCHAR(50),"
                + " consumption VARCHAR(50),"
                + " amtdue VARCHAR(50),"
                + " totaldue VARCHAR(50), "
                + " state VARCHAR(50))");
       
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        sqld.execSQL("DROP TABLE IF EXIST setting");
        sqld.execSQL("DROP TABLE IF EXIST account");
        sqld.execSQL("DROP TABLE IF EXIST reading");
        onCreate(sqld);
    }
    
    //################################################################################
    //  SETTING TABLE
    //################################################################################

    @Override
    public void createSetting(Setting s) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("name", s.getName());
        values.put("value", s.getValue());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("setting", null, values);
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }

    @Override
    public void updateSetting(Setting s) {
        ERROR = "";
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.execSQL("UPDATE setting SET value = '"+s.getValue()+"' WHERE name = '"+s.getName()+"'");
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }

    @Override
    public List<Setting> getAllSettings() {
        ERROR = "";
        List<Setting> list = new ArrayList<Setting>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM setting", null);
            if(cursor.moveToFirst()){
                do{
                    Setting setting = new Setting(cursor.getString(0),cursor.getString(1));
                    list.add(setting);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
    @Override
    public boolean settingExist(Setting s){
        boolean b = false;
        List<Setting> list = getAllSettings();
        Iterator<Setting> i = list.iterator();
        while(i.hasNext()){
            Setting setting = i.next();
            if(setting.getName().equals(s.getName())) b = true;
        }
        return b;
    }
    
    //################################################################################
    //  ACCOUNT TABLE
    //################################################################################
    
    @Override
    public void createAccount(Map acct) {
        ERROR = "";
        String objid = acct.get("objid") != null ? acct.get("objid").toString() : "";
        String state = acct.get("state") != null ? acct.get("state").toString() : "";
        String dtstarted = acct.get("dtstarted") != null ? acct.get("dtstarted").toString() : "";
        String acctno = acct.get("acctno") != null ? acct.get("acctno").toString() : "";
        String applicationid = acct.get("applicationid") != null ? acct.get("applicationid").toString() : "";
        String name = acct.get("name") != null ? acct.get("name").toString() : "";
        String address = acct.get("address") != null ? acct.get("address").toString() : "";
        String cellphoneno = acct.get("cellphoneno") != null ? acct.get("cellphoneno").toString() : "";
        String meterid = acct.get("meterid") != null ? acct.get("meterid").toString() : "";
        String areaid = acct.get("areaid") != null ? acct.get("areaid").toString() : "";
        String area = acct.get("area") != null ? acct.get("area").toString() : "";
        String balance = acct.get("balance") != null ? acct.get("balance").toString() : "";
        String lasttxndate = acct.get("lasttxndate") != null ? acct.get("lasttxndate").toString() : "";
        String serialno = acct.get("serialno") != null ? acct.get("serialno").toString() : "";
        String prevreading = acct.get("reading") != null ? acct.get("reading").toString() : "0";
        String assignee_objid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String assignee_name = SystemPlatformFactory.getPlatform().getSystem().getFullName();
        
        ContentValues values = new ContentValues();
        values.put("objid", objid);
        values.put("state", state);
        values.put("dtstarted", dtstarted);
        values.put("acctno", acctno);
        values.put("applicationid", applicationid);
        values.put("name", name);
        values.put("address", address);
        values.put("cellphoneno", cellphoneno);
        values.put("meterid", meterid);
        values.put("areaid", areaid);
        values.put("area", area);
        values.put("balance", balance);
        values.put("lasttxndate", lasttxndate);
        values.put("serialno", serialno);
        values.put("reading", prevreading);
        values.put("assignee_objid", assignee_objid);
        values.put("assignee_name", assignee_name);
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("account", null, values);
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    } 
    
    @Override
    public void updateAccount(Map acct) {
        ERROR = "";
        String objid = acct.get("objid") != null ? acct.get("objid").toString() : "";
        String state = acct.get("state") != null ? acct.get("state").toString() : "";
        String dtstarted = acct.get("dtstarted") != null ? acct.get("dtstarted").toString() : "";
        String acctno = acct.get("acctno") != null ? acct.get("acctno").toString() : "";
        String applicationid = acct.get("applicationid") != null ? acct.get("applicationid").toString() : "";
        String name = acct.get("name") != null ? acct.get("name").toString() : "";
        String address = acct.get("address") != null ? acct.get("address").toString() : "";
        String cellphoneno = acct.get("cellphoneno") != null ? acct.get("cellphoneno").toString() : "";
        String meterid = acct.get("meterid") != null ? acct.get("meterid").toString() : "";
        String areaid = acct.get("areaid") != null ? acct.get("areaid").toString() : "";
        String area = acct.get("area") != null ? acct.get("area").toString() : "";
        String balance = acct.get("balance") != null ? acct.get("balance").toString() : "";
        String lasttxndate = acct.get("lasttxndate") != null ? acct.get("lasttxndate").toString() : "";
        String serialno = acct.get("serialno") != null ? acct.get("serialno").toString() : "";
        String prevreading = acct.get("reading") != null ? acct.get("reading").toString() : "";
        
        ContentValues values = new ContentValues();
        values.put("objid", objid);
        values.put("state", state);
        values.put("dtstarted", dtstarted);
        values.put("acctno", acctno);
        values.put("applicationid", applicationid);
        values.put("name", name);
        values.put("address", address);
        values.put("cellphoneno", cellphoneno);
        values.put("meterid", meterid);
        values.put("areaid", areaid);
        values.put("area", area);
        values.put("balance", balance);
        values.put("lasttxndate", lasttxndate);
        values.put("serialno", serialno);
        values.put("reading", prevreading);
        
        SQLiteDatabase db = this.getWritableDatabase();
        
        String[] args = new String[]{ objid };
        
        try{
            db.update("account", values, "objid = ?", args);
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }

    @Override
    public Account findAccountByID(String objid) {
        ERROR = "";
        Account account = null;
        String[] args = new String[]{objid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),null,cursor.getString(15),cursor.getString(16));
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return account;
    }
    
    @Override
    public List<Account> getSearchResult(String searchtext) {
        ERROR = "";
        searchtext = searchtext + "%";
        List<Account> list = new ArrayList<Account>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{searchtext, searchtext, searchtext, userid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE (name LIKE ? OR acctno LIKE ? OR serialno LIKE ?) AND assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    String objid = cursor.getString(0);
                    String state = cursor.getString(1);
                    String dtstarted = cursor.getString(2);
                    String acctno = cursor.getString(3);
                    String applicationid = cursor.getString(4);
                    String name = cursor.getString(5);
                    String address = cursor.getString(6);
                    String cellphoneno = cursor.getString(7);
                    String meterid = cursor.getString(8);
                    String areaid = cursor.getString(9);
                    String area = cursor.getString(10);
                    String balance = cursor.getString(11);
                    String lasttxndate = cursor.getString(12);
                    String serialno = cursor.getString(13);
                    String reading = cursor.getString(14);
                    String assignee_objid = cursor.getString(15);
                    String assignee_name = cursor.getString(16);

                    Account acct = new Account(
                        objid,
                        state,
                        dtstarted,
                        acctno,
                        applicationid,
                        name,
                        address,
                        cellphoneno,
                        meterid,
                        areaid,
                        area,
                        balance,
                        lasttxndate,
                        serialno,
                        reading,
                        null,
                        assignee_objid,
                        assignee_name
                    );
                    list.add(acct);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
    @Override
    public List<Account> getResultBySerialno(String serial){
        ERROR = "";
        List<Account> result = new ArrayList<Account>();
        String UserID = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE serialno = '"+serial+"' AND assignee_objid = \'"+UserID+"\'", null);
            if(cursor.moveToFirst()){
                do{
                    String objid = cursor.getString(0);
                    String state = cursor.getString(1);
                    String dtstarted = cursor.getString(2);
                    String acctno = cursor.getString(3);
                    String applicationid = cursor.getString(4);
                    String name = cursor.getString(5);
                    String address = cursor.getString(6);
                    String cellphoneno = cursor.getString(7);
                    String meterid = cursor.getString(8);
                    String areaid = cursor.getString(9);
                    String area = cursor.getString(10);
                    String balance = cursor.getString(11);
                    String lasttxndate = cursor.getString(12);
                    String serialno = cursor.getString(13);
                    String reading = cursor.getString(14);
                    String assignee_objid = cursor.getString(15);
                    String assignee_name = cursor.getString(16);

                    Account acct = new Account(
                        objid,
                        state,
                        dtstarted,
                        acctno,
                        applicationid,
                        name,
                        address,
                        cellphoneno,
                        meterid,
                        areaid,
                        area,
                        balance,
                        lasttxndate,
                        serialno,
                        reading,
                        null,
                        assignee_objid,
                        assignee_name
                    );
                    result.add(acct);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return result;
    }
    
    @Override
    public void deleteAccountByMeter(String meterid){
        try{
            String[] args = new String[]{meterid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM account WHERE meterid = ?",args);
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
    }
    
        @Override
    public int getNoOfTotalRecords() {
        int i = 0;
        try{
            String[] args = new String[]{SystemPlatformFactory.getPlatform().getSystem().getUserID()};
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM account WHERE assignee_objid = ?", args);
            i = c.getCount();
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return i;
    }

    @Override
    public int getNoOfTotalReadRecords() {
        int i = 0;
        try{
            String[] args = new String[]{SystemPlatformFactory.getPlatform().getSystem().getUserID()};
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM account a INNER JOIN reading r ON a.meterid = r.meterid WHERE a.assignee_objid = ?", args);
            i = c.getCount();
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return i;
    }
    
    //################################################################################
    //  READING TABLE
    //################################################################################
    
    @Override
    public void createReading(Reading reading) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("objid", reading.getObjid());
        values.put("meterid", reading.getMeterid());
        values.put("reading", reading.getReading());
        values.put("consumption", reading.getConsumption());
        values.put("amtdue", reading.getAmtDue());
        values.put("totaldue", reading.getTotalDue());
        values.put("state", reading.getState());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("reading", null, values);
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }
    
    @Override
    public Reading findReadingByMeter(String meterid) {
        ERROR = "";
        Reading reading = null;
        String[] args = new String[]{meterid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM reading WHERE meterid = ? AND state = 'OPEN'", args);
            if(cursor.moveToFirst()){
                do{
                    reading = new Reading(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return reading;
    }
    
    @Override
    public void updateMeterReading(Reading reading) {
        ERROR = "";
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("reading", reading.getReading());
        values.put("consumption", reading.getConsumption());
        values.put("amtdue", reading.getAmtDue());
        values.put("totaldue", reading.getTotalDue());
        
        String[] args = new String[]{reading.getMeterid()};
        
        try{
            db.update("reading", values, "meterid = ?", args);
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }
    
    @Override
    public void deleteReadingByMeter(String meterid){
        try{
            String[] args = new String[]{meterid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM reading WHERE meterid = ?",args);
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
    }
    
     @Override
    public List<Reading> getReadingByUser(){
        List<Reading> list = new ArrayList<Reading>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{userid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT r.* FROM account a INNER JOIN reading r ON a.meterid = r.meterid WHERE a.assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    Reading reading = new Reading(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                    if(reading != null) list.add(reading);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }

    @Override
    public String getError() {
        return ERROR;
    }

    @Override
    public List<String> showTableData(String tableName) {
        List<String> list = new ArrayList<String>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName,null);
            if(cursor.moveToFirst()){
                do{
                    int len = cursor.getColumnCount();
                    List l = new ArrayList();
                    for(int a = 0; a < len; a++){
                        l.add(cursor.getColumnName(a)+" : "+cursor.getString(a));
                    }
                    list.add(l.toString());
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
    @Override
    public boolean downloadableArea(String areaid){
        boolean b = true;
        try{
            String[] args = new String[]{areaid};
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account a INNER JOIN reading r ON a.meterid = r.meterid WHERE a.areaid = ? ",args);
            if(cursor.getCount() > 0) b = false;
        }catch(Exception e){
            ERROR = "Database Error: " + e.toString();
        }
        return b;
    }
    
}
