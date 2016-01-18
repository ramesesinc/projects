[getList]
SELECT * FROM notetemplate WHERE section=$P{section}

[getTemplateList]
SELECT text 
FROM notetemplate 
WHERE section=$P{section} AND text LIKE $P{keyword}

[getTemplateListByKeyword]
SELECT keyword, text 
FROM notetemplate 
WHERE section=$P{section} AND keyword LIKE $P{keyword}