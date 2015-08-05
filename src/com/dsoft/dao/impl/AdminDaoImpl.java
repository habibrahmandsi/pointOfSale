package com.dsoft.dao.impl;

import com.dsoft.dao.AdminDao;
import com.dsoft.entity.*;
import com.dsoft.util.Constants;
import com.dsoft.util.Utils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.DoubleBuffer;
import java.util.*;


@Repository("adminDao")
@Transactional
public class AdminDaoImpl implements AdminDao {

    private static Logger logger = Logger.getLogger(AdminDaoImpl.class);

    private HibernateTemplate hibernateTemplate;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Autowired
    private SessionFactory sessionFactory;

    protected final Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }


    @Override
    public List<User> getAllUserList() {
        return hibernateTemplate.find("FROM User");
    }

    @Override
    public List<User> getAllUserList(String userName) {
        return null;
    }

    public int getEntitySize(String entity) {
        Session session = getSession();
        List list = session.createQuery("Select count(*) From " + entity).list();

        if (list != null && list.size() > 0) {
            return Integer.parseInt((list.get(0)).toString());

        }
        return 0;

    }

    public void saveOrUpdateUser(User user) {
        hibernateTemplate.saveOrUpdate(user);
    }

    public User getUser(Long userId) throws Exception {
        Session session = getSession();
        String sql = "FROM User WHERE id = :id ";
        if (!Utils.isInRole(Role.SUPER_ADMIN.getLabel())) {
            logger.debug("SMNLOG:This is super Admin");
            sql = "FROM User WHERE role != :roleName AND id = :id ";
        }

        Query query = session.createQuery(sql);

        if (!Utils.isInRole(Role.SUPER_ADMIN.getLabel()))
            query.setParameter("roleName", Role.SUPER_ADMIN.getLabel());

        query.setParameter("id", userId);

        Object object = query.uniqueResult();
        if (object != null)
            return (User) object;
        return null;
    }

    public void deleteUser(User user) throws Exception {
        hibernateTemplate.delete(user);
    }


    public void saveOrUpdateProductGroup(ProductGroup productGroup) throws Exception {
        hibernateTemplate.saveOrUpdate(productGroup);

    }

    public ProductGroup getProductGroup(Long productGroupId) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM ProductGroup WHERE id = :id");
        query.setParameter("id", productGroupId);
        Object object = query.uniqueResult();
        if (object != null)
            return (ProductGroup) object;
        return null;
    }

    public void deleteProductGroup(ProductGroup productGroup) throws Exception {
        hibernateTemplate.delete(productGroup);
    }

    public void saveOrUpdateProduct(Product product) throws Exception {
        hibernateTemplate.saveOrUpdate(product);

    }

    public Product getProduct(Long productId) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM Product WHERE id = :id");
        query.setParameter("id", productId);
        Object object = query.uniqueResult();
        if (object != null)
            return (Product) object;
        return null;
    }

    public void deleteProduct(Product product) throws Exception {
        hibernateTemplate.delete(product);
    }

    public void saveOrUpdateCompany(Company company) throws Exception {
        hibernateTemplate.saveOrUpdate(company);

    }

    public Company getCompany(Long companyId) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM Company WHERE id = :id");
        query.setParameter("id", companyId);
        Object object = query.uniqueResult();
        if (object != null)
            return (Company) object;
        return null;
    }

    public void deleteCompany(Company company) throws Exception {
        hibernateTemplate.delete(company);
    }

    public void saveOrUpdateUnitOfMeasure(UnitOfMeasure unitOfMeasure) throws Exception {
        hibernateTemplate.saveOrUpdate(unitOfMeasure);

    }

    public UnitOfMeasure getUnitOfMeasure(Long unitOfMeasureId) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM UnitOfMeasure WHERE id = :id");
        query.setParameter("id", unitOfMeasureId);
        Object object = query.uniqueResult();
        if (object != null)
            return (UnitOfMeasure) object;
        return null;
    }

    public void deleteUnitOfMeasure(UnitOfMeasure productType) throws Exception {
        hibernateTemplate.delete(productType);
    }

    public void saveOrUpdateProductType(ProductType productType) throws Exception {
        hibernateTemplate.saveOrUpdate(productType);

    }

    public ProductType getProductType(Long productTypeId) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM ProductType WHERE id = :id");
        query.setParameter("id", productTypeId);
        Object object = query.uniqueResult();
        if (object != null)
            return (ProductType) object;
        return null;
    }

    public void deleteProductType(ProductType productType) throws Exception {
        hibernateTemplate.delete(productType);
    }

    public AbstractBaseEntity getAbstractBaseEntityByString(String className, String anyColumn, String columnValue) {

        List<AbstractBaseEntity> entityList = hibernateTemplate.find("From " + className + " where " + anyColumn + " = ?", columnValue);
        if (entityList != null && entityList.size() > 0)
            return entityList.get(0);
        return null;
    }

    public void saveOrUpdatePurchase(Purchase purchase) throws Exception {
        hibernateTemplate.saveOrUpdate(purchase);
        Product product;
        List<PurchaseItem> purchaseItemList = purchase.getPurchaseItemList() != null ? purchase.getPurchaseItemList() : new ArrayList<PurchaseItem>();
        if (!Utils.isEmpty(purchaseItemList)) {
            for (PurchaseItem purchaseItem : purchaseItemList) {
                if (purchaseItem != null && purchaseItem.getId() != null) {
                    updateObject(purchaseItem);
                } else {
                    purchaseItem.setPurchase(purchase);
                    saveObject(purchaseItem);
                }
                product = purchaseItem.getProduct();
                logger.debug("SMNLOG:PrevQuantity" + purchaseItem.getPrevQuantity() + " current Qty:" + purchaseItem.getQuantity());
                if (purchaseItem.getPrevQuantity() > purchaseItem.getQuantity() || purchaseItem.getPrevQuantity() < purchaseItem.getQuantity()) {
                    this.updateProductQuantity(product.getId(), purchaseItem.getQuantity() - purchaseItem.getPrevQuantity());
                } else {
                    logger.debug("######## No nedd to update product quantity ##########");
                }
            }
        }

    }

    public Purchase getPurchase(Long purchaseId, int purchaseReturn) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM Purchase WHERE id = :id");
        query.setParameter("id", purchaseId);
        Object object = query.uniqueResult();
        if (object != null)
            return (Purchase) object;
        return null;
    }

    public void deletePurchase(Purchase purchase) throws Exception {
        hibernateTemplate.delete(purchase);
    }

    public void saveObject(Object object) {
        hibernateTemplate.save(object);
    }

    public void updateObject(Object object) {
        hibernateTemplate.update(object);
    }

    public List<PurchaseItem> getPurchaseItemListByPurchaseId(Long purchaseId) throws Exception {
        return hibernateTemplate.find("FROM PurchaseItem where purchase.id = ?", purchaseId);
    }

    public void deleteObject(Object object) throws Exception {
        hibernateTemplate.delete(object);
    }

    public void updateProductQuantity(Long productId, Double quantity) throws Exception {
        logger.debug("::****************** Update Product quantity ******************");
        logger.debug("::productId:" + productId + " to add:" + quantity);
        Product product = null;
        Session session = getSession();
        Query query = session.createQuery("FROM Product WHERE id = :id");
        query.setParameter("id", productId);
        Object object = query.uniqueResult();
        if (object != null)
            product = (Product) object;

        if (product != null) {
            Double oldQty = product.getTotalQuantity() != null ? product.getTotalQuantity() : 0;
            product.setTotalQuantity(oldQty + quantity);
            hibernateTemplate.update(product);
        }
    }

    public boolean saveOrUpdatePurchaseReturn(Purchase purchase) throws Exception {
        hibernateTemplate.save(purchase);
        Product product;
        List<PurchaseItem> purchaseItemList = purchase.getPurchaseItemList();
        int count = 0;
        if (!Utils.isEmpty(purchaseItemList)) {
            for (PurchaseItem purchaseItem : purchaseItemList) {
                if (purchaseItem != null && purchaseItem.getId() != null && (purchaseItem.getPrevQuantity() > purchaseItem.getQuantity() || purchaseItem.getPrevQuantity() < purchaseItem.getQuantity())) {
                    logger.debug("SMNLOG:Purchase return item should be saved");
                    purchaseItem.setId(null);// to add as a purchase return
                    purchaseItem.setPurchase(purchase);
                    saveObject(purchaseItem);
                } else {
                    // purchaseItem.setPurchase(purchase);
                    // saveObject(purchaseItem);
                }
                product = purchaseItem.getProduct();
                logger.debug("SMNLOG:PrevQuantity" + purchaseItem.getPrevQuantity() + " current Qty:" + purchaseItem.getQuantity());
                if (purchaseItem.getPrevQuantity() > purchaseItem.getQuantity() || purchaseItem.getPrevQuantity() < purchaseItem.getQuantity()) {
                    purchaseItem.setQuantity(purchaseItem.getQuantity() * (-1));
                    this.updateProductQuantity(product.getId(), purchaseItem.getQuantity()); // negative for return
                    count = count+1;
                } else {
                    logger.debug("######## (purchase return) No need to update product quantity ##########");
                }
            }
        }
        if(count == 0){
            logger.debug("######## (purchase return) Nothing to save: ROLL backing ##########");
            hibernateTemplate.delete(purchase);
            return false;
        }
        return true;
    }


    public Company getCompanyByName(String companyName) throws Exception{
        Session session = getSession();
        Query query = session.createQuery("FROM Company WHERE name = :name");
        query.setParameter("name", companyName);
        Object object = query.uniqueResult();
        if (object != null)
            return (Company) object;
        return null;
    }

  public ProductGroup getProductGroupByName(String name) throws Exception{
        Session session = getSession();
        Query query = session.createQuery("FROM ProductGroup WHERE name = :name");
        query.setParameter("name", name);
        Object object = query.uniqueResult();
        if (object != null)
            return (ProductGroup) object;
        return null;
    }

    public void saveOrUpdateObject(Object object) throws Exception{
        hibernateTemplate.saveOrUpdate(object);
    }

    public ProductKeyValidation getActiveProductKeyValidation() throws Exception{
        Session session = getSession();
        Query query = session.createQuery("FROM ProductKeyValidation WHERE active = :active");
        query.setParameter("active", true);
        Object object = query.uniqueResult();
        if (object != null)
            return (ProductKeyValidation) object;
        return null;
    }

}