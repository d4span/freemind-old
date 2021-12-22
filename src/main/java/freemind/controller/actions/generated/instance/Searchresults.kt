package freemind.controller.actions.generated.instance

import java.util.Collections

/* Searchresults...*/ class Searchresults : XmlAction() {
    /* constants from enums*/
    var timestamp: String? = null
    var attribution: String? = null
    var querystring: String? = null
    var polygon: String? = null
    var excludePlaceIds: String? = null
    var moreUrl: String? = null
    fun addPlace(place: Place?) {
        placeList.add(place)
    }

    fun addAtPlace(position: Int, place: Place?) {
        placeList.add(position, place)
    }

    fun getPlace(index: Int): Place {
        return placeList[index] as Place
    }

    fun removeFromPlaceElementAt(index: Int) {
        placeList.removeAt(index)
    }

    fun sizePlaceList(): Int {
        return placeList.size
    }

    fun clearPlaceList() {
        placeList.clear()
    }

    val listPlaceList: List<Place?>
        get() = Collections.unmodifiableList(placeList)
    protected var placeList: ArrayList<Place?> = ArrayList()
} /* Searchresults*/
