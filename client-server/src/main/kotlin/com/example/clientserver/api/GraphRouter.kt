package com.example.clientserver.api

import com.example.server.graphql.Groups
import com.example.server.graphql.Users
import com.example.server.graphql.users.UserResponse
import com.expediagroup.graphql.client.spring.GraphQLWebClient
import com.expediagroup.graphql.server.operations.Query
import io.netty.resolver.DefaultAddressResolverGroup
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Controller
class GraphRouter: Query {

    suspend fun users(): UserResponse {
        val httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)
        val webClientBuilder = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
        val client = GraphQLWebClient("http://localhost:8080/graphql", builder = webClientBuilder)

        val userRequest = Users(
            Users.Variables(
                userId = "foo"
            )
        )
        val response = client.execute(userRequest)
        response.data.let {
            it.let {
                print(it)
                return it!!.users
            }
        }
    }

    suspend fun groups(): com.example.server.graphql.groups.GroupResponse {
        val httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)
        val webClientBuilder = WebClient.builder().clientConnector(ReactorClientHttpConnector(httpClient))
        val client = GraphQLWebClient("http://localhost:8080/graphql", builder = webClientBuilder)

        val userRequest = Groups(
            Groups.Variables(
                userId = "foo"
            )
        )
        val response = client.execute(userRequest)
        response.data.let {
            it.let {
                print(it)
                return it!!.groups
            }
        }
    }
}
