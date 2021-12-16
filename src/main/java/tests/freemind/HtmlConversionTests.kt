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
/*$Id: HtmlConversionTests.java,v 1.1.2.16 2010/12/04 21:07:23 christianfoltin Exp $*/
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
import java.awt.Color
import freemind.modes.MindIcon
import freemind.modes.MindMapCloud
import freemind.extensions.PermanentNodeHook
import freemind.extensions.NodeHook
import java.util.SortedMap
import freemind.modes.MindMapLinkRegistry
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
import freemind.modes.MapAdapter
import java.net.MalformedURLException
import java.awt.print.Paper
import tests.freemind.ToolsTests.A
import tests.freemind.ToolsTests.B
import java.util.Properties
import freemind.modes.mindmapmode.MindMapController
import freemind.view.mindmapview.IndependantMapViewCreator
import java.net.URISyntaxException
import tests.freemind.FreeMindMainMock
import javax.swing.JDialog
import java.awt.Rectangle
import javax.swing.JPanel
import java.awt.Graphics
import java.awt.BorderLayout
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
import java.awt.Graphics2D
import java.awt.Shape
import java.awt.geom.AffineTransform
import java.awt.image.ImageObserver
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.image.RenderedImage
import java.awt.image.renderable.RenderableImage
import java.text.AttributedCharacterIterator
import java.awt.Polygon
import java.awt.font.GlyphVector
import java.awt.Paint
import java.awt.Stroke
import java.awt.RenderingHints
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
import java.awt.datatransfer.StringSelection
import java.awt.GraphicsEnvironment
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
import java.awt.Dimension
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
import freemind.main.*
import java.io.*
import java.lang.Exception

/**
 * @author foltin
 */
class HtmlConversionTests : FreeMindTestBase() {
    /**
     * @author foltin
     * @date 22.12.2014
     */
    private inner class HtmlTransfer(private val mHtmlData: String) : Transferable {
        private var mFlavor: DataFlavor? = null

        /**
         *
         */
        init {
            try {
                mFlavor = DataFlavor("text/html; class=java.lang.String")
            } catch (e: ClassNotFoundException) {
                Resources.getInstance().logException(e)
            }
        }

        override fun getTransferDataFlavors(): Array<DataFlavor> {
            return arrayOf(mFlavor)
        }

        override fun isDataFlavorSupported(pFlavor: DataFlavor): Boolean {
            return pFlavor.equals(mFlavor)
        }

        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun getTransferData(pFlavor: DataFlavor): Any {
            return mHtmlData
        }
    }

    @Throws(Exception::class)
    fun testSetHtml() {
        val node = MindMapNodeModel(MindMapMock("</map>"))
        node.text = "test"
        // wiped out: <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html
        // PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"
        // \"DTD/xhtml1-transitional.dtd\">\n
        assertEquals("no conversion as test is not html.", null,
                node.getXmlText()) /*
									 * former :
									 * "<html>\n  <head>\n    \n  </head>\n  <body>\n    test\n  </body>\n</html>\n"
									 */
        node.setXmlText("test")
        assertEquals("proper conversion", "test", node.text)
        node.text = "<html><br>"
        assertEquals(
                "proper html conversion",
                "<html>\n  <head>\n    \n  </head>\n  <body>\n    <br />\n  </body>\n</html>\n",
                node.getXmlText())
        // must remove the '/' in <br/>.
        node.setXmlText("<html><br/></html>")
        assertEquals("proper html conversion", "<html><br></html>",
                node.text)
        node.setXmlText("<html><br /></html>")
        assertEquals("proper html conversion", "<html><br ></html>",
                node.text)
    }

    @Throws(Exception::class)
    fun testEndContentMatcher() {
        matchingTest("</" + XMLElement.XML_NODE_XHTML_CONTENT_TAG + ">")
        matchingTest("</ " + XMLElement.XML_NODE_XHTML_CONTENT_TAG + ">")
        matchingTest("</ " + XMLElement.XML_NODE_XHTML_CONTENT_TAG + " >")
        matchingTest("""< /
${XMLElement.XML_NODE_XHTML_CONTENT_TAG} >""")
    }

    /**
     */
    private fun matchingTest(string: String) {
        assertTrue(string
                .matches(XMLElement.XML_NODE_XHTML_CONTENT_END_TAG_REGEXP))
    }

    @Throws(Exception::class)
    fun testNanoXmlContent() {
        val element = XMLElement()
        element.parseFromReader(StringReader("<"
                + XMLElement.XML_NODE_XHTML_CONTENT_TAG
                + "><body>a<b>cd</b>e</body></"
                + XMLElement.XML_NODE_XHTML_CONTENT_TAG + ">"))
        assertEquals("end " + XMLElement.XML_NODE_XHTML_CONTENT_TAG
                + " tag removed", "<body>a<b>cd</b>e</body>",
                element.content)
    }

    @Throws(Exception::class)
    fun testXHtmlToHtmlConversion() {
        assertEquals("br removal", "<br >",
                HtmlTools.getInstance().toHtml("<br />"))
        assertEquals("br removal, not more.", "<brmore/>", HtmlTools
                .getInstance().toHtml("<brmore/>"))
    }

    @Throws(Exception::class)
    fun testWellFormedXml() {
        assertEquals(true, HtmlTools.getInstance().isWellformedXml("<a></a>"))
        // assertEquals(true,
        // HtmlTools.getInstance().isWellformedXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"DTD/xhtml1-transitional.dtd\">\n<a></a>"));
        assertEquals(
                true,
                HtmlTools.getInstance().isWellformedXml(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a></a>"))
        assertEquals(false,
                HtmlTools.getInstance().isWellformedXml("<a><a></a>"))
    }

    @Throws(Exception::class)
    fun testBr() {
        val input = """<html>
  <head>
    
  </head>
  <body>
    <p>
      asdfasdf<br />asdfasdfdasf
    </p>
    <p>
      asdasdfas
    </p>
  </body>
</html>"""
        val result: String = HtmlTools.getInstance().toHtml(input)
        assertFalse(" no > occurs  in $result", result.matches("^.*&gt;.*$"))
    }

    /**
     * I suspected, that the toXhtml method inserts some spaces into the output,
     * but it doesn't seem to be the case.
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun testSpaceHandling() {
        val input = getInputStringWithManySpaces(HtmlTools.SP)
        assertEquals(input, HtmlTools.getInstance().toXhtml(input))
    }
    // public void testSpaceHandlingInShtml() throws Exception {
    // String input = getInputStringWithManySpaces(" ");
    // SHTMLPanel panel = SHTMLPanel.createSHTMLPanel();
    // panel.setCurrentDocumentContent(input);
    // assertEquals(input, panel.getDocumentText());
    // panel.setVisible(false);
    // }
    /**
     * Set the panel to a text, read this text from the panel and set it again.
     * Then, setting and getting this text to the panel must give the same.
     */
    @Throws(Exception::class)
    fun testSpaceHandlingInShtmlIdempotency() {
        if (isHeadless) {
            return
        }
        var input = getInputStringWithManySpaces(" ")
        val panel = SHTMLPanel.createSHTMLPanel()
        panel.setCurrentDocumentContent(input)
        // set the value of the panel itself again.
        input = panel.documentText
        panel.setCurrentDocumentContent(input)
        assertEquals(
                "Setting the input to its output should cause the same output.",
                input, panel.documentText)
        panel.isVisible = false
    }

    @Throws(Exception::class)
    fun testSpaceRemovalInShtml() {
        if (isHeadless) {
            return
        }
        var input = getInputStringWithManySpaces(HtmlTools.SP)
        val panel = SHTMLPanel.createSHTMLPanel()
        panel.setCurrentDocumentContent(input)
        // set the value of the panel itself again (twice)
        input = panel.documentText
        panel.setCurrentDocumentContent(input)
        input = panel.documentText
        panel.setCurrentDocumentContent(input)
        assertEquals(
                "Setting the input to its output should cause the same output.",
                input, panel.documentText)
        panel.isVisible = false
    }

    private fun getInputStringWithManySpaces(pSpaceString: String): String {
        val body = getHtmlBody(pSpaceString)
        return """<html>
  <head>
    
  </head>
  <body>$body</body>
</html>
"""
    }

    private fun getHtmlBody(pSpaceString: String): String {
        return """
    <p>
      Using${pSpaceString}Filters${pSpaceString}the${pSpaceString}current${pSpaceString}mindmap${pSpaceString}can${pSpaceString}be${pSpaceString}reduced${pSpaceString}to${pSpaceString}nodes${pSpaceString}satisfying${pSpaceString}certain${pSpaceString}criteria.${pSpaceString}For${pSpaceString}example,${pSpaceString}if${pSpaceString}you${pSpaceString}only${pSpaceString}want${pSpaceString}to${pSpaceString}see${pSpaceString}every${pSpaceString}node${pSpaceString}containing$pSpaceString&quot;TODO&quot;,${pSpaceString}then${pSpaceString}you${pSpaceString}have${pSpaceString}to${pSpaceString}press${pSpaceString}on${pSpaceString}the${pSpaceString}filter${pSpaceString}symbol$pSpaceString(the${pSpaceString}funnel${pSpaceString}beside${pSpaceString}the${pSpaceString}zoom${pSpaceString}box),${pSpaceString}the${pSpaceString}filter${pSpaceString}toolbar${pSpaceString}appears,${pSpaceString}choose$pSpaceString&quot;edit&quot;${pSpaceString}and${pSpaceString}add${pSpaceString}the${pSpaceString}condition${pSpaceString}that${pSpaceString}the${pSpaceString}node${pSpaceString}content${pSpaceString}contains$pSpaceString&quot;TODO&quot;.${pSpaceString}Then${pSpaceString}select${pSpaceString}the${pSpaceString}filter${pSpaceString}in${pSpaceString}the${pSpaceString}filter${pSpaceString}toolbar.${pSpaceString}Now,${pSpaceString}only${pSpaceString}the${pSpaceString}filtered${pSpaceString}nodes${pSpaceString}and${pSpaceString}its${pSpaceString}ancestors${pSpaceString}are${pSpaceString}displayed${pSpaceString}unless${pSpaceString}you${pSpaceString}choose$pSpaceString&quot;No${pSpaceString}filtering&quot;${pSpaceString}in${pSpaceString}the${pSpaceString}toolbar.$pSpaceString
    </p>
    <p>
      Using${pSpaceString}the${pSpaceString}settings$pSpaceString&quot;Show${pSpaceString}ancestors&quot;${pSpaceString}and$pSpaceString&quot;Show${pSpaceString}descendants&quot;${pSpaceString}you${pSpaceString}can${pSpaceString}influence${pSpaceString}the${pSpaceString}apperance${pSpaceString}of${pSpaceString}the${pSpaceString}parent${pSpaceString}and${pSpaceString}child${pSpaceString}nodes${pSpaceString}that${pSpaceString}are${pSpaceString}connected${pSpaceString}with${pSpaceString}the${pSpaceString}nodes${pSpaceString}being${pSpaceString}filtered.
    </p>
    <p>
      There${pSpaceString}are${pSpaceString}many${pSpaceString}different${pSpaceString}criteria${pSpaceString}filters${pSpaceString}can${pSpaceString}be${pSpaceString}based${pSpaceString}on${pSpaceString}such${pSpaceString}as${pSpaceString}a${pSpaceString}set${pSpaceString}of${pSpaceString}selected${pSpaceString}nodes,${pSpaceString}a${pSpaceString}specific${pSpaceString}icon${pSpaceString}and${pSpaceString}some${pSpaceString}attributes.
    </p>
    <p>
      $pSpaceString
    </p>
  """
    }

    fun testUnicodeHandling() {
        val input = "if (myOldValue != null && myText.startsWith(myOldValue) == true) { \nmyText = myText.substring(myOldValue.length() + terminator.length());\n};\n"
        val escapedText = HtmlTools.toXMLEscapedText(input)
        val unicodeToHTMLUnicodeEntity = HtmlTools
                .unicodeToHTMLUnicodeEntity(escapedText, false)
        println(unicodeToHTMLUnicodeEntity)
        val unescapeHTMLUnicodeEntity = HtmlTools
                .unescapeHTMLUnicodeEntity(unicodeToHTMLUnicodeEntity)
        val back = HtmlTools.toXMLUnescapedText(unescapeHTMLUnicodeEntity)
        println(back)
        assertEquals("unicode conversion", input, back)
    }

    fun testHtmlBodyExtraction() {
        val input = getInputStringWithManySpaces(" ")
        val expectedOutput = getHtmlBody(" ")
        assertTrue(HtmlTools.isHtmlNode(input))
        assertEquals(expectedOutput, HtmlTools.extractHtmlBody(input))
    }

    @Throws(Exception::class)
    fun testIllegalXmlChars() {
        assertEquals(
                "Wrong chars are gone",
                "AB&#32;&#x20;",
                replaceUtf8AndIllegalXmlChars("&#x1f;A&#0;&#31;&#x0001B;B&#x1;&#32;&#1;&#x20;"))
    }

    @Throws(Exception::class)
    fun testSpaceReplacements() {
        assertEquals("Space conversion", " " + HtmlTools.NBSP,
                HtmlTools.replaceSpacesToNonbreakableSpaces("  "))
        assertEquals("Multiple space conversion", " " + HtmlTools.NBSP
                + HtmlTools.NBSP + HtmlTools.NBSP,
                HtmlTools.replaceSpacesToNonbreakableSpaces("    "))
        assertEquals("Double space conversion", " " + HtmlTools.NBSP + "xy "
                + HtmlTools.NBSP + HtmlTools.NBSP,
                HtmlTools.replaceSpacesToNonbreakableSpaces("  xy   "))
    }

    @Throws(Exception::class)
    fun testListDetection() {
        val instance: HtmlTools = HtmlTools.getInstance()
        val created = Tools.IntHolder()

//		new NodeTraversor(new NodeVisitor() {
//
//			@Override
//			public void head(Node pNode, int pDepth) {
//				System.out.println("Node: " + pNode.getClass() + ", " + pNode);
//			}
//
//			@Override
//			public void tail(Node pNode, int pDepth) {
//				System.out.println("/Node: " + pNode.getClass() + ", " + pNode);
//				
//			}}).traverse(Jsoup.parse(testHtml2));
        var rootNode: MindMapNode = TestMindMapNode()
        rootNode.text = "myRoot"
        val creator: NodeCreator = object : NodeCreator {
            override fun createChild(pParent: MindMapNode?): MindMapNode? {
                created.increase()
                println("Create new node as child of: " + pParent!!.text)
                val newNode = TestMindMapNode()
                pParent.insert(newNode, pParent.childCount)
                newNode.setParent(pParent)
                return newNode
            }

            override fun setText(pText: String?, pNode: MindMapNode?) {
                println("Text: $pText")
                pNode!!.text = pText
            }

            override fun setLink(pLink: String?, pNode: MindMapNode?) {}
        }
        instance.insertHtmlIntoNodes(testHtml1, rootNode, creator)
        assertEquals(1, created.value)
        assertEquals("Only one in the first level", 1, rootNode.childCount)
        created.value = 0
        rootNode = TestMindMapNode()
        rootNode.text = "myRoot2"
        instance.insertHtmlIntoNodes(testHtml2, rootNode, creator)
        assertEquals(5, created.value)
        assertEquals("Only two in the first level", 2, rootNode.getChildCount())
        created.value = 0
        rootNode = TestMindMapNode()
        rootNode.text = "myRoot3"
        instance.insertHtmlIntoNodes(testHtml3, rootNode, creator)
        assertEquals(13, created.value)
        assertEquals("first level nodes", 10, rootNode.getChildCount())
    }

    @Throws(Exception::class)
    fun testDetermineNodeAmount() {
        val mapFeedback = ExtendedMapFeedbackImpl()
        val mMap = MindMapMapModel(mapFeedback)
        mapFeedback.map = mMap
        val actor = PasteActor(mapFeedback)
        assertEquals(1, actor.determineAmountOfNewNodes(HtmlTransfer(testHtml1)))
        assertEquals(2, actor.determineAmountOfNewNodes(HtmlTransfer(testHtml2)))
        // this is one less, as the determine... strips the html header and uses its own.
        assertEquals(9, actor.determineAmountOfNewNodes(HtmlTransfer(testHtml3)))
    }

    companion object {
        const val testHtml1 = "<ul><li>bla</li></ul>"
        const val testHtml2 = ("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n"
                + "<html>\n"
                + "<head>\n"
                + "	<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>\n"
                + "	<title></title>\n"
                + "	<meta name=\"generator\" content=\"LibreOffice 4.3.3.2 (Linux)\"/>\n"
                + "	<meta name=\"author\" content=\"Christian Foltin\"/>\n"
                + "	<meta name=\"created\" content=\"2014-11-28T21:27:45.329992028\"/>\n"
                + "	<meta name=\"changedby\" content=\"Christian Foltin\"/>\n"
                + "	<meta name=\"changed\" content=\"2014-11-28T21:28:10.435205408\"/>\n"
                + "	<style type=\"text/css\">\n"
                + "		@page { margin: 2cm }\n"
                + "		p { margin-bottom: 0.25cm; line-height: 120% }\n"
                + "	</style>\n"
                + "</head>\n"
                + "<body lang=\"de-DE\" dir=\"ltr\">\n"
                + "<ul>\n"
                + "	<li/>\n"
                + "<p style=\"margin-bottom: 0cm; line-height: 100%\">Bla</p>\n"
                + "	<ul>\n"
                + "		<li/>\n"
                + "<p style=\"margin-bottom: 0cm; line-height: 100%\">blubber</p>\n"
                + "		<li/>\n"
                + "<p style=\"margin-bottom: 0cm; line-height: 100%\">zweite ebene</p>\n"
                + "		<ul>\n"
                + "			<li/>\n"
                + "<p style=\"margin-bottom: 0cm; line-height: 100%\">dritte\n"
                + "			ebene</p>\n"
                + "		</ul>\n"
                + "	</ul>\n"
                + "	<li/>\n"
                + "<p style=\"margin-bottom: 0cm; line-height: 100%\">1. ebene</p>\n"
                + "</ul>\n" + "</body>\n" + "</html>")
        const val testHtml3 = ("<html>\n"
                + "<head>\n"
                + "<title>FreeMind Import</title>\n"
                + "<link rel=\"important stylesheet\" href=\"chrome://messagebody/skin/messageBody.css\">\n"
                + "</head>\n"
                + "<body>\n"
                + "<table border=0 cellspacing=0 cellpadding=0 width=\"100%\" class=\"header-part1\"><tr><td><b>Betreff: </b>FreeMind Import</td></tr><tr><td><b>Von: </b>xxxxxxxxx yyyyyyy &lt;xxxxxxx.yyyyy@abc.de&gt;</td></tr><tr><td><b>Datum: </b>28.11.14 08:55</td></tr></table><table border=0 cellspacing=0 cellpadding=0 width=\"100%\" class=\"header-part2\"><tr><td><b>An: </b>&quot;xxxxxxxxx.yyyyyy@def.de&quot; &lt;xxxxxx.yyyyy@def.de&gt;</td></tr></table><br>\n"
                + "<html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\" xmlns=\"http://www.w3.org/TR/REC-html40\">\n"
                + "<head>\n"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; \">\n"
                + "<meta name=\"Generator\" content=\"Microsoft Word 14 (filtered medium)\">\n"
                + "<style><!--\n"
                + "/* Font Definitions */\n"
                + "@font-face\n"
                + "	{font-family:Wingdings;\n"
                + "	panose-1:5 0 0 0 0 0 0 0 0 0;}\n"
                + "@font-face\n"
                + "	{font-family:Wingdings;\n"
                + "	panose-1:5 0 0 0 0 0 0 0 0 0;}\n"
                + "@font-face\n"
                + "	{font-family:Calibri;\n"
                + "	panose-1:2 15 5 2 2 2 4 3 2 4;}\n"
                + "/* Style Definitions */\n"
                + "p.MsoNormal, li.MsoNormal, div.MsoNormal\n"
                + "	{margin:0cm;\n"
                + "	margin-bottom:.0001pt;\n"
                + "	font-size:11.0pt;\n"
                + "	font-family:\"Calibri\",\"sans-serif\";\n"
                + "	mso-fareast-language:EN-US;}\n"
                + "a:link, span.MsoHyperlink\n"
                + "	{mso-style-priority:99;\n"
                + "	color:blue;\n"
                + "	text-decoration:underline;}\n"
                + "a:visited, span.MsoHyperlinkFollowed\n"
                + "	{mso-style-priority:99;\n"
                + "	color:purple;\n"
                + "	text-decoration:underline;}\n"
                + "p.MsoListParagraph, li.MsoListParagraph, div.MsoListParagraph\n"
                + "	{mso-style-priority:34;\n"
                + "	margin-top:0cm;\n"
                + "	margin-right:0cm;\n"
                + "	margin-bottom:0cm;\n"
                + "	margin-left:36.0pt;\n"
                + "	margin-bottom:.0001pt;\n"
                + "	font-size:11.0pt;\n"
                + "	font-family:\"Calibri\",\"sans-serif\";\n"
                + "	mso-fareast-language:EN-US;}\n"
                + "span.E-MailFormatvorlage17\n"
                + "	{mso-style-type:personal-compose;\n"
                + "	font-family:\"Arial\",\"sans-serif\";\n"
                + "	color:windowtext;}\n"
                + ".MsoChpDefault\n"
                + "	{mso-style-type:export-only;\n"
                + "	font-family:\"Calibri\",\"sans-serif\";\n"
                + "	mso-fareast-language:EN-US;}\n"
                + "@page WordSection1\n"
                + "	{size:612.0pt 792.0pt;\n"
                + "	margin:72.0pt 72.0pt 72.0pt 72.0pt;}\n"
                + "div.WordSection1\n"
                + "	{page:WordSection1;}\n"
                + "/* List Definitions */\n"
                + "@list l0\n"
                + "	{mso-list-id:1096172370;\n"
                + "	mso-list-type:hybrid;\n"
                + "	mso-list-template-ids:-104571736 2049105486 67567619 67567621 67567617 67567619 67567621 67567617 67567619 67567621;}\n"
                + "@list l0:level1\n"
                + "	{mso-level-start-at:0;\n"
                + "	mso-level-number-format:bullet;\n"
                + "	mso-level-text:\\F0B7;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:Symbol;\n"
                + "	mso-fareast-font-family:Calibri;\n"
                + "	mso-bidi-font-family:\"Times New Roman\";}\n"
                + "@list l0:level2\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:o;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:\"Courier New\";}\n"
                + "@list l0:level3\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:\\F0A7;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:Wingdings;}\n"
                + "@list l0:level4\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:\\F0B7;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:Symbol;}\n"
                + "@list l0:level5\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:o;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:\"Courier New\";}\n"
                + "@list l0:level6\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:\\F0A7;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:Wingdings;}\n"
                + "@list l0:level7\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:\\F0B7;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:Symbol;}\n"
                + "@list l0:level8\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:o;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:\"Courier New\";}\n"
                + "@list l0:level9\n"
                + "	{mso-level-number-format:bullet;\n"
                + "	mso-level-text:\\F0A7;\n"
                + "	mso-level-tab-stop:none;\n"
                + "	mso-level-number-position:left;\n"
                + "	text-indent:-18.0pt;\n"
                + "	font-family:Wingdings;}\n"
                + "ol\n"
                + "	{margin-bottom:0cm;}\n"
                + "ul\n"
                + "	{margin-bottom:0cm;}\n"
                + "--></style><!--[if gte mso 9]><xml>\n"
                + "<o:shapedefaults v:ext=\"edit\" spidmax=\"1026\" />\n"
                + "</xml><![endif]--><!--[if gte mso 9]><xml>\n"
                + "<o:shapelayout v:ext=\"edit\">\n"
                + "<o:idmap v:ext=\"edit\" data=\"1\" />\n"
                + "</o:shapelayout></xml><![endif]-->\n"
                + "</head>\n"
                + "<body lang=\"DE\" link=\"blue\" vlink=\"purple\">\n"
                + "<div class=\"WordSection1\">\n"
                + "<p class=\"MsoListParagraph\" style=\"text-indent:-18.0pt;mso-list:l0 level1 lfo1\"><![if !supportLists]><span style=\"font-family:Symbol\"><span style=\"mso-list:Ignore\">·<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n"
                + "</span></span></span><![endif]><span style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">Bla<o:p></o:p></span></p>\n"
                + "<p class=\"MsoListParagraph\" style=\"margin-left:72.0pt;text-indent:-18.0pt;mso-list:l0 level2 lfo1\">\n"
                + "<![if !supportLists]><span style=\"font-family:&quot;Courier New&quot;\"><span style=\"mso-list:Ignore\">o<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;&nbsp;\n"
                + "</span></span></span><![endif]><span style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">Unterpunkt<o:p></o:p></span></p>\n"
                + "<p class=\"MsoListParagraph\" style=\"margin-left:72.0pt;text-indent:-18.0pt;mso-list:l0 level2 lfo1\">\n"
                + "<![if !supportLists]><span style=\"font-family:&quot;Courier New&quot;\"><span style=\"mso-list:Ignore\">o<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;&nbsp;\n"
                + "</span></span></span><![endif]><span style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">Zweiter Unterpunkt<o:p></o:p></span></p>\n"
                + "<p class=\"MsoListParagraph\" style=\"margin-left:108.0pt;text-indent:-18.0pt;mso-list:l0 level3 lfo1\">\n"
                + "<![if !supportLists]><span style=\"font-family:Wingdings\"><span style=\"mso-list:Ignore\">§<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;\n"
                + "</span></span></span><![endif]><span style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">Dritte Ebene<o:p></o:p></span></p>\n"
                + "<p class=\"MsoListParagraph\" style=\"text-indent:-18.0pt;mso-list:l0 level1 lfo1\"><![if !supportLists]><span style=\"font-family:Symbol\"><span style=\"mso-list:Ignore\">·<span style=\"font:7.0pt &quot;Times New Roman&quot;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n"
                + "</span></span></span><![endif]><span style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\">Hauptpunkt<o:p></o:p></span></p>\n"
                + "<p class=\"MsoNormal\"><span style=\"font-family:&quot;Arial&quot;,&quot;sans-serif&quot;\"><o:p>&nbsp;</o:p></span></p>\n"
                + "<p class=\"MsoNormal\"><span lang=\"EN-US\" style=\"mso-fareast-language:DE\">Viele Grüße/Best regards<o:p></o:p></span></p>\n"
                + "<p class=\"MsoNormal\"><span lang=\"EN-US\" style=\"mso-fareast-language:DE\"><o:p>&nbsp;</o:p></span></p>\n"
                + "<p class=\"MsoNormal\"><span lang=\"EN-US\" style=\"mso-fareast-language:DE\">XXXX YYYYYYY<o:p></o:p></span></p>\n"
                + "<p class=\"MsoNormal\"><span style=\"mso-fareast-language:DE\">(-007)<o:p></o:p></span></p>\n"
                + "<p class=\"MsoNormal\"><o:p>&nbsp;</o:p></p>\n" + "</div>\n"
                + "</body>\n" + "</html>\n" + "\n" + "</body>\n" + "</html>\n")
    }
}