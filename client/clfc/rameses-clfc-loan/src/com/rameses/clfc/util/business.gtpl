<font class="bold">General Information</font>                
<table>
    <tr>
        <td>Tradename : </td>
        <td>$business.tradename</td>
    </tr>                
    <tr>
        <td class="nowrap">Kind of Business : </td>
        <td>$business.kind</td>
    </tr>
    <tr>
        <td>Stall Size(in mtrs.) : </td>
        <td>$business.stallsize</td>
    </tr>
    <tr>
        <td>Address : </td>
        <td>$business.address</td>
    </tr>
    <tr>
        <td>Occupancy : </td>
        <td>$business.occupancy.type</td>
    </tr>
    <tr>
        <td>&nbsp;&nbsp;&nbsp; Rent Type : </td>
        <td>${ifNull(business.occupancy.renttype, '-')}</td>
    </tr>
    <tr>
        <td>&nbsp;&nbsp;&nbsp; Rent Amount : </td>
        <td>${ifNull(business.occupancy.rentamount, '-')}</td>
    </tr>
    <tr>
        <td>Business Started : </td>
        <td>$business.dtstarted</td>
    </tr>
    <tr>
        <td>Ownership : </td>
        <td>$business.ownership</td>
    </tr>
    <tr>
        <td>Capital Invested : </td>
        <td>$business.capital</td>
    </tr>
    <tr>
        <td class="nowrap">Esimated Daily Sales : </td>
        <td>$business.avgsales</td>
    </tr>
    <tr>
        <td>Business Hours : </td>
        <td>$business.officehours</td>
    </tr> 
    </table><br/> 

    <font class="bold">Credit Investigation Report</font>
    <table style="width:100%;"> 
    <tr>
        <td>
            <span class="subtitle">Business Evaluation</span>
            <table class="subtable" border="1" style="width:100%;">
            <tr>
                <td style="padding: 10px 10px 10px 10px;">
                    <p>${ifNull(business.ci?.evaluation, '-')}</p>
                </td>
            </tr> 
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <br/>
            <span class="subtitle">Physical Outlook</span>
            <table class="subtable" border="1" style="width:100%;">
            <tr>
                <td style="padding: 10px 10px 10px 10px;">
                    <p>${ifNull(business.ci?.physicaloutlook, '-')}</p>
                </td>
            </tr> 
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <br/>
            <span class="subtitle">Cash Count</span>
            <table>
            <tr>
                <td width="80px">Filed By: </td>
                <td>${ifNull(business.ci?.filedby, '-')}</td>
            </tr>
            <tr>
                <td>Date: </td>
                <td>${ifNull(business.ci?.dtfiled, '-')}</td>
            </tr>
            <tr>
                <td>Time: </td>
                <td>${ifNull(business.ci?.timefiled, '-')}</td>
            </tr>
            <tr>
                <td>Amount: </td>
                <td>${ifNull(business.ci?.amount, '-')}</td>
            </tr>
            </table>
        </td>
    </tr>
</table>