[getList]
SELECT code,code as objid,title 
FROM icd10_category WHERE title LIKE $P{searchtext}
