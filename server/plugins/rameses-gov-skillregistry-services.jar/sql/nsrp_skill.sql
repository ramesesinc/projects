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