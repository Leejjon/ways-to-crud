package net.leejjon.crud.database

import org.jooq.ExecuteContext
import org.jooq.ExecuteListener
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator

class ExceptionTranslator : ExecuteListener {
    override fun exception(context: ExecuteContext) {
        val dialect = context.configuration().dialect()
        val translator = SQLErrorCodeSQLExceptionTranslator(dialect.name)
        context.exception(translator.translate("Access database using Jooq", context.sql(), context.sqlException()!!))
    }
}
