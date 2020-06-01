package org.study.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.study.model.SysUser;

@Mapper
public interface UserMapper {

	//	xml实现
	/**
	 * 获取用户信息【xml实现】
	 * @param id
	 * @return
	 */
	List<SysUser> getUser(int id);
	
	//	注解实现
	/**
	 * 获取用户信息【注解实现】
	 * @param id
	 * @return
	 */
	@Select(value = "select * from sys_user where id = #{id}")
	List<SysUser> getUserById(int id);

	/**
	 * 新增用户【xml实现】
	 * @param user
	 * @return
	 */
	public boolean addUser(SysUser user);
	
	/**
	 * 查询全部【xml实现】
	 * @return
	 */
	public List<SysUser> getUsers();
	
	/**
	 * 根据用户id删除用户【xml实现】
	 * @param userId
	 * @return
	 */
	public  int delete(int userId);
	
	/**
	 * 修改用户【xml实现】
	 * @param user
	 * @return
	 */
	public  int update(SysUser user);
}
