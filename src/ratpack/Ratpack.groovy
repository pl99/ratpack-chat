import ratpack.func.Action
import ratpack.groovy.templating.TemplatingModule
import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketMessage
import ratpack.websocket.WebSocketSpec

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import static ratpack.websocket.WebSockets.websocket

Map<WebSocket, Action<String>> subscribers = [:]

ratpack {
  bindings {
    add TemplatingModule
  }

  handlers {
    get {
      render groovyTemplate("index.html", title: "My Ratpack App")
    }
    get("ws") {
      websocket(context) { WebSocket ws ->
        ws.send("Welcome")
        subscribers[ws] = { String m ->
          ws.send m
        } as Action<String>
        new AutoCloseable() {
          void close() {
            subscribers.remove(ws)
          }
        }
      } connect { WebSocketSpec spec ->
        spec.onMessage { WebSocketMessage m ->
          subscribers.values()*.execute(m.text.trim())
        }
        spec.onClose { WebSocketClose close ->
          close.openResult.close()
        }
      }
    }

    get("publish/:value") {

    }
        
    assets "public"
  }
}
