/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
package freemind.modes.mindmapmode

import freemind.controller.Controller.informationMessage
import freemind.main.FreeMindMain.jFrame
import freemind.main.Tools.addEscapeActionToDialog
import freemind.main.Tools.setLabelAndMnemonic
import freemind.main.Tools.setDialogLocationRelativeTo
import freemind.common.TextTranslator.getText
import freemind.main.FreeMindMain.getResourceString
import freemind.main.FreeMindMain.getAdjustableProperty
import freemind.main.FreeMindMain.getProperty
import freemind.main.FreeMindMain.setProperty
import freemind.extensions.PermanentNodeHookAdapter.loadFrom
import freemind.extensions.PermanentNodeHookAdapter.loadNameValuePairs
import freemind.extensions.PermanentNodeHookAdapter.save
import freemind.extensions.PermanentNodeHookAdapter.saveNameValuePairs
import freemind.extensions.NodeHookAdapter.getNode
import freemind.extensions.HookAdapter.name
import freemind.extensions.PermanentNodeHookAdapter.shutdownMapHook
import freemind.extensions.NodeHookAdapter.invoke
import freemind.extensions.HookAdapter.getResourceString
import freemind.extensions.HookAdapter.getController
import freemind.main.Tools.safeEquals
import freemind.extensions.PermanentNodeHookAdapter.setToolTip
import freemind.main.Tools.BooleanHolder.value
import freemind.main.Tools.IntHolder.value
import freemind.main.FreeMindMain.getLogger
import freemind.main.FreeMindMain.out
import freemind.main.Tools.isLinux
import freemind.main.Tools.isMacOsX
import freemind.main.FreeMindMain.getIntProperty
import freemind.controller.Controller.setZoom
import freemind.controller.Controller.mapModuleManager
import freemind.controller.MapModuleManager.changeToMapOfMode
import freemind.main.Tools.fileToUrl
import freemind.controller.Controller.getResourceString
import freemind.controller.Controller.view
import freemind.controller.Controller.frame
import freemind.controller.NodeMouseMotionListener.register
import freemind.controller.MapMouseMotionListener.register
import freemind.controller.NodeKeyListener.register
import freemind.controller.NodeMouseMotionListener.deregister
import freemind.controller.MapMouseMotionListener.deregister
import freemind.controller.NodeKeyListener.deregister
import freemind.controller.Controller.obtainFocusForSelected
import freemind.controller.Controller.errorMessage
import freemind.controller.Controller.removeSplitPane
import freemind.controller.Controller.insertComponentIntoSplitPane
import freemind.main.FreeMindMain.openDocument
import freemind.controller.Controller.getResource
import freemind.controller.StructuredMenuHolder.addSeparator
import freemind.main.FreeMindMain.controller
import freemind.extensions.MindMapHook.setProperties
import freemind.extensions.MindMapHook.name
import freemind.extensions.MindMapHook.setPluginBaseClass
import freemind.main.Tools.DesEncrypter.decrypt
import freemind.extensions.HookDescriptorPluginAction.modes
import freemind.extensions.ImportWizard.buildClassList
import freemind.controller.actions.generated.instance.Plugin.listChoiceList
import freemind.controller.actions.generated.instance.PluginAction.label
import freemind.extensions.HookDescriptorBase.pluginClassLoader
import freemind.extensions.HookDescriptorPluginAction.className
import freemind.extensions.HookDescriptorBase.pluginClasspath
import freemind.controller.actions.generated.instance.PluginClasspath.jar
import freemind.extensions.HookDescriptorPluginAction.properties
import freemind.extensions.HookDescriptorPluginAction.name
import freemind.extensions.HookDescriptorPluginAction.documentation
import freemind.extensions.HookDescriptorPluginAction.iconPath
import freemind.extensions.HookDescriptorPluginAction.keyStroke
import freemind.extensions.HookDescriptorPluginAction.instanciationMethod
import freemind.extensions.HookDescriptorRegistration.listPluginModeList
import freemind.controller.actions.generated.instance.PluginMode.className
import freemind.extensions.HookDescriptorBase.pluginBase
import freemind.extensions.HookDescriptorRegistration.className
import freemind.controller.actions.generated.instance.Plugin.label
import freemind.extensions.HookDescriptorPluginAction.isSelectable
import freemind.controller.actions.generated.instance.NodeAction.node
import freemind.controller.actions.generated.instance.CompoundAction.addChoice
import freemind.controller.actions.generated.instance.CompoundAction.addAtChoice
import freemind.controller.actions.generated.instance.CompoundAction.sizeChoiceList
import freemind.controller.actions.generated.instance.TextNodeAction.text
import freemind.controller.actions.generated.instance.AddCloudXmlAction.enabled
import freemind.controller.actions.generated.instance.AddCloudXmlAction.color
import freemind.main.Tools.colorToXml
import freemind.main.Tools.xmlToColor
import freemind.controller.actions.generated.instance.PasteNodeAction.transferableContent
import freemind.controller.actions.generated.instance.PasteNodeAction.asSibling
import freemind.controller.actions.generated.instance.UndoPasteNodeAction.asSibling
import freemind.main.Tools.marshall
import freemind.main.Tools.fileToRelativeUrlString
import freemind.main.Tools.fromBase64
import freemind.main.Tools.firstLetterCapitalized
import freemind.controller.actions.generated.instance.TransferableFile.fileName
import freemind.controller.actions.generated.instance.TransferableContent.addTransferableFile
import freemind.controller.actions.generated.instance.UndoPasteNodeAction.nodeAmount
import freemind.controller.actions.generated.instance.TransferableContent.transferable
import freemind.main.Tools.countOccurrences
import freemind.controller.actions.generated.instance.TransferableContent.transferableAsHtml
import freemind.controller.actions.generated.instance.TransferableContent.transferableAsPlainText
import freemind.main.Tools.toBase64
import freemind.controller.actions.generated.instance.TransferableContent.transferableAsImage
import freemind.controller.actions.generated.instance.TransferableContent.listTransferableFileList
import freemind.controller.actions.generated.instance.TransferableContent.transferableAsRTF
import freemind.controller.actions.generated.instance.TransferableContent.transferableAsDrop
import freemind.extensions.PermanentNodeHook.processUnfinishedLinks
import freemind.controller.actions.generated.instance.MoveNodesAction.listNodeListMemberList
import freemind.controller.actions.generated.instance.NodeListMember.node
import freemind.controller.actions.generated.instance.MoveNodesAction.direction
import freemind.controller.actions.generated.instance.MoveNodesAction.addNodeListMember
import freemind.controller.actions.generated.instance.RevertXmlAction.localFileName
import freemind.controller.actions.generated.instance.RevertXmlAction.map
import freemind.controller.actions.generated.instance.RevertXmlAction.filePrefix
import freemind.extensions.HookFactory.getInstanciationMethod
import freemind.extensions.HookInstanciationMethod.isPermanent
import freemind.extensions.HookInstanciationMethod.isUndoable
import freemind.extensions.HookInstanciationMethod.getDestinationNodes
import freemind.extensions.HookInstanciationMethod.getCenterNode
import freemind.extensions.HookInstanciationMethod.isAlreadyPresent
import freemind.extensions.PermanentNodeHook.save
import freemind.main.XMLElement.countChildren
import freemind.main.XMLElement.getChildren
import freemind.main.XMLElement.name
import freemind.main.XMLElement.enumerateAttributeNames
import freemind.controller.actions.generated.instance.NodeChildParameter.key
import freemind.controller.actions.generated.instance.NodeChildParameter.value
import freemind.main.XMLElement.getStringAttribute
import freemind.controller.actions.generated.instance.HookNodeAction.addNodeChildParameter
import freemind.controller.actions.generated.instance.HookNodeAction.hookName
import freemind.controller.actions.generated.instance.HookNodeAction.addNodeListMember
import freemind.controller.actions.generated.instance.HookNodeAction.listNodeListMemberList
import freemind.main.XMLElement.addChild
import freemind.controller.actions.generated.instance.HookNodeAction.listNodeChildParameterList
import freemind.main.XMLElement.setAttribute
import freemind.extensions.PermanentNodeHook.loadFrom
import freemind.extensions.PermanentNodeHook.onFocusNode
import freemind.controller.actions.generated.instance.AddIconAction.iconName
import freemind.controller.actions.generated.instance.AddIconAction.iconPosition
import freemind.main.Tools.iconFirstIndex
import freemind.main.Tools.iconLastIndex
import freemind.controller.actions.generated.instance.AddLinkXmlAction.destination
import freemind.controller.actions.generated.instance.BoldNodeAction.bold
import freemind.controller.actions.generated.instance.CompoundAction.listChoiceList
import freemind.controller.actions.generated.instance.FontSizeNodeAction.size
import freemind.controller.actions.generated.instance.MoveNodeXmlAction.hGap
import freemind.controller.actions.generated.instance.MoveNodeXmlAction.shiftY
import freemind.controller.actions.generated.instance.MoveNodeXmlAction.vGap
import freemind.controller.actions.generated.instance.NewNodeAction.index
import freemind.controller.actions.generated.instance.NewNodeAction.position
import freemind.controller.actions.generated.instance.NewNodeAction.newId
import freemind.extensions.PermanentNodeHook.onNewChild
import freemind.controller.actions.generated.instance.EdgeColorFormatAction.color
import freemind.controller.actions.generated.instance.EdgeStyleFormatAction.style
import freemind.controller.actions.generated.instance.EdgeWidthFormatAction.width
import freemind.controller.actions.generated.instance.NodeColorFormatAction.color
import freemind.controller.actions.generated.instance.NodeStyleFormatAction.style
import freemind.controller.actions.generated.instance.UnderlinedNodeAction.underlined
import freemind.controller.actions.generated.instance.CloudColorXmlAction.color
import freemind.controller.actions.generated.instance.FontNodeAction.font
import freemind.controller.actions.generated.instance.ItalicNodeAction.italic
import freemind.controller.actions.generated.instance.RemoveIconXmlAction.iconPosition
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.destination
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.newId
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.color
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.endArrow
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.endInclination
import freemind.main.Tools.xmlToPoint
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.startArrow
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction.startInclination
import freemind.controller.actions.generated.instance.AddAttributeAction.name
import freemind.controller.actions.generated.instance.AddAttributeAction.value
import freemind.controller.actions.generated.instance.SetAttributeAction.name
import freemind.controller.actions.generated.instance.SetAttributeAction.value
import freemind.controller.actions.generated.instance.SetAttributeAction.position
import freemind.controller.actions.generated.instance.FoldAction.folded
import freemind.main.Tools.compareText
import freemind.controller.actions.generated.instance.ArrowLinkColorXmlAction.id
import freemind.controller.actions.generated.instance.ArrowLinkColorXmlAction.color
import freemind.controller.actions.generated.instance.InsertAttributeAction.name
import freemind.controller.actions.generated.instance.InsertAttributeAction.value
import freemind.controller.actions.generated.instance.InsertAttributeAction.position
import freemind.controller.actions.generated.instance.RemoveArrowLinkXmlAction.id
import freemind.main.Tools.PointToXml
import freemind.controller.actions.generated.instance.RemoveAttributeAction.position
import freemind.controller.actions.generated.instance.StrikethroughNodeAction.strikethrough
import freemind.controller.actions.generated.instance.NodeBackgroundColorFormatAction.color
import freemind.controller.actions.generated.instance.ArrowLinkArrowXmlAction.id
import freemind.controller.actions.generated.instance.ArrowLinkArrowXmlAction.startArrow
import freemind.controller.actions.generated.instance.ArrowLinkArrowXmlAction.endArrow
import freemind.controller.actions.generated.instance.ArrowLinkPointXmlAction.id
import freemind.controller.actions.generated.instance.ArrowLinkPointXmlAction.startPoint
import freemind.controller.actions.generated.instance.ArrowLinkPointXmlAction.endPoint
import freemind.common.OptionalDontShowMeAgainDialog.show
import freemind.common.OptionalDontShowMeAgainDialog.result
import freemind.controller.Controller.getProperty
import freemind.main.Tools.getKeyStroke
import freemind.main.Tools.printXmlAction
import freemind.main.Tools.deepCopy
import freemind.main.FreeMindMain.setWaitingCursor
import freemind.extensions.HookFactory.getPluginBaseClass
import freemind.controller.MenuItemEnabledListener.isEnabled
import freemind.controller.MenuItemSelectedListener.isSelected
import freemind.main.Tools.removeMnemonic
import freemind.main.Tools.getMindMapNodesFromClipboard
import freemind.controller.actions.generated.instance.Pattern.name
import freemind.main.FreeMindMain.err
import freemind.main.Tools.getFileNameProposal
import freemind.main.Tools.getExtension
import freemind.main.Tools.expandPlaceholders
import freemind.main.FreeMindMain.contentPane
import freemind.main.Tools.removeExtension
import freemind.common.PropertyControl.layout
import freemind.common.PropertyBean.addPropertyChangeListener
import freemind.common.ThreeCheckBoxProperty.value
import freemind.main.FreeMind.getDefaultProperty
import freemind.controller.actions.generated.instance.Pattern.patternNodeColor
import freemind.controller.actions.generated.instance.Pattern.patternNodeBackgroundColor
import freemind.controller.actions.generated.instance.Pattern.patternNodeStyle
import freemind.controller.actions.generated.instance.Pattern.patternNodeText
import freemind.controller.actions.generated.instance.Pattern.patternEdgeColor
import freemind.controller.actions.generated.instance.Pattern.patternEdgeStyle
import freemind.controller.actions.generated.instance.Pattern.patternEdgeWidth
import freemind.controller.actions.generated.instance.Pattern.patternNodeFontName
import freemind.controller.Controller.defaultFontFamilyName
import freemind.controller.actions.generated.instance.Pattern.patternNodeFontSize
import freemind.controller.actions.generated.instance.Pattern.patternNodeFontBold
import freemind.controller.actions.generated.instance.Pattern.patternNodeFontStrikethrough
import freemind.controller.actions.generated.instance.Pattern.patternNodeFontItalic
import freemind.controller.actions.generated.instance.Pattern.patternIcon
import freemind.controller.actions.generated.instance.Pattern.patternScript
import freemind.common.StringProperty.value
import freemind.controller.actions.generated.instance.Pattern.patternChild
import freemind.common.ThreeCheckBoxProperty.label
import freemind.common.PropertyBean.value
import freemind.controller.actions.generated.instance.PatternPropertyBase.value
import freemind.main.Tools.edgeWidthStringToInt
import freemind.common.PropertyControl.setEnabled
import freemind.common.ComboProperty.updateComboBoxEntries
import freemind.common.ComboProperty.value
import freemind.main.Tools.convertPointToAncestor
import freemind.main.Tools.availableFonts
import freemind.controller.Controller.getIntProperty
import freemind.controller.Controller.zooms
import freemind.controller.StructuredMenuHolder.updateMenus
import freemind.controller.Controller.registerZoomListener
import freemind.controller.Controller.deregisterZoomListener
import freemind.main.Tools.setHidden
import freemind.main.Tools.getVectorWithSingleElement
import freemind.main.FreeMindMain.getResource
import freemind.main.FreeMindMain.patternsFile
import freemind.main.Tools.getUpdateReader
import freemind.main.Tools.getReaderFromFile
import freemind.main.Tools.urlToFile
import freemind.extensions.HookFactory.registrations
import freemind.extensions.HookFactory.registerRegistrationContainer
import freemind.extensions.HookRegistration.register
import freemind.controller.NodeDropListener.register
import freemind.controller.NodeMotionListener.register
import freemind.extensions.HookRegistration.deRegister
import freemind.extensions.HookFactory.deregisterAllRegistrationContainer
import freemind.controller.NodeDropListener.deregister
import freemind.controller.NodeMotionListener.deregister
import freemind.controller.Controller.mapModule
import freemind.controller.actions.generated.instance.MenuStructure.listChoiceList
import freemind.controller.StructuredMenuHolder.addMenuItem
import freemind.controller.StructuredMenuHolder.addMenu
import freemind.controller.StructuredMenuHolder.addAction
import freemind.controller.actions.generated.instance.MenuCategoryBase.name
import freemind.controller.StructuredMenuHolder.addCategory
import freemind.controller.actions.generated.instance.MenuSubmenu.nameRef
import freemind.controller.actions.generated.instance.MenuCategoryBase.listChoiceList
import freemind.controller.actions.generated.instance.MenuActionBase.field
import freemind.controller.actions.generated.instance.MenuActionBase.name
import freemind.controller.actions.generated.instance.MenuActionBase.keyRef
import freemind.main.Tools.getField
import freemind.controller.actions.generated.instance.MenuRadioAction.selected
import freemind.main.ExampleFileFilter.addExtension
import freemind.main.ExampleFileFilter.setDescription
import freemind.extensions.HookFactory.createNodeHook
import freemind.extensions.MindMapHook.setController
import freemind.extensions.NodeHook.setMap
import freemind.extensions.HookInstanciationMethod.isSingleton
import freemind.extensions.HookFactory.getHookInNode
import freemind.extensions.MindMapHook.startupMapHook
import freemind.extensions.MindMapHook.shutdownMapHook
import freemind.extensions.PermanentNodeHook.onUpdateNodeHook
import freemind.extensions.PermanentNodeHook.onUpdateChildrenHook
import freemind.main.Tools.unMarshall
import freemind.main.Tools.invokeAndWait
import freemind.extensions.HookFactory.createModeControllerHook
import freemind.controller.Controller.close
import freemind.extensions.PermanentNodeHook.saveHtml
import freemind.main.Tools.DesEncrypter.encrypt
import freemind.main.XMLElement.toString
import freemind.controller.filter.util.SortedMapListModel.addAll
import freemind.main.XMLElement.parseFromReader
import freemind.main.Tools.readFileStart
import freemind.main.Tools.ReaderCreator.createReader
import freemind.main.Tools.getActualReader
import freemind.main.Tools.isHeadless
import freemind.extensions.PermanentNodeHook.onAddChild
import freemind.extensions.PermanentNodeHook.onAddChildren
import freemind.extensions.PermanentNodeHook.onRemoveChild
import freemind.extensions.PermanentNodeHook.onRemoveChildren
import freemind.extensions.NodeHook.setNode
import freemind.extensions.NodeHook.invoke
import freemind.main.XMLElement.setEncodedContent
import freemind.main.Tools.dateToString
import freemind.main.XMLElement.writeWithoutClosingTag
import freemind.main.XMLElement.writeClosingTag
import freemind.main.XMLElement.write
import freemind.controller.filter.Filter.isVisible
import freemind.main.XMLElement.enumerateChildren
import freemind.controller.Controller.setTitle
import freemind.extensions.PermanentNodeHook.onLostFocusNode
import freemind.extensions.PermanentNodeHook.onViewCreatedHook
import freemind.extensions.PermanentNodeHook.onViewRemovedHook
import freemind.controller.MapModuleManager.newMapModule
import freemind.controller.MapModuleManager.checkIfFileIsAlreadyOpened
import freemind.controller.MapModuleManager.changeToMapModule
import freemind.controller.Controller.modeController
import freemind.controller.LastStateStorageManagement.getStorage
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.lastZoom
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.lastSelected
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.listNodeListMemberList
import freemind.main.Tools.isAbsolutePath
import freemind.main.Tools.getURLWithoutReference
import freemind.controller.MapModuleManager.tryToChangeToMapModule
import freemind.main.Tools.makeFileHidden
import freemind.controller.MapModuleManager.updateMapModuleName
import freemind.controller.MapModuleManager.mapModule
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.restorableName
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.x
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.y
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.clearNodeListMemberList
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.addNodeListMember
import freemind.controller.LastStateStorageManagement.changeOrAdd
import freemind.controller.LastStateStorageManagement.xml
import freemind.controller.MapMouseWheelListener.deregister
import freemind.main.FreeMindMain.view
import freemind.controller.MapMouseWheelListener.register
import freemind.controller.MapModuleManager.getModuleGivenModeController
import freemind.controller.Controller.defaultFont
import freemind.controller.Controller.getFontThroughMap
import freemind.controller.Controller.nodeMouseMotionListener
import freemind.controller.Controller.nodeMotionListener
import freemind.controller.Controller.nodeKeyListener
import freemind.controller.Controller.nodeDragListener
import freemind.controller.Controller.nodeDropListener
import freemind.controller.Controller.mapMouseMotionListener
import freemind.controller.Controller.mapMouseWheelListener
import freemind.controller.Controller.setProperty
import freemind.main.XMLElement.userObject
import freemind.main.XMLElement.content
import freemind.main.XMLElement.getAttribute
import freemind.main.Tools.xmlToDate
import freemind.extensions.NodeHookAdapter.setMap
import freemind.main.Tools.generateID
import freemind.controller.actions.generated.instance.Patterns.listChoiceList
import freemind.controller.actions.generated.instance.Pattern.originalName
import freemind.controller.actions.generated.instance.Patterns.addChoice
import java.lang.StringBuffer
import freemind.modes.common.dialogs.EnterPasswordDialog
import freemind.common.TextTranslator
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import freemind.extensions.PermanentNodeHookAdapter
import freemind.modes.common.plugins.ReminderHookBase
import java.text.MessageFormat
import freemind.modes.common.plugins.ReminderHookBase.TimerBlinkTask
import freemind.modes.common.plugins.MapNodePositionHolderBase
import freemind.extensions.PermanentNodeHook
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.modes.common.listeners.CommonNodeMouseMotionListener
import freemind.main.Tools.BooleanHolder
import java.awt.geom.Point2D
import freemind.modes.common.listeners.CommonNodeMouseMotionListener.timeDelayedSelection
import java.lang.Runnable
import freemind.modes.common.listeners.MindMapMouseWheelEventHandler
import freemind.preferences.FreemindPropertyListener
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import freemind.modes.common.CommonNodeKeyListener.EditHandler
import freemind.modes.common.CommonNodeKeyListener
import kotlin.jvm.JvmOverloads
import freemind.modes.common.CommonToggleFoldedAction
import freemind.modes.filemode.FileController
import freemind.modes.filemode.FileToolBar
import freemind.modes.filemode.FileMode
import freemind.modes.filemode.FileNodeModel
import kotlin.Throws
import java.lang.RuntimeException
import freemind.modes.filemode.FileEdgeModel
import java.lang.SecurityException
import java.net.MalformedURLException
import freemind.modes.viewmodes.ViewControllerAdapter
import freemind.modes.common.actions.NewMapAction
import freemind.modes.filemode.FileController.CenterAction
import freemind.modes.filemode.FileController.OpenPathAction
import freemind.modes.filemode.FilePopupMenu
import freemind.modes.filemode.FileMapModel
import freemind.controller.StructuredMenuHolder
import freemind.extensions.HookFactory
import java.net.URISyntaxException
import freemind.modes.viewmodes.CommonToggleChildrenFoldedAction
import freemind.modes.common.actions.FindAction.FindNextAction
import freemind.modes.common.listeners.CommonMouseMotionManager
import freemind.extensions.NodeHook
import freemind.extensions.PermanentNodeHookSubstituteUnknown
import javax.swing.table.TableModel
import freemind.modes.browsemode.BrowseController
import freemind.modes.browsemode.BrowseMode
import freemind.modes.common.dialogs.PersistentEditableComboBox
import freemind.modes.browsemode.BrowseToolBar
import freemind.modes.browsemode.BrowseNodeModel
import freemind.modes.browsemode.BrowseMapModel
import freemind.modes.browsemode.EncryptedBrowseNode
import freemind.modes.browsemode.BrowseEdgeModel
import freemind.modes.browsemode.BrowseCloudModel
import freemind.modes.browsemode.BrowseArrowLinkModel
import freemind.modes.common.plugins.NodeNoteBase
import freemind.modes.ModeController.NodeSelectionListener
import freemind.controller.Controller.SplitComponentType
import freemind.modes.browsemode.NodeNoteViewer
import javax.swing.event.PopupMenuListener
import javax.swing.event.PopupMenuEvent
import freemind.modes.browsemode.BrowseController.FollowMapLink
import freemind.controller.MenuItemEnabledListener
import freemind.modes.browsemode.BrowseHookFactory
import freemind.modes.browsemode.BrowsePopupMenu
import freemind.modes.common.GotoLinkNodeAction
import java.security.AccessControlException
import freemind.extensions.HookFactoryAdapter
import freemind.extensions.ModeControllerHook
import freemind.modes.browsemode.BrowseReminderHook
import freemind.extensions.HookInstanciationMethod
import freemind.extensions.HookFactory.RegistrationContainer
import freemind.main.Tools.SingleDesEncrypter
import freemind.extensions.ModeControllerHookAdapter
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.hooks.MindMapHookFactory
import freemind.extensions.HookRegistration
import freemind.extensions.HookDescriptorPluginAction
import java.lang.ClassNotFoundException
import freemind.extensions.ImportWizard
import freemind.extensions.HookDescriptorRegistration
import org.jibx.runtime.IUnmarshallingContext
import freemind.common.XmlBindingTools
import freemind.extensions.MindMapHook
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.extensions.NodeHookAdapter
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorAdapter
import java.awt.datatransfer.Transferable
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.modes.mindmapmode.actions.xml.actors.NodeXmlActorAdapter
import freemind.modes.mindmapmode.MindMapCloudModel
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.DataFlavorHandler
import freemind.modes.mindmapmode.MindMapNodeModel
import freemind.controller.MindMapNodesSelection
import freemind.main.Tools.StringReaderCreator
import freemind.main.HtmlTools.NodeCreator
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.FileListFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.MindMapNodesFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.DirectHtmlFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.StringFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.ImageFlavorHandler
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import freemind.modes.mindmapmode.actions.xml.actors.AddHookActor
import freemind.extensions.DontSaveMarker
import freemind.modes.mindmapmode.actions.xml.ActorXml
import java.lang.NumberFormatException
import freemind.modes.mindmapmode.MindMapEdgeModel
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory
import freemind.modes.mindmapmode.actions.xml.actors.ItalicNodeActor
import freemind.modes.mindmapmode.actions.xml.actors.BoldNodeActor
import freemind.modes.mindmapmode.actions.xml.actors.StrikethroughNodeActor
import freemind.modes.mindmapmode.actions.xml.actors.NewChildActor
import freemind.modes.mindmapmode.actions.xml.actors.DeleteChildActor
import freemind.modes.mindmapmode.actions.xml.actors.RemoveAllIconsActor
import freemind.modes.mindmapmode.actions.xml.actors.AddIconActor
import freemind.modes.mindmapmode.actions.xml.actors.RemoveIconActor
import freemind.modes.mindmapmode.actions.xml.actors.CloudActor
import freemind.modes.mindmapmode.actions.xml.actors.EdgeStyleActor
import freemind.modes.mindmapmode.actions.xml.actors.EdgeWidthActor
import freemind.modes.mindmapmode.actions.xml.actors.FontFamilyActor
import freemind.modes.mindmapmode.actions.xml.actors.FontSizeActor
import freemind.modes.mindmapmode.actions.xml.actors.MoveNodeActor
import freemind.modes.mindmapmode.actions.xml.actors.NodeStyleActor
import freemind.modes.mindmapmode.actions.xml.actors.UnderlineActor
import freemind.modes.mindmapmode.actions.xml.actors.AddArrowLinkActor
import freemind.modes.mindmapmode.actions.xml.actors.RemoveArrowLinkActor
import freemind.modes.mindmapmode.actions.xml.actors.ChangeArrowLinkEndPointsActor
import freemind.modes.mindmapmode.actions.xml.actors.ChangeArrowsInArrowLinkActor
import freemind.modes.mindmapmode.actions.xml.actors.CloudColorActor
import freemind.modes.mindmapmode.actions.xml.actors.ColorArrowLinkActor
import freemind.modes.mindmapmode.actions.xml.actors.EdgeColorActor
import freemind.modes.mindmapmode.actions.xml.actors.EditActor
import freemind.modes.mindmapmode.actions.xml.actors.NodeBackgroundColorActor
import freemind.modes.mindmapmode.actions.xml.actors.NodeColorActor
import freemind.modes.mindmapmode.actions.xml.actors.NodeUpActor
import freemind.modes.mindmapmode.actions.xml.actors.RevertActor
import freemind.modes.mindmapmode.actions.xml.actors.ToggleFoldedActor
import freemind.modes.mindmapmode.actions.xml.actors.SetLinkActor
import freemind.modes.mindmapmode.actions.xml.actors.AddAttributeActor
import freemind.modes.mindmapmode.actions.xml.actors.InsertAttributeActor
import freemind.modes.mindmapmode.actions.xml.actors.RemoveAttributeActor
import freemind.modes.mindmapmode.actions.xml.actors.SetAttributeActor
import freemind.modes.mindmapmode.actions.xml.actors.CutActor
import freemind.modes.mindmapmode.actions.xml.actors.CompoundActor
import freemind.modes.mindmapmode.actions.xml.actors.UndoPasteActor
import freemind.modes.mindmapmode.actions.xml.actors.ChangeNoteTextActor
import java.lang.IllegalStateException
import freemind.modes.mindmapmode.MindMapArrowLinkModel
import freemind.modes.mindmapmode.actions.xml.ActionFilter
import freemind.modes.mindmapmode.actions.xml.UndoActionHandler
import freemind.modes.mindmapmode.actions.xml.ActionRegistry
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FirstActionFilter
import freemind.modes.mindmapmode.actions.xml.PrintActionHandler
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.OptionalDontShowMeAgainDialog.StandardPropertyHandler
import freemind.controller.MenuItemSelectedListener
import freemind.view.mindmapview.EditNodeBase.EditControl
import freemind.modes.mindmapmode.actions.xml.AbstractXmlAction
import freemind.modes.mindmapmode.EncryptedMindMapNode
import freemind.modes.mindmapmode.MindMapMapModel
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin
import java.beans.PropertyChangeListener
import freemind.common.PropertyControl
import freemind.common.ThreeCheckBoxProperty
import freemind.common.ComboProperty
import freemind.common.FontProperty
import freemind.common.BooleanProperty
import freemind.common.IconProperty
import freemind.common.ScriptEditorProperty
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.common.PropertyBean
import java.beans.PropertyChangeEvent
import freemind.common.SeparatorProperty
import freemind.modes.mindmapmode.dialogs.StylePatternFrame
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.StylePatternFrameType
import freemind.common.NextLineProperty
import freemind.modes.mindmapmode.dialogs.IntegerComboProperty
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.EdgeWidthTransformer
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.ValueTransformator
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.IdentityTransformer
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.EdgeWidthBackTransformer
import java.awt.dnd.DropTargetListener
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTargetEvent
import freemind.controller.NodeMotionListener.NodeMotionAdapter
import freemind.modes.mindmapmode.listeners.MindMapNodeMotionListener
import freemind.modes.mindmapmode.MindMapMode
import freemind.controller.FreeMindToolBar
import freemind.controller.ZoomListener
import freemind.modes.mindmapmode.JAutoScrollBarPane
import freemind.controller.color.JColorCombo
import freemind.modes.mindmapmode.MindMapToolBar
import freemind.modes.mindmapmode.MindMapToolBar.FreeMindComboBox
import freemind.controller.color.ColorPair
import freemind.view.ImageFactory
import freemind.modes.mindmapmode.MindMapMapModel.DummyLockManager
import freemind.modes.mindmapmode.MindMapMapModel.DoAutomaticSave
import java.nio.channels.FileLock
import java.lang.UnsatisfiedLinkError
import java.lang.NoClassDefFoundError
import java.lang.InterruptedException
import java.lang.reflect.InvocationTargetException
import freemind.controller.FreeMindPopupMenu
import freemind.modes.mindmapmode.MindMapPopupMenu
import freemind.modes.MindMap.MapSourceChangedObserver
import java.awt.datatransfer.Clipboard
import freemind.modes.mindmapmode.MindMapController.ExportToHTMLAction
import freemind.modes.mindmapmode.MindMapController.ExportBranchToHTMLAction
import freemind.modes.mindmapmode.MindMapController.EditLongAction
import freemind.modes.mindmapmode.MindMapController.SetLinkByFileChooserAction
import freemind.modes.mindmapmode.MindMapController.SetImageByFileChooserAction
import freemind.modes.mindmapmode.MindMapController.OpenLinkDirectoryAction
import freemind.modes.mindmapmode.MindMapController.ImportBranchAction
import freemind.modes.mindmapmode.MindMapController.ImportLinkedBranchAction
import freemind.modes.mindmapmode.MindMapController.ImportLinkedBranchWithoutRootAction
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction.RemoveNodeBackgroundColorAction
import freemind.modes.mindmapmode.MindMapController.MindMapFilter
import freemind.modes.mindmapmode.actions.xml.DefaultActionHandler
import freemind.main.Tools.FileReaderCreator
import freemind.main.Tools.ReaderCreator
import freemind.modes.MindMap.AskUserBeforeUpdateCallback
import freemind.modes.mindmapmode.listeners.MindMapMouseMotionManager
import freemind.modes.mindmapmode.listeners.MindMapNodeDropListener
import freemind.view.MapModule
import freemind.modes.mindmapmode.MindMapController.NewNodeCreator
import freemind.modes.mindmapmode.MindMapController.NodeInformationTimerAction
import freemind.modes.mindmapmode.MindMapController.DefaultMindMapNodeCreator
import org.jibx.runtime.JiBXException
import freemind.modes.mindmapmode.MindMapController.LinkActionBase
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.BadLocationException
import freemind.extensions.UndoEventReceiver
import freemind.modes.mindmapmode.MindMapController.MapSourceChangeDialog
import freemind.modes.mindmapmode.MindMapHTMLWriter
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreeModel
import freemind.controller.filter.util.SortedListModel
import freemind.view.ScalableImageIcon
import javax.swing.tree.DefaultTreeModel
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import freemind.controller.filter.util.SortedMapListModel
import freemind.controller.filter.condition.NoFilteringCondition
import freemind.modes.MapAdapter.FileChangeInspectorTimerTask
import freemind.modes.MapAdapter.DontAskUserBeforeUpdateAdapter
import freemind.modes.EdgeAdapter.EdgeAdapterListener
import java.lang.CloneNotSupportedException
import freemind.modes.LinkAdapter.LinkAdapterListener
import java.lang.NullPointerException
import freemind.modes.CloudAdapter.CloudAdapterListener
import freemind.controller.MapModuleManager
import freemind.modes.ModeController.NodeLifetimeListener
import freemind.modes.FreeMindFileDialog.DirectoryResultListener
import freemind.controller.LastStateStorageManagement
import freemind.modes.ControllerAdapter.FileOpener
import java.awt.dnd.DropTarget
import freemind.controller.NodeMouseMotionListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeKeyListener
import freemind.controller.NodeDragListener
import freemind.controller.NodeDropListener
import freemind.controller.MapMouseMotionListener
import freemind.controller.MapMouseWheelListenerimport

freemind.controller.MenuBarimport freemind.controller.actions.generated.instance.*import freemind.main.*
import freemind.modes.*
import freemind.modes.MapFeedbackAdapter.NodesDepthComparator
import freemind.modes.MindMapLinkRegistry.SynchronousVector
import freemind.modes.mindmapmode.actions.ApplyPatternAction.ExternalPatternAction
import freemind.modes.FreeMindAwtFileDialog.FreeMindFilenameFilter
import freemind.modes.FreeMindAwtFileDialog.DirFilter
import freemind.modes.FreeMindAwtFileDialog.FileOnlyFilter
import freemind.modes.FreeMindAwtFileDialog.FileAndDirFilter
import kotlin.jvm.JvmStatic
import freemind.modes.ExtendedMapFeedbackAdapter.DummyTransferableimport

freemind.modes.attributes.Attributeimport freemind.modes.common.actions.FindActionimport freemind.modes.mindmapmode.actions.*import freemind.view.mindmapview.*
import java.awt.*
import java.awt.event.*
import java.io.*
import java.lang.Exceptionimport

java.lang.IllegalArgumentExceptionimport java.net.URLimport java.util.*import java.util.regex.Patternimport

javax.swing.*import javax.swing.Timerimport

javax.swing.filechooser.FileFilterimport javax.swing.text.html.HTMLDocument
open class MindMapController(mode: Mode?) : ControllerAdapter(mode), ExtendedMapFeedback, MapSourceChangedObserver {
    /**
     * @author foltin
     * @date 19.11.2013
     */
    private inner class NodeInformationTimerAction : ActionListener {
        private var mIsInterrupted = false
        private var mIsDone = true
        val isRunning: Boolean
            get() = !mIsDone

        /**
         * @return true, if successfully interrupted.
         */
        fun interrupt(): Boolean {
            mIsInterrupted = true
            val i = 1000
            try {
                while (i > 0 && !mIsDone) {
                    Thread.sleep(10)
                }
            } catch (e: InterruptedException) {
                Resources.getInstance().logException(e)
            }
            mIsInterrupted = false
            return i > 0
        }

        override fun actionPerformed(pE: ActionEvent) {
            mIsDone = false
            actionPerformedInternally(pE)
            mIsDone = true
        }

        fun actionPerformedInternally(pE: ActionEvent?) {
            val nodeStatusLine: String
            val selecteds = selecteds
            val amountOfSelecteds = selecteds!!.size
            if (amountOfSelecteds == 0) return
            var sum = 0.0
            val p = Pattern
                    .compile(REGEXP_FOR_NUMBERS_IN_STRINGS)
            for (selectedNode in selecteds) {
                if (mIsInterrupted) {
                    return
                }
                val m = p.matcher(selectedNode.text)
                while (m.find()) {
                    val number = m.group()
                    try {
                        sum += number.toDouble()
                    } catch (e: NumberFormatException) {
                        // freemind.main.Resources.getInstance().logException(e);
                    }
                }
            }
            if (amountOfSelecteds > 1) {
                nodeStatusLine = Resources.getInstance().format(
                        "node_status_line_several_selected_nodes", arrayOf<Any>(amountOfSelecteds, sum))
            } else {
                val sel = selecteds[0] as MindMapNode
                var amountOfChildren: Long = 0
                val allDescendants = getVectorWithSingleElement<MindMapNode?>(sel)
                while (!allDescendants.isEmpty()) {
                    if (mIsInterrupted) {
                        return
                    }
                    val child = allDescendants
                            .firstElement()
                    amountOfChildren += child!!.childCount.toLong()
                    allDescendants.addAll(child.children)
                    allDescendants.removeAt(0)
                }
                nodeStatusLine = Resources.getInstance().format(
                        "node_status_line", arrayOf<Any?>(
                        sel.getShortText(this@MindMapController), sel.childCount,
                        amountOfChildren))
            }
            frame.out(nodeStatusLine)
        }
    }

    /**
     * @author foltin
     * @date 24.01.2012
     */
    private inner class MapSourceChangeDialog
    /**
     * @param pReturnValue
     */
        : Runnable {
        /**
         *
         */
        var returnValue = true
            private set

        override fun run() {
            val showResult = OptionalDontShowMeAgainDialog(
                    frame.jFrame,
                    selectedView,
                    "file_changed_on_disk_reload",
                    "confirmation",
                    this@MindMapController,
                    StandardPropertyHandler(
                            controller,
                            FreeMind.RESOURCES_RELOAD_FILES_WITHOUT_QUESTION),
                    OptionalDontShowMeAgainDialog.BOTH_OK_AND_CANCEL_OPTIONS_ARE_STORED)
                    .show().result
            if (showResult != JOptionPane.OK_OPTION) {
                frame.out(
                        expandPlaceholders(getText("file_not_reloaded"),
                                map.file.toString()))
                mReturnValue = false
                return
            }
            revertAction!!.actionPerformed(null)
        }
    }

    interface MindMapControllerPlugin

    // for MouseEventHandlers
    private val mRegisteredMouseWheelEventHandler = HashSet<MouseWheelEventHandler>()
    override val actionRegistry: ActionRegistry
    private var hookActions: Vector<HookAction>? = null

    // Mode mode;
    private var popupmenu: MindMapPopupMenu? = null

    // private JToolBar toolbar;
    var toolBar: MindMapToolBar? = null
        private set
    private var clipboard: Clipboard? = null
    private var selection: Clipboard? = null
    private var nodeHookFactory: HookFactory? = null
    var patterns = arrayOfNulls<ApplyPatternAction>(0) // Make

    // sure
    // it is
    // initialized
    var newMap: Action = NewMapAction(this)
    var open: Action = OpenAction(this)
    var save: Action = SaveAction()
    var saveAs: Action = SaveAsAction()
    var exportToHTML: Action = ExportToHTMLAction(this)
    var exportBranchToHTML: Action = ExportBranchToHTMLAction(this)
    var editLong: Action = EditLongAction()
    var newSibling: Action = NewSiblingAction(this)
    var newPreviousSibling: Action = NewPreviousSiblingAction(this)
    var setLinkByFileChooser: Action = SetLinkByFileChooserAction()
    var setImageByFileChooser: Action = SetImageByFileChooserAction()
    var followLink: Action = FollowLinkAction()
    var openLinkDirectory: Action = OpenLinkDirectoryAction()
    var exportBranch: Action = ExportBranchAction(this)
    var importBranch: Action = ImportBranchAction()
    var importLinkedBranch: Action = ImportLinkedBranchAction()
    var importLinkedBranchWithoutRoot: Action = ImportLinkedBranchWithoutRootAction()
    var propertyAction: Action? = null
    var increaseNodeFont: Action = NodeGeneralAction(this,
            "increase_node_font_size", null) { map, node -> increaseFontSize(node, 1) }
    var decreaseNodeFont: Action = NodeGeneralAction(this,
            "decrease_node_font_size", null) { map, node -> increaseFontSize(node, -1) }
    var undo: UndoAction? = null
    var redo: RedoAction? = null
    var copy: CopyAction? = null
    var copySingle: Action? = null
    var cut: CutAction? = null
    var paste: PasteAction? = null
    var pasteAsPlainText: PasteAsPlainTextAction? = null
    var bold: BoldAction? = null
    var strikethrough: StrikethroughAction? = null
    var italic: ItalicAction? = null
    var underlined: UnderlinedAction? = null
    var fontSize: FontSizeAction? = null
    var fontFamily: FontFamilyAction? = null
    var nodeColor: NodeColorAction? = null
    var edit: EditAction? = null
    var newChild: NewChildAction? = null
    var deleteChild: DeleteChildAction? = null
    var toggleFolded: ToggleFoldedAction? = null
    var toggleChildrenFolded: ToggleChildrenFoldedAction? = null
    var useRichFormatting: UseRichFormattingAction? = null
    var usePlainText: UsePlainTextAction? = null
    var nodeUp: NodeUpAction? = null
    var nodeDown: NodeDownAction? = null
    var edgeColor: EdgeColorAction? = null
    var EdgeWidth_WIDTH_PARENT: EdgeWidthAction? = null
    var EdgeWidth_WIDTH_THIN: EdgeWidthAction? = null
    var EdgeWidth_1: EdgeWidthAction? = null
    var EdgeWidth_2: EdgeWidthAction? = null
    var EdgeWidth_4: EdgeWidthAction? = null
    var EdgeWidth_8: EdgeWidthAction? = null
    var edgeWidths: Array<EdgeWidthAction>? = null
    var EdgeStyle_linear: EdgeStyleAction? = null
    var EdgeStyle_bezier: EdgeStyleAction? = null
    var EdgeStyle_sharp_linear: EdgeStyleAction? = null
    var EdgeStyle_sharp_bezier: EdgeStyleAction? = null
    var edgeStyles: Array<EdgeStyleAction>? = null
    var nodeColorBlend: NodeColorBlendAction? = null
    var fork: NodeStyleAction? = null
    var bubble: NodeStyleAction? = null
    var cloud: CloudAction? = null
    var cloudColor: CloudColorAction? = null
    var addArrowLinkAction: AddArrowLinkAction? = null
    var removeArrowLinkAction: RemoveArrowLinkAction? = null
    var colorArrowLinkAction: ColorArrowLinkAction? = null
    var changeArrowsInArrowLinkAction: ChangeArrowsInArrowLinkAction? = null
    var nodeBackgroundColor: NodeBackgroundColorAction? = null
    var removeNodeBackgroundColor: RemoveNodeBackgroundColorAction? = null
    var unknownIconAction: IconAction? = null
    var removeLastIconAction: RemoveIconAction? = null
    var removeAllIconsAction: RemoveAllIconsAction? = null
    var setLinkByTextField: SetLinkByTextFieldAction? = null
    var addLocalLinkAction: AddLocalLinkAction? = null
    var gotoLinkNodeAction: GotoLinkNodeAction? = null
    var joinNodes: JoinNodesAction? = null
    var moveNodeAction: MoveNodeAction? = null
    var importExplorerFavorites: ImportExplorerFavoritesAction? = null
    var importFolderStructure: ImportFolderStructureAction? = null
    var find: FindAction? = null
    var findNext: FindNextAction? = null
    var nodeHookAction: NodeHookAction? = null
    var revertAction: RevertAction? = null
    var selectBranchAction: SelectBranchAction? = null
    var selectAllAction: SelectAllAction? = null

    // Extension Actions
    var iconActions = Vector<IconAction?>() // fc
    var filefilter: FileFilter = MindMapFilter()
    private var mMenuStructure: MenuStructure? = null
    private var mRegistrations: MutableList<HookRegistration>? = null

    /**
     * @return the patternsList
     */
    var patternsList: List<freemind.controller.actions.generated.instance.Pattern?>? = Vector()
        private set
    private var mGetEventIfChangedAfterThisTimeInMillies: Long = 0
    protected open fun init() {
        MapFeedbackAdapter.Companion.logger!!.info("createXmlActions")
        actorFactory = XmlActorFactory(this)
        MapFeedbackAdapter.Companion.logger!!.info("createIconActions")
        // create standard actions:
        createStandardActions()
        // icon actions:
        createIconActions()
        MapFeedbackAdapter.Companion.logger!!.info("createNodeHookActions")
        // node hook actions:
        createNodeHookActions()
        MapFeedbackAdapter.Companion.logger!!.info("mindmap_menus")
        // load menus:
        try {
            val `in`: InputStream
            `in` = this.frame.getResource("mindmap_menus.xml").openStream()
            mMenuStructure = updateMenusFromXml(`in`)
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            Resources.getInstance().logException(e)
        }
        MapFeedbackAdapter.Companion.logger!!.info("MindMapPopupMenu")
        popupmenu = MindMapPopupMenu(this)
        MapFeedbackAdapter.Companion.logger!!.info("MindMapToolBar")
        toolBar = MindMapToolBar(this)
        mRegistrations = Vector()
        propertyAction = controller.propertyAction
    }

    private fun createStandardActions() {
        // prepare undo:
        undo = UndoAction(this)
        redo = RedoAction(this)
        // register default action handler:
        // the executor must be the first here, because it is executed last
        // then.
        actionRegistry.registerHandler(
                DefaultActionHandler(actionRegistry))
        actionRegistry.registerUndoHandler(
                UndoActionHandler(this, undo!!, redo!!))
        // debug:
        // getActionFactory().registerHandler(
        // new freemind.modes.mindmapmode.actions.xml.PrintActionHandler(
        // this));
        cut = CutAction(this)
        paste = PasteAction(this)
        pasteAsPlainText = PasteAsPlainTextAction(this)
        copy = CopyAction(this)
        copySingle = CopySingleAction(this)
        bold = BoldAction(this)
        strikethrough = StrikethroughAction(this)
        italic = ItalicAction(this)
        underlined = UnderlinedAction(this)
        fontSize = FontSizeAction(this)
        fontFamily = FontFamilyAction(this)
        edit = EditAction(this)
        useRichFormatting = UseRichFormattingAction(this)
        usePlainText = UsePlainTextAction(this)
        newChild = NewChildAction(this)
        deleteChild = DeleteChildAction(this)
        toggleFolded = ToggleFoldedAction(this)
        toggleChildrenFolded = ToggleChildrenFoldedAction(this)
        nodeUp = NodeUpAction(this)
        nodeDown = NodeDownAction(this)
        edgeColor = EdgeColorAction(this)
        nodeColor = NodeColorAction(this)
        nodeColorBlend = NodeColorBlendAction(this)
        fork = NodeStyleAction(this, MindMapNode.Companion.STYLE_FORK)
        bubble = NodeStyleAction(this, MindMapNode.Companion.STYLE_BUBBLE)
        // this is an unknown icon and thus corrected by mindicon:
        removeLastIconAction = RemoveIconAction(this)
        // this action handles the xml stuff: (undo etc.)
        unknownIconAction = IconAction(this,
                MindIcon.Companion.factory(MindIcon.Companion.getAllIconNames().get(0) as String),
                removeLastIconAction)
        removeLastIconAction!!.setIconAction(unknownIconAction)
        removeAllIconsAction = RemoveAllIconsAction(this, unknownIconAction)
        // load pattern actions:
        loadPatternActions()
        EdgeWidth_WIDTH_PARENT = EdgeWidthAction(this,
                EdgeAdapter.Companion.WIDTH_PARENT)
        EdgeWidth_WIDTH_THIN = EdgeWidthAction(this, EdgeAdapter.Companion.WIDTH_THIN)
        EdgeWidth_1 = EdgeWidthAction(this, 1)
        EdgeWidth_2 = EdgeWidthAction(this, 2)
        EdgeWidth_4 = EdgeWidthAction(this, 4)
        EdgeWidth_8 = EdgeWidthAction(this, 8)
        edgeWidths = arrayOf(EdgeWidth_WIDTH_PARENT!!,
                EdgeWidth_WIDTH_THIN!!, EdgeWidth_1!!, EdgeWidth_2!!, EdgeWidth_4!!,
                EdgeWidth_8!!)
        EdgeStyle_linear = EdgeStyleAction(this,
                EdgeAdapter.Companion.EDGESTYLE_LINEAR)
        EdgeStyle_bezier = EdgeStyleAction(this,
                EdgeAdapter.Companion.EDGESTYLE_BEZIER)
        EdgeStyle_sharp_linear = EdgeStyleAction(this,
                EdgeAdapter.Companion.EDGESTYLE_SHARP_LINEAR)
        EdgeStyle_sharp_bezier = EdgeStyleAction(this,
                EdgeAdapter.Companion.EDGESTYLE_SHARP_BEZIER)
        edgeStyles = arrayOf(EdgeStyle_linear!!,
                EdgeStyle_bezier!!, EdgeStyle_sharp_linear!!,
                EdgeStyle_sharp_bezier!!)
        cloud = CloudAction(this)
        cloudColor = CloudColorAction(
                this)
        addArrowLinkAction = AddArrowLinkAction(this)
        removeArrowLinkAction = RemoveArrowLinkAction(this, null)
        colorArrowLinkAction = ColorArrowLinkAction(this, null)
        changeArrowsInArrowLinkAction = ChangeArrowsInArrowLinkAction(this,
                "none", null, null, true, true)
        nodeBackgroundColor = NodeBackgroundColorAction(this)
        removeNodeBackgroundColor = RemoveNodeBackgroundColorAction(this)
        setLinkByTextField = SetLinkByTextFieldAction(this)
        addLocalLinkAction = AddLocalLinkAction(this)
        gotoLinkNodeAction = GotoLinkNodeAction(this, null)
        moveNodeAction = MoveNodeAction(this)
        joinNodes = JoinNodesAction(this)
        importExplorerFavorites = ImportExplorerFavoritesAction(this)
        importFolderStructure = ImportFolderStructureAction(this)
        find = FindAction(this)
        findNext = FindNextAction(this, find!!)
        nodeHookAction = NodeHookAction("no_title", this)
        revertAction = RevertAction(this)
        selectBranchAction = SelectBranchAction(this)
        selectAllAction = SelectAllAction(this)
    }

    /**
     * Tries to load the user patterns and proposes an update to the new format,
     * if they are old fashioned (this is determined by having an exception
     * while reading the pattern file).
     */
    private fun loadPatternActions() {
        try {
            loadPatterns(patternReader)
        } catch (ex: Exception) {
            System.err.println("Patterns not loaded:$ex")
            // repair old patterns:
            val repairTitle = "Repair patterns"
            val patternsFile = frame.patternsFile
            val result = JOptionPane
                    .showConfirmDialog(
                            null,
                            "<html>The pattern file format has changed, <br>"
                                    + "and it seems, that your pattern file<br>"
                                    + "'"
                                    + patternsFile!!.absolutePath
                                    + "'<br> is formatted in the old way. <br>"
                                    + "Should I try to repair the pattern file <br>"
                                    + "(otherwise, you should update it by hand or delete it)?",
                            repairTitle, JOptionPane.YES_NO_OPTION)
            if (result == JOptionPane.YES_OPTION) {
                // try xslt script:
                var success = false
                try {
                    loadPatterns(getUpdateReader(
                            getReaderFromFile(patternsFile),
                            "patterns_updater.xslt"))
                    // save patterns directly:
                    StylePatternFactory.savePatterns(FileWriter(
                            patternsFile), patternsList)
                    success = true
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
                if (success) {
                    JOptionPane.showMessageDialog(null,
                            "Successfully repaired the pattern file.",
                            repairTitle, JOptionPane.PLAIN_MESSAGE)
                } else {
                    JOptionPane.showMessageDialog(null,
                            "An error occured repairing the pattern file.",
                            repairTitle, JOptionPane.WARNING_MESSAGE)
                }
            }
        }
    }

    /**
     * @throws FileNotFoundException
     * @throws IOException
     */
    @get:Throws(FileNotFoundException::class, IOException::class)
    val patternReader: Reader
        get() {
            var reader: Reader? = null
            val patternsFile = frame.patternsFile
            reader = if (patternsFile != null && patternsFile.exists()) {
                FileReader(patternsFile)
            } else {
                println("User patterns file " + patternsFile
                        + " not found.")
                InputStreamReader(getResource("patterns.xml")
                        .openStream())
            }
            return reader
        }
    val isUndoAction: Boolean
        get() = undo!!.isUndoAction || redo!!.isUndoAction

    @Throws(URISyntaxException::class, XMLParseException::class, IOException::class)
    override fun loadInternally(url: URL?, model: MapAdapter) {
        MapFeedbackAdapter.Companion.logger!!.info("Loading file: " + url.toString())
        val file = urlToFile(url!!)
        if (!file.exists()) {
            throw FileNotFoundException(expandPlaceholders(
                    getText("file_not_found"), file.path))
        }
        if (!file.canWrite()) {
            model.isReadOnly = true
        } else {
            // try to lock the map
            try {
                val lockingUser = model.tryToLock(file)
                if (lockingUser != null) {
                    frame.controller.informationMessage(
                            expandPlaceholders(
                                    getText("map_locked_by_open"),
                                    file.name, lockingUser))
                    model.isReadOnly = true
                } else {
                    model.isReadOnly = false
                }
            } catch (e: Exception) {
                // Thrown by tryToLock
                Resources.getInstance().logException(e)
                frame.controller.informationMessage(
                        expandPlaceholders(
                                getText("locking_failed_by_open"),
                                file.name))
                model.isReadOnly = true
            }
        }
        synchronized(model) {
            val root = loadTree(file)
            if (root != null) {
                model.setRoot(root)
            }
            model.file = file
            model.setFileTime()
        }
    }

    @Throws(XMLParseException::class, IOException::class)
    fun loadTree(pFile: File?): MindMapNode? {
        return loadTree(FileReaderCreator(pFile!!))
    }

    @Throws(XMLParseException::class, IOException::class)
    fun loadTree(pReaderCreator: ReaderCreator): MindMapNode? {
        return map.loadTree(pReaderCreator) {
            val showResult = OptionalDontShowMeAgainDialog(
                    frame.jFrame,
                    selectedView,
                    "really_convert_to_current_version2",
                    "confirmation",
                    this@MindMapController,
                    StandardPropertyHandler(
                            controller,
                            FreeMind.RESOURCES_CONVERT_TO_CURRENT_VERSION),
                    OptionalDontShowMeAgainDialog.ONLY_OK_SELECTION_IS_STORED)
                    .show().result
            showResult == JOptionPane.OK_OPTION
        }
    }

    /**
     * Creates the patterns actions (saved in array patterns), and the pure
     * patterns list (saved in mPatternsList).
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun loadPatterns(reader: Reader?) {
        createPatterns(StylePatternFactory.loadPatterns(reader))
    }

    @Throws(Exception::class)
    private fun createPatterns(patternsList: List<freemind.controller.actions.generated.instance.Pattern?>?) {
        this.patternsList = patternsList
        patterns = arrayOfNulls(patternsList!!.size)
        for (i in patterns.indices) {
            val actualPattern = patternsList[i]
            patterns[i] = ApplyPatternAction(this, actualPattern)

            // search icons for patterns:
            val patternIcon = actualPattern!!.patternIcon
            if (patternIcon != null && patternIcon.value != null) {
                patterns[i]!!.putValue(Action.SMALL_ICON,
                        MindIcon.Companion.factory(patternIcon.value).getIcon())
            }
        }
    }

    /**
     * This method is called after and before a change of the map module. Use it
     * to perform the actions that cannot be performed at creation time.
     *
     */
    override fun startupController() {
        super.startupController()
        toolBar!!.startup()
        val hookFactory = hookFactory
        val pluginRegistrations = hookFactory.registrations
        MapFeedbackAdapter.Companion.logger!!.fine("mScheduledActions are executed: " + pluginRegistrations!!.size)
        for (container in pluginRegistrations) {
            // call constructor:
            try {
                val registrationClass = container!!.hookRegistrationClass
                val hookConstructor = registrationClass!!.getConstructor(*arrayOf(ModeController::class.java, MindMap::class.java))
                val registrationInstance = hookConstructor
                        .newInstance(*arrayOf(this, map)) as HookRegistration
                // register the instance to enable basePlugins.
                hookFactory.registerRegistrationContainer(container,
                        registrationInstance)
                registrationInstance.register()
                mRegistrations!!.add(registrationInstance)
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
        invokeHooksRecursively(rootNode as NodeAdapter, map)

        // register mouse motion handler:
        mapMouseMotionListener.register(
                MindMapMouseMotionManager(this))
        nodeDropListener.register(
                MindMapNodeDropListener(this))
        nodeKeyListener.register(
                CommonNodeKeyListener(this) { e, addNew, editLong -> edit(e, addNew, editLong) })
        nodeMotionListener.register(
                MindMapNodeMotionListener(this))
        nodeMouseMotionListener.register(
                CommonNodeMouseMotionListener(this))
        map.registerMapSourceChangedObserver(this,
                mGetEventIfChangedAfterThisTimeInMillies)
    }

    override fun shutdownController() {
        super.shutdownController()
        for (registrationInstance in mRegistrations!!) {
            registrationInstance.deRegister()
        }
        hookFactory.deregisterAllRegistrationContainer()
        mRegistrations!!.clear()
        // deregister motion handler
        mapMouseMotionListener.deregister()
        nodeDropListener.deregister()
        nodeKeyListener.deregister()
        nodeMotionListener.deregister()
        nodeMouseMotionListener.deregister()
        mGetEventIfChangedAfterThisTimeInMillies = map
                .deregisterMapSourceChangedObserver(this)
        toolBar!!.shutdown()
    }

    override fun newModel(modeController: ModeController?): MapAdapter {
        val model = MindMapMapModel(modeController)
        modeController!!.setModel(model)
        return model
    }

    private fun createIconActions() {
        val iconNames: Vector<String?> = MindIcon.Companion.getAllIconNames()
        val iconDir: File = File(Resources.getInstance().getFreemindDirectory(), "icons")
        if (iconDir.exists()) {
            val userIconArray = iconDir.list { dir, name -> name.matches(".*\\.png") }
            if (userIconArray != null) for (i in userIconArray.indices) {
                var iconName = userIconArray[i]
                iconName = iconName.substring(0, iconName.length - 4)
                if (iconName == "") {
                    continue
                }
                iconNames.add(iconName)
            }
        }
        for (i in iconNames.indices) {
            val myIcon: MindIcon = MindIcon.Companion.factory(iconNames[i])
            val myAction = IconAction(this, myIcon,
                    removeLastIconAction)
            iconActions.add(myAction)
        }
    }

    /**
     *
     */
    protected fun createNodeHookActions() {
        if (hookActions == null) {
            hookActions = Vector()
            // HOOK TEST
            val factory = hookFactory as MindMapHookFactory
            val nodeHookNames: List<String?> = factory.getPossibleNodeHooks()
            for (hookName in nodeHookNames) {
                // create hook action.
                val action = NodeHookAction(hookName, this)
                hookActions!!.add(action)
            }
            val modeControllerHookNames: List<String?> = factory.getPossibleModeControllerHooks()
            for (hookName in modeControllerHookNames) {
                val action = MindMapControllerHookAction(hookName, this)
                hookActions!!.add(action)
            }
            // HOOK TEST END
        }
    }

    override val fileFilter: FileFilter?
        get() = filefilter

    override fun nodeChanged(n: MindMapNode?) {
        super.nodeChanged(n)
        val mapModule = controller.mapModule
        // only for the selected node (fc, 2.5.2004)
        if (mapModule != null
                && n === mapModule.modeController.selected) {
            updateToolbar(n)
            updateNodeInformation()
        }
    }

    override fun nodeStyleChanged(node: MindMapNode?) {
        nodeChanged(node)
        val childrenFolded: ListIterator<*>? = node!!.childrenFolded()
        while (childrenFolded!!.hasNext()) {
            val child = childrenFolded.next() as MindMapNode
            if (!(child.hasStyle() && child.edge.hasStyle())) {
                nodeStyleChanged(child)
            }
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ControllerAdapter#onFocusNode(freemind.view.mindmapview
	 * .NodeView)
	 */
    override fun onFocusNode(pNode: NodeView) {
        super.onFocusNode(pNode)
        val model = pNode.model
        updateToolbar(model)
        updateNodeInformation()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ControllerAdapter#onLostFocusNode(freemind.view.mindmapview
	 * .NodeView)
	 */
    override fun onLostFocusNode(pNode: NodeView) {
        super.onLostFocusNode(pNode)
        updateNodeInformation()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.ControllerAdapter#changeSelection(freemind.view.mindmapview
	 * .NodeView, boolean)
	 */
    override fun changeSelection(pNode: NodeView, pIsSelected: Boolean) {
        super.changeSelection(pNode, pIsSelected)
        updateNodeInformation()
    }

    /**
     * Updates the status line.
     */
    private fun updateNodeInformation() {
        mNodeInformationTimer.stop()
        if (mNodeInformationTimerAction.isRunning()) {
            mNodeInformationTimerAction.interrupt()
        }
        mNodeInformationTimer.start()
    }

    protected fun updateToolbar(n: MindMapNode?) {
        toolBar!!.selectFontSize(n!!.fontSize)
        toolBar!!.selectFontName(n.fontFamilyName)
        toolBar!!.selectColor(n.color)
    }

    // fc, 14.12.2004: changes, such that different models can be used:
    private var myNewNodeCreator: NewNodeCreator? = null

    /**
     * A general list of MindMapControllerPlugin s. Members need to be tested
     * for the right class and casted to be applied.
     */
    private val mPlugins = HashSet<MindMapControllerPlugin?>()
    private val mNodeInformationTimer: Timer
    private val mNodeInformationTimerAction: NodeInformationTimerAction
    override var actorFactory: XmlActorFactory? = null
        private set

    init {
        // create action factory:
        actionRegistry = ActionRegistry()
        // create node information timer and actions. They don't fire, until
        // called to do so.
        mNodeInformationTimerAction = NodeInformationTimerAction()
        mNodeInformationTimer = Timer(100, mNodeInformationTimerAction)
        mNodeInformationTimer.isRepeats = false
        init()
    }

    interface NewNodeCreator {
        fun createNode(userObject: Any?, map: MindMap?): MindMapNode
    }

    inner class DefaultMindMapNodeCreator : NewNodeCreator {
        override fun createNode(userObject: Any?, map: MindMap?): MindMapNode {
            return MindMapNodeModel(userObject, map)
        }
    }

    fun setNewNodeCreator(creator: NewNodeCreator?) {
        myNewNodeCreator = creator
    }

    override fun newNode(userObject: Any?, map: MindMap?): MindMapNode? {
        // singleton default:
        if (myNewNodeCreator == null) {
            myNewNodeCreator = DefaultMindMapNodeCreator()
        }
        return myNewNodeCreator!!.createNode(userObject, map)
    }
    // fc, 14.12.2004: end "different models" change
    // get/set methods
    /**
     */
    override fun updateMenus(holder: StructuredMenuHolder) {
        processMenuCategory(holder, mMenuStructure!!.listChoiceList, "") /*
																			 * MenuBar
																			 * .
																			 * MENU_BAR_PREFIX
																			 */
        // add hook actions to this holder.
        // hooks, fc, 1.3.2004:
        val hookFactory = hookFactory as MindMapHookFactory
        for (i in hookActions!!.indices) {
            val hookAction = hookActions!![i] as AbstractAction
            val hookName = (hookAction as HookAction).hookName
            hookFactory.decorateAction(hookName, hookAction)
            val hookMenuPositions = hookFactory.getHookMenuPositions(hookName)
            for (pos in hookMenuPositions!!) {
                holder.addMenuItem(hookFactory.getMenuItem(hookName, hookAction), pos)
            }
        }
        // update popup and toolbar:
        popupmenu!!.update(holder)
        toolBar!!.update(holder)

        // editMenu.add(getExtensionMenu());
        val formatMenuString = MenuBar.FORMAT_MENU
        createPatternSubMenu(holder, formatMenuString)

        // editMenu.add(getIconMenu());
        addIconsToMenu(holder, MenuBar.INSERT_MENU + "icons")
    }

    fun addIconsToMenu(holder: StructuredMenuHolder, iconMenuString: String) {
        val iconMenu = holder.addMenu(JMenu(getText("icon_menu")), "$iconMenuString/.")
        holder.addAction(removeLastIconAction, iconMenuString
                + "/removeLastIcon")
        holder.addAction(removeAllIconsAction, iconMenuString
                + "/removeAllIcons")
        holder.addSeparator(iconMenuString)
        for (i in iconActions.indices) {
            holder.addAction(iconActions[i] as Action?, "$iconMenuString/$i")
        }
    }

    /**
     */
    fun createPatternSubMenu(holder: StructuredMenuHolder,
                             formatMenuString: String) {
        for (i in patterns.indices) {
            val item = holder.addAction(patterns[i], formatMenuString
                    + "patterns/patterns/" + i)
            item!!.accelerator = KeyStroke
                    .getKeyStroke(frame.getAdjustableProperty(
                            "keystroke_apply_pattern_" + (i + 1)))
        }
    }

    fun updateMenusFromXml(`in`: InputStream?): MenuStructure {
        // get from resources:
        return try {
            val unmarshaller: IUnmarshallingContext = XmlBindingTools.getInstance()
                    .createUnmarshaller()
            unmarshaller
                    .unmarshalDocument(`in`, null) as MenuStructure
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            throw IllegalArgumentException(
                    "Menu structure could not be read.")
        }
    }

    /**
     */
    fun processMenuCategory(holder: StructuredMenuHolder, list: List<Any?>, category: String) {
        var buttonGroup: ButtonGroup? = null
        for (obj in list) {
            if (obj is MenuCategoryBase) {
                val cat = obj
                val newCategory = category + "/" + cat.name
                holder.addCategory(newCategory)
                if (cat is MenuSubmenu) {
                    holder.addMenu(JMenu(getText(cat.nameRef)),
                            "$newCategory/.")
                }
                processMenuCategory(holder, cat.listChoiceList, newCategory)
            } else if (obj is MenuActionBase) {
                val action = obj
                val field = action.field
                var name = action.name
                if (name == null) {
                    name = field
                }
                val keystroke = action.keyRef
                try {
                    val theAction = getField(arrayOf<Any?>(
                            this, controller), field) as Action?
                    val theCategory = "$category/$name"
                    if (obj is MenuCheckedAction) {
                        addCheckBox(holder, theCategory, theAction, keystroke)
                    } else if (obj is MenuRadioAction) {
                        val item = addRadioItem(
                                holder, theCategory, theAction, keystroke,
                                obj.selected) as JRadioButtonMenuItem
                        if (buttonGroup == null) buttonGroup = ButtonGroup()
                        buttonGroup.add(item)
                    } else {
                        add(holder, theCategory, theAction, keystroke)
                    }
                } catch (e1: Exception) {
                    Resources.getInstance().logException(e1)
                }
            } else if (obj is MenuSeparator) {
                holder.addSeparator(category)
            } /* else exception */
        }
    }

    override val popupMenu: JPopupMenu?
        get() = popupmenu

    /**
     * Link implementation: If this is a link, we want to make a popup with at
     * least removelink available.
     */
    override fun getPopupForModel(obj: Any?): JPopupMenu? {
        if (obj is MindMapArrowLinkModel) {
            // yes, this is a link.
            val link = obj
            val arrowLinkPopup = JPopupMenu()
            // block the screen while showing popup.
            arrowLinkPopup.addPopupMenuListener(popupListenerSingleton)
            removeArrowLinkAction.setArrowLink(link)
            arrowLinkPopup.add(RemoveArrowLinkAction(this, link))
            arrowLinkPopup.add(ColorArrowLinkAction(this, link))
            arrowLinkPopup.addSeparator()
            /* The arrow state as radio buttons: */
            val itemnn = JRadioButtonMenuItem(
                    ChangeArrowsInArrowLinkAction(this, "none",
                            "images/arrow-mode-none.png", link, false, false))
            val itemnt = JRadioButtonMenuItem(
                    ChangeArrowsInArrowLinkAction(this, "forward",
                            "images/arrow-mode-forward.png", link, false, true))
            val itemtn = JRadioButtonMenuItem(
                    ChangeArrowsInArrowLinkAction(this, "backward",
                            "images/arrow-mode-backward.png", link, true, false))
            val itemtt = JRadioButtonMenuItem(
                    ChangeArrowsInArrowLinkAction(this, "both",
                            "images/arrow-mode-both.png", link, true, true))
            itemnn.text = null
            itemnt.text = null
            itemtn.text = null
            itemtt.text = null
            arrowLinkPopup.add(itemnn)
            arrowLinkPopup.add(itemnt)
            arrowLinkPopup.add(itemtn)
            arrowLinkPopup.add(itemtt)
            // select the right one:
            val a = link.startArrow != "None"
            val b = link.endArrow != "None"
            itemtt.isSelected = a && b
            itemnt.isSelected = !a && b
            itemtn.isSelected = a && !b
            itemnn.isSelected = !a && !b
            arrowLinkPopup.addSeparator()
            arrowLinkPopup.add(GotoLinkNodeAction(this, link.source))
            arrowLinkPopup.add(GotoLinkNodeAction(this, link.target))
            arrowLinkPopup.addSeparator()
            // add all links from target and from source:
            val NodeAlreadyVisited = HashSet<MindMapNode>()
            NodeAlreadyVisited.add(link.source)
            NodeAlreadyVisited.add(link.target!!)
            val links = mindMapMapModel.linkRegistry!!.getAllLinks(link.source)
            links!!.addAll(mindMapMapModel.linkRegistry!!.getAllLinks(
                    link.target))
            for (i in links.indices) {
                val foreign_link = links[i] as MindMapArrowLinkModel
                if (NodeAlreadyVisited.add(foreign_link.target!!)) {
                    arrowLinkPopup.add(GotoLinkNodeAction(this,
                            foreign_link.target))
                }
                if (NodeAlreadyVisited.add(foreign_link.source)) {
                    arrowLinkPopup.add(GotoLinkNodeAction(this,
                            foreign_link.source))
                }
            }
            return arrowLinkPopup
        }
        return null
    }

    // convenience methods
    val mindMapMapModel: MindMapMapModel
        get() = map as MindMapMapModel
    override val modeToolBar: JToolBar?
        get() = toolBar
    override val leftToolBar: Component?
        get() = toolBar.getLeftToolBar()

    /**
     * Enabled/Disabled all actions that are dependent on whether there is a map
     * open or not.
     */
    override fun setAllActions(enabled: Boolean) {
        MapFeedbackAdapter.Companion.logger!!.fine("setAllActions:$enabled")
        super.setAllActions(enabled)
        // own actions
        increaseNodeFont.isEnabled = enabled
        decreaseNodeFont.isEnabled = enabled
        exportBranch.isEnabled = enabled
        exportBranchToHTML.isEnabled = enabled
        editLong.isEnabled = enabled
        newSibling.isEnabled = enabled
        newPreviousSibling.isEnabled = enabled
        setLinkByFileChooser.isEnabled = enabled
        setImageByFileChooser.isEnabled = enabled
        followLink.isEnabled = enabled
        for (i in iconActions.indices) {
            (iconActions[i] as Action?)!!.isEnabled = enabled
        }
        save.isEnabled = enabled
        saveAs.isEnabled = enabled
        toolBar!!.setAllActions(enabled)
        exportBranch.isEnabled = enabled
        exportToHTML.isEnabled = enabled
        importBranch.isEnabled = enabled
        importLinkedBranch.isEnabled = enabled
        importLinkedBranchWithoutRoot.isEnabled = enabled
        // hooks:
        for (i in hookActions!!.indices) {
            (hookActions!![i] as Action).isEnabled = enabled
        }
        cut!!.isEnabled = enabled
        copy!!.isEnabled = enabled
        copySingle!!.isEnabled = enabled
        paste!!.isEnabled = enabled
        pasteAsPlainText!!.isEnabled = enabled
        undo!!.isEnabled = enabled
        redo!!.isEnabled = enabled
        edit!!.isEnabled = enabled
        newChild!!.isEnabled = enabled
        toggleFolded!!.isEnabled = enabled
        toggleChildrenFolded!!.isEnabled = enabled
        setLinkByTextField!!.isEnabled = enabled
        italic!!.isEnabled = enabled
        bold!!.isEnabled = enabled
        strikethrough!!.isEnabled = enabled
        find!!.isEnabled = enabled
        findNext!!.isEnabled = enabled
        addArrowLinkAction!!.isEnabled = enabled
        addLocalLinkAction!!.isEnabled = enabled
        nodeColorBlend!!.isEnabled = enabled
        nodeUp!!.isEnabled = enabled
        nodeBackgroundColor!!.isEnabled = enabled
        nodeDown!!.isEnabled = enabled
        importExplorerFavorites!!.isEnabled = enabled
        importFolderStructure!!.isEnabled = enabled
        joinNodes!!.isEnabled = enabled
        deleteChild!!.isEnabled = enabled
        cloud!!.isEnabled = enabled
        cloudColor!!.isEnabled = enabled
        // normalFont.setEnabled(enabled);
        nodeColor!!.isEnabled = enabled
        edgeColor!!.isEnabled = enabled
        removeLastIconAction!!.isEnabled = enabled
        removeAllIconsAction!!.isEnabled = enabled
        selectAllAction!!.isEnabled = enabled
        selectBranchAction!!.isEnabled = enabled
        removeNodeBackgroundColor!!.isEnabled = enabled
        moveNodeAction!!.isEnabled = enabled
        revertAction!!.isEnabled = enabled
        for (i in edgeWidths!!.indices) {
            edgeWidths!![i].isEnabled = enabled
        }
        fork!!.isEnabled = enabled
        bubble!!.isEnabled = enabled
        for (i in edgeStyles!!.indices) {
            edgeStyles!![i].isEnabled = enabled
        }
        for (i in patterns.indices) {
            patterns[i]!!.isEnabled = enabled
        }
        useRichFormatting!!.isEnabled = enabled
        usePlainText!!.isEnabled = enabled
    }

    //
    // Actions
    // _______________________________________________________________________________
    // This may later be moved to ControllerAdapter. So far there is no reason
    // for it.
    protected inner class ExportToHTMLAction(controller: MindMapController) : MindmapAction("export_to_html", controller) {
        override fun actionPerformed(e: ActionEvent) {
            // from
            // https://sourceforge.net/tracker2/?func=detail&atid=307118&aid=1789765&group_id=7118
            if (map.file == null) {
                JOptionPane.showMessageDialog(frame.contentPane,
                        getText("map_not_saved"), "FreeMind",
                        JOptionPane.WARNING_MESSAGE)
                return
            }
            try {
                val file = File(mindMapMapModel.file.toString() + ".html")
                saveHTML(mindMapMapModel.root as MindMapNodeModel,
                        file)
                loadURL(file.toString())
            } catch (ex: IOException) {
                Resources.getInstance().logException(ex)
            }
        }
    }

    protected inner class ExportBranchToHTMLAction(controller: MindMapController) : MindmapAction("export_branch_to_html", controller) {
        override fun actionPerformed(e: ActionEvent) {
            val mindmapFile = map.file
            if (mindmapFile == null) {
                JOptionPane.showMessageDialog(frame.contentPane,
                        getText("map_not_saved"), "FreeMind",
                        JOptionPane.WARNING_MESSAGE)
                return
            }
            try {
                val file = File.createTempFile(
                        mindmapFile.name.replace(
                                FreeMindCommon.FREEMIND_FILE_EXTENSION, "_"),
                        ".html", mindmapFile.parentFile)
                saveHTML(selected as MindMapNodeModel, file)
                loadURL(file.toString())
            } catch (ex: IOException) {
            }
        }
    }

    private inner class ImportBranchAction internal constructor() : MindmapAction("import_branch", this@MindMapController) {
        override fun actionPerformed(e: ActionEvent) {
            val parent = selected as MindMapNodeModel ?: return
            val chooser = fileChooser
            val returnVal = chooser!!.showOpenDialog(frame.contentPane)
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    val node = loadTree(
                            chooser.selectedFile)
                    paste(node, parent)
                    invokeHooksRecursively(node, mindMapMapModel)
                } catch (ex: Exception) {
                    handleLoadingException(ex)
                }
            }
        }
    }

    private inner class ImportLinkedBranchAction internal constructor() : MindmapAction("import_linked_branch", this@MindMapController) {
        override fun actionPerformed(e: ActionEvent) {
            val selected = selected as MindMapNodeModel
            if (selected == null || selected.link == null) {
                JOptionPane.showMessageDialog(view,
                        getText("import_linked_branch_no_link"))
                return
            }
            var absolute: URL? = null
            absolute = try {
                val relative = selected.link
                URL(fileToUrl(map.file),
                        relative)
            } catch (ex: MalformedURLException) {
                JOptionPane.showMessageDialog(view,
                        "Couldn't create valid URL for:" + map.file)
                Resources.getInstance().logException(ex)
                return
            }
            try {
                val node = loadTree(
                        urlToFile(absolute))
                paste(node, selected)
                invokeHooksRecursively(node, mindMapMapModel)
            } catch (ex: Exception) {
                handleLoadingException(ex)
            }
        }
    }

    /**
     * This is exactly the opposite of exportBranch.
     */
    private inner class ImportLinkedBranchWithoutRootAction internal constructor() : MindmapAction("import_linked_branch_without_root", this@MindMapController) {
        override fun actionPerformed(e: ActionEvent) {
            val selected = selected as MindMapNodeModel
            if (selected == null || selected.link == null) {
                JOptionPane.showMessageDialog(view,
                        getText("import_linked_branch_no_link"))
                return
            }
            var absolute: URL? = null
            absolute = try {
                val relative = selected.link
                URL(fileToUrl(map.file),
                        relative)
            } catch (ex: MalformedURLException) {
                JOptionPane.showMessageDialog(view,
                        "Couldn't create valid URL.")
                return
            }
            try {
                val node = loadTree(
                        urlToFile(absolute))
                val i: ListIterator<*>? = node!!.childrenUnfolded()
                while (i!!.hasNext()) {
                    val importNode = i.next() as MindMapNodeModel
                    paste(importNode, selected)
                    invokeHooksRecursively(importNode, mindMapMapModel)
                }
            } // getModel().setLink(parent, null); }
            catch (ex: Exception) {
                handleLoadingException(ex)
            }
        }
    }

    /*
	 * MindMapController.java private class NodeViewStyleAction extends
	 * AbstractAction { NodeViewStyleAction(final String style) {
	 * super(getText(style)); m_style = style; } public void
	 * actionPerformed(ActionEvent e) { for(ListIterator it =
	 * getSelecteds().listIterator();it.hasNext();) { MindMapNodeModel selected
	 * = (MindMapNodeModel)it.next(); getModel().setNodeStyle(selected,
	 * m_style); }} private String m_style;}
	 * 
	 * private class EdgeStyleAction extends AbstractAction { String style;
	 * EdgeStyleAction(String style) { super(getText(style)); this.style =
	 * style; } public void actionPerformed(ActionEvent e) { for(ListIterator it
	 * = getSelecteds().listIterator();it.hasNext();) { MindMapNodeModel
	 * selected = (MindMapNodeModel)it.next(); getModel().setEdgeStyle(selected,
	 * style); }}}
	 * 
	 * private class ApplyPatternAction extends AbstractAction { StylePattern
	 * pattern; ApplyPatternAction(StylePattern pattern) {
	 * super(pattern.getName()); this.pattern=pattern; } public void
	 * actionPerformed(ActionEvent e) { for(ListIterator it =
	 * getSelecteds().listIterator();it.hasNext();) { MindMapNodeModel selected
	 * = (MindMapNodeModel)it.next();
	 * ((MindMapMapModel)getModel()).applyPattern(selected, pattern); }}}
	 * 
	 * 
	 * 
	 * 
	 * // Nonaction classes //
	 * ________________________________________________________________________
	 */
    private inner class MindMapFilter : FileFilter() {
        override fun accept(f: File): Boolean {
            if (f.isDirectory) return true
            val extension = getExtension(f.name)
            return if (extension != null) {
                if (extension
                        == FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT) {
                    true
                } else {
                    false
                }
            } else false
        }

        override fun getDescription(): String {
            return getText("mindmaps_desc")
        }
    }

    override fun setBold(node: MindMapNode?, bolded: Boolean) {
        actorFactory.getBoldActor().setBold(node, bolded)
    }

    override fun setStrikethrough(node: MindMapNode?, strikethrough: Boolean) {
        actorFactory.getStrikethroughActor().setStrikethrough(node, strikethrough)
    }

    override fun setItalic(node: MindMapNode?, isItalic: Boolean) {
        actorFactory.getItalicActor().setItalic(node, isItalic)
    }

    override fun setCloud(node: MindMapNode?, enable: Boolean) {
        actorFactory.getCloudActor().setCloud(node, enable)
    }

    override fun setCloudColor(node: MindMapNode?, color: Color?) {
        actorFactory.getCloudColorActor().setCloudColor(node, color)
    }

    // Node editing
    override fun setFontSize(node: MindMapNode, fontSizeValue: String?) {
        actorFactory.getFontSizeActor().setFontSize(node, fontSizeValue)
    }

    /**
     *
     */
    fun increaseFontSize(node: MindMapNode, increment: Int) {
        val newSize = (Integer.valueOf(node.fontSize).toInt()
                + increment)
        if (newSize > 0) {
            setFontSize(node, Integer.toString(newSize))
        }
    }

    override fun setFontFamily(node: MindMapNode, fontFamilyValue: String?) {
        actorFactory.getFontFamilyActor().setFontFamily(node, fontFamilyValue)
    }

    override fun setNodeColor(node: MindMapNode?, color: Color?) {
        actorFactory.getNodeColorActor().setNodeColor(node, color)
    }

    override fun setNodeBackgroundColor(node: MindMapNode?, color: Color?) {
        actorFactory.getNodeBackgroundColorActor().setNodeBackgroundColor(node, color)
    }

    override fun blendNodeColor(node: MindMapNode) {
        val mapColor = view.background
        var nodeColor = node.color
        if (nodeColor == null) {
            nodeColor = MapView.standardNodeTextColor
        }
        setNodeColor(node,
                Color((3 * mapColor.red + nodeColor!!.red) / 4,
                        (3 * mapColor.green + nodeColor.green) / 4,
                        (3 * mapColor.blue + nodeColor.blue) / 4))
    }

    override fun setEdgeColor(node: MindMapNode?, color: Color?) {
        actorFactory.getEdgeColorActor().setEdgeColor(node, color)
    }

    fun applyPattern(node: MindMapNode, patternName: String) {
        for (i in patterns.indices) {
            val patternAction = patterns[i]
            if (patternAction.getPattern().name == patternName) {
                StylePatternFactory.applyPattern(node, patternAction.getPattern(), patternsList, plugins, this)
                break
            }
        }
    }

    override fun applyPattern(node: MindMapNode, pattern: freemind.controller.actions.generated.instance.Pattern?) {
        StylePatternFactory.applyPattern(node, pattern, patternsList, plugins, this)
    }

    override fun addIcon(node: MindMapNode?, icon: MindIcon?) {
        actorFactory.getAddIconActor().addIcon(node, icon)
    }

    override fun removeAllIcons(node: MindMapNode?) {
        actorFactory.getRemoveAllIconsActor().removeAllIcons(node)
    }

    override fun removeLastIcon(node: MindMapNode): Int {
        return actorFactory.getRemoveIconActor().removeLastIcon(node)
    }

    /**
     *
     */
    override fun addLink(source: MindMapNode, target: MindMapNode) {
        actorFactory.getAddArrowLinkActor().addLink(source, target)
    }

    override fun removeReference(arrowLink: MindMapLink?) {
        actorFactory.getRemoveArrowLinkActor().removeReference(arrowLink)
    }

    override fun setArrowLinkColor(arrowLink: MindMapLink?, color: Color) {
        actorFactory.getColorArrowLinkActor().setArrowLinkColor(arrowLink, color)
    }

    /**
     *
     */
    override fun changeArrowsOfArrowLink(arrowLink: MindMapArrowLink?,
                                         hasStartArrow: Boolean, hasEndArrow: Boolean) {
        actorFactory.getChangeArrowsInArrowLinkActor().changeArrowsOfArrowLink(arrowLink,
                hasStartArrow, hasEndArrow)
    }

    override fun setArrowLinkEndPoints(link: MindMapArrowLink, startPoint: Point?,
                                       endPoint: Point?) {
        actorFactory.getChangeArrowLinkEndPointsActor().setArrowLinkEndPoints(link, startPoint,
                endPoint)
    }

    override fun setLink(node: MindMapNode?, link: String?) {
        actorFactory.getSetLinkActor().setLink(node, link)
    }

    // edit begins with home/end or typing (PN 6.2)
    override fun edit(e: KeyEvent?, addNew: Boolean, editLong: Boolean) {
        edit!!.edit(e, addNew, editLong)
    }

    override fun setNodeText(selected: MindMapNode?, newText: String?) {
        actorFactory.getEditActor().setNodeText(selected, newText)
    }

    /**
     *
     */
    override fun setEdgeWidth(node: MindMapNode, width: Int) {
        actorFactory.getEdgeWidthActor().setEdgeWidth(node, width)
    }

    /**
     *
     */
    override fun setEdgeStyle(node: MindMapNode, style: String?) {
        actorFactory.getEdgeStyleActor().setEdgeStyle(node, style)
    }

    /**
     *
     */
    override fun setNodeStyle(node: MindMapNode, style: String?) {
        actorFactory.getNodeStyleActor().setStyle(node, style)
    }

    override fun copy(node: MindMapNode?, saveInvisible: Boolean): Transferable {
        val stringWriter = StringWriter()
        try {
            (node as MindMapNodeModel?)!!.save(stringWriter, map
                    .linkRegistry, saveInvisible, true)
        } catch (e: IOException) {
        }
        val nodeList = getVectorWithSingleElement(getNodeID(node))
        return MindMapNodesSelection(stringWriter.toString(), null, null,
                null, null, null, null, nodeList)
    }

    override fun cut(): Transferable? {
        return cut(view.selectedNodesSortedByY)
    }

    override fun cut(nodeList: List<MindMapNode?>?): Transferable? {
        return actorFactory.getCutActor().cut(nodeList)
    }

    fun paste(t: Transferable?, parent: MindMapNode) {
        paste(t,  /* target= */parent,  /* asSibling= */false,
                parent.isNewChildLeft)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.mindmapmode.actions.MindMapActions#paste(java.awt.datatransfer
	 * .Transferable, freemind.modes.MindMapNode, boolean, boolean)
	 */
    override fun paste(t: Transferable?, target: MindMapNode, asSibling: Boolean,
                       isLeft: Boolean): Boolean {
        if (!asSibling
                && target.isFolded
                && Resources.getInstance().getBoolProperty(
                        RESOURCE_UNFOLD_ON_PASTE)) {
            setFolded(target, false)
        }
        return actorFactory.getPasteActor().paste(t, target, asSibling, isLeft)
    }

    override fun paste(node: MindMapNode?, parent: MindMapNode?) {
        actorFactory.getPasteActor().paste(node, parent)
    }

    fun addNew(target: MindMapNode, newNodeMode: Int,
               e: KeyEvent?): MindMapNode? {
        edit!!.stopEditing()
        return newChild!!.addNew(target, newNodeMode, e)
    }

    override fun addNewNode(parent: MindMapNode?, index: Int,
                            newNodeIsLeft: Boolean): MindMapNode? {
        return actorFactory.getNewChildActor().addNewNode(parent, index, newNodeIsLeft)
    }

    override fun deleteNode(selectedNode: MindMapNode?) {
        actorFactory.getDeleteChildActor().deleteNode(selectedNode)
    }

    override fun toggleFolded() {
        actorFactory.getToggleFoldedActor().toggleFolded(selecteds.listIterator())
    }

    override fun setFolded(node: MindMapNode, folded: Boolean) {
        actorFactory.getToggleFoldedActor().setFolded(node, folded)
    }

    override fun moveNodes(selected: MindMapNode, selecteds: List<MindMapNode?>?, direction: Int) {
        actorFactory.getNodeUpActor().moveNodes(selected, selecteds, direction)
    }

    fun joinNodes(selectedNode: MindMapNode, selectedNodes: List<MindMapNode>) {
        joinNodes!!.joinNodes(selectedNode, selectedNodes)
    }

    protected fun setLinkByFileChooser() {
        val relative = getLinkByFileChooser(null)
        if (relative != null) setLink(selected as NodeAdapter, relative)
    }

    protected fun setImageByFileChooser() {
        val filter = ExampleFileFilter()
        filter.addExtension("jpg")
        filter.addExtension("jpeg")
        filter.addExtension("png")
        filter.addExtension("gif")
        filter.setDescription("JPG, PNG and GIF Images")
        val relative = getLinkByFileChooser(filter)
        if (relative != null) {
            val strText = ("<html><body><img src=\"" + relative
                    + "\"/></body></html>")
            setNodeText(selected as MindMapNode, strText)
        }
    }

    protected fun getLinkByFileChooser(fileFilter: FileFilter?): String? {
        var relative: String? = null
        val input: File
        val chooser = getFileChooser(fileFilter)
        if (map.file == null) {
            JOptionPane.showMessageDialog(frame.contentPane,
                    getText("not_saved_for_link_error"), "FreeMind",
                    JOptionPane.WARNING_MESSAGE)
            return null
            // In the previous version Freemind automatically displayed save
            // dialog. It happened very often, that user took this save
            // dialog to be an open link dialog; as a result, the new map
            // overwrote the linked map.
        }
        val returnVal = chooser!!.showOpenDialog(frame.contentPane)
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            input = chooser.selectedFile
            relative = fileToRelativeUrlString(input, map.file)
        }
        return relative
    }

    override fun loadURL(relative: String?) {
        if (map.file == null) {
            frame.out("You must save the current map first!")
            val result = save()
            // canceled??
            if (!result) {
                return
            }
        }
        super.loadURL(relative)
    }

    override fun addHook(focussed: MindMapNode?, selecteds: List<MindMapNode?>?, hookName: String?, pHookProperties: Properties?) {
        actorFactory.getAddHookActor().addHook(focussed, selecteds, hookName, pHookProperties)
    }

    override fun removeHook(focussed: MindMapNode?, selecteds: List<MindMapNode?>?, hookName: String?) {
        actorFactory.getAddHookActor().removeHook(focussed, selecteds, hookName)
    }

    protected inner class SetLinkByFileChooserAction : MindmapAction("set_link_by_filechooser", this@MindMapController) {
        override fun actionPerformed(e: ActionEvent) {
            setLinkByFileChooser()
        }
    }

    protected inner class SetImageByFileChooserAction : MindmapAction("set_image_by_filechooser", this@MindMapController) {
        override fun actionPerformed(e: ActionEvent) {
            setImageByFileChooser()
            controller.obtainFocusForSelected()
        }
    }

    protected abstract inner class LinkActionBase(pText: String?) : MindmapAction(pText, this@MindMapController) {
        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            if (!super.isEnabled(pItem, pAction)) {
                return false
            }
            for (selNode in selecteds) {
                if (selNode.link != null) return true
            }
            return false
        }
    }

    protected inner class FollowLinkAction : LinkActionBase("follow_link") {
        override fun actionPerformed(e: ActionEvent) {
            for (selNode in selecteds) {
                if (selNode.link != null) {
                    loadURL(selNode.link)
                }
            }
        }
    }

    protected inner class OpenLinkDirectoryAction : LinkActionBase("open_link_directory") {
        override fun actionPerformed(event: ActionEvent) {
            var link = ""
            for (selNode in selecteds) {
                link = selNode.link
                if (link != null) {
                    // as link is an URL, '/' is the only correct one.
                    val i = link.lastIndexOf('/')
                    if (i >= 0) {
                        link = link.substring(0, i + 1)
                    }
                    FreemindAction.Companion.logger!!.info("Opening link for directory $link")
                    loadURL(link)
                }
            }
        }
    }

    override fun moveNodePosition(node: MindMapNode, parentVGap: Int, hGap: Int,
                                  shiftY: Int) {
        actorFactory.getMoveNodeActor().moveNodeTo(node, parentVGap, hGap, shiftY)
    }

    // ///////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////
    override fun plainClick(e: MouseEvent) {
        /* perform action only if one selected node. */
        if (selecteds.size != 1) return
        val component = e.component as MainView
        if (component.isInFollowLinkRegion(e.x.toDouble())) {
            loadURL()
        } else {
            val node = component.nodeView.model
            if (!node.hasChildren()) {
                // then emulate the plain click.
                doubleClick(e)
                return
            }
            toggleFolded()
        }
    }

    // lazy creation.
    override val hookFactory: HookFactory
        get() {
            // lazy creation.
            if (nodeHookFactory == null) {
                nodeHookFactory = MindMapHookFactory()
            }
            return nodeHookFactory!!
        }

    override fun createNodeHook(hookName: String?, node: MindMapNode): NodeHook? {
        val hookFactory = hookFactory
        val hook = hookFactory.createNodeHook(hookName)
        hook!!.setController(this)
        hook.setMap(map)
        if (hook is PermanentNodeHook) {
            if (hookFactory.getInstanciationMethod(hookName)!!.isSingleton) {
                // search for already instanciated hooks of this type:
                val otherHook = hookFactory.getHookInNode(node,
                        hookName!!)
                if (otherHook != null) {
                    return otherHook
                }
            }
            node.addHook(hook)
        }
        return hook
    }

    fun invokeHook(hook: ModeControllerHook?) {
        try {
            hook!!.setController(this)
            // initialize:
            // the main invocation:
            hook.startupMapHook()
            // and good bye.
            hook.shutdownMapHook()
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
    }

    protected inner class EditLongAction : MindmapAction("edit_long_node", this@MindMapController) {
        override fun actionPerformed(e: ActionEvent) {
            edit(null, false, true)
        }
    }

    /**
     */
    fun splitNode(node: MindMapNode, caretPosition: Int, newText: String?) {
        if (node.isRoot) {
            return
        }
        // If there are children, they go to the node below
        val futureText = newText ?: node.toString()
        val strings = getContent(futureText, caretPosition)
                ?: // do nothing
                return
        val newUpperContent = strings[0]
        val newLowerContent = strings[1]
        setNodeText(node, newUpperContent)
        val parent = node.parentNode
        val lowerNode = addNewNode(parent,
                parent!!.getChildPosition(node) + 1, node.isLeft)
        lowerNode.setColor(node.color)
        lowerNode.setFont(node.font)
        setNodeText(lowerNode, newLowerContent)
    }

    private fun getContent(text: String, pos: Int): Array<String?>? {
        if (pos <= 0) {
            return null
        }
        val strings = arrayOfNulls<String>(2)
        if (text.startsWith("<html>")) {
            val kit = HTMLEditorKit()
            val doc = HTMLDocument()
            val buf = StringReader(text)
            try {
                kit.read(buf, doc, 0)
                val firstText = doc.getText(0, pos).toCharArray()
                var firstStart = 0
                var firstLen = pos
                while (firstStart < firstLen
                        && firstText[firstStart] <= ' ') {
                    firstStart++
                }
                while (firstStart < firstLen
                        && firstText[firstLen - 1] <= ' ') {
                    firstLen--
                }
                var secondStart = 0
                var secondLen = doc.length - pos
                val secondText = doc.getText(pos, secondLen)
                        .toCharArray()
                while (secondStart < secondLen
                        && secondText[secondStart] <= ' ') {
                    secondStart++
                }
                while (secondStart < secondLen
                        && secondText[secondLen - 1] <= ' ') {
                    secondLen--
                }
                if (firstStart == firstLen || secondStart == secondLen) {
                    return null
                }
                var out = StringWriter()
                FixedHTMLWriter(out, doc, firstStart, firstLen - firstStart)
                        .write()
                strings[0] = out.toString()
                out = StringWriter()
                FixedHTMLWriter(out, doc, pos + secondStart, secondLen
                        - secondStart).write()
                strings[1] = out.toString()
                return strings
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            } catch (e: BadLocationException) {
                // TODO Auto-generated catch block
                Resources.getInstance().logException(e)
            }
        } else {
            if (pos >= text.length) {
                return null
            }
            strings[0] = text.substring(0, pos)
            strings[1] = text.substring(pos)
        }
        return strings
    }

    override fun updateNode(node: MindMapNode?) {
        super.updateNode(node)
        recursiveCallUpdateHooks(node, node /*
																		 * self
																		 * update
																		 */)
    }

    /**
     */
    private fun recursiveCallUpdateHooks(node: MindMapNode?,
                                         changedNode: MindMapNode?) {
        // Tell any node hooks that the node is changed:
        if (node is MindMapNode) {
            for (hook in node.activatedHooks) {
                if (!isUndoAction || hook is UndoEventReceiver) {
                    if (node === changedNode) hook!!.onUpdateNodeHook() else hook!!.onUpdateChildrenHook(changedNode)
                }
            }
        }
        if (!node!!.isRoot && node.parentNode != null) recursiveCallUpdateHooks(node.parentNode, changedNode)
    }

    override fun doubleClick(e: MouseEvent) {
        /* perform action only if one selected node. */
        if (selecteds.size != 1) return
        val node = (e.component as MainView).nodeView
                .model
        // edit the node only if the node is a leaf (fc 0.7.1), or the root node
        // (fc 0.9.0)
        if (!e.isAltDown && !e.isControlDown && !e.isShiftDown
                && !e.isPopupTrigger && e.button == MouseEvent.BUTTON1 && node.link == null) {
            edit(null, false, false)
        }
    }

    override fun extendSelection(e: MouseEvent?): Boolean {
        val newlySelectedNodeView = (e!!.component as MainView)
                .nodeView
        // MindMapNode newlySelectedNode = newlySelectedNodeView.getModel();
        var extend = e.isControlDown
        // Fixes Cannot select multiple single nodes *
        // https://sourceforge.net/tracker/?func=detail&atid=107118&aid=1675829&group_id=7118
        if (isMacOsX) {
            extend = extend or e.isMetaDown
        }
        val range = e.isShiftDown
        val branch = e.isAltGraphDown || e.isAltDown /*
															 * windows alt,
															 * linux altgraph
															 * ....
															 */
        var retValue = false
        if (extend || range || branch
                || !view.isSelected(newlySelectedNodeView)) {
            retValue = if (!range) {
                if (extend) view.toggleSelected(newlySelectedNodeView) else select(newlySelectedNodeView)
                true
            } else {
                view.selectContinuous(newlySelectedNodeView)
                // /* fc, 25.1.2004: replace getView by controller methods.*/
                // if (newlySelectedNodeView != getView().getSelected() &&
                // newlySelectedNodeView.isSiblingOf(getView().getSelected())) {
                // getView().selectContinuous(newlySelectedNodeView);
                // retValue = true;
                // } else {
                // /* if shift was down, but no range can be selected, then the
                // new node is simply selected: */
                // if(!getView().isSelected(newlySelectedNodeView)) {
                // getView().toggleSelected(newlySelectedNodeView);
                // retValue = true;
                // }
            }
            if (branch) {
                view.selectBranch(newlySelectedNodeView, extend)
                retValue = true
            }
        }
        if (retValue) {
            e.consume()

            // Display link in status line
            val link = newlySelectedNodeView.model.link
            if (link != null) {
                controller.frame.out(link)
            }
        }
        MapFeedbackAdapter.Companion.logger!!.fine("MouseEvent: extend:" + extend + ", range:" + range
                + ", branch:" + branch + ", event:" + e + ", retValue:"
                + retValue)
        obtainFocusForSelected()
        return retValue
    }

    override fun registerMouseWheelEventHandler(handler: MouseWheelEventHandler) {
        MapFeedbackAdapter.Companion.logger!!.fine("Registered   MouseWheelEventHandler $handler")
        mRegisteredMouseWheelEventHandler.add(handler)
    }

    override fun deRegisterMouseWheelEventHandler(handler: MouseWheelEventHandler) {
        MapFeedbackAdapter.Companion.logger!!.fine("Deregistered MouseWheelEventHandler $handler")
        mRegisteredMouseWheelEventHandler.remove(handler)
    }

    override val registeredMouseWheelEventHandler: Set<MouseWheelEventHandler>
        get() = Collections.unmodifiableSet(mRegisteredMouseWheelEventHandler)

    fun marshall(action: XmlAction?): String {
        return Tools.marshall(action)
    }

    fun unMarshall(inputString: String?): XmlAction {
        return Tools.unMarshall(inputString)
    }

    fun storeDialogPositions(dialog: JDialog?,
                             pStorage: WindowConfigurationStorage?,
                             window_preference_storage_property: String?) {
        XmlBindingTools.getInstance().storeDialogPositions(controller,
                dialog, pStorage, window_preference_storage_property)
    }

    fun decorateDialog(dialog: JDialog?,
                       window_preference_storage_property: String?): WindowConfigurationStorage {
        return XmlBindingTools.getInstance().decorateDialog(controller,
                dialog, window_preference_storage_property)
    }

    override fun insertNodeInto(newNode: MindMapNode?, parent: MindMapNode?,
                                index: Int) {
        setSaved(false)
        map.insertNodeInto(newNode, parent, index)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.MindMap#insertNodeInto(javax.swing.tree.MutableTreeNode,
	 * javax.swing.tree.MutableTreeNode)
	 */
    fun insertNodeInto(newChild: MindMapNode?, parent: MindMapNode?) {
        insertNodeInto(newChild, parent, parent!!.childCount)
    }

    override fun removeNodeFromParent(selectedNode: MindMapNode?) {
        setSaved(false)
        // first deselect, and then remove.
        val nodeView = view.getNodeView(selectedNode)
        view.deselect(nodeView)
        model!!.removeNodeFromParent(selectedNode)
    }

    fun repaintMap() {
        view.repaint()
    }

    /**
     * Erases all content of the node as text, colors, fonts, etc.
     */
    fun clearNodeContents(pNode: MindMapNode) {
        val erasePattern = Pattern()
        erasePattern.patternEdgeColor = PatternEdgeColor()
        erasePattern.patternEdgeStyle = PatternEdgeStyle()
        erasePattern.patternEdgeWidth = PatternEdgeWidth()
        erasePattern.patternIcon = PatternIcon()
        erasePattern
                .patternNodeBackgroundColor = PatternNodeBackgroundColor()
        erasePattern.patternNodeColor = PatternNodeColor()
        erasePattern.patternNodeFontBold = PatternNodeFontBold()
        erasePattern.patternNodeFontItalic = PatternNodeFontItalic()
        erasePattern.patternNodeFontName = PatternNodeFontName()
        erasePattern.patternNodeFontSize = PatternNodeFontSize()
        erasePattern.patternNodeStyle = PatternNodeStyle()
        erasePattern.patternNodeText = PatternNodeText()
        applyPattern(pNode, erasePattern)
        setNoteText(pNode, null)
    }

    fun registerPlugin(pPlugin: MindMapControllerPlugin?) {
        mPlugins.add(pPlugin)
    }

    fun deregisterPlugin(pPlugin: MindMapControllerPlugin?) {
        mPlugins.remove(pPlugin)
    }

    val plugins: Set<MindMapControllerPlugin?>
        get() = Collections.unmodifiableSet(mPlugins)
    /**
     */
    /**
     */
    var clipboardContents: Transferable?
        get() {
            getClipboard()
            return clipboard!!.getContents(this)
        }
        set(t) {
            getClipboard()
            clipboard!!.setContents(t, null)
            if (selection != null) {
                selection!!.setContents(t, null)
            }
        }

    protected fun getClipboard() {
        if (clipboard == null) {
            val toolkit = Toolkit.getDefaultToolkit()
            selection = toolkit.systemSelection
            clipboard = toolkit.systemClipboard
        }
    }

    @Throws(Exception::class)
    override fun mapSourceChanged(pMap: MindMap?): Boolean {
        // ask the user, if he wants to reload the map.
        val runnable = MapSourceChangeDialog()
        invokeAndWait(runnable)
        return runnable.getReturnValue()
    }

    fun setNodeHookFactory(pNodeHookFactory: HookFactory?) {
        nodeHookFactory = pNodeHookFactory
    }

    /** Creates and invokes a ModeControllerHook. */
    fun createModeControllerHook(pHookName: String?) {
        val hookFactory = hookFactory
        // two different invocation methods:single or selecteds
        val hook = hookFactory
                .createModeControllerHook(pHookName)
        hook!!.setController(this)
        invokeHook(hook)
    }

    /**
     * Delegate method to Controller. Must be called after cut.s
     */
    fun obtainFocusForSelected() {
        controller.obtainFocusForSelected()
    }

    override fun doTransaction(pName: String?, pPair: ActionPair?): Boolean {
        return actionRegistry.doTransaction(pName, pPair)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.MindMapActions#setAttribute(freemind.modes.MindMapNode, int, freemind.modes.attributes.Attribute)
	 */
    override fun setAttribute(pNode: MindMapNode, pPosition: Int,
                              pAttribute: Attribute?) {
        actorFactory.getSetAttributeActor().setAttribute(pNode, pPosition, pAttribute)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.MindMapActions#insertAttribute(freemind.modes.MindMapNode, int, freemind.modes.attributes.Attribute)
	 */
    override fun insertAttribute(pNode: MindMapNode?, pPosition: Int,
                                 pAttribute: Attribute?) {
        actorFactory.getInsertAttributeActor().insertAttribute(pNode, pPosition, pAttribute)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.MindMapActions#addAttribute(freemind.modes.MindMapNode, freemind.modes.attributes.Attribute)
	 */
    override fun addAttribute(pNode: MindMapNode, pAttribute: Attribute?): Int {
        return actorFactory.getAddAttributeActor().addAttribute(pNode, pAttribute)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.MindMapActions#removeAttribute(freemind.modes.MindMapNode, int)
	 */
    override fun removeAttribute(pNode: MindMapNode, pPosition: Int) {
        actorFactory.getRemoveAttributeActor().removeAttribute(pNode, pPosition)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap.MapFeedback#out(java.lang.String)
	 */
    override fun out(pFormat: String?) {
        frame.out(pFormat)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.ExtendedMapFeedback#close(boolean)
	 */
    override fun close(pForce: Boolean) {
        controller.close(pForce)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.MindMapActions#setNoteText(freemind.modes.MindMapNode, java.lang.String)
	 */
    override fun setNoteText(pSelected: MindMapNode, pNewText: String?) {
        actorFactory.getChangeNoteTextActor().setNoteText(pSelected, pNewText)
    }

    companion object {
        const val REGEXP_FOR_NUMBERS_IN_STRINGS = "([+\\-]?[0-9]*[.,]?[0-9]+)\\b"
        private const val RESOURCE_UNFOLD_ON_PASTE = "unfold_on_paste"
        @Throws(IOException::class)
        fun saveHTML(rootNodeOfBranch: MindMapNodeModel, file: File?) {
            val fileout = BufferedWriter(OutputStreamWriter(
                    FileOutputStream(file)))
            val htmlWriter = MindMapHTMLWriter(fileout)
            htmlWriter.saveHTML(rootNodeOfBranch)
        }

        @Throws(IOException::class)
        fun saveHTML(mindMapNodes: List<MindMapNodeModel>, fileout: Writer) {
            val htmlWriter = MindMapHTMLWriter(fileout)
            htmlWriter.saveHTML(mindMapNodes)
        }
    }
}