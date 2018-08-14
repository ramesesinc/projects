package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.gov.etracs.rptis.util.RPTUtil;

abstract class ExprLookupEditorModel extends LookupController 
{
    def oncomplete;
    def params;
    def openers;
    def hidedepreciate = false;
    
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
    
    def formControl = [
        getFormControls: {
            def ctrls = [];
            params.eachWithIndex{p, idx -> 
                def name = null;
                if (p.param.type?.matches('.*decimal.*')){
                    name = 'params[' + idx + '].decimalvalue'
                    p.intvalue = null;
                    ctrls << new FormControl( "decimal", [caption:p.param.caption, name:name, required:true, preferredSize:'120,19', captionWidth:180]);
                }
                else if (p.param.type?.matches('.*integer.*')){
                    name = 'params[' + idx + '].intvalue'
                    p.decimalvalue = null;
                    ctrls << new FormControl( "integer", [caption:p.param.caption, name:name, required:true, preferredSize:'120,19', captionWidth:180]);
                }
                else if (p.param.type?.matches('.*date.*')){
                    name = 'params[' + idx + '].datevalue'
                    p.value = null;
                    ctrls << new FormControl( "date", [caption:p.param.caption, name:name, required:true, preferredSize:'120,19', captionWidth:180]);
                }
                else {
                    name = 'params[' + idx + '].value'
                    p.value = null;
                    ctrls << new FormControl( "text", [caption:p.param.caption, name:name, required:true, preferredSize:'0,19', captionWidth:180, textCase:com.rameses.rcp.constant.TextCase.NONE]);
                }
            }
            return ctrls;
        },
   ] as FormPanelModel;
        
    
    void validateParamValues(){
        def value = null;
        params.each{
            if (it.param.type?.matches('.*decimal.*'))
                value = it.decimalvalue;
            else if (it.param.type?.matches('.*integer.*'))
                value = it.intvalue;
            else if (it.param.type?.matches('.*date.*'))
                value = it.datevalue;
            else 
                value = it.value;
                
           if (value == null){
               throw new Exception( it.param.caption +  ' value is required.')
           }
                
            if (it.param.paramtype && it.param.paramtype.toLowerCase().startsWith('range')){
                if (value < it.param.minvalue || value > it.param.maxvalue){
                    throw new Exception(it.param.caption + ' must be between ' + it.param.minvalue + ' and ' + it.param.maxvalue + '.')
                }
            }
            it.value = value;
        }
    }
    
    def close(){
        validateParamValues();
        selectedItem.item.params = params;
        return super.doSelect();
    }
    
    def okInfo(){
        return close();
    }
    
}  

