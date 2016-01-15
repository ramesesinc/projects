<font class="bold">Collaterals</font>
<table>
    <tr>
        <td>
            <font class="bold">Appliance(s)</font>
            <% if(collateral.appliances.isEmpty() || collateral.appliances == null) { %>
                <table>
                    <tr>
                        <td width="5">&nbsp;</td>
                        <td class="nowrap" colspan="2">
                            <i>-- No available information found --</i>                         
                        </td> 
                    </tr> 
                </table>
            <% } else { %>
                <table width="980" border="1" style="padding: 0px; margin-left: 10px;">
                    <thead>
                        <th width="200">Type</th>
                        <th width="200">Brand</th>
                        <th width="150">Appraised Value</th>
                        <th>Remarks</th>
                    </thead>
                    <tbody>
                        <% collateral.appliances.each{ %>
                            <tr>
                                <td>$it.type</td>
                                <td>$it.brand</td>
                                <td align="right">$it.marketvalue</td>
                                <td>${ifnull(it.remarks, '-')}</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %><br/>
        </td>
    </tr>
    <tr>
        <td>
            <font class="bold">Vehicle(s)</font>
            <% if(collateral.vehicles.isEmpty() || collateral.vehicles == null) { %>
                <table>
                    <tr>
                        <td width="5">&nbsp;</td>
                        <td class="nowrap" colspan="2">
                            <i>-- No available information found --</i>                         
                        </td> 
                    </tr> 
                </table>
            <% } else { %>
                <table width="980" border="1" style="padding: 0px; margin-left: 10px;">
                    <thead>
                        <th width="200">Make</th>
                        <th width="200">Model</th>
                        <th width="150">Appraised Value</th>
                        <th>Remarks</th>
                    </thead>
                    <tbody>
                        <% collateral.vehicles.each{ %>
                            <tr>
                                <td>$it.make</td>
                                <td>$it.model</td>
                                <td align="right">$it.marketvalue</td>
                                <td>${ifnull(it.remarks, '-')}</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %><br/>
        </td>
    </tr>
    <tr>
        <td>
            <font class="bold">Propert(ies)</font>
            <% if(collateral.properties.isEmpty() || collateral.properties == null) { %>
                <table>
                    <tr>
                        <td width="5">&nbsp;</td>
                        <td class="nowrap" colspan="2">
                            <i>-- No available information found --</i>                         
                        </td> 
                    </tr> 
                </table>
            <% } else { %>
                <table width="980" border="1" style="padding: 0px; margin-left: 10px;">
                    <thead>
                        <th width="150">Classification</th>
                        <th width="200">Location</th>
                        <th width="150">Area</th>
                        <th width="150">Appraised Value</th>
                        <th>Remarks</th>
                    </thead>
                    <tbody>
                        <% collateral.properties.each{ %>
                            <tr>
                                <td>$it.classification</td>
                                <td>$it.location</td>
                                <td align="right">$it.areavalue $it.areauom</td>
                                <td align="right">$it.marketvalue</td>
                                <td>${ifnull(it.remarks, '-')}</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } %><br/>
        </td>
    </tr>
</table><br/>