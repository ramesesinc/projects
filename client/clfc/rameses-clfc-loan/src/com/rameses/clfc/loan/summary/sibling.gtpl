<font class="bold">Sibling(s)</font>
<% if(siblings.isEmpty() || siblings == null) { %>
    <table>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table width="1000" border="1" style="padding: 0px; margin-left: 10px;">
        <thead>
            <tr>
                <th width="250">Name</th>
                <th width="100">Age</th>
                <th>Address</th>
            </tr>
        </thead>
        <tbody>
            <% siblings.each{ %>
                <tr>
                    <td>$it.name</td>
                    <td>$it.age yrs. old</td>
                    <td>$it.address</td>
                </tr>
            <% } %>
        </tbody>
    </table>
<% } %><br/>