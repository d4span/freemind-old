/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 25.02.2006
 */
package freemind.modes.mindmapmode.dialogs

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
import freemind.main.HtmlTools
import javax.swing.JDialog
import javax.swing.JCheckBox
import javax.swing.JTextField
import java.awt.event.ActionEvent
import javax.swing.JOptionPane
import freemind.main.FreeMind
import freemind.main.Tools
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
import javax.swing.JFrame
import freemind.modes.IconInformation
import freemind.main.FreeMindMain
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
import freemind.main.XMLElement
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
import java.io.IOException
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
import java.io.FileNotFoundException
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
import freemind.main.FreeMindCommon
import java.io.FileOutputStream
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.FileListFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.MindMapNodesFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.DirectHtmlFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.StringFlavorHandler
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.ImageFlavorHandler
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.FileReader
import java.io.FileWriter
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
import java.io.BufferedReader
import java.beans.PropertyChangeListener
import java.awt.CardLayout
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.common.*
import java.beans.PropertyChangeEvent
import freemind.modes.mindmapmode.dialogs.StylePatternFrame
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.StylePatternFrameType
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
import java.io.BufferedWriter
import java.io.OutputStreamWriter
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
import java.io.FilenameFilter
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
import freemind.main.FixedHTMLWriter
import javax.swing.text.BadLocationException
import freemind.extensions.UndoEventReceiver
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
import freemind.modes.ExtendedMapFeedbackAdapter.DummyTransferable
import java.util.*

/**
 * @author foltin
 */
open class StylePatternFrame
/**
 * @throws HeadlessException
 */// child pattern
(private val mTranslator: TextTranslator,
 private val mMindMapController: MindMapController, private val mType: StylePatternFrameType) : JPanel(), TextTranslator, PropertyChangeListener {
    object StylePatternFrameType {
        var WITHOUT_NAME_AND_CHILDS: StylePatternFrameType = StylePatternFrameType()
        var WITH_NAME_AND_CHILDS: StylePatternFrameType = StylePatternFrameType()
    }

    private var mControls: Vector<PropertyControl?>? = null
    private var mSetNodeColor: ThreeCheckBoxProperty? = null
    private var mNodeColor: ColorProperty? = null
    private var mSetNodeBackgroundColor: ThreeCheckBoxProperty? = null
    private var mNodeBackgroundColor: ColorProperty? = null
    private var mSetNodeStyle: ThreeCheckBoxProperty? = null
    private var mNodeStyle: ComboProperty? = null
    private var mSetNodeFontName: ThreeCheckBoxProperty? = null
    private var mNodeFontName: FontProperty? = null
    private var mSetNodeFontBold: ThreeCheckBoxProperty? = null
    private var mNodeFontBold: BooleanProperty? = null
    private var mSetNodeFontStrikethrough: ThreeCheckBoxProperty? = null
    private var mNodeFontStrikethrough: BooleanProperty? = null
    private var mSetNodeFontItalic: ThreeCheckBoxProperty? = null
    private var mNodeFontItalic: BooleanProperty? = null
    private var mSetNodeFontSize: ThreeCheckBoxProperty? = null
    private var mNodeFontSize: ComboProperty? = null
    private var mSetNodeText: ThreeCheckBoxProperty? = null
    private var mNodeText: StringProperty? = null
    private var mSetEdgeWidth: ThreeCheckBoxProperty? = null
    private var mEdgeWidth: ComboProperty? = null
    private var mSetEdgeStyle: ThreeCheckBoxProperty? = null
    private var mEdgeStyle: ComboProperty? = null
    private var mSetEdgeColor: ThreeCheckBoxProperty? = null
    private var mEdgeColor: ColorProperty? = null
    private var mSetIcon: ThreeCheckBoxProperty? = null
    private var mIcon: IconProperty? = null
    private var mSetChildPattern: ThreeCheckBoxProperty? = null
    private var mChildPattern: ComboProperty? = null
    private var mSetScriptPattern: ThreeCheckBoxProperty? = null
    private var mScriptPattern: ScriptEditorProperty? = null
    private var mName: StringProperty? = null
    private var mIconInformationVector: Vector<MindIcon?>? = null

    /**
     * Denotes pairs property -> ThreeCheckBoxProperty such that the boolean
     * property can be set, when the format property is changed.
     */
    private val mPropertyChangePropagation = HashMap<ThreeCheckBoxProperty?, PropertyControl?>()
    private var mClearSetters: ThreeCheckBoxProperty? = null

    /**
     * Creates all controls and adds them to the frame.
     */
    fun init() {
        val cardLayout = CardLayout()
        val rightStack = JPanel(cardLayout)
        val form = "right:max(40dlu;p), 4dlu, 20dlu, 7dlu,right:max(40dlu;p), 4dlu, 80dlu, 7dlu"
        val rightLayout = FormLayout(form, "")
        val rightBuilder = DefaultFormBuilder(rightLayout)
        rightBuilder.setDefaultDialogBorder()
        mControls = controls
        for (control in mControls!!) {
            control!!.layout(rightBuilder, this)
        }
        // add the last one, too
        rightStack.add(rightBuilder.panel, "testTab")
        add(rightStack, BorderLayout.CENTER)
    }

    fun addListeners() {
        // add listeners:
        for (control in mControls!!) {
            if (control is PropertyBean) {
                val bean = control as PropertyBean
                bean.addPropertyChangeListener(this)
            }
        }
        mClearSetters!!.addPropertyChangeListener {
            for (booleanProp in mPropertyChangePropagation.keys) {
                booleanProp!!.value = mClearSetters!!.value
            }
        }
    }

    private val sizes = arrayOf("2", "4", "6", "8", "10", "12",
            "14", "16", "18", "20", "22", "24", "30", "36", "48", "72")
    private var mPatternList: List<Pattern>? = null

    // child pattern
    /* **** */
    /* **** */
    /* **** */
    /* **** */
    /* **** */
    // fill map;
    private val controls: Vector<PropertyControl?>
        private get() {
            val controls = Vector<PropertyControl?>()
            controls.add(SeparatorProperty("General"))
            mClearSetters = ThreeCheckBoxProperty(CLEAR_ALL_SETTERS
                    + ".tooltip", CLEAR_ALL_SETTERS)
            mClearSetters!!.value = ThreeCheckBoxProperty.TRUE_VALUE
            controls.add(mClearSetters)
            if (StylePatternFrameType.WITH_NAME_AND_CHILDS == mType) {
                mName = StringProperty(NODE_NAME + ".tooltip", NODE_NAME)
                controls.add(mName)
                // child pattern
                mSetChildPattern = ThreeCheckBoxProperty(SET_CHILD_PATTERN
                        + ".tooltip", SET_CHILD_PATTERN)
                controls.add(mSetChildPattern)
                val childNames = Vector<String?>()
                mChildPattern = ComboProperty(CHILD_PATTERN + ".tooltip",
                        CHILD_PATTERN, childNames, childNames)
                controls.add(mChildPattern)
            }
            controls.add(NextLineProperty())
            controls.add(SeparatorProperty("NodeColors"))
            mSetNodeColor = ThreeCheckBoxProperty(SET_NODE_COLOR + ".tooltip",
                    SET_NODE_COLOR)
            controls.add(mSetNodeColor)
            val fmMain = mMindMapController.frame as FreeMind
            mNodeColor = ColorProperty(NODE_COLOR + ".tooltip", NODE_COLOR,
                    fmMain.getDefaultProperty(FreeMind.RESOURCES_NODE_TEXT_COLOR),
                    this)
            controls.add(mNodeColor)
            mSetNodeBackgroundColor = ThreeCheckBoxProperty(
                    SET_NODE_BACKGROUND_COLOR + ".tooltip",
                    SET_NODE_BACKGROUND_COLOR)
            controls.add(mSetNodeBackgroundColor)
            mNodeBackgroundColor = ColorProperty(NODE_BACKGROUND_COLOR
                    + ".tooltip", NODE_BACKGROUND_COLOR,
                    fmMain.getDefaultProperty(FreeMind.RESOURCES_BACKGROUND_COLOR),
                    this)
            controls.add(mNodeBackgroundColor)
            controls.add(SeparatorProperty("NodeStyles"))
            mSetNodeStyle = ThreeCheckBoxProperty(SET_NODE_STYLE + ".tooltip",
                    SET_NODE_STYLE)
            controls.add(mSetNodeStyle)
            mNodeStyle = ComboProperty(NODE_STYLE + ".tooltip", NODE_STYLE,
                    MindMapNode.Companion.NODE_STYLES, this)
            controls.add(mNodeStyle)
            mIconInformationVector = Vector()
            val controller = mMindMapController
            val iconActions = controller.iconActions
            for (action in iconActions!!) {
                val info = action.mindIcon
                mIconInformationVector!!.add(info)
            }
            mSetIcon = ThreeCheckBoxProperty(SET_ICON + ".tooltip", SET_ICON)
            controls.add(mSetIcon)
            mIcon = IconProperty(ICON + ".tooltip", ICON,
                    mMindMapController.frame, mIconInformationVector)
            controls.add(mIcon)
            controls.add(NextLineProperty())
            controls.add(SeparatorProperty("NodeFont"))
            mSetNodeFontName = ThreeCheckBoxProperty(SET_NODE_FONT_NAME
                    + ".tooltip", SET_NODE_FONT_NAME)
            controls.add(mSetNodeFontName)
            mNodeFontName = FontProperty(NODE_FONT_NAME + ".tooltip",
                    NODE_FONT_NAME, this)
            controls.add(mNodeFontName)
            mSetNodeFontSize = ThreeCheckBoxProperty(SET_NODE_FONT_SIZE
                    + ".tooltip", SET_NODE_FONT_SIZE)
            controls.add(mSetNodeFontSize)
            val sizesVector = Vector<String>()
            for (i in sizes.indices) {
                sizesVector.add(sizes[i])
            }
            mNodeFontSize = IntegerComboProperty(NODE_FONT_SIZE + ".tooltip",
                    NODE_FONT_SIZE, sizes, sizesVector)
            controls.add(mNodeFontSize)
            mSetNodeFontBold = ThreeCheckBoxProperty(SET_NODE_FONT_BOLD
                    + ".tooltip", SET_NODE_FONT_BOLD)
            controls.add(mSetNodeFontBold)
            mNodeFontBold = BooleanProperty(NODE_FONT_BOLD + ".tooltip",
                    NODE_FONT_BOLD)
            controls.add(mNodeFontBold)
            mSetNodeFontStrikethrough = ThreeCheckBoxProperty(SET_NODE_FONT_STRIKETHROUGH
                    + ".tooltip", SET_NODE_FONT_STRIKETHROUGH)
            controls.add(mSetNodeFontStrikethrough)
            mNodeFontStrikethrough = BooleanProperty(NODE_FONT_STRIKETHROUGH + ".tooltip",
                    NODE_FONT_STRIKETHROUGH)
            controls.add(mNodeFontStrikethrough)
            mSetNodeFontItalic = ThreeCheckBoxProperty(SET_NODE_FONT_ITALIC
                    + ".tooltip", SET_NODE_FONT_ITALIC)
            controls.add(mSetNodeFontItalic)
            mNodeFontItalic = BooleanProperty(NODE_FONT_ITALIC + ".tooltip",
                    NODE_FONT_ITALIC)
            controls.add(mNodeFontItalic)
            /* **** */mSetNodeText = ThreeCheckBoxProperty(SET_NODE_TEXT + ".tooltip",
                    SET_NODE_TEXT)
            controls.add(mSetNodeText)
            mNodeText = StringProperty(NODE_TEXT + ".tooltip", NODE_TEXT)
            controls.add(mNodeText)
            /* **** */controls.add(SeparatorProperty("EdgeControls"))
            mSetEdgeWidth = ThreeCheckBoxProperty(SET_EDGE_WIDTH + ".tooltip",
                    SET_EDGE_WIDTH)
            controls.add(mSetEdgeWidth)
            mEdgeWidth = ComboProperty(EDGE_WIDTH + ".tooltip", EDGE_WIDTH,
                    EDGE_WIDTHS, this)
            controls.add(mEdgeWidth)
            /* **** */mSetEdgeStyle = ThreeCheckBoxProperty(SET_EDGE_STYLE + ".tooltip",
                    SET_EDGE_STYLE)
            controls.add(mSetEdgeStyle)
            mEdgeStyle = ComboProperty(EDGE_STYLE + ".tooltip", EDGE_STYLE,
                    EDGE_STYLES, this)
            controls.add(mEdgeStyle)
            /* **** */mSetEdgeColor = ThreeCheckBoxProperty(SET_EDGE_COLOR + ".tooltip",
                    SET_EDGE_COLOR)
            controls.add(mSetEdgeColor)
            mEdgeColor = ColorProperty(EDGE_COLOR + ".tooltip", EDGE_COLOR,
                    fmMain.getDefaultProperty(FreeMind.RESOURCES_EDGE_COLOR), this)
            controls.add(mEdgeColor)
            /* **** */controls.add(SeparatorProperty("ScriptingControl"))
            mSetScriptPattern = ThreeCheckBoxProperty(SET_SCRIPT + ".tooltip",
                    SET_SCRIPT)
            controls.add(mSetScriptPattern)
            mScriptPattern = ScriptEditorProperty(SCRIPT + ".tooltip", SCRIPT,
                    mMindMapController)
            controls.add(mScriptPattern)
            // fill map;
            mPropertyChangePropagation[mSetNodeColor] = mNodeColor
            mPropertyChangePropagation[mSetNodeBackgroundColor] = mNodeBackgroundColor
            mPropertyChangePropagation[mSetNodeStyle] = mNodeStyle
            mPropertyChangePropagation[mSetNodeFontName] = mNodeFontName
            mPropertyChangePropagation[mSetNodeFontSize] = mNodeFontSize
            mPropertyChangePropagation[mSetNodeFontBold] = mNodeFontBold
            mPropertyChangePropagation[mSetNodeFontStrikethrough] = mNodeFontStrikethrough
            mPropertyChangePropagation[mSetNodeFontItalic] = mNodeFontItalic
            mPropertyChangePropagation[mSetNodeText] = mNodeText
            mPropertyChangePropagation[mSetEdgeColor] = mEdgeColor
            mPropertyChangePropagation[mSetEdgeStyle] = mEdgeStyle
            mPropertyChangePropagation[mSetEdgeWidth] = mEdgeWidth
            mPropertyChangePropagation[mSetIcon] = mIcon
            mPropertyChangePropagation[mSetScriptPattern] = mScriptPattern
            if (StylePatternFrameType.WITH_NAME_AND_CHILDS == mType) {
                // child pattern
                mPropertyChangePropagation[mSetChildPattern] = mChildPattern
            }
            return controls
        }
    private val patternNames: Vector<String?>
        private get() {
            val childNames = Vector<String?>()
            for (pattern in mPatternList!!) {
                childNames.add(pattern.name)
            }
            return childNames
        }

    override fun getText(pKey: String?): String {
        return mTranslator.getText("PatternDialog.$pKey")
    }

    fun setPattern(pattern: Pattern) {
        val fmMain = mMindMapController.frame as FreeMind
        setPatternControls(pattern.patternNodeColor, mSetNodeColor,
                mNodeColor,
                fmMain.getDefaultProperty(FreeMind.RESOURCES_NODE_TEXT_COLOR))
        setPatternControls(pattern.patternNodeBackgroundColor,
                mSetNodeBackgroundColor, mNodeBackgroundColor,
                fmMain.getDefaultProperty(FreeMind.RESOURCES_BACKGROUND_COLOR))
        setPatternControls(pattern.patternNodeStyle, mSetNodeStyle,
                mNodeStyle, MindMapNode.Companion.STYLE_AS_PARENT)
        setPatternControls(pattern.patternNodeText, mSetNodeText,
                mNodeText, "")
        setPatternControls(pattern.patternEdgeColor, mSetEdgeColor,
                mEdgeColor,
                fmMain.getDefaultProperty(FreeMind.RESOURCES_EDGE_COLOR))
        setPatternControls(pattern.patternEdgeStyle, mSetEdgeStyle,
                mEdgeStyle, EDGE_STYLES[0])
        setPatternControls(pattern.patternEdgeWidth, mSetEdgeWidth,
                mEdgeWidth, EDGE_WIDTHS[0], EdgeWidthTransformer())
        setPatternControls(pattern.patternNodeFontName, mSetNodeFontName,
                mNodeFontName, mMindMapController.controller
                .defaultFontFamilyName)
        setPatternControls(pattern.patternNodeFontSize, mSetNodeFontSize,
                mNodeFontSize, sizes[0])
        setPatternControls(pattern.patternNodeFontBold, mSetNodeFontBold,
                mNodeFontBold, BooleanProperty.TRUE_VALUE)
        setPatternControls(pattern.patternNodeFontStrikethrough, mSetNodeFontStrikethrough,
                mNodeFontStrikethrough, BooleanProperty.TRUE_VALUE)
        setPatternControls(pattern.patternNodeFontItalic,
                mSetNodeFontItalic, mNodeFontItalic, BooleanProperty.TRUE_VALUE)
        setPatternControls(pattern.patternIcon, mSetIcon, mIcon,
                mIconInformationVector!![0]!!.name)
        setPatternControls(pattern.patternScript, mSetScriptPattern,
                mScriptPattern, "")
        if (StylePatternFrameType.WITH_NAME_AND_CHILDS == mType) {
            mName!!.value = pattern.name
            setPatternControls(
                    pattern.patternChild,
                    mSetChildPattern,
                    mChildPattern,
                    if (mPatternList!!.size > 0) mPatternList!![0]
                            .name else null)
        }
        for (prop in mPropertyChangePropagation.keys) {
            propertyChange(PropertyChangeEvent(prop, prop!!.label, null,
                    prop.value))
        }
    }

    private interface ValueTransformator {
        fun transform(value: String?): String?
    }

    private inner class IdentityTransformer : ValueTransformator {
        override fun transform(value: String?): String? {
            return value
        }
    }

    private inner class EdgeWidthTransformer : ValueTransformator {
        override fun transform(value: String?): String? {
            return transformEdgeWidth(value)
        }
    }

    private inner class EdgeWidthBackTransformer : ValueTransformator {
        override fun transform(value: String?): String? {
            return transformStringToWidth(value)
        }
    }

    private fun setPatternControls(patternProperty: PatternPropertyBase?,
                                   threeCheckBoxProperty: PropertyBean?, property: PropertyBean?,
                                   defaultValue: String?) {
        setPatternControls(patternProperty, threeCheckBoxProperty, property,
                defaultValue, IdentityTransformer())
    }

    /**
     */
    private fun setPatternControls(patternProperty: PatternPropertyBase?,
                                   threeCheckBoxProperty: PropertyBean?, property: PropertyBean?,
                                   defaultValue: String?, transformer: ValueTransformator) {
        if (patternProperty == null) {
            // value is not set:
            property!!.value = defaultValue
            threeCheckBoxProperty
                    .value = ThreeCheckBoxProperty.DON_T_TOUCH_VALUE
            return
        }
        if (patternProperty.value == null) {
            // remove prop:
            property!!.value = defaultValue
            threeCheckBoxProperty!!.value = ThreeCheckBoxProperty.FALSE_VALUE
            return
        }
        property!!.value = transformer.transform(patternProperty.value)
        threeCheckBoxProperty!!.value = ThreeCheckBoxProperty.TRUE_VALUE
    }

    private fun transformEdgeWidth(pEdgeWidth: String?): String? {
        if (pEdgeWidth == null) return null
        val edgeWidth = edgeWidthStringToInt(pEdgeWidth)
        val transformator = edgeWidthTransformation
        for (widthString in transformator.keys) {
            if (edgeWidth == transformator[widthString]!!.toInt()) {
                return widthString
            }
        }
        // not found:
        return null
    }

    private fun transformStringToWidth(value: String?): String? {
        val transformator = edgeWidthTransformation
        val intWidth = transformator[value]!!.toInt()
        return ApplyPatternAction.Companion.edgeWidthIntToString(intWidth)
    }

    private val edgeWidthTransformation: HashMap<String?, Int>
        private get() {
            val transformator = HashMap<String?, Int>()
            transformator[EDGE_WIDTHS[0]] = EdgeAdapter.Companion.WIDTH_PARENT
            transformator[EDGE_WIDTHS[1]] = EdgeAdapter.Companion.WIDTH_THIN
            transformator[EDGE_WIDTHS[2]] = 1
            transformator[EDGE_WIDTHS[3]] = 2
            transformator[EDGE_WIDTHS[4]] = 4
            transformator[EDGE_WIDTHS[5]] = 8
            return transformator
        }
    val pattern: Pattern
        get() {
            val pattern = Pattern()
            return getResultPattern(pattern)
        }
get() {
        return mStylePatternFrame.resultPattern
    }

    fun getResultPattern(pattern: Pattern): Pattern {
        pattern.patternNodeColor = getPatternResult(
                PatternNodeColor(), mSetNodeColor, mNodeColor) as PatternNodeColor?
        pattern.patternNodeBackgroundColor = getPatternResult(
                PatternNodeBackgroundColor(), mSetNodeBackgroundColor,
                mNodeBackgroundColor) as PatternNodeBackgroundColor?
        pattern.patternNodeStyle = getPatternResult(
                PatternNodeStyle(), mSetNodeStyle, mNodeStyle) as PatternNodeStyle?
        pattern.patternNodeText = getPatternResult(
                PatternNodeText(), mSetNodeText, mNodeText) as PatternNodeText?
        /* edges */pattern.patternEdgeColor = getPatternResult(
                PatternEdgeColor(), mSetEdgeColor, mEdgeColor) as PatternEdgeColor?
        pattern.patternEdgeStyle = getPatternResult(
                PatternEdgeStyle(), mSetEdgeStyle, mEdgeStyle) as PatternEdgeStyle?
        pattern.patternEdgeWidth = getPatternResult(
                PatternEdgeWidth(), mSetEdgeWidth, mEdgeWidth,
                EdgeWidthBackTransformer()) as PatternEdgeWidth?
        /* font */pattern.patternNodeFontName = getPatternResult(
                PatternNodeFontName(), mSetNodeFontName, mNodeFontName) as PatternNodeFontName?
        pattern.patternNodeFontSize = getPatternResult(
                PatternNodeFontSize(), mSetNodeFontSize, mNodeFontSize) as PatternNodeFontSize?
        pattern.patternNodeFontBold = getPatternResult(
                PatternNodeFontBold(), mSetNodeFontBold, mNodeFontBold) as PatternNodeFontBold?
        pattern.patternNodeFontStrikethrough = getPatternResult(
                PatternNodeFontStrikethrough(), mSetNodeFontStrikethrough, mNodeFontStrikethrough) as PatternNodeFontStrikethrough?
        pattern.patternNodeFontItalic = getPatternResult(
                PatternNodeFontItalic(), mSetNodeFontItalic,
                mNodeFontItalic) as PatternNodeFontItalic?
        pattern.patternIcon = getPatternResult(
                PatternIcon(), mSetIcon, mIcon) as PatternIcon?
        pattern.patternScript = getPatternResult(
                PatternScript(), mSetScriptPattern, mScriptPattern) as PatternScript?
        if (StylePatternFrameType.WITH_NAME_AND_CHILDS == mType) {
            pattern.name = mName!!.value
            pattern.patternChild = getPatternResult(
                    PatternChild(), mSetChildPattern, mChildPattern) as PatternChild?
        }
        return pattern
    }

    private fun getPatternResult(
            baseProperty: PatternPropertyBase,
            threeCheckBoxProperty: ThreeCheckBoxProperty?, property: PropertyBean?): PatternPropertyBase? {
        val transformer: ValueTransformator = IdentityTransformer()
        return getPatternResult(baseProperty, threeCheckBoxProperty, property,
                transformer)
    }

    /**
     */
    private fun getPatternResult(
            baseProperty: PatternPropertyBase,
            threeCheckBoxProperty: ThreeCheckBoxProperty?, property: PropertyBean?,
            transformer: ValueTransformator): PatternPropertyBase? {
        val checkboxResult = threeCheckBoxProperty!!.value ?: return null
        if (checkboxResult == ThreeCheckBoxProperty.DON_T_TOUCH_VALUE) {
            return null
        }
        if (checkboxResult == ThreeCheckBoxProperty.FALSE_VALUE) {
            // remove property:
            return baseProperty
        }
        baseProperty.value = transformer.transform(property!!.value)
        return baseProperty
    }

    /**
     * Used to enable/disable the attribute controls, if the check boxes are
     * changed.
     */
    override fun propertyChange(pEvt: PropertyChangeEvent) {
        // System.out.println("Propagation of "+ pEvt.getPropertyName()
        // + " with value " + pEvt.getNewValue() + " and source " +
        // pEvt.getSource());
        if (mPropertyChangePropagation.containsKey(pEvt.source)) {
            val booleanProp = pEvt
                    .source as ThreeCheckBoxProperty
            // enable only when set:
            mPropertyChangePropagation[booleanProp]!!.setEnabled(ThreeCheckBoxProperty.TRUE_VALUE == booleanProp
                    .value)
            return
        }
    }

    /**
     * For the child pattern box, the list is set here.
     *
     */
    fun setPatternList(patternList: List<Pattern>?) {
        mPatternList = patternList
        val childNames = patternNames
        mChildPattern!!.updateComboBoxEntries(childNames, childNames)
    }

    companion object {
        private val EDGE_STYLES = arrayOf<String>(
                EdgeAdapter.Companion.EDGESTYLE_LINEAR, EdgeAdapter.Companion.EDGESTYLE_BEZIER,
                EdgeAdapter.Companion.EDGESTYLE_SHARP_LINEAR,
                EdgeAdapter.Companion.EDGESTYLE_SHARP_BEZIER)
        private val EDGE_WIDTHS = arrayOf(
                "EdgeWidth_parent", "EdgeWidth_thin", "EdgeWidth_1", "EdgeWidth_2",
                "EdgeWidth_4", "EdgeWidth_8")
        private const val NODE_BACKGROUND_COLOR = "nodebackgroundcolor"
        private const val SET_RESOURCE = "set_property_text"
        private const val SET_NODE_BACKGROUND_COLOR = SET_RESOURCE
        private const val NODE_COLOR = "nodecolor"
        private const val SET_NODE_COLOR = SET_RESOURCE
        private const val SET_NODE_STYLE = SET_RESOURCE
        private const val NODE_STYLE = "nodestyle"
        private const val NODE_FONT_NAME = "nodefontname"
        private const val SET_NODE_FONT_NAME = SET_RESOURCE
        private const val NODE_FONT_SIZE = "nodefontsize"
        private const val SET_NODE_FONT_SIZE = SET_RESOURCE
        private const val NODE_FONT_BOLD = "nodefontbold"
        private const val SET_NODE_FONT_BOLD = SET_RESOURCE
        private const val NODE_FONT_STRIKETHROUGH = "nodefontStrikethrough"
        private const val SET_NODE_FONT_STRIKETHROUGH = SET_RESOURCE
        private const val NODE_FONT_ITALIC = "nodefontitalic"
        private const val SET_NODE_FONT_ITALIC = SET_RESOURCE
        private const val SET_NODE_TEXT = SET_RESOURCE
        private const val NODE_TEXT = "nodetext"
        private const val SET_EDGE_WIDTH = SET_RESOURCE
        private const val EDGE_WIDTH = "edgewidth"
        private const val SET_EDGE_STYLE = SET_RESOURCE
        private const val EDGE_STYLE = "edgestyle"
        private const val SET_EDGE_COLOR = SET_RESOURCE
        private const val EDGE_COLOR = "edgecolor"
        private const val CLEAR_ALL_SETTERS = "clear_all_setters"
        private const val SET_ICON = SET_RESOURCE
        private const val ICON = "icon"
        private const val NODE_NAME = "patternname"
        private const val SET_CHILD_PATTERN = SET_RESOURCE
        private const val CHILD_PATTERN = "childpattern"
        private const val SET_SCRIPT = "setscript"
        private const val SCRIPT = "script"
    }
}