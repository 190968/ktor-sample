package com.example

import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.ContentType

fun Application.configureRouting() {
    routing {
        userRoutes()
        get("/hello gpt") {
            val ss = call.request.headers
            call.respondText("Hello dghgfhdg  Super World! $ss")
        }
        get("/signup") {
            val name = call.request.queryParameters["name"]

            if (name == "bob") {
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = """    
                       
                        <h2 style="margin: 200px auto; text-align: center">Hello $name. Wellcome to account</h2>
                    """.trimIndent()
                )
            } else {
                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = """   
                    <h2 style="margin: 200px auto">Password is wrong</h2>
                    """.trimIndent()
                )
            }
        }
        get("/gpt") {
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = """
                <div style="width: 350px; margin: 200px auto; text-align: center">
                  
                   <form action="/signup" style="height: auto;display: flex; justify-content: space-around; flex-direction: column">
                      
                            <textarea 
                                rows="5" 
                                name="text"
                                cols="30" 
                                placeholder="Type your quechion here..."
                                style="padding: 5px; margin-top: 5px" id="name" 
                            ></textarea>
                       
                        
                            <input placeholder="Type your name here..." style="padding: 5px; margin: 15px 0; width: 100%" type="text" name="name"/>
                       
                        <button style="padding: 5px" type="submit">go</button>
                   </form>                   
                </div>
                
                """.trimIndent()
            )
        }
    }
}
