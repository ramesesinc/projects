package com.rameses.gov.etracs.rptis.models;
        
import com.rameses.rcp.annotations.* 
import com.rameses.rcp.common.* 
import com.rameses.osiris2.client.*
import com.rameses.osiris2.common.*

public class ChangeLandReferenceModel 
{
	@Binding 
	def binding;

	@Service('PersistenceService')
	def persistence; 

	def entity;
	def improvfaas;
	def landfaas;



	void init() {
		if (entity){
			improvfaas = entity;
			improvfaas.rputype = entity.rpu.rputype;
			improvfaas.pin = entity.rp.pin;
			improvfaas.ry = entity.rp.ry;
		}
	}

	def save() {
		if (!improvfaas) {
			throw new Exception('Improvement TD No. must be specified.')
		}

		if (MsgBox.confirm('Save land reference?')) {
			def schemaname = improvfaas.rputype + 'rpu';
			def info = [_schemaname: schemaname];
			info.landrpuid = landfaas.rpuid;
			info.findBy = [objid: improvfaas.rpuid];
			persistence.update(info);
			if (entity) {
				entity.rpu.landrpuid = info.landrpuid;
			}
			return '_close';
		}
	}


	def getLookupImprovementFaas() {
		return Inv.lookupOpener('faas:lookup', [
			onselect: {
				if (it.rputype == 'land') throw new Exception('Invalid FAAS. Only improvement FAAS is not allowed.')
				if (it.state == 'CANCELLED') throw new Exception('FAAS is already cancelled.')
				improvfaas = it;
			},
			onempty: {
				improvfaas = null;
				binding.refresh('improve.*');
			}
		]);
	}

	def getLookupLandFaas() {
		return Inv.lookupOpener('faas:lookup', [
			onselect: {
				if (it.rputype != 'land') throw new Exception('Invalid FAAS. Only land FAAS is allowed.');
				if (it.state == 'CANCELLED') throw new Exception('FAAS is already cancelled.');
				if (it.ry != improvfaas.ry) throw new Exception('Land FAAS revision year should be equal to ' + improvfaas.ry + '.');
				if (!improvfaas.pin.startsWith(it.pin)) throw new Exception('Land PIN should be equal to improvement PIN.');
				landfaas = it;
			},
			onempty: {
				landfaas = null;
				binding.refresh('landfaas.*');
			}
		]);
	}
}


