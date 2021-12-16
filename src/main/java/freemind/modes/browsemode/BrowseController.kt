/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
package freemind.modes.browsemode

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
import java.util.LinkedList
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import java.awt.Dimension
import java.util.Locale
import java.lang.StringBuffer
import freemind.modes.common.dialogs.EnterPasswordDialog
import freemind.common.TextTranslator
import java.awt.BorderLayout
import java.awt.GridLayout
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import freemind.extensions.PermanentNodeHookAdapter
import freemind.modes.common.plugins.ReminderHookBase
import java.text.MessageFormat
import freemind.modes.common.plugins.ReminderHookBase.TimerBlinkTask
import freemind.modes.common.plugins.MapNodePositionHolderBase
import freemind.extensions.PermanentNodeHook
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import java.awt.Rectangle
import freemind.view.mindmapview.MapView
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.modes.common.listeners.CommonNodeMouseMotionListener
import freemind.main.Tools.BooleanHolder
import freemind.view.mindmapview.MainView
import java.awt.geom.Point2D
import freemind.modes.common.listeners.CommonNodeMouseMotionListener.timeDelayedSelection
import java.util.TimerTask
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
import java.awt.Color
import freemind.modes.filemode.FileEdgeModel
import java.util.Collections
import java.lang.SecurityException
import java.net.MalformedURLException
import freemind.modes.viewmodes.ViewControllerAdapter
import freemind.modes.common.actions.NewMapAction
import freemind.modes.filemode.FileController.CenterAction
import freemind.modes.filemode.FileController.OpenPathAction
import freemind.modes.filemode.FilePopupMenu
import freemind.modes.filemode.FileMapModel
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
import freemind.modes.browsemode.BrowseHookFactory
import freemind.modes.browsemode.BrowsePopupMenu
import freemind.modes.common.GotoLinkNodeAction
import java.security.AccessControlException
import freemind.extensions.HookFactoryAdapter
import freemind.extensions.ModeControllerHook
import freemind.modes.browsemode.BrowseReminderHook
import java.util.Properties
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
import freemind.controller.actions.generated.instance.PluginAction
import freemind.controller.actions.generated.instance.PluginRegistration
import freemind.extensions.MindMapHook
import freemind.controller.actions.generated.instance.PluginClasspath
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.controller.actions.generated.instance.PluginMode
import freemind.extensions.NodeHookAdapter
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorAdapter
import freemind.controller.actions.generated.instance.CutNodeAction
import java.awt.datatransfer.Transferable
import freemind.controller.actions.generated.instance.CompoundAction
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate
import freemind.controller.actions.generated.instance.XmlAction
import freemind.controller.actions.generated.instance.UndoPasteNodeAction
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.controller.actions.generated.instance.EditNodeAction
import freemind.modes.mindmapmode.actions.xml.actors.NodeXmlActorAdapter
import freemind.controller.actions.generated.instance.AddCloudXmlAction
import freemind.modes.mindmapmode.MindMapCloudModel
import freemind.controller.actions.generated.instance.PasteNodeAction
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.DataFlavorHandler
import freemind.modes.mindmapmode.MindMapNodeModel
import freemind.main.Tools.StringReaderCreator
import freemind.main.HtmlTools.NodeCreator
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.FileListFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.MindMapNodesFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.DirectHtmlFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.StringFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.ImageFlavorHandler
import freemind.controller.actions.generated.instance.TransferableContent
import freemind.controller.actions.generated.instance.TransferableFile
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import freemind.controller.actions.generated.instance.MoveNodesAction
import java.util.TreeSet
import freemind.controller.actions.generated.instance.NodeListMember
import freemind.controller.actions.generated.instance.RevertXmlAction
import freemind.modes.mindmapmode.actions.xml.actors.AddHookActor
import freemind.controller.actions.generated.instance.HookNodeAction
import freemind.extensions.DontSaveMarker
import freemind.controller.actions.generated.instance.NodeChildParameter
import freemind.controller.actions.generated.instance.AddIconAction
import freemind.controller.actions.generated.instance.AddLinkXmlAction
import freemind.controller.actions.generated.instance.BoldNodeAction
import freemind.modes.mindmapmode.actions.xml.ActorXml
import freemind.controller.actions.generated.instance.FontSizeNodeAction
import java.lang.NumberFormatException
import freemind.controller.actions.generated.instance.MoveNodeXmlAction
import freemind.controller.actions.generated.instance.DeleteNodeAction
import freemind.controller.actions.generated.instance.EdgeColorFormatAction
import freemind.modes.mindmapmode.MindMapEdgeModel
import freemind.controller.actions.generated.instance.EdgeStyleFormatAction
import freemind.controller.actions.generated.instance.EdgeWidthFormatAction
import freemind.controller.actions.generated.instance.NodeColorFormatAction
import freemind.controller.actions.generated.instance.NodeStyleFormatAction
import freemind.controller.actions.generated.instance.UnderlinedNodeAction
import freemind.controller.actions.generated.instance.CloudColorXmlAction
import freemind.controller.actions.generated.instance.FontNodeAction
import freemind.controller.actions.generated.instance.ItalicNodeAction
import freemind.controller.actions.generated.instance.RemoveIconXmlAction
import freemind.view.mindmapview.ViewFeedback
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
import freemind.controller.actions.generated.instance.AddArrowLinkXmlAction
import freemind.modes.mindmapmode.MindMapArrowLinkModel
import freemind.controller.actions.generated.instance.RemoveArrowLinkXmlAction
import freemind.controller.actions.generated.instance.AddAttributeAction
import freemind.controller.actions.generated.instance.RemoveAttributeAction
import freemind.controller.actions.generated.instance.SetAttributeAction
import freemind.controller.actions.generated.instance.FoldAction
import freemind.controller.actions.generated.instance.EditNoteToNodeAction
import freemind.controller.actions.generated.instance.ArrowLinkColorXmlAction
import freemind.modes.mindmapmode.actions.NodeActorXml
import freemind.controller.actions.generated.instance.RemoveAllIconsXmlAction
import freemind.controller.actions.generated.instance.InsertAttributeAction
import freemind.controller.actions.generated.instance.NodeAction
import freemind.controller.actions.generated.instance.StrikethroughNodeAction
import freemind.controller.actions.generated.instance.NodeBackgroundColorFormatAction
import freemind.controller.actions.generated.instance.ArrowLinkArrowXmlAction
import freemind.controller.actions.generated.instance.ArrowLinkPointXmlAction
import freemind.modes.mindmapmode.actions.xml.ActionFilter
import freemind.modes.mindmapmode.actions.xml.UndoActionHandler
import freemind.modes.mindmapmode.actions.xml.ActionRegistry
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FinalActionFilter
import freemind.modes.mindmapmode.actions.xml.ActionFilter.FirstActionFilter
import freemind.modes.mindmapmode.actions.MindmapAction
import freemind.modes.mindmapmode.actions.xml.PrintActionHandler
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.OptionalDontShowMeAgainDialog.StandardPropertyHandler
import freemind.modes.mindmapmode.actions.NodeGeneralAction
import freemind.view.mindmapview.EditNodeBase
import freemind.modes.mindmapmode.actions.MindMapActions
import freemind.view.mindmapview.EditNodeWYSIWYG
import freemind.view.mindmapview.EditNodeBase.EditControl
import freemind.view.mindmapview.EditNodeExternalApplication
import freemind.view.mindmapview.EditNodeDialog
import freemind.view.mindmapview.EditNodeTextField
import freemind.modes.mindmapmode.actions.EditAction
import freemind.modes.mindmapmode.actions.RemoveIconAction
import freemind.modes.mindmapmode.actions.xml.AbstractXmlAction
import freemind.modes.mindmapmode.actions.NodeUpAction
import freemind.modes.mindmapmode.actions.NewChildAction
import freemind.modes.mindmapmode.EncryptedMindMapNode
import freemind.modes.mindmapmode.actions.HookAction
import freemind.modes.mindmapmode.actions.NodeHookAction
import freemind.modes.mindmapmode.actions.EdgeWidthAction
import freemind.modes.mindmapmode.actions.JoinNodesAction
import freemind.modes.mindmapmode.actions.SingleNodeOperation
import freemind.modes.mindmapmode.MindMapMapModel
import freemind.modes.mindmapmode.actions.IconAction
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin
import freemind.modes.mindmapmode.actions.PasteAsPlainTextAction
import freemind.modes.mindmapmode.actions.ImportFolderStructureAction
import java.beans.PropertyChangeListener
import freemind.common.PropertyControl
import freemind.common.ThreeCheckBoxProperty
import freemind.common.ComboProperty
import freemind.common.FontProperty
import freemind.common.BooleanProperty
import freemind.common.IconProperty
import freemind.common.ScriptEditorProperty
import java.awt.CardLayout
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.common.PropertyBean
import java.beans.PropertyChangeEvent
import freemind.common.SeparatorProperty
import freemind.modes.mindmapmode.dialogs.StylePatternFrame
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.StylePatternFrameType
import freemind.common.NextLineProperty
import freemind.controller.*
import freemind.modes.mindmapmode.dialogs.IntegerComboProperty
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.EdgeWidthTransformer
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.ValueTransformator
import freemind.controller.actions.generated.instance.PatternPropertyBase
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.IdentityTransformer
import freemind.modes.mindmapmode.actions.ApplyPatternAction
import freemind.controller.actions.generated.instance.PatternNodeColor
import freemind.controller.actions.generated.instance.PatternNodeBackgroundColor
import freemind.controller.actions.generated.instance.PatternNodeStyle
import freemind.controller.actions.generated.instance.PatternNodeText
import freemind.controller.actions.generated.instance.PatternEdgeColor
import freemind.controller.actions.generated.instance.PatternEdgeStyle
import freemind.controller.actions.generated.instance.PatternEdgeWidth
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.EdgeWidthBackTransformer
import freemind.controller.actions.generated.instance.PatternNodeFontName
import freemind.controller.actions.generated.instance.PatternNodeFontSize
import freemind.controller.actions.generated.instance.PatternNodeFontBold
import freemind.controller.actions.generated.instance.PatternNodeFontStrikethrough
import freemind.controller.actions.generated.instance.PatternNodeFontItalic
import freemind.controller.actions.generated.instance.PatternIcon
import freemind.controller.actions.generated.instance.PatternScript
import freemind.controller.actions.generated.instance.PatternChild
import java.awt.dnd.DropTargetListener
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTargetEvent
import freemind.controller.NodeMotionListener.NodeMotionAdapter
import freemind.modes.mindmapmode.listeners.MindMapNodeMotionListener
import freemind.view.mindmapview.NodeMotionListenerView
import java.awt.Robot
import java.awt.AWTException
import freemind.modes.mindmapmode.MindMapMode
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
import freemind.modes.mindmapmode.MindMapPopupMenu
import freemind.modes.MindMap.MapSourceChangedObserver
import java.awt.datatransfer.Clipboard
import freemind.modes.mindmapmode.MindMapController.ExportToHTMLAction
import freemind.modes.mindmapmode.MindMapController.ExportBranchToHTMLAction
import freemind.modes.mindmapmode.MindMapController.EditLongAction
import freemind.modes.mindmapmode.actions.NewSiblingAction
import freemind.modes.mindmapmode.actions.NewPreviousSiblingAction
import freemind.modes.mindmapmode.MindMapController.SetLinkByFileChooserAction
import freemind.modes.mindmapmode.MindMapController.SetImageByFileChooserAction
import freemind.modes.mindmapmode.MindMapController.OpenLinkDirectoryAction
import freemind.modes.mindmapmode.actions.ExportBranchAction
import freemind.modes.mindmapmode.MindMapController.ImportBranchAction
import freemind.modes.mindmapmode.MindMapController.ImportLinkedBranchAction
import freemind.modes.mindmapmode.MindMapController.ImportLinkedBranchWithoutRootAction
import freemind.modes.mindmapmode.actions.StrikethroughAction
import freemind.modes.mindmapmode.actions.UnderlinedAction
import freemind.modes.mindmapmode.actions.NodeColorAction
import freemind.modes.mindmapmode.actions.DeleteChildAction
import freemind.modes.mindmapmode.actions.ToggleFoldedAction
import freemind.modes.mindmapmode.actions.ToggleChildrenFoldedAction
import freemind.modes.mindmapmode.actions.UseRichFormattingAction
import freemind.modes.mindmapmode.actions.UsePlainTextAction
import freemind.modes.mindmapmode.actions.EdgeColorAction
import freemind.modes.mindmapmode.actions.EdgeStyleAction
import freemind.modes.mindmapmode.actions.NodeColorBlendAction
import freemind.modes.mindmapmode.actions.NodeStyleAction
import freemind.modes.mindmapmode.actions.CloudAction
import freemind.modes.mindmapmode.actions.CloudColorAction
import freemind.modes.mindmapmode.actions.AddArrowLinkAction
import freemind.modes.mindmapmode.actions.RemoveArrowLinkAction
import freemind.modes.mindmapmode.actions.ColorArrowLinkAction
import freemind.modes.mindmapmode.actions.ChangeArrowsInArrowLinkAction
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction
import freemind.modes.mindmapmode.actions.NodeBackgroundColorAction.RemoveNodeBackgroundColorAction
import freemind.modes.mindmapmode.actions.RemoveAllIconsAction
import freemind.modes.mindmapmode.actions.SetLinkByTextFieldAction
import freemind.modes.mindmapmode.actions.AddLocalLinkAction
import freemind.modes.mindmapmode.actions.MoveNodeAction
import freemind.modes.mindmapmode.actions.ImportExplorerFavoritesAction
import freemind.modes.mindmapmode.actions.RevertAction
import freemind.modes.mindmapmode.actions.SelectBranchAction
import freemind.modes.mindmapmode.MindMapController.MindMapFilter
import freemind.controller.actions.generated.instance.MenuStructure
import freemind.modes.mindmapmode.actions.xml.DefaultActionHandler
import freemind.modes.mindmapmode.actions.CopySingleAction
import freemind.main.Tools.FileReaderCreator
import freemind.main.Tools.ReaderCreator
import freemind.modes.MindMap.AskUserBeforeUpdateCallback
import freemind.modes.mindmapmode.listeners.MindMapMouseMotionManager
import freemind.modes.mindmapmode.listeners.MindMapNodeDropListener
import freemind.modes.mindmapmode.actions.MindMapControllerHookAction
import freemind.view.MapModule
import freemind.modes.mindmapmode.MindMapController.NewNodeCreator
import freemind.modes.mindmapmode.MindMapController.NodeInformationTimerAction
import freemind.modes.mindmapmode.MindMapController.DefaultMindMapNodeCreator
import org.jibx.runtime.JiBXException
import freemind.controller.actions.generated.instance.MenuCategoryBase
import freemind.controller.actions.generated.instance.MenuSubmenu
import freemind.controller.actions.generated.instance.MenuActionBase
import freemind.controller.actions.generated.instance.MenuCheckedAction
import freemind.controller.actions.generated.instance.MenuRadioAction
import freemind.controller.actions.generated.instance.MenuSeparator
import freemind.modes.mindmapmode.MindMapController.LinkActionBase
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.BadLocationException
import freemind.extensions.UndoEventReceiver
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.modes.mindmapmode.MindMapController.MapSourceChangeDialog
import freemind.modes.mindmapmode.MindMapHTMLWriter
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreeModel
import freemind.controller.filter.util.SortedListModel
import freemind.view.ScalableImageIcon
import java.util.StringTokenizer
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
import java.util.SortedMap
import java.util.TreeMap
import java.util.Enumeration
import java.lang.NullPointerException
import freemind.modes.CloudAdapter.CloudAdapterListener
import java.awt.Stroke
import java.awt.BasicStroke
import freemind.modes.ModeController.NodeLifetimeListener
import freemind.modes.FreeMindFileDialog.DirectoryResultListener
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import freemind.view.mindmapview.IndependantMapViewCreator
import freemind.modes.ControllerAdapter.FileOpener
import java.awt.dnd.DropTarget
import freemind.main.*
import freemind.modes.*
import java.awt.HeadlessException
import freemind.modes.MapFeedbackAdapter.NodesDepthComparator
import freemind.modes.MindMapLinkRegistry.SynchronousVector
import freemind.modes.mindmapmode.actions.ApplyPatternAction.ExternalPatternAction
import java.awt.FileDialog
import freemind.modes.FreeMindAwtFileDialog.FreeMindFilenameFilter
import freemind.modes.FreeMindAwtFileDialog.DirFilter
import freemind.modes.FreeMindAwtFileDialog.FileOnlyFilter
import freemind.modes.FreeMindAwtFileDialog.FileAndDirFilter
import kotlin.jvm.JvmStatic
import freemind.modes.ExtendedMapFeedbackAdapter.DummyTransferable
import java.awt.event.*
import java.io.*
import java.lang.Exception
import java.net.URL
import java.util.HashMap
import java.util.HashSet
import java.util.logging.Logger
import javax.swing.*

class BrowseController(mode: Mode?) : ViewControllerAdapter(mode) {
    private val popupmenu: JPopupMenu
    private val toolbar: JToolBar
    var followLink: Action
    var nodeUp: Action? = null
    var nodeDown: Action? = null
    override val hookFactory: HookFactory
    private var noteIcon: ImageIcon? = null
    var followMapLink: FollowMapLink

    class FollowMapLink(private val modeController: ViewControllerAdapter) : AbstractAction(modeController.getText("follow_map_link"), MapNodePositionHolderBase.Companion.getMapLocationIcon()), MenuItemEnabledListener {
        private val logger: Logger

        init {
            logger = modeController.frame.getLogger(
                    this.javaClass.name)
        }

        override fun actionPerformed(e: ActionEvent) {
            val hook = hook
            if (hook != null) {
                val barePositions = hook.barePosition
                try {
                    // GRR, this is doubled code :-(
                    val tileSources = HashMap<String?, String>()
                    tileSources[MapNodePositionHolderBase.Companion.TILE_SOURCE_MAPNIK] = MapNodePositionHolderBase.Companion.SHORT_MAPNIK
                    tileSources[MapNodePositionHolderBase.Companion.TILE_SOURCE_CYCLE_MAP] = MapNodePositionHolderBase.Companion.SHORT_CYCLE_MAP
                    tileSources[MapNodePositionHolderBase.Companion.TILE_SOURCE_TRANSPORT_MAP] = MapNodePositionHolderBase.Companion.SHORT_TRANSPORT_MAP
                    tileSources[MapNodePositionHolderBase.Companion.TILE_SOURCE_MAP_QUEST_OPEN_MAP] = MapNodePositionHolderBase.Companion.SHORT_MAP_QUEST_OPEN_MAP
                    val link = ("http://www.openstreetmap.org/?" + "mlat="
                            + barePositions!![0] + "&mlon=" + barePositions[1]
                            + "&lat=" + barePositions[2] + "&lon="
                            + barePositions[3] + "&zoom=" + barePositions[4]
                            + "&layers=" + tileSources[barePositions[5]])
                    logger.fine("Try to open link $link")
                    modeController.frame.openDocument(URL(link))
                } catch (e1: MalformedURLException) {
                    Resources.getInstance().logException(e1)
                } catch (e1: Exception) {
                    Resources.getInstance().logException(e1)
                }
            }
        }

        protected val hook: MapNodePositionHolderBase?
            protected get() {
                val selected = modeController.selected
                return MapNodePositionHolderBase.Companion.getBaseHook(selected)
            }

        /* (non-Javadoc)
		 * @see freemind.controller.MenuItemEnabledListener#isEnabled(javax.swing.JMenuItem, javax.swing.Action)
		 */
        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            return hook != null
        }
    }

    init {
        hookFactory = BrowseHookFactory()
        // Daniel: Actions are initialized here and not above because of
        // some error it would produce. Not studied in more detail.
        followLink = FollowLinkAction()
        followMapLink = FollowMapLink(this)
        popupmenu = BrowsePopupMenu(this)
        toolbar = BrowseToolBar(this)
        setAllActions(false)
        // for displaying notes.
        registerNodeSelectionListener(NodeNoteViewer(this), false)
    }

    override fun startupController() {
        super.startupController()
        invokeHooksRecursively(rootNode as NodeAdapter, map)
    }

    override fun restoreMapsLastState(pNewModeController: ModeController?,
                                      pModel: MapAdapter) {
        // intentionally do nothing.
    }

    override fun newModel(newModeController: ModeController?): MapAdapter {
        val model = BrowseMapModel(null, newModeController)
        newModeController!!.setModel(model)
        return model
    }

    override fun plainClick(e: MouseEvent) {
        /* perform action only if one selected node. */
        if (selecteds.size != 1) return
        val component = e.component as MainView
        if (component.isInFollowLinkRegion(e.x.toDouble())) {
            loadURL()
        } else {
            val node = component.nodeView.model
            if (!node.hasChildren()) {
                // the emulate the plain click.
                doubleClick(e)
                return
            }
            toggleFolded!!.toggleFolded(selecteds.listIterator())
        }
    }

    fun doubleClick() {
        /* If the link exists, follow the link; toggle folded otherwise */
        if (selected.link == null) {
            toggleFolded!!.toggleFolded()
        } else {
            loadURL()
        }
    }

    // public void anotherNodeSelected(MindMapNode n) {
    // super.anotherNodeSelected(n);
    // if(n.isRoot()){
    // return;
    // }
    // //Presentation:
    // setFolded(n, false);
    // foldOthers(n);
    // }
    //
    //
    // private void foldOthers(MindMapNode n) {
    // if(n.isRoot()){
    // return;
    // }
    // MindMapNode parent = n.getParentNode();
    // for (Iterator iter = parent.childrenUnfolded(); iter.hasNext();) {
    // MindMapNode element = (MindMapNode) iter.next();
    // if(element != n){
    // setFolded(element, true);
    // }
    // }
    // foldOthers(parent);
    // }
    override fun newNode(userObject: Any?, map: MindMap?): MindMapNode? {
        return BrowseNodeModel(userObject, map)
    }

    override val popupMenu: JPopupMenu?
        get() = popupmenu

    /**
     * Link implementation: If this is a link, we want to make a popup with at
     * least removelink available.
     */
    override fun getPopupForModel(obj: Any?): JPopupMenu? {
        if (obj is BrowseArrowLinkModel) {
            // yes, this is a link.
            val link = obj
            val arrowLinkPopup = JPopupMenu()
            arrowLinkPopup.add(getGotoLinkNodeAction(link.source))
            arrowLinkPopup.add(getGotoLinkNodeAction(link.target!!))
            arrowLinkPopup.addSeparator()
            // add all links from target and from source:
            val nodeAlreadyVisited = HashSet<MindMapNode>()
            nodeAlreadyVisited.add(link.source)
            nodeAlreadyVisited.add(link.target!!)
            val links = model.linkRegistry.getAllLinks(link.source)
            links!!.addAll(model.linkRegistry.getAllLinks(link.target))
            for (i in links!!.indices) {
                val foreign_link = links[i] as BrowseArrowLinkModel
                if (nodeAlreadyVisited.add(foreign_link.target!!)) {
                    arrowLinkPopup.add(getGotoLinkNodeAction(foreign_link
                            .target!!))
                }
                if (nodeAlreadyVisited.add(foreign_link.source)) {
                    arrowLinkPopup.add(getGotoLinkNodeAction(foreign_link
                            .source))
                }
            }
            return arrowLinkPopup
        }
        return null
    }

    /**
     */
    private fun getGotoLinkNodeAction(destination: MindMapNode): GotoLinkNodeAction {
        return GotoLinkNodeAction(this, destination)
    }

    override val modeToolBar: JToolBar?
        get() = toolBar
    val toolBar: BrowseToolBar
        get() = toolbar as BrowseToolBar

    // public void loadURL(String relative) {
    // // copy from mind map controller:
    // if (relative.startsWith("#")) {
    // // inner map link, fc, 12.10.2004
    // String target = relative.substring(1);
    // try {
    // MindMapNode node = getNodeFromID(target);
    // centerNode(node);
    // return;
    // } catch (Exception e) {
    // // bad luck.
    // getFrame().out(Tools.expandPlaceholders(getText("link_not_found"),
    // target));
    // return;
    // }
    // }
    //
    // URL absolute = null;
    // try {
    // absolute = new URL(getMap().getURL(), relative);
    // getFrame().out(absolute.toString());
    // } catch (MalformedURLException ex) {
    // freemind.main.Resources.getInstance().logExecption(ex);
    // getController().errorMessage(
    // getText("url_error") + " " + ex.getMessage());
    // // getFrame().err(getText("url_error"));
    // return;
    // }
    //
    // String type = Tools.getExtension(absolute.getFile());
    // try {
    // if
    // (type.equals(freemind.main.FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT))
    // {
    // getFrame().setWaitingCursor(true);
    // load(absolute);
    // } else {
    // getFrame().openDocument(absolute);
    // }
    // } catch (Exception ex) {
    // getController().errorMessage(getText("url_load_error") + absolute);
    // freemind.main.Resources.getInstance().logExecption( ex);
    // // for some reason, this exception is thrown anytime...
    // } finally {
    // getFrame().setWaitingCursor(false);
    // }
    //
    // }
    @Throws(IOException::class, XMLParseException::class, URISyntaxException::class)
    override fun load(url: URL?): ModeController? {
        val newModeController = super.load(url) as ModeController
        // decorator pattern.
        (newModeController.modeToolBar as BrowseToolBar).setURLField(url
                .toString())
        return newModeController
    }

    @Throws(IOException::class)
    override fun load(pFile: File?): ModeController? {
        val newModeController = super.load(pFile) as ModeController
        // decorator pattern.
        (newModeController.modeToolBar as BrowseToolBar).setURLField(fileToUrl(pFile).toString())
        return newModeController
    }

    override fun newMap(mapModel: MindMap, modeController: ModeController?) {
        setNoteIcon(mapModel.rootNode)
        super.newMap(mapModel, modeController)
    }

    private fun setNoteIcon(node: MindMapNode?) {
        val noteText = node.getNoteText()
        if (noteText != null && noteText != "") {
            // icon
            if (noteIcon == null) {
                noteIcon = ImageFactory.getInstance().createUnscaledIcon(controller.getResource(
                        "images/knotes.png"))
            }
            node!!.setStateIcon(NodeNoteBase.Companion.NODE_NOTE_ICON, noteIcon)
        }
        val children = node!!.childrenUnfolded()
        while (children!!.hasNext()) {
            setNoteIcon(children.next() as MindMapNode)
        }
    }

    /**
     * Enabled/Disabled all actions that are dependent on whether there is a map
     * open or not.
     */
    override fun setAllActions(enabled: Boolean) {
        super.setAllActions(enabled)
        toggleFolded!!.isEnabled = enabled
        toggleChildrenFolded!!.isEnabled = enabled
        followLink.isEnabled = enabled
    }

    // ////////
    // Actions
    // ///////
    private inner class FollowLinkAction internal constructor() : AbstractAction(getText("follow_link")) {
        override fun actionPerformed(e: ActionEvent) {
            loadURL()
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.ModeController#updateMenus(freemind.controller.
	 * StructuredMenuHolder)
	 */
    override fun updateMenus(holder: StructuredMenuHolder) {
        add(holder, MenuBar.EDIT_MENU + "/find/find", find, "keystroke_find")
        add(holder, MenuBar.EDIT_MENU + "/find/findNext", findNext,
                "keystroke_find_next")
        add(holder, MenuBar.EDIT_MENU + "/find/followLink", followLink,
                "keystroke_follow_link")
        holder.addSeparator(MenuBar.EDIT_MENU + "/find")
        add(holder, MenuBar.EDIT_MENU + "/find/toggleFolded", toggleFolded,
                "keystroke_toggle_folded")
        add(holder, MenuBar.EDIT_MENU + "/find/toggleChildrenFolded",
                toggleChildrenFolded, "keystroke_toggle_children_folded")
    }

    /* (non-Javadoc)
	 * @see freemind.modes.ControllerAdapter#loadInternally(java.net.URL, freemind.modes.MapAdapter)
	 */
    @Throws(URISyntaxException::class, XMLParseException::class, IOException::class)
    override fun loadInternally(url: URL?, pModel: MapAdapter) {
        (pModel as BrowseMapModel).url = url
        val root = loadTree(url)
        if (root != null) {
            pModel.setRoot(root)
        } else {
            // System.err.println("Err:"+root.toString());
            throw IOException()
        }
    }

    fun loadTree(url: URL?): BrowseNodeModel? {
        var root: BrowseNodeModel? = null
        var urlStreamReader: InputStreamReader? = null
        urlStreamReader = try {
            InputStreamReader(url!!.openStream())
        } catch (ex: AccessControlException) {
            frame.controller
                    .errorMessage(
                            "Could not open URL " + url.toString()
                                    + ". Access Denied.")
            System.err.println(ex)
            return null
        } catch (ex: Exception) {
            frame.controller.errorMessage(
                    "Could not open URL " + url.toString() + ".")
            System.err.println(ex)
            // freemind.main.Resources.getInstance().logExecption(ex);
            return null
        }
        return try {
            val IDToTarget = HashMap<String?, NodeAdapter?>()
            root = map.createNodeTreeFromXml(urlStreamReader, IDToTarget) as BrowseNodeModel
            urlStreamReader.close()
            root
        } catch (ex: Exception) {
            System.err.println(ex)
            null
        }
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMap.MapFeedback#out(java.lang.String)
	 */
    override fun out(pFormat: String?) {
        // TODO Auto-generated method stub
    }
}