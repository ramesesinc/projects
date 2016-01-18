[getList]
SELECT * FROM bank 
WHERE (code LIKE $P{code} OR name LIKE $P{name}) AND depository LIKE $P{depository}
ORDER BY code, name, branchname

[approve]
UPDATE bank SET state='APPROVED' WHERE objid=$P{objid}