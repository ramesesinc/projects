package queue.component;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class QueueSectionItemModel extends ComponentBean {

    public String getCaption() { 
        return userObject?.caption; 
    } 

    public String getActionText() {
        def o = userObject?.actionText; 
        return ( o == null ? "Execute" : o.toString()); 
    }
    
    public void execute() {
        def handler = userObject?.handler; 
        if ( handler ) handler( userObject?.item ); 
    } 
} 
