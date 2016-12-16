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
	and bp.activeyear=b.activeyear 
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
where bp.activeyear=b.activeyear and bp.state='ACTIVE' 
order by bp.version desc


[findBusinessAddress]
SELECT * FROM business_address WHERE objid=$P{objid} 


[getApplicationLOBs]
select 
	bal.objid, bal.applicationid, bal.businessid, ba.txndate,  
	bal.lobid, bal.name, bal.assessmenttype 
from ( 
	select 
		bal.businessid, bal.activeyear, bal.lobid, max(ba.txndate) as txndate 
	from business_application o 
		inner join business_application_lob bal on o.business_objid=bal.businessid 
		inner join business_application ba on bal.applicationid=ba.objid 
	where o.objid=$P{applicationid} 
		and bal.activeyear=o.appyear  
		and ba.state in ('RELEASE','COMPLETED') 
	group by bal.businessid, bal.activeyear, bal.lobid 
)xx 
	inner join business_application_lob bal on xx.businessid=bal.businessid 
	inner join business_application ba on bal.applicationid=ba.objid 
where bal.activeyear=xx.activeyear 
	and bal.lobid=xx.lobid 
	and ba.txndate=xx.txndate 
	and bal.assessmenttype in ('NEW','RENEW') 
order by ba.txndate, bal.name 


[getApplicationPayments]
select p.* from ( 
	select business_objid, appyear 
	from business_application 
	where objid=$P{applicationid} 
)xx  
	inner join business_application ba on (xx.business_objid=ba.business_objid and ba.appyear=xx.appyear) 
	inner join business_payment p on ba.objid=p.applicationid  
where p.voided=0 and ba.state in ('RELEASE','COMPLETED') 
order by p.refdate, p.refno 


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
