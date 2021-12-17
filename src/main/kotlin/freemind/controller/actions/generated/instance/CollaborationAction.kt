package freemind.controller.actions.generated.instance

/* CollaborationAction...*/
class CollaborationAction : XmlAction() {
    var user: String? = null
    var timestamp: String? = null
    var cmd: String? = null
    var map: String? = null
    var filename: String? = null

    companion object {
        /* constants from enums*/
        const val REQUEST_MAP_SHARING = "request_map_sharing"
        const val ACCEPT_MAP_SHARING = "accept_map_sharing"
        const val STOP_MAP_SHARING = "stop_map_sharing"
        const val DECLINE_MAP_SHARING = "decline_map_sharing"
    }
} /* CollaborationAction*/