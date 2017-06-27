package com.rameses.gov.etracs.rptis.master.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.seti2.models.*;

class RPTParameter extends MasterModel
{
    @Binding
    def binding;
    
    def TYPE_RANGE_DECIMAL = 'range_decimal'
    def TYPE_RANGE_INTEGER = 'range_integer'
    
    def dataTypes = ['decimal', 'integer', 'range_decimal', 'range_integer'];
    
    @PropertyChangeListener
    def listener = [
        "entity.name" : {
            fixName();
        }
    ]
    
    public void afterCreate(){
        super.afterCreate();
        entity.paramtype = 'decimal';
        entity.minvalue = 0.0;
        entity.maxvalue = 0.0;
    }
    
    public void beforeSave(def mode){
        fixName();
		validateRange();
    }
    
    void fixName(){
        if ( entity.name ){
            entity.name = entity.name.replaceAll("\\W", "_");
            binding?.refresh('entity.name');
        }
    }

    void validateRange(){
    	if (entity.paramtype == TYPE_RANGE_DECIMAL || entity.paramtype == TYPE_RANGE_INTEGER){
    		if (entity.minvalue >= entity.maxvalue)
    			throw new Exception('Mininum Value must be less than Maximum Value.')
    	}
    }
}