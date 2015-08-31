package com.dsoft.dao;

import com.dsoft.entity.*;
import com.dsoft.service.AdminJdbcService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface AdminDao {

    List<User> getAllUserList();
    List<User> getAllUserList(String userName);
    int getEntitySize(String entity);
    void saveOrUpdateUser(User user);
    User getUser(Long userId) throws Exception;
    void deleteUser(User user) throws Exception;

    void saveOrUpdateProductGroup(ProductGroup productGroup) throws Exception;
    ProductGroup getProductGroup(Long productGroupId) throws Exception;
    void deleteProductGroup(ProductGroup productGroup) throws Exception;

    void saveOrUpdateProduct(Product product) throws Exception;
    Product getProduct(Long productId) throws Exception;
    void deleteProduct(Product product) throws Exception;

    void saveOrUpdateCompany(Company company) throws Exception;
    Company getCompany(Long companyId) throws Exception;
    void deleteCompany(Company company) throws Exception;

    void saveOrUpdateUnitOfMeasure(UnitOfMeasure unitOfMeasure) throws Exception;
    UnitOfMeasure getUnitOfMeasure(Long unitOfMeasureId) throws Exception;
    void deleteUnitOfMeasure(UnitOfMeasure unitOfMeasure) throws Exception;

    void saveOrUpdateProductType(ProductType productType) throws Exception;
    ProductType getProductType(Long productTypeId) throws Exception;
    void deleteProductType(ProductType productType) throws Exception;

    AbstractBaseEntity getAbstractBaseEntityByString(String className,String anyColumn,String columnValue);

    void saveOrUpdatePurchase(Purchase purchase) throws Exception;
    Purchase getPurchase(Long purchaseId, int purchaseReturn) throws Exception;
    void deletePurchase(Purchase purchase) throws Exception;

    List<PurchaseItem> getPurchaseItemListByPurchaseId(Long purchaseId) throws Exception;

    void deleteObject(Object object) throws Exception ;
    boolean saveOrUpdatePurchaseReturn(Purchase purchase) throws Exception;

    Company getCompanyByName(String companyName) throws Exception;
    ProductGroup getProductGroupByName(String name) throws Exception;

    void saveOrUpdateObject(Object object) throws Exception;
    ProductKeyValidation getActiveProductKeyValidation() throws Exception;

    void saveOrUpdateSales(Sales sales) throws Exception;
    Sales getSale(Long saleId, int salesReturn) throws Exception;
    void deleteSale(Sales sales) throws Exception;

    List<SalesItem> getSalesItemListBySalesId(Long salesId) throws Exception;
    void deleteSalesItem(List<SalesItem> salesItemList) throws Exception;
    boolean saveOrUpdateSalesReturn(Sales sales) throws Exception;

    void deletePurchaseItem(List<PurchaseItem> purchaseItemList, int purchaseReturn) throws Exception;
    void updateProductQuantity(Long productId, Double qty) throws Exception;
    void saveObject(Object object) throws Exception;
    SalesItem getSalesItem(Long id) throws Exception;

    void deleteSalesReturnItem(List<SalesItem> salesItemList) throws Exception;

    List<PurchaseItem> getPurchaseItemList(Long productId) throws Exception;
    List<Settings> getSettingsList() throws Exception;
    int getProductEntitySize(Double limitQty) throws Exception;
    List<Sales> getUnpostedSalesListByUserId(Long userId) throws Exception;

    Long savePurchaseItemAsPurchaseReturn(AdminJdbcService adminJdbcService, Product product, Double qty) throws Exception;
}
