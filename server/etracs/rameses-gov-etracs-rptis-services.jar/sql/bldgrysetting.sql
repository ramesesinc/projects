[getList]
SELECT * FROM bldgrysetting 
${filter}
ORDER BY ry 


#=============================================================
# DELETE BldgRySetting
#=============================================================
[deleteAssessLevelRanges]
DELETE FROM bldgassesslevelrange WHERE bldgrysettingid = $P{objid} 


[deleteAssessLevels]
DELETE FROM bldgassesslevel WHERE bldgrysettingid = $P{objid} 


[deleteAdditionalItems]
DELETE FROM bldgadditionalitem WHERE bldgrysettingid = $P{objid} 


[deleteBldgKindBuccs]
DELETE FROM bldgkindbucc WHERE bldgrysettingid = $P{objid} 


[deleteDepreciations]
DELETE FROM bldgtype_depreciation WHERE bldgrysettingid = $P{objid} 


[deleteStoreyAdjustments]
DELETE FROM bldgtype_storeyadjustment WHERE bldgrysettingid = $P{objid} 


[deleteBldgTypes]
DELETE FROM bldgtype WHERE bldgrysettingid = $P{objid} 


[deleteRySettingLgus]
DELETE FROM rysetting_lgu WHERE rysettingid = $P{rysettingid}


#=============================================================
# ASSESSLEVEL / RANGE SUPPORT
#=============================================================
[getAssessLevels]
SELECT bal.*, pc.code AS classification_code, pc.name AS classification_name 
FROM bldgassesslevel bal 
	LEFT JOIN propertyclassification pc ON bal.classification_objid = pc.objid
WHERE bal.bldgrysettingid = $P{bldgrysettingid}
ORDER BY pc.orderno


[getRanges]
SELECT * FROM bldgassesslevelrange WHERE bldgassesslevelid = $P{bldgassesslevelid} ORDER BY mvfrom 


[deleteRanges]
DELETE FROM bldgassesslevelrange WHERE bldgassesslevelid = $P{bldgassesslevelid}


#=============================================================
# BLDGTYPE SUPPORT
#=============================================================
[getBldgTypes]
SELECT * FROM bldgtype bt WHERE bldgrysettingid = $P{bldgrysettingid} ORDER BY code 



#=============================================================
# BLDGKINDBUCC SUPPORT
#=============================================================
[getBldgKindBuccs]
SELECT 
	bkb.*, 
	bt.code AS bldgtype_code, bt.name AS bldgtype_name, 
	bk.code AS bldgkind_code, bk.name AS bldgkind_name
FROM bldgkindbucc bkb
	INNER JOIN bldgtype bt ON bkb.bldgtypeid = bt.objid
	INNER JOIN bldgkind bk ON bkb.bldgkind_objid = bk.objid 
WHERE bkb.bldgtypeid = $P{bldgtypeid} 
ORDER BY bk.code 


[deleteBldgKindBuccByTypeId]
DELETE FROM bldgkindbucc WHERE bldgtypeid = $P{bldgtypeid}



#=============================================================
# DEPRECIATION SUPPORT
#=============================================================
[getDepreciations]
SELECT * FROM bldgtype_depreciation WHERE bldgtypeid = $P{bldgtypeid} ORDER BY agefrom


[deleteDepreciationByTypeId]
DELETE FROM bldgtype_depreciation WHERE bldgtypeid = $P{bldgtypeid} 



#=============================================================
# STOREYADJUSTMENT SUPPORT
#=============================================================
[getStoreyAdjustments]
SELECT * FROM bldgtype_storeyadjustment WHERE bldgtypeid = $P{bldgtypeid}


[deleteStoreyAdjustmentByTypeId]
DELETE FROM bldgtype_storeyadjustment WHERE bldgtypeid = $P{bldgtypeid}



#=============================================================
# ADDITIONALITEM SUPPORT
#=============================================================
[getAdditionalItems]
SELECT * FROM bldgadditionalitem WHERE bldgrysettingid = $P{bldgrysettingid} ORDER BY code 

[getAdditionalItemsByFilter]
SELECT * 
FROM bldgadditionalitem 
WHERE objid in (
	select objid from bldgadditionalitem where bldgrysettingid = $P{objid}  and code like $P{searchtext}
	UNION 
	select objid from bldgadditionalitem where bldgrysettingid = $P{objid}  and name like $P{searchtext}
)
ORDER BY code 

[findAdditionalItemByCode]
select * from bldgadditionalitem where code = $P{code} and bldgrysettingid = $P{bldgrysettingid}

[findAdditionalItemByName]
select * from bldgadditionalitem where name = $P{name} and bldgrysettingid = $P{bldgrysettingid}


#=============================================================
# MISC SUPPORT 
#=============================================================
[getIdByRy]
SELECT objid FROM bldgrysetting WHERE ry = $P{ry} 

[findIdByRy]
SELECT objid FROM bldgrysetting WHERE ry = $P{ry} 

[getSettingsByRy]
SELECT objid FROM bldgrysetting WHERE ry = $P{ry} 


[findDuplicate]
SELECT objid 
FROM bldgrysetting 
WHERE ry = $P{ry} 
AND previd = $P{previd} 



[getSettingsByLguIdRy]
SELECT s.*
FROM bldgrysetting s
	INNER JOIN rysetting_lgu l ON s.objid = l.rysettingid
WHERE l.lguid = $P{lguid}
  AND s.ry = $P{ry}