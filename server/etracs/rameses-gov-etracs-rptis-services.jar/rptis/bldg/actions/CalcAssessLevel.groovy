package rptis.bldg.actions;

import com.rameses.rules.common.*;
import rptis.bldg.facts.*;


public class CalcAssessLevel implements RuleActionHandler {
	def request
	def bldgSettingSvc

	public void execute(def params, def drools) {
		def assessment = params.assessment
		
		def lvl = bldgSettingSvc.lookupBldgAssessLevelById(assessment.actualuseid)
		
		if (lvl.fixrate == true || lvl.fixrate == 1){
			assessment.assesslevel = lvl.rate 
		}
		else {
			def range = bldgSettingSvc.lookupAssessLevelFromRange(lvl.objid, assessment.marketvalue)
			assessment.assesslevel = 0.0 
	        if( range ) {
	            assessment.assesslevel = range.rate 
	        }
		}
	}
}