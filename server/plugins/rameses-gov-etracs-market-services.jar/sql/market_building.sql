[getList]
SELECT * FROM market_building WHERE marketid=$P{marketid}

[approve]
UPDATE market_building SET state='APPROVED' WHERE objid=$P{objid}