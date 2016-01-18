[getList]
SELECT * 
FROM exemptiontype 
WHERE (code LIKE $P{seachtext} OR name LIKE $P{searchtext})
ORDER BY orderno


[getExemptionTypes]
SELECT *
FROM exemptiontype
WHERE state LIKE 'APPROVED'
  AND (code LIKE $P{searchtext} OR name LIKE $P{searchtext} )
ORDER BY orderno


[findById]
SELECT * FROM exemptiontype WHERE objid = $P{objid}

[approve]
UPDATE exemptiontype SET state = 'APPROVED' WHERE objid = $P{objid}
