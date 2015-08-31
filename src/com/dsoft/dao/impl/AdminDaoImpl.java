package com.dsoft.dao.impl;

import com.dsoft.dao.AdminDao;
import com.dsoft.dao.AdminJdbcDao;
import com.dsoft.entity.*;
import com.dsoft.service.AdminJdbcService;
import com.dsoft.service.AdminService;
import com.dsoft.util.Constants;
import com.dsoft.util.Utils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
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

    @Autowired(required = true)
    private AdminJdbcService adminJdbcService;

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
//        return hibernateTemplate.find("FROM User WHERE role != ?", Role.ROLE_SUPER_ADMIN.getLabel());
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
        if (!Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel())) {
            logger.debug("SMNLOG:This is super Admin");
            sql = "FROM User WHERE role != :roleName AND id = :id ";
        }

        Query query = session.createQuery(sql);

        if (!Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))
            query.setParameter("roleName", Role.ROLE_SUPER_ADMIN.getLabel());

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
        Map pItem;
        Product product;
        List<PurchaseItem> purchaseItemList = purchase.getPurchaseItemList() != null ? purchase.getPurchaseItemList() : new ArrayList<PurchaseItem>();
        if (!Utils.isEmpty(purchaseItemList)) {
            for (PurchaseItem purchaseItem : purchaseItemList) {
                if (purchaseItem != null && purchaseItem.getId() != null) {
                    updateObject(purchaseItem);
                } else {
                    purchaseItem.setPurchase(purchase);
                    purchaseItem.setRestQuantity(purchaseItem.getQuantity()); //initially this two are equals
                    saveObject(purchaseItem);
                }
                product = this.getProduct(purchaseItem.getProduct().getId());
                product.setPurchaseRate(purchaseItem.getPurchaseRate());
                product.setSaleRate(purchaseItem.getSaleRate());
                this.updateObject(product); // to update purchase rate and sale rate

                logger.debug("SMNLOG:PrevQuantity" + purchaseItem.getPrevQuantity() + " current Qty:" + purchaseItem.getQuantity());
                if (purchaseItem.getPrevQuantity() > purchaseItem.getQuantity() || purchaseItem.getPrevQuantity() < purchaseItem.getQuantity()) {
                    this.updateProductQuantity(product.getId(), purchaseItem.getQuantity() - purchaseItem.getPrevQuantity());
                } else {
                    logger.debug("######## No need to update product quantity ##########");
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
                if (purchaseItem != null && purchaseItem.getId() != null) {
                    updateObject(purchaseItem);
                } else {
                    purchaseItem.setPurchase(purchase);
                    saveObject(purchaseItem);
                }
                product = purchaseItem.getProduct();
                logger.debug("SMNLOG:PrevQuantity" + purchaseItem.getPrevQuantity() + " current Qty:" + purchaseItem.getQuantity());
                if (purchaseItem.getQuantity() > 0) {
                    this.updateProductQuantity(product.getId(), (-1) * (purchaseItem.getQuantity()));
                    count = count + 1;
                } else {
                    logger.debug("######## No need to update product quantity ##########");
                }
            }
        }

        /*if (!Utils.isEmpty(purchaseItemList)) {
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
                    count = count + 1;
                } else {
                    logger.debug("######## (purchase return) No need to update product quantity ##########");
                }
            }
        }*/
        if (count == 0) {
            logger.debug("######## (purchase return) Nothing to save: ROLL backing ##########");
            hibernateTemplate.delete(purchase);
            return false;
        }
        return true;
    }


    public Company getCompanyByName(String companyName) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM Company WHERE name = :name");
        query.setParameter("name", companyName);
        Object object = query.uniqueResult();
        if (object != null)
            return (Company) object;
        return null;
    }

    public ProductGroup getProductGroupByName(String name) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM ProductGroup WHERE name = :name");
        query.setParameter("name", name);
        Object object = query.uniqueResult();
        if (object != null)
            return (ProductGroup) object;
        return null;
    }

    public void saveOrUpdateObject(Object object) throws Exception {
        hibernateTemplate.saveOrUpdate(object);
    }

    public ProductKeyValidation getActiveProductKeyValidation() throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM ProductKeyValidation WHERE active = :active");
        query.setParameter("active", true);
        Object object = query.uniqueResult();
        if (object != null)
            return (ProductKeyValidation) object;
        return null;
    }


    public void saveOrUpdateSales(Sales sales) throws Exception {
        hibernateTemplate.saveOrUpdate(sales);
        Product product;
        List<SalesItem> salesItemList = sales.getSalesItemList() != null ? sales.getSalesItemList() : new ArrayList<SalesItem>();
        if (!Utils.isEmpty(salesItemList)) {
            for (SalesItem salesItem : salesItemList) {
                if (salesItem != null && salesItem.getId() != null) {
                    updateObject(salesItem);
                } else {
                    salesItem.setSales(sales);
                    saveObject(salesItem);
                }
                product = salesItem.getProduct();
                logger.debug("SMNLOG:PrevQuantity" + salesItem.getPrevQuantity() + " current Qty:" + salesItem.getQuantity());
                if (salesItem.getPrevQuantity() > salesItem.getQuantity() || salesItem.getPrevQuantity() < salesItem.getQuantity()) {
                    this.updateProductQuantity(product.getId(), (salesItem.getQuantity() - salesItem.getPrevQuantity()) * (-1));// as to deduct from totl qty
                } else {
                    logger.debug("######## sales :: No nedd to update product quantity ##########");
                }
            }
        }

    }


    public Sales getSale(Long saleId, int salesReturn) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM Sales WHERE id = :id");
        query.setParameter("id", saleId);
        Object object = query.uniqueResult();
        if (object != null)
            return (Sales) object;
        return null;
    }

    public void deleteSale(Sales sales) throws Exception {
        hibernateTemplate.delete(sales);
    }

    public List<SalesItem> getSalesItemListBySalesId(Long salesId) throws Exception {
        return hibernateTemplate.find("FROM SalesItem where sales.id = ?", salesId);
    }

    public void deleteSalesItem(List<SalesItem> salesItemList) throws Exception {
        SalesItem salesItem = null;
        if (salesItemList != null && salesItemList.size() > 0) {
            for (int i = 0; i < salesItemList.size(); i++) {
                salesItem = salesItemList.get(i);
                logger.debug("SMNLOG:salesItem---:" + salesItem);
                if (salesItem != null && salesItem.getProduct() != null) {
                    this.updateProductQuantity(salesItem.getProduct().getId(), salesItem.getQuantity());// as to add in total qty
                }

                this.deleteObject(salesItem);
            }
        }

    }

    public boolean saveOrUpdateSalesReturn(Sales sales) throws Exception {
        hibernateTemplate.save(sales);
        Product product;
        List<SalesItem> salesItemList = sales.getSalesItemList();
        PurchaseItem purchaseItem = new PurchaseItem();
        int count = 0;
        if (!Utils.isEmpty(salesItemList)) {
            for (SalesItem salesItem : salesItemList) {
                product = salesItem != null ? salesItem.getProduct(): new Product();
                if (salesItem != null && salesItem.getId() != null && (salesItem.getPrevQuantity() > salesItem.getQuantity() || salesItem.getPrevQuantity() < salesItem.getQuantity())) {
                    logger.debug("SMNLOG:Sales return item should be saved");
                    Long purchaseItemId = this.savePurchaseItemAsPurchaseReturn(adminJdbcService, product,salesItem.getQuantity());
                    salesItem.setId(null);// to add as a sales return
                    salesItem.setSales(sales);
                    if(purchaseItemId != null && purchaseItemId > 0)
                        salesItem.setPurchaseItemId(purchaseItemId);

                    saveObject(salesItem);
                } else {
                    // salesItem.setSales(sales);
                    // saveObject(salesItem);
                }

                logger.debug("SMNLOG:PrevQuantity" + salesItem.getPrevQuantity() + " current Qty:" + salesItem.getQuantity());
                if (salesItem.getPrevQuantity() > salesItem.getQuantity() || salesItem.getPrevQuantity() < salesItem.getQuantity()) {
                    this.updateProductQuantity(product.getId(), salesItem.getQuantity()); // positive for return
                    count = count + 1;
                } else {
                    logger.debug("######## (sales return) No need to update product quantity ##########");
                }
            }
        }
        if (count == 0) {
            logger.debug("######## (sales return) Nothing to save: ROLL backing ##########");
            hibernateTemplate.delete(sales);
            return false;
        }
        return true;
    }

    public void deletePurchaseItem(List<PurchaseItem> purchaseItemList, int purchaseReturn) throws Exception {
        logger.debug(":: DELETE PURCHASE ITEM ::");
        PurchaseItem purchaseItem = null;
        if (purchaseItemList != null && purchaseItemList.size() > 0) {
            for (int i = 0; i < purchaseItemList.size(); i++) {
                purchaseItem = purchaseItemList.get(i);
                logger.debug("SMNLOG:purchaseItem---:" + purchaseItem);
                if (purchaseItem != null && purchaseItem.getProduct() != null) {
                    logger.debug("SMNLOG:purchaseReturn:" + purchaseReturn);
                    if (purchaseReturn == 0) {
                        logger.debug("SMNLOG:-------if:");
                        this.updateProductQuantity(purchaseItem.getProduct().getId(), purchaseItem.getQuantity() * (-1));// as to subtract in total qty
                    } else {
                        logger.debug("SMNLOG:-------else if:");
                        this.updateProductQuantity(purchaseItem.getProduct().getId(), purchaseItem.getQuantity());// as to add in total qty
                    }

                }

                this.deleteObject(purchaseItem);
            }
        }
    }

    public SalesItem getSalesItem(Long id) throws Exception {
        Session session = getSession();
        Query query = session.createQuery("FROM SalesItem WHERE id = :id");
        query.setParameter("id", id);
        Object object = query.uniqueResult();
        if (object != null)
            return (SalesItem) object;
        return null;
    }


    public void deleteSalesReturnItem(List<SalesItem> salesItemList) throws Exception {
        SalesItem salesItem = null;
        if (salesItemList != null && salesItemList.size() > 0) {
            for (int i = 0; i < salesItemList.size(); i++) {
                salesItem = salesItemList.get(i);
                logger.debug("SMNLOG:salesItem---:" + salesItem);
                if (salesItem != null && salesItem.getProduct() != null) {
                    this.updateProductQuantity(salesItem.getProduct().getId(), (-1) * salesItem.getQuantity());// as to subtract in total qty
                }
                if(salesItem.getPurchaseItemId() != null && salesItem.getPurchaseItemId() > 0){
                    adminJdbcService.deleteEntityByAnyColValue("purchase_item","id",salesItem.getPurchaseItemId()+"");
                }
                this.deleteObject(salesItem);
            }
        }

    }

    public List<PurchaseItem> getPurchaseItemList(Long productId) throws Exception {
        return hibernateTemplate.find("FROM PurchaseItem where product.id = ? AND restQuantity > 0", productId);
    }

    public List<Settings> getSettingsList() throws Exception {
        return hibernateTemplate.find("FROM Settings");
    }

    public int getProductEntitySize(Double limitQty) throws Exception {
        Session session = getSession();
        String sql = "Select count(*) From Product";
        if (limitQty != null && limitQty > 0)
            sql += " WHERE totalQuantity <= :limitQty";
        Query query = session.createQuery(sql);

        if (limitQty != null && limitQty > 0)
            query.setParameter("limitQty", limitQty);

        List list = query.list();

        if (list != null && list.size() > 0) {
            return Integer.parseInt((list.get(0)).toString());

        }
        return 0;

    }

    public List<Sales> getUnpostedSalesListByUserId(Long userId) throws Exception {
        //Session session = getSession();
//        String sql = "FROM Sales WHERE totalQuantity = :tqty AND unposted=:unposted AND user.id=:userId";
        return hibernateTemplate.find("FROM Sales where totalAmount = 0 AND user.id = ?", userId);
/*
        Query query = session.createQuery(sql);
        query.setParameter("tqty", 0);
        query.setParameter("unposted", true);
        query.setParameter("userId", userId);

        List list = query.list();

        if (list != null && list.size() > 0) {
            return list;

        }
        return null;*/

    }

    public Long savePurchaseItemAsPurchaseReturn(AdminJdbcService adminJdbcService, Product product, Double qty) throws Exception {
        PurchaseItem purchaseItem = new PurchaseItem();
        Map map = adminJdbcService.getLatestPurchaseItemByProductId(product.getId());
        if (map != null && product != null && product.getId() > 0) {
            Double latestPRate = map.get("purchase_rate") != null ? (Double) map.get("purchase_rate") : 0;
            Double latestSaleRate = map.get("sale_rate") != null ? (Double) map.get("sale_rate") : 0;
            logger.debug("SMNLOG:latestPRate:" + latestPRate + " latestSaleRate" + latestSaleRate + " productId:" + product.getId());
            purchaseItem.setQuantity(qty);
            purchaseItem.setRestQuantity(qty);
            purchaseItem.setPurchaseRate(latestPRate);
            purchaseItem.setSaleRate(latestSaleRate);
            purchaseItem.setTotalPrice(latestPRate*qty);
            purchaseItem.setProduct(product);
            this.saveObject(purchaseItem);
            return purchaseItem.getId();
        }
        return null;
    }
}
