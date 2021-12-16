/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2005   Christian Foltin.
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
 * Created on 06.05.2005
 */
package freemind.preferences.layout

import freemind.main.FreeMind.controller
import freemind.controller.actions.generated.instance.OptionPanelWindowConfigurationStorage.panel
import freemind.common.PropertyBean.label
import freemind.main.FreeMind.getAdjustableProperty
import freemind.common.PropertyBean.value
import freemind.common.PropertyControl.layout
import freemind.main.FreeMind.getResourceString
import freemind.common.PropertyBean.firePropertyChangeEvent
import freemind.common.TextTranslator.getText
import freemind.main.Tools.removeTranslateComment
import freemind.main.FreeMind.getDefaultProperty
import freemind.controller.Controller.modeController
import freemind.modes.IconInformation.keystrokeResourceName
import freemind.modes.IconInformation.description
import freemind.modes.IconInformation.icon
import freemind.main.FreeMindMain.getResourceString
import freemind.main.Tools.isMacOsX
import freemind.main.FreeMind
import javax.swing.JDialog
import freemind.preferences.layout.OptionPanel.OptionPanelFeedback
import freemind.preferences.layout.OptionPanel.KeyProperty
import javax.swing.JButton
import freemind.preferences.layout.OptionPanel.ChangeTabAction
import freemind.preferences.layout.OptionPanel
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.builder.DefaultFormBuilder
import java.awt.CardLayout
import freemind.preferences.layout.VariableSizeCardLayout
import javax.swing.JPanel
import freemind.preferences.layout.OptionPanel.NewTabProperty
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import java.awt.BorderLayout
import java.awt.event.ActionListener
import java.awt.event.ActionEvent
import com.jgoodies.forms.builder.ButtonBarBuilder
import kotlin.jvm.JvmOverloads
import javax.swing.ImageIcon
import com.jgoodies.forms.layout.RowSpec
import freemind.common.*
import freemind.preferences.layout.GrabKeyDialog
import java.awt.event.KeyEvent
import javax.swing.JLabel
import freemind.main.FreeMindCommon
import freemind.main.Tools
import freemind.modes.MindMapNode
import javax.swing.UIManager.LookAndFeelInfo
import javax.swing.UIManager
import freemind.controller.StructuredMenuHolder
import freemind.modes.MindIcon
import freemind.modes.ModeController
import freemind.modes.mindmapmode.MindMapController
import freemind.modes.mindmapmode.actions.IconAction
import freemind.modes.IconInformation
import freemind.preferences.FreemindPropertyContributor
import freemind.controller.actions.generated.instance.OptionPanelWindowConfigurationStorage
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import java.awt.Color
import freemind.main.FreeMindMain
import freemind.preferences.layout.GrabKeyDialog.InputPane
import java.awt.AWTEvent
import java.awt.GridLayout
import javax.swing.WindowConstants
import javax.swing.JTextField
import freemind.preferences.layout.KeyEventWorkaround
import freemind.preferences.layout.KeyEventTranslator
import java.lang.StringBuffer
import javax.swing.JOptionPane
import java.awt.event.InputEvent
import java.awt.Dimension
import java.awt.Insets
import java.util.*

/**
 * @author foltin
 */
class OptionPanel(fm: FreeMind, frame: JDialog, feedback: OptionPanelFeedback) : TextTranslator {
    private var controls: Vector<PropertyControl?>? = null
    private fun findControlByKB(binding: GrabKeyDialog.KeyBinding?): KeyProperty? {
        for (control in controls!!) {
            if (control is KeyProperty) {
                val k = control
                if (k.kb == binding) return k
            }
        }
        return null
    }

    private val frame: JDialog
    private val tabButtonMap = HashMap<String?, JButton>()
    private val tabActionMap = HashMap<String?, ChangeTabAction>()
    private var selectedPanel: String? = null
    private val feedback: OptionPanelFeedback
    private val allBindings: Vector<GrabKeyDialog.KeyBinding>

    interface OptionPanelFeedback {
        fun writeProperties(props: Properties?)
    }

    /**
     */
    fun setProperties() {
        val i: Iterator<PropertyControl?> = controls!!.iterator()
        while (i.hasNext()) {
            val control = i.next()
            if (control is PropertyBean) {
                val bean = control as PropertyBean
                // System.out.println("grep -n -e \""+bean.getLabel()+"\" -r * |
                // grep -e \"\\.(java|xml):\"");
                val label = bean.label
                val value = fmMain!!.getAdjustableProperty(label!!)
                // System.out.println("Setting property " + bean.getLabel()
                // + " to " + value);
                bean.value = value
            }
        }
    }

    private val optionProperties: Properties
        private get() {
            val p = Properties()
            val i: Iterator<PropertyControl?> = controls!!.iterator()
            while (i.hasNext()) {
                val control = i.next()
                if (control is PropertyBean) {
                    val bean = control as PropertyBean
                    val value = bean.value
                    if (value != null) p.setProperty(bean.label, value)
                }
            }
            return p
        }

    fun buildPanel() {
        val leftLayout = FormLayout("80dlu", "")
        val leftBuilder = DefaultFormBuilder(leftLayout)
        val cardLayout: CardLayout = VariableSizeCardLayout()
        val rightStack = JPanel(cardLayout)
        var rightLayout: FormLayout? = null // add rows dynamically
        var rightBuilder: DefaultFormBuilder? = null
        var lastTabName: String? = null
        controls = getControls()
        val i: Iterator<PropertyControl?> = controls!!.iterator()
        while (i.hasNext()) {
            val control = i.next()
            // System.out.println("layouting : " + control.getLabel());
            if (control is NewTabProperty) {
                val newTab = control
                if (rightBuilder != null) {
                    // terminate old panel:
                    rightStack.add(rightBuilder.panel, lastTabName)
                }
                rightLayout = FormLayout(newTab.description, "")
                rightBuilder = DefaultFormBuilder(rightLayout)
                rightBuilder.setDefaultDialogBorder()
                lastTabName = newTab.label
                // add a button to the left side:
                val tabButton = JButton(getText(lastTabName))
                val changeTabAction = ChangeTabAction(
                        cardLayout, rightStack, lastTabName)
                tabButton.addActionListener(changeTabAction)
                registerTabButton(tabButton, lastTabName, changeTabAction)
                leftBuilder.append(tabButton)
            } else {
                control!!.layout(rightBuilder!!, this)
            }
        }
        // add the last one, too
        rightStack.add(rightBuilder!!.panel, lastTabName)
        // select one panel:
        if (selectedPanel != null && tabActionMap.containsKey(selectedPanel)) {
            tabActionMap[selectedPanel]
                    .actionPerformed(null)
        }
        val rightScrollPane = JScrollPane(rightStack)
        rightScrollPane.verticalScrollBar.unitIncrement = 100
        val centralPanel = JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                leftBuilder.panel, rightScrollPane)
        frame.contentPane.add(centralPanel, BorderLayout.CENTER)
        val cancelButton = JButton(getText("Cancel"))
        cancelButton.addActionListener { closeWindow() }
        val okButton = JButton(getText("OK"))
        okButton.addActionListener {
            feedback.writeProperties(optionProperties)
            closeWindow()
        }
        frame.rootPane.defaultButton = okButton
        frame.contentPane.add(
                ButtonBarBuilder().addGlue().addButton(cancelButton).addButton(okButton).build(),
                BorderLayout.SOUTH)
    }

    /**
     */
    override fun getText(string: String?): String {
        if (string == null) return null
        checkConnectionToFreeMindMain()
        return fmMain!!.getResourceString("OptionPanel.$string")!!
    }

    /**
     */
    private fun registerTabButton(tabButton: JButton, name: String?,
                                  changeTabAction: ChangeTabAction) {
        tabButtonMap[name] = tabButton
        tabActionMap[name] = changeTabAction
        // if no default panel was given, we use the first.
        if (selectedPanel == null) {
            selectedPanel = name
        }
    }

    private fun getTabButton(name: String?): JButton? {
        return tabButtonMap[name]
    }

    private val allButtons: Collection<JButton>
        private get() = tabButtonMap.values

    private inner class ChangeTabAction(private val cardLayout: CardLayout, private val centralPanel: JPanel,
                                        private val tabName: String?) : ActionListener {
        override fun actionPerformed(arg0: ActionEvent) {
            cardLayout.show(centralPanel, tabName)
            // design: mark selected button with a color
            val c = allButtons
            for (button in c) {
                button.foreground = null
            }
            getTabButton(tabName)!!.foreground = MARKED_BUTTON_COLOR
            selectedPanel = tabName
        }
    }

    class NewTabProperty @JvmOverloads constructor(override val label: String, override val description: String = DEFAULT_LAYOUT_FORMAT) : PropertyControl {

        override fun layout(builder: DefaultFormBuilder,
                            pTranslator: TextTranslator) {
        }

        override fun setEnabled(pEnabled: Boolean) {}
    }

    private inner class KeyProperty(frame: JDialog?, override var description: String?, override var label: String?) : PropertyBean(), PropertyControl {
        private var modifierMask = 0
        var mButton = JButton()
        private var labelText: String? = null
        private var icon: ImageIcon? = null
        private var rowSpec: RowSpec? = null
        var kb: GrabKeyDialog.KeyBinding

        /**
         */
        init {
            kb = createBinding(getText(label), label, fmMain!!.getAdjustableProperty(label!!), this)
            mButton.addActionListener {
                val dialog = GrabKeyDialog(fmMain, frame,
                        GrabKeyDialog.KeyBinding(getLabel(), getLabel(), getValue(), false),
                        allBindings, null, modifierMask)
                if (dialog.isOK) {
                    if (dialog.bindingReset != null) {
                        val k = findControlByKB(dialog.bindingReset)
                        k?.setValue("")
                    }
                    setValue(dialog.shortcut)
                    firePropertyChangeEvent()
                }
            }
        }

        fun disableModifiers() {
            modifierMask = KeyEvent.ALT_MASK or KeyEvent.CTRL_MASK
        }

        override var value: String?
            get() = mButton.text
            set(value) {
                mButton.text = value
                mButton.toolTipText = mButton.text
            }

        override fun layout(builder: DefaultFormBuilder,
                            pTranslator: TextTranslator) {
            if (labelText == null) labelText = pTranslator.getText(getLabel())
            val label = JLabel(labelText, icon, JLabel.RIGHT)
            label.toolTipText = pTranslator.getText(getDescription())
            if (rowSpec == null) {
                rowSpec = RowSpec.decode("fill:20dlu")
            }
            if (3 < builder.column) {
                builder.appendRelatedComponentsGapRow()
                builder.appendRow(rowSpec)
                builder.nextLine(2)
            } else {
                builder.nextColumn(2)
            }
            builder.add(label)
            builder.nextColumn(2)
            builder.add(mButton)
        }

        override fun setEnabled(pEnabled: Boolean) {
            mButton.isEnabled = pEnabled
        }

        fun setLabelText(labelText: String?) {
            this.labelText = labelText
        }

        fun setImageIcon(icon: ImageIcon?) {
            this.icon = icon
        }
    }

    //
    private fun getControls(): Vector<PropertyControl?> {
        val controls = Vector<PropertyControl?>()
        /***********************************************************************
         * Language
         * ****************************************************************
         */
        controls.add(NewTabProperty("Environment"))
        controls.add(SeparatorProperty("language"))
        // TODO: Search class path for translations.
        controls.add(ComboProperty(/**
         * For the codes see
         * http://www.loc.gov/standards/iso639-2/php/English_list.php
         */
                "language.tooltip", FreeMindCommon.RESOURCE_LANGUAGE, arrayOf("automatic", "ar", "bg", "cs", "de", "dk", "en",
                "el", "es", "et", "eu", "fr", "gl", "hr", "hu", "id",
                "it", "ja", "ko", "lt", "nl", "nn", "nb", "pl",
                "pt_BR", "pt_PT", "ro", "ru", "sk", "se", "sl", "sr",
                "tr", "uk_UA", "vi", "zh_TW", "zh_CN"),
                object : TextTranslator {
                    override fun getText(pKey: String?): String {
                        // decorator, that removes "TranslateMe" comments.
                        return removeTranslateComment(this@OptionPanel
                                .getText(pKey))!!
                    }
                })) // automatic
        controls.add(BooleanProperty(FreeMindCommon.CHECK_SPELLING
                + TOOLTIP_EXT, FreeMindCommon.CHECK_SPELLING)) // true

        // INTERNAL PROPERTY.
        // controls
        // .add(new StringProperty(
        // "The Modes which Freemind will load on startup, full Class names,
        // comma, identifier, separated by a comma.",
        // "modes_since_0_8_0")); //
        // freemind.modes.browsemode.BrowseMode,Browse,freemind.modes.mindmapmode.MindMapMode,MindMap,freemind.modes.filemode.FileMode,File
        //
        // controls.add(new StringProperty(
        // "The initial mode that is loaded on startup", "initial_mode")); //
        // MindMap
        //
        // controls
        // .add(new StringProperty(
        // "This is the place where the users properties file is located. It is
        // ignored by the applet (set Parameters in the html file instead). You
        // can write '~' to indicate the users home directory. Of course this
        // works only in the default 'freemind.properties', which is included in
        // the jar file, not for the users freemind.props out of the jar file.",
        // "properties_folder")); // .freemind
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("proxy"))
        controls.add(BooleanProperty(FreeMind.PROXY_USE_SETTINGS
                + TOOLTIP_EXT, FreeMind.PROXY_USE_SETTINGS))
        controls.add(StringProperty(FreeMind.PROXY_HOST + TOOLTIP_EXT,
                FreeMind.PROXY_HOST))
        controls.add(NumberProperty(FreeMind.PROXY_PORT + TOOLTIP_EXT,
                FreeMind.PROXY_PORT, 1, 65535, 1))
        controls.add(BooleanProperty(FreeMind.PROXY_IS_AUTHENTICATED
                + TOOLTIP_EXT, FreeMind.PROXY_IS_AUTHENTICATED))
        controls.add(StringProperty(FreeMind.PROXY_USER + TOOLTIP_EXT,
                FreeMind.PROXY_USER))
        controls.add(PasswordProperty(
                FreeMind.PROXY_PASSWORD + TOOLTIP_EXT, FreeMind.PROXY_PASSWORD))
        controls.add(StringProperty(FreeMind.PROXY_EXCEPTION + TOOLTIP_EXT,
                FreeMind.PROXY_EXCEPTION))
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("files"))
        controls.add(NumberProperty(null, "last_opened_list_length", 0,
                200, 1)) // 25
        controls.add(BooleanProperty(FreeMindCommon.LOAD_LAST_MAP
                + TOOLTIP_EXT, FreeMindCommon.LOAD_LAST_MAP)) // true
        controls.add(BooleanProperty(FreeMind.RESOURCES_DON_T_OPEN_PORT
                + TOOLTIP_EXT, FreeMind.RESOURCES_DON_T_OPEN_PORT))
        controls.add(BooleanProperty(
                FreeMindCommon.LOAD_LAST_MAPS_AND_LAYOUT + TOOLTIP_EXT,
                FreeMindCommon.LOAD_LAST_MAPS_AND_LAYOUT)) // true
        controls.add(BooleanProperty(
                "experimental_file_locking_on.tooltip", "experimental_file_locking_on")) // false
        controls.add(NextLineProperty())
        controls.add(StringProperty(null, "userproperties")) // user.properties

        // FIXME: Remove autoproperties from Freemind.
        // controls.add(new StringProperty(null, "autoproperties")); //
        // auto.properties
        controls.add(StringProperty(null, "patternsfile")) // patterns.xml
        // FIXME: Which one? controls.add(new StringProperty(
        // "The URL of the documentation mindmap (.mm)", "docmapurl")); //
        // ./doc/freemind.mm

        // replaced by browsemode_initial_map?? (See Controller,
        // DocumentationAction).
        // controls.add(new StringProperty(null,
        // "docmapurl_since_version_0_7_0")); // ./doc/freemind.mm
        //
        // The Browse Mode
        //
        controls.add(StringProperty("browsemode_initial_map.tooltip",
                "browsemode_initial_map")) // ./doc/freemind.mm
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("automatic_save"))
        controls.add(StringProperty("time_for_automatic_save.tooltip",
                "time_for_automatic_save")) // 60000
        //
        controls.add(BooleanProperty(
                "delete_automatic_saves_at_exit.tooltip",
                "delete_automatic_saves_at_exit")) // true
        controls.add(StringProperty(
                "number_of_different_files_for_automatic_save.tooltip",
                "number_of_different_files_for_automatic_save")) // 10
        controls.add(StringProperty("path_to_automatic_saves.tooltip",
                "path_to_automatic_saves")) // freemind_home
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("save"))
        controls.add(BooleanProperty(
                "resources_save_folding_state.tooltip",
                FreeMind.RESOURCES_SAVE_FOLDING_STATE)) // true
        controls.add(BooleanProperty(
                "save_only_intrisically_needed_ids.tooltip",
                FreeMindCommon.SAVE_ONLY_INTRISICALLY_NEEDED_IDS)) // false
        /***********************************************************************
         * Defaults
         * ****************************************************************
         */
        controls.add(NewTabProperty("Defaults"))
        controls.add(SeparatorProperty("default_styles"))
        controls.add(ComboProperty("standardnodestyle.tooltip",
                FreeMind.RESOURCES_NODE_STYLE, MindMapNode.NODE_STYLES, this)) // as_parent
        controls.add(ComboProperty("standardrootnodestyle.tooltip",
                FreeMind.RESOURCES_ROOT_NODE_STYLE, arrayOf(
                MindMapNode.STYLE_FORK, MindMapNode.STYLE_BUBBLE,
                MindMapNode.STYLE_COMBINED), this)) // fork
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("default_colors"))
        controls.add(ColorProperty("standardnodetextcolor.tooltip",
                FreeMind.RESOURCES_NODE_TEXT_COLOR,
                fmMain!!.getDefaultProperty(FreeMind.RESOURCES_NODE_TEXT_COLOR),
                this)) // #000000
        controls.add(ColorProperty("standardedgecolor.tooltip",
                FreeMind.RESOURCES_EDGE_COLOR, fmMain
                .getDefaultProperty(FreeMind.RESOURCES_EDGE_COLOR),
                this)) // #808080
        controls.add(ColorProperty("standardlinkcolor.tooltip",
                FreeMind.RESOURCES_LINK_COLOR, fmMain
                .getDefaultProperty(FreeMind.RESOURCES_LINK_COLOR),
                this)) // #b0b0b0
        controls.add(ColorProperty("standardbackgroundcolor.tooltip",
                FreeMind.RESOURCES_BACKGROUND_COLOR,
                fmMain!!.getDefaultProperty(FreeMind.RESOURCES_BACKGROUND_COLOR),
                this)) // #ffffff
        controls.add(BooleanProperty(
                FreeMind.RESOURCE_PRINT_ON_WHITE_BACKGROUND + TOOLTIP_EXT,
                FreeMind.RESOURCE_PRINT_ON_WHITE_BACKGROUND)) // true
        controls.add(ColorProperty("standardcloudcolor.tooltip",
                FreeMind.RESOURCES_CLOUD_COLOR, fmMain
                .getDefaultProperty(FreeMind.RESOURCES_CLOUD_COLOR),
                this)) // #f0f0f0
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("default_fonts"))
        controls.add(StringProperty(
                "defaultfont.tooltip", "defaultfont")) // SansSerif
        controls.add(StringProperty(null, "defaultfontstyle")) // 0
        controls.add(NumberProperty("defaultfontsize.tooltip",
                "defaultfontsize", 1,
                96 /* taken from openoffice as maximum. */, 1)) // 12
        // controls.add(new StringProperty(null, "defaultfontsize")); // 12
        controls.add(NumberProperty("max_node_width.tooltip",
                "max_node_width", 1, Int.MAX_VALUE, 1)) // 600
        controls.add(NumberProperty("max_tooltip_width.tooltip",
                "max_tooltip_width", 1, Int.MAX_VALUE, 1)) // 600
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("other_defaults"))
        controls.add(ComboProperty("standardedgestyle.tooltip",
                FreeMind.RESOURCES_EDGE_STYLE, arrayOf("bezier",
                "linear"), this)) // bezier

        // controls.add(new ComboProperty(
        //
        // "standardcloudestyle.tooltip", "standardcloudestyle",
        // new String[] { "bezier" })); // bezier
        //
        // controls.add(new ComboProperty(
        //
        // "standardlinkestyle.tooltip", "standardlinkestyle",
        // new String[] { "bezier" })); // bezier
        /***********************************************************************
         * Appearance
         * ****************************************************************
         */
        controls.add(NewTabProperty("Appearance"))
        controls.add(SeparatorProperty("look_and_feel"))
        val lafInfo = UIManager.getInstalledLookAndFeels()
        val reservedCount = 6
        val lafNames = arrayOfNulls<String>(lafInfo.size + reservedCount)
        val translatedLafNames = Vector<String>()
        lafNames[0] = "default"
        translatedLafNames.add(getText("default"))
        lafNames[1] = "metal"
        translatedLafNames.add(getText("metal"))
        lafNames[2] = "windows"
        translatedLafNames.add(getText("windows"))
        lafNames[3] = "motif"
        translatedLafNames.add(getText("motif"))
        lafNames[4] = "gtk"
        translatedLafNames.add(getText("gtk"))
        lafNames[5] = "nothing"
        translatedLafNames.add(getText("nothing"))
        for (i in lafInfo.indices) {
            val info = lafInfo[i]
            val className = info.className
            lafNames[i + reservedCount] = className
            translatedLafNames.add(info.name)
        }
        controls.add(ComboProperty("lookandfeel.tooltip",
                FreeMind.RESOURCE_LOOKANDFEEL, lafNames, translatedLafNames)) // default
        controls.add(NumberProperty(
                FreeMind.SCALING_FACTOR_PROPERTY + TOOLTIP_EXT,
                FreeMind.SCALING_FACTOR_PROPERTY, 100, Int.MAX_VALUE, 1))
        controls.add(BooleanProperty("use_tabbed_pane.tooltip",
                FreeMind.RESOURCES_USE_TABBED_PANE)) // true
        controls.add(ComboProperty(FreeMind.J_SPLIT_PANE_SPLIT_TYPE
                + TOOLTIP_EXT, FreeMind.J_SPLIT_PANE_SPLIT_TYPE, arrayOf(FreeMind.VERTICAL_SPLIT_BELOW,
                FreeMind.HORIZONTAL_SPLIT_RIGHT), this))
        controls.add(NumberProperty(
                StructuredMenuHolder.AMOUNT_OF_VISIBLE_MENU_ITEMS + TOOLTIP_EXT,
                StructuredMenuHolder.AMOUNT_OF_VISIBLE_MENU_ITEMS, 10, Int.MAX_VALUE, 1))
        controls.add(BooleanProperty(FreeMind.RESOURCES_DISPLAY_FOLDING_BUTTONS + TOOLTIP_EXT,
                FreeMind.RESOURCES_DISPLAY_FOLDING_BUTTONS)) // true
        controls.add(BooleanProperty(null, "el__show_icon_for_attributes")) // true

        // controls.add(new BooleanProperty(
        //
        // "use_split_pane.tooltip", FreeMind.RESOURCES_USE_SPLIT_PANE)); //
        // true

        /* ***************************************************************** */controls.add(NextLineProperty())
        controls.add(SeparatorProperty("selection_colors"))
        controls.add(BooleanProperty(
                FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION + TOOLTIP_EXT,
                FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION)) // false
        controls.add(ColorProperty(
                "standardselectednoderectanglecolor.tooltip",
                FreeMind.RESOURCES_SELECTED_NODE_RECTANGLE_COLOR,
                fmMain!!.getDefaultProperty(FreeMind.RESOURCES_SELECTED_NODE_RECTANGLE_COLOR),
                this)) // #000000
        controls.add(ColorProperty(
                "standardselectednodecolor.tooltip",
                FreeMind.RESOURCES_SELECTED_NODE_COLOR,
                fmMain!!.getDefaultProperty(FreeMind.RESOURCES_SELECTED_NODE_COLOR),
                this)) // #D2D2D2

        /* ***************************************************************** */controls.add(NextLineProperty())
        val RESOURCE_ROOT_NODE = "root_node_appearance"
        val RESOURCE_USE_COMMON_OUT_POINT_FOR_ROOT_NODE = "use_common_out_point_for_root_node"
        controls.add(SeparatorProperty(RESOURCE_ROOT_NODE))
        controls.add(BooleanProperty(
                RESOURCE_USE_COMMON_OUT_POINT_FOR_ROOT_NODE + TOOLTIP_EXT,
                RESOURCE_USE_COMMON_OUT_POINT_FOR_ROOT_NODE)) // false
        /* ***************************************************************** */controls.add(NextLineProperty())
        controls.add(SeparatorProperty("anti_alias"))
        controls.add(ComboProperty("antialias.tooltip",
                FreeMindCommon.RESOURCE_ANTIALIAS, arrayOf(
                "antialias_edges", "antialias_all", "antialias_none"),
                this)) // true

        /* ***************************************************************** */controls.add(NextLineProperty())
        controls.add(SeparatorProperty("initial_map_size"))
        controls.add(StringProperty("mapxsize.tooltip", "mapxsize")) // 1000
        controls.add(StringProperty(null, "mapysize")) // 3200

        /* ***************************************************************** */controls.add(NextLineProperty())
        controls.add(SeparatorProperty("hyperlink_types"))
        controls.add(ComboProperty("links.tooltip", "links", arrayOf(
                "relative", "absolute"), this)) // relative
        controls.add(BooleanProperty(
                FreeMindCommon.CREATE_THUMBNAIL_ON_SAVE + TOOLTIP_EXT,
                FreeMindCommon.CREATE_THUMBNAIL_ON_SAVE)) // true
        controls.add(NumberProperty(FreeMindCommon.THUMBNAIL_SIZE
                + TOOLTIP_EXT, FreeMindCommon.THUMBNAIL_SIZE, 1, 10000, 1))

        /* ***************************************************************** */controls.add(NextLineProperty())
        controls.add(SeparatorProperty("edit_long_node_window"))
        controls.add(StringProperty("el__buttons_position.tooltip",
                "el__buttons_position")) // above
        controls.add(BooleanProperty(null, "el__position_window_below_node")) // true
        controls.add(StringProperty(null, "el__min_default_window_height")) // 150
        controls.add(StringProperty(null, "el__max_default_window_height")) // 500
        controls.add(StringProperty(null, "el__min_default_window_width")) // 600
        controls.add(StringProperty(null, "el__max_default_window_width")) // 600
        controls.add(BooleanProperty(null, "el__enter_confirms_by_default")) // true
        controls.add(SeparatorProperty("note_properties"))
        controls.add(BooleanProperty(null,
                FreeMind.RESOURCES_DON_T_SHOW_NOTE_ICONS))
        controls.add(BooleanProperty(null,
                FreeMind.RESOURCES_DON_T_SHOW_NOTE_TOOLTIPS))
        controls.add(NumberProperty(FreeMind.TOOLTIP_DISPLAY_TIME
                + ".tooltip", FreeMind.TOOLTIP_DISPLAY_TIME, 0, Int.MAX_VALUE, 1)) // 4000
        controls.add(SeparatorProperty("icon_properties"))
        controls.add(StringProperty("icon_order_description",
                MindIcon.PROPERTY_STRING_ICONS_LIST))
        controls.add(NumberProperty(null, FreeMind.ICON_BAR_COLUMN_AMOUNT, 1, 10, 1))
        /***********************************************************************
         * Keystrokes
         * ****************************************************************
         */
        val form = "right:max(40dlu;p), 4dlu, 80dlu, 7dlu"
        controls.add(NewTabProperty("Keystrokes", "$form,$form")) // ", right:max(40dlu;p), 4dlu, 60dlu"));
        //
        // These are the accelerators for the menu items. Valid modifiers are:
        // shift | control | alt | meta | button1 | button2 | button3
        // Valid keys should be all that are defined in java.awt.event.KeyEvent
        // (without the "VK_" prefix), but I found this buggy. All normal char's
        // should work.

        // The ideas employed in choice of keyboard shortcuts are:
        // If there is a standard for a feature, use it
        // Use control modifier whereever possible

        // Commands for the program
        controls.add(SeparatorProperty("commands_for_the_program"))
        controls.add(KeyProperty(frame, null, "keystroke_newMap")) // control
        // N
        controls.add(KeyProperty(frame, null, "keystroke_open")) // control
        // O
        controls.add(KeyProperty(frame, null, "keystroke_save")) // control
        // S
        controls.add(KeyProperty(frame, null, "keystroke_saveAs")) // control
        // shift
        // S
        controls.add(KeyProperty(frame, null, "keystroke_print")) // control
        // P
        controls.add(KeyProperty(frame, null, "keystroke_close")) // control
        // W
        controls.add(KeyProperty(frame, null, "keystroke_quit")) // control
        // Q
        controls.add(KeyProperty(frame, null, "keystroke_option_dialog")) // control
        // COMMA
        controls.add(KeyProperty(frame, null, "keystroke_export_to_html")) // control
        // E
        controls.add(KeyProperty(frame, null,
                "keystroke_export_branch_to_html")) // control
        // H
        controls.add(KeyProperty(frame, null,
                "keystroke_open_first_in_history")) // control
        // shift
        // W
        controls.add(KeyProperty(frame, null,
                FreeMind.KEYSTROKE_PREVIOUS_MAP)) // control
        // LEFT
        controls.add(KeyProperty(frame, null, FreeMind.KEYSTROKE_NEXT_MAP)) // control
        // RIGHT
        controls.add(KeyProperty(frame, null,
                FreeMind.KEYSTROKE_MOVE_MAP_LEFT))
        controls.add(KeyProperty(frame, null,
                FreeMind.KEYSTROKE_MOVE_MAP_RIGHT))
        controls.add(KeyProperty(frame, null, "keystroke_mode_MindMap")) // alt
        // 1
        controls.add(KeyProperty(frame, null, "keystroke_mode_Browse")) // alt
        // 2
        controls.add(KeyProperty(frame, null, "keystroke_mode_File")) // alt
        // 3
        controls.add(KeyProperty(frame, null,
                "keystroke_node_toggle_italic")) // control
        // I
        controls.add(KeyProperty(frame, null,
                "keystroke_node_toggle_boldface")) // control
        // B
        controls.add(KeyProperty(frame, null,
                "keystroke_node_toggle_strikethrough"))
        controls.add(KeyProperty(frame, null,
                "keystroke_node_toggle_underlined")) // control
        // U
        controls.add(KeyProperty(frame, null, "keystroke_node_toggle_cloud")) // control
        // shift
        // B
        controls.add(KeyProperty(frame, null, "keystroke_undo")) // control
        // Z
        controls.add(KeyProperty(frame, null, "keystroke_redo")) // control
        // Y
        controls.add(KeyProperty(frame, null, "keystroke_delete_child")) // DELETE
        controls.add(KeyProperty(frame, null, "keystroke_select_all")) // control
        // A
        controls.add(KeyProperty(frame, null, "keystroke_select_branch")) // control
        // shift A
        controls.add(KeyProperty(frame, null, "keystroke_zoom_out")) // alt UP
        controls.add(KeyProperty(frame, null, "keystroke_zoom_in")) // alt DOWN

        // Node editing commands
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("node_editing_commands"))
        controls.add(KeyProperty(frame, null, "keystroke_cut")) // control
        // X
        controls.add(KeyProperty(frame, null, "keystroke_copy")) // control
        // C
        controls.add(KeyProperty(frame, null, "keystroke_copy_single")) // control
        // shift C
        controls.add(KeyProperty(frame, null, "keystroke_paste")) // control
        // V
        controls.add(KeyProperty(frame, null, "keystroke_pasteAsPlainText"))
        controls.add(KeyProperty(frame, null, "keystroke_remove")) // none
        controls.add(KeyProperty(frame, null,
                "keystroke_add_arrow_link_action")) // control
        // L
        controls.add(KeyProperty(frame, null,
                "keystroke_add_local_link_action")) // alt

        // L

        // Unline with control X, the node you remove with action remove cannot
        // be
        // pasted again. Therefore, we do not provide any quick shortcut. We
        // suggest
        // that you use cut instead of remove.

        // Node navigation commands
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("node_navigation_commands"))
        controls.add(KeyProperty(frame, null, "keystroke_moveToRoot")) // ESCAPE
        controls.add(KeyProperty(frame, null, "keystroke_move_up")) // E
        controls.add(KeyProperty(frame, null, "keystroke_move_down")) // D
        controls.add(KeyProperty(frame, null, "keystroke_move_left")) // S
        controls.add(KeyProperty(frame, null, "keystroke_move_right")) // F
        controls.add(KeyProperty(frame, null, "keystroke_follow_link")) // control

        // ENTER

        // New node commands
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("new_node_commands"))
        controls.add(KeyProperty(frame, null, "keystroke_add")) // ENTER
        controls.add(KeyProperty(frame, null, "keystroke_add_child")) // INSERT
        controls.add(KeyProperty(frame, null, "keystroke_add_child_mac")) // TAB
        controls.add(KeyProperty(frame, null,
                "keystroke_add_sibling_before")) // shift

        // ENTER

        // Node editing commands
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("node_editing_commands"))
        controls.add(KeyProperty(frame, null, "keystroke_edit")) // F2
        controls.add(KeyProperty(frame, null, "keystroke_edit_long_node")) // alt
        // ENTER
        controls.add(KeyProperty(frame, null, "keystroke_join_nodes")) // control
        // J
        controls.add(KeyProperty(frame, null, "keystroke_toggle_folded")) // SPACE
        controls.add(KeyProperty(frame, null,
                "keystroke_toggle_children_folded")) // control
        // SPACE
        controls.add(KeyProperty(frame, null,
                "keystroke_set_link_by_filechooser")) // control
        // shift
        // K
        controls.add(KeyProperty(frame, null,
                "keystroke_set_link_by_textfield")) // control
        // K
        controls.add(KeyProperty(frame, null,
                "keystroke_set_image_by_filechooser")) // alt
        // K
        controls.add(KeyProperty(frame, null, "keystroke_node_up")) // control
        // UP
        controls.add(KeyProperty(frame, null, "keystroke_node_down")) // control
        // DOWN
        controls.add(KeyProperty(frame, null,
                "keystroke_node_increase_font_size")) // control
        // PLUS
        controls.add(KeyProperty(frame, null,
                "keystroke_node_decrease_font_size")) // control
        // MINUS

        // controls.add(new KeyProperty(frame, null,
        // "keystroke_branch_increase_font_size")); // control
        // // shift
        // // PLUS
        //
        // controls.add(new KeyProperty(frame, null,
        // "keystroke_branch_decrease_font_size")); // control
        // // shift
        // // MINUS
        //
        controls.add(KeyProperty(frame, null, "keystroke_export_branch")) // alt
        // A
        //
        controls.add(KeyProperty(frame, null, "keystroke_node_color")) // alt
        // F
        controls.add(KeyProperty(frame, null, "keystroke_node_color_blend")) // alt
        // B
        controls.add(KeyProperty(frame, null, "keystroke_edge_color")) // alt
        // E
        controls.add(KeyProperty(frame, null, "keystroke_find")) // ctrl F
        controls.add(KeyProperty(frame, null, "keystroke_find_next")) // ctrl
        // G

        // Apply patterns

        // There is no limiting number of the pattern, you can have as many
        // keystrokes for patterns as you want. The reason I do not follow to
        // F10 and further in this default is that F10 has special function on
        // Windows.
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("patterns"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/ManagePatterns_manage_patterns_dialog")) // control
        // shift
        // F1
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_1")) // F1
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_2")) // control
        // shift
        // N
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_3")) // F3
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_4")) // F4
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_5")) // F5
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_6")) // F6
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_7")) // F7
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_8")) // F8
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_9")) // F9
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_10")) // control
        // F1
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_11")) // control
        // F2
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_12")) // control
        // F3
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_13")) // control

        // F4
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_14")) // control
        // F5
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_15")) // control
        // F6
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_16")) // control
        // F7
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_17")) // control
        // F8
        controls.add(KeyProperty(frame, null, "keystroke_apply_pattern_18")) // control
        // F9
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("others"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/ChangeNodeLevelAction_left.properties_key"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/ChangeNodeLevelAction_right.properties_key"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/FormatCopy.properties.properties_key"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/FormatPaste.properties.properties_key"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/IconSelectionPlugin.properties.properties_key"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/NewParentNode.properties_key"))
        // controls.add(new KeyProperty(frame, null,
        // "keystroke_accessories/plugins/NodeNote.properties_key"));
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/NodeNote_jumpto.keystroke.alt_N"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/NodeNote_hide_show.keystroke.control_shift_less"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/RemoveNote.properties.properties_key"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/UnfoldAll.keystroke.alt_PAGE_UP"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/UnfoldAll.keystroke.alt_PAGE_DOWN"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/UnfoldAll.keystroke.alt_HOME"))
        controls.add(KeyProperty(frame, null,
                "keystroke_accessories/plugins/UnfoldAll.keystroke.alt_END"))
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("attributes"))
        controls.add(KeyProperty(frame, null, "keystroke_edit_attributes")) // control
        controls.add(KeyProperty(frame, null,
                "keystroke_show_all_attributes")) // control
        controls.add(KeyProperty(frame, null,
                "keystroke_show_selected_attributes")) // control
        controls.add(KeyProperty(frame, null,
                "keystroke_hide_all_attributes")) // control
        controls.add(KeyProperty(frame, null,
                "keystroke_show_attribute_manager")) // control
        controls.add(KeyProperty(frame, null, "keystroke_assign_attributes")) // control
        controls.add(KeyProperty(frame, null,
                "keystroke_plugins/ScriptingEngine.keystroke.evaluate"))
        val modeController = fmMain!!.controller
                .modeController
        if (modeController is MindMapController) {
            val controller = modeController
            val iconActions: Vector<IconAction?> = controller.iconActions
            val actions = Vector<IconInformation?>()
            actions.addAll(iconActions)
            actions.add(controller.removeLastIconAction)
            actions.add(controller.removeAllIconsAction)
            controls.add(NextLineProperty())
            controls.add(SeparatorProperty("icons"))
            for (info in actions) {
                val keyProperty = KeyProperty(frame, null, info!!.keystrokeResourceName)
                keyProperty.setLabelText(info.description)
                keyProperty.setImageIcon(info.icon)
                keyProperty.disableModifiers()
                controls.add(keyProperty)
            }
        }
        /***********************************************************************
         * Misc ****************************************************************
         */
        controls.add(NewTabProperty("Behaviour"))
        controls.add(SeparatorProperty("behaviour"))
        controls.add(BooleanProperty("enable_node_movement.tooltip",
                "enable_node_movement"))
        controls.add(BooleanProperty(FreeMind.RESOURCES_SEARCH_IN_NOTES_TOO
                + TOOLTIP_EXT, FreeMind.RESOURCES_SEARCH_IN_NOTES_TOO))
        controls.add(ComboProperty("placenewbranches.tooltip",
                "placenewbranches", arrayOf("first", "last"), this)) // last
        controls.add(BooleanProperty("draganddrop.tooltip", "draganddrop")) // true
        controls.add(BooleanProperty("unfold_on_paste.tooltip",
                "unfold_on_paste")) // true
        controls.add(BooleanProperty(FreeMind.RESOUCES_PASTE_HTML_STRUCTURE + TOOLTIP_EXT,
                FreeMind.RESOUCES_PASTE_HTML_STRUCTURE)) // true
        controls.add(BooleanProperty("disable_cursor_move_paper.tooltip",
                "disable_cursor_move_paper")) // false
        controls.add(BooleanProperty("enable_leaves_folding.tooltip",
                "enable_leaves_folding")) // false
        controls.add(StringProperty("foldingsymbolwidth.tooltip",
                "foldingsymbolwidth")) // 6
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("key_typing"))
        controls.add(BooleanProperty("disable_key_type.tooltip",
                "disable_key_type")) // false
        controls.add(BooleanProperty("key_type_adds_new.tooltip",
                "key_type_adds_new")) // false
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("resources_notifications"))
        controls.add(RemindValueProperty(
                "remind_type_of_new_nodes.tooltip",
                FreeMind.RESOURCES_REMIND_USE_RICH_TEXT_IN_NEW_LONG_NODES,
                modeController!!))
        controls.add(DontShowNotificationProperty(
                "resources_convert_to_current_version.tooltip",
                FreeMind.RESOURCES_CONVERT_TO_CURRENT_VERSION))
        controls.add(RemindValueProperty(
                "reload_files_without_question.tooltip",
                FreeMind.RESOURCES_RELOAD_FILES_WITHOUT_QUESTION,
                modeController))
        controls.add(DontShowNotificationProperty(
                "delete_nodes_without_question.tooltip",
                FreeMind.RESOURCES_DELETE_NODES_WITHOUT_QUESTION))
        controls.add(DontShowNotificationProperty(
                "cut_nodes_without_question.tooltip",
                FreeMind.RESOURCES_CUT_NODES_WITHOUT_QUESTION))
        controls.add(DontShowNotificationProperty(
                "remove_notes_without_question.tooltip",
                FreeMind.RESOURCES_REMOVE_NOTES_WITHOUT_QUESTION))
        controls.add(RemindValueProperty(
                FreeMind.RESOURCES_COMPLETE_CLONING + ".tooltip",
                FreeMind.RESOURCES_COMPLETE_CLONING, modeController))
        controls.add(DontShowNotificationProperty(
                "execute_scripts_without_asking.tooltip",
                FreeMind.RESOURCES_EXECUTE_SCRIPTS_WITHOUT_ASKING))
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty(FreeMind.RESOURCES_SELECTION_METHOD))
        controls.add(ComboProperty("selection_method.tooltip",
                FreeMind.RESOURCES_SELECTION_METHOD, arrayOf(
                "selection_method_direct", "selection_method_delayed",
                "selection_method_by_click"), this)) // selection_method_direct
        controls.add(NumberProperty("time_for_delayed_selection.tooltip",
                "time_for_delayed_selection", 1, Int.MAX_VALUE, 1)) // 500
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("mouse_wheel"))
        controls.add(NumberProperty("wheel_velocity.tooltip",
                FreeMind.RESOURCES_WHEEL_VELOCITY, 1, 250, 1))
        controls.add(NextLineProperty())
        controls.add(SeparatorProperty("undo"))
        controls.add(NumberProperty("undo_levels.tooltip", "undo_levels",
                2, 1000, 1))
        /***********************************************************************
         * Browser/external apps
         * ****************************************************************
         */
        controls.add(NewTabProperty("HTML"))
        controls.add(SeparatorProperty("browser"))
        //
        // The default browser setting
        //
        // For Windows (the \"\" signs are necessary due to links, that have "="
        // in their URL).
        // default_browser_command_windows_nt = explorer "{0}"
        //
        // The next setting works for the default browser, but eventually starts
        // programs without questions, so be careful!
        //
        // default_browser_command_windows_nt = rundll32
        // url.dll,FileProtocolHandler {0}
        controls.add(StringProperty(
                "default_browser_command_windows_nt.tooltip",
                "default_browser_command_windows_nt")) // cmd.exe
        // /c
        // start
        // ""
        // "{0}"
        controls.add(StringProperty(
                "default_browser_command_windows_9x.tooltip",
                "default_browser_command_windows_9x")) // command.com
        // /c
        // start
        // "{0}"
        // Dimitri proposed:
        // default_browser_command_windows_9x = explorer "{0}"
        //
        // Here the default browser for other operating systems goes:
        //
        controls.add(StringProperty(
                "default_browser_command_other_os.tooltip",
                "default_browser_command_other_os")) // mozilla {0}
        //
        controls.add(StringProperty("default_browser_command_mac.tooltip",
                "default_browser_command_mac")) // open -a
        // /Applications/Safari.app {0}
        controls.add(SeparatorProperty("html_export"))
        //
        controls.add(ComboProperty(
                null,
                "html_export_folding", arrayOf("html_export_no_folding",
                "html_export_fold_currently_folded",
                "html_export_fold_all", "html_export_based_on_headings"),
                this)) // html_export_fold_currently_folded
        controls.add(NextLineProperty())
        controls.add(BooleanProperty("export_icons_in_html.tooltip",
                "export_icons_in_html")) // false
        val iter: Iterator<FreemindPropertyContributor> = sContributors.iterator()
        while (iter.hasNext()) {
            val contributor = iter.next()
            controls.addAll(contributor.getControls(this)!!)
        }
        return controls
    }

    fun closeWindow() {
        val storage = OptionPanelWindowConfigurationStorage()
        storage.panel = selectedPanel
        XmlBindingTools.getInstance().storeDialogPositions(
                fmMain!!.controller, frame, storage,
                PREFERENCE_STORAGE_PROPERTY)
        frame.isVisible = false
        frame.dispose()
    }

    /**
     * @throws IOException
     */
    init {
        if (fmMain == null) {
            fmMain = fm
        }
        this.frame = frame
        this.feedback = feedback
        // Retrieve window size and column positions.
        val storage: WindowConfigurationStorage = XmlBindingTools.getInstance()
                .decorateDialog(fm.controller, frame,
                        PREFERENCE_STORAGE_PROPERTY)
        if (storage != null
                && storage is OptionPanelWindowConfigurationStorage) {
            selectedPanel = storage.panel
        }
        allBindings = Vector()
    }

    //{{{ createBinding() method
    private fun createBinding(name: String, label: String?, shortcut: String?, keyProperty: KeyProperty): GrabKeyDialog.KeyBinding {
        var shortcut = shortcut
        if (shortcut != null && shortcut.isEmpty()) shortcut = null
        val binding = GrabKeyDialog.KeyBinding(name, label, shortcut, false)
        allBindings.add(binding)
        return binding
    } //}}}

    companion object {
        // TODO: Cancel and windowClose => Are you sure, or save.
        // FIXME: key dialog
        // FIXME: Translate me and html
        private const val TOOLTIP_EXT = ".tooltip"
        private val MARKED_BUTTON_COLOR = Color.BLUE
        private var fmMain: FreeMind? = null
        private const val PREFERENCE_STORAGE_PROPERTY = "OptionPanel_Window_Properties"
        private const val DEFAULT_LAYOUT_FORMAT = "right:max(40dlu;p), 4dlu, 120dlu, 7dlu"
        private fun checkConnectionToFreeMindMain() {
            requireNotNull(fmMain) { "FreeMindMain not set yet." }
        }

        private val sContributors: MutableSet<FreemindPropertyContributor> = HashSet()
        fun addContributor(contributor: FreemindPropertyContributor) {
            sContributors.add(contributor)
        }

        fun removeContributor(contributor: FreemindPropertyContributor) {
            sContributors.remove(contributor)
        }
    }
}