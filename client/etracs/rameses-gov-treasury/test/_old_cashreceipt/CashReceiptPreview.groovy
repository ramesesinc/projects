package com.rameses.enterprise.treasury.models;

import com.rameses.osiris2.common.*;
import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import java.rmi.server.*;
import com.rameses.osiris2.client.*;
import com.rameses.util.*;

public class CashReceiptPreview {
    
    @FormTitle
    def title = "Cash Receipt Preview";
    
    def entity;
    def selectedItem;
    
    def listModel = [
        fetchList: { o->
            return entity.items;
        }
    ] as BasicListModel;
    
}