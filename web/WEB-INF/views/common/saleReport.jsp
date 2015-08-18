<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="sale.report.header"/></title>

<script src="<%= contextPath %>/resources/js/common/saleReport.js"  type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                        <spring:message code="sale.report.header"/>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-4"></div>
                        <div class="col-lg-4">
                            <div class="form-group">
                                <label><spring:message code="user.form.userName"/></label>
                                <select id="userId" class="form-control">
                                    <option value="0" selected> --- All --- </option>
                                    <c:forEach items="${userList}" var="pg">
                                                <option value="${pg.id}">${pg.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="form-group">
                                <label><spring:message code="unposted.form.fromDate"/></label>

                                <div class="input-append date" id="dp1-1" data-date="${fromDate}" data-date-format="${dateFormateForJs}">
                                    <div class="input-group">
                                        <input type="text" id="fromDate" class="form-control" value="${fromDate}"/>
                                        <span class="add-on input-group-btn"><img class="btn btn-default cal-btns" src="<%= contextPath %>/resources/images/cal.gif" /></span>
                                        <span class="input-group-btn"><img class="btn btn-default cal-btns cal-btns-last opaque crossBtn" src="<%= contextPath %>/resources/images/delete.png"/></span>
                                    </div>
                                </div>

                            </div>

                         <div class="form-group">
                                <label><spring:message code="unposted.form.toDate"/></label>

                                <div class="input-append date" id="dp1-2" data-date="${toDate}" data-date-format="${dateFormateForJs}">
                                    <div class="input-group">
                                        <input type="text" id="toDate" class="form-control"/>
                                        <span class="add-on input-group-btn"><img class="btn btn-default cal-btns" src="<%= contextPath %>/resources/images/cal.gif" /></span>
                                        <span class="input-group-btn"><img class="btn btn-default cal-btns cal-btns-last opaque crossBtn" src="<%= contextPath %>/resources/images/delete.png"/></span>
                                    </div>
                                </div>

                            </div>
                            <button class="btn btn-success search" type="button"><spring:message
                                    code="button.serch"/></button>
                        </div>
                        <div class="col-lg-4"></div>

                        <!-- /.col-lg-6 (nested) -->
                    </div>
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
            var userIdFromQparam = '${userId}';
            console.log("SMNLOG:********** userIdFromQparam:"+userIdFromQparam);
        </script>
        </div>
        <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->

