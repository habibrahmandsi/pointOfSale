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
        Query query = session.createQuery("FROM User WHERE id = :id");
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
}