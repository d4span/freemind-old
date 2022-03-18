package freemind.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.checkAll
import io.mockk.every
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class MindmapNodeTest : StringSpec({
    "NodeAdapters use the supplied converter upon initialization" {
        checkAll { text: String?, xmlText: String? ->
            val toXmlConverter = mockk<(String?) -> String?>()
            every { toXmlConverter(text) } returns xmlText
            every { toXmlConverter(null) } returns null

            val fromXmlConverter = mockk<(String?) -> String?>()
            every { fromXmlConverter(xmlText) } returns text
            every { fromXmlConverter(null) } returns null

            with(MindmapNode(text, toXmlConverter, fromXmlConverter)) {
                assertSame(text, this.text)

                if (text == null)
                    assertNull(this.xmlText)
                else
                    assertSame(xmlText, this.xmlText)
            }
        }
    }

    "NodeAdapters use the converter upon updates" {
        checkAll { text: String?, updatedText: String?, xmlText: String?, updatedXmlText: String? ->
            val toXmlConverter = mockk<(String?) -> String?>()
            every { toXmlConverter(text) } returns xmlText
            every { toXmlConverter(updatedText) } returns updatedXmlText
            every { toXmlConverter(null) } returns null

            val fromXmlConverter = mockk<(String?) -> String?>()
            every { fromXmlConverter(xmlText) } returns text
            every { fromXmlConverter(updatedXmlText) } returns updatedText
            every { fromXmlConverter(null) } returns null

            val mindmapNode = MindmapNode(text, toXmlConverter, fromXmlConverter)

            with(mindmapNode.copy(text = updatedText)) {
                assertEquals(updatedText, this.text)

                if (updatedText != null)
                    assertEquals(updatedXmlText, this.xmlText)
                else
                    assertNull(this.xmlText)
            }
        }
    }
})
