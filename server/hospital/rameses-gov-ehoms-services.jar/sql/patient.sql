[getList]
SELECT b.* FROM 
(
	SELECT * FROM patient WHERE lastname LIKE $P{searchtext} 
	UNION 
	SELECT * FROM patient WHERE firstname LIKE $P{searchtext} 
	UNION 
	SELECT * FROM patient WHERE acctid LIKE $P{searchtext} 
) b

[getLookup]
SELECT b.* FROM 
(
	SELECT * FROM patient WHERE lastname LIKE $P{searchtext} 
	UNION 
	SELECT * FROM patient WHERE firstname LIKE $P{searchtext} 
	UNION 
	SELECT * FROM patient WHERE acctid LIKE $P{searchtext} 
) b

[findPhoto]
SELECT photo FROM patient WHERE objid=$P{objid}

[updatePhoto]
UPDATE patient SET photo=$P{photo}, thumbnail=$P{thumbnail} WHERE objid=$P{objid}

[findThumbnail]
SELECT thumbnail FROM patient WHERE objid=$P{objid}

[getLookupNames]
SELECT a.* FROM 
(SELECT objid, CONCAT( lastname, ', ', firstname) AS name 
FROM patient
WHERE lastname LIKE $P{lastname}) a
WHERE a.name LIKE  $P{searchtext} ORDER BY a.name 

[getLookupMatches]
SELECT objid, lastname, firstname, gender, birthdate FROM patient WHERE ${filter}

[getReligionList]
SELECT DISTINCT religion FROM patient WHERE religion LIKE $P{searchtext}

[getNationalityList]
SELECT DISTINCT nationality FROM patient WHERE nationality LIKE $P{searchtext} 

