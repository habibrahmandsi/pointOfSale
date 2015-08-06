package com.dsoft.service;


import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdminJdbcService {

    List getPartialDataList( int page, int rp , String  qtype, String query, String sortname, String sortorder, String className);
    List getPartialDataListWithJoinQuery( int page, int rp , String  qtype, String query, String sortname, String sortorder, String className,String joinQuery);
    Map<String, Object> getUsers(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getProductGroups(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getProducts(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getCompanies(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getUnitOfMeasure(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;
    Map<String, Object> getProductType(Integer start, Integer length, String sortColName, String sortType, String searchKey) throws Exception ;

    List getCompanyList(String name) throws Exception;
    List getProductTypeList(String name) throws Exception;
    List getProductGroupList(String name) throws Exception;
    List getUnitOfMeasureList(String name) throws Exception;

    void deleteEntityByAnyColValue(String tableName,String colName, String colValue) throws Exception;

    Map<String, Object> getPurchases(Integer start, Integer length, String sortColName, String sortType, String searchKey, int purchaseReturn) throws Exception ;
    Map<String, Object> getSales(Integer start, Integer length, String sortColName, String sortType, String searchKey, int salesReturn) throws Exception ;

}