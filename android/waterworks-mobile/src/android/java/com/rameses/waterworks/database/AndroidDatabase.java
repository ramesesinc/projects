package com.rameses.waterworks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bean.Area;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.StuboutAccount;
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
                + " acctno VARCHAR(50),"
                + " acctname TEXT,"
                + " address TEXT,"
                + " mobileno VARCHAR(50),"
                + " phoneno VARCHAR(50),"
                + " email VARCHAR(50),"
                + " serialno VARCHAR(50),"
                + " areaid VARCHAR(50),"
                + " balance VARCHAR(50),"
                + " penalty VARCHAR(50),"
                + " othercharge VARCHAR(50),"
                + " lastreading VARCHAR(50),"
                + " lasttxndate VARCHAR(50),"
                + " areaname VARCHAR(50),"
                + " classificationid VARCHAR(50),"
                + " lastreadingyear VARCHAR(50),"
                + " lastreadingmonth VARCHAR(50),"
                + " lastreadingdate VARCHAR(50),"
                + " barcode VARCHAR(50),"
                + " batchid VARCHAR(50),"
                + " month VARCHAR(50),"
                + " year VARCHAR(50),"
                + " period VARCHAR(50),"
                + " duedate VARCHAR(50),"
                + " discodate VARCHAR(50),"
                + " rundate VARCHAR(50),"
                + " info TEXT,"
                + " assignee_objid VARCHAR(50),"
                + " assignee_name VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE reading ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " acctid VARCHAR(50), "
                + " reading VARCHAR(50),"
                + " consumption VARCHAR(50),"
                + " amtdue VARCHAR(50),"
                + " totaldue VARCHAR(50), "
                + " state VARCHAR(50), "
                + " dtreading VARCHAR(50), "
                + " batchid VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE rule ("
                + " salience INTEGER ,"
                + " condition TEXT,"
                + " var TEXT,"
                + " action TEXT)");
       
        sqld.execSQL("CREATE TABLE area ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " title VARCHAR(50), "
                + " duedate VARCHAR(50), "
                + " assigneeid VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE stubout ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " title VARCHAR(50), "
                + " description TEXT, "
                + " areaid VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE stubout_account ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " parentid VARCHAR(50), "
                + " acctid TEXT, "
                + " sortorder INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        sqld.execSQL("DROP TABLE IF EXIST setting");
        sqld.execSQL("DROP TABLE IF EXIST account");
        sqld.execSQL("DROP TABLE IF EXIST reading");
        sqld.execSQL("DROP TABLE IF EXIST rule");
        sqld.execSQL("DROP TABLE IF EXIST area");
        sqld.execSQL("DROP TABLE IF EXIST stubout");
        sqld.execSQL("DROP TABLE IF EXIST stubout_account");
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
        String acctno = acct.get("acctno") != null ? acct.get("acctno").toString() : "";
        String acctname = acct.get("acctname") != null ? acct.get("acctname").toString() : "";
        String address = acct.get("address") != null ? acct.get("address").toString() : "";
        String mobileno = acct.get("mobileno") != null ? acct.get("mobileno").toString() : "";
        String phoneno = acct.get("phoneno") != null ? acct.get("phoneno").toString() : "";
        String email = acct.get("email") != null ? acct.get("email").toString() : "";
        String serialno = acct.get("serialno") != null ? acct.get("serialno").toString() : "";
        String areaid = acct.get("areaid") != null ? acct.get("areaid").toString() : "";
        String balance = acct.get("balance") != null ? acct.get("balance").toString() : "0.00";
        String penalty = acct.get("penalty") != null ? acct.get("penalty").toString() : "0.00";
        String othercharge = acct.get("othercharge") != null ? acct.get("othercharge").toString() : "0.00";
        String lastreading = acct.get("lastreading") != null ? acct.get("lastreading").toString() : "";
        String lasttxndate = acct.get("lasttxndate") != null ? acct.get("lasttxndate").toString() : "";
        String areaname = acct.get("areaname") != null ? acct.get("areaname").toString() : "";
        String classificationid = acct.get("classificationid") != null ? acct.get("classificationid").toString() : "";
        String lastreadingyear = acct.get("lastreadingyear") != null ? acct.get("lastreadingyear").toString() : "";
        String lastreadingmonth = acct.get("lastreadingmonth") != null ? acct.get("lastreadingmonth").toString() : "";
        String lastreadingdate = acct.get("lastreadingdate") != null ? acct.get("lastreadingdate").toString() : "";
        String barcode = acct.get("barcode") != null ? acct.get("barcode").toString() : "";
        String assignee_objid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String assignee_name = SystemPlatformFactory.getPlatform().getSystem().getFullName();
        String batchid = acct.get("batchid") != null ? acct.get("batchid").toString() : "";
        String month = acct.get("month") != null ? getMonth(acct.get("month").toString()) : "";
        String year = acct.get("year") != null ? acct.get("year").toString() : "";
        String fromdate = acct.get("fromdate") != null ? acct.get("fromdate").toString() : "";
        String todate = acct.get("todate") != null ? acct.get("todate").toString() : "";
        String duedate = acct.get("duedate") != null ? acct.get("duedate").toString() : "";
        String discodate = acct.get("discodate") != null ? acct.get("discodate").toString() : "";
        String rundate = acct.get("rundate") != null ? acct.get("rundate").toString() : "";
        String info = acct.get("info") != null ? acct.get("info").toString() : "";
        
        ContentValues values = new ContentValues();
        values.put("objid", objid);
        values.put("acctno", acctno);
        values.put("acctname", acctname);
        values.put("address", address);
        values.put("mobileno", mobileno);
        values.put("phoneno", phoneno);
        values.put("email", email);
        values.put("serialno", serialno);
        values.put("areaid", areaid);
        values.put("balance", balance);
        values.put("penalty", penalty);
        values.put("othercharge", othercharge);
        values.put("lastreading", lastreading);
        values.put("lasttxndate", lasttxndate);
        values.put("areaname", areaname);
        values.put("classificationid", classificationid);
        values.put("lastreadingyear", lastreadingyear);
        values.put("lastreadingmonth", lastreadingmonth);
        values.put("lastreadingdate", lastreadingdate);
        values.put("barcode", barcode);
        values.put("assignee_objid", assignee_objid);
        values.put("assignee_name", assignee_name);
        values.put("batchid", batchid);
        values.put("month", month);
        values.put("year", year);
        values.put("period", fromdate + " - " + todate);
        values.put("duedate", duedate);
        values.put("discodate", discodate);
        values.put("rundate", rundate);
        values.put("info", info);
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("account", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    } 
    
    @Override
    public void updateAccount(Map acct) {
        ERROR = "";
        String objid = acct.get("objid") != null ? acct.get("objid").toString() : "";
        String acctno = acct.get("acctno") != null ? acct.get("acctno").toString() : "";
        String acctname = acct.get("acctname") != null ? acct.get("acctname").toString() : "";
        String address = acct.get("address") != null ? acct.get("address").toString() : "";
        String mobileno = acct.get("mobileno") != null ? acct.get("mobileno").toString() : "";
        String phoneno = acct.get("phoneno") != null ? acct.get("phoneno").toString() : "";
        String email = acct.get("email") != null ? acct.get("email").toString() : "";
        String serialno = acct.get("serialno") != null ? acct.get("serialno").toString() : "";
        String areaid = acct.get("areaid") != null ? acct.get("areaid").toString() : "";
        String balance = acct.get("balance") != null ? acct.get("balance").toString() : "0.00";
        String penalty = acct.get("penalty") != null ? acct.get("penalty").toString() : "0.00";
        String othercharge = acct.get("othercharge") != null ? acct.get("othercharge").toString() : "0.00";
        String lastreading = acct.get("lastreading") != null ? acct.get("lastreading").toString() : "";
        String lasttxndate = acct.get("lasttxndate") != null ? acct.get("lasttxndate").toString() : "";
        String areaname = acct.get("areaname") != null ? acct.get("areaname").toString() : "";
        String classificationid = acct.get("classificationid") != null ? acct.get("classificationid").toString() : "";
        String lastreadingyear = acct.get("lastreadingyear") != null ? acct.get("lastreadingyear").toString() : "";
        String lastreadingmonth = acct.get("lastreadingmonth") != null ? acct.get("lastreadingmonth").toString() : "";
        String lastreadingdate = acct.get("lastreadingdate") != null ? acct.get("lastreadingdate").toString() : "";
        String barcode = acct.get("barcode") != null ? acct.get("barcode").toString() : "";
        String batchid = acct.get("batchid") != null ? acct.get("batchid").toString() : "";
        String month = acct.get("month") != null ? acct.get("month").toString() : "";
        String year = acct.get("year") != null ? acct.get("year").toString() : "";
        String fromdate = acct.get("fromdate") != null ? acct.get("fromdate").toString() : "";
        String todate = acct.get("todate") != null ? acct.get("todate").toString() : "";
        String duedate = acct.get("duedate") != null ? acct.get("duedate").toString() : "";
        String discodate = acct.get("discodate") != null ? acct.get("discodate").toString() : "";
        String rundate = acct.get("rundate") != null ? acct.get("rundate").toString() : "";
        String info = acct.get("info") != null ? acct.get("info").toString() : "";
        
        ContentValues values = new ContentValues();
        values.put("objid", objid);
        values.put("acctno", acctno);
        values.put("acctname", acctname);
        values.put("address", address);
        values.put("mobileno", mobileno);
        values.put("phoneno", phoneno);
        values.put("email", email);
        values.put("serialno", serialno);
        values.put("areaid", areaid);
        values.put("balance", balance);
        values.put("penalty", penalty);
        values.put("othercharge", othercharge);
        values.put("lastreading", lastreading);
        values.put("lasttxndate", lasttxndate);
        values.put("areaname", areaname);
        values.put("classificationid", classificationid);
        values.put("lastreadingyear", lastreadingyear);
        values.put("lastreadingmonth", lastreadingmonth);
        values.put("lastreadingdate", lastreadingdate);
        values.put("barcode", barcode);
        values.put("batchid", batchid);
        values.put("month", month);
        values.put("year", year);
        values.put("period", fromdate + " - " + todate);
        values.put("duedate", duedate);
        values.put("discodate", discodate);
        values.put("rundate", rundate);
        values.put("info", info);
        
        SQLiteDatabase db = this.getWritableDatabase();
        
        String[] args = new String[]{ objid };
        
        try{
            db.update("account", values, "objid = ?", args);
        }catch(Exception e){
            e.printStackTrace();
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
                    account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),cursor.getString(20),cursor.getString(21),cursor.getString(22),cursor.getString(23),cursor.getString(24),cursor.getString(25),cursor.getString(26),cursor.getString(27));
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return account;
    }
    
    @Override
    public List<Account> getSearchAccountResult(String searchtext) {
        ERROR = "";
        searchtext = searchtext + "%";
        List<Account> list = new ArrayList<Account>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{searchtext, searchtext, searchtext, userid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE (acctname LIKE ? OR acctno LIKE ? OR serialno LIKE ?) AND assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    String objid = cursor.getString(0);
                    String acctno = cursor.getString(1);
                    String acctname = cursor.getString(2);
                    String address = cursor.getString(3);
                    String mobileno = cursor.getString(4);
                    String phoneno = cursor.getString(5);
                    String email = cursor.getString(6);
                    String serialno = cursor.getString(7);
                    String areaid = cursor.getString(8);
                    String balance = cursor.getString(9);
                    String penalty = cursor.getString(10);
                    String othercharge = cursor.getString(11);
                    String lastreading = cursor.getString(12);
                    String lasttxndate = cursor.getString(13);
                    String areaname = cursor.getString(14);
                    String classificationid = cursor.getString(15);
                    String lastreadingyear = cursor.getString(16);
                    String lastreadingmonth = cursor.getString(17);
                    String lastreadingdate = cursor.getString(18);
                    String barcode = cursor.getString(19);
                    String batchid = cursor.getString(20);
                    String month = cursor.getString(21);
                    String year = cursor.getString(22);
                    String period = cursor.getString(23);
                    String duedate = cursor.getString(24);
                    String discodate = cursor.getString(25);
                    String rundate = cursor.getString(26);
                    String info = cursor.getString(27);

                    Account acct = new Account(
                        objid,
                        acctno,
                        acctname,
                        address,
                        mobileno,
                        phoneno,
                        email,
                        serialno,
                        areaid,
                        balance,
                        penalty,
                        othercharge,
                        lastreading,
                        lasttxndate,
                        areaname,
                        classificationid,
                        lastreadingyear,
                        lastreadingmonth,
                        lastreadingdate,
                        barcode,
                        batchid,
                        month,
                        year,
                        period,
                        duedate,
                        discodate,
                        rundate,
                        info
                    );
                    list.add(acct);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
    @Override
    public List<Account> getResultBySerialno(String serial){
        ERROR = "";
        List<Account> result = new ArrayList<Account>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{serial, userid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE serialno = ? AND assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    String objid = cursor.getString(0);
                    String acctno = cursor.getString(1);
                    String acctname = cursor.getString(2);
                    String address = cursor.getString(3);
                    String mobileno = cursor.getString(4);
                    String phoneno = cursor.getString(5);
                    String email = cursor.getString(6);
                    String serialno = cursor.getString(7);
                    String areaid = cursor.getString(8);
                    String balance = cursor.getString(9);
                    String penalty = cursor.getString(10);
                    String othercharge = cursor.getString(11);
                    String lastreading = cursor.getString(12);
                    String lasttxndate = cursor.getString(13);
                    String areaname = cursor.getString(14);
                    String classificationid = cursor.getString(15);
                    String lastreadingyear = cursor.getString(16);
                    String lastreadingmonth = cursor.getString(17);
                    String lastreadingdate = cursor.getString(18);
                    String barcode = cursor.getString(19);
                    String batchid = cursor.getString(20);
                    String month = cursor.getString(21);
                    String year = cursor.getString(22);
                    String period = cursor.getString(23);
                    String duedate = cursor.getString(24);
                    String discodate = cursor.getString(25);
                    String rundate = cursor.getString(26);
                    String info = cursor.getString(27);

                    Account acct = new Account(
                        objid,
                        acctno,
                        acctname,
                        address,
                        mobileno,
                        phoneno,
                        email,
                        serialno,
                        areaid,
                        balance,
                        penalty,
                        othercharge,
                        lastreading,
                        lasttxndate,
                        areaname,
                        classificationid,
                        lastreadingyear,
                        lastreadingmonth,
                        lastreadingdate,
                        barcode,
                        batchid,
                        month,
                        year,
                        period,
                        duedate,
                        discodate,
                        rundate,
                        info
                    );
                    result.add(acct);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return result;
    }
    
    @Override
    public void deleteAccountById(String acctid){
        try{
            String[] args = new String[]{acctid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM account WHERE objid = ?",args);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
            Cursor c = db.rawQuery("SELECT * FROM account a INNER JOIN reading r ON a.objid = r.acctid WHERE a.assignee_objid = ?", args);
            i = c.getCount();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
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
        String date = SystemPlatformFactory.getPlatform().getSystem().getDate();
        ContentValues values = new ContentValues();
        values.put("objid", reading.getObjid());
        values.put("acctid", reading.getAcctId());
        values.put("reading", reading.getReading());
        values.put("dtreading", date);
        values.put("consumption", reading.getConsumption());
        values.put("amtdue", reading.getAmtDue());
        values.put("totaldue", reading.getTotalDue());
        values.put("state", reading.getState());
        values.put("batchid", reading.getBatchId());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("reading", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }
    
    @Override
    public Reading findReadingByAccount(String acctid) {
        ERROR = "";
        Reading reading = null;
        String[] args = new String[]{acctid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM reading WHERE acctid = ? AND state = 'OPEN'", args);
            if(cursor.moveToFirst()){
                do{
                    reading = new Reading(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
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
        
        String[] args = new String[]{reading.getAcctId()};
        
        try{
            db.update("reading", values, "acctid = ?", args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }
    
    @Override
    public void deleteReadingByMeter(String acctid){
        try{
            String[] args = new String[]{acctid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM reading WHERE acctid = ?",args);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
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
            Cursor cursor = db.rawQuery("SELECT r.* FROM account a INNER JOIN reading r ON a.objid = r.acctid WHERE a.assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    Reading reading = new Reading(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                    if(reading != null) list.add(reading);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
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
            e.printStackTrace();
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
            Cursor cursor = db.rawQuery("SELECT * FROM account a INNER JOIN reading r ON a.objid = r.acctid WHERE a.areaid = ? ",args);
            if(cursor.getCount() > 0) b = false;
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return b;
    }
    
    @Override
    public void createRule(Rule rule) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("salience", rule.getSalience());
        values.put("condition", rule.getCondition());
        values.put("var", rule.getVar());
        values.put("action", rule.getAction());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("rule", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }
    
    @Override
    public List<Rule> getRules() {
        ERROR = "";
        List<Rule> list = new ArrayList<Rule>();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM rule ORDER  BY salience DESC", null);
            if(cursor.moveToFirst()){
                do{
                    Rule rule = new Rule(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                    list.add(rule);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
    @Override
    public void clearRule(){
        ERROR = "";
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM rule");
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
    }
    
    String getMonth(String m){
        String result = "?";
        try{
            int month = Integer.parseInt(m);
            if(month == 1) result = "January";
            if(month == 2) result = "February";
            if(month == 3) result = "March";
            if(month == 4) result = "April";
            if(month == 5) result = "May";
            if(month == 6) result = "June";
            if(month == 7) result = "July";
            if(month == 8) result = "August";
            if(month == 9) result = "September";
            if(month == 10) result = "October";
            if(month == 11) result = "November";
            if(month == 12) result = "December";
        }catch(Exception e){}
        return result;
    }

    @Override
    public void createReadingGroup(Area r) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("objid", r.getObjid());
        values.put("title", r.getTitle());
        values.put("duedate", r.getDueDate());
        values.put("assigneeid", r.getAssigneeId());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("area", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }

    @Override
    public void createStubout(Stubout s) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("objid", s.getObjid());
        values.put("title", s.getTitle());
        values.put("description", s.getDescription());
        values.put("areaid", s.getAreaId());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("stubout", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }

    @Override
    public void createStuboutAccount(StuboutAccount sa) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("objid", sa.getObjid());
        values.put("parentid", sa.getParentId());
        values.put("acctid", sa.getAcctId());
        values.put("sortorder", sa.getSortOrder());
        
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            db.insert("stubout_account", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        db.close();
    }

    @Override
    public void clearReadingGroup() {
        ERROR = "";
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM area where assigneeid = ?",args);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
    }

    @Override
    public void clearStubout() {
        ERROR = "";
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM stubout WHERE areaid IN (SELECT objid FROM area WHERE assigneeid = ?)",args);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
    }

    @Override
    public void clearStuboutAccount() {
        ERROR = "";
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM stubout_account WHERE parentid IN (SELECT s.objid FROM stubout s INNER JOIN area a ON s.areaid = a.objid WHERE a.assigneeid = ?)",args);
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
    }
    
    @Override
    public List<Stubout> getSearchStuboutResult(String searchtext) {
        ERROR = "";
        searchtext = searchtext + "%";
        List<Stubout> list = new ArrayList<Stubout>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{searchtext, userid};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT s.* FROM stubout s INNER JOIN area g ON s.areaid = g.objid WHERE s.title LIKE ? AND g.assigneeid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    String objid = cursor.getString(0);
                    String title = cursor.getString(1);
                    String description = cursor.getString(2);
                    String areaid = cursor.getString(3);
                    
                    Stubout stubout = new Stubout(objid, title, description, areaid, null);
                    list.add(stubout);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
    @Override
    public List<Account> getAccountByStubout(Stubout s,String searchtext) {
        ERROR = "";
        searchtext = searchtext + "%";
        List<Account> list = new ArrayList<Account>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{searchtext, searchtext, searchtext, s.getObjid()};
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT a.* FROM account a INNER JOIN stubout_account sa ON a.objid = sa.acctid INNER JOIN stubout s ON sa.parentid = s.objid WHERE (a.acctname LIKE ? OR a.acctno LIKE ? OR a.serialno LIKE ?) AND s.objid = ? ORDER BY sa.sortorder", args);
            if(cursor.moveToFirst()){
                do{
                    String objid = cursor.getString(0);
                    String acctno = cursor.getString(1);
                    String acctname = cursor.getString(2);
                    String address = cursor.getString(3);
                    String mobileno = cursor.getString(4);
                    String phoneno = cursor.getString(5);
                    String email = cursor.getString(6);
                    String serialno = cursor.getString(7);
                    String areaid = cursor.getString(8);
                    String balance = cursor.getString(9);
                    String penalty = cursor.getString(10);
                    String othercharge = cursor.getString(11);
                    String lastreading = cursor.getString(12);
                    String lasttxndate = cursor.getString(13);
                    String areaname = cursor.getString(14);
                    String classificationid = cursor.getString(15);
                    String lastreadingyear = cursor.getString(16);
                    String lastreadingmonth = cursor.getString(17);
                    String lastreadingdate = cursor.getString(18);
                    String barcode = cursor.getString(19);
                    String batchid = cursor.getString(20);
                    String month = cursor.getString(21);
                    String year = cursor.getString(22);
                    String period = cursor.getString(23);
                    String duedate = cursor.getString(24);
                    String discodate = cursor.getString(25);
                    String rundate = cursor.getString(26);
                    String info = cursor.getString(27);

                    Account acct = new Account(
                        objid,
                        acctno,
                        acctname,
                        address,
                        mobileno,
                        phoneno,
                        email,
                        serialno,
                        areaid,
                        balance,
                        penalty,
                        othercharge,
                        lastreading,
                        lasttxndate,
                        areaname,
                        classificationid,
                        lastreadingyear,
                        lastreadingmonth,
                        lastreadingdate,
                        barcode,
                        batchid,
                        month,
                        year,
                        period,
                        duedate,
                        discodate,
                        rundate,
                        info
                    );
                    list.add(acct);
                }while(cursor.moveToNext());
            }
            db.close();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }
        return list;
    }
    
}
