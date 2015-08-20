<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="sale.report.header"/></title>

<script src="<%= contextPath %>/resources/js/common/saleReport.js"  type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/d3js/d3.v3.min.js"  type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->

<!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <c:if test="${searchBean.opt == 0}">
                        <spring:message code="sale.report.header"/>
                    </c:if>
                    <c:if test="${searchBean.opt == 1}">
                        <spring:message code="sale.return.report.header"/>
                    </c:if>
                    <c:if test="${searchBean.opt == 2}">
                        <spring:message code="unposted.sales.header"/>
                    </c:if>
                </div>
                <div class="panel-body">
                    <form:form method="post" commandName="searchBean">
                        <form:hidden path="opt"/>
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
                            <c:if test="${not empty searchBean.totalSaleList}">
                                <table class="table table-striped salesTotalByUser">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>User Name</th>
                                        <th style="text-align: right">Amount</th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${searchBean.totalSaleList}" var="data" varStatus="idx">
                                        <tr>
                                            <td style="min-width: 10px;">${idx.index+1}</td>
                                            <td>${data.userName}</td>
                                            <td style="text-align: right">${data.totalSaleAmount}</td>
                                            <td style="padding-left:5px;min-width: 10px;"><div class="legentRect"></div></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>

                        </div>

                        <div class="col-lg-3">
                            <div id="chartByUserTotalSales"></div>
                            <p class="chartDesc">N.B. This is a donut chart showing percentage by users.</p>
                        </div>

                        <!-- /.col-lg-6 (nested) -->
                    </div>
                    </form:form>
                    <br>
                    <div class="row">
                        <div class="col-lg-12 unpostedSalesWrapperDiv">
                            <table id="salesReportList" class="table table-striped table-hover dataTable">
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
            var userIdFromQparam = +'${userId}';
            var userIdFromBean = +'${searchBean.userId}';
            var opt = +'${searchBean.opt}';
            var donutChartData = [
                    <c:forEach items="${searchBean.totalSaleList}" var="data" varStatus="idx">
                    {"label":'${data.userName}',"count":'${data.totalSaleAmount}'}
                    <c:if test="${!idx.last}">,</c:if>
                    </c:forEach>

            ]
            console.log("SMNLOG:********** donutChartData:"+JSON.stringify(donutChartData));
            console.log("SMNLOG:********** userIdFromQparam:"+userIdFromQparam);
            console.log("SMNLOG:********** userIdFromBean:"+userIdFromBean);
            console.log("SMNLOG:********** opt:"+opt);
        </script>
        </div>
        <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

