package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;

public abstract class AbstractTypeSelectionModel {
        
    @Service("QueryService")
    def qryService;
    
    @Binding
    def binding;
    
    public abstract def getList();
    public abstract def openItem(def o);
        
    def model = [
        fetchList: {o-> 
            def list = getList();
            list.each {
                if(!it.icon) it.icon = 'home/icons/folder.png';  
            }
            return list;
        }, 
        onOpenItem: {o-> 
            return openItem(o);
        }
    ] as TileViewModel;
            
    

}       