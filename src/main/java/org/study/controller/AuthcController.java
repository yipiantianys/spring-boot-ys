package org.study.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.study.model.SysUser;

/**
 * 【Shiro】
 * 		AuthcController	:测试shiro需要授权的请求
 * 		LoginContoller	:测试shiro不用授权的请求
 * @author ys
 *
 */
@RestController
@RequestMapping("authc")
public class AuthcController {

	/**
     * 登录的用户都能访问，规则参考ShiroConfiguration的配置
     * @return
     */
    @GetMapping("index")
    public Object index() {
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getSession().getAttribute("user");
        return user.toString();
    }

    /**
     * 有【admin角色】的用户才能访问，规则参考ShiroConfiguration的配置
     * @return
     */
    @GetMapping("admin")
    public Object admin() {
        return "Welcome Admin";
    }

    /**
     * 具有【Delete权限】的用户才能能访问，规则参考ShiroConfiguration的配置
     * @return
     */
    // delete
    @GetMapping("removable")
    public Object removable() {
        return "removable";
    }

    /**
     * 具有【Create,Update权限】的用户才能能访问，规则参考ShiroConfiguration的配置
     * @return
     */
    // insert & update
    @GetMapping("renewable")
    public Object renewable() {
        return "renewable";
    }
}
