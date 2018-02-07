package treasury.facts;

import java.util.*;

public class CompromiseBillItem extends BillItem {

	int year;
	int month;
	Date duedate;

	public int hashCode() {
		return (super.hashCode() + (year+"-"+month)).hashCode();			
	}

    public int getPaypriority() {
       return (year*12)+month;
    }

}