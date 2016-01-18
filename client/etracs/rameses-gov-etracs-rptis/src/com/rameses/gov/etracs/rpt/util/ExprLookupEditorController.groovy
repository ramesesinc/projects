package com.rameses.gov.etracs.rpt.util;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;

abstract class ExprLookupEditorController extends LookupController 
{
    def oncomplete;
    def params;
    def openers;
    
    abstract List fetchList(Map params);
    abstract def getVariables();
    abstract Column[] getColumns();
    
    
    Object doSelect(){
        buildParamList();
        if (params){
            return "info";
        }
        return close()
    }
              
    def buildParamList(){
        params = [];
        getVariables().each{
            def expr = selectedItem.item.expr;
            if (expr.indexOf(it.name) >= 0 ) {
                if (!params.contains(it))
                    params << [param:it];
            }
        }
    }
    
    
    def infoListHandler = [
        fetchList : { return params },
            
        validate : { li ->
            def item = li.item;
            checkRange(item)
            setParamValue(item)
        }
    ] as EditorListModel
    
    void setParamValue(item){
        if (item.param.paramtype.toLowerCase().indexOf('integer') >= 0) 
            item.intvalue = RPTUtil.toInteger(item.value)
        else
            item.decimalvalue = RPTUtil.toDecimal(item.value)
    }

    void checkRange(item){
        def value = null;
        if (item.param.paramtype.toLowerCase().indexOf('integer') >= 0) 
            value = RPTUtil.toInteger(item.value)
        else
            value = RPTUtil.toDecimal(item.value)
            
        if (item.param.paramtype.toLowerCase().startsWith('range')){
            if (value < item.param.minvalue || value > item.param.maxvalue){
                throw new Exception('Value must be between ' + item.param.minvalue + ' and ' + item.param.maxvalue + '.')
            }
        }
    }
    
    def close(){
        selectedItem.item.params = params;
        params.each{
            if ( it.intvalue == null && it.decimalvalue == null)
                throw new Exception( it.param.name +  ' value is required.')
        }
        return super.doSelect();
    }
    
    def okInfo(){
        return close();
    }
    
    
    
    
    def buildOpeners(){
        def openers = [];
        params.each{ param->
            def openertype = 'handler:decimalvalue';
            if (param.paramtype.toLowerCase().indexOf('int') >= 0){
                openertype = 'handler:integervalue';
            }
            openers.add(InvokerUtil.lookupOpener(openertype, [param:param]));
        }
        return openers;
    }
    
    
    
}  

