package com.example.routes.ip

import com.example.Ip
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json


fun  Route.ip(client: HttpClient) {
    get("/ktor") {
        val env = environment.config.propertyOrNull("ktor.environment")?.getString()
        val forwardedFor = call.request.headers["X-Forwarded-For"]
        val ip = when(env) {
            "dev" -> "146.19.136.111"
            else -> forwardedFor?.split(",")?.firstOrNull()?.trim()
        }


        val response: HttpResponse = client.get (  "http://ip-api.com/json/$ip?fields=city,country,lat,lon" )
        val body: String = response.body()
        val answer = Json.decodeFromString<Ip>(body)


        call.respondText(
            contentType = ContentType.parse("text/html"),
            text = """
                <div style="background-color: #ccc;height: 100vh;padding-top: 200px;box-sizing: border-box">
                <h2 style="text-align: center">${answer.country} -  ${answer.city}  </h2>
                <h2 style="text-align: center">Lat:${answer.lat} , Lon: ${answer.lon}  </h2>
                <a style="display: block;text-align: center" href="/map?lat=${answer.lat}&lon=${answer.lon}">view map</a>
                <h2 style="text-align: center">Your ip:  $ip  </h2>
                </div>
                """.trimIndent()
        )
    }
}