<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="user.list.header"/></title>

<script src="<%= contextPath %>/resources/js/common/userList.js"  type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <spring:message code="user.list.header"/>
                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-md-9">

                        </div>
                        <div class="col-md-3">
                            <a href="./upsertUser.do" class="btn btn-primary" style="width: 98%;">New User</a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">

                            <table id="userList" class="table table-striped table-hover dataTable">
                            </table>

                        </div>
                        <!-- /.col-lg-6 (nested) -->
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>

            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->
