package plugins.search

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
import java.awt.event.ActionEvent
import org.openstreetmap.gui.jmapviewer.Coordinate
import freemind.common.TextTranslator
import javax.swing.table.AbstractTableModel
import plugins.map.FreeMindMapController.CursorPositionListener
import plugins.map.MapDialog
import freemind.controller.actions.generated.instance.Place
import plugins.map.MapSearchMarkerLocation
import org.openstreetmap.gui.jmapviewer.OsmMercator
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import freemind.main.Tools
import plugins.map.FreeMindMapController
import org.openstreetmap.gui.jmapviewer.tilesources.OsmTileSource.Mapnik
import org.openstreetmap.gui.jmapviewer.OsmTileLoader
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
import java.lang.Runnable
import org.openstreetmap.gui.jmapviewer.events.JMVCommandEvent
import freemind.view.MapModule
import freemind.modes.MindMapNode
import java.util.Collections
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
import java.util.LinkedHashSet
import plugins.script.ScriptEditorPanel.ScriptModel
import plugins.script.ScriptEditorPanel
import freemind.controller.actions.generated.instance.ScriptEditorWindowConfigurationStorage
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
import freemind.main.Tools.StringReaderCreator
import tests.freemind.FreeMindMainMock
import freemind.main.Tools.FileReaderCreator
import plugins.collaboration.socket.StandaloneMindMapMaster
import plugins.collaboration.database.DatabaseBasics.ResultHandler
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter
import org.apache.lucene.queryparser.classic.ParseException
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
import java.io.*
import java.lang.Exception
import java.util.logging.Logger
import javax.swing.*

class SearchViewPanel internal constructor(searchController: ISearchController) : JDialog(searchController.jFrame, "Search Multiple Maps", false), ListSelectionListener {
    private var searchTermsField = JTextField()
    private var rdbtnOpen: JRadioButton? = null
    private var rdbtnDirectorySearch: JRadioButton? = null
    private val directoryButtonGroup = ButtonGroup()
    private var btnChooseDirectoryButton: JButton? = null
    private val selectedDirectoryField = JTextField()
    private val searchControllerHook: ISearchController
    private var selectedDirectory: File? = null
    private val btnGoButton = JButton("Search")
    private var splitPane: JSplitPane? = null
    private var scorePanel: JTextArea? = null
    private var resultsListPane: JScrollPane? = null
    /**
     * Initialize the contents of the frame.
     */
    /**
     *
     */
    private fun initialize() {
        val content = JPanel()
        contentPane = content
        val criteriaPanel = JPanel()
        updateSelectedFolderField()

        // / Bottom pane
        scorePanel = JTextArea("")
        val minimumSize = Dimension(100, 0)
        val listing = arrayOf("No results")
        resultsList = JList(listing)
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        resultsList.setSelectedIndex(0)
        resultsList.addListSelectionListener(this)
        resultsListPane = JScrollPane(resultsList)
        scorePanel!!.minimumSize = minimumSize
        splitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT, resultsListPane,
                scorePanel)
        splitPane!!.setDividerLocation(0.5)
        setMainPanelText("Choose search terms and select go")
        content.layout = BorderLayout(0, 0)
        content.add(criteriaPanel, BorderLayout.NORTH)
        val gbl_criteriaPanel = GridBagLayout()
        gbl_criteriaPanel.columnWidths = intArrayOf(224, 224, 0)
        gbl_criteriaPanel.rowHeights = intArrayOf(25, 25, 25, 0)
        gbl_criteriaPanel.columnWeights = doubleArrayOf(0.0, 0.0, Double.MIN_VALUE)
        gbl_criteriaPanel.rowWeights = doubleArrayOf(0.0, 0.0, 0.0, Double.MIN_VALUE)
        criteriaPanel.layout = gbl_criteriaPanel
        searchTermsField = JTextField()
        searchTermsField.columns = 30
        searchTermsField.minimumSize = searchTermsField.preferredSize

        // React when the user presses Enter while the editor is
        // active. (Tab is handled as specified by
        // JFormattedTextField's focusLostBehavior property.)
        searchTermsField.inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "search")
        searchTermsField.actionMap.put("search", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                try {
                    runSearch()
                } catch (e1: Exception) {
                    _logger!!.warning("Failed:" + e1.localizedMessage)
                }
            }
        })
        val gbc_searchTermsField = GridBagConstraints()
        gbc_searchTermsField.fill = GridBagConstraints.BOTH
        gbc_searchTermsField.insets = Insets(0, 0, 5, 5)
        gbc_searchTermsField.gridx = 0
        gbc_searchTermsField.gridy = 0
        criteriaPanel.add(searchTermsField, gbc_searchTermsField)
        btnGoButton.addActionListener(object : AbstractAction() {
            init {
                putValue(NAME, "Go")
                putValue(SHORT_DESCRIPTION, "Search for the chosen terms")
            }

            override fun actionPerformed(e: ActionEvent) {
                try {
                    runSearch()
                } catch (e1: Exception) {
                    _logger!!.warning("Failed:" + e1.localizedMessage)
                }
            }
        })
        val gbc_btnGoButton = GridBagConstraints()
        gbc_btnGoButton.fill = GridBagConstraints.BOTH
        gbc_btnGoButton.insets = Insets(0, 0, 5, 0)
        gbc_btnGoButton.gridx = 1
        gbc_btnGoButton.gridy = 0
        criteriaPanel.add(btnGoButton, gbc_btnGoButton)
        rdbtnDirectorySearch = JRadioButton("Directory Search")
        rdbtnDirectorySearch!!.isSelected = true
        rdbtnDirectorySearch!!.action = object : AbstractAction() {
            init {
                putValue(NAME, "Directory Search")
                putValue(SHORT_DESCRIPTION,
                        "Choose a folder with Freemind maps")
            }

            override fun actionPerformed(e: ActionEvent) {
                btnChooseDirectoryButton!!.isEnabled = true
                updateSelectedFolderField()
            }
        }
        directoryButtonGroup.add(rdbtnDirectorySearch)
        val gbc_rdbtnDirectorySearch = GridBagConstraints()
        gbc_rdbtnDirectorySearch.fill = GridBagConstraints.BOTH
        gbc_rdbtnDirectorySearch.insets = Insets(0, 0, 5, 5)
        gbc_rdbtnDirectorySearch.gridx = 0
        gbc_rdbtnDirectorySearch.gridy = 1
        criteriaPanel.add(rdbtnDirectorySearch, gbc_rdbtnDirectorySearch)
        rdbtnOpen = JRadioButton("Open Maps")
        rdbtnOpen!!.isSelected = false
        rdbtnOpen!!.action = object : AbstractAction() {
            init {
                putValue(NAME, "Open Maps")
                putValue(SHORT_DESCRIPTION,
                        "Search the maps currently open in the application")
            }

            override fun actionPerformed(e: ActionEvent) {
                btnChooseDirectoryButton!!.isEnabled = false
                btnGoButton.isEnabled = true
            }
        }
        directoryButtonGroup.add(rdbtnOpen)
        val gbc_rdbtnOpen = GridBagConstraints()
        gbc_rdbtnOpen.fill = GridBagConstraints.BOTH
        gbc_rdbtnOpen.insets = Insets(0, 0, 5, 0)
        gbc_rdbtnOpen.gridx = 1
        gbc_rdbtnOpen.gridy = 1
        criteriaPanel.add(rdbtnOpen, gbc_rdbtnOpen)
        btnChooseDirectoryButton = JButton("Choose Directory")
        btnChooseDirectoryButton!!.isEnabled = true
        btnChooseDirectoryButton!!.action = object : AbstractAction() {
            init {
                putValue(NAME, "Choose Directory")
                putValue(SHORT_DESCRIPTION, "Choose Directory")
            }

            override fun actionPerformed(e: ActionEvent) {
                _logger!!.fine("Opened file chooser")
                val fc: JFileChooser = object : JFileChooser() {
                    @Throws(HeadlessException::class)
                    override fun createDialog(parent: Component): JDialog {
                        val dialog = super.createDialog(parent)
                        // config here as needed - just to see a difference
                        dialog.isLocationByPlatform = true
                        // might help - can't know because I can't reproduce the
                        // problem
                        dialog.isAlwaysOnTop = true
                        return dialog
                    }
                }
                fc.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                val retValue = fc.showOpenDialog(null)
                if (retValue == JFileChooser.APPROVE_OPTION) {
                    selectedDirectory = fc.selectedFile
                    _logger.info("Selected : $selectedDirectory")
                } else {
                    _logger.fine("Cancelled")
                }
                updateSelectedFolderField()
            }
        }
        val gbc_btnChooseDirectoryButton = GridBagConstraints()
        gbc_btnChooseDirectoryButton.fill = GridBagConstraints.BOTH
        gbc_btnChooseDirectoryButton.insets = Insets(0, 0, 0, 5)
        gbc_btnChooseDirectoryButton.gridx = 0
        gbc_btnChooseDirectoryButton.gridy = 2
        criteriaPanel.add(btnChooseDirectoryButton,
                gbc_btnChooseDirectoryButton)
        selectedDirectoryField.isEditable = true
        selectedDirectoryField.columns = 10
        val gbc_selectedDirectoryField = GridBagConstraints()
        gbc_selectedDirectoryField.fill = GridBagConstraints.BOTH
        gbc_selectedDirectoryField.gridx = 1
        gbc_selectedDirectoryField.gridy = 2
        criteriaPanel.add(selectedDirectoryField, gbc_selectedDirectoryField)
        content.add(splitPane, BorderLayout.CENTER)
        val width = 600
        val height = 400
        btnGoButton.setSize(10, 10)
        splitPane!!.setSize(width / 2, height / 2)
        criteriaPanel.setSize(width / 2, height / 2)
        content.setSize(width, height)
        setSize(width, height)
        isModal = true
    }

    fun updateSelectedFolderField() {
        if (null == selectedDirectory) {
            selectedDirectory = File(System.getProperty("user.dir"))
        }
        btnGoButton.toolTipText = "Choose a valid folder or open maps before doing search"
        selectedDirectoryField.text = selectedDirectory!!.path
        btnGoButton.isEnabled = true
    }

    @Throws(IOException::class, ParseException::class)
    private fun runSearch() {
        SwingUtilities.getRoot(this).cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
        setMainPanelText("Starting search...")
        try {
            val mapsFiles: Array<File?>?
            val isDirectoryMode = isDirectoryMode
            if (isDirectoryMode) {
                val file = File(selectedDirectoryField.text)
                mapsFiles = arrayOf(file)
                if (!file.exists()) {
                    val message = ("Directory does not exist:"
                            + selectedDirectoryField.text)
                    setMainPanelText(message)
                    throw ParseException(message)
                }
            } else {
                mapsFiles = searchControllerHook.filesOfOpenTabs
            }
            val searchString = searchTermsField.text
            setMainPanelText("Searching [" + Arrays.asList(*mapsFiles)
                    + "] for [" + searchString + "]")
            val search = Search(_logger)
            val listData: Array<Any?>? = search.runSearch(searchString, mapsFiles)
            resultsList.setListData(listData)
            updateScorePanel()
        } finally {
            SwingUtilities.getRoot(this).cursor = Cursor.getDefaultCursor()
        }
    }

    val isDirectoryMode: Boolean
        get() {
            var directoryMode = false
            val mode = directoryButtonGroup
                    .elements
            while (mode.hasMoreElements()) {
                val button = mode.nextElement()
                if (button.text == "Directory Search" && button.isSelected) {
                    directoryMode = true
                }
            }
            return directoryMode
        }
    private var resultsList: JList<*>? = null

    /**
     * Create the application.
     */
    init {
        _logger = searchController.getLogger(SearchViewPanel::class.java)
        searchControllerHook = searchController
        initialize()
    }

    override fun valueChanged(arg0: ListSelectionEvent) {
        updateScorePanel()
        val selectedItem = selectedItem
        if (null != selectedItem) {
            searchControllerHook.openMap(selectedItem.path)
        }
    }

    fun updateScorePanel() {
        val selectedItem = selectedItem
        if (null != selectedItem) {
            setMainPanelText(selectedItem.path)
        }
    }

    val selectedItem: Search.SearchResult?
        get() {
            var selectedItem: Search.SearchResult? = null
            var selectedIndex = resultsList!!.selectedIndex
            if (resultsList!!.model.size > -1) {
                if (selectedIndex < 0) {
                    selectedIndex = 0
                }
                selectedItem = resultsList!!.selectedValue as Search.SearchResult
            }
            return selectedItem
        }

    fun setMainPanelText(text: String?) {
        _logger!!.info("Set panel text to : $text")
        scorePanel!!.text = text
    }

    companion object {
        /**
         *
         */
        private const val serialVersionUID = -4038199355190689628L

        /**
         * Launch the application.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            class TestHook(override val filesOfOpenTabs: Array<File?>) : ISearchController {

                override fun getLogger(className: Class<*>): Logger? {
                    return Logger.getLogger(className.name)
                }

                override fun setWaitingCursor(waiting: Boolean) {
                    // TODO Auto-generated method stub
                }

                override val jFrame: JFrame
                    get() = JFrame()

                override fun openMap(path: String?) {
                    // TODO Auto-generated method stub
                }
            }

            val files = arrayOf<File?>(File("data/freemind.mm"))
            val searchPanel = SearchViewPanel(TestHook(
                    files))
            searchPanel.isVisible = true
        }

        private var _logger: Logger? = null
    }
}