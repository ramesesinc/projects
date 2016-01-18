[getList]
SELECT * FROM radiology_test WHERE name LIKE $P{searchtext} ORDER BY name
