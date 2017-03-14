package market.facts;

import java.util.*;
import treasury.facts.*;
import com.rameses.util.*;

public class MarketBillItem extends BillItem {
    
    MarketRentalUnit marketunit;
    MarketMonthEntry monthentry;

    int month;
    int year;
    int days;
    Date date;
    Date duedate;
    Date fromdate;
    Date todate;
    int index;

    def monthNames = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];
    String txntype = "market";

    public int getPaypriority() {
       return (year*12)+month;
    }

    public String getMonthname() {
        return monthNames[ month - 1 ]; 
    }

    public def toMap() {
        def m = super.toMap();
        m.month = month;
        m.year = year;
        m.days = days;
        m.date = date;
        m.duedate = duedate;
        m.fromdate = fromdate;
        m.todate = todate;
        m.monthname = monthname; 
        m.sortorder = (year*12)+month;

        m.linetotal = total;
        m.rate = marketunit.rate;
        m.extrate = marketunit.extrate;
        return m;
    }

    public int hashCode() {
        return super.hashCode() + (this.year + "-" + this.month + "-" + this.fromdate + "-" + this.todate).hashCode(); 
    }

    //during the update partial we need to update the number of days and todate for
    //pay frequency that is not monthly.
    public void recalc() {
        if( marketunit.payfrequency != 'MONTHLY' ) {
            int correctedDays = (int)(this.amount / marketunit.rate);
            if( (this.amount % marketunit.rate) > 0 ) correctedDays += 1;
            this.days = correctedDays;
            todate = DateUtil.add( this.fromdate, (this.days-1) +"d");
        }
    } 

}