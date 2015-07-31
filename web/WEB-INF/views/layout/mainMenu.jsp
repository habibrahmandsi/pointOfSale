<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!-- ==================== MAIN MENU ==================== -->
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav" id="side-menu">

            <li>
                <a href="#"><i class="fa fa-bar-chart-o fa-fw"></i> <spring:message code="menu.salesAndPurchase"/><span
                        class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                    <li>
                        <a href="flot.html"><i class="fa fa-money fa-fw"></i>&nbsp;<spring:message code="menu.sales"/></a>
                    </li>
                    <li>
                        <a href="flot.html"><i class="fa fa-upload fa-fw"></i>&nbsp;<spring:message code="menu.sales.return"/></a>
                    </li>
                    <li>
                        <a href="morris.html"><i class="fa fa-table fa-fw"></i>&nbsp;<spring:message code="menu.purchase"/></a>
                    </li>

                    <li>
                        <a href="morris.html"><i class="fa fa-upload fa-fw"></i>&nbsp;<spring:message code="menu.purchase.return"/></a>
                    </li>
                </ul>
                <!-- /.nav-second-level -->
            </li>
            <li>
                <a href="tables.html"><i class="fa fa-table fa-fw"></i> <spring:message code="menu.administration"/>
                    <span class="fa arrow"></span>
                </a>

                <ul class="nav nav-second-level">
                   <%-- <li>
                        <a href="./upsertUser.do"><spring:message code="menu.administration.new.user"/></a>
                    </li>--%>
                    <li>
                        <a href="./userList.do"><spring:message code="menu.administration.user"/></a>
                    </li>
                    <li>
                        <a href="./companyList.do"><spring:message code="menu.administration.new.company"/></a>
                    </li>
                    <li>
                        <a href="./productList.do"><spring:message code="menu.administration.new.product"/></a>
                    </li>
                    <li>
                        <a href="./productGroupList.do"><spring:message code="menu.administration.new.productGroup"/></a>
                    </li>
                    <li>
                        <a href="./productTypeList.do"><spring:message code="menu.administration.new.productType"/></a>
                    </li>
                    <li>
                        <a href="./unitOfMeasureList.do"><spring:message code="menu.administration.new.unitOfMeasure"/></a>
                    </li>
                </ul>
                <!-- /.nav-second-level -->
            </li>

        </ul>
        <!-- /#side-menu -->
    </div>
    <!-- /.sidebar-collapse -->
</nav>
<!-- /.navbar-static-side -->