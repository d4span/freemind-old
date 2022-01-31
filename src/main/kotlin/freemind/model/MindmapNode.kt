package freemind.model

class MindmapNode(
    var text: String?,
    private val toXmlConverter: (String?) -> String?,
    private val fromXmlConverter: (String?) -> String?
) {

    var xmlText: String?
        get() = toXmlConverter(text)
        set(value) {
            text = fromXmlConverter(value)
        }
}
