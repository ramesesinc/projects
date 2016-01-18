[getList]
SELECT * FROM dailycollection
ORDER BY txndate DESC

[getListByState]
SELECT * FROM dailycollection
WHERE state = $P{state}
ORDER BY txndate DESC

[getCbs]
SELECT * FROM dailycollection_cbs
WHERE parentid = $P{objid}
ORDER BY txndate DESC, cbsno DESC

[getOverages]
SELECT * FROM dailycollection_overage
WHERE parentid = $P{objid}
ORDER BY txndate DESC, refno DESC

[getShortages]
SELECT * FROM dailycollection_shortage
WHERE parentid = $P{objid}
ORDER BY txndate DESC, cbsno DESC

[getEncashments]
SELECT * FROM dailycollection_encashment
WHERE parentid = $P{objid}
ORDER BY txndate DESC

[getDepositSlips]
SELECT * FROM dailycollection_depositslip
WHERE parentid = $P{objid}
ORDER BY txndate DESC, controlno DESC

[findDailyCollectionByTxndate]
SELECT * FROM dailycollection
WHERE txndate = $P{txndate}

[findCbsByRefid]
SELECT * FROM dailycollection_cbs
WHERE refid = $P{refid}

[findEncashmentByRefid]
SELECT * FROM dailycollection_encashment
WHERE refid = $P{refid}

[findCurrentSendback]
SELECT s.*
FROM dailycollection_sendback s
WHERE s.parentid = $P{objid}
ORDER BY s.dtcreated DESC

[changeState]
UPDATE dailycollection SET state = $P{state}
WHERE objid = $P{objid}