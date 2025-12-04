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
                <!DOCTYPE html >
               
                <head>
                    <meta charset="UTF-8">
                    <title>All Current Tasks</title>
                </head>
                <body>
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
                        
                            <input  
                                required 
                                placeholder="Type your name here..." 
                                style="border-radius: 15px;padding: 15px; margin: 15px 0" 
                                type="text" name="name"
                            />
                       
                        <button style="margin-bottom: 15px; border-color: grey; font-size: 20px;padding: 15px;border-radius: 15px" type="submit">GO</button>
                   </form>
                   <a href="/ktor/prev" >last question</a>
                  
                </div> 
                </body>
                <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.8/dist/htmx.min.js" integrity="sha384-/TgkGk7p307TH7EXJDuUlgG3Ce1UVolAOFopFekQkkXihi5u/6OCvVKyz1W+idaz" cross origin="anonymous"></script>
                """.trimIndent()
        )
    }
}