[getList]
SELECT 
    ${columns}
FROM rptledger rl 
    INNER JOIN entity e ON rl.taxpayer_objid = e.objid 
    INNER JOIN barangay b ON rl.barangayid = b.objid 
    LEFT JOIN faas f on rl.faasid = f.objid 
WHERE 1=1
${fixfilters}
${filters}
${orderby}


[closePaidAvDifference]
update rptledger_avdifference set 
	paid = 1
where not exists(
	select * from rptledger_item 
	where parentid = rptledger_avdifference.parent_objid
	and year = rptledger_avdifference.year 
	and taxdifference = 1 
)


[findLastPayment]
select 
	cro.year, 
	sum(cro.basic) as basic,
	sum(cro.sef) as sef 
from cashreceiptitem_rpt_online cro 
where cro.rptledgerid = $P{objid}
and cro.year = $P{year}
group by year 

