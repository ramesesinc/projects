package com.rameses.waterworks.printer;

import application.WaterworksBillCalculator;
import com.rameses.waterworks.bean.Account;
import com.rameses.waterworks.util.SystemPlatformFactory;
import java.text.DecimalFormat;

public class OneilPrinterHandler implements PrinterHandler{
    
    StringBuilder sb;
    
    public OneilPrinterHandler(Account a){
        sb = new StringBuilder();
        sb.append("EZ{PRINT,");
        sb.append("@0,0:MF204|    SAN CARLOS CITY WATER DISTRICT       |");
        sb.append("@30,0:MF204|     SAN CARLOS CITY WATER DISTRICT      |");
        sb.append("@60,0:MF204|   SAN CARLOS CITY, NEGROS OCCIDENTAL    |");
        sb.append("@90,0:MF204|             (034) 7293131               |");
        sb.append("@150,0:MF204|For the month of Jan 2016               |");
        sb.append("@180,0:MF204|---------------------------------------|");
        sb.append("@210,0:MF204|Acct No.   :  "+a.getAcctno()+"|");
        sb.append("@240,0:MF204|Meter No.  :  "+a.getSerialno()+"|");
        sb.append("@270,0:MF204|Class Type :  RESIDENTIAL|");
        sb.append("@300,0:MF204|"+a.getName()+"|");
        sb.append("@330,0:MF204|"+a.getAddress()+"|");
        sb.append("@360,0:MF204|---------------------------------------|");
        sb.append("@390,0:MF204|Prev Reading   :  "+a.getPrevReading()+"|");
        sb.append("@420,0:MF204|Pres Reading   :  "+a.getPresReading()+"|");
        sb.append("@450,0:MF204|Consumption    :  "+a.getConsumption()+"|");
        sb.append("@480,0:MF204|---------------------------------------|");
        sb.append("@510,0:MF107|Balance|");
        sb.append("@510,265:MF107|"+a.getBalance()+"|");
        sb.append("@560,0:MF107|Charge|");
        sb.append("@560,265:MF107|"+a.getAmtDue()+"|");
        sb.append("@610,0:MF107|TOTAL DUE|");
        sb.append("@610,265:MF107|"+a.getTotalDue()+"|");
        sb.append("@640,0:MF204|---------------------------------------|");
        sb.append("@670,0:MF204|Period       :  12/2/2015 - 1/1/2016|");
        sb.append("@700,0:MF204|Due Date     :  01/15/2016|");
        sb.append("@730,0:MF204|DiscoDate    :  01/30/2016|");
        sb.append("@770,0:MF204|Meter Reader :  "+SystemPlatformFactory.getPlatform().getSystem().getFullName()+"|");
        sb.append("@800,0:MF204|Run Date     :  "+SystemPlatformFactory.getPlatform().getSystem().getDate()+" "+SystemPlatformFactory.getPlatform().getSystem().getTime()+"|");
        sb.append("@830,0:MF204|---------------------------------------|");
        sb.append("@870,0:MF226|*Service may be disconnected 5 days after|");
        sb.append("@900,0:MF226|due date w/o prior notice if bill is not|");
        sb.append("@930,0:MF226|paid.|");
        sb.append("@970,0:MF226|*Please pay your water bill before due|");
        sb.append("@1000,0:MF226|date to avoid paying penalty|");
        sb.append("@1030,0:MF226|*Disregard arrears if payment has been|");
        sb.append("@1070,0:MF226|made.|");
        sb.append("@1100,35:BC39N,HIGH 15,WIDE 2|51030-"+a.getAcctno()+"|");
        sb.append("@1180,35:MF204|51030-"+a.getAcctno()+"|");
        sb.append("@1260,2:MF204|.|");
        sb.append("}");
    }

    @Override
    public String getData() {
        return sb.toString();
    }
    
}
