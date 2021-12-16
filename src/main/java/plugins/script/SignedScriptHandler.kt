/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2008 Christian Foltin and others.
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
 * Created on 16.04.2008
 */
/*$Id: SignedScriptHandler.java,v 1.1.2.2 2008/04/18 21:18:27 christianfoltin Exp $*/
package plugins.script

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
import javax.swing.JLabel
import freemind.modes.mindmapmode.MindMapController
import javax.swing.JDialog
import plugins.map.MapNodePositionHolder
import plugins.map.MapMarkerLocation
import javax.swing.JPanel
import javax.swing.JSplitPane
import javax.swing.JTextField
import javax.swing.JTable
import plugins.map.MapDialog.ResultTableModel
import java.awt.Color
import java.awt.event.ActionEvent
import org.openstreetmap.gui.jmapviewer.Coordinate
import freemind.common.TextTranslator
import javax.swing.table.AbstractTableModel
import plugins.map.FreeMindMapController.CursorPositionListener
import plugins.map.MapDialog
import freemind.controller.actions.generated.instance.Place
import plugins.map.MapSearchMarkerLocation
import org.openstreetmap.gui.jmapviewer.OsmMercator
import javax.swing.WindowConstants
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import plugins.map.FreeMindMapController
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.Mapnik
import org.openstreetmap.gui.jmapviewer.OsmTileLoader
import java.awt.BorderLayout
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JButton
import freemind.common.ScalableJTable
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import java.awt.event.ActionListener
import java.awt.event.MouseListener
import java.awt.event.MouseAdapter
import javax.swing.JScrollPane
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
import java.awt.image.RenderedImage
import org.openstreetmap.gui.jmapviewer.TileController
import freemind.modes.ModeController
import freemind.modes.MindMap
import freemind.extensions.HookRegistration
import freemind.modes.mindmapmode.actions.xml.ActorXml
import org.openstreetmap.gui.jmapviewer.interfaces.TileLoaderListener
import freemind.controller.MenuItemEnabledListener
import java.util.TimerTask
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
import javax.swing.JMenuItem
import freemind.modes.mindmapmode.actions.NodeHookAction
import plugins.map.SearchInMapForNodeTextAction
import plugins.map.ShowMapToNodeAction
import plugins.map.AddLinkToMapAction
import plugins.map.RemoveMapToNodeAction
import plugins.map.AddMapImageToNodeAction
import plugins.map.Registration.CachePurger
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker
import plugins.map.MapMarkerBase
import java.awt.Graphics
import org.openstreetmap.gui.jmapviewer.Layer
import java.awt.Stroke
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker.STYLE
import org.openstreetmap.gui.jmapviewer.JMapViewer
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource
import plugins.map.JCursorMapViewer.ScaledTile
import java.awt.RenderingHints
import plugins.map.JCursorMapViewer.ScalableTileController
import java.awt.Rectangle
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter
import org.openstreetmap.gui.jmapviewer.JMapController
import java.awt.event.MouseMotionListener
import java.awt.event.MouseWheelListener
import java.awt.event.KeyListener
import javax.swing.JPopupMenu
import org.openstreetmap.gui.jmapviewer.tilesources.AbstractOsmTileSource
import plugins.map.FreeMindMapController.TransportMap
import org.openstreetmap.gui.jmapviewer.interfaces.TileSource.TileUpdate
import plugins.map.FreeMindMapController.MapQuestOpenMap
import freemind.view.mindmapview.EditNodeBase.EditControl
import javax.swing.JComponent
import freemind.view.mindmapview.EditNodeTextField
import freemind.controller.MenuItemSelectedListener
import plugins.map.FreeMindMapController.MaxmimalZoomToCursorAction
import java.awt.datatransfer.StringSelection
import freemind.extensions.ExportHook
import javax.swing.KeyStroke
import freemind.modes.common.plugins.MapNodePositionHolderBase
import plugins.map.FreeMindMapController.MapEditTextFieldControl
import plugins.map.FreeMindMapController.MapEditNoteTextField
import java.awt.Cursor
import javax.swing.event.PopupMenuListener
import javax.swing.event.PopupMenuEvent
import plugins.map.FreeMindMapController.MoveForwardAction
import plugins.map.FreeMindMapController.MoveBackwardAction
import javax.swing.JMenuBar
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
import javax.swing.JMenu
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
import javax.swing.JOptionPane
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
import javax.swing.JComboBox
import javax.swing.JRadioButton
import javax.swing.ButtonGroup
import java.awt.Dimension
import javax.swing.BorderFactory
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
import javax.swing.JTextArea
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter
import java.util.LinkedHashSet
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
import javax.swing.JList
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
import javax.swing.JFileChooser
import java.awt.HeadlessException
import javax.swing.SwingUtilities
import java.util.Enumeration
import javax.swing.AbstractButton
import kotlin.jvm.JvmStatic
import javax.swing.JFrame
import javax.swing.SpringLayout
import java.lang.ClassCastException
import plugins.collaboration.jabber.view.SpringUtilities
import plugins.collaboration.jabber.view.MapSharingWizardView
import javax.swing.JPasswordField
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
import freemind.controller.actions.generated.instance.CollaborationActionBase
import freemind.controller.actions.generated.instance.CollaborationTransaction
import plugins.collaboration.socket.MindMapMaster
import freemind.controller.actions.generated.instance.CollaborationWhoAreYou
import freemind.controller.actions.generated.instance.CollaborationOffers
import freemind.controller.actions.generated.instance.CollaborationHello
import javax.swing.AbstractListModel
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
import java.security.Signature
import java.security.cert.Certificate
import java.util.regex.Pattern

/**
 * @author foltin
 */
class SignedScriptHandler {
    class ScriptContents() {
        var mScript: String? = null
        var mSignature: String? = null
        var mKeyName: String? = null

        init {
            if (sSignWithKeyPattern == null) sSignWithKeyPattern = Pattern.compile(SIGN_PREFIX_REGEXP)
        }

        constructor(pScript: String?) : this() {
            val indexOfSignaturePrefix = pScript!!.lastIndexOf(SIGN_PREFIX)
            val indexOfSignature = (indexOfSignaturePrefix
                    + SIGN_PREFIX.length)
            if (indexOfSignaturePrefix > 0
                    && pScript.length > indexOfSignature) {
                mSignature = pScript.substring(indexOfSignature)
                mScript = pScript.substring(0, indexOfSignaturePrefix)
                mKeyName = null
            } else {
                val matcher = sSignWithKeyPattern!!.matcher(pScript)
                if (matcher.find()) {
                    mScript = pScript.substring(0, matcher.start())
                    mKeyName = matcher.group(1)
                    mSignature = matcher.group(2)
                } else {
                    mSignature = null
                    mScript = pScript
                    mKeyName = null
                }
            }
        }

        override fun toString(): String {
            val prefix: String
            prefix = if (mKeyName != null) "//SIGN($mKeyName):" else SIGN_PREFIX
            return """
                $mScript$prefix$mSignature
                
                """.trimIndent()
        }

        companion object {
            private var sSignWithKeyPattern: Pattern? = null
        }
    }

    private fun initializeKeystore(pPassword: CharArray?) {
        if (mKeyStore != null) return
        var fis: FileInputStream? = null
        try {
            mKeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            fis = FileInputStream(System.getProperty("user.home")
                    + File.separator + ".keystore")
            mKeyStore.load(fis, pPassword)
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                    Resources.getInstance().logException(e)
                }
            }
        }
    }

    fun signScript(pScript: String?, pTranslator: TextTranslator?,
                   pFrame: FreeMindMain): String? {
        val content = ScriptContents(pScript)
        // it is assumed, that keystore and key password are identical.
        val pwdDialog = EnterPasswordDialog(
                pFrame.jFrame, pTranslator, false)
        pwdDialog.isModal = true
        pwdDialog.isVisible = true
        if (pwdDialog.result == EnterPasswordDialog.CANCEL) {
            return content.mScript
        }
        val password = pwdDialog.password.toString().toCharArray()
        initializeKeystore(password)
        try {
            val instance = Signature.getInstance("SHA1withDSA")
            var keyName: String? = FREEMIND_SCRIPT_KEY_NAME
            val propertyKeyName: String = Resources.getInstance().getProperty(
                    FreeMind.RESOURCES_SCRIPT_USER_KEY_NAME_FOR_SIGNING)
            if (content.mKeyName != null) {
                keyName = content.mKeyName
            } else if (propertyKeyName != null && propertyKeyName.length > 0) {
                content.mKeyName = propertyKeyName
                keyName = content.mKeyName
            }
            instance.initSign(mKeyStore!!.getKey(keyName, password) as PrivateKey)
            instance.update(content.mScript!!.toByteArray())
            val signature = instance.sign()
            content.mSignature = toBase64(signature)
            // System.out.println("Signed: " +content);
            return content.toString()
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            pFrame.controller!!.errorMessage(e.localizedMessage)
        }
        return content.mScript
    }

    fun isScriptSigned(pScript: String?, pOutStream: OutputStream): Boolean {
        val content = ScriptContents(pScript)
        if (content.mSignature != null) {
            try {
                val instanceVerify = Signature.getInstance("SHA1withDSA")
                if (content.mKeyName == null) {
                    /**
                     * This is the FreeMind public key. keytool -v -rfc
                     * -exportcert -alias freemindscriptkey
                     */
                    val cer = """
                        -----BEGIN CERTIFICATE-----
                        MIIDKDCCAuWgAwIBAgIESAY2ADALBgcqhkjOOAQDBQAwdzELMAkGA1UEBhMCREUxCzAJBgNVBAgTAkRFMRMwEQYDVQQHEwpPcGVuU291cmNlMRgwFgYDVQQKEw9zb3VyY2Vmb3JnZS5uZXQxETAPBgNVBAsTCEZyZWVNaW5kMRkwFwYDVQQDExBDaHJpc3RpYW4gRm9sdGluMB4XDTA4MDQxNjE3MjMxMloXDTA4MDcxNTE3MjMxMlowdzELMAkGA1UEBhMCREUxCzAJBgNVBAgTAkRFMRMwEQYDVQQHEwpPcGVuU291cmNlMRgwFgYDVQQKEw9zb3VyY2Vmb3JnZS5uZXQxETAPBgNVBAsTCEZyZWVNaW5kMRkwFwYDVQQDExBDaHJpc3RpYW4gRm9sdGluMIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAZm5z5EZXVhtye5jY3X9w24DJ3yNJbNl2tfkOBIc0KfgyxONTSJKtUpmLI3btUxy3pQf/T8BShlY3PAC0fp3MeDG8WRq1wM3luLd1V9SS8EG6tPJBZ3mciCUymTT7n9CZNzATIpqNIXHSD/wljRABedUi8PMg4KbVPnhu6Y6b1uAwCwYHKoZIzjgEAwUAAzAAMC0CFQCFHGwe+HHOvY0MmKYHbiq7fRxMGwIUC0voAGYUu6vgVFqdLI5F96JLTqk=
                        -----END CERTIFICATE-----
                        
                        """.trimIndent()
                    val cf = CertificateFactory
                            .getInstance("X.509")
                    val c = cf.generateCertificates(ByteArrayInputStream(cer.toByteArray()))
                    val i: Iterator<Certificate> = c.iterator()
                    if (i.hasNext()) {
                        val cert = i.next()
                        instanceVerify.initVerify(cert)
                    } else {
                        throw IllegalArgumentException(
                                "Internal certificate wrong.")
                    }
                } else {
                    initializeKeystore(null)
                    instanceVerify.initVerify(mKeyStore
                            .getCertificate(content.mKeyName))
                }
                instanceVerify.update(content.mScript!!.toByteArray())
                // System.out.println("Signature result: " + verify);
                return instanceVerify.verify(fromBase64(content.mSignature!!))
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
                try {
                    pOutStream.write(e.toString().toByteArray())
                    pOutStream.write("\n".toByteArray())
                } catch (e1: Exception) {
                    Resources.getInstance().logException(e1)
                }
            }
        }
        return false
    }

    companion object {
        const val FREEMIND_SCRIPT_KEY_NAME = "FreeMindScriptKey"
        private const val SIGN_PREFIX = "//SIGN:"

        /** This is for / /SIGN(keyname):signature  */
        private const val SIGN_PREFIX_REGEXP = "//SIGN\\((.*?)\\):(.*)"
        private var mKeyStore: KeyStore? = null
    }
}