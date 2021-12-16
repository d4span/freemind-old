/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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
package freemind.modes.mindmapmode.actions.xml.actors

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
import java.awt.GridBagLayout
import javax.swing.JLabel
import java.awt.GridBagConstraints
import java.awt.Insets
import java.awt.Dimension
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
import java.awt.BorderLayout
import java.awt.GridLayout
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
import java.awt.Rectangle
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
import java.awt.Color
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
import java.awt.CardLayout
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
import java.awt.Robot
import java.awt.AWTException
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
import java.awt.Stroke
import java.awt.BasicStroke
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
import freemind.controller.MapMouseWheelListenerimport

freemind.main.*
import java.awt.HeadlessException
import freemind.modes.MapFeedbackAdapter.NodesDepthComparator
import freemind.modes.MindMapLinkRegistry.SynchronousVector
import freemind.modes.mindmapmode.actions.ApplyPatternAction.ExternalPatternAction
import java.awt.FileDialog
import freemind.modes.FreeMindAwtFileDialog.FreeMindFilenameFilter
import freemind.modes.FreeMindAwtFileDialog
import freemind.modes.FreeMindAwtFileDialog.DirFilter
import freemind.modes.FreeMindAwtFileDialog.FileOnlyFilter
import freemind.modes.FreeMindAwtFileDialog.FileAndDirFilter
import kotlin.jvm.JvmStatic
import freemind.modes.NodeViewEvent
import freemind.modes.ExtendedMapFeedbackAdapter
import freemind.modes.ExtendedMapFeedbackAdapter.DummyTransferableimport

java.io.*import java.lang.Exceptionimport

java.util.*import java.util.logging.Levelimport

java.util.logging.Loggerimport java.util.regex.Pattern
/**
 * @author foltin
 * @date 20.03.2014
 */
class PasteActor(pMapFeedback: ExtendedMapFeedback) : XmlActorAdapter(pMapFeedback) {
    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
	 * generated.instance.XmlAction)
	 */
    override fun act(action: XmlAction?) {
        val pasteAction = action as PasteNodeAction?
        _paste(getTransferable(pasteAction!!.transferableContent),
                getNodeFromID(pasteAction.node!!),
                pasteAction.asSibling, pasteAction.getIsLeft())
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.actions.ActorXml#getDoActionClass()
	 */
    override val doActionClass: Class<*>
        get() = PasteNodeAction::class.java

    /**
     * @param t
     * @param coord
     * @param pUndoAction
     * is filled automatically when not null.
     * @return a new PasteNodeAction.
     */
    fun getPasteNodeAction(t: Transferable?,
                           coord: NodeCoordinate, pUndoAction: UndoPasteNodeAction?): PasteNodeAction {
        val pasteAction = PasteNodeAction()
        val targetId = getNodeID(coord.target)
        pasteAction.node = targetId
        pasteAction.transferableContent = getTransferableContent(t,
                pUndoAction)
        pasteAction.asSibling = coord.asSibling
        pasteAction.setIsLeft(coord.isLeft)
        if (pUndoAction != null) {
            pUndoAction.node = targetId
            pUndoAction.asSibling = coord.asSibling
            pUndoAction.setIsLeft(coord.isLeft)
            if (logger!!.isLoggable(Level.FINE)) {
                val s = marshall(pUndoAction)
                logger!!.fine("Undo action: $s")
            }
        }
        return pasteAction
    }

    /** URGENT: Change this method.  */
    fun paste(node: MindMapNode?, parent: MindMapNode?) {
        if (node != null) {
            insertNodeInto(node, parent)
            exMapFeedback.map.nodeStructureChanged(parent)
        }
    }

    /**
     * @param t
     * the content
     * @param target
     * where to add the content
     * @param asSibling
     * if true, the content is added beside the target, otherwise as
     * new children
     * @param isLeft
     * if something is pasted as a sibling to root, it must be
     * decided on which side of root
     * @return true, if successfully executed.
     */
    fun paste(t: Transferable?, target: MindMapNode?, asSibling: Boolean,
              isLeft: Boolean): Boolean {
        val undoAction = UndoPasteNodeAction()
        val pasteAction: PasteNodeAction
        pasteAction = getPasteNodeAction(t, NodeCoordinate(target,
                asSibling, isLeft), undoAction)
        // Undo-action
        /*
		 * how to construct the undo action for a complex paste? a) Paste pastes
		 * a number of new nodes that are adjacent. This number should be
		 * determined.
		 * 
		 * 
		 * d) But, as there are many possibilities which data flavor is pasted,
		 * it has to be determined before, which one will be taken.
		 */return exMapFeedback.doTransaction("paste",
                ActionPair(pasteAction, undoAction))
    }

    class NodeCoordinate {
        var target: MindMapNode? = null
        var asSibling = false
        var isLeft: Boolean

        constructor(target: MindMapNode?, asSibling: Boolean,
                    isLeft: Boolean) {
            this.target = target
            this.asSibling = asSibling
            this.isLeft = isLeft
        }

        val node: MindMapNode
            get() = if (asSibling) {
                val parentNode = target.getParentNode()
                parentNode!!.getChildAt(parentNode
                        .getChildPosition(target) - 1)
            } else {
                logger!!.finest("getChildCount = " + target!!.childCount
                        + ", target = " + target)
                target
                        .getChildAt(target!!.childCount - 1) as MindMapNode
            }

        constructor(node: MindMapNode?, isLeft: Boolean) {
            this.isLeft = isLeft
            val parentNode = node.getParentNode()
            val childPosition = parentNode!!.getChildPosition(node)
            if (childPosition == parentNode!!.childCount - 1) {
                target = parentNode
                asSibling = false
            } else {
                target = parentNode!!.getChildAt(childPosition + 1)
                asSibling = true
            }
        }
    }

    private interface DataFlavorHandler {
        @Throws(UnsupportedFlavorException::class, IOException::class)
        fun paste(TransferData: Any, target: MindMapNode?, asSibling: Boolean,
                  isLeft: Boolean, t: Transferable?)

        val dataFlavor: DataFlavor?
    }

    private inner class FileListFlavorHandler : DataFlavorHandler {
        override fun paste(TransferData: Any, target: MindMapNode?,
                           asSibling: Boolean, isLeft: Boolean, t: Transferable?) {
            // TODO: Does not correctly interpret asSibling.
            val fileList = TransferData as List<File>
            for (file in fileList) {
                val node = exMapFeedback.newNode(file.name,
                        target.getMap())
                node.isLeft = isLeft
                node.link = fileToRelativeUrlString(file,
                        exMapFeedback.map.file)
                insertNodeInto(node as MindMapNodeModel, target, asSibling,
                        isLeft, false)
                // addUndoAction(node);
            }
        }
    }

    private inner class MindMapNodesFlavorHandler : DataFlavorHandler {
        override fun paste(TransferData: Any, target: MindMapNode?,
                           asSibling: Boolean, isLeft: Boolean, t: Transferable?) {
            val textFromClipboard = TransferData as String
            if (textFromClipboard != null) {
                val textLines: Array<String> = textFromClipboard
                        .split(ModeController.Companion.NODESEPARATOR).toTypedArray()
                if (textLines.size > 1) {
                    setWaitingCursor(true)
                }
                // and now? paste it:
                var mapContent: String = (MapAdapter.Companion.MAP_INITIAL_START
                        + FreeMind.XML_VERSION + "\"><node TEXT=\"DUMMY\">")
                for (j in textLines.indices) {
                    mapContent += textLines[j]
                }
                mapContent += "</node></map>"
                // logger.info("Pasting " + mapContent);
                try {
                    val node = exMapFeedback.map.loadTree(
                            StringReaderCreator(
                                    mapContent), MapAdapter.Companion.sDontAskInstance)
                    run {
                        val i: ListIterator<*>? = node!!.childrenUnfolded()
                        while (i!!.hasNext()) {
                            val importNode = i
                                    .next() as MindMapNodeModel
                            insertNodeInto(importNode, target, asSibling, isLeft,
                                    true)
                        }
                    }
                    run {
                        val i: ListIterator<*>? = node!!.childrenUnfolded()
                        while (i!!.hasNext()) {
                            val importNode = i
                                    .next() as MindMapNodeModel
                            exMapFeedback.invokeHooksRecursively(importNode,
                                    exMapFeedback.map)
                        }
                    }
                    val i: ListIterator<*>? = node!!.childrenUnfolded()
                    while (i!!.hasNext()) {
                        val importNode = i
                                .next() as MindMapNodeModel
                        processUnfinishedLinksInHooks(importNode)
                    }
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
            }
        }
    }

    private inner class DirectHtmlFlavorHandler : DataFlavorHandler {
        private var mNodeCreator: NodeCreator

        /**
         * @param pNodeCreator the nodeCreator to set
         */
        fun setNodeCreator(pNodeCreator: NodeCreator) {
            mNodeCreator = pNodeCreator
        }

        /**
         *
         */
        init {
            mNodeCreator = object : NodeCreator {
                override fun createChild(pParent: MindMapNode?): MindMapNode? {
                    val node = exMapFeedback.newNode("",
                            exMapFeedback.map)
                    insertNodeInto(node, pParent)
                    node!!.setParent(pParent)
                    exMapFeedback.nodeChanged(pParent)
                    return node
                }

                override fun setText(pText: String?, pNode: MindMapNode?) {
                    pNode.setText(pText)
                    exMapFeedback.nodeChanged(pNode)
                }

                override fun setLink(pLink: String?, pNode: MindMapNode?) {
                    pNode.setLink(pLink)
                    exMapFeedback.nodeChanged(pNode)
                }
            }
        }

        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun paste(transferData: Any, target: MindMapNode?,
                           asSibling: Boolean, isLeft: Boolean, t: Transferable?) {
            var textFromClipboard = transferData as String
            // workaround for java decoding bug
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6740877
            textFromClipboard = textFromClipboard.replace(65533.toChar(), ' ')
            //			if (textFromClipboard.charAt(0) == 65533) {
//				throw new UnsupportedFlavorException(
//						MindMapNodesSelection.htmlFlavor);
//			}
            // ^ This outputs transfer data to standard output. I don't know
            // why.
            // { Alternative pasting of HTML
            setWaitingCursor(true)
            logger!!.finer("directHtmlFlavor (original): $textFromClipboard")
            textFromClipboard = textFromClipboard
                    .replace("(?i)(?s)<meta[^>]*>".toRegex(), "")
                    .replace("(?i)(?s)<head>.*?</head>".toRegex(), "")
                    .replace("(?i)(?s)</?html[^>]*>".toRegex(), "")
                    .replace("(?i)(?s)</?body[^>]*>".toRegex(), "")
                    .replace("(?i)(?s)<script.*?>.*?</script>".toRegex(), "") // Java HTML Editor
                    // does not like
                    // the tag.
                    .replace("(?i)(?s)</?tbody.*?>".toRegex(), "").replace // Java HTML Editor
            // shows comments in
            // not very nice
            // manner.
            "(?i)(?s)<!--.*?-->".toRegex(), "").replace // Java HTML Editor
            // does not like
            // Microsoft Word's
            // <o> tag.
            "(?i)(?s)</?o[^>]*>".toRegex(), "")
            textFromClipboard = "<html><body>$textFromClipboard</body></html>"
            logger!!.finer("directHtmlFlavor: $textFromClipboard")
            if (Resources.getInstance().getBoolProperty(
                            FreeMind.RESOUCES_PASTE_HTML_STRUCTURE)) {
                HtmlTools.getInstance().insertHtmlIntoNodes(textFromClipboard,
                        target, mNodeCreator)
            } else {
                if (safeEquals(
                                exMapFeedback.getProperty(
                                        "cut_out_pictures_when_pasting_html"), "true")) {
                    textFromClipboard = textFromClipboard.replace(
                            "(?i)(?s)<img[^>]*>".toRegex(), "")
                } // Cut out images.
                textFromClipboard = HtmlTools
                        .unescapeHTMLUnicodeEntity(textFromClipboard)
                val node = exMapFeedback.newNode(textFromClipboard,
                        exMapFeedback.map)
                // if only one <a>...</a> element found, set link
                val m = HREF_PATTERN.matcher(textFromClipboard)
                if (m.matches()) {
                    val body = m.group(2)
                    if (!body.matches(".*<\\s*a.*")) {
                        val href = m.group(1)
                        node.link = href
                    }
                }
                insertNodeInto(node, target)
                // addUndoAction(node);
            }
            setWaitingCursor(false)
        }
    }

    private inner class StringFlavorHandler : DataFlavorHandler {
        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun paste(TransferData: Any, target: MindMapNode?,
                           asSibling: Boolean, isLeft: Boolean, t: Transferable?) {
            pasteStringWithoutRedisplay(t, target, asSibling, isLeft)
        }

        override val dataFlavor: DataFlavor?
            get() = DataFlavor.stringFlavor
    }

    private inner class ImageFlavorHandler : DataFlavorHandler {
        @Throws(UnsupportedFlavorException::class, IOException::class)
        override fun paste(transferData: Any, target: MindMapNode?,
                           asSibling: Boolean, isLeft: Boolean, t: Transferable?) {
            logger!!.info("imageFlavor")
            setWaitingCursor(true)
            val mindmapFile = exMapFeedback.map.file
            val imgfile: String
            if (mindmapFile == null) {
                JOptionPane.showMessageDialog(
                        exMapFeedback.viewAbstraction.selected,
                        exMapFeedback.getResourceString("map_not_saved"),
                        "FreeMind", JOptionPane.ERROR_MESSAGE)
                return
            }
            val parentFile = mindmapFile.parentFile
            var filePrefix = mindmapFile.name.replace(
                    FreeMindCommon.FREEMIND_FILE_EXTENSION, "_")
            /* prefix for createTempFile must be at least three characters long. 
			 See  [bugs:#1261] Unable to paste images from clipboard */while (filePrefix.length < 3) {
                filePrefix += "_"
            }
            val tempFile = File
                    .createTempFile(filePrefix, ".jpeg", parentFile)
            val fos = FileOutputStream(tempFile)
            fos.write(fromBase64(transferData.toString()))
            fos.close()

            // Absolute, if not in the correct directory!
            imgfile = tempFile.name
            logger!!.info("Writing image to $imgfile")
            val strText = ("<html><body><img src=\"" + imgfile
                    + "\"/></body></html>")
            val node = exMapFeedback.newNode(strText,
                    exMapFeedback.map)
            // if only one <a>...</a> element found, set link
            insertNodeInto(node, target)
            // addUndoAction(node);
            setWaitingCursor(false)
        }

        override val dataFlavor: DataFlavor?
            get() = DataFlavor.imageFlavor
    }

    /*
     *
     */
    private fun _paste(t: Transferable?, target: MindMapNode?, asSibling: Boolean,
                       isLeft: Boolean) {
        if (t == null) {
            return
        }
        // Uncomment to print obtained data flavors

        /*
		 * DataFlavor[] fl = t.getTransferDataFlavors(); for (int i = 0; i <
		 * fl.length; i++) { System.out.println(fl[i]); }
		 */
        val dataFlavorHandlerList = flavorHandlers
        for (i in dataFlavorHandlerList.indices) {
            val handler = dataFlavorHandlerList[i]
            val flavor: DataFlavor = handler.getDataFlavor()
            if (t.isDataFlavorSupported(flavor)) {
                try {
                    handler.paste(t.getTransferData(flavor), target, asSibling,
                            isLeft, t)
                    break
                } catch (e: UnsupportedFlavorException) {
                    Resources.getInstance().logException(e)
                } catch (e: IOException) {
                    Resources.getInstance().logException(e)
                }
            }
        }
        setWaitingCursor(false)
    }// %%% Make dependent on an option?: new HtmlFlavorHandler(),

    /**
     */
    private val flavorHandlers: Array<DataFlavorHandler>
        private get() =// %%% Make dependent on an option?: new HtmlFlavorHandler(),
            arrayOf(
                    FileListFlavorHandler(), MindMapNodesFlavorHandler(),
                    DirectHtmlFlavorHandler(), StringFlavorHandler(),
                    ImageFlavorHandler())

    @Throws(XMLParseException::class)
    fun pasteXMLWithoutRedisplay(pasted: String,
                                 target: MindMapNode, asSibling: Boolean, changeSide: Boolean,
                                 isLeft: Boolean, pIDToTarget: HashMap<String?, NodeAdapter?>): MindMapNodeModel? {
        // Call nodeStructureChanged(target) after this function.
        logger!!.fine("Pasting $pasted to $target")
        return try {
            val node = exMapFeedback.map
                    .createNodeTreeFromXml(StringReader(pasted), pIDToTarget) as MindMapNodeModel
            insertNodeInto(node, target, asSibling, isLeft, changeSide)
            exMapFeedback.invokeHooksRecursively(node,
                    exMapFeedback.map)
            processUnfinishedLinksInHooks(node)
            node
        } catch (ee: IOException) {
            Resources.getInstance().logException(ee)
            null
        }
    }

    private fun insertNodeInto(node: MindMapNodeModel?, target: MindMapNode?,
                               asSibling: Boolean, isLeft: Boolean, changeSide: Boolean) {
        val parent: MindMapNode?
        parent = if (asSibling) {
            target.getParentNode()
        } else {
            target
        }
        if (changeSide) {
            node.setParent(parent)
            node.setLeft(isLeft)
        }
        // now, the import is finished. We can inform others about the new
        // nodes:
        if (asSibling) {
            insertNodeInto(node, parent, parent!!.getChildPosition(target))
        } else {
            insertNodeInto(node, target)
        }
    }

    /**
     * @param pMapFeedback
     */
    init {
        if (logger == null) {
            logger = Resources.getInstance().getLogger(
                    this.javaClass.name)
        }
    }

    /**
     * Paste String (as opposed to other flavors)
     *
     * Split the text into lines; determine the new tree structure by the number
     * of leading spaces in lines. In case that trimmed line starts with
     * protocol (http:, https:, ftp:), create a link with the same content.
     *
     * If there was only one line to be pasted, return the pasted node, null
     * otherwise.
     *
     * @param isLeft
     * TODO
     */
    @Throws(UnsupportedFlavorException::class, IOException::class)
    private fun pasteStringWithoutRedisplay(t: Transferable?,
                                            parent: MindMapNode?, asSibling: Boolean, isLeft: Boolean): MindMapNode? {
        var parent = parent
        val textFromClipboard = t
                .getTransferData(DataFlavor.stringFlavor) as String
        val mailPattern = Pattern.compile("([^@ <>\\*']+@[^@ <>\\*']+)")
        val textLines = textFromClipboard.split("\n").toTypedArray()
        if (textLines.size > 1) {
            setWaitingCursor(true)
        }
        if (asSibling) {
            // When pasting as sibling, we use virtual node as parent. When the
            // pasting to
            // virtual node is completed, we insert the children of that virtual
            // node to
            // the parent of real parent.
            parent = MindMapNodeModel(exMapFeedback.map)
        }
        val parentNodes = ArrayList<MindMapNode?>()
        val parentNodesDepths = ArrayList<Int>()
        parentNodes.add(parent)
        parentNodesDepths.add(-1)
        val linkPrefixes = arrayOf("http://", "ftp://", "https://")
        var pastedNode: MindMapNode? = null
        for (i in textLines.indices) {
            var text = textLines[i]
            //			System.out.println("Text to paste: "+text);
            text = text.replace("\t".toRegex(), "        ")
            if (text.matches(" *")) {
                continue
            }
            var depth = 0
            while (depth < text.length && text[depth] == ' ') {
                ++depth
            }
            var visibleText = text.trim { it <= ' ' }

            // If the text is a recognizable link (e.g.
            // http://www.google.com/index.html),
            // make it more readable by look nicer by cutting off obvious prefix
            // and other
            // transforamtions.
            if (visibleText.matches("^http://(www\\.)?[^ ]*$")) {
                visibleText = visibleText.replace("^http://(www\\.)?".toRegex(), "")
                        .replace("(/|\\.[^\\./\\?]*)$".toRegex(), "")
                        .replace("((\\.[^\\./]*\\?)|\\?)[^/]*$".toRegex(), " ? ...")
                        .replace("_|%20".toRegex(), " ")
                val textParts = visibleText.split("/").toTypedArray()
                visibleText = ""
                for (textPartIdx in textParts.indices) {
                    if (textPartIdx > 0) {
                        visibleText += " > "
                    }
                    visibleText += if (textPartIdx == 0) textParts[textPartIdx] else firstLetterCapitalized(textParts[textPartIdx]
                            .replace("^~*".toRegex(), ""))
                }
            }
            val node = exMapFeedback.newNode(visibleText,
                    parent.getMap())
            if (textLines.size == 1) {
                pastedNode = node
            }

            // Heuristically determine, if there is a mail.
            val mailMatcher = mailPattern.matcher(visibleText)
            if (mailMatcher.find()) {
                node.link = "mailto:" + mailMatcher.group()
            }

            // Heuristically determine, if there is a link. Because this is
            // heuristic, it is probable that it can be improved to include
            // some matches or exclude some matches.
            for (j in linkPrefixes.indices) {
                val linkStart = text.indexOf(linkPrefixes[j])
                if (linkStart != -1) {
                    var linkEnd = linkStart
                    while (linkEnd < text.length
                            && !nonLinkCharacter.matcher(
                                    text.substring(linkEnd, linkEnd + 1))
                                    .matches()) {
                        linkEnd++
                    }
                    node.link = text.substring(linkStart, linkEnd)
                }
            }

            // Determine parent among candidate parents
            // Change the array of candidate parents accordingly
            for (j in parentNodes.indices.reversed()) {
                if (depth > parentNodesDepths[j].toInt()) {
                    for (k in j + 1 until parentNodes.size) {
                        if (parentNodes[k].getParentNode() === parent) {
                            // addUndoAction(n);
                        }
                        parentNodes.removeAt(k)
                        parentNodesDepths.removeAt(k)
                    }
                    node.isLeft = isLeft
                    insertNodeInto(node, parentNodes[j])
                    parentNodes.add(node)
                    parentNodesDepths.add(depth)
                    break
                }
            }
        }
        for (k in parentNodes.indices) {
            if (parentNodes[k].getParentNode() === parent) {
                // addUndoAction(n);
            }
        }
        return pastedNode
    }

    /**
     */
    private fun insertNodeInto(node: MindMapNodeModel?, parent: MindMapNode?, i: Int) {
        exMapFeedback.insertNodeInto(node, parent, i)
    }

    private fun insertNodeInto(node: MindMapNode?, parent: MindMapNode?) {
        exMapFeedback.insertNodeInto(node, parent, parent!!.childCount)
    }

    private fun getTransferableContent(t: Transferable?,
                                       pUndoAction: UndoPasteNodeAction?): TransferableContent? {
        var amountAlreadySet = false
        try {
            val trans = TransferableContent()
            if (t!!.isDataFlavorSupported(MindMapNodesSelection.fileListFlavor)) {
                /*
				 * Since the JAXB-generated interface TransferableContent
				 * doesn't supply a setTranserableAsFileList method, we have to
				 * get the fileList, clear it, and then set it to the new value.
				 */
                val fileList = t.getTransferData(MindMapNodesSelection.fileListFlavor) as List<File>
                val iter = fileList.iterator()
                while (iter.hasNext()) {
                    val fileName = iter.next()
                    val transferableFile = TransferableFile()
                    transferableFile.fileName = fileName.absolutePath
                    trans.addTransferableFile(transferableFile)
                }
                if (pUndoAction != null && !amountAlreadySet) {
                    pUndoAction.nodeAmount = fileList.size
                    amountAlreadySet = true
                }
            }
            if (t.isDataFlavorSupported(MindMapNodesSelection.mindMapNodesFlavor)) {
                val textFromClipboard: String
                textFromClipboard = t
                        .getTransferData(MindMapNodesSelection.mindMapNodesFlavor) as String
                trans.transferable = HtmlTools.makeValidXml(textFromClipboard)
                if (pUndoAction != null && !amountAlreadySet) {
                    pUndoAction
                            .nodeAmount = countOccurrences(
                            textFromClipboard,
                            ModeController.Companion.NODESEPARATOR) + 1
                    amountAlreadySet = true
                }
            }
            if (t.isDataFlavorSupported(MindMapNodesSelection.htmlFlavor)) {
                val textFromClipboard: String
                textFromClipboard = t
                        .getTransferData(MindMapNodesSelection.htmlFlavor) as String
                trans.transferableAsHtml = HtmlTools
                        .makeValidXml(textFromClipboard)
                if (pUndoAction != null && !amountAlreadySet) {
                    // on html paste, the string text is taken and "improved".
                    // Thus, we count its lines.
                    try {
                        pUndoAction.nodeAmount = determineAmountOfNewNodes(t)
                        amountAlreadySet = true
                    } catch (e: Exception) {
                        Resources.getInstance().logException(e)
                        // ok, something went wrong, but this breaks undo, only.
                        pUndoAction.nodeAmount = 1
                    }
                }
            }
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                val textFromClipboard: String
                textFromClipboard = t
                        .getTransferData(DataFlavor.stringFlavor) as String
                trans.transferableAsPlainText = HtmlTools
                        .makeValidXml(textFromClipboard)
                if (pUndoAction != null && !amountAlreadySet) {
                    // determine amount of new nodes using the algorithm:
                    val childCount = determineAmountOfNewTextNodes(t)
                    pUndoAction.nodeAmount = childCount
                    amountAlreadySet = true
                }
            }
            if (t.isDataFlavorSupported(MindMapNodesSelection.rtfFlavor)) {
                // byte[] textFromClipboard = (byte[])
                // t.getTransferData(MindMapNodesSelection.rtfFlavor);
                // trans.setTransferableAsRTF(textFromClipboard.toString());
            }
            if (t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                logger!!.info("image...")
                try {
                    // Get data from clipboard and assign it to an image.
                    // clipboard.getData() returns an object, so we need to cast
                    // it to a BufferdImage.
                    val image = t
                            .getTransferData(DataFlavor.imageFlavor) as BufferedImage
                    logger!!.info("Starting to write clipboard image $image")
                    val baos = ByteArrayOutputStream()
                    ImageIO.write(image, "jpg", baos)
                    val base64String = toBase64(baos.toByteArray())
                    trans.transferableAsImage = base64String
                    if (pUndoAction != null && !amountAlreadySet) {
                        pUndoAction.nodeAmount = 1
                        amountAlreadySet = true
                    }
                } // getData throws this.
                catch (ufe: UnsupportedFlavorException) {
                    Resources.getInstance().logException(ufe)
                } catch (ioe: IOException) {
                    Resources.getInstance().logException(ioe)
                }
            }
            return trans
        } catch (e: UnsupportedFlavorException) {
            Resources.getInstance().logException(e)
        } catch (e: IOException) {
            Resources.getInstance().logException(e)
        }
        return null
    }

    /*
	 * TODO: This is a bit dirty here. Better would be to separate the algorithm
	 * from the node creation and use the pure algo.
	 */
    @Throws(UnsupportedFlavorException::class, IOException::class)
    protected fun determineAmountOfNewTextNodes(t: Transferable?): Int {
        // create a new node for testing purposes.
        val parent = MindMapNodeModel(
                exMapFeedback.map)
        pasteStringWithoutRedisplay(t, parent, false, false)
        return parent.childCount
    }

    /**
     * Only for HTML nodes.
     * @param t
     * @return
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    @Throws(UnsupportedFlavorException::class, IOException::class)
    fun determineAmountOfNewNodes(t: Transferable?): Int {
        // create a new node for testing purposes.
        val parent = MindMapNodeModel(
                exMapFeedback.map)
        parent.text = "ROOT"
        val handler = DirectHtmlFlavorHandler()
        // creator, that only creates dummy nodes.
        handler.setNodeCreator(object : NodeCreator {
            override fun createChild(pParent: MindMapNode?): MindMapNode? {
                try {
                    val newNode = MindMapNodeModel("",
                            exMapFeedback.map)
                    pParent!!.insert(newNode, pParent.childCount)
                    newNode.setParent(pParent)
                    return newNode
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
                return null
            }

            override fun setText(pText: String?, pNode: MindMapNode?) {
                pNode.setText(pText)
            }

            override fun setLink(pLink: String?, pNode: MindMapNode?) {}
        })
        handler.paste(t!!.getTransferData(handler.getDataFlavor()), parent, false, true, t)
        return parent.childCount
    }

    private fun getTransferable(trans: TransferableContent?): Transferable {
        // create Transferable:
        // Add file list to this selection.
        val fileList = Vector<File?>()
        val iter: Iterator<TransferableFile> = trans!!.listTransferableFileList.iterator()
        while (iter.hasNext()) {
            val tFile = iter.next()
            fileList.add(File(tFile.fileName))
        }
        return MindMapNodesSelection(trans.transferable!!,
                trans.transferableAsImage!!,
                trans.transferableAsPlainText!!,
                trans.transferableAsRTF, trans.transferableAsHtml,
                trans.transferableAsDrop!!, fileList, null)
    }

    protected fun setWaitingCursor(waitingCursor: Boolean) {
        exMapFeedback.setWaitingCursor(waitingCursor)
    }

    /**
     *
     */
    fun processUnfinishedLinksInHooks(node: MindMapNode?) {
        val i: Iterator<MindMapNode?>? = node!!.childrenUnfolded()
        while (i!!.hasNext()) {
            val child = i.next()
            processUnfinishedLinksInHooks(child)
        }
        for (hook in node.hooks) {
            hook!!.processUnfinishedLinks()
        }
    }

    companion object {
        protected var logger: Logger? = null
        private val HREF_PATTERN = Pattern
                .compile("<html>\\s*<body>\\s*<a\\s+href=\"([^>]+)\">(.*)</a>\\s*</body>\\s*</html>")
        val nonLinkCharacter = Pattern.compile("[ \n()'\",;]")
    }
}