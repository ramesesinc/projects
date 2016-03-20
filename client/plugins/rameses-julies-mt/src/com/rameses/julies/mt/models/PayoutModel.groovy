import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class PayoutModel {
    
    @Service('PayoutService')
    def payoutSvc; 
    
    @Binding 
    def binding;

    @Caller
    def caller;
    
    def title = 'Payout'; 
    def entity = [:];
    
    void create() {
        def pout = payoutSvc.init([ sendoutid: entity.objid, handler: entity.handler ]);  
        pout.currency = pout.sendout.currency;
        pout.amount = pout.sendout.principal;
        pout.handler = entity.handler;
        entity = pout; 
    }
    
    void submit() { 
        if ( MsgBox.confirm('You are about to submit the data. Do you want to continue? ') ) {
            entity = payoutSvc.submit( entity ); 
            MsgBox.alert('Transaction successfully saved'); 
            caller.close(); 
        }
    }
}
