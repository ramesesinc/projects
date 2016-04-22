import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class RedFlagModel {

    @Service('AMLRedFlagService')
    def svc; 
    
    def title = 'RedFlag';
    def entity = [:];
    def message;
    
    void open() { 
        entity = svc.read([ objid: entity.objid ]); 
        if ( entity.items ) {
            message = entity.items*.message.join('\n\n');
        }
    }
    
    void resolve() { 
        if ( entity.state == 'CLOSED' ) {
            throw new Exception('Transaction status is already closed.'); 
        }
        
        if ( MsgBox.confirm('You are about to submit this transaction. Continue? ') ) {
            def result = svc.resolve([ objid: entity.objid ]); 
            if ( result ) entity += result; 
        } 
    }

} 
