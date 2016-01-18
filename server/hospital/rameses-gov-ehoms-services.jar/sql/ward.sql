[getList]
SELECT * FROM ward ORDER BY unittype, unitno

[getAvailableList]
SELECT * FROM ward
WHERE objid NOT IN 
(SELECT wardid FROM ward_admission)
