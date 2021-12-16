/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
/*
 * Created on Mar 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package plugins.collaboration.jabber.view

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
import freemind.main.Tools
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
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.FileInputStream
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
import freemind.main.FreeMind
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
import java.io.FileOutputStream
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
import java.io.UnsupportedEncodingException
import freemind.common.FreeMindTask
import freemind.common.FreeMindTask.ProgressDescription
import plugins.map.FreeMindMapController.AddSearchResultsToMapTask
import plugins.map.FreeMindMapController.TileSourceStore
import plugins.map.FreeMindMapController.HttpMapnik
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.CycleMap
import freemind.main.XMLElement
import java.util.Arrays
import java.lang.StringBuffer
import freemind.main.HtmlTools
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
import java.io.BufferedOutputStream
import org.apache.batik.transcoder.TranscoderOutput
import java.io.OutputStreamWriter
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
import java.io.FileNotFoundException
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
import java.io.PrintStream
import plugins.script.ScriptingRegistration
import plugins.script.ScriptingEngine
import freemind.main.Tools.BooleanHolder
import plugins.script.ScriptEditorPanel.ScriptHolder
import plugins.script.ScriptEditor.NodeScriptModel
import freemind.main.FreeMindMain
import groovy.lang.GroovyRuntimeException
import plugins.script.SignedScriptHandler
import plugins.script.ScriptingSecurityManager
import freemind.main.FreeMindSecurityManager
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
import java.io.DataInputStream
import freemind.controller.actions.generated.instance.CollaborationActionBase
import java.io.EOFException
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
import freemind.main.Tools.StringReaderCreator
import tests.freemind.FreeMindMainMock
import java.io.FilenameFilter
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

//TODO: Which deprecated API is used here? Fix this.
/**
 * @author RReppel
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
class MapSharingWizardView : JFrame() {
    //TODO: Hack alert ... For some reason, this whole gaggle here needs to be
    // global, otherwise display doesn't update correctly.
    //Common UI components:
    //TODO: Update resource files.
    var closeBtn = JButton("Close")
    var nextBtn = JButton("Next")
    var backBtn = JButton("Back")
    var wizardButtonPanel = JPanel(SpringLayout()) //A panel

    // showing
    // "Close" "Back"
    // and
    // (optionally)
    // "Next"
    // buttons.
    var closeButtonPanel = JPanel(SpringLayout()) //A panel showing

    // just the
    // "Close" button.
    //ShareMapSelection GUI UI Components:
    //TODO: Update resource files.
    var shareMap = JRadioButton(
            "Share this map with another user.")
    var waitForMapSharing = JRadioButton(
            "Wait for another user to share a map.")
    var shareMapSelectionPanel = JPanel()

    //ConnectToJabberServer GUI UI Components:
    //TODO: Update resource files
    var serverName = JTextField("amessage.de", TEXTFIELD_CHARACTERS)
    var userName = JTextField("", TEXTFIELD_CHARACTERS)
    var password = JPasswordField("", TEXTFIELD_CHARACTERS)
    var connectToServerPanel = JPanel(BorderLayout())

    //SelectUser GUI UI Components:
    var mapShareUserNameLbl = JLabel("Username:")
    var mapShareUserName = JTextField("@amessage.de", TEXTFIELD_CHARACTERS)
    var selectUserPanel = JPanel(BorderLayout())

    //MapShareRequestAcceptDecline GUI UI Components:
    //TODO: Update resource files.
    var acceptBtn = JButton("Accept")
    var declineBtn = JButton("Decline")

    //NotificationMessage GUI UI Components:
    var notificationMessagePanel = JPanel(BorderLayout())
    var notificationMessageDisplayPanel = JPanel()
    var notificationMessage = JLabel()

    //AcceptDeclineMessage GUI UI Components:
    var acceptDeclineMessagePanel = JPanel(BorderLayout())
    var acceptDeclineMessage = JLabel()

    //TODO: Update resource file.
    var acceptDeclineMessageDisplayPanel = JPanel()
    var buttonPanel = JPanel(SpringLayout()) //A panel showing just

    // the "Close" button.
    init {
        setSize(WIDTH, HEIGHT)
        background = BACKGROUNDCOLOR
    }

    fun addCloseButtonMouseListener(
            closeButtonMouseListener: MouseListener?) {
        closeBtn.addMouseListener(closeButtonMouseListener)
    }

    fun addNextButtonMouseListener(nextButtonMouseListener: MouseListener?) {
        nextBtn.addMouseListener(nextButtonMouseListener)
    }

    fun addBackButtonMouseListener(backButtonMouseListener: MouseListener?) {
        backBtn.addMouseListener(backButtonMouseListener)
    }

    fun addAcceptButtonMouseListener(
            acceptButtonMouseListener: MouseListener?) {
        acceptBtn.addMouseListener(acceptButtonMouseListener)
    }

    fun addDeclineButtonMouseListener(
            declineButtonMouseListener: MouseListener?) {
        declineBtn.addMouseListener(declineButtonMouseListener)
    }

    /**
     * This is the GUI where the user chooses between sharing her/his map or
     * waiting for another user to share a map. It's the first screen of the
     * wizard.
     *
     */
    private fun buildShareMapSelectionGui() {
        val shareMapEditPanel = JPanel()
        val group = ButtonGroup()

        //Window title:
        //TODO: Update resource file.
        title = "Share Map"
        shareMapSelectionPanel.setSize(WIDTH, HEIGHT)
        shareMapSelectionPanel.background = BACKGROUNDCOLOR

        //Main edit panel:
        val layout = SpringLayout()
        shareMapEditPanel.layout = layout
        shareMapEditPanel.background = BACKGROUNDCOLOR
        shareMapEditPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT)

        //Radio buttons:
        shareMap.isSelected = true
        shareMap.background = BACKGROUNDCOLOR
        waitForMapSharing.background = BACKGROUNDCOLOR
        group.add(waitForMapSharing)
        group.add(shareMap)
        shareMapEditPanel.add(waitForMapSharing)
        shareMapEditPanel.add(shareMap)
        SpringUtilities.makeCompactGrid(shareMapEditPanel,  //parent
                2, 1,  //rows, cols
                15, 15,  //initX, initY
                10, 10) //xPad, yPad
        shareMapSelectionPanel.add(shareMapEditPanel, BorderLayout.NORTH)
        addWizardButtons(shareMapSelectionPanel, true)
        this.contentPane.add(shareMapSelectionPanel)
    }

    /**
     * This builds the user interface which prompts the user for the Jabber
     * server name, a username and a password.
     */
    private fun buildConnectToServerGui() {
        val userNameLbl = JLabel("Username:")
        val passwordLbl = JLabel("Password:")
        val serverNameLbl = JLabel("Server Name:")
        val connectToServerEditPanel = JPanel()

        //Window title:
        //TODO: Update resource file.
        title = "Connect To Jabber Instant Messaging Server"

        //Main edit panel:
        val layout = SpringLayout()
        connectToServerEditPanel.layout = layout
        serverNameLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT)
        userNameLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT)
        passwordLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT)
        serverName.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT)
        userName.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT)
        password.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT)
        connectToServerEditPanel.background = BACKGROUNDCOLOR
        connectToServerEditPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT)
        connectToServerEditPanel.add(serverNameLbl)
        connectToServerEditPanel.add(serverName)
        connectToServerEditPanel.add(userNameLbl)
        connectToServerEditPanel.add(userName)
        connectToServerEditPanel.add(passwordLbl)
        connectToServerEditPanel.add(password)
        SpringUtilities.makeCompactGrid(connectToServerEditPanel,  //parent
                3, 2,  //rows, cols
                15, 15,  //initX, initY
                10, 10) //xPad, yPad
        connectToServerPanel.add(connectToServerEditPanel, BorderLayout.NORTH)
        addWizardButtons(connectToServerPanel, false)
        this.contentPane.add(connectToServerPanel)
    }

    /**
     * Builds the interface which prompts a user to select somebody to share a
     * map with.
     *
     */
    private fun buildSelectUserGui() {
        val selectUserEditPanel = JPanel()

        //Window title:
        //TODO: Update resource file.
        title = "Select User (example: user@jabber.org)"
        selectUserPanel.background = BACKGROUNDCOLOR

        //Main edit panel:
        val layout = SpringLayout()
        selectUserEditPanel.layout = layout
        mapShareUserNameLbl.setSize(LABEL_WIDTH, LABEL_HEIGHT)
        mapShareUserName.setSize(TEXTFIELD_CHARACTERS, TEXTFIELD_HEIGHT)
        selectUserEditPanel.background = BACKGROUNDCOLOR
        selectUserEditPanel.setSize(WIDTH, HEIGHT - COMMANDPANEL_HEIGHT)
        selectUserEditPanel.add(mapShareUserNameLbl)
        selectUserEditPanel.add(mapShareUserName)
        SpringUtilities.makeCompactGrid(selectUserEditPanel,  //parent
                1, 2,  //rows, cols
                15, 15,  //initX, initY
                10, 10) //xPad, yPad
        selectUserPanel.add(selectUserEditPanel, BorderLayout.NORTH)
        addWizardButtons(selectUserPanel, true)
        this.contentPane.add(selectUserPanel)
    }

    /**
     * Builds the interface which informs the user that a decision to accept map
     * sharing is pending.
     *
     */
    private fun buildNotificationMessageGui(title: String, message: String) {
        //AwaitingMapSharing GUI UI Components:
        //TODO: Update resource file.

        //Window title:
        setTitle(title)
        notificationMessagePanel.background = BACKGROUNDCOLOR

        //Main edit panel:
        notificationMessageDisplayPanel.setSize(WIDTH, HEIGHT
                - COMMANDPANEL_HEIGHT)
        notificationMessageDisplayPanel.background = BACKGROUNDCOLOR
        val layout = SpringLayout()
        notificationMessageDisplayPanel.layout = layout
        notificationMessageDisplayPanel.remove(notificationMessage)
        notificationMessage.setSize(INFO_MESSAGE_WIDTH, INFO_MESSAGE_HEIGHT)
        notificationMessage.background = BACKGROUNDCOLOR
        notificationMessage.text = message
        notificationMessageDisplayPanel.add(notificationMessage)
        SpringUtilities.makeCompactGrid(notificationMessageDisplayPanel,  //parent
                1, 1,  //rows, cols
                50, 50,  //initX, initY
                10, 10) //xPad, yPad
        notificationMessagePanel.add(notificationMessageDisplayPanel,
                BorderLayout.NORTH)
        addCloseButton(notificationMessagePanel)
        this.contentPane.add(notificationMessagePanel)
    }

    private fun buildMapShareAcceptDeclineGui(message: String) {
        acceptDeclineMessage.text = message

        //Window title:
        title = "Map Sharing Request"
        acceptDeclineMessagePanel.background = BACKGROUNDCOLOR

        //Main edit panel:
        acceptDeclineMessageDisplayPanel.setSize(WIDTH, HEIGHT
                - COMMANDPANEL_HEIGHT)
        acceptDeclineMessageDisplayPanel.background = BACKGROUNDCOLOR
        val layout = SpringLayout()
        acceptDeclineMessageDisplayPanel.layout = layout
        acceptDeclineMessage.setSize(INFO_MESSAGE_WIDTH, INFO_MESSAGE_HEIGHT)
        acceptDeclineMessage.background = BACKGROUNDCOLOR
        acceptDeclineMessageDisplayPanel.add(acceptDeclineMessage)
        SpringUtilities.makeCompactGrid(acceptDeclineMessageDisplayPanel,  //parent
                1, 1,  //rows, cols
                50, 50,  //initX, initY
                10, 10) //xPad, yPad
        acceptDeclineMessagePanel.add(acceptDeclineMessageDisplayPanel,
                BorderLayout.NORTH)
        addAcceptDeclineButtons(acceptDeclineMessagePanel)
        this.contentPane.add(acceptDeclineMessagePanel)
    }

    /**
     * Adds the "Close", "Back" and "Next" buttons.
     *
     * @param panel
     * @param showBackButtion
     */
    private fun addWizardButtons(panel: JPanel, showBackButton: Boolean) {
        wizardButtonPanel.setSize(WIDTH, COMMANDPANEL_HEIGHT)
        wizardButtonPanel.background = BACKGROUNDCOLOR
        wizardButtonPanel.add(closeBtn)
        wizardButtonPanel.add(backBtn)
        backBtn.isVisible = showBackButton
        wizardButtonPanel.add(nextBtn)
        SpringUtilities.makeCompactGrid(wizardButtonPanel,  //parent
                1, 3,  //rows, cols
                80, 15,  //initX, initY
                30, 10) //xPad, yPad
        panel.add(wizardButtonPanel, BorderLayout.SOUTH)
    }

    /**
     * Adds the "Close", "Back" and "Next" buttons.
     *
     * @param panel
     */
    private fun addCloseButton(panel: JPanel) {
        closeButtonPanel = JPanel(SpringLayout()) //A panel showing
        // just the "Close"
        // button.
        closeButtonPanel.setSize(WIDTH, COMMANDPANEL_HEIGHT)
        closeButtonPanel.background = BACKGROUNDCOLOR
        closeButtonPanel.add(closeBtn)
        SpringUtilities.makeCompactGrid(closeButtonPanel,  //parent
                1, 1,  //rows, cols
                80 + closeBtn.width + 30, 15,  //initX, initY
                30, 10) //xPad, yPad
        panel.add(closeButtonPanel, BorderLayout.SOUTH)
    }

    /**
     * Adds the "Accept" and "Decline" buttons.
     *
     * @param panel
     */
    private fun addAcceptDeclineButtons(panel: JPanel) {
        buttonPanel.background = BACKGROUNDCOLOR
        buttonPanel.add(acceptBtn)
        buttonPanel.add(declineBtn)
        SpringUtilities.makeCompactGrid(buttonPanel,  //parent
                1, 2,  //rows, cols
                80 + closeBtn.width + 30, 15,  //initX, initY
                30, 10) //xPad, yPad
        panel.add(buttonPanel, BorderLayout.SOUTH)
    }

    /**
     * Removes any previous user interfaces shown on the wizard's content pane
     *
     */
    private fun removeUIs() {
        this.contentPane.remove(shareMapSelectionPanel)
        this.contentPane.remove(connectToServerPanel)
        this.contentPane.remove(selectUserPanel)
        this.contentPane.remove(notificationMessagePanel)
        this.contentPane.remove(acceptDeclineMessagePanel)
        shareMapSelectionPanel = JPanel()
        connectToServerPanel = JPanel(BorderLayout())
        selectUserPanel = JPanel(BorderLayout())
        notificationMessagePanel = JPanel(BorderLayout())
        notificationMessage = JLabel()
        notificationMessageDisplayPanel = JPanel()
        acceptDeclineMessagePanel = JPanel(BorderLayout())
        acceptDeclineMessage = JLabel()
        acceptDeclineMessageDisplayPanel = JPanel()
        wizardButtonPanel = JPanel(SpringLayout()) //A panel showing
        // "Close" "Back"
        // and (optionally)
        // "Next" buttons.
        buttonPanel = JPanel(SpringLayout()) //A panel showing just
        // the "Close" button.
    }

    fun showMapSharingSelectionDialog() {
        //Start with giving the user a choice between sharing his/her own map
        // or waiting
        //for another user to share a map:
        //TODO: "RemoveUIs", etc. should be in "BuildShareMapSelectionGui".
        removeUIs()
        buildShareMapSelectionGui()
        show()
        this.repaint()
    }

    fun showConnectToServerDialog() {
        removeUIs()
        buildConnectToServerGui()
        show()
        this.repaint()
    }

    fun showSelectUserDialog() {
        removeUIs()
        buildSelectUserGui()
        show()
        this.repaint()
    }

    fun showAwaitMapSharingMessage() {
        removeUIs()
        //TODO: Update resource files.
        buildNotificationMessageGui("Share Map",
                "Waiting for user to accept invitation to share map...")
        show()
        this.repaint()
    }

    fun showSharingAcceptedMessage(userName: String) {
        //TODO: Update resource files.
        val message = ("You are now sharing the current map with " + userName
                + ".")
        removeUIs()
        //TODO: Update resource files.
        hide()
        buildNotificationMessageGui("Sharing Map", message)
        show()
        this.repaint()
    }

    fun showSharingDeclinedMessage(userName: String) {
        //TODO: Update resource files.
        val message = (userName
                + " has declined your invitation to share a map.")
        removeUIs()
        //TODO: Update resource files.
        buildNotificationMessageGui("Declined Sharing Map", message)
        show()
        this.repaint()
    }

    fun showSharingStoppedMessage(userName: String) {
        //TODO: Update resource files.
        val message = "$userName has stopped map sharing."
        removeUIs()
        //TODO: Update resource files.
        buildNotificationMessageGui("Stopped Sharing Map", message)
        show()
        this.repaint()
    }

    fun showMapShareAcceptDeclineMessage(userName: String?) {
        //TODO: Update resource files.
        val message = "$userName wants to share a map with you."
        removeUIs()
        //TODO: Update resource files.
        buildMapShareAcceptDeclineGui(message)
        show()
        this.repaint()
    }
    // Properties:
    /**
     * True if the user has chosen to share his own map with another user.
     *
     * @return
     */
    val isShareMapSelected: Boolean
        get() = shareMap.isSelected

    /**
     * True if the user has chosen to wait for another user to share a map.
     *
     * @return
     */
    val isWaitForMapSharingSelected: Boolean
        get() = waitForMapSharing.isSelected

    /**
     * The name of the Jabber server to which to connect.
     *
     * @return
     */
    val servername: String
        get() = serverName.text

    /**
     * The name with which the user with which the user should connect to the
     * Jabber server.
     *
     * @return
     */
    val username: String
        get() = userName.text

    /**
     * The password for connecting to the Jabber server.
     *
     * @return
     */
    fun getPassword(): String {
        return password.text
    }

    /**
     * The user name of the use with whom to share a map, e.g.
     * "johnsmith@chat.jabbermind.com".
     *
     * @return
     */
    fun getMapShareUserName(): String {
        return mapShareUserName.text
    }

    companion object {
        private const val INFO_MESSAGE_HEIGHT = 20
        private const val INFO_MESSAGE_WIDTH = 120
        private val BACKGROUNDCOLOR = Color.lightGray
        private const val WIDTH = 400
        private const val HEIGHT = 180
        private const val BUTTON_WIDTH = 60
        private const val BUTTON_HEIGHT = 20
        private const val LABEL_WIDTH = 80
        private const val LABEL_HEIGHT = 20
        private const val TEXTFIELD_CHARACTERS = 25
        private const val TEXTFIELD_HEIGHT = 20
        private const val COMMANDPANEL_HEIGHT = 40
    }
}