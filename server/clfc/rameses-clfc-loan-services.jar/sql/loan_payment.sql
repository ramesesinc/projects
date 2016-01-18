[getList]
SELECT lp.*, llb.collector_objid, llb.collector_name
FROM loan_payment lp
INNER JOIN loan_ledger_billing llb ON lp.objid=llb.objid
ORDER BY lp.txndate

[getDetailsWithNoControlByParentid]
SELECT * FROM loan_payment_detail
WHERE parentid=$P{parentid}
	AND control_objid IS NULL

[getDetailsByParentid]
SELECT * FROM loan_payment_detail
WHERE parentid=$P{parentid}

[getPaymentsByTxndate]
SELECT * FROM loan_payment
WHERE txndate=$P{txndate}