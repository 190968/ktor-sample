package com.example.routes.prevmessage

import com.example.Message
import com.example.Output
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json


fun Route.prev(client: HttpClient, myUrl: String) {
    get("/ktor/prev") {
        val response: HttpResponse = client.post(myUrl) {
            contentType(ContentType.Application.Json)
            setBody(Message("alekc", "admin", "тест"))
        }

        val body: String = response.body()
        val answer = Json.decodeFromString<Output>(body)

        call.respondText(
            contentType = ContentType.parse("text/html"),
            text = """ 
                    <div style="width: 350px; margin: 200px auto; text-align: center">     
                    <h4 style="margin-top: 200px;text-align: left">Question:</h4>
                    <p>${answer.message}</p>
                    <h4 style="margin-top: 20px;text-align: left">Answer:</h4>
                    <p style="text-align: center; font-size: 17px">${answer.response} .</p>
                    <a href="/gpt">Next question</a>                    
                    <a style="margin-left: 20px" href="/ktor/prev">Prev question</a>
                    </div>
                    """.trimIndent()
        )
    }
}