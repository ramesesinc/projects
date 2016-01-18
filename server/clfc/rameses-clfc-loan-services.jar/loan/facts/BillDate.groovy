package loan.facts;

import java.util.*;

public class BillDate {
	
	Date date;

	public BillDate(def m ) {
		if (!m.billdate) this.date = new Date();
		else if (m.billdate instanceof Date) {
			this.date = m.billdate;
		} else {
			this.date = java.sql.Date.valueOf(m.billdate);
		}
	}
}