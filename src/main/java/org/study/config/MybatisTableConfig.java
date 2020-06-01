package org.study.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * mybatis配置：
  		（1）包括数据源配置、sqlSessionFactory配置、事务配置等
		（2）同时配置了actable可实现自动创建表和字段
 * @author ys
 *  
 */
/**@Configuration等同@SpringBootConfiguration*/
@Configuration
@ComponentScan(basePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*"})
public class MybatisTableConfig {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${mybatis.config-location}")
    private String mybatisConfigLocation;
    
    @Value("${mybatis.mapper-locations}")
    private String mybatisMapperXMLLocations;
    
    @Value("${mybatis.type-aliases-package}")
    private String mybatisTypeAliasesPackage;

    @Bean
    public PropertiesFactoryBean configProperties() throws Exception{
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        propertiesFactoryBean.setLocations(resolver.getResources("classpath*:application.properties"));
        return propertiesFactoryBean;
    }

    @Bean
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(30);
        dataSource.setInitialSize(10);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        return dataSource;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据源
        sqlSessionFactoryBean.setDataSource(dataSource());
        // 设置MyBatis 配置文件的路径【使用@Configuration配置默认位置在classpath下，因此路径中不需再加classpath:开头】
        //sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisConfigLocation));
        // 设置mapper 对应的XML 文件的路径【需要和actable接口融合，因此要手动整合后再设置】
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] r= resolver.getResources("classpath*:com/gitee/sunchenbin/mybatis/actable/mapping/*/*.xml");
//        Resource[] r2= resolver.getResources("classpath*:mybatis/*.xml");
        Resource[] r2= resolver.getResources(mybatisMapperXMLLocations);
        Resource[] re=new Resource[r.length+r2.length];
        for(int x=0;x<r.length;x++){
            re[x] = r[x];
        }
        for(int y=0;y<r2.length;y++){
            re[r.length+y]=r2[y];
        }
        sqlSessionFactoryBean.setMapperLocations(re);
        //sqlSessionFactoryBean.setMapperLocations(resolver.getResources(mybatisMapperXMLLocation));
        // 设置mapper 接口所在的包
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisTypeAliasesPackage);
        //sqlSessionFactoryBean.setTypeAliasesPackage("org.study.model.*");
        return sqlSessionFactoryBean;
    }
     
}
