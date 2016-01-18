package market.facts;

import java.util.*;
import com.rameses.util.*;

public class BillItem {

    double amount;
    private Date duedate;
    int month;
    int year;

    double amtdue;
    boolean expired;
    double surcharge = 0;
    double interest = 0;
    boolean compromise;
    double amtpaid = 0;

    Date date;


    public BillItem( Date duedate, double rate ) {
        setDuedate( duedate );
        amount = rate;
        amtdue = rate;
    }


    public double getTotal() {
        return NumberUtil.round(amtdue + surcharge + interest);
    }

    def toItem() {
        return [
            amount: amount,
            amtdue: amtdue,
            expired: expired,
            surcharge:surcharge,
            interest: interest,
            total: total,
            duedate: duedate,
            amtpaid: amtpaid,
            compromise: compromise,
            year: year,
            month: month
        ]
    }
    
    void updatePayment(double d) {
        this.amtpaid = d;
        //if amtpaid less than total, we need to correct how much proportion is applied to 
        if( amtpaid < total ) {
            double _total = total;
            amtdue = NumberUtil.round( amtdue / _total * amtpaid);    
            if( surcharge > 0.0 ) {
                surcharge = NumberUtil.round( surcharge / _total * amtpaid );
            }
            if( interest > 0.0 ) {
                interest = NumberUtil.round( amtpaid - amtdue - surcharge );
            }
        }
    }

    public void setDuedate( Date d ) {
        def db = new DateBean(d);    
        this.month = db.month;
        this.year = db.year;
	    def df = new java.text.SimpleDateFormat("yyyy-MM-dd");
        this.date = df.parse( this.year + "-" + this.month + "-01" );
        //this.duedate = df.parse( this.year + "-" + this.month + "-20" ); 
        this.duedate = getMonthEndDate( this.date ); 
    } 

    public Date getDuedate() {
        return this.duedate;        
    }


    private def getMonthEndDate( dt ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int d = cal.getActualMaximum( Calendar.DAY_OF_MONTH );
        cal.set( Calendar.DAY_OF_MONTH, d );
        return cal.getTime();
    }
}
