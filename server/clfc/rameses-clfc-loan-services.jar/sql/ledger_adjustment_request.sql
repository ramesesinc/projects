[findRequestByAdjustmentidAndTxnstate]
SELECT a.* FROM ledger_adjustment_request a
WHERE a.txnstate = $P{txnstate}
	AND a.adjustmentid = $P{adjustmentid}
ORDER BY a.dtcreated DESC

[findRequestByAdjustmentidAndTxnstateAndTxntype]
SELECT a.* FROM ledger_adjustment_request a
WHERE a.txnstate = $P{txnstate}
	AND a.adjustmentid = $P{adjustmentid}
	AND a.txntype = $P{txntype}
ORDER BY a.dtcreated DESC

[changeState]
UPDATE ledger_adjustment_request SET txnstate = $P{txnstate}
WHERE objid = $P{objid}