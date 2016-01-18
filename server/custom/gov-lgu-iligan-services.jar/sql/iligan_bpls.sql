[getAssessmentGrossInfos]
select 
	x.lobid, i.lob_name as lobname, 
	sum(currentgross) as currentgross, sum(previousgross) as previousgross 
from ( 
	select 
		applicationid, lob_objid as lobid, 
		decimalvalue as currentgross, 0.0 as previousgross 
	from business_application_info a 
	where applicationid=$P{applicationid}  
		and attribute_objid='GROSS' 
		and type='assessmentinfo' 
		and lob_objid is not null 

	union 

	select 
		x.sourceid as applicationid, i.lob_objid as lobid, 
		0.0 as currentgross, i.decimalvalue as previousgross 
	from ( 
		select top 1 aa.objid, a.objid as sourceid  
		from business_application a 
			inner join business_application aa on a.business_objid=aa.business_objid 
		where a.objid=$P{applicationid} and aa.appyear < a.appyear 
		order by aa.appyear desc, aa.txndate desc  
	)x 
		inner join business_application_info i on x.objid=i.applicationid 
	where i.attribute_objid='GROSS' 
		and i.type='assessmentinfo' 
		and i.lob_objid is not null 
)x, business_application_info i 
where x.applicationid=i.applicationid 
	and x.lobid=i.lob_objid 
	and i.attribute_objid='GROSS' 
	and i.type='assessmentinfo'
group by x.lobid, i.lob_name 
order by i.lob_name 
