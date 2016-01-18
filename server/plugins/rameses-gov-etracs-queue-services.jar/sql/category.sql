[getList]
SELECT
*
FROM queue_category
WHERE name LIKE $P{searchtext}
ORDER BY sortorder