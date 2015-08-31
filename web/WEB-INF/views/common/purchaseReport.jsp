<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="purchase.report.header"/></title>

<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/tipsy.css"/>
<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/lineChart.css"/>

<script src="<%= contextPath %>/resources/js/common/purchaseReport.js"  type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/d3js/d3.v3.min.js"  type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/d3js/tipsy.js"></script>
<script src="<%= contextPath %>/resources/js/common/lineChart.js"></script>
<!-- ==================== COMMON ELEMENTS ROW ==================== -->

<!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <c:if test="${searchBean.opt == 0}">
                        <spring:message code="purchase.report.header"/>
                    </c:if>
                    <c:if test="${searchBean.opt == 1}">
                        <spring:message code="purchase.return.report.header"/>
                    </c:if>
                    <c:if test="${searchBean.opt == 2}">
                        <spring:message code="unposted.purchase.header"/>
                    </c:if>
                    <c:if test="${searchBean.opt == 3}">
                        <spring:message code="unposted.sales.return.header"/>
                    </c:if>
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
                                    <option value="0" selected> --- All --- </option>
                                    <c:forEach items="${searchBean.userList}" var="pg">
                                        <c:choose>
                                            <c:when test="${pg.id == searchBean.userId}" >
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
                                <label><spring:message code="company.header"/></label>
                                <form:select path="companyId" id="companyId" cssClass="form-control">
                                    <option value="0" selected> --- All --- </option>
                                    <c:forEach items="${searchBean.companyList}" var="pg">
                                        <c:choose>
                                            <c:when test="${pg.id == searchBean.companyId}" >
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

                                <div class="input-append date" id="dp1-1" data-date="${fromDateStr}" data-date-format="${dateFormateForJs}">
                                    <div class="input-group">
                                        <form:input path="fromDateStr" id="fromDate" cssClass="form-control"/>
                                        <span class="add-on input-group-btn"><img class="btn btn-default cal-btns" src="<%= contextPath %>/resources/images/cal.gif" /></span>
                                        <span class="input-group-btn"><img class="btn btn-default cal-btns cal-btns-last opaque crossBtn" src="<%= contextPath %>/resources/images/delete.png"/></span>
                                    </div>
                                </div>

                            </div>

                            <div class="form-group">
                                <label><spring:message code="unposted.form.toDate"/></label>

                                <div class="input-append date" id="dp1-2" data-date="${toDateStr}" data-date-format="${dateFormateForJs}">
                                    <div class="input-group">
                                        <form:input path="toDateStr" id="toDate" cssClass="form-control"/>
                                        <span class="add-on input-group-btn"><img class="btn btn-default cal-btns" src="<%= contextPath %>/resources/images/cal.gif" /></span>
                                        <span class="input-group-btn"><img class="btn btn-default cal-btns cal-btns-last opaque crossBtn" src="<%= contextPath %>/resources/images/delete.png"/></span>
                                    </div>
                                </div>

                            </div>
                            <button class="btn btn-success search" type="submit"><spring:message
                                    code="button.serch"/></button>
                        </div>
                        <div class="col-lg-5">
                            <c:if test="${not empty searchBean.totalPurchaseList}">
                                <table class="table table-striped salesTotalByUser">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>User Name</th>
                                        <th style="text-align: right">Discount</th>
                                        <th style="text-align: right">Amount</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:set var="total" value="${0}"/>
                                    <c:forEach items="${searchBean.totalPurchaseList}" var="data" varStatus="idx">
                                        <c:set var="total" value="${total + data.totalPurchaseAmount}" />
                                        <c:set var="totalDis" value="${totalDis + data.totalDiscount}" />
                                        <tr>
                                            <td style="min-width: 10px;">${idx.index+1}</td>
                                            <td>${data.userName}</td>
                                            <td style="text-align: right"><fmt:formatNumber type="number" value="${data.totalDiscount}" minFractionDigits="${maxFractionNum}" maxFractionDigits="${maxFractionNum}" /></td>
                                            <td style="text-align: right"><fmt:formatNumber type="number" value="${data.totalPurchaseAmount}" minFractionDigits="${maxFractionNum}" maxFractionDigits="${maxFractionNum}" /></td>
                                            <td style="padding-left:5px;min-width: 10px;"><div class="legentRect"></div></td>
                                        </tr>
                                    </c:forEach>
                                    <tr><td></td><td class="italicFont">Total: </td>
                                        <td class="italicFont grandTotal"><label><fmt:formatNumber type="number" value="${totalDis}" minFractionDigits="${maxFractionNum}" maxFractionDigits="${maxFractionNum}" /></label></td>
                                        <td class="italicFont grandTotal"><label><fmt:formatNumber type="number" value="${total}" minFractionDigits="${maxFractionNum}" maxFractionDigits="${maxFractionNum}" /></label></td>
                                        <td></td></tr>
                                    </tbody>
                                </table>
                            </c:if>

                        </div>

                        <div class="col-lg-3">
                            <div id="chartByUserTotalSales"></div>
                            <c:if test="${not empty searchBean.totalPurchaseList}"><p class="chartDesc">N.B. This is a donut chart showing percentage by users.</p></c:if>

                        </div>

                        <!-- /.col-lg-6 (nested) -->
                    </div>
                    </form:form>
                    <br>
                    <div class="row">
                        <div class="col-lg-12 unpostedSalesWrapperDiv">
                            <div id="annualYieldsChart" class="d3charts"></div>
                            <br>
                            <table id="purchaseReportList" class="table table-striped table-hover dataTable">
                            </table>

                        </div>
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>
            </div>

            <!-- /.col-lg-12 -->
        <script>
            var userIdFromBean = +'${searchBean.userId}';
            var companyIdFromBean = +'${searchBean.companyId}';
            var opt = +'${searchBean.opt}';
            var donutChartData = [
                    <c:forEach items="${searchBean.totalPurchaseList}" var="data" varStatus="idx">
                    {"label":'${data.userName}',"count":'${data.totalPurchaseAmount}'}
                    <c:if test="${!idx.last}">,</c:if>
                    </c:forEach>

            ]
            var data = [
                <c:forEach items="${searchBean.dateWiseGroupByList}" var="data" varStatus="idx">
                {"date": '${data.purchase_date}',"totalCounts":+'${data.totalPurchaseAmount}'}
                <c:if test="${!idx.last}">,</c:if>
                </c:forEach>

            ]
            var lineChartData = [
                {
                    "id":"1",
                    "values":data
                }
            ]
            console.log("SMNLOG:********** donutChartData:"+JSON.stringify(donutChartData));
            console.log("SMNLOG:********** lineChartData:"+JSON.stringify(lineChartData));
            console.log("SMNLOG:********** userIdFromBean:"+userIdFromBean+" companyIdFromBean:"+companyIdFromBean);
            console.log("SMNLOG:********** opt:"+opt);
        </script>
        </div>
        <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

