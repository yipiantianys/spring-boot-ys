package org.study.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.mapper.UserMapper;
import org.study.model.SysUser;
import org.study.repository.UserRepository;

/**
 * @author ys
 *
 */

@Service
@Transactional
public class UserService{

	
	/**
	 * JPA接口
	 */
	@Autowired
	public UserRepository userRepository;
	/**
	 * Mybatis接口
	 */
	@Autowired
	public UserMapper userMapper;
	
	/**==Mybatis接口测试=============================================================================*/
	/**
	 * 获取用户信息【xml实现】
	 * @param id
	 * @return
	 */
	public List<SysUser> getUser(int id) {
		return userMapper.getUser(id);
	}
	
	/**
	 * 获取用户信息【注解实现】
	 * @param id
	 * @return
	 */
	public List<SysUser> getUserId(int id) {
		return userMapper.getUserById(id);
	}

	/**
	 * 添加用户【xml实现】
	 * @return
	 */
	public int addUser() {
		SysUser u=new SysUser();
		u.setName("张三111");
		u.setAge(32l);
		userMapper.addUser(u);
		return u.getId();
	}
	
	/**
	 * 根据id删除用户【xml实现】
	 * @param id
	 * @return
	 */
	public int deleteUser(int id) {
		return userMapper.delete(id);
	}

	
	/**
	 * 获取所有用户【xml实现】
	 * @return
	 */
	public List<SysUser> getUsers() {
		return userMapper.getUsers();
	}
	
	/**
	 * 修改用户【xml实现】
	 * @return
	 */
	public int updateUser(int id) {
		SysUser u=userMapper.getUser(id).get(0);
		u.setName(u.getName()+(Math.random()*10000)%10000);
		u.setUpdateTime(new Date());
		userMapper.update(u);
		return u.getId();
	}
	
	 /**==JPA接口测试=============================================================================*/

	/**
	 * 添加用户【jpa实现】
	 * @return
	 */
	public int saveUserByJpa() {
		SysUser u=new SysUser();
		u.setName("李四111jpa");
		u.setAge(33l);
		userRepository.save(u);
		return u.getId();
	}

	/**
	 * 获取所有用户【jpa实现】
	 * @return
	 */
	public List<SysUser> getUsersByJpa() {
		return userRepository.findAll();
	}
	

	/**
	 * 修改用户【jpa实现】
	 * @return
	 */
	public int updateUserByJpa(int id) {
		SysUser u=userRepository.getOne(id);
		u.setName(u.getName()+(Math.random()*10000)%10000);
		u.setUpdateTime(new Date());
		userRepository.saveAndFlush(u);
		return u.getId();
	}
	
	/**
	 * 根据登录名获取用户【jpa实现】
	 * @return
	 */
	public SysUser getUserByLoginId(String loginId) {
		List<SysUser> userList=userRepository.findAllByLoginId(loginId);
		return userList!=null&&userList.size()>0?userList.get(0):null;
	}
	
	/**
	 * 添加用户【jpa实现】
	 * @return
	 */
	public void addUser(SysUser user) {
		userRepository.save(user);
	}
}
