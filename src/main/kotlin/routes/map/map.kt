package com.example.routes.map

import io.ktor.http.ContentType
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun  Route.map(env: String?) {

    get("/ktor/map") {
        val lat  = call.queryParameters["lat"]
        val lon = call.queryParameters["lon"]

        val apimapkey = when(env) {
            "dev" -> "02835b43e315415b81400e7cff3227ac"
            else -> System.getenv("API_MAP_KEY")
        }
        call.respondText(
            contentType = ContentType.parse("text/html"),
            text = """
                <!DOCTYPE html >
               
                <head>
                    <meta charset="UTF-8">
                    <script type="text/javascript" src="https://unpkg.com/maplibre-gl@1.15.2/dist/maplibre-gl.js"></script>
                    <link rel="stylesheet" type="text/css" href="https://unpkg.com/maplibre-gl@1.15.2/dist/maplibre-gl.css">
                    <title>Gpt question</title>
                </head>
                <body>               
                    <div style="width: 1000px;height:700px;margin: 0 auto" id="my-map"></div>
                </body>
                <script> 


                  const map = new maplibregl.Map({
                       container: 'my-map',
                       style: `https://maps.geoapify.com/v1/styles/klokantech-basic/style.json?apiKey=${apimapkey}`,
                       center: [${lon}, ${lat}], 
                       zoom: 9 // starting zoom
                  });
                  let marker = new maplibregl.Marker()
                        .setLngLat([${lon}, ${lat}])
                        .addTo(map);

                    map.addControl(new maplibregl.NavigationControl());
                </script> 
                
                
                """.trimIndent()
        )
    }
}