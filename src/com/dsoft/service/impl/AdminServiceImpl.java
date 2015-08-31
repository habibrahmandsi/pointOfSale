package com.dsoft.service.impl;

import com.dsoft.dao.AdminDao;
import com.dsoft.entity.*;
import com.dsoft.service.AdminJdbcService;
import com.dsoft.service.AdminService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("adminService")
public class AdminServiceImpl implements AdminService {

    private static Logger logger = Logger.getLogger(AdminServiceImpl.class);

    @Autowired(required = true)
    @Qualifier("adminDao")
    private AdminDao adminDao;

    @Override
    public List<User> getAllUserList() {

        return adminDao.getAllUserList();
    }

    @Override
    public List<User> getAllUserList(String userName) {
        return null;
    }

    public int getEntitySize(String entity) {
        return adminDao.getEntitySize(entity);

    }

    public void saveOrUpdateUser(User user) {
        adminDao.saveOrUpdateUser(user);

    }

    public User getUser(Long userId) throws Exception {
        return adminDao.getUser(userId);
    }

    public void deleteUser(User user) throws Exception {
        adminDao.deleteUser(user);
    }

    public void saveOrUpdateProductGroup(ProductGroup productGroup) throws Exception {
        adminDao.saveOrUpdateProductGroup(productGroup);

    }

    public ProductGroup getProductGroup(Long productGroupId) throws Exception {
        return adminDao.getProductGroup(productGroupId);
    }

    public void deleteProductGroup(ProductGroup productGroup) throws Exception {
        adminDao.deleteProductGroup(productGroup);
    }

    public void saveOrUpdateProduct(Product product) throws Exception {
        adminDao.saveOrUpdateProduct(product);

    }

    public Product getProduct(Long productId) throws Exception {
        return adminDao.getProduct(productId);
    }

    public void deleteProduct(Product product) throws Exception {
        adminDao.deleteProduct(product);
    }

    public void saveOrUpdateCompany(Company company) throws Exception {
        adminDao.saveOrUpdateCompany(company);

    }

    public Company getCompany(Long companyId) throws Exception {
        return adminDao.getCompany(companyId);
    }

    public void deleteCompany(Company company) throws Exception {
        adminDao.deleteCompany(company);
    }

    public void saveOrUpdateUnitOfMeasure(UnitOfMeasure unitOfMeasure) throws Exception {
        adminDao.saveOrUpdateUnitOfMeasure(unitOfMeasure);

    }

    public UnitOfMeasure getUnitOfMeasure(Long unitOfMeasureId) throws Exception {
        return adminDao.getUnitOfMeasure(unitOfMeasureId);
    }

    public void deleteUnitOfMeasure(UnitOfMeasure unitOfMeasure) throws Exception {
        adminDao.deleteUnitOfMeasure(unitOfMeasure);
    }

    public void saveOrUpdateProductType(ProductType productType) throws Exception {
        adminDao.saveOrUpdateProductType(productType);

    }

    public ProductType getProductType(Long productTypeId) throws Exception {
        return adminDao.getProductType(productTypeId);
    }

    public void deleteProductType(ProductType productType) throws Exception {
        adminDao.deleteProductType(productType);
    }


    @Override
    public AbstractBaseEntity getAbstractBaseEntityByString(String className, String anyColumn, String columnValue) {
        return adminDao.getAbstractBaseEntityByString(className, anyColumn, columnValue);
    }

    public void saveOrUpdatePurchase(Purchase purchase) throws Exception {
        adminDao.saveOrUpdatePurchase(purchase);

    }

    public Purchase getPurchase(Long purchaseId, int purchaseReturn) throws Exception {
        return adminDao.getPurchase(purchaseId, purchaseReturn);
    }

    public void deletePurchase(Purchase purchase) throws Exception {
        adminDao.deletePurchase(purchase);
    }

    public List<PurchaseItem> getPurchaseItemListByPurchaseId(Long purchaseId) throws Exception {
        return adminDao.getPurchaseItemListByPurchaseId(purchaseId);
    }

    public void deleteObject(Object object) throws Exception {
        adminDao.deleteObject(object);
    }

    public boolean saveOrUpdatePurchaseReturn(Purchase purchase) throws Exception {
        return adminDao.saveOrUpdatePurchaseReturn(purchase);
    }

    public Company getCompanyByName(String companyName) throws Exception {
        return adminDao.getCompanyByName(companyName);
    }

    public ProductGroup getProductGroupByName(String name) throws Exception {
        return adminDao.getProductGroupByName(name);
    }

    public void saveOrUpdateObject(Object object) throws Exception {
        adminDao.saveOrUpdateObject(object);
    }

    public ProductKeyValidation getActiveProductKeyValidation() throws Exception {
        return adminDao.getActiveProductKeyValidation();
    }


    public void saveOrUpdateSales(Sales sales) throws Exception {
        adminDao.saveOrUpdateSales(sales);
    }

    public Sales getSale(Long saleId, int salesReturn) throws Exception {
        return adminDao.getSale(saleId, salesReturn);
    }

    public void deleteSale(Sales sales) throws Exception {
        adminDao.deleteSale(sales);
    }

    public List<SalesItem> getSalesItemListBySalesId(Long salesId) throws Exception {
        return adminDao.getSalesItemListBySalesId(salesId);
    }

    public void deleteSalesItem(List<SalesItem> salesItemList) throws Exception {
        adminDao.deleteSalesItem(salesItemList);
    }

    public boolean saveOrUpdateSalesReturn(Sales sales) throws Exception {
        return adminDao.saveOrUpdateSalesReturn(sales);
    }

    public void deletePurchaseItem(List<PurchaseItem> purchaseItemList, int purchaseReturn) throws Exception {
        adminDao.deletePurchaseItem(purchaseItemList, purchaseReturn);
    }

    public void updateProductQuantity(Long productId, Double qty) throws Exception {
        adminDao.updateProductQuantity(productId, qty);
    }

    public void saveObject(Object object) throws Exception {
        adminDao.saveObject(object);
    }

    public SalesItem getSalesItem(Long id) throws Exception {
        return adminDao.getSalesItem(id);
    }

    public void deleteSalesReturnItem(List<SalesItem> salesItemList) throws Exception {
        adminDao.deleteSalesReturnItem(salesItemList);
    }


    public List<PurchaseItem> getPurchaseItemList(Long productId) throws Exception {
        return adminDao.getPurchaseItemList(productId);
    }

    public List<Settings> getSettingsList() throws Exception {
        return adminDao.getSettingsList();
    }

    public int getProductEntitySize(Double limitQty) throws Exception {
        return adminDao.getProductEntitySize(limitQty);
    }


    public List<Sales> getUnpostedSalesListByUserId(Long userId) throws Exception {
        return adminDao.getUnpostedSalesListByUserId(userId);
    }

    public Long savePurchaseItemAsPurchaseReturn(AdminJdbcService adminJdbcService, Product product, Double qty) throws Exception {
        return adminDao.savePurchaseItemAsPurchaseReturn(adminJdbcService, product, qty);
    }

}
