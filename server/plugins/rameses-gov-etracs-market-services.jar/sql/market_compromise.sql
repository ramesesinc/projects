[getList]
SELECT b.* FROM market_compromise WHERE acctid=$P{acctid} 

[approve]
UPDATE market_compromise SET state='APPROVED' WHERE objid=$P{objid}
