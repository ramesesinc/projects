package rptis.land.actions;

import com.rameses.rules.common.*;
import rptis.land.facts.*;
import rptis.planttree.facts.*;
import rptis.facts.*;


public class AddAssessmentInfo implements RuleActionHandler {
	def request
	def NS

	/*-----------------------------------------------------
	* create a assessment fact summarized based 
	* on the actualuseid
	*
	-----------------------------------------------------*/
	public void execute(def params, def drools) {
		def ld = params.landdetail 
		def ldentity = ld.entity
		def rpuentity = ld.rpu.entity

		def classificationid = params.classification?.objid
		def actualuseid = ldentity.actualuse?.objid
		def rputype = 'land';

		def a = request.assessments.find{it.rputype == rputype && it.classificationid == classificationid && it.actualuseid == actualuseid}
		if ( ! a){
			if (rpuentity.assessments == null) 
				rpuentity.assessments = []
			
			def entity = [
				objid  :  'BA' + new java.rmi.server.UID(),
				rpuid  : rpuentity.objid, 
				rputype : rputype,
				classificationid : classificationid,
				classification   : [objid:classificationid],
				actualuseid  : actualuseid,
				actualuse    : [objid:actualuseid],
				areasqm      : NS.round(ldentity.areasqm), 
				areaha       : NS.roundA( ldentity.areaha, 6),
				marketvalue  : ldentity.marketvalue,
				assesslevel  : ldentity.assesslevel,
				assessedvalue  : ldentity.assessedvalue,
			]
			
			a = new RPUAssessment(entity)
			a.assesslevel = ldentity.assesslevel
			a.assessedvalue = ldentity.assessedvalue
			rpuentity.assessments << entity
			request.assessments << a
			request.facts << a 
			drools.insert(a)
		}
		else{
			a.marketvalue = NS.round(a.marketvalue + ldentity.marketvalue)
			a.assessedvalue = NS.round(a.assessedvalue + ldentity.assessedvalue)
			a.areasqm = NS.round( a.areasqm + ldentity.areasqm )
			a.areaha  = NS.roundA( a.areaha + ldentity.areaha , 6)
			drools.update(a)
		}
	}
}