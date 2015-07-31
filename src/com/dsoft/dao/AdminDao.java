package com.dsoft.dao;

import com.dsoft.entity.*;

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
}