[getList]
SELECT
*
FROM queue_section
WHERE name LIKE $P{searchtext}
ORDER BY sortorder
