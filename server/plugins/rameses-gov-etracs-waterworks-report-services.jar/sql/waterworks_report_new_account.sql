[getReport]
select 
	a.acctno, a.acctname, a.address_text, a.classificationid, a.dtstarted, 
	wm.serialno as meter_serialno, wm.brand as meter_brand, wms.title as meter_size, 
	so.barangay_objid, so.barangay_name, z.code as block_code, z.description as block_description, 
	z.sectorid as sector_objid, z.schedule_objid, 
	case 
		when wm.state = 'DISCONNECTED' then wm.state else a.state 
	end as state, 
	case 
		when a.classificationid='BULK' then a.classificationid 
		else substring(a.classificationid,1,3) 
	end as classcode 
from waterworks_account a 
	inner join waterworks_stubout_node sn on sn.objid = a.stuboutnodeid 
	inner join waterworks_stubout so on so.objid = sn.stuboutid 
	inner join waterworks_zone z on z.objid = so.zoneid 
	left join waterworks_meter wm on wm.objid = a.meterid 
	left join waterworks_metersize wms on wms.objid = wm.sizeid 
where a.dtstarted >= $P{startdate} 
	and a.dtstarted <  $P{enddate} 
	${filters} 
order by a.dtstarted, a.acctname 
