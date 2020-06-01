package org.study.base.vo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description：存放登录用户信息
 * @Author：ys
 */
public class SessionUser implements Serializable {
	private static final long serialVersionUID = 5865691321002088134L;
	/**
	 * 登录用户ID
	 */
	private Integer userId;
	/**
	 * 登录账号
	 */
	private String loginId;
	/**
	 * 登录密码
	 */
	private String password;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 工作单位名称
	 */
	private String workUnit;
	/**
	 * 工作单位层级代码
	 */
	private String workUnitLevelCode;
	/**
	 * 访问者ip
	 */
	private String ip;
	/**
	 * 存放用户相关功能权限（权限树型结构菜单）
	 */
	private Set<String> perms = new HashSet<String>();
	
	/**
	 * 存放用户相关角色
	 */
	private Set<String> roles = new HashSet<String>();

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getWorkUnitLevelCode() {
		return workUnitLevelCode;
	}

	public void setWorkUnitLevelCode(String workUnitLevelCode) {
		this.workUnitLevelCode = workUnitLevelCode;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Set<String> getPerms() {
		return perms;
	}

	public void setPerms(Set<String> perms) {
		this.perms = perms;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}


}
