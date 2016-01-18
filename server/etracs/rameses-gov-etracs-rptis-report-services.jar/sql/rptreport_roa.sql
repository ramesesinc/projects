[getCurrentFaasRecord]
SELECT
f.objid,
f.owner_name,
f.tdno,
f.fullpin,
r.taxable,
r.rputype,
'CURRENT' as state,
null as parentlguindex,
null as lguindex,
CASE WHEN r.taxable=1 THEN r.totalareasqm ELSE null END AS totalareasqm,
f.dtapproved,
f.txntype_objid as code,
CASE WHEN r.taxable=1 AND r.rputype='land' THEN r.totalmv ELSE null END AS taxmvland,
CASE WHEN r.taxable=1 AND r.rputype='bldg' THEN r.totalmv ELSE null END AS taxmvbuild,
CASE WHEN r.taxable=1 AND r.rputype='mach' THEN r.totalmv ELSE null END AS taxmvmach,
CASE WHEN r.taxable=1 AND r.rputype='land' THEN r.totalav ELSE null END AS taxavland,
CASE WHEN r.taxable=1 AND r.rputype='bldg' THEN r.totalav ELSE null END AS taxavbuild,
CASE WHEN r.taxable=1 AND r.rputype='mach' THEN r.totalav ELSE null END AS taxavmach,
f.effectivityyear as yeartaxbegin,
CASE WHEN r.taxable!=1 THEN r.totalareasqm ELSE null END AS exptlandarea,
CASE WHEN r.taxable!=1 AND r.rputype='land' THEN r.totalmv ELSE null END AS exptmvland,
CASE WHEN r.taxable!=1 AND r.rputype='bldg' THEN r.totalmv ELSE null END AS exptmvbuild,
CASE WHEN r.taxable!=1 AND r.rputype='mach' THEN r.totalmv ELSE null END AS exptmvmach,
CASE WHEN r.taxable!=1 AND r.rputype='land' THEN r.totalav ELSE null END AS exptavland,
CASE WHEN r.taxable!=1 AND r.rputype='bldg' THEN r.totalav ELSE null END AS exptavbuild,
CASE WHEN r.taxable!=1 AND r.rputype='mac' THEN r.totalav ELSE null END AS exptavmach
FROM faas f
INNER JOIN rpu r ON f.rpuid = r.objid
INNER JOIN realproperty rp ON f.realpropertyid = rp.objid
INNER JOIN barangay b ON rp.barangayid = b.objid
WHERE ${filter}
 AND f.state in ('CURRENT', 'CANCELLED')
AND b.objid LIKE $P{barangay}
AND r.classification_objid LIKE $P{classification}
ORDER BY f.tdno


[getPreviousFaases]
select prevfaasid from previousfaas where faasid = $P{objid}


[findCancelledFaasRecord]
SELECT
f.objid, 
f.owner_name,
f.tdno,
f.fullpin,
r.taxable,
r.rputype,
'CANCELLED' as state,
NULL AS parentlguindex,
NULL AS lguindex,
CASE WHEN r.taxable=1 THEN r.totalareasqm ELSE NULL END AS totalareasqm,
f.dtapproved,
f.txntype_objid as code,
CASE WHEN r.taxable=1 AND r.rputype='land' THEN r.totalmv ELSE NULL END AS taxmvland,
CASE WHEN r.taxable=1 AND r.rputype='bldg' THEN r.totalmv ELSE NULL END AS taxmvbuild,
CASE WHEN r.taxable=1 AND r.rputype='mach' THEN r.totalmv ELSE NULL END AS taxmvmach,
CASE WHEN r.taxable=1 AND r.rputype='land' THEN r.totalav ELSE NULL END AS taxavland,
CASE WHEN r.taxable=1 AND r.rputype='bldg' THEN r.totalav ELSE NULL END AS taxavbuild,
CASE WHEN r.taxable=1 AND r.rputype='mach' THEN r.totalav ELSE NULL END AS taxavmach,
f.effectivityyear AS yeartaxbegin,
CASE WHEN r.taxable!=1 THEN r.totalareasqm ELSE NULL END AS exptlandarea,
CASE WHEN r.taxable!=1 AND r.rputype='land' THEN r.totalmv ELSE NULL END AS exptmvland,
CASE WHEN r.taxable!=1 AND r.rputype='bldg' THEN r.totalmv ELSE NULL END AS exptmvbuild,
CASE WHEN r.taxable!=1 AND r.rputype='mach' THEN r.totalmv ELSE NULL END AS exptmvmach,
CASE WHEN r.taxable!=1 AND r.rputype='land' THEN r.totalav ELSE NULL END AS exptavland,
CASE WHEN r.taxable!=1 AND r.rputype='bldg' THEN r.totalav ELSE NULL END AS exptavbuild,
CASE WHEN r.taxable!=1 AND r.rputype='mac' THEN r.totalav ELSE NULL END AS exptavmach
FROM faas f
LEFT JOIN rpu r ON f.rpuid = r.objid
LEFT JOIN realproperty rp ON f.realpropertyid = rp.objid
LEFT JOIN barangay b ON rp.barangayid = b.objid
WHERE f.objid = $P{prevfaasid}
ORDER BY f.tdno