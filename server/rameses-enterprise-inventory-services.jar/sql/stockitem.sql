[getList]
SELECT r.* 
FROM stockitem r 
WHERE r.itemclass = $P{itemclass}

[approve]
update stockitem set state='APPROVED' where objid=$P{objid}

[getItemClasses]
SELECT DISTINCT 
itemclass, itemclass as caption 
FROM stockitem

[getLookup]
SELECT r.objid, r.code, r.title, su.unit 
FROM stockitem r
INNER JOIN stockitem_unit su ON r.objid=su.itemid 
WHERE r.itemclass =  $P{itemclass}
AND r.code LIKE $P{searchtext}

[getUnits]
SELECT su.*, CASE WHEN si.objid IS NULL THEN 0 ELSE 1 END AS defaultunit
FROM stockitem_unit su 
LEFT JOIN stockitem si ON su.itemid = si.objid AND si.defaultunit=su.unit
WHERE su.itemid=$P{itemid}

[updateDefault]
UPDATE stockitem SET defaultunit=$P{unit} WHERE objid=$P{objid}

[findUnitQty]
SELECT qty FROM stockitem_unit WHERE itemid=$P{itemid} AND unit=$P{unit}

[findItemType]
SELECT type FROM stockitem WHERE objid=$P{objid}

[removeItemUnit]
delete from stockitem_unit where itemid=$P{objid}
