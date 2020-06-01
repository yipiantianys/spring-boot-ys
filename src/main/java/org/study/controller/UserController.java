package org.study.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.study.base.vo.SessionUser;
import org.study.model.SysUser;
import org.study.service.UserService;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**==【Mybatis接口测试】=============================================================================*/
	/**
	 * 获取用户信息【mapper的XML实现】
	 * @param id
	 * @return
	 */
	@RequestMapping("/getUser")
	public List<SysUser> getUser(@RequestParam("id") int id) {
		return userService.getUser(id);
	}

	/**
	 * 获取用户信息【mapper注解实现】
	 * @param id
	 * @return
	 */
	@RequestMapping("/getUserId")
	public List<SysUser> getUserId(@RequestParam("id") int id) {
		return userService.getUserId(id);
	}
	
	/**
	 * 添加用户信息
	 * @return
	 */
	@RequestMapping("/addUser")
	public String addUser() {
		return userService.addUser() + "";
	}

	/**
	 * 根据id删除用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteUser")
	public String deleteUser(@RequestParam("id") int id) {
		return userService.deleteUser(id) + "";
	}
	
	/**
	 * 根据id修改用户
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateUser")
	public String updateUser(@RequestParam("id") int id) {
		return userService.updateUser(id) + "";
	}
	
	/**
	 * 获取所有用户信息【xml实现】
	 * @return
	 */
	@RequestMapping("/getUsers")
	public List<SysUser> getUsers() {
		return userService.getUsers();
	}
	
	/**==【JPA测试】=============================================================================*/

	/**
	 * 添加用户信息【jpa实现】
	 * @return
	 */
	@RequestMapping("/addUserByJpa")
	public String addUserByJpa() {
		return userService.saveUserByJpa() + "";
	}
	
	/**
	 * 获取所有用户信息【jpa实现】
	 * @return
	 */
	@RequestMapping("/getUsersByJpa")
	public List<SysUser> getUsersByJpa() {
		return userService.getUsersByJpa();
	}
	
	/**
	 * 根据id修改用户【jpa实现】
	 * @param id
	 * @return
	 */
	@RequestMapping("/updateUserByJpa")
	public String updateUserByJpa(@RequestParam("id") int id) {
		return userService.updateUserByJpa(id) + "";
	}
	
	/**
	 * 测试shiro的Session
	 * @param id
	 * @return
	 */
	@RequestMapping("/testSession")
	public String testSession() {
		/**
		 * 可通过SecurityUtils.getSubject().getSession()获取session信息
		 */
		SessionUser sessionUser=(SessionUser)SecurityUtils.getSubject().getSession().getAttribute("sessionUser");
		List<SysUser> userList=userService.getUserId(sessionUser.getUserId());
		return "sessionUser="+JSONObject.toJSONString(sessionUser)+"\n userList = "+JSONObject.toJSONString(userList.get(0));
	}
	
}
