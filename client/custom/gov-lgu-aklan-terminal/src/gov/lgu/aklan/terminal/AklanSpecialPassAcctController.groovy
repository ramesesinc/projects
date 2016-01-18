/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.lgu.aklan.terminal

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.util.*;

/**
 *
 * @author wflores 
 */
class AklanSpecialPassAcctController extends CRUDController {

    String serviceName = 'AklanSpecialPassAcctService';    
    String title = 'Special Pass Account Information';
    boolean allowApprove = false;
    boolean allowDelete = false; 
    
    def idTypes = LOV.get('INDIVIDUAL_ID_TYPES');
    def genderTypes = [
        [key:'M', value:'Male'],
        [key:'F', value:'Female'] 
    ];
    
    
    
    Map createEntity() {
        return [acctno: KeyGen.newProvider().generateRandomKey()]; 
    }
    
    def getLookupAcctType() {
        return Inv.lookupOpener('aklanspecialpassaccttype:lookup', [:]); 
    } 
    
    def getSelectedEntity() { 
        return entity; 
    }
    def print() {        
        return Inv.lookupOpener('aklanspecialpassacct:printpass', [:]);
    }
}