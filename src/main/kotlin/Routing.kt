package com.example

import com.example.routes.gpt.gpt
import com.example.routes.ip.ip
import com.example.routes.map.map
import com.example.routes.prevmessage.prev
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.ContentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.*
import io.swagger.codegen.v3.generators.html.*


@Serializable
data class Message(val sender: String, val instance: String, val message: String)

@Serializable
data class Ip(val city: String, val country: String, val lat: Float, val lon: Float)

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
    val env = environment.config.propertyOrNull("ktor.environment")?.getString()


    val myUrl = when(env) {
        "prod" -> System.getenv("N8N_URL")
        else -> "http://144.124.245.103/n8n/webhook/APIsssi"
    }
    routing {


        ip(client)
        post("/ktor/answer") {

            val formParameters = call.receiveParameters()
            val name = formParameters["name"].toString()
            val text = formParameters["text"].toString()

            val response: HttpResponse = client.post(myUrl) {
                contentType(ContentType.Application.Json)
                setBody(Message(name, "none", text))
            }

            val body: String = response.body()
            val answer = Json.decodeFromString<Output>(body)

            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """ 
                    <!DOCTYPE html>
                      <html eng>
                     <head><title>GPT answer</title></head>
                        <body>
                            <div style="max-width: 80%; margin: 200px auto; text-align: center">     
                            <h3 style="font-family: monospace; margin-top: 200px;text-align: center">Hello ${answer.name}</h3>
                            <p style="text-align: justify; font-size: 2vw; font-family: cursive">
                                <b style="font-size: 2.1vw; font-family: monospace">Answer:</b> ${answer.response} 
                            </p>
                            <a style="font-family:monospace; font-size: 18px" href="/ktor/gpt">Next question</a>                    
                            <a style="margin-left: 20px;font-family:monospace; font-size: 18pxy" href="/ktor/prev">Prev question</a>
                            </div>
                            <script>
                                localStorage.setItem("answer", ${answer.message});
                            </script>
                     </body>
                </html>
                    """.trimIndent()
            )

        }
        prev(client, myUrl)
        gpt(env)
        map(env)
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }

    }
}
