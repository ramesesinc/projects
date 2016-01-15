<font class="bold">Business(es)</font>
<% if(businesses.isEmpty() || businesses == null) { %>
    <table>
        <tr>
            <td width="10">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table width="1000">
        <% businesses.each{ %>
            <tr>
                <td width="10">&raquo; </td>
                <td>$it.tradename</td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <table width="980">
                        <tr>
                            <td width="150">Address: </td>
                            <td width="340">$it.address</td>
                            <td width="150">Initial Invested: </td>
                            <td width="340">$it.capital</td>
                        </tr>
                        <tr>
                            <td>Kind of Business: </td>
                            <td>$it.kind</td>
                            <td>Ownership: </td>
                            <td>$it.ownership</td>
                        </tr>
                        <tr>
                            <td>Stall Size(in mtrs.):</td>
                            <td>$it.stallsize</td>
                            <td>Occupancy: </td>
                            <td>$it.occupancy.type</td>
                        </tr>
                        <tr>
                            <td>Business Started: </td>
                            <td>$it.dtstarted</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td valign="top">Business Evaluation: </td>
                            <td colspan="3">
                                <table class="subtable" border="1" width="815">
                                    <tr>
                                        <td style="padding: 10px;">
                                            <p>${ifnull(it.ci?.evaluation, '-')}</p>
                                        </td>
                                    </tr> 
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">Physical Outlook: </td>
                            <td colspan="3">
                                <table class="subtable" border="1" width="815">
                                    <tr>
                                        <td style="padding: 10px;">
                                            <p>${ifnull(it.ci?.physicaloutlook, '-')}</p>
                                        </td>
                                    </tr> 
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        <% } %>
    </table>
<% } %><br/>