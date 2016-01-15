<font class="bold">Other Lending(s)</font>
<% if(otherlendings.isEmpty() || otherlendings == null) { %>
    <table>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table width="1000">
        <% otherlendings.each{ %>
            <tr>
                <td width="5">&#8226; </td>
                <td>$it.name</td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <table width="985">
                        <tr>
                            <td width="150">Kind of Loan: </td>
                            <td width="340">${ifnull(it.kind, '-')}</td>
                            <td width="150">Term: </td>
                            <td width="345">${ifnull(it.term, '-')}</td>
                        </tr>
                        <tr>
                            <td>Address: </td>
                            <td>${ifnull(it.address, '-')}</td>
                            <td>Interest Rate: </td>
                            <td>${ifnull(it.interest, '-')}</td>
                        </tr>
                        <tr>
                            <td>Loan Amount:</td>
                            <td>$it.amount</td>
                            <td>Mode of Payment: </td>
                            <td>${ifnull(it.paymentmode, '-')}</td>
                        </tr>
                        <tr>
                            <td>Date Granted: </td>
                            <td>${ifnull(it.dtgranted, '-')}</td>
                            <td>Payment: </td>
                            <td>$it.paymentamount</td>
                        </tr>
                        <tr>
                            <td>Date Matured: </td>
                            <td>${ifnull(it.dtmatured, '-')}</td>
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                        </tr>
                        <tr>
                            <td valign="top">Collateral(s) Offered: </td>
                            <td colspan="3">
                                <table class="subtable" border="1" width="815">
                                    <tr>
                                        <td style="padding: 10px;">
                                            <p>${ifnull(it.collateral, '-')}</p>
                                        </td>
                                    </tr> 
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">Remarks: </td>
                            <td colspan="3">
                                <table class="subtable" border="1" width="815">
                                    <tr>
                                        <td style="padding: 10px;">
                                            <p>${ifnull(it.remarks, '-')}</p>
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