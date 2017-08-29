
import com.rameses.annotations.*;


class RemittanceService {
	

	@DataContext("remittance_cashreceipt")
	def remittanceCashreceiptEm;

	@DataContext("cashreceipt")
	def cashreceiptEm;

	@ProxyMethod
	public def getUnremittedCollections(def o) {
		if(! o.collectorid ) throw new Exception("collectorid must not be null")
		return cashreceiptEm.where( "collector.objid=:collectorid state='APPROVED' AND  remittance.remittanceid IS NULL", [collectorid: o.collectorid ] ).list();
	}


	@ProxyMethod
	public void create(def o) {

	}


	@ProxyMethod
	public def open(def o) {

	}


	@ProxyMethod
	public def getRemittedCollections(def o) {
		if(! o.remittanceid ) throw new Exception("remittanceid must not be null")
		return cashreceiptEm.where( "remittance.remittanceid =:rid", [rid: o.remittanceid ] ).list();
	}



}