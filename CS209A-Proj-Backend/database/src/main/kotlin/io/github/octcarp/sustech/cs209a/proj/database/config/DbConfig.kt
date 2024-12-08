package io.github.octcarp.sustech.cs209a.proj.database.config

import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

@Configuration
@MapperScan("io.github.octcarp.sustech.cs209a.proj.database.mapper")
open class DbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    open fun dataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    open fun sqlSessionFactory(dataSource: DataSource): SqlSessionFactory {
        val factoryBean = SqlSessionFactoryBean()
        factoryBean.setDataSource(dataSource)
        return factoryBean.getObject()!!
    }

    @Bean
    open fun transactionManager(dataSource: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}