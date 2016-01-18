[getList]
SELECT * FROM amnesty_history
ORDER BY dtcreated DESC

[getListByRefno]
SELECT * FROM amnesty_history
WHERE refno = $P{refno}
ORDER BY dtcreated DESC