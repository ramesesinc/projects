[getEntitySearchResultID]
	SELECT
		entityid
	FROM entity_skill
	WHERE name LIKE $P{searchtext}

UNION

	SELECT
		entityid
	FROM entity_workexperience 
	WHERE position LIKE $P{searchtext}

UNION

	SELECT
		entityid 
	FROM entity_education 
	WHERE course LIKE $P{searchtext}