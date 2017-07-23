package com.rameses.gov.etracs.market.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;

public class MarketAccountModel extends CrudFormModel {
    
    void afterOpen() {
        itemHandler.reload();
        //unitListModel.reload();
    }
    
    def itemHandler = [
        fetchList: {
            return entity.recurringfees;
        }
    ] as BasicListModel;
    
    def unitListModel = [
        fetchList: {
            return entity.units;
        }
    ] as BasicListModel;
    
    def showEditMenu() {
        return showDropdownMenu("editActions");
    }
                    
    
}