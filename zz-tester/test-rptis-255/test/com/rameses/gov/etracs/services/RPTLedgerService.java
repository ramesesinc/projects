/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.gov.etracs.services;

import java.util.List;
import java.util.Map;

public interface RPTLedgerService {
    public Map approve(Map ledger);
    public Map postNewLedger(Map faas);
    public Map saveLedgerItem(Map item);
    public Map removeLedgerItem(Map item);
    public List<Map> getLedgerEntries(Map entity);
    public Map fixLedger(Map info);
    public Map saveNewRevisionLedgerFaas(Map ledgerfaas);
    public void postLedgerItems( Map ledger, int toyear );
    public Map open(Map ledger);
}

