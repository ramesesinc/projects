package com.rameses.gov.etracs.waterworks.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.text.*;
import com.rameses.treasury.common.models.*;

class MeterEditModel extends ChangeInfoModel {

    boolean validate( def keyfield, def value ) {
        if(keyfield=="lastreading") {
            def v = value.toInteger();
            if( v >= entity.capacity.toInteger() ) {
                throw new Exception("Last reading must be less than capacity");
            }
        }
        return true;
    }

}
