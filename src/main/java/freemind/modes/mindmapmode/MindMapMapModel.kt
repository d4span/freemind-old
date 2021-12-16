/*FreeMindget - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
import freemind.modes.ControllerAdapter
import freemind.modes.FreemindAction
import freemind.modes.MindMapNode
import javax.swing.JDialog
import javax.swing.JCheckBox
import javax.swing.JTextField
import java.awt.event.ActionEvent
import javax.swing.JOptionPane
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JLabel
import javax.swing.ImageIcon
import javax.swing.JButton
import java.lang.StringBuffer
import freemind.modes.ModeController
import freemind.modes.common.dialogs.EnterPasswordDialog
import javax.swing.JPanel
import javax.swing.JPasswordField
import freemind.common.TextTranslator
import javax.swing.JFrame
import freemind.modes.IconInformation
import java.awt.event.KeyListener
import java.awt.event.MouseListener
import javax.swing.BorderFactory
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.KeyStroke
import javax.swing.JComboBox
import java.awt.event.ActionListener
import freemind.extensions.PermanentNodeHookAdapter
import freemind.modes.common.plugins.ReminderHookBase
import java.text.MessageFormat
import freemind.modes.common.plugins.ReminderHookBase.TimerBlinkTask
import freemind.modes.MindIcon
import freemind.modes.common.plugins.MapNodePositionHolderBase
import freemind.extensions.PermanentNodeHook
import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver
import freemind.view.mindmapview.MapView
import freemind.controller.NodeMouseMotionListener.NodeMouseMotionObserver
import freemind.modes.common.listeners.CommonNodeMouseMotionListener
import freemind.main.Tools.BooleanHolder
import freemind.view.mindmapview.MainView
import java.awt.geom.Point2D
import freemind.modes.common.listeners.CommonNodeMouseMotionListener.timeDelayedSelection
import javax.swing.SwingUtilities
import java.lang.Runnable
import java.awt.event.MouseWheelListener
import freemind.modes.common.listeners.MindMapMouseWheelEventHandler
import freemind.preferences.FreemindPropertyListener
import java.awt.event.MouseWheelEvent
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import freemind.modes.common.CommonNodeKeyListener.EditHandler
import freemind.modes.common.CommonNodeKeyListener
import kotlin.jvm.JvmOverloads
import freemind.modes.common.CommonToggleFoldedAction
import freemind.modes.filemode.FileController
import javax.swing.JToolBar
import freemind.modes.filemode.FileToolBar
import freemind.modes.filemode.FileMode
import freemind.modes.MapAdapter
import freemind.modes.MindMapLinkRegistry
import freemind.modes.filemode.FileNodeModel
import kotlin.Throws
import java.lang.RuntimeException
import freemind.modes.MindMap
import freemind.modes.NodeAdapter
import freemind.modes.EdgeAdapter
import freemind.modes.CloudAdapter
import freemind.modes.ArrowLinkAdapter
import freemind.modes.ArrowLinkTarget
import freemind.modes.MapFeedback
import freemind.modes.filemode.FileEdgeModel
import java.lang.SecurityException
import java.net.MalformedURLException
import javax.swing.JPopupMenu
import javax.swing.JMenuItem
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
import javax.swing.JComponent
import javax.swing.JScrollPane
import freemind.controller.Controller.SplitComponentType
import freemind.modes.browsemode.NodeNoteViewer
import javax.swing.event.PopupMenuListener
import javax.swing.event.PopupMenuEvent
import freemind.modes.browsemode.BrowseController.FollowMapLink
import freemind.controller.MenuItemEnabledListener
import freemind.modes.browsemode.BrowseHookFactory
import freemind.modes.browsemode.BrowsePopupMenu
import freemind.modes.MindMapLink
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
import freemind.controller.actions.generated.instance.PluginAction
import freemind.controller.actions.generated.instance.PluginRegistration
import freemind.extensions.MindMapHook
import freemind.controller.actions.generated.instance.PluginClasspath
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import freemind.controller.actions.generated.instance.PluginMode
import javax.swing.JCheckBoxMenuItem
import freemind.extensions.NodeHookAdapter
import freemind.modes.ExtendedMapFeedback
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
import freemind.controller.MindMapNodesSelection
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
import freemind.controller.actions.generated.instance.NodeListMember
import freemind.controller.actions.generated.instance.RevertXmlAction
import freemind.modes.mindmapmode.actions.xml.actors.AddHookActor
import freemind.controller.actions.generated.instance.HookNodeAction
import freemind.extensions.DontSaveMarker
import freemind.controller.actions.generated.instance.NodeChildParameter
import freemind.modes.ViewAbstraction
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
import freemind.modes.MindMapEdge
import freemind.controller.actions.generated.instance.EdgeWidthFormatAction
import freemind.controller.actions.generated.instance.NodeColorFormatAction
import freemind.controller.actions.generated.instance.NodeStyleFormatAction
import freemind.controller.actions.generated.instance.UnderlinedNodeAction
import freemind.controller.actions.generated.instance.CloudColorXmlAction
import freemind.modes.LineAdapter
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
import freemind.modes.MindMapArrowLink
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
import javax.swing.Icon
import freemind.modes.mindmapmode.actions.MindmapAction
import freemind.modes.mindmapmode.actions.xml.PrintActionHandler
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.OptionalDontShowMeAgainDialog.StandardPropertyHandler
import freemind.modes.mindmapmode.actions.NodeGeneralAction
import freemind.controller.MenuItemSelectedListener
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
import freemind.modes.StylePatternFactory
import freemind.modes.FreeMindFileDialog
import javax.swing.JFileChooser
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
import freemind.modes.mindmapmode.MindMapMode
import freemind.controller.FreeMindToolBar
import freemind.controller.ZoomListener
import freemind.modes.mindmapmode.JAutoScrollBarPane
import java.awt.event.ItemListener
import freemind.controller.color.JColorCombo
import freemind.modes.mindmapmode.MindMapToolBar
import freemind.modes.mindmapmode.MindMapToolBar.FreeMindComboBox
import java.awt.event.ItemEvent
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
import freemind.modes.NodeDownAction
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
import freemind.modes.MapFeedbackAdapter
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
import javax.swing.JMenu
import org.jibx.runtime.JiBXException
import javax.swing.ButtonGroup
import freemind.controller.actions.generated.instance.MenuCategoryBase
import freemind.controller.actions.generated.instance.MenuSubmenu
import freemind.controller.actions.generated.instance.MenuActionBase
import freemind.controller.actions.generated.instance.MenuCheckedAction
import freemind.controller.actions.generated.instance.MenuRadioAction
import javax.swing.JRadioButtonMenuItem
import freemind.controller.actions.generated.instance.MenuSeparator
import freemind.modes.mindmapmode.MindMapController.LinkActionBase
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.BadLocationException
import freemind.extensions.UndoEventReceiver
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.modes.mindmapmode.MindMapController.MapSourceChangeDialog
import freemind.modes.mindmapmode.MindMapHTMLWriter
import javax.swing.ScrollPaneConstants
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreeModel
import freemind.controller.filter.util.SortedListModel
import freemind.view.ScalableImageIcon
import javax.swing.tree.DefaultTreeModel
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import freemind.controller.filter.util.SortedMapListModel
import freemind.modes.XMLElementAdapter
import freemind.controller.filter.condition.NoFilteringCondition
import freemind.modes.MapAdapter.FileChangeInspectorTimerTask
import freemind.modes.MapAdapter.DontAskUserBeforeUpdateAdapter
import freemind.modes.EdgeAdapter.EdgeAdapterListener
import freemind.modes.MindMapLine
import java.lang.CloneNotSupportedException
import freemind.modes.LinkAdapter
import freemind.modes.LinkAdapter.LinkAdapterListener
import freemind.modes.MindMapCloud
import freemind.modes.HistoryInformation
import java.lang.NullPointerException
import freemind.modes.CloudAdapter.CloudAdapterListener
import freemind.modes.ModesCreator
import freemind.modes.StylePattern
import freemind.controller.MapModuleManager
import freemind.modes.ModeController.NodeLifetimeListener
import freemind.modes.FreeMindFileDialog.DirectoryResultListener
import freemind.controller.LastStateStorageManagement
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import freemind.view.mindmapview.IndependantMapViewCreator
import freemind.modes.ControllerAdapter.FileOpener
import java.awt.dnd.DropTarget
import freemind.controller.NodeMouseMotionListener
import freemind.controller.NodeMotionListener
import freemind.controller.NodeKeyListener
import freemind.controller.NodeDragListener
import freemind.controller.NodeDropListener
import freemind.controller.MapMouseMotionListener
import freemind.controller.MapMouseWheelListener
import freemind.main.*
import freemind.modes.MapFeedbackAdapter.NodesDepthComparator
import freemind.modes.MindMapLinkRegistry.SynchronousVector
import freemind.modes.mindmapmode.actions.ApplyPatternAction.ExternalPatternAction
import freemind.modes.FreeMindAwtFileDialog.FreeMindFilenameFilter
import freemind.modes.FreeMindAwtFileDialog
import freemind.modes.FreeMindAwtFileDialog.DirFilter
import freemind.modes.FreeMindAwtFileDialog.FileOnlyFilter
import freemind.modes.FreeMindAwtFileDialog.FileAndDirFilter
import kotlin.jvm.JvmStatic
import freemind.modes.NodeViewEvent
import freemind.modes.ExtendedMapFeedbackAdapter
import freemind.modes.ExtendedMapFeedbackAdapter.DummyTransferable
import java.awt.*
import java.io.*
import java.lang.Exception
import java.util.*

class MindMapMapModel(root: MindMapNodeModel?, pMapFeedback: MapFeedback?) : MapAdapter(pMapFeedback) {
    var lockManager: LockManager
    private override val linkRegistry: MindMapLinkRegistry
    private var timerForAutomaticSaving: Timer? = null

    //
    // Constructors
    //
    constructor(pMapFeedback: MapFeedback?) : this(null, pMapFeedback) {}

    init {
        var root = root
        lockManager = if (Resources.getInstance().getBoolProperty(
                        "experimental_file_locking_on")) LockManager() else DummyLockManager()

        // register new LinkRegistryAdapter
        linkRegistry = MindMapLinkRegistry()
        if (root == null) root = MindMapNodeModel(pMapFeedback!!.getResourceString("new_mindmap"),
                this)
        setRoot(root)
        readOnly = false
        // automatic save: start timer after the map is completely loaded
        EventQueue.invokeLater { scheduleTimerForAutomaticSaving() }
    }

    //
    override fun getLinkRegistry(): MindMapLinkRegistry? {
        return linkRegistry
    }

    override val restorable: String?
        get() = if (file == null) null else RESTORE_MODE_MIND_MAP
    + file.absolutePath
    fun changeNode(node: MindMapNode, newText: String?) {
        if (node.toString().startsWith("<html>")) {
            node.setUserObject(HtmlTools.unescapeHTMLUnicodeEntity(newText!!))
        } else {
            node.setUserObject(newText)
        }
        nodeChanged(node)
    }

    //
    // Other methods
    //
    override fun toString(): String {
        return if (file == null) null else file.name
    }

    //
    // Export and saving
    //
    override fun getAsHTML(mindMapNodes: List<*>?): String? {
        // Returns success of the operation.
        return try {
            val stringWriter = StringWriter()
            val fileout = BufferedWriter(stringWriter)
            MindMapController.Companion.saveHTML(mindMapNodes, fileout)
            fileout.close()
            stringWriter.toString()
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            null
        }
    }

    override fun getAsPlainText(mindMapNodes: List<*>): String? {
        // Returns success of the operation.
        return try {
            val stringWriter = StringWriter()
            val fileout = BufferedWriter(stringWriter)
            val it: ListIterator<MindMapNodeModel> = mindMapNodes.listIterator()
            while (it.hasNext()) {
                it.next().saveTXT(fileout,  /* depth= */0)
            }
            fileout.close()
            stringWriter.toString()
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            null
        }
    }

    fun saveTXT(rootNodeOfBranch: MindMapNodeModel, file: File?): Boolean {
        // Returns success of the operation.
        return try {
            val fileout = BufferedWriter(OutputStreamWriter(
                    FileOutputStream(file)))
            rootNodeOfBranch.saveTXT(fileout,  /* depth= */0)
            fileout.close()
            true
        } catch (e: Exception) {
            System.err.println("Error in MindMapMapModel.saveTXT(): ")
            Resources.getInstance().logException(e)
            false
        }
    }

    override fun getAsRTF(mindMapNodes: List<*>): String? {
        // Returns success of the operation.
        return try {
            val stringWriter = StringWriter()
            val fileout = BufferedWriter(stringWriter)
            saveRTF(mindMapNodes, fileout)
            fileout.close()
            stringWriter.toString()
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            null
        }
    }

    fun saveRTF(mindMapNodes: List<MindMapNodeModel>, fileout: BufferedWriter): Boolean {
        // Returns success of the operation.
        return try {

            // First collect all used colors
            val colors = HashSet<Color?>()
            for (nodeModel in mindMapNodes) {
                nodeModel.collectColors(colors)
            }

            // Prepare table of colors containing indices to color table
            var colorTableString = "{\\colortbl;\\red0\\green0\\blue255;"
            // 0 - Automatic, 1 - blue for links
            val colorTable = HashMap<Color?, Int>()
            var colorPosition = 2
            val it: Iterator<Color?> = colors.iterator()
            while (it.hasNext()) {
                val color = it.next()
                colorTableString += ("\\red" + color!!.red + "\\green"
                        + color.green + "\\blue" + color.blue + ";")
                colorTable[color] = colorPosition
                ++colorPosition
            }
            colorTableString += "}"
            fileout.write("{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1033{\\u000conttbl{\\u000c0\\u000cswiss\\u000ccharset0 Arial;}"
                    + colorTableString
                    + "}"
                    + "\\viewkind4\\uc1\\pard\\u000c0\\u000cs20{}")
            // ^ If \\ud is appended here, Unicode does not work in MS Word.
            for (nodeModel in mindMapNodes) {
                nodeModel.saveRTF(fileout,  /* depth= */0, colorTable)
            }
            fileout.write("}")
            true
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            false
        }
    }

    /**
     * Return the success of saving
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun save(file: File?): Boolean {
        var result: Boolean
        synchronized(this) {
            result = saveInternal(file, false)
            // TODO: Set only, when ok?
            if (result) {
                setFileTime()
            }
        }
        return result
    }

    /**
     * This method is intended to provide both normal save routines and saving
     * of temporary (internal) files.
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun saveInternal(file: File?, isInternal: Boolean): Boolean {
        if (!isInternal && readOnly) { // unexpected situation, yet it's better
            // to back it up
            System.err.println("Attempt to save read-only map.")
            return false
        }
        try {
            if (timerForAutomaticSaving != null) {
                timerForAutomaticSaving!!.cancel()
            }
            // Generating output Stream
            val fileout = BufferedWriter(OutputStreamWriter(
                    FileOutputStream(file)))
            getXml(fileout)
            if (!isInternal) {
                setFile(file)
            }
        } finally {
            scheduleTimerForAutomaticSaving()
        }
        return true
    }

    /**
     * writes the content of the map to a writer.
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getXml(fileout: Writer, saveInvisible: Boolean) {
        getXml(fileout, saveInvisible, rootNode)
    }

    /**
     * writes the content of the map to a writer.
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    fun getXml(fileout: Writer, saveInvisible: Boolean,
               pRootNode: MindMapNode?) {
        fileout.write("<map ")
        fileout.write("version=\"" + FreeMind.XML_VERSION + "\"")
        fileout.write(">\n")
        fileout.write("<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->\n")
        pRootNode!!.save(fileout, getLinkRegistry(), saveInvisible, true)
        fileout.write("</map>\n")
        fileout.close()
    }

    @Throws(IOException::class)
    override fun getXml(fileout: Writer) {
        getXml(fileout, true)
    }

    @Throws(IOException::class)
    override fun getFilteredXml(fileout: Writer) {
        getXml(fileout, false)
    }

    /**
     * Attempts to lock the map using a semaphore file
     *
     * @return If the map is locked, return the name of the locking user,
     * otherwise return null.
     * @throws Exception
     * , when the locking failed for other reasons than that the
     * file is being edited.
     */
    @Throws(Exception::class)
    override fun tryToLock(file: File?): String? {
        val lockingUser = lockManager.tryToLock(file)
        val lockingUserOfOldLock = lockManager.popLockingUserOfOldLock()
        if (lockingUserOfOldLock != null) {
            mapFeedback.out(
                    expandPlaceholders(
                            getText("locking_old_lock_removed")!!,
                            file!!.name, lockingUserOfOldLock))
        }
        if (lockingUser == null) {
            readOnly = false
        }
        // The map sure is not read only when the locking succeeded.
        return lockingUser
    }

    /** When a map is closed, this method is called.  */
    override fun destroy() {
        super.destroy()
        lockManager.releaseLock()
        lockManager.releaseTimer()
        if (timerForAutomaticSaving != null) {
            /* cancel the timer, if map is closed. */
            timerForAutomaticSaving!!.cancel()
        }
    }

    private fun scheduleTimerForAutomaticSaving() {
        val numberOfTempFiles = mapFeedback.getProperty(
                "number_of_different_files_for_automatic_save").toInt()
        val filesShouldBeDeletedAfterShutdown: Boolean = Resources.getInstance()
                .getBoolProperty("delete_automatic_saves_at_exit")
        var path = mapFeedback.getProperty("path_to_automatic_saves")
        /* two standard values: */if (safeEquals(path, "default")) {
            path = null
        }
        if (safeEquals(path, "freemind_home")) {
            path = Resources.getInstance().getFreemindDirectory()
        }
        var delay = mapFeedback.getProperty(
                "time_for_automatic_save").toInt()
        var dirToStore: File? = null
        if (path != null) {
            dirToStore = File(path)
            /* existence? */if (!dirToStore.isDirectory) {
                dirToStore = null
                System.err.println("Temporary directory " + path
                        + " not found. Disabling automatic store.")
                delay = Int.MAX_VALUE
                return
            }
        }
        timerForAutomaticSaving = Timer()
        timerForAutomaticSaving!!.schedule(DoAutomaticSave(
                this@MindMapMapModel, numberOfTempFiles,
                filesShouldBeDeletedAfterShutdown, dirToStore), delay.toLong(), delay.toLong())
    }

    open inner class LockManager : TimerTask() {
        var lockedSemaphoreFile: File? = null
        var lockTimer: Timer? = null
        val lockUpdatePeriod = (4 * 60 * 1000 // four minutes
                ).toLong()
        val lockSafetyPeriod = (5 * 60 * 1000 // five minutes
                ).toLong()
        var lockingUserOfOldLock: String? = null
        private fun getSemaphoreFile(mapFile: File?): File {
            return File(mapFile!!.parent
                    + System.getProperty("file.separator") + "$~"
                    + mapFile.name + "~")
        }

        @Synchronized
        open fun popLockingUserOfOldLock(): String? {
            val toReturn = lockingUserOfOldLock
            lockingUserOfOldLock = null
            return toReturn
        }

        @Throws(Exception::class)
        private fun writeSemaphoreFile(inSemaphoreFile: File) {
            var semaphoreOutputStream: FileOutputStream? = FileOutputStream(
                    inSemaphoreFile)
            var lock: FileLock? = null
            try {
                lock = semaphoreOutputStream!!.channel.tryLock()
                if (lock == null) {
                    semaphoreOutputStream.close()
                    System.err.println("Locking failed.")
                    throw Exception()
                }
            } // locking failed
            catch (eUle: UnsatisfiedLinkError) {
            } // This may come with Windows95. We don't insist on detailed
            // locking in that case.
            catch (eDcdf: NoClassDefFoundError) {
            } // ^ just like above.
            // ^ On Windows95, the necessary libraries are missing.
            semaphoreOutputStream!!.write(System.getProperty("user.name")
                    .toByteArray())
            semaphoreOutputStream.write('\n'.code)
            semaphoreOutputStream.write(
                    System.currentTimeMillis().toString().toByteArray())
            semaphoreOutputStream.close()
            semaphoreOutputStream = null
            setHidden(inSemaphoreFile, true,  /* synchro= */false) // Exception
            // free
            lock?.release()
        }

        @Synchronized
        @Throws(Exception::class)
        open fun tryToLock(file: File?): String? {
            // Locking should work for opening as well as for saving as.
            // We are especially carefull when it comes to exclusivity of
            // writing.
            val semaphoreFile = getSemaphoreFile(file)
            if (semaphoreFile === lockedSemaphoreFile) {
                return null
            }
            try {
                val semaphoreReader = BufferedReader(
                        FileReader(semaphoreFile))
                val lockingUser = semaphoreReader.readLine()
                val lockTime = semaphoreReader.readLine().toLong()
                val timeDifference = System.currentTimeMillis() - lockTime
                // catch (NumberFormatException enf) {} // This means that the
                // time was not written at all - lock is corrupt
                if (timeDifference > lockSafetyPeriod) { // the lock is old
                    semaphoreReader.close()
                    lockingUserOfOldLock = lockingUser
                    semaphoreFile.delete()
                } else return lockingUser
            } catch (e: FileNotFoundException) {
            }
            writeSemaphoreFile(semaphoreFile)
            if (lockTimer == null) {
                lockTimer = Timer()
                lockTimer!!.schedule(this, lockUpdatePeriod, lockUpdatePeriod)
            }
            releaseLock()
            lockedSemaphoreFile = semaphoreFile
            return null
        }

        @Synchronized
        open fun releaseLock() {
            if (lockedSemaphoreFile != null) {
                lockedSemaphoreFile!!.delete()
                lockedSemaphoreFile = null
            }
        } // this may fail, TODO: ensure real deletion

        @Synchronized
        open fun releaseTimer() {
            if (lockTimer != null) {
                lockTimer!!.cancel()
                lockTimer = null
            }
        }

        @Synchronized
        override fun run() { // update semaphore file
            if (lockedSemaphoreFile == null) {
                System.err
                        .println("unexpected: lockedSemaphoreFile is null upon lock update")
                return
            }
            try {
                setHidden(lockedSemaphoreFile!!, false,  /* synchro= */true) // Exception
                // free
                // ^ We unhide the file before overwriting because JavaRE1.4.2
                // does
                // not let us open hidden files for writing. This is a
                // workaround for Java bug,
                // I guess.
                writeSemaphoreFile(lockedSemaphoreFile!!)
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
    }

    private inner class DummyLockManager : LockManager() {
        @Synchronized
        override fun popLockingUserOfOldLock(): String? {
            return null
        }

        @Synchronized
        @Throws(Exception::class)
        override fun tryToLock(file: File?): String? {
            return null
        }

        @Synchronized
        override fun releaseLock() {
        }

        @Synchronized
        override fun releaseTimer() {
        }

        @Synchronized
        override fun run() {
        }
    }

    private class DoAutomaticSave internal constructor(private val model: MindMapMapModel, numberOfTempFiles: Int,
                                                       filesShouldBeDeletedAfterShutdown: Boolean, pathToStore: File?) : TimerTask() {
        private val tempFileStack: Vector<File>
        private val numberOfFiles: Int
        private val filesShouldBeDeletedAfterShutdown: Boolean
        private val pathToStore: File?

        /**
         * This value is compared with the result of
         * getNumberOfChangesSinceLastSave(). If the values coincide, no further
         * automatic saving is performed until the value changes again.
         */
        private var changeState: Int

        init {
            tempFileStack = Vector()
            numberOfFiles = if (numberOfTempFiles > 0) numberOfTempFiles else 1
            this.filesShouldBeDeletedAfterShutdown = filesShouldBeDeletedAfterShutdown
            this.pathToStore = pathToStore
            changeState = model.numberOfChangesSinceLastSave
        }

        override fun run() {
            /* Map is dirty enough? */
            if (model.numberOfChangesSinceLastSave == changeState) return
            changeState = model.numberOfChangesSinceLastSave
            if (changeState == 0) {
                /* map was recently saved. */
                return
            }
            try {
                cancel()
                EventQueue.invokeAndWait(Runnable {
                    /* Now, it is dirty, we save it. */
                    val tempFile: File
                    if (tempFileStack.size >= numberOfFiles) tempFile = tempFileStack.removeAt(0) as File // pop
                    else {
                        try {
                            tempFile = File.createTempFile("FM_"
                                    + if (model.toString() == null) "unnamed" else model.toString(),
                                    FreeMindCommon.FREEMIND_FILE_EXTENSION,
                                    pathToStore)
                            if (filesShouldBeDeletedAfterShutdown) tempFile.deleteOnExit()
                        } catch (e: Exception) {
                            System.err
                                    .println("Error in automatic MindMapMapModel.save(): "
                                            + e.message)
                            Resources.getInstance()
                                    .logException(e)
                            return@Runnable
                        }
                    }
                    try {
                        model.saveInternal(tempFile, true /* =internal call */)
                        model.mapFeedback
                                .out(Resources
                                        .getInstance()
                                        .format("automatically_save_message", arrayOf<Any>(tempFile
                                                .toString())))
                    } catch (e: Exception) {
                        System.err
                                .println("Error in automatic MindMapMapModel.save(): "
                                        + e.message)
                        Resources.getInstance().logException(
                                e)
                    }
                    tempFileStack.add(tempFile) // add at the back.
                })
            } catch (e: InterruptedException) {
                Resources.getInstance().logException(e)
            } catch (e: InvocationTargetException) {
                Resources.getInstance().logException(e)
            }
        }
    }

    override fun createNodeAdapter(pMap: MindMap?, nodeClass: String?): NodeAdapter {
        return if (nodeClass == null) {
            MindMapNodeModel(pMap)
        } else try {
            // construct class loader:
            val loader = this.javaClass.classLoader
            // constructed.
            val nodeJavaClass = Class.forName(nodeClass, true, loader)
            val constrArgs = arrayOf(Any::class.java,
                    MindMap::class.java)
            val constrObjs = arrayOf<Any?>(null, pMap)
            val constructor = nodeJavaClass.getConstructor(*constrArgs)
            constructor
                    .newInstance(*constrObjs) as NodeAdapter
        } catch (e: Exception) {
            Resources.getInstance().logException(e,
                    "Error occurred loading node implementor: $nodeClass")
            // the best we can do is to return the normal class:
            MindMapNodeModel(pMap)
        }
        // reflection:
    }

    override fun createEdgeAdapter(node: NodeAdapter?): EdgeAdapter? {
        return MindMapEdgeModel(node, mMapFeedback)
    }

    override fun createCloudAdapter(node: NodeAdapter?): CloudAdapter? {
        return MindMapCloudModel(node, mMapFeedback)
    }

    override fun createArrowLinkAdapter(source: NodeAdapter?,
                                        target: NodeAdapter?): ArrowLinkAdapter? {
        return MindMapArrowLinkModel(source, target, mMapFeedback)
    }

    override fun createArrowLinkTarget(source: NodeAdapter?,
                                       target: NodeAdapter?): ArrowLinkTarget? {
        return ArrowLinkTarget(source, target, mMapFeedback)
    }

    override fun createEncryptedNode(additionalInfo: String?): NodeAdapter? {
        val node = createNodeAdapter(mMapFeedback.map,
                EncryptedMindMapNode::class.java.name)
        node.additionalInfo = additionalInfo
        return node
    }

    companion object {
        const val RESTORE_MODE_MIND_MAP = "MindMap:"
    }
}