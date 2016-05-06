/**
 * Created by Kotin on 11.02.2016.
 */

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ratpack.groovy.Groovy
import ratpack.handling.RequestLogger
import ru.advantum.chat.ChatHandler
import ru.advantum.chat.LogHandler
import ru.advantum.chat.RootHandler

import static ratpack.groovy.Groovy.groovyMarkupTemplate
import static ratpack.groovy.Groovy.ratpack

final Logger logger = LoggerFactory.getLogger(Groovy.Ratpack.class);

ratpack {
    handlers {
        all(RequestLogger.ncsa(logger))
        all(new LogHandler())
        path("", new RootHandler())
        path("ws", new ChatHandler())

    }
}