<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.dsoft.util.Utils" %>
<%@ page import="com.dsoft.util.Constants" %>
<%@ page import="com.dsoft.entity.Role" %>
<%@ page import="com.dsoft.service.AdminService" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%
    final String contextPath = request.getContextPath();
    final String loggedUserName = (String)request.getSession().getAttribute("loggedUserName");
%>

<c:set var="maxFractionNum" value="<%=Constants.TWO_DECIMAL_GLOBAL_ROUND%>" scope="session"/>
<c:set var="loggedUserName" value="<%=loggedUserName%>" scope="session"/>
<c:set var="dateFormateForJstlTag" value="<%=Constants.DATE_FORMAT%>" scope="session"/>
<c:set var="dateFormateForJs" value="<%=Constants.DATE_FORMAT_JS%>" scope="session"/>
<c:set var="now" value="<fmt:formatDate value='<%=new java.util.Date()%>' pattern='${dateFormateForJstlTag}'/>" scope="session"/>
<c:set var="admin" value="<%=Role.ROLE_ADMIN.getLabel()%>" scope="session"/>
<c:set var="employee" value="<%=Role.ROLE_EMPLOYEE.getLabel()%>" scope="session"/>


<c:set var="superAdmin" value="<%=Role.ROLE_SUPER_ADMIN.getLabel()%>"/>
<c:set var="adminUser" value="<%=Role.ROLE_ADMIN.getLabel()%>"/>
<c:set var="employee" value="<%=Role.ROLE_EMPLOYEE.getLabel()%>"/>

<c:set var="UserInfo" value="<%=Role.ROLE_USER.getLabel()%>"/>
<c:set var="CompanyInfo" value="<%=Role.ROLE_COMPANY.getLabel()%>"/>

<c:set var="ProductInfo" value="<%=Role.ROLE_PRODUCT.getLabel()%>"/>
<c:set var="ProductGroupInfo" value="<%=Role.ROLE_PRODUCT_GROUP.getLabel()%>"/>

<c:set var="ProductTypeInfo" value="<%=Role.ROLE_PRODUCT_TYPE.getLabel()%>"/>
<c:set var="UomInfo" value="<%=Role.ROLE_UOM_INFO.getLabel()%>"/>

<c:set var="CreateSales" value="<%=Role.ROLE_CREATE_SALES.getLabel()%>"/>
<c:set var="SalesInfo" value="<%=Role.ROLE_SALES_INFO.getLabel()%>"/>
<c:set var="DeleteSales" value="<%=Role.ROLE_DELETE_SALES.getLabel()%>"/>
<c:set var="SalesReturn" value="<%=Role.ROLE_SALES_RETURN.getLabel()%>"/>
<c:set var="DeleteSalesReturn" value="<%=Role.ROLE_DELETE_SALES_RETURN.getLabel()%>"/>
<c:set var="CreatePurchase" value="<%=Role.ROLE_CREATE_PURCHASE.getLabel()%>"/>
<c:set var="PurchaseInfo" value="<%=Role.ROLE_PURCHASE_INFO.getLabel()%>"/>
<c:set var="DeletePurchase" value="<%=Role.ROLE_DELETE_PURCHASE.getLabel()%>"/>
<c:set var="PurchaseReturnInfo" value="<%=Role.ROLE_PURCHASE_RETURN_INFO.getLabel()%>"/>
<c:set var="DeletePurchaseReturn" value="<%=Role.ROLE_DELETE_PURCHASE_RETURN.getLabel()%>"/>


<!DOCTYPE html lang="en">
<link rel="shortcut icon" type="image/x-icon" href="<%= contextPath %>/resources/images/new_logo.png" />
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Start Bootstrap - SB Admin Version 2.0 Demo</title>

    <!-- Page-Level Plugin CSS - Forms -->

    <!-- SB Admin CSS - Include with every page -->

    <link href='<%= contextPath %>/resources/theme/css/bootstrap.min.css' rel='stylesheet' type='text/css'>
    <link href='<%= contextPath %>/resources/theme/font-awesome/css/font-awesome.css' rel='stylesheet' type='text/css'>
    <link href='<%= contextPath %>/resources/theme/css/sb-admin.css' rel='stylesheet' type='text/css'>


    <link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/datatables.bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/dataTables.responsive.css"/>
    <link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/datepicker.css"/>

    <link href='<%= contextPath %>/resources/css/style.css' rel='stylesheet' type='text/css'>

    <!-- Core Scripts - Include with every page -->
    <script src="<%= contextPath %>/resources/js/jquery-2.1.0.min.js"></script>
    <script src="<%= contextPath %>/resources/theme/js/bootstrap.min.js"></script>
    <script src="<%= contextPath %>/resources/theme/js/plugins/metisMenu/jquery.metisMenu.js"></script>
    <script src="<%= contextPath %>/resources/theme/js/sb-admin.js"></script>
    <script src="<%= contextPath %>/resources/js/common/util.js"></script>

    <script type="text/javascript" charset="utf8" src="<%= contextPath %>/resources/js/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf8" src="<%= contextPath %>/resources/js/datatables.bootstrap.js"></script>
    <script type="text/javascript" charset="utf8" src="<%= contextPath %>/resources/js/dataTables.responsive.js"></script>
    <script type="text/javascript" charset="utf8" src="<%= contextPath %>/resources/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" charset="utf8" src="<%= contextPath %>/resources/js/validation/bootstrapValidator.min.js"></script>

</head>
<body>

<div id="wrapper">

    <tiles:insertAttribute name="header"/>
    <tiles:insertAttribute name="mainMenu"/>

    <div id="page-wrapper" style="padding-top: 5px;">
    <%@ include file="/WEB-INF/views/message.jsp" %>
    <tiles:insertAttribute name="body"/>
    </div>
</div>

<script>
    contextPath = "<%=request.getContextPath()%>"
    globalUserName = '<%=loggedUserName%>';
    console.log("SMNLOG:globalUserName:"+globalUserName);
</script>

</body>
</html>