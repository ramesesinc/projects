import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

class SendoutModel extends com.rameses.seti2.models.CrudFormModel {

    @Binding 
    def binding;
    
    @Service('SendoutService')
    def sendoutSvc;
    
    @Service('EntityFinderService')
    def entityFinderSvc;
    
    def currencylist = LOV.CURRENCY_TYPES;
    
    @PropertyChangeListener 
    def changelisteners = [
       'entity.sender' : {
            entity.receiver = null; 
            binding.refresh('entity.receiver'); 
       }, 
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
        
        def result = sendoutSvc.calculateCharge( entity ); 
        def charge = ( result.charge? result.charge : 0.0 );
        def othercharge = ( result.othercharge? result.othercharge : 0.0 );
        def discount = ( result.discount? result.discount : 0.0 );

        entity.othercharge = othercharge;
        entity.discount = discount;
        entity.charge = charge; 
        entity.total = entity.principal + ((charge + othercharge)-discount);
    }
    
    void findSenderByBarcode() {
        def barcode = MsgBox.prompt("Enter barcode");
        if(!barcode) return;
        def qry = [:];
        qry._schemaname = 'vw_entityindividual_lookup';
        qry.findBy = [entityno: barcode];
        def z = qryService.findFirst( qry );
        if ( !z ) throw new Exception('Record not found');
        
        entity.sender = z; 
        binding.refresh();
    }
    
    def openSender() {
        if ( !entity.sender ) return;
        
        return Inv.lookupOpener('individualentity:open', [entity: entity.sender]); 
    }
    def openReceiver() {
        if ( !entity.receiver ) return;
        
        return Inv.lookupOpener('individualentity:open', [entity: entity.receiver]); 
    }    
}
