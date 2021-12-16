/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 11.11.2010
 */
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
import javax.swing.event.ListSelectionListener
import javax.swing.event.ListSelectionEvent
import freemind.modes.MindMapNode
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
import freemind.modes.MindIcon
import freemind.modes.ModeController
import freemind.view.mindmapview.MultipleImage
import freemind.view.MapModule
import kotlin.jvm.JvmOverloads
import java.beans.PropertyChangeListener
import accessories.plugins.time.JDayChooser
import accessories.plugins.time.JMonthChooser
import accessories.plugins.time.JYearChooser
import java.awt.BorderLayout
import java.beans.PropertyChangeEvent
import accessories.plugins.time.JCalendar
import java.awt.Color
import kotlin.jvm.JvmStatic
import javax.swing.event.ChangeListener
import javax.swing.event.CaretListener
import java.awt.event.ActionListener
import java.awt.event.FocusListener
import java.awt.Dimension
import javax.swing.event.ChangeEvent
import javax.swing.event.CaretEvent
import java.lang.NumberFormatException
import java.awt.event.FocusEvent
import accessories.plugins.time.JSpinField
import freemind.common.ScalableJButton
import javax.swing.border.Border
import accessories.plugins.time.ICalendarMarkingEvaluator
import freemind.common.XmlBindingTools
import accessories.plugins.time.CalendarMarkingEvaluator
import freemind.preferences.FreemindPropertyListener
import java.awt.GridLayout
import accessories.plugins.time.JDayChooser.DecoratorButton
import accessories.plugins.time.JDayChooser.ChangeAwareButton
import java.awt.Graphics
import java.text.DateFormatSymbols
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
import java.awt.Rectangle
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
import accessories.plugins.LogFileViewer.UpdateTextAreaThread
import java.util.logging.SimpleFormatter
import accessories.plugins.LogFileViewer
import accessories.plugins.LogFileViewer.PrintOperationAction
import accessories.plugins.LogFileViewer.SetLogLevelAction
import java.lang.InterruptedException
import accessories.plugins.ChangeRootNode
import freemind.view.mindmapview.NodeMotionListenerView
import accessories.plugins.ExportWithXSLT
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FilenameFilter
import accessories.plugins.util.html.ClickableImageCreator
import java.io.FileWriter
import accessories.plugins.RevisionPlugin
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
import freemind.main.Tools.MindMapNodePair
import accessories.plugins.ExportToOoWriter
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import freemind.extensions.UndoEventReceiver
import freemind.modes.IconInformation
import freemind.modes.mindmapmode.actions.IconAction
import freemind.modes.common.dialogs.IconSelectionPopupDialog
import accessories.plugins.JumpLastEditLocation.JumpLastEditLocationRegistration
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
import freemind.controller.actions.generated.instance.*
import freemind.main.*
import javax.swing.table.TableRowSorter
import freemind.modes.mindmapmode.actions.xml.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*
import java.util.logging.Logger
import javax.swing.*

/**
 * This plugin stores the location of last edit taken place in order to jump to
 * it on keystroke.
 *
 * @author foltin
 */
class JumpLastEditLocation : MindMapNodeHookAdapter() {
    override fun invoke(pNode: MindMapNode?) {
        super.invoke(pNode)
        try {
            val base = getPluginBaseClass() as JumpLastEditLocationRegistration
            val node = base.getLastEditLocation(pNode) ?: return
            logger!!.fine("Selecting $node as last edit location.")
            mindMapController!!.select(node,
                    getVectorWithSingleElement<MindMapNode?>(node))
        } catch (e: Exception) {
            Resources.getInstance().logException(e)
        }
    }

    class JumpLastEditLocationRegistration(controller: ModeController,
                                           map: MindMap?) : HookRegistration, ActionHandler, MenuItemEnabledListener {
        private val controller: MindMapController
        private val logger: Logger
        private val mLastEditLocations = Vector<String?>()
        fun getLastEditLocation(pCurrentNode: MindMapNode?): MindMapNode? {
            var pCurrentNode = pCurrentNode
            val size = mLastEditLocations.size
            if (size == 0) {
                return null
            }
            // search for the current node inside the vector:
            var id = controller.getNodeID(pCurrentNode)
            var index = mLastEditLocations.lastIndexOf(id)
            do {
                if (index < 0) {
                    // current node not present, we start with the last position:
                    index = size - 1
                } else {
                    index = index - 1
                    if (index < 0) {
                        index = 0
                    }
                }
                id = mLastEditLocations.elementAt(index)
                try {
                    pCurrentNode = controller.getNodeFromID(id)
                    return pCurrentNode
                } catch (e: IllegalArgumentException) {
                    // node not found, retry...
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                }
            } while (index > 0)
            return null
        }

        init {
            this.controller = controller as MindMapController
            logger = controller.frame.getLogger(this.javaClass.name)
        }

        override fun register() {
            controller.actionRegistry.registerHandler(this)
        }

        override fun deRegister() {
            controller.actionRegistry.deregisterHandler(this)
        }

        override fun executeAction(action: XmlAction?) {
            // detect format changes:
            detectFormatChanges(action)
        }

        /**
         */
        private fun detectFormatChanges(doAction: XmlAction?) {
            if (doAction is CompoundAction) {
                val i: Iterator<XmlAction> = doAction.listChoiceList.iterator()
                while (i.hasNext()) {
                    val childAction = i.next()
                    detectFormatChanges(childAction)
                }
            } else if (doAction is NodeAction
                    && doAction !is FoldAction) {
                // remove myself:
                if (doAction is HookNodeAction) {
                    if (safeEquals(doAction.hookName, PLUGIN_NAME)) {
                        return
                    }
                }
                var lastLocation = doAction.node
                if (doAction is NewNodeAction) {
                    lastLocation = doAction.newId
                }
                // prevent double entries
                if (mLastEditLocations.size > 0
                        && safeEquals(lastLocation,
                                mLastEditLocations.lastElement())) {
                    return
                }
                mLastEditLocations.add(lastLocation)
                if (mLastEditLocations.size > 10) {
                    mLastEditLocations.removeAt(0)
                }
                try {
                    logger.fine("New last edit location: " + lastLocation
                            + " from " + controller.marshall(doAction))
                } catch (e: Exception) {
                    Resources.getInstance().logException(e)
                    logger.warning("Not able to marshall the action "
                            + doAction.javaClass + " as " + doAction)
                }
            }
        }

        override fun startTransaction(name: String?) {}
        override fun endTransaction(name: String?) {}
        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            val hookName = (pAction as NodeHookAction?)!!.hookName
            return if (PLUGIN_NAME == hookName) {
                // back is only enabled if there are already some nodes to go
                // back ;-)
                !mLastEditLocations.isEmpty()
            } else true
        }

        companion object {
            private const val PLUGIN_NAME = "accessories/plugins/JumpLastEditLocation.properties"
        }
    }
}