[getList]
SELECT * 
FROM faas_txntype 
WHERE (objid LIKE $P{seachtext} OR name LIKE $P{searchtext})
ORDER BY objid 


[updateData]
update faas_txntype set 
	displaycode = $P{displaycode} 
where objid = $P{objid}

[deleteAttributes]
delete from faas_txntype_attribute where txntype_objid = $P{objid}

[insertAttribute]	
insert into faas_txntype_attribute (
	txntype_objid, attribute, idx
)
values 
($P{objid}, $P{attribute}, $P{idx})



[getAttributes]
select *, txntype_objid as objid from faas_txntype_attribute where txntype_objid = $P{objid} order by idx 

[insertAttributeType]
insert into faas_txntype_attribute_type
	(attribute)
values($P{attribute})

[findAttributeType]
select * from faas_txntype_attribute_type where attribute = $P{attribute}