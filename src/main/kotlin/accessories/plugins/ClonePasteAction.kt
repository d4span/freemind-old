/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2011 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
import freemind.modes.mindmapmode.actions.xml.PrintActionHandler
import accessories.plugins.LogFileViewer.UpdateTextAreaThread
import java.util.logging.SimpleFormatter
import accessories.plugins.LogFileViewer
import accessories.plugins.LogFileViewer.PrintOperationAction
import accessories.plugins.LogFileViewer.SetLogLevelAction
import java.lang.InterruptedException
import accessories.plugins.ChangeRootNode
import freemind.modes.mindmapmode.actions.xml.ActionPair
import freemind.modes.mindmapmode.actions.xml.ActorXml
import accessories.plugins.ExportWithXSLT
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FilenameFilter
import accessories.plugins.util.html.ClickableImageCreator
import freemind.modes.mindmapmode.actions.xml.ActionFilter
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
import freemind.view.ImageFactory
import freemind.view.mindmapview.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import javax.swing.table.TableRowSorter
import java.util.*
import java.util.logging.Logger
import javax.swing.*

/**
 * This is the "paste node as clone" action from the menu.
 *
 * @author foltin
 * @date 25.4.2011
 */
class ClonePasteAction
/**
 *
 */
    : MindMapNodeHookAdapter() {
    override fun invoke(pNode: MindMapNode?) {
        super.invoke(pNode)
        val mindMapNodes = mindMapNodes
        logger!!.fine("Clones for nodes: " + listToString(mindMapNodes))
        // now, construct the plugin for those nodes:
        for (copiedNode in mindMapNodes) {
            val clonePlugin: ClonePlugin = ClonePlugin.Companion.getHook(copiedNode)
            // first the clone master
            if (clonePlugin == null) {
                val showResult = OptionalDontShowMeAgainDialog(
                        mindMapController!!.frame.jFrame,
                        mindMapController!!.selectedView,
                        "choose_clone_type",
                        "clone_type_question",
                        mindMapController!!,
                        StandardPropertyHandler(
                                mindMapController!!.controller!!,
                                FreeMind.RESOURCES_COMPLETE_CLONING),
                        OptionalDontShowMeAgainDialog.BOTH_OK_AND_CANCEL_OPTIONS_ARE_STORED)
                        .show().result
                val properties = Properties()
                properties
                        .setProperty(
                                ClonePlugin.Companion.XML_STORAGE_CLONE_ITSELF,
                                if (showResult == JOptionPane.OK_OPTION) ClonePlugin.Companion.CLONE_ITSELF_TRUE else ClonePlugin.Companion.CLONE_ITSELF_FALSE)
                val selecteds = getVectorWithSingleElement(copiedNode)
                mindMapController!!.addHook(copiedNode, selecteds,
                        ClonePlugin.Companion.PLUGIN_LABEL, properties)
            }
            // finally, we construct a new one:
            val copy = mindMapController!!.copy(copiedNode, true)
            addNewClone(copiedNode, pNode, copy)
        }
    }

    fun addNewClone(originalNode: MindMapNode?,
                    pDestinationNode: MindMapNode?, copy: Transferable?) {
        val originalNodeId = mindMapController!!.getNodeID(originalNode)!!
        logger!!.fine("Original node $originalNode, id $originalNodeId")
        if (originalNode!!.isRoot) {
            mindMapController!!.controller!!.errorMessage(
                    mindMapController!!.getText(
                            "clone_plugin_no_root_cloning"))
            return
        }
        // insert clone:
        val listOfChilds = pDestinationNode!!.children
        val listOfChildIds = Vector<String>()
        for (node in listOfChilds) {
            val nodeID = mindMapController!!.getNodeID(node)!!
            listOfChildIds.add(nodeID)
            logger!!.fine("Old child id:$nodeID")
        }
        mindMapController!!.paste(copy, pDestinationNode)
    }

    val mindMapNodes: Vector<MindMapNode?>
        get() = getMindMapNodesFromClipboard(mindMapController!!)
    protected val registration: Registration
        protected get() = getPluginBaseClass() as Registration

    interface ClonePropertiesObserver {
        fun propertiesChanged(pCloneProperties: CloneProperties)
    }

    class CloneProperties {
        var mCloneItself = false
        private val mObserverSet = HashSet<ClonePropertiesObserver>()

        /**
         *
         */
        init {
            if (logger == null) {
                logger = Resources.getInstance().getLogger(
                        this.javaClass.name)
            }
        }

        var isCloneItself: Boolean
            get() = mCloneItself
            set(pCloneItself) {
                logger!!.finest("Setting mCloneItself to $pCloneItself")
                var fire = false
                if (pCloneItself != mCloneItself) {
                    fire = true
                }
                mCloneItself = pCloneItself
                if (fire) {
                    firePropertiesChanged()
                }
            }

        fun registerObserver(pObserver: ClonePropertiesObserver) {
            mObserverSet.add(pObserver)
        }

        fun deregisterObserver(pObserver: ClonePropertiesObserver) {
            mObserverSet.remove(pObserver)
        }

        private fun firePropertiesChanged() {
            for (observer in mObserverSet) {
                observer.propertiesChanged(this)
            }
        }

        companion object {
            protected var logger: Logger? = null
        }
    }

    class Registration(controller: ModeController, map: MindMap) : HookRegistration, MenuItemEnabledListener, ActionFilter, NodeSelectionListener {
        /**
         * Mapping of clone id (String) to a HashSet of [MindMapNode]s
         */
        private val mCloneIdsMap = HashMap<String?, HashSet<MindMapNode>?>()

        /**
         * This is the reverse of mCloneIdsMap: [MindMapNode] to cloneId.
         */
        private val mClonesMap = HashMap<MindMapNode?, String?>()

        /**
         * This is a storage cloneId to clone properties.
         */
        private val mClonePropertiesMap = HashMap<String?, CloneProperties>()
        private val controller: MindMapController?
        private val mMap: MindMap
        private val logger: Logger
        private val mLastMarkedNodeViews = Vector<MindMapNode>()

        init {
            this.controller = controller as MindMapController
            mMap = map
            logger = controller.frame.getLogger(this.javaClass.name)
        }

        override fun register() {
            if (sCloneIcon == null) {
                sCloneIcon = ImageFactory.getInstance().createUnscaledIcon(
                        controller!!.getResource("images/clone.png"))
                sOriginalIcon = ImageFactory.getInstance().createUnscaledIcon(
                        controller.getResource("images/clone_original.png"))
                sShowIcon = Resources.getInstance().getBoolProperty(
                        FreeMind.RESOURCES_DON_T_SHOW_CLONE_ICONS)
            }
            controller!!.actionRegistry.registerFilter(this)
            controller.registerNodeSelectionListener(this, false)
        }

        override fun deRegister() {
            controller!!.deregisterNodeSelectionListener(this)
            controller.actionRegistry.deregisterFilter(this)
        }

        override fun isEnabled(pItem: JMenuItem?, pAction: Action?): Boolean {
            if (controller == null) return false
            val hookName = (pAction as NodeHookAction?)!!.hookName
            if (PLUGIN_NAME == hookName) {
                // only enabled, if nodes have been copied before.
                val mindMapNodes = getMindMapNodesFromClipboard(controller)
                // logger.warning("Nodes " + Tools.listToString(mindMapNodes));
                return !mindMapNodes.isEmpty()
            }
            val selecteds = controller.selecteds
            for (node in selecteds) {
                if (ClonePlugin.Companion.getHook(node) != null) {
                    return true
                }
            }
            return false
        }

        fun generateNewCloneId(pProposedID: String?): String {
            return generateID(pProposedID, mCloneIdsMap, "CLONE_")
        }

        /**
         * @param pCloneId
         * @return true, if the pCloneId is new (not already registered)
         */
        fun registerClone(pCloneId: String?, pPlugin: ClonePlugin): Boolean {
            val vectorPresent = mCloneIdsMap.containsKey(pCloneId)
            val v = getHashSetToCloneId(pCloneId)
            val node: MindMapNode = pPlugin.getNode()!!
            val it = v!!.iterator()
            while (it.hasNext()) {
                val otherCloneNode = it.next()
                val otherClone: ClonePlugin = ClonePlugin.Companion.getHook(otherCloneNode)
                if (otherClone == null) {
                    it.remove()
                    logger.warning("Found clone node "
                            + controller!!.getNodeID(otherCloneNode)
                            + " which isn't a clone any more.")
                    continue
                }
                // inform all others
                otherClone.addClone(node)
                // inform this clone about its brothers
                pPlugin.addClone(otherCloneNode)
            }
            v.add(node)
            mClonesMap[node] = pCloneId
            selectShadowNode(node, true, node)
            if (!mClonePropertiesMap.containsKey(pCloneId)) {
                mClonePropertiesMap[pCloneId] = CloneProperties()
            }
            return !vectorPresent
        }

        fun deregisterClone(pCloneId: String?, pPlugin: ClonePlugin) {
            val cloneSet = getHashSetToCloneId(pCloneId)
            val node: MindMapNode = pPlugin.getNode()!!
            cloneSet!!.remove(node)
            mClonesMap.remove(node)
            // inform all others
            val it = cloneSet.iterator()
            while (it.hasNext()) {
                val otherCloneNode = it.next()
                val otherClone: ClonePlugin = ClonePlugin.Companion.getHook(otherCloneNode)
                if (otherClone == null) {
                    it.remove()
                    logger.warning("Found clone node "
                            + controller!!.getNodeID(otherCloneNode)
                            + " which isn't a clone any more.")
                    continue
                }
                otherClone.removeClone(node)
            }
            if (cloneSet.isEmpty()) {
                // remove entire clone
                mCloneIdsMap.remove(cloneSet)
                mClonePropertiesMap.remove(pCloneId)
            }
        }

        fun getCloneProperties(pCloneId: String?): CloneProperties? {
            if (mClonePropertiesMap.containsKey(pCloneId)) {
                return mClonePropertiesMap[pCloneId]
            }
            throw IllegalArgumentException(
                    "Clone properties not found for $pCloneId")
        }

        protected fun getHashSetToCloneId(pCloneId: String?): HashSet<MindMapNode>? {
            var v: HashSet<MindMapNode>? = null
            if (!mCloneIdsMap.containsKey(pCloneId)) {
                v = HashSet()
                mCloneIdsMap[pCloneId] = v
            } else {
                v = mCloneIdsMap[pCloneId]
            }
            return v
        }

        override fun filterAction(pair: ActionPair?): ActionPair? {
            // shortcut for no clones for speed up.
            if (mCloneIdsMap.isEmpty()) {
                return pair
            }
            var doAction = pair!!.doAction
            doAction = cloneAction(doAction)
            pair.doAction = doAction
            return pair
        }

        private fun cloneAction(doAction: XmlAction?): XmlAction? {
            var doAction = doAction
            logger.fine("Found do action: " + doAction!!.javaClass.name)
            if (doAction is NodeAction) {
                val nodeAction = doAction
                val node: MindMapNode = controller!!.getNodeFromID(nodeAction
                        .node!!)
                // check for clone or original?
                doAction = cloneAction(nodeAction, node)
            } else {
                if (doAction is CompoundAction) {
                    val compoundAction = doAction
                    val choiceList: List<XmlAction?> = compoundAction.listChoiceList
                    var index = 0
                    for (subAction in choiceList) {
                        subAction = cloneAction(subAction)
                        compoundAction.setAtChoice(index, subAction)
                        index++
                    }
                }
            }
            return doAction
        }

        private fun cloneAction(nodeAction: NodeAction, node: MindMapNode): XmlAction {
            val correspondingNodes = getCorrespondingNodes(nodeAction, node)
            if (correspondingNodes.isEmpty()) {
                return nodeAction
            }
            // create new action:
            val compound = CompoundAction()
            compound.addChoice(nodeAction)
            for (pair in correspondingNodes) {
                getNewCompoundAction(nodeAction, pair, compound)
            }
            return compound
        }

        private fun getNewCompoundAction(nodeAction: NodeAction,
                                         correspondingNodePair: MindMapNodePair,
                                         compound: CompoundAction) {
            // deep copy:
            val copiedNodeAction = deepCopy(nodeAction) as NodeAction
            // special cases:
            if (copiedNodeAction is MoveNodesAction) {
                val moveAction = copiedNodeAction
                for (i in moveAction.listNodeListMemberList
                        .indices) {
                    val member = moveAction.getNodeListMember(i)
                    changeNodeListMember(correspondingNodePair, moveAction,
                            member)
                }
            }
            if (copiedNodeAction is HookNodeAction) {
                val hookAction = copiedNodeAction
                for (i in hookAction.listNodeListMemberList
                        .indices) {
                    val member = hookAction.getNodeListMember(i)
                    changeNodeListMember(correspondingNodePair, hookAction,
                            member)
                }
            }
            if (copiedNodeAction is NewNodeAction) {
                val newId = mMap.linkRegistry!!.generateUniqueID(null)
                copiedNodeAction.newId = newId
            }
            copiedNodeAction.node = controller!!.getNodeID(correspondingNodePair
                    .corresponding)
            if (copiedNodeAction is PasteNodeAction) {
                /*
				 * difficult thing here: if something is pasted, the paste
				 * action itself contains the node ids of the paste. The first
				 * pasted action will get that node id. This should be the
				 * corresponding node itself. This presumably corrects a bug
				 * that the selection on move actions is changing.
				 */
                compound.addChoice(copiedNodeAction)
            } else {
                compound.addAtChoice(0, copiedNodeAction)
            }
        }

        fun changeNodeListMember(
                correspondingNodePair: MindMapNodePair,
                pAction: NodeAction?, member: NodeListMember) {
            val memberNode = controller!!.getNodeFromID(member.node!!)
            val correspondingMoveNodes = getCorrespondingNodes(pAction, memberNode)
            if (!correspondingMoveNodes.isEmpty()) {
                // search for this clone:
                for (pair in correspondingMoveNodes) {
                    if (pair.cloneNode === correspondingNodePair
                                    .cloneNode) {
                        // found:
                        member.node = controller.getNodeID(pair
                                .corresponding)
                        break
                    }
                }
            }
        }

        /**
         * Method takes into account, that some actions are different.
         *
         * @param nodeAction
         * @param node
         * @return
         */
        fun getCorrespondingNodes(nodeAction: NodeAction?,
                                  node: MindMapNode?): List<MindMapNodePair> {
            var startWithParent = false
            // Behavior for complete cloning.
            if (mClonesMap.containsKey(node)) {
                if (getCloneProperties(mClonesMap[node])!!.isCloneItself) {
                    // Behavior for complete cloning
                    if (nodeAction is MoveNodesAction
                            || nodeAction is MoveNodeXmlAction
                            || nodeAction is DeleteNodeAction
                            || nodeAction is CutNodeAction) {
                        // ok, there is an action for a clone itself. be
                        // careful:
                        // clone only, if parents are clones:
                        startWithParent = true
                    } else if (nodeAction is PasteNodeAction) {
                        if (nodeAction.asSibling) {
                            // sibling means, that the paste goes below the
                            // clone.
                            // skip.
                            startWithParent = true
                        } else {
                            // here, the action changes the children, thus, they
                            // are
                            // subject to cloning.
                        }
                    } else if (nodeAction is UndoPasteNodeAction) {
                        if (nodeAction.asSibling) {
                            // sibling means, that the paste goes below the
                            // clone.
                            // skip.
                            startWithParent = true
                        } else {
                            // here, the action changes the children, thus, they
                            // are
                            // subject to cloning.
                        }
                    }
                } else {
                    // Behavior for children cloning only
                    /*
					 * new node action belongs to the children, so clone it,
					 * even, when node is the clone itself.
					 */
                    if (nodeAction is NewNodeAction) {
                        // here, the action changes the children, thus, they are
                        // subject to cloning.
                    } else if (nodeAction is PasteNodeAction) {
                        if (nodeAction.asSibling) {
                            // sibling means, that the paste goes below the
                            // clone.
                            // skip.
                            startWithParent = true
                        } else {
                            // here, the action changes the children, thus, they
                            // are
                            // subject to cloning.
                        }
                    } else if (nodeAction is UndoPasteNodeAction) {
                        if (nodeAction.asSibling) {
                            // sibling means, that the paste goes below the
                            // clone.
                            // skip.
                            startWithParent = true
                        } else {
                            // here, the action changes the children, thus, they
                            // are
                            // subject to cloning.
                        }
                    } else {
                        // ok, there is an action for a clone itself. be
                        // careful:
                        // clone only, if parents are clones:
                        startWithParent = true
                    }
                }
            }
            return getCorrespondingNodes(node, startWithParent)
        }

        /**
         * This is the main method here. It returns to a given node its cloned
         * nodes on the other side.
         *
         * @param pNode
         * is checked to be son of one of the clones/original.
         * @param pStartWithParent
         * Sometimes, it is relevant, if only one of the parents is a
         * clone, eg. for all actions, that affect the clone itself,
         * thus not need to be cloned, but perhaps the clone is
         * itself a node inside of another clone!
         * @return a list of [MindMapNodePair]s where the first is the
         * corresponding node and the second is the clone. If the return
         * value is empty, the node isn't son of any.
         */
        fun getCorrespondingNodes(pNode: MindMapNode?,
                                  pStartWithParent: Boolean): List<MindMapNodePair> {
            // in case, no clones are present, this method returns very fast.
            if (mClonesMap.isEmpty()) {
                return emptyList()
            }
            var clone: MindMapNode?
            run {
                var child: MindMapNode?
                // code doubling to speed up. First check for a clone on the way
                // to root.
                child = if (pStartWithParent) {
                    pNode!!.parentNode
                } else {
                    pNode
                }
                while (!mClonesMap.containsKey(child)) {
                    if (child!!.isRoot) {
                        // nothing found!
                        return emptyList()
                    }
                    child = child!!.parentNode
                }
                clone = child
            }
            var child: MindMapNode?
            // now, there is a clone on the way. Collect the indices.
            val indexVector = Vector<Int>()
            child = if (pStartWithParent) {
                addNodePosition(indexVector, pNode)
                pNode!!.parentNode
            } else {
                pNode
            }
            while (clone !== child) {
                addNodePosition(indexVector, child)
                child = child!!.parentNode
            }
            val returnValue = Vector<MindMapNodePair>()
            val originalNode = child
            val targets = mCloneIdsMap[mClonesMap[child]]
            CloneLoop@ for (cloneNode in targets!!) {
                var target = cloneNode
                if (cloneNode === originalNode) continue
                for (i in indexVector.indices.reversed()) {
                    val index = (indexVector[i] as Int).toInt()
                    if (target.childCount <= index) {
                        logger.warning("Index " + index
                                + " in other tree not found from "
                                + printNodeIds(targets) + " originating from "
                                + printNodeId(cloneNode) + " start at parent "
                                + pStartWithParent)
                        // with crossed fingers.
                        continue@CloneLoop
                    }
                    target = target.getChildAt(index) as MindMapNode
                }
                // logger.fine("Found corresponding node " + printNodeId(target)
                // + " on clone " + printNodeId(cloneNode));
                returnValue.add(MindMapNodePair(target, cloneNode))
            }
            return returnValue
        }

        private fun addNodePosition(indexVector: Vector<Int>, child: MindMapNode?) {
            indexVector.add(child!!.parentNode!!.getChildPosition(
                    child))
        }

        /**
         * @param pCloneNode
         * @return
         */
        private fun printNodeId(pCloneNode: MindMapNode): String {
            return try {
                (controller!!.getNodeID(pCloneNode) + ": '"
                        + pCloneNode.getShortText(controller) + "'")
            } catch (e: Exception) {
                "NOT FOUND: '$pCloneNode'"
            }
        }

        /**
         * @param pClones
         * @return
         */
        private fun printNodeIds(pClones: HashSet<MindMapNode>?): String {
            val strings = Vector<String?>()
            for (pluginNode in pClones!!) {
                strings.add(printNodeId(pluginNode))
            }
            return listToString(strings)
        }

        /**
         * Is sent when a node is selected.
         */
        override fun onFocusNode(node: NodeView?) {
            markShadowNode(node, true)
        }

        /**
         * Is sent when a node is deselected.
         */
        override fun onLostFocusNode(node: NodeView?) {
            markShadowNode(node, false)
        }

        fun markShadowNode(pNode: NodeView?, pEnableShadow: Boolean) {
            // at startup, the node is null.
            if (pNode == null || pNode.model == null) {
                return
            }
            if (!pEnableShadow) {
                if (!mLastMarkedNodeViews.isEmpty()) {
                    for (node in mLastMarkedNodeViews) {
                        if (mClonesMap.containsKey(node)) {
                            setIcon(node, sOriginalIcon)
                        } else {
                            setIcon(node, null)
                        }
                    }
                    mLastMarkedNodeViews.clear()
                }
            } else {
                markShadowNode(pNode.model, pEnableShadow)
            }
        }

        fun markShadowNode(model: MindMapNode?, pEnableShadow: Boolean) {
            mLastMarkedNodeViews.clear()
            try {
                val shadowNodes = getCorrespondingNodes(model, false)
                for (shadowNode in shadowNodes) {
                    val correspondingNode = shadowNode.corresponding
                    mLastMarkedNodeViews.add(correspondingNode)
                    selectShadowNode(correspondingNode, pEnableShadow,
                            shadowNode.cloneNode)
                }
            } catch (e: IllegalArgumentException) {
                Resources.getInstance().logException(e)
            }
        }

        private fun selectShadowNode(node: MindMapNode, pEnableShadow: Boolean,
                                     pCloneNode: MindMapNode) {
            var node: MindMapNode? = node
            if (!sShowIcon) {
                return
            }
            while (node != null) {
                var i = if (pEnableShadow) sCloneIcon else null
                if (node === pCloneNode) {
                    i = sOriginalIcon
                }
                setIcon(node, i)
                if (node === pCloneNode) break
                node = node.parentNode
                // comment this out to get a complete marked path to the root of
                // the
                // clones.
                break
            }
        }

        fun setIcon(node: MindMapNode, i: ImageIcon?) {
            node.setStateIcon(ClonePlugin.Companion.PLUGIN_LABEL, i)
            controller!!.nodeRefresh(node)
        }

        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * freemind.modes.ModeController.NodeSelectionListener#onSelectionChange
		 * (freemind.modes.MindMapNode, boolean)
		 */
        override fun onSelectionChange(pNode: NodeView?, pIsSelected: Boolean) {}
        override fun onUpdateNodeHook(pNode: MindMapNode?) {}
        override fun onSaveNode(pNode: MindMapNode?) {}

        companion object {
            private const val PLUGIN_NAME = "accessories/plugins/ClonePasteAction.properties"
            private var sCloneIcon: ImageIcon? = null
            private var sOriginalIcon: ImageIcon? = null
            private var sShowIcon = true
        }
    }
}