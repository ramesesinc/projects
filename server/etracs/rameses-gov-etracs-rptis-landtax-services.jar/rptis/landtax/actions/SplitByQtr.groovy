package rptis.landtax.actions;

import com.rameses.rules.common.*;
import rptis.landtax.facts.*;

public class SplitByQtr implements RuleActionHandler {
	def billingSvc 

	public void execute(def params, def drools) {
		def item = params.rptledgeritem
		item.entity.qtrly = 1
		billingSvc.setLedgerItemQtrlyFlag(item)
	}
}