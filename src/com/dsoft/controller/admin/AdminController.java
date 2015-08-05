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
            logger.debug("SMNLOG:: productId:: " + productId);
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
        logger.debug("SMNLOG:iSortColIndex:" + iSortColIndex + " sortType:" + sortType + " searchKey:" + searchKey);

        // sorting related operation for data Tables

        sortColName = tableColumns[iSortColIndex];
        logger.debug("SMNLOG:sortColName:" + sortColName);

        String trackingDetailsDataStr = null;
        Map<String, Object> userDataMap;

        try {
            int totalRecords = adminService.getEntitySize(Constants.PRODUCT_CLASS);
            if (length < 0) {
                userDataMap = adminJdbcService.getProducts(start, totalRecords, sortColName, sortType, searchKey);
            } else {
                userDataMap = adminJdbcService.getProducts(start, length, sortColName, sortType, searchKey);
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
        String[] tableColumns = "name,agent_name,agent_cell_no,permanent_address".split(",");
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
        logger.error("SMNLOG:: upsert Purchase view controller ::");
        Purchase purchase = new Purchase();
        List<PurchaseItem> purchaseItemList = null;
        Long purchaseId = request.getParameter("purchaseId") != null ? Long.parseLong(request.getParameter("purchaseId")) : 0;
        logger.debug("SMNLOG:purchaseId:" + purchaseId);
        int purchaseReturn = 0; // for purchase
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
        logger.debug("SMNLOG:: purchase POST Controller::");
        logger.debug("SMNLOG:: purchase::" + purchase);
        Long purchaseId = purchase.getId();
        try {
            logger.debug("SMNLOG:: purchaseId:: " + purchaseId);
            //if(user.getJoiningDate() == null)user.setJoiningDate(new Date());
            if (purchaseId != null) { // when to update
                if (Utils.isEmpty(purchase.getPurchaseTokenNo()))
                    purchase.setPurchaseTokenNo(Utils.generateUniqueId("P"));
            } else { // when to save
                purchase.setPurchaseDate(new Date());
                purchase.setPurchaseTokenNo(Utils.generateUniqueId("P"));
            }


            User user = (User) adminService.getAbstractBaseEntityByString(Constants.USER, "userName", Utils.getLoggedUserName());
            purchase.setUser(user);
            adminService.saveOrUpdatePurchase(purchase);

            if (purchaseId != null)
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.update.success.msg"));
            else
                Utils.setGreenMessage(request, Utils.getMessageBundlePropertyValue("purchase.save.success.msg") + "<b>&nbsp;PURCHASE INVOICE NO:</b>&nbsp;<b style='color:red'>" + purchase.getPurchaseTokenNo() + "</b>");
        } catch (Exception ex) {
            logger.error("Save Purchase exception:: " + ex);
            if (purchaseId != null)
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.update.failed.msg"));
            else
                Utils.setErrorMessage(request, Utils.getMessageBundlePropertyValue("purchase.save.failed.msg"));

        }
        return "redirect:./purchaseList.do";
    }


    @RequestMapping(value = "/*/deleteAnyObject.do", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean deleteAnyObject(HttpServletRequest request, Model model) {
        logger.error("SMNLOG:: delete Any Object controller ::");
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
        logger.error("SMNLOG: :: purchase view controller :: ");
        model.addAttribute("opt", 0);
        return "common/purchaseList";
    }

    @RequestMapping(value = "/*/getPurchases.do", method = RequestMethod.GET)
    public
    @ResponseBody
    DataModelBean getPurchases(HttpServletRequest request) throws Exception {
        logger.info(":: Get Purchase List Ajax Controller ::");
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
            int totalRecords = adminService.getEntitySize(Constants.PRODUCT_TYPE_CLASS);
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
        logger.error("SMNLOG: :: delete Purchase controller :: ");
        Long purchaseId = request.getParameter("purchaseId") != null ? Long.parseLong(request.getParameter("purchaseId")) : 0;
        Long opt = request.getParameter("opt") != null ? Long.parseLong(request.getParameter("opt")) : 0;
        logger.error("SMNLOG: :: purchaseId :: " + purchaseId);
        List<PurchaseItem> purchaseItemList = new ArrayList<PurchaseItem>();
        PurchaseItem purchaseItem;
        Boolean isError = true;
        try {
            if (purchaseId > 0) {
                purchaseItemList = adminService.getPurchaseItemListByPurchaseId(purchaseId);
                if (purchaseItemList != null && purchaseItemList.size() > 0) {
                    for (int i = 0; i < purchaseItemList.size(); i++) {
                        purchaseItem = purchaseItemList.get(i);
                        adminService.deleteObject(purchaseItem);
                    }
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
        if (opt == 1)
            return "redirect:./purchaseReturnList.do";

        return "redirect:./purchaseList.do";

    }


    @RequestMapping(value = "/*/upsertPurchaseReturn.do", method = RequestMethod.GET)
    public String purchaseReturn(HttpServletRequest request, Model model) {
        logger.error("SMNLOG:: Purchase return view controller ::");
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
                }

                purchaseItemList = adminService.getPurchaseItemListByPurchaseId(purchaseId);
                logger.debug("SMNLOG:purchaseItemList size:" + purchaseItemList.size());

                if (purchaseItemList != null && purchaseItemList.size() > 0) {
                    for (PurchaseItem purchaseItem : purchaseItemList) {
                        if (purchaseItem != null) {
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
        logger.debug("SMNLOG:: purchase return POST Controller::");
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
        logger.error("SMNLOG: :: purchase Return list controller :: ");
        model.addAttribute("opt", 1);
        return "common/purchaseList";
    }

    @RequestMapping(value = "/superAdmin/generateKey.do", method = RequestMethod.POST)
    public String generateKey(HttpServletRequest request, @ModelAttribute("productKey") ProductKey productKey, Model model) {
        logger.error("SMNLOG: :: generateKey controller :: ");
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
            logger.debug("SMNLOG:keyValue::" + keyValue+" :: key"+key);

            String message = productKey.getUserName() + Constants.P_KEY_SEPARATOR
                    +  productKey.getPrivateKey() + Constants.P_KEY_SEPARATOR
                    +  Utils.getStringFromDate(Constants.DATE_FORMAT, new Date())+ Constants.P_KEY_SEPARATOR
                    +  productKey.getValidUpTo();

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

}




