[getList]
SELECT t.* 
FROM calendar_event t 
WHERE t.name LIKE $P{searchtext} 
ORDER BY t.date DESC 

[getListBetweenStartdateAndEnddate]
SELECT t.* FROM calendar_event t
WHERE t.date BETWEEN $P{startdate} AND $P{enddate}

[findByDate]
SELECT * FROM calendar_event WHERE date = $P{date}