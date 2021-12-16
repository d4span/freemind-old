/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 04.02.2005
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
import accessories.plugins.time.TimeList.NodeRenderer
import accessories.plugins.time.TimeList.IconsRenderer
import accessories.plugins.time.FlatNodeTableFilterModel
import accessories.plugins.time.TimeList.NotesRenderer
import freemind.modes.mindmapmode.MindMapController
import accessories.plugins.time.TimeList
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
import accessories.plugins.time.TimeList.ReplaceAllInfo
import accessories.plugins.time.TimeList.ReplaceSelectedInfo
import accessories.plugins.time.TimeList.ToggleViewFoldedNodesAction
import freemind.controller.StructuredMenuHolder
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
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
import javax.swing.event.DocumentListener
import accessories.plugins.time.TimeList.FilterTextDocumentListener.DelayedTextEntry
import java.lang.Runnable
import java.awt.event.MouseAdapter
import java.awt.event.KeyListener
import freemind.common.ScalableJTable
import javax.swing.table.TableCellRenderer
import javax.swing.table.DefaultTableCellRenderer
import java.text.DateFormat
import freemind.view.mindmapview.MultipleImage
import freemind.view.MapModule
import kotlin.jvm.JvmOverloads
import java.beans.PropertyChangeListener
import accessories.plugins.time.JDayChooser
import accessories.plugins.time.JMonthChooser
import accessories.plugins.time.JYearChooser
import java.beans.PropertyChangeEvent
import accessories.plugins.time.JCalendar
import kotlin.jvm.JvmStatic
import javax.swing.event.ChangeListener
import javax.swing.event.CaretListener
import java.awt.event.ActionListener
import java.awt.event.FocusListener
import javax.swing.event.ChangeEvent
import javax.swing.event.CaretEvent
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
import accessories.plugins.time.JDayChooser.DecoratorButton
import accessories.plugins.time.JDayChooser.ChangeAwareButton
import java.text.DateFormatSymbols
import freemind.controller.actions.generated.instance.CalendarMarking
import java.awt.event.MouseListener
import javax.swing.table.AbstractTableModel
import javax.swing.table.TableModel
import javax.swing.table.JTableHeader
import javax.swing.event.TableModelListener
import javax.swing.event.TableModelEvent
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
import accessories.plugins.time.JTripleCalendar.JSwitchableCalendar
import javax.swing.border.LineBorder
import tests.freemind.FreeMindMainMock
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter
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
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import accessories.plugins.util.xslt.ExportDialog
import accessories.plugins.util.xslt.XmlExporter
import accessories.plugins.util.window.WindowClosingAdapter
import accessories.plugins.util.xslt.ExportDialog.FileChooseListener
import accessories.plugins.util.xslt.ExportDialog.ExportListener
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.UnsupportedFlavorException
import java.awt.datatransfer.DataFlavor
import accessories.plugins.dialogs.ListTransferHandler.ListTransferable
import java.lang.ClassNotFoundException
import freemind.common.TextTranslator
import accessories.plugins.dialogs.ChooseFormatPopupDialog
import freemind.modes.mindmapmode.dialogs.StylePatternFrame
import accessories.plugins.dialogs.ChooseFormatPopupDialog.DemoMapFeedback
import freemind.modes.mindmapmode.MindMapMapModel
import freemind.main.Tools.StringReaderCreator
import freemind.modes.mindmapmode.dialogs.StylePatternFrame.StylePatternFrameType
import accessories.plugins.dialogs.ManagePatternsPopupDialog
import freemind.controller.actions.generated.instance.ManageStyleEditorWindowConfigurationStorage
import accessories.plugins.dialogs.ManagePatternsPopupDialog.PatternListSelectionListener
import java.awt.event.MouseMotionListener
import com.jgoodies.forms.builder.ButtonBarBuilder
import freemind.extensions.ModeControllerHookAdapter
import accessories.plugins.NodeNoteRegistration
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
import accessories.plugins.ExportToOoWriter
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import freemind.extensions.UndoEventReceiver
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
import java.util.zip.ZipInputStream
import java.io.FileInputStream
import accessories.plugins.NodeAttributeTableRegistration.AttributeManager
import javax.swing.table.TableRowSorter
import freemind.controller.actions.generated.instance.AttributeTableProperties
import freemind.controller.actions.generated.instance.TableColumnOrder
import freemind.main.*
import freemind.modes.*
import java.awt.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*
import javax.swing.*

//FIXME: Reminder: more than once. (later)
/**
 * @author foltin
 */
class TimeManagement : MindMapHookAdapter(), PropertyChangeListener, ActionListener, MapModuleChangeObserver {
    private interface NodeFactory {
        fun getNode(pNode: MindMapNode?): MindMapNode?
    }

    private open inner class AppendDateAbstractAction : AbstractAction() {
        private var mFactory: NodeFactory? = null
        fun init(pFactory: NodeFactory?, pText: String?) {
            mFactory = pFactory
            putValue(NAME, mindMapController!!.getText(pText))
        }

        override fun actionPerformed(actionEvent: ActionEvent) {
            var lastElement: MindMapNode? = null
            val sel = Vector<MindMapNode?>()
            for (element in mindMapController!!.selecteds) {
                element = mFactory!!.getNode(element)
                val df = DateFormat.getDateInstance(DateFormat.SHORT)
                val dateAsString = df.format(calendarDate)
                mindMapController!!.setNodeText(element,
                        element!!.text + " " + dateAsString)
                lastElement = element
                sel.add(element)
            }
            mindMapController!!.select(lastElement, sel)
            requestFocusForDay()
        }
    }

    private inner class AppendDateAction : AppendDateAbstractAction() {
        init {
            init(object : NodeFactory {
                override fun getNode(pNode: MindMapNode?): MindMapNode? {
                    return pNode
                }
            }, "plugins/TimeManagement.xml_appendButton")
        }
    }

    private inner class AppendDateToChildAction : AppendDateAbstractAction() {
        init {
            init(object : NodeFactory {
                override fun getNode(pNode: MindMapNode?): MindMapNode? {
                    return mindMapController!!.addNewNode(pNode,
                            pNode!!.childCount, pNode.isLeft)
                }
            }, "plugins/TimeManagement.xml_appendAsNewButton")
        }
    }

    private inner class AppendDateToSiblingAction : AppendDateAbstractAction() {
        init {
            init(object : NodeFactory {
                override fun getNode(pNode: MindMapNode?): MindMapNode? {
                    var parent = pNode
                    if (!pNode!!.isRoot) {
                        parent = pNode.parentNode
                    }
                    return mindMapController!!.addNewNode(parent,
                            parent!!.getIndex(pNode) + 1, parent.isLeft)
                }
            }, "plugins/TimeManagement.xml_appendAsNewSiblingButton")
        }
    }

    private inner class RemindAction : AbstractAction(mindMapController!!.getText(
            "plugins/TimeManagement.xml_reminderButton")) {
        override fun actionPerformed(pE: ActionEvent) {
            this@TimeManagement.actionPerformed(pE)
        }
    }

    private inner class RemoveReminders : AbstractAction(mindMapController!!.getText(
            "plugins/TimeManagement.xml_removeReminderButton")) {
        override fun actionPerformed(e: ActionEvent) {
            for (node in mindMapController!!.selecteds) {
                val alreadyPresentHook: ReminderHookBase = TimeManagementOrganizer.Companion.getHook(node)
                if (alreadyPresentHook != null) {
                    addHook(node, 0L) // means remove hook, as it is already
                    // present.
                }
            }
        }
    }

    private inner class TodayAction : AbstractAction(mindMapController!!.getText(
            "plugins/TimeManagement.xml_todayButton")) {
        override fun actionPerformed(arg0: ActionEvent) {
            calendar!!.calendar = Calendar.getInstance()
        }
    }

    private inner class CloseAction : AbstractAction(mindMapController!!.getText(
            "plugins/TimeManagement.xml_closeButton")) {
        override fun actionPerformed(arg0: ActionEvent) {
            disposeDialog()
        }
    }

    private inner class AddMarkAction : AbstractAction() {
        init {
            putValue(NAME, mindMapController!!.getText("plugins/TimeManagement.xml_addMarkingsButton"))
        }

        override fun actionPerformed(actionEvent: ActionEvent) {
            val cal = getCalendar()
            val res: Resources = Resources.getInstance()
            val xml = res.getProperty(FreeMindCommon.TIME_MANAGEMENT_MARKING_XML)
            val bind: XmlBindingTools = XmlBindingTools.getInstance()
            val result = bind.unMarshall(xml) as CalendarMarkings?
            val dialog = CalendarMarkingDialog(mindMapController)
            dialog.setDates(cal)
            dialog.modalityType = Dialog.ModalityType.DOCUMENT_MODAL
            dialog.isVisible = true
            if (dialog.result == CalendarMarkingDialog.Companion.OK) {
                result!!.addCalendarMarking(dialog.calendarMarking)
                mindMapController!!.setProperty(FreeMindCommon.TIME_MANAGEMENT_MARKING_XML, bind.marshall(result))
                calendar!!.repaint()
            }
        }
    }

    private inner class RemoveMarkAction : AbstractAction() {
        init {
            putValue(NAME, mindMapController!!.getText("plugins/TimeManagement.removeMarkingsButton"))
        }

        override fun actionPerformed(actionEvent: ActionEvent) {
            val cal = getCalendar()
            val res: Resources = Resources.getInstance()
            val xml = res.getProperty(FreeMindCommon.TIME_MANAGEMENT_MARKING_XML)
            val bind: XmlBindingTools = XmlBindingTools.getInstance()
            val result = bind.unMarshall(xml) as CalendarMarkings?
            val ev = CalendarMarkingEvaluator(result)
            val marking = ev.isMarked(cal)
            if (marking != null) {
                for (i in 0 until result!!.sizeCalendarMarkingList()) {
                    val mark = result.getCalendarMarking(i)
                    if (mark == marking) {
                        result.removeFromCalendarMarkingElementAt(i)
                        break
                    }
                }
            }
            mindMapController!!.setProperty(FreeMindCommon.TIME_MANAGEMENT_MARKING_XML, bind.marshall(result))
            calendar!!.repaint()
        }
    }

    private var calendar: JTripleCalendar? = null
    private var mDialog: JDialog? = null
    private var timePanel: JPanel? = null
    private var hourField: JTextField? = null
    private var minuteField: JTextField? = null

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.modes.mindmapmode.hooks.MindMapHookAdapter#getMindMapController
	 * ()
	 */  override var mindMapController: MindMapController? = null
        private set

    override fun startupMapHook() {
        super.startupMapHook()
        if (sCurrentlyOpenTimeManagement != null) {
            sCurrentlyOpenTimeManagement!!.mDialog!!.contentPane.isVisible = true
            return
        }
        sCurrentlyOpenTimeManagement = this
        mindMapController = super.mindMapController
        mindMapController!!.controller!!.mapModuleManager
                .addListener(this)
        mDialog = JDialog(mindMapController!!.frame.jFrame,
                false /*
					 * not modal
					 */)
        mDialog!!.title = getResourceString("plugins/TimeManagement.xml_WindowTitle")
        mDialog!!.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        mDialog!!.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                disposeDialog()
            }
        })
        val closeAction: Action = CloseAction()
        addEscapeActionToDialog(mDialog!!, closeAction)
        /** Menu  */
        val menuHolder = StructuredMenuHolder()
        val menu = JMenuBar()
        menuHolder.addMenu(JMenu(mindMapController!!.getText(
                "TimeManagement.Actions")), "main/actions/.")
        addAccelerator(menuHolder.addAction(AppendDateAction(),
                "main/actions/append")!!,
                "keystroke_plugins/TimeManagement_append")
        addAccelerator(menuHolder.addAction(AppendDateToChildAction(),
                "main/actions/appendAsChild")!!,
                "keystroke_plugins/TimeManagement_appendAsChild")
        addAccelerator(menuHolder.addAction(AppendDateToSiblingAction(),
                "main/actions/appendAsSibling")!!,
                "keystroke_plugins/TimeManagement_appendAsSibling")
        val remindMenuItem = addAccelerator(
                menuHolder.addAction(RemindAction(), "main/actions/remind")!!,
                "keystroke_plugins/TimeManagementRemind")
        remindMenuItem.toolTipText = getResourceString("plugins/TimeManagement.xml_reminderButton_tooltip")
        val removeRemindersItem = addAccelerator(menuHolder.addAction(
                RemoveReminders(), "main/actions/removeReminders")!!,
                "keystroke_plugins/TimeManagementRemoveReminders")
        removeRemindersItem.toolTipText = getResourceString("plugins/TimeManagement.xml_removeReminderButton_tooltip")
        addAccelerator(
                menuHolder.addAction(TodayAction(), "main/actions/today")!!,
                "keystroke_plugins/TimeManagementToday")
        menuHolder.addAction(CloseAction(), "main/actions/close")
        menuHolder.addMenu(JMenu(mindMapController!!.getText(
                "TimeManagement.Markings")), "main/markings/.")
        addAccelerator(
                menuHolder.addAction(AddMarkAction(), "main/markings/add")!!,
                "keystroke_plugins/TimeManagement_add_marking")
        addAccelerator(
                menuHolder.addAction(RemoveMarkAction(), "main/markings/remove")!!,
                "keystroke_plugins/TimeManagement_remove_marking")
        menuHolder.updateMenus(menu, "main/")
        mDialog!!.jMenuBar = menu
        calendar = JTripleCalendar(lastActivePosition, lastDate)
        val contentPane = mDialog!!.contentPane
        contentPane.layout = GridBagLayout()
        val gb1 = GridBagConstraints()
        gb1.gridx = 0
        gb1.gridwidth = 4
        gb1.fill = GridBagConstraints.BOTH
        gb1.gridy = 0
        gb1.weightx = 1.0
        gb1.weighty = 1.0
        calendar.getDayChooser().addPropertyChangeListener(this)
        contentPane.add(calendar, gb1)
        run {
            val gb2 = GridBagConstraints()
            gb2.gridx = 0
            gb2.gridy = 1
            gb2.gridwidth = 4
            gb2.weightx = 0.0
            gb2.weighty = 0.0
            gb2.fill = GridBagConstraints.HORIZONTAL
            contentPane.add(getTimePanel(), gb2)
        }
        mDialog!!.pack()
        // focus fix after startup.
        mDialog!!.addWindowFocusListener(object : WindowAdapter() {
            override fun windowGainedFocus(e: WindowEvent) {
                requestFocusForDay()
                mDialog!!.removeWindowFocusListener(this)
            }
        })
        mindMapController!!.decorateDialog(mDialog, WINDOW_PREFERENCE_STORAGE_PROPERTY)
        mDialog!!.isVisible = true
    }

    /**
     */
    private fun getTimePanel(): JPanel {
        if (timePanel == null) {
            timePanel = JPanel()
            timePanel!!.layout = GridBagLayout()
            run {
                val gb2 = GridBagConstraints()
                gb2.gridx = 0
                gb2.gridy = 0
                gb2.fill = GridBagConstraints.HORIZONTAL
                timePanel!!.add(JLabel(
                        getResourceString("plugins/TimeManagement.xml_hour")),
                        gb2)
            }
            run {
                val gb2 = GridBagConstraints()
                gb2.gridx = 1
                gb2.gridy = 0
                gb2.fill = GridBagConstraints.HORIZONTAL
                hourField = JTextField(2)
                hourField!!.text = "" + Calendar.getInstance()[Calendar.HOUR_OF_DAY]
                timePanel!!.add(hourField, gb2)
            }
            run {
                val gb2 = GridBagConstraints()
                gb2.gridx = 2
                gb2.gridy = 0
                gb2.fill = GridBagConstraints.HORIZONTAL
                timePanel
                        .add(JLabel(
                                getResourceString("plugins/TimeManagement.xml_minute")),
                                gb2)
            }
            run {
                val gb2 = GridBagConstraints()
                gb2.gridx = 3
                gb2.gridy = 0
                gb2.fill = GridBagConstraints.HORIZONTAL
                minuteField = JTextField(2)
                var minuteString = Calendar.getInstance()[Calendar.MINUTE].toString()
                // padding with "0"
                if (minuteString.length < 2) {
                    minuteString = "0$minuteString"
                }
                minuteField!!.text = minuteString
                timePanel!!.add(minuteField, gb2)
            }
        }
        return timePanel!!
    }

    override fun propertyChange(event: PropertyChangeEvent) {
        if (event.propertyName == JDayChooser.Companion.DAY_PROPERTY) {
        }
    }

    override fun actionPerformed(arg0: ActionEvent) {
        val date = calendarDate
        // add permanent node hook to the nodes and this hook checks
        // permanently.
        for (node in mindMapController!!.selecteds) {
            val alreadyPresentHook: ReminderHookBase = TimeManagementOrganizer.Companion.getHook(node)
            if (alreadyPresentHook != null) {
                // already present:
                val messageArguments = arrayOf<Any>(
                        Date(alreadyPresentHook.remindUserAt), date)
                val formatter = MessageFormat(
                        mindMapController
                                .getText(
                                        "plugins/TimeManagement.xml_reminderNode_onlyOneDate"))
                val message = formatter.format(messageArguments)
                logger!!.info(messageArguments.size.toString() + ", " + message)
                val result = JOptionPane.showConfirmDialog(
                        mindMapController!!.frame.jFrame, message,
                        "FreeMind", JOptionPane.YES_NO_OPTION)
                if (result == JOptionPane.NO_OPTION) return
                // here, the old has to be removed and the new one installed.
                addHook(node, 0L) // means remove hook, as it is already
                // present.
            }
            addHook(node, date.time)
            val rh: ReminderHookBase = TimeManagementOrganizer.Companion.getHook(node)
                    ?: throw IllegalArgumentException(
                            "hook not found although it is present!!")
            node!!.invokeHook(rh)
            mindMapController!!.nodeChanged(node)
        }
        // disposeDialog();
    }

    /**
     * @param pRemindAt
     * TODO
     */
    private fun addHook(node: MindMapNode?, pRemindAt: Long) {
        // add the hook:
        val properties = Properties()
        if (pRemindAt != 0L) {
            properties[ReminderHookBase.REMINDUSERAT] = pRemindAt.toString()
        }
        mindMapController!!.addHook(node,
                getVectorWithSingleElement(node), REMINDER_HOOK_NAME,
                properties)
    }

    /**
     *
     */
    private fun disposeDialog() {
        val storage = WindowConfigurationStorage()
        mindMapController!!.storeDialogPositions(mDialog, storage,
                WINDOW_PREFERENCE_STORAGE_PROPERTY)
        mDialog!!.isVisible = false
        mDialog!!.dispose()
        lastDate = getCalendar()
        lastActivePosition = calendar.getCurrentMonthPosition()
        sCurrentlyOpenTimeManagement = null
    }

    /**
     */
    private val calendarDate: Date
        private get() {
            val cal = getCalendar()
            try {
                var value = 0
                value = hourField!!.text.toInt()
                cal!![Calendar.HOUR_OF_DAY] = value
                value = minuteField!!.text.toInt()
                cal[Calendar.MINUTE] = value
                cal[Calendar.SECOND] = 0
            } catch (e: Exception) {
            }
            return cal!!.time
        }

    protected fun getCalendar(): Calendar? {
        return calendar.getCalendar()
    }

    override fun afterMapClose(oldMapModule: MapModule?, oldMode: Mode?) {}
    override fun afterMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                      newMapModule: MapModule?, newMode: Mode) {
    }

    override fun beforeMapModuleChange(oldMapModule: MapModule?, oldMode: Mode?,
                                       newMapModule: MapModule?, newMode: Mode?) {
        mindMapController!!.controller!!.mapModuleManager
                .removeListener(this)
        disposeDialog()
    }

    override fun isMapModuleChangeAllowed(oldMapModule: MapModule?,
                                          oldMode: Mode?, newMapModule: MapModule?, newMode: Mode?): Boolean {
        return true
    }

    override fun numberOfOpenMapInformation(number: Int, pIndex: Int) {}

    /**
     *
     */
    private fun requestFocusForDay() {
        calendar.getDayChooser().getSelectedDay().requestFocus()
    }

    companion object {
        private val WINDOW_PREFERENCE_STORAGE_PROPERTY = TimeManagement::class.java
                .name + "_properties"
        const val REMINDER_HOOK_NAME = "plugins/TimeManagementReminder.xml"
        private var lastDate: Calendar? = null
        private var lastActivePosition = 4
        private var sCurrentlyOpenTimeManagement: TimeManagement? = null
    }
}