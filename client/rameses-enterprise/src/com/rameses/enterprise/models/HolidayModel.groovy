package com.rameses.enterprise.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.seti2.models.*;

public class HolidayModel extends CrudFormModel {
    
    def yearOption = 0;
    def dayOption = 0;
    
    @PropertyChangeListener
    def listener = [
        "yearOption" : { o->
            if(o==0) entity.year = 0;
        },
        "dayOption" : { o->
            if(o==1) {
                entity.day = 0;
            }
            else {
                entity.dow = 0;
                entity.week = 0;
            }
        }
        
    ]
    
    void afterOpen() {
        if( entity.year == 0 ) yearOption = 0;
        if( entity.day == 0 ) dayOption = 1;
    }
    
}