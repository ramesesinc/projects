[getList]
SELECT objid, 
acctno AS code, 
accttitle AS title,
fundid AS fund_objid,
fundid AS fund_code,
fundname AS fund_title
FROM incomeaccount
WHERE accttitle LIKE $P{searchtext}

[findAccount]
SELECT r.objid, r.acctno as code, r.accttitle AS title, 
r.fundid AS fund_objid, r.fundid AS fund_code, r.fundname AS fund_title 
FROM incomeaccount r 
WHERE r.objid=$P{objid}
