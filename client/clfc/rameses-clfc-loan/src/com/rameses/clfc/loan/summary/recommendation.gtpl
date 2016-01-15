<font class="bold">CI Recommendation</font>
<% if(recommendation?.ciremarks == null) { %>
    <table>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table class="subtable" border="1" width="1000" style="margin-left: 10px;">
        <tr>
            <td style="padding: 10px;">
                <p>$recommendation.ciremarks</p>
            </td>
        </tr> 
    </table>
<% } %><br/>
<font class="bold">CRECOM Recommendation</font>
<% if(recommendation?.crecomremarks == null) { %>
    <table>
        <tr>
            <td width="5">&nbsp;</td>
            <td class="nowrap" colspan="2">
                <i>-- No available information found --</i>                         
            </td> 
        </tr> 
    </table>
<% } else { %>
    <table class="subtable" border="1" width="1000" style="margin-left: 10px;">
        <tr>
            <td style="padding: 10px;">
                <p>$recommendation.crecomremarks</p>
            </td>
        </tr>
    </table>
    <table width="1000" style="margin-left: 10px;">
        <tr>
            <td width="150">Marketer: $recommendation.marketeramount</td>
            <td width="150">CI: $recommendation.ciamount</td>
            <td width="150">FCA: $recommendation.fcaamount</td>
            <td width="150">CAO: $recommendation.caoamount</td>
            <td width="150">BCOH: $recommendation.bcohamount</td>
            <td>&nbsp;</td>
        </tr>
    </table>
<% } %><br/>