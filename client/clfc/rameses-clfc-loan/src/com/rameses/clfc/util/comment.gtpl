<font class="bold">Comments</font><br/><br/>
<table>
    <% if(list.isEmpty() || list == null) { %>
        <tr>
            <td width="10">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    <% } else { 
        list.each{ %>
            <tr>
                <td width="10">&raquo; &nbsp;</td>
                <td>posted by $it.postedby @ $it.dtposted</td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <p style="padding: 0px 10px 0px 0px;">$it.remarks</p><br/>
                </td>
            </tr>
    <%    }
    } %>
</table>