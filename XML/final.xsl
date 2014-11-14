<?xml version="1.0" encoding="ISO-8859-1"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
#hello
{
background-color:#d0e4fe;
}
#h1
{
color:orange;
text-align:center;
}
#p
{
font-family:"Times New Roman";
font-size:20px;
}
</style>
</head>
<body>
<table border="1">
<tr>
<th><p id="hello">PRODUCT</p></th>
<th><p id="h1">PRICE</p></th>
</tr>
<xsl:for-each select="staff/product">
<tr>
<td><p id="p"><xsl:value-of select="name"/></p></td>
<td><p id="p"><xsl:value-of select="price"/></p></td>
</tr>

</xsl:for-each>
</table>
</body>

</html>