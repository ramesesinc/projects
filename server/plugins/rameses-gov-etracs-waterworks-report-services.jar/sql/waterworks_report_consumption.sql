[getReport]
select 
	barangay_objid, barangay_name, 
	sum(res_range1) as res_range1, sum(res_range2) as res_range2, sum(res_range3) as res_range3, 
	sum(res_range4) as res_range4, sum(res_range5) as res_range5, sum(res_range6) as res_range6,
	sum(com_range1) as com_range1, sum(com_range2) as com_range2, sum(com_range3) as com_range3, 
	sum(com_range4) as com_range4, sum(com_range5) as com_range5, sum(com_range6) as com_range6,
	sum(ind_range1) as ind_range1, sum(ind_range2) as ind_range2, sum(ind_range3) as ind_range3, 
	sum(ind_range4) as ind_range4, sum(ind_range5) as ind_range5, sum(ind_range6) as ind_range6,
	sum(bulk_range1) as bulk_range1, sum(bulk_range2) as bulk_range2, sum(bulk_range3) as bulk_range3, 
	sum(bulk_range4) as bulk_range4, sum(bulk_range5) as bulk_range5, sum(bulk_range6) as bulk_range6,
	sum(gov_range1) as gov_range1, sum(gov_range2) as gov_range2, sum(gov_range3) as gov_range3, 
	sum(gov_range4) as gov_range4, sum(gov_range5) as gov_range5, sum(gov_range6) as gov_range6
from ( 
	select 
		so.barangay_objid, so.barangay_name, 
		case when a.classificationid='RESIDENTIAL' and c.volume between 00 and 10 then 1 else 0 end as res_range1,  
		case when a.classificationid='RESIDENTIAL' and c.volume between 11 and 20 then 1 else 0 end as res_range2, 
		case when a.classificationid='RESIDENTIAL' and c.volume between 21 and 30 then 1 else 0 end as res_range3, 
		case when a.classificationid='RESIDENTIAL' and c.volume between 31 and 40 then 1 else 0 end as res_range4, 
		case when a.classificationid='RESIDENTIAL' and c.volume between 41 and 50 then 1 else 0 end as res_range5, 
		case when a.classificationid='RESIDENTIAL' and c.volume > 50 then 1 else 0 end as res_range6, 
		case when a.classificationid='COMMERCIAL' and c.volume between 00 and 10 then 1 else 0 end as com_range1,  
		case when a.classificationid='COMMERCIAL' and c.volume between 11 and 20 then 1 else 0 end as com_range2, 
		case when a.classificationid='COMMERCIAL' and c.volume between 21 and 30 then 1 else 0 end as com_range3, 
		case when a.classificationid='COMMERCIAL' and c.volume between 31 and 40 then 1 else 0 end as com_range4, 
		case when a.classificationid='COMMERCIAL' and c.volume between 41 and 50 then 1 else 0 end as com_range5, 
		case when a.classificationid='COMMERCIAL' and c.volume > 50 then 1 else 0 end as com_range6, 
		case when a.classificationid='INDUSTRIAL' and c.volume between 00 and 10 then 1 else 0 end as ind_range1,  
		case when a.classificationid='INDUSTRIAL' and c.volume between 11 and 20 then 1 else 0 end as ind_range2, 
		case when a.classificationid='INDUSTRIAL' and c.volume between 21 and 30 then 1 else 0 end as ind_range3, 
		case when a.classificationid='INDUSTRIAL' and c.volume between 31 and 40 then 1 else 0 end as ind_range4, 
		case when a.classificationid='INDUSTRIAL' and c.volume between 41 and 50 then 1 else 0 end as ind_range5, 
		case when a.classificationid='INDUSTRIAL' and c.volume > 50 then 1 else 0 end as ind_range6, 
		case when a.classificationid='BULK' and c.volume between 00 and 10 then 1 else 0 end as bulk_range1,  
		case when a.classificationid='BULK' and c.volume between 11 and 20 then 1 else 0 end as bulk_range2, 
		case when a.classificationid='BULK' and c.volume between 21 and 30 then 1 else 0 end as bulk_range3, 
		case when a.classificationid='BULK' and c.volume between 31 and 40 then 1 else 0 end as bulk_range4, 
		case when a.classificationid='BULK' and c.volume between 41 and 50 then 1 else 0 end as bulk_range5, 
		case when a.classificationid='BULK' and c.volume > 50 then 1 else 0 end as bulk_range6,  
		case when a.classificationid='GOVERNMENT' and c.volume between 00 and 10 then 1 else 0 end as gov_range1, 
		case when a.classificationid='GOVERNMENT' and c.volume between 11 and 20 then 1 else 0 end as gov_range2, 
		case when a.classificationid='GOVERNMENT' and c.volume between 21 and 30 then 1 else 0 end as gov_range3, 
		case when a.classificationid='GOVERNMENT' and c.volume between 31 and 40 then 1 else 0 end as gov_range4, 
		case when a.classificationid='GOVERNMENT' and c.volume between 41 and 50 then 1 else 0 end as gov_range5, 
		case when a.classificationid='GOVERNMENT' and c.volume > 50 then 1 else 0 end as gov_range6 
	from waterworks_billing_schedule bs 
		inner join waterworks_consumption c on (c.scheduleid = bs.objid and c.state='POSTED' and c.hold=0)
		inner join waterworks_account a on a.objid = c.acctid 
		inner join waterworks_stubout_node sn on sn.objid = a.stuboutnodeid  
		inner join waterworks_stubout so on so.objid = sn.stuboutid 
		inner join waterworks_zone z on z.objid = so.zoneid 
	where bs.year = $P{year} ${filters} 
)t1 
group by barangay_objid, barangay_name 
order by barangay_name
