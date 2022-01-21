/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
package freemind.modes.browsemode

import freemind.controller.MenuBar
import freemind.controller.MenuItemEnabledListener
import freemind.controller.StructuredMenuHolder
import freemind.extensions.HookFactory
import freemind.main.Resources
import freemind.main.Tools
import freemind.main.XMLParseException
import freemind.modes.MapAdapter
import freemind.modes.MindMap
import freemind.modes.MindMapNode
import freemind.modes.Mode
import freemind.modes.ModeController
import freemind.modes.NodeAdapter
import freemind.modes.common.GotoLinkNodeAction
import freemind.modes.common.plugins.MapNodePositionHolderBase
import freemind.modes.common.plugins.NodeNoteBase
import freemind.modes.viewmodes.ViewControllerAdapter
import freemind.view.ImageFactory.Companion.instance
import freemind.view.mindmapview.MainView
import java.awt.event.ActionEvent
import java.awt.event.MouseEvent
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URISyntaxException
import java.net.URL
import java.util.logging.Logger
import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.ImageIcon
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JToolBar

@Suppress("DEPRECATION")
class BrowseController(mode: Mode?) : ViewControllerAdapter(mode) {
    private val popupmenu: JPopupMenu
    private val toolbar: JToolBar
    @JvmField
    var followLink: Action
    var nodeUp: Action? = null
    var nodeDown: Action? = null
    private val mBrowseHookFactory: HookFactory
    private var noteIcon: ImageIcon? = null
    @JvmField
    var followMapLink: FollowMapLink

    class FollowMapLink(private val modeController: ViewControllerAdapter) :
        AbstractAction(
            modeController.getText("follow_map_link"), MapNodePositionHolderBase.getMapLocationIcon()
        ),
        MenuItemEnabledListener {
        private val logger: Logger

        init {
            logger = modeController.frame.getLogger(
                this.javaClass.name
            )
        }

        override fun actionPerformed(e: ActionEvent) {
            val hook = hook
            if (hook != null) {
                val barePositions = hook.barePosition
                try {
                    // GRR, this is doubled code :-(
                    val tileSources = HashMap<String, String>()
                    tileSources[MapNodePositionHolderBase.TILE_SOURCE_MAPNIK] = MapNodePositionHolderBase.SHORT_MAPNIK
                    tileSources[MapNodePositionHolderBase.TILE_SOURCE_CYCLE_MAP] =
                        MapNodePositionHolderBase.SHORT_CYCLE_MAP
                    tileSources[MapNodePositionHolderBase.TILE_SOURCE_TRANSPORT_MAP] =
                        MapNodePositionHolderBase.SHORT_TRANSPORT_MAP
                    tileSources[MapNodePositionHolderBase.TILE_SOURCE_MAP_QUEST_OPEN_MAP] =
                        MapNodePositionHolderBase.SHORT_MAP_QUEST_OPEN_MAP
                    val link = (
                        "http://www.openstreetmap.org/?" + "mlat=" +
                            barePositions[0] + "&mlon=" + barePositions[1] +
                            "&lat=" + barePositions[2] + "&lon=" +
                            barePositions[3] + "&zoom=" + barePositions[4] +
                            "&layers=" + tileSources[barePositions[5]]
                        )
                    logger.fine("Try to open link $link")
                    modeController.frame.openDocument(URL(link))
                } catch (e1: MalformedURLException) {
                    Resources.getInstance().logException(e1)
                } catch (e1: Exception) {
                    Resources.getInstance().logException(e1)
                }
            }
        }

        protected val hook: MapNodePositionHolderBase?
            get() {
                val selected = modeController.selected
                return MapNodePositionHolderBase
                    .getBaseHook(selected)
            }

        /* (non-Javadoc)
		 * @see freemind.controller.MenuItemEnabledListener#isEnabled(javax.swing.JMenuItem, javax.swing.Action)
		 */
        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            return hook != null
        }
    }

    init {
        mBrowseHookFactory = BrowseHookFactory()
        // Daniel: Actions are initialized here and not above because of
        // some error it would produce. Not studied in more detail.
        followLink = FollowLinkAction()
        followMapLink = FollowMapLink(this)
        popupmenu = BrowsePopupMenu(this)
        toolbar = BrowseToolBar(this)
        setAllActions(false)
        // for displaying notes.
        registerNodeSelectionListener(NodeNoteViewer(this), false)
    }

    override fun startupController() {
        super.startupController()
        invokeHooksRecursively(rootNode as NodeAdapter, map)
    }

    override fun restoreMapsLastState(
        pNewModeController: ModeController,
        pModel: MapAdapter
    ) {
        // intentionally do nothing.
    }

    override fun newModel(newModeController: ModeController): MapAdapter {
        val model = BrowseMapModel(null, newModeController)
        newModeController.setModel(model)
        return model
    }

    override fun plainClick(e: MouseEvent) {
        /* perform action only if one selected node. */
        if (selecteds.size != 1) return
        val component = e.component as MainView
        if (component.isInFollowLinkRegion(e.x.toDouble())) {
            loadURL()
        } else {
            val node = component.nodeView.model
            if (!node.hasChildren()) {
                // the emulate the plain click.
                doubleClick(e)
                return
            }
            toggleFolded.toggleFolded(selecteds.listIterator())
        }
    }

    fun doubleClick() {
        /* If the link exists, follow the link; toggle folded otherwise */
        if (selected.link == null) {
            toggleFolded.toggleFolded()
        } else {
            loadURL()
        }
    }

    // public void anotherNodeSelected(MindMapNode n) {
    // super.anotherNodeSelected(n);
    // if(n.isRoot()){
    // return;
    // }
    // //Presentation:
    // setFolded(n, false);
    // foldOthers(n);
    // }
    //
    //
    // private void foldOthers(MindMapNode n) {
    // if(n.isRoot()){
    // return;
    // }
    // MindMapNode parent = n.getParentNode();
    // for (Iterator iter = parent.childrenUnfolded(); iter.hasNext();) {
    // MindMapNode element = (MindMapNode) iter.next();
    // if(element != n){
    // setFolded(element, true);
    // }
    // }
    // foldOthers(parent);
    // }
    override fun newNode(userObject: Any, map: MindMap): MindMapNode {
        return BrowseNodeModel(userObject, map)
    }

    override fun getPopupMenu(): JPopupMenu {
        return popupmenu
    }

    /**
     * Link implementation: If this is a link, we want to make a popup with at
     * least removelink available.
     */
    override fun getPopupForModel(obj: Any): JPopupMenu? {
        if (obj is BrowseArrowLinkModel) {
            // yes, this is a link.
            val link = obj
            val arrowLinkPopup = JPopupMenu()
            arrowLinkPopup.add(getGotoLinkNodeAction(link.source))
            arrowLinkPopup.add(getGotoLinkNodeAction(link.target))
            arrowLinkPopup.addSeparator()
            // add all links from target and from source:
            val nodeAlreadyVisited = HashSet<MindMapNode>()
            nodeAlreadyVisited.add(link.source)
            nodeAlreadyVisited.add(link.target)
            val links = model.linkRegistry.getAllLinks(link.source)
            links.addAll(model.linkRegistry.getAllLinks(link.target))
            for (i in links.indices) {
                val foreign_link = links[i] as BrowseArrowLinkModel
                if (nodeAlreadyVisited.add(foreign_link.target)) {
                    arrowLinkPopup.add(
                        getGotoLinkNodeAction(
                            foreign_link
                                .target
                        )
                    )
                }
                if (nodeAlreadyVisited.add(foreign_link.source)) {
                    arrowLinkPopup.add(
                        getGotoLinkNodeAction(
                            foreign_link
                                .source
                        )
                    )
                }
            }
            return arrowLinkPopup
        }
        return null
    }

    /**
     */
    private fun getGotoLinkNodeAction(destination: MindMapNode): GotoLinkNodeAction {
        return GotoLinkNodeAction(this, destination)
    }

    override fun getModeToolBar(): JToolBar {
        return toolBar
    }

    val toolBar: BrowseToolBar
        get() = toolbar as BrowseToolBar

    // public void loadURL(String relative) {
    // // copy from mind map controller:
    // if (relative.startsWith("#")) {
    // // inner map link, fc, 12.10.2004
    // String target = relative.substring(1);
    // try {
    // MindMapNode node = getNodeFromID(target);
    // centerNode(node);
    // return;
    // } catch (Exception e) {
    // // bad luck.
    // getFrame().out(Tools.expandPlaceholders(getText("link_not_found"),
    // target));
    // return;
    // }
    // }
    //
    // URL absolute = null;
    // try {
    // absolute = new URL(getMap().getURL(), relative);
    // getFrame().out(absolute.toString());
    // } catch (MalformedURLException ex) {
    // freemind.main.Resources.getInstance().logExecption(ex);
    // getController().errorMessage(
    // getText("url_error") + " " + ex.getMessage());
    // // getFrame().err(getText("url_error"));
    // return;
    // }
    //
    // String type = Tools.getExtension(absolute.getFile());
    // try {
    // if
    // (type.equals(freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT))
    // {
    // getFrame().setWaitingCursor(true);
    // load(absolute);
    // } else {
    // getFrame().openDocument(absolute);
    // }
    // } catch (Exception ex) {
    // getController().errorMessage(getText("url_load_error") + absolute);
    // freemind.main.Resources.getInstance().logExecption( ex);
    // // for some reason, this exception is thrown anytime...
    // } finally {
    // getFrame().setWaitingCursor(false);
    // }
    //
    // }
    @Throws(IOException::class, XMLParseException::class, URISyntaxException::class)
    override fun load(url: URL): ModeController {
        val newModeController = super.load(url) as ModeController
        // decorator pattern.
        (newModeController.modeToolBar as BrowseToolBar).setURLField(
            url
                .toString()
        )
        return newModeController
    }

    @Throws(IOException::class)
    override fun load(pFile: File): ModeController {
        val newModeController = super.load(pFile) as ModeController
        // decorator pattern.
        (newModeController.modeToolBar as BrowseToolBar).setURLField(
            Tools
                .fileToUrl(pFile).toString()
        )
        return newModeController
    }

    override fun newMap(mapModel: MindMap, modeController: ModeController) {
        setNoteIcon(mapModel.rootNode)
        super.newMap(mapModel, modeController)
    }

    private fun setNoteIcon(node: MindMapNode) {
        val noteText = node.noteText
        if (noteText != null && noteText != "") {
            // icon
            if (noteIcon == null) {
                noteIcon = instance!!.createUnscaledIcon(
                    controller.getResource(
                        "images/knotes.png"
                    )
                )
            }
            node.setStateIcon(NodeNoteBase.NODE_NOTE_ICON, noteIcon)
        }
        val children = node.childrenUnfolded()
        while (children.hasNext()) {
            setNoteIcon(children.next() as MindMapNode)
        }
    }

    /**
     * Enabled/Disabled all actions that are dependent on whether there is a map
     * open or not.
     */
    override fun setAllActions(enabled: Boolean) {
        super.setAllActions(enabled)
        toggleFolded.isEnabled = enabled
        toggleChildrenFolded.isEnabled = enabled
        followLink.isEnabled = enabled
    }

    // ////////
    // Actions
    // ///////
    private inner class FollowLinkAction internal constructor() : AbstractAction(getText("follow_link")) {
        override fun actionPerformed(e: ActionEvent) {
            loadURL()
        }
    }

    /*
	 * (non-Javadoc)
	 *
	 * @see freemind.modes.ModeController#updateMenus(freemind.controller.
	 * StructuredMenuHolder)
	 */
    override fun updateMenus(holder: StructuredMenuHolder) {
        add(holder, MenuBar.EDIT_MENU + "/find/find", find, "keystroke_find")
        add(
            holder, MenuBar.EDIT_MENU + "/find/findNext", findNext,
            "keystroke_find_next"
        )
        add(
            holder, MenuBar.EDIT_MENU + "/find/followLink", followLink,
            "keystroke_follow_link"
        )
        holder.addSeparator(MenuBar.EDIT_MENU + "/find")
        add(
            holder, MenuBar.EDIT_MENU + "/find/toggleFolded", toggleFolded,
            "keystroke_toggle_folded"
        )
        add(
            holder, MenuBar.EDIT_MENU + "/find/toggleChildrenFolded",
            toggleChildrenFolded, "keystroke_toggle_children_folded"
        )
    }

    override fun getHookFactory(): HookFactory {
        return mBrowseHookFactory
    }

    /* (non-Javadoc)
	 * @see freemind.modes.ControllerAdapter#loadInternally(java.net.URL, freemind.modes.MapAdapter)
	 */
    @Throws(URISyntaxException::class, XMLParseException::class, IOException::class)
    override fun loadInternally(url: URL, pModel: MapAdapter) {
        (pModel as BrowseMapModel).setURL(url)
        val root = loadTree(url)
        if (root != null) {
            pModel.setRoot(root)
        } else {
            // System.err.println("Err:"+root.toString());
            throw IOException()
        }
    }

    fun loadTree(url: URL): BrowseNodeModel? {
        var root: BrowseNodeModel?
        var urlStreamReader: InputStreamReader?
        urlStreamReader = try {
            InputStreamReader(url.openStream())
        } catch (ex: java.security.AccessControlException) {
            frame.controller
                .errorMessage(
                    "Could not open URL " + url.toString() +
                        ". Access Denied."
                )
            System.err.println(ex)
            return null
        } catch (ex: Exception) {
            frame.controller.errorMessage(
                "Could not open URL $url."
            )
            System.err.println(ex)
            // freemind.main.Resources.getInstance().logExecption(ex);
            return null
        }
        return try {
            val IDToTarget = HashMap<String, NodeAdapter>()
            root = map.createNodeTreeFromXml(urlStreamReader, IDToTarget) as BrowseNodeModel
            urlStreamReader?.close()
            root
        } catch (ex: Exception) {
            System.err.println(ex)
            null
        }
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap.MapFeedback#out(java.lang.String)
	 */
    override fun out(pFormat: String) {
        // TODO Auto-generated method stub
    }
}
