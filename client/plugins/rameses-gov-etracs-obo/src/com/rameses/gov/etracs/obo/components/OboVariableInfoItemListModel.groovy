package com.rameses.gov.etracs.obo.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.enterprise.models.*;

class OboVariableInfoItemListModel extends ComponentBean {

    String section;
    
    void addInfo() {
        if(!section)
            throw new Exception("Please specify a section");

        //first handler
        def h1 = { o->
            def newList = [];
            o.each { xx->
                def infos = getValue();
                if( !infos.find{ it.objid == xx.objid } ) {
                    xx.type = xx.datatype;
                    xx.categoryid = xx.category;
                    newList << xx;
                }
            };
            //second handler
            def h2 = { xinfos->
                def infos = getValue();
                infos.addAll(xinfos);
                listModel.reload();
            }
            return Inv.lookupOpener( "dynamic_data_entry", [infos:newList , title:section, handler:h2] );
        }
        Modal.show("obovariable_" + section.toLowerCase() + ":picklist", [onselect: h1 ] );        
    }
    
    void updateInfo() {
        def infos = getValue();
        def h = { o->
            listModel.reload();
        }
        Modal.show( "dynamic_data_entry", [infos: infos , title:section, handler:h] );
    }
    
    def listModel = [
        fetchList: { o->
            return getValue();
        }, 
        onRemoveItem: { o->
            getValue().remove(o);
        }
    ] as EditorListModel;
    
}