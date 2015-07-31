<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%-- <title><tiles:insertAttribute name="title" ignore="true"/></title> --%>
    <link type="text/css" href="resources/styles/base.css" rel="stylesheet" />
    <title><spring:message code="accessDenied.title"/></title>
</head>
<body>
<div align="center" id="form">
    <table width=60% align="center" cellspacing=0 style="border: 2px red solid" bgcolor="#FFDDDD">
        <tr><td colspan=2 align=center>&nbsp;</td></tr>
        <tr>
            <td align=center style="color: red">
                <spring:message code="accessDenied.AccessDeniedMessage"/>
            </td>
        </tr>
        <tr><td colspan=2 align=center>&nbsp;</td></tr>
    </table>
</div>

</body>
</html>