package freemind.model

import groovy.util.GroovyTestCase.assertEquals
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class MindmapNodeTest : StringSpec({
    "A MindmapNode has a read/write text property" {
        checkAll(Arb.string()) { text ->
            val mindmapNode = object : MindmapNode {
                private var textField = ""

                override var text: String
                    get() = textField
                    set(value) {
                        textField = value
                    }
            }

            mindmapNode.text = text
            assertEquals(text, mindmapNode.text)
        }
    }
})
