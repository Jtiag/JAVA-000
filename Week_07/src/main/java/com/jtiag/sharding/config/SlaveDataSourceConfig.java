//package com.jtiag.sharding.config;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Objects;
//
///**
// * @author jasper
// */
//@Configuration
//@MapperScan(basePackages = "com.jtiag.sharding.dao.slave", sqlSessionTemplateRef = "slaveSqlSessionTemplate")
//@EnableTransactionManagement
//public class SlaveDataSourceConfig {
//    @Value("${jdbc.url.slave}")
//    private String url;
//    @Value("${jdbc.driver}")
//    private String driver;
//    @Value("${jdbc.user.slave}")
//    private String userName;
//    @Value("${jdbc.password.slave}")
//    private String password;
//
//    @Bean(name = "slaveDataSource")
//    public DataSource materDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(driver);
//        dataSource.setUrl(url);
//        dataSource.setUsername(userName);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//
//    @Bean(name = "slaveSqlSessionFactory")
//    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        Objects.requireNonNull(bean.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true);
//        return bean.getObject();
//    }
//
//    @Bean(name = "slaveTransactionManager")
//    public DataSourceTransactionManager slaveTransactionManager(@Qualifier("slaveDataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "slaveSqlSessionTemplate")
//    public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory")
//                                                             SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}
