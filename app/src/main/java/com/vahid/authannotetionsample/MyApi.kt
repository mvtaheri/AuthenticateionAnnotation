package com.vahid.authannotetionsample

import retrofit2.http.GET

interface MyApi {
    @GET("/users")
    suspend fun getUsers(): List<User>

    @GET("/posts")
    @Authenticated
    suspend fun getPosts(): List<Post>

}

@Target(AnnotationTarget.FUNCTION)
annotation class Authenticated