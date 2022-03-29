package com.example.server.api

import com.example.server.resources.ApiResponseCode
import com.expediagroup.graphql.server.operations.Query
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class GraphRouter : Query {

    /*
    This is a GrapghQL router, each method will be exposed as a top level container
     */
    fun users(userId: String) = Mono.just(UserResponse(Meta(ApiResponseCode.GROUPS_FOUND), "Test User response"))

    fun groups(userId: String) = Mono.just(GroupResponse(
        Meta(ApiResponseCode.GROUPS_FOUND),
        "Test Group response"
    ))
}

class GroupResponse(val meta: Meta, val data: String)
class UserResponse(val meta: Meta, val data: String)
class Meta(val responseStatus: String, val hint: String) {
    constructor(apiResponseCode: ApiResponseCode) : this(apiResponseCode.appCode, apiResponseCode.message)
}
