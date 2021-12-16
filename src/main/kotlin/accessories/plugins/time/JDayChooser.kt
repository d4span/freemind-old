/*
 *  JDayChooser.java  - A bean for choosing a day
 *  Copyright (C) 2004 Kai Toedter
 *  kai@toedter.com
 *  www.toedter.com
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package accessories.plugins.time

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
import javax.swing.JDialog
import javax.swing.JTable
import accessories.plugins.time.TimeList.NodeRenderer
import accessories.plugins.time.TimeList.IconsRenderer
import accessories.plugins.time.FlatNodeTableFilterModel
import javax.swing.JTextField
import accessories.plugins.time.TimeList.NotesRenderer
import javax.swing.JLabel
import freemind.modes.mindmapmode.MindMapController
import accessories.plugins.time.TimeList
import javax.swing.WindowConstants
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.event.ActionEvent
import accessories.plugins.time.TimeList.FilterTextDocumentListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import accessories.plugins.time.TimeList.FlatNodeTable
import accessories.plugins.time.TimeList.FlatNodeTableKeyListener
import accessories.plugins.time.TimeList.FlatNodeTableMouseAdapter
import accessories.plugins.time.TimeList.NotesHolder
import accessories.plugins.time.TimeList.IconsHolder
import javax.swing.JScrollPane
import accessories.plugins.time.TimeList.ReplaceAllInfo
import accessories.plugins.time.TimeList.ReplaceSelectedInfo
import accessories.plugins.time.TimeList.ToggleViewFoldedNodesAction
import freemind.controller.StructuredMenuHolder
import javax.swing.JMenuBar
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.ListSelectionModel
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import freemind.modes.MindMapNode
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.controller.actions.generated.instance.TimeWindowConfigurationStorage
import freemind.controller.actions.generated.instance.TimeWindowColumnSetting
import javax.swing.JButton
import freemind.controller.MenuItemSelectedListener
import accessories.plugins.time.TimeList.IReplaceInputInformation
import javax.swing.text.BadLocationException
import accessories.plugins.time.TimeList.MindmapTableModel
import freemind.modes.common.plugins.ReminderHookBase
import accessories.plugins.time.TimeManagementOrganizer
import kotlin.Throws
import javax.swing.event.DocumentListener
import accessories.plugins.time.TimeList.FilterTextDocumentListener.DelayedTextEntry
import javax.swing.SwingUtilities
import java.lang.Runnable
import java.awt.event.MouseAdapter
import java.awt.event.KeyListener
import freemind.common.ScalableJTable
import javax.swing.table.TableCellRenderer
import javax.swing.table.DefaultTableCellRenderer
import java.text.DateFormat
import freemind.modes.MindIcon
import freemind.modes.ModeController
import freemind.view.mindmapview.MultipleImage
import freemind.view.MapModule
import kotlin.jvm.JvmOverloads
import javax.swing.JPanel
import java.beans.PropertyChangeListener
import accessories.plugins.time.JDayChooser
import accessories.plugins.time.JMonthChooser
import accessories.plugins.time.JYearChooser
import javax.swing.BorderFactory
import java.beans.PropertyChangeEvent
import accessories.plugins.time.JCalendar
import kotlin.jvm.JvmStatic
import javax.swing.JFrame
import javax.swing.event.ChangeListener
import javax.swing.event.CaretListener
import java.awt.event.ActionListener
import java.awt.event.FocusListener
import javax.swing.JSpinner
import javax.swing.SwingConstants
import javax.swing.event.ChangeEvent
import javax.swing.SpinnerNumberModel
import javax.swing.event.CaretEvent
import java.lang.NumberFormatException
import javax.swing.UIManager
import java.awt.event.FocusEvent
import accessories.plugins.time.JSpinField
import freemind.common.ScalableJButton
import javax.swing.border.Border
import accessories.plugins.time.ICalendarMarkingEvaluator
import freemind.controller.actions.generated.instance.CalendarMarkings
import freemind.common.XmlBindingTools
import accessories.plugins.time.CalendarMarkingEvaluator
import freemind.preferences.FreemindPropertyListener
import accessories.plugins.time.JDayChooser.DecoratorButton
import accessories.plugins.time.JDayChooser.ChangeAwareButton
import java.text.DateFormatSymbols
import freemind.controller.actions.generated.instance.CalendarMarking
import java.awt.event.MouseListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableModel
import javax.swing.table.JTableHeader
import javax.swing.event.TableModelListener
import javax.swing.Icon
import javax.swing.event.TableModelEvent
import javax.swing.table.TableColumnModel
import java.awt.event.ItemListener
import javax.swing.JComboBox
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
import javax.swing.JOptionPane
import accessories.plugins.time.JTripleCalendar.JSwitchableCalendar
import javax.swing.JSeparator
import javax.swing.border.LineBorder
import tests.freemind.FreeMindMainMock
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter
import javax.swing.JColorChooser
import javax.swing.JTextArea
import javax.swing.GroupLayout
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
import accessories.plugins.util.html.ClickableImageCreator.AreaHolder
import freemind.view.mindmapview.MapView
import java.lang.StringBuffer
import javax.swing.JComponent
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import accessories.plugins.util.xslt.ExportDialog
import accessories.plugins.util.xslt.XmlExporter
import accessories.plugins.util.window.WindowClosingAdapter
import accessories.plugins.util.xslt.ExportDialog.FileChooseListener
import accessories.plugins.util.xslt.ExportDialog.ExportListener
import freemind.modes.FreeMindFileDialog
import javax.swing.JFileChooser
import javax.swing.TransferHandler
import javax.swing.JList
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
import javax.swing.JPopupMenu
import javax.swing.JSplitPane
import freemind.controller.actions.generated.instance.ManageStyleEditorWindowConfigurationStorage
import accessories.plugins.dialogs.ManagePatternsPopupDialog.PatternListSelectionListener
import javax.swing.DefaultListCellRenderer
import java.awt.event.MouseMotionListener
import com.jgoodies.forms.builder.ButtonBarBuilder
import freemind.extensions.ModeControllerHookAdapter
import accessories.plugins.NodeNoteRegistration
import javax.swing.JViewport
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
import freemind.view.mindmapview.NodeMotionListenerView
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
import freemind.view.mindmapview.NodeViewVisitor
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
import javax.swing.ImageIcon
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
import javax.swing.KeyStroke
import javax.swing.JEditorPane
import accessories.plugins.NodeNote
import com.lightdev.app.shtm.SHTMLPanel
import accessories.plugins.NodeNoteRegistration.NotesManager
import accessories.plugins.NodeNoteRegistration.NoteDocumentListener
import freemind.modes.common.plugins.NodeNoteBase
import accessories.plugins.NodeNoteRegistration.SouthPanel
import freemind.controller.Controller.SplitComponentType
import accessories.plugins.NodeNoteRegistration.SimplyHtmlResources
import com.inet.jortho.SpellChecker
import java.util.zip.ZipInputStream
import java.io.FileInputStream
import accessories.plugins.NodeAttributeTableRegistration.AttributeManager
import freemind.controller.Controller
import javax.swing.DefaultCellEditor
import javax.swing.RowSorter
import javax.swing.table.TableRowSorter
import freemind.controller.actions.generated.instance.AttributeTableProperties
import freemind.controller.actions.generated.instance.TableColumnOrder
import freemind.main.*
import java.awt.*
import java.lang.Exception
import java.util.*
import javax.swing.SortOrder

/**
 * JDayChooser is a bean for choosing a day.
 *
 * @author Kai Toedter
 * @version $LastChangedRevision: 104 $
 * @version $LastChangedDate: 2006-06-04 15:20:45 +0200 (So, 04 Jun 2006) $
 */
open class JDayChooser @JvmOverloads constructor(weekOfYearVisible: Boolean = false) : JPanel(), ActionListener, KeyListener, FocusListener {
    open class ChangeAwareButton
    /**
     * @param pString
     */
    (pString: String?) : ScalableJButton(pString) {
        /* (non-Javadoc)
		 * @see javax.swing.AbstractButton#setText(java.lang.String)
		 */
        override fun setText(pText: String) {
            if (text == pText) return
            super.setText(pText)
        }

        /* (non-Javadoc)
		 * @see javax.swing.JComponent#setForeground(java.awt.Color)
		 */
        override fun setForeground(pFg: Color) {
            if (!safeEquals(foreground, pFg)) super.setForeground(pFg)
        }

        /* (non-Javadoc)
		 * @see javax.swing.JComponent#setBackground(java.awt.Color)
		 */
        override fun setBackground(pBg: Color) {
            if (!safeEquals(background, pBg)) super.setBackground(pBg)
        }
    }

    protected var days: Array<JButton?>?
    protected var weeks: Array<JButton?>?
    var selectedDay: JButton?
        protected set
    protected var weekPanel: JPanel?

    /**
     * Returns the day panel.
     *
     * @return the day panel
     */
    var dayPanel: JPanel
        protected set
    protected var day = 0
    protected var oldDayBackgroundColor: Color? = null
    protected var selectedColor: Color? = null
    protected var sundayForeground: Color?
    protected var weekdayForeground: Color?
    protected var decorationBackgroundColor: Color?
    protected var dayNames: Array<String>
    protected var calendar: Calendar?
    protected var today: Calendar
    protected var locale: Locale
    protected var initialized: Boolean
    protected var weekOfYearVisible: Boolean
    protected var decorationBackgroundVisible = true
    protected var decorationBordersVisible = false
    protected var dayBordersVisible = false
    private var alwaysFireDayProperty = false

    /**
     * Gets the minimum selectable date.
     *
     * @return the minimum selectable date
     */
    var minSelectableDate: Date
        protected set

    /**
     * Gets the maximum selectable date.
     *
     * @return the maximum selectable date
     */
    var maxSelectableDate: Date
        protected set
    protected var defaultMinSelectableDate: Date
    protected var defaultMaxSelectableDate: Date
    protected var maxDayCharacters = 0
    var redBorder = BorderFactory.createLineBorder(Color.RED, 2)
    protected var monthChooser: JMonthChooser? = null
    protected var yearChooser: JYearChooser? = null
    private var mCalendarMarkingEvaluator: ICalendarMarkingEvaluator? = null
    fun setMonthChooser(monthChooser: JMonthChooser?) {
        this.monthChooser = monthChooser
    }

    fun setYearChooser(yearChooser: JYearChooser?) {
        this.yearChooser = yearChooser
    }
    /**
     * JDayChooser constructor.
     *
     * @param weekOfYearVisible
     * true, if the weeks of a year shall be shown
     */
    /**
     * Default JDayChooser constructor.
     */
    init {
        if (mCalendarMarkingEvaluator == null) {
            var markings: CalendarMarkings? = null
            try {
                val marking: String = Resources.getInstance().getProperty(FreeMindCommon.TIME_MANAGEMENT_MARKING_XML)
                if (!marking.isEmpty()) {
                    markings = XmlBindingTools.getInstance().unMarshall(marking)
                }
            } catch (e: Exception) {
                Resources.getInstance().logException(e)
            }
            if (markings == null) {
                // empty one.
                markings = CalendarMarkings()
            }
            mCalendarMarkingEvaluator = CalendarMarkingEvaluator(markings)
            Controller.addPropertyChangeListener(object : FreemindPropertyListener {
                override fun propertyChanged(pPropertyName: String?, pNewValue: String?, pOldValue: String?) {
                    if (FreeMindCommon.TIME_MANAGEMENT_MARKING_XML == pPropertyName) {
                        val markings = XmlBindingTools.getInstance()
                                .unMarshall(pNewValue) as CalendarMarkings
                        mCalendarMarkingEvaluator.changeMarkings(markings)
                    }
                }
            })
        }
        name = "JDayChooser"
        background = Color.blue
        this.weekOfYearVisible = weekOfYearVisible
        locale = Locale.getDefault()
        days = arrayOfNulls<ScalableJButton>(49)
        selectedDay = null
        calendar = Calendar.getInstance(locale)
        today = calendar.clone() as Calendar
        layout = BorderLayout()
        dayPanel = JPanel()
        dayPanel.layout = GridLayout(7, 7)
        sundayForeground = Color(164, 0, 0)
        weekdayForeground = Color(0, 90, 164)

        // decorationBackgroundColor = new Color(194, 211, 252);
        // decorationBackgroundColor = new Color(206, 219, 246);
        decorationBackgroundColor = Color(210, 228, 238)
        for (y in 0..6) {
            for (x in 0..6) {
                val index = x + 7 * y
                if (y == 0) {
                    // Create a button that doesn't react on clicks or focus
                    // changes.
                    // Thanks to Thomas Schaefer for the focus hint :)
                    days.get(index) = DecoratorButton()
                } else {
                    days.get(index) = object : ChangeAwareButton("x") {
                        private static
                        val serialVersionUID = -7433645992591669725L
                        override fun paint(g: Graphics) {
                            if (isWindows || isMacOsX) {
                                // this is a hack to get the background painted
                                // when using Windows Look & Feel
                                if (selectedDay === this) {
                                    g.color = selectedColor
                                    g.fillRect(0, 0, width, height)
                                }
                            }
                            super.paint(g)
                        }
                    }
                    addListeners(index)
                }
                days.get(index).setMargin(Insets(0, 0, 0, 0))
                days.get(index).setFocusPainted(false)
                dayPanel.add(days.get(index))
            }
        }
        weekPanel = JPanel()
        weekPanel!!.layout = GridLayout(7, 1)
        weeks = arrayOfNulls<ScalableJButton>(7)
        for (i in 0..6) {
            weeks.get(i) = DecoratorButton()
            weeks.get(i).setMargin(Insets(0, 0, 0, 0))
            weeks.get(i).setFocusPainted(false)
            weeks.get(i).setForeground(Color(100, 100, 100))
            if (i != 0) {
                weeks.get(i).setText("0" + (i + 1))
            }
            weekPanel!!.add(weeks.get(i))
        }
        val tmpCalendar = Calendar.getInstance()
        tmpCalendar[1, 0, 1, 1] = 1
        defaultMinSelectableDate = tmpCalendar.time
        minSelectableDate = defaultMinSelectableDate
        tmpCalendar[9999, 0, 1, 1] = 1
        defaultMaxSelectableDate = tmpCalendar.time
        maxSelectableDate = defaultMaxSelectableDate
        init()
        setDay(Calendar.getInstance()[Calendar.DAY_OF_MONTH])
        add(dayPanel, BorderLayout.CENTER)
        if (weekOfYearVisible) {
            add(weekPanel, BorderLayout.WEST)
        }
        initialized = true
        updateUI()
    }

    fun addListeners(index: Int) {
        days!![index]!!.addActionListener(this)
        days!![index]!!.addKeyListener(this)
        days!![index]!!.addFocusListener(this)
    }

    /**
     * Initializes the locale specific names for the days of the week.
     */
    protected open fun init() {
        val testButton: JButton = ScalableJButton()
        oldDayBackgroundColor = testButton.background
        selectedColor = Color(160, 160, 160)
        val date = calendar!!.time
        calendar = Calendar.getInstance(locale)
        calendar.setTime(date)
        drawDayNames()
        drawDays()
    }

    /**
     * Draws the day names of the day columnes.
     */
    private fun drawDayNames() {
        val firstDayOfWeek = calendar!!.firstDayOfWeek
        val dateFormatSymbols = DateFormatSymbols(locale)
        dayNames = dateFormatSymbols.shortWeekdays
        var day = firstDayOfWeek
        for (i in 0..6) {
            if (maxDayCharacters > 0 && maxDayCharacters < 5) {
                if (dayNames[day].length >= maxDayCharacters) {
                    dayNames[day] = dayNames[day]
                            .substring(0, maxDayCharacters)
                }
            }
            days!![i]!!.text = dayNames[day]
            if (day == 1) {
                days!![i]!!.foreground = sundayForeground
            } else {
                days!![i]!!.foreground = weekdayForeground
            }
            if (day < 7) {
                day++
            } else {
                day -= 6
            }
        }
    }

    /**
     * Initializes both day names and weeks of the year.
     */
    protected fun initDecorations() {
        for (x in 0..6) {
            days!![x]!!.isContentAreaFilled = decorationBackgroundVisible
            days!![x]!!.isBorderPainted = decorationBordersVisible
            days!![x]!!.invalidate()
            days!![x]!!.repaint()
            weeks!![x]!!.isContentAreaFilled = decorationBackgroundVisible
            weeks!![x]!!.isBorderPainted = decorationBordersVisible
            weeks!![x]!!.invalidate()
            weeks!![x]!!.repaint()
        }
    }

    /**
     * Hides and shows the week buttons.
     */
    protected fun drawWeeks() {
        val tmpCalendar = calendar!!.clone() as Calendar
        for (i in 1..6) {
            tmpCalendar[Calendar.DAY_OF_MONTH] = i * 7 - 6
            val week = tmpCalendar[Calendar.WEEK_OF_YEAR]
            var buttonText = Integer.toString(week)
            if (week < 10) {
                buttonText = "0$buttonText"
            }
            val currentWeek = weeks!![i]
            if (currentWeek!!.text != buttonText) currentWeek.text = buttonText
            if (i == 5 || i == 6) {
                currentWeek.isVisible = days!![i * 7]!!.isVisible
            }
        }
    }

    /**
     * Hides and shows the day buttons.
     */
    protected fun drawDays() {
        val tmpCalendar = calendar!!.clone() as Calendar
        tmpCalendar[Calendar.HOUR_OF_DAY] = 0
        tmpCalendar[Calendar.MINUTE] = 0
        tmpCalendar[Calendar.SECOND] = 0
        tmpCalendar[Calendar.MILLISECOND] = 0
        val minCal = Calendar.getInstance()
        minCal.time = minSelectableDate
        minCal[Calendar.HOUR_OF_DAY] = 0
        minCal[Calendar.MINUTE] = 0
        minCal[Calendar.SECOND] = 0
        minCal[Calendar.MILLISECOND] = 0
        val maxCal = Calendar.getInstance()
        maxCal.time = maxSelectableDate
        maxCal[Calendar.HOUR_OF_DAY] = 0
        maxCal[Calendar.MINUTE] = 0
        maxCal[Calendar.SECOND] = 0
        maxCal[Calendar.MILLISECOND] = 0
        val firstDayOfWeek = tmpCalendar.firstDayOfWeek
        tmpCalendar[Calendar.DAY_OF_MONTH] = 1
        var firstDay = tmpCalendar[Calendar.DAY_OF_WEEK] - firstDayOfWeek
        if (firstDay < 0) {
            firstDay += 7
        }
        var i: Int
        i = 0
        while (i < firstDay) {
            val currentDay = days!![i + 7]
            currentDay!!.isVisible = false
            currentDay.text = ""
            i++
        }
        val clone = tmpCalendar.clone() as Calendar
        clone.add(Calendar.MONTH, 1)
        val firstDayInNextMonth = clone.time
        var day = tmpCalendar.time
        var n = 0
        val foregroundColor = foreground
        while (day.before(firstDayInNextMonth)) {
            val index = i + n + 7
            val currentDay = days!![index]
            val currentText = Integer.toString(n + 1)
            currentDay!!.text = currentText
            currentDay.isVisible = true
            if (tmpCalendar[Calendar.DAY_OF_YEAR] == today[Calendar.DAY_OF_YEAR]
                    && tmpCalendar[Calendar.YEAR] == today[Calendar.YEAR]) {
                currentDay.foreground = sundayForeground
            } else {
                currentDay.foreground = foregroundColor
            }
            if (n + 1 == this.day) {
                currentDay.background = selectedColor
                selectedDay = currentDay
            } else {
                currentDay.background = oldDayBackgroundColor
            }
            var currentBorder = NullBorder
            var currentToolTipText: String? = null
            if (mCalendarMarkingEvaluator != null) {
                val marked = mCalendarMarkingEvaluator
                        .isMarked(tmpCalendar)
                if (marked != null) {
                    currentBorder = BorderFactory.createLineBorder(xmlToColor(marked.color), 2)
                    currentToolTipText = marked.name
                }
            }
            currentDay.border = currentBorder
            currentDay.toolTipText = currentToolTipText
            if (tmpCalendar.before(minCal) || tmpCalendar.after(maxCal)) {
                currentDay.isEnabled = false
            } else {
                currentDay.isEnabled = true
            }
            n++
            tmpCalendar.add(Calendar.DATE, 1)
            day = tmpCalendar.time
        }
        for (k in n + i + 7..48) {
            val currentDay = days!![k]
            currentDay!!.isVisible = false
            currentDay.text = ""
            /* this was a try to display the days of the adjacent months, too,
			 * but some points are missing: 
			 * - only the last week should be filled
			 *  
			 */
//			days[k].setVisible(true);
//			days[k].setEnabled(false);
//			days[k].setText(Integer.toString(k-(n+i+6)));
        }
        drawWeeks()
    }

    /**
     * Returns the locale.
     *
     * @return the locale value
     *
     * @see .setLocale
     */
    override fun getLocale(): Locale {
        return locale
    }

    /**
     * Sets the locale.
     *
     * @param locale
     * the new locale value
     *
     * @see .getLocale
     */
    override fun setLocale(locale: Locale) {
        if (!initialized) {
            super.setLocale(locale)
        } else {
            this.locale = locale
            super.setLocale(locale)
            init()
        }
    }

    /**
     * Sets the day. This is a bound property.
     *
     * @param d
     * the day
     *
     * @see .getDay
     */
    fun setDay(d: Int) {
        var d = d
        if (d < 1) {
            d = 1
        }
        val maxDaysInMonth = daysInMonth
        if (d > maxDaysInMonth) {
            d = maxDaysInMonth
        }
        day = d
        if (selectedDay != null) {
            selectedDay!!.background = oldDayBackgroundColor
            selectedDay!!.repaint()
        }
        for (i in 7..48) {
            if (days!![i]!!.text == Integer.toString(day)) {
                selectedDay = days!![i]
                selectedDay!!.background = selectedColor
                break
            }
        }
        setFocus()
    }

    /**
     * this is needed for JDateChooser.
     *
     * @param alwaysFire
     * true, if day property shall be fired every time a day is
     * chosen.
     */
    fun setAlwaysFireDayProperty(alwaysFire: Boolean) {
        alwaysFireDayProperty = alwaysFire
    }

    /**
     * Returns the selected day.
     *
     * @return the day value
     *
     * @see .setDay
     */
    fun getDay(): Int {
        return day
    }

    val daysInMonth: Int
        get() = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)

    /**
     * Sets a specific year. This is needed for correct graphical representation
     * of the days.
     *
     * @param year
     * the new year
     */
    // Since the day does not change,
    var year: Int
        get() = calendar!![Calendar.YEAR]
        set(year) {
            calendar!![Calendar.YEAR] = year
            drawDays()
        }
    // don't fire a day property change, even if alwaysFireDayProperty is
    // true :)
    /**
     * Sets a specific month. This is needed for correct graphical
     * representation of the days.
     *
     * @param month
     * the new month
     */
    var month: Int
        get() = calendar!![Calendar.MONTH]
        set(month) {
            val maxDays = daysInMonth
            calendar!![Calendar.MONTH] = month
            if (maxDays == day) {
                day = daysInMonth
            }

            // Since the day does not change,
            // don't fire a day property change, even if alwaysFireDayProperty is
            // true :)
            val storedMode = alwaysFireDayProperty
            alwaysFireDayProperty = false
            setDay(day)
            alwaysFireDayProperty = storedMode
            drawDays()
        }

    /**
     * Sets a specific calendar. This is needed for correct graphical
     * representation of the days.
     *
     * @param calendar
     * the new calendar
     */
    fun setCalendar(calendar: Calendar?) {
        this.calendar = calendar
        drawDays()
    }

    /**
     * Sets the font property.
     *
     * @param font
     * the new font
     */
    override fun setFont(font: Font) {
        if (days != null) {
            for (i in 0..48) {
                days!![i]!!.font = font
            }
        }
        if (weeks != null) {
            for (i in 0..6) {
                weeks!![i]!!.font = font
            }
        }
    }

    /**
     * Sets the foregroundColor color.
     *
     * @param foreground
     * the new foregroundColor
     */
    override fun setForeground(foreground: Color) {
        super.setForeground(foreground)
        if (days != null) {
            for (i in 7..48) {
                days!![i]!!.foreground = foreground
            }
            drawDays()
        }
    }

    /**
     * JDayChooser is the ActionListener for all day buttons.
     *
     * @param e
     * the ActionEvent
     */
    override fun actionPerformed(e: ActionEvent) {
        val button = e.source as JButton
        val buttonText = button.text
        val day = buttonText.toInt()
        fire(day)
    }

    /**
     * JDayChooser is the FocusListener for all day buttons. (Added by Thomas
     * Schaefer)
     *
     * @param e
     * the FocusEvent
     */
    /*
	 * Code below commented out by Mark Brown on 24 Aug 2004. This code breaks
	 * the JDateChooser code by triggering the actionPerformed method on the
	 * next day button. This causes the date chosen to always be incremented by
	 * one day.
	 */
    override fun focusGained(e: FocusEvent) {
        // JButton button = (JButton) e.getSource();
        // String buttonText = button.getText();
        //
        // if ((buttonText != null) && !buttonText.equals("") &&
        // !e.isTemporary()) {
        // actionPerformed(new ActionEvent(e.getSource(), 0, null));
        // }
    }

    /**
     * Does nothing.
     *
     * @param e
     * the FocusEvent
     */
    override fun focusLost(e: FocusEvent) {}

    /**
     * JDayChooser is the KeyListener for all day buttons. (Added by Thomas
     * Schaefer and modified by Austin Moore)
     *
     * @param e
     * the KeyEvent
     */
    override fun keyPressed(e: KeyEvent) {
        // System.out.println("KEY " + e);
        var newDay = getDay()
        when (e.keyCode) {
            KeyEvent.VK_UP -> newDay -= 7
            KeyEvent.VK_DOWN -> newDay += 7
            KeyEvent.VK_LEFT -> newDay -= 1
            KeyEvent.VK_RIGHT -> newDay += 1
            KeyEvent.VK_PAGE_DOWN -> newDay = daysInMonth + diffMonth(1)
            KeyEvent.VK_PAGE_UP -> newDay = diffMonth(-1)
            KeyEvent.VK_END -> newDay = daysInMonth
            else -> return
        }
        fire(newDay)
    }

    fun fire(newDay: Int) {
        val tempCalendar = temporaryCalendar
        tempCalendar[Calendar.DAY_OF_MONTH] = newDay
        firePropertyChange(DAY_PROPERTY, null, tempCalendar)
    }

    private fun diffMonth(pMonthDiff: Int): Int {
        val tempCalendar = temporaryCalendar
        tempCalendar.add(Calendar.MONTH, pMonthDiff)
        val max = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val dayMonth = tempCalendar[Calendar.DAY_OF_MONTH]
        return if (pMonthDiff > 0) dayMonth else -max + dayMonth
    }

    val temporaryCalendar: GregorianCalendar
        get() = GregorianCalendar(
                yearChooser.getYear(), monthChooser!!.month, getDay())

    /**
     * Does nothing.
     *
     * @param e
     * the KeyEvent
     */
    override fun keyTyped(e: KeyEvent) {}

    /**
     * Does nothing.
     *
     * @param e
     * the KeyEvent
     */
    override fun keyReleased(e: KeyEvent) {}

    /**
     * Enable or disable the JDayChooser.
     *
     * @param enabled
     * The new enabled value
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        for (i in days!!.indices) {
            if (days!![i] != null) {
                days!![i]!!.isEnabled = enabled
            }
        }
        for (i in weeks!!.indices) {
            if (weeks!![i] != null) {
                weeks!![i]!!.isEnabled = enabled
            }
        }
    }

    /**
     * In some Countries it is often usefull to know in which week of the year a
     * date is.
     *
     * @return boolean true, if the weeks of the year is shown
     */
    fun isWeekOfYearVisible(): Boolean {
        return weekOfYearVisible
    }

    /**
     * In some Countries it is often usefull to know in which week of the year a
     * date is.
     *
     * @param weekOfYearVisible
     * true, if the weeks of the year shall be shown
     */
    fun setWeekOfYearVisible(weekOfYearVisible: Boolean) {
        if (weekOfYearVisible == this.weekOfYearVisible) {
            return
        } else if (weekOfYearVisible) {
            add(weekPanel, BorderLayout.WEST)
        } else {
            remove(weekPanel)
        }
        this.weekOfYearVisible = weekOfYearVisible
        validate()
        dayPanel.validate()
    }

    /**
     * Returns the color of the decoration (day names and weeks).
     *
     * @return the color of the decoration (day names and weeks).
     */
    fun getDecorationBackgroundColor(): Color? {
        return decorationBackgroundColor
    }

    /**
     * Sets the background of days and weeks of year buttons.
     *
     * @param decorationBackgroundColor
     * The background to set
     */
    fun setDecorationBackgroundColor(decorationBackgroundColor: Color?) {
        this.decorationBackgroundColor = decorationBackgroundColor
        if (days != null) {
            for (i in 0..6) {
                days!![i]!!.background = decorationBackgroundColor
            }
        }
        if (weeks != null) {
            for (i in 0..6) {
                weeks!![i]!!.background = decorationBackgroundColor
            }
        }
    }

    /**
     * Returns the Sunday foreground.
     *
     * @return Color the Sunday foreground.
     */
    fun getSundayForeground(): Color? {
        return sundayForeground
    }

    /**
     * Returns the weekday foreground.
     *
     * @return Color the weekday foreground.
     */
    fun getWeekdayForeground(): Color? {
        return weekdayForeground
    }

    /**
     * Sets the Sunday foreground.
     *
     * @param sundayForeground
     * The sundayForeground to set
     */
    fun setSundayForeground(sundayForeground: Color?) {
        this.sundayForeground = sundayForeground
        drawDayNames()
        drawDays()
    }

    /**
     * Sets the weekday foreground.
     *
     * @param weekdayForeground
     * The weekdayForeground to set
     */
    fun setWeekdayForeground(weekdayForeground: Color?) {
        this.weekdayForeground = weekdayForeground
        drawDayNames()
        drawDays()
    }

    /**
     * Requests that the selected day also have the focus.
     */
    fun setFocus() {
        if (selectedDay != null) {
            selectedDay!!.requestFocusInWindow()
        }
    }

    /**
     * The decoration background is the background color of the day titles and
     * the weeks of the year.
     *
     * @return Returns true, if the decoration background is painted.
     */
    fun isDecorationBackgroundVisible(): Boolean {
        return decorationBackgroundVisible
    }

    /**
     * The decoration background is the background color of the day titles and
     * the weeks of the year.
     *
     * @param decorationBackgroundVisible
     * true, if the decoration background shall be painted.
     */
    fun setDecorationBackgroundVisible(
            decorationBackgroundVisible: Boolean) {
        this.decorationBackgroundVisible = decorationBackgroundVisible
        initDecorations()
    }

    /**
     * The decoration border is the button border of the day titles and the
     * weeks of the year.
     *
     * @return Returns true, if the decoration border is painted.
     */
    fun isDecorationBordersVisible(): Boolean {
        return decorationBordersVisible
    }

    fun isDayBordersVisible(): Boolean {
        return dayBordersVisible
    }

    /**
     * The decoration border is the button border of the day titles and the
     * weeks of the year.
     *
     * @param decorationBordersVisible
     * true, if the decoration border shall be painted.
     */
    fun setDecorationBordersVisible(decorationBordersVisible: Boolean) {
        this.decorationBordersVisible = decorationBordersVisible
        initDecorations()
    }

    fun setDayBordersVisible(dayBordersVisible: Boolean) {
        this.dayBordersVisible = dayBordersVisible
        if (initialized) {
            for (x in 7..48) {
                if ("Windows" == UIManager.getLookAndFeel().id) {
                    days!![x]!!.isContentAreaFilled = dayBordersVisible
                } else {
                    days!![x]!!.isContentAreaFilled = true
                }
                days!![x]!!.isBorderPainted = dayBordersVisible
            }
        }
    }

    /**
     * Updates the UI and sets the day button preferences.
     */
    override fun updateUI() {
        super.updateUI()
        var font = Font.decode("Dialog Plain 11")
        val fontSize: Int = Resources.getInstance().getIntProperty(FreeMind.RESOURCES_CALENDAR_FONT_SIZE, 0)
        val scale: Int = Resources.getInstance().getIntProperty(FreeMind.SCALING_FACTOR_PROPERTY, 100)
        if (fontSize > 0) {
            font = font.deriveFont(fontSize * scale / 100f)
        } else if (scale != 100) {
            font = font.deriveFont(font.size * scale / 100f)
        }
        setFont(font)
        if (weekPanel != null) {
            weekPanel!!.updateUI()
        }
        if (initialized) {
            setDayBordersVisible(true)
            if ("Windows" == UIManager.getLookAndFeel().id) {
                setDecorationBackgroundVisible(true)
                setDecorationBordersVisible(false)
            } else {
                setDecorationBackgroundVisible(decorationBackgroundVisible)
            }
            setDecorationBordersVisible(decorationBordersVisible)
        }
    }

    /**
     * Sets a valid date range for selectable dates. If max is before min, the
     * default range with no limitation is set.
     *
     * @param min
     * the minimum selectable date or null (then the minimum date is
     * set to 01\01\0001)
     * @param max
     * the maximum selectable date or null (then the maximum date is
     * set to 01\01\9999)
     */
    fun setSelectableDateRange(min: Date?, max: Date?) {
        minSelectableDate = min ?: defaultMinSelectableDate
        maxSelectableDate = max ?: defaultMaxSelectableDate
        if (maxSelectableDate.before(minSelectableDate)) {
            minSelectableDate = defaultMinSelectableDate
            maxSelectableDate = defaultMaxSelectableDate
        }
        drawDays()
    }

    /**
     * Sets the maximum selectable date. If null, the date 01\01\9999 will be
     * set instead.
     *
     * @param max
     * the maximum selectable date
     *
     * @return the maximum selectable date
     */
    fun setMaxSelectableDate(max: Date?): Date {
        maxSelectableDate = max ?: defaultMaxSelectableDate
        drawDays()
        return maxSelectableDate
    }

    /**
     * Sets the minimum selectable date. If null, the date 01\01\0001 will be
     * set instead.
     *
     * @param min
     * the minimum selectable date
     *
     * @return the minimum selectable date
     */
    fun setMinSelectableDate(min: Date?): Date {
        minSelectableDate = min ?: defaultMinSelectableDate
        drawDays()
        return minSelectableDate
    }

    /**
     * Gets the maximum number of characters of a day name or 0. If 0 is
     * returned, dateFormatSymbols.getShortWeekdays() will be used.
     *
     * @return the maximum number of characters of a day name or 0.
     */
    fun getMaxDayCharacters(): Int {
        return maxDayCharacters
    }

    /**
     * Sets the maximum number of characters per day in the day bar. Valid
     * values are 0-4. If set to 0, dateFormatSymbols.getShortWeekdays() will be
     * used, otherwise theses strings will be reduced to the maximum number of
     * characters.
     *
     * @param maxDayCharacters
     * the maximum number of characters of a day name.
     */
    fun setMaxDayCharacters(maxDayCharacters: Int) {
        if (maxDayCharacters == this.maxDayCharacters) {
            return
        }
        if (maxDayCharacters < 0 || maxDayCharacters > 4) {
            this.maxDayCharacters = 0
        } else {
            this.maxDayCharacters = maxDayCharacters
        }
        drawDayNames()
        drawDays()
        invalidate()
    }

    internal inner class DecoratorButton : ScalableJButton() {
        init {
            background = decorationBackgroundColor
            isContentAreaFilled = decorationBackgroundVisible
            isBorderPainted = decorationBordersVisible
        }

        override fun addMouseListener(l: MouseListener) {}
        override fun isFocusable(): Boolean {
            return false
        }

        override fun paint(g: Graphics) {
            if ("Windows" == UIManager.getLookAndFeel().id) {
                // this is a hack to get the background painted
                // when using Windows Look & Feel
                if (decorationBackgroundVisible) {
                    g.color = decorationBackgroundColor
                } else {
                    g.color = days!![7]!!.background
                }
                g.fillRect(0, 0, width, height)
                isContentAreaFilled = if (isBorderPainted) {
                    true
                } else {
                    false
                }
            }
            super.paint(g)
        }

        companion object {
            private const val serialVersionUID = -5306477668406547496L
        }
    }

    companion object {
        const val DAY_PROPERTY = "day"
        private const val serialVersionUID = 5876398337018781820L
        private val NullBorder = BorderFactory.createLineBorder(Color.BLACK, 0)

        /**
         * Creates a JFrame with a JDayChooser inside and can be used for testing.
         *
         * @param s
         * The command line arguments
         */
        @JvmStatic
        fun main(s: Array<String>) {
            val frame = JFrame("JDayChooser")
            val dayChooser = JDayChooser()
            frame.contentPane.add(dayChooser)
            dayChooser.setWeekOfYearVisible(true)
            frame.pack()
            frame.isVisible = true
        }
    }
}