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
import java.awt.GridBagLayout
import java.awt.GridBagConstraints
import java.awt.Insets
import accessories.plugins.time.TimeList.FilterTextDocumentListener
import accessories.plugins.time.TimeList.FlatNodeTable
import accessories.plugins.time.TimeList.FlatNodeTableKeyListener
import accessories.plugins.time.TimeList.FlatNodeTableMouseAdapter
import accessories.plugins.time.TimeList.NotesHolder
import accessories.plugins.time.TimeList.IconsHolder
import accessories.plugins.time.TimeList.ReplaceAllInfo
import accessories.plugins.time.TimeList.ReplaceSelectedInfo
import accessories.plugins.time.TimeList.ToggleViewFoldedNodesAction
import freemind.controller.StructuredMenuHolder
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
import java.lang.Runnable
import freemind.common.ScalableJTable
import java.text.DateFormat
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
import java.awt.Dimension
import java.lang.NumberFormatException
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
import java.awt.CardLayout
import freemind.controller.actions.generated.instance.ManageStyleEditorWindowConfigurationStorage
import accessories.plugins.dialogs.ManagePatternsPopupDialog.PatternListSelectionListener
import com.jgoodies.forms.builder.ButtonBarBuilder
import freemind.extensions.ModeControllerHookAdapter
import accessories.plugins.NodeNoteRegistration
import java.awt.KeyboardFocusManager
import accessories.plugins.SortNodes.NodeTextComparator
import javax.swing.text.html.HTMLEditorKit
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler
import accessories.plugins.UnfoldAll
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
import freemind.controller.actions.generated.instance.AttributeTableProperties
import freemind.controller.actions.generated.instance.TableColumnOrder
import freemind.main.*
import freemind.modes.*
import java.awt.event.*
import java.util.*
import java.util.Timer
import java.util.regex.Pattern
import javax.swing.*
import javax.swing.event.*
import javax.swing.table.*
import javax.swing.text.Document

/**
 * @author foltin
 *
 * TODO: - Extract HTML from nodes and notes.
 */
class TimeList : MindMapHookAdapter(), MapModuleChangeObserver {
    private var mDialog: JDialog? = null
    private var mTimeTable: JTable? = null
    private var mTimeTableModel: DefaultTableModel? = null
    private var mSorter: TableSorter? = null
    private var mDateRenderer: DateRenderer? = null
    private var mNodeRenderer: NodeRenderer? = null
    private var mIconsRenderer: IconsRenderer? = null
    private var mShowAllNodes = false
    private var mFlatNodeTableFilterModel: FlatNodeTableFilterModel? = null
    private var mFilterTextSearchField: JTextField? = null
    private var mFilterTextReplaceField: JTextField? = null
    private var mNotesRenderer: NotesRenderer? = null
    private var mTreeLabel: JLabel? = null

    /**
     * Overwritten, as this dialog is not modal, but after the plugin has
     * terminated, the dialog is still present and needs the controller to store
     * its values.
     */
    override var mindMapController: MindMapController? = null
        private set
    private var mViewFoldedNodes = true
    private var mStatisticsLabel: JLabel? = null
    override fun startupMapHook() {
        super.startupMapHook()
        mindMapController = super.mindMapController
        mindMapController!!.controller!!.mapModuleManager
                .addListener(this)

        // get strings from resources:
        COLUMN_MODIFIED = getResourceString("plugins/TimeList.xml_Modified")
        COLUMN_CREATED = getResourceString("plugins/TimeList.xml_Created")
        COLUMN_ICONS = getResourceString("plugins/TimeList.xml_Icons")
        COLUMN_TEXT = getResourceString("plugins/TimeList.xml_Text")
        COLUMN_DATE = getResourceString("plugins/TimeList.xml_Date")
        COLUMN_NOTES = getResourceString("plugins/TimeList.xml_Notes")
        mShowAllNodes = xmlToBoolean(getResourceString("show_all_nodes"))
        mDialog = JDialog(getController()!!.frame.getJFrame(), false /* unmodal */)
        val windowTitle: String
        windowTitle = if (mShowAllNodes) {
            "plugins/TimeManagement.xml_WindowTitle_All_Nodes"
        } else {
            "plugins/TimeManagement.xml_WindowTitle"
        }
        mDialog!!.title = getResourceString(windowTitle)
        mDialog!!.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
        mDialog!!.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(event: WindowEvent) {
                disposeDialog()
            }
        })
        addEscapeActionToDialog(mDialog!!, object : AbstractAction() {
            override fun actionPerformed(arg0: ActionEvent) {
                disposeDialog()
            }
        })
        val contentPane = mDialog!!.contentPane
        val gbl = GridBagLayout()
        gbl.columnWeights = doubleArrayOf(1.0)
        gbl.rowWeights = doubleArrayOf(1.0)
        contentPane.layout = gbl
        contentPane.add(JLabel(
                getResourceString("plugins/TimeManagement.xml_Find")),
                GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        Insets(0, 0, 0, 0), 0, 0))
        mFilterTextSearchField = JTextField()
        mFilterTextSearchField!!.document.addDocumentListener(
                FilterTextDocumentListener())
        mFilterTextSearchField!!.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(pEvent: KeyEvent) {
                // logger.info("Key event:" + pEvent.getKeyCode());
                if (pEvent.keyCode == KeyEvent.VK_DOWN) {
                    logger!!.info("Set Focus to replace fields")
                    mFilterTextReplaceField!!.requestFocusInWindow()
                }
            }
        })
        contentPane.add( /* new JScrollPane */mFilterTextSearchField,
                GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        Insets(0, 0, 0, 0), 0, 0))
        contentPane.add(JLabel(
                getResourceString("plugins/TimeManagement.xml_Replace")),
                GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        Insets(0, 0, 0, 0), 0, 0))
        mFilterTextReplaceField = JTextField()
        contentPane.add( /* new JScrollPane */mFilterTextReplaceField,
                GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                        Insets(0, 0, 0, 0), 0, 0))
        mFilterTextReplaceField!!.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(pEvent: KeyEvent) {
                if (pEvent.keyCode == KeyEvent.VK_DOWN) {
                    logger!!.info("Set Focus to table")
                    mTimeTable!!.requestFocusInWindow()
                } else if (pEvent.keyCode == KeyEvent.VK_UP) {
                    logger!!.info("Set Focus to table")
                    mFilterTextSearchField!!.requestFocusInWindow()
                }
            }
        })
        mDateRenderer = DateRenderer()
        mNodeRenderer = NodeRenderer()
        mNotesRenderer = NotesRenderer()
        mIconsRenderer = IconsRenderer(getController())
        mTimeTable = FlatNodeTable()
        mTimeTable.addKeyListener(FlatNodeTableKeyListener())
        // double click = goto.
        mTimeTable.addMouseListener(FlatNodeTableMouseAdapter())
        // disable moving:
        mTimeTable.getTableHeader().reorderingAllowed = false
        updateModel()
        mSorter!!.tableHeader = mTimeTable.getTableHeader()
        mSorter!!.setColumnComparator(Date::class.java,
                TableSorter.Companion.COMPARABLE_COMAPRATOR)
        mSorter!!.setColumnComparator(NodeHolder::class.java,
                TableSorter.Companion.LEXICAL_COMPARATOR)
        mSorter!!.setColumnComparator(NotesHolder::class.java,
                TableSorter.Companion.LEXICAL_COMPARATOR)
        mSorter!!.setColumnComparator(IconsHolder::class.java,
                TableSorter.Companion.COMPARABLE_COMAPRATOR)
        // Sort by default by date.
        mSorter!!.setSortingStatus(DATE_COLUMN, TableSorter.Companion.ASCENDING)
        val pane = JScrollPane(mTimeTable)
        contentPane.add(pane, GridBagConstraints(0, 4, 1, 1, 1.0, 10.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, Insets(0,
                0, 0, 0), 0, 0))
        mTreeLabel = JLabel()
        contentPane.add(JScrollPane(mTreeLabel), GridBagConstraints(0,
                5, 1, 2, 1.0, 4.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, Insets(0, 0, 0, 0), 0, 0))
        mStatisticsLabel = JLabel()
        contentPane.add(JScrollPane(mStatisticsLabel), GridBagConstraints(0,
                7, 1, 2, 1.0, 4.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, Insets(0, 0, 0, 0), 0, 0))
        // button bar
        val selectAction: AbstractAction = object : AbstractAction(
                getResourceString("plugins/TimeManagement.xml_Select")) {
            override fun actionPerformed(arg0: ActionEvent) {
                selectSelectedRows()
            }
        }
        val exportAction: AbstractAction = object : AbstractAction(
                getResourceString("plugins/TimeManagement.xml_Export")) {
            override fun actionPerformed(arg0: ActionEvent) {
                exportSelectedRowsAndClose()
            }
        }
        val replaceAllAction: AbstractAction = object : AbstractAction(
                getResourceString("plugins/TimeManagement.xml_Replace_All")) {
            override fun actionPerformed(arg0: ActionEvent) {
                replace(ReplaceAllInfo())
            }
        }
        val replaceSelectedAction: AbstractAction = object : AbstractAction(
                getResourceString("plugins/TimeManagement.xml_Replace_Selected")) {
            override fun actionPerformed(arg0: ActionEvent) {
                replace(ReplaceSelectedInfo())
            }
        }
        val gotoAction: AbstractAction = object : AbstractAction(
                getResourceString("plugins/TimeManagement.xml_Goto")) {
            override fun actionPerformed(arg0: ActionEvent) {
                selectSelectedRows()
                disposeDialog()
            }
        }
        val disposeAction: AbstractAction = object : AbstractAction(
                getResourceString("plugins/TimeManagement.xml_Cancel")) {
            override fun actionPerformed(arg0: ActionEvent) {
                disposeDialog()
            }
        }
        val toggleViewFoldedNodesAction: AbstractAction = ToggleViewFoldedNodesAction(
                getResourceString("plugins/TimeManagement.xml_ToggleViewFoldedNodesAction"))
        /** Menu  */
        val menuHolder = StructuredMenuHolder()
        val menuBar = JMenuBar()
        val menu = JMenu(
                getResourceString("plugins/TimeManagement.xml_menu_actions"))
        menuHolder.addMenu(menu, "main/actions/.")
        val selectMenuItem = addAccelerator(
                menuHolder.addAction(selectAction, "main/actions/select")!!,
                "keystroke_plugins/TimeList_select")
        val gotoMenuItem = addAccelerator(
                menuHolder.addAction(gotoAction, "main/actions/goto")!!,
                "keystroke_plugins/TimeList_goto")
        val replaceSelectedMenuItem = addAccelerator(
                menuHolder.addAction(replaceSelectedAction,
                        "main/actions/replaceSelected")!!,
                "keystroke_plugins/TimeList_replaceSelected")
        addAccelerator(
                menuHolder.addAction(replaceAllAction,
                        "main/actions/replaceAll")!!,
                "keystroke_plugins/TimeList_replaceAll")
        val exportMenuItem = addAccelerator(
                menuHolder.addAction(exportAction, "main/actions/export")!!,
                "keystroke_plugins/TimeList_export")
        addAccelerator(
                menuHolder.addAction(disposeAction, "main/actions/dispose")!!,
                "keystroke_plugins/TimeList_dispose")
        val viewMenu = JMenu(
                getResourceString("plugins/TimeManagement.xml_menu_view"))
        menuHolder.addMenu(viewMenu, "main/view/.")
        addAccelerator(menuHolder.addAction(toggleViewFoldedNodesAction,
                "main/view/showFoldedNodes")!!,
                "keystroke_plugins/TimeList_showFoldedNodes")
        menuHolder.updateMenus(menuBar, "main/")
        mDialog!!.jMenuBar = menuBar

        /* Initial State */selectMenuItem.isEnabled = false
        gotoMenuItem.isEnabled = false
        exportMenuItem.isEnabled = false
        replaceSelectedMenuItem.isEnabled = false

        // table selection listeners to enable/disable menu actions:
        val rowSM = mTimeTable.getSelectionModel()
        rowSM.addListSelectionListener(ListSelectionListener { e -> // Ignore extra messages.
            if (e.valueIsAdjusting) return@ListSelectionListener
            val lsm = e.source as ListSelectionModel
            val enable = !lsm.isSelectionEmpty
            replaceSelectedMenuItem.isEnabled = enable
            selectMenuItem.isEnabled = enable
            gotoMenuItem.isEnabled = enable
            exportMenuItem.isEnabled = enable
        })
        // table selection listener to display the history of the selected nodes
        rowSM.addListSelectionListener(object : ListSelectionListener {
            fun getNodeText(node: MindMapNode?): String {
                return getNodeTextHierarchy(node!!, mindMapController)
            }

            override fun valueChanged(e: ListSelectionEvent) {
                // Ignore extra messages.
                if (e.valueIsAdjusting) return
                updateStatistics()
                val lsm = e.source as ListSelectionModel
                if (lsm.isSelectionEmpty) {
                    mTreeLabel!!.text = ""
                    return
                }
                val selectedRow = lsm.leadSelectionIndex
                val mindMapNode = getMindMapNode(selectedRow)
                mTreeLabel!!.text = getNodeText(mindMapNode)
            }
        })

        // restore preferences:
        // Retrieve window size and column positions.
        val storage: WindowConfigurationStorage = mindMapController
                .decorateDialog(mDialog, WINDOW_PREFERENCE_STORAGE_PROPERTY)
        if (storage != null) {
            tableConfiguration = storage
        }
        updateStatistics()
        mDialog!!.isVisible = true
    }

    /**
     *
     */
    protected fun toggleViewFoldedNodes() {
        mViewFoldedNodes = !mViewFoldedNodes
        updateModel()
        updateStatistics()
    }

    protected fun decorateButtonAndAction(stringProperty: String?,
                                          selectAction: AbstractAction, selectButton: JButton?) {
        val resourceString = getResourceString(stringProperty!!)
        selectAction.putValue(AbstractAction.NAME,
                resourceString.replace("&".toRegex(), ""))
        setLabelAndMnemonic(selectButton!!, resourceString)
    }

    protected fun exportSelectedRowsAndClose() {
        val selectedRows = mTimeTable!!.selectedRows
        val selectedNodes = Vector<MindMapNode?>()
        for (i in selectedRows.indices) {
            val row = selectedRows[i]
            selectedNodes.add(getMindMapNode(row))
        }
        // create new map:
        val newMindMapController = mindMapController!!.newMap() as MindMapController?
        // Tools.BooleanHolder booleanHolder = new Tools.BooleanHolder();
        // booleanHolder.setValue(false);
        for (node in selectedNodes) {
            val copy = node!!.shallowCopy()
            if (copy != null) {
                newMindMapController!!.insertNodeInto(copy, newMindMapController.rootNode)
            }
        }
        disposeDialog()
    }

    /**
     * @author foltin
     * @date 25.04.2012
     */
    private inner class MindmapTableModel : DefaultTableModel() {
        /*
		 * (non-Javadoc)
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
        override fun getColumnClass(arg0: Int): Class<*> {
            return when (arg0) {
                DATE_COLUMN, NODE_CREATED_COLUMN, NODE_MODIFIED_COLUMN -> Date::class.java
                NODE_TEXT_COLUMN -> NodeHolder::class.java
                NODE_ICON_COLUMN -> IconsHolder::class.java
                NODE_NOTES_COLUMN -> NotesHolder::class.java
                else -> Any::class.java
            }
        }
    }

    /**
     * @author foltin
     * @date 25.04.2012
     */
    private inner class ToggleViewFoldedNodesAction
    /**
     * @param pName
     */(pName: String) : AbstractAction(pName), MenuItemSelectedListener {
        override fun actionPerformed(arg0: ActionEvent) {
            toggleViewFoldedNodes()
        }

        /* (non-Javadoc)
		 * @see freemind.controller.MenuItemSelectedListener#isSelected(javax.swing.JMenuItem, javax.swing.Action)
		 */
        override fun isSelected(pCheckItem: JMenuItem?, pAction: Action?): Boolean {
            return mViewFoldedNodes
        }
    }

    interface IReplaceInputInformation {
        val length: Int
        fun getNodeHolderAt(i: Int): NodeHolder?
        fun changeString(holder: NodeHolder?, newText: String?)
    }

    private fun replace(info: IReplaceInputInformation) {
        try {
            val searchString = getText(mFilterTextSearchField!!.document)
            val replaceString = getText(mFilterTextReplaceField
                    .getDocument())
            replace(info, searchString, replaceString)
            mTimeTableModel!!.fireTableDataChanged()
            mFlatNodeTableFilterModel!!.resetFilter()
            mFilterTextSearchField!!.text = ""
        } catch (e: BadLocationException) {
            Resources.getInstance().logException(e)
        }
    }

    private inner class ReplaceAllInfo : IReplaceInputInformation {
        override val length: Int
            get() = mFlatNodeTableFilterModel!!.rowCount

        override fun getNodeHolderAt(i: Int): NodeHolder? {
            return mFlatNodeTableFilterModel!!.getValueAt(i,
                    NODE_TEXT_COLUMN) as NodeHolder
        }

        override fun changeString(nodeHolder: NodeHolder?, newText: String?) {
            mindMapController!!.setNodeText(nodeHolder!!.node, newText)
        }
    }

    private inner class ReplaceSelectedInfo : IReplaceInputInformation {
        override val length: Int
            get() = mTimeTable!!.selectedRowCount

        override fun getNodeHolderAt(i: Int): NodeHolder? {
            return mSorter!!.getValueAt(
                    mTimeTable!!.selectedRows[i], NODE_TEXT_COLUMN) as NodeHolder
        }

        override fun changeString(nodeHolder: NodeHolder?, newText: String?) {
            mindMapController!!.setNodeText(nodeHolder!!.node, newText)
        }
    }

    private fun selectSelectedRows() {
        selectNodes(mTimeTable!!.selectedRow, mTimeTable!!.selectedRows)
    }

    private fun gotoNodesAndClose(focussedRow: Int, selectedRows: IntArray) {
        selectNodes(focussedRow, selectedRows)
        disposeDialog()
    }

    private fun selectNodes(focussedRow: Int, selectedRows: IntArray) {
        if (focussedRow >= 0) {
            val focussedNode = getMindMapNode(focussedRow)
            // getController().centerNode(focussedNode);
            val selectedNodes = Vector<MindMapNode?>()
            for (i in selectedRows.indices) {
                val row = selectedRows[i]
                selectedNodes.add(getMindMapNode(row))
            }
            mindMapController!!.select(focussedNode, selectedNodes)
        }
    }

    /**
     */
    private fun getMindMapNode(focussedRow: Int): MindMapNode? {
        return (mTimeTable!!.model
                .getValueAt(focussedRow, NODE_TEXT_COLUMN) as NodeHolder).node
    }

    /**
     * Creates a table model for the new table and returns it.
     */
    private fun updateModel(): DefaultTableModel {
        var storage: TimeWindowConfigurationStorage? = null
        // if not first call, get configuration
        if (mSorter != null) {
            storage = tableConfiguration
        }
        val model: DefaultTableModel = MindmapTableModel()
        model.addColumn(COLUMN_DATE)
        model.addColumn(COLUMN_TEXT)
        model.addColumn(COLUMN_ICONS)
        model.addColumn(COLUMN_CREATED)
        model.addColumn(COLUMN_MODIFIED)
        model.addColumn(COLUMN_NOTES)
        val node = mindMapController!!.map!!.rootNode
        updateModel(model, node)
        mTimeTableModel = model
        mFlatNodeTableFilterModel = FlatNodeTableFilterModel(
                mTimeTableModel, NODE_TEXT_COLUMN, NODE_NOTES_COLUMN)
        if (mSorter == null) {
            mSorter = TableSorter(mFlatNodeTableFilterModel)
            mTimeTable!!.model = mSorter
        } else {
            mSorter!!.setTableModel(mFlatNodeTableFilterModel)
        }
        if (storage != null) {
            tableConfiguration = storage
        }
        try {
            val text = getRegularExpression(getText(mFilterTextSearchField
                    .getDocument()))
            mFlatNodeTableFilterModel!!.setFilter(text)
        } catch (e: BadLocationException) {
            Resources.getInstance().logException(e)
        }
        return model
    }

    private fun updateModel(model: DefaultTableModel, node: MindMapNode?) {
        val hook: ReminderHookBase = TimeManagementOrganizer.Companion.getHook(node)
        // show all nodes or only those with reminder:
        if (mShowAllNodes || hook != null) {
            var date: Date? = null
            if (hook != null) {
                date = Date(hook.remindUserAt)
            }
            model.addRow(arrayOf<Any?>(date, NodeHolder(node),
                    IconsHolder(node),
                    node!!.historyInformation!!.getCreatedAt(),
                    node.historyInformation!!.getLastModifiedAt(),
                    NotesHolder(node)))
        }
        if (!mViewFoldedNodes && node!!.isFolded) {
            // no recursion, if folded nodes should be hidden.
            return
        }
        val i: Iterator<MindMapNode?>? = node!!.childrenUnfolded()
        while (i!!.hasNext()) {
            val child = i.next()
            updateModel(model, child)
        }
    }

    /**
     *
     */
    private fun disposeDialog() {
        // store window positions:
        val storage = tableConfiguration
        mindMapController!!.storeDialogPositions(mDialog, storage,
                WINDOW_PREFERENCE_STORAGE_PROPERTY)
        mindMapController!!.controller!!.mapModuleManager
                .removeListener(this)
        mDialog!!.isVisible = false
        mDialog!!.dispose()
    }

    // Disable auto resizing
    protected var tableConfiguration: TimeWindowConfigurationStorage
        protected get() {
            val storage = TimeWindowConfigurationStorage()
            storage.viewFoldedNodes = mViewFoldedNodes
            for (i in 0 until mTimeTable!!.columnCount) {
                val setting = TimeWindowColumnSetting()
                setting.columnWidth = mTimeTable!!.columnModel.getColumn(i)
                        .width
                setting.columnSorting = mSorter!!.getSortingStatus(i)
                storage.addTimeWindowColumnSetting(setting)
            }
            return storage
        }
        protected set(storage) {
            // Disable auto resizing
            mTimeTable!!.autoResizeMode = JTable.AUTO_RESIZE_OFF
            val timeStorage = storage
            if (mViewFoldedNodes != timeStorage.viewFoldedNodes) {
                toggleViewFoldedNodes()
            }
            var column = 0
            val i: Iterator<TimeWindowColumnSetting> = timeStorage.listTimeWindowColumnSettingList.iterator()
            while (i.hasNext()) {
                val setting = i.next()
                mTimeTable!!.columnModel.getColumn(column).preferredWidth = setting.columnWidth
                mSorter!!.setSortingStatus(column, setting.columnSorting)
                column++
            }
        }

    /**
     * @throws BadLocationException
     */
    @Throws(BadLocationException::class)
    private fun getText(document: Document): String {
        return document.getText(0, document.length)
    }

    private inner class FilterTextDocumentListener : DocumentListener {
        private var mTypeDelayTimer: Timer? = null
        @Synchronized
        private fun change(event: DocumentEvent) {
            // stop old timer, if present:
            if (mTypeDelayTimer != null) {
                mTypeDelayTimer!!.cancel()
                mTypeDelayTimer = null
            }
            mTypeDelayTimer = Timer()
            mTypeDelayTimer!!.schedule(DelayedTextEntry(event),
                    TYPE_DELAY_TIME.toLong())
        }

        override fun insertUpdate(event: DocumentEvent) {
            change(event)
        }

        override fun removeUpdate(event: DocumentEvent) {
            change(event)
        }

        override fun changedUpdate(event: DocumentEvent) {
            change(event)
        }

        protected inner class DelayedTextEntry internal constructor(private val event: DocumentEvent) : TimerTask() {
            override fun run() {
                SwingUtilities.invokeLater {
                    try {
                        val document = event.document
                        val text = getRegularExpression(getText(document))
                        mFlatNodeTableFilterModel!!.setFilter(text)
                        updateStatistics()
                    } catch (e: BadLocationException) {
                        Resources.getInstance().logException(
                                e)
                        mFlatNodeTableFilterModel!!.resetFilter()
                    }
                }
            }
        }
    }

    private inner class FlatNodeTableMouseAdapter : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            if (e.clickCount == 2) {
                val p = e.point
                val row = mTimeTable!!.rowAtPoint(p)
                gotoNodesAndClose(row, intArrayOf(row))
            }
        }
    }

    private inner class FlatNodeTableKeyListener : KeyListener {
        override fun keyTyped(arg0: KeyEvent) {}
        override fun keyPressed(arg0: KeyEvent) {}
        override fun keyReleased(arg0: KeyEvent) {
            if (arg0.keyCode == KeyEvent.VK_ESCAPE) {
                disposeDialog()
            }
            if (arg0.keyCode == KeyEvent.VK_ENTER) {
                selectSelectedRows()
                disposeDialog()
            }
        }
    }

    private inner class FlatNodeTable : ScalableJTable() {
        override fun getCellRenderer(row: Int, column: Int): TableCellRenderer {
            val `object` = model.getValueAt(row, column)
            if (`object` is Date) return mDateRenderer!!
            if (`object` is NodeHolder) return mNodeRenderer!!
            if (`object` is NotesHolder) return mNotesRenderer!!
            return if (`object` is IconsHolder) mIconsRenderer!! else super.getCellRenderer(row, column)
        }

        override fun isCellEditable(rowIndex: Int, vColIndex: Int): Boolean {
            return false
        }

        override fun processKeyEvent(e: KeyEvent) {
            if (e.keyCode == KeyEvent.VK_ENTER) {
                val el: Array<EventListener> = super.getListeners(KeyListener::class.java)
                if (e.id != KeyEvent.KEY_RELEASED) return
                for (i in el.indices) {
                    val kl = el[i] as KeyListener
                    kl.keyReleased(e)
                }
                return
            }
            super.processKeyEvent(e)
        }

        /* (non-Javadoc)
		 * @see javax.swing.JTable#getToolTipText(java.awt.event.MouseEvent)
		 */
        override fun getToolTipText(pEvent: MouseEvent): String {
            val point = pEvent.point
            val row = mTimeTable!!.rowAtPoint(point)
            val colIndex = columnAtPoint(point)
            if (row >= 0 && colIndex >= 0) {
                if (colIndex == NODE_TEXT_COLUMN) {
                    val mindMapNode = getMindMapNode(row)
                    return mindMapNode!!.text!!
                }
                if (colIndex == NODE_NOTES_COLUMN) {
                    val mindMapNode = getMindMapNode(row)
                    return mindMapNode!!.noteText!!
                }
            }
            return null
        }
    }

    internal class DateRenderer : DefaultTableCellRenderer() {
        var formatter: DateFormat? = null
        public override fun setValue(value: Any) {
            if (formatter == null) {
                formatter = DateFormat.getDateTimeInstance()
            }
            text = if (value == null) "" else formatter!!.format(value)
        }
    }

    internal class NodeRenderer : DefaultTableCellRenderer() {
        public override fun setValue(value: Any) {
            val holder = value as NodeHolder
            text = if (value == null) "" else holder
                    .untaggedNodeText
        }
    }

    internal class NotesRenderer : DefaultTableCellRenderer() {
        public override fun setValue(value: Any) {
            text = if (value == null) "" else (value as NotesHolder)
                    .untaggedNotesText
        }
    }

    /** removes html in nodes before comparison.  */
    class NodeHolder
    /**
     *
     */(val node: MindMapNode?) : Comparable<NodeHolder> {

        // cache empty or dirty?:
        var untaggedNodeText: String? = null
            get() {
                val nodeText = node!!.text
                // cache empty or dirty?:
                if (field == null
                        || originalNodeText != null && originalNodeText != nodeText) {
                    originalNodeText = nodeText
                    // remove tags:
                    field = HtmlTools.htmlToPlain(nodeText!!)
                            .replaceAll("\\s+", " ")
                }
                return field
            }
            private set

        /**
         * Holds the original node content to cache the untaggedNodeText and to
         * see whether or not the cache is dirty.
         */
        private var originalNodeText: String? = null
        override fun compareTo(compareToObject: NodeHolder): Int {
            return toString().compareTo(compareToObject.toString())
        }

        override fun toString(): String {
            return untaggedNodeText!!
        }
    }

    /** removes html in notes before comparison.  */
    class NotesHolder
    /**
     *
     */(private val node: MindMapNode?) : Comparable<NotesHolder> {
        // remove tags:
        var untaggedNotesText: String? = null
            get() {
                val notesText = node!!.noteText ?: return ""
                if (field == null
                        || originalNotesText != null && originalNotesText != notesText) {
                    originalNotesText = notesText
                    // remove tags:
                    field = HtmlTools.removeHtmlTagsFromString(
                            notesText).replaceAll("\\s+", " ")
                }
                return field
            }
            private set
        private var originalNotesText: String? = null
        override fun compareTo(compareToObject: NotesHolder): Int {
            return toString().compareTo(compareToObject.toString())
        }

        override fun toString(): String {
            return untaggedNotesText!!
        }
    }

    internal class IconsHolder(node: MindMapNode?) : Comparable<IconsHolder> {
        var icons = Vector<MindIcon?>()
        private val iconNames: Vector<String?>

        init {
            icons.addAll(node!!.icons)
            // sorting the output.
            iconNames = Vector()
            for (icon in icons) {
                iconNames.add(icon!!.getName())
            }
            Collections.sort(iconNames)
        }

        override fun compareTo(compareToObject: IconsHolder): Int {
            return toString().compareTo(compareToObject.toString())
        }

        /** Returns a sorted list of icon names.  */
        override fun toString(): String {
            var result = ""
            for (name in iconNames) {
                result += "$name "
            }
            return result
        }
    }

    internal class IconsRenderer(controller: ModeController?) : DefaultTableCellRenderer() {
        public override fun setValue(value: Any) {
            if (value is IconsHolder) {
                val iconImages = MultipleImage(1.0f)
                for (icon in value.icons) {
                    iconImages.addImage(icon!!.icon)
                }
                icon = if (iconImages.imageCount > 0) {
                    iconImages
                } else {
                    null
                }
            }
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * isMapModuleChangeAllowed(freemind.view.MapModule, freemind.modes.Mode,
	 * freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun isMapModuleChangeAllowed(pOldMapModule: MapModule?,
                                          pOldMode: Mode?, pNewMapModule: MapModule?, pNewMode: Mode?): Boolean {
        return true
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * beforeMapModuleChange(freemind.view.MapModule, freemind.modes.Mode,
	 * freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun beforeMapModuleChange(pOldMapModule: MapModule?, pOldMode: Mode?,
                                       pNewMapModule: MapModule?, pNewMode: Mode?) {
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * freemind.controller.MapModuleManager.MapModuleChangeObserver#afterMapClose
	 * (freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun afterMapClose(pOldMapModule: MapModule?, pOldMode: Mode?) {
        disposeDialog()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * afterMapModuleChange(freemind.view.MapModule, freemind.modes.Mode,
	 * freemind.view.MapModule, freemind.modes.Mode)
	 */
    override fun afterMapModuleChange(pOldMapModule: MapModule?, pOldMode: Mode?,
                                      pNewMapModule: MapModule?, pNewMode: Mode) {
        disposeDialog()
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see freemind.controller.MapModuleManager.MapModuleChangeObserver#
	 * numberOfOpenMapInformation(int, int)
	 */
    override fun numberOfOpenMapInformation(pNumber: Int, pIndex: Int) {}
    fun updateStatistics() {
        val lsm = mTimeTable!!.selectionModel
        var count = 0
        val rowCount = mFlatNodeTableFilterModel!!.rowCount
        for (i in 0 until rowCount) {
            if (lsm.isSelectedIndex(i)) {
                count++
            }
        }
        mStatisticsLabel!!.text = Resources.getInstance().format("timelist_statistics", arrayOf(count, rowCount, mTimeTableModel!!.rowCount, if (mViewFoldedNodes) 1 else 0))
    }

    companion object {
        private const val TYPE_DELAY_TIME = 500
        private var COLUMN_MODIFIED = "Modified"
        private var COLUMN_CREATED = "Created"
        private var COLUMN_ICONS = "Icons"
        private var COLUMN_TEXT = "Text"
        private var COLUMN_DATE = "Date"
        private var COLUMN_NOTES = "Notes"
        private const val DATE_COLUMN = 0
        const val NODE_TEXT_COLUMN = 1
        protected const val NODE_ICON_COLUMN = 2
        protected const val NODE_CREATED_COLUMN = 3
        protected const val NODE_MODIFIED_COLUMN = 4
        protected const val NODE_NOTES_COLUMN = 5
        private val WINDOW_PREFERENCE_STORAGE_PROPERTY = TimeList::class.java
                .name + "_properties"

        fun replace(info: IReplaceInputInformation,
                    searchString: String, replaceString: String) {
            val regExp = "($searchString)"
            val p = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE)
            // String replacement = getPureRegularExpression(replaceString);
            val length = info.length
            for (i in 0 until length) {
                val nodeHolder = info.getNodeHolderAt(i)
                val text = nodeHolder!!.node!!.text
                val replaceResult: String = HtmlTools.getInstance().getReplaceResult(p,
                        replaceString, text)
                if (!safeEquals(text, replaceResult)) {
                    // set new node text only, if different.
                    info.changeString(nodeHolder, replaceResult)
                }
            }
        }

        @Throws(BadLocationException::class)
        fun getRegularExpression(text: String): String {
            var text = text
            text = ".*($text).*"
            return text
        }

        /**
         * Removes all regular expression stuff with exception of "*", which is
         * replaced by ".*".
         */
        fun getPureRegularExpression(text: String): String {
            // remove regexp:
            var text = text
            text = text.replace("([().\\[\\]^$|])".toRegex(), "\\\\$1")
            text = text.replace("\\*".toRegex(), ".*")
            return text
        }
    }
}