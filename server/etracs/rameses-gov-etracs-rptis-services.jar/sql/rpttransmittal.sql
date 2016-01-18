[getList]
select * 
from rpttransmittal
where objid in (
	select objid from rpttransmittal where txnno = $P{searchtext}
	union
	select objid from rpttransmittal where lgu_name LIKE $P{searchtext}
)
order by txnno 


[getItems]
SELECT * FROM rpttransmittal_item 
WHERE transmittalid = $P{objid}
ORDER BY tdno, fullpin 

[getData]
SELECT * FROM rpttransmittal_item_data WHERE parentid = $P{objid}


[deleteAllItems]
delete from rpttransmittal_item where transmittalid = $P{objid}

[deleteAllData]
delete from rpttransmittal_item_data where transmittalid = $P{objid}

[deleteItemData]
delete from rpttransmittal_item_data where parentid = $P{objid}

[findActiveTransmittalItem]
select t.objid, t.txnno 
from rpttransmittal t 
	inner join rpttransmittal_item ti on t.objid = ti.transmittalid
where t.state <> 'APPROVED'
  and ti.refid = $P{refid}


[findDataCount]
select count(*) as icount from rpttransmittal_item_data where transmittalid = $P{objid}


[findLastFaasTask]
select * from faas_task where refid = $P{objid} order by startdate desc 
