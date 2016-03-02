[getViolations]
SELECT
v2.*
FROM violation_ticket v1
INNER JOIN violation v2 on v1.violationid = v2.objid
WHERE parentid = $P{objid}