<html>
    <body>
        <table style="border:1px solid black;" width="100%">
             <tr>
                     <th align="left">AF No</th>
                     <th align="left">Description</th>
                     <th align="center">Unit</th>
                     <th align="center">Qty Requested</th>
                     <th align="center">Qty Issued</th>
                     <th align="center">Unit Cost</th>
                     <th>&nbsp;</th>
             </tr>
             
             <%entity.items.each { o-> %>
                <tr style="border-top:1px solid black;">
                   <td>${o.item.objid}</td>
                   <td>${o.item.title}</td>
                   <td align="center">${o.unit}</td>
                   <td align="center">${o.qtyrequested}</td>
                   <td align="center">${o.qtyissued}</td>
                   <td align="center">${o.cost}</td>
                   <td align="center">
                      
                   </td>
                </tr>

                <%o.items.each { ii-> %>
                    <tr>
                        <td>&nbsp;</td>
                        <td colspan="6">
                            series: ${ii.startseries} - ${ii.endseries} stub: ${ii.startstub} - ${ii.endstub} 
                        </td>
                    </tr>
                <% } %>

             <% } %>
            	

        </table>
    </body>
</html>