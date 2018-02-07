package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class ApplyIncentive implements RuleActionHandler {
	def numSvc

	public void execute(def params, def drools) {
		def rli = params.rptledgeritem
		def incentive = params.incentive

		if (incentive.basicrate > 0.0){
			rli.basic = numSvc.round( rli.basic * (100 - incentive.basicrate) / 100.0 )
			rli.basicdisc = numSvc.round( rli.basicdisc * (100 - incentive.basicrate) / 100.0 )
			rli.basicint = numSvc.round( rli.basicint * (100 - incentive.basicrate) / 100.0 )
		}
		
		if (incentive.sefrate > 0.0){
			rli.sef = numSvc.round( rli.sef * (100 - incentive.sefrate) / 100.0 )
			rli.sefdisc = numSvc.round( rli.sefdisc * (100 - incentive.sefrate) / 100.0 )
			rli.sefint = numSvc.round( rli.sefint * (100 - incentive.sefrate) / 100.0 )
		}
	}
}	
