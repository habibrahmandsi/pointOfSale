package com.dsoft.controller;

import com.dsoft.entity.Role;
import com.dsoft.entity.User;
import com.dsoft.service.AdminJdbcService;
import com.dsoft.service.AdminService;
import com.dsoft.util.Utils;
import com.dsoft.validation.UserValidation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Controller
@SessionAttributes({"user"})
public class LoginController {  // to handle login related task

    private static Logger logger = Logger.getLogger(LoginController.class);
    @Autowired(required = true)
    private AdminService adminService;
    @Autowired(required = true)
    private AdminJdbcService adminJdbcService;
    @Autowired(required = true)
    private UserValidation userValidator;

    /*
    * Method to show login page
    * @param @RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model
    * @return type String
    */
    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public String redirectLogin(@RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model) {
        if (!Utils.isNullOrEmpty(error)) {
            logger.debug("SMNLOG:error:"+error);
            model.addAttribute("error", Utils.getMessageBundlePropertyValue("login.error"));
        }
        return "login";
    }

    /*
    * Method to show login page
    * @param @RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model
    * @return type String
    */
    @RequestMapping(value = "/*/landingPage.do", method = RequestMethod.GET)
    public String landingPageView(HttpServletRequest request, Model model) {
        return "admin/landingPage";
    }


    /*
    * Method to show landing page for successful login  or login page again
    * @param HttpServletRequest request, Model model
    * @return type String
    */
    @RequestMapping(value = "/forward.do", method = RequestMethod.GET)
    public String forwardOnRole(HttpServletRequest request, Model model) {
        logger.debug("Forward Controller Start.");
        if (Utils.isInRole(Role.SUPER_ADMIN.getLabel())) {
            logger.debug("Forward Controller Redirect AS " + Role.SUPER_ADMIN.getLabel());
            return "redirect:./superAdmin/superAdmin.do";
        }else if(Utils.isInRole(Role.ROLE_ADMIN.getLabel())){
            logger.debug("Forward Controller Redirect AS " + Role.ROLE_EMPLOYEE.getLabel());
            return "redirect:./admin/landingPage.do";
        }

/*        if (Utils.isInRole(Role.ADMIN.getLabel())) {
            logger.debug("Forward Controller Redirect AS " + Role.ADMIN.getLabel());
            return "redirect:/admin/landingPage.do";
        }else if(Utils.isInRole(Role.EMPLOYEE.getLabel())){
            logger.debug("Forward Controller Redirect AS " + Role.EMPLOYEE.getLabel());
            return "redirect:/common/landingPage.do";
        } else if(Utils.isInRole(Role.LEGAL.getLabel())){
            logger.debug("Forward Controller Redirect AS " + Role.LEGAL.getLabel());
            return "redirect:/legal/landingPage.do";
        }else if(Utils.isInRole(Role.IA_ANALYST.getLabel())){
            logger.debug("Forward Controller Redirect AS " + Role.IA_ANALYST.getLabel());
            return "redirect:/iaanalyst/landingPage.do";
        }else if(Utils.isInRole(Role.IA_MANAGER.getLabel())){
            logger.debug("Forward Controller Redirect AS " + Role.IA_MANAGER.getLabel());
            return "redirect:/iamanager/landingPage.do";
        }else if(Utils.isInRole(Role.COMPLIANCE.getLabel())){
            logger.debug("Forward Controller Redirect AS " + Role.COMPLIANCE.getLabel());
            return "redirect:/compliance/landingPage.do";
        } else {
            return "redirect:/login.do";
        }*/
        return "redirect:./login.do";
    }

    /*
    * Method to show access denied page for unauthorised or invalid request
    * @param HttpServletRequest request, Model model
    * @return type String
    */
    @RequestMapping(value = "/access-denied.do", method = RequestMethod.GET)
    public String accessDenied(HttpServletRequest request, Model model) {
        logger.debug("SMNLOG:ACCESS DENIED");
        return "accessDenied";
    }


    @RequestMapping(value = "/*/sendAnEmail.do", method = RequestMethod.GET)
    public String sendAnEmail( HttpServletRequest request, @RequestParam("name") String name, Model model) {
        logger.debug("Send email controller start.");
        String subject = Utils.getApplicationPropertyValue("reset.mail.subject");
        String from = Utils.getApplicationPropertyValue("reset.password.mail.sender");
        String body = "";
       /* User user = null;
        try {
            Object userObj = adminService.getAbstractBaseEntityByString(Constants.USER, "user_name", name);
            if(userObj == null) {
                model.addAttribute("error", Utils.getMessageBundlePropertyValue("user.not.found.error"));
                return "redirect:/login.do";
            } else {
                user = (User) userObj;
            }
            String[] to = {user.getEmail()};
            String[] bcc={};
            body = Utils.getStringFromFile(LoginController.class.getResource("/../../resources/reset-password-mail.txt").getPath());
            body = body.replace("?", user.getUserName());
            body = body.replace("#", "http://" + request.getLocalAddr() + ":" + request.getLocalPort()+request.getContextPath() + "/admin/resetPassword.do?name=" + Utils.encryptSt(user.getUserName().trim()));
            logger.debug("AMLOG:: User name decript: " + Utils.decryptSt(Utils.encryptSt(user.getUserName().trim())) );
            Utils.sendAnEmail(from, to,bcc, subject, body);
            model.addAttribute("userMail", user.getEmail());
        } catch (Exception e) {
            logger.debug("CERROR:: Failed to send mail. " + e);
            logger.debug("CERROR:: Failed to send mail description. " + e.getMessage());
        }
*/
        logger.debug("Send email controller end.");
        return "admin/emailRecovery";
    }
    @RequestMapping(value = "/*/resetPassword.do", method = RequestMethod.GET)
    public String resetUserPassword(HttpServletRequest request, @RequestParam("name") String name, Model model ) {
        logger.debug("Password change controller start.");
        User tempUser = new User();

       /* try {
             logger.debug("AMLOG:: User name: " + Utils.decryptSt(name) );
             tempUser = (User)adminService.getAbstractBaseEntityByString(Constants.USER, "user_name", Utils.decryptSt(name) );
             logger.debug("User id: " + tempUser.getId());
        } catch (Exception ex) {
            logger.debug("CERROR:: Reset password exception: " + ex.getMessage());
            logger.debug("CERROR:: Reset password exception description: " + ex);
        }*/
        logger.debug("Password change controller end.");
        model.addAttribute("user",tempUser);
        return "admin/resetPassword";
    }
    @RequestMapping(value = "/*/resetPassword.do", method = RequestMethod.POST)
    public String saveNewPassword(HttpServletRequest request, @ModelAttribute("user") User tempUser, BindingResult result) {
        logger.debug("Save new password start.");
       /* try {

            userValidator.validateNewPassword(tempUser, result, adminService);
            if(result.hasErrors()) {
                logger.debug("Conformation password not match.");
                return "admin/resetPassword";
            } else {
                tempUser.setPassword(Utils.encrypt(tempUser.getGivenPassword()));
                adminService.saveOrUpdate(tempUser);
            }

        } catch (Exception e) {
            logger.debug("CERROR:: Save new password exception: " + e);
            logger.debug("CERROR:: Save new password exception description: " + e.getMessage());
        }*/

        logger.debug("Save new password end.");
        return "redirect:/login.do";
    }

    /*
* Method to show login page
* @param @RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model
* @return type String
*/
    @RequestMapping(value = "home.do", method = RequestMethod.GET)
    public String getHome(HttpServletRequest request, Model model) {
        logger.debug("Home page Controller:");
        return "common/home";
    }
/*
* Method to show login page
* @param @RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model
* @return type String
*/
    @RequestMapping(value = "about.do", method = RequestMethod.GET)
    public String getAbout(HttpServletRequest request, Model model) {
        logger.debug("About page Controller:");
        return "common/about";
    }
/*
* Method to show login page
* @param @RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model
* @return type String
*/
    @RequestMapping(value = "contact.do", method = RequestMethod.GET)
    public String getContact(HttpServletRequest request, Model model) {
        logger.debug("Contact page Controller:");
        return "common/contact";
    }

    /*
* Method to show login page
* @param @RequestParam(value = "error", required = false) String error, HttpServletRequest request, Model model
* @return type String
*/
    @RequestMapping(value = "feature.do", method = RequestMethod.GET)
    public String getFeature(HttpServletRequest request, Model model) {
        logger.debug("Feature page Controller:");
        return "common/feature";
    }
}

