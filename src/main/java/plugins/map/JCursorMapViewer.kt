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
package plugins.map

import freemind.extensions.HookAdapter.getResourceString
import freemind.common.TextTranslator.getText
import freemind.controller.actions.generated.instance.Place.lat
import freemind.controller.actions.generated.instance.Place.lon
import freemind.controller.actions.generated.instance.Place.displayName
import freemind.extensions.HookAdapter.startupMapHook
import freemind.modes.mindmapmode.hooks.MindMapHookAdapter.mindMapController
import freemind.modes.ControllerAdapter.controller
import freemind.controller.Controller.mapModuleManager
import freemind.controller.MapModuleManager.addListener
import freemind.extensions.HookAdapter.getController
import freemind.modes.ModeController.frame
import freemind.main.Tools.addEscapeActionToDialog
import freemind.main.Tools.correctJSplitPaneKeyMap
import freemind.modes.ControllerAdapter.registerNodeSelectionListener
import freemind.modes.mindmapmode.MindMapController.decorateDialog
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.zoomControlsVisible
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.tileGridVisible
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.showMapMarker
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.hideFoldedNodes
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.listTableColumnSettingList
import freemind.controller.actions.generated.instance.TableColumnSetting.columnWidth
import freemind.controller.actions.generated.instance.TableColumnSetting.columnSorting
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.limitSearchToVisibleArea
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.searchControlVisible
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.lastDividerPosition
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.listMapLocationStorageList
import freemind.controller.actions.generated.instance.MapLocationStorage.cursorLatitude
import freemind.controller.actions.generated.instance.MapLocationStorage.cursorLongitude
import freemind.controller.actions.generated.instance.MapLocationStorage.zoom
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.mapLocationStorageIndex
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.mapCenterLatitude
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.mapCenterLongitude
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.zoom
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.cursorLatitude
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.cursorLongitude
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.tileSource
import freemind.extensions.HookAdapter.getPluginBaseClass
import freemind.main.Tools.isMacOsX
import freemind.modes.ControllerAdapter.deregisterNodeSelectionListener
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.addTableColumnSetting
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage.addMapLocationStorage
import freemind.modes.mindmapmode.MindMapController.storeDialogPositions
import freemind.controller.MapModuleManager.removeListener
import freemind.view.mindmapview.NodeView.model
import freemind.main.Tools.fromBase64
import freemind.main.Tools.toBase64
import freemind.main.Tools.IntHolder.value
import freemind.modes.mindmapmode.MindMapController.actionRegistry
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.deregisterActor
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.registerActor
import freemind.main.Tools.safeEquals
import freemind.modes.ControllerAdapter.frame
import freemind.main.FreeMindMain.freemindVersion
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.doTransaction
import freemind.controller.actions.generated.instance.NodeAction.node
import freemind.modes.ControllerAdapter.getNodeID
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction.cursorLatitude
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction.cursorLongitude
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction.mapCenterLatitude
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction.mapCenterLongitude
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction.zoom
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction.tileSource
import freemind.modes.ControllerAdapter.getNodeFromID
import freemind.modes.common.plugins.MapNodePositionHolderBase.setTooltip
import freemind.modes.mindmapmode.MindMapController.nodeChanged
import freemind.modes.mindmapmode.actions.NodeHookAction.hookName
import freemind.modes.ControllerAdapter.selecteds
import freemind.modes.MindMapNode.text
import freemind.modes.MindMapNode.color
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter.mindMapController
import freemind.modes.MindMapNode.link
import freemind.modes.mindmapmode.MindMapController.setLink
import freemind.modes.mindmapmode.MindMapController.createModeControllerHook
import freemind.modes.ControllerAdapter.view
import freemind.view.mindmapview.MapView.selectAsTheOnlyOneSelected
import freemind.modes.mindmapmode.MindMapController.cut
import freemind.main.Tools.getVectorWithSingleElement
import freemind.modes.ControllerAdapter.select
import freemind.modes.ControllerAdapter.getNodeView
import freemind.modes.mindmapmode.MindMapController.setNodeText
import freemind.modes.ControllerAdapter.isBlocked
import freemind.modes.ControllerAdapter.selected
import freemind.controller.Controller.setProperty
import freemind.modes.MindMapNode.isRoot
import freemind.modes.MindMapNode.isFolded
import freemind.modes.mindmapmode.MindMapController.setFolded
import freemind.modes.MindMapNode.parentNode
import freemind.main.Tools.clipboard
import freemind.main.FreeMindMain.setWaitingCursor
import freemind.modes.mindmapmode.MindMapController.addNewNode
import freemind.modes.MindMapNode.isLeft
import freemind.controller.StructuredMenuHolder.addMenu
import freemind.controller.StructuredMenuHolder.addAction
import freemind.controller.StructuredMenuHolder.addSeparator
import freemind.controller.StructuredMenuHolder.updateMenus
import freemind.main.FreeMindMain.getProperty
import freemind.modes.ControllerAdapter.getText
import freemind.main.Tools.convertPointToAncestor
import freemind.view.mindmapview.EditNodeTextField.show
import freemind.modes.MindMapNode.getChildPosition
import freemind.controller.actions.generated.instance.Searchresults.listPlaceList
import freemind.controller.actions.generated.instance.Place.osmType
import freemind.controller.actions.generated.instance.Reversegeocode.listResultList
import freemind.controller.actions.generated.instance.Reversegeocode.getResult
import freemind.controller.actions.generated.instance.Searchresults.addPlace
import freemind.main.Tools.isAboveJava4
import freemind.main.Tools.getFile
import freemind.main.Tools.getNodeTextHierarchy
import freemind.main.Tools.invokeActionsToKeyboardLayoutDependantCharacters
import freemind.common.FreeMindTask.rounds
import freemind.modes.ModeController.selected
import freemind.modes.common.plugins.MapNodePositionHolderBase.invoke
import freemind.modes.common.plugins.MapNodePositionHolderBase.isTooltipDesired
import freemind.modes.common.plugins.MapNodePositionHolderBase.shutdownMapHook
import freemind.extensions.PermanentNodeHookAdapter.save
import freemind.extensions.PermanentNodeHookAdapter.saveNameValuePairs
import freemind.modes.common.plugins.MapNodePositionHolderBase.loadFrom
import freemind.extensions.PermanentNodeHookAdapter.loadNameValuePairs
import freemind.extensions.NodeHookAdapter.getNode
import freemind.modes.MindMapNode.hasFoldedParents
import freemind.extensions.PermanentNodeHookAdapter.onViewCreatedHook
import freemind.extensions.PermanentNodeHookAdapter.onViewRemovedHook
import freemind.extensions.PermanentNodeHookAdapter.saveHtml
import freemind.modes.common.plugins.MapNodePositionHolderBase.imageHtml
import freemind.extensions.NodeHookAdapter.invoke
import freemind.main.FreeMindMain.jFrame
import freemind.modes.ControllerAdapter.selectedView
import freemind.common.OptionalDontShowMeAgainDialog.show
import freemind.common.OptionalDontShowMeAgainDialog.result
import freemind.modes.ModeController.selecteds
import freemind.modes.MindMapNode.getShortText
import freemind.extensions.ExportHook.chooseFile
import freemind.main.Tools.fileToUrl
import freemind.modes.ModeController.controller
import freemind.modes.ModeController.view
import freemind.controller.Controller.getResourceString
import freemind.controller.Controller.getProperty
import freemind.view.mindmapview.MapView.preparePrinting
import freemind.view.mindmapview.MapView.innerBounds
import freemind.view.mindmapview.MapView.endPrinting
import freemind.view.mindmapview.MapView.getViewers
import freemind.view.mindmapview.NodeView.innerBounds
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter.mindMapController
import freemind.view.mindmapview.NodeView.getContentPane
import freemind.modes.ModeController.nodeChanged
import freemind.main.XMLElement.getAttribute
import freemind.extensions.PermanentNodeHookAdapter.loadFrom
import freemind.main.XMLElement.setAttribute
import freemind.extensions.PermanentNodeHookAdapter.shutdownMapHook
import freemind.modes.attributes.Attribute.name
import freemind.modes.attributes.Attribute.value
import freemind.modes.MindMapNode.attributeTableLength
import freemind.modes.mindmapmode.MindMapController.addAttribute
import freemind.modes.MindMapNode.getAttribute
import freemind.modes.mindmapmode.MindMapController.setAttribute
import freemind.modes.ControllerAdapter.map
import freemind.modes.MindMap.rootNode
import freemind.modes.MindMapNode.childrenUnfolded
import freemind.main.Tools.BooleanHolder.value
import freemind.main.Tools.isPreferenceTrue
import freemind.main.FreeMindSecurityManager.setFinalSecurityManager
import freemind.main.FreeMindMain.setProperty
import freemind.controller.Controller.errorMessage
import freemind.main.FreeMindMain.getResourceString
import freemind.main.FreeMindMain.getLogger
import freemind.main.Tools.setLabelAndMnemonic
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage.leftRatio
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage.topRatio
import freemind.controller.MenuItemEnabledListener.isEnabled
import freemind.modes.common.dialogs.EnterPasswordDialog.result
import freemind.modes.common.dialogs.EnterPasswordDialog.password
import freemind.main.FreeMindMain.controller
import freemind.modes.mindmapmode.MindMapController.registerPlugin
import freemind.main.FreeMindMain.registerStartupDoneListener
import freemind.modes.mindmapmode.MindMapController.deregisterPlugin
import freemind.controller.actions.generated.instance.Pattern.patternScript
import freemind.controller.actions.generated.instance.PatternPropertyBase.value
import freemind.main.Tools.expandFileName
import freemind.modes.ControllerAdapter.rootNode
import freemind.modes.ModeController.loadURL
import freemind.view.MapModule.model
import freemind.modes.MindMap.file
import freemind.controller.actions.generated.instance.CollaborationAction.filename
import freemind.modes.MindMap.getXml
import freemind.controller.actions.generated.instance.CollaborationAction.map
import freemind.controller.actions.generated.instance.CollaborationAction.cmd
import freemind.controller.actions.generated.instance.CollaborationAction.user
import freemind.controller.actions.generated.instance.CollaborationAction.timestamp
import freemind.modes.mindmapmode.MindMapController.marshall
import freemind.main.Tools.compress
import freemind.controller.actions.generated.instance.CompoundAction.listChoiceList
import freemind.modes.mindmapmode.actions.xml.ActionPair.doAction
import freemind.modes.mindmapmode.actions.xml.ActionPair.undoAction
import freemind.controller.Controller.frame
import freemind.main.Tools.decompress
import freemind.modes.mindmapmode.MindMapController.unMarshall
import freemind.main.FreeMindMain.setTitle
import freemind.common.PropertyControl.layout
import freemind.common.PropertyBean.addPropertyChangeListener
import freemind.main.Tools.addKeyActionToDialog
import freemind.main.Tools.userName
import freemind.controller.Controller.registerMapTitleContributor
import freemind.controller.Controller.deregisterMapTitleContributor
import freemind.controller.Controller.setTitle
import freemind.extensions.HookAdapter.shutdownMapHook
import freemind.modes.MapFeedback.setProperty
import freemind.common.NumberProperty.value
import freemind.modes.MapFeedback.getIntProperty
import freemind.view.MapModule.modeController
import freemind.controller.actions.generated.instance.CollaborationUserInformation.masterHostname
import freemind.controller.actions.generated.instance.CollaborationUserInformation.masterIp
import freemind.controller.actions.generated.instance.CollaborationUserInformation.masterPort
import freemind.controller.actions.generated.instance.CollaborationUserInformation.userIds
import freemind.controller.actions.generated.instance.HookNodeAction.hookName
import freemind.main.Tools.marshall
import freemind.modes.ExtendedMapFeedback.actionRegistry
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.registerFilter
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.deregisterFilter
import freemind.modes.ExtendedMapFeedback.doTransaction
import freemind.main.Tools.hostName
import freemind.main.Tools.hostIpAsString
import freemind.modes.ExtendedMapFeedbackImpl.map
import freemind.modes.MapAdapter.loadTree
import freemind.modes.MapAdapter.root
import freemind.modes.MapFeedbackAdapter.invokeHooksRecursively
import freemind.modes.ExtendedMapFeedbackAdapter.actionRegistry
import freemind.modes.MapAdapter.file
import freemind.extensions.HookAdapter.getProperties
import freemind.modes.mindmapmode.MindMapController.out
import freemind.modes.ControllerAdapter.getResourceString
import freemind.common.StringProperty.value
import freemind.common.NumberProperty.intValue
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.userId
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.password
import freemind.modes.MapFeedback.map
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.mapName
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap.map
import freemind.modes.MindMapNode.activatedHooks
import freemind.modes.MapFeedback.out
import freemind.modes.ModeController.getText
import freemind.controller.actions.generated.instance.CollaborationGoodbye.userId
import freemind.main.Tools.unMarshall
import freemind.controller.actions.generated.instance.CollaborationTransaction.doAction
import freemind.main.Tools.printXmlAction
import freemind.controller.actions.generated.instance.CollaborationTransaction.id
import freemind.controller.actions.generated.instance.CollaborationTransaction.undoAction
import freemind.controller.actions.generated.instance.CollaborationWhoAreYou.serverVersion
import freemind.modes.MapFeedback.getResourceString
import freemind.controller.actions.generated.instance.CollaborationOffers.listCollaborationMapOfferList
import freemind.controller.actions.generated.instance.CollaborationHello.map
import freemind.controller.actions.generated.instance.CollaborationOffers.getCollaborationMapOffer
import freemind.controller.actions.generated.instance.CollaborationMapOffer.map
import freemind.controller.actions.generated.instance.CollaborationWelcome.map
import freemind.controller.actions.generated.instance.CollaborationReceiveLock.id
import freemind.controller.actions.generated.instance.CollaborationGetOffers.userId
import freemind.controller.actions.generated.instance.CollaborationGetOffers.password
import freemind.modes.ControllerAdapter.mode
import freemind.modes.Mode.createModeController
import freemind.modes.ControllerAdapter.setModel
import freemind.modes.MapAdapter.createNodeTreeFromXml
import freemind.modes.NodeAdapter.map
import freemind.modes.ControllerAdapter.newMap
import freemind.controller.actions.generated.instance.CollaborationOffers.addCollaborationMapOffer
import freemind.modes.MindMap.save
import freemind.controller.actions.generated.instance.CollaborationWelcome.filename
import freemind.modes.MindMap.isSaved
import freemind.modes.mindmapmode.MindMapController.doTransaction
import freemind.main.FreeMindMain.getIntProperty
import freemind.main.FreeMindMain.freemindDirectory
import freemind.modes.mindmapmode.hooks.MindMapHookAdapter
import freemind.controller.MapModuleManager.MapModuleChangeObserver
import plugins.map.MapNodePositionHolder.MapNodePositionListener
import freemind.modes.ModeController.NodeSelectionListener
import plugins.map.Registration.NodeVisibilityListener
import plugins.map.JCursorMapViewer
import freemind.modes.mindmapmode.MindMapController
import plugins.map.MapNodePositionHolder
import plugins.map.MapMarkerLocation
import plugins.map.MapDialog.ResultTableModel
import java.awt.Color
import java.awt.event.ActionEvent
import freemind.common.TextTranslator
import javax.swing.table.AbstractTableModel
import plugins.map.FreeMindMapController.CursorPositionListener
import plugins.map.MapDialog
import freemind.controller.actions.generated.instance.Place
import plugins.map.MapSearchMarkerLocation
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import plugins.map.FreeMindMapController
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.Mapnik
import java.awt.BorderLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import freemind.common.ScalableJTable
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseListener
import java.awt.event.MouseAdapter
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage
import freemind.controller.actions.generated.instance.TableColumnSetting
import plugins.map.FreeMindMapController.PositionHolder
import freemind.controller.actions.generated.instance.MapLocationStorage
import java.awt.AWTEvent
import java.lang.Runnable
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent
import freemind.view.MapModule
import freemind.modes.MindMapNode
import java.util.Collections
import java.awt.image.ImageObserver
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.BasicStroke
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.FileInputStream
import java.awt.image.RenderedImage
import freemind.modes.ModeController
import freemind.modes.MindMap
import freemind.extensions.HookRegistration
import freemind.modes.mindmapmode.actions.xml.ActorXml
import freemind.controller.MenuItemEnabledListener
import java.util.TimerTask
import plugins.map.Registration.CachePurger.AgeFilter
import plugins.map.Registration.MapDialogPropertyContributor
import freemind.preferences.FreemindPropertyContributor
import freemind.common.PropertyControl
import freemind.preferences.layout.OptionPanel.NewTabProperty
import freemind.common.SeparatorProperty
import freemind.common.BooleanProperty
import freemind.common.DontShowNotificationProperty
import freemind.preferences.layout.OptionPanel
import freemind.main.FreeMindMain.VersionInformation
import freemind.controller.actions.generated.instance.PlaceNodeXmlAction
import freemind.modes.mindmapmode.actions.xml.ActionRegistry
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.controller.actions.generated.instance.XmlAction
import freemind.modes.mindmapmode.actions.NodeHookAction
import plugins.map.SearchInMapForNodeTextAction
import plugins.map.ShowMapToNodeAction
import plugins.map.AddLinkToMapAction
import plugins.map.RemoveMapToNodeAction
import plugins.map.AddMapImageToNodeAction
import plugins.map.Registration.CachePurger
import plugins.map.MapMarkerBase
import java.awt.Graphics
import java.awt.Stroke
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker.STYLE
import plugins.map.JCursorMapViewer.ScaledTile
import java.awt.RenderingHints
import plugins.map.JCursorMapViewer.ScalableTileController
import java.awt.Rectangle
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelListener
import java.awt.event.KeyListener
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource
import plugins.map.FreeMindMapController.TransportMap
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource.TileUpdate
import plugins.map.FreeMindMapController.MapQuestOpenMap
import freemind.view.mindmapview.EditNodeBase.EditControl
import freemind.view.mindmapview.EditNodeTextField
import freemind.controller.MenuItemSelectedListener
import plugins.map.FreeMindMapController.MaxmimalZoomToCursorAction
import java.awt.datatransfer.StringSelection
import freemind.extensions.ExportHook
import java.io.FileOutputStream
import freemind.modes.common.plugins.MapNodePositionHolderBase
import plugins.map.FreeMindMapController.MapEditTextFieldControl
import plugins.map.FreeMindMapController.MapEditNoteTextField
import java.awt.Cursor
import javax.swing.event.PopupMenuListener
import javax.swing.event.PopupMenuEvent
import plugins.map.FreeMindMapController.MoveForwardAction
import plugins.map.FreeMindMapController.MoveBackwardAction
import plugins.map.FreeMindMapController.PlaceNodeAction
import plugins.map.FreeMindMapController.ShowNodeAction
import plugins.map.FreeMindMapController.SetDisplayToFitMapMarkers
import plugins.map.FreeMindMapController.ShowMapMarker
import plugins.map.FreeMindMapController.TileGridVisible
import plugins.map.FreeMindMapController.ZoomControlsVisible
import plugins.map.FreeMindMapController.SearchControlVisible
import plugins.map.FreeMindMapController.GotoSearch
import plugins.map.FreeMindMapController.HideFoldedNodes
import plugins.map.FreeMindMapController.CopyLinkToClipboardAction
import plugins.map.FreeMindMapController.CopyCoordinatesToClipboardAction
import plugins.map.FreeMindMapController.ExportMapAction
import freemind.controller.StructuredMenuHolder
import plugins.map.FreeMindMapController.LimitSearchToRegionAction
import plugins.map.FreeMindMapController.ChangeTileSource
import plugins.map.FreeMindMapController.SetHomeAction
import plugins.map.FreeMindMapController.MoveHomeAction
import plugins.map.FreeMindMapController.MoveLeftAction
import plugins.map.FreeMindMapController.MoveRightAction
import plugins.map.FreeMindMapController.EditNodeInContextMenu
import plugins.map.FreeMindMapController.RemoveNodeLocationInContextMenu
import plugins.map.FreeMindMapController.SelectNodeInContextMenu
import plugins.map.FreeMindMapController.SelectNodeAndCloseInContextMenu
import plugins.map.FreeMindMapController.ShowNodeMapInContextMenu
import plugins.map.FreeMindMapController.AddMapPictureToNode
import java.awt.event.MouseWheelEvent
import java.lang.RuntimeException
import freemind.controller.actions.generated.instance.Searchresults
import freemind.controller.actions.generated.instance.Reversegeocode
import java.lang.StringBuilder
import java.util.Locale
import freemind.common.XmlBindingTools
import java.net.URLEncoder
import java.text.MessageFormat
import kotlin.Throws
import java.net.MalformedURLException
import java.io.UnsupportedEncodingException
import freemind.common.FreeMindTask
import freemind.common.FreeMindTask.ProgressDescription
import plugins.map.FreeMindMapController.AddSearchResultsToMapTask
import plugins.map.FreeMindMapController.TileSourceStore
import plugins.map.FreeMindMapController.HttpMapnik
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.CycleMap
import java.util.Arrays
import java.lang.StringBuffer
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.OptionalDontShowMeAgainDialog.StandardPropertyHandler
import plugins.svg.ExportVectorGraphic
import plugins.svg.ExportPdfPapers
import org.apache.batik.transcoder.TranscodingHints
import plugins.svg.ExportPdfDialog
import java.awt.print.PageFormat
import java.awt.print.Paper
import kotlin.jvm.JvmOverloads
import freemind.view.mindmapview.MapView
import org.apache.batik.svggen.SVGGraphics2D
import org.apache.batik.transcoder.TranscoderInput
import java.io.BufferedOutputStream
import org.apache.batik.transcoder.TranscoderOutput
import java.io.OutputStreamWriter
import java.awt.Dimension
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import javax.print.attribute.standard.MediaSizeName
import javax.print.attribute.standard.MediaSize
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.util.SVGConstants
import org.apache.batik.svggen.SVGGeneratorContext
import org.apache.batik.svggen.SVGGeneratorContext.GraphicContextDefaults
import java.io.FileNotFoundException
import freemind.extensions.ModeControllerHookAdapter
import javax.help.HelpSet
import javax.help.HelpBroker
import plugins.latex.LatexNodeHook
import atp.sHotEqn
import plugins.latex.JZoomedHotEqn
import java.awt.geom.AffineTransform
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter
import java.util.LinkedHashSet
import plugins.script.ScriptEditorPanel.ScriptModel
import plugins.script.ScriptEditorPanel
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage
import java.io.PrintStream
import plugins.script.ScriptingRegistration
import plugins.script.ScriptingEngine
import freemind.main.Tools.BooleanHolder
import plugins.script.ScriptEditorPanel.ScriptHolder
import plugins.script.ScriptEditor.NodeScriptModel
import groovy.lang.GroovyRuntimeException
import plugins.script.SignedScriptHandler
import plugins.script.ScriptingSecurityManager
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer
import groovy.transform.ThreadInterrupt
import groovy.lang.GroovyShell
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.ast.ASTNode
import java.lang.InterruptedException
import javax.swing.event.MenuListener
import plugins.script.ScriptEditorPanel.SignAction
import plugins.script.ScriptEditorPanel.StopAction
import javax.swing.event.CaretListener
import javax.swing.event.CaretEvent
import javax.swing.text.BadLocationException
import plugins.script.ScriptEditorPanel.NewScriptAction
import plugins.script.ScriptEditorPanel.RunAction
import freemind.controller.BlindIcon
import plugins.script.ScriptEditorPanel.ResultFieldStream
import javax.swing.event.MenuEvent
import plugins.script.SignedScriptHandler.ScriptContents
import java.security.KeyStore
import freemind.modes.common.dialogs.EnterPasswordDialog
import java.security.PrivateKey
import java.security.cert.CertificateFactory
import freemind.modes.mindmapmode.actions.ApplyPatternAction.ExternalPatternAction
import freemind.main.FreeMindMain.StartupDoneListener
import freemind.common.ScriptEditorProperty.ScriptEditorStarter
import plugins.script.ScriptingRegistration.ScriptingPluginPropertyContributor
import plugins.script.ScriptingRegistration.PatternScriptModel
import java.net.InetAddress
import java.io.FileDescriptor
import java.lang.SecurityException
import plugins.search.FileSearchModel
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.TopScoreDocCollector
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.IndexWriter
import java.io.FileReader
import org.apache.lucene.document.StringField
import org.apache.lucene.analysis.standard.StandardAnalyzer
import plugins.search.ISearchController
import plugins.search.SearchViewPanel
import java.awt.Insets
import java.awt.HeadlessException
import java.util.Enumeration
import kotlin.jvm.JvmStatic
import java.lang.ClassCastException
import plugins.collaboration.jabber.view.SpringUtilities
import plugins.collaboration.jabber.view.MapSharingWizardView
import plugins.collaboration.jabber.mindmap.MapSharingController
import freemind.modes.mindmapmode.actions.xml.ActionFilter
import plugins.collaboration.jabber.mindmap.JabberSender
import freemind.controller.actions.generated.instance.CollaborationAction
import freemind.controller.actions.generated.instance.CompoundAction
import java.util.LinkedList
import plugins.collaboration.jabber.mindmap.JabberListener
import plugins.collaboration.jabber.mindmap.JabberListener.FreeMindJabberMessageListener
import plugins.collaboration.jabber.mindmap.MapSharingController.CloseButtonClickListener
import plugins.collaboration.jabber.mindmap.MapSharingController.NextButtonClickListener
import plugins.collaboration.jabber.mindmap.MapSharingController.BackButtonClickListener
import plugins.collaboration.jabber.mindmap.MapSharingController.AcceptButtonClickListener
import plugins.collaboration.jabber.mindmap.MapSharingController.DeclineButtonClickListener
import freemind.controller.actions.generated.instance.RevertXmlAction
import plugins.collaboration.socket.FormDialog.FormDialogValidator
import plugins.collaboration.socket.FormDialog
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.common.PropertyBean
import com.jgoodies.forms.builder.ButtonBarBuilder
import freemind.controller.MapModuleManager.MapTitleContributor
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FirstActionFilter
import freemind.modes.ExtendedMapFeedback
import plugins.collaboration.socket.SocketBasics
import freemind.controller.actions.generated.instance.CollaborationUserInformation
import freemind.controller.actions.generated.instance.HookNodeAction
import plugins.collaboration.socket.SocketBasics.UnableToGetLockException
import plugins.collaboration.socket.ServerCommunication
import java.lang.IllegalStateException
import freemind.main.Tools.ReaderCreator
import freemind.modes.ExtendedMapFeedbackImpl
import freemind.modes.mindmapmode.MindMapMapModel
import freemind.modes.MapAdapter
import plugins.collaboration.socket.SocketConnectionHook
import plugins.collaboration.socket.MindMapClient
import java.net.Socket
import plugins.collaboration.socket.ClientCommunication
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap
import plugins.collaboration.socket.CommunicationBase
import freemind.extensions.PermanentNodeHook
import plugins.collaboration.socket.SocketMaster
import freemind.extensions.DontSaveMarker
import java.net.ServerSocket
import plugins.collaboration.socket.TerminateableThread
import java.net.SocketTimeoutException
import freemind.controller.actions.generated.instance.CollaborationGoodbye
import java.io.DataInputStream
import freemind.controller.actions.generated.instance.CollaborationActionBase
import java.io.EOFException
import freemind.controller.actions.generated.instance.CollaborationTransaction
import plugins.collaboration.socket.MindMapMaster
import freemind.controller.actions.generated.instance.CollaborationWhoAreYou
import freemind.controller.actions.generated.instance.CollaborationOffers
import freemind.controller.actions.generated.instance.CollaborationHello
import freemind.controller.actions.generated.instance.CollaborationWelcome
import freemind.controller.actions.generated.instance.CollaborationWrongCredentials
import freemind.controller.actions.generated.instance.CollaborationWrongMap
import freemind.controller.actions.generated.instance.CollaborationReceiveLock
import freemind.controller.actions.generated.instance.CollaborationUnableToLock
import freemind.controller.actions.generated.instance.CollaborationGetOffers
import freemind.controller.actions.generated.instance.CollaborationRequireLock
import freemind.modes.NodeAdapter
import freemind.modes.mindmapmode.MindMapNodeModel
import freemind.controller.actions.generated.instance.CollaborationMapOffer
import freemind.main.*
import freemind.main.Tools.StringReaderCreator
import tests.freemind.FreeMindMainMock
import java.io.FilenameFilter
import freemind.main.Tools.FileReaderCreator
import plugins.collaboration.socket.StandaloneMindMapMaster
import plugins.collaboration.database.DatabaseBasics.ResultHandler
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter
import org.openstreetmap.gui.jmapviewer.*
import org.openstreetmap.gui.jmapviewer.interfaces.*
import java.math.BigInteger
import java.sql.PreparedStatement
import plugins.collaboration.database.UpdateThread
import java.sql.SQLException
import java.sql.ResultSet
import plugins.collaboration.database.DatabaseBasics
import plugins.collaboration.database.DatabaseConnectionHook
import java.lang.reflect.InvocationTargetException
import java.sql.DriverManager
import plugins.collaboration.database.DatabaseConnector
import plugins.collaboration.database.DatabaseStarter
import javax.swing.*

/**
 * @author foltin
 * @date 24.10.2011
 */
class JCursorMapViewer(pMindMapController: MindMapController?,
                       pMapDialog: JDialog?, pTileCache: TileCache?, pMapHook: MapDialog) : JMapViewer(pTileCache, 8) {
    private class ScalableTileController(pSource: TileSource,
                                         pTileCache: TileCache?, pListener: TileLoaderListener) : TileController(pSource, pTileCache, pListener) {
        override fun getTile(tilex: Int, tiley: Int, zoom: Int): Tile {
            val max = 1 shl zoom
            if (tilex < 0 || tilex >= max || tiley < 0 || tiley >= max) return null
            var tile = tileCache.getTile(tileSource, tilex, tiley, zoom)
            if (tile == null) {
                val scale: Int = Resources.getInstance().getIntProperty(FreeMind.SCALING_FACTOR_PROPERTY, 100)
                tile = if (scale >= 200) {
                    ScaledTile(this, tileSource, tilex, tiley, zoom)
                } else {
                    Tile(tileSource, tilex, tiley, zoom)
                }
                tileCache.addTile(tile)
                tile.loadPlaceholderFromCache(tileCache)
            }
            return super.getTile(tilex, tiley, zoom)
        }
    }

    private class ScaledTile(private val tileController: TileController, pSource: TileSource, pXtile: Int, pYtile: Int, pZoom: Int) : Tile(pSource, pXtile, pYtile, pZoom) {
        private var scaledImage: BufferedImage? = null
        override fun paint(pG: Graphics, pX: Int, pY: Int) {
            if (scaledImage == null) {
                val xtile_low = xtile shr 1
                val ytile_low = ytile shr 1
                val tile = tileController.getTile(xtile_low, ytile_low, zoom - 1)
                if (tile != null && tile.isLoaded) {
                    val tileSize = source.tileSize
                    val translate_x = xtile % 2 * tileSize / 2
                    val translate_y = ytile % 2 * tileSize / 2
                    val image2 = tile.image
                    scaledImage = BufferedImage(tileSize, tileSize, image2.type)
                    val g = scaledImage!!.createGraphics()
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
                    g.drawImage(image2, 0, 0, scaledImage!!.width, scaledImage!!.height, translate_x, translate_y, translate_x + tileSize / 2, translate_y + tileSize / 2, null)
                    g.dispose()
                }
            }
            if (scaledImage != null) {
                pG.drawImage(scaledImage, pX, pY, null)
            } else {
                pG.drawImage(getImage(), pX, pY, null)
            }
        }
    }

    var mShowCursor = false
    var mUseCursor = false
    var mCursorPosition: Coordinate? = null
    var mStroke: Stroke
    var mRectangularStroke: Stroke = BasicStroke(1, BasicStroke.CAP_SQUARE,
            BasicStroke.JOIN_MITER, 10.0f, floatArrayOf(10.0f, 10.0f), 0.0f)
    val freeMindMapController: FreeMindMapController
    private var mHideFoldedNodes = true
    private var mRectangularStart: Coordinate? = null
    private var mRectangularEnd: Coordinate? = null
    var isDrawRectangular = false
    private val mCursorLength: Int

    /**
     * @param pMindMapController
     * @param pMapDialog
     * @param pMapHook
     */
    init {
        tileController = ScalableTileController(tileSource, pTileCache, this)
        val scaleProperty: Int = Resources.getInstance().getIntProperty(FreeMind.SCALING_FACTOR_PROPERTY, 100)
        mCursorLength = 15 * scaleProperty / 100
        mStroke = BasicStroke((2 * scaleProperty / 100).toFloat())
        freeMindMapController = FreeMindMapController(this,
                pMindMapController, pMapDialog, pMapHook)
        val updateCursorAction: Action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                mShowCursor = !mShowCursor
                repaint()
            }
        }
        Timer(1000, updateCursorAction).start()
        isFocusable = false
    }

    var isUseCursor: Boolean
        get() = mUseCursor
        set(pUseCursor) {
            mUseCursor = pUseCursor
            repaint()
        }
    var cursorPosition: Coordinate?
        get() = mCursorPosition
        set(pCursorPosition) {
            mCursorPosition = pCursorPosition
            repaint()
        }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openstreetmap.gui.jmapviewer.JMapViewer#paintComponent(java.awt.Graphics
	 * )
	 */
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (g is Graphics2D) {
            val g2d = g
            val oldStroke = g2d.stroke
            val oldColor = g2d.color
            // do cursor
            if (mUseCursor && mShowCursor) {
                val position = getMapPosition(mCursorPosition)
                if (position != null) {
                    val size_h = mCursorLength
                    g2d.stroke = mStroke
                    g2d.color = Color.RED
                    g2d.drawLine(position.x - size_h, position.y, position.x
                            + size_h, position.y)
                    g2d.drawLine(position.x, position.y - size_h, position.x,
                            position.y + size_h)
                }
            }
            if (isDrawRectangular) {
                g2d.color = Color.BLACK
                g2d.stroke = mRectangularStroke
                val r = getRectangle(mRectangularStart, mRectangularEnd)
                if (r != null) {
                    g2d.drawRect(r.x, r.y, r.width, r.height)
                }
            }
            g2d.color = oldColor
            g2d.stroke = oldStroke
        }
    }

    fun getRectangle(rectangularStart: Coordinate?,
                     rectangularEnd: Coordinate?): Rectangle? {
        val positionStart = getMapPosition(rectangularStart)
        val positionEnd = getMapPosition(rectangularEnd)
        var r: Rectangle? = null
        if (positionStart != null && positionEnd != null) {
            val x = Math.min(positionStart.x, positionEnd.x)
            val y = Math.min(positionStart.y, positionEnd.y)
            val width = Math.abs(positionStart.x - positionEnd.x)
            val height = Math.abs(positionStart.y - positionEnd.y)
            r = Rectangle(x, y, width, height)
        }
        return r
    }

    override fun getTileController(): TileController {
        return tileController
    }

    /* (non-Javadoc)
	 * @see org.openstreetmap.gui.jmapviewer.JMapViewer#initializeZoomSlider()
	 */
    override fun initializeZoomSlider() {
        super.initializeZoomSlider()
        //focus
        zoomSlider.isFocusable = false
        zoomInButton.isFocusable = false
        zoomOutButton.isFocusable = false
    }

    /**
     * @param pHideFoldedNodes
     */
    var isHideFoldedNodes: Boolean
        get() = mHideFoldedNodes
        set(pHideFoldedNodes) {
            mHideFoldedNodes = pHideFoldedNodes
            repaint()
        }

    fun setRectangular(pRectangularStart: Coordinate?, pRectangularEnd: Coordinate?) {
        mRectangularStart = pRectangularStart
        mRectangularEnd = pRectangularEnd
    }

    /**
     * @param pMapCenterLatitude
     * @param pMapCenterLongitude
     * @param pZoom
     */
    fun setDisplayPositionByLatLon(pMapCenterLatitude: Double,
                                   pMapCenterLongitude: Double, pZoom: Int) {
        super.setDisplayPosition(Coordinate(pMapCenterLatitude, pMapCenterLongitude), pZoom)
    }
}