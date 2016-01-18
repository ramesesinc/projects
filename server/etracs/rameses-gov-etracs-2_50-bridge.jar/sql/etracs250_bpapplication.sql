[getList]
SELECT objid, businessname, businessid, appinfos, assessmentinfos, lobs, taxfees 
FROM zzzzz_bpapplication 

[getExtractAppInfos]
select z.objid, z.businessid, z.businessname, z.appinfos
from ( 
	select objid from business_application a 
	where a.objid not in (select applicationid from business_application_info where applicationid=a.objid and type='appinfo') 
)bt inner join zzzzz_bpapplication z on bt.objid=z.objid 


[getExtractAssessmentInfos]
select z.objid, z.businessid, z.businessname, z.assessmentinfos 
from (
	select objid from business_application a 
	where a.objid not in (select applicationid from business_application_info where applicationid=a.objid and type='assessmentinfo') 
)bt inner join zzzzz_bpapplication z on bt.objid=z.objid 


[getExtractTaxFees]
select z.objid, z.businessid, z.businessname, z.taxfees  
from ( 
	select objid from business_application a 
	where a.objid not in (select applicationid from business_receivable where applicationid=a.objid) 
)bt inner join zzzzz_bpapplication z on bt.objid=z.objid 


[removeApplicationLob]
delete from zzzzz_bpapplication_lob where applicationid=$P{applicationid} 

[removeApplicationInfo] 
delete from zzzzz_bpapplication_info where applicationid=$P{applicationid} and type=$P{type} 

[removeApplicationTaxFee]
delete from zzzzz_bpapplication_taxfee where applicationid=$P{applicationid} 
