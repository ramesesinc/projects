[getList]
select * from mtopunittype where code like $P{searchtext} 

[approve]
update mtopunittype set state='APPROVED' where objid=$P{objid} 