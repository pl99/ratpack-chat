package ru.advantum.chat

import groovy.util.logging.Slf4j
import ratpack.func.Action
import ratpack.handling.RequestLogger
import ratpack.server.BaseDir
import ratpack.server.RatpackServer
import ratpack.server.RatpackServerSpec

/**
 * Created by Kotin on 28.04.2016.
 *
 * Main class for application
 */
@Slf4j
class ChatApp {
    public static void main(String... args) {
        RatpackServer.start((Action){ RatpackServerSpec ratpackServerSpec ->
            ratpackServerSpec.serverConfig() { config ->
                config
                        .baseDir(BaseDir.find())
                        .props("config.properties")
                        .props("rethinkdb.prop")
                        .sysProps()
                        .require("/rethinkdb", RethinkDbConfig)
            }

            .handlers { chain ->
                chain
                        .all(RequestLogger.ncsa(log))
                        .all(new LogHandler())
                        .path("", new RootHandler())
                        .path("ws", new ChatHandler())
            }
        })
    }
}
