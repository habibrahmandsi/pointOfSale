package com.dsoft.service;


import com.dsoft.entity.PurchaseItem;
import com.dsoft.entity.SearchBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdminJdbcService {

    List getPartialDataList( int page, int rp , String  qtype, String query, String sortname, String sortorder, String className);
    List getPartialDataListWithJoinQuery( int page, int rp , String  qtype, String query, String sortname, String sortorder, String className,String joinQuery);
    Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getProductGroups(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getProducts(Integer start, Integer length, String sortColName, String sortType, String searchKey, Double limitQty) throws Exception ;
    Map<String, Object> getCompanies(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getUnitOfMeasure(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getProductType(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;

    List getCompanyList(String name) throws Exception;
    List getProductTypeList(String name) throws Exception;
    List getProductGroupList(String name) throws Exception;
    List getUnitOfMeasureList(String name) throws Exception;

    void deleteEntityByAnyColValue(String tableName,String colName, String colValue) throws Exception;

    Map<String, Object> getPurchases(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn) throws Exception ;
    Map<String, Object> getSales(Integer start, Integer length, String sortColName, String sortType, String searchKey,Long userId, int salesReturn,int unposted) throws Exception ;
    int getSalesCount(Long userId,int salesReturn,int unposted) throws Exception;
    int getPurchaseCount(int purchaseReturn) throws Exception;
    Map<String, Object> getProductsForAutoComplete(String sortColName, String sortType, String searchKey) throws Exception ;

    Map<String, Object> getUnpostedSales(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception ;
    int getUnpostedSalesCount(int salesReturn, Date fromDate, Date toDate, Long userId, int unposted) throws Exception;

    Map<String, Object> getSalesReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception ;
    int getSalesReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception;
    List getTotalSaleByDateAndUserId(Date fromDate, Date toDate, Long userId,int salesReturn,int unposted,int groupByDateOrUser) throws Exception;

    int getIncomeReportCount(String searchKey, int salesReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception;
    List getTotalIncomeByDateAndUserId(Date fromDate, Date toDate, Long userId,int salesReturn,int unposted,int groupByDateOrUser) throws Exception;
    Map getLatestPurchaseItemByProductId(Long productId) throws Exception;

    List getTotalPurchaseByDateAndUserId(Date fromDate, Date toDate, Long userId,int purchaseReturn,int unposted,int groupByDateOrUser,Long companyId) throws Exception;

    Map<String, Object> getPurchaseReport(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception ;
    int getPurchaseReportCount(String searchKey, int purchaseReturn, Date fromDate, Date toDate, Long userId,int unposted) throws Exception;

    void clearAllData() throws Exception;
    Map getStockTotal(Date today, int purchaseReturn,int unposted) throws Exception;
    Map<String, Object> getStockReportDetails(Integer start, Integer length, String sortColName, String sortType, String searchKey, Long productId, Long companyId) throws Exception;
    int getStockReportCount(String searchKey,Long productId, Long companyId) throws Exception;
}