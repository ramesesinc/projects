package market.facts;

import java.util.*;
import com.rameses.util.*;

public class BillInfo {
    
    Date startdate;
    Date billdate;   
    double rate;
    int type;
    int day;
    
    public BillInfo(Map map) {
    	if(map.rate) {
    		this.rate = Double.parseDouble(map.rate+"");
    	}
        def df = new DateBean( map.startdate );
        this.startdate = df.date;
        this.day = df.day;

        if(map.lastyearmonthpaid?.iyear!=null ) {
    	    def t = map.lastyearmonthpaid; 
            if(t.balance == 0 ) {
                t.imonth = t.imonth + 1;
                if(t.imonth > 12) {
                    t.imonth = 1;
                    t.iyear = t.iyear + 1;
                } 
            }
            def sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            this.startdate = sdf.parse( t.iyear + "-"+t.imonth +"-01" );
        } 
        def df1 = new DateBean( (map.billdate)? map.billdate : map.today );
        this.billdate = df1.date;
 		if(!map.billingtype) map.billingtype = "0";
       	type = Integer.parseInt( map.billingtype + "" );   
    }



}