[getList]
SELECT * FROM labtest_item WHERE name LIKE $P{searchtext} ORDER BY name

[getAll]
SELECT a.* FROM 
(SELECT objid, name, 'GROUP' AS reftype FROM labtest_template WHERE name LIKE $P{searchtext} 
UNION ALL
SELECT objid, name, 'ITEM' AS reftype FROM labtest_item WHERE name LIKE $P{searchtext} ) a
ORDER BY a.name


[getBillItems]
SELECT objid, name, price FROM labtest_item WHERE objid IN (${ids})