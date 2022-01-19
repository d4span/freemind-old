/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
package freemind.modes.mindmapmode.actions.xml.actors

import freemind.controller.actions.generated.instance.CompoundAction
import freemind.controller.actions.generated.instance.HookNodeAction
import freemind.controller.actions.generated.instance.NodeChildParameter
import freemind.controller.actions.generated.instance.NodeListMember
import freemind.controller.actions.generated.instance.XmlAction
import freemind.extensions.DontSaveMarker
import freemind.extensions.HookFactory
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.PermanentNodeHook
import freemind.extensions.PermanentNodeHookAdapter
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.XMLElement
import freemind.modes.ExtendedMapFeedback
import freemind.modes.MindMapNode
import freemind.modes.ViewAbstraction
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.view.mindmapview.NodeView
import java.util.Properties
import java.util.Vector
import java.util.logging.Logger

/**
 * @author foltin
 * @date 01.04.2014
 */
class AddHookActor(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback!!) {
    /**
     * @param pMapFeedback
     */
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                this.javaClass.name
            )
        }
    }

    /**
     */
    private val hookFactory: HookFactory?
        get() = exMapFeedback?.hookFactory

    /**
     */
    private fun getInstanciationMethod(hookName: String?): HookInstanciationMethod? {
        val factory = hookFactory
        // determine instanciation method
        return factory?.getInstanciationMethod(hookName)
    }

    fun addHook(focussed: MindMapNode?, selecteds: List<MindMapNode?>, hookName: String, pHookProperties: Properties?) {
        val doAction = createHookNodeAction(
            focussed, selecteds,
            hookName, pHookProperties
        )
        var undoAction: XmlAction?
        // this is the non operation:
        undoAction = CompoundAction()
        if (getInstanciationMethod(hookName)!!.isPermanent) {
            // double application = remove.
            undoAction = createHookNodeUndoAction(focussed, selecteds, hookName)
        }
        if (getInstanciationMethod(hookName)!!.isUndoable) {
            execute(ActionPair(doAction, undoAction))
        } else {
            // direct invocation without undo and such stuff.
            invoke(focussed, selecteds, hookName, null)
        }
    }

    private fun createHookNodeUndoAction(
        focussed: MindMapNode?,
        selecteds: List<MindMapNode?>,
        hookName: String
    ): XmlAction {
        val undoAction = CompoundAction()
        val hookNodeAction = createHookNodeAction(
            focussed,
            selecteds, hookName, null
        )
        undoAction.addChoice(hookNodeAction)
        val instMethod = getInstanciationMethod(hookName)
        // get destination nodes
        val destinationNodes = instMethod!!.getDestinationNodes(
            exMapFeedback, focussed, selecteds
        )
        val adaptedFocussedNode = instMethod.getCenterNode(
            exMapFeedback, focussed!!, selecteds
        )
        // test if hook already present
        if (instMethod.isAlreadyPresent(hookName, adaptedFocussedNode)) {
            // remove the hook:
            for (currentDestinationNode in destinationNodes) {
                // find the hook in the current node, if present:
                for (hook in currentDestinationNode?.activatedHooks ?: emptyList()) {
                    if (hook.name == hookName) {
                        val child = XMLElement()
                        if (hook !is DontSaveMarker) {
                            hook.save(child)
                            if (child.countChildren() == 1) {
                                val parameters = child
                                    .children.firstElement() as XMLElement
                                if (Tools.safeEquals(
                                        parameters.name,
                                        PermanentNodeHookAdapter.PARAMETERS
                                    )
                                ) {
                                    // standard save mechanism
                                    val it = parameters.enumerateAttributeNames()
                                    while (it.hasNext()) {
                                        val name = it.next()
                                        val nodeHookChild = NodeChildParameter()
                                        nodeHookChild.key = name
                                        nodeHookChild.value = parameters
                                            .getStringAttribute(name)
                                        hookNodeAction.addNodeChildParameter(nodeHookChild)
                                    }
                                } else {
                                    logger!!.warning("Unusual save mechanism, implement me.")
                                }
                            } else {
                                logger!!.warning("Unusual save mechanism, implement me.")
                            }
                        }
                        /*
						 * fc, 30.7.2004: we have to break. otherwise the
						 * collection is modified at two points (i.e., the
						 * collection is not valid anymore after removing one
						 * element). But this is no problem, as there exist only
						 * "once" plugins currently.
						 */break
                    }
                }
            }
        }
        return undoAction
    }

    fun createHookNodeAction(
        focussed: MindMapNode?,
        selecteds: List<MindMapNode?>,
        hookName: String?,
        pHookProperties: Properties?
    ): HookNodeAction {
        val hookNodeAction = HookNodeAction()
        hookNodeAction.node = getNodeID(focussed)
        hookNodeAction.hookName = hookName
        // selectedNodes list
        for (node in selecteds) {
            val nodeListMember = NodeListMember()
            nodeListMember.node = getNodeID(node)
            hookNodeAction.addNodeListMember(nodeListMember)
        }
        if (pHookProperties != null) {
            for ((key, value) in pHookProperties) {
                val nodeChildParameter = NodeChildParameter()
                nodeChildParameter.key = key as String
                nodeChildParameter.value = value as String
                hookNodeAction.addNodeChildParameter(nodeChildParameter)
            }
        }
        return hookNodeAction
    }

    override fun act(action: XmlAction) {
        if (action is HookNodeAction) {
            val hookNodeAction = action
            val selected = getNodeFromID(
                hookNodeAction.node
            )
            val selecteds = Vector<MindMapNode?>()
            val i = hookNodeAction.listNodeListMemberList.iterator()
            while (i.hasNext()) {
                selecteds.add(getNodeFromID(i.next()?.node))
            }
            // reconstruct child-xml:
            val xmlParent = XMLElement()
            xmlParent.name = hookNodeAction.hookName
            val child = XMLElement()
            xmlParent.addChild(child)
            child.name = PermanentNodeHookAdapter.PARAMETERS
            val it = hookNodeAction.listNodeChildParameterList.iterator()
            while (it.hasNext()) {
                val childParameter = it.next()
                child.setAttribute(
                    childParameter?.key,
                    childParameter?.value
                )
            }
            invoke(selected, selecteds, hookNodeAction.hookName, xmlParent)
        }
    }

    override fun getDoActionClass(): Class<HookNodeAction> {
        return HookNodeAction::class.java
    }

    fun removeHook(pFocussed: MindMapNode?, pSelecteds: List<MindMapNode?>, pHookName: String) {
        val undoAction = createHookNodeAction(pFocussed, pSelecteds, pHookName, null)
        var doAction: XmlAction?
        // this is the non operation:
        doAction = CompoundAction()
        if (getInstanciationMethod(pHookName)!!.isPermanent) {
            // double application = remove.
            doAction = createHookNodeUndoAction(
                pFocussed, pSelecteds,
                pHookName
            )
        }
        execute(ActionPair(undoAction, doAction))
    }

    private operator fun invoke(
        focussed: MindMapNode?,
        selecteds: List<MindMapNode?>,
        hookName: String?,
        pXmlParent: XMLElement?
    ) {
        logger!!.finest("invoke(selecteds) called.")
        val instMethod = getInstanciationMethod(hookName)
        // get destination nodes
        val destinationNodes = instMethod!!.getDestinationNodes(exMapFeedback, focussed, selecteds)
        val adaptedFocussedNode = instMethod.getCenterNode(
            exMapFeedback, focussed!!, selecteds
        )
        // test if hook already present
        if (instMethod.isAlreadyPresent(hookName!!, adaptedFocussedNode)) {
            // remove the hook:
            val i = destinationNodes.iterator()
            while (i.hasNext()) {
                val currentDestinationNode = i.next()
                // find the hook ini the current node, if present:
                for (hook in currentDestinationNode?.activatedHooks ?: emptyList()) {
                    if (hook.name == hookName) {
                        currentDestinationNode?.removeHook(hook)
                        exMapFeedback?.nodeChanged(currentDestinationNode)
                        /*
						 * fc, 30.7.2004: we have to break. otherwise the
						 * collection is modified at two points (i.e., the
						 * collection is not valid anymore after removing one
						 * element). But this is no problem, as there exist only
						 * "once" plugins currently.
						 */break
                    }
                }
            }
        } else {
            // add the hook
            for (currentDestinationNode in destinationNodes) {
                val hook = exMapFeedback?.createNodeHook(
                    hookName,
                    currentDestinationNode
                )
                logger!!.finest("created hook $hookName")
                // set parameters, if present
                if (pXmlParent != null && hook is PermanentNodeHook) {
                    hook.loadFrom(pXmlParent)
                }
                // call invoke.
                currentDestinationNode?.invokeHook(hook)
                if (hook is PermanentNodeHook) {
                    logger!!.finest("This is a permanent hook $hookName")
                    // the focused receives the focus:
                    if (currentDestinationNode === adaptedFocussedNode) {
                        hook.onFocusNode(getNodeView(currentDestinationNode))
                    }
                    // using this method, the map is dirty now. This is
                    // important to
                    // guarantee, that the hooks are saved.
                    exMapFeedback?.nodeChanged(currentDestinationNode)
                }
            }
            finishInvocation(
                focussed, selecteds
            )
        }
    }

    /**
     * @param pNode
     * @return
     */
    private fun getNodeView(pNode: MindMapNode?): NodeView {
        return viewAbstraction.getNodeView(pNode)
    }

    protected val viewAbstraction: ViewAbstraction
        get() = exMapFeedback?.viewAbstraction
            ?: throw IllegalArgumentException("View abstraction not available.")

    /**
     * @param focussed
     * The real focussed node
     * @param selecteds
     * The list of selected nodes
     * @param adaptedFocussedNode
     * The calculated focussed node (if the hook specifies, that the
     * hook should apply to root, then this is the root node).
     * @param destinationNodes
     * The calculated list of selected nodes (see last)
     */
    private fun finishInvocation(
        focussed: MindMapNode?,
        selecteds: List<MindMapNode?>
    ) {
        // restore selection only, if nothing selected.
        if (viewAbstraction.selecteds.size == 0) {
            // select all destination nodes:
            exMapFeedback?.select(focussed, selecteds)
        }
    }

    companion object {
        protected var logger: Logger? = null
    }
}
