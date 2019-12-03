package gov.zamboanga.landtax.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;

class AssignTaxpayerModel {
    @Service('ZamboangaLandTaxService')
    def svc;
    
    def taxpayer;
    def items;
    def selectedItem;
    def onassign = {};
    
    def assign() {
        if (!taxpayer) {
            throw new Exception('A new taxpayer should be selected.');
        }
        
        if (MsgBox.confirm('Assign new taxpayer to the listed properties?') ) {
            svc.assignTaxpayer([taxpayer: taxpayer, items: items]);
            onassign();
            return '_close';
        }
    }
    
    
    def listHandler = [
        fetchList: { items }
    ] as BasicListModel;
    
}