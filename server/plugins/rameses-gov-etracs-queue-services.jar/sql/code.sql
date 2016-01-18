[getList]
SELECT
*
FROM queue_code
WHERE objid = $P{objid}

[getNumber]
SELECT
*
FROM queue_code
WHERE section_objid = $P{sectionid}
AND category_objid = $P{categoryid}
AND state LIKE $P{state}
ORDER BY codeindex

[getNumberByState]
SELECT
*
FROM queue_code
WHERE state LIKE $P{state}
ORDER BY codeindex

[getIndexes]
SELECT
	codeindex 
FROM queue_code
WHERE section_objid = $P{sectionid}
AND category_objid = $P{categoryid}
ORDER BY dtgenerated, codeindex

[deleteDraft]
DELETE
FROM queue_code
WHERE state = 'DRAFT'
AND section_objid = $P{sectionid}
AND category_objid = $P{categoryid}