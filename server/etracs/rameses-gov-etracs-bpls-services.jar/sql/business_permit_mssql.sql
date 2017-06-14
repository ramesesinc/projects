[getList]
SELECT DISTINCT bb.*  
FROM 
(
	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid 
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate >= $P{currentdate} 
    AND b.owner_name LIKE $P{searchtext} 

    UNION 

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid 
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate >= $P{currentdate} 
    AND b.businessname LIKE $P{searchtext} 

    UNION 

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid 
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate >= $P{currentdate} 
    AND b.bin LIKE $P{searchtext} 

) bb


[getExpiredList]
SELECT DISTINCT bb.*  
FROM 
(
	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate < $P{currentdate}
	AND b.owner_name LIKE $P{searchtext} 

	UNION 

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate < $P{currentdate}
	AND b.businessname LIKE $P{searchtext} 

	UNION

	SELECT bp.*, 
		b.businessname AS business_businessname, 
		b.address_text AS business_address_text, 
		b.bin AS business_bin,
		b.owner_name AS business_owner_name,
		ba.apptype
	FROM business_permit bp
	INNER JOIN business b ON bp.businessid=b.objid
	INNER JOIN business_application ba ON bp.applicationid=ba.objid
	WHERE bp.expirydate < $P{currentdate}
	AND b.bin LIKE $P{searchtext} 

) bb


[findPermitByApplication]
select 
	bp.*, ba.appno, ba.apptype, ba.ownername, ba.owneraddress, ba.tradename, 
	ba.businessaddress, b.bin, b.pin, b.address_objid, b.owner_address_objid, 
	(SELECT photo FROM entityindividual WHERE objid=b.owner_objid) AS photo, 
	ba.parentapplicationid  
from business_application ba 
	inner join business_permit bp on ba.objid=bp.applicationid  
	inner join business b on bp.businessid=b.objid 
where ba.objid=$P{applicationid} 
	and bp.activeyear=ba.appyear 
	and bp.state='ACTIVE' 


[findPermitForReport]
select 
	bp.*, ba.appno, ba.apptype, ba.ownername, ba.owneraddress, ba.tradename, 
	ba.businessaddress, b.bin, b.pin, b.address_objid, b.owner_address_objid, ba.parentapplicationid, 
	(select apptype from business_application where objid=ba.parentapplicationid) as parentapptype,  
	(select photo from entityindividual where objid=b.owner_objid) AS photo 
from ( 
	select objid as appid from business_application 
	where objid=$P{applicationid} and state in ('RELEASE','COMPLETED')  
	union 
	select objid as appid from business_application 
	where parentapplicationid=$P{applicationid} and state in ('RELEASE','COMPLETED')  
)xx 
	inner join business_application ba on xx.appid=ba.objid 
	inner join business_permit bp on ba.objid=bp.applicationid 
	inner join business b on bp.businessid=b.objid 
where bp.activeyear=ba.appyear and bp.state='ACTIVE' 
order by bp.version desc


[findBusinessAddress]
SELECT * FROM business_address WHERE objid=$P{objid} 


[getApplicationLOBs]
select 
	bal.objid, bal.applicationid, bal.businessid, ba.txndate,  
	bal.lobid, bal.name, bal.assessmenttype 
from ( 
	select business_objid, appyear 
	from business_application 
	where objid=$P{applicationid} 
)xx 
	inner join business_application ba on (xx.business_objid=ba.business_objid and ba.appyear=xx.appyear) 
	inner join business_application_lob bal on ba.objid=bal.applicationid 
where ba.state in ('RELEASE','COMPLETED') 
	and ba.objid in (select applicationid from business_permit where applicationid=ba.objid and state='ACTIVE') 
order by ba.txndate 


[updatePlateno]
UPDATE business_permit SET plateno=$P{plateno} WHERE objid=$P{objid}


[findPermitCount]
select count(*) as icount from business_permit 
where businessid=$P{businessid} and activeyear=$P{activeyear}  


[findPermit]
select * from business_permit 
where businessid=$P{businessid} and applicationid=$P{applicationid} 


[updateRemarks]
update business_permit set remarks=$P{remarks} where objid=$P{objid} 


[getAppLOBs]
select 
	alob.objid, alob.businessid, alob.applicationid, a.appyear, 
	a.apptype, a.txndate, a.dtfiled, alob.lobid, alob.name  
from business_permit p 
	inner join business_application pa on p.applicationid=pa.objid 
	inner join business_application a on (a.business_objid=p.businessid and a.appyear=pa.appyear)
	inner join business_application_lob alob on alob.applicationid=a.objid 
where p.objid = $P{permitid}  
	and a.state = 'COMPLETED' 
	and a.txndate <= pa.txndate 


[getPermits]
select p.*  
from business_permit p 
	inner join business_application pa on pa.objid=p.applicationid 
where p.businessid = $P{businessid} 
	and p.state = 'ACTIVE' 
order by pa.appyear, pa.txndate 


[getPayments]
select p.* from ( 

	select ba.objid as applicationid 
	from business_application a 
		inner join business_application ba on (ba.business_objid=a.business_objid and ba.appyear=a.appyear) 	
	where a.objid = $P{applicationid} 
		and ba.state='COMPLETED' 
		and ba.txndate <= a.txndate 

)tmp1, business_payment p 	
where p.applicationid=tmp1.applicationid 
	and p.voided = 0 
order by p.refdate, p.refno 
