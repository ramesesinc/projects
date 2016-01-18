[getList]
SELECT * FROM labtest_template WHERE name LIKE $P{searchtext} ORDER BY name


[getLookup]
SELECT * FROM labtest_template WHERE name LIKE $P{searchtext} ORDER BY name

[findPriceItem]
SELECT objid, name, price, items FROM labtest_template WHERE objid=$P{objid}