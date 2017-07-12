package treasury.utils;

import treasury.facts.*;

public class MonthEntryBuilder {
	

	 public static List buildMonthEntries( Date fromdate, Date todate ) {
        def stack = new Stack();
        def cal = Calendar.instance;
        int idx = 1;
        (fromdate..todate).each {
            cal.setTime( it);
            int month = cal.get( Calendar.MONTH )+1;
            int year = cal.get( Calendar.YEAR );
            int day = cal.get( Calendar.DATE );
            if( stack.empty() || stack.peek().month != month ) {
                def me = new MonthEntry();
                me.month = month;
                me.index = (idx++);
                me.fromdate = it;
                me.fromday = day;     
                me.year = year;   
                me.maxdays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);   
                stack.push( me );         
            }
            stack.peek().numdays++;
            stack.peek().todate = it;
            stack.peek().today = it.date;   
        }
        def list = [];
        while( !stack.empty() ) {
            def st = stack.pop();
            list.add( 0, st );
        }
        list.first().first = true;
        list.last().last = true;
        return list;
    } 


    public static List buildDailyMonthEntryByAmount(  Date startdate, double dividend, double divisor ) {
        def cal = Calendar.instance;
        cal.setTime( startdate );

        //determine number of days to add
        int days = (int)(dividend / divisor);
        if( dividend % divisor  > 0) days = days + 1;
        cal.add( Calendar.DATE, days );
        def todate = cal.getTime();
        return buildMonthEntries(startdate, todate );
    }

}