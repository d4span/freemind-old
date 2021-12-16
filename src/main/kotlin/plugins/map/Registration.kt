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
import java.awt.image.ImageObserver
import java.awt.image.BufferedImage
import java.awt.Graphics2D
import java.awt.BasicStroke
import javax.imageio.ImageIO
import java.awt.image.RenderedImage
import freemind.modes.ModeController
import freemind.modes.MindMap
import freemind.extensions.HookRegistration
import freemind.modes.mindmapmode.actions.xml.ActorXml
import freemind.controller.MenuItemEnabledListener
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
import freemind.common.XmlBindingTools
import java.net.URLEncoder
import java.text.MessageFormat
import kotlin.Throws
import java.net.MalformedURLException
import freemind.common.FreeMindTask
import freemind.common.FreeMindTask.ProgressDescription
import plugins.map.FreeMindMapController.AddSearchResultsToMapTask
import plugins.map.FreeMindMapController.TileSourceStore
import plugins.map.FreeMindMapController.HttpMapnik
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.CycleMap
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
import org.apache.batik.transcoder.TranscoderOutput
import java.awt.Dimension
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import javax.print.attribute.standard.MediaSizeName
import javax.print.attribute.standard.MediaSize
import org.apache.batik.dom.GenericDOMImplementation
import org.apache.batik.util.SVGConstants
import org.apache.batik.svggen.SVGGeneratorContext
import org.apache.batik.svggen.SVGGeneratorContext.GraphicContextDefaults
import freemind.extensions.ModeControllerHookAdapter
import javax.help.HelpSet
import javax.help.HelpBroker
import plugins.latex.LatexNodeHook
import atp.sHotEqn
import plugins.latex.JZoomedHotEqn
import java.awt.geom.AffineTransform
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter
import plugins.script.ScriptEditorPanel.ScriptModel
import plugins.script.ScriptEditorPanel
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage
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
import java.lang.SecurityException
import plugins.search.FileSearchModel
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.ScoreDoc
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.TopScoreDocCollector
import org.apache.lucene.store.RAMDirectory
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.document.StringField
import org.apache.lucene.analysis.standard.StandardAnalyzer
import plugins.search.ISearchController
import plugins.search.SearchViewPanel
import java.awt.Insets
import java.awt.HeadlessException
import kotlin.jvm.JvmStatic
import java.lang.ClassCastException
import plugins.collaboration.jabber.view.SpringUtilities
import plugins.collaboration.jabber.view.MapSharingWizardView
import plugins.collaboration.jabber.mindmap.MapSharingController
import freemind.modes.mindmapmode.actions.xml.ActionFilter
import plugins.collaboration.jabber.mindmap.JabberSender
import freemind.controller.actions.generated.instance.CollaborationAction
import freemind.controller.actions.generated.instance.CompoundAction
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
import freemind.controller.actions.generated.instance.CollaborationActionBase
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
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*
import java.util.Timer
import java.util.logging.Logger
import javax.swing.*

class Registration(controller: ModeController, map: MindMap?) : HookRegistration, ActorXml, TileLoaderListener, MenuItemEnabledListener {
    /**
     *
     * Clean the file cache periodically.
     *
     * @author foltin
     * @date 27.04.2012
     */
    inner class CachePurger
    /**
     * @param pCacheDirectory
     * @param pCacheMaxAge
     */(private val mCacheDirectory: File, private val mCacheMaxAge: Long) : TimerTask() {
        /**
         * @author foltin
         * @date 27.04.2012
         */
        private inner class AgeFilter(private val mYoungestFileToAccept: Long) : FileFilter {
            override fun accept(pPathname: File): Boolean {
                return (pPathname.name.endsWith(".tags")
                        && pPathname.lastModified() <= mYoungestFileToAccept)
            }
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
        override fun run() {
            // the jobs must not overtake themselves.
            synchronized(mCachePurgerSemaphore) {
                if (mCachePurgerSemaphore.value > 0) {
                    return
                }
                mCachePurgerSemaphore.value = 1
            }
            try {
                logger.info("Start purging for $mCacheDirectory")
                if (mCacheDirectory.exists()) {
                    val cacheDirectories = mCacheDirectory.listFiles()
                    for (i in cacheDirectories.indices) {
                        val cacheDirectory = cacheDirectories[i]
                        purgeDirectory(cacheDirectory)
                    }
                }
                logger.info("Finished purging")
            } finally {
                mCachePurgerSemaphore.value = 0
            }
        }

        /**
         * @param pCacheDirectory
         */
        private fun purgeDirectory(pCacheDirectory: File) {
            logger.fine("Start purging for subdir $pCacheDirectory")
            val listTagFiles = pCacheDirectory.listFiles(AgeFilter(
                    System.currentTimeMillis() - mCacheMaxAge)) ?: return
            for (i in listTagFiles.indices) {
                val tagFile = listTagFiles[i]
                val imageFile = File(tagFile.path.replace(".tags",
                        ".png"))
                try {
                    logger.finest("Deleting $tagFile")
                    logger.finest("Deleting $imageFile")
                    tagFile.delete()
                    imageFile.delete()
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
            }
        }
    }

    /*
	 * Collects MapNodePositionHolder. This is necessary to be able to display
	 * them all efficiently.
	 */
    private val mMapNodePositionHolders = HashSet<MapNodePositionHolder>()
    private val mMapNodePositionListeners = HashSet<MapNodePositionListener>()
    private val controller: MindMapController
    private val logger: Logger
    private val mTileCache: MemoryTileCache
    var mapDialog: MapDialog? = null
    private val mOptionContributor: MapDialogPropertyContributor
    private val mCachePurgerSemaphore = Tools.IntHolder(0)

    private class MapDialogPropertyContributor(modeController: MindMapController?) : FreemindPropertyContributor {
        override fun getControls(pTextTranslator: TextTranslator?): List<PropertyControl?>? {
            val controls = Vector<PropertyControl?>()
            controls.add(NewTabProperty(
                    "plugins/map/MapDialog.properties_MapDialogTabName"))
            controls.add(SeparatorProperty(
                    "plugins/map/MapDialog.properties_PatternSeparatorName"))
            controls.add(BooleanProperty("node_map_show_tooltip.tooltip",
                    "node_map_show_tooltip"))
            controls.add(DontShowNotificationProperty(
                    "resources_search_for_node_text_without_question.tooltip",
                    FreeMind.RESOURCES_SEARCH_FOR_NODE_TEXT_WITHOUT_QUESTION))
            return controls
        }
    }

    override fun deRegister() {
        OptionPanel.removeContributor(mOptionContributor)
        controller.actionRegistry.deregisterActor(doActionClass)
    }

    override fun register() {
        OptionPanel.addContributor(mOptionContributor)
        controller.actionRegistry.registerActor(this, doActionClass)
    }

    fun registerMapNode(pMapNodePositionHolder: MapNodePositionHolder) {
        mMapNodePositionHolders.add(pMapNodePositionHolder)
        for (listener in mMapNodePositionListeners) {
            try {
                listener.registerMapNode(pMapNodePositionHolder)
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
    }

    val mapNodePositionHolders: Set<MapNodePositionHolder>
        get() = Collections.unmodifiableSet(mMapNodePositionHolders)

    fun deregisterMapNode(pMapNodePositionHolder: MapNodePositionHolder) {
        mMapNodePositionHolders.remove(pMapNodePositionHolder)
        for (listener in mMapNodePositionListeners) {
            try {
                listener.deregisterMapNode(pMapNodePositionHolder)
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
    }

    fun registerMapNodePositionListener(
            pMapNodePositionListener: MapNodePositionListener) {
        mMapNodePositionListeners.add(pMapNodePositionListener)
    }

    fun deregisterMapNodePositionListener(
            pMapNodePositionListener: MapNodePositionListener) {
        mMapNodePositionListeners.remove(pMapNodePositionListener)
    }

    fun createTileLoader(mMap: TileLoaderListener?): OsmTileLoader {
        var loader: OsmTileLoader? = null
        val tileCacheClass: String = Resources.getInstance().getProperty(
                MapDialog.Companion.TILE_CACHE_CLASS)
        if (safeEquals(tileCacheClass, "file")) {
            val cacheDir = cacheDirectory
            try {
                val osmFileCacheTileLoader = OsmFileCacheTileLoader(
                        mMap, cacheDir)
                loader = osmFileCacheTileLoader
                val maxFileAge = cacheMaxAge
                logger.info("Setting cache max age to " + (maxFileAge
                        / OsmFileCacheTileLoader.FILE_AGE_ONE_DAY) + " days.")
                osmFileCacheTileLoader.setCacheMaxFileAge(maxFileAge)
            } catch (e1: Exception) {
                Resources.getInstance().logException(e1)
            }
        }
        if (loader == null) {
            logger.info("Using osm tile loader")
            loader = OsmTileLoader(mMap)
        }
        val freemindVersion = controller.frame
                .freemindVersion
        loader.headers["User-agent"] = "FreeMind $freemindVersion"
        return loader
    }

    protected val cacheMaxAge: Long
        protected get() = Resources.getInstance().getLongProperty(
                MapDialog.Companion.TILE_CACHE_MAX_AGE,
                OsmFileCacheTileLoader.FILE_AGE_ONE_WEEK)
    protected val cacheDirectory: File
        protected get() {
            var directory: String = Resources.getInstance().getProperty(
                    MapDialog.Companion.FILE_TILE_CACHE_DIRECTORY)
            if (directory.startsWith("%/")) {
                directory = (Resources.getInstance().getFreemindDirectory()
                        + File.separator + directory.substring(2))
            }
            val cacheDir = File(directory)
            logger.info("Trying to use file cache tile loader with dir "
                    + directory)
            return cacheDir
        }

    /**
     * Set map position. Is undoable.
     *
     * @param pTileSource
     */
    fun changePosition(pHolder: MapNodePositionHolder,
                       pPosition: Coordinate?, pMapCenter: Coordinate?, pZoom: Int,
                       pTileSource: String?) {
        val node: MindMapNode = pHolder.getNode()
        val doAction = createPlaceNodeXmlActionAction(node,
                pPosition, pMapCenter, pZoom, pTileSource)
        val undoAction = createPlaceNodeXmlActionAction(node,
                pHolder.position, pHolder.mapCenter,
                pHolder.zoom, pHolder.tileSource)
        val actionFactory = controller.actionRegistry
        actionFactory.doTransaction(PLUGINS_MAP_NODE_POSITION, ActionPair(
                doAction, undoAction))
    }

    /**
     * @param pNode
     * @param pPosition
     * @param pMapCenter
     * @param pZoom
     * @param pTileSource
     * @return
     */
    private fun createPlaceNodeXmlActionAction(
            pNode: MindMapNode?, pPosition: Coordinate?, pMapCenter: Coordinate?,
            pZoom: Int, pTileSource: String?): PlaceNodeXmlAction {
        logger.info("Setting position of node $pNode")
        val action = PlaceNodeXmlAction()
        action.node = controller.getNodeID(pNode)
        action.cursorLatitude = pPosition!!.lat
        action.cursorLongitude = pPosition.lon
        action.mapCenterLatitude = pMapCenter!!.lat
        action.mapCenterLongitude = pMapCenter.lon
        action.zoom = pZoom
        action.tileSource = pTileSource
        return action
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.mindmapmode.actions.xml.ActorXml#act(freemind.controller
	 * .actions.generated.instance.XmlAction)
	 */
    override fun act(pAction: XmlAction?) {
        if (pAction is PlaceNodeXmlAction) {
            val placeAction = pAction
            val node: MindMapNode = controller.getNodeFromID(placeAction.node)
            val hook: MapNodePositionHolder = MapNodePositionHolder.Companion.getHook(node)
            if (hook != null) {
                hook.mapCenter = Coordinate(placeAction
                        .mapCenterLatitude, placeAction
                        .mapCenterLongitude)
                hook.position = Coordinate(
                        placeAction.cursorLatitude, placeAction
                        .cursorLongitude)
                hook.zoom = placeAction.zoom
                hook.tileSource = placeAction.tileSource
                hook.setTooltip()
                // TODO: Only, if values really changed.
                controller.nodeChanged(node)
            } else {
                throw IllegalArgumentException(
                        "MapNodePositionHolder to node id "
                                + placeAction.node + " not found.")
            }
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.mindmapmode.actions.xml.ActorXml#getDoActionClass()
	 */
    override val doActionClass: Class<PlaceNodeXmlAction>
        get() = PlaceNodeXmlAction::class.java

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener#
	 * getTileCache()
	 */
    override fun getTileCache(): TileCache {
        return mTileCache
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener#
	 * tileLoadingFinished(org.openstreetmap.gui.jmapviewer.Tile, boolean)
	 */
    override fun tileLoadingFinished(pTile: Tile, pSuccess: Boolean) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.MenuItemEnabledListener#isEnabled(javax.swing.JMenuItem
	 * , javax.swing.Action)
	 */
    override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
        val hookName = (pAction as NodeHookAction?)!!.hookName
        logger.fine("Enabled for $hookName")
        if (SearchInMapForNodeTextAction.Companion.NODE_CONTEXT_PLUGIN_NAME
                == hookName) {
            return true
        }
        if (ShowMapToNodeAction.Companion.NODE_CONTEXT_PLUGIN_NAME == hookName || AddLinkToMapAction.Companion.NODE_CONTEXT_PLUGIN_NAME == hookName || (RemoveMapToNodeAction.Companion.NODE_CONTEXT_PLUGIN_NAME
                        == hookName) || (AddMapImageToNodeAction.Companion.NODE_CONTEXT_PLUGIN_NAME
                        == hookName)) {
            for (node in controller.selecteds) {
                val hook: MapNodePositionHolder = MapNodePositionHolder.Companion.getHook(node)
                if (hook != null) {
                    return true
                }
            }
        }
        return false
    }

    interface NodeVisibilityListener {
        fun nodeVisibilityChanged(
                pMapNodePositionHolder: MapNodePositionHolder?, pVisible: Boolean)
    }

    private val mNodeVisibilityListeners = HashSet<NodeVisibilityListener>()

    init {
        this.controller = controller as MindMapController
        logger = controller.frame.getLogger(this.javaClass.name)
        mTileCache = MemoryTileCache()
        mOptionContributor = MapDialogPropertyContributor(this.controller)
        synchronized(sTimerSemaphore) {
            if (sTimer == null) {
                // only once in the system
                sTimer = Timer()
                val purgeTime: Long = Resources.getInstance().getLongProperty(
                        MapDialog.Companion.TILE_CACHE_PURGE_TIME,
                        MapDialog.Companion.TILE_CACHE_PURGE_TIME_DEFAULT)
                sTimer!!.schedule(CachePurger(cacheDirectory,
                        cacheMaxAge), purgeTime, purgeTime)
            }
        }
    }

    fun registerNodeVisibilityListener(
            pNodeVisibilityListener: NodeVisibilityListener) {
        mNodeVisibilityListeners.add(pNodeVisibilityListener)
    }

    fun deregisterNodeVisibilityListener(
            pNodeVisibilityListener: NodeVisibilityListener) {
        mNodeVisibilityListeners.remove(pNodeVisibilityListener)
    }

    /**
     * @param pVisible
     * is true, when a node is visible now.
     * @param pMapNodePositionHolder
     */
    fun fireNodeVisibilityChanged(pVisible: Boolean,
                                  pMapNodePositionHolder: MapNodePositionHolder?) {
        for (listener in mNodeVisibilityListeners) {
            try {
                listener.nodeVisibilityChanged(pMapNodePositionHolder, pVisible)
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
    }

    companion object {
        private val PLUGINS_MAP_NODE_POSITION = MapNodePositionHolder::class.java
                .name
        private var sTimer: Timer? = null
        private const val sTimerSemaphore = false
    }
}