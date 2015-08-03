<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="purchase.header"/></title>

<script src="<%= contextPath %>/resources/js/typeahead.bundle.js" type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/common/purchase.js" type="text/javascript"></script>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


<!-- /.row -->
<div class="row">
    <form:form action="./upsertPurchase.do" method="post" id="purchaseForm" commandName="purchase">
        <form:hidden path="id"/>
    <div class="col-lg-12 zeroPaddingForm">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="purchase.header"/> Form
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-3">

                       <%-- <div class="form-group">
                            <label><spring:message code="company.form.name"/></label>

                            <div class="col-lg-12 noLeftRightPadding">
                                <input type="text" class="form-control companyName"/>
                            </div>

                        </div>
--%>
                        <div class="form-group">
                            <label><spring:message code="product.form.name"/></label>

                            <div class="col-lg-12 noLeftRightPadding">
                                <input type="text" class="form-control productName"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label><spring:message code="product.form.purchaseRate"/></label>
                            <input type="text" class="form-control purchaseRateInput"/>
                        </div>

                        <div class="form-group">
                            <label><spring:message code="product.form.saleRate"/></label>
                            <input type="text" class="form-control saleRateInput"/>
                        </div>

                        <div class="form-group">
                            <label><spring:message code="product.quantity"/></label>
                            <input type="text" class="form-control qty"/>
                        </div>
                        <button class="btn btn-success btn-block addLineItem" type="button"><spring:message
                                code="button.add"/></button>

                    </div>
                    <!-- /.row (nested) -->
                    <div class="col-lg-9 purchaseLineItemsDiv">
                        <c:if test="${not empty purchase.purchaseItemList}">
                            <table class="table table-striped">
                                <thead>
                                <th style="width:5%">Sl #</th>
                                <th>Product name</th>
                                <th>Company Name</th>
                                <th>Purchase rate</th>
                                <th>Qty</th>
                                <th>Item total</th>
                                <th></th>
                                </thead>
                                <tbody>
                                <c:forEach items="${purchase.purchaseItemList}" var="purchaseItem" varStatus="k"
                                           begin="0">
                                    <tr id="${purchaseItem.product.id}">
                                        <td><label>${k.index+1}</label>
                                            <form:input path="purchaseItemList[${k.index}].product.id"
                                                        cssClass="productId hidden"/>
                                            <form:input path="purchaseItemList[${k.index}].id"
                                                        cssClass="itemId hidden"/>
                                            <form:input path="purchaseItemList[${k.index}].purchase.id"
                                                        cssClass="purchaseId hidden"/>
                                            <form:input path="purchaseItemList[${k.index}].prevQuantity"
                                                        cssClass="prevQuantity hidden"/>
                                        </td>
                                        <td> ${purchaseItem.product.name}</td>
                                        <td> ${purchaseItem.product.company.name}</td>
                                        <td> ${purchaseItem.purchaseRate} <form:input
                                                path="purchaseItemList[${k.index}].purchaseRate"
                                                cssClass="purchaseRate hidden"/></td>
                                        <td><form:input path="purchaseItemList[${k.index}].quantity"
                                                        cssClass="qunatityInput"
                                                        cssStyle="max-width: 80px;padding-right: 5px;padding-left:5px;"/></td>
                                        <td class="lineTotal italicFont">
                                            <label> ${purchaseItem.totalPrice} </label><form:input
                                                path="purchaseItemList[${k.index}].totalPrice"
                                                cssClass="totalPrice hidden"/></td>
                                        <td style="text-align: right"><img alt="Saved" title="Saved"
                                                                           class="iconInsideTable"
                                                                           src="<%=contextPath %>/resources/images/crossIcon.jpeg">
                                        </td>
                                    </tr>
                                </c:forEach>

                                </tbody>

                                <tfoot>
                                <tr>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                    <td class="italicFont">Sub Total:</td>
                                    <td class="italicFont subTotal"><c:out
                                            value="${purchase.totalAmount + purchase.discount}"/></td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>
                                    <td></td>
                                    <td></td>
                                    <td class="italicFont">Discount:</td>
                                    <td class="italicFont discount"><form:input path="discount" cssClass="italicFont"
                                                                                cssStyle="max-width: 50px;padding-right: 5px;"/></td>
                                    <td style="text-align: right"><img class="hidden" alt="Invalid" title="Invalid"
                                                                       style="width: 20px;"
                                                                       src="<%=contextPath %>/resources/images/crossIcon.jpeg">
                                    </td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>
                                    <td></td>
                                    <td></td>
                                    <td class="italicFont">Total:</td>
                                    <td class="italicFont grandTotal"><label>${purchase.totalAmount}</label>
                                        <form:input path="totalAmount" cssClass="productId hidden"/></td>
                                    <td></td>
                                </tr>
                                </tfoot>
                            </table>
                            </br>
                            <div style="text-align: right;">
                                <button class="btn btn-danger" type="reset">Cancel</button>
                                &nbsp;
                                <button class="btn btn-success purchaseSave" type="button">Purchase</button>
                            </div>

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
