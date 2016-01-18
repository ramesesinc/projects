package bpls.actions;

import com.rameses.rules.common.*;
import bpls.facts.*;

public class AskBusinessInfo extends AbstractBusinessInfoAction implements RuleActionHandler {

	def request;

	public void execute(def params, def drools) {
		def lob = params.lob;
		def attrid = params.attribute.key;
		def defvalue = params.defaultvalue;
		def entity = request.entity;
		def newinfos = request.newinfos;
		def info = getInfo( entity, newinfos, lob, attrid, null, request.phase );
		if(info) info.defaultvalue = defvalue;
	}

}

