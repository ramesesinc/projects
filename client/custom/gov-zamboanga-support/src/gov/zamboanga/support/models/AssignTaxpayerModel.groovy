package gov.zamboanga.landtax.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.client.*;

class AssignTaxpayerModel {
    @Binding
    def binding;
    
    @Service('ZamboangaLandTaxService')
    def svc;
    
    String title = "Assing Property Owner";
    
    def taxpayer;
    def items;
    def selectedItem;
    def onassign = {};
    
    def onSaveEntity = {
        taxpayer = it;
        binding.refresh('taxpayer.*');
    }
    
    def createEntity(param) {
        def opener = 'entity' + param.properties.tag + ':create';
        def inv = Inv.lookupOpener(opener, [onSaveHandler: onSaveEntity]);
        inv.target = 'popup';
        return inv;
    }
        
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