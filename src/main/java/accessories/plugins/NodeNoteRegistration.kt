/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2007  Christian Foltin, Dimitry Polivaev and others.
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
 * Created on 11.09.2007
 */
/*$Id: NodeNoteRegistration.java,v 1.1.2.25 2010/10/07 21:19:51 christianfoltin Exp $*/
package accessories.plugins

import freemind.extensions.HookAdapter.startupMapHook
import freemind.modes.mindmapmode.hooks.MindMapHookAdapter.mindMapController
import freemind.modes.ControllerAdapter.controller
import freemind.controller.Controller.mapModuleManager
import freemind.controller.MapModuleManager.addListener
import freemind.extensions.HookAdapter.getResourceString
import freemind.main.Tools.xmlToBoolean
import freemind.extensions.HookAdapter.getController
import freemind.modes.ModeController.frame
import freemind.main.Tools.addEscapeActionToDialog
import freemind.controller.StructuredMenuHolder.addMenu
import freemind.modes.mindmapmode.hooks.MindMapHookAdapter.addAccelerator
import freemind.controller.StructuredMenuHolder.addAction
import freemind.controller.StructuredMenuHolder.updateMenus
import freemind.main.Tools.getNodeTextHierarchy
import freemind.modes.mindmapmode.MindMapController.decorateDialog
import freemind.controller.actions.generated.instance.TimeWindowConfigurationStorage.viewFoldedNodes
import freemind.controller.actions.generated.instance.TimeWindowConfigurationStorage.listTimeWindowColumnSettingList
import freemind.controller.actions.generated.instance.TableColumnSetting.columnWidth
import freemind.controller.actions.generated.instance.TableColumnSetting.columnSorting
import freemind.main.Tools.setLabelAndMnemonic
import freemind.modes.ControllerAdapter.newMap
import freemind.modes.MindMapNode.shallowCopy
import freemind.modes.mindmapmode.MindMapController.insertNodeInto
import freemind.modes.ControllerAdapter.rootNode
import freemind.modes.MindMapNode.text
import freemind.main.Tools.safeEquals
import freemind.modes.mindmapmode.MindMapController.setNodeText
import freemind.modes.ControllerAdapter.select
import freemind.modes.ControllerAdapter.map
import freemind.modes.MindMap.rootNode
import freemind.modes.common.plugins.ReminderHookBase.remindUserAt
import freemind.modes.MindMapNode.historyInformation
import freemind.modes.MindMapNode.isFolded
import freemind.modes.MindMapNode.childrenUnfolded
import freemind.modes.mindmapmode.MindMapController.storeDialogPositions
import freemind.controller.MapModuleManager.removeListener
import freemind.controller.actions.generated.instance.TimeWindowConfigurationStorage.addTimeWindowColumnSetting
import freemind.modes.MindMapNode.noteText
import freemind.modes.MindMapNode.icons
import freemind.modes.MindIcon.getName
import freemind.view.mindmapview.MultipleImage.addImage
import freemind.modes.MindIcon.icon
import freemind.view.mindmapview.MultipleImage.imageCount
import freemind.main.Tools.isWindows
import freemind.main.Tools.isMacOsX
import freemind.main.Tools.xmlToColor
import freemind.controller.actions.generated.instance.CalendarMarking.color
import freemind.controller.actions.generated.instance.CalendarMarking.name
import freemind.modes.ControllerAdapter.nodeRefresh
import freemind.modes.ControllerAdapter.setToolTip
import freemind.modes.ControllerAdapter.getText
import freemind.modes.ControllerAdapter.selecteds
import freemind.modes.mindmapmode.MindMapController.addNewNode
import freemind.modes.MindMapNode.isLeft
import freemind.modes.MindMapNode.isRoot
import freemind.modes.MindMapNode.parentNode
import freemind.main.Resources.getProperty
import freemind.common.XmlBindingTools.unMarshall
import freemind.controller.actions.generated.instance.CalendarMarkings.addCalendarMarking
import freemind.modes.ControllerAdapter.setProperty
import freemind.common.XmlBindingTools.marshall
import freemind.controller.actions.generated.instance.CalendarMarkings.sizeCalendarMarkingList
import freemind.controller.actions.generated.instance.CalendarMarkings.getCalendarMarking
import freemind.controller.actions.generated.instance.CalendarMarkings.removeFromCalendarMarkingElementAt
import freemind.modes.ControllerAdapter.frame
import freemind.main.FreeMindMain.jFrame
import freemind.modes.MindMapNode.invokeHook
import freemind.modes.mindmapmode.MindMapController.nodeChanged
import freemind.modes.mindmapmode.actions.MindMapActions.addHook
import freemind.main.Tools.getVectorWithSingleElement
import freemind.extensions.NodeHookAdapter.invoke
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter.mindMapController
import freemind.main.Tools.unMarshall
import freemind.main.Tools.marshall
import freemind.main.Tools.colorToXml
import freemind.controller.actions.generated.instance.CalendarMarking.startDate
import freemind.controller.actions.generated.instance.CalendarMarking.endDate
import freemind.controller.actions.generated.instance.CalendarMarking.firstOccurence
import freemind.controller.actions.generated.instance.CalendarMarking.repeatEachNOccurence
import freemind.controller.actions.generated.instance.CalendarMarking.repeatType
import freemind.modes.mindmapmode.actions.NodeHookAction.hookName
import freemind.modes.ModeController.selecteds
import freemind.modes.MindMapNode.activatedHooks
import freemind.modes.ModeController.view
import freemind.view.mindmapview.MapView.innerBounds
import freemind.view.mindmapview.MapView.getNodeView
import freemind.modes.MindMapNode.getShortText
import freemind.modes.MindMapNode.getObjectId
import freemind.modes.MindMapNode.link
import freemind.view.mindmapview.MapView.getNodeContentLocation
import freemind.view.mindmapview.NodeView.content
import freemind.modes.ModeController.getFileChooser
import freemind.modes.FreeMindFileDialog.setDialogTitle
import freemind.modes.FreeMindFileDialog.showOpenDialog
import freemind.modes.FreeMindFileDialog.showSaveDialog
import freemind.modes.FreeMindFileDialog.selectedFile
import freemind.swing.DefaultListModel.getSize
import freemind.swing.DefaultListModel.addAll
import freemind.swing.DefaultListModel.remove
import freemind.controller.Controller.defaultFont
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.setPattern
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.addListeners
import freemind.modes.MapAdapter.loadTree
import freemind.modes.MapAdapter.root
import freemind.view.mindmapview.MapView.centerNode
import freemind.modes.StylePatternFactory.removeAllPattern
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.propertyChange
import freemind.modes.ExtendedMapFeedbackAdapter.select
import freemind.modes.ExtendedMapFeedbackAdapter.applyPattern
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.resultPattern
import freemind.view.mindmapview.NodeView.updateAll
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.init
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.getResultPattern
import freemind.swing.DefaultListModel.get
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.setPatternList
import freemind.swing.DefaultListModel.unmodifiableList
import freemind.main.FreeMindMain.getLogger
import freemind.modes.StylePatternFactory.loadPatterns
import freemind.modes.mindmapmode.MindMapController.patternReader
import freemind.controller.actions.generated.instance.Pattern.name
import freemind.controller.actions.generated.instance.ManageStyleEditorWindowConfigurationStorage.dividerPosition
import freemind.controller.StructuredMenuHolder.addMenuItem
import freemind.controller.StructuredMenuHolder.addSeparator
import freemind.swing.DefaultListModel.add
import freemind.main.Tools.deepCopy
import freemind.modes.StylePatternFactory.createPatternFromSelected
import freemind.modes.ControllerAdapter.selected
import freemind.modes.mindmapmode.MindMapController.applyPattern
import freemind.controller.actions.generated.instance.Pattern.patternChild
import freemind.controller.actions.generated.instance.PatternPropertyBase.value
import freemind.modes.ModeController.controller
import freemind.controller.Controller.mapModule
import freemind.controller.MapModuleManager.changeToMapModule
import freemind.view.MapModule.toString
import freemind.view.MapModule.modeController
import freemind.modes.ModeController.save
import freemind.modes.mindmapmode.MindMapController.obtainFocusForSelected
import freemind.extensions.HookAdapter.getPluginBaseClass
import freemind.view.mindmapview.MapView.scrollBy
import freemind.modes.MindMapNode.plainTextContent
import freemind.modes.MindMapNode.children
import freemind.modes.mindmapmode.MindMapController.cut
import freemind.modes.mindmapmode.MindMapController.paste
import freemind.modes.ModeController.select
import freemind.extensions.HookAdapter.obtainFocusForSelected
import freemind.modes.MindMapNode.toString
import freemind.modes.MindMapNode.getChildPosition
import freemind.modes.MindMapNode.color
import freemind.modes.MindMapNode.font
import freemind.modes.ControllerAdapter.view
import freemind.view.mindmapview.MapView.toggleSelected
import freemind.extensions.HookAdapter.setController
import freemind.extensions.NodeHookAdapter.setMap
import freemind.modes.mindmapmode.MindMapController.registerMouseWheelEventHandler
import freemind.modes.mindmapmode.MindMapController.deRegisterMouseWheelEventHandler
import freemind.view.mindmapview.MapView.root
import freemind.modes.MindMapNode.hasChildren
import freemind.modes.mindmapmode.MindMapController.setFolded
import freemind.modes.ControllerAdapter.selectedView
import freemind.common.OptionalDontShowMeAgainDialog.show
import freemind.common.OptionalDontShowMeAgainDialog.result
import freemind.modes.mindmapmode.MindMapController.setNoteText
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter.mindMapController
import freemind.modes.ControllerAdapter.getNodeID
import freemind.controller.Controller.errorMessage
import freemind.extensions.NodeHookAdapter.getNode
import freemind.extensions.PermanentNodeHookAdapter.save
import freemind.extensions.PermanentNodeHookAdapter.saveNameValuePairs
import freemind.extensions.PermanentNodeHookAdapter.loadFrom
import freemind.extensions.PermanentNodeHookAdapter.loadNameValuePairs
import freemind.extensions.PermanentNodeHookAdapter.shutdownMapHook
import freemind.modes.MindMapNode.isDescendantOf
import freemind.modes.ControllerAdapter.registerNodeLifetimeListener
import freemind.modes.ControllerAdapter.deregisterNodeLifetimeListener
import freemind.modes.MindMapNode.setStateIcon
import freemind.extensions.HookAdapter.name
import freemind.modes.ControllerAdapter.getNodeFromID
import freemind.modes.MindMapNode.isDescendantOfOrEqual
import freemind.extensions.NodeHookAdapter.nodeId
import freemind.extensions.PermanentNodeHookAdapter.processUnfinishedLinks
import freemind.modes.ModeController.selected
import freemind.modes.mindmapmode.EncryptedMindMapNode.isAccessible
import freemind.modes.ModeController.nodeRefresh
import freemind.modes.ControllerAdapter.mode
import freemind.modes.Mode.createModeController
import freemind.modes.ModeController.setModel
import freemind.modes.mindmapmode.EncryptedMindMapNode.setPassword
import freemind.modes.mindmapmode.EncryptedMindMapNode.map
import freemind.modes.mindmapmode.MindMapController.setNewNodeCreator
import freemind.modes.common.dialogs.EnterPasswordDialog.result
import freemind.modes.common.dialogs.EnterPasswordDialog.password
import freemind.modes.mindmapmode.EncryptedMindMapNode.encrypt
import freemind.modes.mindmapmode.EncryptedMindMapNode.setShuttingDown
import freemind.modes.ControllerAdapter.nodeStructureChanged
import freemind.view.mindmapview.MapView.selectAsTheOnlyOneSelected
import freemind.main.FreeMindMain.contentPane
import freemind.modes.mindmapmode.EncryptedMindMapNode.decrypt
import freemind.modes.StylePatternFactory.createPatternFromNode
import freemind.modes.ControllerAdapter.mapModule
import freemind.modes.ModeController.getNodeFromID
import freemind.controller.MapModuleManager.mapModules
import freemind.modes.ControllerAdapter.registerNodeSelectionListener
import freemind.modes.ControllerAdapter.deregisterNodeSelectionListener
import freemind.view.mindmapview.NodeView.model
import freemind.modes.ModeController.setFolded
import freemind.modes.ModeController.getNodeView
import freemind.extensions.ExportHook.createBufferedImage
import freemind.extensions.ExportHook.chooseFile
import freemind.modes.mindmapmode.MindMapController.actionRegistry
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.registerHandler
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.deregisterHandler
import freemind.main.FreeMindMain.loggerList
import freemind.main.FreeMindMain.freemindDirectory
import freemind.main.Tools.getFile
import freemind.main.LogFileLogHandler.setLogReceiver
import freemind.modes.MapFeedbackAdapter.sortNodesByDepth
import freemind.modes.ControllerAdapter.copySingle
import freemind.modes.mindmapmode.MindMapController.clearNodeContents
import freemind.main.FreeMindMain.repaint
import freemind.extensions.NodeHookAdapter.nodeChanged
import freemind.modes.mindmapmode.MindMapController.doTransaction
import freemind.controller.actions.generated.instance.NodeAction.node
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.registerActor
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.deregisterActor
import freemind.modes.MindMapNode.removeAllHooks
import freemind.modes.MindMap.changeRoot
import freemind.view.mindmapview.MapView.getViewers
import freemind.view.mindmapview.MapView.removeViewer
import freemind.view.mindmapview.MapView.initRoot
import freemind.modes.ControllerAdapter.centerNode
import freemind.extensions.HookAdapter.getProperties
import freemind.extensions.ExportHook.getTranslatableResourceString
import freemind.modes.ModeController.map
import freemind.modes.MindMap.file
import freemind.modes.MindMap.isReadOnly
import freemind.main.Tools.fileToUrl
import freemind.modes.MindMap.getFilteredXml
import freemind.extensions.ExportHook.copyFromResource
import freemind.modes.MindIcon.iconBaseFileName
import freemind.extensions.ExportHook.copyFromFile
import freemind.extensions.HookAdapter.getResource
import freemind.main.Tools.fileToRelativeUrlString
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.registerFilter
import freemind.modes.mindmapmode.actions.xml.ActionRegistry.deregisterFilter
import freemind.controller.actions.generated.instance.CompoundAction.listChoiceList
import freemind.modes.mindmapmode.actions.xml.ActionPair.doAction
import freemind.controller.actions.generated.instance.CompoundAction.addChoice
import freemind.controller.actions.generated.instance.NewNodeAction.newId
import freemind.modes.mindmapmode.actions.xml.ActionPair.undoAction
import freemind.main.FreeMind.jFrame
import freemind.modes.StylePatternFactory.savePatterns
import freemind.modes.mindmapmode.MindMapController.loadPatterns
import freemind.main.FreeMindMain.freeMindMenuBar
import freemind.controller.MenuBar.updateMenus
import freemind.modes.NodeAdapter.backgroundColor
import freemind.modes.StylePatternFactory.toString
import freemind.common.TextTranslator.getText
import freemind.common.PropertyBean.firePropertyChangeEvent
import freemind.modes.StylePatternFactory.getPatternFromString
import freemind.controller.actions.generated.instance.Patterns.listChoiceList
import freemind.modes.StylePatternFactory.getPatternsFromString
import freemind.controller.actions.generated.instance.Patterns.getChoice
import freemind.controller.actions.generated.instance.Patterns.sizeChoiceList
import freemind.extensions.PermanentNodeHookAdapter.onAddChild
import freemind.extensions.PermanentNodeHookAdapter.onUpdateChildrenHook
import freemind.extensions.PermanentNodeHookAdapter.onUpdateNodeHook
import freemind.main.FreeMindMain.getProperty
import freemind.modes.ModeController.isBlocked
import freemind.view.mindmapview.NodeView.getMainView
import freemind.main.Tools.listToString
import freemind.modes.mindmapmode.MindMapController.copy
import freemind.main.Tools.getMindMapNodesFromClipboard
import freemind.modes.ControllerAdapter.getResource
import freemind.main.Tools.generateID
import freemind.controller.actions.generated.instance.CompoundAction.setAtChoice
import freemind.controller.actions.generated.instance.MoveNodesAction.listNodeListMemberList
import freemind.controller.actions.generated.instance.MoveNodesAction.getNodeListMember
import freemind.controller.actions.generated.instance.HookNodeAction.listNodeListMemberList
import freemind.controller.actions.generated.instance.HookNodeAction.getNodeListMember
import freemind.modes.MindMap.linkRegistry
import freemind.modes.MindMapLinkRegistry.generateUniqueID
import freemind.main.Tools.MindMapNodePair.corresponding
import freemind.controller.actions.generated.instance.CompoundAction.addAtChoice
import freemind.controller.actions.generated.instance.NodeListMember.node
import freemind.main.Tools.MindMapNodePair.cloneNode
import freemind.controller.actions.generated.instance.PasteNodeAction.asSibling
import freemind.controller.actions.generated.instance.UndoPasteNodeAction.asSibling
import freemind.main.Tools.copyStream
import freemind.extensions.PermanentNodeHookAdapter.onRemoveChildren
import freemind.modes.ModeController.selectedView
import freemind.view.mindmapview.MapView.scrollNodeToVisible
import freemind.main.Tools.setDialogLocationRelativeTo
import freemind.modes.common.dialogs.IconSelectionPopupDialog.result
import freemind.modes.common.dialogs.IconSelectionPopupDialog.modifiers
import freemind.controller.actions.generated.instance.HookNodeAction.hookName
import freemind.modes.mindmapmode.MindMapController.marshall
import freemind.modes.MindMap.isSaved
import freemind.main.Tools.compareText
import freemind.modes.ControllerAdapter.setSaved
import freemind.modes.MindMapNode.map
import freemind.modes.MindMapNode.xmlNoteText
import freemind.modes.ControllerAdapter.getProperty
import freemind.controller.Controller.obtainFocusForSelected
import freemind.main.FreeMindMain.getAdjustableProperty
import freemind.main.Tools.updateFontSize
import freemind.view.mindmapview.MapView.getZoom
import freemind.main.FreeMindMain.openDocument
import freemind.controller.Controller.insertComponentIntoSplitPane
import freemind.controller.Controller.removeSplitPane
import freemind.extensions.NodeHookAdapter.getMap
import freemind.modes.MindMapLinkRegistry.registerLocalHyperlinkId
import freemind.modes.ModeController.load
import freemind.extensions.PermanentNodeHookAdapter.setToolTip
import freemind.modes.MindMapNode.childrenFolded
import freemind.modes.MindMapNode.attributeTableLength
import freemind.modes.MindMapNode.getAttribute
import freemind.modes.mindmapmode.MindMapController.removeAttribute
import freemind.modes.mindmapmode.MindMapController.addAttribute
import freemind.modes.attributes.Attribute.name
import freemind.modes.attributes.Attribute.value
import freemind.controller.actions.generated.instance.AttributeTableProperties.listTableColumnOrderList
import freemind.controller.actions.generated.instance.TableColumnOrder.columnIndex
import freemind.controller.actions.generated.instance.TableColumnOrder.columnSorting
import freemind.controller.actions.generated.instance.AttributeTableProperties.addTableColumnOrder
import freemind.modes.mindmapmode.hooks.MindMapHookAdapter
import freemind.controller.MapModuleManager.MapModuleChangeObserver
import accessories.plugins.time.TimeList.NodeRenderer
import accessories.plugins.time.TimeList.IconsRenderer
import accessories.plugins.time.FlatNodeTableFilterModel
import accessories.plugins.time.TimeList.NotesRenderer
import freemind.modes.mindmapmode.MindMapController
import accessories.plugins.time.TimeList
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.ActionEvent
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import accessories.plugins.time.TimeList.FilterTextDocumentListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import accessories.plugins.time.TimeList.FlatNodeTable
import accessories.plugins.time.TimeList.FlatNodeTableKeyListener
import accessories.plugins.time.TimeList.FlatNodeTableMouseAdapter
import accessories.plugins.time.TimeList.NotesHolder
import accessories.plugins.time.TimeList.IconsHolder
import accessories.plugins.time.TimeList.ReplaceAllInfo
import accessories.plugins.time.TimeList.ReplaceSelectedInfo
import accessories.plugins.time.TimeList.ToggleViewFoldedNodesAction
import freemind.controller.StructuredMenuHolder
import freemind.modes.MindMapNode
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.controller.actions.generated.instance.TimeWindowConfigurationStorage
import freemind.controller.actions.generated.instance.TimeWindowColumnSetting
import freemind.controller.MenuItemSelectedListener
import accessories.plugins.time.TimeList.IReplaceInputInformation
import javax.swing.text.BadLocationException
import accessories.plugins.time.TimeList.MindmapTableModel
import freemind.modes.common.plugins.ReminderHookBase
import accessories.plugins.time.TimeManagementOrganizer
import kotlin.Throws
import accessories.plugins.time.TimeList.FilterTextDocumentListener.DelayedTextEntry
import java.util.TimerTask
import java.lang.Runnable
import java.awt.event.MouseAdapter
import java.awt.event.KeyListener
import freemind.common.ScalableJTable
import javax.swing.table.TableCellRenderer
import javax.swing.table.DefaultTableCellRenderer
import java.text.DateFormat
import freemind.modes.MindIcon
import java.util.Collections
import freemind.modes.ModeController
import freemind.view.MapModule
import kotlin.jvm.JvmOverloads
import java.util.Locale
import java.beans.PropertyChangeListener
import java.util.Calendar
import accessories.plugins.time.JDayChooser
import accessories.plugins.time.JMonthChooser
import accessories.plugins.time.JYearChooser
import java.awt.BorderLayout
import java.beans.PropertyChangeEvent
import accessories.plugins.time.JCalendar
import java.awt.Color
import kotlin.jvm.JvmStatic
import java.awt.event.ActionListener
import java.awt.event.FocusListener
import java.awt.Dimension
import java.lang.NumberFormatException
import java.awt.event.FocusEvent
import accessories.plugins.time.JSpinField
import freemind.common.ScalableJButton
import javax.swing.border.Border
import accessories.plugins.time.ICalendarMarkingEvaluator
import freemind.controller.actions.generated.instance.CalendarMarkings
import freemind.common.XmlBindingTools
import accessories.plugins.time.CalendarMarkingEvaluator
import freemind.preferences.FreemindPropertyListener
import java.awt.GridLayout
import accessories.plugins.time.JDayChooser.DecoratorButton
import accessories.plugins.time.JDayChooser.ChangeAwareButton
import java.awt.Graphics
import java.text.DateFormatSymbols
import freemind.controller.actions.generated.instance.CalendarMarking
import java.util.GregorianCalendar
import java.awt.event.MouseListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableModel
import javax.swing.table.JTableHeader
import java.util.Arrays
import javax.swing.table.TableColumnModel
import java.awt.event.ItemListener
import java.awt.event.ItemEvent
import accessories.plugins.time.TimeManagement.NodeFactory
import accessories.plugins.time.TimeManagement.AppendDateAbstractAction
import accessories.plugins.time.CalendarMarkingDialog
import accessories.plugins.time.JTripleCalendar
import accessories.plugins.time.TimeManagement
import accessories.plugins.time.TimeManagement.AppendDateAction
import accessories.plugins.time.TimeManagement.AppendDateToChildAction
import accessories.plugins.time.TimeManagement.AppendDateToSiblingAction
import accessories.plugins.time.TimeManagement.RemindAction
import accessories.plugins.time.TimeManagement.RemoveReminders
import accessories.plugins.time.TimeManagement.TodayAction
import accessories.plugins.time.TimeManagement.AddMarkAction
import accessories.plugins.time.TimeManagement.RemoveMarkAction
import java.text.MessageFormat
import java.util.Properties
import accessories.plugins.time.JTripleCalendar.JSwitchableCalendar
import javax.swing.border.LineBorder
import tests.freemind.FreeMindMainMock
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter
import freemind.modes.MindMap
import freemind.extensions.HookRegistration
import freemind.controller.MenuItemEnabledListener
import freemind.modes.mindmapmode.actions.NodeHookAction
import freemind.extensions.PermanentNodeHook
import accessories.plugins.time.CalendarMarkingEvaluator.RepetitionHandler
import accessories.plugins.time.CalendarMarkingEvaluator.BasicRepetitionHandler
import accessories.plugins.time.CalendarMarkingEvaluator.DirektBeginnerHandler
import accessories.plugins.time.CalendarMarkingEvaluator.NeverHandler
import accessories.plugins.time.CalendarMarkingEvaluator.DailyHandler
import accessories.plugins.time.CalendarMarkingEvaluator.WeeklyHandler
import accessories.plugins.time.CalendarMarkingEvaluator.WeeklyEveryNthDayHandler
import accessories.plugins.time.CalendarMarkingEvaluator.MonthlyHandler
import accessories.plugins.time.CalendarMarkingEvaluator.MonthlyEveryNthDayHandler
import accessories.plugins.time.CalendarMarkingEvaluator.MonthlyEveryNthWeekHandler
import accessories.plugins.time.CalendarMarkingEvaluator.YearlyHandler
import accessories.plugins.time.CalendarMarkingEvaluator.YearlyEveryNthDayHandler
import accessories.plugins.time.CalendarMarkingEvaluator.YearlyEveryNthWeekHandler
import accessories.plugins.time.CalendarMarkingEvaluator.YearlyEveryNthMonthHandler
import java.util.TreeSet
import java.awt.Rectangle
import accessories.plugins.util.html.ClickableImageCreator.AreaHolder
import java.lang.StringBuffer
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import accessories.plugins.util.xslt.ExportDialog
import accessories.plugins.util.xslt.XmlExporter
import accessories.plugins.util.window.WindowClosingAdapter
import accessories.plugins.util.xslt.ExportDialog.FileChooseListener
import accessories.plugins.util.xslt.ExportDialog.ExportListener
import freemind.modes.FreeMindFileDialog
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import accessories.plugins.dialogs.ListTransferHandler.ListTransferable
import java.lang.ClassNotFoundException
import freemind.common.TextTranslator
import freemind.modes.ExtendedMapFeedbackAdapter
import accessories.plugins.dialogs.ChooseFormatPopupDialog
import freemind.modes.mindmapmode.dialogs.StylePatternFrame
import accessories.plugins.dialogs.ChooseFormatPopupDialog.DemoMapFeedback
import freemind.modes.mindmapmode.MindMapMapModel
import freemind.main.Tools.StringReaderCreator
import freemind.modes.MapAdapter
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.StylePatternFrameType
import freemind.modes.StylePatternFactory
import accessories.plugins.dialogs.ManagePatternsPopupDialog
import java.awt.CardLayout
import freemind.controller.actions.generated.instance.ManageStyleEditorWindowConfigurationStorage
import accessories.plugins.dialogs.ManagePatternsPopupDialog.PatternListSelectionListener
import java.awt.event.MouseMotionListener
import com.jgoodies.forms.builder.ButtonBarBuilder
import freemind.extensions.ModeControllerHookAdapter
import accessories.plugins.NodeNoteRegistration
import java.awt.KeyboardFocusManager
import accessories.plugins.SortNodes.NodeTextComparator
import javax.swing.text.html.HTMLEditorKit
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import accessories.plugins.UnfoldAll
import java.awt.event.MouseWheelEvent
import java.awt.event.InputEvent
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.OptionalDontShowMeAgainDialog.StandardPropertyHandler
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter
import freemind.modes.ModeController.NodeLifetimeListener
import accessories.plugins.ClonePasteAction.ClonePropertiesObserver
import accessories.plugins.ClonePasteAction.CloneProperties
import accessories.plugins.ClonePlugin
import java.util.StringTokenizer
import freemind.modes.mindmapmode.EncryptedMindMapNode
import freemind.modes.mindmapmode.MindMapController.NewNodeCreator
import freemind.modes.common.dialogs.EnterPasswordDialog
import accessories.plugins.FormatPaste
import freemind.modes.ModeController.NodeSelectionListener
import accessories.plugins.NodeHistory
import accessories.plugins.NodeAttributeTableRegistration
import accessories.plugins.NewLineTable
import freemind.extensions.ExportHook
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import freemind.main.LogFileLogHandler.LogReceiver
import freemind.modes.mindmapmode.actions.xml.PrintActionHandler
import accessories.plugins.LogFileViewer.UpdateTextAreaThread
import java.util.logging.SimpleFormatter
import accessories.plugins.LogFileViewer
import accessories.plugins.LogFileViewer.PrintOperationAction
import accessories.plugins.LogFileViewer.SetLogLevelAction
import freemind.controller.actions.generated.instance.LogFileViewerConfigurationStorage
import java.lang.InterruptedException
import accessories.plugins.ChangeRootNode
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.controller.actions.generated.instance.XmlAction
import freemind.controller.actions.generated.instance.ChangeRootNodeAction
import freemind.modes.mindmapmode.actions.xml.ActorXml
import accessories.plugins.ExportWithXSLT
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FilenameFilter
import accessories.plugins.util.html.ClickableImageCreator
import freemind.modes.mindmapmode.actions.xml.ActionFilter
import freemind.controller.actions.generated.instance.CompoundAction
import freemind.controller.actions.generated.instance.FormatNodeAction
import java.io.FileWriter
import accessories.plugins.RevisionPlugin
import freemind.controller.actions.generated.instance.EditNodeAction
import freemind.modes.NodeAdapter
import accessories.plugins.AutomaticLayout.AutomaticLayoutPropertyContributor
import accessories.plugins.AutomaticLayout
import accessories.plugins.AutomaticLayout.Registration.MyFreemindPropertyListener
import freemind.preferences.layout.OptionPanel
import freemind.common.PropertyBean
import freemind.common.PropertyControl
import accessories.plugins.AutomaticLayout.StylePropertyTranslator
import com.jgoodies.forms.builder.DefaultFormBuilder
import freemind.preferences.FreemindPropertyContributor
import freemind.preferences.layout.OptionPanel.NewTabProperty
import freemind.common.SeparatorProperty
import accessories.plugins.AutomaticLayout.StylePatternListProperty
import accessories.plugins.BlinkingNodeHook.TimerColorChanger
import accessories.plugins.BlinkingNodeHook
import freemind.controller.actions.generated.instance.NodeAction
import freemind.main.Tools.MindMapNodePair
import freemind.controller.actions.generated.instance.MoveNodesAction
import freemind.controller.actions.generated.instance.NodeListMember
import freemind.controller.actions.generated.instance.HookNodeAction
import freemind.controller.actions.generated.instance.PasteNodeAction
import freemind.controller.actions.generated.instance.MoveNodeXmlAction
import freemind.controller.actions.generated.instance.DeleteNodeAction
import freemind.controller.actions.generated.instance.CutNodeAction
import freemind.controller.actions.generated.instance.UndoPasteNodeAction
import accessories.plugins.ExportToOoWriter
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import freemind.extensions.UndoEventReceiver
import freemind.modes.IconInformation
import freemind.modes.mindmapmode.actions.IconAction
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import accessories.plugins.JumpLastEditLocation.JumpLastEditLocationRegistration
import freemind.controller.actions.generated.instance.FoldAction
import com.lightdev.app.shtm.TextResources
import accessories.plugins.NodeNote
import com.lightdev.app.shtm.SHTMLPanel
import accessories.plugins.NodeNoteRegistration.NotesManager
import accessories.plugins.NodeNoteRegistration.NoteDocumentListener
import freemind.modes.common.plugins.NodeNoteBase
import accessories.plugins.NodeNoteRegistration.SouthPanel
import freemind.controller.Controller.SplitComponentType
import accessories.plugins.NodeNoteRegistration.SimplyHtmlResources
import com.inet.jortho.SpellChecker
import java.util.LinkedList
import java.util.zip.ZipInputStream
import java.io.FileInputStream
import accessories.plugins.NodeAttributeTableRegistration.AttributeManager
import javax.swing.table.TableRowSorter
import freemind.controller.actions.generated.instance.AttributeTableProperties
import freemind.controller.actions.generated.instance.TableColumnOrder
import freemind.main.*
import freemind.view.ImageFactory
import freemind.view.mindmapview.*
import java.lang.Exception
import java.net.URL
import java.util.Enumeration
import java.util.logging.Logger
import javax.swing.*
import javax.swing.event.*
import javax.swing.text.html.HTMLDocument

class NodeNoteRegistration(controller: ModeController, map: MindMap?) : HookRegistration, MenuItemSelectedListener {
    class SimplyHtmlResources : TextResources {
        override fun getString(pKey: String): String {
            // no splash for SimplyHtml.
            var pKey = pKey
            if (safeEquals("show_splash_screen", pKey)) {
                return "false"
            }
            if (safeEquals("default_paste_mode", pKey)) {
                return "PASTE_HTML"
            }
            pKey = "simplyhtml.$pKey"
            var resourceString: String
            resourceString = Resources.getInstance().getResourceString(
                    pKey, null)
            if (resourceString == null) {
                resourceString = Resources.getInstance().getProperty(pKey)
            }
            //			if(resourceString == null) {
//				System.err.println("Can't find string " + pKey);
//			}
            return resourceString
        }
    }

    private class SouthPanel : JPanel(BorderLayout()) {
        init {
            border = BorderFactory.createEmptyBorder(10, 10, 0, 10)
        }

        override fun processKeyBinding(ks: KeyStroke, e: KeyEvent,
                                       condition: Int, pressed: Boolean): Boolean {
            return (super.processKeyBinding(ks, e, condition, pressed)
                    || e.keyChar.code == KeyEvent.VK_SPACE || e.keyChar.code == KeyEvent.VK_ALT)
        }

        companion object {
            private const val serialVersionUID = -4624762713662343786L
        }
    }

    private inner class NoteDocumentListener : DocumentListener {
        private var mNode: MindMapNode? = null
        override fun changedUpdate(arg0: DocumentEvent) {
            docEvent()
        }

        private fun docEvent() {
            // test if not already marked as dirty:
            if (mindMapController.map!!.isSaved) {
                // now test, if different:
                val documentText = normalizeString(documentText)
                val noteText = normalizeString(mNode!!.noteText)
                logger.fine("""
    Old doc =
    '$noteText', Current document: 
    '$documentText'. Comparison: '${compareText(noteText, documentText)}'.
    """.trimIndent())
                if (!safeEquals(noteText, documentText)) {
                    logger.finest("Making map dirty.")
                    // make map dirty in order to enable automatic save on note
                    // change.
                    mindMapController.setSaved(false)
                }
            }
        }

        override fun insertUpdate(arg0: DocumentEvent) {
            docEvent()
        }

        override fun removeUpdate(arg0: DocumentEvent) {
            docEvent()
        }

        fun setNode(pNode: MindMapNode?) {
            mNode = pNode
        }
    }

    // private NodeTextListener listener;
    private inner class NotesManager : NodeSelectionListener, NodeLifetimeListener {
        private var node: MindMapNode? = null
        override fun onLostFocusNode(node: NodeView?) {
            document.removeDocumentListener(
                    mNoteDocumentListener)
            // store its content:
            onSaveNode(node!!.model)
            this.node = null
        }

        override fun onFocusNode(nodeView: NodeView?) {
            node = nodeView!!.model
            val document = document
            // remove listener to avoid unnecessary dirty events.
            document.removeDocumentListener(mNoteDocumentListener)
            try {
                // Dimitry:
                // Images referenced from documents with bases given by
                // pFile.toURI().toURL() are not shown in SimplyHTML
                // (bug [ freemind-Bugs-2019223 ] Images are not shown in the
                // Notes view)
                // => the old method File.toURL() must be used again.
                document.base = node!!.map!!.file.toURI().toURL()
            } catch (e: Exception) {
            }
            val note = node!!.noteText
            if (note != null) {
                noteViewerComponent!!.setCurrentDocumentContent(note)
                mLastContentEmpty = false
            } else if (!mLastContentEmpty) {
                noteViewerComponent!!.setCurrentDocumentContent("")
                mLastContentEmpty = true
            }
            mNoteDocumentListener!!.setNode(node)
            document.addDocumentListener(mNoteDocumentListener)
        }

        override fun onSaveNode(node: MindMapNode?) {
            if (this.node !== node) {
                return
            }
            var editorContentEmpty = true
            // // TODO: Save the style with the note.
            // StyleSheet styleSheet = noteViewerComponent.getDocument()
            // .getStyleSheet();
            // styleSheet.removeStyle("body");
            // styleSheet.removeStyle("p");
            val editorPane = noteViewerComponent!!.editorPane
            val caretPosition = editorPane.caretPosition
            val selectionStart = editorPane.selectionStart
            val selectionEnd = editorPane.selectionEnd
            val documentText = documentText
            editorContentEmpty = (documentText
                    == NodeNote.Companion.EMPTY_EDITOR_STRING) || (documentText
                    == NodeNote.Companion.EMPTY_EDITOR_STRING_ALTERNATIVE) || (documentText
                    == NodeNote.Companion.EMPTY_EDITOR_STRING_ALTERNATIVE2)
            if (noteViewerComponent!!.needsSaving()) {
                if (editorContentEmpty) {
                    mindMapController.setNoteText(node!!, null as String?)
                } else {
                    mindMapController.setNoteText(node!!, documentText)
                }
                mLastContentEmpty = editorContentEmpty
            }
            try {
                // on inserting tabs, the caret position changes, as they are deleted:
                if (caretPosition < document.length) {
                    editorPane.caretPosition = caretPosition
                }
                editorPane.selectionEnd = selectionEnd
                editorPane.selectionStart = selectionStart
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }

        override fun onCreateNodeHook(node: MindMapNode?) {
            if (node!!.xmlNoteText != null) {
                setStateIcon(node, true)
            }
        }

        override fun onUpdateNodeHook(node: MindMapNode?) {
            // update display only, if the node is displayed.
            val newText = node!!.noteText
            if (node === mindMapController.selected
                    && !safeEquals(newText, htmlEditorPanel
                            .getDocumentText())) {
                htmlEditorPanel!!.setCurrentDocumentContent(
                        newText ?: "")
            }
            setStateIcon(node, !(newText == null || newText == ""))
        }

        override fun onPreDeleteNode(node: MindMapNode?) {}
        override fun onPostDeleteNode(pNode: MindMapNode?, pParent: MindMapNode?) {}
        override fun onSelectionChange(pNode: NodeView?, pIsSelected: Boolean) {}
    }

    private val mindMapController: MindMapController
    protected var noteViewerComponent: SHTMLPanel? = null
    private val logger: Logger
    private var mNotesManager: NotesManager? = null
    private var mNoteDocumentListener: NoteDocumentListener? = null
    var splitPaneVisible = false
        private set

    init {
        mindMapController = controller as MindMapController
        logger = controller.frame.getLogger(this.javaClass.name)
    }

    protected fun setStateIcon(node: MindMapNode?, enabled: Boolean) {
        // icon
        if (noteIcon == null) {
            noteIcon = ImageFactory.getInstance().createUnscaledIcon(
                    Resources.getInstance().getResource("images/knotes.png"))
        }
        var showIcon = enabled
        if (Resources.getInstance().getBoolProperty(
                        FreeMind.RESOURCES_DON_T_SHOW_NOTE_ICONS)) {
            showIcon = false
        }
        node!!.setStateIcon(NodeNoteBase.NODE_NOTE_ICON, if (showIcon) noteIcon else null)
        // tooltip, first try.
        if (!Resources.getInstance().getBoolProperty(
                        FreeMind.RESOURCES_DON_T_SHOW_NOTE_TOOLTIPS)) {
            mindMapController.setToolTip(node, "nodeNoteText",
                    if (enabled) node.noteText else null)
        }
    }

    /**
     * @return true, if the split pane is to be shown.
     * E.g. when freemind was closed before, the state of the split pane was stored and
     * is restored at the next start.
     */
    fun shouldShowSplitPane(): Boolean {
        return "true" == mindMapController.getProperty(
                FreeMind.RESOURCES_SHOW_NOTE_PANE)
    }

    internal inner class JumpToMapAction : AbstractAction() {
        override fun actionPerformed(e: ActionEvent) {
            logger.info("Jumping back to map!")
            mindMapController.controller!!.obtainFocusForSelected()
        }

        companion object {
            private const val serialVersionUID = -531070508254258791L
        }
    }

    override fun register() {
        // moved to registration:
        noteViewerComponent = getNoteViewerComponent()
        // register "leave note" action:
        val jumpToMapAction: Action = JumpToMapAction()
        val keystroke = mindMapController
                .frame
                .getAdjustableProperty(
                        "keystroke_accessories/plugins/NodeNote_jumpto.keystroke.alt_N")
        noteViewerComponent!!.getInputMap(
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(keystroke), "jumpToMapAction")

        // Register action
        noteViewerComponent!!.actionMap.put("jumpToMapAction",
                jumpToMapAction)
        if (shouldShowSplitPane()) {
            showNotesPanel()
        }
        mNotesManager = NotesManager()
        mindMapController.registerNodeSelectionListener(mNotesManager!!, false)
        mindMapController.registerNodeLifetimeListener(mNotesManager!!, true)
        mNoteDocumentListener = NoteDocumentListener()
    }

    override fun deRegister() {
        mindMapController.deregisterNodeSelectionListener(mNotesManager!!)
        mindMapController.deregisterNodeLifetimeListener(mNotesManager!!)
        if (noteViewerComponent != null && shouldShowSplitPane()) {
            noteViewerComponent!!.actionMap.remove("jumpToMapAction")
            hideNotesPanel()
            noteViewerComponent = null
        }
    }

    fun showNotesPanel() {
        val southPanel = SouthPanel()
        southPanel.add(noteViewerComponent, BorderLayout.CENTER)
        noteViewerComponent!!.isVisible = true
        if ("true" == mindMapController.getProperty(
                        FreeMind.RESOURCES_USE_DEFAULT_FONT_FOR_NOTES_TOO)) {
            // set default font for notes:
            var defaultFont = mindMapController.controller!!.defaultFont
            if (Resources.getInstance().getBoolProperty(
                            "experimental_font_sizing_for_long_node_editors")) {
                /*
				 * This is a proposal of Dan, but it doesn't work as expected.
				 * 
				 * http://sourceforge.net/tracker/?func=detail&aid=2800933&group_id
				 * =7118&atid=107118
				 */
                defaultFont = updateFontSize(defaultFont,
                        mindMapController.view!!.getZoom(),
                        defaultFont!!.size)
            }
            var rule = "BODY {"
            rule += "font-family: " + defaultFont!!.family + ";"
            rule += "font-size: " + defaultFont.size + "pt;"
            rule += "}\n"
            if ("true" == mindMapController.getProperty(
                            FreeMind.RESOURCES_USE_MARGIN_TOP_ZERO_FOR_NOTES)) {
                /*
				 * this is used for paragraph spacing. I put it here, too, as
				 * the tooltip display uses the same spacing. But it is to be
				 * discussed. fc, 23.3.2009.
				 */
                rule += "p {"
                rule += "margin-top:0;"
                rule += "}\n"
            }
            document.styleSheet.addRule(rule)
            // done setting default font.
        }
        noteViewerComponent!!.setOpenHyperlinkHandler { pE ->
            try {
                mindMapController.frame.openDocument(
                        URL(pE.actionCommand))
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
        }
        mindMapController.controller!!.insertComponentIntoSplitPane(
                southPanel, SplitComponentType.NOTE_PANEL)
        splitPaneVisible = true
        southPanel.revalidate()
    }

    fun hideNotesPanel() {
        // shut down the display:
        noteViewerComponent!!.isVisible = false
        mindMapController.controller!!.removeSplitPane(SplitComponentType.NOTE_PANEL)
        splitPaneVisible = false
    }

    protected fun getNoteViewerComponent(): SHTMLPanel? {
        return htmlEditorPanel
    }

    override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
        return splitPaneVisible
    }

    // (?s) makes . matching newline as well.
    private val documentText: String
        private get() {
            var documentText = noteViewerComponent!!.documentText
            // (?s) makes . matching newline as well.
            documentText = documentText.replaceFirst("(?s)<style.*?</style>".toRegex(), "")
            return documentText
        }

    private fun normalizeString(input: String?): String {
        var input = input
        if (input == null) input = NodeNote.Companion.EMPTY_EDITOR_STRING
        // return null;
        return input.replace("\\s+".toRegex(), " ").replace("  +".toRegex(), " ").trim { it <= ' ' }
    }

    protected val document: HTMLDocument
        protected get() = noteViewerComponent!!.document

    companion object {
        var htmlEditorPanel: SHTMLPanel? = null
            get() {
                if (field == null) {
                    SHTMLPanel.setResources(SimplyHtmlResources())
                    field = SHTMLPanel.createSHTMLPanel()
                    field.setMinimumSize(Dimension(100, 100))
                    val checkSpelling: Boolean = Resources.getInstance().getBoolProperty(FreeMindCommon.CHECK_SPELLING)
                    if (checkSpelling) {
                        SpellChecker.register(field.getEditorPane())
                    }
                }
                return field
            }
            private set

        /**
         * Indicates, whether or not the main panel has to be refreshed with new
         * content. The typical content will be empty, so this state is saved here.
         */
        private var mLastContentEmpty = true
        private var noteIcon: ImageIcon? = null
    }
}