[findLoanCountByAcctid]
SELECT IFNULL(MAX(loancount),0) AS loancount 
FROM loan_ledger WHERE acctid=$P{acctid} 

[getCollectionsheets]
SELECT ll.objid, la.objid AS loanappid, la.appno, ll.acctname, ll.dailydue,
		ll.dtlastpaid, ll.dtstarted, ll.overduepenalty, ll.dtmatured,
		ll.producttypeid, ll.balance, ll.overpaymentamount, la.loanamount,
		ll.absentpenalty, ll.dtmatured, ll.paymentmethod, ll.interestamount,
		ll.dtcurrentschedule, la.dtcreated AS loandate, ll.term, ll.acctid,
		c.address_text AS homeaddress, ll.compromiseid
FROM loan_ledger ll
INNER JOIN loanapp la ON ll.appid = la.objid
INNER JOIN customer c ON c.objid = ll.acctid
WHERE la.route_code = $P{route_code}
	AND ll.state = 'OPEN'

[xgetCollectionsheets]
SELECT ll.objid, la.objid AS loanappid, la.appno, ll.acctname, ll.dailydue,
		ll.dtlastpaid, ll.dtstarted, ll.overduepenalty, ll.dtmatured,
		ll.producttypeid, ll.balance, ll.overpaymentamount, la.loanamount,
		ll.absentpenalty, ll.dtmatured, ll.paymentmethod, ll.interestamount,
		ll.dtcurrentschedule, la.dtcreated AS loandate, ll.term, ll.acctid,
		c.address_text AS homeaddress, ll.compromiseid, 
		CASE
			WHEN ac.dtreleased IS NOT NULL THEN ac.dtreleased ELSE NULL
		END AS dtreleased
FROM loan_ledger ll
INNER JOIN loanapp la ON ll.appid = la.objid
LEFT JOIN loanapp_capture ac ON la.objid = ac.objid
INNER JOIN customer c ON c.objid = ll.acctid
WHERE la.route_code = '${route_code}'
	AND ll.state = 'OPEN'

[findByAppId]
SELECT * FROM loan_ledger
WHERE appid=$P{appid}

[findLastLedgerItemByParentId]
SELECT * FROM loan_ledger_detail
WHERE parentid=$P{parentid}
	AND state NOT IN ('OFFSET', 'AMNESTY', 'PREPAIDINTEREST')
ORDER BY day DESC, dtpaid DESC

[updateDtlastpaid]
UPDATE loan_ledger SET dtlastpaid=$P{dtlastpaid} WHERE objid=$P{objid}

[findLastLedgerItemNotSameDatePaid]
SELECT * FROM loan_ledger_detail
WHERE dtpaid < $P{dtpaid}
	AND parentid=$P{parentid}
	AND refno IS NOT NULL
ORDER BY dtpaid DESC
LIMIT 1

[getList]
SELECT ll.objid AS ledgerid, la.borrower_objid, la.objid AS appid, 
	la.borrower_name, ll.producttypeid, la.appno, la.route_code, ll.dailydue, 
	lr.description AS route_description, lr.area AS route_area, ll.dtstarted, 
	ll.dtmatured, ll.state, la.loanamount, ll.interestamount AS interest, 
	ll.compromiseid, ll.overduepenalty, ll.dtlastpaid,
	ll.balance, CASE WHEN NOW() > dtmatured THEN 1 ELSE 0 END AS ismatured
FROM loan_ledger ll
INNER JOIN loanapp la ON ll.appid = la.objid
INNER JOIN loan_route lr ON la.route_code = lr.code
WHERE la.state IN('RELEASED','CLOSED')
	AND la.borrower_name LIKE $P{searchtext}
ORDER BY la.borrower_name, ll.dtstarted DESC

[getListByState]
SELECT ll.objid AS ledgerid, la.borrower_objid, la.objid AS appid, 
	la.borrower_name, ll.producttypeid, la.appno, la.route_code, ll.dailydue, 
	lr.description AS route_description, lr.area AS route_area, ll.dtstarted, 
	ll.dtmatured, ll.state, la.loanamount, ll.interestamount AS interest, 
	ll.compromiseid, ll.overduepenalty, ll.dtlastpaid,
	ll.balance, CASE WHEN NOW() > dtmatured THEN 1 ELSE 0 END AS ismatured
FROM loan_ledger ll
INNER JOIN loanapp la ON ll.appid = la.objid
INNER JOIN loan_route lr ON la.route_code = lr.code
WHERE ll.state = $P{state}
	AND la.borrower_name LIKE $P{searchtext}
ORDER BY la.borrower_name, ll.dtstarted DESC

[getListByRootidAndTypeid]
SELECT l.objid AS ledgerid, la.borrower_objid, la.objid AS appid,
	la.borrower_name, l.producttypeid, la.appno, la.route_code, l.dailydue,
	r.description AS route_description, r.area AS route_area, l.dtstarted,
	l.dtmatured, l.state, la.loanamount, l.interestamount AS interest,
	l.overduepenalty, l.balance
FROM (
	SELECT b.objid
	FROM (
		SELECT a.objid
		FROM loan_ledger a
		INNER JOIN loan_ledger_segregation s ON a.objid = s.refid
		WHERE a.state = 'OPEN'
			AND s.typeid = $P{rootid}
	) b 
	INNER JOIN (
		SELECT a.objid
		FROM loan_ledger a
		INNER JOIN loan_ledger_segregation s ON a.objid = s.refid
		WHERE a.state = 'OPEN'
			AND s.typeid = $P{typeid}
	) b2 ON b.objid = b2.objid
) qry INNER JOIN loan_ledger l ON qry.objid = l.objid
INNER JOIN loanapp la ON l.appid = la.objid
INNER JOIN loan_route r ON la.route_code = r.code
WHERE l.state = 'OPEN'
	AND la.borrower_name LIKE $P{searchtext}
ORDER BY la.borrower_name, l.dtstarted DESC

[getListBySegregationType]
SELECT l.objid AS ledgerid, la.borrower_objid, la.objid AS appid,
	la.borrower_name, l.producttypeid, la.appno, la.route_code, l.dailydue,
	r.description AS route_description, r.area AS route_area, l.dtstarted,
	l.dtmatured, l.state, la.loanamount, l.interestamount AS interest,
	l.overduepenalty, l.balance
FROM loan_ledger l
INNER JOIN loan_ledger_segregation ls ON l.objid = ls.refid
INNER JOIN loanapp la ON l.appid = la.objid
INNER JOIN loan_route r ON la.route_code = r.code
WHERE l.state = 'OPEN'
	AND la.borrower_name LIKE $P{searchtext}
	AND ls.typeid = $P{type}
ORDER BY la.borrower_name, l.dtstarted DESC

[getLedgerDetailsByLedgerid]
SELECT d.* 
FROM loan_ledger_detail d
INNER JOIN ledger_detail_state_type s ON d.state = s.name
WHERE d.parentid = $P{parentid}
ORDER BY d.day, d.dtpaid, d.refno, s.level, d.txndate

[getLedgerDetailsByLedgeridWithoutAmnesty]
SELECT d.* 
FROM loan_ledger_detail d
INNER JOIN ledger_detail_state_type s ON d.state = s.name
WHERE d.parentid = $P{parentid}
	AND d.amnestyid IS NULL
ORDER BY d.day, d.dtpaid, d.refno, s.level, d.txndate

[getPaymentsFromLedgerDetail]
SELECT ll.appid AS appid, lld.refno, lld.dtpaid AS txndate, 
		lld.amtpaid AS payamount
FROM loan_ledger_detail lld
INNER JOIN loan_ledger ll ON lld.parentid=ll.objid
WHERE lld.parentid=$P{parentid} 
	AND lld.amtpaid > 0 
	AND lld.state='RECEIVED'
ORDER BY lld.dtpaid, lld.refno

[removeLedgerDetail]
DELETE FROM loan_ledger_detail
WHERE parentid=$P{parentid}

[removeLedgerDetailByParentidAndAmnestyid]
DELETE FROM loan_ledger_detail
WHERE parentid = $P{parentid}
	AND amnestyid = $P{amnestyid}

[findLedgerById]
SELECT ll.*, la.appno, la.loanamount, la.route_code AS routecode,
	(SELECT COUNT(objid) FROM loan_ledger_detail WHERE parentid = ll.objid) AS ledgercount
FROM loan_ledger ll
INNER JOIN loanapp la
ON ll.appid = la.objid
WHERE ll.objid = $P{ledgerid}

[findLedgerByIdWithoutAmnestyCount]
SELECT ll.*, la.appno, la.loanamount, la.route_code AS routecode,
	(
		SELECT COUNT(objid) 
		FROM loan_ledger_detail 
		WHERE parentid = ll.objid
			AND amnestyid IS NULL
	) AS ledgercount
FROM loan_ledger ll
INNER JOIN loanapp la
ON ll.appid = la.objid
WHERE ll.objid = $P{ledgerid}

[findLedgerWithInfo]
SELECT l.*, la.objid AS loanapp_objid, la.appno AS loanapp_appno, la.loanamount AS loanapp_loanamount,
		r.code AS route_code, r.description AS route_description, r.area AS route_area,
		CONCAT(r.description, " - ", r.area) AS route_name, c.objid AS borrower_objid,
		c.name AS borrower_name, c.address_text AS borrower_address,
		(SELECT COUNT(objid) FROM loan_ledger_detail WHERE parentid = l.objid) AS ledgercount
FROM loan_ledger l
INNER JOIN loanapp la ON l.appid = la.objid
INNER JOIN loan_route r ON la.route_code = r.code
INNER JOIN customer c ON l.acctid = c.objid
WHERE l.objid = $P{objid}

[findDetailByParentidAndAmnestyid]
SELECT l.*
FROM loan_ledger_detail l
WHERE l.parentid = $P{parentid}
	AND l.amnestyid = $P{amnestyid}

[findAmnestyDetailByParentidAndAmnestyid]
SELECT l.*
FROM loan_ledger_detail l
WHERE l.parentid = $P{parentid}
	AND l.amnestyid = $P{amnestyid}
	AND l.state = 'AMNESTY'

[getAmnestyidsByParentid]
SELECT DISTINCT l.amnestyid
FROM loan_ledger_detail l
WHERE l.amnestyid IS NOT NULL
	AND l.parentid = $P{parentid}

[getOpenLedgersByCurrentDateGreaterThanMaturityDate]
SELECT * FROM loan_ledger
WHERE state='OPEN'
	AND $P{date} > dtmatured
ORDER BY acctname, dtstarted DESC

[getOpenLedgers]
SELECT c.objid AS borrower_objid, ll.acctname AS borrower_name, c.address_text AS borrower_address,
	lr.code AS route_code, lr.description AS route_description, lr.area AS route_area,
	la.appno AS loanapp_appno, la.objid AS loanapp_objid, ll.*, la.loanamount AS loanapp_loanamount,
	CONCAT(lr.description, ' - ', lr.area) AS route_name, ac.dtreleased, ll.dtmatured
FROM loan_ledger ll
INNER JOIN customer c ON ll.acctid = c.objid
INNER JOIN loanapp la ON ll.appid = la.objid
INNER JOIN loanapp_capture ac ON la.objid = ac.objid
INNER JOIN loan_route lr ON la.route_code = lr.code
WHERE ll.state = 'OPEN'
	AND ll.acctname LIKE $P{searchtext}
ORDER BY ll.acctname, ll.dtstarted DESC

[getOpenPastDueLedgers]
SELECT c.objid AS borrower_objid, ll.acctname AS borrower_name, c.address_text AS borrower_address,
	lr.code AS route_code, lr.description AS route_description, lr.area AS route_area,
	la.appno AS loanapp_appno, la.objid AS loanapp_objid, ll.*, la.loanamount AS loanapp_loanamount,
	CONCAT(lr.description, ' - ', lr.area) AS route_name, ac.dtreleased, ll.dtmatured
FROM loan_ledger ll
INNER JOIN customer c ON ll.acctid = c.objid
INNER JOIN loanapp la ON ll.appid = la.objid
INNER JOIN loanapp_capture ac ON la.objid = ac.objid
INNER JOIN loan_route lr ON la.route_code = lr.code
WHERE ll.state = 'OPEN'
	AND CURDATE() > ll.dtmatured
	AND ll.acctname LIKE $P{searchtext}
ORDER BY ll.acctname, ll.dtstarted DESC

[getPastDueLedgers]
SELECT c.objid AS borrower_objid, ll.acctname AS borrower_name, c.address_text AS borrower_address,
	lr.code AS route_code, lr.description AS route_description, lr.area AS route_area,
	la.appno AS loanapp_appno, la.objid AS loanapp_objid, ll.*, la.loanamount AS loanapp_loanamount,
	CONCAT(lr.description, ' - ', lr.area) AS route_name, DATE_ADD(ll.dtstarted, INTERVAL -1 DAY) AS dtreleased
FROM loan_ledger ll
INNER JOIN customer c ON ll.acctid=c.objid
INNER JOIN loanapp la ON ll.appid=la.objid
INNER JOIN loan_route lr ON la.route_code=lr.code
WHERE ll.state = 'OPEN'
	AND ll.acctname LIKE $P{searchtext}
	AND CURDATE() > ll.dtmatured
ORDER BY ll.acctname, ll.dtstarted DESC

[getOpenLedgersByRoute]
SELECT c.objid AS borrower_objid, ll.acctname AS borrower_name, c.address_text AS borrower_address,
	lr.code AS route_code, lr.description AS route_description, lr.area AS route_area,
	la.appno AS loanapp_appno, la.objid AS loanapp_objid, ll.*, la.loanamount AS loanapp_loanamount
FROM loan_ledger ll
INNER JOIN customer c ON ll.acctid=c.objid
INNER JOIN loanapp la ON ll.appid=la.objid
INNER JOIN loan_route lr ON la.route_code=lr.code
WHERE ll.state='OPEN'
	AND ll.acctname LIKE $P{searchtext}
	AND lr.code = $P{routecode}
ORDER BY ll.acctname, ll.dtstarted DESC

[changeState]
UPDATE loan_ledger SET state = $P{state}
WHERE objid = $P{objid}

[updateLedgerPayment]
UPDATE loan_ledger_payment SET
	parentid = $P{parentid},
	refno = $P{refno},
	txndate = $P{txndate},
	amount = $P{amount}
WHERE objid = $P{objid}