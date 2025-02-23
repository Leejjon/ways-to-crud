package net.leejjon.crud

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import net.leejjon.crud.database.ExceptionTranslator
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.jooq.impl.DefaultExecuteListenerProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@SpringBootApplication
@EnableTransactionManagement
@OpenAPIDefinition
class WaysToCrudApplication {

	@Autowired
	lateinit var dataSource: DataSource

	@Bean
	fun connectionProvider(): DataSourceConnectionProvider {
		return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(dataSource))
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

		val dialect = SQLDialect.valueOf("H2")
		jooqConfiguration.set(dialect)

		return jooqConfiguration
	}
}

fun main(args: Array<String>) {
	runApplication<WaysToCrudApplication>(*args)
}
