<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="sales.return.header"/></title>

<script src="<%= contextPath %>/resources/js/common/salesReturn.js" type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


<!-- /.row -->
<div class="row">
    <form:form action="./upsertSalesReturn.do" method="post" id="salesForm" commandName="sales">
        <form:hidden path="id"/>
    <div class="col-lg-12 zeroPaddingForm">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="sales.return.header"/> Details
            </div>
            <div class="panel-body">
                <div class="row">
                       <!-- /.row (nested) -->
                    <div class="col-lg-12 salesLineItemsDiv">
                        <c:if test="${not empty sales.salesItemList}">
                            <table class="table table-striped">
                                <thead>
                                <th style="width:5%">Sl #</th>
                                <th>Product name</th>
                                <th>Company Name</th>
                                <th>Sales rate</th>
                                <c:if test="${update == 0}">
                                    <th>Previous Qty</th>
                                </c:if>
                                <th>Return Qty</th>
                                <th>Item total</th>
                                </thead>
                                <tbody>
                                <c:forEach items="${sales.salesItemList}" var="salesItem" varStatus="k"
                                           begin="0">
                                    <tr id="${salesItem.product.id}">
                                        <td><label>${k.index+1}</label>
                                            <form:input path="salesItemList[${k.index}].product.id"
                                                        cssClass="productId hidden"/>
                                            <form:input path="salesItemList[${k.index}].id"
                                                        cssClass="itemId hidden"/>
                                            <form:input path="salesItemList[${k.index}].sales.id"
                                                        cssClass="salesId hidden"/>
                                            <form:input path="salesItemList[${k.index}].prevQuantity"
                                                        cssClass="prevQuantity hidden"/>
                                        </td>
                                        <td> ${salesItem.product.name}</td>
                                        <td> ${salesItem.product.company.name}</td>
                                        <td> ${salesItem.salesRate} <form:input
                                                path="salesItemList[${k.index}].salesRate"
                                                cssClass="salesRate hidden"/></td>
                                        <c:if test="${update == 0}">
                                            <td> ${salesItem.prevQuantity}</td>
                                            <td><form:input path="salesItemList[${k.index}].quantity"
                                                            cssClass="qunatityInput"
                                                            cssStyle="max-width: 80px;padding-right: 5px;padding-left:5px;"/></td>
                                        </c:if>
                                        <c:if test="${update == 1}">
                                        <td> ${salesItem.quantity}</td>
                                        </c:if>

                                        <td class="lineTotal italicFont">
                                            <label> ${salesItem.totalPrice} </label><form:input
                                                path="salesItemList[${k.index}].totalPrice"
                                                cssClass="totalPrice hidden"/></td>
                                    </tr>
                                </c:forEach>

                                </tbody>

                                <tfoot>

                               <%-- <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <c:if test="${update == 0}">
                                        <td></td>
                                    </c:if>
                                    <td class="italicFont">Discount:</td>
                                    <td class="italicFont discount"><form:input path="discount" cssClass="italicFont"
                                                                                cssStyle="max-width: 50px;padding-right: 5px;"/></td>
                                    <td style="text-align: right"><img class="hidden" alt="Invalid" title="Invalid"
                                                                       style="width: 20px;"
                                                                       src="<%=contextPath %>/resources/images/crossIcon.jpeg">
                                    </td>
                                </tr>--%>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <c:if test="${update == 0}">
                                        <td></td>
                                    </c:if>
                                    <td class="italicFont">Total:</td>
                                    <td class="italicFont grandTotal"><label>${sales.totalAmount}</label>
                                        <form:input path="totalAmount" cssClass="productId hidden"/></td>
                                </tr>
                                </tfoot>
                            </table>
                            <c:if test="${update == 0}">
                                </br>
                                <div style="text-align: right;">
                                    <button class="btn btn-danger" type="reset">Cancel</button>
                                    &nbsp;
                                    <button class="btn btn-success salesReturnSave" type="submit">Sales Return</button>
                                </div>
                            </c:if>
                        </c:if>
                    </div>
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
