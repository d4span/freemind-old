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
package freemind.modes

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
import freemind.modes.browsemode.BrowseController.FollowMapLink
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
import freemind.modes.mindmapmode.MindMapMode
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
import freemind.controller.filter.util.SortedListModel
import freemind.view.ScalableImageIcon
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
import freemind.modes.ModeController.NodeLifetimeListener
import freemind.modes.FreeMindFileDialog.DirectoryResultListener
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import freemind.view.mindmapview.IndependantMapViewCreator
import freemind.modes.ControllerAdapter.FileOpener
import java.awt.dnd.DropTarget
import freemind.controller.filter.FilterInfo
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
import freemind.modes.attributes.Attribute
import java.awt.*
import java.awt.font.TextAttribute
import java.io.*
import java.lang.Exception
import java.lang.UnsupportedOperationException
import java.net.URL
import java.util.*
import java.util.logging.Logger
import javax.swing.event.*
import javax.swing.tree.*

/**
 * This class represents a single Node of a Tree. It contains direct handles to
 * its parent and children and to its view.
 */
abstract class NodeAdapter protected constructor(userObject: Any?, pMap: MindMap?) : MindMapNode {
    private override var activatedHooks: HashSet<PermanentNodeHook?>?
    private override var hooks: MutableList<PermanentNodeHook?>?
    protected var userObject: Any? = "no text"
    private override var xmlText: String? = "no text"
    override var link: String? = null // Change this to vector in future for full
        set(link) {
            if (link != null && link.startsWith("#")) {
                map.getLinkRegistry().registerLocalHyperlinkId(
                        link.substring(1))
            }
            field = link
        }
    private override var toolTip: TreeMap<String?, String>? = null // lazy, fc, 30.6.2005

    // these Attributes have default values, so it can be useful to directly
    // access them in
    // the save() method instead of using getXXX(). This way the stored file is
    // smaller and looks better.
    // (if the default is used, it is not stored) Look at mindmapmode for an
    // example.
    override var bareStyle: String? = null
        protected set

    /** stores the icons associated with this node.  */
    protected override var icons: Vector<MindIcon?>? = null // lazy, fc, 30.6.2005
    protected override var stateIcons: TreeMap<String?, ImageIcon>? = null // lazy, fc,
    // 30.6.2005
    // /**stores the label associated with this node:*/
    // protected String mLabel;
    /** parameters of an eventually associated cloud  */
    protected override var cloud: MindMapCloud? = null
    /** The Foreground/Font Color  */
    override var color: Color? = null

    // fc, 24.2.2004: background color:
    override var backgroundColor: Color? = null
    override var isFolded = false
    private var position = UNKNOWN_POSITION
    private override var vGap = VGAP

    // hGap = Math.max(HGAP, gap);
    override var hGap = HGAP

    /**
     * @param shiftY
     * The shiftY to set.
     */
    override var shiftY = 0
    protected override var children: List<MindMapNode>? = null
    private var preferredChild: MindMapNode? = null
    override var font: Font? = null

    // not implemented
    override var isUnderlined = false
    override val filterInfo = FilterInfo()
    override var parentNode: MindMapNode? = null
        private set

    /**
     * the edge which leads to this node, only root has none In future it has to
     * hold more than one view, maybe with a Vector in which the index specifies
     * the MapView which contains the NodeViews
     */
    override var edge: MindMapEdge? = null
    override var historyInformation: HistoryInformation? = null

    /**
     */
    override var map: MindMap? = null
    private override var noteText: String? = null
    private override var xmlNoteText: String? = null
    private var mAttributeVector: Vector<Attribute?>? = null
    override var text: String?
        get() {
            var string = ""
            if (userObject != null) {
                string = userObject.toString()
            }
            return string
        }
        set(text) {
            if (text == null) {
                userObject = null
                xmlText = null
                return
            }
            userObject = HtmlTools.makeValidXml(text)
            xmlText = HtmlTools.getInstance().toXhtml(userObject as String?)
        }

    override fun getXmlText(): String? {
        return xmlText
    }

    override fun setXmlText(pXmlText: String?) {
        xmlText = HtmlTools.makeValidXml(pXmlText!!)
        userObject = HtmlTools.getInstance().toHtml(xmlText)
    }

    /* ************************************************************
	 * ******** Notes *******
	 * ************************************************************
	 */
    override fun getXmlNoteText(): String? {
        return xmlNoteText
    }

    override fun getNoteText(): String? {
        // logger.info("Note html: " + noteText);
        return noteText
    }

    override fun setXmlNoteText(pXmlNoteText: String?) {
        if (pXmlNoteText == null) {
            xmlNoteText = null
            noteText = null
            return
        }
        xmlNoteText = HtmlTools.makeValidXml(pXmlNoteText)
        noteText = HtmlTools.getInstance().toHtml(xmlNoteText)
    }

    override fun setNoteText(pNoteText: String?) {
        if (pNoteText == null) {
            xmlNoteText = null
            noteText = null
            return
        }
        noteText = HtmlTools.makeValidXml(pNoteText)
        xmlNoteText = HtmlTools.getInstance().toXhtml(noteText)
    }

    // Redefined in MindMapNodeModel.
    override val plainTextContent: String
        get() =// Redefined in MindMapNodeModel.
            toString()

    override fun getShortText(controller: ModeController?): String {
        var adaptedText = plainTextContent
        // adaptedText = adaptedText.replaceAll("<html>", "");
        if (adaptedText.length > 40) adaptedText = adaptedText.substring(0, 40) + " ..."
        return adaptedText
    }
    //
    // Interface MindMapNode
    //
    //
    // get/set methods
    //
    /** Creates the TreePath recursively  */
    override val path: TreePath
        get() {
            val pathVector = Vector<NodeAdapter>()
            val treePath: TreePath
            addToPathVector(pathVector)
            treePath = TreePath(pathVector.toTypedArray())
            return treePath
        }

    override fun getCloud(): MindMapCloud? {
        return cloud
    }

    override fun setCloud(cloud: MindMapCloud?) {
        // Take care to keep the calculated iterative levels consistent
        if (cloud != null && this.cloud == null) {
            changeChildCloudIterativeLevels(1)
        } else if (cloud == null && this.cloud != null) {
            changeChildCloudIterativeLevels(-1)
        }
        this.cloud = cloud
    }

    /**
     * Correct iterative level values of children
     */
    private fun changeChildCloudIterativeLevels(deltaLevel: Int) {
        val e: ListIterator<NodeAdapter>? = childrenUnfolded()
        while (e!!.hasNext()) {
            val childNode = e.next()
            val childCloud = childNode.getCloud()
            childCloud?.changeIterativeLevel(deltaLevel)
            childNode.changeChildCloudIterativeLevels(deltaLevel)
        }
    }

    /** A Node-Style like MindMapNode.STYLE_FORK or MindMapNode.STYLE_BUBBLE  */
    override fun getStyle(): String? {
        var returnedString = bareStyle /* Style string returned */
        if (bareStyle == null) {
            returnedString = if (isRoot) {
                mapFeedback!!.getProperty(
                        FreeMind.RESOURCES_ROOT_NODE_STYLE)
            } else {
                val stdstyle = mapFeedback!!.getProperty(
                        FreeMind.RESOURCES_NODE_STYLE)
                if (stdstyle == MindMapNode.Companion.STYLE_AS_PARENT) {
                    parentNode.getStyle()
                } else {
                    stdstyle
                }
            }
        } else if (isRoot && bareStyle == MindMapNode.Companion.STYLE_AS_PARENT) {
            returnedString = mapFeedback!!.getProperty(
                    FreeMind.RESOURCES_ROOT_NODE_STYLE)
        } else if (bareStyle == MindMapNode.Companion.STYLE_AS_PARENT) {
            returnedString = parentNode.getStyle()
        }

        // Handle the combined node style
        return if (returnedString == MindMapNode.Companion.STYLE_COMBINED) {
            if (isFolded) {
                MindMapNode.Companion.STYLE_BUBBLE
            } else {
                MindMapNode.Companion.STYLE_FORK
            }
        } else returnedString
    }

    override fun hasStyle(): Boolean {
        return bareStyle != null
    }

    // ////
    // The set methods. I'm not sure if they should be here or in the
    // implementing class.
    // ///
    override fun setStyle(style: String?) {
        bareStyle = style
    }

    //
    // font handling
    //
    // Remark to setBold and setItalic implementation
    //
    // Using deriveFont() is a bad idea, because it does not really choose
    // the appropriate face. For example, instead of choosing face
    // "Arial Bold", it derives the bold face from "Arial".
    // Node holds font only in the case that the font is not default.
    fun establishOwnFont() {
        font = if (font != null) font else mapFeedback.getDefaultFont()
    }

    fun toggleStrikethrough() {
        establishOwnFont()
        val attributes: MutableMap<*, *> = font!!.attributes
        if (attributes.containsKey(TextAttribute.STRIKETHROUGH) && attributes[TextAttribute.STRIKETHROUGH] === TextAttribute.STRIKETHROUGH_ON) {
            attributes.remove(TextAttribute.STRIKETHROUGH)
        } else {
            attributes[TextAttribute.STRIKETHROUGH] = TextAttribute.STRIKETHROUGH_ON
        }
        font = Font(attributes)
    }

    fun toggleBold() {
        establishOwnFont()
        font = mapFeedback!!.getFontThroughMap(font!!.deriveFont(font!!.style xor Font.BOLD))
    }

    fun toggleItalic() {
        establishOwnFont()
        font = mapFeedback!!.getFontThroughMap(font!!.deriveFont(font!!.style xor Font.ITALIC))
    }

    override fun setFontSize(fontSize: Int) {
        establishOwnFont()
        font = mapFeedback!!.getFontThroughMap(
                font!!.deriveFont(fontSize.toFloat()))
    }

    override fun getFontSize(): String? {
        return if (font != null) {
            font!!.size.toString()
        } else {
            mapFeedback!!.getProperty("defaultfontsize")
        }
    }

    override val fontFamilyName: String?
        get() = if (font != null) {
            font!!.family
        } else {
            mapFeedback!!.getProperty("defaultfont")
        }
    override var isBold: Boolean
        get() = if (font != null) font!!.isBold else false
        set(bold) {
            if (bold != isBold) {
                toggleBold()
            }
        }
    override var isItalic: Boolean
        get() = if (font != null) font!!.isItalic else false
        set(italic) {
            if (italic != isItalic) {
                toggleItalic()
            }
        }
    override var isStrikethrough: Boolean
        get() {
            if (font != null) {
                val attr = font!!.attributes
                if (attr.containsKey(TextAttribute.STRIKETHROUGH)) {
                    return attr[TextAttribute.STRIKETHROUGH] === TextAttribute.STRIKETHROUGH_ON
                }
            }
            return false
        }
        set(strikethrough) {
            if (strikethrough != isStrikethrough) {
                toggleStrikethrough()
            }
        }

    // fc, 24.9.2003:
    override fun getIcons(): List<MindIcon?> {
        return if (icons == null) emptyList<MindIcon>() else icons
    }

    override fun addIcon(_icon: MindIcon?, position: Int) {
        createIcons()
        if (position == MindIcon.Companion.LAST) {
            icons!!.add(_icon)
        } else {
            icons!!.add(position, _icon)
        }
    }

    /** @return returns the number of remaining icons.
     */
    override fun removeIcon(position: Int): Int {
        var position = position
        createIcons()
        if (position == MindIcon.Companion.LAST) {
            position = icons!!.size - 1
        }
        icons!!.removeAt(position)
        val returnSize = icons!!.size
        if (returnSize == 0) {
            icons = null
        }
        return returnSize
    }
    // end, fc, 24.9.2003
    /**
     * True iff one of node's *strict* descendants is folded. A node N is
     * not its strict descendant - the fact that node itself is folded is not
     * sufficient to return true.
     */
    fun hasFoldedStrictDescendant(): Boolean {
        val e: ListIterator<NodeAdapter>? = childrenUnfolded()
        while (e!!.hasNext()) {
            val child = e.next()
            if (child.isFolded || child.hasFoldedStrictDescendant()) {
                return true
            }
        }
        return false
    }

    /**
     * @return true, if one of its parents is folded. If itself is folded,
     * doesn't matter.
     */
    override fun hasFoldedParents(): Boolean {
        if (isRoot) return false
        return if (parentNode!!.isFolded) {
            true
        } else parentNode!!.hasFoldedParents()
    }

    override fun shallowCopy(): MindMapNode? {
        return try {
            // get XML from me.
            val writer = StringWriter()
            save(writer, map.getLinkRegistry(), true, false)
            val result = writer.toString()
            val IDToTarget = HashMap<String?, NodeAdapter?>()
            val copy = map!!.createNodeTreeFromXml(StringReader(result), IDToTarget)
            copy.isFolded = false
            copy
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
            null
        }
    }
    //
    // other
    //
    /**
     * @return
     */
    override val mapFeedback: MapFeedback?
        get() = map.getMapFeedback()

    override fun toString(): String {
        return text!!
    }

    override fun isDescendantOf(pParentNode: MindMapNode): Boolean {
        return if (isRoot) false else if (pParentNode === parentNode) true else parentNode!!.isDescendantOf(pParentNode)
    }

    override val isRoot: Boolean
        get() = parentNode == null

    override fun isDescendantOfOrEqual(pParentNode: MindMapNode): Boolean {
        return if (this === pParentNode) {
            true
        } else isDescendantOf(pParentNode)
    }

    override fun hasChildren(): Boolean {
        return children != null && !children!!.isEmpty()
    }

    override fun getChildPosition(childNode: MindMapNode?): Int {
        var position = 0
        val i = children!!.listIterator()
        while (i.hasNext()) {
            if (i.next() === childNode) {
                return position
            }
            ++position
        }
        return -1
    }

    override fun childrenUnfolded(): MutableListIterator<MindMapNode>? {
        return if (children != null) children!!.listIterator() else Collections.EMPTY_LIST.listIterator()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#sortedChildrenUnfolded()
	 */
    override fun sortedChildrenUnfolded(): ListIterator<MindMapNode>? {
        if (children == null) return null
        val sorted = LinkedList(children)
        /*
		 * Using this stable sort, we assure that the left nodes came in front
		 * of the right ones.
		 */Collections.sort(sorted, object : Comparator<MindMapNode> {
            override fun compare(pO1: MindMapNode, pO2: MindMapNode): Int {
                return comp(pO2.isLeft,
                        pO1.isLeft)
            }

            private fun comp(pLeft: Boolean, pLeft2: Boolean): Int {
                if (pLeft == pLeft2) {
                    return 0
                }
                return if (pLeft) {
                    1
                } else -1
            }
        })
        return sorted.listIterator()
    }

    override fun childrenFolded(): ListIterator<MindMapNode?>? {
        return if (isFolded) {
            Collections.emptyListIterator()
        } else childrenUnfolded()
    }

    override fun getChildren(): List<MindMapNode> {
        return Collections.unmodifiableList(if (children != null) children else emptyList())
    }
    //
    // Interface TreeNode
    //
    /**
     * AFAIK there is no way to get an enumeration out of a linked list. So this
     * exception must be thrown, or we can't implement TreeNode anymore (maybe
     * we shouldn't?)
     */
    override fun children(): Enumeration<out TreeNode>? {
        throw UnsupportedOperationException(
                "Use childrenFolded or childrenUnfolded instead")
    }

    override fun getAllowsChildren(): Boolean {
        return ALLOWSCHILDREN
    }

    override fun getChildAt(childIndex: Int): TreeNode {
        // fc, 11.12.2004: This is not understandable, that a child does not
        // exist if the parent is folded.
        // if (isFolded()) {
        // return null;
        // }
        return children!![childIndex]
    }

    override fun getChildCount(): Int {
        return if (children == null) 0 else children!!.size
    }

    // (PN)
    // public int getChildCount() {
    // if (isFolded()) {
    // return 0;
    // }
    // return children.size();
    // }
    // // Daniel: ^ The name of this method is confusing. It does nto convey
    // // the meaning, at least not to me.
    override fun getIndex(node: TreeNode): Int {
        return children!!.indexOf(node as MindMapNode) // uses equals()
    }

    override fun getParent(): TreeNode {
        return parentNode!!
    }

    override fun isLeaf(): Boolean {
        return childCount == 0
    }

    // fc, 16.12.2003 left-right bug:
    override var isLeft: Boolean
        get() {
            if (parent != null && !parentNode!!.isRoot) {
                return parentNode!!.isLeft
            }
            if (position == UNKNOWN_POSITION && !isRoot) {
                isLeft = parentNode!!.isLeft
            }
            return position == LEFT_POSITION
        }
        set(isLeft) {
            position = if (isLeft) LEFT_POSITION else RIGHT_POSITION
            if (!isRoot) {
                for (i in 0 until childCount) {
                    val child = getChildAt(i) as NodeAdapter
                    child.position = position
                }
            }
        }
    override val isNewChildLeft: Boolean
        get() {
            if (!isRoot) {
                return isLeft
            }
            var rightChildrenCount = 0
            for (i in 0 until childCount) {
                if (!(getChildAt(i) as MindMapNode).isLeft) rightChildrenCount++
                if (rightChildrenCount > childCount / 2) {
                    return true
                }
            }
            return false
        }

    //
    // Interface MutableTreeNode
    //
    // do all remove methods have to work recursively to make the
    // Garbage Collection work (Nodes in removed Sub-Trees reference each
    // other)?
    override fun insert(child: MutableTreeNode, index: Int) {
        var index = index
        logger!!.finest("Insert at $index the node $child")
        val childNode = child as MindMapNode
        if (index < 0) { // add to the end (used in xml load) (PN)
            index = childCount
            children.add(index, childNode)
        } else { // mind preferred child :-)
            children.add(index, childNode)
            preferredChild = childNode
        }
        child.setParent(this)
        recursiveCallAddChildren(this, childNode)
    }

    override fun remove(index: Int) {
        val node = children!![index] as MutableTreeNode
        remove(node)
    }

    override fun remove(node: MutableTreeNode) {
        if (node === preferredChild) { // mind preferred child :-) (PN)
            val index = children!!.indexOf(node)
            if (children!!.size > index + 1) {
                preferredChild = children!![index + 1]
            } else {
                preferredChild = if (index > 0) children
                        .get(index - 1) else null
            }
        }
        node.setParent(null)
        children.remove(node)
        // call remove child hook after removal.
        recursiveCallRemoveChildren(this, node as MindMapNode, this)
    }

    private fun recursiveCallAddChildren(node: MindMapNode?,
                                         addedChild: MindMapNode) {
        // Tell any node hooks that the node is added:
        if (node is MindMapNode) {
            for (hook in node.activatedHooks) {
                if (addedChild.parentNode === node) {
                    hook!!.onAddChild(addedChild)
                }
                hook!!.onAddChildren(addedChild)
            }
        }
        if (!node!!.isRoot && node.parentNode != null) recursiveCallAddChildren(node.parentNode, addedChild)
    }

    /**
     * @param oldDad
     * the last dad node had.
     */
    private fun recursiveCallRemoveChildren(node: MindMapNode?,
                                            removedChild: MindMapNode, oldDad: MindMapNode) {
        for (hook in node.getActivatedHooks()) {
            if (removedChild.parentNode === node) {
                hook!!.onRemoveChild(removedChild)
            }
            hook!!.onRemoveChildren(removedChild, oldDad)
        }
        if (!node!!.isRoot && node.parentNode != null) recursiveCallRemoveChildren(node.parentNode, removedChild,
                oldDad)
    }

    override fun removeFromParent() {
        parentNode!!.remove(this)
    }

    override fun setParent(newParent: MutableTreeNode) {
        parentNode = newParent as MindMapNode
    }

    fun setParent(newParent: MindMapNode?) {
        parentNode = newParent
    }

    override fun setUserObject(`object`: Any) {
        text = `object` as String
    }
    // //////////////
    // Private methods. Internal Implementation
    // ////////////
    /** Recursive Method for getPath()  */
    private fun addToPathVector(pathVector: Vector<NodeAdapter>) {
        pathVector.add(0, this) // Add myself to beginning of Vector
        if (parentNode != null) {
            (parentNode as NodeAdapter).addToPathVector(pathVector)
        }
    }

    // for cursor navigation within a level (PN)
    override val nodeLevel: Int
        get() { // for cursor navigation within a level (PN)
            var level = 0
            var parent: MindMapNode?
            parent = this
            while (!parent!!.isRoot) {
                if (parent.isVisible) {
                    level++
                }
                parent = parent.parentNode
            }
            return level
        }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#addHook(freemind.modes.NodeHook)
	 */
    override fun addHook(hook: PermanentNodeHook?): PermanentNodeHook {
        // add then
        requireNotNull(hook) { "Added null hook." }
        createHooks()
        hooks!!.add(hook)
        return hook
    }

    override fun invokeHook(hook: NodeHook?) {
        // initialize:
        hook!!.startupMapHook()
        // the main invocation:
        hook.setNode(this)
        try {
            hook.invoke(this)
        } catch (e: Exception) {
            // FIXME: Do something special here, but in any case, do not add the
            // hook
            // to the activatedHooks:
            Resources.getInstance().logException(e)
            return
        }
        if (hook is PermanentNodeHook) {
            createActivatedHooks()
            activatedHooks!!.add(hook as PermanentNodeHook?)
        } else {
            // end of its short life:
            hook.shutdownMapHook()
        }
    }

    private fun createActivatedHooks() {
        if (activatedHooks == null) {
            activatedHooks = HashSet()
        }
    }

    private fun createToolTip() {
        if (toolTip == null) {
            toolTip = TreeMap()
        }
    }

    private fun createHooks() {
        if (hooks == null) {
            hooks = Vector()
        }
    }

    private fun createStateIcons() {
        if (stateIcons == null) {
            stateIcons = TreeMap()
        }
    }

    private fun createIcons() {
        if (icons == null) {
            icons = Vector()
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#getHooks()
	 */
    override fun getHooks(): List<PermanentNodeHook?> {
        return if (hooks == null) emptyList<PermanentNodeHook>() else Collections.unmodifiableList(hooks)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#getActivatedHooks()
	 */
    override fun getActivatedHooks(): Collection<PermanentNodeHook?> {
        return if (activatedHooks == null) {
            emptyList<PermanentNodeHook>()
        } else Collections.unmodifiableCollection(activatedHooks)
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#removeHook(freemind.modes.NodeHook)
	 */
    override fun removeHook(hook: PermanentNodeHook?) {
        // the order is crucial here: the shutdown method should be able to
        // perform "nodeChanged"
        // calls without having its own updateNodeHook method to be called
        // again.
        val name = hook!!.name
        createActivatedHooks()
        if (activatedHooks!!.contains(hook)) {
            activatedHooks!!.remove(hook)
            if (activatedHooks!!.size == 0) {
                activatedHooks = null
            }
            hook.shutdownMapHook()
        }
        createHooks()
        hooks!!.remove(hook)
        if (hooks!!.size == 0) hooks = null
        logger!!.fine("Removed hook $name at $hook.")
    }

    override fun removeAllHooks() {
        var timeout = getHooks().size * 2
        while (getHooks().size > 0 && timeout-- > 0) {
            try {
                removeHook(getHooks()[0])
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
    }

    /**
     */
    override fun getToolTip(): SortedMap<String?, String> {
        var toolTipChanged = false
        var result = toolTip
        if (result == null) result = TreeMap()
        // add preview to other map, if appropriate:
        var link = link
        // replace jump mark
        if (link != null && link.matches(".*\\" + FreeMindCommon.FREEMIND_FILE_EXTENSION + "(#.*)?")) {
            link = link.replaceFirst("#.*?$".toRegex(), "")
        }
        if (link != null && link.endsWith(FreeMindCommon.FREEMIND_FILE_EXTENSION)) {
            val linkHtmlPart = "alt=\"$link\""
            var addIt = true
            // this should be done only once per link, so we have to prevent doing that every time again.
            if (result.containsKey(TOOLTIP_PREVIEW_KEY)) {
                // check, if the contained link belongs to the same file (ie. hasn't change in between)
                val prev = result[TOOLTIP_PREVIEW_KEY]
                if (prev != null && prev.contains(linkHtmlPart)) {
                    addIt = false
                }
            }
            if (addIt) {
                try {
                    val mmFile = urlToFile(URL(map.getURL(), link))
                    val thumbnailFileName: String = Resources.getInstance().createThumbnailFileName(mmFile)
                    if (File(thumbnailFileName).exists()) {
                        val thumbUrl = fileToUrl(File(thumbnailFileName))
                        val imgHtml = "<img src=\"$thumbUrl\" $linkHtmlPart/>"
                        logger!!.info("Adding new tooltip: $imgHtml")
                        result[TOOLTIP_PREVIEW_KEY] = imgHtml
                        toolTipChanged = true
                    }
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
            }
        } else {
            if (result.containsKey(TOOLTIP_PREVIEW_KEY)) {
                result.remove(TOOLTIP_PREVIEW_KEY)
                toolTipChanged = true
            }
        }
        if (toolTipChanged) {
            // write back, if changed
            toolTip = if (result.size == 0) {
                null
            } else {
                result
            }
        }
        return Collections.unmodifiableSortedMap(result)
    }

    /**
     */
    override fun setToolTip(key: String?, string: String?) {
        createToolTip()
        if (string == null) {
            if (toolTip!!.containsKey(key)) {
                toolTip!!.remove(key)
            }
            if (toolTip!!.size == 0) toolTip = null
        } else {
            toolTip!![key] = string
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.MindMapNode#getNodeId()
	 */
    override fun getObjectId(controller: ModeController?): String? {
        return controller!!.getNodeID(this)
    }

    @Throws(IOException::class)
    override fun save(writer: Writer?, registry: MindMapLinkRegistry?,
                      saveInvisible: Boolean, saveChildren: Boolean): XMLElement? {
        // pre save event to save all contents of the node:
        mapFeedback!!.firePreSaveEvent(this)
        val node = XMLElement()

        // if (!isNodeClassToBeSaved()) {
        node.name = XMLElementAdapter.Companion.XML_NODE
        // } else {
        // node.setName(XMLElementAdapter.XML_NODE_CLASS_PREFIX
        // + this.getClass().getName());
        // }
        /** fc, 12.6.2005: XML must not contain any zero characters.  */
        val text = this.toString().replace('\u0000', ' ')
        if (!HtmlTools.isHtmlNode(text)) {
            node.setAttribute(XMLElementAdapter.Companion.XML_NODE_TEXT, text)
        } else {
            // save <content> tag:
            val htmlElement = XMLElement()
            htmlElement.name = XMLElement.XML_NODE_XHTML_CONTENT_TAG
            htmlElement.setAttribute(XMLElementAdapter.Companion.XML_NODE_XHTML_TYPE_TAG,
                    XMLElementAdapter.Companion.XML_NODE_XHTML_TYPE_NODE)
            htmlElement
                    .setEncodedContent(convertToEncodedContent(getXmlText()))
            node.addChild(htmlElement)
        }
        if (getXmlNoteText() != null) {
            val htmlElement = XMLElement()
            htmlElement.name = XMLElement.XML_NODE_XHTML_CONTENT_TAG
            htmlElement.setAttribute(XMLElementAdapter.Companion.XML_NODE_XHTML_TYPE_TAG,
                    XMLElementAdapter.Companion.XML_NODE_XHTML_TYPE_NOTE)
            htmlElement
                    .setEncodedContent(convertToEncodedContent(getXmlNoteText()))
            node.addChild(htmlElement)
        }
        // save additional info:
        if (additionalInfo != null) {
            node.setAttribute(XMLElementAdapter.Companion.XML_NODE_ENCRYPTED_CONTENT,
                    additionalInfo!!)
        }
        // ((MindMapEdgeModel)getEdge()).save(doc,node);
        val edge = edge!!.save()
        if (edge != null) {
            node.addChild(edge)
        }
        if (getCloud() != null) {
            val cloud = getCloud()!!.save()
            node.addChild(cloud!!)
        }
        val linkVector = registry!!.getAllLinksFromMe(this)
        for (i in linkVector!!.indices) {
            if (linkVector[i] is ArrowLinkAdapter) {
                val arrowLinkElement = (linkVector[i] as ArrowLinkAdapter).save()
                node.addChild(arrowLinkElement!!)
            }
        }

        // virtual link targets:
        val targetVector = registry.getAllLinksIntoMe(this)
        for (i in targetVector!!.indices) {
            if (targetVector[i] is ArrowLinkAdapter) {
                val arrowLinkTargetElement = (targetVector[i] as ArrowLinkAdapter).createArrowLinkTarget(registry).save()
                node.addChild(arrowLinkTargetElement!!)
            }
        }
        if (isFolded) {
            node.setAttribute("FOLDED", "true")
        }

        // fc, 17.12.2003: Remove the left/right bug.
        // VVV save if and only if parent is root.
        if (!isRoot && parentNode!!.isRoot) {
            node.setAttribute("POSITION", if (isLeft) "left" else "right")
        }

        // the id is used, if there is a local hyperlink pointing to me or a
        // real link.
        val label = registry.getLabel(this)
        if (!sSaveOnlyIntrinsicallyNeededIds
                || registry.isTargetOfLocalHyperlinks(label) || registry
                        .getAllLinksIntoMe(this).size > 0) {
            if (label != null) {
                node.setAttribute("ID", label)
            }
        }
        if (color != null) {
            node.setAttribute("COLOR", colorToXml(color)!!)
        }

        // new background color.
        if (backgroundColor != null) {
            node.setAttribute("BACKGROUND_COLOR",
                    colorToXml(backgroundColor)!!)
        }
        if (bareStyle != null) {
            node.setAttribute("STYLE", this.style!!)
        }
        // ^ Here cannot be just getStyle() without super. This is because
        // getStyle's style depends on folded / unfolded. For example, when
        // real style is fork and node is folded, getStyle returns
        // MindMapNode.STYLE_BUBBLE, which is not what we want to save.

        // layout
        if (vGap != VGAP) {
            node.setAttribute("VGAP", Integer.toString(vGap))
        }
        if (hGap != HGAP) {
            node.setAttribute("HGAP", Integer.toString(hGap))
        }
        if (shiftY != 0) {
            node.setAttribute("VSHIFT", Integer.toString(shiftY))
        }
        // link
        if (link != null) {
            node.setAttribute("LINK", link!!)
        }

        // history information, fc, 11.4.2005
        if (historyInformation != null) {
            node.setAttribute(XMLElementAdapter.Companion.XML_NODE_HISTORY_CREATED_AT,
                    dateToString(historyInformation!!.getCreatedAt()))
            node.setAttribute(
                    XMLElementAdapter.Companion.XML_NODE_HISTORY_LAST_MODIFIED_AT, dateToString(historyInformation!!
                    .getLastModifiedAt()))
        }
        // font
        if (font != null) {
            val fontElement = XMLElement()
            fontElement.name = "font"
            if (font != null) {
                fontElement.setAttribute("NAME", font!!.family)
            }
            if (font!!.size != 0) {
                fontElement.setAttribute("SIZE",
                        Integer.toString(font!!.size))
            }
            if (isBold) {
                fontElement.setAttribute("BOLD", "true")
            }
            if (isStrikethrough) {
                fontElement.setAttribute("STRIKETHROUGH", "true")
            }
            if (isItalic) {
                fontElement.setAttribute("ITALIC", "true")
            }
            if (isUnderlined) {
                fontElement.setAttribute("UNDERLINE", "true")
            }
            node.addChild(fontElement)
        }
        for (i in getIcons().indices) {
            val iconElement = XMLElement()
            iconElement.name = "icon"
            iconElement.setAttribute("BUILTIN",
                    getIcons()[i]!!.name!!)
            node.addChild(iconElement)
        }
        for (permHook in getActivatedHooks()) {
            if (permHook is DontSaveMarker) {
                continue
            }
            val hookElement = XMLElement()
            hookElement.name = "hook"
            permHook!!.save(hookElement)
            node.addChild(hookElement)
        }
        if (mAttributeVector != null) {
            for (i in mAttributeVector!!.indices) {
                val attributeElement = XMLElement()
                attributeElement.name = XMLElementAdapter.Companion.XML_NODE_ATTRIBUTE
                val attr = mAttributeVector!![i]
                attributeElement.setAttribute("NAME", attr.getName())
                attributeElement.setAttribute("VALUE", attr.getValue())
                node.addChild(attributeElement)
            }
        }
        if (saveChildren && childrenUnfolded()!!.hasNext()) {
            node.writeWithoutClosingTag(writer!!)
            // recursive
            saveChildren(writer, registry, this, saveInvisible)
            node.writeClosingTag(writer)
        } else {
            node.write(writer!!)
        }
        return node
    }

    @Throws(IOException::class)
    private fun saveChildren(writer: Writer?, registry: MindMapLinkRegistry?,
                             node: NodeAdapter, saveHidden: Boolean) {
        val e: ListIterator<NodeAdapter>? = node.childrenUnfolded()
        while (e!!.hasNext()) {
            val child = e.next()
            if (saveHidden || child.isVisible) child.save(writer, registry, saveHidden, true) else saveChildren(writer, registry, child, saveHidden)
        }
    }

    override fun hasExactlyOneVisibleChild(): Boolean {
        var count = 0
        val i: ListIterator<MindMapNode>? = childrenUnfolded()
        while (i!!.hasNext()) {
            if (i.next().isVisible) count++
            if (count == 2) return false
        }
        return count == 1
    }

    override fun hasVisibleChilds(): Boolean {
        val i: ListIterator<MindMapNode>? = childrenUnfolded()
        while (i!!.hasNext()) {
            if (i.next().isVisible) return true
        }
        return false
    }

    override fun calcShiftY(): Int {
        return try {
            // return 0;
            shiftY + if (parentNode!!.hasExactlyOneVisibleChild()) SHIFT else 0
        } catch (e: NullPointerException) {
            0
        }
    }

    /**
     *
     */
    override var additionalInfo: String?
        get() = null
        set(info) {}

    /** This method must be synchronized as the TreeMap isn't.  */
    @Synchronized
    override fun setStateIcon(key: String?, icon: ImageIcon?) {
        // logger.info("Set state of key:"+key+", icon "+icon);
        createStateIcons()
        if (icon != null) {
            stateIcons!![key] = icon
        } else if (stateIcons!!.containsKey(key)) {
            stateIcons!!.remove(key)
        }
        if (stateIcons!!.size == 0) stateIcons = null
    }

    override fun getStateIcons(): Map<String?, ImageIcon> {
        return if (stateIcons == null) emptyMap() else Collections.unmodifiableSortedMap(stateIcons)
    }

    override fun getVGap(): Int {
        return vGap
    }

    override fun setVGap(gap: Int) {
        vGap = Math.max(gap, 0)
    }

    override val isVisible: Boolean
        get() {
            val filter = map.getFilter()
            return filter == null || filter.isVisible(this)
        }
    override var listeners = EventListenerList()

    //
    // Constructors
    //
    init {
        map = pMap
        text = userObject as String?
        hooks = null // lazy, fc, 30.6.2005.
        activatedHooks = null // lazy, fc, 30.6.2005
        if (logger == null) logger = Resources.getInstance().getLogger(this.javaClass.name)
        // create creation time:
        historyInformation = HistoryInformation()
        if (sSaveIdPropertyChangeListener == null) {
            sSaveIdPropertyChangeListener = FreemindPropertyListener { propertyName, newValue, oldValue ->
                if (propertyName
                        == FreeMindCommon.SAVE_ONLY_INTRISICALLY_NEEDED_IDS) {
                    sSaveOnlyIntrinsicallyNeededIds = java.lang.Boolean.valueOf(
                            newValue).toBoolean()
                }
            }
            Controller
                    .addPropertyChangeListenerAndPropagate(sSaveIdPropertyChangeListener)
        }
    }

    override fun addTreeModelListener(l: TreeModelListener) {
        listeners.add(TreeModelListener::class.java, l)
    }

    override fun removeTreeModelListener(l: TreeModelListener) {
        listeners.remove(TreeModelListener::class.java, l)
    }

    //
    // Attributes
    //
    override val attributeKeyList: List<String?>
        get() {
            if (mAttributeVector == null) {
                return emptyList<String>()
            }
            val returnValue = Vector<String?>()
            for (attr in mAttributeVector!!) {
                returnValue.add(attr.getName())
            }
            return returnValue
        }
    override val attributes: List<Attribute?>
        get() = if (mAttributeVector == null) {
            emptyList<Attribute>()
        } else Collections.unmodifiableList(mAttributeVector)
    override val attributeTableLength: Int
        get() = if (mAttributeVector == null) {
            0
        } else mAttributeVector!!.size

    override fun getAttribute(pPosition: Int): Attribute {
        checkAttributePosition(pPosition)
        return Attribute(attributeVector[pPosition])
    }

    /**
     * @param pPosition
     */
    fun checkAttributePosition(pPosition: Int) {
        require(!(mAttributeVector == null || attributeTableLength <= pPosition || pPosition < 0)) { "Attribute position out of range: $pPosition" }
    }

    override fun getAttribute(pKey: String?): String? {
        if (mAttributeVector == null) {
            return null
        }
        for (attr in mAttributeVector!!) {
            if (safeEquals(attr.getName(), pKey)) {
                return attr.getValue()
            }
        }
        return null
    }

    override fun getAttributePosition(pKey: String?): Int {
        if (mAttributeVector == null) {
            return -1
        }
        var index = 0
        for (attr in mAttributeVector!!) {
            if (safeEquals(attr.getName(), pKey)) {
                return index
            }
            index++
        }
        return -1
    }

    override fun setAttribute(pPosition: Int, pAttribute: Attribute?) {
        checkAttributePosition(pPosition)
        mAttributeVector!![pPosition] = pAttribute
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#addAttribute(freemind.modes.attributes.Attribute)
	 */
    override fun addAttribute(pAttribute: Attribute?): Int {
        attributeVector.add(pAttribute)
        return attributeVector.indexOf(pAttribute)
    }

    /* (non-Javadoc)
	 * @see freemind.modes.MindMapNode#insertAttribute(int, freemind.modes.attributes.Attribute)
	 */
    override fun insertAttribute(pPosition: Int, pAttribute: Attribute?) {
        checkAttributePosition(pPosition)
        attributeVector.add(pPosition, pAttribute)
    }

    override fun removeAttribute(pPosition: Int) {
        checkAttributePosition(pPosition)
        mAttributeVector!!.removeAt(pPosition)
    }

    private val attributeVector: Vector<Attribute?>
        private get() {
            if (mAttributeVector == null) {
                mAttributeVector = Vector()
            }
            return mAttributeVector!!
        }

    companion object {
        const val SHIFT = -2 // height of the vertical shift between node and

        // its closest child
        const val HGAP = 20 // width of the horizontal gap that

        // contains the edges
        const val VGAP = 3 // height of the vertical gap between nodes
        const val LEFT_POSITION = -1
        const val RIGHT_POSITION = 1
        const val UNKNOWN_POSITION = 0

        // graph support
        private const val TOOLTIP_PREVIEW_KEY = "preview"
        private const val ALLOWSCHILDREN = true

        // Logging:
        protected var logger: Logger? = null
        private var sSaveIdPropertyChangeListener: FreemindPropertyListener? = null
        private var sSaveOnlyIntrinsicallyNeededIds = false
        fun convertToEncodedContent(xmlText2: String?): String {
            val replace = HtmlTools.makeValidXml(xmlText2!!)
            return HtmlTools.unicodeToHTMLUnicodeEntity(replace, true)
        }
    }
}