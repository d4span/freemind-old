package plugins.map

import accessories.plugins.time.TableSorter
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
import java.awt.image.ImageObserver
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.FileInputStream
import java.awt.image.RenderedImage
import org.openstreetmap.gui.jmapviewer.TileController
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
import java.io.FileOutputStream
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
import plugins.collaboration.socket.SocketBasics
import freemind.controller.actions.generated.instance.CollaborationUserInformation
import freemind.controller.actions.generated.instance.HookNodeAction
import plugins.collaboration.socket.SocketBasics.UnableToGetLockException
import plugins.collaboration.socket.ServerCommunication
import java.lang.IllegalStateException
import freemind.main.Tools.ReaderCreator
import freemind.modes.mindmapmode.MindMapMapModel
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
import freemind.modes.mindmapmode.MindMapNodeModel
import freemind.controller.actions.generated.instance.CollaborationMapOffer
import freemind.main.*
import freemind.main.Tools.StringReaderCreator
import tests.freemind.FreeMindMainMock
import java.io.FilenameFilter
import freemind.main.Tools.FileReaderCreator
import freemind.modes.*
import plugins.collaboration.socket.StandaloneMindMapMaster
import plugins.collaboration.database.DatabaseBasics.ResultHandler
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter
import freemind.view.ImageFactory
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
import java.lang.IllegalArgumentException
import java.util.*
import javax.swing.*

//License: GPL. Copyright 2008 by Jan Peter Stotz
/**
 *
 * Demonstrates the usage of [JMapViewer]
 *
 * @author Jan Peter Stotz adapted for FreeMind by Chris.
 */
class MapDialog : MindMapHookAdapter(), JMapViewerEventListener, MapModuleChangeObserver, MapNodePositionListener, NodeSelectionListener, NodeVisibilityListener {
    var map: JCursorMapViewer? = null
        private set
    private val zoomValue: JLabel? = null
    private val mperpLabelValue: JLabel? = null

    /**
     * Overwritten, as this dialog is not modal, but after the plugin has
     * terminated, the dialog is still present and needs the controller to store
     * its values.
     */
    override var mindMapController: MindMapController? = null
        private set
    var mapDialog: JDialog? = null
        private set
    private val mMarkerMap = HashMap<MapNodePositionHolder?, MapMarkerLocation>()
    var closeAction: CloseAction? = null
        private set
    private var mSearchFieldPanel: JPanel? = null
    private var mSearchSplitPane: JSplitPane? = null
    var isSearchBarVisible = false
        private set
    private var mSearchPanel: JPanel? = null
    private var mSearchTerm: JTextField? = null
    var statusLabel: JLabel? = null
        private set

    /**
     * Indicates that after a search, when a place was selected, the search
     * dialog should close
     */
    private var mSingleSearch = false
    private var mResultTable: JTable? = null
    private var mResultTableModel: ResultTableModel? = null
    private var mResultTableSorter: TableSorter? = null
    private var mTableOriginalBackgroundColor: Color? = null

    /**
     * I know, that the JSplitPane collects this information, but I want to
     * handle it here.
     */
    private var mLastDividerPosition = 300

    /**
     * @return
     */
    var isLimitSearchToRegion = false
        private set
    private var mSearchStringLabel: JLabel? = null
    private var mResourceSearchLocationString: String? = null
    private var mResourceSearchString: String? = null

    inner class CloseAction : AbstractAction(getResourceString("MapDialog_close")) {
        override fun actionPerformed(arg0: ActionEvent) {
            disposeDialog()
        }
    }

    /**
     * @author foltin
     * @date 25.04.2012
     */
    inner class ResultTableModel(pCursorCoordinate: Coordinate?,
                                 pTextTranslator: TextTranslator?) : AbstractTableModel(), CursorPositionListener {
        /**
         *
         */
        private val COLUMNS = arrayOf(
                SEARCH_DESCRIPTION_COLUMN_TEXT, SEARCH_DISTANCE_COLUMN_TEXT)
        var mData = Vector<Place>()
        private var mCursorCoordinate: Coordinate? = Coordinate(0, 0)
        private val mMapSearchMarkerLocationHash = HashMap<Place, MapSearchMarkerLocation>()
        private val mTextTranslator: TextTranslator?

        /**
         * @param pCursorCoordinate
         */
        init {
            mCursorCoordinate = pCursorCoordinate
            mTextTranslator = pTextTranslator
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
        override fun getColumnClass(arg0: Int): Class<*> {
            return when (arg0) {
                SEARCH_DESCRIPTION_COLUMN -> String::class.java
                SEARCH_DISTANCE_COLUMN -> Double::class.java
                else -> Any::class.java
            }
        }

        /**
         * @param pPlace
         */
        fun addPlace(pPlace: Place) {
            mData.add(pPlace)
            val row = mData.size - 1
            val location = MapSearchMarkerLocation(
                    this@MapDialog, pPlace)
            mMapSearchMarkerLocationHash[pPlace] = location
            map!!.addMapMarker(location)
            fireTableRowsInserted(row, row)
        }

        fun getMapSearchMarkerLocation(index: Int): MapSearchMarkerLocation? {
            if (index >= 0 && index < rowCount) {
                val place = getPlace(index)
                return mMapSearchMarkerLocationHash[place]
            }
            throw IllegalArgumentException("Index " + index
                    + " is out of range.")
        }

        fun getPlace(pIndex: Int): Place {
            return mData[pIndex] as Place
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
        override fun getColumnName(pColumn: Int): String {
            return mTextTranslator!!.getText(COLUMNS[pColumn])
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
        override fun getRowCount(): Int {
            return mData.size
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
        override fun getColumnCount(): Int {
            return 2
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
        override fun getValueAt(pRowIndex: Int, pColumnIndex: Int): Any {
            val place = getPlace(pRowIndex)
            when (pColumnIndex) {
                SEARCH_DISTANCE_COLUMN -> {
                    val value = OsmMercator.getDistance(
                            mCursorCoordinate!!.lat, mCursorCoordinate!!.lon,
                            place.lat, place.lon) / 1000.0
                    return if (java.lang.Double.isInfinite(value) || java.lang.Double.isNaN(value)) {
                        java.lang.Double.valueOf(-1.0)
                    } else value
                }
                SEARCH_DESCRIPTION_COLUMN -> return place.displayName!!
            }
            return null
        }

        /**
         *
         */
        fun clear() {
            // clear old search results:
            for (place in mMapSearchMarkerLocationHash.keys) {
                map!!.removeMapMarker(mMapSearchMarkerLocationHash[place])
            }
            mMapSearchMarkerLocationHash.clear()
            mData.clear()
            fireTableDataChanged()
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see plugins.map.FreeMindMapController.CursorPositionListener#
		 * cursorPositionChanged(org.openstreetmap.gui.jmapviewer.Coordinate)
		 */
        override fun cursorPositionChanged(pCursorPosition: Coordinate?) {
            mCursorCoordinate = pCursorPosition
            fireTableDataChanged()
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.HookAdapter#startupMapHook()
	 */
    override fun startupMapHook() {
        super.startupMapHook()
        mindMapController = super.mindMapController
        mindMapController!!.controller!!.mapModuleManager
                .addListener(this)
        mapDialog = JDialog(getController()!!.frame.getJFrame(), false /* unmodal */)
        mapDialog!!.title = getResourceString("MapDialog_title")
        mapDialog
                .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE)
        mapDialog!!.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                disposeDialog()
            }
        })
        closeAction = CloseAction()
        // the action title is changed by the following method, thus we create
        // another close action.
        addEscapeActionToDialog(mapDialog!!, CloseAction())
        mapDialog!!.setSize(400, 400)
        map = JCursorMapViewer(mindMapController, mapDialog,
                registration.tileCache, this)
        map!!.addJMVListener(this)
        map!!.isScrollWrapEnabled = true
        FreeMindMapController.Companion.changeTileSource(Mapnik::class.java.name, map)
        val loader = registration.createTileLoader(map)
        map!!.setTileLoader(loader)
        map.setCursorPosition(Coordinate(49.8, 8.8))
        map.setUseCursor(true)
        mapDialog!!.layout = BorderLayout()
        mSearchPanel = JPanel(BorderLayout())
        mResourceSearchString = getResourceString("MapDialog_Search")
        mResourceSearchLocationString = getResourceString("MapDialog_Search_Location")
        mSearchStringLabel = JLabel(mResourceSearchString)
        mSearchTerm = JTextField()
        mSearchTerm!!.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(pEvent: KeyEvent) {
                if (pEvent.keyCode == KeyEvent.VK_DOWN
                        && pEvent.modifiers == 0) {
                    logger!!.info("Set Focus to search list.")
                    mResultTable!!.requestFocusInWindow()
                    mResultTable!!.selectionModel.setSelectionInterval(0, 0)
                    pEvent.consume()
                }
            }
        })
        mSearchFieldPanel = JPanel()
        mSearchFieldPanel!!.layout = BorderLayout(10, 0)
        val clearButton = JButton(ImageFactory.getInstance().createIcon(Resources.getInstance()
                .getResource("images/clear_box.png")))
        clearButton.isFocusable = false
        mSearchFieldPanel!!.add(mSearchStringLabel, BorderLayout.WEST)
        mSearchFieldPanel!!.add(mSearchTerm, BorderLayout.CENTER)
        mSearchFieldPanel!!.add(clearButton, BorderLayout.EAST)
        mResultTable = ScalableJTable()
        mTableOriginalBackgroundColor = mResultTable.getBackground()
        mResultTable
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
        mResultTable.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(pEvent: KeyEvent) {
                val index = mResultTable.getSelectedRow()
                if (index == 0 && pEvent.keyCode == KeyEvent.VK_UP && pEvent.modifiers == 0) {
                    logger!!.info("Set Focus to search item.")
                    mResultTable.clearSelection()
                    mSearchTerm!!.requestFocusInWindow()
                    pEvent.consume()
                    return
                }
                if (pEvent.keyCode == KeyEvent.VK_ENTER && pEvent.modifiers == 0 && index >= 0) {
                    logger!!.info("Set result in map.")
                    pEvent.consume()
                    displaySearchItem(index)
                    return
                }
                if (pEvent.keyCode == KeyEvent.VK_ENTER && pEvent.isControlDown && index >= 0) {
                    pEvent.consume()
                    addSearchResultsToMap()
                    displaySearchItem(index)
                    return
                }
            }
        })
        mResultTable.getSelectionModel().addListSelectionListener(
                object : ListSelectionListener {
                    override fun valueChanged(pE: ListSelectionEvent) {
                        clearIndexes()
                        val selectedRow = mResultTable.getSelectedRow()
                        if (selectedRow >= 0
                                && selectedRow < mResultTableSorter
                                        .getRowCount()) {
                            var index = selectedRow
                            index = mResultTableSorter!!.modelIndex(index)
                            val marker = mResultTableModel
                                    .getMapSearchMarkerLocation(index)
                            marker.setSelected(true)
                        }
                        mResultTable.repaint()
                        map!!.repaint()
                    }

                    private fun clearIndexes() {
                        for (i in 0 until mResultTableModel!!.rowCount) {
                            val marker = mResultTableModel
                                    .getMapSearchMarkerLocation(i)
                            marker.setSelected(false)
                        }
                    }
                })
        mResultTable.getTableHeader().reorderingAllowed = false
        mResultTableModel = ResultTableModel(map.getCursorPosition(),
                mindMapController)
        freeMindMapController!!.addCursorPositionListener(mResultTableModel!!)
        mResultTableSorter = TableSorter(mResultTableModel)
        mResultTable.setModel(mResultTableSorter)
        mResultTableSorter!!.tableHeader = mResultTable.getTableHeader()
        mResultTableSorter!!.setColumnComparator(String::class.java, TableSorter.LEXICAL_COMPARATOR)
        mResultTableSorter!!.setColumnComparator(Double::class.java, TableSorter.COMPARABLE_COMAPRATOR)
        // Sort by default by date.
        mResultTableSorter!!.setSortingStatus(SEARCH_DISTANCE_COLUMN,
                TableSorter.ASCENDING)
        clearButton.addActionListener(ActionListener {
            mResultTableModel!!.clear()
            mSearchTerm!!.text = ""
            mResultTable.setBackground(mTableOriginalBackgroundColor)
        })
        val mouseListener: MouseListener = object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (e.clickCount == 2) {
                    // int index = mResultTable.locationToIndex(e.getPoint());
                    val index = mResultTable.getSelectedRow()
                    displaySearchItem(index)
                }
            }
        }
        mResultTable.addMouseListener(mouseListener)
        mSearchTerm!!.addActionListener { search(mSearchTerm!!.text, false) }
        val resultTableScrollPane = JScrollPane(mResultTable)
        mSearchPanel!!.layout = BorderLayout()
        mSearchPanel!!.add(mSearchFieldPanel, BorderLayout.NORTH)
        mSearchPanel!!.add(resultTableScrollPane, BorderLayout.CENTER)
        isSearchBarVisible = true
        mSearchSplitPane = JSplitPane(JSplitPane.VERTICAL_SPLIT,
                mSearchPanel, map)
        mSearchSplitPane!!.isContinuousLayout = true
        mSearchSplitPane!!.isOneTouchExpandable = false
        correctJSplitPaneKeyMap()
        mSearchSplitPane!!.resizeWeight = 0.0
        mSearchSplitPane!!.addPropertyChangeListener(
                JSplitPane.DIVIDER_LOCATION_PROPERTY
        ) {
            val dividerLocation = mSearchSplitPane!!
                    .dividerLocation
            if (dividerLocation > 1) {
                mLastDividerPosition = dividerLocation
                logger!!.info("Setting last divider to "
                        + mLastDividerPosition)
            }
        }
        mapDialog!!.add(mSearchSplitPane, BorderLayout.CENTER)
        statusLabel = JLabel(" ")
        mapDialog!!.add(statusLabel, BorderLayout.SOUTH)
        registration.registerMapNodePositionListener(this)
        registration.registerNodeVisibilityListener(this)
        mindMapController!!.registerNodeSelectionListener(this, true)
        mapDialog!!.validate()
        // restore preferences:
        // Retrieve window size and column positions.
        val storage = mindMapController
                .decorateDialog(mapDialog, WINDOW_PREFERENCE_STORAGE_PROPERTY) as MapWindowConfigurationStorage
        if (storage != null) {
            map!!.zoomContolsVisible = storage.zoomControlsVisible
            map!!.isTileGridVisible = storage.tileGridVisible
            map!!.setMapMarkerVisible(storage.showMapMarker)
            map.setHideFoldedNodes(storage.hideFoldedNodes)
            var column = 0
            val i: Iterator<TableColumnSetting> = storage.listTableColumnSettingList.iterator()
            while (i.hasNext()) {
                val setting = i.next()
                mResultTable.getColumnModel().getColumn(column).preferredWidth = setting.columnWidth
                mResultTableSorter!!.setSortingStatus(column,
                        setting.columnSorting)
                column++
            }
            // default is false, so if true, toggle it.
            if (storage.limitSearchToVisibleArea) {
                toggleLimitSearchToRegion()
            }
            if (!storage.searchControlVisible) {
                toggleSearchBar()
            }
            mLastDividerPosition = storage.lastDividerPosition
            mSearchSplitPane!!.dividerLocation = mLastDividerPosition
            // restore last map positions
            val positionHolderVector = freeMindMapController.getPositionHolderVector()
            val it: Iterator<MapLocationStorage> = storage.listMapLocationStorageList.iterator()
            while (it.hasNext()) {
                val location = it.next()
                positionHolderVector!!.add(PositionHolder(location
                        .cursorLatitude, location
                        .cursorLongitude, location.zoom))
            }
            if (freeMindMapController!!.checkPositionHolderIndex(
                            storage.mapLocationStorageIndex)) {
                freeMindMapController.setPositionHolderIndex(
                        storage.mapLocationStorageIndex)
            }
            // TODO: Better would be to store these data per map.
            map!!.setDisplayPositionByLatLon(storage.mapCenterLatitude,
                    storage.mapCenterLongitude, storage.zoom)
            val position = Coordinate(
                    storage.cursorLatitude, storage.cursorLongitude)
            freeMindMapController!!.setCursorPosition(position)
            FreeMindMapController.Companion.changeTileSource(storage.tileSource, map)
        }
        addMarkersToMap()
        mapDialog!!.isVisible = true
        registration.mapDialog = this
    }

    fun addMarkersToMap() {
        // add known markers to the map.
        val mapNodePositionHolders = allMapNodePositionHolders
        for (nodePositionHolder in mapNodePositionHolders!!) {
            val visible = !nodePositionHolder!!.hasFoldedParents()
            changeVisibilityOfNode(nodePositionHolder, visible)
        }
    }

    protected fun changeVisibilityOfNode(
            nodePositionHolder: MapNodePositionHolder?, pVisible: Boolean) {
        if (!pVisible && map!!.isHideFoldedNodes) {
            removeMapMarker(nodePositionHolder)
        } else {
            addMapMarker(nodePositionHolder)
        }
    }

    val registration: Registration
        get() = getPluginBaseClass() as Registration

    fun toggleSearchBar() {
        mSingleSearch = false
        toggleSearchBar(null)
    }

    fun toggleSearchBar(pEvent: AWTEvent?) {
        if (isSearchBarVisible) {
            mLastDividerPosition = mSearchSplitPane!!.dividerLocation
            logger!!.fine("Setting last divider to $mLastDividerPosition")
            // clear results
            mResultTableModel!!.clear()
            // hide search bar
            mSearchSplitPane!!.bottomComponent = null
            mapDialog!!.remove(mSearchSplitPane)
            mapDialog!!.add(map, BorderLayout.CENTER)
            mapDialog!!.requestFocusInWindow()
            isSearchBarVisible = false
        } else {
            // show search bar
            mapDialog!!.remove(map)
            mapDialog!!.add(mSearchSplitPane, BorderLayout.CENTER)
            mSearchSplitPane!!.bottomComponent = map
            mSearchSplitPane!!.dividerLocation = mLastDividerPosition
            focusSearchTerm()
            isSearchBarVisible = true
        }
        mapDialog!!.validate()
        if (pEvent != null) {
            mSearchTerm!!.text = ""
            mSearchTerm!!.dispatchEvent(pEvent)
            /* Special for mac, as otherwise, everything is selected... GRRR. */if (isMacOsX) {
                EventQueue.invokeLater {
                    mSearchTerm!!.caretPosition = mSearchTerm!!.document
                            .length
                }
            }
        }
    }

    fun focusSearchTerm() {
        mSearchTerm!!.selectAll()
        mSearchTerm!!.requestFocusInWindow()
    }

    /**
     * @return a set of MapNodePositionHolder elements of all nodes (even if
     * hidden)
     */
    val allMapNodePositionHolders: Set<MapNodePositionHolder?>?
        get() = registration.mapNodePositionHolders

    /**
     * @return a set of MapNodePositionHolder elements to those nodes currently
     * displayed (ie. not hidden).
     */
    val mapNodePositionHolders: Set<MapNodePositionHolder?>
        get() = mMarkerMap.keys

    protected fun addMapMarker(nodePositionHolder: MapNodePositionHolder?) {
        if (mMarkerMap.containsKey(nodePositionHolder)) {
            // already present.
            logger!!.fine("Node $nodePositionHolder already present.")
            return
        }
        val position = nodePositionHolder.getPosition()
        logger!!.fine("Adding map position for " + nodePositionHolder!!.getNode()
                + " at " + position)
        val marker = MapMarkerLocation(nodePositionHolder,
                this)
        map!!.addMapMarker(marker)
        mMarkerMap[nodePositionHolder] = marker
    }

    protected fun removeMapMarker(pMapNodePositionHolder: MapNodePositionHolder?) {
        val marker = mMarkerMap
                .remove(pMapNodePositionHolder)
        if (marker != null) {
            map!!.removeMapMarker(marker)
        }
    }

    val freeMindMapController: FreeMindMapController?
        get() = map.getFreeMindMapController()

    /**
     *
     */
    fun disposeDialog() {
        val registration = getPluginBaseClass() as Registration?
        if (registration != null) {
            // on close, it is null. Why?
            registration.mapDialog = null
            registration.deregisterMapNodePositionListener(this)
            registration.deregisterNodeVisibilityListener(this)
        }
        mindMapController!!.deregisterNodeSelectionListener(this)

        // store window positions:
        val storage = MapWindowConfigurationStorage()
        // Set coordinates
        storage.zoom = map!!.zoom
        val position = map!!.position
        storage.mapCenterLongitude = position.lon
        storage.mapCenterLatitude = position.lat
        val cursorPosition = map.getCursorPosition()
        storage.cursorLongitude = cursorPosition!!.lon
        storage.cursorLatitude = cursorPosition!!.lat
        storage.tileSource = map!!.tileController.tileSource
                .javaClass.name
        storage.tileGridVisible = map!!.isTileGridVisible
        storage.zoomControlsVisible = map!!.zoomContolsVisible
        storage.showMapMarker = map!!.mapMarkersVisible
        storage.searchControlVisible = isSearchBarVisible
        storage.limitSearchToVisibleArea = isLimitSearchToRegion
        storage.hideFoldedNodes = map!!.isHideFoldedNodes
        storage.lastDividerPosition = mLastDividerPosition
        for (i in 0 until mResultTable!!.columnCount) {
            val setting = TableColumnSetting()
            setting.columnWidth = mResultTable!!.columnModel.getColumn(i)
                    .width
            setting.columnSorting = mResultTableSorter!!.getSortingStatus(i)
            storage.addTableColumnSetting(setting)
        }
        for (pos in freeMindMapController.getPositionHolderVector()) {
            val mapLocationStorage = MapLocationStorage()
            mapLocationStorage.cursorLatitude = pos!!.lat
            mapLocationStorage.cursorLongitude = pos!!.lon
            mapLocationStorage.zoom = pos!!.zoom
            storage.addMapLocationStorage(mapLocationStorage)
        }
        storage.mapLocationStorageIndex = freeMindMapController
                .getPositionHolderIndex()
        mindMapController!!.storeDialogPositions(mapDialog, storage,
                WINDOW_PREFERENCE_STORAGE_PROPERTY)
        mindMapController!!.controller!!.mapModuleManager
                .removeListener(this)
        mapDialog!!.isVisible = false
        mapDialog!!.dispose()
    }

    private fun updateZoomParameters() {
        if (mperpLabelValue != null) mperpLabelValue.text = format("%s", map!!.meterPerPixel)
        if (zoomValue != null) zoomValue.text = format("%s", map!!.zoom.toDouble())
    }

    /**
     * @param pString
     * @param pMeterPerPixel
     * @return
     */
    private fun format(pString: String, pObject: Double): String {
        return "" + pObject
    }

    override fun processCommand(command: JMVCommandEvent) {
        if (command.command == JMVCommandEvent.COMMAND.ZOOM || command.command == JMVCommandEvent.COMMAND.MOVE) {
            updateZoomParameters()
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * isMapModuleChangeAllowed(freemind.view.MapModule, freemind.modes.Mode,
	 * freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun isMapModuleChangeAllowed(pOldMapModule: MapModule?,
                                          pOldMode: Mode?, pNewMapModule: MapModule?, pNewMode: Mode?): Boolean {
        return true
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * beforeMapModuleChange(freemind.view.MapModule, freemind.modes.Mode,
	 * freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun beforeMapModuleChange(pOldMapModule: MapModule?, pOldMode: Mode?,
                                       pNewMapModule: MapModule?, pNewMode: Mode?) {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.MapModuleManager.MapModuleChangeObserver#afterMapClose
	 * (freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun afterMapClose(pOldMapModule: MapModule?, pOldMode: Mode?) {
        disposeDialog()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * afterMapModuleChange(freemind.view.MapModule, freemind.modes.Mode,
	 * freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun afterMapModuleChange(pOldMapModule: MapModule?, pOldMode: Mode?,
                                      pNewMapModule: MapModule?, pNewMode: Mode) {
        disposeDialog()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * numberOfOpenMapInformation(int, int)
	 */
    override fun numberOfOpenMapInformation(pNumber: Int, pIndex: Int) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * plugins.map.MapNodePositionHolder.MapNodePositionListener#registerMapNode
	 * (plugins.map.MapNodePositionHolder)
	 */
    override fun registerMapNode(pMapNodePositionHolder: MapNodePositionHolder?) {
        addMapMarker(pMapNodePositionHolder)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * plugins.map.MapNodePositionHolder.MapNodePositionListener#deregisterMapNode
	 * (plugins.map.MapNodePositionHolder)
	 */
    override fun deregisterMapNode(pMapNodePositionHolder: MapNodePositionHolder?) {
        removeMapMarker(pMapNodePositionHolder)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ModeController.NodeSelectionListener#onUpdateNodeHook(
	 * freemind.modes.MindMapNode)
	 */
    override fun onUpdateNodeHook(pNode: MindMapNode?) {
        // update MapMarkerLocation if present:
        val hook: MapNodePositionHolder = MapNodePositionHolder.Companion.getHook(pNode)
        if (hook != null && mMarkerMap.containsKey(hook)) {
            val location = mMarkerMap[hook]
            location!!.update()
            location.repaint()
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ModeController.NodeSelectionListener#onSelectHook(freemind
	 * .view.mindmapview.NodeView)
	 */
    override fun onFocusNode(pNode: NodeView?) {}
    fun selectMapPosition(pNode: NodeView?, sel: Boolean) {
        // test for map position:
        val hook: MapNodePositionHolder = MapNodePositionHolder.Companion.getHook(pNode
                .model)
        if (hook != null) {
            if (mMarkerMap.containsKey(hook)) {
                mMarkerMap[hook].setSelected(sel)
                map!!.repaint()
            }
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ModeController.NodeSelectionListener#onDeselectHook(freemind
	 * .view.mindmapview.NodeView)
	 */
    override fun onLostFocusNode(pNode: NodeView?) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ModeController.NodeSelectionListener#onSaveNode(freemind
	 * .modes.MindMapNode)
	 */
    override fun onSaveNode(pNode: MindMapNode?) {}

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ModeController.NodeSelectionListener#onSelectionChange
	 * (freemind.modes.MindMapNode, boolean)
	 */
    override fun onSelectionChange(pNode: NodeView?, pIsSelected: Boolean) {
        selectMapPosition(pNode, pIsSelected)
    }

    /**
     * @return < MapNodePositionHolder, MapMarkerLocation > of those nodes
     * currently displayed (ie. not hidden)
     */
    val markerMap: Map<MapNodePositionHolder?, MapMarkerLocation>
        get() = Collections.unmodifiableMap(mMarkerMap)

    fun displaySearchItem(index: Int) {
        val place = getPlace(index)
        freeMindMapController!!.setCursorPosition(place)
        if (mSingleSearch && isSearchBarVisible) {
            toggleSearchBar()
        }
        mSingleSearch = false
    }

    fun getPlace(index: Int): Place {
        var index = index
        require(index >= 0) {
            ("Index " + index
                    + " out of bounds.")
        }
        index = mResultTableSorter!!.modelIndex(index)
        return mResultTableModel!!.getPlace(index)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * plugins.map.Registration.NodeVisibilityListener#nodeVisibilityChanged
	 * (boolean)
	 */
    override fun nodeVisibilityChanged(
            pMapNodePositionHolder: MapNodePositionHolder?, pVisible: Boolean) {
        changeVisibilityOfNode(pMapNodePositionHolder, pVisible)
    }

    fun search(searchText: String, pSelectFirstResult: Boolean) {
        if (!isSearchBarVisible) {
            toggleSearchBar()
        }
        mSearchTerm!!.text = searchText
        val resultOk = freeMindMapController!!.search(mResultTableModel,
                mResultTable, searchText, mTableOriginalBackgroundColor)
        val rowCount = mResultTableModel!!.rowCount
        if (resultOk) {
            if (mSingleSearch && rowCount == 1) {
                displaySearchItem(0)
                map!!.requestFocus()
                return
            }
            if (pSelectFirstResult) {
                if (rowCount > 0) {
                    displaySearchItem(0)
                }
            }
            mResultTable!!.requestFocus()
        } else {
            mSearchTerm!!.requestFocus()
        }
    }

    /**
     *
     */
    fun setSingleSearch() {
        mSingleSearch = true
    }

    /**
     *
     */
    fun toggleLimitSearchToRegion() {
        isLimitSearchToRegion = !isLimitSearchToRegion
        if (isLimitSearchToRegion) {
            mSearchStringLabel!!.text = mResourceSearchLocationString
        } else {
            mSearchStringLabel!!.text = mResourceSearchString
        }
        mSearchStringLabel!!.validate()
    }

    protected fun addSearchResultsToMap() {
        val selectedRows = mResultTable!!.selectedRows
        logger!!.info("Add results to map.")
        freeMindMapController!!.addSearchResultsToMap(selectedRows)
    }

    companion object {
        private val WINDOW_PREFERENCE_STORAGE_PROPERTY = MapDialog::class.java
                .name
        const val TILE_CACHE_CLASS = "tile_cache_class"
        const val FILE_TILE_CACHE_DIRECTORY = "file_tile_cache_directory"
        const val TILE_CACHE_MAX_AGE = "tile_cache_max_age"
        private const val SEARCH_DESCRIPTION_COLUMN = 0
        const val SEARCH_DISTANCE_COLUMN = 1
        const val MAP_HOOK_NAME = "plugins/map/MapDialog.properties"
        const val TILE_CACHE_PURGE_TIME = "tile_cache_purge_time"
        const val TILE_CACHE_PURGE_TIME_DEFAULT = (1000 * 60 * 10).toLong()
        private const val SEARCH_DESCRIPTION_COLUMN_TEXT = "plugins/map/MapDialog.Description"
        private const val SEARCH_DISTANCE_COLUMN_TEXT = "plugins/map/MapDialog.Distance"
    }
}