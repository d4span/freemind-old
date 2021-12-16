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
import org.openstreetmap.gui.jmapviewer.interfaces.JMapViewerEventListener
import freemind.controller.MapModuleManager.MapModuleChangeObserver
import plugins.map.MapNodePositionHolder.MapNodePositionListener
import freemind.modes.ModeController.NodeSelectionListener
import plugins.map.Registration.NodeVisibilityListener
import plugins.map.JCursorMapViewer
import freemind.modes.mindmapmode.MindMapController
import plugins.map.MapNodePositionHolder
import plugins.map.MapMarkerLocation
import plugins.map.MapDialog.ResultTableModel
import org.openstreetmap.gui.jmapviewer.Coordinate
import freemind.common.TextTranslator
import javax.swing.table.AbstractTableModel
import plugins.map.FreeMindMapController.CursorPositionListener
import plugins.map.MapDialog
import freemind.controller.actions.generated.instance.Place
import plugins.map.MapSearchMarkerLocation
import org.openstreetmap.gui.jmapviewer.OsmMercator
import plugins.map.FreeMindMapController
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.Mapnik
import org.openstreetmap.gui.jmapviewer.OsmTileLoader
import freemind.common.ScalableJTable
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent
import freemind.controller.actions.generated.instance.MapWindowConfigurationStorage
import freemind.controller.actions.generated.instance.TableColumnSetting
import plugins.map.FreeMindMapController.PositionHolder
import freemind.controller.actions.generated.instance.MapLocationStorage
import java.lang.Runnable
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent
import freemind.view.MapModule
import freemind.modes.MindMapNode
import java.awt.image.ImageObserver
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.awt.image.RenderedImage
import org.openstreetmap.gui.jmapviewer.TileController
import freemind.modes.ModeController
import freemind.modes.MindMap
import freemind.extensions.HookRegistration
import freemind.modes.mindmapmode.actions.xml.ActorXml
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener
import freemind.controller.MenuItemEnabledListener
import plugins.map.Registration.CachePurger.AgeFilter
import org.openstreetmap.gui.jmapviewer.MemoryTileCache
import plugins.map.Registration.MapDialogPropertyContributor
import freemind.preferences.FreemindPropertyContributor
import freemind.common.PropertyControl
import freemind.preferences.layout.OptionPanel.NewTabProperty
import freemind.common.SeparatorProperty
import freemind.common.BooleanProperty
import freemind.common.DontShowNotificationProperty
import freemind.preferences.layout.OptionPanel
import org.openstreetmap.gui.jmapviewer.OsmFileCacheTileLoader
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
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker
import plugins.map.MapMarkerBase
import org.openstreetmap.gui.jmapviewer.Layer
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker.STYLE
import org.openstreetmap.gui.jmapviewer.JMapViewer
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource
import plugins.map.JCursorMapViewer.ScaledTile
import plugins.map.JCursorMapViewer.ScalableTileController
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter
import org.openstreetmap.gui.jmapviewer.JMapController
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
import java.lang.RuntimeException
import freemind.controller.actions.generated.instance.Searchresults
import freemind.controller.actions.generated.instance.Reversegeocode
import java.lang.StringBuilder
import freemind.common.XmlBindingTools
import java.text.MessageFormat
import kotlin.Throws
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
import plugins.collaboration.socket.ClientCommunication
import freemind.controller.actions.generated.instance.CollaborationPublishNewMap
import plugins.collaboration.socket.CommunicationBase
import freemind.extensions.PermanentNodeHook
import plugins.collaboration.socket.SocketMaster
import freemind.extensions.DontSaveMarker
import plugins.collaboration.socket.TerminateableThread
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
import freemind.view.mindmapview.NodeView
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
import java.awt.*
import java.awt.event.*
import java.io.*
import java.lang.Exception
import java.net.*
import java.util.*
import java.util.logging.Logger
import java.util.regex.Pattern
import javax.swing.*
import javax.swing.Timer

//License: GPL. Copyright 2008 by Jan Peter Stotz
/**
 * Default map controller which implements map moving by pressing the right
 * mouse button and zooming by double click or by mouse wheel.
 *
 * @author Jan Peter Stotz
 *
 * FreeMind Extensions: - Move with button 1 (consistency with FreeMind
 * UI) OK - Single click for Set Cursor OK - Mouse Wheel: Zoom OK -
 * Control-Mouse Wheel: ? - (Right click +) Menu: popup menu mit * If
 * right click, then the cursor is set to that position (consistency
 * with FM-UI) * Place node(s) ==> the node gets a
 * [MapMarkerLocation] here. The position, the position of the map
 * and the zoom is stored in the node. *
 *
 * Node Extra Menu Items: * Show node(s) in Map ==> Chooses the best
 * view for the nodes and selects them.
 *
 *
 * FIXME: On undo place node, the position is gone. (Undo action
 * contains the initial zeros, I guess).
 */
class FreeMindMapController(map: JMapViewer?,
                            private val mMindMapController: MindMapController?, private val mMapDialog: JDialog?,
                            private val mMapHook: MapDialog) : JMapController(map), MouseListener, MouseMotionListener, MouseWheelListener, ActionListener, KeyListener {
    /**
     * @author foltin
     * @date 27.07.2012
     */
    interface CursorPositionListener {
        fun cursorPositionChanged(pCursorPosition: Coordinate?)
    }

    val popupMenu = JPopupMenu()
    private var lastDragPoint: Point? = null
    private var mDragStartingPoint: Point? = null

    /**
     * Enables or disables that the map pane can be moved using the mouse.
     *
     * @param movementEnabled
     */
    var isMovementEnabled = true
    private var isMoving = false
    var isClickEnabled = true
    private var movementMouseButton = MouseEvent.BUTTON1
    private var movementMouseButtonMask = MouseEvent.BUTTON1_DOWN_MASK
    var isWheelZoomEnabled = true
    private var mContextPopupMenu: JPopupMenu? = null
    private var mCurrentPopupPositionHolder: MapNodePositionHolder? = null
    private var isMapNodeMoving = false
    private var mMapNodeMovingSource: MapNodePositionHolder? = null
    private val mMouseHitsNodeTimer: Timer
    private var mIsRectangularSelect = false
    private var mRectangularStart: Coordinate? = null
    val positionHolderVector = Vector<PositionHolder>()

    /**
     * Marks the index of the current position or -1 if none.
     */
    private var mPositionHolderIndex = -1

    class TileSourceStore(var mTileSource: TileSource, var mLayerName: String)
    class TransportMap : AbstractOsmTileSource("OSM Transport Map", PATTERN) {
        private var SERVER_NUM = 0
        override fun getBaseUrl(): String {
            val url = String.format(baseUrl,
                    *arrayOf<Any>(SERVER[SERVER_NUM]))
            SERVER_NUM = (SERVER_NUM + 1) % SERVER.size
            return url
        }

        override fun getMaxZoom(): Int {
            return 18
        }

        override fun getTileUpdate(): TileUpdate {
            return TileUpdate.LastModified
        }

        companion object {
            // http://b.tile2.opencyclemap.org/transport/14/8800/5373.png
            private const val PATTERN = "http://%s.tile2.opencyclemap.org/transport"
            private val SERVER = arrayOf("a", "b", "c")
        }
    }

    class MapQuestOpenMap : AbstractOsmTileSource("OSM MapQuest.Open Map", PATTERN) {
        private var SERVER_NUM = 0
        override fun getBaseUrl(): String {
            val url = String.format(baseUrl,
                    *arrayOf<Any>(SERVER[SERVER_NUM]))
            SERVER_NUM = (SERVER_NUM + 1) % SERVER.size
            return url
        }

        override fun getMaxZoom(): Int {
            return 18
        }

        override fun getTileUpdate(): TileUpdate {
            return TileUpdate.LastModified
        }

        companion object {
            // http://otile1.mqcdn.com/tiles/1.0.0/osm/14/8800/5374.png
            private const val PATTERN = "http://otile%s.mqcdn.com/tiles/1.0.0/osm"
            private val SERVER = arrayOf("1", "2", "3", "4")
        }
    }

    private class HttpMapnik : Mapnik() {
        init {
            baseUrl = baseUrl.replaceFirst("^https".toRegex(), "http")
        }
    }

    private inner class MapEditTextFieldControl(private val mNodeView: NodeView?,
                                                private val mNewNode: MindMapNode?, private val mTargetNode: MindMapNode?,
                                                private val mIsEditOfExistingNode: Boolean) : EditControl {
        override fun cancel() {
            if (!mIsEditOfExistingNode) {
                mMindMapController!!.view!!.selectAsTheOnlyOneSelected(
                        mNodeView)
                mMindMapController.cut(getVectorWithSingleElement(mNewNode))
                mMindMapController.select(mMindMapController
                        .getNodeView(mTargetNode))
            }
            endEdit()
        }

        override fun ok(newText: String?) {
            mMindMapController!!.setNodeText(mNewNode, newText)
            placeNode(mNewNode)
            endEdit()
        }

        private fun endEdit() {
            setMouseControl(true)
            mMindMapController!!.isBlocked = false
            mMapDialog!!.requestFocus()
        }

        override fun split(newText: String?, position: Int) {}
    }

    private inner class MapEditNoteTextField(pNode: NodeView?, pText: String?,
                                             pFirstEvent: KeyEvent?, pController: ModeController?,
                                             pEditControl: EditControl, pParent: JComponent, private val mPoint: Point) : EditNodeTextField(pNode!!, pText!!, pFirstEvent!!, pController!!, pEditControl,
            pParent, pParent) {
        override fun setTextfieldLoaction(pMPoint: Point?) {
            textfield!!.location = mPoint
        }

        override fun addTextfield() {
            // add to front to make it visible over the map.
            mParent!!.add(textfield)
        }
    }

    /**
     * @author foltin
     * @date 16.11.2011
     */
    inner class ChangeTileSource
    /**
     * @param pSource
     */(private val mSource: TileSource) : AbstractAction(Resources.getInstance().getText(
            "map_ChangeTileSource_" + getTileSourceName(mSource))), MenuItemSelectedListener {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
        override fun actionPerformed(pE: ActionEvent) {
            map.setTileSource(mSource)
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * freemind.controller.MenuItemSelectedListener#isSelected(javax.swing
		 * .JMenuItem, javax.swing.Action)
		 */
        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return tileSource === mSource
        }
    }

    /**
     * @author foltin
     * @date 31.10.2011
     */
    private inner class PlaceNodeAction : AbstractAction(getText("MapControllerPopupDialog.place"),
            getMapLocationIcon()) {
        override fun actionPerformed(actionEvent: ActionEvent) {
            placeNode(mMindMapController!!.selected)
        }
    }

    /**
     * @author foltin
     * @date 31.10.2011
     */
    private inner class ShowNodeAction : AbstractAction(getText("MapControllerPopupDialog.show_nodes")) {
        override fun actionPerformed(actionEvent: ActionEvent) {
            showSelectedNodes()
        }
    }

    /**
     * @author foltin
     * @date 31.10.2011
     */
    private abstract inner class MoveAction
    /**
     * @param pText
     */
    (pText: String?) : AbstractAction(pText) {
        override fun actionPerformed(actionEvent: ActionEvent) {
            if (!searchForNearestNode(false)) {
                searchForNearestNode(true)
            }
        }

        /**
         * @param alternative
         * @return true, if a node was found
         */
        protected fun searchForNearestNode(alternative: Boolean): Boolean {
            var returnValue = false
            val cursorPosition: Coordinate = map.getCursorPosition()
            // get map marker locations:
            val mapNodePositionHolders = HashSet(mMapHook.mapNodePositionHolders)
            logger.fine("Before removal " + mapNodePositionHolders.size
                    + " elements")
            // take only those elements in the correct quadrant (eg. -45° -
            // +45°) which are not identical to the current
            val it = mapNodePositionHolders.iterator()
            while (it.hasNext()) {
                val holder = it.next()
                val pointPosition = holder.getPosition()
                val inDestinationQuadrant = destinationQuadrantCheck(
                        cursorPosition, pointPosition, alternative)
                if (!inDestinationQuadrant
                        || safeEquals(pointPosition, cursorPosition)) {
                    it.remove()
                }
            }
            logger.fine("After removal " + mapNodePositionHolders.size
                    + " elements")
            // now, we have all points on the left angle (eg. -45° to 45°) and
            // search
            // for the nearest
            var nearest: MapNodePositionHolder? = null
            var distance = Double.MAX_VALUE
            for (holder in mapNodePositionHolders) {
                val newDist = dist(holder.getPosition(), cursorPosition)
                logger.fine("Position $holder is $newDist")
                if (newDist < distance) {
                    distance = newDist
                    nearest = holder
                }
            }
            if (nearest != null) {
                selectNode(nearest.getNode())
                // don't change the zoom
                setCursorPosition(nearest, map.zoom)
                returnValue = true
            }
            return returnValue
        }

        fun destinationQuadrantCheck(cursorPosition: Coordinate?,
                                     pointPosition: Coordinate?, alternative: Boolean): Boolean {
            val mapZoomMax = maxZoom
            val x1 = OsmMercator.LonToX(cursorPosition!!.lon,
                    mapZoomMax).toInt()
            val y1 = OsmMercator.LatToY(cursorPosition.lat,
                    mapZoomMax).toInt()
            val x2 = OsmMercator.LonToX(pointPosition!!.lon,
                    mapZoomMax).toInt()
            val y2 = OsmMercator.LatToY(pointPosition.lat,
                    mapZoomMax).toInt()
            return destinationQuadrantCheck(x1, y1, x2, y2, alternative)
        }

        /**
         * If no point was found from the destinationQuadrantCheck, here,
         * alternative = true is tried
         */
        abstract fun destinationQuadrantCheck(x1: Int, y1: Int,
                                              x2: Int, y2: Int, alternative: Boolean): Boolean

        /**
         * @param pPointPosition
         * @param pCursorPosition
         * @return
         */
        private fun safeEquals(p1: Coordinate?, p2: Coordinate?): Boolean {
            return p1 != null && p2 != null && p1.lon == p2.lon && p1
                    .lat == p2.lat || p1 == null && p2 == null
        }

        /**
         * @param pPosition
         * @param pCursorPosition
         * @return
         */
        private fun dist(p1: Coordinate?, p2: Coordinate?): Double {
            return OsmMercator.getDistance(p1!!.lat, p1.lon,
                    p2!!.lat, p2.lon)
        }
    }

    private inner class MoveLeftAction
    /**
     *
     */
        : MoveAction(getText("MapControllerPopupDialog.moveLeft")) {
        override fun destinationQuadrantCheck(x1: Int, y1: Int, x2: Int, y2: Int,
                                              alternative: Boolean): Boolean {
            return if (alternative) x2 < x1 else x2 < x1 && Math.abs(y2 - y1) < Math.abs(x2 - x1)
        }
    }

    private inner class MoveRightAction
    /**
     *
     */
        : MoveAction(getText("MapControllerPopupDialog.moveRight")) {
        override fun destinationQuadrantCheck(x1: Int, y1: Int, x2: Int, y2: Int,
                                              alternative: Boolean): Boolean {
            return if (alternative) x2 > x1 else x2 > x1 && Math.abs(y2 - y1) < Math.abs(x2 - x1)
        }
    }

    private inner class MoveUpAction
    /**
     *
     */
        : MoveAction(getText("MapControllerPopupDialog.moveUp")) {
        override fun destinationQuadrantCheck(x1: Int, y1: Int, x2: Int, y2: Int,
                                              alternative: Boolean): Boolean {
            return if (alternative) y2 < y1 else y2 < y1 && Math.abs(y2 - y1) > Math.abs(x2 - x1)
        }
    }

    private inner class MoveDownAction
    /**
     *
     */
        : MoveAction(getText("MapControllerPopupDialog.moveDown")) {
        override fun destinationQuadrantCheck(x1: Int, y1: Int, x2: Int, y2: Int,
                                              alternative: Boolean): Boolean {
            return if (alternative) y2 > y1 else y2 > y1 && Math.abs(y2 - y1) > Math.abs(x2 - x1)
        }
    }

    private inner class MoveForwardAction : AbstractAction(getText("MapControllerPopupDialog.moveForward")), MenuItemEnabledListener {
        override fun actionPerformed(pE: ActionEvent) {
            if (isEnabledCheck()) {
                val posHolder = positionHolderVector[positionHolderIndex + 1] as PositionHolder
                map.setCursorPosition(posHolder.coordinate)
                map.setDisplayPosition(Coordinate(posHolder.lat,
                        posHolder.lon), posHolder.zoom)
                positionHolderIndex = positionHolderIndex + 1
            }
        }

        protected val isEnabledCheck: Boolean
            protected get() = (positionHolderIndex >= 0
                    && positionHolderIndex < positionHolderVector
                    .size - 1)

        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            return isEnabledCheck()
        }
    }

    private inner class MoveBackwardAction : AbstractAction(getText("MapControllerPopupDialog.moveBackward")), MenuItemEnabledListener {
        override fun actionPerformed(pE: ActionEvent) {
            if (isEnabledCheck()) {
                val posHolder = positionHolderVector[positionHolderIndex - 1] as PositionHolder
                map.setCursorPosition(posHolder.coordinate)
                map.setDisplayPosition(Coordinate(posHolder.lat,
                        posHolder.lon), posHolder.zoom)
                positionHolderIndex = positionHolderIndex - 1
            }
        }

        protected val isEnabledCheck: Boolean
            protected get() = positionHolderIndex > 0

        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            return isEnabledCheck()
        }
    }

    class PositionHolder(var lat: Double, var lon: Double, var zoom: Int) {
        override fun toString(): String {
            return ("PositionHolder [lat=" + lat + ", lon=" + lon + ", zoom="
                    + zoom + "]")
        }

        val coordinate: Coordinate
            get() = Coordinate(lat, lon)

        override fun hashCode(): Int {
            val prime = 31
            var result = 1
            var temp: Long
            temp = java.lang.Double.doubleToLongBits(lat)
            result = prime * result + (temp xor (temp ushr 32)).toInt()
            temp = java.lang.Double.doubleToLongBits(lon)
            result = prime * result + (temp xor (temp ushr 32)).toInt()
            result = prime * result + zoom
            return result
        }

        override fun equals(obj: Any?): Boolean {
            if (this === obj) return true
            if (obj == null) return false
            if (javaClass != obj.javaClass) return false
            val other = obj as PositionHolder
            if (java.lang.Double.doubleToLongBits(lat) != java.lang.Double
                            .doubleToLongBits(other.lat)) return false
            if (java.lang.Double.doubleToLongBits(lon) != java.lang.Double
                            .doubleToLongBits(other.lon)) return false
            return if (zoom != other.zoom) false else true
        }
    }

    private inner class MoveHomeAction : AbstractAction(getText("MapControllerPopupDialog.MoveHome")), MenuItemEnabledListener {
        override fun actionPerformed(pE: ActionEvent) {
            val posHolder: PositionHolder = getPosHolder() ?: return
            setZoom(posHolder.zoom)
            val coordinates = posHolder.coordinate
            setCursorPosition(coordinates)
        }

        val posHolder: PositionHolder?
            get() = try {
                val homeProperty: String = Resources.getInstance().getProperty(
                        NODE_MAP_HOME_PROPERTY)
                if (homeProperty == null || homeProperty.isEmpty()) {
                    return null
                }
                val splitResult = homeProperty.split(":").toTypedArray()
                if (splitResult.size != 3) {
                    return null
                }
                val lat = splitResult[0].toDouble()
                val lon = splitResult[1].toDouble()
                val zoom = splitResult[2].toInt()
                PositionHolder(lat, lon, zoom)
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
                null
            }

        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            return getPosHolder() != null
        }
    }

    private inner class SetHomeAction
    /**
     *
     */
        : AbstractAction(getText("MapControllerPopupDialog.SetHome")) {
        override fun actionPerformed(pE: ActionEvent) {
            val cursorPosition: Coordinate = map.getCursorPosition()
            val propertyValue = (cursorPosition.lat.toString() + ":"
                    + cursorPosition.lon + ":" + map.zoom)
            mMindMapController!!.controller!!.setProperty(
                    NODE_MAP_HOME_PROPERTY, propertyValue)
        }
    }

    private inner class SetDisplayToFitMapMarkers : AbstractAction(getText("MapControllerPopupDialog.SetDisplayToFitMapMarkers")) {
        override fun actionPerformed(pE: ActionEvent) {
            map.setDisplayToFitMapMarkers()
        }
    }

    private inner class ShowMapMarker : AbstractAction(getText("MapControllerPopupDialog.ShowMapMarker")), MenuItemSelectedListener {
        override fun actionPerformed(pE: ActionEvent) {
            map.setMapMarkerVisible(!map.mapMarkersVisible)
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return map.mapMarkersVisible
        }
    }

    private inner class TileGridVisible : AbstractAction(getText("MapControllerPopupDialog.TileGridVisible")), MenuItemSelectedListener {
        override fun actionPerformed(pE: ActionEvent) {
            map.isTileGridVisible = !map.isTileGridVisible
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return map.isTileGridVisible
        }
    }

    private inner class ZoomControlsVisible : AbstractAction(getText("MapControllerPopupDialog.ZoomControlsVisible")), MenuItemSelectedListener {
        override fun actionPerformed(pE: ActionEvent) {
            map.zoomContolsVisible = !map.zoomContolsVisible
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return map.zoomContolsVisible
        }
    }

    private inner class HideFoldedNodes : AbstractAction(getText("MapControllerPopupDialog.HideFoldedNodes")), MenuItemSelectedListener {
        override fun actionPerformed(pE: ActionEvent) {
            map.setHideFoldedNodes(!map.isHideFoldedNodes())
            mMapHook.addMarkersToMap()
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return map.isHideFoldedNodes()
        }
    }

    private inner class SearchControlVisible : AbstractAction(getText("MapControllerPopupDialog.SearchControlVisible")), MenuItemSelectedListener {
        override fun actionPerformed(pE: ActionEvent) {
            mMapHook.toggleSearchBar()
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return mMapHook.isSearchBarVisible
        }
    }

    private inner class LimitSearchToRegionAction : AbstractAction(getText("MapControllerPopupDialog.LimitSearchToRegionAction")), MenuItemSelectedListener {
        override fun actionPerformed(pE: ActionEvent) {
            mMapHook.toggleLimitSearchToRegion()
            mMapHook.focusSearchTerm()
        }

        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return mMapHook.isLimitSearchToRegion
        }
    }

    private inner class GotoSearch : AbstractAction(getText("MapControllerPopupDialog.GotoSearch")) {
        override fun actionPerformed(pE: ActionEvent) {
            if (!mMapHook.isSearchBarVisible) {
                mMapHook.toggleSearchBar()
            } else {
                mMapHook.focusSearchTerm()
            }
        }
    }

    private inner class AddMapPictureToNode : AbstractAction(getText("MapControllerPopupDialog.AddMapPictureToNode")) {
        override fun actionPerformed(pE: ActionEvent) {
            addMapPictureToNode()
        }
    }

    private inner class NewNodeAction : AbstractAction(getText("MapControllerPopupDialog.NewNodeAction")) {
        override fun actionPerformed(pE: ActionEvent) {
            val pos = map.getMapPosition(map.getCursorPosition(),
                    true)
            val e = MouseEvent(map, 0, 0, 0, pos.x, pos.y, 1, false)
            newNode(e)
        }
    }

    private inner class EditNodeInContextMenu : AbstractAction(getText("MapControllerPopupDialog.EditNodeInContextMenu")) {
        override fun actionPerformed(pE: ActionEvent) {
            if (mCurrentPopupPositionHolder == null) {
                return
            }
            setCursorPosition(mCurrentPopupPositionHolder.getPosition())
            var pos = map.getMapPosition(
                    mCurrentPopupPositionHolder.getPosition(), true)
            // unfold node (and its parents):
            var node: MindMapNode? = mCurrentPopupPositionHolder!!.getNode()
            while (!node!!.isRoot) {
                if (node.isFolded) {
                    mMindMapController!!.setFolded(node, false)
                }
                node = node.parentNode
            }
            pos = MapMarkerBase.Companion.adjustToTextfieldLocation(pos)
            val e = MouseEvent(map, 0, 0, 0, pos.x, pos.y, 1, false)
            editNode(mCurrentPopupPositionHolder!!, e)
        }
    }

    private inner class MaxmimalZoomToCursorAction : AbstractAction(
            getText("MapControllerPopupDialog.MaxmimalZoomToCursorAction")) {
        override fun actionPerformed(pE: ActionEvent) {
            val cursorPosition: Coordinate = map.getCursorPosition()
            var zoom = maxZoom - Companion.CURSOR_MAXIMAL_ZOOM_HANDBREAK
            if (map.zoom >= zoom) {
                zoom += Companion.CURSOR_MAXIMAL_ZOOM_HANDBREAK
            }
            map.setDisplayPosition(Coordinate(cursorPosition.lat,
                    cursorPosition.lon), zoom)
        }

        companion object {
            private const val CURSOR_MAXIMAL_ZOOM_HANDBREAK = 2
        }
    }

    private inner class ZoomAction(private val mZoomDelta: Int) : AbstractAction(getText("MapControllerPopupDialog.ZoomAction$pZoomDelta")) {
        override fun actionPerformed(pE: ActionEvent) {
            val mapCenter = map.position
            var zoom = map.zoom + mZoomDelta
            if (zoom < JMapViewer.MIN_ZOOM) {
                zoom = JMapViewer.MIN_ZOOM
            }
            if (zoom > maxZoom) {
                zoom = maxZoom
            }
            map.setDisplayPosition(
                    Coordinate(mapCenter.lat, mapCenter.lon),
                    zoom)
        }
    }

    private inner class CopyLinkToClipboardAction : AbstractAction(getText("MapControllerPopupDialog.CopyLinkToClipboardAction")) {
        override fun actionPerformed(pE: ActionEvent) {
            val link: String
            link = if (mCurrentPopupPositionHolder != null) {
                getLink(mCurrentPopupPositionHolder!!)
            } else {
                val cursorPosition: Coordinate = map.getCursorPosition()
                val position = map.position
                val zoom = map.zoom
                getLink(tileSourceAsString, cursorPosition,
                        position, zoom)
            }
            // Put link into clipboard.
            clipboard.setContents(StringSelection(link), null)
        }
    }

    private inner class CopyCoordinatesToClipboardAction : AbstractAction(
            getText("MapControllerPopupDialog.CopyCoordinatesToClipboardAction")) {
        override fun actionPerformed(pE: ActionEvent) {
            val coordinates: String
            coordinates = if (mCurrentPopupPositionHolder != null) {
                getCoordinates(mCurrentPopupPositionHolder
                        .getPosition())
            } else {
                getCoordinates(map.getCursorPosition())
            }
            // Put Coordinates into clipboard.
            clipboard.setContents(StringSelection(coordinates),
                    null)
        }

        /**
         * @param pCoordinate
         * @return
         */
        private fun getCoordinates(pCoordinate: Coordinate?): String {
            return pCoordinate!!.lat.toString() + " " + pCoordinate.lon
        }
    }

    private inner class ShowNodeMapInContextMenu : AbstractAction(getText("MapControllerPopupDialog.ShowNodeMapInContextMenu")) {
        override fun actionPerformed(pE: ActionEvent) {
            if (mCurrentPopupPositionHolder != null) {
                showNode(mCurrentPopupPositionHolder!!)
            }
        }
    }

    private inner class SelectNodeInContextMenu : AbstractAction(getText("MapControllerPopupDialog.SelectNodeInContextMenu")) {
        override fun actionPerformed(pE: ActionEvent) {
            if (mCurrentPopupPositionHolder != null) {
                selectContextMenuNode()
            }
        }
    }

    private inner class SelectNodeAndCloseInContextMenu : AbstractAction(
            getText("MapControllerPopupDialog.SelectNodeAndCloseInContextMenu")) {
        override fun actionPerformed(pE: ActionEvent) {
            if (mCurrentPopupPositionHolder != null) {
                selectContextMenuNode()
                mMapHook.disposeDialog()
            }
        }
    }

    private inner class RemoveNodeLocationInContextMenu : AbstractAction(
            getText("MapControllerPopupDialog.RemoveNodeLocationInContextMenu")) {
        override fun actionPerformed(pE: ActionEvent) {
            if (mCurrentPopupPositionHolder != null) {
                val node: MindMapNode = mCurrentPopupPositionHolder!!.getNode()
                removeNodePosition(node)
            }
        }
    }

    private inner class ExportMapAction : AbstractAction(getText("MapControllerPopupDialog.ExportMapMenu")) {
        override fun actionPerformed(pE: ActionEvent) {
            val chosenFile = ExportHook.chooseImageFile("png",
                    getText("Portable_Network_Graphic"), null,
                    mMindMapController) ?: return
            val zoomContolsVisible = map.zoomContolsVisible
            try {
                mMindMapController!!.frame.setWaitingCursor(true)
                map.zoomContolsVisible = false
                // Create an image containing the map:
                val myImage = map.createImage(
                        map.width, map.height) as BufferedImage
                map.print(myImage.graphics)
                val out = FileOutputStream(chosenFile)
                ImageIO.write(myImage, "png", out)
                out.close()
            } catch (e1: IOException) {
                Resources.getInstance().logException(e1)
            }
            map.zoomContolsVisible = zoomContolsVisible
            mMindMapController!!.frame.setWaitingCursor(false)
            return
        }
    }

    var map: JCursorMapViewer
        get() = map as JCursorMapViewer
        set(map) {
            super.map = map
        }

    /**
     *
     */
    fun addMapPictureToNode() {
        if (mCurrentPopupPositionHolder == null) {
            // strange.
            return
        }
        addPictureToNode(mCurrentPopupPositionHolder!!, mMindMapController)
    }

    fun addAccelerator(menuItem: JMenuItem?, key: String?) {
        val keyProp = mMindMapController!!.frame.getProperty(key)
        val keyStroke = KeyStroke.getKeyStroke(keyProp)
        // menuItem.setAccelerator(keyStroke);
        menuItem!!.action.putValue(Action.ACCELERATOR_KEY, keyStroke)
    }

    /**
     * @param pSelected
     * @return
     */
    protected fun placeNode(pSelected: MindMapNode?): MapNodePositionHolderBase? {
        val cursorPosition: Coordinate = map.getCursorPosition()
        val position = map.position
        val zoom = map.zoom
        return placeNodeAt(pSelected, cursorPosition, position, zoom)
    }

    protected fun placeNodeAt(pSelected: MindMapNode?,
                              cursorPosition: Coordinate?, position: Coordinate?, zoom: Int): MapNodePositionHolderBase? {
        var hook: MapNodePositionHolder? = MapNodePositionHolder.Companion.getHook(pSelected)
        if (hook == null) {
            hook = addHookToNode(pSelected)
        }
        if (hook != null) {
            // set parameters:
            val tileSource = tileSourceAsString
            hook.changePosition(cursorPosition, position, zoom, tileSource)
        } else {
            logger.warning("Hook not found although it was recently added. Node was "
                    + pSelected)
        }
        return hook
    }

    val tileSourceAsString: String
        get() = getTileSourceName(tileSource)
    val tileSource: TileSource
        get() = map.tileController.tileSource

    fun removeNodePosition(selected: MindMapNode?) {
        val hook: MapNodePositionHolderBase = MapNodePositionHolder.Companion.getHook(selected)
        if (hook != null) {
            // double add == remove
            addHookToNode(selected)
        }
    }

    /**
     */
    fun showSelectedNodes() {
        val selected = mMindMapController!!.selected!!
        val selecteds = mMindMapController.selecteds
        if (selecteds.size == 1) {
            val hook: MapNodePositionHolder = MapNodePositionHolder.Companion.getHook(selected)
            if (hook != null) {
                showNode(hook)
            }
            return
        }
        // find common center. Code adapted from JMapViewer.
        var x_min = Int.MAX_VALUE
        var y_min = Int.MAX_VALUE
        var x_max = Int.MIN_VALUE
        var y_max = Int.MIN_VALUE
        val mapZoomMax = maxZoom
        for (node in selecteds) {
            val hook: MapNodePositionHolder = MapNodePositionHolder.Companion.getHook(node)
            if (hook != null) {
                val x = OsmMercator.LonToX(hook.position.lon,
                        mapZoomMax).toInt()
                val y = OsmMercator.LatToY(hook.position.lat,
                        mapZoomMax).toInt()
                x_max = Math.max(x_max, x)
                y_max = Math.max(y_max, y)
                x_min = Math.min(x_min, x)
                y_min = Math.min(y_min, y)
                if (node === selected) {
                    setCursorPosition(hook.position)
                    changeTileSource(hook.tileSource, map)
                }
            }
        }
        val height = Math.max(0, map.height)
        val width = Math.max(0, map.width)
        var newZoom = mapZoomMax
        var x = x_max - x_min
        var y = y_max - y_min
        while (x > width || y > height) {
            newZoom--
            x = x shr 1
            y = y shr 1
        }
        x = x_min + (x_max - x_min) / 2
        y = y_min + (y_max - y_min) / 2
        val z = 1 shl mapZoomMax - newZoom
        x /= z
        y /= z
        map.setDisplayPosition(x, y, newZoom)
    }

    val maxZoom: Int
        get() = tileSource.maxZoom

    fun showNode(hook: MapNodePositionHolder) {
        val zoom = hook.zoom
        changeTileSource(hook.tileSource, map)
        setCursorPosition(hook, zoom)
    }

    fun setCursorPosition(hook: MapNodePositionHolder, zoom: Int) {
        val position = hook.position
        val mapCenter = hook.mapCenter
        setZoom(zoom)
        if (mapCenter != null) {
            // move map:
            logger.fine("Set display position to " + mapCenter
                    + " and cursor to " + position + " and zoom " + zoom
                    + " where max zoom is " + maxZoom)
            map.setDisplayPosition(
                    Coordinate(mapCenter.lat, mapCenter.lon),
                    zoom)
        }
        setCursorPosition(position)
    }

    /**
     * Sets the cursor to the specified position and moves the display, such
     * that the cursor is visible.
     */
    fun setCursorPosition(position: Coordinate?) {
        map.setCursorPosition(position)
        // is the cursor now visible and the zoom correct? if not, display it
        // directly.
        if (map.getMapPosition(position, true) == null) {
            map.setDisplayPosition(
                    Coordinate(position!!.lat, position.lon),
                    map.zoom)
        }
        storeMapPosition(position)
        for (listener in mCursorPositionListeners) {
            listener.cursorPositionChanged(position)
        }
    }

    /**
     * Sets the zoom.
     */
    protected fun setZoom(zoom: Int) {
        var zoom = zoom
        if (zoom > maxZoom) {
            zoom = maxZoom
        }
        if (zoom == 0) {
            zoom = map.zoom
        }
        map.zoom = zoom
    }

    fun addHookToNode(selected: MindMapNode?): MapNodePositionHolder? {
        val hook: MapNodePositionHolder
        val selecteds: List<MindMapNode?> = getVectorWithSingleElement(selected)
        mMindMapController!!.addHook(selected, selecteds,
                MapNodePositionHolderBase.NODE_MAP_HOOK_NAME, null)
        hook = MapNodePositionHolder.Companion.getHook(selected)
        return hook
    }

    /**
     * Translate String
     *
     * @param pString
     * @return
     */
    private fun getText(pString: String): String {
        return mMindMapController!!.getText(pString)
    }

    override fun mouseDragged(e: MouseEvent) {
        if (!isMovementEnabled
                || !(isMoving || isMapNodeMoving || mIsRectangularSelect)) return
        if (isMapNodeMoving) {
            lastDragPoint = e.point
            var diffx = 0
            var diffy = 0
            if (e.x < SCROLL_MARGIN) {
                diffx = -SCROLL_PIXEL_AMOUNT
            }
            if (map.width - e.x < SCROLL_MARGIN) {
                diffx = SCROLL_PIXEL_AMOUNT
            }
            if (e.y < SCROLL_MARGIN) {
                diffy = -SCROLL_PIXEL_AMOUNT
            }
            if (map.height - e.y < SCROLL_MARGIN) {
                diffy = SCROLL_PIXEL_AMOUNT
            }
            map.moveMap(diffx, diffy)
            return
        }
        if (mIsRectangularSelect) {
            // Actualize second point of rectangle.
            map.setRectangular(mRectangularStart,
                    getCoordinateFromMouseEvent(e))
            map.setDrawRectangular(true)
            map.repaint()
            return
        }
        // Is only the selected mouse button pressed?
        if (e.modifiersEx and MOUSE_BUTTONS_MASK == movementMouseButtonMask) {
            moveMapOnDrag(e)
        }
    }

    fun moveMapOnDrag(e: MouseEvent) {
        val p = e.point
        if (lastDragPoint != null) {
            val diffx = lastDragPoint!!.x - p.x
            val diffy = lastDragPoint!!.y - p.y
            map.moveMap(diffx, diffy)
            // System.out.println("Move to " + map.getPosition() +
            // " with zoom " + map.getZoom() );
        }
        lastDragPoint = p
    }

    override fun mouseClicked(e: MouseEvent) {
        if (!isClickEnabled) {
            return
        }
        if (e.clickCount == 2 && e.button == MouseEvent.BUTTON1) {
            // on double click: new node.
            newNode(e)
            return
        }
        // is button 1?
        if (e.button == MouseEvent.BUTTON1 || isMacOsX
                && e.modifiersEx == MAC_MOUSE_BUTTON1_MASK) {
            setCursorPosition(e)
        }
    }

    private fun setMouseControl(pEnable: Boolean) {
        isMovementEnabled = pEnable
        isWheelZoomEnabled = pEnable
        isClickEnabled = pEnable
    }

    /**
     * @param pEvent
     * : location
     */
    private fun newNode(pEvent: MouseEvent) {
        val targetNode = mMindMapController!!.selected!!
        val newNode = insertNewNode(targetNode)
        val nodeView = mMindMapController.getNodeView(newNode)
        mMindMapController.select(nodeView)
        // inline editing:
        mMindMapController.isBlocked = true
        setMouseControl(false)
        val point = pEvent.point
        convertPointToAncestor((pEvent.source as Component), point, map)
        storeMapPosition(map.getCursorPosition())
        val editControl = MapEditTextFieldControl(
                nodeView, newNode, targetNode, false)
        val textfield: EditNodeTextField = MapEditNoteTextField(nodeView, "",
                null, mMindMapController, editControl, map, point)
        textfield.show()
    }

    fun insertNewNode(targetNode: MindMapNode?): MindMapNode? {
        var childPosition: Int
        val parent: MindMapNode?
        if (targetNode!!.isRoot) {
            parent = targetNode
            childPosition = 0
        } else {
            // new sibling:
            parent = targetNode.parentNode
            childPosition = parent!!.getChildPosition(targetNode)
            childPosition++
        }
        return mMindMapController!!.addNewNode(parent,
                childPosition, targetNode.isLeft)
    }

    /**
     * @param pPositionHolder
     * @param pEvent
     * : location
     */
    private fun editNode(pPositionHolder: MapNodePositionHolder,
                         pEvent: MouseEvent) {
        val editNode: MindMapNode = pPositionHolder.getNode()
        val nodeView = mMindMapController!!.getNodeView(editNode) ?: return
        mMindMapController.select(nodeView)
        // inline editing:
        mMindMapController.isBlocked = true
        setMouseControl(false)
        val point = pEvent.point
        convertPointToAncestor((pEvent.source as Component), point, map)
        val editControl = MapEditTextFieldControl(
                nodeView, editNode, editNode, true)
        val textfield: EditNodeTextField = MapEditNoteTextField(nodeView,
                editNode.text, null, mMindMapController, editControl, map,
                point)
        textfield.show()
    }

    fun setCursorPosition(e: MouseEvent) {
        val coordinates = map.getPosition(e.point)
        setCursorPosition(coordinates)
    }

    override fun mousePressed(e: MouseEvent) {
        if (!isClickEnabled) {
            return
        }
        showPopupMenu(e)
        if (e.isConsumed) {
            return
        }
        if (e.button == movementMouseButton
                || isMacOsX && e.modifiersEx == MAC_MOUSE_BUTTON1_MASK) {
            if (e.isShiftDown) {
                // rectangular select:
                mIsRectangularSelect = true
                mRectangularStart = getCoordinateFromMouseEvent(e)
                logger.fine("Starting rect on $mRectangularStart")
                return
            }
            // detect collision with map marker:
            val mapMarker = checkHit(e)
            if (mapMarker is MapMarkerLocation) {
                val posHolder = mapMarker
                        .nodePositionHolder
                mDragStartingPoint = Point(e.point)
                correctPointByMapCenter(mDragStartingPoint!!)
                isMapNodeMoving = true
                mMapNodeMovingSource = posHolder
                setCursor(Cursor.MOVE_CURSOR, true)
                return
            }
            lastDragPoint = null
            isMoving = true
        }
    }

    protected fun correctPointByMapCenter(dragStartingPoint: Point) {
        val center = map.center
        dragStartingPoint.translate(center.x, center.y)
    }

    fun checkHit(e: MouseEvent?): MapMarkerBase? {
        // check for hit on map marker:
        val it: Iterator<*> = map.mapMarkerList.iterator()
        while (it.hasNext()) {
            val location = it.next() as MapMarkerBase
            val locationC = location.coordinate
            val locationXY = map.getMapPosition(locationC, true) ?: continue
            val checkHitResult = location.checkHit(e!!.x - locationXY.x,
                    e.y - locationXY.y)
            logger.fine("Checking for hit for location " + location
                    + " at location " + locationXY + " to event " + e.x
                    + " and " + e.y + " is " + checkHitResult)
            if (checkHitResult) {
                return location
            }
        }
        return null
    }

    fun getCoordinateFromMouseEvent(e: MouseEvent?): Coordinate {
        return map
                .getPosition(Point(e!!.x, e.y))
    }

    /**
     * @param e
     * event.
     */
    private fun showPopupMenu(e: MouseEvent) {
        if (e.isPopupTrigger) {
            val popupmenu = popupMenu
            // check for hit on map marker:
            val mapMarker = checkHit(e)
            if (mapMarker is MapMarkerLocation) {
                val posHolder = mapMarker
                        .nodePositionHolder
                mCurrentPopupPositionHolder = posHolder
                setCursorPosition(posHolder.position)
                contextPopupMenu
                        .show(e.component, e.x, e.y)
                e.consume()
                return
            }
            if (mapMarker is MapSearchMarkerLocation) {
                setCursorPosition(mapMarker.coordinate)
                searchPopupMenu.show(e.component, e.x, e.y)
                e.consume()
                return
            }
            mCurrentPopupPositionHolder = null
            if (popupmenu != null) {
                setCursorPosition(e)
                popupmenu.show(e.component, e.x, e.y)
                e.consume()
            }
        }
    }

    /**
     * listener, that blocks the controler if the menu is active (PN) Take care!
     * This listener is also used for modelpopups (as for graphical links).
     */
    private inner class ControllerPopupMenuListener : PopupMenuListener {
        override fun popupMenuWillBecomeVisible(e: PopupMenuEvent) {
            setMouseControl(false) // block controller
        }

        override fun popupMenuWillBecomeInvisible(e: PopupMenuEvent) {
            setMouseControl(true) // unblock controller
        }

        override fun popupMenuCanceled(e: PopupMenuEvent) {
            setMouseControl(true) // unblock controller
        }
    }

    /**
     * Take care! This listener is also used for modelpopups (as for graphical
     * links).
     */
    protected val popupListenerSingleton: ControllerPopupMenuListener = ControllerPopupMenuListener()
    private var mTimerMouseEvent: MouseEvent? = null
    private val mZoomInAction: Action
    private val mZoomOutAction: Action
    private val mMoveForwardAction: MoveForwardAction
    private val mMoveBackwardAction: MoveBackwardAction
    var mMenuBar: JMenuBar
    private var mWheelZoomLastTime: Long = 0
    private val mCursorPositionListeners = Vector<CursorPositionListener>()
    private var mSearchPopupMenu: JPopupMenu? = null

    init {
        mMouseHitsNodeTimer = Timer(500, this)
        mMouseHitsNodeTimer.isRepeats = false
        val placeAction: Action = PlaceNodeAction()
        val showAction: Action = ShowNodeAction()
        mZoomInAction = ZoomAction(1)
        mZoomOutAction = ZoomAction(-1)
        val setDisplayToFitMapMarkers: Action = SetDisplayToFitMapMarkers()
        val showMapMarker: Action = ShowMapMarker()
        val tileGridVisible: Action = TileGridVisible()
        val zoomControlsVisible: Action = ZoomControlsVisible()
        val searchControlVisible: Action = SearchControlVisible()
        val gotoSearch: Action = GotoSearch()
        val hideFoldedNodes: Action = HideFoldedNodes()
        val newNodeAction: Action = NewNodeAction()
        // Action newNodeReverseLookupAction = new NewNodeReverseLookupAction();
        val maxmimalZoomToCursorAction: Action = MaxmimalZoomToCursorAction()
        val copyLinkToClipboardAction: Action = CopyLinkToClipboardAction()
        val copyCoordinatesToClipboardAction: Action = CopyCoordinatesToClipboardAction()
        val exportAction: Action = ExportMapAction()
        /** Menu  */
        val menuHolder = StructuredMenuHolder()
        mMenuBar = JMenuBar()
        val mainItem = JMenu(getText("MapControllerPopupDialog.Actions"))
        menuHolder.addMenu(mainItem, "main/actions/.")
        addAccelerator(menuHolder.addAction(placeAction, "main/actions/place"),
                "keystroke_plugins/map/MapDialog_Place")
        menuHolder.addAction(exportAction, "main/actions/exportPng")
        addAccelerator(menuHolder.addAction(mMapHook.closeAction,
                "main/actions/close"), "keystroke_plugins/map/MapDialog_Close")
        val searchItem = JMenu(getText("MapControllerPopupDialog.Search"))
        menuHolder.addMenu(searchItem, "main/search/.")
        addAccelerator(menuHolder.addAction(searchControlVisible,
                "main/search/showSearchControl"),
                "keystroke_plugins/map/MapDialog_toggle_search")
        addAccelerator(
                menuHolder.addAction(gotoSearch, "main/search/gotoSearch"),
                "keystroke_plugins/map/MapDialog_goto_search")
        addAccelerator(menuHolder.addAction(LimitSearchToRegionAction(),
                "main/search/limitSearchToRegion"),
                "keystroke_plugins/map/MapDialog_limitSearchToRegion")
        val viewItem = JMenu(getText("MapControllerPopupDialog.Views"))
        menuHolder.addMenu(viewItem, "main/view/.")
        menuHolder.addAction(showAction, "main/view/showNode")
        menuHolder.addAction(setDisplayToFitMapMarkers,
                "main/view/setDisplayToFitMapMarkers")
        menuHolder.addSeparator("main/view/")
        for (i in sTileSources.indices) {
            val source = sTileSources[i].mTileSource
            addAccelerator(menuHolder.addAction(ChangeTileSource(source),
                    "main/view/$i"),
                    "keystroke_plugins/map/MapDialog_tileSource_$i")
        }
        menuHolder.addSeparator("main/view/")
        menuHolder.addAction(showMapMarker, "main/view/showMapMarker")
        menuHolder.addAction(tileGridVisible, "main/view/tileGridVisible")
        menuHolder.addAction(zoomControlsVisible,
                "main/view/zoomControlsVisible")
        addAccelerator(menuHolder.addAction(hideFoldedNodes,
                "main/view/hideFoldedNodes"),
                "keystroke_plugins/map/MapDialog_hideFoldedNodes")
        menuHolder.addSeparator("main/view/")
        addAccelerator(
                menuHolder.addAction(mZoomInAction, "main/view/ZoomInAction"),
                "keystroke_plugins/map/MapDialog_zoomIn")
        addAccelerator(
                menuHolder.addAction(mZoomOutAction, "main/view/ZoomOutAction"),
                "keystroke_plugins/map/MapDialog_zoomOut")
        val navigationItem = JMenu(
                getText("MapControllerPopupDialog.Navigation"))
        menuHolder.addMenu(navigationItem, "main/navigation/.")
        // menuHolder.addSeparator("main/navigation/");
        addAccelerator(menuHolder.addAction(SetHomeAction(),
                "main/navigation/SetHome"),
                "keystroke_plugins/map/MapDialogSetHome")
        addAccelerator(menuHolder.addAction(MoveHomeAction(),
                "main/navigation/MoveHome"),
                "keystroke_plugins/map/MapDialogMoveHome")
        menuHolder.addSeparator("main/navigation/")
        mMoveBackwardAction = MoveBackwardAction()
        addAccelerator(menuHolder.addAction(mMoveBackwardAction,
                "main/navigation/moveBackward"),
                "keystroke_plugins/map/MapDialog_moveBackward")
        mMoveForwardAction = MoveForwardAction()
        addAccelerator(menuHolder.addAction(mMoveForwardAction,
                "main/navigation/moveForward"),
                "keystroke_plugins/map/MapDialog_moveForward")
        menuHolder.addSeparator("main/navigation/")
        addAccelerator(menuHolder.addAction(MoveLeftAction(),
                "main/navigation/moveLeft"),
                "keystroke_plugins/map/MapDialog_moveLeft")
        addAccelerator(menuHolder.addAction(MoveRightAction(),
                "main/navigation/moveRight"),
                "keystroke_plugins/map/MapDialog_moveRight")
        addAccelerator(menuHolder.addAction(MoveUpAction(),
                "main/navigation/moveUp"),
                "keystroke_plugins/map/MapDialog_moveUp")
        addAccelerator(menuHolder.addAction(MoveDownAction(),
                "main/navigation/moveDown"),
                "keystroke_plugins/map/MapDialog_moveDown")
        menuHolder.addSeparator("main/navigation/")
        menuHolder.updateMenus(mMenuBar, "main/")
        mMapDialog!!.jMenuBar = mMenuBar
        /* Popup menu */menuHolder.addAction(newNodeAction, "popup/newNode")
        // currently disabled, as the reverse functionality from
        // nominatim doesn't convince me.
        // menuHolder.addAction(newNodeReverseLookupAction,
        // "popup/newNodeReverseLookup");
        menuHolder.addAction(placeAction, "popup/place")
        menuHolder.addSeparator("popup/")
        menuHolder.addAction(maxmimalZoomToCursorAction,
                "popup/maxmimalZoomToCursorAction")
        menuHolder.addSeparator("popup/")
        menuHolder.addAction(copyLinkToClipboardAction,
                "popup/copyLinkToClipboardAction")
        menuHolder.addAction(copyCoordinatesToClipboardAction,
                "popup/copyCoordinatesToClipboardAction")
        menuHolder.updateMenus(popupMenu, "popup/")
        /*
		 * map location context menu
		 */menuHolder.addAction(EditNodeInContextMenu(),
                "contextPopup/editNodeInContextMenu")
        menuHolder.addAction(RemoveNodeLocationInContextMenu(),
                "contextPopup/RemoveNodeLocationInContextMenu")
        menuHolder.addAction(SelectNodeInContextMenu(),
                "contextPopup/SelectNodeInContextMenu")
        menuHolder.addAction(SelectNodeAndCloseInContextMenu(),
                "contextPopup/SelectNodeAndCloseInContextMenu")
        menuHolder.addSeparator("contextPopup/")
        menuHolder.addAction(ShowNodeMapInContextMenu(),
                "contextPopup/showNodeMapInContextMenu")
        menuHolder.addAction(maxmimalZoomToCursorAction,
                "contextPopup/maxmimalZoomToCursorAction")
        menuHolder.addSeparator("contextPopup/")
        menuHolder.addAction(copyLinkToClipboardAction,
                "contextPopup/copyLinkToClipboardAction")
        menuHolder.addAction(AddMapPictureToNode(),
                "contextPopup/addPictureToNode")
        menuHolder.updateMenus(contextPopupMenu, "contextPopup/")
        menuHolder.addAction(maxmimalZoomToCursorAction,
                "searchPopup/maxmimalZoomToCursorAction")
        menuHolder.updateMenus(searchPopupMenu, "searchPopup/")
        mMapDialog.addKeyListener(this)
        // Tools.addFocusPrintTimer();
    }

    override fun mouseReleased(e: MouseEvent) {
        if (!isClickEnabled) {
            return
        }
        showPopupMenu(e)
        if (e.isConsumed) {
            return
        }
        if (e.button == movementMouseButton || isMacOsX
                && e.button == MouseEvent.BUTTON1) {
            val coordinates = getCoordinateFromMouseEvent(e)
            if (isMapNodeMoving) {
                // check for minimal drag distance:
                val currentPoint = Point(e.point)
                correctPointByMapCenter(currentPoint)
                if (mDragStartingPoint!!.distance(currentPoint) > MapMarkerBase.Companion.CIRCLE_RADIUS) {
                    mMapNodeMovingSource!!.changePosition(coordinates,
                            map.position, map.zoom,
                            tileSourceAsString)
                } else {
                    // select the node (single click)
                    var node: MindMapNode? = mMapNodeMovingSource!!.getNode()
                    if (e.isShiftDown) {
                        val sel = Vector(mMindMapController!!.selecteds)
                        if (sel.contains(node)) {
                            // remove:
                            sel.remove(node)
                            node = mMindMapController.selected
                        } else {
                            sel.add(node)
                        }
                        mMindMapController.select(node, sel)
                    } else {
                        selectNode(node)
                    }
                }
                mMapNodeMovingSource = null
                setCursor(Cursor.DEFAULT_CURSOR, false)
            }
            if (mIsRectangularSelect) {
                // gather all locations and select them:
                val mapNodePositionHolders = Vector<MindMapNode?>()
                // take only those elements in the correct rectangle:
                val r: Rectangle = map.getRectangle(mRectangularStart,
                        coordinates)
                if (r != null) {
                    var last: MindMapNode? = null
                    for (holder in mMapHook.mapNodePositionHolders) {
                        val pointPosition = holder.position
                        val mapPosition = map.getMapPosition(
                                pointPosition, true)
                        if (mapPosition != null && r.contains(mapPosition)) {
                            // ok
                            mapNodePositionHolders.add(holder!!.getNode())
                            last = holder!!.getNode()
                        }
                    }
                    if (last != null) {
                        // ie. at least one found:
                        mMindMapController!!.select(last, mapNodePositionHolders)
                    }
                }
            }
            map.setDrawRectangular(false)
            mIsRectangularSelect = false
            mRectangularStart = null
            isMapNodeMoving = false
            if (lastDragPoint != null) {
                storeMapPosition(coordinates)
            }
            lastDragPoint = null
            isMoving = false
        }
    }

    protected fun storeMapPosition(coordinates: Coordinate?) {
        val holder = PositionHolder(coordinates!!.lat,
                coordinates.lon, map.zoom)
        val positionHolderVector = positionHolderVector
        if (positionHolderIndex >= 0) {
            // check for equalness
            val currentPosition = positionHolderVector[positionHolderIndex] as PositionHolder
            if (currentPosition == holder) {
                return
            }
        }
        // if position is not at the end, the locations in front are deleted.
        while (positionHolderIndex < positionHolderVector.size - 1) {
            positionHolderVector.removeAt(positionHolderVector.size - 1)
        }
        logger.fine("Storing position " + holder + " at index "
                + positionHolderIndex)
        positionHolderVector.insertElementAt(holder,
                positionHolderIndex + 1)
        positionHolderIndex = positionHolderIndex + 1
        // assure that max size is below limit.
        while (positionHolderVector.size >= POSITION_HOLDER_LIMIT
                && positionHolderIndex > 0) {
            positionHolderIndex = Math.max(positionHolderIndex - 1, 0)
            positionHolderVector.removeAt(0)
        }
        // update actions
        mMoveForwardAction.isEnabled = mMoveForwardAction.isEnabled
        mMoveBackwardAction.isEnabled = mMoveBackwardAction.isEnabled
    }

    protected fun setCursor(defaultCursor: Int, pVisible: Boolean) {
        val glassPane = glassPane
        glassPane.cursor = Cursor.getPredefinedCursor(defaultCursor)
        glassPane.isVisible = pVisible
    }

    val glassPane: Component
        get() = map.rootPane.glassPane

    override fun mouseWheelMoved(e: MouseWheelEvent) {
        if (isWheelZoomEnabled) {
            /*
			 * This is problematic under Mac as the zoom is too fast. First
			 * idea: looking for the last time the zoom was changed. It must not
			 * be changed within 100ms again. Moreover, limit the rotation
			 * number.
			 */
            if (System.currentTimeMillis() - mWheelZoomLastTime >= WHEEL_ZOOM_MINIMAL_TIME_BETWEEN_CHANGES) {
                var wheelRotation = e.wheelRotation
                if (Math.abs(wheelRotation) > 2) {
                    wheelRotation = (2 * Math.signum(wheelRotation.toFloat())).toInt()
                }
                map.setZoom(map.zoom - wheelRotation, e.point)
                mWheelZoomLastTime = System.currentTimeMillis()
            }
        }
    }

    fun getMovementMouseButton(): Int {
        return movementMouseButton
    }

    val contextPopupMenu: JPopupMenu
        get() {
            if (mContextPopupMenu == null) {
                mContextPopupMenu = JPopupMenu()
                mContextPopupMenu!!.addPopupMenuListener(popupListenerSingleton)
            }
            return mContextPopupMenu!!
        }
    val searchPopupMenu: JPopupMenu
        get() {
            if (mSearchPopupMenu == null) {
                mSearchPopupMenu = JPopupMenu()
                mSearchPopupMenu!!.addPopupMenuListener(popupListenerSingleton)
            }
            return mSearchPopupMenu!!
        }

    /**
     * Sets the mouse button that is used for moving the map. Possible values
     * are:
     *
     *  * [MouseEvent.BUTTON1] (left mouse button)
     *  * [MouseEvent.BUTTON2] (middle mouse button)
     *  * [MouseEvent.BUTTON3] (right mouse button)
     *
     *
     * @param movementMouseButton
     */
    fun setMovementMouseButton(movementMouseButton: Int) {
        this.movementMouseButton = movementMouseButton
        movementMouseButtonMask = when (movementMouseButton) {
            MouseEvent.BUTTON1 -> MouseEvent.BUTTON1_DOWN_MASK
            MouseEvent.BUTTON2 -> MouseEvent.BUTTON2_DOWN_MASK
            MouseEvent.BUTTON3 -> MouseEvent.BUTTON3_DOWN_MASK
            else -> throw RuntimeException("Unsupported button")
        }
    }

    override fun mouseEntered(e: MouseEvent) {}
    override fun mouseExited(e: MouseEvent) {}
    override fun mouseMoved(e: MouseEvent) {
        if (!isMovementEnabled) {
            return
        }
        // Mac OSX simulates with ctrl + mouse 1 the second mouse button hence
        // no dragging events get fired.
        //
        if (isMacOsX) {
            if (isMapNodeMoving) {
                lastDragPoint = e.point
                return
            }
            // Is only the selected mouse button pressed?
            if (isMoving && e.modifiersEx == 0 /* MouseEvent.CTRL_DOWN_MASK */) {
                moveMapOnDrag(e)
                return
            }
        }
        // no move events, thus the cursor is just moving.
        mMouseHitsNodeTimer.restart()
        mTimerMouseEvent = e
    }

    /**
     * Action handler for search result handling.
     *
     * @param pPlace
     */
    fun setCursorPosition(pPlace: Place) {
        map.setDisplayPosition(
                Coordinate(pPlace.lat, pPlace.lon), map.zoom)
        val cursorPosition = Coordinate(pPlace.lat,
                pPlace.lon)
        setCursorPosition(cursorPosition)
    }

    /**
     * @return true, if ok, false if error.
     */
    fun search(dataModel: ResultTableModel?,
               mResultTable: JTable?, mSearchText: String,
               mTableOriginalBackgroundColor: Color?): Boolean {
        // Display hour glass
        var returnValue = true
        setCursor(Cursor.WAIT_CURSOR, true)
        try {
            dataModel!!.clear()
            // doesn't work due to event thread...
            mResultTable!!.background = Color.GRAY
            val results = getSearchResults(mSearchText)
            if (results == null) {
                mResultTable.background = Color.RED
            } else {
                val it: Iterator<Place> = results.listPlaceList.iterator()
                while (it.hasNext()) {
                    val place = it.next()
                    logger.fine("Found place " + place.displayName)
                    // error handling, if the query wasn't successful.
                    if (safeEquals("ERROR", place.osmType)) {
                        mResultTable.background = Color.RED
                        returnValue = false
                    } else if (safeEquals("WARNING", place.osmType)) {
                        mResultTable.background = Color.YELLOW
                        returnValue = false
                    } else {
                        mResultTable.background = Color.WHITE
                        mResultTable.background = mTableOriginalBackgroundColor
                    }
                    dataModel.addPlace(place)
                }
            }
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            returnValue = false
        }
        setCursor(Cursor.DEFAULT_CURSOR, false)
        return returnValue
    }

    fun getReverseLookup(pCoordinate: Coordinate, pZoom: Int): Reversegeocode? {
        val b = StringBuilder()
        b.append("https://nominatim.openstreetmap.org/reverse?format=xml&email=christianfoltin%40users.sourceforge.net&addressdetails=0") //$NON-NLS-1$
        b.append("&accept-language=").append(Locale.getDefault().language) //$NON-NLS-1$
        b.append("&lat=")
        b.append(pCoordinate.lat)
        b.append("&lon=")
        b.append(pCoordinate.lon)
        b.append("&zoom=")
        b.append(pZoom)
        try {
            val result = wget(b.toString())
            return XmlBindingTools
                    .getInstance().unMarshall(result)
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
        return null
    }

    /**
     * @param pText
     * @return
     */
    fun getSearchResults(pText: String): Searchresults? {
        var result: String? = "unknown"
        var results = Searchresults()
        // special case: lat lon[;lat2 lon2;...]
        if (pText.matches("[ 0-9eE.,;\\-]+")) {
            val regex = " *([0-9eE.,\\-]+) +([0-9eE.,\\-]+) *"
            val pattern = Pattern.compile(regex)
            var reverseLookupErrorOccured: Boolean = !Resources.getInstance()
                    .getBoolProperty(DO_REVERSE_LOOKUP_ON_LAT_LON_SEARCH)
            val coords = pText.split(";").toTypedArray()
            for (i in coords.indices) {
                val coord = coords[i]
                val matcher = pattern.matcher(coord)
                if (matcher.matches()) {
                    val lat = matcher.group(1).toDouble()
                    val lon = matcher.group(2).toDouble()
                    val place = Place()
                    if (!reverseLookupErrorOccured) {
                        try {
                            // Try reverse lookup:
                            val reverseLookup = getReverseLookup(
                                    Coordinate(lat, lon), map.zoom)
                            if (reverseLookup!!.listResultList.size > 0) {
                                place.displayName = reverseLookup.getResult(0).content
                            }
                        } catch (e: Exception) {
                            Resources.getInstance().logException(
                                    e)
                            reverseLookupErrorOccured = true
                        }
                    }
                    if (place.displayName == null) {
                        place.displayName = coord
                    }
                    place.osmType = "node"
                    place.lat = lat
                    place.lon = lon
                    results.addPlace(place)
                }
            }
            return results
        }
        val b = StringBuilder()
        val limitSearchToRegion = mMapHook.isLimitSearchToRegion
        try {
            if (true) {
                b.append("https://nominatim.openstreetmap.org/search/?email=christianfoltin%40users.sourceforge.net&q=") //$NON-NLS-1$
                b.append(URLEncoder.encode(pText, "UTF-8"))
                b.append("&format=xml&limit=30&accept-language=").append(Locale.getDefault().language) //$NON-NLS-1$
                if (limitSearchToRegion) {
                    val topLeftCorner = map.getPosition(0, 0)
                    val bottomRightCorner = map.getPosition(
                            map.width, map.height)
                    b.append("&viewbox=")
                    b.append(topLeftCorner.lon)
                    b.append(",")
                    b.append(topLeftCorner.lat)
                    b.append(",")
                    b.append(bottomRightCorner.lon)
                    b.append(",")
                    b.append(bottomRightCorner.lat)
                    b.append("&bounded=1")
                }
                result = wget(b.toString())
            } else {
                // only for offline testing:
                result = """$XML_VERSION_1_0_ENCODING_UTF_8<searchresults timestamp="Tue, 08 Nov 11 22:49:54 -0500" attribution="Data Copyright OpenStreetMap Contributors, Some Rights Reserved. CC-BY-SA 2.0." querystring="innsbruck" polygon="false" exclude_place_ids="228452,25664166,26135863,25440203" more_url="http://open.mapquestapi.com/nominatim/v1/search?format=xml&amp;exclude_place_ids=228452,25664166,26135863,25440203&amp;accept-language=&amp;q=innsbruck">
  <place place_id="228452" osm_type="node" osm_id="34840064" place_rank="16" boundingbox="47.2554266357,47.2754304504,11.3827679062,11.4027688599" lat="47.2654296" lon="11.3927685" display_name="Innsbruck, Bezirk Innsbruck-Stadt, Innsbruck-Stadt, Tirol, Österreich, Europe" class="place" type="city" icon="http://open.mapquestapi.com/nominatim/v1/images/mapicons/poi_place_city.p.20.png"/>
  <place place_id="25664166" osm_type="way" osm_id="18869490" place_rank="27" boundingbox="43.5348739624023,43.5354156494141,-71.1319198608398,-71.1316146850586" lat="43.5351336524196" lon="-71.1317853486877" display_name="Innsbruck, New Durham, Strafford County, New Hampshire, United States of America" class="highway" type="service"/>
  <place place_id="26135863" osm_type="way" osm_id="18777572" place_rank="27" boundingbox="38.6950759887695,38.6965446472168,-91.1586227416992,-91.1520233154297" lat="38.6957456083531" lon="-91.1552550683042" display_name="Innsbruck, Warren, Aspenhoff, Warren County, Missouri, United States of America" class="highway" type="service"/>
  <place place_id="25440203" osm_type="way" osm_id="18869491" place_rank="27" boundingbox="43.5335311889648,43.5358810424805,-71.1356735229492,-71.1316146850586" lat="43.5341678362733" lon="-71.1338615946084" display_name="Innsbruck, New Durham, Strafford County, New Hampshire, 03855, United States of America" class="highway" type="service"/>
</searchresults>"""
                result = ("<?xml version=\"1.0\" encoding=\"UTF-8\""
                        + " ?><searchresults timestamp='Wed, 29 Aug"
                        + " 12 06:33:22 +0100' attribution='Data Co"
                        + "pyright OpenStreetMap Contributors, Some"
                        + " Rights Reserved. CC-BY-SA 2.0.' queryst"
                        + "ring='bäckerei' polygon='false' exclude_"
                        + "place_ids='2323884,1350101,7261519,17658"
                        + "198,16228926,7825940,8072208,16133988,51"
                        + "52777,7708711,16471512,7844042,12267468,"
                        + "6699146,7114466,6856494,856383,9874163,7"
                        + "135888,868611,11403029,6568269,16118527,"
                        + "7540110,11628259,1339026,19587330,115253"
                        + "72,11534612,11748035' more_url='http://n"
                        + "ominatim.openstreetmap.org/search?format"
                        + "=xml&amp;exclude_place_ids=2323884,13501"
                        + "01,7261519,17658198,16228926,7825940,807"
                        + "2208,16133988,5152777,7708711,16471512,7"
                        + "844042,12267468,6699146,7114466,6856494,"
                        + "856383,9874163,7135888,868611,11403029,6"
                        + "568269,16118527,7540110,11628259,1339026"
                        + ",19587330,11525372,11534612,11748035&amp"
                        + ";accept-language=de&amp;viewbox=13.24470"
                        + "5200195312%2C52.43435075954755%2C13.3324"
                        + "2416381836%2C52.461762311435194&amp;q=b%"
                        + "C3%A4ckerei'><place place_id='2323884' o"
                        + "sm_type='node' osm_id='352983574' place_"
                        + "rank='30' boundingbox=\"52.443815460205,"
                        + "52.463819274902,13.313097229004,13.33309"
                        + "8182678\" lat='52.4538175' lon='13.32309"
                        + "74' display_name='Bäckerei Mälzer, Schüt"
                        + "zenstraße, Steglitz, Steglitz-Zehlendorf"
                        + ", Berlin, 12165, Deutschland' class='sho"
                        + "p' type='bakery' icon='http://nominatim."
                        + "openstreetmap.org/images/mapicons/shoppi"
                        + "ng_bakery.p.20.png'/><place place_id='13"
                        + "50101' osm_type='node' osm_id='298794800"
                        + "' place_rank='30' boundingbox=\"52.43134"
                        + "9029541,52.451352844238,13.282660713196,"
                        + "13.30266166687\" lat='52.4413499' lon='1"
                        + "3.2926616' display_name='Bäckerei Bertra"
                        + "m, 27, Curtiusstraße, Lichterfelde, Steg"
                        + "litz-Zehlendorf, Berlin, 12205, Deutschl"
                        + "and' class='shop' type='bakery' icon='ht"
                        + "tp://nominatim.openstreetmap.org/images/"
                        + "mapicons/shopping_bakery.p.20.png'/><pla"
                        + "ce place_id='7261519' osm_type='node' os"
                        + "m_id='792690678' place_rank='30' boundin"
                        + "gbox=\"52.434942474365,52.454946289062,1"
                        + "3.282605400085,13.30260635376\" lat='52."
                        + "444945' lon='13.292606' display_name='Kn"
                        + "ese-Bäckerei, Knesebeckstraße, Lichterfe"
                        + "lde, Steglitz-Zehlendorf, Berlin, 12205,"
                        + " Deutschland' class='shop' type='bakery'"
                        + " icon='http://nominatim.openstreetmap.or"
                        + "g/images/mapicons/shopping_bakery.p.20.p"
                        + "ng'/><place place_id='17658198' osm_type"
                        + "='node' osm_id='1655185388' place_rank='"
                        + "30' boundingbox=\"52.426340332031,52.446"
                        + "344146728,13.256445159912,13.27644611358"
                        + "6\" lat='52.4363419' lon='13.2664454' di"
                        + "splay_name='Bäckerei Strauch, Berliner S"
                        + "traße, Zehlendorf, Steglitz-Zehlendorf, "
                        + "Berlin, 14169, Deutschland' class='shop'"
                        + " type='bakery' icon='http://nominatim.op"
                        + "enstreetmap.org/images/mapicons/shopping"
                        + "_bakery.p.20.png'/><place place_id='1622"
                        + "8926' osm_type='node' osm_id='1455112119"
                        + "' place_rank='30' boundingbox=\"52.43713"
                        + "973999,52.457143554687,13.296548118591,1"
                        + "3.316549072266\" lat='52.4471403' lon='1"
                        + "3.3065482' display_name='Bäckerei Hillma"
                        + "nn, 52, Moltkestraße, Lichterfelde, Steg"
                        + "litz-Zehlendorf, Berlin, 12203, Deutschl"
                        + "and' class='shop' type='bakery' icon='ht"
                        + "tp://nominatim.openstreetmap.org/images/"
                        + "mapicons/shopping_bakery.p.20.png'/><pla"
                        + "ce place_id='7825940' osm_type='node' os"
                        + "m_id='803776974' place_rank='30' boundin"
                        + "gbox=\"52.447733154297,52.467736968994,1"
                        + "3.280044784546,13.30004573822\" lat='52."
                        + "4577338' lon='13.2900455' display_name='"
                        + "Wiener Feinbäcker Heberer, Brümmerstraße"
                        + ", Dahlem, Steglitz-Zehlendorf, Berlin, 1"
                        + "4195, Deutschland' class='shop' type='ba"
                        + "kery' icon='http://nominatim.openstreetm"
                        + "ap.org/images/mapicons/shopping_bakery.p"
                        + ".20.png'/><place place_id='8072208' osm_"
                        + "type='node' osm_id='814072915' place_ran"
                        + "k='30' boundingbox=\"52.430979003906,52."
                        + "450982818603,13.279904594421,13.29990554"
                        + "8096\" lat='52.4409802' lon='13.2899047'"
                        + " display_name='Brotmeisterei Steinecke, "
                        + "36-38, Curtiusstraße, Lichterfelde, Steg"
                        + "litz-Zehlendorf, Berlin, 12205, Deutschl"
                        + "and' class='shop' type='bakery' icon='ht"
                        + "tp://nominatim.openstreetmap.org/images/"
                        + "mapicons/shopping_bakery.p.20.png'/><pla"
                        + "ce place_id='16133988' osm_type='node' o"
                        + "sm_id='1391486692' place_rank='30' bound"
                        + "ingbox=\"52.44658493042,52.466588745117,"
                        + "13.310922851563,13.330923805237\" lat='5"
                        + "2.4565867' lon='13.3209229' display_name"
                        + "='Wiedemann, Albrechtstraße, Steglitz, S"
                        + "teglitz-Zehlendorf, Berlin, 12165, Deuts"
                        + "chland' class='shop' type='bakery' icon="
                        + "'http://nominatim.openstreetmap.org/imag"
                        + "es/mapicons/shopping_bakery.p.20.png'/><"
                        + "place place_id='5152777' osm_type='node'"
                        + " osm_id='570034727' place_rank='30' boun"
                        + "dingbox=\"52.441808929443,52.46181274414"
                        + "1,13.320470085144,13.340471038818\" lat="
                        + "'52.4518101' lon='13.3304701' display_na"
                        + "me='Konditorei Rabien, Klingsorstraße, S"
                        + "teglitz, Steglitz-Zehlendorf, Berlin, 12"
                        + "167, Deutschland' class='shop' type='bak"
                        + "ery' icon='http://nominatim.openstreetma"
                        + "p.org/images/mapicons/shopping_bakery.p."
                        + "20.png'/></searchresults>")
                // result = XML_VERSION_1_0_ENCODING_UTF_8
                // +
                // "<searchresults timestamp=\"Tue, 08 Nov 11 22:49:54 -0500\" attribution=\"Data Copyright OpenStreetMap Contributors, Some Rights Reserved. CC-BY-SA 2.0.\" querystring=\"innsbruck\" polygon=\"false\" exclude_place_ids=\"228452,25664166,26135863,25440203\" more_url=\"http://open.mapquestapi.com/nominatim/v1/search?format=xml&amp;exclude_place_ids=228452,25664166,26135863,25440203&amp;accept-language=&amp;q=innsbruck\">\n"
                // + "</searchresults>";
            }
            results = XmlBindingTools.getInstance().unMarshall(
                    result)
            if (results == null) {
                logger.warning("URL: $b, result:$result can't be parsed")
            }
        } catch (e: Exception) {
            logger.fine("Searching for $b gave an error")
            val errorString = e.toString()
            Resources.getInstance().logException(e)
            logger.warning("Result was $result")
            results.addPlace(getErrorPlace(errorString, "ERROR"))
        }
        if (results.listPlaceList.isEmpty()) {
            val textId: String
            textId = if (limitSearchToRegion) {
                "plugins.map.FreeMindMapController.LimitedSearchWithoutResult"
            } else {
                "plugins.map.FreeMindMapController.SearchWithoutResult"
            }
            val messageArguments = arrayOf<Any>(pText)
            val formatter = MessageFormat(
                    mMindMapController!!.getText(textId))
            val message = formatter.format(messageArguments)
            results.addPlace(getErrorPlace(message, "WARNING"))
        }
        return results
    }

    @Throws(MalformedURLException::class, IOException::class, UnsupportedEncodingException::class)
    fun wget(b: String): String? {
        var result: String?
        mMindMapController!!.frame.setWaitingCursor(true)
        try {
            logger.fine("Searching for $b")
            val url = URL(b)
            val urlConnection = url.openConnection()
            if (isAboveJava4) {
                urlConnection.connectTimeout = Resources.getInstance()
                        .getIntProperty(OSM_NOMINATIM_CONNECT_TIMEOUT_IN_MS,
                                10000)
                urlConnection.readTimeout = Resources.getInstance().getIntProperty(
                        OSM_NOMINATIM_READ_TIMEOUT_IN_MS, 30000)
            }
            // set user agent
//			urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:17.0) Gecko/20100101 Firefox/17.0");
//			urlConnection.setRequestProperty("Accept","*/*");
            var status = 200
            if (urlConnection is HttpURLConnection) {
                status = urlConnection.responseCode
            }
            val `in`: InputStream
            `in` = if (status >= 400) (urlConnection as HttpURLConnection).errorStream else urlConnection.getInputStream()
            result = getFile(InputStreamReader(`in`))
            result = String(result!!.toByteArray(), "UTF-8")
            // some proxies are using the Location field to reload the correct document via the proxy
            if (status >= 400 && result.contains("document.location.reload")) {
                val huc = urlConnection as HttpURLConnection
                val newUrl = huc.getHeaderField("Location")
                // again
                logger.info("Received: $result and therefore reload of '$newUrl'.")
                return wget(newUrl)
            }
            logger.info("$result was received for search $b with status $status")
        } finally {
            mMindMapController.frame.setWaitingCursor(false)
        }
        return result
    }

    protected fun getErrorPlace(errorString: String?, errorLevel: String?): Place {
        val place = Place()
        place.displayName = errorString
        place.osmType = errorLevel
        val cursorPosition: Coordinate = map.getCursorPosition()
        place.lat = cursorPosition.lat
        place.lon = cursorPosition.lon
        return place
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    override fun actionPerformed(pE: ActionEvent) {
        var statusText: String? = ""
        // here, we look wether or not the cursor is above a node.
        val mapMarker = checkHit(mTimerMouseEvent)
        if (mapMarker is MapMarkerLocation) {
            val posHolder = mapMarker
                    .nodePositionHolder
            logger.fine("Looking for hit on node $posHolder")
            if (posHolder != null) {
                statusText = getNodeTextHierarchy(posHolder.getNode()!!,
                        mMapHook.getMindMapController()) + ". "
            }
        }
        // calculate the distance to the cursor
        val coordinate = getCoordinateFromMouseEvent(mTimerMouseEvent)
        val cursorPosition: Coordinate = map.getCursorPosition()
        val distance = OsmMercator.getDistance(coordinate.lat,
                coordinate.lon, cursorPosition.lat,
                cursorPosition.lon) / 1000.0
        val messageArguments = arrayOf<Any>(distance, coordinate.lat, coordinate.lon)
        val formatter = MessageFormat(
                mMindMapController!!.getText("plugins/map/MapDialog_Distance"))
        val message = formatter.format(messageArguments)
        statusText += message
        mMapHook.statusLabel.text = statusText
    }

    protected fun selectContextMenuNode() {
        val node: MindMapNode = mCurrentPopupPositionHolder!!.getNode()
        selectNode(node)
    }

    protected fun selectNode(node: MindMapNode?) {
        mMindMapController!!.select(node, getVectorWithSingleElement(node))
    }

    override fun keyTyped(pEvent: KeyEvent) {
        if (mMapHook.isSearchBarVisible) {
            return
        }
        val specialKeyActions = arrayOf(mZoomInAction, mZoomOutAction)
        invokeActionsToKeyboardLayoutDependantCharacters(pEvent,
                specialKeyActions, mMapDialog)
        if (!pEvent.isConsumed && !pEvent.isActionKey
                && Character.isLetter(pEvent.keyChar)
                && pEvent.modifiers and MODIFIERS_WITHOUT_SHIFT == 0) {
            // open search bar and process event.
            // logger.info("Key event processed: " + pEvent);
            mMapHook.toggleSearchBar(pEvent)
            mMapHook.setSingleSearch()
        }
    }

    override fun keyReleased(pEvent: KeyEvent) {}
    override fun keyPressed(pEvent: KeyEvent) {
        if (mMapHook.isSearchBarVisible) {
            return
        }
        val modifiers = pEvent.modifiers and MODIFIERS_WITHOUT_SHIFT
        // only plain of shifted cursor keys are consumed here.
        if (modifiers == 0) {
            var dx = MOVE_PIXEL_AMOUNT
            var dy = MOVE_PIXEL_AMOUNT
            if (pEvent.isShiftDown) {
                dx = (map.width * PAGE_DOWN_FACTOR).toInt()
                dy = (map.height * PAGE_DOWN_FACTOR).toInt()
            }
            when (pEvent.keyCode) {
                KeyEvent.VK_LEFT -> {
                    map.moveMap(-dx, 0)
                    pEvent.consume()
                }
                KeyEvent.VK_RIGHT -> {
                    map.moveMap(dx, 0)
                    pEvent.consume()
                }
                KeyEvent.VK_UP -> {
                    map.moveMap(0, -dy)
                    pEvent.consume()
                }
                KeyEvent.VK_DOWN -> {
                    map.moveMap(0, dy)
                    pEvent.consume()
                }
            }
        }
    }

    var positionHolderIndex: Int
        get() = mPositionHolderIndex
        set(positionHolderIndex) {
            require(checkPositionHolderIndex(positionHolderIndex)) {
                ("Index out of range "
                        + positionHolderIndex)
            }
            mPositionHolderIndex = positionHolderIndex
        }

    /**
     * @param positionHolderIndex
     * @return true, if positionHolderIndex is ok.
     */
    fun checkPositionHolderIndex(positionHolderIndex: Int): Boolean {
        return !(positionHolderIndex < -1 || positionHolderIndex >= positionHolderVector
                .size)
    }

    /**
     * @param pListener
     */
    fun addCursorPositionListener(pListener: CursorPositionListener) {
        mCursorPositionListeners.add(pListener)
    }

    /**
     * @param pSelected
     * @param pPlace
     */
    fun addNode(pSelected: MindMapNode?, pPlace: Place?) {
        addNode(pSelected, pPlace!!.displayName, pPlace.lat,
                pPlace.lon)
    }

    fun addNode(pSelected: MindMapNode?, pText: String?, lat: Double,
                lon: Double) {
        val newNode = insertNewNode(pSelected)
        mMindMapController!!.setNodeText(newNode, pText)
        placeNodeAt(newNode, Coordinate(lat, lon), map.position,
                map.zoom)
    }

    private inner class AddSearchResultsToMapTask(pSelectedRows: IntArray) : FreeMindTask(mMapDialog!!, pSelectedRows.size, MAP_DIALOG_PROGRESS_MESSAGE) {
        private val mPlaces: Array<Place?>

        init {
            // deep copy
            mPlaces = arrayOfNulls(pSelectedRows.size)
            for (i in pSelectedRows.indices) {
                mPlaces[i] = mMapHook.getPlace(pSelectedRows[i])
            }
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see freemind.common.FreeMindTask#processAction()
		 */
        @Throws(Exception::class)
        override fun processAction(): Boolean {
            val selIndex = rounds
            val place = mPlaces[selIndex]
            mProgressDescription = ProgressDescription(
                    MAP_DIALOG_ADD_PLACES, arrayOf<Any?>(place!!.displayName))
            val selected = mindMapController!!.selected
            EventQueue.invokeAndWait { addNode(selected, place) }
            return true
        }
    }

    fun addSearchResultsToMap(pSelectedRows: IntArray) {
        val task = AddSearchResultsToMapTask(
                pSelectedRows)
        task.start()
    }

    private val mindMapController: ModeController?
        private get() = mMindMapController

    companion object {
        /**
         *
         */
        private const val MODIFIERS_WITHOUT_SHIFT = Int.MAX_VALUE xor KeyEvent.SHIFT_MASK
        private const val NODE_MAP_HOME_PROPERTY = "node_map_home"
        private const val MAP_DIALOG_PROGRESS_MESSAGE = "MapDialog.progressMessage"
        private const val MAP_DIALOG_ADD_PLACES = "MapDialog.addPlaces"
        private const val XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        private const val MOUSE_BUTTONS_MASK = (MouseEvent.BUTTON3_DOWN_MASK
                or MouseEvent.BUTTON1_DOWN_MASK or MouseEvent.BUTTON2_DOWN_MASK)
        private const val MAC_MOUSE_BUTTON1_MASK = MouseEvent.BUTTON1_DOWN_MASK
        private const val SCROLL_MARGIN = 5
        private const val SCROLL_PIXEL_AMOUNT = 25
        private const val OSM_NOMINATIM_CONNECT_TIMEOUT_IN_MS = "osm_nominatim_connect_timeout_in_ms"
        private const val OSM_NOMINATIM_READ_TIMEOUT_IN_MS = "osm_nominatim_read_timeout_in_ms"
        private const val MOVE_PIXEL_AMOUNT = 50
        private const val PAGE_DOWN_FACTOR = 0.85f
        private const val POSITION_HOLDER_LIMIT = 50
        private const val WHEEL_ZOOM_MINIMAL_TIME_BETWEEN_CHANGES: Long = 333
        private const val DO_REVERSE_LOOKUP_ON_LAT_LON_SEARCH = "do_reverse_lookup_on_lat_lon_search"
        protected var logger: Logger = Resources
                .getInstance().getLogger("plugins.map.FreeMindMapController")
        private val sTileSources = arrayOf(
                TileSourceStore(Mapnik(),
                        MapNodePositionHolderBase.SHORT_MAPNIK),
                TileSourceStore(HttpMapnik(),
                        MapNodePositionHolderBase.SHORT_MAPNIK),
                TileSourceStore(CycleMap(),
                        MapNodePositionHolderBase.SHORT_CYCLE_MAP),
                TileSourceStore(TransportMap(),
                        MapNodePositionHolderBase.SHORT_TRANSPORT_MAP),
                TileSourceStore(MapQuestOpenMap(),
                        MapNodePositionHolderBase.SHORT_MAP_QUEST_OPEN_MAP) /* , new BingAerialTileSource() license problems.... */
        )

        fun addPictureToNode(positionHolder: MapNodePositionHolder,
                             mindMapController: MindMapController?) {
            val selected: MindMapNode = positionHolder.getNode()
            val addNewNode = mindMapController!!.addNewNode(selected, 0,
                    selected.isLeft)
            mindMapController
                    .setNodeText(addNewNode, positionHolder.getImageHtml())
        }

        /**
         * @param pTileSource
         * @param pMap
         * if found, the map tile source is set. Set null, if you don't
         * want this.
         * @return null, if the string is not found.
         */
        fun changeTileSource(pTileSource: String?,
                             pMap: JMapViewer?): TileSource? {
            logger.fine("Searching for tile source $pTileSource")
            val tileSource = getTileSourceByName(pTileSource)
            if (tileSource != null && pMap != null) {
                pMap.setTileSource(tileSource.mTileSource)
                return tileSource.mTileSource
            }
            return null
        }

        fun getTileSourceByName(sourceName: String?): TileSourceStore? {
            for (i in sTileSources.indices) {
                val source = sTileSources[i]
                if (safeEquals(getTileSourceName(source.mTileSource),
                                sourceName)) {
                    logger.fine("Found  tile source $source")
                    return source
                }
            }
            logger.info("Tile source $sourceName not found!")
            return null
        }

        fun getTileSourceName(source: TileSource): String {
            return source.javaClass.name
        }

        fun getmTileSources(): Array<TileSourceStore> {
            return sTileSources
        }

        fun getLink(hook: MapNodePositionHolder): String {
            val tileSource = hook.tileSource
            val position = hook.position
            val mapCenter = hook.mapCenter
            val zoom = hook.zoom
            return getLink(tileSource, position, mapCenter, zoom)
        }

        fun getLink(tileSource: String?, position: Coordinate?,
                    mapCenter: Coordinate?, zoom: Int): String {
            var layer = "M"
            val tileSourceByName = getTileSourceByName(tileSource)
            if (tileSourceByName != null) {
                layer = tileSourceByName.mLayerName
            }
            /*
		 * The embedded link would work for IE, too. But it is not easy to
		 * configure as a bounding box is necessary. It reads like
		 * osm.org/export/embed.html?bbox=...
		 */
            return ("http://www.openstreetmap.org/?" + "mlat="
                    + position!!.lat + "&mlon=" + position.lon + "&lat="
                    + mapCenter!!.lat + "&lon=" + mapCenter.lon + "&zoom="
                    + zoom + "&layers=" + layer)
        }
    }
}