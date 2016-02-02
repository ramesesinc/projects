package treasury.facts;

import java.util.*;
import com.rameses.util.*;

public class BillItem {

    def item;                       //store the original value
    Date duedate;
    def refid;
    int sortorder = 0;
    int pmtorder = 0;
    

    boolean compromise;             //if true, this line is under compromise so no penalty
    double amtdue = 0;              //the original amount due
    double amount = 0;              //amount paid
    double discount = 0;            //discount
    double surcharge = 0;           //surcharge paid
    double interest = 0;            //interest paid
    def category;

    Account account;                //the principal account
    Account surchargeAccount;       //surcharge account
    Account interestAccount;        //interest account
    Account discountAccount;        //discount account

    public double getTotal() {
        return NumberUtil.round( (amount - discount) + surcharge + interest);
    }

    public BillItem() {
    }
        
    //for display
    def toItem() {
        return [
            item: account.toItem(),
            refid: refid,
            amtdue: amtdue,
            amount: amount,
            discount: discount,
            surcharge:surcharge,
            interest: interest,
            total: total,
            duedate: duedate,
            compromise: compromise,
            sortorder: sortorder
        ];
    }
    
    //if amtpaid less than total, we need to correct how much proportion is applied to 
    void applyPayment(double d) {
        if( d < total ) {
            double _total = total;
            amount = NumberUtil.round( (amount / _total) * d );
            if( discount > 0.0) {
                discount = NumberUtil.round( (discount / _total) * d );
            }    
            if( surcharge > 0.0 ) {
                surcharge = NumberUtil.round( (surcharge / _total) * d );
            }
            if( interest > 0.0 ) {
                interest = NumberUtil.round( d - (amount - discount) - surcharge );
            }
        }
    }

    public boolean equals(def obj) {
        return hashCode() == obj.hashCode();
    }    

    public int hashCode() {
        if( category!=null ) {
            return (category + ":" + account.objid).hashCode();        
        }
        else {
            return account.objid.hashCode();
        }
    }

}
