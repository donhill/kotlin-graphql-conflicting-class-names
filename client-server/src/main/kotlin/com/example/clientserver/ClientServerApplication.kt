package com.example.clientserver

import com.example.server.graphql.Groups
import com.example.server.graphql.Users
import com.example.server.graphql.users.UserResponse
import com.expediagroup.graphql.client.spring.GraphQLWebClient
import com.expediagroup.graphql.server.operations.Query
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@SpringBootApplication
class ClientServerApplication

fun main(args: Array<String>) {
    runApplication<ClientServerApplication>(*args)
}

