<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="sales.header"/></title>

<%--<script src="<%= contextPath %>/resources/js/typeahead.bundle.js" type="text/javascript"></script>--%>
<script src="<%= contextPath %>/resources/js/jquery.typeahead.min.js" type="text/javascript"></script>
<script src="<%= contextPath %>/resources/js/common/sales.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="<%= contextPath %>/resources/css/jquery.typeahead.css"/>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


<!-- /.row -->
<div class="row">
    <form:form action="./upsertSales.do" method="post" id="salesForm" commandName="sales">
        <form:hidden path="id" id="saleId"/>
        <form:hidden path="salesReturn" id="salesReturn"/>
        <form:hidden path="unposted" id="unposted"/>

    <div class="col-lg-12 zeroPaddingForm">
        <div class="panel panel-default">
            <div class="panel-heading">
                <c:if test="${sales.salesReturn == true}">
                    <spring:message code="sales.header"/> Return Form
                </c:if>
                <c:if test="${sales.salesReturn == false}">
                    <spring:message code="sales.header"/> Form

                </c:if>


                <label style="float: right;color:#205eff" class="italicFont">
                My Today's Sale Total = <fmt:formatNumber type="number" value="${totalSaleByUser}" minFractionDigits="${maxFractionNum}" maxFractionDigits="${maxFractionNum}" />
                </label>
                <div style="clear: both"></div>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-4 leftPanelDiv" style="padding-right: 0px;padding-left: 6px;">

                        <div class="form-group">
                            <label><spring:message code="product.form.name"/></label>

                            <div class="col-lg-12 noLeftRightPadding">
                                <%--<input type="text" class="form-control productName"/>--%>

                                <div class="typeahead-container">
                                    <div class="typeahead-field">
                                     <span class="typeahead-query">
                                     <input class="productName" type="search" autocomplete="off">
                                     </span>
                                     </div>
                                 </div>

                            </div>
                        </div>
                        <div class="form-group">
                            <label><spring:message code="product.form.saleRate"/></label>
                            <input type="text" class="form-control salesRateInput"/>
                        </div>

                        <div class="form-group">
                            <label><spring:message code="product.quantity"/></label>
                            <input id="" type="text" class="form-control qty"/>
                        </div>
                        <button class="btn btn-success btn-block addLineItem" type="button"><spring:message
                                code="button.add"/></button>

                    </div>
                    <!-- /.row (nested) -->
                    <div class="col-lg-8 salesLineItemsDiv">
                        <c:if test="${not empty sales.salesItemList}">
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
                                            <form:input path="salesItemList[${k.index}].purchaseRate"
                                                        cssClass="purchaseRate hidden"/>
                                        </td>
                                        <td> ${salesItem.product.name}</td>
                                        <td> ${salesItem.product.company.name}</td>
                                        <td> ${salesItem.salesRate} <form:input
                                                path="salesItemList[${k.index}].salesRate"
                                                cssClass="salesRate hidden"/></td>
                                        <td><form:input path="salesItemList[${k.index}].quantity"
                                                        cssClass="qunatityInput"
                                                        cssStyle="max-width: 80px;padding-right: 5px;padding-left:5px;"/></td>
                                        <td class="lineTotal italicFont">
                                            <label> ${salesItem.totalPrice} </label><form:input
                                                path="salesItemList[${k.index}].totalPrice"
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
                                            value="${sales.totalAmount + sales.discount}"/></td>
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
                                    <td class="italicFont grandTotal"><label>${sales.totalAmount}</label>
                                        <form:input path="totalAmount" cssClass="productId hidden"/></td>
                                    <td></td>
                                </tr>
                                </tfoot>
                            </table>
                            </br>
                            <div style="text-align: right;">

                                <button class="btn btn-danger" type="reset">Cancel</button>
                                &nbsp;

                                <c:if test="${sales.salesReturn == true}">
                                    <button class="btn btn-success salesSave" type="button">Sales Return</button>
                                </c:if>
                                <c:if test="${sales.salesReturn == false}">
                                    <button class="btn btn-success salesSave" type="button">Sale</button>
                                </c:if>
                            </div>

                        </c:if>
                    </div>
                </div>
                <!-- /.panel-body -->
                <div class="row" style="padding-top: 20px;">
                    <div class="col-lg-4 zeroPaddingForm">
                        <c:if test="${sales.salesReturn == false}">
                            <a href="./upsertSales.do" class="btn btn-primary btn-block unpostedSaleLnk" >New Sale</a>
                        </c:if>
                        <c:if test="${sales.salesReturn == true}">
                            <a href="./upsertSales.do?salesReturn=1" class="btn btn-primary btn-block unpostedSaleLnk" >New Sale Return</a>
                        </c:if>
                    </div>
                    <div class="col-lg-4 zeroPaddingForm"></div>
                    <div class="col-lg-4 zeroPaddingForm">
                        <c:if test="${sales.salesReturn == false}">
                            <a href="./unpostedSale.do?userId=${sales.user.id}&opt=2" class="btn btn-primary btn-block unpostedSaleLnk" >My Un-posted Sale</a>
                        </c:if>
                        <c:if test="${sales.salesReturn == true}">
                            <a href="./unpostedSale.do?userId=${sales.user.id}&opt=3" class="btn btn-primary btn-block unpostedSaleLnk" >My Un-posted Sale Return</a>
                        </c:if>
                    </div>

                </div>
                <div class="row" style="padding-top: 40px;">
                <ul class="nav nav-tabs">
                    <li class="active"><a href="#sale" data-toggle="tab">Today's Sale</a></li>
                    <li><a href="#saleReturn" data-toggle="tab">Today's Sale Return</a></li>
                    <li><a href="#unpostedSale" data-toggle="tab">Today's Un-posted Sale</a></li>
                    <li><a href="#unpostedSaleeturn" data-toggle="tab">Today's Un-posted Sale Return</a></li>
                </ul>
                <div id="myTabContent" class="tab-content">

                    <div class="tab-pane fade active in" id="sale">
                        <br>
                        <table id="salesReportList" class="table table-striped table-hover dataTable">
                        </table>
                        <br>
                    </div>

                    <div class="tab-pane fade" id="saleReturn">
                        <br>
                            <table id="salesReturnReportList" class="table table-striped table-hover dataTable">
                        </table>
                        <br>
                    </div>
                    <div class="tab-pane fade" id="unpostedSale">
                        <br>
                        <table id="unpostedSalesReportList" class="table table-striped table-hover dataTable">
                        </table>
                        <br>
                    </div>
                    <div class="tab-pane fade" id="unpostedSaleeturn">
                        <br>
                        <table id="unpostedSalesReturnReportList" class="table table-striped table-hover dataTable">
                        </table>
                        <br>
                    </div>
                </div>

             </div><%--end of second row--%>
            </div>
            <!-- /.panel -->
        </div>

      <!-- /.col-lg-12 -->
    </div>
    </form:form>
    <script>
        var limitQty = +"${limitQty}";
        var salesId = +"${salesId}";
        var unposted = "${sales.unposted}";
        var userId = "${sales.user.id}";
        console.log("SMNLOG:unposted:" + unposted+" userId :"+userId);
        console.log("SMNLOG:limitQty:" + limitQty);
        console.log("SMNLOG:salesId:" + salesId);
    </script>
    <!-- /.row -->
    <!-- ==================== END OF COMMON ELEMENTS ROW ==================== -->
