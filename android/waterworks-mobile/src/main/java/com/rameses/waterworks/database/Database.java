package com.rameses.waterworks.database;

import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bean.Area;
import com.rameses.waterworks.bean.Rule;
import com.rameses.waterworks.bean.Setting;
import com.rameses.waterworks.bean.Stubout;
import com.rameses.waterworks.bean.Zone;
import java.util.List;
import java.util.Map;

public interface Database {
    
    public String getError();
    
    public List<String> showTableData(String tableName);
    
    //SETTING TABLE
    public void createSetting(Setting s);
    
    public void updateSetting(Setting s);
    
    public List<Setting> getAllSettings();
    
    public boolean settingExist(Setting s);
    
    //ACCOUNT TABLE
    public void createAccount(Map map);
    
    public void updateAccount(Map map);
    
    public Account findAccountByID(String objid);
    
    public List<Account> getSearchAccountResult(String searchtext);
    
    public List<Account> getResultBySerialno(String serialno);
    
    public void deleteAccountById(String acctid);
    
    public int getNoOfTotalRecords();
    
    public int getNoOfTotalReadRecords();
    
    //READING TABLE
    public void createReading(Reading reading);
    
    public Reading findReadingByAccount(String acctid);
    
    public void updateMeterReading(Reading reading);
    
    public void deleteReadingByMeter(String acctid);
    
    public List<Reading> getReadingByUser();
    
    //AREA
    public boolean downloadableArea(String areaid);
    
    //RULE
    public void createRule(Rule rule);
    
    public List<Rule> getRules();
    
    public void clearRule();
    
    public void createArea(Area r);
    
    public void clearArea();
    
    public void createStubout(Stubout s);
    
    public void clearStubout();
    
    public List<Stubout> getSearchStuboutResult(String searchtext,Zone zone);
    
    public List<Account> getAccountByStubout(Stubout s,String searchtext);
    
    public void createZone(Zone zone);
    
    public void clearZone();
    
    public List<Zone> getSearchZoneResult(String searchtext);
    
}
