alter table itax_kolambugan.t_classification add classid varchar(50);

update itax_kolambugan.t_classification c, etracs254_ldn.propertyclassification pc set 
	c.classid = pc.objid 
where c.code = pc.code and c.classid is null;


update itax_kolambugan.t_classification c set 
	c.classid = (select objid from etracs254_ldn.propertyclassification where code = 'A')
where c.code = 'AR';



-- AGRI SETTINGS
insert into etracs254_ldn.landrysetting(
	objid,
	state,
	ry,
	appliedto,
	previd
)
select 
	'LAND-05707-2013-A' as objid,
	'DRAFT' as state,
	2013 as ry,
	'KOLAMBUGAN' as appliedto,
	null as previd;



alter table itax_kolambugan.t_assessmentlevel add classid varchar(50);

update itax_kolambugan.t_assessmentlevel a set 
	a.classid = (select objid from etracs254_ldn.propertyclassification where code = substring(a.actualuse_ct,2,1));


insert into etracs254_ldn.landassesslevel(
	objid,
	landrysettingid,
	classification_objid,
	code,
	name,
	fixrate,
	rate,
	previd
)
select
	concat('LAND-05707-2013-', a.assessmentlevel_id) as objid,
	'LAND-05707-2013-A' as landrysettingid,
	a.classid as classification_objid,
	a.actualuse_ct as code,
	a.classid as name,
	1 as fixrate,
	a.assessmentlevel * 100 as  rate,
	null as previd
from itax_kolambugan.t_assessmentlevel a
where a.propertykind_ct = 'L' 
  and a.yearstart = 2013
  and a.classid = 'AGRICULTURAL';


insert into etracs254_ldn.lcuvspecificclass(
	objid,
	landrysettingid,
	classification_objid,
	code,
	name,
	areatype,
	previd
)
select distinct 
	concat( 'LAND-05707-2013-', s.code) as objid,
	'LAND-05707-2013-A' as landrysettingid,
	c.classid as  classification_objid,
	s.code,
	s.description as name,
	case when s.classcode_ct = 'A' then 'HA' else 'SQM' end as areatype,
	null as previd
from itax_kolambugan.t_sfmvland l 
	inner join itax_kolambugan.t_subclassification s on l.subclass_ct = s.code 
	inner join itax_kolambugan.t_classification c on s.classcode_ct = c.code
where l.yearstart = 2013
 and s.classcode_ct = 'A';


insert into etracs254_ldn.lcuvsubclass(
	objid,
	specificclass_objid,
	landrysettingid,
	code,
	name,
	unitvalue,
	previd
)
select
	concat( 'LAND-05707-2013-', l.sfmvland_id),
	concat( 'LAND-05707-2013-', s.code) as specificclass_objid,
	'LAND-05707-2013-A' as landrysettingid,
	l.sfmvland_id as code,
	cl.description as  name,
	l.landvalue * 10000 as  unitvalue,
	null as previd
from itax_kolambugan.t_sfmvland l 
	inner join itax_kolambugan.t_subclassification s on l.subclass_ct = s.code 
	inner join itax_kolambugan.t_classification c on s.classcode_ct = c.code
	inner join itax_kolambugan.t_classlevel cl on l.classlevel_ct = cl.code
where l.yearstart = 2013
 and s.classcode_ct = 'A'
 and l.landvalue * 10000 < 500000;




-- NON-AGRI SETTINGS
insert into etracs254_ldn.landrysetting(
	objid,
	state,
	ry,
	appliedto,
	previd
)
select 
	'LAND-05707-2013' as objid,
	'DRAFT' as state,
	2013 as ry,
	'KOLAMBUGAN' as appliedto,
	null as previd;




insert into etracs254_ldn.lcuvspecificclass(
	objid,
	landrysettingid,
	classification_objid,
	code,
	name,
	areatype,
	previd
)
select distinct 
	concat( 'LAND-05707-2013-', s.code) as objid,
	'LAND-05707-2013' as landrysettingid,
	c.classid as  classification_objid,
	s.code,
	s.description as name,
	case when s.classcode_ct = 'A' then 'HA' else 'SQM' end as areatype,
	null as previd
from itax_kolambugan.t_sfmvland l 
	inner join itax_kolambugan.t_subclassification s on l.subclass_ct = s.code 
	inner join itax_kolambugan.t_classification c on s.classcode_ct = c.code
where l.yearstart = 2013
 and s.classcode_ct <> 'A';



insert into etracs254_ldn.lcuvsubclass(
	objid,
	specificclass_objid,
	landrysettingid,
	code,
	name,
	unitvalue,
	previd
)
select
	concat( 'LAND-05707-2013-', l.sfmvland_id),
	concat( 'LAND-05707-2013-', s.code) as specificclass_objid,
	'LAND-05707-2013' as landrysettingid,
	l.sfmvland_id as code,
	cl.description as  name,
	l.landvalue as  unitvalue,
	null as previd
from itax_kolambugan.t_sfmvland l 
	inner join itax_kolambugan.t_subclassification s on l.subclass_ct = s.code 
	inner join itax_kolambugan.t_classification c on s.classcode_ct = c.code
	inner join itax_kolambugan.t_classlevel cl on l.classlevel_ct = cl.code
where l.yearstart = 2013
 and s.classcode_ct <> 'A';



insert into etracs254_ldn.landadjustmenttype(
	objid,
	landrysettingid,
	code,
	name,
	expr,
	appliedto,
	previd,
	idx
)

select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as objid,
	'LAND-05707-2013-A' as landrysettingid,
	la.sfmvlandadjustment_id as code,
	a.description as name,
	concat('SYS_BASE_MARKET_VALUE * (', ifnull(adjvalue,0.0),')' )  as expr,
	'AGRICULTURAL' as appliedto,
	null as previd,
	la.adjustment_ct as idx
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code = 1;  -- type of roads



insert into etracs254_ldn.landadjustmenttype_classification(
	landadjustmenttypeid,
	classification_objid,
	landrysettingid
)
select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as landadjustmenttypeid,
	(select objid from etracs254_ldn.propertyclassification where code = 'a') as classification_objid,
	'LAND-05707-2013-A' as landrysettingid
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code = 1;  -- type of roads




insert into etracs254_ldn.landadjustmenttype(
	objid,
	landrysettingid,
	code,
	name,
	expr,
	appliedto,
	previd,
	idx
)

select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as objid,
	'LAND-05707-2013-A' as landrysettingid,
	la.sfmvlandadjustment_id as code,
	concat('DISTANCE TO WEATHER ROAD (', a.description, ')') as name,
	concat('SYS_BASE_MARKET_VALUE * (', ifnull(adjvalue,0.0),')' )  as expr,
	'AGRICULTURAL' as appliedto,
	null as previd,
	la.adjustment_ct as idx
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code = 2;  -- all weather road



insert into etracs254_ldn.landadjustmenttype_classification(
	landadjustmenttypeid,
	classification_objid,
	landrysettingid
)
select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as landadjustmenttypeid,
	(select objid from etracs254_ldn.propertyclassification where code = 'a') as classification_objid,
	'LAND-05707-2013-A' as landrysettingid
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code = 2;  -- all weather road





insert into etracs254_ldn.landadjustmenttype(
	objid,
	landrysettingid,
	code,
	name,
	expr,
	appliedto,
	previd,
	idx
)
select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as objid,
	'LAND-05707-2013-A' as landrysettingid,
	la.sfmvlandadjustment_id as code,
	concat('DISTANCE TO TRADING CENTER (', a.description, ')') as name,
	concat('SYS_BASE_MARKET_VALUE * (', ifnull(adjvalue,0.0),')' )  as expr,
	'AGRICULTURAL' as appliedto,
	null as previd,
	la.adjustment_ct as idx
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code = 3;  -- trading center



insert into etracs254_ldn.landadjustmenttype_classification(
	landadjustmenttypeid,
	classification_objid,
	landrysettingid
)
select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as landadjustmenttypeid,
	(select objid from etracs254_ldn.propertyclassification where code = 'a') as classification_objid,
	'LAND-05707-2013-A' as landrysettingid
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code = 3;  -- trading center




insert into etracs254_ldn.landadjustmenttype(
	objid,
	landrysettingid,
	code,
	name,
	expr,
	appliedto,
	previd,
	idx
)
select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as objid,
	'LAND-05707-2013-A' as landrysettingid,
	la.sfmvlandadjustment_id as code,
	concat('DISTANCE TO TRADING CENTER (', a.description, ')') as name,
	concat('SYS_BASE_MARKET_VALUE * (', ifnull(adjvalue,0.0),')' )  as expr,
	'RESIDENTIAL, COMMERCIAL' as appliedto,
	null as previd,
	la.adjustment_ct as idx
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code in ( 4, 5);  -- stripping / ci



insert into etracs254_ldn.landadjustmenttype_classification(
	landadjustmenttypeid,
	classification_objid,
	landrysettingid
)
select
	concat('LAND-05707-2013-A-', la.sfmvlandadjustment_id) as landadjustmenttypeid,
	(select objid from etracs254_ldn.propertyclassification where code in ('r', 'c')) as classification_objid,
	'LAND-05707-2013-A' as landrysettingid
from itax_kolambugan.t_sfmvlandadjustment la 
	inner join itax_kolambugan.t_adjustmenttype t on la.adjustmenttype_ct = t.code
	inner join itax_kolambugan.t_adjustment a on la.adjustment_ct = a.code 
where la.yearstart = 2013
  and t.code in ( 4, 5);  -- stripping / ci









-- BLDG SETTINGS
insert into etracs254_ldn.bldgrysetting(
	objid,
	state,
	ry,
	appliedto,
	previd
)
select 
	'BLDG-05707-2013' as objid,
	'DRAFT' as state,
	2013 as ry,
	'KOLAMBUGAN' as appliedto,
	null as previd;



insert into etracs254_ldn.bldgassesslevel(
	objid,
	bldgrysettingid,
	classification_objid,
	code,
	name,
	fixrate,
	rate,
	previd
)
select DISTINCT
	concat('BLDG-05707-2013-', a.actualuse_ct) as objid,
	'BLDG-05707-2013' as bldgrysettingid,
	a.classid as classification_objid,
	a.actualuse_ct as code,
	a.classid as name,
	0 as fixrate,
	0 as  rate,
	null as previd
from itax_kolambugan.t_assessmentlevel a
where a.propertykind_ct = 'B' 
  and a.yearstart = 2013
order by a.actualuse_ct;


insert into etracs254_ldn.bldgassesslevelrange(
	objid,
	bldgassesslevelid,
	bldgrysettingid,
	mvfrom,
	mvto,
	rate
)
select 
	a.assessmentlevel_id as objid,
	concat('BLDG-05707-2013-', a.actualuse_ct) as bldgassesslevelid,
	'BLDG-05707-2013' as bldgrysettingid,
	a.valuefrom as  mvfrom,
	a.valueto as  mvto,
	a.assessmentlevel * 100 as rate
from itax_kolambugan.t_assessmentlevel a
where a.propertykind_ct = 'B' 
  and a.yearstart = 2013
order by a.actualuse_ct;	


insert into etracs254_ldn.bldgkind(
	objid,
	state,
	code,
	name
)
select 
	code as objid,
	'APPROVED' as state,
	code,
	description as name
from itax_kolambugan.t_bldgkind 
where yearstart = 2013
order by description;



insert into etracs254_ldn.bldgtype(
	objid,
	bldgrysettingid,
	code,
	name,
	basevaluetype,
	residualrate,
	previd
)
select distinct 
	concat('BLDG-05707-2013-BT', bst.code,'-',replace(bc.description,'CLASS ', '')) as objid,
	'BLDG-05707-2013' as bldgrysettingid,
	concat(replace(bst.description, 'TYPE ', ''),'-', replace(bc.description,'CLASS ', '')) as code,
	concat(bst.description,'-', replace(bc.description,'CLASS ', ''), ' (', bst.longdescription, ')') as name,
	'fix' as basevaluetype,
	0 as residualrate,
	null as previd
from itax_kolambugan.t_sfmvbldgcost c
	inner join itax_kolambugan.t_bldgstructuretype bst on c.structuretype_ct = bst.code 
	inner  join itax_kolambugan.t_bldgclass bc on c.bldgclass_ct = bc.code 
where c.yearstart = 2013
order by bst.code ;




insert into etracs254_ldn.bldgkindbucc
(
	objid,
	bldgrysettingid,
	bldgtypeid,
	bldgkind_objid,
	basevaluetype,
	basevalue,
	minbasevalue,
	maxbasevalue,
	gapvalue,
	minarea,
	maxarea,
	bldgclass,
	previd
)
select
	c.sfmvbldgcost_id as objid,
	'BLDG-05707-2013' as bldgrysettingid,
	concat('BLDG-05707-2013-BT', bst.code,'-',replace(bc.description,'CLASS ', '')) as bldgtypeid,
	c.bldgkind_ct as bldgkind_objid,
	'fix' as basevaluetype,
	c.minvalue as basevalue,
	0 as minbasevalue,
	0 as maxbasevalue,
	0 as gapvalue,
	0 as minarea,
	0 as maxarea,
	bc.description as bldgclass,
	null as previd
from itax_kolambugan.t_sfmvbldgcost c
	inner join itax_kolambugan.t_bldgstructuretype bst on c.structuretype_ct = bst.code 
	inner join itax_kolambugan.t_bldgkind bk on c.bldgkind_ct = bk.code
	inner  join itax_kolambugan.t_bldgclass bc on c.bldgclass_ct = bc.code 
where c.yearstart = 2013 
and bk.yearstart = 2013;





-- MACH SETTINGS
insert into etracs254_ldn.machrysetting(
	objid,
	state,
	ry,
	appliedto,
	residualrate,
	previd
)
select 
	'MACH-05707-2013' as objid,
	'DRAFT' as state,
	2013 as ry,
	'KOLAMBUGAN' as appliedto,
	0 as residualrate,
	null as previd;




insert into etracs254_ldn.machassesslevel(
	objid,
	machrysettingid,
	classification_objid,
	code,
	name,
	fixrate,
	rate,
	previd
)
select DISTINCT
	concat('MACH-05707-2013-', a.actualuse_ct) as objid,
	'MACH-05707-2013' as machrysettingid,
	a.classid as classification_objid,
	a.actualuse_ct as code,
	a.classid as name,
	1 as fixrate,
	a.assessmentlevel * 100 as  rate,
	null as previd
from itax_kolambugan.t_assessmentlevel a
where a.propertykind_ct = 'M' 
  and a.yearstart = 2013
order by a.actualuse_ct;


