[buildBillingAccounts]
INSERT IGNORE INTO waterworks_billing_account 
(objid, parentid, acctid, state, 
	prevyear, prevmonth, prevreadingdate, prevreading, 
	discrate, surcharge, interest, credit )
SELECT 
CONCAT( 'WBILLACCT-', UUID() ), $P{billid}, wa.objid, 'DRAFT', 
    wb.year, wb.month, wa.lastdateread, wa.currentreading, 
    0, 0, 0, 0 
FROM waterworks_account wa
INNER JOIN waterworks_billing_cycle wb ON wa.billingcycleid = wb.objid
WHERE wa.sectorid = $P{sectorid}
