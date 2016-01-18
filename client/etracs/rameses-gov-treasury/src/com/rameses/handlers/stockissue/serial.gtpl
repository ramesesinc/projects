<table >
    <tr >
        <th><font size="4">Start Series</font></th>
        <th><font size="4">End Series</font></th>
        <th><font size="4">Start Stub</font></th>        
        <th><font size="4">End Stub</font></th>
        <th><font size="4">Quantity</font></th>
    </tr>
    <%entity.items.each { %>
        <tr>
            <td width="110"><font size="4">${it.startseries}</font></td>
            <td width="110"><font size="4">${it.endseries}</font></td>
            <td width="80"><font size="4">${it.startstub}</font></td>
            <td width="80"><font size="4">${it.endstub}</font></td>
            <td width="80"><font size="4">${it.qtyissued}</font></td>
        </tr>
    <% } %>
</table>