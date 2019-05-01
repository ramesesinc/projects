drop view if exists vw_report_billing_summary
;
create view vw_report_billing_summary as 
select 
	wbs.year, wbs.month, ws.barangay_objid, ws.barangay_name, 
	wz.objid as zone_objid, wz.code as zone_code, ws.description as zone_title, 
	case when cl.objid = 'RESIDENTIAL' and wm.state = 'ACTIVE' then 1 else 0 end as res_metered, 
	case when cl.objid = 'RESIDENTIAL' and wm.state = 'ACTIVE' then wc.volume else 0 end as res_meteredvol, 
	case when cl.objid = 'RESIDENTIAL' and wm.state = 'ACTIVE' then wc.amount else 0 end as res_meteredamt, 
	case when cl.objid = 'RESIDENTIAL' and wm.state = 'DEFECTIVE' then 1 else 0 end as res_defective, 
	case when cl.objid = 'RESIDENTIAL' and wm.state = 'DEFECTIVE' then wc.volume else 0 end as res_defectivevol, 
	case when cl.objid = 'RESIDENTIAL' and wm.state = 'DEFECTIVE' then wc.amount else 0 end as res_defectiveamt, 
	case when cl.objid = 'RESIDENTIAL' and wm.objid is null then 1 else 0 end as res_unmetered, 
	case when cl.objid = 'RESIDENTIAL' and wm.objid is null then wc.volume else 0 end as res_unmeteredvol, 
	case when cl.objid = 'RESIDENTIAL' and wm.objid is null then wc.amount else 0 end as res_unmeteredamt, 
	case when cl.objid = 'COMMERCIAL' and wm.state = 'ACTIVE' then 1 else 0 end as com_metered, 
	case when cl.objid = 'COMMERCIAL' and wm.state = 'ACTIVE' then wc.volume else 0 end as com_meteredvol, 
	case when cl.objid = 'COMMERCIAL' and wm.state = 'ACTIVE' then wc.amount else 0 end as com_meteredamt, 
	case when cl.objid = 'COMMERCIAL' and wm.state = 'DEFECTIVE' then 1 else 0 end as com_defective, 
	case when cl.objid = 'COMMERCIAL' and wm.state = 'DEFECTIVE' then wc.volume else 0 end as com_defectivevol, 
	case when cl.objid = 'COMMERCIAL' and wm.state = 'DEFECTIVE' then wc.amount else 0 end as com_defectiveamt, 
	case when cl.objid = 'COMMERCIAL' and wm.objid is null then 1 else 0 end as com_unmetered, 
	case when cl.objid = 'COMMERCIAL' and wm.objid is null then wc.volume else 0 end as com_unmeteredvol, 
	case when cl.objid = 'COMMERCIAL' and wm.objid is null then wc.amount else 0 end as com_unmeteredamt, 
	case when cl.objid = 'INDUSTRIAL' and wm.state = 'ACTIVE' then 1 else 0 end as ind_metered, 
	case when cl.objid = 'INDUSTRIAL' and wm.state = 'ACTIVE' then wc.volume else 0 end as ind_meteredvol, 
	case when cl.objid = 'INDUSTRIAL' and wm.state = 'ACTIVE' then wc.amount else 0 end as ind_meteredamt, 
	case when cl.objid = 'INDUSTRIAL' and wm.state = 'DEFECTIVE' then 1 else 0 end as ind_defective, 
	case when cl.objid = 'INDUSTRIAL' and wm.state = 'DEFECTIVE' then wc.volume else 0 end as ind_defectivevol, 
	case when cl.objid = 'INDUSTRIAL' and wm.state = 'DEFECTIVE' then wc.amount else 0 end as ind_defectiveamt, 
	case when cl.objid = 'INDUSTRIAL' and wm.objid is null then 1 else 0 end as ind_unmetered, 
	case when cl.objid = 'INDUSTRIAL' and wm.objid is null then wc.volume else 0 end as ind_unmeteredvol, 
	case when cl.objid = 'INDUSTRIAL' and wm.objid is null then wc.amount else 0 end as ind_unmeteredamt, 
	case when cl.objid = 'GOVERNMENT' and wm.state = 'ACTIVE' then 1 else 0 end as gov_metered, 
	case when cl.objid = 'GOVERNMENT' and wm.state = 'ACTIVE' then wc.volume else 0 end as gov_meteredvol, 
	case when cl.objid = 'GOVERNMENT' and wm.state = 'ACTIVE' then wc.amount else 0 end as gov_meteredamt, 
	case when cl.objid = 'GOVERNMENT' and wm.state = 'DEFECTIVE' then 1 else 0 end as gov_defective, 
	case when cl.objid = 'GOVERNMENT' and wm.state = 'DEFECTIVE' then wc.volume else 0 end as gov_defectivevol, 
	case when cl.objid = 'GOVERNMENT' and wm.state = 'DEFECTIVE' then wc.amount else 0 end as gov_defectiveamt, 
	case when cl.objid = 'GOVERNMENT' and wm.objid is null then 1 else 0 end as gov_unmetered, 
	case when cl.objid = 'GOVERNMENT' and wm.objid is null then wc.volume else 0 end as gov_unmeteredvol, 
	case when cl.objid = 'GOVERNMENT' and wm.objid is null then wc.amount else 0 end as gov_unmeteredamt, 
	case when cl.objid = 'BULK' and wm.state = 'ACTIVE' then 1 else 0 end as bulk_metered, 
	case when cl.objid = 'BULK' and wm.state = 'ACTIVE' then wc.volume else 0 end as bulk_meteredvol, 
	case when cl.objid = 'BULK' and wm.state = 'ACTIVE' then wc.amount else 0 end as bulk_meteredamt, 
	case when cl.objid = 'BULK' and wm.state = 'DEFECTIVE' then 1 else 0 end as bulk_defective, 
	case when cl.objid = 'BULK' and wm.state = 'DEFECTIVE' then wc.volume else 0 end as bulk_defectivevol, 
	case when cl.objid = 'BULK' and wm.state = 'DEFECTIVE' then wc.amount else 0 end as bulk_defectiveamt, 
	case when cl.objid = 'BULK' and wm.objid is null then 1 else 0 end as bulk_unmetered, 
	case when cl.objid = 'BULK' and wm.objid is null then wc.volume else 0 end as bulk_unmeteredvol, 
	case when cl.objid = 'BULK' and wm.objid is null then wc.amount else 0 end as bulk_unmeteredamt 
from waterworks_billing_schedule wbs 
	inner join waterworks_consumption wc on wc.scheduleid = wbs.objid 
	inner join waterworks_account wa on wa.objid = wc.acctid 
	inner join waterworks_stubout_node wsn on wsn.objid = wa.stuboutnodeid 
	inner join waterworks_stubout ws on ws.objid = wsn.stuboutid 
	inner join waterworks_zone wz on wz.objid = ws.zoneid 
	inner join waterworks_classification cl on cl.objid = wa.classificationid 
	left join waterworks_meter wm on wm.objid = wa.meterid 
; 

