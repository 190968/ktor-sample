package com.example

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.http.*


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)

}

fun Application.module() {
    configureRouting()
    install(RequestValidation)
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondText(
                """
                <html>
                  <head><title>404 Not Found</title></head>
                  <body>
                    <h1>Page Not Found</h1>
                    <p>The resource you are looking for does not exist.</p>
                  </body>
                </html>
                """.trimIndent(),
                contentType = ContentType.Text.Html,
                status = HttpStatusCode.NotFound
            )
        }
    }
}
