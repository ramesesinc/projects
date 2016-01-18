[getList]
SELECT a.* FROM loanar_liquidation a
WHERE a.refno LIKE $P{searchtext}
ORDER BY a.dtcreated DESC

[getListByState]
SELECT a.* FROM loanar_liquidation a
WHERE a.refno LIKE $P{searchtext}
	AND a.txnstate = $P{txnstate}
ORDER BY a.dtcreated DESC

[getArList]
SELECT a.* FROM loanar_liquidation_ar a 
WHERE a.parentid = $P{objid}

[getArDetailList]
SELECT a.* FROM loanar_liquidation_ardetail a
WHERE a.parentid = $P{objid}

[getArDetailBreakdownList]
SELECT a.* FROM loanar_liquidation_breakdown a
WHERE a.parentid = $P{objid}

[findLiquidationSendBackByLiquidationidAndState]
SELECT s.*
FROM loanar_liquidation_sendback s
WHERE s.liquidationid = $P{liquidationid}
	AND s.txnstate = $P{state}
ORDER BY s.dtcreated DESC

[changeState]
UPDATE loanar_liquidation SET txnstate = $P{txnstate}
WHERE objid = $P{objid}