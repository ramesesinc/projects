package treasury.facts;

class MonthEntry {

   int numdays;
   Date fromdate;
   Date todate;
   int month;
   int year;
   int fromday;
   int today;
   int index;  //this is base 1. indicates position if first or last entry
   int maxdays;

   def monthNames = ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'];

   int _qtr = 0;

   boolean first = false;
   boolean last = false;
   int getMonthYearIndex() {
   		return (year * 12)+month;	
   }

   int getQtr() {
      if( _qtr == 0 ) {
         if( month <= 3 ) _qtr = 1;
         else if( month <= 6 ) _qtr = 2;
         else if( month <= 9 ) _qtr = 3;
         else _qtr = 4;
      }
      return _qtr;
   }

   public String getMonthname() {
        return monthNames[ month - 1 ]; 
    }

  
}