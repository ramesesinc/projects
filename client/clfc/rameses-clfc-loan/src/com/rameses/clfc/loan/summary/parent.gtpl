<font class="bold">Parents Information</font>
<% if(parent.fathername == null) { %>
    <table>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table>
        <tr>
            <td width="120">Father's Name: </td>
            <td width="150">$parent.fathername</td>
            <td width="50">Age: </td>
            <td width="100">$parent.fatherage</td>
        </tr>
        <tr>
            <td>Mother's Name: </td>
            <td>$parent.mothername</td>
            <td>Age</td>
            <td>$parent.motherage</td>
        </tr>
        <tr>
            <td>Address: </td>
            <td colspan="3">$parent.address</td>
        </tr>
        <tr>
            <td>Remarks: </td>
            <td>${ifnull(parent.remarks, '-')}</td>
        </tr>
    </table>
<% } %><br/>