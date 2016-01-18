[getList]
SELECT * FROM  vitalsign WHERE name LIKE $P{searchtext} ORDER BY sortorder

[getAll]
SELECT * FROM vitalsign WHERE NOT(objid='BMI')  ORDER BY sortorder


