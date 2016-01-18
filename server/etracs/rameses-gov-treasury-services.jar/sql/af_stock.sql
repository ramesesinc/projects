[findAFInfo]
SELECT af.*, su.qty AS unitqty, su.unit 
FROM af 
INNER JOIN stockitem si ON si.objid=af.objid
INNER JOIN stockitem_unit su ON si.objid=su.itemid
WHERE af.objid=$P{objid} AND su.unit=$P{unit}

[getAFByType]
select af.*, siu.unit, siu.qty 
from af 
	inner join stockitem si on af.objid=si.objid 
	inner join stockitem_unit siu on si.objid=siu.itemid 
where af.formtype=$P{formtype}  

[findAF]
select af.*, siu.unit, siu.qty 
from af 
	inner join stockitem si on af.objid=si.objid 
	inner join stockitem_unit siu on si.objid=siu.itemid 
where af.objid=$P{objid} and siu.unit=$P{unit}

[getAF]
select af.*, siu.unit, siu.qty 
from af 
	inner join stockitem si on af.objid=si.objid 
	inner join stockitem_unit siu on si.objid=siu.itemid 
where af.objid=$P{objid} 
