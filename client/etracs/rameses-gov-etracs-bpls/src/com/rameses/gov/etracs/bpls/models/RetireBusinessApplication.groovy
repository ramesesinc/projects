package com.rameses.gov.etracs.bpls.models;

import com.rameses.rcp.annotations.*
import com.rameses.rcp.common.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;
import com.rameses.gov.etracs.bpls.application.*;
import com.rameses.util.*;

public class RetireBusinessApplication extends AbstractBusinessApplicationPageFlow  {

    @Script("BusinessSearchUtil")
    def searchList;

    @Script("BusinessApplicationUtil")
    def application;

    @Script("BusinessAddressUtil")
    def address;

    @Script("BusinessRequirementUtil")
    def requirement;

    @Script("BusinessLobUtil")
    def lob;

    @Script("BusinessInfoUtil")
    def appinfo;

    @FormTitle
    def title = "Retire Business";

    def entity;

    def text = "Please enter reason for the retirement. ";

    void open() {
        application.init( [businessid:searchList.selectedItem.objid, apptype: 'RETIRE'] );
        lob.reset();
        appinfo.reset();
    }

    void saveCreate() {
        application.save();
        MsgBox.alert('Application sent for assessment!'); 
    }

}