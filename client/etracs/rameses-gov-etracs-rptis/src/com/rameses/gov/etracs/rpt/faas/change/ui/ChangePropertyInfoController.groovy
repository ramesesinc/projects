package com.rameses.gov.etracs.rpt.faas.change.ui;
        

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*
import com.rameses.gov.etracs.rpt.faas.change.ui.*;


public class ChangePropertyInfoController extends ChangeFaasInfoController
{
    String title = 'Modify Property Information';
    
    
    public def getModifiedEntity(){
        return [
            cadastrallotno : entity.rp.cadastrallotno,
            surveyno	: entity.rp.surveyno,
            blockno 	: entity.rp.blockno,
            street 	: entity.rp.street,
            purok	: entity.rp.purok,
            north 	: entity.rp.north,
            east 	: entity.rp.east,
            south 	: entity.rp.south,
            west 	: entity.rp.west
        ]
    }
    
    public void updateEntityInfo(newinfo){
        entity.rp.cadastrallotno  = newinfo.cadastrallotno;
        entity.rp.surveyno  = newinfo.surveyno;
        entity.rp.blockno   = newinfo.blockno;
        entity.rp.street    = newinfo.street;
        entity.rp.purok     = newinfo.purok;
        entity.rp.north     = newinfo.north;
        entity.rp.east      = newinfo.east;
        entity.rp.south     = newinfo.south;
        entity.rp.west      = newinfo.west;
    }
}
       