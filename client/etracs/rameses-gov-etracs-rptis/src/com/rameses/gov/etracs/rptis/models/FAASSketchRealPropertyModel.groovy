package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

class FAASSketchRealPropertyModel extends FAASSketchModel
{
    void update(){
        entity.rp.drawing = handler.data
        committed = true;
        mode = MODE_READ;
        handler?.refresh();
    }
}
