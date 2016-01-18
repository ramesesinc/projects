[getList]
SELECT a.*
FROM (
	SELECT objid FROM amnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getListByState]
SELECT a.*
FROM (
	SELECT objid FROM amnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
WHERE a.txnstate = $P{state}
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getActiveList]
SELECT a.*
FROM (
	SELECT a.objid FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.borrower_name LIKE $P{searchtext}
	UNION
	SELECT a.objid FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT a.objid FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getListByLedgerid]
SELECT a.*
FROM (
	SELECT objid FROM amnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
WHERE a.ledgerid = $P{ledgerid}
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getCaptureList]
SELECT a.*
FROM (
	SELECT objid FROM amnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
WHERE a.txnmode = 'CAPTURE'
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getCaptureListByState]
SELECT a.*
FROM (
	SELECT objid FROM amnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT objid FROM amnesty
	WHERE refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
WHERE a.txnmode = 'CAPTURE'
	AND a.txnstate = $P{state}
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getActiveCaptureList]
SELECT a.*
FROM (
	SELECT a.objid FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.borrower_name LIKE $P{searchtext}
	UNION
	SELECT a.objid FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT a.objid FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
WHERE a.txnmode = 'CAPTURE'
ORDER BY a.dtcreated DESC, a.dtstarted DESC

[getLookupList]
SELECT b.*, br.address AS borrower_address
FROM (
	SELECT * FROM amnesty
	WHERE borrower_name LIKE $P{searchtext}
	UNION
	SELECT * FROM amnesty
	WHERE loanapp_appno LIKE $P{searchtext}
	UNION
	SELECT * FROM amnesty
	WHERE refno LIKE $P{searchtext}
) b INNER JOIN amnesty a ON b.objid = a.objid
	INNER JOIN borrower br ON br.objid = b.borrower_objid
${filter}
ORDER BY b.dtcreated DESC, b.dtstarted DESC

[getOffers]
SELECT * FROM amnesty_offer
WHERE parentid = $P{objid}
ORDER BY amount

[getActiveAmnestiesByLedgerid]
SELECT a.*,
	(SELECT COUNT(objid) FROM amnesty_detail WHERE parentid = a.objid) AS ledgercount
FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
WHERE a.ledgerid = $P{ledgerid}
ORDER BY a.dtstarted

[getAmnestyDetails]
SELECT d.* 
FROM amnesty_detail d
INNER JOIN ledger_detail_state_type s ON d.state = s.name 
WHERE d.parentid = $P{objid}
ORDER BY d.day, d.dtpaid, d.refno, s.level, d.txndate

[getActiveExpiredAmnesty]
SELECT a.*
FROM (
	SELECT DISTINCT a.objid
	FROM loan_ledger_detail ld
	INNER JOIN amnesty a ON ld.amnestyid = a.objid
) b INNER JOIN amnesty a ON b.objid = a.objid
WHERE a.amnestyoption = 'FIX'
	AND a.dtended < CURDATE()

[findActiveAmnestyByLedgeridAndDate]
SELECT b.*, (SELECT COUNT(objid) FROM amnesty_detail WHERE parentid = b.objid) AS ledgercount
FROM (
	SELECT a.*, CASE WHEN a.dtended IS NULL THEN CURDATE() ELSE a.dtended END AS dateended
	FROM amnesty a
		INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.ledgerid = $P{ledgerid}
) b 
WHERE $P{date} BETWEEN b.dtstarted AND b.dateended
ORDER BY b.dtstarted DESC

[findActiveAmnestyByBorroweridAndDate]
SELECT a.*, CASE WHEN a.dtended IS NULL THEN CURDATE() ELSE a.dtended END AS dtended
FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
WHERE a.borrower_objid = $P{borrowerid}
GROUP BY a.objid
HAVING $P{date} BETWEEN a.dtstarted AND dtended
ORDER BY a.dtstarted DESC

[findLatestAmnesty]
SELECT a.*, CURDATE() AS currentdate
FROM amnesty a
INNER JOIN amnesty_active ac ON a.objid = ac.objid
ORDER BY a.dtstarted DESC

[findLatestAmnestyByLedgerid]
SELECT b.*, CURDATE() AS currentdate
FROM (
	SELECT a.*, CASE WHEN a.dtended IS NULL THEN CURDATE() ELSE a.dtended END AS dateended
	FROM amnesty a
	INNER JOIN amnesty_active ac ON a.objid = ac.objid
	WHERE a.ledgerid = $P{ledgerid}
) b
WHERE CURDATE() BETWEEN b.dtstarted AND b.dateended
ORDER BY b.dtstarted DESC

[findPrevLatestAmnestyByLedgerid]
SELECT a.*, CURDATE() AS currentdate
FROM amnesty a
INNER JOIN amnesty_active ac ON a.objid = ac.objid
WHERE a.ledgerid = $P{ledgerid}
	AND a.dtstarted < $P{dtstarted}
ORDER BY a.dtstarted DESC

[findOverlapping]
SELECT b.*
FROM (
	SELECT a.* 
	FROM (
		SELECT a.*, CASE WHEN a.dtended IS NULL THEN CURDATE() ELSE a.dtended END AS dateended
		FROM amnesty a
		INNER JOIN amnesty_active ac ON a.objid = ac.objid
		WHERE a.ledgerid = $P{ledgerid}
	) a
	WHERE $P{startdate} BETWEEN a.dtstarted AND a.dateended
	UNION
	SELECT a.* 
	FROM (
		SELECT a.*, CASE WHEN a.dtended IS NULL THEN CURDATE() ELSE a.dtended END AS dateended
		FROM amnesty a
		INNER JOIN amnesty_active ac ON a.objid = ac.objid
		WHERE a.ledgerid = $P{ledgerid}
	) a
	WHERE $P{enddate} BETWEEN a.dtstarted AND a.dateended
	UNION
	SELECT a.* 
	FROM (
		SELECT a.*, CASE WHEN a.dtended IS NULL THEN CURDATE() ELSE a.dtended END AS dateended
		FROM amnesty a
		INNER JOIN amnesty_active ac ON a.objid = ac.objid
		WHERE a.ledgerid = $P{ledgerid}
	) a
	WHERE $P{startdate} < a.dtstarted AND $P{enddate} > a.dateended
) b

[findSendBackByParentid]
SELECT sb.* FROM amnesty_sendback sb
WHERE sb.parentid = $P{parentid}
ORDER BY sb.dtcreated DESC

[findSendBackByParentidAndState]
SELECT sb.* FROM amnesty_sendback sb
WHERE sb.parentid = $P{parentid}	
	AND sb.state = $P{state}
ORDER BY sb.dtcreated DESC

[getExpiredListReportData]
SELECT a.*
FROM amnesty a
INNER JOIN amnesty_active ac ON a.objid = ac.objid
WHERE a.dtended BETWEEN $P{startdate} AND $P{enddate}
ORDER BY a.dtended, a.borrower_name

[removeOffers]
DELETE FROM amnesty_offer
WHERE parentid = $P{objid}

[removeDetails]
DELETE FROM amnesty_detail
WHERE parentid = $P{objid}

[changeState]
UPDATE amnesty SET txnstate = $P{txnstate}
WHERE objid = $P{objid}
