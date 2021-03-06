package com.dsoft.service.impl;

import com.dsoft.dao.AdminJdbcDao;
import com.dsoft.entity.*;
import com.dsoft.service.AdminJdbcService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("adminJdbcService")
public class AdminJdbcServiceImpl implements AdminJdbcService {

    private static Logger logger = Logger.getLogger(AdminJdbcServiceImpl.class);
    @Autowired(required = true)
    private AdminJdbcDao adminJdbcDao;


    public List getPartialDataList( int page, int rp , String qtype, String query, String sortname, String sortorder,String className) {
        return  adminJdbcDao.getPartialDataList( page, rp ,qtype, query,   sortname,   sortorder,className);
    }

    @Override
    public List getPartialDataListWithJoinQuery(int page, int rp, String qtype, String query, String sortname, String sortorder, String className, String joinQuery) {
        return adminJdbcDao.getPartialDataListWithJoinQuery(page, rp, qtype, query, sortname, sortorder, className, joinQuery);
    }

    @Override
    public Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        return adminJdbcDao.getUsers(start, length, sortColName, sortType, searchKey);
    }

    @Override
    public Map<String, Object> getProductGroups(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        return adminJdbcDao.getProductGroups(start, length, sortColName, sortType, searchKey);
    }

    @Override
    public Map<String, Object> getProducts(Integer start, Integer length, String sortColName, String sortType, String searchKey, Double limitQty) throws Exception{
        return adminJdbcDao.getProducts(start, length, sortColName, sortType, searchKey, limitQty);
    }

    @Override
    public Map<String, Object> getCompanies(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        return adminJdbcDao.getCompanies(start, length, sortColName, sortType, searchKey);
    }

    @Override
    public Map<String, Object> getUnitOfMeasure(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        return adminJdbcDao.getUnitOfMeasure(start, length, sortColName, sortType, searchKey);
    }

    @Override
    public Map<String, Object> getProductType(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception{
        return adminJdbcDao.getProductType(start, length, sortColName, sortType, searchKey);
    }

    public List getCompanyList( String name) throws Exception{
        return  adminJdbcDao.getCompanyList(name);
    }
    public List getProductTypeList( String name) throws Exception{
        return  adminJdbcDao.getProductTypeList(name);
    }
    public List getProductGroupList( String name) throws Exception{
        return  adminJdbcDao.getProductGroupList(name);
    }
    public List getUnitOfMeasureList( String name) throws Exception{
        return  adminJdbcDao.getUnitOfMeasureList(name);
    }

    public void deleteEntityByAnyColValue(String tableName,String colName, String colValue) throws Exception{
        adminJdbcDao.deleteEntityByAnyColValue(tableName, colName, colValue);
    }

    @Override
    public Map<String, Object> getPurchases(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn) throws Exception {
        return adminJdbcDao.getPurchases(start, length, sortColName, sortType, searchKey, purchaseReturn);
    }

   @Override
    public Map<String, Object> getSales(Integer start, Integer length, String sortColName, String sortType, String searchKey,Long userId, int salesReturn,int unposted) throws Exception {
        return adminJdbcDao.getSales(start, length, sortColName, sortType, searchKey,userId, salesReturn, unposted);
    }

    public int getSalesCount(Long userId,int salesReturn,int unposted) throws Exception{
        return adminJdbcDao.getSalesCount(userId,salesReturn, unposted);
    }

   public int getPurchaseCount(int purchaseReturn) throws Exception{
        return adminJdbcDao.getPurchaseCount(purchaseReturn);
    }

    @Override
    public Map<String, Object> getProductsForAutoComplete(String sortColName, String sortType, String searchKey) throws Exception{
        return adminJdbcDao.getProductsForAutoComplete(sortColName, sortType, searchKey);
    }

    @Override
    public Map<String, Object> getUnpostedSales(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception{
        return adminJdbcDao.getUnpostedSales(start, length, sortColName, sortType, searchKey, salesReturn, fromDate, toDate, userId, unposted);
    }

    public int getUnpostedSalesCount(int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception{
        return adminJdbcDao.getUnpostedSalesCount(salesReturn, fromDate, toDate, userId, unposted);
    }


    @Override
    public Map<String, Object> getSalesReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn,Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        return adminJdbcDao.getSalesReport(start, length, sortColName, sortType, searchKey, salesReturn, fromDate, toDate, userId,unposted);
    }

    @Override
    public int getSalesReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        return adminJdbcDao.getSalesReportCount(searchKey, salesReturn, fromDate, toDate, userId,unposted);
    }

    @Override
    public List getTotalSaleByDateAndUserId(Date fromDate, Date toDate, Long userId,int salesReturn,int unposted,int groupByDateOrUser) throws Exception{
        return adminJdbcDao.getTotalSaleByDateAndUserId(fromDate, toDate, userId,salesReturn,unposted,groupByDateOrUser);
    }

    @Override
    public int getIncomeReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        return adminJdbcDao.getIncomeReportCount(searchKey, salesReturn, fromDate, toDate, userId, unposted);
    }

    @Override
    public List getTotalIncomeByDateAndUserId(Date fromDate, Date toDate, Long userId,int salesReturn,int unposted,int groupByDateOrUser) throws Exception{
        return adminJdbcDao.getTotalIncomeByDateAndUserId(fromDate, toDate, userId, salesReturn, unposted,groupByDateOrUser);
    }

    public Map getLatestPurchaseItemByProductId(Long productId) throws Exception{
        return adminJdbcDao.getLatestPurchaseItemByProductId(productId);
    }

    @Override
    public List getTotalPurchaseByDateAndUserId(Date fromDate, Date toDate, Long userId,int purchaseReturn,int unposted,int groupByDateOrUser,Long companyId) throws Exception{
        return adminJdbcDao.getTotalPurchaseByDateAndUserId(fromDate, toDate, userId, purchaseReturn, unposted,groupByDateOrUser,companyId);
    }



    @Override
    public   Map<String, Object> getPurchaseReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        return adminJdbcDao.getPurchaseReport(start, length, sortColName, sortType, searchKey, purchaseReturn, fromDate, toDate, userId, unposted);
    }

    @Override
    public int getPurchaseReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception{
        return adminJdbcDao.getPurchaseReportCount(searchKey, salesReturn, fromDate, toDate, userId, unposted);
    }

    @Override
    public  void clearAllData() throws Exception{
        adminJdbcDao.clearAllData();
    }

    @Override
    public Map getStockTotal(Date today, int purchaseReturn,int unposted) throws Exception{
        return adminJdbcDao.getStockTotal(today, purchaseReturn, unposted);
    }

    public Map<String, Object> getStockReportDetails(Integer start, Integer length, String sortColName, String sortType, String searchKey, Long productId, Long companyId) throws Exception{
        return adminJdbcDao.getStockReportDetails(start, length, sortColName, sortType, searchKey, productId, companyId);
    }

    public int getStockReportCount(String searchKey,Long productId, Long companyId) throws Exception{
        return adminJdbcDao.getStockReportCount(searchKey,productId,companyId);
    }

}
