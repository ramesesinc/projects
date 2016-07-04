package com.rameses.gov.etracs.rpt.transmittal.ui;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.util.*;


public abstract class AbstractTransmittalExporter
{
    def writer;
    def showInfo;
    def service;
    
    public AbstractTransmittalExporter(def writer){
        this(writer, null);
    }
    
    public AbstractTransmittalExporter(def writer, def showInfo){
        this.writer = writer ;
        this.showInfo = showInfo;
    }
    
    
    public abstract void export(def item);
    
    public def getService(){
        return service;
    }
    
    def getFaas(item){
        return service.getFaas([objid:item.refid]);
    }
    
    def getSignatories(item){
        service.getSignatories(item);
    }
    
    def getRequirements(item){
        service.getRequirements(item)
    }
    
    def getImages(item){
        def images = service.getImageHeaders(item)
        images.each{ image -> 
            image.chunks = service.getImageChunks(image)
        }
        return images; 
    }
    
    void doShowInfo(msg){
        if(showInfo)
            showInfo(msg);
    }
    
    void doSleep(){
        try{
            Thread.sleep(500);
        }
        catch(e){
            ;
        }
    }   
       
}