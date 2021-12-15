package freemind.modes

import freemind.model.MindmapNode
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.checkAll
import io.kotest.property.forAll
import io.mockk.mockk
import kotlin.test.assertNotNull

class MindMapNodeTest : StringSpec({
    "A MindMapNode is a MindmapNode" {
        checkAll<Boolean> {
            val mindMapNode: MindmapNode = mockk<MindMapNode>()
            assertNotNull(mindMapNode)
        }
    }

    "A MindMapNode extends MindmapNode" {
        forAll<Boolean> {
            val declaredMethods = MindMapNode::class.java.declaredMethods
            declaredMethods.none { method -> method.name == "getText" || method.name == "setText" }
        }
    }
})