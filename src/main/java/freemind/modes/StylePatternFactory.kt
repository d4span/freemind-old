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
/*$Id: StylePatternFactory.java,v 1.1.2.3.2.5 2007/09/13 20:33:07 christianfoltin Exp $*/
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
import java.util.LinkedList
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
import java.util.Locale
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
import java.util.TimerTask
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
import java.util.Collections
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
import freemind.extensions.MindMapHook
import freemind.extensions.MindMapHook.PluginBaseClassSearcher
import javax.swing.JCheckBoxMenuItem
import freemind.extensions.NodeHookAdapter
import freemind.modes.ExtendedMapFeedback
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
import java.util.TreeSet
import freemind.modes.mindmapmode.actions.xml.actors.AddHookActor
import freemind.extensions.DontSaveMarker
import freemind.modes.ViewAbstraction
import freemind.modes.mindmapmode.actions.xml.ActorXml
import java.lang.NumberFormatException
import freemind.modes.mindmapmode.MindMapEdgeModel
import freemind.modes.MindMapEdge
import freemind.modes.LineAdapter
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
import freemind.modes.mindmapmode.MindMapArrowLinkModel
import freemind.modes.mindmapmode.actions.NodeActorXml
import freemind.modes.MindMapArrowLink
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
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.IdentityTransformer
import freemind.modes.mindmapmode.actions.ApplyPatternAction
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.EdgeWidthBackTransformer
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
import javax.swing.JRadioButtonMenuItem
import freemind.modes.mindmapmode.MindMapController.LinkActionBase
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.BadLocationException
import freemind.extensions.UndoEventReceiver
import freemind.modes.mindmapmode.MindMapController.MapSourceChangeDialog
import freemind.modes.mindmapmode.MindMapHTMLWriter
import javax.swing.ScrollPaneConstants
import javax.swing.tree.MutableTreeNode
import javax.swing.tree.TreeModel
import freemind.controller.filter.util.SortedListModel
import freemind.view.ScalableImageIcon
import java.util.StringTokenizer
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
import java.util.SortedMap
import freemind.modes.HistoryInformation
import java.util.TreeMap
import java.util.Enumeration
import java.lang.NullPointerException
import freemind.modes.CloudAdapter.CloudAdapterListener
import freemind.modes.ModesCreator
import freemind.modes.StylePattern
import freemind.controller.MapModuleManager
import freemind.modes.ModeController.NodeLifetimeListener
import freemind.modes.FreeMindFileDialog.DirectoryResultListener
import freemind.controller.LastStateStorageManagement
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
import freemind.controller.actions.generated.instance.*
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
import java.util.HashMap

/**
 * This class constructs patterns from files or from nodes and saves them back.
 */
object StylePatternFactory {
    const val FALSE_VALUE = "false"
    const val TRUE_VALUE = "true"
    @Throws(Exception::class)
    fun loadPatterns(file: File?): List<Pattern?> {
        return loadPatterns(BufferedReader(FileReader(file)))
    }

    /**
     * @return a List of Pattern elements.
     * @throws Exception
     */
    @Throws(Exception::class)
    fun loadPatterns(reader: Reader?): List<Pattern?> {
        val patterns = XmlBindingTools.getInstance()
                .unMarshall(reader) as Patterns
        // translate standard strings:
        val iterator: Iterator<Pattern> = patterns.listChoiceList.iterator()
        while (iterator.hasNext()) {
            val pattern = iterator.next()
            val originalName = pattern.name
            var name = originalName ?: continue
            // make private:
            name = "__pattern_string_" + name.replace(" ", "_")
            val translatedName: String = Resources.getInstance().getResourceString(
                    name)
            if (!safeEquals(translatedName, name)) {
                // there is a translation:
                pattern.name = translatedName
                // store original name to be able to translate back
                pattern.originalName = originalName
                // look, whether or not the string occurs in other situations:
                val it: Iterator<Pattern> = patterns.listChoiceList.iterator()
                while (it.hasNext()) {
                    val otherPattern = it.next()
                    val child = otherPattern.patternChild
                    if (child != null) {
                        if (safeEquals(originalName, child.value)) {
                            child.value = translatedName
                        }
                    }
                }
            }
        }
        return patterns.listChoiceList
    }

    /**
     * the result is written to, and it is closed afterwards List of Pattern
     * elements.
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun savePatterns(writer: Writer, listOfPatterns: List<Pattern?>?) {
        val patterns = Patterns()
        val nameToPattern = HashMap<String?, Pattern?>()
        for (pattern in listOfPatterns!!) {
            patterns.addChoice(pattern)
            if (pattern!!.originalName != null) {
                nameToPattern[pattern.name] = pattern
                pattern.name = pattern.originalName
                pattern.originalName = null
            }
        }
        val it: Iterator<Pattern> = patterns.listChoiceList.iterator()
        while (it.hasNext()) {
            val pattern = it.next()
            val patternChild = pattern.patternChild
            if (patternChild != null
                    && nameToPattern.containsKey(patternChild.value)) {
                patternChild.value = nameToPattern[patternChild
                        .value]!!.name
            }
        }
        val marshalledResult: String = XmlBindingTools.getInstance().marshall(
                patterns)
        writer.write(marshalledResult)
        writer.close()
    }

    fun createPatternFromNode(node: MindMapNode): Pattern {
        val pattern = Pattern()
        if (node.color != null) {
            val subPattern = PatternNodeColor()
            subPattern.value = colorToXml(node.color)
            pattern.patternNodeColor = subPattern
        }
        if (node.backgroundColor != null) {
            val subPattern = PatternNodeBackgroundColor()
            subPattern.value = colorToXml(node.backgroundColor)
            pattern.patternNodeBackgroundColor = subPattern
        }
        if (node.style != null) {
            val subPattern = PatternNodeStyle()
            subPattern.value = node.style
            pattern.patternNodeStyle = subPattern
        }
        val nodeFontBold = PatternNodeFontBold()
        nodeFontBold.value = if (node.isBold) TRUE_VALUE else FALSE_VALUE
        pattern.patternNodeFontBold = nodeFontBold
        val nodeFontStrikethrough = PatternNodeFontStrikethrough()
        nodeFontStrikethrough.value = if (node.isStrikethrough) TRUE_VALUE else FALSE_VALUE
        pattern.patternNodeFontStrikethrough = nodeFontStrikethrough
        val nodeFontItalic = PatternNodeFontItalic()
        nodeFontItalic.value = if (node.isItalic) TRUE_VALUE else FALSE_VALUE
        pattern.patternNodeFontItalic = nodeFontItalic
        if (node.fontSize != null) {
            val nodeFontSize = PatternNodeFontSize()
            nodeFontSize.value = node.fontSize
            pattern.patternNodeFontSize = nodeFontSize
        }
        if (node.fontFamilyName != null) {
            val subPattern = PatternNodeFontName()
            subPattern.value = node.fontFamilyName
            pattern.patternNodeFontName = subPattern
        }
        if (node.icons.size == 1) {
            val iconPattern = PatternIcon()
            iconPattern.value = (node.icons[0] as MindIcon).name
            pattern.patternIcon = iconPattern
        }
        if (node.edge.color != null) {
            val subPattern = PatternEdgeColor()
            subPattern.value = colorToXml(node.edge.color)
            pattern.patternEdgeColor = subPattern
        }
        if (node.edge.style != null) {
            val subPattern = PatternEdgeStyle()
            subPattern.value = node.edge.style
            pattern.patternEdgeStyle = subPattern
        }
        if (node.edge.width != LineAdapter.Companion.DEFAULT_WIDTH) {
            val subPattern = PatternEdgeWidth()
            subPattern.value = "" + node.edge.width
            pattern.patternEdgeWidth = subPattern
        }
        return pattern
    }

    fun toString(pPattern: Pattern, translator: TextTranslator): String {
        var result = ""
        if (pPattern.patternNodeColor != null) {
            result = addSeparatorIfNecessary(result)
            result += if (pPattern.patternNodeColor!!.value == null) {
                "-" + translator.getText("PatternToString.color")
            } else {
                "+" + translator.getText("PatternToString.color")
            }
        }
        if (pPattern.patternNodeBackgroundColor != null) {
            result = addSeparatorIfNecessary(result)
            result += if (pPattern.patternNodeBackgroundColor!!.value == null) {
                ("-"
                        + translator.getText("PatternToString.backgroundColor"))
            } else {
                ("+"
                        + translator.getText("PatternToString.backgroundColor"))
            }
        }
        result = addSubPatternToString(translator, result,
                pPattern.patternNodeFontSize,
                "PatternToString.NodeFontSize")
        result = addSubPatternToString(translator, result,
                pPattern.patternNodeFontName, "PatternToString.FontName")
        result = addSubPatternToString(translator, result,
                pPattern.patternNodeFontBold, "PatternToString.FontBold")
        result = addSubPatternToString(translator, result,
                pPattern.patternNodeFontStrikethrough, "PatternToString.FontStrikethrough")
        result = addSubPatternToString(translator, result,
                pPattern.patternNodeFontItalic,
                "PatternToString.FontItalic")
        result = addSubPatternToString(translator, result,
                pPattern.patternEdgeStyle, "PatternToString.EdgeStyle")
        result = addSubPatternToString(translator, result,
                pPattern.patternEdgeColor, "PatternToString.EdgeColor")
        result = addSubPatternToString(translator, result,
                pPattern.patternEdgeWidth, "PatternToString.EdgeWidth")
        result = addSubPatternToString(translator, result,
                pPattern.patternIcon, "PatternToString.Icon")
        result = addSubPatternToString(translator, result,
                pPattern.patternChild, "PatternToString.Child")
        return result
    }

    private fun addSubPatternToString(translator: TextTranslator,
                                      result: String, patternType: PatternPropertyBase?, patternString: String): String {
        var result = result
        if (patternType != null) {
            result = addSeparatorIfNecessary(result)
            result += if (patternType.value == null) {
                "-" + translator.getText(patternString)
            } else {
                ("+" + translator.getText(patternString) + " "
                        + patternType.value)
            }
        }
        return result
    }

    private fun addSeparatorIfNecessary(result: String): String {
        var result = result
        if (result.length > 0) {
            result += ", "
        }
        return result
    }

    private const val PATTERN_DUMMY = "<pattern name='dummy'/>"
    fun getPatternFromString(pattern: String?): Pattern {
        var patternString = pattern
        if (patternString == null) {
            patternString = PATTERN_DUMMY
        }
        return XmlBindingTools.getInstance().unMarshall(
                patternString)
    }

    private const val PATTERNS_DUMMY = "<patterns/>"
    fun getPatternsFromString(patterns: String?): Patterns {
        var patternsString = patterns
        if (patternsString == null) {
            patternsString = PATTERNS_DUMMY
        }
        return XmlBindingTools.getInstance().unMarshall(
                patternsString)
    }

    /**
     * Build the intersection of two patterns. Only, if the property is the
     * same, or both properties are to be removed, it is kept, otherwise it is
     * set to 'don't touch'.
     */
    fun intersectPattern(p1: Pattern, p2: Pattern): Pattern {
        val result = Pattern()
        result.patternEdgeColor = processPatternProperties(
                p1.patternEdgeColor, p2.patternEdgeColor,
                PatternEdgeColor()) as PatternEdgeColor?
        result.patternEdgeStyle = processPatternProperties(
                p1.patternEdgeStyle, p2.patternEdgeStyle,
                PatternEdgeStyle()) as PatternEdgeStyle?
        result.patternEdgeWidth = processPatternProperties(
                p1.patternEdgeWidth, p2.patternEdgeWidth,
                PatternEdgeWidth()) as PatternEdgeWidth?
        result.patternIcon = processPatternProperties(
                p1.patternIcon, p2.patternIcon, PatternIcon()) as PatternIcon?
        result.patternNodeBackgroundColor = processPatternProperties(
                p1.patternNodeBackgroundColor,
                p2.patternNodeBackgroundColor,
                PatternNodeBackgroundColor()) as PatternNodeBackgroundColor?
        result.patternNodeColor = processPatternProperties(
                p1.patternNodeColor, p2.patternNodeColor,
                PatternNodeColor()) as PatternNodeColor?
        result.patternNodeFontBold = processPatternProperties(
                p1.patternNodeFontBold, p2.patternNodeFontBold,
                PatternNodeFontBold()) as PatternNodeFontBold?
        result.patternNodeFontStrikethrough = processPatternProperties(
                p1.patternNodeFontStrikethrough, p2.patternNodeFontStrikethrough,
                PatternNodeFontStrikethrough()) as PatternNodeFontStrikethrough?
        result.patternNodeFontItalic = processPatternProperties(
                p1.patternNodeFontItalic, p2.patternNodeFontItalic,
                PatternNodeFontItalic()) as PatternNodeFontItalic?
        result.patternNodeFontName = processPatternProperties(
                p1.patternNodeFontName, p2.patternNodeFontName,
                PatternNodeFontName()) as PatternNodeFontName?
        result.patternNodeFontSize = processPatternProperties(
                p1.patternNodeFontSize, p2.patternNodeFontSize,
                PatternNodeFontSize()) as PatternNodeFontSize?
        result.patternNodeStyle = processPatternProperties(
                p1.patternNodeStyle, p2.patternNodeStyle,
                PatternNodeStyle()) as PatternNodeStyle?
        return result
    }

    private fun processPatternProperties(
            prop1: PatternPropertyBase?, prop2: PatternPropertyBase?,
            destination: PatternPropertyBase): PatternPropertyBase? {
        if (prop1 == null || prop2 == null) {
            return null
        }
        // both delete the value or both have the same value:
        if (safeEquals(prop1.value, prop2.value)) {
            destination.value = prop1.value
            return destination
        }
        return null
    }

    fun createPatternFromSelected(focussed: MindMapNode,
                                  selected: List<MindMapNode>): Pattern {
        var nodePattern = createPatternFromNode(focussed)
        for (node in selected) {
            val tempNodePattern = createPatternFromNode(node)
            nodePattern = intersectPattern(nodePattern, tempNodePattern)
        }
        return nodePattern
    }

    /**
     * @return a pattern, that removes all properties of a node to its defaults.
     */
    val removeAllPattern: Pattern
        get() {
            val result = Pattern()
            result.patternEdgeColor = PatternEdgeColor()
            result.patternEdgeStyle = PatternEdgeStyle()
            result.patternEdgeWidth = PatternEdgeWidth()
            result.patternIcon = PatternIcon()
            result.patternNodeBackgroundColor = PatternNodeBackgroundColor()
            result.patternNodeColor = PatternNodeColor()
            result.patternNodeFontBold = PatternNodeFontBold()
            result.patternNodeFontStrikethrough = PatternNodeFontStrikethrough()
            result.patternNodeFontItalic = PatternNodeFontItalic()
            result.patternNodeFontName = PatternNodeFontName()
            result.patternNodeFontSize = PatternNodeFontSize()
            result.patternNodeStyle = PatternNodeStyle()
            return result
        }

    @Deprecated("")
    fun applyPattern(pattern: Pattern, pNode: MindMapNode, pFeedback: MapFeedback) {
        if (pattern.patternNodeColor != null) {
            pNode.color = xmlToColor(pattern
                    .patternNodeColor!!.value)
        }
        if (pattern.patternNodeBackgroundColor != null) {
            pNode.backgroundColor = xmlToColor(pattern
                    .patternNodeBackgroundColor!!
                    .value)
        }
        if (pattern.patternNodeStyle != null) {
            pNode.style = pattern.patternNodeStyle!!
                    .value
        }
        if (pattern.patternEdgeColor != null) {
            (pNode.edge as EdgeAdapter).setColor(
                    xmlToColor(pattern.patternEdgeColor!!
                            .value))
        }
        if (pattern.patternNodeText != null) {
            if (pattern.patternNodeText!!.value != null) {
                pNode.text = pattern.patternNodeText!!.value
            } else {
                // clear text:
                pNode.text = ""
            }
        }
        if (pattern.patternIcon != null) {
            val iconName = pattern.patternIcon!!.value
            if (iconName == null) {
                while (pNode.icons.size > 0 && pNode.removeIcon(0) > 0) {
                }
            } else {
                // check if icon is already present:
                val icons = pNode.icons
                var found = false
                for (icon in icons!!) {
                    if (icon!!.name != null
                            && icon.name == iconName) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    pNode.addIcon(MindIcon.Companion.factory(iconName), pNode.icons.size)
                }
            }
        }
        if (pattern.patternNodeFontName != null) {
            val nodeFontFamily = pattern.patternNodeFontName!!.value
            if (nodeFontFamily == null) {
                pNode.font = pFeedback.defaultFont
            } else {
                (pNode as NodeAdapter).establishOwnFont()
                pNode.setFont(pFeedback.getFontThroughMap(
                        Font(nodeFontFamily, pNode.getFont().style, pNode
                                .getFont().size)))
            }
        }
        if (pattern.patternNodeFontSize != null) {
            val nodeFontSize = pattern.patternNodeFontSize!!.value
            if (nodeFontSize == null) {
                pNode.setFontSize(pFeedback.defaultFont.size)
            } else {
                try {
                    pNode.setFontSize(nodeFontSize.toInt())
                } catch (e: Exception) {
                    Resources.getInstance()
                            .logException(e)
                }
            }
        }
        if (pattern.patternNodeFontItalic != null) {
            (pNode as NodeAdapter).isItalic = TRUE_VALUE == pattern.patternNodeFontItalic!!
                    .value
        }
        if (pattern.patternNodeFontBold != null) {
            (pNode as NodeAdapter).isBold = TRUE_VALUE == pattern.patternNodeFontBold!!.value
        }
        if (pattern.patternNodeFontStrikethrough != null) {
            (pNode as NodeAdapter).isStrikethrough = TRUE_VALUE == pattern.patternNodeFontStrikethrough!!.value
        }
        if (pattern.patternEdgeStyle != null) {
            (pNode.edge as EdgeAdapter).setStyle(pattern.patternEdgeStyle!!.value)
        }
        val patternEdgeWidth = pattern.patternEdgeWidth
        if (patternEdgeWidth != null) {
            if (patternEdgeWidth.value != null) {
                (pNode.edge as EdgeAdapter).setWidth(edgeWidthStringToInt(patternEdgeWidth.value))
            } else {
                (pNode.edge as EdgeAdapter).setWidth(LineAdapter.Companion.DEFAULT_WIDTH)
            }
        }
    }

    fun applyPattern(node: MindMapNode, pattern: Pattern?,
                     pPatternList: List<Pattern?>?, pPlugins: Set<MindMapControllerPlugin?>?, pMapFeedback: ExtendedMapFeedback?) {
        if (pattern!!.patternNodeText != null) {
            if (pattern.patternNodeText!!.value != null) {
                pMapFeedback!!.setNodeText(node, pattern.patternNodeText!!
                        .value)
            } else {
                // clear text:
                pMapFeedback!!.setNodeText(node, "")
            }
        }
        if (pattern.patternNodeColor != null) {
            pMapFeedback!!.setNodeColor(node,
                    xmlToColor(pattern.patternNodeColor!!.value))
        }
        if (pattern.patternNodeBackgroundColor != null) {
            pMapFeedback!!.setNodeBackgroundColor(node, xmlToColor(pattern
                    .patternNodeBackgroundColor!!.value))
        }
        // Perhaps already fixed?:
        // FIXME: fc, 3.1.2004: setting the style to "null" causes strange
        // behaviour.
        // see
        // https://sourceforge.net/tracker/?func=detail&atid=107118&aid=1094623&group_id=7118
        if (pattern.patternNodeStyle != null) {
            pMapFeedback!!.setNodeStyle(node, pattern.patternNodeStyle!!
                    .value)
        }
        if (pattern.patternIcon != null) {
            val iconName = pattern.patternIcon!!.value
            if (iconName == null) {
                while (pMapFeedback!!.removeLastIcon(node) > 0) {
                }
            } else {
                // check if icon is already present:
                val icons = node.icons
                var found = false
                for (icon in icons!!) {
                    if (icon!!.name != null
                            && icon.name == iconName) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    pMapFeedback!!.addIcon(node, MindIcon.Companion.factory(iconName))
                }
            }
        } // fc, 28.9.2003
        if (pattern.patternNodeFontName != null) {
            var nodeFontFamily = pattern.patternNodeFontName!!.value
            if (nodeFontFamily == null) {
                nodeFontFamily = pMapFeedback.getDefaultFont().family
            }
            pMapFeedback!!.setFontFamily(node, nodeFontFamily)
        }
        if (pattern.patternNodeFontSize != null) {
            var nodeFontSize = pattern.patternNodeFontSize!!.value
            if (nodeFontSize == null) {
                nodeFontSize = "" + pMapFeedback.getDefaultFont().size
            }
            pMapFeedback!!.setFontSize(node, nodeFontSize.toString())
        }
        if (pattern.patternNodeFontItalic != null) {
            pMapFeedback!!.setItalic(node, TRUE_VALUE == pattern
                    .patternNodeFontItalic!!.value)
        }
        if (pattern.patternNodeFontBold != null) {
            pMapFeedback!!.setBold(node, TRUE_VALUE == pattern.patternNodeFontBold!!.value)
        }
        if (pattern.patternNodeFontStrikethrough != null) {
            pMapFeedback!!.setStrikethrough(node, TRUE_VALUE == pattern.patternNodeFontStrikethrough!!.value)
        }
        if (pattern.patternEdgeColor != null) {
            pMapFeedback!!.setEdgeColor(node,
                    xmlToColor(pattern.patternEdgeColor!!.value))
        }
        if (pattern.patternEdgeStyle != null) {
            pMapFeedback!!.setEdgeStyle(node, pattern.patternEdgeStyle!!
                    .value)
        }
        val patternEdgeWidth = pattern.patternEdgeWidth
        if (patternEdgeWidth != null) {
            if (patternEdgeWidth.value != null) {
                pMapFeedback
                        .setEdgeWidth(node, edgeWidthStringToInt(patternEdgeWidth
                                .value))
            } else {
                pMapFeedback!!.setEdgeWidth(node, LineAdapter.Companion.DEFAULT_WIDTH)
            }
        }
        if (pattern.patternChild != null
                && pattern.patternChild!!.value != null) {
            // find children among all patterns:
            val searchedPatternName = pattern.patternChild!!.value
            for (otherPattern in pPatternList!!) {
                if (otherPattern!!.name == searchedPatternName) {
                    val j: ListIterator<*>? = node.childrenUnfolded()
                    while (j!!.hasNext()) {
                        val child = j.next() as NodeAdapter
                        applyPattern(child, otherPattern, pPatternList, pPlugins, pMapFeedback)
                    }
                    break
                }
            }
        }
        for (plugin in pPlugins!!) {
            if (plugin is ExternalPatternAction) {
                plugin.act(node, pattern)
            }
        }
    }
}