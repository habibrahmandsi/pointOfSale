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

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length + " limitQty:" + limitQty);

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
        if (limitQty != null && limitQty > 0)
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
        if (limitQty != null && limitQty > 0)
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
    public Map<String, Object> getSales(Integer start, Integer length, String sortColName, String sortType, String searchKey, Long userId, int salesReturn, int unposted) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = "SELECT s.*, u.name userName "
                + "FROM sales s "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.total_amount > 0 AND s.sale_return = ? AND s.unposted = ? ";

        logger.debug("SMNLOG: searchKey:" + searchKey + " length:" + length);

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND sales_token_no LIKE ? "
                    + " OR sales_date LIKE ? "
                    + " OR total_amount LIKE ? "
                    + " OR discount LIKE ? "
                    + " OR u.name LIKE ? ";
        }
        if (userId > 0) {
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
        if (userId > 0) {
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

    public int getSalesCount(Long userId, int salesReturn, int unposted) throws Exception {
        String sql = "SELECT COUNT(*) FROM sales WHERE total_amount > 0 AND sale_return=" + salesReturn + " AND unposted = " + unposted;
        if (userId > 0)
            sql += " AND sale_by_id=" + userId;

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

        if (fromDate != null && toDate != null) {
            sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";
        } else if (fromDate != null && toDate == null)
            sql = sql + " AND s.sales_date = ? ";

        if (userId > 0) {
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
        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null)
            paramList.add(fromDate);

        if (userId > 0) {
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

    public int getUnpostedSalesCount(int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        String sql = "SELECT COUNT(*) "
                + "FROM sales s "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ? AND unposted = ? ";


        sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";

        if (userId > 0) {
            sql = sql + " AND s.sale_by_id = ? ";
        }

        List paramList = new ArrayList();
        paramList.add(salesReturn);
        paramList.add(unposted);

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }
        if (userId > 0) {
            paramList.add(userId);
        }

        logger.debug("SMNLOG:sql:" + sql);

        return jdbcTemplate.queryForInt(sql, paramList.toArray());
    }


    @Override
    public Map<String, Object> getSalesReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        Map<String, Object> result = new HashMap();

        String sql = this.getSaleReportSql(sortColName, sortType, searchKey, fromDate, toDate, userId);
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

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }
        if (!Utils.isEmpty(sortColName)) {
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

    public static String getSaleReportSql(String sortColName, String sortType, String searchKey, Date fromDate, Date toDate, Long userId) {
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
        if (fromDate != null)
            sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";

        if (userId > 0) {
            sql = sql + " AND s.sale_by_id = ? ";
        }
        if (!Utils.isEmpty(sortColName))
            sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";
        return sql;
    }


    @Override
    public int getSalesReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        String sql = this.getSaleReportSql("", "", searchKey, fromDate, toDate, userId);
        sql = sql.substring(sql.indexOf("FROM"));
        logger.debug("SMNLOG:sql 11:" + sql);
        sql = "SELECT COUNT(*) " + sql;
        logger.debug("SMNLOG:sql 22:" + sql);
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

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }
        logger.debug("SMNLOG:sql:" + sql);
        return jdbcTemplate.queryForInt(sql, paramList.toArray());
    }

    @Override
    public List getTotalSaleByDateAndUserId(Date fromDate, Date toDate, Long userId, int salesReturn, int unposted, int groupByDateOrUser) throws Exception {
        logger.debug("SMNLOG:fromDate:" + fromDate + " toDate:" + toDate + " userId:" + userId + " salesReturn:" + salesReturn);
        String sql = "SELECT SUM(si.total_price) totalSaleAmount,ns.nsDiscount totalDiscount,u.name userName "
                + "FROM sales s "
                + "JOIN sales_item si ON(s.id = si.sales_id) "
                + "LEFT JOIN (select ss.id, SUM(ss.discount) nsDiscount FROM sales ss "
                + "WHERE ss.sale_return = ? AND ss.unposted=? "
                + "AND ss.sales_date >= ? AND ss.sales_date <= ? "
                + (userId > 0 ? "AND ss.sale_by_id = ?" : "")
                + " GROUP BY ss.sale_by_id "
                + " ) ns "
                + "ON(ns.id = s.id) "
                + "JOIN product p ON(p.id = si.product_id) "
                + "LEFT JOIN company c ON(p.company_id = c.id) "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE s.sale_return = ? AND s.unposted=?";

        if (groupByDateOrUser == 1)// 0 = group by user and 1= group by date
            sql = sql.replace("SELECT", "SELECT DATE_FORMAT(s.sales_date, '%m/%d/%Y') sales_date,");

        sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";
        if (userId > 0) {
            sql = sql + " AND s.sale_by_id = ? ";
        }
        if (groupByDateOrUser == 0)// 0 = group by user and 1= group by date
            sql += " GROUP BY s.sale_by_id ";
        else
            sql += " GROUP BY DATE_FORMAT(s.sales_date,\"%m/%d/%Y\") ";

        logger.debug("SMNLOG:getTotalSaleByDateAndUserId: sql:" + sql);
        List paramList = new ArrayList();
        /* inner query param */
        paramList.add(salesReturn);
        paramList.add(unposted);
        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }
        if (userId > 0) {
            paramList.add(userId);
        }
        /* inner query param ends*/

        paramList.add(salesReturn);
        paramList.add(unposted);

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    @Override
    public int getIncomeReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        String sql = this.getSaleReportSql("", "", searchKey, fromDate, toDate, userId);
        sql = sql.substring(sql.indexOf("FROM"));
        logger.debug("SMNLOG:sql 11:" + sql);
        sql = "SELECT COUNT(*) " + sql;
        logger.debug("SMNLOG:sql 22:" + sql);
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

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }
        logger.debug("SMNLOG:sql:" + sql);
        return jdbcTemplate.queryForInt(sql, paramList.toArray());
    }

    @Override
    public List getTotalIncomeByDateAndUserId(Date fromDate, Date toDate, Long userId, int salesReturn, int unposted, int groupByDateOrUser) throws Exception {
        logger.debug("SMNLOG:fromDate:" + fromDate + " toDate:" + toDate + " userId:" + userId + " salesReturn:" + salesReturn);
        String sql = "SELECT SUM(si.total_price) totalSaleAmount,SUM(si.total_purchase_price) totalPurchasePrice,"
                + "SUM(si.benefit) benefit, ns.nsDiscount totalDiscount, u.name userName "
                + "FROM sales s "
                + "JOIN sales_item si ON(s.id = si.sales_id) "
                + "LEFT JOIN (select ss.id, SUM(ss.discount) nsDiscount FROM sales ss "
                + "WHERE ss.sale_return = ? AND ss.unposted=? "
                + "AND ss.sales_date >= ? AND ss.sales_date <= ? "
                + (userId > 0 ? "AND ss.sale_by_id = ?" : "")
                + " GROUP BY ss.sale_by_id "
                + " ) ns "
                + "ON(ns.id = s.id) "
                + "JOIN product p ON(p.id = si.product_id) "
                + "LEFT JOIN company c ON(p.company_id = c.id) "
                + "LEFT JOIN user u ON(u.id = s.sale_by_id) WHERE si.benefit IS NOT NULL AND s.sale_return = ? AND s.unposted=?";

        if (groupByDateOrUser == 1)// 0 = group by user and 1= group by date
            sql = sql.replace("SELECT", "SELECT s.sales_date,");

        sql = sql + " AND s.sales_date >= ? AND s.sales_date <= ? ";
        if (userId > 0) {
            sql = sql + " AND s.sale_by_id = ? ";
        }
        if (groupByDateOrUser == 0)// 0 = group by user and 1= group by date
            sql += " GROUP BY u.name ";
        else
            sql += " GROUP BY DATE_FORMAT(s.sales_date,\"%m/%d/%Y\") ";

        logger.debug("SMNLOG:sql:" + sql);
        List paramList = new ArrayList();
          /* inner query param */
        paramList.add(salesReturn);
        paramList.add(unposted);
        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }
        if (userId > 0) {
            paramList.add(userId);
        }
        /* inner query param ends*/
        paramList.add(salesReturn);
        paramList.add(unposted);

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }

        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }

    public Map getLatestPurchaseItemByProductId(Long productId) throws Exception {
        logger.debug("SMNLOG:------------------------------------");
        String sql = "Select * From purchase_item p WHERE p.product_id = ? ORDER BY p.id DESC";

        List param = new ArrayList();
        param.add(productId);

        List list = jdbcTemplate.queryForList(sql, param.toArray());
        if (list != null && list.size() > 0) {
            return (Map) list.get(0);
        }
        return null;

    }

    @Override
    public List getTotalPurchaseByDateAndUserId(Date fromDate, Date toDate, Long userId, int purchaseReturn, int unposted, int groupByDateOrUser, Long companyId) throws Exception {
        logger.debug("SMNLOG:fromDate:" + fromDate + " toDate:" + toDate + " userId:" + userId + " purchaseReturn:" + purchaseReturn + " unposted:" + unposted);
        String sql = "SELECT SUM(pi.total_price) totalPurchaseAmount,"
                + "ns.prrDiscount totalDiscount, u.name userName "
                + "FROM purchase pr "
                + "JOIN purchase_item pi ON(pr.id = pi.purchase_id) "
                + "LEFT JOIN (select prr.id, SUM(prr.discount) prrDiscount FROM purchase prr "
                + "JOIN purchase_item ppi ON(prr.id = ppi.purchase_id) "
                + "JOIN product pp ON(pp.id = ppi.product_id) "
                + "JOIN company cc ON(pp.company_id = cc.id) "
                + "WHERE prr.purchase_return = ? AND prr.unposted=? "
                + "AND prr.purchase_date >= ? AND prr.purchase_date <= ? "
                + (userId > 0 ? "AND prr.purchase_by_id = ?" : "")
                + (companyId > 0 ? "AND cc.id = ?" : "")
                + " GROUP BY prr.purchase_by_id "
                + " ) ns "
                + "ON(ns.id = pr.id) "
                + "JOIN product p ON(p.id = pi.product_id) "
                + "JOIN company c ON(p.company_id = c.id) "
                + "LEFT JOIN user u ON(u.id = pr.purchase_by_id) WHERE pr.purchase_return = ? AND pr.unposted=?";

        if (groupByDateOrUser == 1)// 0 = group by user and 1= group by date
            sql = sql.replace("SELECT", "SELECT DATE_FORMAT(pr.purchase_date, '%m/%d/%Y') purchase_date,");

        sql = sql + " AND pr.purchase_date >= ? AND pr.purchase_date <= ? ";

        if (userId > 0) {
            sql = sql + " AND pr.purchase_by_id = ? ";
        }

        if (companyId > 0) {
            sql = sql + " AND c.id = ? ";
        }

        if (groupByDateOrUser == 0)// 0 = group by user and 1= group by date
            sql += " GROUP BY u.name ";
        else
            sql += " GROUP BY DATE_FORMAT(pr.purchase_date,\"%m/%d/%Y\") ";

        logger.debug("SMNLOG:sql:" + sql);
        List paramList = new ArrayList();
          /* inner query param */
        paramList.add(purchaseReturn);
        paramList.add(unposted);
        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }
        if (userId > 0) {
            paramList.add(userId);
        }
        if (companyId > 0) {
            paramList.add(userId);
        }
        /* inner query param ends*/
        paramList.add(purchaseReturn);
        paramList.add(unposted);

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }
        if (companyId > 0) {
            paramList.add(companyId);
        }
        List list = jdbcTemplate.queryForList(sql, paramList.toArray());
        if (list != null && list.size() > 0)
            return list;
        return null;
    }


    @Override
    public Map<String, Object> getPurchaseReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        Map<String, Object> result = new HashMap();

        String sql = this.getPurchaseReportSql(sortColName, sortType, searchKey, fromDate, toDate, userId);
        List paramList = new ArrayList();
        paramList.add(purchaseReturn);
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

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }
        if (!Utils.isEmpty(sortColName)) {
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

    @Override
    public int getPurchaseReportCount(String searchKey, int purchaseReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception {
        String sql = this.getPurchaseReportSql("", "", searchKey, fromDate, toDate, userId);
        sql = sql.substring(sql.indexOf("FROM"));
        logger.debug("SMNLOG:sql 11:" + sql);
        sql = "SELECT COUNT(*) " + sql;
        logger.debug("SMNLOG:sql 22:" + sql);
        List paramList = new ArrayList();
        paramList.add(purchaseReturn);
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

        if (fromDate != null && toDate != null) {
            paramList.add(fromDate);
            paramList.add(toDate);
        } else if (fromDate != null && toDate == null) {
            paramList.add(fromDate);
            paramList.add(Utils.endOfDate(fromDate));
        }

        if (userId > 0) {
            paramList.add(userId);
        }
        logger.debug("SMNLOG:sql:" + sql);
        return jdbcTemplate.queryForInt(sql, paramList.toArray());
    }

    public static String getPurchaseReportSql(String sortColName, String sortType, String searchKey, Date fromDate, Date toDate, Long userId) {
        String sql = "SELECT pr.*,pi.quantity purchaseItemQty, pi.purchase_rate pItemPRate, pi.sale_rate pItemSaleRate "
                + ",pi.total_price pItemTotalPrice, u.name userName,p.name productName,c.name companyName "
                + "FROM purchase pr "
                + "JOIN purchase_item pi ON(pr.id = pi.purchase_id) "
                + "JOIN product p ON(p.id = pi.product_id) "
                + "LEFT JOIN company c ON(p.company_id = c.id) "
                + "LEFT JOIN user u ON(u.id = pr.purchase_by_id) WHERE pr.purchase_return = ? AND pr.unposted = ? ";

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND (purchase_token_no LIKE ?"
                    + " OR purchase_date LIKE ?"
                    + " OR pi.quantity LIKE ?"
                    + " OR pi.sale_rate LIKE ?"
                    + " OR pi.purchase_rate LIKE ?"
                    + " OR pi.total_price LIKE ?"
                    + " OR p.name LIKE ?"
                    + " OR c.name LIKE ?"
                    + " OR u.name LIKE ?)";
        }
        if (fromDate != null)
            sql = sql + " AND pr.purchase_date >= ? AND pr.purchase_date <= ? ";

        if (userId > 0) {
            sql = sql + " AND pr.purchase_by_id = ? ";
        }
        if (!Utils.isEmpty(sortColName))
            sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";
        return sql;
    }

    public void clearAllData() throws Exception {
        jdbcTemplate.update("DELETE FROM purchase_item WHERE id >0");
        jdbcTemplate.update("DELETE FROM purchase WHERE id >0");
        jdbcTemplate.update("DELETE FROM sales_item WHERE id >0");
        jdbcTemplate.update("DELETE FROM sales WHERE id >0");
        jdbcTemplate.update("UPDATE product set total_quantity = 0 WHERE id >0");
    }

    public Map getStockTotal(Date today, int purchaseReturn, int unposted) throws Exception {
        String sql = "SELECT SUM(purchase_rate*rest_quantity) tpRate,"
                + "SUM(sale_rate*rest_quantity) mrpRate "
                + "FROM purchase pr "
                + "LEFT JOIN purchase_item pi ON(pi.purchase_id = pr.id) "
                + "WHERE pr.purchase_return = ? AND pr.unposted = ? ";

        List paramList = new ArrayList();
        paramList.add(purchaseReturn);
        paramList.add(unposted);

        Map map = jdbcTemplate.queryForMap(sql,paramList.toArray());
        if (map != null) {
            return map;
        }
        return null;

    }

    public Map<String, Object> getStockReportDetails(Integer start, Integer length, String sortColName, String sortType, String searchKey, Long productId, Long companyId) throws Exception {
        Map<String, Object> result = new HashMap();
        String sql = this.getStockReportSql(sortColName, sortType, searchKey, productId, companyId);

        List paramList = new ArrayList();
        if (productId > 0)
            paramList.add(productId);
        if (companyId > 0)
            paramList.add(companyId);


        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
//            paramList.add(searchKey + "%");
//            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }
        if (!Utils.isEmpty(sortColName)) {
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

    public static String getStockReportSql(String sortColName, String sortType, String searchKey, Long productId, Long companyId) {
        String sql = "SELECT pi.quantity purchaseItemQty, pi.purchase_rate pItemPRate, "
                + "pi.sale_rate pItemSaleRate,pi.rest_quantity rest_quantity, "
                + "(pi.purchase_rate*pi.rest_quantity) tpRate, "
                + "(pi.sale_rate*pi.rest_quantity) mrpRate, "
                + "p.name productName,c.name companyName "
                + "FROM purchase pr "
                + "LEFT JOIN purchase_item pi ON(pi.purchase_id = pr.id) "
                + "JOIN product p ON(p.id = pi.product_id) "
//                + "LEFT JOIN company c ON(p.company_id = c.id) WHERE rest_quantity > 0 AND pr.purchase_return = 0";
                + "LEFT JOIN company c ON(p.company_id = c.id) WHERE pr.purchase_return = 0";

        if (productId > 0)
            sql += " AND p.id = ? ";
        if (companyId > 0)
            sql += " AND c.id = ? ";

        if (!Utils.isEmpty(searchKey)) {
            sql = sql + " AND (pi.quantity LIKE ?"
                    + " OR pi.sale_rate LIKE ?"
                    + " OR pi.purchase_rate LIKE ?"
                    + " OR pi.total_price LIKE ?"
                    + " OR p.name LIKE ?"
                    + " OR c.name LIKE ?)";
        }

        if (!Utils.isEmpty(sortColName))
            sql = sql + " ORDER BY " + sortColName + " " + sortType + " LIMIT ?, ? ";

        return sql;
    }

    @Override
    public int getStockReportCount(String searchKey, Long productId, Long companyId) throws Exception {
        String sql = this.getStockReportSql("", "", searchKey, productId, companyId);
        sql = sql.substring(sql.indexOf("FROM"));
        sql = "SELECT COUNT(*) " + sql;
        logger.debug("SMNLOG:sql:" + sql);
        List paramList = new ArrayList();
        if (productId > 0)
            paramList.add(productId);
        if (companyId > 0)
            paramList.add(companyId);


        if (!Utils.isEmpty(searchKey)) {
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
            paramList.add(searchKey + "%");
        }

        logger.debug("SMNLOG:sql:" + sql);
        return jdbcTemplate.queryForInt(sql, paramList.toArray());
    }

}
