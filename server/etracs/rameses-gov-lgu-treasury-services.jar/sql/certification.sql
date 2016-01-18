[getList]
SELECT *, type AS _filetype FROM certification WHERE requestedby LIKE $P{searchtext} ORDER BY txnno DESC  