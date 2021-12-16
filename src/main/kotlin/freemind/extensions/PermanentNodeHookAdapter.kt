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
 * Created on 06.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.extensions

import freemind.controller.Controller.obtainFocusForSelected
import freemind.controller.actions.generated.instance.Plugin.listChoiceList
import freemind.controller.actions.generated.instance.PluginClasspath.jar
import freemind.controller.actions.generated.instance.Plugin.label
import freemind.controller.actions.generated.instance.PluginAction.name
import freemind.controller.actions.generated.instance.PluginAction.label
import freemind.controller.actions.generated.instance.PluginAction.listChoiceList
import freemind.controller.actions.generated.instance.PluginMenu.location
import freemind.controller.actions.generated.instance.PluginProperty.name
import freemind.controller.actions.generated.instance.PluginProperty.value
import freemind.controller.actions.generated.instance.PluginMode.className
import freemind.controller.actions.generated.instance.PluginAction.instanciation
import freemind.controller.actions.generated.instance.PluginAction.base
import freemind.controller.actions.generated.instance.PluginAction.className
import freemind.controller.actions.generated.instance.PluginAction.documentation
import freemind.controller.actions.generated.instance.PluginAction.iconPath
import freemind.controller.actions.generated.instance.PluginAction.keyStroke
import freemind.controller.actions.generated.instance.PluginRegistration.className
import freemind.controller.actions.generated.instance.PluginRegistration.listPluginModeList
import freemind.extensions.MindMapHook
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.extensions.ModeControllerHookAdapter
import freemind.view.mindmapview.MapView
import freemind.modes.ModeController
import freemind.extensions.ExportHook
import freemind.main.Tools
import java.awt.image.BufferedImage
import java.awt.Rectangle
import java.awt.Graphics
import java.io.FileOutputStream
import java.io.FileInputStream
import freemind.modes.FreeMindFileDialog
import javax.swing.JFileChooser
import java.text.MessageFormat
import javax.swing.JOptionPane
import java.util.Properties
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.modes.MapFeedback
import freemind.extensions.ModeControllerHook
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHook
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.HookFactory.RegistrationContainer
import freemind.extensions.HookRegistration
import freemind.extensions.ImportWizard
import java.util.StringTokenizer
import java.io.IOException
import java.util.Locale
import java.util.zip.ZipFile
import java.util.Enumeration
import java.util.zip.ZipEntry
import freemind.extensions.HookAdapter
import freemind.main.XMLElement
import kotlin.Throws
import freemind.controller.actions.generated.instance.PluginClasspath
import freemind.extensions.HookDescriptorBase
import java.net.URLClassLoader
import java.net.MalformedURLException
import freemind.extensions.HookFactory
import freemind.extensions.HookInstanciationMethod.DestinationNodesGetter
import freemind.extensions.HookInstanciationMethod.DefaultDestinationNodesGetter
import freemind.extensions.HookInstanciationMethod.RootDestinationNodesGetter
import freemind.extensions.HookInstanciationMethod.AllDestinationNodesGetter
import freemind.extensions.NodeHookAdapter
import freemind.extensions.PermanentNodeHookAdapter
import java.lang.InterruptedException
import java.lang.reflect.InvocationTargetException
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.controller.actions.generated.instance.PluginAction
import freemind.controller.actions.generated.instance.PluginMenu
import freemind.controller.actions.generated.instance.PluginProperty
import freemind.controller.actions.generated.instance.PluginMode
import freemind.controller.actions.generated.instance.PluginRegistration
import freemind.view.mindmapview.NodeView
import java.io.Writer
import java.util.HashMap

/**
 * Simple, straight forward implementation of PermanentNodeHook
 * with some support for saving and loading
 *
 * @author foltin
 */
open class PermanentNodeHookAdapter  // Logging:
// private static java.util.logging.Logger logger;
/**
 */
    : NodeHookAdapter(), PermanentNodeHook {
    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#shutdownMapHook()
	 */
    override fun shutdownMapHook() {
        logger!!.finest("shutdownMapHook")
        node = null
        map = null
        super.shutdownMapHook()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#onUpdateNodeHook()
	 */
    override fun onUpdateNodeHook() {
        logger!!.finest("onUpdateNodeHook")
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#onUpdateChildrenHook()
	 */
    override fun onUpdateChildrenHook(updatedNode: MindMapNode?) {
        logger!!.finest("onUpdateChildrenHook")
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#onAddChild(freemind.modes.MindMapNode)
	 */
    override fun onAddChild(newChildNode: MindMapNode?) {
        logger!!.finest("onAddChild")
    }

    override fun onNewChild(newChildNode: MindMapNode?) {
        logger!!.finest("onNewChild")
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.PermanentNodeHook#onRemoveChild(freemind.modes.
	 * MindMapNode)
	 */
    override fun onRemoveChild(oldChildNode: MindMapNode?) {
        logger!!.finest("onRemoveChild")
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.PermanentNodeHook#save(freemind.main.XMLElement)
	 */
    override fun save(xml: XMLElement) {
        val saveName = name
        // saveName=saveName.replace(File.separatorChar, '/');
        xml.setAttribute("name", saveName)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.extensions.PermanentNodeHook#loadFrom(freemind.main.XMLElement)
	 */
    override fun loadFrom(child: XMLElement?) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.NodeHook#onSelectHook()
	 */
    override fun onFocusNode(nodeView: NodeView?) {
        logger!!.finest("onSelectHook")
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.PermanentNodeHook#onDeselectHook()
	 */
    override fun onLostFocusNode(nodeView: NodeView?) {
        logger!!.finest("onDeselectHook")
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.PermanentNodeHook#onAddChildren(freemind.modes.
	 * MindMapNode)
	 */
    override fun onAddChildren(addedChild: MindMapNode?) {
        logger!!.finest("onAddChildren")
    }

    /**
     */
    protected fun loadNameValuePairs(xml: XMLElement): HashMap<String, String> {
        val result = HashMap<String, String>()
        if (xml.children.isEmpty()) {
            return result
        }
        val child = xml.children[0] as XMLElement
        if (child != null && PARAMETERS == child.name) {
            val i = child.enumerateAttributeNames()
            while (i.hasNext()) {
                val name = i.next()
                result[name] = child.getStringAttribute(name)
            }
        }
        return result
    }

    /**
     */
    protected fun saveNameValuePairs(nameValuePairs: HashMap<String?, Any?>, xml: XMLElement) {
        if (!nameValuePairs.isEmpty()) {
            val child = XMLElement()
            child.name = PARAMETERS
            for (key in nameValuePairs.keys) {
                val value = nameValuePairs[key]
                child.setAttribute(key, value)
            }
            xml.addChild(child)
        }
    }

    override fun onRemoveChildren(oldChildNode: MindMapNode?, oldDad: MindMapNode?) {
        logger!!.finest("onRemoveChildren")
    }

    override fun onViewCreatedHook(nodeView: NodeView?) {}
    override fun onViewRemovedHook(nodeView: NodeView?) {}

    /**
     */
    protected fun setToolTip(key: String?, value: String?) {
        setToolTip(node, key, value)
    }

    protected open fun setToolTip(node: MindMapNode?, key: String?, value: String?) {
        controller!!.setToolTip(node, key, value)
    }

    /* (non-Javadoc)
	 * @see freemind.extensions.PermanentNodeHook#executeTransaction(freemind.modes.mindmapmode.actions.xml.ActionPair)
	 */
    @Throws(InterruptedException::class, InvocationTargetException::class)
    protected fun executeTransaction(pair: ActionPair?) {
    }

    /* (non-Javadoc)
	 * @see freemind.extensions.PermanentNodeHook#registerFilter()
	 */
    fun registerFilter() {}

    /* (non-Javadoc)
	 * @see freemind.extensions.PermanentNodeHook#deregisterFilter()
	 */
    fun deregisterFilter() {}

    /* (non-Javadoc)
	 * @see freemind.extensions.PermanentNodeHook#filterAction(freemind.modes.mindmapmode.actions.xml.ActionPair)
	 */
    fun filterAction(pPair: ActionPair?): ActionPair? {
        return null
    }

    override fun processUnfinishedLinks() {}

    /* (non-Javadoc)
	 * @see freemind.extensions.PermanentNodeHook#saveHtml(java.io.Writer)
	 */
    @Throws(IOException::class)
    override fun saveHtml(pFileout: Writer?) {
    }

    companion object {
        const val PARAMETERS = "Parameters"
    }
}