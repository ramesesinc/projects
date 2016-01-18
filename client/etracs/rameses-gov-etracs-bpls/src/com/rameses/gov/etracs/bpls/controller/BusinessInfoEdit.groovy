package com.rameses.gov.etracs.bpls.controller;
import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.rulemgmt.constraint.*;
import com.rameses.rulemgmt.*;
import java.rmi.server.*;

public abstract class BusinessInfoEdit extends PageFlowController {

    @Binding
    def binding;

    def entity;
    def infoLoaded = true;
    def handler;
    def formInfos = [];
    def infos = [];
    boolean completed;
    def query = [:];
    def initialInfos = [];
    def existingInfos = [];
    int level = 0;

    @FormTitle
    def title;

    public abstract def execute(); 

    def start() {
        query.putAll( entity );
        initialInfos?.each { it.level = -1 }; 
        query.infos = initialInfos;
        query.taxfees = [];
        query.requirements = [];
        completed = false;
        return super.start();
    }

    def formPanel = [
        getCategory: { key->
            if(!key) return "";
            def lobname = entity.lobs.find{ it.lobid == key }?.name    
            return ((lobname) ? lobname : key);
        },
        updateBean: {name,value,item->
            item.bean.value = value;
        },
        getControlList: {
            return formInfos;
        }
    ] as FormPanelModel;

    def sortInfos(sinfos) {
        def list = sinfos.findAll{it.lob?.objid==null && it.attribute.category==null}?.sort{it.attribute.sortorder};
        def catGrp = sinfos.findAll{it.lob?.objid==null && it.attribute.category!=null};
        if(catGrp) {
            def grpList = catGrp.groupBy{ it.attribute.category };
            grpList.each { k,v->
                v.sort{ it.attribute.sortorder }.each{z->
                    list.add( z );
                }
            }
        }
        list = list + sinfos.findAll{ it.lob?.objid!=null }?.sort{ [it.lob.name, it.attribute.sortorder] }; 
        return list; 
    }

    def findValue( info ) {
        if(info.lob?.objid!=null) {
            def filter = existingInfos.findAll{ it.lob?.objid!=null };
            def m = filter.find{ it.lob.objid==info.lob.objid && it.attribute.objid == info.attribute.objid };
            if(m) return m.value;
        }
        else {
            def filter = existingInfos.findAll{ it.lob?.objid==null };
            def m = filter.find{ it.attribute.objid == info.attribute.objid };
            if(m) return m.value;
        }
        return null;
     }

     def buildFormInfos() {
        formInfos.clear();
        infos = sortInfos( infos );
        infos.each {x->
            def i = [
                type:x.attribute.datatype, 
                caption:x.attribute.caption, 
                categoryid:  ((x.lob?.objid!=null) ? x.lob.objid : x.attribute.category),
                handler: x.attribute.handler,
                name:x.attribute.name, 
                bean: x,
                properties: [:],
                value: x.value
            ];
            //fix the datatype
            x.datatype = x.attribute.datatype;
            if(x.datatype.indexOf("_")>0) {
                x.datatype = x.datatype.substring(0, x.datatype.indexOf("_"));
            }
            if(i.type == "boolean") {
                i.type = "subform";
                i.handler = "business_application:yesno";
                i.properties = [item:x];
            }
            else if(i.type == "string_array") {
                i.type = "combo";
                i.preferredSize = '150,20';
                i.itemsObject = x.attribute.arrayvalues;
            }
            else if( i.type == 'decimal' ) {
                i.preferredSize = '150,20';
            }
            else if( i.type == "string" ) {
                i.type = "text";
            }
            else if( i.type == "info") {
                i.type = "subform";
                i.properties = [item:i.bean];
                i.showCaption = false;
            }
            formInfos << i;
        }
     }

     public void loadInfos() {
        infos.clear();
        def result = execute();
        //phase 0 is the looping phase.  
        if( result.phase > 1 ) {
            query.infos.addAll(result.infos);
            if( !query.infos )
                throw new Exception("There must be at least one value for infos");
            result.each {k,v->
                if( k!='infos') {
                    query.put(k, v);
                }
            }    
            infos.clear();
            completed = true;
            if(handler) handler( query );
        }
        else {
            if( !result.infos)
                throw new Exception("No information result found");
            //check if there is already values for this info
            result.infos.each {
                it.value = findValue(it);
                it.level = level;
            }
            level++;
            infos.addAll( result.infos );
            query.infos.addAll(infos);
            buildFormInfos();
            completed = false;
        }
    }

    void reset() {
        completed = false;
    }

    void goBack() {
        infos.clear();
        level--;
        def rem = query.infos.findAll{ it.level == level };
        query.infos.removeAll(rem);
        infos = query.infos.findAll{ it.level == (level-1) };
        buildFormInfos();
        completed = false;
    }
    
    
}