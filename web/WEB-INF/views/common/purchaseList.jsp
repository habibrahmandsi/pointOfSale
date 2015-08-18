<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="purchase.list.header"/></title>

<script src="<%= contextPath %>/resources/js/common/purchaseList.js"  type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <c:if test="${opt ==0}">
                        <spring:message code="purchase.list.header"/>
                    </c:if>
                    <c:if test="${opt ==1}">
                        <spring:message code="purchase.return.list.header"/>
                    </c:if>


                </div>
                <div class="panel-body">
                    <div class="row" style="margin-bottom: 10px;">
                        <div class="col-md-9">

                        </div>
                        <div class="col-md-3">
                            <c:if test="${opt == 0}">
                                <a href="./upsertPurchase.do" class="btn btn-primary" style="width: 98%;">New Purchase</a>
                            </c:if>
                            <c:if test="${opt == 1}">
                                <a href="./upsertPurchase.do?purchaseReturn=1" class="btn btn-primary" style="width: 98%;">New Purchase Return</a>
                            </c:if>

                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12">

                            <table id="purchaseList" class="table table-striped table-hover dataTable">
                            </table>

                        </div>
                        <!-- /.col-lg-6 (nested) -->
                    </div>
                    <!-- /.panel-body -->
                </div>
                <!-- /.panel -->
            </div>
            </div>

            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->
<script>

    var opt = '${opt}';
    console.log("SMNLOG:opt:"+opt);
</script>