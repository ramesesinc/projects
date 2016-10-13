[findPreceedingExcerpt]
SELECT 
	SUM( case 
		when r.taxable = 1 and 'av' = $P{type} then r.totalav 
		when r.taxable = 1 and 'mv' = $P{type} then r.totalmv
		else 0.0 end 
	) AS taxable,
	SUM( case when r.taxable = 1 then 1.0 else 0.0 end ) AS taxablecnt,
	SUM(case 
		when r.taxable = 0 and 'av' = $P{type} then r.totalav 
		when r.taxable = 0 and 'mv' = $P{type} then r.totalmv 
		else 0.0 end 
	) AS exempt,
	SUM( case when r.taxable = 0 then 1.0 else 0.0 end ) AS exemptcnt
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state in ('CURRENT', 'CANCELLED')


[findCurrentExcerpt]
SELECT 
	SUM( case 
		when r.taxable = 1 and 'av' = $P{type} then r.totalav 
		when r.taxable = 1 and 'mv' = $P{type} then r.totalmv
		else 0.0 end 
	) AS taxable,
	SUM( case when r.taxable = 1 then 1.0 else 0.0 end ) AS taxablecnt,
	SUM(case 
		when r.taxable = 0 and 'av' = $P{type} then r.totalav 
		when r.taxable = 0 and 'mv' = $P{type} then r.totalmv 
		else 0.0 end 
	) AS exempt,
	SUM( case when r.taxable = 0 then 1.0 else 0.0 end ) AS exemptcnt
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CURRENT'  


[findCancelledExcerpt]
SELECT 
	SUM( case 
		when r.taxable = 1 and 'av' = $P{type} then r.totalav 
		when r.taxable = 1 and 'mv' = $P{type} then r.totalmv
		else 0.0 end 
	) AS taxable,
	SUM( case when r.taxable = 1 then 1.0 else 0.0 end ) AS taxablecnt,
	SUM(case 
		when r.taxable = 0 and 'av' = $P{type} then r.totalav 
		when r.taxable = 0 and 'mv' = $P{type} then r.totalmv 
		else 0.0 end 
	) AS exempt,
	SUM( case when r.taxable = 0 then 1.0 else 0.0 end ) AS exemptcnt
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CANCELLED'  


[findEndingExcerpt]
SELECT 
	SUM( case 
		when r.taxable = 1 and 'av' = $P{type} then r.totalav 
		when r.taxable = 1 and 'mv' = $P{type} then r.totalmv
		else 0.0 end 
	) AS taxable,
	SUM( case when r.taxable = 1 then 1.0 else 0.0 end ) AS taxablecnt,
	SUM(case 
		when r.taxable = 0 and 'av' = $P{type} then r.totalav 
		when r.taxable = 0 and 'mv' = $P{type} then r.totalmv 
		else 0.0 end 
	) AS exempt,
	SUM( case when r.taxable = 0 then 1.0 else 0.0 end ) AS exemptcnt
FROM faas f
	INNER JOIN rpu r ON f.rpuid = r.objid 
	INNER JOIN realproperty rp on f.realpropertyid = rp.objid 
	INNER JOIN propertyclassification pc ON r.classification_objid = pc.objid 
WHERE ${filter}
  AND f.state = 'CURRENT'  

