import groovy.text.Template
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.func.Action
import ratpack.groovy.template.MarkupTemplateModule
import ratpack.handling.Context
import ratpack.server.BaseDir
import ratpack.websocket.WebSocket
import ratpack.websocket.WebSocketClose
import ratpack.websocket.WebSocketMessage
import ratpack.websocket.WebSocketSpec
import ratpack.server.ServerConfigBuilder

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import static ratpack.websocket.WebSockets.websocket


Map<WebSocket, Action<String>> subscribers = [:]
final Logger logger = LoggerFactory.getLogger(Ratpack.class);

ratpack {
    serverConfig {
        ServerConfigBuilder config ->
            config
                    .baseDir(BaseDir.find())
                    .props("config.properties")
                    .env()
                    .sysProps()
    }

  bindings {
      module MarkupTemplateModule
  }

  handlers {
    get (){
      render groovyTemplate("index.html", title: "My Ratpack App")
//        render groovyMarkupTemplate("index.gtpl", title: "My Ratpack App")
    }

    get("ws") { Context ctx ->

      ctx.websocket { WebSocket ws ->
        subscribers[ws] = {
          ws.send (it)
        } as Action<String>
        new AutoCloseable() {
          void close() {
            subscribers.remove(ws)
          }
        }
      }.connect { WebSocketSpec spec ->
        spec.onMessage { WebSocketMessage m ->
          subscribers.values()*.execute(m.text.trim())
        }
        spec.onClose { WebSocketClose close ->
          close?.openResult.close()
        }
      }
    }


    get("publish/:value") {

    }

    files {dir "public"}
  }
}
