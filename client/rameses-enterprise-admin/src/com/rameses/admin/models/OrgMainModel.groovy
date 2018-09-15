package com.rameses.admin.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;
import com.rameses.common.*;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.constant.*;


class OrgMainModel {
    
    @Service("OrgService")
    def service;

    @Binding 
    def binding; 

    String title = "Manage Organizations";

    def actions = []; 

    def getRoot() {
        return service.getRoot();
    }
    
    def manageOrgTypes() {
        return Inv.lookupOpener( "orgmain.action", [:] );
    }
    
    void reloadOrgTypes() {
        binding.refresh();
    }
    
    void init() {
        /*
        try { 
            Inv.lookup('orgmain.action').each{
                def a = [
                    caption : it.properties.actiontitle, 
                    icon    : it.properties.icon, 
                    invoker : it 
                ]; 
                if (!a.caption) a.caption = it.caption;
                if (!a.icon) a.icon = 'home/icons/folder.png'; 

                actions << a; 
            } 
        } catch(e) {;} 
        */
    } 

    def orgModel = [

        fetchList: { o->
            def list = service.getOrgClasses().collect {[
                name:it.name.toLowerCase(),
                caption:it.title,
                icon:'home/icons/folder.png',
                parentclass:it.parentclass, 
                handler: it.handler
            ]}
            def resultlist = []; 
            //resultlist.addAll(actions);
            resultlist.addAll(list);
            return resultlist;
        },

        onOpenItem: { o->
            /*
            if (o.invoker) { 
                return Inv.createOpener(o.invoker); 
            } 
            */
            def opener = null;
            try {
                def ops = Inv.lookupOpeners( o.name+":manage", [:] );
                ops.each {
                    def vw = it.properties.visibleWhen;
                    if( vw ) {
                        try {
                            boolean b = ExpressionResolver.getInstance().evalBoolean(vw, [root:root] );
                            if( b ) {
                                opener = it;
                                return;
                            }
                        } catch(e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        opener = it;
                    }
                }
            }
            catch(e){;};

            if(!opener) {
                opener = Inv.lookupOpener( "org:manage", [orgclass: o.name, title: o.caption, parentclass:o.parentclass, handler:o.handler]);
            }
            return opener;    
        }

    ] as TileViewModel;
}       