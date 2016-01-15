<% if(list.isEmpty() || list == null ) { %>
    <tr>
        <td width="5">&nbsp;</td>
        <td class="nowrap" colspan="2">
            <i>-- No available information found --</i>                         
        </td> 
    </tr> 
<% } else {
    list.each{ %>
        <tr>
            <td width="5">&#8226; </td>
            <td>Source of Income : </td>
            <td>$it.name</td>
        </tr>
        <tr>
            <td width="5">&nbsp; </td>
            <td>Remarks : </td>
            <td><p>${ifNull(it.remarks, '-')}</p></td>
        </tr>
<%    }
} %>