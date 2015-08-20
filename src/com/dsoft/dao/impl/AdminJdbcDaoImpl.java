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
    public List<Object> getPartialDataList(int page, int rp, String qtype, String query, String sortname, String sortorder, String className) {
        int start = (page - 1) * rp;
        String sql = "SELECT * "
                + " FROM " + className;
        if (!Utils.isEmpty(query)) {
            sql += " WHERE " + qtype + " LIKE ?  LIMIT ?, ? ";
        } else {
            sql += " ORDER BY " + sortname + " " + sortorder + " LIMIT ?, ? ";
        }

        List paramList = new ArrayList();
        if (!Utils.isEmpty(query)) {
            paramList.add("%" + query + "%");
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
        int start = (page - 1) * rp;
        String sql = "SELECT * ," + className + ".id " + className + "_id"
                + " FROM " + className;

        if (!Utils.isEmpty(joinQuery))
            sql = sql + " " + joinQuery;

        if (!Utils.isEmpty(query)) {
            sql += " WHERE " + qtype + " LIKE ?  LIMIT ?, ? ";
        } else {
            sql += " ORDER BY " + sortname + " " + sortorder + " LIMIT ?, ? ";
        }

        List paramList = new ArrayList();
        if (!Utils.isEmpty(query)) {
            paramList.add("%" + query + "%");
        }
        paramList.add(start);
        paramList.add(rp);
        List list = jdbcTemplate.queryForList(sql, paramList.toArray());

        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    public Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM user";

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " WHERE name LIKE ? "
                    + "OR user_name LIKE ? "
                    + "OR father_name LIKE ? "
                    + "OR mother_name LIKE ? "
                    + "OR national_id_no LIKE ? "
                    + "OR address LIKE ? "
                    + "OR max_discount_percent LIKE ? "
                    + "OR age LIKE ? "
                    + "OR email LIKE ?";
        }
        logger.debug("SMNLOG:-----_>" + Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()));
        if (!Utils.isEmpty(searchKey) && !Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel())) {
            sql += " AND role != ? ";
        } else if (Utils.isEmpty(searchKey) && !Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel())) {
            sql += " WHERE role != ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        if (!Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel())) {
            paramList.add(Role.ROLE_SUPER_ADMIN.getLabel());
        }

        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public Map<String, Object> getProductGroups(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM product_group";
        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " WHERE name LIKE ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());

        result.put("data", list);
        if (list != null && list.size() > 0) {
            logger.debug("SMNLOG:list Size:" + list.size());
            result.put("total", list.size());
        } else
            result.put("total", 0);


        return result;
    }

    public Map<String, Object> getProducts(Integer start, Integer length, String sortColName, String sortType, String searchKey, Double limitQty) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT "
                + "p.id productId,p.name productName, p.sale_rate, p.purchase_rate,p.total_quantity, "
                + "pg.name groupName, pt.name typeName, uom.name uomName,cm.name companyName "
                + "FROM product p "
                + "LEFT JOIN company cm ON(cm.id = p.company_id) "
                + "LEFT JOIN product_group pg ON(pg.id = p.product_group_id) "
                + "LEFT JOIN product_type pt ON(pt.id = p.product_type_id) "
                + "LEFT JOIN unit_of_measure uom ON(uom.id = p.unit_of_measure_id) WHERE 1=1 ";

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length+" limitQty:"+limitQty);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND (p.name LIKE ? "
                    + "OR pg.name LIKE ? "
                    + "OR pt.name LIKE ? "
                    + "OR uom.name LIKE ? "
                    + "OR cm.name LIKE ? "
                    + "OR p.sale_rate LIKE ? "
                    + "OR p.purchase_rate LIKE ? "
                    + "OR p.total_quantity LIKE ? ) "
            ;
        }
        if(limitQty != null && limitQty > 0)
            sql = sql + " AND p.total_quantity <= ? ";

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        if(limitQty != null && limitQty > 0)
            paramList.add(limitQty);


        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);
        return result;
    }

    public Map<String, Object> getCompanies(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM company";
        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " WHERE name LIKE ? "
                    + "OR agent_name LIKE ? "
                    + "OR agent_cell_no LIKE ? "
                    + "OR permanent_address LIKE ? "
                    + "OR company_address LIKE ? "
                    + "OR company_cell_no LIKE ? "
                    + "OR company_email LIKE ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public Map<String, Object> getUnitOfMeasure(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM unit_of_measure";
        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " WHERE name LIKE ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public Map<String, Object> getProductType(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT *  FROM product_type";
        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " WHERE name LIKE ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public List getCompanyList(String name) throws Exception {
        String sql = "SELECT *  FROM company";
        if (!Utils.isEmpty(name)) {
            sql = sql + " WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if (!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;

    }

    public List getProductTypeList(String name) throws Exception {
        String sql = "SELECT *  FROM product_type";
        if (!Utils.isEmpty(name)) {
            sql = sql + " WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if (!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    public List getProductGroupList(String name) throws Exception {
        String sql = "SELECT *  FROM product_group";
        if (!Utils.isEmpty(name)) {
            sql = sql + " WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if (!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    public List getUnitOfMeasureList(String name) throws Exception {
        String sql = "SELECT *  FROM unit_of_measure";
        if (!Utils.isEmpty(name)) {
            sql = sql + " WHERE name LIKE ? ";
        }


        List paramList = new ArrayList();
        if (!Utils.isEmpty(name)) {
            paramList.add(name);
        }
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }


    public void deleteEntityByAnyColValue(String tableName, String colName, String colValue) throws Exception {
        String sql = "DELETE FROM " + tableName + " WHERE " + colName + " = ?";
        List paramList = new ArrayList();
        paramList.add(colValue);
        jdbcTemplate.update(sql, paramList.toArray());

    }

    @Override
    public Map<String, Object> getPurchases(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT p.*, u.name userName "
                + "FROM purchase p "
                + "LEFT JOIN user u ON(u.id = p.purchase_by_id) WHERE p.purchase_return = ?";

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND purchase_token_no LIKE ? "
                    + " OR purchase_date LIKE ? "
                    + " OR total_amount LIKE ? "
                    + " OR u.name LIKE ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        paramList.add(purchaseReturn);
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    @Override
    public Map<String, Object> getSales(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT s.*, u.name userName "
                + "FROM sales s "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ?";

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND sales_token_no LIKE ? "
                    + " OR sales_date LIKE ? "
                    + " OR total_amount LIKE ? "
                    + " OR discount LIKE ? "
                    + " OR u.name LIKE ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        paramList.add(salesReturn);
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public int getSalesCount(int salesReturn) throws Exception {
        String sql = "SELECT COUNT(*) FROM sales WHERE sale_return=" + salesReturn;
        return jdbcTemplate.queryForInt(sql);
    }

    public int getPurchaseCount(int purchaseReturn) throws Exception {
        String sql = "SELECT COUNT(*) FROM purchase WHERE purchase_return=" + purchaseReturn;
        return jdbcTemplate.queryForInt(sql);
    }

    public Map<String, Object> getProductsForAutoComplete(String sortColName, String sortType, String searchKey) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT "
                + "p.id productId,p.name productName, p.sale_rate saleRate, p.purchase_rate purchaseRate,p.total_quantity totalQuantity "
                + ",cm.name companyName "
//                + ", pg.name groupName, pt.name typeName, uom.name uomName,cm.name companyName "
                + "FROM product p "
                + "LEFT JOIN company cm ON(cm.id = p.company_id) ";
//                + "LEFT JOIN product_group pg ON(pg.id = p.product_group_id) "
//                + "LEFT JOIN product_type pt ON(pt.id = p.product_type_id) "
//                + "LEFT JOIN unit_of_measure uom ON(uom.id = p.unit_of_measure_id) ";

        logger.debug("SMNLOG: searchKey:" + searchKey);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " WHERE p.name LIKE ? "
//                    + "OR pg.name LIKE ? "
//                    + "OR pt.name LIKE ? "
//                    + "OR uom.name LIKE ? "
//                    + "OR cm.name LIKE ? "
//                    + "OR p.sale_rate LIKE ? "
//                    + "OR p.purchase_rate LIKE ? "
//                    + "OR p.total_quantity LIKE ? "
            ;
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType;

        List paramList = new ArrayList();
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");

        }
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);
        return result;
    }


    @Override
    public Map<String, Object> getUnpostedSales(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT s.*, u.name userName "
                + "FROM sales s "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ? AND unposted = ? ";

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND sales_token_no LIKE ? "
                    + " OR sales_date LIKE ? "
                    + " OR total_amount LIKE ? "
                    + " OR discount LIKE ? "
                    + " OR u.name LIKE ? ";
        }

        if(fromDate != null && toDate != null){
            sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";
        }else if(fromDate != null && toDate == null)
            sql = sql + " AND s.sales_date = ? ";

        if(userId > 0){
            sql = sql + " AND s.sale_by_id = ? ";
        }

        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        List paramList = new ArrayList();
        paramList.add(salesReturn);
        paramList.add(unposted);
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        if(fromDate != null && toDate != null){
            paramList.add(fromDate);
            paramList.add(toDate);
        }else if(fromDate != null && toDate == null)
            paramList.add(fromDate);

        if(userId > 0){
            paramList.add(userId);
        }

        paramList.add(start);
        paramList.add(length);
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public int getUnpostedSalesCount(int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception{
        String sql = "SELECT COUNT(*) "
                + "FROM sales s "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ? AND unposted = ? ";


        sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";

        if(userId > 0){
            sql = sql + " AND s.sale_by_id = ? ";
        }

        List paramList = new ArrayList();
        paramList.add(salesReturn);
        paramList.add(unposted);

        if(fromDate != null && toDate != null){
            paramList.add(fromDate);
            paramList.add(toDate);
        }else if(fromDate != null && toDate == null){
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }
        if(userId > 0){
            paramList.add(userId);
        }

        logger.debug("SMNLOG:sql:" + sql);

        return jdbcTemplate.queryForInt(sql,paramList.toArray());
    }


    @Override
    public Map<String, Object> getSalesReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        Map<String, Object> result = new HashMap();

        String sql = this.getSaleReportSql(sortColName, sortType, searchKey,fromDate, toDate, userId);
        List paramList = new ArrayList();
        paramList.add(salesReturn);
        paramList.add(unposted);
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }

        if(fromDate != null && toDate != null){
            paramList.add(fromDate);
            paramList.add(toDate);
        }else if(fromDate != null && toDate == null){
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if(userId > 0){
            paramList.add(userId);
        }
        if(!Utils.isEmpty(sortColName)){
            paramList.add(start);
            paramList.add(length);
        }
        logger.debug("SMNLOG:sql:" + sql);

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
//        logger.debug("SMNLOG:list:"+list);

        result.put("data", list);
        if (list != null && list.size() > 0)
            result.put("total", list.size());
        else
            result.put("total", 0);


        return result;
    }

    public static String getSaleReportSql(String sortColName, String sortType, String searchKey,Date fromDate, Date toDate, Long userId){
        String sql = "SELECT s.*,si.quantity saleItemQty, si.purchase_rate sItemPRate, si.sales_rate sItemSaleRate "
                + ",si.total_price sItemTotalPrice, u.name userName,p.name productName,c.name companyName "
                + "FROM sales s "
                + "JOIN sales_item si ON(s.id = si.sales_id) "
                + "JOIN product p ON(p.id = si.product_id) "
                + "LEFT JOIN company c ON(p.company_id = c.id) "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ? AND s.unposted = ? ";

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND (sales_token_no LIKE ?"
                    + " OR sales_date LIKE ?"
                    + " OR si.quantity LIKE ?"
                    + " OR si.purchase_rate LIKE ?"
                    + " OR si.sales_rate LIKE ?"
                    + " OR si.total_price LIKE ?"
                    + " OR p.name LIKE ?"
                    + " OR c.name LIKE ?"
                    + " OR u.name LIKE ?)";
        }

        sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";

        if(userId > 0){
            sql = sql + " AND s.sale_by_id = ? ";
        }
        if(!Utils.isEmpty(sortColName))
        sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";
        return sql;
    }


    @Override
    public int getSalesReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        String sql = this.getSaleReportSql("", "", searchKey,fromDate, toDate, userId);
        sql = sql.substring(sql.indexOf("FROM"));
        logger.debug("SMNLOG:sql 11:"+sql);
        sql = "SELECT COUNT(*) "+sql;
        logger.debug("SMNLOG:sql 22:"+sql);
        List paramList = new ArrayList();
        paramList.add(salesReturn);
        paramList.add(unposted);
        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }

        if(fromDate != null && toDate != null){
            paramList.add(fromDate);
            paramList.add(toDate);
        }else if(fromDate != null && toDate == null){
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if(userId > 0){
            paramList.add(userId);
        }
        logger.debug("SMNLOG:sql:" + sql);
        return jdbcTemplate.queryForInt(sql, paramList.toArray());
    }

    @Override
    public List getTotalSaleByDateAndUserId(Date fromDate, Date toDate, Long userId,int salesReturn,int unposted) throws Exception{
        logger.debug("SMNLOG:fromDate:"+fromDate+" toDate:"+toDate+" userId:"+userId+" salesReturn:"+salesReturn);
        String sql = "SELECT SUM(si.total_price) totalSaleAmount,u.name userName "
                + "FROM sales s "
                + "JOIN sales_item si ON(s.id = si.sales_id) "
                + "JOIN product p ON(p.id = si.product_id) "
                + "LEFT JOIN company c ON(p.company_id = c.id) "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ? AND s.unposted=?";


        sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";
        if(userId > 0){
            sql = sql + " AND s.sale_by_id = ? ";
        }
        sql +=" GROUP BY u.name ";

        logger.debug("SMNLOG:sql:" + sql);
        List paramList = new ArrayList();
        paramList.add(salesReturn);
        paramList.add(unposted);

        if(fromDate != null && toDate != null){
            paramList.add(fromDate);
            paramList.add(toDate);
        }else if(fromDate != null && toDate == null){
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if(userId > 0){
            paramList.add(userId);
        }

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if(list != null && list.size() > 0)
            return list;
        return null;
    }

}
