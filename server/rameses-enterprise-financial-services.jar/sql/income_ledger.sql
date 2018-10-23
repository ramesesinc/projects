[getReport]
SELECT * FROM (
	
	SELECT x1.objid, x1.code, x1.title, x1.amount, x1.leftindex, x1.rightindex, x1.level, x1.type 
	FROM 
	(SELECT main.objid, main.code, main.title, main.type, 
		(SELECT SUM(a2.amount)
	    FROM ( SELECT a1.itemacctid, SUM(a1.amount) AS amount
		   FROM (
		     SELECT il.itemacctid, SUM(il.amount) AS amount
		     FROM vw_income_ledger il 
		     ${filter}
		     GROUP BY il.itemacctid
		   ) a1
		  GROUP BY a1.itemacctid) a2
	   INNER JOIN itemaccount ia ON ia.objid = a2.itemacctid
	   LEFT JOIN ( SELECT itemid, acctid FROM account_item_mapping aim WHERE maingroupid = $P{maingroup} ) aim ON ia.objid = aim.itemid
	   LEFT JOIN account acc ON acc.objid = aim.acctid 
	   WHERE NOT(acc.leftindex IS NULL)
	   AND acc.leftindex > main.leftindex AND acc.rightindex < main.rightindex 	   
	) AS amount,
	main.leftindex, main.rightindex, main.level
	FROM account main
	WHERE main.maingroupid = $P{maingroup}) x1 
	WHERE NOT(x1.amount IS NULL)
	
				
    UNION
    
    (SELECT x2.objid, x2.code, x2.title, x2.amount, x2.leftindex, x2.rightindex, x2.level, 'itemaccount' AS type
    FROM
    (SELECT ia.objid, ia.code, ia.title, a2.amount, acc.leftindex, acc.rightindex, acc.level + 1 AS level 
	FROM ( SELECT a1.itemacctid, SUM(a1.amount) AS amount
		   FROM (
		     SELECT il.itemacctid, SUM(il.amount) AS amount
		     FROM vw_income_ledger il 
		     ${filter}
		     GROUP BY il.itemacctid) a1
		  GROUP BY a1.itemacctid) a2
	INNER JOIN itemaccount ia ON ia.objid = a2.itemacctid
	LEFT JOIN ( SELECT itemid, acctid FROM account_item_mapping aim WHERE maingroupid = $P{maingroup} ) aim ON ia.objid = aim.itemid
	LEFT JOIN account acc ON acc.objid = aim.acctid 
	WHERE NOT(acc.leftindex IS NULL)) x2)
				
) tp 				
${typefilter}
ORDER BY tp.leftindex, tp.level		
		



