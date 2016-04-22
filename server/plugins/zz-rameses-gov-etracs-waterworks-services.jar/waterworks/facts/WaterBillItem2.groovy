package waterworks.facts;

import java.util.*;
import com.rameses.util.*;
import treasury.facts.*;

public class WaterBillItem extends BillItem {

	int volume;
    int month;
    int year;
    String smonth;
    Date duedate;

    int prevreading;
    int reading;

    //for display
    def toItem() {
        def m = super.toItem();
        m.volume = volume;
        m.year = year;
        m.month = month;
        m.smonth = smonth;
        m.duedate = duedate;
        m.prevreading = prevreading;
        m.reading = reading;
        return m;
    }

    int getSortorder() {
        return (year*100)+month;
    }

    int getPmtorder() {
        return this.getSortorder();
    }

    public int hashCode() {
        return (" " + year + "-" + month + "-" + account?.objid?.toString()).hashCode();
    }

}
