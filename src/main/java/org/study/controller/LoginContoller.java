package org.study.controller;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.study.base.vo.SessionUser;
import org.study.config.PasswordHelper;
import org.study.model.SysPermission;
import org.study.model.SysRole;
import org.study.model.SysUser;
import org.study.service.UserService;

/**
 * 登录controller
 * 【Shiro】
 * 		AuthcController	:测试shiro需要授权的请求
 * 		LoginContoller	:测试shiro不用授权的请求
 * -用户登录【http://localhost:8081/login/doLogin?loginId=admin&password=123456】
 * -注册用户【（http://localhost:8081/login/register?loginId=admin&password=123456】
 * @author ys
 *
 */
@RestController
@RequestMapping("/login")
public class LoginContoller {

	private static Logger logger = LoggerFactory.getLogger(LoginContoller.class);

	@Autowired
    private UserService userService;
    @Autowired
    private PasswordHelper passwordHelper;

    /**
     * 登录页请求【测试用，返回提示字符串，不转页面】
     * @return
     */
    @GetMapping("login")
    public Object login() {
        return "Here is Login page";
    }
    /**
     * 登录页请求【转页面】
     * @param model
     * @return
     */
//    @RequestMapping("/login")
//    public ModelAndView list(Model model) {
//        return new ModelAndView("login");
//    		return new ModelAndView("redirect:/toSuccess");
//    }
    
    /**
     * 未授权页请求【测试用，返回提示字符串，不转页面】
     * @return
     */
    @GetMapping("unauthc")
    public Object unauthc() {
        return "Here is Unauthc page";
    }

    /**
     * -用户登录【http://localhost:8081/login/doLogin?loginId=admin&password=123456】
     * @param loginId
     * @param password
     * @return
     */
    @GetMapping("doLogin")
    public Object doLogin(@RequestParam String loginId, @RequestParam String password) {
    	logger.info("loginId="+loginId +" pwd="+password);
        try {
        	UsernamePasswordToken token = new UsernamePasswordToken(loginId, password);
        	Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            //验证是否登录成功
            if (subject.isAuthenticated()) {
                logger.info("用户[" + loginId + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                //return new ModelAndView("redirect:/toSuccess");
            } else {
                token.clear();
                System.out.println("用户[" + loginId + "]登录认证失败,重新登陆");
                //return new ModelAndView("redirect:/toLogin");
            }
            SysUser user = userService.getUserByLoginId(loginId);
            subject.getSession().setAttribute("user", user);
            SessionUser sessionUser=new SessionUser();
            sessionUser.setLoginId(user.getLoginId());
            sessionUser.setUserId(user.getId());
            sessionUser.setPassword(user.getPwd());
            sessionUser.setUserName(user.getName());
            //保存当前登录用户的角色和权限
            Set<String> perms=new HashSet<String>();
            Set<String> roles=new HashSet<String>();
            for(SysRole role:user.getRoles()) {
            	roles.add(role.getRole());
            	for(SysPermission perm:role.getPermissions()) {
            		perms.add(perm.getName());
            	}
            }
            sessionUser.setRoles(roles);
            sessionUser.setPerms(perms);
            subject.getSession().setAttribute("sessionUser", sessionUser);
            
            //注：
            //执行JSONObject.toJSONString(user)时，会触发user获取所有懒加载数据，可将user对象直接放入session，后续再从其他请求session中获取user中的所有关联对象时不会报错；
            //不执行JSONObject.toJSONString(user)时，将user对象直接放入session，后续再从其他请求session中获取user中的所有关联对象时会报错，报无session，无法获取懒加载数据。
            // subject.getSession().setAttribute("userJson", JSONObject.toJSONString(user));
            
        } catch (IncorrectCredentialsException ice) {
            return "password error!";
        } catch (UnknownAccountException uae) {
            return "username error!";
        }

        return "SUCCESS";
    }

    /**
     * -注册用户【（http://localhost:8081/login/register?loginId=admin&password=123456】
     * @param loginId
     * @param password
     * @return
     */
    @GetMapping("register")
    public Object register(@RequestParam String loginId, @RequestParam String password) {
    	SysUser user = new SysUser();
        user.setLoginId(loginId);
        user.setName(loginId);
        user.setPwd(password); 
        passwordHelper.encryptPassword(user);
        userService.addUser(user);
        return "SUCCESS";
    }
    
   
}
