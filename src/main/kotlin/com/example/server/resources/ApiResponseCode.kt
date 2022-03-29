package com.example.server.resources

enum class ApiResponseCode(val appCode: String, val message: String) {
    USERS_FOUND("200100", "Users found"),
    GROUPS_FOUND("200100", "Users found");

    val httpStatus: Int
        get() = Integer.valueOf(appCode.substring(0, 3))
    val appStatus: String
        get() = appCode.substring(3, 6)
    val pexCode: String
        get() = "pex:isc-data:$appCode"

    companion object {
        fun of(appCode: String): ApiResponseCode {
            val responseCodes = values()
            for (responseCode in responseCodes) {
                if (responseCode.appCode == appCode) {
                    return responseCode
                }
            }
            return USERS_FOUND
//           return  throw IllegalArgumentException("No matching app code for [$appCode]")
        }
    }
}