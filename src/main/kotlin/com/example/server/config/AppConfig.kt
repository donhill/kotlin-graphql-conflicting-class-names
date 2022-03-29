package com.example.server.config

import com.expediagroup.graphql.generator.execution.FunctionDataFetcher
import com.expediagroup.graphql.generator.execution.SimpleKotlinDataFetcherFactoryProvider
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.DataFetcherFactory
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLScalarType
import graphql.schema.GraphQLType
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType

@Configuration
open class AppConfig {

    companion object {
        val LOGGER = LoggerFactory.getLogger(this::class.java)
    }

    @Bean
    open fun dataFetcherFactoryProvider() = CustomDataFetcherFactoryProvider()
}


class CustomFunctionDataFetcher(target: Any?, fn: KFunction<*>) : FunctionDataFetcher(target, fn) {

    private fun convert(environment: DataFetchingEnvironment): Any? {
        return when (val result = super.get(environment)) {
            is Mono<*> -> result.toFuture()
            is Flux<*> -> result.collectList().toFuture()
            else -> result
        }
    }

    override fun get(environment: DataFetchingEnvironment): Any? {
        return convert(environment)
    }
}

class CustomDataFetcherFactoryProvider() : SimpleKotlinDataFetcherFactoryProvider() {

    override fun functionDataFetcherFactory(target: Any?, kFunction: KFunction<*>): DataFetcherFactory<Any?> =
        DataFetcherFactory {
            CustomFunctionDataFetcher(
                target = target,
                fn = kFunction
            )
        }

}


class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {

    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        BigDecimal::class -> graphqlBigDecimalType
        else -> null
    }
}

val graphqlBigDecimalType = GraphQLScalarType.newScalar()
    .name("BigDecimal")
    .description("A type representing a formatted BigDecimal")
    .coercing(BigDecimalCoercing)
    .build()

object BigDecimalCoercing : Coercing<BigDecimal, String> {
    override fun parseValue(input: Any): BigDecimal = BigDecimal(serialize(input))

    override fun parseLiteral(input: Any): BigDecimal {
        val uuidString = (input as? StringValue)?.value
        return BigDecimal(uuidString)
    }

    override fun serialize(dataFetcherResult: Any): String = dataFetcherResult.toString()

}
