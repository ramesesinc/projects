[getList]
SELECT * FROM physician

[getLookup]
SELECT * FROM physician

[findById]
SELECT * FROM physician WHERE objid=$P{objid}


[updateUsername]
UPDATE physician SET username=$P{username} WHERE objid=$P{objid}

