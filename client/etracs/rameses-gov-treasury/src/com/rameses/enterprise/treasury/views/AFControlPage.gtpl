<html>
    <head>
        <style>
            body {font-family:arial; font-size:8px;}
            th { background-color: #848482; color:white; }
            table { border: 1px solid #848482; }
            td { background-color: #E0FFFF }
        </style>
    </head>
    <body>
        <table  cellpadding="1" cellspacing="1" width="100%">
             <tr>
                <th rowspan="2" align="center">Date Filed</th>
                <th rowspan="2" align="center">Ref No</th>
                <th rowspan="2" align="center">Issued To</th>
                <th colspan="3" align="center">Received</th>
                <th colspan="3" align="center">Begin</th>
                <th colspan="3" align="center">Issued</th>
                <th colspan="3" align="center">Ending</th>
                <th rowspan="2">Remarks</th>
             </tr>
             <tr>
                <th align="center">Start</th>
                <th align="center">End</th>
                <th align="center">Qty</th>
                <th align="center">Start</th>
                <th align="center">End</th>
                <th align="center">Qty</th>
                <th align="center">Start</th>
                <th align="center">End</th>
                <th align="center">Qty</th>
                <th align="center">Start</th>
                <th align="center">End</th>
                <th align="center">Qty</th>
             </tr>

             <% details.each { i-> %>
                <tr>
                   <td align="center">${i.refdate}</td>
                   <td align="center"><a href="viewRef" refid="${i.refid}">${i.refno}</a></td>
                   <td align="center">${ (i.issuedto?.name != null) ? i.issuedto?.name : '' }</td>

                   <td align="center">${i.receivedstartseries}</td>
                   <td align="center">${i.receivedendseries}</td>
                   <td align="center">${i.qtyreceived}</td>

                   <td align="center">${i.beginstartseries}</td>
                   <td align="center">${i.beginendseries}</td>
                   <td align="center">${i.qtybegin}</td>

                   <td align="center">${i.issuedstartseries}</td>
                   <td align="center">${i.issuedendseries}</td>
                   <td align="center">${i.qtyissued}</td>

                   <td align="center">${i.endingstartseries}</td>
                   <td align="center">${i.endingendseries}</td>
                   <td align="center">${i.qtyending}</td>

                    <td align="left">${i.remarks}</td>

                </tr>
             <% } %>

    </body>
</html>