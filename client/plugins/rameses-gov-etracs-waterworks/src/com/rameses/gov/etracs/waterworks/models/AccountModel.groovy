package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;

public class AccountModel extends CrudFormModel {
    
    def dateFormatter = new java.text.SimpleDateFormat('yyyy-MM-dd'); 
    
    void afterCreate() {
        entity.address = [:];
        entity.attributes = [];
        entity.units = 1;
    }

     def edit() {
        def mp = new PopupMenuOpener();
        if( entity.meter?.objid ) {
            mp.add( new FormAction(caption:'View Meter', name:'viewMeter', context: this )  );
            mp.add( new FormAction(caption:'Detach Meter', name:'detachMeter', context: this )  );
        }
        else {
            mp.add( new FormAction(caption:'Change Meter', name:'changeMeter', context: this )  );
        }
        mp.add( new FormAction(caption:'Change Owner', name:'changeOwner', context: this)  );
        mp.add( new FormAction(caption:'Change Address', name:'changeAddress', context: this)  );
        mp.add( new FormAction(caption:'Change Stubout Node', name:'changeNode', context: this)  );
        mp.add( new FormAction(caption:'Change No. of units', name:'changeUnits', context: this)  );
        return mp;
    }
    
    void showEdit( def fields, def data, def title, def beforeUpdate ) {
        def h = [fields:fields, data:data];
        h.reftype = "waterworks_account";
        h.refkeys = [objid: entity.objid];
        if( beforeUpdate ) h.beforeUpdate = beforeUpdate;
        
        Modal.show("waterworks_changeinfo", h, [title:title]);
        reload();
    }
    
    def viewMeter() {
        Modal.show( "waterworks_meter:open", [entity: entity.meter ]);
        reload();
    }
    
    void detachMeter() { 
        if(!MsgBox.confirm('This action will remove the meter from this account. Proceed?')) return;
        updateAccount( [meterid: "{NULL}"])
    }
    
    void changeAddress() { 
        def fields = [
            [name:'address', caption:'Address', datatype:'local_address', preferredSize:'550,100', captionWidth:60],
        ];
        def data = [ address: entity.address ];
        showEdit( fields, data, "Change Address", null );
    }
    
    void changeMeter() { 
        def fields = [
            [name:'meter', caption:'Meter', datatype:'lookup', expression:'#{data.meter.serialno}', handler: 'waterworks_meter_wo_account:lookup'],
        ];
        def data = [ meter: entity.meter ];
        def onUpdate = { o->o.meterid=o.meter.objid; };
        showEdit( fields, data, "Change Meter", onUpdate );
    }
    
    def changeNode() {
         def fields = [
            [name:'stuboutnode', caption:'Stubout Node', datatype:'lookup',handler: 'waterworks_stuboutnode_unassigned:lookup',
                expression:'#{data.stuboutnode.indexno} Zone: #{data.stuboutnode.zone.code} Sector: #{data.stuboutnode.sector.code}'],
        ];
        def data = [ stuboutnode: entity.stuboutnode ];
        def onUpdate = { o->o.stuboutnodeid=o.stuboutnode.objid; };
        showEdit( fields, data, "Change Stubout Node", onUpdate );
    }
    
    void changeOwner() {
        def fields = [
            [name:'owner', caption:'Owner', datatype:'lookup', expression:'#{data.owner.name}', handler: 'entity:lookup'],
        ];
        def data = [ owner: entity.owner ];
        showEdit( fields, data, "Change Owner", null );
    }
    
    void changeUnits() {
        def fields = [
            [name:'units', caption:'Edit Units', datatype:'integer'],
        ];
        def data = [ units: entity.units ];
        showEdit( fields, data, "Edit No. of Units", null );
    }

    
    //***************************************************************************
    def selectedAttribute;
    def attributeList = [
        fetchList: { o->
            def m = [_schemaname:'waterworks_account_attribute'];
            m.findBy = [parentid: entity.objid];
            return queryService.getList(m);
        }
    ] as BasicListModel;
    
    void addAttribute() {
        def p = [:]
        p.onselect = { o->
            def m = [_schemaname:'waterworks_account_attribute'];
            m.parent = entity;
            m.attribute = o;
            persistenceService.create( m );
            attributeList.reload();
        }
        Modal.show( "waterworks_attribute:lookup", p );
    }
    
    void removeAttribute() {
        if(!selectedAttribute) throw new Exception("Please select an attribute");
        def m = [_schemaname:'waterworks_account_attribute'];
        m.findBy = [objid: selectedAttribute.objid];
        persistenceService.removeEntity( m );
        attributeList.reload();
    }
    
}