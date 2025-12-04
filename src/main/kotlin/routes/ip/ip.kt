package com.example.routes.ip

import com.example.Ip
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.server.plugins.origin
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.json.Json
import kotlin.text.split


fun  Route.ip(client: HttpClient) {
    get("/ktor") {
//        val myip = call.request.origin.remoteHost

        val forwardedFor = call.request.headers["X-Forwarded-For"]
        val ip = forwardedFor?.split(",")?.firstOrNull()?.trim()
            ?: call.request.origin.remoteHost


        val response: HttpResponse = client.get (  "http://ip-api.com/json/$ip?fields=city,country" )
        val body: String = response.body()
        val answer = Json.decodeFromString<Ip>(body)


        call.respondText(
            contentType = ContentType.parse("text/html"),
            text = """
                <div style="background-color: #ccc;height: 100vh;padding-top: 200px;box-sizing: border-box">
                <h2 style="text-align: center">${answer.country} -  ${answer.city}  </h2>
                <h2 style="text-align: center">Your ip:  $ip  </h2>
                </div>
                """.trimIndent()
        )
    }
}