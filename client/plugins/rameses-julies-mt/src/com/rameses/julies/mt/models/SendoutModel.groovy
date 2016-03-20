import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class SendoutModel extends com.rameses.seti2.models.CrudFormModel {

    @Binding 
    def binding;
    
    @Service('SendoutService')
    def sendoutSvc;
    
    def currencylist = LOV.CURRENCY_TYPES;
    
    @PropertyChangeListener 
    def changelisteners = [
       'entity.principal' : {
            calculateCharge(); 
       } 
    ]; 
    
    void afterCreate() {
        def result = sendoutSvc.init(); 
        entity += result; 
    }
    
    void calculateCharge() { 
        if ( !entity.principal ) throw new Exception('Please specify principal amount');
        
        entity.charge = 0.0;
        entity.othercharge = 0.0; 
        entity.total = entity.principal + entity.charge + entity.othercharge; 
    }
}
