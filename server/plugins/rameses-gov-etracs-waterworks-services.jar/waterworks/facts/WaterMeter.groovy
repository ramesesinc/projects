package waterworks.facts;

import com.rameses.util.*;
import treasury.facts.*;

public class WaterMeter {

    String sizeid;
    int capacity;	
    String state;

    public WaterMeter(def o ) {
    	if(o.sizeid ) sizeid = o.sizeid;
    	if( o.capacity ) capacity = o.capacity;
    	if( o.state ) state = o.state;
    }

}
