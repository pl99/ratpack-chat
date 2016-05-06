/**
 * Created by Kotin on 11.02.2016.
 */
import ratpack.groovy.template.MarkupTemplateModule
import ru.advantum.chat.RethinkDbConfig

import static ratpack.groovy.Groovy.ratpack

ratpack {
    bindings {
        module MarkupTemplateModule
        module RethinkDbConfig
        bind(RethinkDbConfig)

    }
}