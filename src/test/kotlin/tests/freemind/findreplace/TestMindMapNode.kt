/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2006  Christian Foltin <christianfoltin@users.sourceforge.net>
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
/*$Id: TestMindMapNode.java,v 1.1.2.17 2008/05/26 19:25:09 christianfoltin Exp $*/
package tests.freemind.findreplace

import freemind.modes.MindMapNode.isFolded
import freemind.modes.MindMapNode.hasFoldedParents
import freemind.main.Tools.arrayToUrls
import freemind.main.Tools.urlStringToUrls
import freemind.main.Tools.getUpdateReader
import freemind.main.Tools.getFile
import freemind.main.Tools.fileToUrl
import freemind.main.Tools.urlToFile
import freemind.main.Tools.isWindows
import freemind.main.Tools.getPrefix
import freemind.main.Tools.fileToRelativeUrlString
import freemind.main.Tools.countOccurrences
import freemind.main.Tools.getPageFormatAsString
import freemind.main.Tools.setPageFormatFromString
import freemind.main.Tools.copyChangedProperties
import freemind.main.Tools.makeFileHidden
import freemind.main.Base64Coding.encode64
import freemind.main.Base64Coding.decode64
import freemind.view.mindmapview.IndependantMapViewCreator.exportFileToPng
import freemind.view.mindmapview.IndependantMapViewCreator.createMapViewForFile
import freemind.view.mindmapview.MapView.innerBounds
import freemind.controller.Controller.initialization
import freemind.modes.mindmapmode.MindMapMode.init
import freemind.modes.mindmapmode.MindMapMode.createModeController
import freemind.modes.ControllerAdapter.setModel
import freemind.modes.NodeAdapter.insert
import freemind.modes.MapAdapter.root
import freemind.modes.ControllerAdapter.view
import freemind.main.Tools.waitForEventQueue
import freemind.modes.NodeAdapter.shiftY
import freemind.modes.MapAdapter.insertNodeInto
import freemind.view.mindmapview.MapView.viewPosition
import freemind.view.mindmapview.MapView.scrollBy
import freemind.view.mindmapview.MapView.root
import freemind.view.mindmapview.NodeView.getMainView
import freemind.main.Tools.getVectorWithSingleElement
import freemind.view.mindmapview.NodeView.childrenViews
import freemind.view.mindmapview.NodeView.model
import freemind.view.mindmapview.MapView.getViewers
import freemind.main.Tools.convertPointToAncestor
import freemind.main.IFreeMindSplash.setVisible
import freemind.main.IFreeMindSplash.getFeedBack
import freemind.main.FeedBack.setMaximumValue
import freemind.main.FeedBack.increase
import freemind.controller.actions.generated.instance.Plugin.listChoiceList
import freemind.controller.actions.generated.instance.PluginAction.label
import freemind.controller.actions.generated.instance.PluginAction.listChoiceList
import freemind.controller.actions.generated.instance.PluginProperty.name
import freemind.controller.actions.generated.instance.PluginProperty.value
import freemind.main.Tools.copyStream
import freemind.extensions.HookAdapter.setController
import freemind.extensions.HookAdapter.setProperties
import freemind.controller.LastOpenedList.add
import freemind.controller.LastOpenedList.save
import freemind.controller.actions.generated.instance.PatternPropertyBase.value
import freemind.controller.actions.generated.instance.Pattern.patternChild
import freemind.controller.actions.generated.instance.Pattern.name
import freemind.controller.actions.generated.instance.Searchresults.sizePlaceList
import freemind.controller.actions.generated.instance.Searchresults.listPlaceList
import freemind.controller.actions.generated.instance.Place.lat
import freemind.controller.actions.generated.instance.Reversegeocode.getResult
import freemind.controller.actions.generated.instance.ResultBase.placeId
import freemind.controller.actions.generated.instance.ResultBase.content
import freemind.main.FreeMindStarter.readDefaultPreferences
import freemind.common.FreeMindTask.isFinished
import freemind.controller.actions.generated.instance.CollaborationGetOffers.userId
import freemind.main.Tools.userName
import freemind.controller.actions.generated.instance.CollaborationGetOffers.password
import freemind.controller.actions.generated.instance.CollaborationOffers.listCollaborationMapOfferList
import freemind.controller.actions.generated.instance.CollaborationMapOffer.map
import freemind.controller.actions.generated.instance.CollaborationHello.map
import freemind.controller.actions.generated.instance.CollaborationWelcome.map
import freemind.controller.actions.generated.instance.CollaborationReceiveLock.id
import freemind.controller.actions.generated.instance.CollaborationTransaction.doAction
import freemind.modes.ExtendedMapFeedbackAdapter.actorFactory
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory.changeNoteTextActor
import freemind.modes.mindmapmode.actions.xml.actors.ChangeNoteTextActor.createEditNoteToNodeAction
import freemind.modes.ExtendedMapFeedbackImpl.map
import freemind.modes.MindMap.rootNode
import freemind.main.Tools.marshall
import freemind.controller.actions.generated.instance.CollaborationTransaction.id
import freemind.controller.actions.generated.instance.CollaborationTransaction.undoAction
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.userId
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.password
import freemind.modes.MapFeedback.map
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.mapName
import freemind.modes.MindMap.getXml
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.map
import freemind.modes.MapAdapter.loadTree
import freemind.modes.mindmapmode.MindMapMapModel.getFilteredXml
import freemind.modes.ExtendedMapFeedbackAdapter.setStrikethrough
import freemind.modes.MindMapNode.isStrikethrough
import freemind.modes.ExtendedMapFeedbackAdapter.setBold
import freemind.modes.MindMapNode.isBold
import freemind.modes.ExtendedMapFeedbackAdapter.setItalic
import freemind.modes.MindMapNode.isItalic
import freemind.modes.ExtendedMapFeedbackAdapter.setFontSize
import freemind.modes.MindMapNode.getFontSize
import freemind.modes.ExtendedMapFeedbackAdapter.addNewNode
import freemind.modes.ExtendedMapFeedbackAdapter.deleteNode
import freemind.modes.ExtendedMapFeedbackAdapter.paste
import freemind.modes.MindMapNode.icons
import freemind.modes.ExtendedMapFeedbackAdapter.addIcon
import freemind.modes.ExtendedMapFeedbackAdapter.removeLastIcon
import freemind.modes.ExtendedMapFeedbackAdapter.removeAllIcons
import freemind.modes.ExtendedMapFeedbackAdapter.setCloud
import freemind.modes.MindMapNode.cloud
import freemind.modes.ExtendedMapFeedbackAdapter.setCloudColor
import freemind.modes.MindMapLine.color
import freemind.modes.ExtendedMapFeedbackAdapter.setEdgeStyle
import freemind.modes.ExtendedMapFeedbackAdapter.setEdgeWidth
import freemind.modes.ExtendedMapFeedbackAdapter.setEdgeColor
import freemind.modes.ExtendedMapFeedbackAdapter.setFontFamily
import freemind.modes.MindMapNode.fontFamilyName
import freemind.modes.ExtendedMapFeedbackAdapter.moveNodePosition
import freemind.modes.MindMapNode.vGap
import freemind.modes.MindMapNode.hGap
import freemind.modes.MindMapNode.shiftY
import freemind.modes.ExtendedMapFeedbackAdapter.setNodeStyle
import freemind.modes.MindMapNode.style
import freemind.modes.MindMapNode.hasStyle
import freemind.modes.ExtendedMapFeedbackAdapter.addLink
import freemind.modes.MindMap.linkRegistry
import freemind.modes.MindMapLinkRegistry.getAllLinksFromMe
import freemind.modes.MindMapLink.target
import freemind.modes.ExtendedMapFeedbackAdapter.setArrowLinkEndPoints
import freemind.modes.MindMapArrowLink.startInclination
import freemind.modes.MindMapArrowLink.endInclination
import freemind.modes.ExtendedMapFeedbackAdapter.changeArrowsOfArrowLink
import freemind.modes.MindMapArrowLink.startArrow
import freemind.modes.MindMapArrowLink.endArrow
import freemind.modes.ExtendedMapFeedbackAdapter.setArrowLinkColor
import freemind.modes.ExtendedMapFeedbackAdapter.removeReference
import freemind.modes.ExtendedMapFeedbackAdapter.setNodeText
import freemind.modes.MindMapNode.text
import freemind.modes.ExtendedMapFeedbackAdapter.setNodeBackgroundColor
import freemind.modes.MindMapNode.backgroundColor
import freemind.modes.ExtendedMapFeedbackAdapter.setNodeColor
import freemind.modes.MindMapNode.color
import freemind.modes.ExtendedMapFeedbackAdapter.moveNodes
import freemind.modes.ExtendedMapFeedbackAdapter.setFolded
import freemind.modes.ExtendedMapFeedbackAdapter.setLink
import freemind.modes.MindMapNode.link
import freemind.modes.ExtendedMapFeedbackAdapter.addAttribute
import freemind.modes.MindMapNode.attributeTableLength
import freemind.modes.MindMapNode.getAttribute
import freemind.modes.ExtendedMapFeedbackAdapter.setAttribute
import freemind.modes.ExtendedMapFeedbackAdapter.insertAttribute
import freemind.modes.ExtendedMapFeedbackAdapter.removeAttribute
import freemind.modes.ExtendedMapFeedbackAdapter.cut
import freemind.modes.ExtendedMapFeedbackAdapter.setNoteText
import freemind.modes.MindMapNode.noteText
import freemind.modes.StylePatternFactory.createPatternFromNode
import freemind.controller.actions.generated.instance.Pattern.patternNodeColor
import freemind.main.Tools.colorToXml
import freemind.modes.ExtendedMapFeedbackAdapter.applyPattern
import freemind.modes.NodeAdapter.text
import freemind.modes.NodeAdapter.getXmlText
import freemind.modes.NodeAdapter.setXmlText
import freemind.main.XMLElement.parseFromReader
import freemind.main.XMLElement.content
import freemind.main.Tools.isHeadless
import freemind.main.Tools.replaceUtf8AndIllegalXmlChars
import freemind.main.Tools.IntHolder.increase
import freemind.main.HtmlTools.insertHtmlIntoNodes
import freemind.main.Tools.IntHolder.value
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.determineAmountOfNewNodes
import freemind.controller.actions.generated.instance.CalendarMarkings.sizeCalendarMarkingList
import freemind.controller.actions.generated.instance.CalendarMarkings.getCalendarMarking
import freemind.controller.actions.generated.instance.CalendarMarking.startDate
import freemind.controller.actions.generated.instance.CalendarMarking.name
import freemind.controller.actions.generated.instance.WindowConfigurationStorage.height
import freemind.controller.actions.generated.instance.WindowConfigurationStorage.width
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage.leftRatio
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage.topRatio
import freemind.controller.actions.generated.instance.CollaborationGoodbye.userId
import freemind.modes.MapAdapter.createNodeTreeFromXml
import freemind.modes.NodeAdapter.map
import freemind.modes.MapFeedback.invokeHooksRecursively
import freemind.common.OptionalDontShowMeAgainDialog.show
import freemind.common.OptionalDontShowMeAgainDialog.result
import freemind.controller.LastStateStorageManagement.xml
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.restorableName
import freemind.controller.LastStateStorageManagement.changeOrAdd
import freemind.controller.LastStateStorageManagement.getStorage
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.tabIndex
import freemind.controller.LastStateStorageManagement.lastOpenList
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.y
import tests.freemind.FreeMindTestBase
import kotlin.Throws
import freemind.main.HtmlTools
import javax.swing.table.AbstractTableModel
import tests.freemind.findreplace.TestMindMapNode
import accessories.plugins.time.TimeList
import accessories.plugins.time.TimeList.NotesHolder
import accessories.plugins.time.FlatNodeTableFilterModel
import junit.framework.TestCase
import freemind.main.HtmlTools.IndexPair
import accessories.plugins.time.TimeList.IReplaceInputInformation
import tests.freemind.findreplace.FindTextTests.TestReplaceInputInfo
import freemind.modes.MindMapNode
import freemind.modes.ModeController
import freemind.modes.MindMapEdge
import freemind.modes.MindIcon
import freemind.modes.MindMapCloud
import freemind.extensions.PermanentNodeHook
import freemind.extensions.NodeHook
import freemind.modes.MindMapLinkRegistry
import freemind.main.XMLElement
import javax.swing.ImageIcon
import freemind.modes.HistoryInformation
import freemind.modes.MapFeedback
import javax.swing.event.TreeModelListener
import javax.swing.tree.MutableTreeNode
import freemind.modes.MindMap
import kotlin.jvm.JvmStatic
import junit.framework.TestResult
import junit.framework.TestSuite
import tests.freemind.ScriptEditorPanelTest
import tests.freemind.Base64Tests
import tests.freemind.findreplace.FindTextTests
import tests.freemind.HtmlConversionTests
import tests.freemind.TransformTest
import tests.freemind.MarshallerTests
import tests.freemind.SignedScriptTests
import tests.freemind.LastStorageManagementTests
import tests.freemind.ToolsTests
import tests.freemind.ExportTests
import tests.freemind.LayoutTests
import tests.freemind.LastOpenedTests
import tests.freemind.StandaloneMapTests
import tests.freemind.CollaborationTests
import tests.freemind.CalendarMarkingTests
import freemind.main.Tools
import freemind.modes.MapAdapter
import java.net.MalformedURLException
import freemind.main.FreeMindSecurityManager
import java.awt.print.Paper
import tests.freemind.ToolsTests.A
import tests.freemind.ToolsTests.B
import freemind.modes.mindmapmode.MindMapController
import freemind.main.Base64Coding
import freemind.view.mindmapview.IndependantMapViewCreator
import java.net.URISyntaxException
import tests.freemind.FreeMindMainMock
import javax.swing.JDialog
import javax.swing.JPanel
import freemind.view.mindmapview.MapView
import javax.swing.WindowConstants
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import freemind.modes.mindmapmode.MindMapNodeModel
import freemind.modes.mindmapmode.MindMapMapModel
import freemind.modes.mindmapmode.MindMapMode
import freemind.modes.MindMap.MapSourceChangedObserver
import freemind.modes.MapFeedbackAdapter
import freemind.controller.filter.util.SortedListModel
import freemind.controller.filter.util.SortedMapListModel
import freemind.main.Tools.ReaderCreator
import freemind.modes.MindMap.AskUserBeforeUpdateCallback
import freemind.modes.NodeAdapter
import freemind.modes.EdgeAdapter
import freemind.modes.CloudAdapter
import freemind.modes.ArrowLinkAdapter
import freemind.modes.ArrowLinkTarget
import java.lang.InterruptedException
import freemind.main.IFreeMindSplash
import freemind.main.FreeMindSplashModern
import java.awt.geom.AffineTransform
import java.awt.image.ImageObserver
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.image.RenderedImage
import java.awt.image.renderable.RenderableImage
import java.text.AttributedCharacterIterator
import java.awt.font.GlyphVector
import java.awt.font.FontRenderContext
import org.jibx.runtime.IUnmarshallingContext
import freemind.common.XmlBindingTools
import freemind.controller.actions.generated.instance.PluginAction
import freemind.controller.actions.generated.instance.PluginProperty
import accessories.plugins.ExportWithXSLT
import tests.freemind.MindMapControllerMock
import accessories.plugins.ExportToOoWriter
import freemind.controller.LastOpenedList
import freemind.controller.actions.generated.instance.PatternChild
import freemind.controller.actions.generated.instance.Searchresults
import freemind.controller.actions.generated.instance.Place
import freemind.controller.actions.generated.instance.Reversegeocode
import freemind.main.FreeMindMain
import freemind.main.FreeMindStarter
import javax.swing.JFrame
import java.lang.NumberFormatException
import javax.swing.JLayeredPane
import freemind.main.FreeMindMain.VersionInformation
import java.net.URLClassLoader
import javax.swing.JComponent
import javax.swing.JSplitPane
import javax.swing.JScrollPane
import freemind.main.FreeMindMain.StartupDoneListener
import freemind.common.FreeMindTask
import freemind.common.FreeMindTask.ProgressDescription
import java.lang.reflect.InvocationTargetException
import javax.swing.JButton
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import java.lang.Runnable
import plugins.script.SignedScriptHandler
import plugins.script.SignedScriptHandler.ScriptContents
import plugins.collaboration.socket.StandaloneMindMapMaster
import java.net.Socket
import freemind.modes.ExtendedMapFeedback
import tests.freemind.CollaborationTestClient
import freemind.controller.actions.generated.instance.CollaborationTransaction
import freemind.controller.actions.generated.instance.CollaborationWhoAreYou
import freemind.controller.actions.generated.instance.CollaborationGetOffers
import plugins.collaboration.socket.CommunicationBase
import freemind.controller.actions.generated.instance.CollaborationOffers
import freemind.controller.actions.generated.instance.CollaborationMapOffer
import freemind.controller.actions.generated.instance.CollaborationHello
import freemind.controller.actions.generated.instance.CollaborationWelcome
import plugins.collaboration.socket.TerminateableThread
import freemind.controller.actions.generated.instance.CollaborationReceiveLock
import plugins.collaboration.socket.MindMapMaster
import freemind.modes.ExtendedMapFeedbackImpl
import tests.freemind.CollaborationTests.NormalTestClient
import freemind.controller.actions.generated.instance.CollaborationRequireLock
import freemind.controller.actions.generated.instance.EditNoteToNodeAction
import tests.freemind.CollaborationTests.CreateNewMapClient
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap
import freemind.main.Tools.StringReaderCreator
import freemind.main.FreeMind
import java.awt.datatransfer.StringSelection
import freemind.modes.MindMapLink
import freemind.modes.MindMapArrowLink
import freemind.modes.StylePatternFactory
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import java.lang.ClassNotFoundException
import java.awt.datatransfer.UnsupportedFlavorException
import tests.freemind.MindMapMock
import com.lightdev.app.shtm.SHTMLPanel
import freemind.main.HtmlTools.NodeCreator
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor
import tests.freemind.HtmlConversionTests.HtmlTransfer
import freemind.controller.actions.generated.instance.CalendarMarkings
import accessories.plugins.time.CalendarMarkingEvaluator
import java.text.DateFormat
import freemind.controller.actions.generated.instance.CalendarMarking
import freemind.controller.MapModuleManager
import freemind.modes.ModeController.NodeSelectionListener
import freemind.modes.ModeController.NodeLifetimeListener
import javax.swing.JToolBar
import freemind.controller.StructuredMenuHolder
import javax.swing.JPopupMenu
import freemind.view.MapModule
import freemind.extensions.HookFactory
import freemind.modes.FreeMindFileDialog
import plugins.script.ScriptEditorPanel.ScriptModel
import plugins.script.ScriptEditorPanel.ScriptHolder
import plugins.script.ScriptingEngine
import groovy.lang.GroovyShell
import plugins.script.ScriptEditorPanel
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage
import tests.freemind.ScriptEditorPanelTest.TestScriptModel
import freemind.controller.actions.generated.instance.CollaborationGoodbye
import freemind.controller.actions.generated.instance.CollaborationActionBase
import freemind.controller.actions.generated.instance.CollaborationUserInformation
import freemind.controller.actions.generated.instance.CollaborationWrongCredentials
import freemind.controller.actions.generated.instance.CollaborationWrongMap
import freemind.controller.actions.generated.instance.CollaborationUnableToLock
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.TextTranslator
import freemind.common.OptionalDontShowMeAgainDialog.DontShowPropertyHandler
import freemind.controller.LastStateStorageManagement
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import freemind.controller.filter.FilterInfo
import freemind.modes.attributes.Attribute
import java.awt.*
import java.io.*
import java.util.*
import javax.swing.event.EventListenerList
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath

class TestMindMapNode : MindMapNode {
    override var text = ""
    private override val children = Vector<TestMindMapNode>()
    private var mNewParent: TestMindMapNode? = null
    override fun hasFoldedParents(): Boolean {
        if (isRoot) return false
        return if (parentNode!!.isFolded) {
            true
        } else parentNode!!.hasFoldedParents()
    }

    override fun getObjectId(controller: ModeController?): String? {
        return null
    }

    override fun childrenFolded(): ListIterator<*> {
        return children.listIterator()
    }

    override fun childrenUnfolded(): ListIterator<*> {
        return children.listIterator()
    }

    override fun hasChildren(): Boolean {
        return !children.isEmpty()
    }

    override val filterInfo: FilterInfo?
        get() = null

    override fun getChildPosition(childNode: MindMapNode?): Int {
        return children.indexOf(childNode)
    }

    var preferredChild: MindMapNode?
        get() = null
        set(node) {}
    override val nodeLevel: Int
        get() = 0
    override var link: String?
        get() = null
        set(link) {}

    override fun getShortText(controller: ModeController?): String {
        return null
    }

    val edge: MindMapEdge?
        get() = null
    override var color: Color?
        get() = null
        set(color) {}
    override var style: String?
        get() = null
        set(style) {}

    override fun hasStyle(): Boolean {
        return false
    }

    override val parentNode: MindMapNode?
        get() = mNewParent
    override val isBold: Boolean
        get() = false
    override val isItalic: Boolean
        get() = false
    override val isUnderlined: Boolean
        get() = false
    override var font: Font?
        get() = null
        set(font) {}

    override fun getFontSize(): String? {
        return null
    }

    override val fontFamilyName: String?
        get() = null
    override val plainTextContent: String?
        get() = null
    override val path: TreePath?
        get() = null

    override fun isDescendantOf(node: MindMapNode): Boolean {
        return false
    }

    override val isRoot: Boolean
        get() = false
    override var isFolded: Boolean
        get() = false
        set(folded) {}
    override var isLeft: Boolean
        get() = false
        set(isLeft) {}
    val isOnLeftSideOfRoot: Boolean
        get() = false
    override var shiftY: Int
        get() = 0
        set(y) {}

    override fun calcShiftY(): Int {
        return 0
    }

    override var vGap: Int
        get() = 0
        set(i) {}

    fun calcVGap(): Int {
        return 0
    }

    override var hGap: Int
        get() = 0
        set(i) {}

    override fun setFontSize(fontSize: Int) {}
    override val icons: List<MindIcon>?
        get() = null

    override fun addIcon(icon: MindIcon?, position: Int) {}
    override fun removeIcon(position: Int): Int {
        return 0
    }

    override var cloud: MindMapCloud?
        get() = null
        set(cloud) {}
    override var backgroundColor: Color?
        get() = null
        set(color) {}
    override val hooks: List<PermanentNodeHook>?
        get() = null
    override val activatedHooks: Collection<PermanentNodeHook>?
        get() = null

    override fun addHook(hook: PermanentNodeHook?): PermanentNodeHook {
        return null
    }

    override fun invokeHook(hook: NodeHook?) {}
    override fun removeHook(hook: PermanentNodeHook?) {}
    override fun setToolTip(key: String?, tip: String?) {}
    override val toolTip: SortedMap<String, String>?
        get() = null
    override var additionalInfo: String?
        get() = null
        set(info) {}

    override fun shallowCopy(): MindMapNode? {
        return null
    }

    @Throws(IOException::class)
    override fun save(writer: Writer?, registry: MindMapLinkRegistry?,
                      saveHidden: Boolean, saveChildren: Boolean): XMLElement? {
        return null
    }

    override val stateIcons: Map<String, ImageIcon>?
        get() = null

    override fun setStateIcon(key: String?, icon: ImageIcon?) {}
    override var historyInformation: HistoryInformation?
        get() = null
        set(historyInformation) {}
    val isVisible: Boolean
        get() = false

    override fun hasExactlyOneVisibleChild(): Boolean {
        return false
    }

    override val mapFeedback: MapFeedback?
        get() = null

    override fun addTreeModelListener(l: TreeModelListener) {}
    override fun removeTreeModelListener(l: TreeModelListener) {}
    override fun insert(child: MutableTreeNode, index: Int) {
        children.insertElementAt(child as TestMindMapNode, index)
    }

    override fun remove(index: Int) {
        children.removeAt(index)
    }

    override fun remove(node: MutableTreeNode) {
        children.remove(node)
    }

    override fun setUserObject(`object`: Any) {}
    override fun removeFromParent() {}
    override fun setParent(newParent: MutableTreeNode) {
        mNewParent = newParent as TestMindMapNode
    }

    override fun getChildAt(childIndex: Int): TreeNode {
        return children[childIndex]
    }

    override fun getChildCount(): Int {
        return children.size
    }

    override fun getParent(): TreeNode {
        return mNewParent!!
    }

    override fun getIndex(node: TreeNode): Int {
        return 0
    }

    override fun getAllowsChildren(): Boolean {
        return false
    }

    override fun isLeaf(): Boolean {
        return false
    }

    override fun children(): Enumeration<out TreeNode>? {
        return children.elements()
    }

    override var xmlText: String?
        get() = null
        set(structuredText) {}

    // TODO Auto-generated method stub
    // TODO Auto-generated method stub
    override var xmlNoteText: String?
        get() =// TODO Auto-generated method stub
            null
        set(structuredNoteText) {
            // TODO Auto-generated method stub
        }

    override fun getChildren(): List<*> {
        return children
    }

    override var noteText: String?
        get() = null
        set(noteText) {}

    override fun getAttribute(pPosition: Int): Attribute {
        return null
    }

    override val attributeKeyList: List<String>?
        get() = null
    override val attributes: List<Attribute>?
        get() = null

    override fun getAttributePosition(key: String?): Int {
        return 0
    }

    override fun setAttribute(pPosition: Int, pAttribute: Attribute?) {}
    override val attributeTableLength: Int
        get() = 0

    // TODO Auto-generated method stub
    override val listeners: EventListenerList?
        get() =// TODO Auto-generated method stub
            null

    // TODO Auto-generated method stub
    override val isNewChildLeft: Boolean
        get() =// TODO Auto-generated method stub
            false

    fun createAttributeTableModel() {
        // TODO Auto-generated method stub
    }

    override fun getAttribute(key: String?): String? {
        // TODO Auto-generated method stub
        return null
    }

    override val isWriteable: Boolean
        get() = true

    override fun isDescendantOfOrEqual(pParentNode: MindMapNode): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#removeAllHooks()
	 */
    override fun removeAllHooks() {}

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#sortedChildrenUnfolded()
	 */
    override fun sortedChildrenUnfolded(): ListIterator<*>? {
        return null
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#hasVisibleChilds()
	 */
    override fun hasVisibleChilds(): Boolean {
        return false
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#addAttribute(freemind.modes.attributes.Attribute)
	 */
    override fun addAttribute(pAttribute: Attribute?): Int {
        // TODO Auto-generated method stub
        return 0
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#removeAttribute(int)
	 */
    override fun removeAttribute(pPosition: Int) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#insertAttribute(int, freemind.modes.attributes.Attribute)
	 */
    override fun insertAttribute(pPosition: Int, pAttribute: Attribute?) {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#getMap()
	 */
    override val map: MindMap?
        get() = null
    override val isStrikethrough: Boolean
        get() = false
    override val bareStyle: String?
        get() = null
}