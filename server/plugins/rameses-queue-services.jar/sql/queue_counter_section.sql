[getCounterSections]
SELECT a.* FROM queue_section a
INNER JOIN queue_counter_section b ON a.objid=b.sectionid
WHERE b.counterid = $P{counterid}
ORDER BY a.groupid, a.title