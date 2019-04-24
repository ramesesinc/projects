package com.rameses.gov.etracs.landtax.models;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.seti2.models.*;

class AuctionSectionForPublicationModel extends CrudListModel
{
    //auction entity
    def entity; 
    
    public def getCustomFilter(){
        return [''' step.objid = 'NOPAS' and state in('FORPUBLICATION') ''', [:]]
    }
    
    def showMenu(){
        return null;
    }
    
    boolean isSurroundSearch(){
        return false;
    }
}