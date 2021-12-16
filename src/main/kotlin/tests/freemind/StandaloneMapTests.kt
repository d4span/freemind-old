/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
package tests.freemind

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
import java.util.SortedMap
import freemind.modes.MindMapLinkRegistry
import freemind.main.XMLElement
import javax.swing.ImageIcon
import freemind.modes.HistoryInformation
import freemind.modes.MapFeedback
import javax.swing.event.TreeModelListener
import javax.swing.tree.MutableTreeNode
import java.util.Enumeration
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
import java.util.Properties
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
import java.util.ResourceBundle
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
import java.util.Calendar
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
import freemind.modes.attributes.Attribute
import java.awt.*
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException

/**
 * @author foltin
 * @date 16.03.2014
 */
class StandaloneMapTests : FreeMindTestBase() {
    @Throws(Exception::class)
    fun testStandaloneCreation() {
        val mapFeedback = ExtendedMapFeedbackImpl()
        val mMap = MindMapMapModel(mapFeedback)
        mapFeedback.map = mMap
        val readerCreator = StringReaderCreator(INITIAL_MAP)
        val root = mMap.loadTree(readerCreator,
                MapAdapter.sDontAskInstance)
        mMap.root = root
        var xmlResult = getMapContents(mMap)
        xmlResult = xmlResult.replace("CREATED=\"[0-9]*\"".toRegex(), "CREATED=\"\"")
        xmlResult = xmlResult
                .replace("MODIFIED=\"[0-9]*\"".toRegex(), "MODIFIED=\"\"")
        val expected = """<map version="${FreeMind.XML_VERSION}">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="" MODIFIED="" TEXT="ROOT">
<node CREATED="" MODIFIED="" POSITION="right" TEXT="FormatMe">
<node CREATED="" MODIFIED="" TEXT="Child1"/>
<node CREATED="" MODIFIED="" TEXT="Child2"/>
<node CREATED="" MODIFIED="" TEXT="Child3"/>
</node>
</node>
</map>
"""
        assertEquals(expected, xmlResult)
    }

    @Throws(IOException::class)
    protected fun getMapContents(mMap: MindMapMapModel): String {
        val stringWriter = StringWriter()
        mMap.getFilteredXml(stringWriter)
        return stringWriter.buffer.toString()
    }

    @Throws(Exception::class)
    fun testXmlChangeWithoutModeController() {
        val mapFeedback = ExtendedMapFeedbackImpl()
        val mMap = MindMapMapModel(mapFeedback)
        mapFeedback.map = mMap
        val readerCreator = StringReaderCreator(INITIAL_MAP)
        val root = mMap.loadTree(readerCreator,
                MapAdapter.sDontAskInstance)
        mMap.root = root
        val firstChild = root!!.getChildAt(0) as MindMapNode
        val subChild1 = firstChild.getChildAt(0) as MindMapNode
        val subChild2 = firstChild.getChildAt(1) as MindMapNode
        var subChild3: MindMapNode? = firstChild.getChildAt(2) as MindMapNode
        mapFeedback.setStrikethrough(root, true)
        assertEquals(true, root.isStrikethrough)
        mapFeedback.setBold(root, true)
        assertEquals(true, root.isBold)
        assertEquals(true, root.isStrikethrough)
        mapFeedback.setItalic(root, true)
        assertEquals(true, root.isItalic)
        assertEquals(true, root.isStrikethrough)
        mapFeedback.setFontSize(root, "24")
        assertEquals("24", root.getFontSize())
        assertEquals(true, root.isStrikethrough)
        mapFeedback.setStrikethrough(root, false)
        assertEquals(false, root.isStrikethrough)
        val amount = root.childCount
        var newNode = mapFeedback.addNewNode(root, 0,
                true)
        assertEquals(amount + 1, root.childCount)
        mapFeedback.deleteNode(newNode)
        newNode = null
        assertEquals(amount, root.childCount)
        try {
            mapFeedback.deleteNode(root)
            assertTrue("Must throw.", false)
        } catch (e: IllegalArgumentException) {
        }
        mapFeedback.paste(StringSelection("bla"), root, false,
                true)
        assertEquals(amount + 1, root.childCount)
        assertEquals(0, root.icons.size)
        val icon = MindIcon.factory("attach")
        mapFeedback.addIcon(root, icon)
        assertEquals(1, root.icons.size)
        mapFeedback.removeLastIcon(root)
        assertEquals(0, root.icons.size)
        mapFeedback.removeLastIcon(root)
        mapFeedback.addIcon(root, icon)
        mapFeedback.addIcon(root, icon)
        mapFeedback.addIcon(root, icon)
        mapFeedback.addIcon(root, icon)
        mapFeedback.addIcon(root, icon)
        assertEquals(5, root.icons.size)
        mapFeedback.removeAllIcons(root)
        assertEquals(0, root.icons.size)
        // cloud
        mapFeedback.setCloud(firstChild, true)
        assertNotNull(firstChild.cloud)
        mapFeedback.setCloudColor(firstChild, Color.CYAN)
        assertEquals(Color.CYAN, firstChild.cloud!!.color)
        mapFeedback.setCloud(firstChild, false)
        assertNull(firstChild.cloud)
        // edges
        try {
            mapFeedback.setEdgeStyle(firstChild, "bluber")
            assertTrue("Must throw.", false)
        } catch (e: Exception) {
        }
        mapFeedback.setEdgeStyle(firstChild,
                EdgeAdapter.EDGESTYLE_SHARP_BEZIER)
        assertTrue(firstChild.getEdge().hasStyle())
        assertEquals(EdgeAdapter.EDGESTYLE_SHARP_BEZIER, firstChild.getEdge()
                .getStyle())
        mapFeedback.setEdgeStyle(firstChild, null)
        assertFalse(firstChild.getEdge().hasStyle())
        assertEquals(EdgeAdapter.EDGESTYLE_BEZIER, firstChild.getEdge()
                .getStyle())
        mapFeedback.setEdgeWidth(firstChild, 8)
        assertEquals(8, firstChild.getEdge().getWidth())
        mapFeedback.setEdgeWidth(firstChild,
                EdgeAdapter.WIDTH_THIN)
        assertEquals(EdgeAdapter.WIDTH_THIN, firstChild.getEdge().getWidth())
        mapFeedback.setEdgeColor(firstChild, Color.GREEN)
        assertEquals(Color.GREEN, firstChild.getEdge().getColor())
        var ge: GraphicsEnvironment? = null
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val fontNames = ge.availableFontFamilyNames
        if (fontNames.size > 0) {
            val fontFamilyValue = fontNames[0]
            mapFeedback.setFontFamily(firstChild,
                    fontFamilyValue)
            assertEquals(fontFamilyValue, firstChild.fontFamilyName)
        }
        val fontSizeValue = "32"
        mapFeedback.setFontSize(firstChild, fontSizeValue)
        assertEquals(fontSizeValue, firstChild.getFontSize())
        mapFeedback.moveNodePosition(firstChild, 20, 30, 17)
        assertEquals(20, root.vGap)
        assertEquals(30, firstChild.hGap)
        assertEquals(17, firstChild.shiftY)
        mapFeedback.setNodeStyle(firstChild, MindMapNode.STYLE_FORK)
        assertEquals(MindMapNodeModel.STYLE_FORK, firstChild.style)
        assertTrue(firstChild.hasStyle())
        mapFeedback.setNodeStyle(firstChild, null)
        assertFalse(firstChild.hasStyle())
        try {
            mapFeedback.setNodeStyle(firstChild, "bla")
            assertTrue("Must throw.", false)
        } catch (e: Exception) {
        }
        // underline not implemented
//		mapFeedback.setUnderlined(firstChild, true);
//		assertTrue(firstChild.isUnderlined());
        // arrow links
        mapFeedback.addLink(subChild1, subChild2)
        var mapLinks = mapFeedback.map.linkRegistry!!.getAllLinksFromMe(subChild1)
        assertEquals(1, mapLinks.size)
        val mapLink = mapLinks.firstElement() as MindMapArrowLink?
        assertEquals(subChild2, mapLink!!.target)
        val startPoint = Point(40, 50)
        val endPoint = Point(-10, -20)
        mapFeedback.setArrowLinkEndPoints(mapLink, startPoint, endPoint)
        assertEquals(startPoint, mapLink.startInclination)
        assertEquals(endPoint, mapLink.endInclination)
        mapFeedback.changeArrowsOfArrowLink(mapLink, true, false)
        assertEquals(MindMapArrowLink.ARROW_DEFAULT, mapLink.startArrow)
        assertEquals(MindMapArrowLink.ARROW_NONE, mapLink.endArrow)
        mapFeedback.setArrowLinkColor(mapLink, Color.RED)
        assertEquals(Color.RED, mapLink.color)
        mapFeedback.removeReference(mapLink)
        mapLinks = mapFeedback.map.linkRegistry!!.getAllLinksFromMe(subChild1)
        assertEquals(0, mapLinks.size)
        val newText = "blabla"
        mapFeedback.setNodeText(firstChild, newText)
        assertEquals(newText, firstChild.text)
        val darkGray = Color.DARK_GRAY
        mapFeedback.setNodeBackgroundColor(firstChild, darkGray)
        assertEquals(darkGray, firstChild.backgroundColor)
        mapFeedback.setNodeColor(firstChild, darkGray)
        assertEquals(darkGray, firstChild.color)
        //		// hooks: currently disabled, as too dependent from MindMapController
//		mapFeedback.addHook(firstChild,
//				Tools.getVectorWithSingleElement(firstChild),
//				"accessories/plugins/BlinkingNodeHook.properties", null);
//		int timeout = 10;
//		Color nodeColor = firstChild.getColor();
//		boolean found = false;
//		while(timeout -- > 0) {
//			if(firstChild.getColor() != nodeColor) {
//				found = true;
//				break;
//			}
//			Thread.sleep(1000);
//		}
//		assertTrue(found);
//		mapFeedback.addHook(firstChild,
//				Tools.getVectorWithSingleElement(firstChild),
//				"accessories/plugins/BlinkingNodeHook.properties", null);
        assertEquals(0, firstChild.getIndex(subChild1))
        mapFeedback.moveNodes(subChild1, getVectorWithSingleElement<MindMapNode?>(subChild1), 1)
        assertEquals(1, firstChild.getIndex(subChild1))
        mapFeedback.moveNodes(subChild1, getVectorWithSingleElement<MindMapNode?>(subChild1), -1)
        assertEquals(0, firstChild.getIndex(subChild1))
        mapFeedback.moveNodes(subChild1, getVectorWithSingleElement<MindMapNode?>(subChild1), -1)
        assertEquals(2, firstChild.getIndex(subChild1))
        mapFeedback.setFolded(firstChild, true)
        assertTrue(firstChild.isFolded)
        val link = "http://freemind.sf.net/"
        mapFeedback.setLink(subChild3, link)
        assertEquals(link, subChild3!!.link)
        // attributes
        mapFeedback.addAttribute(root, Attribute("name", "value"))
        assertEquals(1, root.attributeTableLength)
        assertEquals("value", root.getAttribute("name"))
        mapFeedback.setAttribute(root, 0, Attribute("oname", "ovalue"))
        assertEquals(1, root.attributeTableLength)
        assertEquals("ovalue", root.getAttribute("oname"))
        assertEquals(null, root.getAttribute("name"))
        mapFeedback.insertAttribute(root, 0, Attribute("0name", "0"))
        assertEquals(2, root.attributeTableLength)
        assertEquals("0", root.getAttribute("0name"))
        mapFeedback.removeAttribute(root, 1)
        assertEquals(1, root.attributeTableLength)
        assertEquals("0", root.getAttribute("0name"))
        assertEquals(null, root.getAttribute("oname"))
        // cut
        assertEquals(3, firstChild.childCount)
        mapFeedback.cut(getVectorWithSingleElement<MindMapNode?>(subChild3))
        assertEquals(2, firstChild.childCount)
        subChild3 = null
        // note
        val htmlText = "<html><body>blaNOTE</body></html>"
        mapFeedback.setNoteText(subChild2, htmlText)
        assertEquals(htmlText, subChild2.noteText)
        // patterns
        mapFeedback.setNodeColor(subChild1, Color.MAGENTA)
        val p = createPatternFromNode(subChild1)
        assertNotNull(p.patternNodeColor)
        assertEquals(colorToXml(Color.MAGENTA), p.patternNodeColor!!.value)
        mapFeedback.applyPattern(subChild2, p)
        assertEquals(Color.MAGENTA, subChild2.color)
        val xmlResult = getMapContents(mMap)
        println(xmlResult)
    }

    companion object {
        /**
         *
         */
        private const val INITIAL_MAP = ("<map>" + "<node TEXT='ROOT'>"
                + "<node TEXT='FormatMe'>"
                + "<node TEXT='Child1'/>"
                + "<node TEXT='Child2'/>"
                + "<node TEXT='Child3'/>"
                + "</node>"
                + "</node>" + "</map>")
    }
}