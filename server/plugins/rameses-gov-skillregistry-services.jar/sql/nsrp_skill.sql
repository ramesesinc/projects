[getEntitySearchResultID]
	SELECT
		entityid
	FROM entity_skill
	WHERE name LIKE $P{searchtext}

UNION

	SELECT
		w.entityid
	FROM entity_workexperience w
	LEFT JOIN jobtitle j 
		ON w.jobtitle_objid = j.objid
	WHERE j.name LIKE $P{searchtext}

UNION

	SELECT
		e.entityid 
	FROM entity_education e
	LEFT JOIN course c 
		ON e.course_objid = c.objid
	WHERE c.name LIKE $P{searchtext}


[getAdvanceEntitySearchResultID]
SELECT
	e.entityid AS objid
FROM entity_education e 
LEFT JOIN entity_workexperience w 
	ON e.entityid = w.entityid
LEFT JOIN entity_skill s 
	ON w.entityid = s.entityid
WHERE COALESCE(w.jobtitle_name,'') LIKE $P{profession}
AND COALESCE(e.course_name,'') LIKE $P{education} 
AND COALESCE(s.name,'') LIKE $P{skill}
AND e.entityid in ${entityids}
GROUP BY objid


[getJobTitleCount]
SELECT
	j.name,
	(
		SELECT COUNT(objid)
		FROM entity_workexperience
		WHERE jobtitle_objid = j.objid
	) as counter
FROM jobtitle j
ORDER BY counter