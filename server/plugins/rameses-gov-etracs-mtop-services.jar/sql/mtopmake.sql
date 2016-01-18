[getList]
select * from mtopmake where code like $P{searchtext} 

[approve]
update mtopmake set state='APPROVED' where objid=$P{objid} 

[deleteModels]
delete from mtopmodel where makeid = $P{objid}  

[getModels]
select * from mtopmodel where  makeid = $P{objid}  