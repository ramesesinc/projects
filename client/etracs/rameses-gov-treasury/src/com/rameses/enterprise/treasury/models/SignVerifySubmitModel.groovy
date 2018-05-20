package com.rameses.enterprise.treasury.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.osiris2.common.* 
import com.rameses.rcp.common.SigIdModel;
import com.rameses.rcp.sigid.SigIdViewer;
import com.rameses.util.*;
import com.rameses.common.*;

class SignVerifySubmitModel  { 

    @Binding
    def binding;
    
    @Service('Var')
    def var;
    
    def message;
    def handler;
    def signature = [:]
    def digitalsigactive;
 
    void init() {
        showSignViewer();  
    }

    void loadDigitalSignatureStatus(){
        digitalsigactive = digitalsigactive.toString().toLowerCase().matches('1|y|yes|t|true');
    }
    
    def model = [
        onselect: {sig-> 
             signature.image = sig.imageData;
             signature.sigstring = sig.sigString;
             signature.numstrokes = sig.numberOfStrokes;
             binding?.refresh('signature.image');
         },
         getPenWidth: {
             return 5;
         }            
    ] as SigIdModel;
    
    def showSignViewer() {
        signature = [:];
        loadDigitalSignatureStatus();
        digitalsigactive = true;
        if (digitalsigactive) {
            SigIdViewer.open(model);   
        }
        else {
            throw new BreakException();
        }
    }

    /*
    void paste(){
        signature.image = DBImageUtil.getInstance().getClipboardImage();
        binding.refresh('signature.image');
    }
    */
    
    def doOk() {
        if ( digitalsigactive && signature.numstrokes == 0)
            throw new Exception('Signature is required.');
        if (!digitalsigactive) {
            signature = null;
        }
        else {
            if( signature !=null) {
                Base64Cipher cipher = new Base64Cipher()
                handler( cipher.encode(signature) );
            }
            else {
                handler(null);
            }
        }
        return "_close";
    }

    def doCancel() {
        return "_close";
    }

} 