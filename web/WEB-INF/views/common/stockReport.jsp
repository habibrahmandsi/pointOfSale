<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="menu.reporting.stock"/></title>

<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/tipsy.css"/>
<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/lineChart.css"/>

<script src="<%= contextPath %>/resources/js/common/stockReport.js" type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->

<!-- /.row -->
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="menu.reporting.stock"/>
            </div>
            <div class="panel-body">
                <form:form method="post" commandName="searchBean">
                    <form:hidden path="opt"/>
                    <%--<form:hidden path="userId" id="userIdHidden"/>--%>
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="form-group">
                                <label><spring:message code="user.form.userName"/></label>
                                <form:select path="userId" id="userId" cssClass="form-control">
                                    <option value="0" selected> --- All ---</option>
                                    <c:forEach items="${searchBean.userList}" var="pg">
                                        <c:choose>
                                            <c:when test="${pg.id == searchBean.userId}">
                                                <option value="${pg.id}" selected>${pg.name}</option>
                                            </c:when>

                                            <c:otherwise>
                                                <option value="${pg.id}">${pg.name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="unposted.form.fromDate"/></label>

                                <div class="input-append date" id="dp1-1" data-date="${fromDateStr}"
                                     data-date-format="${dateFormateForJs}">
                                    <div class="input-group">
                                        <form:input path="fromDateStr" id="fromDate" cssClass="form-control"/>
                                        <span class="add-on input-group-btn"><img class="btn btn-default cal-btns"
                                                                                  src="<%= contextPath %>/resources/images/cal.gif"/></span>
                                        <span class="input-group-btn"><img
                                                class="btn btn-default cal-btns cal-btns-last opaque crossBtn"
                                                src="<%= contextPath %>/resources/images/delete.png"/></span>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group">
                                <label><spring:message code="unposted.form.toDate"/></label>

                                <div class="input-append date" id="dp1-2" data-date="${toDateStr}"
                                     data-date-format="${dateFormateForJs}">
                                    <div class="input-group">
                                        <form:input path="toDateStr" id="toDate" cssClass="form-control"/>
                                        <span class="add-on input-group-btn"><img class="btn btn-default cal-btns"
                                                                                  src="<%= contextPath %>/resources/images/cal.gif"/></span>
                                        <span class="input-group-btn"><img
                                                class="btn btn-default cal-btns cal-btns-last opaque crossBtn"
                                                src="<%= contextPath %>/resources/images/delete.png"/></span>
                                    </div>
                                </div>

                            </div>
                            <button class="btn btn-success search" type="submit"><spring:message
                                    code="button.serch"/></button>
                        </div>
                        <div class="col-lg-5">
                            <table class="table table-striped salesTotalByUser">
                                <thead>
                                <tr>
                                    <th>Type</th>
                                    <th style="text-align: right">TP Rate</th>
                                    <th style="text-align: right">MRP Rate</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>Purchase</td>
                                    <td class="italicFont" style="text-align: right"><fmt:formatNumber type="number"
                                                                                                       value="${searchBean.tpRate}"
                                                                                                       minFractionDigits="${maxFractionNum}"
                                                                                                       maxFractionDigits="${maxFractionNum}"/></td>
                                    <td class="italicFont" style="text-align: right"><fmt:formatNumber type="number"
                                                                                                       value="${searchBean.mrpRate}"
                                                                                                       minFractionDigits="${maxFractionNum}"
                                                                                                       maxFractionDigits="${maxFractionNum}"/></td>
                                </tr>
                                <tr>
                                    <td>Purchase Return</td>
                                    <td class="italicFont" style="text-align: right"><fmt:formatNumber type="number"
                                                                                                       value="${searchBean.tpRateReturn}"
                                                                                                       minFractionDigits="${maxFractionNum}"
                                                                                                       maxFractionDigits="${maxFractionNum}"/></td>
                                    <td class="italicFont" style="text-align: right"><fmt:formatNumber type="number"
                                                                                                       value="${searchBean.mrpRateReturn}"
                                                                                                       minFractionDigits="${maxFractionNum}"
                                                                                                       maxFractionDigits="${maxFractionNum}"/></td>
                                </tr>
                                <tr>
                                    <td>Total Stock</td>
                                    <td class="italicFont" style="text-align: right"><fmt:formatNumber type="number"
                                                                                                       value="${searchBean.tpRateTotal}"
                                                                                                       minFractionDigits="${maxFractionNum}"
                                                                                                       maxFractionDigits="${maxFractionNum}"/></td>
                                    <td class="italicFont" style="text-align: right"><fmt:formatNumber type="number"
                                                                                                       value="${searchBean.mrpRateTotal}"
                                                                                                       minFractionDigits="${maxFractionNum}"
                                                                                                       maxFractionDigits="${maxFractionNum}"/></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <!-- /.col-lg-6 (nested) -->
                    </div>
                </form:form>
                <br>

                <div class="row">
                    <div class="col-lg-12 unpostedSalesWrapperDiv">
                        <br>
                        <table id="stockReportList" class="table table-striped table-hover dataTable">
                        </table>

                    </div>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
    </div>

    <!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<script>
    var companyId = "${searchBean.companyId}";
    var productId = "${searchBean.productId}";
    console.log("SMNLOG:companyId:" + companyId+" productId :"+productId);
</script>
<!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

