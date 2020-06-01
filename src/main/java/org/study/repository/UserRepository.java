package org.study.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.study.model.SysUser;

/**
 * 用户jpa持久化dao接口
 * @author ys
 *
 */
@Repository
public interface UserRepository extends JpaRepository<SysUser, Integer>,JpaSpecificationExecutor<SysUser> {

	
	/**
	 * 根据登录名获取用户信息
	 * @param loginId
	 * @return
	 */
	public List<SysUser> findAllByLoginId(String loginId);
}
