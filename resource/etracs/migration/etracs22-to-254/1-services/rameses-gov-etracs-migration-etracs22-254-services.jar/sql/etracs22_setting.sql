[findLguByType]
select * from lgu where lgutype = $P{lgutype}

[findClassification]
select objid 
from propertyclassification
where propertycode = $P{code} or propertydesc = $P{name}

[findAgriClassification]
select * 
from propertyclassification 
where propertycode in ('A', 'AGR') or propertydesc = 'AGRICULTURAL'


#============= LAND  ==============================
[getLandSettings]
SELECT 
	objid,
	'DRAFT' as state,
	ry,
	previd,
	$P{appliedto} as appliedto 
FROM landrysetting


[getLandAssessLevels]
SELECT 
	objid,
	landrysettingid,
	classcode as code,
	classname as name,
	fixrate,
	rate,
	previd,
	ranges
FROM landassesslevel


[getSpecificClasses]
SELECT 
	spc.objid, 
	spc.landrysettingid, 
	spc.objid as landspecificclass_objid, 
	p.objid as classification_objid, 
	spc.classcode as code, 
	spc.classname as name, 
	spc.areatype as areatype, 
	spc.previd 
FROM lcuvspecificclass spc
inner join lcuv l on spc.lcuvid = l.objid
inner join propertyclassification p on l.classcode = p.propertycode


[getSubClasses]
SELECT 
	sub.objid,
	sub.specificclassid as specificclass_objid,
	sub.landrysettingid,
	sub.subclasscode as code,
	sub.subclassname as name,
	sub.unitvalue,
	sub.previd 
FROM lcuvsubclass sub 
inner join lcuvspecificclass spc on sub.specificclassid = spc.objid 



[getStrippings]
SELECT 
	st.objid,
	st.landrysettingid,
	p.objid as classification_objid,
	st.striplevel,
	st.rate,
	st.previd 
FROM lcuvstripping st 
inner join lcuv l on st.lcuvid = l.objid
inner join propertyclassification p on l.classcode = p.propertycode



[getAdjustmentTypes]
SELECT 
	objid,
	landrysettingid,
	adjustmentcode as code,
	adjustmentname as name,
	expression as expr,
	appliedto,
	previd,
	0 as idx,
	classifications 
FROM landadjustment





#============= BLDG ==============================
[getBldgSettings]
SELECT 
	objid,
	'DRAFT' as state,
	ry,
	predominant,
	depreciatecoreanditemseparately,
	0 as computedepreciationbasedonschedule,
	straightdepreciation,
	calcbldgagebasedondtoccupied,
	$P{appliedto} as appliedto, 
	previd 
FROM bldgrysetting


[getBldgAssessLevels]
SELECT 
	objid,
	bldgrysettingid,
	code,
	name,
	fixrate,
	rate,
	previd,
	ranges
FROM bldgassesslevel


[getBldgTypes]
SELECT 
	bt.objid,
	bt.bldgrysettingid,
	bt.code,
	bt.name,
	bt.basevaluetype,
	bt.residualrate,
	bt.previd,
	depreciations,
	multistoreyadjustments
FROM bldgtype bt 


[getBldgKindBuccs]
SELECT 
	bucc.objid,
	bucc.bldgrysettingid,
	bucc.bldgtypeid,
	bucc.bldgkindid as bldgkind_objid,
	bucc.bldgkindcode as bldgkind_code,
	bucc.bldgkindname as bldgkind_name,
	bucc.basevaluetype,
	bucc.basevalue,
	bucc.minbasevalue,
	bucc.maxbasevalue,
	bucc.gapvalue,
	bucc.minarea,
	bucc.maxarea,
	bucc.bldgclass,
	bucc.previd 
FROM bldgkindbucc bucc 
inner join bldgrysetting bs on bucc.bldgrysettingid = bs.objid 


[getBldgAdditionalItems]
SELECT 
	objid,
	bldgrysettingid,
	'additionalitem' as type, 
	code,
	name,
	unit,
	expr,
	previd
FROM bldgadditionalitem 





#============= MACH ==============================
[getMachSettings]
SELECT 
	objid,
	'DRAFT' as state,
	ry,
	previd,
	$P{appliedto} as appliedto, 
	0.0 as residualrate 
FROM machrysetting


[getMachAssessLevels]
SELECT 
	objid,
	machrysettingid,
	code, 
	name, 
	fixrate,
	rate,
	previd,
	ranges
FROM machassesslevel


[getMachForexes]
SELECT 
	objid,
	machrysettingid,
	iyear as year,
	forex,
	previd 
FROM machforex




#============= PLANT/TREE ==============================
[getPlantTreeSettings]
SELECT 
	objid,
	'DRAFT' as state,
	ry,
	applyagriadjustment,
	$P{appliedto} as appliedto, 
	previd, 
	assesslevels 
FROM planttreerysetting


[getPlantTreeUnitValues]
SELECT 
	objid,
	planttreerysettingid,
	planttreeid as planttree_objid,
	code,
	name,
	unitvalue,
	previd 
FROM planttreeunitvalue



#============= MISC  ==============================
[getMiscSettings]
SELECT 
	objid,
	'DRAFT' as state,
	ry,
	previd,
	$P{appliedto} as appliedto
FROM miscrysetting


[getMiscAssessLevels]
SELECT 
	objid,
	miscrysettingid,
	code,
	name,
	fixrate,
	rate,
	previd,
	ranges
FROM miscassesslevel


[getMiscItemUnitValues]
select 
	objid,
	miscrysettingid,
	miscitemid as miscitem_objid,
	expr,
	previd
from miscitemvalue 



#============= RYSETTING  ==============================
[findLguById]
select * from lgu where objid = $P{objid}


[getLguRySettings]
select objid, objid as rysettingid, 'land' as settingtype from landrysetting
union
select objid, objid as rysettingid, 'bldg' as settingtype from bldgrysetting
union 
select objid, objid as rysettingid, 'mach' as settingtype from machrysetting
union 
select objid, objid as rysettingid, 'planttree' as settingtype from planttreerysetting
union 
select objid, objid as rysettingid, 'misc' as settingtype from miscrysetting
