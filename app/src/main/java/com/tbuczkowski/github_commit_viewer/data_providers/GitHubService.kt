package com.tbuczkowski.github_commit_viewer.data_providers

import com.tbuczkowski.github_commit_viewer.model.dto.GitHubCommit
import com.tbuczkowski.github_commit_viewer.model.dto.GitHubRepository
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {

    @GET("/repos/{owner}/{name}")
    fun fetchRepositoryInfo(@Path("owner") owner: String, @Path("name") name: String): Observable<GitHubRepository>

    @GET("/repos/{owner}/{name}/commits")
    fun fetchCommits(@Path("owner") owner: String, @Path("name") name: String) : Observable<List<GitHubCommit>>

    companion object {
        fun create(): GitHubService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client =
                OkHttpClient.Builder().addInterceptor(interceptor).build()

            val retrofit: Retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl("https://api.github.com")
                .build()
            return retrofit.create(GitHubService::class.java)
        }

    }

}