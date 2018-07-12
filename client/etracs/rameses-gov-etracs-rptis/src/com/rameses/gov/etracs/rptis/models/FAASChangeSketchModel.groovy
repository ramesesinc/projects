package com.rameses.gov.etracs.rptis.models;


import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rptis.util.*;
import com.rameses.rcp.draw.figures.ImageFigure
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import com.rameses.rcp.draw.utils.DataUtil;
import com.rameses.rcp.draw.figures.ImageFigure

import com.rameses.rcp.draw.figures.ImageFigure
import java.awt.Rectangle

class FAASChangeSketchModel extends FAASSketchModel
{
    @Caller
    def caller 
    
    @Invoker 
    def invoker
    
    @Service('FAASChangeInfoService')
    def svc;  
    
    def changeinfo = [:]
    def converted = false; 
    
    void init() {
        super.init();
        def previnfo = [:];
        previnfo.north = entity.rp.north;
        previnfo.east = entity.rp.east;
        previnfo.west = entity.rp.west;
        previnfo.south = entity.rp.south;
        
        changeinfo = [
            objid       : 'CI' + new java.rmi.server.UID(),
            refid       : entity.objid,
            faasid      : entity.objid,
            rpid        : entity.rp.objid,
            rpuid       : entity.rpu.objid,
            action      : invoker.properties.actiontype,
            newinfo     : [:],
            previnfo    : previnfo,
            redflagid   : entity._redflag?.objid,
        ];
        mode = 'edit'
    }
    
    void initChangeInfo(){
        init();
        mode = 'changeinfo'
    }

    void initConvert() {
        initChangeInfo();
        converted = true;
    }
    
    def getChangeinfo() {
        updateInfo();
        return changeinfo;
    }
    
    void updateInfo() {
        changeinfo.newinfo.north = entity.rp.north;
        changeinfo.newinfo.east = entity.rp.east;
        changeinfo.newinfo.west = entity.rp.west;
        changeinfo.newinfo.south = entity.rp.south;
        changeinfo.sketch = [objid: entity.objid, drawing: handler.data];
    }
    
    def save(){
        if (MsgBox.confirm('Save and apply changes?')){
            updateInfo();
            svc.updateInfo(changeinfo);
            if (converted) {
                return '_exit';
            } else {
                return '_close';
            }
            caller.refreshForm();
        }
        return null;
    }

}
