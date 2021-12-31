/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*
 * Created on 29.02.2004
 *
 */
package freemind.extensions

import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.main.Resources
import freemind.modes.MapFeedback
import freemind.modes.ModeController
import java.net.URL
import java.util.Properties
import java.util.logging.Logger

/**
 * Implments MindMapHook as an Adapter class. Implementation is straight
 * forward.
 *
 * @author foltin
 */
open class HookAdapter : MindMapHook {
    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.NodeHook#getName()
	 */ override var name: String? = null
    private var properties: Properties? = null
    protected var controller: ModeController? = null

    // Logging:
    @JvmField
    protected var logger: Logger? = null

    /**
     * Stores the plugin base class as declared by the
     * plugin_registration/isBaseClass attribute.
     */
    private var baseClass: PluginBaseClassSearcher?
    @JvmField
    protected var mapFeedback: MapFeedback? = null

    /**
     */
    init {
        if (logger == null) logger = Resources.getInstance().getLogger(
            this.javaClass.name
        )
        baseClass = null
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.NodeHook#startupMapHook(java.lang.String)
	 */
    override fun startupMapHook() {
        // TODO Auto-generated method stub
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.NodeHook#shutdownMapHook()
	 */
    override fun shutdownMapHook() {
        controller = null
    }

    /**
     */
    protected fun getProperties(): Properties? {
        return properties
    }

    /**
     */
    override fun setProperties(properties: Properties?) {
        this.properties = properties
    }

    /**
     */
    override fun setController(controller: MapFeedback?) {
        mapFeedback = controller
        if (controller is ModeController) {
            this.controller = controller
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.MindMapHook#getResourceString(java.lang.String)
	 */
    override fun getResourceString(property: String?): String? {
        var result = properties!!.getProperty(property)
        if (result == null) {
            result = controller!!.getText(property)
        }
        if (result == null) {
            logger!!.warning("The following property was not found:$property")
        }
        return result
    }

    fun getResource(resourceName: String?): URL {
        return this.javaClass.classLoader.getResource(resourceName)
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.extensions.MindMapHook#getPluginBaseClass()
	 */
    override fun getPluginBaseClass(): Any? {
        return baseClass!!.pluginBaseObject
    }

    override fun setPluginBaseClass(baseClass: PluginBaseClassSearcher?) {
        this.baseClass = baseClass
    }

    /**
     * After tree node change, the focus must be obtained as it is invalid.
     */
    protected fun obtainFocusForSelected() {
        // Focus fix
        controller!!.controller.obtainFocusForSelected()
    }
}
