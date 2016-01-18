[getList]
SELECT ba.*, b.branchname, ba.fund_code, ba.fund_title 
FROM bankaccount ba 
INNER JOIN bank b ON ba.bank_objid=b.objid
ORDER BY ba.code

[getListByBankAccountCode]
SELECT ba.*, b.branchname, ba.fund_code, ba.fund_title 
FROM bankaccount ba 
INNER JOIN bank b ON ba.bank_objid=b.objid
WHERE ba.code LIKE $P{searchtext}
ORDER BY ba.code

[getListByBankCode]
SELECT ba.*, b.branchname, ba.fund_code, ba.fund_title 
FROM bankaccount ba 
INNER JOIN bank b ON ba.bank_objid=b.objid
WHERE ba.bank_code LIKE $P{searchtext}
ORDER BY ba.code

[getListByBankName]
SELECT ba.*, b.branchname, ba.fund_code, ba.fund_title 
FROM bankaccount ba 
INNER JOIN bank b ON ba.bank_objid=b.objid
WHERE ba.bank_name LIKE $P{searchtext}
ORDER BY ba.code

[approve]
UPDATE bankaccount SET state='APPROVED' WHERE objid=$P{objid} 


[getEntries]
SELECT refdate,refno,reftype,particulars,dr,cr,runbalance,"lineno" 
FROM bankaccount_entry 
WHERE parentid=$P{objid} order by "lineno" 


[getListByFund]
SELECT ba.*
FROM bankaccount ba 
WHERE ba.fund_objid = $P{fundid}


[getListBySpecialFund]
SELECT ba.*
FROM bankaccount ba 
	INNER JOIN fund f ON ba.fund_objid = f.objid 
WHERE ba.fund_objid LIKE $P{fundid}
  AND f.special = 1 