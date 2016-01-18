[getList]
SELECT * FROM marketsection

[approve]
UPDATE marketsection SET state='APPROVED' WHERE objid=$P{objid}




