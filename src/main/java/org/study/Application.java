package org.study;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * -辅助信息记录：
 * -用户登录【http://localhost:8081/login/doLogin?loginId=admin&password=123456】
 * -注册用户【（http://localhost:8081/login/register?loginId=admin&password=123456】
 * =================================历史更新记录==================================================================
 * 2020-03-21【 SpringBoot+Mybatis+mysql+druid】
 * 1，mapper的xml和注解两种实现、javaConfig注解配置
 * 	（1）同时支持mapper接口方法的xml配置和注解两个种实现方式
 * 	（2）数据库连接池可以在application.yml配置，也可使用@Configration注解配置
 *  
 * 2020-03-29【 SpringBoot+Mybatis+mysql+druid+jpa】
 * 2，整合使用jpa
 * 		（1）一定要在实体类和主键上分别加@Entity和@Id注解,属于jpa专用，且@Entity中要指明数据表名，否则根据类名识别
 * 		（2）主键一定要加@GeneratedValue注解，且在springboot中@GeneratedValue中不能加属性内容
 *
 *2020-04-12【 SpringBoot+Mybatis+mysql+druid+jpa+shiro】
 *3,整合shiro：pom导入shiro-spring，还有其他的可以选择导入
 *		（1）使用的权限配置类：MyShiroRealm、PasswordHelper、ShiroConfiguration
 *		（2）测试权限的控制层类：LoginContoller、AuthcController
 *		（3）测试使用到的实体：SysUser、 SysRole、 SysPermission
 * @author ys
 *
 *2020-04-19【 SpringBoot+Mybatis+mysql+druid+jpa+shiro+（shiro-session，shiro-ehcache,spring-ehcache）】
 *4，springboot整合shiro-session管理，springboot集成shiro并使用ehcache缓存
	（1）添加自定义session监听器：ShiroSessionListener
  	（2）完善ShiroConfiguration
  	（3）application增加@EnableCaching，开启缓存的注解,开启后springboot中的缓存注解@Cacheable，@CachePut等就可以使用了
  	目的说明：让springboot和shrio都能使用ehcache缓存，这里要明确的一点是在shiro2.5以后的版本不允许再一个应用中出现两个缓存管理器，
  		只允许一个缓存管理器的定义（这是使用ehcache使用缓存的关键），那么如何做到这一点哪，这里就需要定义一个缓存管理器工厂bean，该bean属于spring范畴之内,
    	通过该工厂bean让缓存管理器被spring（spring主要用于缓存数据库数据及其它的需要缓存的数据）和shrio共享（shiro主要用于会话的存储和持久化）
                  【具体数据缓存如何使用参考https://blog.csdn.net/weixin_39910092/article/details/78175465】
  	参考信息：
  	【参考https://blog.csdn.net/cai454692590/article/details/96475354】
  	【参考https://blog.csdn.net/qq_34021712/article/details/80418112】
	【参考https://blog.csdn.net/tanleijin/article/details/81118963】
	【参考https://blog.csdn.net/jialijuan521_zhang/article/details/85913613】
 *  
 *5，【延后，redis和echcahe正常只能用一个，暂不改】springboot使用shiro-整合redis作为缓存
 *
 *6，整合vue
 *
 */

@EnableCaching //【2020-04-19】开启缓存的注解,springboot中的缓存注解@Cacheable，@CachePut等就可以使用了
@MapperScan(basePackages = {"org.study.mapper"})
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
