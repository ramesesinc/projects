package com.rameses.waterworks.database;

import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.bean.Formula;
import com.rameses.waterworks.bean.Reading;
import com.rameses.waterworks.bean.Setting;
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
    
    public List<Account> getSearchResult(String searchtext);
    
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
    
    //FORMULA
    public void createFormula(Formula formula);
    
    public List<Formula> getFormula();
    
    public boolean formulaExist(Formula formula);
    
    public void updateFormula(Formula formula);
    
    public Formula getFormula(String classification);
    
}
