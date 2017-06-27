package com.rameses.gov.etracs.rptis.models;

import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.* 
import com.rameses.osiris2.common.* 

class RPUInfoAssessmentModel extends SubPageModel
{
    def assessmentListHandler = [
        fetchList : {return entity.rpu.assessments }
    ] as BasicListModel
    
   
}    