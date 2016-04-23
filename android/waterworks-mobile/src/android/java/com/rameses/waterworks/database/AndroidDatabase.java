package com.rameses.waterworks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Sector;
import com.rameses.waterworks.bean.SectorReader;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
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
                + " serialno VARCHAR(50),"
                + " sectorid VARCHAR(50),"
                + " sectorcode VARCHAR(50),"
                + " lastreading VARCHAR(50),"
                + " classificationid VARCHAR(50),"
                + " barcode VARCHAR(50),"
                + " batchid VARCHAR(50),"
                + " month VARCHAR(50),"
                + " year VARCHAR(50),"
                + " period VARCHAR(50),"
                + " duedate VARCHAR(50),"
                + " discodate VARCHAR(50),"
                + " rundate VARCHAR(50),"
                + " items TEXT,"
                + " info TEXT,"
                + " stuboutid VARCHAR(50),"
                + " sortorder INTEGER,"
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
       
        sqld.execSQL("CREATE TABLE sector ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " code VARCHAR(50),"
                + " assigneeid VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE sectorreader ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " sectorid VARCHAR(50),"
                + " title VARCHAR(50),"
                + " assigneeid VARCHAR(50),"
                + " assigneename VARCHAR(255))");
        
        sqld.execSQL("CREATE TABLE stubout ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " code VARCHAR(50), "
                + " description VARCHAR(255), "
                + " zoneid VARCHAR(50), "
                + " barangayid VARCHAR(50), "
                + " barangayname VARCHAR(255), "
                + " assigneeid VARCHAR(50))");
        
        sqld.execSQL("CREATE TABLE zone ("
                + " objid VARCHAR(50) PRIMARY KEY,"
                + " sectorid VARCHAR(50), "
                + " code VARCHAR(50), "
                + " description VARCHAR(255), "
                + " readerid VARCHAR(50), "
                + " assigneeid VARCHAR(50))");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int i, int i1) {
        sqld.execSQL("DROP TABLE IF EXIST setting");
        sqld.execSQL("DROP TABLE IF EXIST account");
        sqld.execSQL("DROP TABLE IF EXIST reading");
        sqld.execSQL("DROP TABLE IF EXIST rule");
        sqld.execSQL("DROP TABLE IF EXIST area");
        sqld.execSQL("DROP TABLE IF EXIST stubout");
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
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("setting", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }

    @Override
    public void updateSetting(Setting s) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("name", s.getName());
        values.put("value", s.getValue());
        
        String[] args = new String[]{ s.getName() };
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.update("setting", values, "name = ?", args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }

    @Override
    public List<Setting> getAllSettings() {
        ERROR = "";
        List<Setting> list = new ArrayList<Setting>();
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM setting", null);
            if(cursor.moveToFirst()){
                do{
                    Setting setting = new Setting(cursor.getString(0),cursor.getString(1));
                    list.add(setting);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
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
        String serialno = acct.get("serialno") != null ? acct.get("serialno").toString() : "";
        String sectorid = acct.get("sectorid") != null ? acct.get("sectorid").toString() : "";
        String sectorcode = acct.get("sectorcode") != null ? acct.get("sectorcode").toString() : "";
        String lastreading = acct.get("lastreading") != null ? acct.get("lastreading").toString() : "";
        String classificationid = acct.get("classificationid") != null ? acct.get("classificationid").toString() : "";
        String barcode = acct.get("barcode") != null ? acct.get("barcode").toString() : "";
        String batchid = acct.get("batchid") != null ? acct.get("batchid").toString() : "";
        String month = acct.get("month") != null ? getMonth(acct.get("month").toString()) : "";
        String year = acct.get("year") != null ? acct.get("year").toString() : "";
        String duedate = acct.get("duedate") != null ? acct.get("duedate").toString() : "";
        String period = acct.get("period") != null ? acct.get("period").toString() : "";
        String discodate = acct.get("discodate") != null ? acct.get("discodate").toString() : "";
        String rundate = acct.get("rundate") != null ? acct.get("rundate").toString() : "";
        String items = acct.get("items") != null ? acct.get("items").toString() : "";
        String info = acct.get("info") != null ? acct.get("info").toString() : "";
        String stuboutid = acct.get("stuboutid") != null ? acct.get("stuboutid").toString() : "";
        int sortorder = acct.get("sortorder") != null ? Integer.parseInt(acct.get("sortorder").toString()) : -1;
        String assignee_objid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String assignee_name = SystemPlatformFactory.getPlatform().getSystem().getFullName();
        
        ContentValues values = new ContentValues();
        values.put("objid", objid);
        values.put("acctno", acctno);
        values.put("acctname", acctname);
        values.put("address", address);
        values.put("serialno", serialno);
        values.put("sectorid", sectorid);
        values.put("sectorcode", sectorcode);
        values.put("lastreading", lastreading);
        values.put("classificationid", classificationid);
        values.put("barcode", barcode);
        values.put("batchid", batchid);
        values.put("month", month);
        values.put("year", year);
        values.put("period", period);
        values.put("duedate", duedate);
        values.put("discodate", discodate);
        values.put("rundate", rundate);
        values.put("items", items);
        values.put("info",info);
        values.put("stuboutid",stuboutid);
        values.put("sortorder",sortorder);
        values.put("assignee_objid", assignee_objid);
        values.put("assignee_name", assignee_name);
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("account", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    } 
    
    @Override
    public void updateAccount(Map acct) {
        ERROR = "";
        String objid = acct.get("objid") != null ? acct.get("objid").toString() : "";
        String acctno = acct.get("acctno") != null ? acct.get("acctno").toString() : "";
        String acctname = acct.get("acctname") != null ? acct.get("acctname").toString() : "";
        String address = acct.get("address") != null ? acct.get("address").toString() : "";
        String serialno = acct.get("serialno") != null ? acct.get("serialno").toString() : "";
        String sectorid = acct.get("sectorid") != null ? acct.get("sectorid").toString() : "";
        String sectorcode = acct.get("sectorcode") != null ? acct.get("sectorcode").toString() : "";
        String lastreading = acct.get("lastreading") != null ? acct.get("lastreading").toString() : "";
        String classificationid = acct.get("classificationid") != null ? acct.get("classificationid").toString() : "";
        String barcode = acct.get("barcode") != null ? acct.get("barcode").toString() : "";
        String batchid = acct.get("batchid") != null ? acct.get("batchid").toString() : "";
        String month = acct.get("month") != null ? getMonth(acct.get("month").toString()) : "";
        String year = acct.get("year") != null ? acct.get("year").toString() : "";
        String duedate = acct.get("duedate") != null ? acct.get("duedate").toString() : "";
        String period = acct.get("period") != null ? acct.get("period").toString() : "";
        String discodate = acct.get("discodate") != null ? acct.get("discodate").toString() : "";
        String rundate = acct.get("rundate") != null ? acct.get("rundate").toString() : "";
        String items = acct.get("items") != null ? acct.get("items").toString() : "";
        String info = acct.get("info") != null ? acct.get("info").toString() : "";
        String stuboutid = acct.get("stuboutid") != null ? acct.get("stuboutid").toString() : "";
        int sortorder = acct.get("sortorder") != null ? Integer.parseInt(acct.get("sortorder").toString()) : -1;
        String assignee_objid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String assignee_name = SystemPlatformFactory.getPlatform().getSystem().getFullName();
        
        ContentValues values = new ContentValues();
        values.put("objid", objid);
        values.put("acctno", acctno);
        values.put("acctname", acctname);
        values.put("address", address);
        values.put("serialno", serialno);
        values.put("sectorid", sectorid);
        values.put("sectorcode", sectorcode);
        values.put("lastreading", lastreading);
        values.put("classificationid", classificationid);
        values.put("barcode", barcode);
        values.put("batchid", batchid);
        values.put("month", month);
        values.put("year", year);
        values.put("period", period);
        values.put("duedate", duedate);
        values.put("discodate", discodate);
        values.put("rundate", rundate);
        values.put("items", items);
        values.put("info",info);
        values.put("stuboutid",stuboutid);
        values.put("sortorder",sortorder);
        values.put("assignee_objid", assignee_objid);
        values.put("assignee_name", assignee_name);
        
        SQLiteDatabase db = null;
        
        String[] args = new String[]{ objid };
        
        try{
            db = this.getWritableDatabase();
            db.update("account", values, "objid = ?", args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }

    @Override
    public Account findAccountByID(String objid) {
        ERROR = "";
        Account account = null;
        String[] args = new String[]{objid};
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),Integer.parseInt(cursor.getString(20)));
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
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
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE (acctname LIKE ? OR acctno LIKE ? OR serialno LIKE ?) AND assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    Account acct = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),Integer.parseInt(cursor.getString(20)));
                    list.add(acct);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return list;
    }
    
    @Override
    public List<Account> getResultBySerialno(String serial){
        ERROR = "";
        List<Account> result = new ArrayList<Account>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{serial, userid};
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE serialno = ? AND assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    Account acct = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),Integer.parseInt(cursor.getString(20)));
                    result.add(acct);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return result;
    }
    
    @Override
    public void deleteAccountById(String acctid){
        SQLiteDatabase db = null;
        try{
            String[] args = new String[]{acctid};
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM account WHERE objid = ?",args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
        @Override
    public int getNoOfTotalRecords() {
        int i = 0;
        SQLiteDatabase db = null;
        try{
            String[] args = new String[]{SystemPlatformFactory.getPlatform().getSystem().getUserID()};
            db = this.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM account WHERE assignee_objid = ?", args);
            i = c.getCount();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return i;
    }

    @Override
    public int getNoOfTotalReadRecords() {
        int i = 0;
        SQLiteDatabase db = null;
        try{
            String[] args = new String[]{SystemPlatformFactory.getPlatform().getSystem().getUserID()};
            db = this.getWritableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM account a INNER JOIN reading r ON a.objid = r.acctid WHERE a.assignee_objid = ?", args);
            i = c.getCount();
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
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
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("reading", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public Reading findReadingByAccount(String acctid) {
        ERROR = "";
        Reading reading = null;
        String[] args = new String[]{acctid};
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM reading WHERE acctid = ? AND state = 'OPEN'", args);
            if(cursor.moveToFirst()){
                do{
                    reading = new Reading(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return reading;
    }
    
    @Override
    public void updateMeterReading(Reading reading) {
        ERROR = "";
        SQLiteDatabase db = null;
        
        ContentValues values = new ContentValues();
        values.put("reading", reading.getReading());
        values.put("consumption", reading.getConsumption());
        values.put("amtdue", reading.getAmtDue());
        values.put("totaldue", reading.getTotalDue());
        
        String[] args = new String[]{reading.getAcctId()};
        
        try{
            db = this.getWritableDatabase();
            db.update("reading", values, "acctid = ?", args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public void deleteReadingByMeter(String acctid){
        SQLiteDatabase db = null;
        try{
            String[] args = new String[]{acctid};
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM reading WHERE acctid = ?",args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
     @Override
    public List<Reading> getReadingByUser(){
        List<Reading> list = new ArrayList<Reading>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{userid};
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT r.* FROM account a INNER JOIN reading r ON a.objid = r.acctid WHERE a.assignee_objid = ?", args);
            if(cursor.moveToFirst()){
                do{
                    Reading reading = new Reading(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                    if(reading != null) list.add(reading);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
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
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
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
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return list;
    }
    
    @Override
    public void createRule(Rule rule) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("salience", rule.getSalience());
        values.put("condition", rule.getCondition());
        values.put("var", rule.getVar());
        values.put("action", rule.getAction());
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("rule", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public List<Rule> getRules() {
        ERROR = "";
        List<Rule> list = new ArrayList<Rule>();
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM rule ORDER  BY salience DESC", null);
            if(cursor.moveToFirst()){
                do{
                    Rule rule = new Rule(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
                    list.add(rule);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return list;
    }
    
    @Override
    public void clearRule(){
        ERROR = "";
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM rule");
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
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
    public void createSector(Sector s) {
        ERROR = "";
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        
        ContentValues values = new ContentValues();
        values.put("objid", s.getObjid());
        values.put("code", s.getCode());
        values.put("assigneeid", userid);
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("sector", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public void clearSector() {
        ERROR = "";
        SQLiteDatabase db = null;
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM sector WHERE assigneeid = ?",args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public void createSectorReader(SectorReader s) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("objid", s.getObjid());
        values.put("sectorid", s.getSectorId());
        values.put("title", s.getTitle());
        values.put("assigneeid", s.getAssigneeId());
        values.put("assigneename", s.getAssigneeName());
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("sectorreader", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public void clearSectorReader() {
        ERROR = "";
        SQLiteDatabase db = null;
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM sectorreader WHERE assigneeid = ?",args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }

    @Override
    public void createStubout(Stubout s) {
        ERROR = "";
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        
        ContentValues values = new ContentValues();
        values.put("objid", s.getObjid());
        values.put("code", s.getCode());
        values.put("description", s.getDescription());
        values.put("zoneid", s.getZoneId());
        values.put("barangayid", s.getBarangayId());
        values.put("barangayname", s.getBarangayName());
        values.put("assigneeid", userid);
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("stubout", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }

    @Override
    public void clearStubout() {
        ERROR = "";
        SQLiteDatabase db = null;
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM stubout WHERE assigneeid = ?",args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public List<Stubout> getSearchStuboutResult(String searchtext, Zone zone) {
        ERROR = "";
        searchtext = searchtext + "%";
        List<Stubout> list = new ArrayList<Stubout>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{searchtext,zone.getObjid()};
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM stubout WHERE code LIKE ?  AND zoneid = ?", args);
            if(cursor.moveToFirst()){
                do{   
                    Stubout stubout = new Stubout(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                    list.add(stubout);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
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
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM account WHERE (acctname LIKE ? OR acctno LIKE ? OR serialno LIKE ?) AND stuboutid = ? ORDER BY sortorder", args);
            if(cursor.moveToFirst()){
                do{
                    Account acct = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),cursor.getString(13),cursor.getString(14),cursor.getString(15),cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(19),Integer.parseInt(cursor.getString(20)));
                    list.add(acct);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
        return list;
    }
    
    @Override
    public void createZone(Zone z) {
        ERROR = "";
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        
        ContentValues values = new ContentValues();
        values.put("objid", z.getObjid());
        values.put("sectorid", z.getSectorId());
        values.put("code", z.getCode());
        values.put("description", z.getDescription());
        values.put("readerid", z.getReaderId());
        values.put("assigneeid", userid);
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.insert("zone", null, values);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public void clearZone() {
        ERROR = "";
        SQLiteDatabase db = null;
        try{
            String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
            String[] args = new String[]{userid};
            db = this.getWritableDatabase();
            db.execSQL("DELETE FROM zone WHERE assigneeid = ?",args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
    @Override
    public List<Zone> getSearchZoneResult(String searchtext) {
        ERROR = "";
        searchtext = searchtext + "%";
        List<Zone> list = new ArrayList<Zone>();
        String userid = SystemPlatformFactory.getPlatform().getSystem().getUserID();
        String[] args = new String[]{searchtext,userid};
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM zone WHERE code LIKE ?  AND readerid IN (SELECT objid FROM sectorreader WHERE assigneeid = ?)", args);
            if(cursor.moveToFirst()){
                do{   
                    Zone zone = new Zone(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
                    list.add(zone);
                }while(cursor.moveToNext());
            }
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{
                db.close();
            }catch(Throwable t){}
        }
        return list;
    }

    @Override
    public void updateLocation(String acctid, String latitude, String longitude) {
        ERROR = "";
        ContentValues values = new ContentValues();
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        
        String[] args = new String[]{ acctid };
        
        SQLiteDatabase db = null;
        try{
            db = this.getWritableDatabase();
            db.update("account", values, "objid = ?", args);
        }catch(Exception e){
            e.printStackTrace();
            ERROR = "Database Error: " + e.toString();
        }finally{
            try{ db.close(); }catch(Exception e){}
        }
    }
    
}
