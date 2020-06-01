package org.study.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.study.model.SysPermission;
import org.study.model.SysRole;
import org.study.model.SysUser;
import org.study.service.UserService;

/**
 * 自定义Realm
 * @author ys
 *
 */
public class MyShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    //一般这里都写的是servic，如果为了测试方便也可直接调用dao接口（如Repository或mapper）
    @Autowired
	private UserService userService;

    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        logger.info("验证当前Subject时获取到token为：" + token.toString());
        //查出是否有此用户
        //SysUser user=(SysUser)token.getPrincipal();
        String loginId=(String)token.getPrincipal();
//        String username = token.getUsername();
        SysUser user=userService.getUserByLoginId(loginId);

        if(user != null) {
            // 若存在，将此用户存放到登录认证info中，无需自己做密码对比，Shiro会为我们进行密码对比校验
        	//获取用户角色和用户权限
        	//TODO
        	//把获取到的用户角色集合和权限集合放到用户对象属性中
        	//TODO
//            List<URole> rlist = uRoleDao.findRoleByUid(hasUser.getId());//获取用户角色
//            List<UPermission> plist = uPermissionDao.findPermissionByUid(hasUser.getId());//获取用户权限
//            List<String> roleStrlist=new ArrayList<String>();////用户的角色集合
//            List<String> perminsStrlist=new ArrayList<String>();//用户的权限集合
//            for (URole role : rlist) {
//                roleStrlist.add(role.getName());
//            }
//            for (UPermission uPermission : plist) {
//                perminsStrlist.add(uPermission.getName());
//            }
//            hasUser.setRoleStrlist(roleStrlist);
//            hasUser.setPerminsStrlist(perminsStrlist);
        	
            //return new SimpleAuthenticationInfo(user, user.getPwd(), getName());
            SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getLoginId(), user.getPwd(),
                    ByteSource.Util.bytes(user.getCredentialsSalt()), getName());
            return authenticationInfo;
        }

        return null;
    }

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.info("##################执行Shiro权限认证##################");
        // SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
        String loginId = (String) principalCollection.getPrimaryPrincipal();
        SysUser user=userService.getUserByLoginId(loginId);
        if (user != null) {
            //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            /**
             * 不同添加方式针对的登录保存和验证有区别
             * 方式一：直接添加角色名称和权限名称
             */
            for (SysRole role : user.getRoles()) {
            	//添加用户角色到角色集合
            	info.addRole(role.getRole());
                for (SysPermission permission : role.getPermissions()) {
                	//添加用户权限到权限集合
                	info.addStringPermission(permission.getName());
                }
            }
            /**
             * 方式二：添加角色集合和权限集合
             */
            //添加用户的角色集合
            //info.addRoles(user.getRoleStrlist());
            //添加用户的权限集合
            //info.addStringPermissions(user.getPerminsStrlist());
            
            // 检查用户状态
            //checkUserStatus(sysUser.getStatus());
            return info;
        }
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return null;
    }
    
    /**
     * 检查用户状态
     * 如果用户状态异常，则抛出对应的错误
     * @param status 用户状态
     * @throws AuthenticationException 用户状态错误信息
     */
    @SuppressWarnings("unused")
	private void checkUserStatus(int status)throws AuthenticationException{
//        switch (status){
//            case ShiroConstants.ADMIN_STATUS_DISABLED:
//                throw new DisabledAccountException();
//            case ShiroConstants.ADMIN_STATUS_LOCKED:
//                throw new LockedAccountException();
//            case ShiroConstants.ADMIN_STATUS_DELETE:
//                throw new UnknownAccountException();
//        }
    }

}
