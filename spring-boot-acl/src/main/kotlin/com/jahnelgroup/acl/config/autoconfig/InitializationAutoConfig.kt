@file:Suppress("SpringJavaInjectionPointsAutowiringInspection")

package com.jahnelgroup.acl.config.autoconfig

import com.jahnelgroup.acl.config.loggerFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.datasource.init.ScriptUtils
import java.io.FileNotFoundException
import javax.sql.DataSource
import java.sql.SQLException
import java.io.IOException
import javax.annotation.PostConstruct

@AutoConfigureAfter(DataSourceAutoConfiguration::class)
@Configuration
class InitializationAutoConfig(
        private var resourceLoader: ResourceLoader,

        private var dataSource: DataSource,

        @Value("\${spring.datasource.platform}")
        private var platform: String
) {

    private val log = loggerFor(javaClass)

    /**
     * Initialize the database. This would normally work but @EnableGlobalMethodSecurity and the
     * ACL config prevent proxying of those beans during startup.
     *
     * @throws SQLException
     * @throws IOException
     */
    @PostConstruct
    @Throws(SQLException::class, IOException::class)
    fun init() {
        setOf(
                "classpath:schema-${platform}.sql",
                "classpath:schema.sql",
                "classpath:data-${platform}.sql",
                "classpath:data.sql"
        ).forEach(this::executeSql)
    }

    private fun executeSql(file: String) {
        try{
            var sql = resourceLoader.getResource(file)
            log.info("executing sql {}", sql.file.absolutePath)
            ScriptUtils.executeSqlScript(dataSource.getConnection(), sql)
        }catch (fnfe: FileNotFoundException){
            // doesn't need to be present.
        }
    }
}