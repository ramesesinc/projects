package gov.lgu.gensan.rptis.models;
        
import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

public class RedeemAuctionModel 
{
    @Caller
    def caller;
    
    @Service('GSCAuctionService')
    def svc; 
    
    @Script("User")
    def user;
    
    
    def entity; 
    def redeem;
    
    void init() {
        def ledger = [objid: entity.objid, tdno: entity.tdno];
        redeem = [ledger: ledger];
        redeem.objid = 'RA' + new java.rmi.server.UID();
    }
    
    def redeemAuction() {
        if (MsgBox.confirm('Redeem auction?')) {
            redeem.password = user.encodePwd(redeem.pwd, redeem.username);
            svc.redeemAuction(redeem);
            caller.reloadEntity();
            return '_close';
        }
    }
    
}


