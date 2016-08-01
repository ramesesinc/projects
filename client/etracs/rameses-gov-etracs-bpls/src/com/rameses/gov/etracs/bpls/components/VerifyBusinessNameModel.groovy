package com.rameses.gov.etracs.bpls.components;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;
import java.rmi.server.UID;

class VerifyBusinessNameModel extends ComponentBean {
    
    @Service("BusinessNameVerificationService")
    def service;
    
    def query = [:];
    def handler;
    def searchList;
    def selectedItem;
    def mode = "specify-name";
    
    public void verify() {
        searchList = service.getList(query.businessname); 
        if(searchList) {
            mode = "view-list"
            listModel.reload();
        }
        else {
           mode = "specify-name"
           if(!handler) throw new Exception("Please specify handler in verify business name");
           handler(query);
           query = [:];
        }
     }
             
     def listModel = [
        fetchList: { o->return searchList;}
     ] as BasicListModel;

     void retry() {
        mode = "specify-name"
        searchList = [];
        listModel.reload();
     }
     
    void copy() {
        query.tradename = query.businessname;
        binding.refresh("query.tradename");
    }
    
     void doContinue() {
         if( searchList.find{ it.weight == 100 } )
            throw new Exception("Exact business name already exists. Please choose another business name"); 
         if(!handler) throw new Exception("Please specify handler in verify business name");
         handler(query);
         mode = "specify-name"
     }

}