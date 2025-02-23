package net.leejjon.crud

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import net.leejjon.crud.database.ExceptionTranslator
import org.h2.jdbcx.JdbcDataSource
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@SpringBootApplication
//@EnableTransactionManagement
@OpenAPIDefinition
class WaysToCrudApplication {
	@Autowired
	lateinit var environment: Environment

	@Autowired
	lateinit var dataSource: DataSource

//	@Bean
//	fun transactionAwareDataSource(): TransactionAwareDataSourceProxy {
//		return TransactionAwareDataSourceProxy(dataSource)
//	}

	@Bean
	fun transactionManager(): DataSourceTransactionManager {
		return DataSourceTransactionManager(dataSource)
	}

	@Bean
	fun connectionProvider(): DataSourceConnectionProvider {
		return DataSourceConnectionProvider(dataSource)
	}

	@Bean
	fun exceptionTransformer(): ExceptionTranslator {
		return ExceptionTranslator()
	}

	@Bean
	fun dsl(): DefaultDSLContext {
		return DefaultDSLContext(configuration())
	}

	@Bean
	fun configuration(): DefaultConfiguration {
		val jooqConfiguration = DefaultConfiguration()
		jooqConfiguration.set(connectionProvider())
		jooqConfiguration.set(DefaultExecuteListenerProvider(exceptionTransformer()))

		val sqlDialectName = environment.getRequiredProperty("jooq.sql.dialect")
		val dialect = SQLDialect.valueOf(sqlDialectName)
		jooqConfiguration.set(dialect)

		return jooqConfiguration
	}
}

fun main(args: Array<String>) {
	runApplication<WaysToCrudApplication>(*args)
}
