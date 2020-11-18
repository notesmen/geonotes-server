package org.geonotes.server.config

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MappingMongoConverter
import org.springframework.context.annotation.Bean

import org.geonotes.server.logger

@Configuration
open class MongoConfig : AbstractMongoClientConfiguration() {

    override fun getDatabaseName(): String {
        return databaseName
    }

    override fun mongoTemplate(databaseFactory: MongoDatabaseFactory, converter: MappingMongoConverter): MongoTemplate {
        return super.mongoTemplate(databaseFactory, converter)
    }

    @Bean
    override fun mongoClient(): MongoClient? {
        var mongoClient: MongoClient? = null
        try {
            val settings: MongoClientSettings = MongoClientSettings.builder()
                .credential(MongoCredential.createCredential(
                    username,
                    databaseName,
                    password.toCharArray()
                ))
                .applyToClusterSettings { builder ->
                    builder.hosts(listOf(ServerAddress(host, port!!)))
                }.build()

            mongoClient = MongoClients.create(settings)
        } catch (e: java.net.UnknownHostException) {
            logger.error("Unable to create mongo client: ${e.message}")
        }

        return mongoClient
    }

    @Value("\${spring.data.mongodb.database}")
    private lateinit var databaseName: String

    @Value("\${spring.data.mongodb.username}")
    private lateinit var username: String

    @Value("\${spring.data.mongodb.password}")
    private lateinit var password: String

    @Value("\${spring.data.mongodb.host}")
    private lateinit var host: String

    @Value("\${spring.data.mongodb.port}")
    private var port: Int? = null

    private val logger: Logger by logger()
}
