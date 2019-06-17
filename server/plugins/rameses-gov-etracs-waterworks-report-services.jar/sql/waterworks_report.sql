[getBarangays]
select distinct 
	ws.barangay_objid as objid, ws.barangay_name as name 
from waterworks_stubout ws 
	inner join waterworks_zone wz on wz.objid = ws.zoneid 
where ws.barangay_objid is not null 
order by ws.barangay_name 

[getZones]
select 
	wz.objid, wz.code, wz.description, 
	ws.barangay_objid, ws.barangay_name 
from waterworks_stubout ws 
	inner join waterworks_zone wz on wz.objid = ws.zoneid 
where ws.barangay_objid is not null 
order by wz.code 

[getReportSummaryOfBilling]
select 
	barangay_objid, zone_objid, barangay_name, zone_code, 
	sum(res_metered) as res_metered, sum(res_meteredvol) as res_meteredvol, sum(res_meteredamt) as res_meteredamt, 
	sum(res_defective) as res_defective, sum(res_defectivevol) as res_defectivevol, sum(res_defectiveamt) as res_defectiveamt,
	sum(res_unmetered) as res_unmetered, sum(res_unmeteredvol) as res_unmeteredvol, sum(res_unmeteredamt) as res_unmeteredamt,
	sum(com_metered) as com_metered, sum(com_meteredvol) as com_meteredvol, sum(com_meteredamt) as com_meteredamt, 
	sum(com_defective) as com_defective, sum(com_defectivevol) as com_defectivevol, sum(com_defectiveamt) as com_defectiveamt,
	sum(com_unmetered) as com_unmetered, sum(com_unmeteredvol) as com_unmeteredvol, sum(com_unmeteredamt) as com_unmeteredamt,
	sum(ind_metered) as ind_metered, sum(ind_meteredvol) as ind_meteredvol, sum(ind_meteredamt) as ind_meteredamt, 
	sum(ind_defective) as ind_defective, sum(ind_defectivevol) as ind_defectivevol, sum(ind_defectiveamt) as ind_defectiveamt,
	sum(ind_unmetered) as ind_unmetered, sum(ind_unmeteredvol) as ind_unmeteredvol, sum(ind_unmeteredamt) as ind_unmeteredamt,
	sum(gov_metered) as gov_metered, sum(gov_meteredvol) as gov_meteredvol, sum(gov_meteredamt) as gov_meteredamt, 
	sum(gov_defective) as gov_defective, sum(gov_defectivevol) as gov_defectivevol, sum(gov_defectiveamt) as gov_defectiveamt,
	sum(gov_unmetered) as gov_unmetered, sum(gov_unmeteredvol) as gov_unmeteredvol, sum(gov_unmeteredamt) as gov_unmeteredamt,
	sum(bulk_metered) as bulk_metered, sum(bulk_meteredvol) as bulk_meteredvol, sum(bulk_meteredamt) as bulk_meteredamt, 
	sum(bulk_defective) as bulk_defective, sum(bulk_defectivevol) as bulk_defectivevol, sum(bulk_defectiveamt) as bulk_defectiveamt,
	sum(bulk_unmetered) as bulk_unmetered, sum(bulk_unmeteredvol) as bulk_unmeteredvol, sum(bulk_unmeteredamt) as bulk_unmeteredamt 
from vw_report_billing_summary v 
where ${filters} 
group by barangay_objid, barangay_name, zone_objid, zone_code 
order by barangay_name, zone_code 


[getReportMeterConnectionStatus]
select 
	barangay_objid, barangay_name, 
	sum(res_metered) as res_metered, sum(res_unmetered) as res_unmetered, sum(res_defective) as res_defective, 
	sum(com_metered) as com_metered, sum(com_unmetered) as com_unmetered, sum(com_defective) as com_defective, 
	sum(ind_metered) as ind_metered, sum(ind_unmetered) as ind_unmetered, sum(ind_defective) as ind_defective, 
	sum(gov_metered) as gov_metered, sum(gov_unmetered) as gov_unmetered, sum(gov_defective) as gov_defective, 
	sum(bulk_metered) as bulk_metered, sum(bulk_unmetered) as bulk_unmetered, sum(bulk_defective) as bulk_defective 
from ( 
	select 
		wst.barangay_objid, wst.barangay_name, 
		case when wa.classificationid = 'RESIDENTIAL' and wm.state = 'ACTIVE' then 1 else 0 end as res_metered, 
		case when wa.classificationid = 'RESIDENTIAL' and wm.state = 'DEFECTIVE' then 1 else 0 end as res_defective, 
		case when wa.classificationid = 'RESIDENTIAL' and wm.objid is null then 1 else 0 end as res_unmetered, 
		case when wa.classificationid = 'COMMERCIAL' and wm.state = 'ACTIVE' then 1 else 0 end as com_metered, 
		case when wa.classificationid = 'COMMERCIAL' and wm.state = 'DEFECTIVE' then 1 else 0 end as com_defective, 
		case when wa.classificationid = 'COMMERCIAL' and wm.objid is null then 1 else 0 end as com_unmetered, 
		case when wa.classificationid = 'INDUSTRIAL' and wm.state = 'ACTIVE' then 1 else 0 end as ind_metered, 
		case when wa.classificationid = 'INDUSTRIAL' and wm.state = 'DEFECTIVE' then 1 else 0 end as ind_defective, 
		case when wa.classificationid = 'INDUSTRIAL' and wm.objid is null then 1 else 0 end as ind_unmetered, 
		case when wa.classificationid = 'GOVERNMENT' and wm.state = 'ACTIVE' then 1 else 0 end as gov_metered, 
		case when wa.classificationid = 'GOVERNMENT' and wm.state = 'DEFECTIVE' then 1 else 0 end as gov_defective, 
		case when wa.classificationid = 'GOVERNMENT' and wm.objid is null then 1 else 0 end as gov_unmetered, 
		case when wa.classificationid = 'BULK' and wm.state = 'ACTIVE' then 1 else 0 end as bulk_metered, 
		case when wa.classificationid = 'BULK' and wm.state = 'DEFECTIVE' then 1 else 0 end as bulk_defective, 
		case when wa.classificationid = 'BULK' and wm.objid is null then 1 else 0 end as bulk_unmetered 
	from waterworks_account wa 
		inner join waterworks_stubout_node wsn on wsn.objid = wa.stuboutnodeid 
		inner join waterworks_stubout wst on wst.objid = wsn.stuboutid 
		left join waterworks_meter wm on wm.objid = wa.meterid 
	where wa.state = 'ACTIVE' 
		and wst.barangay_objid is not null 
) v  
group by barangay_objid, barangay_name  
order by barangay_name 
