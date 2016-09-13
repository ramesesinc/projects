package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

/************************************************************
* This fires the rule and loops until there is no data found
*************************************************************/
public class BusinessInfoPanelModel extends ComponentBean {
       
    @Service("BPInfoRuleService")
    def infoService;
            
    @Service("BusinessAssessmentService")
    def assessmentService;
    
    def handler;
    def formControls = [];
    def existingInfos = [];
    def editingInfos = [];     //utility to memorize last list info made
    
    def invokerList; 
    
    def infoStack = new InfoStack();
    
    //info is the beginning record;
    def getParams() {
        return getValue();
    }
    
    void setParams( def ent ) {
        setValue( ent );
    }

    def formPanel = [
        getCategory: { key->
            if(!key) return "";
            def lobname = params.lobs.find{ it.lobid == key }?.name    
            return ((lobname) ? lobname : key);
        },
        updateBean: {name,value,item->
            item.bean.value = value;
        },
        getControlList: {
            return formControls;
        }
    ] as FormPanelModel;
    
    def findValue(def info, def list ) {
        if(info.lob?.objid!=null) {
            def filter = list.findAll{ it.lob?.objid!=null };
            def m = filter.find{ it.lob.objid==info.lob.objid && it.attribute.objid == info.attribute.objid };
            if(m) return m;
        }
        else {
            def filter = list.findAll{ it.lob?.objid==null };
            def m = filter.find{ it.attribute.objid == info.attribute.objid };
            if(m) return m;
        }
        return null;
    }
    
    void buildControls(def items ) { 
        //sort first the elements before saksakin in the stack;
        def list = items.findAll{it.lob?.objid==null && it.attribute.category==null}?.sort{it.attribute.sortorder};
        def catGrp = items.findAll{it.lob?.objid==null && it.attribute.category!=null};
        if(catGrp) {
            def grpList = catGrp.groupBy{ it.attribute.category };
            grpList.each { k,v->
                v.sort{ it.attribute.sortorder }.each{z->
                    list.add( z );
                }
            }
        }
        list = list + items.findAll{ it.lob?.objid!=null }?.sort{ [it.lob.name, it.attribute.sortorder] }; 
        
        formControls.clear();
        list.each {x->
            
            //store default value if value is null. Test first if you can find the info
            //from 3 sources. Draft info, existing or default value.
            if( x.value==null ) x.value = findValue( x, editingInfos )?.value;
            if( x.value ==null) x.value = findValue( x, existingInfos )?.value;
            if( x.value==null && x.defaultvalue ) x.value = x.defaultvalue;
            
            def i = [
                type:x.attribute.datatype, 
                caption:x.attribute.caption, 
                categoryid:  ((x.lob?.objid!=null) ? x.lob.objid : x.attribute.category),
                handler: x.attribute.handler,
                name: x.attribute.name, 
                bean: x,
                properties: [:],
                value: x.value
            ];
            /*
            x.datatype = x.attribute.datatype;
            if(x.datatype.indexOf("_")>0) {
                x.datatype = x.datatype.substring(0, x.datatype.indexOf("_"));
            }
            */
            if(i.type == "boolean") {
                i.type = 'yesno';
            }
            else if( i.type == "string" ) {
                i.type = "text";
            }
            else if(i.type == "string_array") {
                i.type = "combo";
                i.preferredSize = '150,20';
                i.itemsObject = x.attribute.arrayvalues;
            }
            else if( i.type.matches('decimal|integer')) {
                i.preferredSize = '150,20';
            }
            i.required = true;
            formControls << i;
        } 
    }
    
    //initialize the rule actions.
    void init() {
        if(!handler) throw new Exception("Handler is required in BusinessInfoPanelModel");
        existingInfos = params.remove("infos");
        def invokerList = [
            new InvokerStack({ p-> return infoService.execute(p); }),
            new InvokerStack({ p-> return assessmentService.assess(p); }),
        ];
        
        invokerList.each {
            infoStack.addInvoker( it );    
        }
        
        infoStack.params = params;
        boolean b = infoStack.fireInit();
        if(b) {
            buildControls( infoStack.getCurrentInfos() );
        }
        else {
            def m = [:];
            m.infos = infoStack.getAllInfos();
            m.taxfees = infoStack.getTaxFees();
            handler(m);
        }
    }
    
    void doNext() {
        boolean b = infoStack.fireRules();
        if(b) {
            buildControls( infoStack.getCurrentInfos() );
        }
        else {
            def m = [:];
            m.infos = infoStack.getAllInfos();
            m.taxfees = infoStack.getTaxFees();
            handler(m);
        }
    }
    
    def doBack() {
        def infoList = infoStack.pop();
        infoList?.each {
            def g = findValue( it, editingInfos );
            if(g) {
                g.value = it.value;
            }
            else {
                editingInfos << it;
            }
        }
        buildControls(infoStack.getCurrentInfos());
    }
    
    int getCurrentIndex() {
        return infoStack.indexno;
    }
}

public static class InfoStack {
    
    def params;
    private def invokerList = [];
    private def invokerStack = new Stack();
    private int counter = 0;        //this the index of the invokers
    private int indexno = 0;        //this is used for backing up 
    
    public InfoStack() {
        super();
    }

    public void addInvoker(InvokerStack i) {
        invokerList << i;
    }
    
    def getAllInfos() {
        def list = [];
        invokerStack.each { list.addAll( it.getAllInfos() ); };
        return list;
    }

    //if already completed. It should return null
    public def getCurrentInfos() {
        invokerStack.peek().getCurrentInfos();
    }

    //this should be called before firing rules
    boolean hasMoreRuleInvokers() {
        return (!invokerList.empty());
    }

    //return the info list so it can be stored
    public def pop() {
        if(indexno==1) return;
        def invoker = invokerStack.peek(); 
        def infoList = invoker.dequeue();   //to store in draft.
        if(!invoker.hasItems()) {
            def c = invokerStack.pop();
            counter--;
        }
        indexno--;
        return infoList;
    }

    def getNextInvoker() {
        if( counter >= invokerList.size()) {
            return null;
        }
        else {
            def v = invokerList[counter];
            counter++;
            return v;
        }
    }
    
    //called to initiate the rules
    public boolean fireInit() {
        if(invokerStack.empty() || invokerStack.peek().completed ) {
            def nextInv = getNextInvoker();
            if(nextInv==null) return false;
            invokerStack.push( nextInv );
        }
        fireRules();
        return true;
    }
    
    //returns true if rules need to be executed further, false if not. 
    public boolean fireRules() {
        def m = [:];
        m.putAll(params);
        m.infos = getAllInfos();
        def invoker = invokerStack.peek();
        invoker.execute(m);
        if(invoker.completed) {
            return fireInit();
        }
        else {
            indexno++;
            return true;
        }
    }

    def getTaxFees() {
        def list = [];
        invokerStack.each {
            if(it.taxfees) list.addAll( it.taxfees );
        }
        return list;
    }
    
}

public static class InvokerStack  {
        
    private def invoker;
    private def stack = new Stack();
    private def completedInfos;
    private def taxfees;
    private def requirements;
    boolean completed;
    
    public InvokerStack(def inv) {
        invoker = inv;
    }

    //returns true if more rules need to be continuously fired
    public boolean execute( def params ) {
        def result = invoker(params);
        completed = (result.phase > 1);
        if( !completed) {
            if(!result.infos) throw new Exception("There must be at least one info in the rule");
            stack.push(result.infos);
            return true;
        }
        else {
            if(result.infos) completedInfos = result.infos;
            if(result.taxfees) taxfees = result.taxfees;
            if(result.requirements) requirements = result.requirements;
            return false;
        }
    }

    public boolean hasItems() {
        return (stack.empty()==true)?false:true
    }
    
    //return true if pop successful, return false, if non popped.
    public def dequeue() {
        if(stack.empty()) return null;
        def r = stack.pop();
        completedInfos = null;
        taxfees = null;
        requirements = null;
        return r;
    }

    public def getAllInfos() {
        def list = [];
        stack.each {
            list.addAll(it);
        }
        if(completedInfos) list.addAll(completedInfos);
        return list;
    }

    public def getCurrentInfos() {
        return stack.peek();
    }


}