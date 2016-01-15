<font class="bold">General Information</font>
<table>
<tr>
    <td width="100px">Classification : </td>
    <td>$property.classification</td>
</tr>
<tr>
    <td>Location : </td>
    <td>$property.location</td>
</tr>
<tr>
    <td>Area : </td>
    <td>$property.areavalue $property.areauom</td>
</tr>
<tr>
    <td>Zonal Value : </td>
    <td>$property.zonalvalue</td>
</tr>
<tr>
    <td>Date Acquired : </td>
    <td>${ifNull(property.dtacquired, '-')}</td>
</tr>
<tr>
    <td>Acquired From : </td>
    <td>$property.acquiredfrom</td>
</tr>
<tr>
    <td class="nowrap">Mode of Acquisition : </td>
    <td>$property.modeofacquisition</td>
</tr>
<tr>
    <td>Registered Name : </td>
    <td>$property.registeredname</td>
</tr>
<tr>
    <td>Market Value : </td>
    <td>$property.marketvalue</td>
</tr>
<tr>
    <td>Remarks : </td>
    <td> <p>$property.remarks</p> </td>
</tr>
</table>