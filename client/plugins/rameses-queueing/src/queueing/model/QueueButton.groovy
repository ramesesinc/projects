package queueing.model;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.util.*;

public class QueueButton {
        
    @Caller
    def caller;

    def item = [:]; 

    void take() {
        caller.take( item );
    }
}