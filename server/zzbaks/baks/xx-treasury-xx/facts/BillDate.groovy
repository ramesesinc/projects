package treasury.facts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.rameses.rules.common.*;

public class BillDate extends DateFact {
    
    Date validUntil;
    
	public BillDate(String s) {
        super(s);
    }

    public BillDate() {
        super();
    }

    public BillDate(Date d) {
        super(d);
    }
    
}
