[getAuctions]
select distinct 
	a.td_no as tdno,
	a.batch, 
	a.yr,
	a.period 
from tblauctions a 
left join etracs_auction_redeemed r on a.td_no = r.tdno 
where a.td_no = $P{tdno}
and r.objid is null 



[insertRedeemedAuction]
insert into etracs_auction_redeemed (
	objid,
	tdno,
	createdby_name,
	createdby_title,
	approvedby_name,
	approvedby_title,
	dtapproved,
	remarks
)
values (
	$P{objid},
	$P{tdno},
	$P{createdby_name},
	$P{createdby_title},
	$P{approvedby_name},
	$P{approvedby_title},
	$P{dtapproved},
	$P{remarks}
)