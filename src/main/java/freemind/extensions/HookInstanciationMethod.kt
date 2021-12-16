/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 22.07.2004
 */
/*$Id: HookInstanciationMethod.java,v 1.1.4.1.16.3 2007/06/05 20:53:30 dpolivaev Exp $*/
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
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.modes.MapFeedback
import freemind.extensions.ModeControllerHook
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHook
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.HookFactory.RegistrationContainer
import freemind.extensions.HookRegistration
import freemind.extensions.ImportWizard
import java.io.IOException
import java.util.zip.ZipFile
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
import java.util.*

class HookInstanciationMethod private constructor(
        /**
         * @return Returns the isPermanent.
         */
        val isPermanent: Boolean, val isSingleton: Boolean,
        private val getter: DestinationNodesGetter,
        /**
         */
        val isUndoable: Boolean) {
    private interface DestinationNodesGetter {
        fun getDestinationNodes(controller: MapFeedback, focussed: MindMapNode?, selecteds: List<MindMapNode>): Collection<MindMapNode>
        fun getCenterNode(controller: MapFeedback, focussed: MindMapNode, selecteds: List<MindMapNode?>?): MindMapNode
    }

    private class DefaultDestinationNodesGetter : DestinationNodesGetter {
        override fun getDestinationNodes(controller: MapFeedback, focussed: MindMapNode?, selecteds: List<MindMapNode>): Collection<MindMapNode> {
            return selecteds
        }

        override fun getCenterNode(controller: MapFeedback,
                                   focussed: MindMapNode, selecteds: List<MindMapNode?>?): MindMapNode {
            return focussed
        }
    }

    private class RootDestinationNodesGetter : DestinationNodesGetter {
        override fun getDestinationNodes(controller: MapFeedback, focussed: MindMapNode?, selecteds: List<MindMapNode>): Collection<MindMapNode> {
            val returnValue = Vector<MindMapNode>()
            returnValue.add(controller.map.rootNode)
            return returnValue
        }

        override fun getCenterNode(controller: MapFeedback, focussed: MindMapNode, selecteds: List<MindMapNode?>?): MindMapNode {
            return controller.map.rootNode
        }
    }

    private class AllDestinationNodesGetter : DestinationNodesGetter {
        private fun addChilds(node: MindMapNode, allNodeCollection: MutableCollection<MindMapNode>) {
            allNodeCollection.add(node)
            val i: Iterator<MindMapNode> = node.childrenFolded()
            while (i.hasNext()) {
                val child = i.next()
                addChilds(child, allNodeCollection)
            }
        }

        override fun getDestinationNodes(controller: MapFeedback, focussed: MindMapNode?, selecteds: List<MindMapNode>): Collection<MindMapNode> {
            val returnValue = Vector<MindMapNode>()
            addChilds(controller.map.rootNode, returnValue)
            return returnValue
        }

        override fun getCenterNode(controller: MapFeedback, focussed: MindMapNode, selecteds: List<MindMapNode?>?): MindMapNode {
            return focussed
        }
    }

    /**
     */
    fun getDestinationNodes(controller: MapFeedback,
                            focussed: MindMapNode?, selecteds: List<MindMapNode>): Collection<MindMapNode> {
        return getter.getDestinationNodes(controller, focussed, selecteds)
    }

    /**
     */
    fun isAlreadyPresent(hookName: String, focussed: MindMapNode): Boolean {
        val i: Iterator<PermanentNodeHook> = focussed.activatedHooks.iterator()
        while (i.hasNext()) {
            val hook = i.next()
            if (hookName == hook.name) {
                return true
            }
        }
        return false
    }

    /**
     */
    fun getCenterNode(controller: MapFeedback,
                      focussed: MindMapNode, selecteds: List<MindMapNode?>?): MindMapNode {
        return getter.getCenterNode(controller, focussed, selecteds)
    }

    companion object {
        val Once = HookInstanciationMethod(
                true, true, DefaultDestinationNodesGetter(), true)

        /** The hook should only be added/removed to the root node.  */
        val OnceForRoot = HookInstanciationMethod(
                true, true, RootDestinationNodesGetter(), true)

        /** Each (or none) node should have the hook.  */
        val OnceForAllNodes = HookInstanciationMethod(
                true, true, AllDestinationNodesGetter(), true)

        /**
         * This is for MindMapHooks in general. Here, no undo- or redoaction are
         * performed, the undo information is given by the actions the hook
         * performs.
         */
        val Other = HookInstanciationMethod(
                false, false, DefaultDestinationNodesGetter(), false)

        /**
         * This is for MindMapHooks that wish to be applied to root, whereevery they
         * are called from. Here, no undo- or redoaction are performed, the undo
         * information is given by the actions the hook performs.
         */
        val ApplyToRoot = HookInstanciationMethod(
                false, false, RootDestinationNodesGetter(), false)
        val allInstanciationMethods: HashMap<String, HookInstanciationMethod>
            get() {
                val res = HashMap<String, HookInstanciationMethod>()
                res["Once"] = Once
                res["OnceForRoot"] = OnceForRoot
                res["OnceForAllNodes"] = OnceForAllNodes
                res["Other"] = Other
                res["ApplyToRoot"] = ApplyToRoot
                return res
            }
    }
}