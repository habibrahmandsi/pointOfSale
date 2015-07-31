package com.dsoft.dao.impl;

/**
 * Created with IntelliJ IDEA.
 * User: habib
 * Date: 7/22/13
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */

import com.dsoft.dao.AdminJdbcDao;
import com.dsoft.entity.*;
import com.dsoft.util.Constants;
import com.dsoft.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class AdminJdbcDaoImpl implements AdminJdbcDao {
    private static Logger logger = Logger.getLogger(AdminJdbcDaoImpl.class);
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Autowired(required = true)
    public void setJdbcDataSource(DataSource jdbcDataSource) {
        this.jdbcTemplate = new JdbcTemplate(jdbcDataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcDataSource);
    }


/*
    public List getControlIdList(String controlIds,String controlCompareString) {
        String clause = "";
        if (Utils.getLoggedUserRoleName().equals(Role.IA_ANALYST.getLabel())) {
            clause = " AND rp.assign_to ='"+Utils.getLoggedUserName()+"' ";
        }
        if(Utils.getLoggedUserRoleName().equals(Role.IA_MANAGER.getLabel())){
           clause = " AND rcnd.decision = 'Further Action Required' ";
        }

        logger.debug("ControlIds:"+controlIds+" controlCompareString:"+controlCompareString+" clause:"+clause);

        String sql = "SELECT GROUP_CONCAT( pcnd.control_ids ) control_ids,"
                + " GROUP_CONCAT( proactive_transaction_id ) transaction_ids"
//                + ", GROUP_CONCAT(region.id) region_ids"
                + " FROM"
                + " proactive_transaction_CND pcnd"
                + " JOIN proactive_transaction ptx ON(ptx.id = pcnd.proactive_transaction_id)"
                + " JOIN proactive_project pp ON(pp.id = ptx.proactive_project_id)"
//                + " JOIN region ON(region.id = pp.region_id)"
                + " WHERE pcnd.control_ids IS NOT NULL "
                + (Utils.getLoggedUserRoleName().equals(Role.IA_MANAGER.getLabel())? " AND pcnd.decision = 'Further Action Required' " : "")
//                + " AND pcnd.control_ids > '' "
                + (!"0".equals(controlIds)? (!Utils.isEmpty(controlCompareString)? controlCompareString : ""):" AND pcnd.control_ids > '' " )
                + " UNION ALL"
                + " SELECT GROUP_CONCAT( rcnd.control_ids ) control_ids,"
                + " GROUP_CONCAT( reactive_transaction_id ) transaction_ids "
//                + ", GROUP_CONCAT(region.id) region_ids"
                + " FROM "
                + " reactive_transaction_CND rcnd  "
                + " JOIN reactive_transaction rtx ON(rtx.id = rcnd.reactive_transaction_id)"
                + " JOIN reactive_project rp ON(rp.id = rtx.reactive_project_id)"
//                + " JOIN region ON(region.id = rp.region_id)"
                + " WHERE rcnd.control_ids IS NOT NULL "
                + (Utils.getLoggedUserRoleName().equals(Role.IA_MANAGER.getLabel())? " AND rcnd.decision = 'Further Action Required' " : "")
//                + " AND rcnd.control_ids > '' "
                + (!"0".equals(controlIds)? (!Utils.isEmpty(controlCompareString)? controlCompareString : ""):"  AND rcnd.control_ids > '' " )
                + " UNION ALL"
                + " SELECT GROUP_CONCAT( rcnd.control_ids ) control_ids,"
                + " GROUP_CONCAT( real_time_transaction_id ) transaction_ids"
//                + ", GROUP_CONCAT(region.id) region_ids"
                + " FROM "
                + " real_time_transaction_CND rcnd  "
                + " JOIN real_time_transaction rtx ON(rtx.id = rcnd.real_time_transaction_id)"
                + " JOIN real_time_project rp ON(rp.id = rtx.real_time_project_id)"
//                + " JOIN region ON(region.id = rp.region_id)"
                + " WHERE rcnd.control_ids IS NOT NULL "
                + clause
//                + " AND rcnd.control_ids > '' "
                + (!"0".equals(controlIds)? (!Utils.isEmpty(controlCompareString)? controlCompareString : ""):" AND rcnd.control_ids > '' " );
        logger.debug("SMN LOG:SQL:"+sql);
        return jdbcTemplate.queryForList(sql);
    }
*/
public List<Object> getPartialDataList(int page, int rp,  String qtype, String query, String sortname, String sortorder,String className) {
    int start = (page - 1)*rp ;
    String sql = "SELECT * "
            + " FROM " + className;
    if(!Utils.isEmpty(query)) {
        sql +=  " WHERE "+ qtype+" LIKE ?  LIMIT ?, ? ";
    } else {
        sql +=  " ORDER BY " + sortname + " "+ sortorder +" LIMIT ?, ? ";
    }

    List paramList = new ArrayList();
    if(!Utils.isEmpty(query)) {
        paramList.add("%"+query+"%");
    }
    paramList.add(start);
    paramList.add(rp);
    List list = jdbcTemplate.queryForList(sql, paramList.toArray());

    if (list != null && list.size() > 0)
        return list;
    return null;
}

    @Override
    public List getPartialDataListWithJoinQuery(int page, int rp, String qtype, String query, String sortname, String sortorder, String className, String joinQuery) {
        int start = (page - 1)*rp ;
        String sql = "SELECT * ,"+className+".id "+className+"_id"
                + " FROM " + className;

        if(!Utils.isEmpty(joinQuery))
            sql = sql+" "+joinQuery;

        if(!Utils.isEmpty(query)) {
            sql +=  " WHERE "+ qtype+" LIKE ?  LIMIT ?, ? ";
        } else {
            sql +=  " ORDER BY " + sortname + " "+ sortorder +" LIMIT ?, ? ";
        }

        List paramList = new ArrayList();
        if(!Utils.isEmpty(query)) {
            paramList.add("%"+query+"%");
        }
        paramList.add(start);
        paramList.add(rp);
        List list = jdbcTemplate.queryForList(sql, paramList.toArray());

        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    public Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM user";
        logger.debug("SMNLOG: searchKey:"+searchKey);

        if(!Utils.isEmpty(searchKey)){
            sql = sql+" WHERE name LIKE ? "
                    +"OR user_name LIKE ? "
                    +"OR father_name LIKE ? "
                    +"OR mother_name LIKE ? "
                    +"OR national_id_no LIKE ? "
                    +"OR address LIKE ? "
                    +"OR max_discount_percent LIKE ? "
                    +"OR age LIKE ? "
                    +"OR email LIKE ?";
        }

        sql = sql+" ORDER BY " + sortColName + " "+ sortType +" LIMIT ?, ? ";

        List paramList = new ArrayList();
        if(!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
        result.put("total" ,list.size());
        else
            result.put("total" ,0);


        return result;
    }

    public Map<String, Object> getProductGroups(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM product_group";
        logger.debug("SMNLOG: searchKey:"+searchKey);

        if(!Utils.isEmpty(searchKey)){
            sql = sql+" WHERE name LIKE ? ";
        }

        sql = sql+" ORDER BY " + sortColName + " "+ sortType +" LIMIT ?, ? ";

        List paramList = new ArrayList();
        if(!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey+"%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
        result.put("total" ,list.size());
        else
            result.put("total" ,0);


        return result;
    }

    public Map<String, Object> getProducts(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        Map<String, Object> result = new HashMap();
        String sql = "SELECT "
                     +"p.id productId,p.name productName, p.sale_rate, p.purchase_rate, "
                     +"pg.name groupName, pt.name typeName, uom.name uomName,cm.name companyName "
                     +"FROM product p "
                     +"LEFT JOIN company cm ON(cm.id = p.company_id) "
                     +"LEFT JOIN product_group pg ON(pg.id = p.product_group_id) "
                     +"LEFT JOIN product_type pt ON(pt.id = p.product_type_id) "
                     +"LEFT JOIN unit_of_measure uom ON(uom.id = p.unit_of_measure_id) ";

        logger.debug("SMNLOG: searchKey:"+searchKey);

        if(!Utils.isEmpty(searchKey)){
            sql = sql+" WHERE p.name LIKE ? "
                    +"OR pg.name LIKE ? "
                    +"OR pt.name LIKE ? "
                    +"OR uom.name LIKE ? "
                    +"OR cm.name LIKE ? "
                    +"OR p.sale_rate LIKE ? "
                    +"OR p.purchase_rate LIKE ? "
            ;
        }

        sql = sql+" ORDER BY " + sortColName + " "+ sortType +" LIMIT ?, ? ";

        List paramList = new ArrayList();
        if(!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
        result.put("total" ,list.size());
        else
            result.put("total" ,0);
        return result;
    }

    public Map<String, Object> getCompanies(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM company";
        logger.debug("SMNLOG: searchKey:"+searchKey);

        if(!Utils.isEmpty(searchKey)){
            sql = sql+" WHERE name LIKE ? "
                    +"OR agent_name LIKE ? "
                    +"OR agent_cell_no LIKE ? "
                    +"OR permanent_address LIKE ? ";
        }

        sql = sql+" ORDER BY " + sortColName + " "+ sortType +" LIMIT ?, ? ";

        List paramList = new ArrayList();
        if(!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
            paramList.add(searchKey+"%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
        result.put("total" ,list.size());
        else
            result.put("total" ,0);


        return result;
    }

    public Map<String, Object> getUnitOfMeasure(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM unit_of_measure";
        logger.debug("SMNLOG: searchKey:"+searchKey);

        if(!Utils.isEmpty(searchKey)){
            sql = sql+" WHERE name LIKE ? ";
        }

        sql = sql+" ORDER BY " + sortColName + " "+ sortType +" LIMIT ?, ? ";

        List paramList = new ArrayList();
        if(!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey+"%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
        result.put("total" ,list.size());
        else
            result.put("total" ,0);


        return result;
    }

    public Map<String, Object> getProductType(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM product_type";
        logger.debug("SMNLOG: searchKey:"+searchKey);

        if(!Utils.isEmpty(searchKey)){
            sql = sql+" WHERE name LIKE ? ";
        }

        sql = sql+" ORDER BY " + sortColName + " "+ sortType +" LIMIT ?, ? ";

        List paramList = new ArrayList();
        if(!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey+"%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
        result.put("total" ,list.size());
        else
            result.put("total" ,0);


        return result;
    }

    public List getCompanyList( String name) throws Exception{
        String sql = "SELECT *  FROM company";
        if(!Utils.isEmpty(name)){
            sql = sql+" WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if(!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;

    }
    public List getProductTypeList( String name) throws Exception{
        String sql = "SELECT *  FROM product_type";
        if(!Utils.isEmpty(name)){
            sql = sql+" WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if(!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }
    public List getProductGroupList( String name) throws Exception{
        String sql = "SELECT *  FROM product_group";
        if(!Utils.isEmpty(name)){
            sql = sql+" WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if(!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }
    public List getUnitOfMeasureList( String name) throws Exception{
        String sql = "SELECT *  FROM unit_of_measure";
        if(!Utils.isEmpty(name)){
            sql = sql+" WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if(!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:"+sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

}
