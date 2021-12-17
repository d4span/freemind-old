package freemind.controller.actions.generated.instance

import java.util.*

/* CollaborationOffers...*/   class CollaborationOffers : CollaborationActionBase() {
    /* constants from enums*/
    var isSingleOffer = false
    fun addCollaborationMapOffer(collaborationMapOffer: CollaborationMapOffer?) {
        collaborationMapOfferList.add(collaborationMapOffer)
    }

    fun addAtCollaborationMapOffer(position: Int, collaborationMapOffer: CollaborationMapOffer?) {
        collaborationMapOfferList.add(position, collaborationMapOffer)
    }

    fun getCollaborationMapOffer(index: Int): CollaborationMapOffer {
        return collaborationMapOfferList[index] as CollaborationMapOffer
    }

    fun removeFromCollaborationMapOfferElementAt(index: Int) {
        collaborationMapOfferList.removeAt(index)
    }

    fun sizeCollaborationMapOfferList(): Int {
        return collaborationMapOfferList.size
    }

    fun clearCollaborationMapOfferList() {
        collaborationMapOfferList.clear()
    }

    val listCollaborationMapOfferList: List<*>
        get() = Collections.unmodifiableList(collaborationMapOfferList)
    protected var collaborationMapOfferList: MutableList<CollaborationMapOffer?> = mutableListOf()
} /* CollaborationOffers*/