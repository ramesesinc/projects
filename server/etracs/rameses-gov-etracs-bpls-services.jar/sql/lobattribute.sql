[getList]
SELECT *
FROM lobattribute
WHERE name LIKE $P{searchtext}

[changeState-approved]
UPDATE lobattribute SET state = 'APPROVED'
WHERE objid = $P{objid}

[approve]
UPDATE lobattribute SET state='APPROVED' WHERE objid=$P{objid}