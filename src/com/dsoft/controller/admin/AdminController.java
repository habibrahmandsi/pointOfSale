package com.dsoft.controller.admin;

import com.dsoft.entity.*;
import com.dsoft.service.AdminJdbcService;
import com.dsoft.service.AdminService;
import com.dsoft.util.Constants;
import com.dsoft.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Controller
@SessionAttributes({"", ""})
public class AdminController {

    private static Logger logger = Logger.getLogger(AdminController.class);
    @Autowired(required = true)
    private AdminService adminService;
    @Autowired(required = true)
    private AdminJdbcService adminJdbcService;

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, false));
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value = "/*/upsertUser.do", method = RequestMethod.GET)
    public String getUser(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: upsert user controller :: ");
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        User user = new User();
        try {
            if (userId > 0) {
                user = adminService.getUser(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        logger.debug("SMNLOG:All Roles:" + Role.getRoles());
        model.addAttribute("roleList", Role.getRoles());
        if (user == null) {
            logger.debug("ERROR:Failed to load user");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("user.load.failed.msg"));
            return "redirect:./userList.do";
        }


        model.addAttribute("user", user);

        return "common/user";
    }


    @RequestMapping(value = "/*/upsertUser.do", method = RequestMethod.POST)
    public String saveUser(HttpServletRequest request, @ModelAttribute("user") User user) {
        logger.debug("SMNLOG:: User POST Controller::");
        Long userId = user.getId();
        try {
            logger.debug("SMNLOG:: userId:: " + userId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            adminService.saveOrUpdateUser(user);

            if (userId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("user.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("user.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save user exception:: " + ex);
            if (userId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("user.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("user.save.failed.msg"));

        }
        return "redirect:./userList.do";
    }

    @RequestMapping(value = "/*/deleteUser.do", method = RequestMethod.GET)
    public String deleteUser(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: delete user controller :: ");
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        User user = new User();
        try {
            if (userId > 0) {
                user = adminService.getUser(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (user == null) {
            logger.debug("ERROR:Failed to load user");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("user.load.failed.msg"));
            return "redirect:./userList.do";
        } else {

            try {
                adminService.deleteUser(user);
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("user.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete user");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("user.delete.failed.msg"));
                return "redirect:./userList.do";
            }
        }

        model.addAttribute("user", user);

        return "common/user";
    }

    @RequestMapping(value = "/*/userList.do", method = RequestMethod.GET)
    public String userListView(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: userList view controller :: ");

        return "common/userList";
    }

    @RequestMapping(value = "/*/getUsers.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getUsers(HttpServletRequest request) throws Exception {
        logger.info(":: Get User List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "name,father_name,mother_name,sex,age,date_of_birth,max_discount_percent,national_id_no".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getEntitySize(Constants.USER_CLASS);
            if (length < 0) {
                userDataMap = adminJdbcService.getUsers(start, totalRecords + 1, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminJdbcService.getUsers(start, length, sortColName, sortType, searchKey);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load productGroup details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/*/upsertProductGroup.do", method = RequestMethod.GET)
    public String getProductGroup(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: upsert ProductGroup controller :: ");
        Long productGroupId = request.getParameter("productGroupId") != null ? Long.parseLong(request.getParameter("productGroupId")) : 0;
        ProductGroup productGroup = new ProductGroup();
        try {
            if (productGroupId > 0) {
                productGroup = adminService.getProductGroup(productGroupId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (productGroup == null) {
            logger.debug("ERROR:Failed to load productGroup");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productGroup.load.failed.msg"));
            return "redirect:./userList.do";
        }

        model.addAttribute("productGroup", productGroup);

        return "common/productGroup";
    }


    @RequestMapping(value = "/*/upsertProductGroup.do", method = RequestMethod.POST)
    public String saveProductGroup(HttpServletRequest request, @ModelAttribute("productGroup") ProductGroup productGroup) {
        logger.debug("SMNLOG:: ProductGroup POST Controller::");
        Long productGroupId = productGroup.getId();
        try {
            logger.debug("SMNLOG:: productGroupId:: " + productGroupId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            adminService.saveOrUpdateProductGroup(productGroup);

            if (productGroupId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productGroup.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productGroup.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save user exception:: " + ex);
            if (productGroupId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productGroup.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productGroup.save.failed.msg"));

        }
        return "redirect:./productGroupList.do";
    }

    @RequestMapping(value = "/*/deleteProductGroup.do", method = RequestMethod.GET)
    public String deleteproductGroup(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: delete productGroup controller :: ");
        Long productGroupId = request.getParameter("productGroupId") != null ? Long.parseLong(request.getParameter("productGroupId")) : 0;
        ProductGroup productGroup = new ProductGroup();
        try {
            if (productGroupId > 0) {
                productGroup = adminService.getProductGroup(productGroupId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (productGroup == null) {
            logger.debug("ERROR:Failed to load productGroup");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productGroup.load.failed.msg"));
            return "redirect:./productGroupList.do";
        } else {

            try {
                adminService.deleteProductGroup(productGroup);
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productGroup.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete productGroup");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productGroup.delete.failed.msg"));
                return "redirect:./productGroupList.do";
            }
        }

        model.addAttribute("productGroup", productGroup);

        return "redirect:./productGroupList.do";
    }

    @RequestMapping(value = "/*/productGroupList.do", method = RequestMethod.GET)
    public String productGroupListView(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: productGroup view controller :: ");

        return "common/productGroupList";
    }

    @RequestMapping(value = "/*/getProductGroups.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getProductGroups(HttpServletRequest request) throws Exception {
        logger.info(":: Get Product Group List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "name".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getEntitySize(Constants.PRODUCT_GROUP_CLASS);
            if (length < 0) {
                userDataMap = adminJdbcService.getProductGroups(start, totalRecords, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminJdbcService.getProductGroups(start, length, sortColName, sortType, searchKey);
            }
            logger.debug("SMNLOG:Product Group totalRecords:" + totalRecords + " start:" + start + " length:" + length);

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);// filtered from total
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load productGroup details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/*/upsertProduct.do", method = RequestMethod.GET)
    public String getProduct(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: upsert Product controller :: ");
        Long productId = request.getParameter("productId") != null ? Long.parseLong(request.getParameter("productId")) : 0;
        Product product = new Product();
        List productCompanyList = new ArrayList();
        List productGroupList = new ArrayList();
        List unitOfMeasureList = new ArrayList();
        List productTypeList = new ArrayList();
        try {
            if (productId > 0) {
                product = adminService.getProduct(productId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (product == null) {
            logger.debug("ERROR:Failed to load product");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("product.load.failed.msg"));
            return "redirect:./userList.do";
        }

        try {
            productCompanyList = adminJdbcService.getCompanyList("");
            productGroupList = adminJdbcService.getProductGroupList("");
            unitOfMeasureList = adminJdbcService.getUnitOfMeasureList("");
            productTypeList = adminJdbcService.getProductTypeList("");

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("product", product);
        model.addAttribute("productCompanyList", productCompanyList);
        model.addAttribute("productGroupList", productGroupList);
        model.addAttribute("unitOfMeasureList", unitOfMeasureList);
        model.addAttribute("productTypeList", productTypeList);

        logger.debug("SMNLOG:productCompanyList:" + productCompanyList);
        logger.debug("SMNLOG:productGroupList:" + productGroupList);
        logger.debug("SMNLOG:unitOfMeasureList:" + unitOfMeasureList);
        logger.debug("SMNLOG:productTypeList:" + productTypeList);

        return "common/product";
    }


    @RequestMapping(value = "/*/upsertProduct.do", method = RequestMethod.POST)
    public String saveProduct(HttpServletRequest request, @ModelAttribute("product") Product product) {
        logger.debug("SMNLOG:: Product POST Controller::");
        Long productId = product.getId();
        try {
            if(product.getProductGroup() != null && (product.getProductGroup().getId() == null || product.getProductGroup().getId() == 0)){
                product.setProductGroup(null);
            }
            logger.debug("SMNLOG:: product:: " + product);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            adminService.saveOrUpdateProduct(product);

            if (productId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("product.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("product.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save user exception:: " + ex);
            if (productId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("product.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("product.save.failed.msg"));

        }
        return "redirect:./productList.do";
    }

    @RequestMapping(value = "/*/deleteProduct.do", method = RequestMethod.GET)
    public String deleteproduct(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: delete product controller :: ");
        Long productId = request.getParameter("productId") != null ? Long.parseLong(request.getParameter("productId")) : 0;
        Product product = new Product();
        try {
            if (productId > 0) {
                product = adminService.getProduct(productId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (product == null) {
            logger.debug("ERROR:Failed to load product");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("product.load.failed.msg"));
            return "redirect:./productList.do";
        } else {

            try {
                adminService.deleteProduct(product);
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("product.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete product");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("product.delete.failed.msg"));
                return "redirect:./productList.do";
            }
        }

        model.addAttribute("product", product);

        return "redirect:./productList.do";
    }

    @RequestMapping(value = "/*/productList.do", method = RequestMethod.GET)
    public String productListView(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: product view controller :: ");

        return "common/productList";
    }

    @RequestMapping(value = "/*/getProducts.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getProducts(HttpServletRequest request) throws Exception {
        logger.info(":: Get Product List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "p.name,pg.name,purchase_rate,sale_rate,companyName,p.total_quantity".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        Double limitQty = request.getParameter("limitQty") != null ? Double.parseDouble(request.getParameter("limitQty")) : 0d;
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName + " limitQty:" + limitQty);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getProductEntitySize(limitQty);
            if (length < 0) {
                userDataMap = adminJdbcService.getProducts(start, totalRecords, sortColName, sortType, searchKey, limitQty);
            } else {
                userDataMap = adminJdbcService.getProducts(start, length, sortColName, sortType, searchKey, limitQty);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));

            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load product details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/*/getProductsForAutoComplete.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelForTypeAhead getProductsForAutoComplete(HttpServletRequest request) throws Exception {
        logger.info(":: Get getProducts For Auto Complete Ajax Controller ::");
        DataModelForTypeAhead dataModelForTypeAhead = new DataModelForTypeAhead();
    /* this params is for dataTables */
        String[] tableColumns = "p.name".split(",");
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            userDataMap = adminJdbcService.getProductsForAutoComplete(sortColName, sortType, searchKey);

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            Product product = new Product();
            product.setProductList((List) userDataMap.get("data"));
            dataModelForTypeAhead.setData(product);
            dataModelForTypeAhead.setStatus(true);
        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to getProducts For Auto Complete:: " + ex);
            dataModelForTypeAhead.setStatus(false);
        }

        return dataModelForTypeAhead;
    }

    @RequestMapping(value = "/*/upsertCompany.do", method = RequestMethod.GET)
    public String getCompany(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: upsert Company controller :: ");
        Long companyId = request.getParameter("companyId") != null ? Long.parseLong(request.getParameter("companyId")) : 0;
        Company company = new Company();
        try {
            if (companyId > 0) {
                company = adminService.getCompany(companyId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (company == null) {
            logger.debug("ERROR:Failed to load company");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("company.load.failed.msg"));
            return "redirect:./userList.do";
        }

        model.addAttribute("company", company);

        return "common/company";
    }


    @RequestMapping(value = "/*/upsertCompany.do", method = RequestMethod.POST)
    public String saveCompany(HttpServletRequest request, @ModelAttribute("company") Company company) {
        logger.debug("SMNLOG:: Company POST Controller::");
        Long companyId = company.getId();
        try {
            logger.debug("SMNLOG:: companyId:: " + companyId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            adminService.saveOrUpdateCompany(company);

            if (companyId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("company.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("company.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save user exception:: " + ex);
            if (companyId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("company.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("company.save.failed.msg"));

        }
        return "redirect:./companyList.do";
    }

    @RequestMapping(value = "/*/deleteCompany.do", method = RequestMethod.GET)
    public String deletecompany(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: delete company controller :: ");
        Long companyId = request.getParameter("companyId") != null ? Long.parseLong(request.getParameter("companyId")) : 0;
        Company company = new Company();
        try {
            if (companyId > 0) {
                company = adminService.getCompany(companyId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (company == null) {
            logger.debug("ERROR:Failed to load company");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("company.load.failed.msg"));
            return "redirect:./companyList.do";
        } else {

            try {
                adminService.deleteCompany(company);
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("company.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete company");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("company.delete.failed.msg"));
                return "redirect:./companyList.do";
            }
        }

        model.addAttribute("company", company);

        return "redirect:./companyList.do";
    }

    @RequestMapping(value = "/*/companyList.do", method = RequestMethod.GET)
    public String companyListView(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: company view controller :: ");

        return "common/companyList";
    }

    @RequestMapping(value = "/*/getCompanys.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getCompanys(HttpServletRequest request) throws Exception {
        logger.info(":: Get Company Group List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "name,agent_name,agent_cell_no,permanent_address,company_address,company_cell_no,company_email".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getEntitySize(Constants.COMPANY_CLASS);
            if (length < 0) {
                userDataMap = adminJdbcService.getCompanies(start, totalRecords, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminJdbcService.getCompanies(start, length, sortColName, sortType, searchKey);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load company details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/*/upsertUnitOfMeasure.do", method = RequestMethod.GET)
    public String getUnitOfMeasure(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: upsert UnitOfMeasure controller :: ");
        Long unitOfMeasureId = request.getParameter("unitOfMeasureId") != null ? Long.parseLong(request.getParameter("unitOfMeasureId")) : 0;
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        try {
            if (unitOfMeasureId > 0) {
                unitOfMeasure = adminService.getUnitOfMeasure(unitOfMeasureId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (unitOfMeasure == null) {
            logger.debug("ERROR:Failed to load unitOfMeasure");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.load.failed.msg"));
            return "redirect:./userList.do";
        }

        model.addAttribute("unitOfMeasure", unitOfMeasure);

        return "common/unitOfMeasure";
    }


    @RequestMapping(value = "/*/upsertUnitOfMeasure.do", method = RequestMethod.POST)
    public String saveUnitOfMeasure(HttpServletRequest request, @ModelAttribute("unitOfMeasure") UnitOfMeasure unitOfMeasure) {
        logger.debug("SMNLOG:: UnitOfMeasure POST Controller::");
        Long unitOfMeasureId = unitOfMeasure.getId();
        try {
            logger.debug("SMNLOG:: unitOfMeasureId:: " + unitOfMeasureId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            adminService.saveOrUpdateUnitOfMeasure(unitOfMeasure);

            if (unitOfMeasureId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save user exception:: " + ex);
            if (unitOfMeasureId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.save.failed.msg"));

        }
        return "redirect:./unitOfMeasureList.do";
    }

    @RequestMapping(value = "/*/deleteUnitOfMeasure.do", method = RequestMethod.GET)
    public String deleteunitOfMeasure(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: delete unitOfMeasure controller :: ");
        Long unitOfMeasureId = request.getParameter("unitOfMeasureId") != null ? Long.parseLong(request.getParameter("unitOfMeasureId")) : 0;
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        try {
            if (unitOfMeasureId > 0) {
                unitOfMeasure = adminService.getUnitOfMeasure(unitOfMeasureId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (unitOfMeasure == null) {
            logger.debug("ERROR:Failed to load unitOfMeasure");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.load.failed.msg"));
            return "redirect:./unitOfMeasureList.do";
        } else {

            try {
                adminService.deleteUnitOfMeasure(unitOfMeasure);
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete unitOfMeasure");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unitOfMeasure.delete.failed.msg"));
                return "redirect:./unitOfMeasureList.do";
            }
        }

        model.addAttribute("unitOfMeasure", unitOfMeasure);

        return "redirect:./unitOfMeasureList.do";
    }

    @RequestMapping(value = "/*/unitOfMeasureList.do", method = RequestMethod.GET)
    public String unitOfMeasureListView(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: unitOfMeasure view controller :: ");

        return "common/unitOfMeasureList";
    }

    @RequestMapping(value = "/*/getUnitOfMeasures.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getUnitOfMeasures(HttpServletRequest request) throws Exception {
        logger.info(":: Get UnitOfMeasure List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "name".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getEntitySize(Constants.UNIT_OF_MEASURE_CLASS);
            if (length < 0) {
                userDataMap = adminJdbcService.getUnitOfMeasure(start, totalRecords, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminJdbcService.getUnitOfMeasure(start, length, sortColName, sortType, searchKey);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load unitOfMeasure details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/*/upsertProductType.do", method = RequestMethod.GET)
    public String getProductType(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: upsert ProductType controller :: ");
        Long productTypeId = request.getParameter("productTypeId") != null ? Long.parseLong(request.getParameter("productTypeId")) : 0;
        ProductType productType = new ProductType();
        try {
            if (productTypeId > 0) {
                productType = adminService.getProductType(productTypeId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (productType == null) {
            logger.debug("ERROR:Failed to load productType");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productType.load.failed.msg"));
            return "redirect:./userList.do";
        }

        model.addAttribute("productType", productType);

        return "common/productType";
    }


    @RequestMapping(value = "/*/upsertProductType.do", method = RequestMethod.POST)
    public String saveProductType(HttpServletRequest request, @ModelAttribute("productType") ProductType productType) {
        logger.debug("SMNLOG:: ProductType POST Controller::");
        Long productTypeId = productType.getId();
        try {
            logger.debug("SMNLOG:: productTypeId:: " + productTypeId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            adminService.saveOrUpdateProductType(productType);

            if (productTypeId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productType.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productType.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save user exception:: " + ex);
            if (productTypeId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productType.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productType.save.failed.msg"));

        }
        return "redirect:./productTypeList.do";
    }

    @RequestMapping(value = "/*/deleteProductType.do", method = RequestMethod.GET)
    public String deleteproductType(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: delete productType controller :: ");
        Long productTypeId = request.getParameter("productTypeId") != null ? Long.parseLong(request.getParameter("productTypeId")) : 0;
        ProductType productType = new ProductType();
        try {
            if (productTypeId > 0) {
                productType = adminService.getProductType(productTypeId);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (productType == null) {
            logger.debug("ERROR:Failed to load productType");
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productType.load.failed.msg"));
            return "redirect:./productTypeList.do";
        } else {

            try {
                adminService.deleteProductType(productType);
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productType.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete productType");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productType.delete.failed.msg"));
                return "redirect:./productTypeList.do";
            }
        }

        model.addAttribute("productType", productType);

        return "redirect:./productTypeList.do";
    }

    @RequestMapping(value = "/*/productTypeList.do", method = RequestMethod.GET)
    public String productTypeListView(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: productType view controller :: ");

        return "common/productTypeList";
    }

    @RequestMapping(value = "/*/getProductTypes.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getProductTypes(HttpServletRequest request) throws Exception {
        logger.info(":: Get ProductType List Ajax Controller ::");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "name".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getEntitySize(Constants.PRODUCT_TYPE_CLASS);
            if (length < 0) {
                userDataMap = adminJdbcService.getProductType(start, totalRecords, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminJdbcService.getProductType(start, length, sortColName, sortType, searchKey);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load productType details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/superAdmin/superAdmin.do", method = RequestMethod.GET)
    public String getsuperAdminViewPage(HttpServletRequest request, Model model) {
        logger.error("SMNLOG: :: super Admin view controller :: ");
        AdminBean adminBean = new AdminBean();
        ProductKey key = new ProductKey();
        model.addAttribute("adminBean", adminBean);
        model.addAttribute("productKey", key);
        return "admin/superAdmin";
    }

    @RequestMapping(value = "/superAdmin/superAdmin.do", method = RequestMethod.POST)
    public String realTimeMonitoringIntervalSetupPost(@ModelAttribute("adminBean") AdminBean adminBean, HttpServletRequest request,
                                                      BindingResult result, Model model) {
        Integer opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) : 0;
        String fileName = "";
        String filePlacedPath;
        File filePlacedDir;
        String uploadPath = "import";
        String data = "";
        boolean isSaved = false;
        List<String> dataList = new ArrayList<String>();
        List<ProductGroup> productGroupList = new ArrayList<ProductGroup>();
        List<Product> productList = new ArrayList<Product>();
        List<Company> companyList = new ArrayList<Company>();
        Product product = new Product();
        ProductGroup productGroup = new ProductGroup();
        Company company = new Company();
        ProductKey productKey = new ProductKey();

        logger.debug("product Group Bulk Upload post start.....");

        try {
            MultipartFile importFile = adminBean.getProductGroupFile();
            if (importFile != null) {
                fileName = importFile.getOriginalFilename();
                filePlacedPath = System.getProperty("user.home") + File.separator + uploadPath + File.separator + fileName;
                logger.debug("SMNLOG:filePlacedPath:" + filePlacedPath);
                logger.debug("SMNLOG:home:" + System.getProperty("user.home") + File.separator);
                filePlacedDir = new File(System.getProperty("user.home") + File.separator + uploadPath);
                logger.debug("user.home : " + filePlacedPath);
                if (!filePlacedDir.exists()) {
                    filePlacedDir.mkdirs();
                } else {
                    logger.debug("File path exist.");
                }
                if (!Utils.isEmpty(fileName)) {
                    logger.debug("SMNLOG:Start uploading...");
                    importFile.transferTo(new File(filePlacedPath));
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("superAdmin.file.upload.success.msg"));
                    logger.debug("SMNLOG:Uploading is success.");
                    logger.debug("SMNLOG:Saving to db starts.");

                    if (opt > 0 && opt == 1) { // productGroup upload
                        logger.debug("SMNLOG :: Inside ProductGroup bulk upload ::");
                        dataList = this.getDataFromXlFile(new File(filePlacedPath), 0);

                        for (int i = 0; i < dataList.size(); i++) {
                            data = dataList.get(i);
                            if (!Utils.isEmpty(data)) {
                                productGroup.setName(data);
                                try {
                                    adminService.saveOrUpdateProductGroup(productGroup);
                                    isSaved = true;
                                } catch (Exception e) {
                                    isSaved = false;
                                }

                                if (isSaved == true && productGroup.getId() != null) {
                                    logger.debug("SMNLOG:Data saved:" + productGroup.getName());
                                    productGroup.setSaved(true);
                                    productGroupList.add(productGroup);
                                    productGroup = new ProductGroup();

                                }
                            }

                        }
                        model.addAttribute("productGroupList", productGroupList);
                    } else if (opt > 0 && opt == 2) {// product upload
                        logger.debug("SMNLOG :: Inside Product bulk upload ::");
                    } else if (opt > 0 && opt == 3) {// company upload
                        logger.debug("SMNLOG :: Inside Company bulk upload ::");
                        companyList = this.getCompanyListFromXlFile(new File(filePlacedPath));

                        for (int i = 0; i < companyList.size(); i++) {
                            company = companyList.get(i);
                            try {
                                adminService.saveOrUpdateCompany(company);
                                isSaved = true;
                            } catch (Exception e) {
                                isSaved = false;
                            }

                            if (isSaved == true && company.getId() != null) {
                                logger.debug("SMNLOG:Data saved:" + company.getName());
                                companyList.get(i).setSaved(true);
                            }

                        }
                        model.addAttribute("companyList", companyList);
                    } else if (opt > 0 && opt == 4) { // product upload with company name
                        logger.debug("SMNLOG :: Inside Product bulk upload ::");
                        productList = this.getDataObjectFromXlFile(new File(filePlacedPath));

                        for (int i = 0; i < productList.size(); i++) {
                            product = productList.get(i);

                            logger.debug("SMNLOG:NOW SERVING:" + i + " OUT OF :" + productList.size());
                            if (product != null) {
                                company = product.getCompany();
                                productGroup = product.getProductGroup();
                                product.setCompany(null);
                                product.setProductGroup(null);

                                try {
                                    logger.debug("SMNLOG:Company-- 11---->:" + company.getName());
                                    if (company != null && !Utils.isEmpty(company.getName())) {
                                        company = adminService.getCompanyByName(company.getName());
                                        product.setCompany(null);
                                        if (company != null) {
                                            logger.debug("SMNLOG:Company-- 222---company.getId->:" + company.getId());
                                            product.setCompany(company);
                                        }


                                    }
                                    if (productGroup != null && !Utils.isEmpty(productGroup.getName())) {
                                        logger.debug("SMNLOG:Company-- 333---->:" + productGroup.getName());

                                        productGroup = adminService.getProductGroupByName(productGroup.getName());

                                        if (productGroup != null) {
                                            logger.debug("SMNLOG:Company-- 44----productGroup.getId>:" + productGroup.getId());
                                            product.setProductGroup(productGroup);
                                        }
                                    }

//                                    adminService.saveOrUpdateProduct(product);

                                    isSaved = true;
                                } catch (Exception e) {
                                    isSaved = false;
                                    logger.debug("SMNLOG:Failed to saved:" + e);
                                }

                                if (isSaved == true && product.getId() != null) {
                                    logger.debug("SMNLOG:Data saved:" + product.getName());
                                    product.setSaved(true);
                                    productList.add(product);
                                    product = new Product();

                                }
                            }

                        }
                        model.addAttribute("productGroupList", productGroupList);
                    }


                    logger.debug("SMNLOG:Saving to db ends.");
                } else {
                    logger.debug("No file name found.");
                }
            } else {
                logger.debug("No file for import.");
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("superAdmin.file.notFound"));
                //return "redirect:./superAdmin.do";
            }


        } catch (Exception ex) {
            logger.debug("Error while uploading  product Group file :: " + ex);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("superAdmin.file.upload.error.msg"));
            // return "redirect:./superAdmin.do";
        }
        logger.debug("SMNLOG:adding productGroupList to model" + productGroupList.size());
        model.addAttribute("productKey",productKey);
        return "admin/superAdmin";
    }

    public List<String> getDataFromXlFile(File file, int columnIndex) {
        logger.debug("SMNLOG:Data collection from xls file starts.....");
        XSSFRow row;
        List<String> dataList = new ArrayList<String>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getRowIndex() > 0 && cell.getColumnIndex() == columnIndex) {
                        dataList.add(cell.getStringCellValue());
                    }
//

                    /*switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            System.out.print(
                                    cell.getNumericCellValue() + " \t\t ");
                            break;
                        case Cell.CELL_TYPE_STRING:
                            System.out.print(
                                    cell.getStringCellValue() + " \t\t ");
                            break;
                    }*/
                }
                System.out.println();
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("SMNLOG:Data collection from xls ends.");
        return dataList;
    }

    public List<Product> getDataObjectFromXlFile(File file) {
        logger.debug("SMNLOG:Data collection from xls file starts.....");
        XSSFRow row;
        List<Product> productList = new ArrayList<Product>();
        Product product = new Product();
        Company company = new Company();
        ProductGroup productGroup = new ProductGroup();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 0) {// product name
                        product.setName(cell.getStringCellValue());
                    }
                    if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 1) { // company name
                        logger.debug("SMNLOG:C name-->:" + cell.getStringCellValue());
                        company.setName(cell.getStringCellValue());

                    }
                    if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 2) {// group name
                        logger.debug("SMNLOG:p group name---->:" + cell.getStringCellValue());
                        productGroup.setName(cell.getStringCellValue());

                    }
                }
                System.out.println();
                product.setCompany(company);
                product.setProductGroup(productGroup);
                logger.debug("SMNLOG:###### product:" + product);
                productList.add(product);
                product = new Product();
                company = new Company();
                productGroup = new ProductGroup();
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("SMNLOG:Data collection from xls ends.");
        return productList;
    }

    public List<Company> getCompanyListFromXlFile(File file) {
        logger.debug("SMNLOG:Data collection from xls file starts.....");
        XSSFRow row;
        List<Company> dataList = new ArrayList<Company>();
        Company company = new Company();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = spreadsheet.iterator();
            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 0) {
                        company.setName(cell.getStringCellValue());
                    } else if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 1) {
                        company.setAgentName(cell.getStringCellValue());
                    } else if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 2) {
                        company.setAgentCellNo(cell.getStringCellValue().replace("Mob:", ""));

                    } else if (cell.getRowIndex() > 0 && cell.getColumnIndex() == 3) {
                        company.setPermanentAddress(cell.getStringCellValue());
                    }

                }
                if (!Utils.isEmpty(company.getName()))
                    dataList.add(company);

                company = new Company();
                System.out.println();
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.debug("SMNLOG:Data collection from xls ends.");
        return dataList;
    }


    @RequestMapping(value = "/*/upsertPurchase.do", method = RequestMethod.GET)
    public String upsertPurchase(HttpServletRequest request, Model model) {
        logger.debug("********* upsert Purchase view controller *********");
        Purchase purchase = new Purchase();
        List<PurchaseItem> purchaseItemList = null;
        Long purchaseId = request.getParameter("purchaseId") != null ? Long.parseLong(request.getParameter("purchaseId")) : 0;
        int purchaseReturn = request.getParameter("purchaseReturn") != null ? Integer.parseInt(request.getParameter("purchaseReturn")) : 0;
        logger.debug("SMNLOG:purchaseId:" + purchaseId + " purchaseReturn::" + purchaseReturn);
        if (purchaseReturn == 1)
            purchase.setPurchaseReturn(true);

//        int purchaseReturn = 0; // for purchase
        try {
            if (purchaseId > 0) {
                purchase = adminService.getPurchase(purchaseId, purchaseReturn);
                if (purchase == null) {
                    logger.debug("ERROR:Failed to load purchase");
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.load.failed.msg"));
                    return "redirect:./purchaseList.do";
                }
                purchaseItemList = adminService.getPurchaseItemListByPurchaseId(purchaseId);
                logger.debug("SMNLOG:purchaseItemList size:" + purchaseItemList.size());

                if (purchaseItemList != null && purchaseItemList.size() > 0) {
                    for (PurchaseItem purchaseItem : purchaseItemList) {
                        if (purchaseItem != null) {
                            purchaseItem.setPrevQuantity(purchaseItem.getQuantity());
                        }
                    }
                    purchase.setPurchaseItemList(purchaseItemList);
                }
                logger.debug("SMNLOG:purchase:" + purchase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        purchase.setPurchaseItemList(purchaseItemList);
        model.addAttribute("purchase", purchase);
        return "common/purchase";
    }


    @RequestMapping(value = "/*/upsertPurchase.do", method = RequestMethod.POST)
    public String upsertPurchase(HttpServletRequest request, @ModelAttribute("purchase") Purchase purchase) {
        logger.debug("********* purchase POST Controller *********");
        logger.debug("SMNLOG:: purchase::" + purchase);
        Long purchaseId = purchase.getId();
        try {
            logger.debug("SMNLOG:: purchaseId:: " + purchaseId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());

            if (purchase.getPurchaseReturn()) {
                if (purchaseId != null) { // when to update
                    if (Utils.isEmpty(purchase.getPurchaseTokenNo()))
                        purchase.setPurchaseTokenNo(Utils.generateUniqueId("PR"));
                } else { // when to save
                    purchase.setPurchaseDate(new Date());
                    purchase.setPurchaseTokenNo(Utils.generateUniqueId("PR"));
                }
            } else {
                if (purchaseId != null) { // when to update
                    if (Utils.isEmpty(purchase.getPurchaseTokenNo()))
                        purchase.setPurchaseTokenNo(Utils.generateUniqueId("P"));
                } else { // when to save
                    purchase.setPurchaseDate(new Date());
                    purchase.setPurchaseTokenNo(Utils.generateUniqueId("P"));
                }

            }
            User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());
            purchase.setUser(user);
            purchase.setUnposted(false);
            if (purchase.getPurchaseReturn())
                adminService.saveOrUpdatePurchaseReturn(purchase);
            else
                adminService.saveOrUpdatePurchase(purchase);

            if (purchase.getPurchaseReturn()) {
                if (purchaseId != null)
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.update.success.msg"));
                else
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.save.success.msg") + "<b>&nbsp;PURCHASE RETURN INVOICE NO:</b>&nbsp;<b style='color:red'>" + purchase.getPurchaseTokenNo() + "</b>");
            } else {
                if (purchaseId != null)
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.update.success.msg"));
                else
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.save.success.msg") + "<b>&nbsp;PURCHASE INVOICE NO:</b>&nbsp;<b style='color:red'>" + purchase.getPurchaseTokenNo() + "</b>");
            }
        } catch (Exception ex) {
            if (purchase.getPurchaseReturn()) {
                logger.error("Save Purchase Return exception:: " + ex);
                if (purchase.getId() != null)
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.update.failed.msg"));
                else
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.save.failed.msg"));

            } else {
                logger.error("Save Purchase exception:: " + ex);
                if (purchaseId != null)
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.update.failed.msg"));
                else
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.save.failed.msg"));
            }
        }
        if (purchase.getPurchaseReturn())
            return "redirect:./purchaseReturnList.do";
        else
            return "redirect:./purchaseList.do";


    }


    @RequestMapping(value = "/*/deleteAnyObject.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean deleteAnyObject(HttpServletRequest request, Model model) {
        logger.error("********* delete Any Object controller *********");
        String tableName = request.getParameter("tableName") != null ? (String) request.getParameter("tableName") : "";
        String colName = request.getParameter("colName") != null ? (String) request.getParameter("colName") : "";
        String colValue = request.getParameter("colValue") != null ? (String) request.getParameter("colValue") : "";
        logger.debug("SMNLOG:tableName:" + tableName + " colName:" + colName + " colValue:" + colValue);
        Boolean isSuccess = false;
        try {
            if (!Utils.isEmpty(tableName) && !Utils.isEmpty(colName) && !Utils.isEmpty(colValue)) {
                adminJdbcService.deleteEntityByAnyColValue(tableName, colName, colValue);
                isSuccess = true;
            } else {
                logger.debug("SMNLOG:EMPTY :");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return isSuccess;
    }

    @RequestMapping(value = "/*/purchaseList.do", method = RequestMethod.GET)
    public String purchaseListView(HttpServletRequest request, Model model) {
        logger.error("********* purchase view controller *********");
        model.addAttribute("opt", 0);
        return "common/purchaseList";
    }

    @RequestMapping(value = "/*/getPurchases.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getPurchases(HttpServletRequest request) throws Exception {
        logger.info("********* Get Purchase List Ajax Controller *********");
        DataModelBean dataModelBean = new DataModelBean();
    /* this params is for dataTables */
        String[] tableColumns = "purchase_token_no,purchase_date,total_amount,discount,userName".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        Integer purchaseReturn = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) : 0;
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;
        try {
            int totalRecords = adminJdbcService.getPurchaseCount(purchaseReturn);
            if (length < 0) {
                userDataMap = adminJdbcService.getPurchases(start, totalRecords, sortColName, sortType, searchKey, purchaseReturn);
            } else {
                userDataMap = adminJdbcService.getPurchases(start, length, sortColName, sortType, searchKey, purchaseReturn);
            }

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load Purchase details data:: " + ex);
        }

        return dataModelBean;
    }


    @RequestMapping(value = "/*/deletePurchase.do", method = RequestMethod.GET)
    public String deletePurchase(HttpServletRequest request, Model model) {
        logger.debug("********* delete Purchase controller *********");
        Long purchaseId = request.getParameter("purchaseId") != null ? Long.parseLong(request.getParameter("purchaseId")) : 0;
        Long opt = request.getParameter("opt") != null ? Long.parseLong(request.getParameter("opt")) : 0;
        logger.debug("SMNLOG: :: purchaseId :: " + purchaseId);
        List<PurchaseItem> purchaseItemList = new ArrayList<PurchaseItem>();
        PurchaseItem purchaseItem;
        Boolean isError = true;
        try {
            if (purchaseId > 0) {
                purchaseItemList = adminService.getPurchaseItemListByPurchaseId(purchaseId);
                logger.debug("SMNLOG:111---------opt:" + opt);
                if (purchaseItemList != null && purchaseItemList.size() > 0) {
                    logger.debug("SMNLOG: 222---------opt:" + opt);
                    adminService.deletePurchaseItem(purchaseItemList, opt.intValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isError = false;
        }

        if (isError == true) {
            logger.debug("SMNLOG:Purchase item deleted successful.Now to delete Purchase:" + purchaseId);
            try {
                adminJdbcService.deleteEntityByAnyColValue(Constants.PURCHASE_TABLE, "id", purchaseId + "");
                Utils.setGreenMessage(request, opt == 1 ? Utils.getMessageBundlePropertyValue("purchase.return.delete.success.msg") :
                        Utils.getMessageBundlePropertyValue("purchase.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete user");
                Utils.setErrorMessage(request, opt == 1 ? Utils.getMessageBundlePropertyValue("purchase.return.delete.failed.msg") :
                        Utils.getMessageBundlePropertyValue("purchase.delete.failed.msg"));
            }
        } else {
            Utils.setErrorMessage(request, opt == 1 ? Utils.getMessageBundlePropertyValue("purchase.return.delete.failed.msg") :
                    Utils.getMessageBundlePropertyValue("purchase.delete.failed.msg"));
        }
        if(opt == 0)
            return "redirect:./upsertPurchase.do";
        else if (opt == 1)
            return "redirect:./purchaseReturnList.do";

        return "redirect:./purchaseList.do";

    }


    @RequestMapping(value = "/*/upsertPurchaseReturn.do", method = RequestMethod.GET)
    public String purchaseReturn(HttpServletRequest request, Model model) {
        logger.error("********* Purchase return view controller *********");
        Purchase purchase = new Purchase();
        List<PurchaseItem> purchaseItemList = null;
        Long purchaseId = request.getParameter("purchaseId") != null ? Long.parseLong(request.getParameter("purchaseId")) : 0;
        Long update = request.getParameter("update") != null ? Long.parseLong(request.getParameter("update")) : 0;
        logger.debug("SMNLOG:purchaseId:" + purchaseId);
        int purchaseReturn = 1; // for purchase return
        try {
            if (purchaseId > 0) {
                purchase = adminService.getPurchase(purchaseId, purchaseReturn);
                if (purchase == null) {
                    logger.debug("ERROR:Failed to load purchase return");
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.load.failed.msg"));
                    return "redirect:./purchaseList.do";
                }/*else if(!purchase.getPurchaseReturn()){
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.failed.msg"));
                    return "redirect:./purchaseList.do";
                }*/

                purchaseItemList = adminService.getPurchaseItemListByPurchaseId(purchaseId);
                logger.debug("SMNLOG:purchaseItemList size:" + purchaseItemList.size());

                if (purchaseItemList != null && purchaseItemList.size() > 0) {
                    for (PurchaseItem purchaseItem : purchaseItemList) {
                        if (purchaseItem != null) {
                            purchaseItem.setId(null);//always to create new pr
                            if (update == 0) {
                                purchaseItem.setTotalPrice(0.0);
                            } else {
                                purchaseItem.setQuantity(purchaseItem.getQuantity() < 0 ? (purchaseItem.getQuantity() * (-1)) : purchaseItem.getQuantity());
                            }
                            purchaseItem.setPrevQuantity(purchaseItem.getQuantity());

                        }
                    }
                    purchase.setPurchaseItemList(purchaseItemList);
                }
                logger.debug("SMNLOG:purchase:" + purchase);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (update == 0)
            purchase.setTotalAmount(0.0);

        purchase.setId(null); // As it is purchase return
        purchase.setPurchaseItemList(purchaseItemList);
        model.addAttribute("update", update);
        model.addAttribute("purchase", purchase);
        return "common/purchaseReturn";
    }


    @RequestMapping(value = "/*/upsertPurchaseReturn.do", method = RequestMethod.POST)
    public String upsertPurchaseReturn(HttpServletRequest request, @ModelAttribute("purchase") Purchase purchase) {
        logger.debug("********* purchase return POST Controller *********");
        logger.debug("SMNLOG:: purchase::" + purchase);
        boolean status = false;
        Long purchaseId = purchase.getId();
        try {
            logger.debug("SMNLOG:: purchaseId:: " + purchaseId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());
            purchase.setPurchaseDate(new Date());
            User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());

            purchase.setPurchaseTokenNo(Utils.generateUniqueId("PR"));
            purchase.setUser(user);
            purchase.setPurchaseReturn(true); // As this is for purchase return
            status = adminService.saveOrUpdatePurchaseReturn(purchase);
            if (status == false) {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.nothing.save.msg"));
                return "redirect:./purchaseReturnList.do";
            }

            if (purchaseId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.save.success.msg") + "<b>&nbsp;PURCHASE RETURN INVOICE NO:</b>&nbsp;<b style='color:red'>" + purchase.getPurchaseTokenNo() + "</b>");

        } catch (Exception ex) {
            logger.error("Save Purchase Return exception:: " + ex);
            if (purchase.getId() != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.return.save.failed.msg"));

        }
        return "redirect:./purchaseReturnList.do";
    }

    @RequestMapping(value = "/*/purchaseReturnList.do", method = RequestMethod.GET)
    public String purchaseReturnListView(HttpServletRequest request, Model model) {
        logger.error("********* purchase Return list controller *********");
        model.addAttribute("opt", 1);
        return "common/purchaseList";
    }

    @RequestMapping(value = "/superAdmin/generateKey.do", method = RequestMethod.POST)
    public String generateKey(HttpServletRequest request, @ModelAttribute("productKey") ProductKey productKey, Model model) {
        logger.error("********* generateKey controller *********");
        logger.error("SMNLOG: :: productKey:: " + productKey);
        Long productKeyId = productKey.getId();
        try {
           /* String txt="some text to be encrypted" ;
            String key="key phrase used for XOR-ing";
            System.out.println(txt+" XOR-ed to: "+(txt=xorMessage( txt, key )));
            String encoded=base64encode( txt );
            System.out.println( " is encoded to: "+encoded+" and that is decoding to: "+ (txt=base64decode( encoded )));
            System.out.print( "XOR-ing back to original: "+xorMessage( txt, key ) );*/
//            byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
            byte[] keyValue = productKey.getPrivateKey().getBytes();

            Key key = Utils.generateKey(keyValue);
            logger.debug("SMNLOG:keyValue::" + keyValue + " :: key" + key);

            String message = productKey.getUserName() + Constants.P_KEY_SEPARATOR
                    + productKey.getPrivateKey() + Constants.P_KEY_SEPARATOR
                    + Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()) + Constants.P_KEY_SEPARATOR
                    + productKey.getValidUpTo();

            logger.debug("SMNLOG:message::" + message);

            String generatedProductKey = Utils.encrypt(message, key);
            productKey.setProductKey(generatedProductKey);
            logger.debug("SMNLOG:generatedProductKey::" + generatedProductKey);
/*

            key = Utils.generateKey(keyValue);
            logger.debug("SMNLOG:original value::" + Utils.decrypt(generatedProductKey,key));
*/

            adminService.saveOrUpdateObject(productKey);

            if (productKeyId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productKey.return.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("productKey.return.save.success.msg") + "<b>&nbsp;PRODUCT KEY:</b>&nbsp;<b style='color:red'>" + productKey.getProductKey() + "</b>");


        } catch (Exception ex) {
            logger.error("Product Key exception:: " + ex);
            if (productKey.getId() != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productKey.return.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("productKey.return.save.failed.msg"));

        }

        return "redirect:./superAdmin.do";
    }

    @RequestMapping(value = "/*/upsertSales.do", method = RequestMethod.GET)
    public String getupsertSales(HttpServletRequest request, Model model) {
        logger.error("********* upsert Sales view controller *********");
        Sales sales = new Sales();
        List<SalesItem> salesItemList = null;
        List totalSaleByUser = null;
        User user = null;
        Map map = new HashMap();
        List<Sales> salesList = new ArrayList<Sales>();
        Long salesId = request.getParameter("salesId") != null ? Long.parseLong(request.getParameter("salesId")) : 0;
        int salesReturn = request.getParameter("salesReturn") != null ? Integer.parseInt(request.getParameter("salesReturn")) : 0;
        logger.debug("SMNLOG:salesId:" + salesId);
//        int salesReturn = 0; // for sales
        try {
            if (salesId > 0) {
                sales = adminService.getSale(salesId, salesReturn);
                if (sales == null) {
                    logger.debug("ERROR:Failed to load sales");
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.load.failed.msg"));
                    return "redirect:./salesList.do";
                }
                salesItemList = adminService.getSalesItemListBySalesId(salesId);
                logger.debug("SMNLOG:salesItemList size:" + salesItemList.size());

                if (salesItemList != null && salesItemList.size() > 0) {
                    for (SalesItem salesItem : salesItemList) {
                        if (salesItem != null) {
                            salesItem.setPrevQuantity(salesItem.getQuantity());
                        }
                    }
                    sales.setSalesItemList(salesItemList);
                }
                logger.debug("SMNLOG:sales:" + sales);
            } else {


                logger.debug("SMNLOG:------- Saving sales as unposted ---------------");

                user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());

                logger.debug("SMNLOG:------- first to check empty unposted sales---------------");
                salesList = adminService.getUnpostedSalesListByUserId(user.getId());
                if (salesList != null && salesList.size() > 0) {
                    for (Sales sale : salesList) {
                        logger.debug("SMNLOG:------- Found empty unposted sales---------------" + sale.getId());
                        salesItemList = adminService.getSalesItemListBySalesId(sale.getId());
                        adminService.deleteSalesItem(salesItemList);
                        adminService.deleteSale(sale);
                        logger.debug("SMNLOG:------- delete complete unposted sales---------------" + sale.getId());
                    }

                }
                if (salesReturn == 1){
                    sales.setSalesReturn(true);
                    sales.setSalesTokenNo(Utils.generateUniqueId("SR"));
                }else{
                    sales.setSalesReturn(false);
                    sales.setSalesTokenNo(Utils.generateUniqueId("S"));
                }

                sales.setUser(user);
                sales.setSalesDate(new Date());
                logger.debug("SMNLOG:-------1 sales id:" + sales.getId());
                adminService.saveOrUpdateObject(sales);
                logger.debug("SMNLOG:-------2 sales id:" + sales.getId());
            }
            Date fmDate = Utils.startOfDate(new Date());
            totalSaleByUser = adminJdbcService.getTotalSaleByDateAndUserId(fmDate, null, user != null ? user.getId() : 0, salesReturn, 0, 0);
            logger.debug("SMNLOG:-------totalSaleByUser:" + totalSaleByUser);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("ERROR:" + e);

        }
        sales.setSalesItemList(salesItemList);
        model.addAttribute("sales", sales);
        if(totalSaleByUser != null && totalSaleByUser.size() > 0){
             map = (Map)totalSaleByUser.get(0);
        }
        model.addAttribute("totalSaleByUser", map.get("totalSaleAmount") != null ? map.get("totalSaleAmount"): "0.0");
        model.addAttribute("salesId", salesId);
        return "common/sales";
    }

    @RequestMapping(value = "/*/upsertSales.do", method = RequestMethod.POST)
    public String upsertSales(HttpServletRequest request, @ModelAttribute("sales") Sales sales) {
        logger.debug("********* sales POST Controller *********");
        logger.debug("SMNLOG:: sales::" + sales);
        Long salesId = sales.getId();
        List<SalesItem> salesItemList = new ArrayList<SalesItem>();
        String redirectUrl = "./upsertSales.do";
        if(sales.getSalesReturn())
            redirectUrl += "?salesReturn=1";
        try {
            logger.debug("SMNLOG:: salesId:: " + salesId);
            if (salesId != null) { // when to update
                sales.setSalesDate(new Date());
                sales.setUnposted(false);
                if (Utils.isEmpty(sales.getSalesTokenNo()))
                    sales.setSalesTokenNo(Utils.generateUniqueId("S"));

                // update purchase li
                salesItemList = adminService.getSalesItemListBySalesId(salesId);
                if(salesItemList!= null && salesItemList.size() > 0){
                    for(SalesItem salesItem: salesItemList){
                        logger.debug("## -- pid:"+salesItem.getProduct().getId()+" Qty:"+salesItem.getQuantity());
                        if(sales.getSalesReturn()){ // add an sales item for sale return
                            logger.debug("SMNLOG:-----------add an sales item for sale return --------------");
                            adminService.savePurchaseItemAsPurchaseReturn(adminJdbcService, salesItem.getProduct(), salesItem.getQuantity());
                        }else{
                            if(updatePurchaseItem(salesItem.getProduct().getId(), salesItem.getQuantity(), salesItem)){
                                logger.debug("SMNLOG:----------- updatePurchaseItem in Li --------------SUCCESS");
                            }else{
                                logger.debug("SMNLOG:----------- updatePurchaseItem in Li --------------FAILED");
                                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.save.failed.msg"));
                                return "redirect:"+redirectUrl;
                            }
                        }

                    }

                }
            } else { // when to save
                sales.setSalesDate(new Date());
                sales.setSalesReturn(false);// as it is sales not sales return
                sales.setSalesTokenNo(Utils.generateUniqueId("S"));
            }

            User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());
            sales.setUser(user);
            sales.setSalesItemList(null);
            adminService.saveOrUpdateSales(sales);

            if(sales.getSalesReturn()){
               /* if (salesId != null)
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.return.update.success.msg"));
                else*/
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.return.save.success.msg") + "<b>&nbsp;SALES RETURN INVOICE NO:</b>&nbsp;<b style='color:red'>" + sales.getSalesTokenNo() + "</b>");

              }else{
                /*if (salesId != null)
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.update.success.msg"));
                else*/
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.save.success.msg") + "<b>&nbsp;SALES INVOICE NO:</b>&nbsp;<b style='color:red'>" + sales.getSalesTokenNo() + "</b>");
            }
           } catch (Exception ex) {
            logger.error("Save sales exception:: " + ex);
            if(sales.getSalesReturn()){
                /*if (sales.getId() != null)
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.update.failed.msg"));
                else*/
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.save.failed.msg"));

              }else{
               /* if (salesId != null)
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.update.failed.msg"));
                else*/
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.save.failed.msg"));
            }

        }
        return "redirect:"+redirectUrl;
    }


    @RequestMapping(value = "/*/getSales.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getSales(HttpServletRequest request) throws Exception {
        logger.info("********* Get Sales List Ajax Controller *********");
        DataModelBean dataModelBean = new DataModelBean();
        /* this params is for dataTables */
        String[] tableColumns = "sales_token_no,sales_date,total_amount,discount,userName".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) : 0;
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);
        int salesReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a sales report");
        }else if(opt == 1){
            logger.debug("----- :this is a sales Return report");
            salesReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted sales report");
            salesReturn = 0;
            unposted = 1;
        }else if(opt == 3 ){
            logger.debug("----- :this is a unposted sales return report");
            salesReturn = 1;
            unposted = 1;
        }
        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;
        try {
            int totalRecords = adminJdbcService.getSalesCount(userId,salesReturn,unposted);// 0 for sales and 1 for sales Return
            if (length < 0) {
                userDataMap = adminJdbcService.getSales(start, totalRecords, sortColName, sortType, searchKey, userId,salesReturn, unposted);
            } else {
                userDataMap = adminJdbcService.getSales(start, length, sortColName, sortType, searchKey,userId, salesReturn, unposted);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load Sales details data:: " + ex);
        }

        return dataModelBean;
    }


    @RequestMapping(value = "/*/salesList.do", method = RequestMethod.GET)
    public String salesListView(HttpServletRequest request, Model model) {
        logger.error("********* sales view controller *********");
        Long opt = request.getParameter("opt") != null ? Long.parseLong(request.getParameter("opt")) : 0;
        model.addAttribute("opt", opt);
        return "common/salesList";
    }

    @RequestMapping(value = "/*/deleteSales.do", method = RequestMethod.GET)
    public String deleteSales(HttpServletRequest request, Model model) {
        logger.error("********* delete Sales controller *********");
        Long salesId = request.getParameter("salesId") != null ? Long.parseLong(request.getParameter("salesId")) : 0;
        Long opt = request.getParameter("opt") != null ? Long.parseLong(request.getParameter("opt")) : 0;
        logger.error("SMNLOG: :: salesId :: " + salesId);
        List<SalesItem> salesItemList = new ArrayList<SalesItem>();
        Boolean isError = true;
        try {
            if (salesId > 0) {
                salesItemList = adminService.getSalesItemListBySalesId(salesId);
                if (salesItemList != null && salesItemList.size() > 0) {
                    if (opt == 0 || opt == 2)
                        adminService.deleteSalesItem(salesItemList);
                    else if (opt == 1)
                        adminService.deleteSalesReturnItem(salesItemList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isError = false;
            logger.debug("SMNLOG:ERROR to delete salesItem:" + e);
        }

        if (isError == true) {
            logger.debug("SMNLOG:Sales item deleted successful.Now to delete Sales:" + salesId);
            try {
                adminJdbcService.deleteEntityByAnyColValue(Constants.SALES_TABLE, "id", salesId + "");
                if (opt == 1)
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.return.delete.success.msg"));
                else if (opt == 2)
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("unposted.delete.success.msg"));
                else
                    Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.delete.success.msg"));
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("ERROR:Failed to delete un-posted sales");
                if (opt == 1)
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.delete.failed.msg"));
                else if (opt == 2)
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unposted.delete.failed.msg"));
                else
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.delete.failed.msg"));

            }
        } else {
            if (opt == 1)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.delete.failed.msg"));
            else if (opt == 2)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("unposted.delete.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.delete.failed.msg"));
        }
        if(opt == 0)
            return "redirect:./upsertSales.do";
        else if (opt == 1)
            return "redirect:./salesReturnList.do";
        else if (opt == 2)
            return "redirect:./unpostedSale.do";

        return "redirect:./salesList.do";

    }


    @RequestMapping(value = "/*/upsertSalesReturn.do", method = RequestMethod.GET)
    public String salesReturn(HttpServletRequest request, Model model) {
        logger.error("********* Sales return view controller *********");
        Sales sales = new Sales();
        List<SalesItem> salesItemList = null;
        Long salesId = request.getParameter("salesId") != null ? Long.parseLong(request.getParameter("salesId")) : 0;
        Long update = request.getParameter("update") != null ? Long.parseLong(request.getParameter("update")) : 0;
        logger.debug("SMNLOG:salesId:" + salesId);
        int salesReturn = 1; // for sales return
        try {
            if (salesId > 0) {
                sales = adminService.getSale(salesId, salesReturn);
                if (sales == null) {
                    logger.debug("ERROR:Failed to load sales return");
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.load.failed.msg"));
                    return "redirect:./salesList.do";
                }/*else if(!sales.getSalesReturn()){
                    Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.failed.msg"));
                    return "redirect:./salesList.do";
                }*/

                salesItemList = adminService.getSalesItemListBySalesId(salesId);
                logger.debug("SMNLOG:salesItemList size:" + salesItemList.size());

                if (salesItemList != null && salesItemList.size() > 0) {
                    for (SalesItem salesItem : salesItemList) {
                        if (salesItem != null) {
                            if (update == 0) {
                                salesItem.setTotalPrice(0.0);
                            } else {
                                salesItem.setQuantity(salesItem.getQuantity() < 0 ? (salesItem.getQuantity() * (-1)) : salesItem.getQuantity());
                            }
                            salesItem.setPrevQuantity(salesItem.getQuantity());

                        }
                    }
                    sales.setSalesItemList(salesItemList);
                }
                logger.debug("SMNLOG:sales:" + sales);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (update == 0)
            sales.setTotalAmount(0.0);

        sales.setId(null); // As it is sales return
        sales.setSalesItemList(salesItemList);
        model.addAttribute("update", update);
        model.addAttribute("sales", sales);
        return "common/salesReturn";
    }


    @RequestMapping(value = "/*/upsertSalesReturn.do", method = RequestMethod.POST)
    public String upsertSalesReturn(HttpServletRequest request, @ModelAttribute("sales") Sales sales) {
        logger.debug("********* sales return POST Controller *********");
        logger.debug("SMNLOG:: sales::" + sales);
        boolean status = false;
        Long salesId = sales.getId();
        List <SalesItem> salesItemList = new ArrayList<SalesItem>();
        try {
            logger.debug("SMNLOG:: salesId:: " + salesId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());
            sales.setSalesDate(new Date());
            User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());

            sales.setSalesTokenNo(Utils.generateUniqueId("SR"));
            sales.setUser(user);
            sales.setSalesReturn(true); // As this is for sales return
            sales.setUnposted(false);
            status = adminService.saveOrUpdateSalesReturn(sales);

            if (status == false) {
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.nothing.save.msg"));
                return "redirect:./salesReturnList.do";
            }

            if (salesId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.return.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("sales.return.save.success.msg") + "<b>&nbsp;SALES RETURN INVOICE NO:</b>&nbsp;<b style='color:red'>" + sales.getSalesTokenNo() + "</b>");

        } catch (Exception ex) {
            logger.error("Save Sales Return exception:: " + ex);
            if (sales.getId() != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("sales.return.save.failed.msg"));

        }
        return "redirect:./salesReturnList.do";
    }

    @RequestMapping(value = "/*/salesReturnList.do", method = RequestMethod.GET)
    public String salesReturnListView(HttpServletRequest request, Model model) {
        logger.error("********* sales Return list controller *********");
        model.addAttribute("opt", 1);
        return "common/salesList";
    }

    @RequestMapping(value = "/*/addSalesItems.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String addSalesItem(HttpServletRequest request, Model model) {
        logger.debug("********* add Sales items AJAX controller *********");
        SalesItem salesItem = new SalesItem();

        try {

            Long sId = request.getParameter("sId") != null ? Long.parseLong(request.getParameter("sId")) : 0;
            Long pId = request.getParameter("pId") != null ? Long.parseLong(request.getParameter("pId")) : 0;
            Long sItemId = request.getParameter("sItemId") != null ? Long.parseLong(request.getParameter("sItemId")) : 0;
            int onUpdateTxt = request.getParameter("onUpdateTxt") != null ? Integer.parseInt(request.getParameter("onUpdateTxt")) : 0;
            Double qty = request.getParameter("qty") != null ? Double.parseDouble(request.getParameter("qty")) : 0;
            Double pRate = request.getParameter("pRate") != null ? Double.parseDouble(request.getParameter("pRate")) : 0;
            Double sRate = request.getParameter("sRate") != null ? Double.parseDouble(request.getParameter("sRate")) : 0;
            Double totalPrice = request.getParameter("totalPrice") != null ? Double.parseDouble(request.getParameter("totalPrice")) : 0;
            Double grandTotal = request.getParameter("grandTotal") != null ? Double.parseDouble(request.getParameter("grandTotal")) : 0;
            Double discount = request.getParameter("discount") != null ? Double.parseDouble(request.getParameter("discount")) : 0;
            Double prevQty = 0d;
            int salesReturn = request.getParameter("salesReturn") != null ? Integer.parseInt(request.getParameter("salesReturn")) : 0;
            logger.debug("sId:" + sId + " pId:" + pId + " sItemId" + sItemId + " qty" + qty + " pRate:" + pRate
                    + " sRate:" + sRate + " totalPrice:" + totalPrice + " grandTotal:" + grandTotal + " discount:" + discount+" salesReturn:"+salesReturn);
//        int salesReturn = 0; // for sales

            if (sId > 0 && pId > 0) {
                Sales sales = adminService.getSale(sId, salesReturn);
                sales.setId(sId);

                sales.setTotalAmount(grandTotal);
                sales.setDiscount(discount);

                Product product = new Product();
                product.setId(pId);

                /*if(updatePurchaseItem(pId, qty)){
                    logger.debug("SMNLOG:----------- updatePurchaseItem --------------SUCCESS");
                }else{
                    logger.debug("SMNLOG:----------- updatePurchaseItem --------------FAILED");
                    return "false";
                }*/
                if (sItemId > 0) {
                    salesItem = adminService.getSalesItem(sItemId);
                    prevQty = salesItem.getQuantity();
                } else {
                    salesItem.setSales(sales);
                    salesItem.setProduct(product);
                }

                salesItem.setPurchaseRate(pRate);
                salesItem.setSalesRate(sRate);
                salesItem.setQuantity(qty);
                salesItem.setTotalPrice(salesItem.getQuantity() * salesItem.getSalesRate());

                adminService.saveOrUpdateObject(sales);
                adminService.saveOrUpdateObject(salesItem);
                if(salesReturn == 1){
                    logger.debug("salesReturn ::"+salesReturn);
                    qty = qty*(-1);
                }
                if (sItemId > 0) {
                    logger.debug("SMNLOG:-------QTY :--------:" + prevQty);
                    logger.debug("SMNLOG:-------QTY TO UPDATE--------:" + (qty - prevQty));
                    adminService.updateProductQuantity(pId, (-1) * (qty - prevQty));
                } else
                    adminService.updateProductQuantity(pId, qty * (-1));
            } else {
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }
        return salesItem.getId() + "";
    }


    @RequestMapping(value = "/*/deleteSalesItem.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String deleteSalesItemsAndSReturnItems(HttpServletRequest request) {
        logger.debug("********* delete SalesItems And SReturn Items AJAX Controller *********");
        try {
            SalesItem salesItem = new SalesItem();
            Long sId = request.getParameter("sId") != null ? Long.parseLong(request.getParameter("sId")) : 0;
            Long sItemId = request.getParameter("sItemId") != null ? Long.parseLong(request.getParameter("sItemId")) : 0;
            Double totalPrice = request.getParameter("totalPrice") != null ? Double.parseDouble(request.getParameter("totalPrice")) : 0;

            Boolean sr = request.getParameter("salesReturn") != null ? Boolean.parseBoolean(request.getParameter("salesReturn")) : false;
            int salesReturn = 0;
            if (sr)
                salesReturn = 1;

            logger.debug(" 1 ---->sId:" + sId + " totalPrice:" + totalPrice);
            if (sId > 0 && sItemId > 0) {
                Sales sales = adminService.getSale(sId, salesReturn);
                sales.setTotalAmount(sales.getTotalAmount() - totalPrice);
                salesItem = adminService.getSalesItem(sItemId);
                adminService.saveOrUpdateObject(sales);
                adminService.deleteObject(salesItem);
                if (sr) {
                    adminService.updateProductQuantity(salesItem.getProduct().getId(), (-1) * salesItem.getQuantity());
                } else {
                    adminService.updateProductQuantity(salesItem.getProduct().getId(), salesItem.getQuantity());
                }

            } else {
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("SMNLOG:ERROR: deleteSalesItemsAndSReturnItems:" + e);
            return "false";
        }
        return "true";
    }

    public boolean updatePurchaseItem(Long pId, Double qty, SalesItem salesItem) {
        logger.debug("******************* Update purchase item ****************** ");
        List<PurchaseItem> purchaseItemList = new ArrayList<PurchaseItem>();
        PurchaseItem purchaseItem = new PurchaseItem();
        Double restQty = qty;
        Double totalPurchasePrice =  0d;
        try {
            purchaseItemList = adminService.getPurchaseItemList(pId);
            logger.debug("SMNLOG:purchaseItemList:" + purchaseItemList);

            if (purchaseItemList != null && purchaseItemList.size() > 0) {
                for (int i = 0; i < purchaseItemList.size(); i++) {
                    purchaseItem = purchaseItemList.get(i);
                    logger.debug("SMNLOG:purchaseItem------:" + purchaseItem);
                    if (restQty > 0 && purchaseItem.getRestQuantity() <= restQty) {
                        totalPurchasePrice = purchaseItem.getRestQuantity()*purchaseItem.getPurchaseRate();
                        salesItem.setPurchaseRate(purchaseItem.getPurchaseRate());

                        if(!Utils.isEmpty(salesItem.getTotalPurchaseDetails())){
                            logger.debug(" 1111 TotalPurchasePrice before:"+salesItem.getTotalPurchasePrice()+" totalPurchasePrice:"+totalPurchasePrice);
                            salesItem.setTotalPurchasePrice(salesItem.getTotalPurchasePrice()+totalPurchasePrice);
                            logger.debug(" 1111 TotalPurchasePrice after:"+salesItem.getTotalPurchasePrice());
                            salesItem.setTotalPurchaseDetails(salesItem.getTotalPurchaseDetails()
                                    + (!Utils.isEmpty(salesItem.getTotalPurchaseDetails())? ",":"" )+purchaseItem.getRestQuantity()+"*"+purchaseItem.getPurchaseRate());
                        }else {
                            logger.debug(" 222 TotalPurchasePrice before:" + salesItem.getTotalPurchasePrice());
                            salesItem.setTotalPurchasePrice(totalPurchasePrice);
                            salesItem.setTotalPurchaseDetails(purchaseItem.getRestQuantity() + "*" + purchaseItem.getPurchaseRate());
                            logger.debug(" 222 TotalPurchasePrice after:" + salesItem.getTotalPurchasePrice());
                        }

                        logger.debug("******:if :: getRestQuantity:" + purchaseItem.getRestQuantity() + " to update:"
                                + restQty+" totalPurchasePrice:"+totalPurchasePrice);

                        restQty = restQty - purchaseItem.getRestQuantity();
                        logger.debug("SMNLOG:restQty:" + restQty);
                        purchaseItem.setRestQuantity(0d);
                        salesItem.setBenefit(Utils.roundDouble(salesItem.getTotalPrice() - salesItem.getTotalPurchasePrice(),Constants.TWO_DECIMAL_GLOBAL_ROUND));
                        adminService.saveOrUpdateObject(purchaseItem);
                        adminService.saveOrUpdateObject(salesItem);

                    } else if (restQty > 0 && purchaseItem.getRestQuantity() > restQty) {
                        logger.debug("################ :else if :: getRestQuantity:" + purchaseItem.getRestQuantity() + " to update:"
                                + restQty+" totalPurchasePrice:"+totalPurchasePrice);
                        purchaseItem.setRestQuantity(purchaseItem.getRestQuantity() - restQty);
                        totalPurchasePrice = restQty*purchaseItem.getPurchaseRate();

                        salesItem.setPurchaseRate(purchaseItem.getPurchaseRate());

                        if(!Utils.isEmpty(salesItem.getTotalPurchaseDetails())){
                            logger.debug(" 3333 TotalPurchasePrice before:"+salesItem.getTotalPurchasePrice()+" totalPurchasePrice:"+totalPurchasePrice);
                            totalPurchasePrice = restQty*purchaseItem.getPurchaseRate();
                            salesItem.setTotalPurchasePrice(salesItem.getTotalPurchasePrice()+totalPurchasePrice);
                            logger.debug(" 3333 TotalPurchasePrice after:"+salesItem.getTotalPurchasePrice());
                            salesItem.setTotalPurchaseDetails(salesItem.getTotalPurchaseDetails()
                                    + (!Utils.isEmpty(salesItem.getTotalPurchaseDetails())? ",":"" )+restQty+"*"+purchaseItem.getPurchaseRate());
                        }else{
                            logger.debug(" 4444 ");
                            salesItem.setTotalPurchasePrice(totalPurchasePrice);
                            salesItem.setTotalPurchaseDetails(restQty+"*"+purchaseItem.getPurchaseRate());
                        }
                        restQty = 0d;
                        salesItem.setBenefit(Utils.roundDouble(salesItem.getTotalPrice() - salesItem.getTotalPurchasePrice(),Constants.TWO_DECIMAL_GLOBAL_ROUND));
                        adminService.saveOrUpdateObject(purchaseItem);
                        adminService.saveOrUpdateObject(salesItem);
                        return true;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("SMNLOG:ERROR:" + e);
            return false;
        }
        return true;
    }


    @RequestMapping(value = "/*/settings.do", method = RequestMethod.GET)
    public String settingsView(HttpServletRequest request, Model model) {
        logger.error("********* Settings view  controller *********");
        List<Settings> settingsList = new ArrayList<Settings>();
        Settings settings = null;
        try {
            settingsList = adminService.getSettingsList();
            if (settingsList != null && settingsList.size() > 0) {
                settings = settingsList.get(0);//always take the first one
                logger.error(":: settings found:: " + settings);
            }
            if (settings == null)
                settings = new Settings();


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(":: ERROR: error to load settings list:: " + e);
        }

        model.addAttribute("settings", settings);
        return "common/settings";
    }


    @RequestMapping(value = "/*/settings.do", method = RequestMethod.POST)
    public String savesettingsView(HttpServletRequest request, @ModelAttribute("settings") Settings settings) {
        logger.debug("********* Settings POST Controller *********");
        Long settingsId = settings.getId();
        try {
            logger.debug("SMNLOG:: unitOfMeasureId:: " + settingsId);
            adminService.saveOrUpdateObject(settings);

            if (settingsId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("settings.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("settings.save.success.msg"));
        } catch (Exception ex) {
            logger.error("Save settings exception:: " + ex);
            if (settingsId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("settings.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("settings.save.failed.msg"));

        }
        return "redirect:./landingPage.do";
    }

    @RequestMapping(value = "/*/getUnpostedSales.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean v(HttpServletRequest request) throws Exception {
        logger.info("********* Get Unposted Sales List Ajax Controller *********");
        DataModelBean dataModelBean = new DataModelBean();
        /* this params is for dataTables */
        String[] tableColumns = "sales_token_no,sales_date,total_amount,discount,userName".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String fromDate = request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "";
        String toDate = request.getParameter("toDate") != null ? request.getParameter("toDate") : "";
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        Integer salesReturn = 0;
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);
        logger.debug("SMNLOG:fromDateStr:" + fromDate + " toDateStr:" + toDate + " userId:" + userId);

        Date fmDate = !Utils.isEmpty(fromDate) ? Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, fromDate)) : Utils.startOfDate(new Date());
        Date tDate = !Utils.isEmpty(toDate) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, toDate)) : null;
        if (tDate == null)
            tDate = Utils.endOfDate(fmDate);

        logger.debug("SMNLOG:fmDate:" + fmDate + " tDate:" + tDate + " userId:" + userId);
        int unposted = 1;
        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;
        try {
            int totalRecords = adminJdbcService.getUnpostedSalesCount(salesReturn, fmDate, tDate, userId, unposted);// 0 for sales and 1 for sales Return
            logger.debug("SMNLOG: totalRecords:" + totalRecords);
            if (length < 0) {
                userDataMap = adminJdbcService.getUnpostedSales(start, totalRecords, sortColName, sortType, searchKey, salesReturn, fmDate, tDate, userId, unposted);
            } else {
                userDataMap = adminJdbcService.getUnpostedSales(start, length, sortColName, sortType, searchKey, salesReturn, fmDate, tDate, userId, unposted);
            }


                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load Unposted Sales details data:: " + ex);
        }

        return dataModelBean;
    }

    @RequestMapping(value = "/*/unpostedSale.do", method = RequestMethod.GET)
    public String unpostedSale(HttpServletRequest request, Model model) {
        logger.error("********* Un-posted Sale  controller *********");
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());
        Long opt = request.getParameter("opt") != null ? Long.parseLong(request.getParameter("opt")) : 0;
        try{
            if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel())) || user.getId() == userId) {

                List<User> userList = adminService.getAllUserList();
                model.addAttribute("userList", userList);
                model.addAttribute("fromDate", Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()));
                model.addAttribute("toDate", "");
                model.addAttribute("opt", opt);
                model.addAttribute("userId", userId);
                return "common/unpostedSales";
            }else{
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
                return "redirect:./landingPage.do";
            }
        }catch(Exception e){
            logger.debug("SMNLOG:ERROR:"+e);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
            return "redirect:./landingPage.do";
        }
    }


    @RequestMapping(value = "/*/getSaleReport.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getSaleReport(HttpServletRequest request) throws Exception {
        logger.info("********* Get Sale Report List Ajax Controller *********");
        DataModelBean dataModelBean = new DataModelBean();
        /* this params is for dataTables */
        String[] tableColumns = "sales_token_no,sales_date,p.name,c.name,si.purchase_rate,si.sales_rate,si.quantity,sItemTotalPrice,discount,userName".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 1;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "desc";
        String fromDate = request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "";
        String toDate = request.getParameter("toDate") != null ? request.getParameter("toDate") : "";
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        // to support sales return, sales and unposted sales report at he same time
        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) : 0;
        int salesReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a sales report");
        }else if(opt == 1){
            logger.debug("----- :this is a sales Return report");
            salesReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted sales report");
            salesReturn = 0;
            unposted = 1;
        }else if(opt == 3 ){
            logger.debug("----- :this is a unposted sales return report");
            salesReturn = 1;
            unposted = 1;
        }
        logger.debug("----- FINAL:salesReturn:"+salesReturn+" unposted:"+unposted);
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);
        logger.debug("SMNLOG:fromDateStr:" + fromDate + " toDateStr:" + toDate + " userId:" + userId);

        Date fmDate = !Utils.isEmpty(fromDate) ? Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, fromDate)) : Utils.startOfDate(new Date());
        Date tDate = !Utils.isEmpty(toDate) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, toDate)) : null;
        if (tDate == null)
            tDate = Utils.endOfDate(fmDate);

        logger.debug("SMNLOG:fmDate:" + fmDate + " tDate:" + tDate + " userId:" + userId);

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;
        try {
            int totalRecords = adminJdbcService.getSalesReportCount(searchKey, salesReturn, fmDate, tDate, userId,unposted);
            logger.debug("SMNLOG: totalRecords:" + totalRecords);
            if (length < 0) {
                userDataMap = adminJdbcService.getSalesReport(start, totalRecords, sortColName, sortType, searchKey, salesReturn, fmDate, tDate, userId,unposted);
            } else {
                userDataMap = adminJdbcService.getSalesReport(start, length, sortColName, sortType, searchKey, salesReturn, fmDate, tDate, userId,unposted);
            }

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load Sale Report details data:: " + ex);
        }

        return dataModelBean;
    }


    @RequestMapping(value = "/*/getPurchaseReport.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getPurchaseReport(HttpServletRequest request) throws Exception {
        logger.info("********* Get Purchase Report List Ajax Controller *********");
        DataModelBean dataModelBean = new DataModelBean();
        /* this params is for dataTables */
        String[] tableColumns = "purchase_token_no,purchase_date,p.name,c.name,pi.purchase_rate,pi.sales_rate,pi.quantity,pItemTotalPrice,userName".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        String fromDate = request.getParameter("fromDate") != null ? request.getParameter("fromDate") : "";
        String toDate = request.getParameter("toDate") != null ? request.getParameter("toDate") : "";
        Long userId = request.getParameter("userId") != null ? Long.parseLong(request.getParameter("userId")) : 0;
        // to support sales return, sales and unposted sales report at he same time
        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) : 0;
        int purchaseReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a sales report");
        }else if(opt == 1){
            logger.debug("----- :this is a sales Return report");
            purchaseReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted sales report");
            purchaseReturn = 0;
            unposted = 1;
        }else if(opt == 3 ){
            logger.debug("----- :this is a unposted sales return report");
            purchaseReturn = 1;
            unposted = 1;
        }
        logger.debug("----- FINAL:purchaseReturn:"+purchaseReturn+" unposted:"+unposted);
        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);
        logger.debug("SMNLOG:fromDateStr:" + fromDate + " toDateStr:" + toDate + " userId:" + userId);

        Date fmDate = !Utils.isEmpty(fromDate) ? Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, fromDate)) : Utils.startOfDate(new Date());
        Date tDate = !Utils.isEmpty(toDate) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, toDate)) : null;
        if (tDate == null)
            tDate = Utils.endOfDate(fmDate);

        logger.debug("SMNLOG:fmDate:" + fmDate + " tDate:" + tDate + " userId:" + userId);

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;
        try {
            int totalRecords = adminJdbcService.getPurchaseReportCount(searchKey, purchaseReturn, fmDate, tDate, userId,unposted);
            logger.debug("SMNLOG: totalRecords:" + totalRecords);
            if (length < 0) {
                userDataMap = adminJdbcService.getPurchaseReport(start, totalRecords, sortColName, sortType, searchKey, purchaseReturn, fmDate, tDate, userId,unposted);
            } else {
                userDataMap = adminJdbcService.getPurchaseReport(start, length, sortColName, sortType, searchKey, purchaseReturn, fmDate, tDate, userId,unposted);
            }

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load Purchase Report details data:: " + ex);
        }

        return dataModelBean;
    }


    @RequestMapping(value = "/*/saleReport.do", method = RequestMethod.GET)
    public String saleReportView(HttpServletRequest request, Model model) {
        logger.debug("******  Sale Report controller ********* ");
        SearchBean searchBean = request.getSession().getAttribute("searchBean") != null ? (SearchBean)request.getSession().getAttribute("searchBean"): new SearchBean();
        User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());

        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) :searchBean.getOpt();
        int salesReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a sales report");

        }else if(opt == 1){
            logger.debug("----- :this is a sales Return report");
            salesReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted sales report");
            salesReturn = 0;
            unposted = 1;
        }else if(opt == 3 ){
            logger.debug("----- :this is a unposted sales return report");
            salesReturn = 1;
            unposted = 1;
        }
        logger.debug("SMNLOG:searchBean:"+searchBean);
        List totalSaleList = new ArrayList();
        List totalSaleListForChart = new ArrayList();
        try{
            if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))) {
                if(Utils.isEmpty(searchBean.getFromDateStr()))
                searchBean.setFromDateStr(Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()));
                Date fmDate = Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getFromDateStr()));
                Date tDate = !Utils.isEmpty(searchBean.getToDateStr()) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getToDateStr())) : null;
                logger.debug("SMNLOG:FromDate:"+fmDate+" toDate:"+tDate);
                if(searchBean.getUserList() == null || (searchBean.getUserList() != null && searchBean.getUserList().size() < 1)){
                    List<User> userList = adminService.getAllUserList();
                    searchBean.setUserList(userList);
                }
                int groupByDateOrUser = 0;
//              0 = group by user and 1= group by date
                totalSaleList = adminJdbcService.getTotalSaleByDateAndUserId( fmDate, tDate, searchBean.getUserId() != null? searchBean.getUserId(): 0,salesReturn,unposted,groupByDateOrUser);
                groupByDateOrUser = 1;
                totalSaleListForChart = adminJdbcService.getTotalSaleByDateAndUserId( fmDate, tDate, searchBean.getUserId() != null? searchBean.getUserId(): 0,salesReturn,unposted,groupByDateOrUser);
                logger.debug("SMNLOG:totalSaleList:" + totalSaleList);
                searchBean.setOpt(opt);
                searchBean.setTotalSaleList(totalSaleList);
                searchBean.setDateWiseGroupByList(totalSaleListForChart);
                model.addAttribute("userId", searchBean.getUserId());
                model.addAttribute("searchBean", searchBean);
                return "common/saleReport";
            }else{
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
                return "redirect:./landingPage.do";
            }
        }catch(Exception e){
            logger.debug("SMNLOG:ERROR: Sale report:"+e);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
            return "redirect:./landingPage.do";
        }
    }

    @RequestMapping(value = "/*/saleReport.do", method = RequestMethod.POST)
    public String saleReportPost(HttpServletRequest request, @ModelAttribute("searchBean") SearchBean searchBean, Model model) {
        logger.debug("SMNLOG:: Sale Report POST Controller::");
        logger.debug("SMNLOG:searchBean:"+searchBean);
        request.getSession().setAttribute("searchBean",searchBean);
        return "redirect:./saleReport.do";
    }

    @RequestMapping(value = "/*/incomeReport.do", method = RequestMethod.GET)
    public String incomeReportView(HttpServletRequest request, Model model) {
        logger.debug("******  Income Report controller ********* ");
        SearchBean searchBean = request.getSession().getAttribute("searchBean") != null ? (SearchBean)request.getSession().getAttribute("searchBean"): new SearchBean();
        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) :searchBean.getOpt();
        int salesReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a sales report");

        }else if(opt == 1){
            logger.debug("----- :this is a sales Return report");
            salesReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted sales report");
            salesReturn = 0;
            unposted = 1;
        }
        logger.debug("SMNLOG:searchBean:"+searchBean);
        List totalIncomeList = new ArrayList();
        List totalIncomeListGroupByDate = new ArrayList();
        try{
            if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))) {

                if(Utils.isEmpty(searchBean.getFromDateStr()))
                searchBean.setFromDateStr(Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()));

                Date fmDate = Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getFromDateStr()));
                Date tDate = !Utils.isEmpty(searchBean.getToDateStr()) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getToDateStr())) : null;
                logger.debug("SMNLOG:FromDate:"+fmDate+" toDate:"+tDate);
                if(searchBean.getUserList() == null || (searchBean.getUserList() != null && searchBean.getUserList().size() < 1)){
                    List<User> userList = adminService.getAllUserList();
                    searchBean.setUserList(userList);
                }
                int groupByDateOrUser = 0;
//              0 = group by user and 1= group by date
                totalIncomeList = adminJdbcService.getTotalIncomeByDateAndUserId(fmDate, tDate, searchBean.getUserId() != null ? searchBean.getUserId() : 0, salesReturn, unposted,groupByDateOrUser);
                groupByDateOrUser = 1;
                totalIncomeListGroupByDate = adminJdbcService.getTotalIncomeByDateAndUserId(fmDate, tDate, searchBean.getUserId() != null ? searchBean.getUserId() : 0, salesReturn, unposted,groupByDateOrUser);

                logger.debug("SMNLOG:totalIncomeList:" + totalIncomeList);
                searchBean.setOpt(opt);
                searchBean.setTotalIncomeList(totalIncomeList);
                searchBean.setDateWiseGroupByList(totalIncomeListGroupByDate);

                model.addAttribute("searchBean", searchBean);
                return "common/incomeReport";
            }else{
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
                return "redirect:./landingPage.do";
            }
        }catch(Exception e){
            logger.debug("SMNLOG:ERROR: Sale report:"+e);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
            return "redirect:./landingPage.do";
        }
    }

    @RequestMapping(value = "/*/incomeReport.do", method = RequestMethod.POST)
    public String incomeReportPost(HttpServletRequest request, @ModelAttribute("searchBean") SearchBean searchBean, Model model) {
        logger.debug("SMNLOG:: income Report POST Controller::");
        logger.debug("SMNLOG:searchBean:"+searchBean);
        request.getSession().setAttribute("searchBean",searchBean);
        return "redirect:./incomeReport.do";
    }


    @RequestMapping(value = "/*/incomeReportChart.do", method = RequestMethod.GET)
    public String incomeReportAsChartView(HttpServletRequest request, Model model) {
        logger.debug("******  Income Report Chart controller ********* ");
        SearchBean searchBean = request.getSession().getAttribute("searchBean") != null ? (SearchBean)request.getSession().getAttribute("searchBean"): new SearchBean();
        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) :searchBean.getOpt();
        int salesReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a sales report");

        }else if(opt == 1){
            logger.debug("----- :this is a sales Return report");
            salesReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted sales report");
            salesReturn = 0;
            unposted = 1;
        }
        logger.debug("SMNLOG:searchBean:"+searchBean);
        List totalIncomeList = new ArrayList();
        List totalIncomeListGroupByDate = new ArrayList();
        try{
            if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))) {

                if(Utils.isEmpty(searchBean.getFromDateStr()))
                    searchBean.setFromDateStr(Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()));

                Date fmDate = Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getFromDateStr()));
                Date tDate = !Utils.isEmpty(searchBean.getToDateStr()) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getToDateStr())) : null;
                logger.debug("SMNLOG:FromDate:"+fmDate+" toDate:"+tDate);
                if(searchBean.getUserList() == null || (searchBean.getUserList() != null && searchBean.getUserList().size() < 1)){
                    List<User> userList = adminService.getAllUserList();
                    searchBean.setUserList(userList);
                }
                int groupByDateOrUser = 0;
//              0 = group by user and 1= group by date
                totalIncomeList = adminJdbcService.getTotalIncomeByDateAndUserId(fmDate, tDate, searchBean.getUserId() != null ? searchBean.getUserId() : 0, salesReturn, unposted,groupByDateOrUser);
                groupByDateOrUser = 1;
                totalIncomeListGroupByDate = adminJdbcService.getTotalIncomeByDateAndUserId(fmDate, tDate, searchBean.getUserId() != null ? searchBean.getUserId() : 0, salesReturn, unposted,groupByDateOrUser);

                logger.debug("SMNLOG:totalIncomeList:" + totalIncomeList);
                searchBean.setOpt(opt);
                searchBean.setTotalIncomeList(totalIncomeList);
                searchBean.setDateWiseGroupByList(totalIncomeListGroupByDate);

                model.addAttribute("searchBean", searchBean);
                return "common/incomeReport";
            }else{
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
                return "redirect:./landingPage.do";
            }
        }catch(Exception e){
            logger.debug("SMNLOG:ERROR: Sale report:"+e);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
            return "redirect:./landingPage.do";
        }
    }

    @RequestMapping(value = "/*/incomeReportChart.do", method = RequestMethod.POST)
    public String incomeReportAsChartPost(HttpServletRequest request, @ModelAttribute("searchBean") SearchBean searchBean, Model model) {
        logger.debug("SMNLOG:: income ReportChart POST Controller::");
        logger.debug("SMNLOG:searchBean:"+searchBean);
        request.getSession().setAttribute("searchBean",searchBean);
        return "redirect:./incomeReport.do";
    }


    @RequestMapping(value = "/*/updateSalesDiscounts.do", method = RequestMethod.GET)
    public
    @ResponseBody
    String updateSalesDiscounts(HttpServletRequest request) {
        logger.debug("********* Update Sales Discounts AJAX Controller *********");
        try {
            Long sId = request.getParameter("sId") != null ? Long.parseLong(request.getParameter("sId")) : 0;
            Double totalPrice = request.getParameter("totalPrice") != null ? Double.parseDouble(request.getParameter("totalPrice")) : 0;
            Double discount = request.getParameter("discount") != null ? Double.parseDouble(request.getParameter("discount")) : 0;
            int salesReturn = request.getParameter("salesReturn") != null ? Integer.parseInt(request.getParameter("salesReturn")) : 0;

            logger.debug(" sId:" + sId + " totalPrice:" + totalPrice+" salesReturn:"+salesReturn);
            if (sId > 0) {
                Sales sales = adminService.getSale(sId, salesReturn);
                sales.setTotalAmount(totalPrice);
                sales.setDiscount(discount);
                adminService.saveOrUpdateObject(sales);
            } else {
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("SMNLOG:ERROR: Update Sales Discounts:" + e);
            return "false";
        }
        return "true";
    }


    @RequestMapping(value = "/*/purchaseReport.do", method = RequestMethod.GET)
    public String purchaseReportView(HttpServletRequest request, Model model) {
        logger.debug("******  Purchase Report controller ********* ");
        SearchBean searchBean = request.getSession().getAttribute("searchBean") != null ? (SearchBean)request.getSession().getAttribute("searchBean"): new SearchBean();
        User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());

        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) :searchBean.getOpt();
        int purchaseReturn = 0;
        int unposted = 0;
        if(opt == 0){
            logger.debug("----- :this is a purchase report");

        }else if(opt == 1){
            logger.debug("----- :this is a purchase Return report");
            purchaseReturn = 1;
            unposted = 0;
        }else if(opt == 2 ){
            logger.debug("----- :this is a unposted purchase report");
            purchaseReturn = 0;
            unposted = 1;
        }else if(opt == 3 ){
            logger.debug("----- :this is a unposted purchase return report");
            purchaseReturn = 1;
            unposted = 1;
        }
        logger.debug("SMNLOG:searchBean:"+searchBean);
        List totalSaleList = new ArrayList();
        List totalPurchaseListGroupByDate = new ArrayList();
        try{
            if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))) {
                if(Utils.isEmpty(searchBean.getFromDateStr()))
                    searchBean.setFromDateStr(Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()));
                Date fmDate = Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getFromDateStr()));
                Date tDate = !Utils.isEmpty(searchBean.getToDateStr()) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getToDateStr())) : null;
                logger.debug("SMNLOG:FromDate:"+fmDate+" toDate:"+tDate);
                if(searchBean.getUserList() == null || (searchBean.getUserList() != null && searchBean.getUserList().size() < 1)){
                    List<User> userList = adminService.getAllUserList();
                    searchBean.setUserList(userList);
                }
                 if(searchBean.getCompanyList() == null || (searchBean.getCompanyList() != null && searchBean.getCompanyList().size() < 1)){
                    List companyList = adminJdbcService.getCompanyList("");
                    searchBean.setCompanyList(companyList);
                }
                int groupByDateOrUser = 0;
//              0 = group by user and 1= group by date

                totalSaleList = adminJdbcService.getTotalPurchaseByDateAndUserId(fmDate, tDate, searchBean.getUserId() != null ? searchBean.getUserId() : 0,
                        purchaseReturn, unposted,groupByDateOrUser,searchBean.getCompanyId() != null ? searchBean.getCompanyId() : 0);
                groupByDateOrUser = 1;
                totalPurchaseListGroupByDate = adminJdbcService.getTotalPurchaseByDateAndUserId(fmDate, tDate,
                        searchBean.getUserId() != null ? searchBean.getUserId() : 0, purchaseReturn, unposted,groupByDateOrUser
                        ,searchBean.getCompanyId() != null ? searchBean.getCompanyId() : 0);
                logger.debug("SMNLOG:totalSaleList:"+totalSaleList);
                searchBean.setOpt(opt);
                searchBean.setDateWiseGroupByList(totalPurchaseListGroupByDate);
                searchBean.setTotalPurchaseList(totalSaleList);
                model.addAttribute("userId", searchBean.getUserId());
                model.addAttribute("searchBean", searchBean);
                return "common/purchaseReport";
            }else{
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
                return "redirect:./landingPage.do";
            }
        }catch(Exception e){
            logger.debug("SMNLOG:ERROR: Purchase report:"+e);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
            return "redirect:./landingPage.do";
        }
    }

    @RequestMapping(value = "/*/purchaseReport.do", method = RequestMethod.POST)
    public String purchaseReportPost(HttpServletRequest request, @ModelAttribute("searchBean") SearchBean searchBean) {
        logger.debug("SMNLOG:: Purchase Report POST Controller::");
        logger.debug("SMNLOG:searchBean:"+searchBean);
        request.getSession().setAttribute("searchBean",searchBean);
        return "redirect:./purchaseReport.do";
    }

    @RequestMapping(value = "/*/clearAllData.do", method = RequestMethod.GET)
    public String clearAllData(HttpServletRequest request,Model model) {
        logger.debug("***************** clear All Data GET Controller ****************");
        User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());
        try {
        if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))) {
            logger.debug("**** clearing All Data .. ***");
                adminJdbcService.clearAllData();
            Utils.setGreenMessage(request,"All Data is clear now !!!!!!!!!!!!! ");

        }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("SMNLOG:ERROR CLEAR DATA:"+e);
            Utils.setErrorMessage(request, "Failed to clear Data. Try again");
        }
        return "redirect:./landingPage.do";
    }

    @RequestMapping(value = "/*/stockReport.do", method = RequestMethod.GET)
    public String stockReportView(HttpServletRequest request, Model model) {
        logger.debug("******  Stock Report controller ********* ");
        SearchBean searchBean = request.getSession().getAttribute("searchBean") != null ? (SearchBean)request.getSession().getAttribute("searchBean"): new SearchBean();
//        int opt = request.getParameter("opt") != null ? Integer.parseInt(request.getParameter("opt")) :searchBean.getOpt();

        logger.debug("SMNLOG:searchBean:"+searchBean);
        Map totalStock = new HashMap();
        Map totalStockReturn = new HashMap();
        int purchaseReturn = 0;
        int unposted = 0;
        List totalIncomeListGroupByDate = new ArrayList();
        try{
            if ((Utils.isInRole(Role.ROLE_ADMIN.getLabel()) || Utils.isInRole(Role.ROLE_SUPER_ADMIN.getLabel()))) {

                if(Utils.isEmpty(searchBean.getFromDateStr()))
                    searchBean.setFromDateStr(Utils.getStringFromDate(Constants.DATE_FORMAT, new Date()));

                Date fmDate = Utils.startOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getFromDateStr()));
                Date tDate = !Utils.isEmpty(searchBean.getToDateStr()) ? Utils.endOfDate(Utils.getDateFromString(Constants.DATE_FORMAT, searchBean.getToDateStr())) : null;
                logger.debug("SMNLOG:FromDate:"+fmDate+" toDate:"+tDate);
                if(searchBean.getUserList() == null || (searchBean.getUserList() != null && searchBean.getUserList().size() < 1)){
                    List<User> userList = adminService.getAllUserList();
                    searchBean.setUserList(userList);
                }
                totalStock = adminJdbcService.getStockTotal(fmDate, purchaseReturn,unposted);
                purchaseReturn = 1;
                totalStockReturn = adminJdbcService.getStockTotal(fmDate, purchaseReturn,unposted);

                logger.debug("SMNLOG:totalStock:" + totalStock+" totalStockReturn:"+totalStockReturn);
                if(totalStock != null){
                    Double tpRateFromTotalStock = totalStock.get("tpRate") != null ? (Double)totalStock.get("tpRate"):0;
                    Double tpRateFromTotalStockReturn = totalStockReturn.get("tpRate") != null ? (Double)totalStockReturn.get("tpRate"):0;

                    Double mrpRateFromTotalStock = totalStock.get("mrpRate") != null ? (Double)totalStock.get("mrpRate"):0;
                    Double mrpRateFromTotalStockReturn = totalStockReturn.get("mrpRate") != null ? (Double)totalStockReturn.get("mrpRate"):0;

                    searchBean.setTpRate(tpRateFromTotalStock);
                    searchBean.setTpRateReturn(tpRateFromTotalStockReturn);
                    searchBean.setMrpRate(mrpRateFromTotalStock);
                    searchBean.setMrpRateReturn(mrpRateFromTotalStockReturn);

                    searchBean.setTpRateTotal(tpRateFromTotalStock - tpRateFromTotalStockReturn);
                    searchBean.setMrpRateTotal(mrpRateFromTotalStock - mrpRateFromTotalStockReturn);
                }
                model.addAttribute("searchBean", searchBean);
                return "common/stockReport";
            }else{
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
                return "redirect:./landingPage.do";
            }
        }catch(Exception e){
            logger.debug("SMNLOG:ERROR: Sale report:"+e);
            Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("accessDenied.AccessDeniedMessage"));
            return "redirect:./landingPage.do";
        }
    }

    @RequestMapping(value = "/*/stockReport.do", method = RequestMethod.POST)
    public String stockReportPost(HttpServletRequest request, @ModelAttribute("searchBean") SearchBean searchBean, Model model) {
        logger.debug("SMNLOG:: Stock Report POST Controller::");
        logger.debug("SMNLOG:searchBean:"+searchBean);
        request.getSession().setAttribute("searchBean",searchBean);
        return "redirect:./stockReport.do";
    }

    @RequestMapping(value = "/*/getStockReport.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getStockReport(HttpServletRequest request) throws Exception {
        logger.info("********* Get Stock Report List Ajax Controller *********");
        DataModelBean dataModelBean = new DataModelBean();
        /* this params is for dataTables */
        String[] tableColumns = "p.name,c.name,purchaseItemQty,pItemPRate,pItemSaleRate,rest_quantity,tpRate,mrpRate".split(",");
        int start = request.getParameter(Constants.IDISPLAY_START) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_START)) : 0;
        int length = request.getParameter(Constants.IDISPLAY_LENGTH) != null ? Integer.parseInt(request.getParameter(Constants.IDISPLAY_LENGTH)) : 5;
        int sEcho = request.getParameter(Constants.sEcho) != null ? Integer.parseInt(request.getParameter(Constants.sEcho)) : 0;
        int iSortColIndex = request.getParameter(Constants.iSortCOL) != null ? Integer.parseInt(request.getParameter(Constants.iSortCOL)) : 0;
        String searchKey = request.getParameter(Constants.sSearch) != null ? request.getParameter(Constants.sSearch) : "";
        String sortType = request.getParameter(Constants.sortType) != null ? request.getParameter(Constants.sortType) : "asc";
        Long productId = request.getParameter("productId") != null ? Long.parseLong(request.getParameter("productId")) : 0;
        Long companyId = request.getParameter("companyId") != null ? Long.parseLong(request.getParameter("companyId")) : 0;

        String sortColName = "";
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);
        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        int purchaseReturn = 0;
        int unposted = 0;

        Map<String, Object> userDataMap;
        try {
            int totalRecords = adminJdbcService.getStockReportCount(searchKey,productId,companyId);
            logger.debug("SMNLOG: totalRecords:" + totalRecords);
            if (length < 0) {
                userDataMap = adminJdbcService.getStockReportDetails(start, totalRecords, sortColName, sortType, searchKey, productId,companyId);
            } else {
                userDataMap = adminJdbcService.getStockReportDetails(start, length, sortColName, sortType, searchKey, productId, companyId);
            }

                /*
                * DataModelBean is a bean of Data table to
                * handle data Table search, paginatin operation very simply
                */
            dataModelBean.setAaData((List) userDataMap.get("data"));
            if (!Utils.isEmpty(searchKey)) {
                dataModelBean.setiTotalDisplayRecords((Integer) userDataMap.get("total"));
            } else {
                dataModelBean.setiTotalDisplayRecords(totalRecords);
            }
            dataModelBean.setiTotalRecords(totalRecords);
            dataModelBean.setsEcho(sEcho);
            dataModelBean.setiDisplayStart(start);
            dataModelBean.setiDisplayLength(totalRecords);

        } catch (Exception ex) {
            logger.error(":: ERROR:: Failed to load Stock Report details data:: " + ex);
        }

        return dataModelBean;
    }


}




