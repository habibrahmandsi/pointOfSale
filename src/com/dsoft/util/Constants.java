package com.dsoft.util;

public class Constants {

    public static final String MESSAGE_FILE_PATH = "../configurations/messages/messages_en.properties";
    public static final String APPLICATION_CONFIGURATION_FILE_PATH = "../configurations/properties/database.properties";
    public static final String MONTH_DAY_YEAR_UNDER_SCORE="MMddyyyy";
    public static final String USER="User";
    public static final String USER_TABLE="user";
    public static final String STUDENT_CLASS="Student";
    public static final String STUDENT_TABLE="student";
    public static final String STUDENT_TABLE_JOIN_WITH_PROFILE_QUERY="LEFT JOIN standard ON(student.standard_id = standard.id)";
    public static final String TUITION_FEE_CLASS="TuitionFee";
    public static final String TUITION_FEE_TABLE="tuition_fee";
    public static final String SERVER_URL="http://localhost";
    public static final String DEFAULT_SIMPLE_DATE_FORMAT="dd-MM-yyyy";
    public static final String MM_DD_YYYY_FORMAT="MM-dd-yyyy";
    public static final String DATE_FORMAT="dd/MM/yyyy";
    public static final String DATE_FORMAT_JS="dd/mm/yyyy";
    public static final String IDISPLAY_LENGTH = "iDisplayLength";
    public static final String IDISPLAY_START= "iDisplayStart";
    public static final String sEcho= "sEcho";
    public static final String iSortCOL= "iSortCol_0";
    public static final String sSearch= "sSearch";
    public static final String sortType= "sSortDir_0";
    public static final String FILE_PATH= "tmp/";

}
