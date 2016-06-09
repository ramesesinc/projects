package treasury.facts;

import java.util.*;
import com.rameses.util.*;

public class BillItem {

    def item;                       //store the original value
    Date duedate;
    def refid;
    int sortorder = 0;
    int pmtorder = 0;

    //if payment is required this must be paid based on its order. We cannot choose to pay what we want.
    boolean required = true;    

    boolean compromise;             //if true, this line is under compromise so no penalty
    double amtdue = 0;              //the original amount due
    double amount = 0;              //amount paid
    double discount = 0;            //discount
    double surcharge = 0;           //surcharge paid
    double interest = 0;            //interest paid
    def category;

    String title;                   //this is used in lieu of account. if account is not specified.
    String remarks;             
    String txntype;                 //this is used for short codes. System specified txntype

    Account account;                //the principal account
    Account surchargeAccount;       //surcharge account
    Account interestAccount;        //interest account
    Account discountAccount;        //discount account

    public double getTotal() {
        return NumberUtil.round( (amount - discount) + surcharge + interest);
    }

    //for display
    def toItem() {
        return [
            item: account?.toItem(),
            refid: refid,
            amtdue: amtdue,
            amount: amount,
            discount: discount,
            surcharge:surcharge,
            interest: interest,
            total: total,
            duedate: duedate,
            compromise: compromise,
            sortorder: sortorder,
            title: title,
            txntype: txntype,
            remarks: remarks
        ];
    }
    

    public boolean equals(def obj) {
        return hashCode() == obj.hashCode();
    }    

    public int hashCode() {
        if( category!=null  && account?.objid!=null) {
            return (category + ":" + account.objid).hashCode();        
        }
        else if(account?.objid!=null){
            return account.objid.hashCode();
        }
        else {
            return super.hashCode();
        }
    }

}
