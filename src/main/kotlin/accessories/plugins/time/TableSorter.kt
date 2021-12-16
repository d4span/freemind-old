/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
import freemind.main.Tools
import javax.swing.WindowConstants
import accessories.plugins.time.TimeList.FilterTextDocumentListener
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
import java.util.TimerTask
import javax.swing.SwingUtilities
import java.lang.Runnable
import freemind.common.ScalableJTable
import javax.swing.table.TableCellRenderer
import javax.swing.table.DefaultTableCellRenderer
import java.text.DateFormat
import freemind.main.HtmlTools
import freemind.modes.MindIcon
import java.util.Collections
import freemind.modes.ModeController
import freemind.view.mindmapview.MultipleImage
import freemind.view.MapModule
import kotlin.jvm.JvmOverloads
import java.util.Locale
import javax.swing.JPanel
import java.beans.PropertyChangeListener
import java.util.Calendar
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
import javax.swing.JSpinner
import javax.swing.SwingConstants
import javax.swing.event.ChangeEvent
import javax.swing.SpinnerNumberModel
import javax.swing.event.CaretEvent
import java.lang.NumberFormatException
import javax.swing.UIManager
import accessories.plugins.time.JSpinField
import freemind.common.ScalableJButton
import javax.swing.border.Border
import accessories.plugins.time.ICalendarMarkingEvaluator
import freemind.controller.actions.generated.instance.CalendarMarkings
import freemind.main.FreeMindCommon
import freemind.common.XmlBindingTools
import accessories.plugins.time.CalendarMarkingEvaluator
import freemind.preferences.FreemindPropertyListener
import accessories.plugins.time.JDayChooser.DecoratorButton
import accessories.plugins.time.JDayChooser.ChangeAwareButton
import java.text.DateFormatSymbols
import freemind.controller.actions.generated.instance.CalendarMarking
import java.util.GregorianCalendar
import freemind.main.FreeMind
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableModel
import javax.swing.table.JTableHeader
import javax.swing.event.TableModelListener
import javax.swing.Icon
import java.util.Arrays
import javax.swing.event.TableModelEvent
import javax.swing.table.TableColumnModel
import javax.swing.JComboBox
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
import java.util.Properties
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
import java.util.TreeSet
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
import com.jgoodies.forms.builder.ButtonBarBuilder
import freemind.extensions.ModeControllerHookAdapter
import accessories.plugins.NodeNoteRegistration
import javax.swing.JViewport
import accessories.plugins.SortNodes.NodeTextComparator
import javax.swing.text.html.HTMLEditorKit
import freemind.main.FixedHTMLWriter
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import accessories.plugins.UnfoldAll
import freemind.common.OptionalDontShowMeAgainDialog
import freemind.common.OptionalDontShowMeAgainDialog.StandardPropertyHandler
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter
import freemind.modes.ModeController.NodeLifetimeListener
import accessories.plugins.ClonePasteAction.ClonePropertiesObserver
import accessories.plugins.ClonePasteAction.CloneProperties
import accessories.plugins.ClonePlugin
import freemind.main.XMLElement
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
import freemind.main.LogFileLogHandler
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
import java.util.LinkedList
import java.util.zip.ZipInputStream
import java.io.FileInputStream
import accessories.plugins.NodeAttributeTableRegistration.AttributeManager
import javax.swing.DefaultCellEditor
import javax.swing.RowSorter
import javax.swing.table.TableRowSorter
import freemind.controller.actions.generated.instance.AttributeTableProperties
import freemind.controller.actions.generated.instance.TableColumnOrderimport

java.awt.*import java.awt.event.*
import java.util.ArrayListimport

java.util.Comparator
import javax.swing.SortOrder
import java.util.Enumerationimport

java.util.HashMap
/**
 * TableSorter is a decorator for TableModels; adding sorting functionality to a
 * supplied TableModel. TableSorter does not store or copy the data in its
 * TableModel; instead it maintains a map from the row indexes of the view to
 * the row indexes of the model. As requests are made of the sorter (like
 * getValueAt(row, col)) they are passed to the underlying model after the row
 * numbers have been translated via the internal mapping array. This way, the
 * TableSorter appears to hold another copy of the table with the rows in a
 * different order.
 *
 *
 * TableSorter registers itself as a listener to the underlying model, just as
 * the JTable itself would. Events recieved from the model are examined,
 * sometimes manipulated (typically widened), and then passed on to the
 * TableSorter's listeners (typically the JTable). If a change to the model has
 * invalidated the order of TableSorter's rows, a note of this is made and the
 * sorter will resort the rows the next time a value is requested.
 *
 *
 * When the tableHeader property is set, either by using the setTableHeader()
 * method or the two argument constructor, the table header may be used as a
 * complete UI for TableSorter. The default renderer of the tableHeader is
 * decorated with a renderer that indicates the sorting status of each column.
 * In addition, a mouse listener is installed with the following behavior:
 *
 *  *
 * Mouse-click: Clears the sorting status of all other columns and advances the
 * sorting status of that column through three values: {NOT_SORTED, ASCENDING,
 * DESCENDING} (then back to NOT_SORTED again).
 *  *
 * SHIFT-mouse-click: Clears the sorting status of all other columns and cycles
 * the sorting status of the column through the same three values, in the
 * opposite order: {NOT_SORTED, DESCENDING, ASCENDING}.
 *  *
 * CONTROL-mouse-click and CONTROL-SHIFT-mouse-click: as above except that the
 * changes to the column do not cancel the statuses of columns that are already
 * sorting - giving a way to initiate a compound sort.
 *
 *
 *
 * This is a long overdue rewrite of a class of the same name that first
 * appeared in the swing table demos in 1997.
 *
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @version 2.0 02/27/04
 */
class TableSorter() : AbstractTableModel() {
    protected var tableModel: TableModel? = null
    private var viewToModel: Array<Row?>?
    private var modelToView: IntArray?
    private var tableHeader: JTableHeader? = null
    private val mouseListener: MouseListener
    private val tableModelListener: TableModelListener
    private val columnComparators: MutableMap<Class<*>, Comparator<*>> = HashMap()
    private val sortingColumns: MutableList<Directive> = ArrayList()

    init {
        mouseListener = MouseHandler()
        tableModelListener = TableModelHandler()
    }

    constructor(tableModel: TableModel?) : this() {
        setTableModel(tableModel)
    }

    constructor(tableModel: TableModel?, tableHeader: JTableHeader?) : this() {
        setTableHeader(tableHeader)
        setTableModel(tableModel)
    }

    private fun clearSortingState() {
        viewToModel = null
        modelToView = null
    }

    fun getTableModel(): TableModel? {
        return tableModel
    }

    fun setTableModel(tableModel: TableModel?) {
        if (this.tableModel != null) {
            this.tableModel!!.removeTableModelListener(tableModelListener)
        }
        this.tableModel = tableModel
        if (this.tableModel != null) {
            this.tableModel!!.addTableModelListener(tableModelListener)
        }
        clearSortingState()
        fireTableStructureChanged()
    }

    fun getTableHeader(): JTableHeader? {
        return tableHeader
    }

    fun setTableHeader(tableHeader: JTableHeader?) {
        if (this.tableHeader != null) {
            this.tableHeader!!.removeMouseListener(mouseListener)
            val defaultRenderer = this.tableHeader!!
                    .defaultRenderer
            if (defaultRenderer is SortableHeaderRenderer) {
                this.tableHeader!!.defaultRenderer = defaultRenderer.tableCellRenderer
            }
        }
        this.tableHeader = tableHeader
        if (this.tableHeader != null) {
            this.tableHeader!!.addMouseListener(mouseListener)
            this.tableHeader!!.defaultRenderer = SortableHeaderRenderer(
                    this.tableHeader!!.defaultRenderer)
        }
    }

    val isSorting: Boolean
        get() = sortingColumns.size != 0

    private fun getDirective(column: Int): Directive {
        for (i in sortingColumns.indices) {
            val directive = sortingColumns[i]
            if (directive.column == column) {
                return directive
            }
        }
        return EMPTY_DIRECTIVE
    }

    fun getSortingStatus(column: Int): Int {
        return getDirective(column).direction
    }

    private fun sortingStatusChanged() {
        clearSortingState()
        fireTableDataChanged()
        if (tableHeader != null) {
            tableHeader!!.repaint()
        }
    }

    fun setSortingStatus(column: Int, status: Int) {
        val directive = getDirective(column)
        if (directive !== EMPTY_DIRECTIVE) {
            sortingColumns.remove(directive)
        }
        if (status != NOT_SORTED) {
            sortingColumns.add(Directive(column, status))
        }
        sortingStatusChanged()
    }

    protected fun getHeaderRendererIcon(column: Int, size: Int): Icon? {
        val directive = getDirective(column)
        return if (directive === EMPTY_DIRECTIVE) {
            null
        } else Arrow(directive.direction == DESCENDING, size,
                sortingColumns.indexOf(directive))
    }

    private fun cancelSorting() {
        sortingColumns.clear()
        sortingStatusChanged()
    }

    fun setColumnComparator(type: Class<*>, comparator: Comparator<*>?) {
        if (comparator == null) {
            columnComparators.remove(type)
        } else {
            columnComparators[type] = comparator
        }
    }

    protected fun getComparator(column: Int): Comparator<*> {
        val columnType = tableModel!!.getColumnClass(column)
        val comparator = columnComparators[columnType]
        if (comparator != null) {
            return comparator
        }
        return if (Comparable::class.java.isAssignableFrom(columnType)) {
            COMPARABLE_COMAPRATOR
        } else LEXICAL_COMPARATOR
    }

    private fun getViewToModel(): Array<Row?> {
        if (viewToModel == null) {
            val tableModelRowCount = tableModel!!.rowCount
            viewToModel = arrayOfNulls(tableModelRowCount)
            for (row in 0 until tableModelRowCount) {
                viewToModel!![row] = Row(row)
            }
            if (isSorting) {
                Arrays.sort(viewToModel)
            }
        }
        return viewToModel!!
    }

    fun modelIndex(viewIndex: Int): Int {
        return getViewToModel()[viewIndex]!!.modelIndex
    }

    private fun getModelToView(): IntArray {
        if (modelToView == null) {
            val n = getViewToModel().size
            modelToView = IntArray(n)
            for (i in 0 until n) {
                modelToView!![modelIndex(i)] = i
            }
        }
        return modelToView!!
    }

    // TableModel interface methods
    override fun getRowCount(): Int {
        return if (tableModel == null) 0 else tableModel!!.rowCount
    }

    override fun getColumnCount(): Int {
        return if (tableModel == null) 0 else tableModel!!.columnCount
    }

    override fun getColumnName(column: Int): String {
        return tableModel!!.getColumnName(column)
    }

    override fun getColumnClass(column: Int): Class<*> {
        return tableModel!!.getColumnClass(column)
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return tableModel!!.isCellEditable(modelIndex(row), column)
    }

    override fun getValueAt(row: Int, column: Int): Any {
        return tableModel!!.getValueAt(modelIndex(row), column)
    }

    override fun setValueAt(aValue: Any, row: Int, column: Int) {
        tableModel!!.setValueAt(aValue, modelIndex(row), column)
    }

    // Helper classes
    private inner class Row(val modelIndex: Int) : Comparable<Row> {
        override fun compareTo(o: Row): Int {
            val row1 = modelIndex
            val row2 = o.modelIndex
            val it: Iterator<Directive> = sortingColumns.iterator()
            while (it.hasNext()) {
                val directive = it.next()
                val column = directive.column
                val o1 = tableModel!!.getValueAt(row1, column)
                val o2 = tableModel!!.getValueAt(row2, column)
                var comparison = 0
                // Define null less than everything, except null.
                comparison = if (o1 == null && o2 == null) {
                    0
                } else if (o1 == null) {
                    -1
                } else if (o2 == null) {
                    1
                } else {
                    getComparator(column).compare(o1, o2)
                }
                if (comparison != 0) {
                    return if (directive.direction == DESCENDING) -comparison else comparison
                }
            }
            return 0
        }
    }

    private inner class TableModelHandler : TableModelListener {
        override fun tableChanged(e: TableModelEvent) {
            // If we're not sorting by anything, just pass the event along.
            if (!isSorting) {
                clearSortingState()
                fireTableChanged(e)
                return
            }

            // If the table structure has changed, cancel the sorting; the
            // sorting columns may have been either moved or deleted from
            // the model.
            if (e.firstRow == TableModelEvent.HEADER_ROW) {
                cancelSorting()
                fireTableChanged(e)
                return
            }

            // We can map a cell event through to the view without widening
            // when the following conditions apply:
            //
            // a) all the changes are on one row (e.getFirstRow() ==
            // e.getLastRow()) and,
            // b) all the changes are in one column (column !=
            // TableModelEvent.ALL_COLUMNS) and,
            // c) we are not sorting on that column (getSortingStatus(column) ==
            // NOT_SORTED) and,
            // d) a reverse lookup will not trigger a sort (modelToView != null)
            //
            // Note: INSERT and DELETE events fail this test as they have column
            // == ALL_COLUMNS.
            //
            // The last check, for (modelToView != null) is to see if
            // modelToView
            // is already allocated. If we don't do this check; sorting can
            // become
            // a performance bottleneck for applications where cells
            // change rapidly in different parts of the table. If cells
            // change alternately in the sorting column and then outside of
            // it this class can end up re-sorting on alternate cell updates -
            // which can be a performance problem for large tables. The last
            // clause avoids this problem.
            val column = e.column
            if (e.firstRow == e.lastRow && column != TableModelEvent.ALL_COLUMNS && getSortingStatus(column) == NOT_SORTED && modelToView != null) {
                val viewIndex = getModelToView()[e.firstRow]
                fireTableChanged(TableModelEvent(this@TableSorter,
                        viewIndex, viewIndex, column, e.type))
                return
            }

            // Something has happened to the data that may have invalidated the
            // row order.
            clearSortingState()
            fireTableDataChanged()
            return
        }
    }

    private inner class MouseHandler : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            val h = e.source as JTableHeader
            val columnModel = h.columnModel
            val viewColumn = columnModel.getColumnIndexAtX(e.x)
            val column = columnModel.getColumn(viewColumn).modelIndex
            if (column != -1) {
                var status = getSortingStatus(column)
                if (!e.isControlDown) {
                    cancelSorting()
                }
                // Cycle the sorting states through {NOT_SORTED, ASCENDING,
                // DESCENDING} or
                // {NOT_SORTED, DESCENDING, ASCENDING} depending on whether
                // shift is pressed.
                status = status + if (e.isShiftDown) -1 else 1
                status = (status + 4) % 3 - 1 // signed mod, returning {-1, 0,
                // 1}
                setSortingStatus(column, status)
            }
        }
    }

    private class Arrow(private val descending: Boolean, private val size: Int, private val priority: Int) : Icon {
        override fun paintIcon(c: Component, g: Graphics, x: Int, y: Int) {
            var y = y
            val color = if (c == null) Color.GRAY else c.background
            // In a compound sort, make each succesive triangle 20%
            // smaller than the previous one.
            val dx = (size / 2 * Math.pow(0.8, priority.toDouble())).toInt()
            val dy = if (descending) dx else -dx
            // Align icon (roughly) with font baseline.
            y = y + 5 * size / 6 + if (descending) -dy else 0
            val shift = if (descending) 1 else -1
            g.translate(x, y)

            // Right diagonal.
            g.color = color.darker()
            g.drawLine(dx / 2, dy, 0, 0)
            g.drawLine(dx / 2, dy + shift, 0, shift)

            // Left diagonal.
            g.color = color.brighter()
            g.drawLine(dx / 2, dy, dx, 0)
            g.drawLine(dx / 2, dy + shift, dx, shift)

            // Horizontal line.
            if (descending) {
                g.color = color.darker().darker()
            } else {
                g.color = color.brighter().brighter()
            }
            g.drawLine(dx, 0, 0, 0)
            g.color = color
            g.translate(-x, -y)
        }

        override fun getIconWidth(): Int {
            return size
        }

        override fun getIconHeight(): Int {
            return size
        }
    }

    private inner class SortableHeaderRenderer(val tableCellRenderer: TableCellRenderer) : TableCellRenderer {
        override fun getTableCellRendererComponent(table: JTable,
                                                   value: Any, isSelected: Boolean, hasFocus: Boolean, row: Int,
                                                   column: Int): Component {
            val c = tableCellRenderer.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column)
            if (c is JLabel) {
                val l = c
                l.horizontalTextPosition = JLabel.LEFT
                val modelColumn = table.convertColumnIndexToModel(column)
                l.icon = getHeaderRendererIcon(modelColumn, l.font
                        .size)
            }
            return c
        }
    }

    private class Directive(val column: Int, val direction: Int)
    companion object {
        const val DESCENDING = -1
        const val NOT_SORTED = 0
        const val ASCENDING = 1
        private val EMPTY_DIRECTIVE = Directive(-1, NOT_SORTED)
        val COMPARABLE_COMAPRATOR: Comparator<*> = Comparator<Comparable<*>> { o1, o2 -> o1.compareTo(o2) }
        val LEXICAL_COMPARATOR: Comparator<*> = Comparator<Any?> { o1, o2 -> o1.toString().compareTo(o2.toString(), ignoreCase = true) }
    }
}