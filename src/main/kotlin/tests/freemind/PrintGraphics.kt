/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
import java.io.IOException
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
import java.io.FileNotFoundException
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
import java.io.DataInputStream
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
import java.io.PrintWriter
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
import java.io.PrintStream
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
import java.awt.*

/**
 * @author foltin
 * @date 05.09.2011
 */
class PrintGraphics(pG: Graphics) : Graphics2D() {
    private val mG: Graphics2D

    /**
     * @param pG
     */
    init {
        mG = pG as Graphics2D
        // TODO Auto-generated constructor stub
    }

    override fun create(): Graphics {
        return PrintGraphics(mG.create())
    }

    override fun create(pX: Int, pY: Int, pWidth: Int, pHeight: Int): Graphics {
        return PrintGraphics(mG.create(pX, pY, pWidth, pHeight))
    }

    override fun drawRoundRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int,
                               pArcWidth: Int, pArcHeight: Int) {
        logMe("drawRoundRect", pX, pY, pWidth, pHeight)
        mG.drawRoundRect(pX, pY, pWidth, pHeight, pArcWidth, pArcHeight)
    }

    protected fun logMe(title: String, pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        println(title + ", " + pX + ", " + pY + ", " + pWidth + ", "
                + pHeight)
    }

    override fun drawOval(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        logMe("drawOval", pX, pY, pWidth, pHeight)
        mG.drawOval(pX, pY, pWidth, pHeight)
    }

    override fun hashCode(): Int {
        return mG.hashCode()
    }

    override fun equals(pObj: Any?): Boolean {
        return mG == pObj
    }

    override fun getColor(): Color {
        return mG.color
    }

    override fun setColor(pC: Color) {
        println("Color: $pC")
        mG.color = pC
    }

    override fun setPaintMode() {
        mG.setPaintMode()
    }

    override fun setXORMode(pC1: Color) {
        mG.setXORMode(pC1)
    }

    override fun getFont(): Font {
        return mG.font
    }

    override fun setFont(pFont: Font) {
        mG.font = pFont
    }

    override fun getFontMetrics(): FontMetrics {
        return mG.fontMetrics
    }

    override fun getFontMetrics(pF: Font): FontMetrics {
        return mG.getFontMetrics(pF)
    }

    override fun getClipBounds(): Rectangle {
        return mG.clipBounds
    }

    override fun clipRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        mG.clipRect(pX, pY, pWidth, pHeight)
    }

    override fun setClip(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        mG.setClip(pX, pY, pWidth, pHeight)
    }

    override fun getClip(): Shape {
        return mG.clip
    }

    override fun setClip(pClip: Shape) {
        mG.clip = pClip
    }

    override fun copyArea(pX: Int, pY: Int, pWidth: Int, pHeight: Int, pDx: Int,
                          pDy: Int) {
        mG.copyArea(pX, pY, pWidth, pHeight, pDx, pDy)
    }

    override fun drawLine(pX1: Int, pY1: Int, pX2: Int, pY2: Int) {
        mG.drawLine(pX1, pY1, pX2, pY2)
    }

    override fun fillRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        mG.fillRect(pX, pY, pWidth, pHeight)
    }

    override fun drawRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        mG.drawRect(pX, pY, pWidth, pHeight)
    }

    override fun draw3DRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int,
                            pRaised: Boolean) {
        mG.draw3DRect(pX, pY, pWidth, pHeight, pRaised)
    }

    override fun clearRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        mG.clearRect(pX, pY, pWidth, pHeight)
    }

    override fun fill3DRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int,
                            pRaised: Boolean) {
        mG.fill3DRect(pX, pY, pWidth, pHeight, pRaised)
    }

    override fun fillRoundRect(pX: Int, pY: Int, pWidth: Int, pHeight: Int,
                               pArcWidth: Int, pArcHeight: Int) {
        mG.fillRoundRect(pX, pY, pWidth, pHeight, pArcWidth, pArcHeight)
    }

    override fun draw(pS: Shape) {
        mG.draw(pS)
    }

    override fun drawImage(pImg: Image, pXform: AffineTransform,
                           pObs: ImageObserver): Boolean {
        return mG.drawImage(pImg, pXform, pObs)
    }

    override fun drawImage(pImg: BufferedImage, pOp: BufferedImageOp, pX: Int,
                           pY: Int) {
        mG.drawImage(pImg, pOp, pX, pY)
    }

    override fun drawRenderedImage(pImg: RenderedImage, pXform: AffineTransform) {
        mG.drawRenderedImage(pImg, pXform)
    }

    override fun fillOval(pX: Int, pY: Int, pWidth: Int, pHeight: Int) {
        mG.fillOval(pX, pY, pWidth, pHeight)
    }

    override fun drawArc(pX: Int, pY: Int, pWidth: Int, pHeight: Int,
                         pStartAngle: Int, pArcAngle: Int) {
        mG.drawArc(pX, pY, pWidth, pHeight, pStartAngle, pArcAngle)
    }

    override fun drawRenderableImage(pImg: RenderableImage, pXform: AffineTransform) {
        mG.drawRenderableImage(pImg, pXform)
    }

    override fun drawString(pStr: String, pX: Int, pY: Int) {
        mG.drawString(pStr, pX, pY)
    }

    override fun fillArc(pX: Int, pY: Int, pWidth: Int, pHeight: Int,
                         pStartAngle: Int, pArcAngle: Int) {
        mG.fillArc(pX, pY, pWidth, pHeight, pStartAngle, pArcAngle)
    }

    override fun drawString(pStr: String, pX: Float, pY: Float) {
        mG.drawString(pStr, pX, pY)
    }

    override fun drawPolyline(pXPoints: IntArray, pYPoints: IntArray, pNPoints: Int) {
        mG.drawPolyline(pXPoints, pYPoints, pNPoints)
    }

    override fun drawString(pIterator: AttributedCharacterIterator, pX: Int, pY: Int) {
        mG.drawString(pIterator, pX, pY)
    }

    override fun drawPolygon(pXPoints: IntArray, pYPoints: IntArray, pNPoints: Int) {
        mG.drawPolygon(pXPoints, pYPoints, pNPoints)
    }

    override fun drawString(pIterator: AttributedCharacterIterator, pX: Float,
                            pY: Float) {
        mG.drawString(pIterator, pX, pY)
    }

    override fun drawPolygon(pP: Polygon) {
        mG.drawPolygon(pP)
    }

    override fun fillPolygon(pXPoints: IntArray, pYPoints: IntArray, pNPoints: Int) {
        mG.fillPolygon(pXPoints, pYPoints, pNPoints)
    }

    override fun drawGlyphVector(pG: GlyphVector, pX: Float, pY: Float) {
        mG.drawGlyphVector(pG, pX, pY)
    }

    override fun fillPolygon(pP: Polygon) {
        mG.fillPolygon(pP)
    }

    override fun fill(pS: Shape) {
        mG.fill(pS)
    }

    override fun hit(pRect: Rectangle, pS: Shape, pOnStroke: Boolean): Boolean {
        return mG.hit(pRect, pS, pOnStroke)
    }

    override fun drawChars(pData: CharArray, pOffset: Int, pLength: Int, pX: Int, pY: Int) {
        mG.drawChars(pData, pOffset, pLength, pX, pY)
    }

    override fun getDeviceConfiguration(): GraphicsConfiguration {
        return mG.deviceConfiguration
    }

    override fun setComposite(pComp: Composite) {
        mG.composite = pComp
    }

    override fun drawBytes(pData: ByteArray, pOffset: Int, pLength: Int, pX: Int, pY: Int) {
        mG.drawBytes(pData, pOffset, pLength, pX, pY)
    }

    override fun setPaint(pPaint: Paint) {
        mG.paint = pPaint
    }

    override fun drawImage(pImg: Image, pX: Int, pY: Int, pObserver: ImageObserver): Boolean {
        return mG.drawImage(pImg, pX, pY, pObserver)
    }

    override fun setStroke(pS: Stroke) {
        mG.stroke = pS
    }

    override fun setRenderingHint(pHintKey: RenderingHints.Key, pHintValue: Any) {
        mG.setRenderingHint(pHintKey, pHintValue)
    }

    override fun getRenderingHint(pHintKey: RenderingHints.Key): Any {
        return mG.getRenderingHint(pHintKey)
    }

    override fun drawImage(pImg: Image, pX: Int, pY: Int, pWidth: Int,
                           pHeight: Int, pObserver: ImageObserver): Boolean {
        return mG.drawImage(pImg, pX, pY, pWidth, pHeight, pObserver)
    }

    override fun setRenderingHints(pHints: Map<*, *>?) {
        mG.setRenderingHints(pHints)
    }

    override fun addRenderingHints(pHints: Map<*, *>?) {
        mG.addRenderingHints(pHints)
    }

    override fun getRenderingHints(): RenderingHints {
        return mG.renderingHints
    }

    override fun drawImage(pImg: Image, pX: Int, pY: Int, pBgcolor: Color,
                           pObserver: ImageObserver): Boolean {
        return mG.drawImage(pImg, pX, pY, pBgcolor, pObserver)
    }

    override fun translate(pX: Int, pY: Int) {
        mG.translate(pX, pY)
    }

    override fun translate(pTx: Double, pTy: Double) {
        mG.translate(pTx, pTy)
    }

    override fun rotate(pTheta: Double) {
        mG.rotate(pTheta)
    }

    override fun drawImage(pImg: Image, pX: Int, pY: Int, pWidth: Int,
                           pHeight: Int, pBgcolor: Color, pObserver: ImageObserver): Boolean {
        return mG.drawImage(pImg, pX, pY, pWidth, pHeight, pBgcolor, pObserver)
    }

    override fun rotate(pTheta: Double, pX: Double, pY: Double) {
        mG.rotate(pTheta, pX, pY)
    }

    override fun scale(pSx: Double, pSy: Double) {
        mG.scale(pSx, pSy)
    }

    override fun shear(pShx: Double, pShy: Double) {
        mG.shear(pShx, pShy)
    }

    override fun drawImage(pImg: Image, pDx1: Int, pDy1: Int, pDx2: Int,
                           pDy2: Int, pSx1: Int, pSy1: Int, pSx2: Int, pSy2: Int,
                           pObserver: ImageObserver): Boolean {
        return mG.drawImage(pImg, pDx1, pDy1, pDx2, pDy2, pSx1, pSy1, pSx2,
                pSy2, pObserver)
    }

    override fun transform(Tx: AffineTransform) {
        mG.transform(Tx)
    }

    override fun setTransform(Tx: AffineTransform) {
        mG.transform = Tx
    }

    override fun getTransform(): AffineTransform {
        return mG.transform
    }

    override fun drawImage(pImg: Image, pDx1: Int, pDy1: Int, pDx2: Int,
                           pDy2: Int, pSx1: Int, pSy1: Int, pSx2: Int, pSy2: Int, pBgcolor: Color,
                           pObserver: ImageObserver): Boolean {
        return mG.drawImage(pImg, pDx1, pDy1, pDx2, pDy2, pSx1, pSy1, pSx2,
                pSy2, pBgcolor, pObserver)
    }

    override fun getPaint(): Paint {
        return mG.paint
    }

    override fun getComposite(): Composite {
        return mG.composite
    }

    override fun setBackground(pColor: Color) {
        mG.background = pColor
    }

    override fun getBackground(): Color {
        return mG.background
    }

    override fun getStroke(): Stroke {
        return mG.stroke
    }

    override fun clip(pS: Shape) {
        mG.clip(pS)
    }

    override fun getFontRenderContext(): FontRenderContext {
        return mG.fontRenderContext
    }

    override fun dispose() {
        mG.dispose()
    }

    override fun finalize() {
        mG.finalize()
    }

    override fun toString(): String {
        return mG.toString()
    }

    override fun getClipRect(): Rectangle {
        return mG.clipRect
    }

    override fun hitClip(pX: Int, pY: Int, pWidth: Int, pHeight: Int): Boolean {
        return mG.hitClip(pX, pY, pWidth, pHeight)
    }

    override fun getClipBounds(pR: Rectangle): Rectangle {
        return mG.getClipBounds(pR)
    }
}