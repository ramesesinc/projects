<table>
    <tr>
        <th>Start Stub</th>
        <th>End Stub</th>
        <th>Qty</th>
    </tr>
    <%entity.items.each { %>
        <tr>
            <td>${it.startstub}</td>
            <td>${it.endstub}</td>
            <td>${it.qtyissued}</td>
        </tr>
    <% } %>
</table>