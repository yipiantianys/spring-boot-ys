package org.study.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.study.model.SysUser;

/**
 * 【暂未使用】是旧版用于测试权限的，带页面跳转效果
 * @author ys
 *
 */
@RestController
@RequestMapping("/test")
public class OldLoginTestController {

	private static Logger logger = LoggerFactory.getLogger(LoginContoller.class);

	 /**
     * 页面跳转验证权限方式：========================================
     */
    //跳转到登录表单页面
    @RequestMapping(value = "/toLogin")
    public ModelAndView toLogin() {
        return new ModelAndView("login");
    }
    
    //登录成功后，跳转的页面
    @RequestMapping("/toSuccess")
    public ModelAndView index(Model model) {
        return new ModelAndView("success");
    }

    //未登录，可以访问的页面
    @RequestMapping("/toIndex")
    public ModelAndView list(Model model) {
        return new ModelAndView("index");
    }
    
    //登陆验证，这里方便url测试，正式上线需要使用POST方式提交
    @RequestMapping(value = "/submitLogin")
    public ModelAndView index(SysUser user) {
        if (user.getLoginId() != null && user.getPwd() != null) {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getLoginId(), user.getPwd(), "login");
            Subject currentUser = SecurityUtils.getSubject();
            logger.info("对用户[" + user.getLoginId() + "]进行登录验证..验证开始");
            try {
                currentUser.login( token );
                //验证是否登录成功
                if (currentUser.isAuthenticated()) {
                    logger.info("用户[" + user.getLoginId() + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                    System.out.println("用户[" + user.getLoginId() + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                    return new ModelAndView("redirect:/toSuccess");
                } else {
                    token.clear();
                    System.out.println("用户[" + user.getLoginId() + "]登录认证失败,重新登陆");
                    return new ModelAndView("redirect:/toLogin");
                }
            } catch ( UnknownAccountException uae ) {
                logger.info("对用户[" + user.getLoginId() + "]进行登录验证..验证失败-username wasn't in the system");
            } catch ( IncorrectCredentialsException ice ) {
                logger.info("对用户[" + user.getLoginId() + "]进行登录验证..验证失败-password didn't match");
            } catch ( LockedAccountException lae ) {
                logger.info("对用户[" + user.getLoginId() + "]进行登录验证..验证失败-account is locked in the system");
            } catch ( AuthenticationException ae ) {
                logger.error(ae.getMessage());
            }
        }
        return new ModelAndView("login");
    }
      
    /**
     * ajax登录请求接口方式登陆
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value="/ajaxLogin",method= RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> submitLogin(@RequestParam(value = "nickname") String username, @RequestParam(value = "pswd") String password) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        try {

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            SecurityUtils.getSubject().login(token);
            resultMap.put("status", 200);
            resultMap.put("message", "登录成功");

        } catch (Exception e) {
            resultMap.put("status", 500);
            resultMap.put("message", e.getMessage());
        }
        return resultMap;
    }

    //登出
    @RequestMapping(value = "/toLogout")
    public ModelAndView logout(){
        return new ModelAndView("logout");
    }

    //错误页面展示
    @GetMapping("/to403")
    public ModelAndView error(){
        return new ModelAndView("403");
    }
    
    //管理员功能
//    @RequiresRoles("admin")
//    @RequiresPermissions("管理员添加")
//    @RequestMapping(value = "/admin/add")
//    public String create(){
//        return "add success!";
//    }

    //用户功能
//    @RequiresRoles("user")
//    @RequiresPermissions("用户查询")
//    @RequestMapping(value = "/user/select")
//    public String detail(){
//    	return  "select success";
//    }
    
    
}
