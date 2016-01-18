[getList]
SELECT * FROM controlseries
WHERE user_name LIKE $P{searchtext}
ORDER BY user_name, state DESC, dtfiled

[findPreviousControlSeriesByUserid]
SELECT objid FROM controlseries
WHERE user_objid=$P{userid}

[findOpenControlSeriesByUserid]
SELECT * FROM controlseries 
WHERE state='OPEN'
	AND user_objid=$P{userid}
ORDER BY dtfiled

[findSeriesByPrefixAndUserid]
SELECT * FROM controlseries
WHERE prefix=$P{prefix}
	AND user_objid=$P{userid}

[changeState]
UPDATE controlseries SET state = $P{state}
WHERE objid = $P{objid}