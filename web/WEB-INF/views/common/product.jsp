<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%
    final String contextPath = request.getContextPath();
%>
<title><spring:message code="product.header"/></title>

<%--<script src="<%= contextPath %>/resources/js/common/product.js"  type="text/javascript"></script>--%>

<!-- ==================== COMMON ELEMENTS ROW ==================== -->


<!-- /.row -->
<div class="row">
    <form:form method="post" id="productForm" commandName="product">
        <form:hidden path="id"/>
        <form:hidden path="totalQuantity"/>
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <spring:message code="product.header"/> Form
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-6">

                        <div class="form-group">
                            <label><spring:message code="product.form.name"/></label>
                            <form:input path="name" cssClass="form-control"/>
                        </div>

                        <div class="form-group">
                            <label><spring:message code="product.form.purchaseRate"/></label>
                            <form:input path="purchaseRate" cssClass="form-control"/>
                        </div>

                        <div class="form-group">
                            <label><spring:message code="product.form.saleRate"/></label>
                            <form:input path="saleRate" cssClass="form-control"/>
                        </div>


                        <button class="btn btn-danger" type="reset"><spring:message code="button.cancel"/></button>
                        <button class="btn btn-success" type="submit"><spring:message
                                code="button.submit"/></button>


                    </div>
                    <div class="col-lg-6">

                        <div class="form-group">
                            <label><spring:message code="product.form.type"/></label>
                            <form:select path="productType.id" cssClass="form-control">
                                <option value=""></option>
                                <c:forEach items="${productTypeList}" var="pg">
                                    <c:choose>
                                        <c:when test="${product.productType.id == pg.id}">
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
                            <label><spring:message code="product.form.type"/></label>
                            <form:select path="unitOfMeasure.id" cssClass="form-control">
                                <c:forEach items="${unitOfMeasureList}" var="pg">
                                    <c:choose>
                                        <c:when  test="${product.unitOfMeasure.id == pg.id}">
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
                            <label><spring:message code="productGroup.form.name"/></label>
                            <form:select path="productGroup.id" cssClass="form-control">
                                <option value=""></option>
                                <c:forEach items="${productGroupList}" var="pg">
                                    <c:choose>
                                        <c:when test="${product.productGroup.id == pg.id}">
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
                            <label><spring:message code="company.form.name"/></label>
                            <form:select path="company.id" cssClass="form-control">
                                <option value=""></option>
                                <c:forEach items="${productCompanyList}" var="pg">
                                    <c:choose>
                                        <c:when test="${product.company.id == pg.id}">
                                            <option value="${pg.id}" selected>${pg.name}</option>
                                        </c:when>

                                        <c:otherwise>
                                            <option value="${pg.id}">${pg.name}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </form:select>
                        </div>

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
