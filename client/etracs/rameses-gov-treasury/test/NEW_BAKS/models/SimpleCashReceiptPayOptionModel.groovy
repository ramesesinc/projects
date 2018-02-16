package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*
import com.rameses.rcp.annotations.*
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import java.rmi.server.*;

public class SimpleCashReceiptPayOptionModel {

    @Binding
    def binding;

    //passed from the outside
    def payOptions;
    
    def onselect;
    def payOption;
    def selectedPayOption;
    
    def doOk() {
        onselect( payOption.handle.getValue() );
        return "_close";
    }
    
    void doNext() {
        payOption = selectedPayOption;
    }
    
    void doBack() {
        payOption = null;
    }
    
}       