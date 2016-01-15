<font class="bold">Joint Borrower(s)</font>
<% if(jointborrowers.isEmpty() || jointborrowers == null) { %>
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
        <% jointborrowers.each{ %>
            <tr>
                <td width="10">&raquo; </td>
                <td> $it.lastname, $it.firstname ${ifnull(it.middlename, '')}</td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <table width="980">
                        <tr>
                            <td width="150">Address: </td>
                            <td>${ifnull(it.address, '-')}</td>
                        </tr>
                        <tr>
                            <td>Birth Date: </td>
                            <td>${ifnull(it.birthdate, '-')}</td>
                        </tr>
                        <tr>
                            <td>Marital Status:</td>
                            <td>${ifnull(it.civilstatus, '-')}</td>
                        </tr>
                        <tr>
                            <td>Citizenship: </td>
                            <td>${ifnull(it.citizenship, '-')}</td>
                        </tr>
                    </table><br/>
                    <table width="980">
                        <tr>
                            <td width="490">
                                <font class="bold">Residency</font>
                                <table>
                                    <tr>
                                        <td width="130">Type:</td>
                                        <td>$it.residency.type</td>
                                    </tr>
                                    <tr>
                                        <td>Since:</td>
                                        <td>$it.residency.since</td>
                                    </tr>
                                    <tr>
                                        <td>Rent Type:</td>
                                        <td>${ifnull(it.residency.renttype, '-')}</td>
                                    </tr>
                                    <tr>
                                        <td>Rent Amount:</td>
                                        <td>${ifnull(it.residency.rentamount, '-')}</td>
                                    </tr>
                                    <tr>
                                        <td>Remarks:</td>
                                        <td>${ifnull(it.residency.remarks, '-')}</td>
                                    </tr>
                                </table>
                            </td>
                            <td>
                                <font class="bold">Lot Occupancy</font>
                                <table>
                                    <tr>
                                        <td width="140">Type:</td>
                                        <td>$it.occupancy.type</td>
                                    </tr>
                                    <tr>
                                        <td>Since:</td>
                                        <td>$it.occupancy.since</td>
                                    </tr>
                                    <tr>
                                        <td>Rent Type:</td>
                                        <td>${ifnull(it.occupancy.renttype, '-')}</td>
                                    </tr>
                                    <tr>
                                        <td>Rent Amount:</td>
                                        <td>${ifnull(it.occupancy.rentamount, '-')}</td>
                                    </tr>
                                    <tr>
                                        <td>Remarks:</td>
                                        <td>${ifnull(it.occupancy.remarks, '-')}</td>
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