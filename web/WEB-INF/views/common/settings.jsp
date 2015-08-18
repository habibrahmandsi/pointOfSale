<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="settings.header"/></title>

<%--<script src="<%= contextPath %>/resources/js/common/unitOfMeasure.js"  type="text/javascript"></script>--%>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


    <!-- /.row -->
    <div class="row">
        <form:form method="post" id="settingsForm" commandName="settings">
                <form:hidden path="id"/>
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <spring:message code="settings.header"/> Form
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">

                            <div class="form-group">
                                <label><spring:message code="settings.form.shop.name"/></label>
                                <form:input path="shopName" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="settings.form.shop.address"/></label>
                                <form:input path="shopAddress" cssClass="form-control"/>
                            </div>

                            <div class="form-group">
                                <label><spring:message code="settings.form.stock.limit.alarm"/></label>
                                <form:input path="stockLimitAlarmQty" cssClass="form-control"/>
                            </div>

                             <div class="form-group">
                                <label><spring:message code="settings.form.expire.date.alarm"/></label>
                                <form:input path="expireDateAlarmDay" cssClass="form-control"/>
                            </div>

                            <button class="btn btn-danger" type="reset"><spring:message code="button.cancel"/></button>
                            <button class="btn btn-success" type="submit"><spring:message
                                    code="button.submit"/></button>


                        </div>

                        <!-- /.row (nested) -->
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>

            <!-- /.col-lg-12 -->
        </div>
        </form:form>
        <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->
