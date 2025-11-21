package com.example

import com.example.routes.gpt.gpt
import com.example.routes.userRoutes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.ContentType
import io.ktor.server.plugins.origin
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import jdk.internal.org.jline.utils.AttributedStringBuilder.append
import java.net.http.HttpHeaders



@Serializable

data class Message(val sender: String, val instance: String, val message: String)

@Serializable
data class Output(
    val name: String, val message: String,
    val response: String, val createdAt: String, val updatedAt: String, val id: Int
)

fun Application.configureRouting() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }
    }
    routing {
        userRoutes()
        get("/ktor/prev") {
            val response: HttpResponse = client.post("http://144.124.245.103/n8n/webhook/APItable") {
                contentType(ContentType.Application.Json)
                setBody(Message("Alex", "dsdsa", "Hello from Ktor!"))
            }

            val body: String = response.body()
            val anser = Json.decodeFromString<Output>(body)

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """ 
                    <div style="width: 350px; margin: 200px auto; text-align: center">     
                    <h4 style="margin-top: 200px;text-align: left">Question:</h4>
                    <p>${anser.message}</p>
                    <h4 style="margin-top: 20px;text-align: left">Answer:</h4>
                    <p style="text-align: center; font-size: 17px">${anser.response} .</p>
                    <a href="/gpt">Next question</a>                    
                    <a style="margin-left: 20px" href="/ktor/prev">Prev question</a>
                    </div>
                    """.trimIndent()
            )
        }
        get("/ktor") {
            val forwardedFor = call.request.headers["X-Forwarded-For"]
            val ip = forwardedFor?.split(",")?.firstOrNull()?.trim()
                ?: call.request.origin.remoteHost
            call.respondText("Hello ${ip}  Super World! ")
        }
        post("/ktor/answer") {
            val formParameters = call.receiveParameters()
            val name = formParameters["name"].toString()
            val text = formParameters["text"].toString()

            val response: HttpResponse = client.post("http://144.124.245.103/n8n/webhook/APIsssi") {
                contentType(ContentType.Application.Json)
                setBody(Message(name, "gopol", text))
            }

            val body: String = response.body()
            val anser = Json.decodeFromString<Output>(body)

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """ 
                    <!DOCTYPE html>
                      <html>
                     <head><title>Ktor Example</title></head>
                        <body>
                            <div style="max-width: 80%; margin: 200px auto; text-align: center">     
                            <h3 style="font-family: monospace; margin-top: 200px;text-align: center">Hello ${anser.name}</h3>
                            <p style="text-align: justify; font-size: 2vw; font-family: cursive">
                                <b style="font-size: 2.1vw; font-family: monospace">Answer:</b> ${anser.response} 
                            </p>
                            <a style="font-family:monospace; font-size: 18px" href="/gpt">Next question</a>                    
                            <a style="margin-left: 20px;font-family:monospace; font-size: 18pxy" href="/ktor/prev">Prev question</a>
                            </div>
                     </body>
                </html>
                    """.trimIndent()
            )

        }
        gpt()

    }
}
