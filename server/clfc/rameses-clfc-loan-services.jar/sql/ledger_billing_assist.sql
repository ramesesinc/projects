[findByPrevbillingid]
SELECT * FROM ledger_billing_assist
WHERE prevbillingid = $P{objid}
LIMIT 1