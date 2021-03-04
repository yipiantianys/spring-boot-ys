package org.study.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ShiroConfiguration {
	/*
    1.LifecycleBeanPostProcessor，这是个DestructionAwareBeanPostProcessor的子类，负责org.apache.shiro.util.Initializable类型bean的生命周期的，初始化和销毁。主要是AuthorizingRealm类的子类，以及EhCacheManager类。
    2.HashedCredentialsMatcher，这个类是为了对密码进行编码的，防止密码在数据库里明码保存，当然在登陆认证的生活，这个类也负责对form里输入的密码进行编码。
    3.ShiroRealm，这是个自定义的认证类，继承自AuthorizingRealm，负责用户的认证和权限的处理，可以参考JdbcRealm的实现。
    4.EhCacheManager，缓存管理，用户登陆成功后，把用户信息和权限信息缓存起来，然后每次用户请求时，放入用户的session中，如果不设置这个bean，每个请求都会查询一次数据库。
    5.SecurityManager，权限管理，这个类组合了登陆，登出，权限，session的处理，是个比较重要的类。
    6.ShiroFilterFactoryBean，是个factorybean，为了生成ShiroFilter。它主要保持了三项数据，securityManager，filters，filterChainDefinitionManager。
    7.DefaultAdvisorAutoProxyCreator，Spring的一个bean，由Advisor决定对哪些类的方法进行AOP代理。
    8.AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
*/
	
//	@Value("${server.session-timeout}")
//    private int tomcatTimeout;
	
	
    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     * Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     *
     */
	
	/**
	 * 把shiro生命周期交给spring boot管理
	 * @return
	 */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM_NAME); // 散列算法
        hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS); // 散列次数
        return hashedCredentialsMatcher;
    }
    
    //
    @Bean(name = "shiroRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public MyShiroRealm shiroRealm() {
        MyShiroRealm realm = new MyShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher()); // 原来在这里
        return realm;
    }

    /**
     * shiro核心拦截器
     * @param securityManager
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //拦截器.
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边，必须是LinkedHashMap，因为要保证有序
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        // filterChainDefinitionManager.put("/toLogout", "logout");
		
        shiroFilterFactoryBean.setLoginUrl("/login/login");// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/login/unauthc");// 未授权界面
        shiroFilterFactoryBean.setSuccessUrl("/login/toSuccess");// 登录成功后要跳转的链接
        
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/login/*", "anon");//所有login登录请求都能访问
        filterChainDefinitionMap.put("/authc/index", "authc");
        filterChainDefinitionMap.put("/authc/admin", "roles[admin]");//需要具有【admin角色】才能访问
        filterChainDefinitionMap.put("/authc/removable", "perms[Delete]");//需要具有【Delete权限】才能访问
        filterChainDefinitionMap.put("/authc/renewable", "perms[Create,Update]");//需要具有【Create,Update权限】才能访问
        filterChainDefinitionMap.put("/user/**", "authc");//所有user类请求都需要登录后访问
        filterChainDefinitionMap.put("/**", "authc");//其他所有请求都需要登录后访问
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    //DefaultAdvisorAutoProxyCreator实现Spring自动代理
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }
    
    @Bean
    public PasswordHelper passwordHelper() {
        return new PasswordHelper();
    }
    /**
     * 让springboot和shrio都能使用ehcache缓存，这里要明确的一点是在shiro2.5以后的版本不允许再一个应用中出现两个缓存管理器，
     * 只允许一个缓存管理器的定义（这是使用ehcache使用缓存的关键），那么如何做到这一点哪，这里就需要定义一个缓存管理器工厂bean，该bean属于spring范畴之内,
     * 通过该工厂bean让缓存管理器被spring（spring主要用于缓存数据库数据及其它的需要缓存的数据）和shrio共享（shiro主要用于会话的存储和持久化）
     * @return
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
    	EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();  // 该工厂bean产生并管理缓存管理器
        if (ehCacheManagerFactoryBean.getObject() == null) {  // 如果还没有产生缓存管理器，那么就配置该ehcacheManagerFactoryBean 用于创建缓存管理器。
        	ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        	ehCacheManagerFactoryBean.setShared(true);// 关键，让该工厂创建的管理器能够被共享， spring本身能使用创建的缓存管理器，shrio也能使用该缓存管理器。
        }
        return ehCacheManagerFactoryBean;  // 返回配置好的缓存管理器工厂bean
    }

    /**
     * spring的ehcache缓存管理器
     * @return
     */
    @Bean
    public EhCacheCacheManager springCacheManager() {
    	EhCacheCacheManager ehcacheCacheManager = new EhCacheCacheManager();
        ehcacheCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return ehcacheCacheManager;
    }

    /**
     * shiro的ehcache缓存管理器
     * @return
     */
    @Bean(name = "ehCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager ehCacheManager(){
    	/**
    	 * 这是配置shiro的缓存管理器org.apache.shiro.cache.ehcach.EhCacheManager，
    	 * 上面的方法的参数是把spring容器中的cacheManager对象注入到EhCacheManager中，这样就实现了shiro和缓存注解使用同一种缓存方式。
    	 */
    	//将ehcacheManager转换成shiro包装后的ehcacheManager对象
        EhCacheManager em = new EhCacheManager();
        em.setCacheManager(ehCacheManagerFactoryBean().getObject());
        //em.setCacheManagerConfigFile("classpath:ehcache.xml");
        return em;
        
		/**
		 * EhCacheManager cacheManager = new EhCacheManager();
		 * cacheManager.setCacheManagerConfigFile("classpath:config/ehcache.xml");
		 * 一定不要这么配置，这只是shiro集成了ehcache缓存，根本没有交给spring容器去管理。
		 */
    }
    
    /**
     * 配置核心安全事务管理器
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager(){
    	DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置自定义realm.
        securityManager.setRealm(shiroRealm());
        //【暂未实现】配置记住我 
        //securityManager.setRememberMeManager(rememberMeManager());
        //配置 ehcache缓存管理器 参考博客：
        securityManager.setCacheManager(ehCacheManager());//用户授权/认证信息Cache, 采用EhCache 缓存
       
        /**
         * 【2020-04-19】配置自定义session管理，使用ehcache 或redis
         */
        securityManager.setSessionManager(sessionManager());
        
        return securityManager;
    }
    
    //【2020-04-19】============session start=========================================
    /**
     * 配置session监听
     * @return
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }
    
    /**
     * 配置会话ID生成器
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }
    
    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //使用ehCacheManager
        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        //设置session缓存的名字 默认为 shiro-activeSessionCache
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
        
        //return new MemorySessionDAO();
    }
    /**
     * 配置保存sessionId的cookie 
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
        
//        SimpleCookie simpleCookie = new SimpleCookie();
//        simpleCookie.setName("jeesite.session.id");
//        return simpleCookie;
        
    }
 

    /**
     * 配置会话管理器，设定会话超时及保存
     * @return
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        /**
         * 注意：这里的SessionIdCookie 是新建的一个SimpleCookie对象,不是之前整合记住我的那个rememberMeCookie 
         * 如果配错了，就会出现session经典问题：每次请求都是一个新的session 并且后台报以下异常，
         * org.apache.shiro.crypto.CryptoException: Unable to execute 'doFinal' with cipher instance [javax.crypto.Cipher@461df537].
         * 解析的时候报错.因为记住我cookie是加密的用户信息,所以报解密错误
         */
        sessionManager.setSessionIdCookie(sessionIdCookie());
        //设置sessionDao对session查询，在查询在线用户service中用到了
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setCacheManager(ehCacheManager());

        //全局会话超时时间（单位毫秒），默认30分钟  可暂时设置为10秒钟（10000） 用来测试
        sessionManager.setGlobalSessionTimeout(1800000);//30分钟 1800000
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        /**
         * 【增加一个会话的验证调度】以解决用户不点注销，直接关闭浏览器，不能够进行session的清空处理的问题
         */
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒（5000） 用来测试
        sessionManager.setSessionValidationInterval(3600000);//1小时 3600000

        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;

    }
    //【2020-04-19】============session end=========================================    
}
