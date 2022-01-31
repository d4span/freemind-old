package freemind.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.property.forAll
import io.mockk.every
import io.mockk.mockk

class MindmapNodeTest : StringSpec({
    "NodeAdapters use the converter upon initialization" {
        forAll { text: String?, xmlText: String? ->
            val toXmlConverter = mockk<(String?) -> String?>()
            every { toXmlConverter(text) } returns xmlText
            every { toXmlConverter(null) } returns null

            val fromXmlConverter = mockk<(String?) -> String?>()
            every { fromXmlConverter(xmlText) } returns text
            every { fromXmlConverter(null) } returns null

            MindmapNode(text, toXmlConverter, fromXmlConverter).let {
                it.text == text && if (text != null) it.xmlText == xmlText else it.xmlText == null
            }
        }
    }

    "NodeAdapters use the converter upon updates" {
        forAll { text: String?, updatedText: String?, xmlText: String?, updatedXmlText: String? ->
            val toXmlConverter = mockk<(String?) -> String?>()
            every { toXmlConverter(text) } returns xmlText
            every { toXmlConverter(updatedText) } returns updatedXmlText
            every { toXmlConverter(null) } returns null

            val fromXmlConverter = mockk<(String?) -> String?>()
            every { fromXmlConverter(xmlText) } returns text
            every { fromXmlConverter(updatedXmlText) } returns updatedText
            every { fromXmlConverter(null) } returns null

            val mindmapNode = MindmapNode(text, toXmlConverter, fromXmlConverter)
            mindmapNode.text = updatedText

            mindmapNode.let {
                it.text == updatedText && if (updatedText != null) it.xmlText == updatedXmlText else it.xmlText == null
            }
        }
    }
})
