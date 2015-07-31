<%@ page import="com.dsoft.entity.Role" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%
    final String contextPath = request.getContextPath();
%>

<title></title>


    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Dashboard</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-8">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> Area Chart Example
                    <div class="pull-right">
                        <div class="btn-group">
                            <button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle" type="button">
                                Actions
                                <span class="caret"></span>
                            </button>
                            <ul role="menu" class="dropdown-menu pull-right">
                                <li><a href="#">Action</a>
                                </li>
                                <li><a href="#">Another action</a>
                                </li>
                                <li><a href="#">Something else here</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div id="morris-area-chart" style="position: relative;"><svg height="377" version="1.1" width="609" xmlns="http://www.w3.org/2000/svg" style="overflow: hidden; position: relative; top: -0.600006px;"><desc>Created with Raphaël 2.1.0</desc><defs/><text style="text-anchor: end; font: 12px sans-serif;" x="56.5" y="342" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">0</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M69,342H584" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="56.5" y="262.75" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">7,500</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M69,262.75H584" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="56.5" y="183.5" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">15,000</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M69,183.5H584" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="56.5" y="104.25" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">22,500</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M69,104.25H584" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="56.5" y="25" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">30,000</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M69,25H584" stroke-width="0.5"/><text style="text-anchor: middle; font: 12px sans-serif;" x="488.8845686512758" y="354.5" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal" transform="matrix(1,0,0,1,0,7.5)"><tspan dy="4">2012</tspan></text><text style="text-anchor: middle; font: 12px sans-serif;" x="260.4823815309842" y="354.5" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal" transform="matrix(1,0,0,1,0,7.5)"><tspan dy="4">2011</tspan></text><path style="fill-opacity: 1;" fill="#7cb47c" stroke="none" d="M69,285.8593C83.39246658566222,280.04763333333335,112.17739975698663,267.98710416666665,126.56986634264885,262.61263333333335C140.96233292831107,257.23816250000004,169.74726609963548,250.22093588342443,184.1397326852977,242.86353333333335C198.37575941676792,235.58610255009108,226.84781287970839,206.24479378453037,241.0838396111786,204.0733C255.16342648845685,201.92566878453036,283.32260024301337,224.00769404761903,297.4021871202916,225.58703333333332C311.7946537059538,227.20146904761904,340.57958687727825,215.77324166666668,354.97205346294044,216.8484C369.3645200486027,217.92355833333335,398.14945321992707,253.27372094717668,412.5419198055893,234.1883C426.77794653705956,215.31032928051002,455.25,74.20368333333332,469.48602673147025,64.9948333333333C483.7220534629405,55.785983333333306,512.1941069258809,147.01484458105645,526.4301336573511,160.51749999999998C540.8226002430133,174.16853624772313,569.6075334143378,170.33657499999998,584,173.6096L584,342L69,342Z" fill-opacity="1"/><path style="" fill="none" stroke="#4da74d" d="M69,285.8593C83.39246658566222,280.04763333333335,112.17739975698663,267.98710416666665,126.56986634264885,262.61263333333335C140.96233292831107,257.23816250000004,169.74726609963548,250.22093588342443,184.1397326852977,242.86353333333335C198.37575941676792,235.58610255009108,226.84781287970839,206.24479378453037,241.0838396111786,204.0733C255.16342648845685,201.92566878453036,283.32260024301337,224.00769404761903,297.4021871202916,225.58703333333332C311.7946537059538,227.20146904761904,340.57958687727825,215.77324166666668,354.97205346294044,216.8484C369.3645200486027,217.92355833333335,398.14945321992707,253.27372094717668,412.5419198055893,234.1883C426.77794653705956,215.31032928051002,455.25,74.20368333333332,469.48602673147025,64.9948333333333C483.7220534629405,55.785983333333306,512.1941069258809,147.01484458105645,526.4301336573511,160.51749999999998C540.8226002430133,174.16853624772313,569.6075334143378,170.33657499999998,584,173.6096" stroke-width="3"/><circle cx="69" cy="285.8593" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="126.56986634264885" cy="262.61263333333335" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="184.1397326852977" cy="242.86353333333335" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="241.0838396111786" cy="204.0733" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="297.4021871202916" cy="225.58703333333332" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="354.97205346294044" cy="216.8484" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="412.5419198055893" cy="234.1883" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="469.48602673147025" cy="64.9948333333333" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="526.4301336573511" cy="160.51749999999998" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><circle cx="584" cy="173.6096" r="2" fill="#4da74d" stroke="#ffffff" style="" stroke-width="1"/><path style="fill-opacity: 1;" fill="#a7b3bc" stroke="none" d="M69,313.8292666666667C83.39246658566222,307.4734166666667,112.17739975698663,293.9731791666667,126.56986634264885,288.40586666666667C140.96233292831107,282.83855416666665,169.74726609963548,272.3346595628415,184.1397326852977,269.2907666666667C198.37575941676792,266.27995956284155,226.84781287970839,266.6348116482505,241.0838396111786,264.1870666666667C255.16342648845685,261.7662199815838,283.32260024301337,253.21152225274724,297.4021871202916,249.8164C311.7946537059538,246.34583058608058,340.57958687727825,236.58032916666667,354.97205346294044,236.7243C369.3645200486027,236.86827083333333,398.14945321992707,265.67898761384333,412.5419198055893,250.96816666666666C426.77794653705956,236.4172459471767,455.25,128.3050166666667,469.48602673147025,119.67733333333334C483.7220534629405,111.04965000000001,512.1941069258809,172.88931999089255,526.4301336573511,181.9467C540.8226002430133,191.10361165755918,569.6075334143378,189.88755,584,192.5345L584,342L69,342Z" fill-opacity="1"/><path style="" fill="none" stroke="#7a92a3" d="M69,313.8292666666667C83.39246658566222,307.4734166666667,112.17739975698663,293.9731791666667,126.56986634264885,288.40586666666667C140.96233292831107,282.83855416666665,169.74726609963548,272.3346595628415,184.1397326852977,269.2907666666667C198.37575941676792,266.27995956284155,226.84781287970839,266.6348116482505,241.0838396111786,264.1870666666667C255.16342648845685,261.7662199815838,283.32260024301337,253.21152225274724,297.4021871202916,249.8164C311.7946537059538,246.34583058608058,340.57958687727825,236.58032916666667,354.97205346294044,236.7243C369.3645200486027,236.86827083333333,398.14945321992707,265.67898761384333,412.5419198055893,250.96816666666666C426.77794653705956,236.4172459471767,455.25,128.3050166666667,469.48602673147025,119.67733333333334C483.7220534629405,111.04965000000001,512.1941069258809,172.88931999089255,526.4301336573511,181.9467C540.8226002430133,191.10361165755918,569.6075334143378,189.88755,584,192.5345" stroke-width="3"/><circle cx="69" cy="313.8292666666667" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="126.56986634264885" cy="288.40586666666667" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="184.1397326852977" cy="269.2907666666667" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="241.0838396111786" cy="264.1870666666667" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="297.4021871202916" cy="249.8164" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="354.97205346294044" cy="236.7243" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="412.5419198055893" cy="250.96816666666666" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="469.48602673147025" cy="119.67733333333334" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="526.4301336573511" cy="181.9467" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><circle cx="584" cy="192.5345" r="2" fill="#7a92a3" stroke="#ffffff" style="" stroke-width="1"/><path style="fill-opacity: 1;" fill="#2577b5" stroke="none" d="M69,313.8292666666667C83.39246658566222,313.53340000000003,112.17739975698663,315.61239166666667,126.56986634264885,312.6458C140.96233292831107,309.67920833333335,169.74726609963548,291.4099757741348,184.1397326852977,290.09653333333335C198.37575941676792,288.79736744080145,226.84781287970839,304.716158839779,241.0838396111786,302.19536666666664C255.16342648845685,299.70227550644563,283.32260024301337,272.5269244505495,297.4021871202916,270.041C311.7946537059538,267.49983278388277,340.57958687727825,279.45854166666663,354.97205346294044,282.087C369.3645200486027,284.71545833333334,398.14945321992707,303.55633023679417,412.5419198055893,291.0686666666667C426.77794653705956,278.7167385701275,455.25,190.47796250000002,469.48602673147025,182.72863333333333C483.7220534629405,174.97930416666668,512.1941069258809,220.35031170309654,526.4301336573511,229.07403333333332C540.8226002430133,237.89362003642987,569.6075334143378,246.94490833333333,584,252.90186666666665L584,342L69,342Z" fill-opacity="1"/><path style="" fill="none" stroke="#0b62a4" d="M69,313.8292666666667C83.39246658566222,313.53340000000003,112.17739975698663,315.61239166666667,126.56986634264885,312.6458C140.96233292831107,309.67920833333335,169.74726609963548,291.4099757741348,184.1397326852977,290.09653333333335C198.37575941676792,288.79736744080145,226.84781287970839,304.716158839779,241.0838396111786,302.19536666666664C255.16342648845685,299.70227550644563,283.32260024301337,272.5269244505495,297.4021871202916,270.041C311.7946537059538,267.49983278388277,340.57958687727825,279.45854166666663,354.97205346294044,282.087C369.3645200486027,284.71545833333334,398.14945321992707,303.55633023679417,412.5419198055893,291.0686666666667C426.77794653705956,278.7167385701275,455.25,190.47796250000002,469.48602673147025,182.72863333333333C483.7220534629405,174.97930416666668,512.1941069258809,220.35031170309654,526.4301336573511,229.07403333333332C540.8226002430133,237.89362003642987,569.6075334143378,246.94490833333333,584,252.90186666666665" stroke-width="3"/><circle cx="69" cy="313.8292666666667" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="126.56986634264885" cy="312.6458" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="184.1397326852977" cy="290.09653333333335" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="241.0838396111786" cy="302.19536666666664" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="297.4021871202916" cy="270.041" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="354.97205346294044" cy="282.087" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="412.5419198055893" cy="291.0686666666667" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="469.48602673147025" cy="182.72863333333333" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="526.4301336573511" cy="229.07403333333332" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/><circle cx="584" cy="252.90186666666665" r="2" fill="#0b62a4" stroke="#ffffff" style="" stroke-width="1"/></svg><div class="morris-hover morris-default-style" style="left: 7px; top: 205px; display: none;"><div class="morris-hover-row-label">2010 Q1</div><div style="color: #0b62a4" class="morris-hover-point">
                        iPhone:
                        2,666
                    </div><div style="color: #7A92A3" class="morris-hover-point">
                        iPad:
                        -
                    </div><div style="color: #4da74d" class="morris-hover-point">
                        iPod Touch:
                        2,647
                    </div></div></div>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> Bar Chart Example
                    <div class="pull-right">
                        <div class="btn-group">
                            <button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle" type="button">
                                Actions
                                <span class="caret"></span>
                            </button>
                            <ul role="menu" class="dropdown-menu pull-right">
                                <li><a href="#">Action</a>
                                </li>
                                <li><a href="#">Another action</a>
                                </li>
                                <li><a href="#">Something else here</a>
                                </li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-4">
                            <div class="table-responsive">
                                <table class="table table-bordered table-hover table-striped">
                                    <thead>
                                    <tr>
                                        <th>#</th>
                                        <th>Date</th>
                                        <th>Time</th>
                                        <th>Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>3326</td>
                                        <td>10/21/2013</td>
                                        <td>3:29 PM</td>
                                        <td>$321.33</td>
                                    </tr>
                                    <tr>
                                        <td>3325</td>
                                        <td>10/21/2013</td>
                                        <td>3:20 PM</td>
                                        <td>$234.34</td>
                                    </tr>
                                    <tr>
                                        <td>3324</td>
                                        <td>10/21/2013</td>
                                        <td>3:03 PM</td>
                                        <td>$724.17</td>
                                    </tr>
                                    <tr>
                                        <td>3323</td>
                                        <td>10/21/2013</td>
                                        <td>3:00 PM</td>
                                        <td>$23.71</td>
                                    </tr>
                                    <tr>
                                        <td>3322</td>
                                        <td>10/21/2013</td>
                                        <td>2:49 PM</td>
                                        <td>$8345.23</td>
                                    </tr>
                                    <tr>
                                        <td>3321</td>
                                        <td>10/21/2013</td>
                                        <td>2:23 PM</td>
                                        <td>$245.12</td>
                                    </tr>
                                    <tr>
                                        <td>3320</td>
                                        <td>10/21/2013</td>
                                        <td>2:15 PM</td>
                                        <td>$5663.54</td>
                                    </tr>
                                    <tr>
                                        <td>3319</td>
                                        <td>10/21/2013</td>
                                        <td>2:13 PM</td>
                                        <td>$943.45</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.col-lg-4 (nested) -->
                        <div class="col-lg-8">
                            <div id="morris-bar-chart" style="position: relative;"><svg height="377" version="1.1" width="396" xmlns="http://www.w3.org/2000/svg" style="overflow: hidden; position: relative; left: -0.100006px; top: -0.599976px;"><desc>Created with Raphaël 2.1.0</desc><defs/><text style="text-anchor: end; font: 12px sans-serif;" x="36.5" y="342" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">0</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M49,342H371" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="36.5" y="262.75" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">25</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M49,262.75H371" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="36.5" y="183.5" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">50</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M49,183.5H371" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="36.5" y="104.25" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">75</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M49,104.25H371" stroke-width="0.5"/><text style="text-anchor: end; font: 12px sans-serif;" x="36.5" y="25" text-anchor="end" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal"><tspan dy="4">100</tspan></text><path style="" fill="none" stroke="#aaaaaa" d="M49,25H371" stroke-width="0.5"/><text style="text-anchor: middle; font: 12px sans-serif;" x="348" y="354.5" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal" transform="matrix(1,0,0,1,0,7.5)"><tspan dy="4">2012</tspan></text><text style="text-anchor: middle; font: 12px sans-serif;" x="256" y="354.5" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal" transform="matrix(1,0,0,1,0,7.5)"><tspan dy="4">2010</tspan></text><text style="text-anchor: middle; font: 12px sans-serif;" x="164" y="354.5" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal" transform="matrix(1,0,0,1,0,7.5)"><tspan dy="4">2008</tspan></text><text style="text-anchor: middle; font: 12px sans-serif;" x="72" y="354.5" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#888888" font-size="12px" font-family="sans-serif" font-weight="normal" transform="matrix(1,0,0,1,0,7.5)"><tspan dy="4">2006</tspan></text><rect x="54.75" y="25" width="15.75" height="317" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="73.5" y="56.69999999999999" width="15.75" height="285.3" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="100.75" y="104.25" width="15.75" height="237.75" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="119.5" y="135.95000000000002" width="15.75" height="206.04999999999998" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="146.75" y="183.5" width="15.75" height="158.5" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="165.5" y="215.2" width="15.75" height="126.80000000000001" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="192.75" y="104.25" width="15.75" height="237.75" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="211.5" y="135.95000000000002" width="15.75" height="206.04999999999998" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="238.75" y="183.5" width="15.75" height="158.5" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="257.5" y="215.2" width="15.75" height="126.80000000000001" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="284.75" y="104.25" width="15.75" height="237.75" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="303.5" y="135.95000000000002" width="15.75" height="206.04999999999998" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="330.75" y="25" width="15.75" height="317" r="0" rx="0" ry="0" fill="#0b62a4" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/><rect x="349.5" y="56.69999999999999" width="15.75" height="285.3" r="0" rx="0" ry="0" fill="#7a92a3" stroke="none" style="fill-opacity: 1;" fill-opacity="1"/></svg><div class="morris-hover morris-default-style" style="display: none;"></div></div>
                        </div>
                        <!-- /.col-lg-8 (nested) -->
                    </div>
                    <!-- /.row -->
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-clock-o fa-fw"></i> Timeline
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <ul class="timeline">
                        <li>
                            <div class="timeline-badge"><i class="fa fa-check"></i>
                            </div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                    <p>
                                        <small class="text-muted"><i class="fa fa-time"></i> 11 hours ago via Twitter</small>
                                    </p>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                </div>
                            </div>
                        </li>
                        <li class="timeline-inverted">
                            <div class="timeline-badge warning"><i class="fa fa-credit-card"></i>
                            </div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="timeline-badge danger"><i class="fa fa-credit-card"></i>
                            </div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                </div>
                            </div>
                        </li>
                        <li class="timeline-inverted">
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="timeline-badge info"><i class="fa fa-save"></i>
                            </div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                    <hr>
                                    <div class="btn-group">
                                        <button data-toggle="dropdown" class="btn btn-primary btn-sm dropdown-toggle" type="button">
                                            <i class="fa fa-cog"></i>
                                            <span class="caret"></span>
                                        </button>
                                        <ul role="menu" class="dropdown-menu">
                                            <li><a href="#">Action</a>
                                            </li>
                                            <li><a href="#">Another action</a>
                                            </li>
                                            <li><a href="#">Something else here</a>
                                            </li>
                                            <li class="divider"></li>
                                            <li><a href="#">Separated link</a>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                </div>
                            </div>
                        </li>
                        <li class="timeline-inverted">
                            <div class="timeline-badge success"><i class="fa fa-thumbs-up"></i>
                            </div>
                            <div class="timeline-panel">
                                <div class="timeline-heading">
                                    <h4 class="timeline-title">Timeline Event</h4>
                                </div>
                                <div class="timeline-body">
                                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin vel justo eu mi scelerisque vulputate. Aliquam in metus eu lectus aliquet egestas.</p>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-8 -->
        <div class="col-lg-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bell fa-fw"></i> Notifications Panel
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="list-group">
                        <a class="list-group-item" href="#">
                            <i class="fa fa-comment fa-fw"></i> New Comment
                                    <span class="pull-right text-muted small"><em>4 minutes ago</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-twitter fa-fw"></i> 3 New Followers
                                    <span class="pull-right text-muted small"><em>12 minutes ago</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-envelope fa-fw"></i> Message Sent
                                    <span class="pull-right text-muted small"><em>27 minutes ago</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-tasks fa-fw"></i> New Task
                                    <span class="pull-right text-muted small"><em>43 minutes ago</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-upload fa-fw"></i> Server Rebooted
                                    <span class="pull-right text-muted small"><em>11:32 AM</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-bolt fa-fw"></i> Server Crashed!
                                    <span class="pull-right text-muted small"><em>11:13 AM</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-warning fa-fw"></i> Server Not Responding
                                    <span class="pull-right text-muted small"><em>10:57 AM</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-shopping-cart fa-fw"></i> New Order Placed
                                    <span class="pull-right text-muted small"><em>9:49 AM</em>
                                    </span>
                        </a>
                        <a class="list-group-item" href="#">
                            <i class="fa fa-money fa-fw"></i> Payment Received
                                    <span class="pull-right text-muted small"><em>Yesterday</em>
                                    </span>
                        </a>
                    </div>
                    <!-- /.list-group -->
                    <a class="btn btn-default btn-block" href="#">View All Alerts</a>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> Donut Chart Example
                </div>
                <div class="panel-body">
                    <div id="morris-donut-chart"><svg height="377" version="1.1" width="274" xmlns="http://www.w3.org/2000/svg" style="overflow: hidden; position: relative; left: -0.333313px; top: -0.599976px;"><desc>Created with Raphaël 2.1.0</desc><defs/><path style="opacity: 0;" fill="none" stroke="#0b62a4" d="M137,275.6666666666667A84.66666666666667,84.66666666666667,0,0,0,217.09847945831342,218.43497827428675" stroke-width="2" opacity="0"/><path style="" fill="#0b62a4" stroke="#ffffff" d="M137,278.6666666666667A87.66666666666667,87.66666666666667,0,0,0,219.93661455723006,219.40708380369063L252.4174940226091,230.53229152909034A122,122,0,0,1,137,313Z" stroke-width="3"/><path style="opacity: 1;" fill="none" stroke="#3980b5" d="M217.09847945831342,218.43497827428675A84.66666666666667,84.66666666666667,0,0,0,61.065019305836884,153.5523945836116" stroke-width="2" opacity="1"/><path style="" fill="#3980b5" stroke="#ffffff" d="M219.93661455723006,219.40708380369063A87.66666666666667,87.66666666666667,0,0,0,58.374409753681505,152.2255109271254L23.097528958755333,134.82859187541743A127,127,0,0,1,257.14771918747016,232.15246741143014Z" stroke-width="3"/><path style="opacity: 0;" fill="none" stroke="#679dc6" d="M61.065019305836884,153.5523945836116A84.66666666666667,84.66666666666667,0,0,0,136.97340118263716,275.6666624885342" stroke-width="2" opacity="0"/><path style="" fill="#679dc6" stroke="#ffffff" d="M58.374409753681505,152.2255109271254A87.66666666666667,87.66666666666667,0,0,0,136.97245870485656,278.6666623404901L136.9616725702567,312.9999939795414A122,122,0,0,1,27.581878212347647,137.04006463622775Z" stroke-width="3"/><text style="text-anchor: middle; font: 800 15px &quot;Arial&quot;;" x="137" y="181" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#000000" font-size="15px" font-weight="800" transform="matrix(1.5551,0,0,1.5551,-76.049,-105.4694)" stroke-width="0.6430446194225722"><tspan dy="5">In-Store Sales</tspan></text><text style="text-anchor: middle; font: 14px &quot;Arial&quot;;" x="137" y="201" text-anchor="middle" font="10px &quot;Arial&quot;" stroke="none" fill="#000000" font-size="14px" transform="matrix(1.7639,0,0,1.7639,-104.6528,-147.4306)" stroke-width="0.5669291338582677"><tspan dy="5">30</tspan></text></svg></div>
                    <a class="btn btn-default btn-block" href="#">View Details</a>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
            <div class="chat-panel panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-comments fa-fw"></i>
                    Chat
                    <div class="btn-group pull-right">
                        <button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle" type="button">
                            <i class="fa fa-chevron-down"></i>
                        </button>
                        <ul class="dropdown-menu slidedown">
                            <li>
                                <a href="#">
                                    <i class="fa fa-refresh fa-fw"></i> Refresh
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-check-circle fa-fw"></i> Available
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-times fa-fw"></i> Busy
                                </a>
                            </li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-clock-o fa-fw"></i> Away
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="#">
                                    <i class="fa fa-sign-out fa-fw"></i> Sign Out
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <ul class="chat">
                        <li class="left clearfix">
                                    <span class="chat-img pull-left">
                                        <img class="img-circle" alt="User Avatar" src="http://placehold.it/50/55C1E7/fff">
                                    </span>
                            <div class="chat-body clearfix">
                                <div class="header">
                                    <strong class="primary-font">Jack Sparrow</strong>
                                    <small class="pull-right text-muted">
                                        <i class="fa fa-clock-o fa-fw"></i> 12 mins ago
                                    </small>
                                </div>
                                <p>
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.
                                </p>
                            </div>
                        </li>
                        <li class="right clearfix">
                                    <span class="chat-img pull-right">
                                        <img class="img-circle" alt="User Avatar" src="http://placehold.it/50/FA6F57/fff">
                                    </span>
                            <div class="chat-body clearfix">
                                <div class="header">
                                    <small class=" text-muted">
                                        <i class="fa fa-clock-o fa-fw"></i> 13 mins ago</small>
                                    <strong class="pull-right primary-font">Bhaumik Patel</strong>
                                </div>
                                <p>
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.
                                </p>
                            </div>
                        </li>
                        <li class="left clearfix">
                                    <span class="chat-img pull-left">
                                        <img class="img-circle" alt="User Avatar" src="http://placehold.it/50/55C1E7/fff">
                                    </span>
                            <div class="chat-body clearfix">
                                <div class="header">
                                    <strong class="primary-font">Jack Sparrow</strong>
                                    <small class="pull-right text-muted">
                                        <i class="fa fa-clock-o fa-fw"></i> 14 mins ago</small>
                                </div>
                                <p>
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.
                                </p>
                            </div>
                        </li>
                        <li class="right clearfix">
                                    <span class="chat-img pull-right">
                                        <img class="img-circle" alt="User Avatar" src="http://placehold.it/50/FA6F57/fff">
                                    </span>
                            <div class="chat-body clearfix">
                                <div class="header">
                                    <small class=" text-muted">
                                        <i class="fa fa-clock-o fa-fw"></i> 15 mins ago</small>
                                    <strong class="pull-right primary-font">Bhaumik Patel</strong>
                                </div>
                                <p>
                                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur bibendum ornare dolor, quis ullamcorper ligula sodales.
                                </p>
                            </div>
                        </li>
                    </ul>
                </div>
                <!-- /.panel-body -->
                <div class="panel-footer">
                    <div class="input-group">
                        <input type="text" placeholder="Type your message here..." class="form-control input-sm" id="btn-input">
                                <span class="input-group-btn">
                                    <button id="btn-chat" class="btn btn-warning btn-sm">
                                        Send
                                    </button>
                                </span>
                    </div>
                </div>
                <!-- /.panel-footer -->
            </div>
            <!-- /.panel .chat-panel -->
        </div>
        <!-- /.col-lg-4 -->
    </div>
    <!-- /.row -->

