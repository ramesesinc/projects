package gov.zamboanga.treasury.models;

import com.rameses.rcp.annotations.*;
import com.rameses.rcp.common.*;
import com.rameses.osiris2.client.*;
import com.rameses.osiris2.common.*;
import com.rameses.enterprise.treasury.cashreceipt.*;

class MtopCollectionModel extends BasicCashReceipt {
	@Service('ZamboangaCollectionMtopService')
	def svc;

    def zoningcode;
    def franchiseno;


    void searchFranchise() {
        validateFranchiseInfo();
        
        try {
            entity.mtop = [:]
            entity.mtop.zoningcode = zoningcode;
            entity.mtop.franchiseno = franchiseno;
            entity.mtop.putAll(svc.getFranchise(entity.mtop));
            entity.items = entity.mtop.items;
            itemListModel.reload();
            updateBalances();
            if (!entity.paidby) {
                entity.paidby = entity.mtop.operator.name
                entity.paidbyaddress = entity.mtop.operator.address
                binding.refresh('entity.paidby.*');
            }
            binding.refresh('franchiseno');
        } catch(e) {
            resetInfo();
            throw e;
        }
    }
    
    void validateFranchiseInfo() {
        if (!zoningcode) {
            binding.requestFocus('zoningcode');
            throw new Exception('Zone No. is required');
        } 
        if (!franchiseno) {
            binding.requestFocus('franchiseno');
            throw new Exception('Franchise No. is required');
        }
    }
    
    void resetInfo() {
        entity.mtop = [:];
        entity.paidby = null;
        entity.paidbyaddress = null;
        entity.items = [];
        itemListModel.reload();
        updateBalances();
        binding.refresh('entity.paid.*');
        binding.requestFocus('franchiseno');
    }
}