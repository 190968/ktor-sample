package com.example.routes.gpt

import io.ktor.http.ContentType
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun  Route.gpt() {
    get("/gpt") {
        call.respondText(
            contentType = ContentType.parse("text/html"),
            text = """
                <div style="width: 350px; margin: 200px auto; text-align: center">                  
                   <form action="/ktor/answer" method="post" style="height: auto;display: flex; justify-content: space-around; flex-direction: column">
                      
                            <textarea 
                                rows="5" 
                                name="text"
                                required
                                cols="30" 
                                placeholder="Type your question here..."
                                style="border-radius: 15px;padding: 15px; margin-top: 15px" id="name" 
                            ></textarea>                
                        
                            <input  required placeholder="Type your name here..." style="padding: 15px; margin: 15px 0; width: 100%" type="text" name="name"/>
                       
                        <button style="border: none;font-size: 20px;padding: 15px;border-radius: 15px" type="submit">GO</button>
                   </form>                   
                </div>                
                """.trimIndent()
        )
    }
}