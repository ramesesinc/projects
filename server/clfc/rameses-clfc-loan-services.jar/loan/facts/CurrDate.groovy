package loan.facts;

import java.util.*;

public class CurrDate
{
	Date date;

	public CurrDate( o ) {
		if (o.currentdate instanceof Date) {
			date = o.currentdate;
		} else {
			date = java.sql.Date.valueOf(o.currentdate);
		}
	}

	def toMap() {
		return [date: date];
	}
}