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
/* XMLElement.java
 *
 * $Revision: 1.7.18.4.2.8 $
 * $Date: 2009/08/27 20:04:10 $
 * $Name: fm_060405_integration $
 *
 * This file is part of NanoXML 2 Lite.
 * Copyright (C) 2000-2002 Marc De Scheemaecker, All Rights Reserved.
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from the
 * use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 *  1. The origin of this software must not be misrepresented; you must not
 *     claim that you wrote the original software. If you use this software in
 *     a product, an acknowledgment in the product documentation would be
 *     appreciated but is not required.
 *
 *  2. Altered source versions must be plainly marked as such, and must not be
 *     misrepresented as being the original software.
 *
 *  3. This notice may not be removed or altered from any source distribution.
 *****************************************************************************/
/*
 * This version of XMLElement has been *altered* for the purposes of FreeMind
 * toUpperCase(Locale.ENGLISH) for turkish added.
 */
package freemind.main

import freemind.controller.actions.generated.instance.CompoundAction.listChoiceList
import freemind.controller.Controller.init
import freemind.controller.Controller.OptionAntialiasAction.changeAntialias
import freemind.controller.Controller.createNewMode
import freemind.controller.Controller.filterController
import freemind.controller.filter.FilterController.saveConditions
import freemind.controller.Controller.view
import freemind.controller.Controller.obtainFocusForSelected
import freemind.controller.Controller.addTabbedPane
import freemind.controller.Controller.modeController
import freemind.controller.LastStateStorageManagement.lastOpenList
import freemind.controller.actions.generated.instance.MindmapLastStateStorage.restorableName
import freemind.controller.Controller.lastOpenedList
import freemind.controller.LastOpenedList.open
import freemind.controller.LastStateStorageManagement.lastFocussedTab
import freemind.controller.Controller.mapModule
import freemind.controller.Controller.mapModuleManager
import freemind.controller.MapModuleManager.changeToMapModule
import freemind.controller.Controller.errorMessage
import freemind.main.Tools
import java.awt.Color
import java.lang.StringBuffer
import java.awt.GraphicsEnvironment
import java.net.MalformedURLException
import freemind.main.Base64Coding
import java.util.zip.Deflater
import java.util.zip.Inflater
import java.util.zip.DataFormatException
import java.lang.RuntimeException
import freemind.main.Tools.BooleanHolder
import javax.swing.JDialog
import java.awt.Dimension
import java.awt.Insets
import kotlin.Throws
import java.lang.Runnable
import freemind.main.HtmlTools
import java.awt.datatransfer.Transferable
import java.awt.datatransfer.DataFlavor
import java.awt.event.ActionEvent
import javax.swing.JComponent
import javax.swing.KeyStroke
import freemind.main.FreeMindCommon
import javax.swing.SwingUtilities
import javax.swing.AbstractButton
import freemind.main.Tools.ButtonHolder
import freemind.main.Tools.ActionHolder
import freemind.main.Tools.NameMnemonicHolder
import java.net.URISyntaxException
import java.awt.Graphics2D
import java.awt.RenderingHints
import freemind.modes.MindMapNode
import java.net.InetAddress
import freemind.controller.actions.generated.instance.XmlAction
import freemind.common.XmlBindingTools
import java.lang.SecurityException
import java.lang.IllegalAccessException
import java.lang.NoSuchFieldException
import freemind.modes.mindmapmode.MindMapController
import java.awt.datatransfer.Clipboard
import freemind.controller.MindMapNodesSelection
import java.awt.event.ActionListener
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent
import javax.swing.InputMap
import javax.swing.UIManager
import java.awt.print.Paper
import freemind.controller.actions.generated.instance.CompoundAction
import java.lang.reflect.InvocationTargetException
import java.lang.InterruptedException
import freemind.main.FreeMindStarter
import java.net.URLDecoder
import freemind.modes.EdgeAdapter
import freemind.modes.MindIcon
import java.nio.file.attribute.DosFileAttributes
import java.awt.GraphicsDevice
import freemind.main.FreeMind
import javax.crypto.Cipher
import java.security.spec.KeySpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.spec.PBEParameterSpec
import java.security.InvalidAlgorithmParameterException
import java.security.spec.InvalidKeySpecException
import javax.crypto.NoSuchPaddingException
import java.security.NoSuchAlgorithmException
import freemind.main.Tools.DesEncrypter
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import freemind.main.Tools.ReaderCreator
import javax.swing.JFrame
import freemind.main.FreeMindMain
import javax.swing.JLabel
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.JTabbedPane
import javax.swing.ImageIcon
import freemind.main.FreeMindMain.StartupDoneListener
import freemind.main.EditServer
import freemind.main.FreeMindSecurityManager
import freemind.main.FeedBack
import java.awt.BorderLayout
import freemind.preferences.FreemindPropertyListener
import freemind.main.FreeMindMain.VersionInformation
import java.lang.NumberFormatException
import freemind.view.mindmapview.MapView
import java.awt.Desktop
import java.awt.Cursor
import java.util.logging.ConsoleHandler
import java.util.logging.FileHandler
import freemind.main.StdFormatter
import freemind.main.LogFileLogHandler
import java.util.logging.SimpleFormatter
import com.inet.jortho.SpellChecker
import freemind.main.FreeMindStarter.ProxyAuthenticator
import java.net.Socket
import java.awt.event.InputEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import freemind.modes.ModeController
import freemind.controller.LastStateStorageManagement
import freemind.view.MapModule
import freemind.controller.actions.generated.instance.MindmapLastStateStorage
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.JPanel
import freemind.main.IFreeMindSplash
import freemind.main.FreeMindSplashModern
import java.awt.event.WindowFocusListener
import javax.swing.JOptionPane
import freemind.main.XHTMLWriter
import javax.swing.text.BadLocationException
import freemind.main.HtmlTools.IndexPair
import freemind.main.HtmlTools.NodeCreator
import org.jsoup.select.NodeTraversor
import freemind.main.HtmlTools.HtmlNodeVisitor
import org.jsoup.Jsoup
import kotlin.jvm.JvmOverloads
import freemind.common.TextTranslator
import java.text.MessageFormat
import freemind.modes.FreeMindFileDialog
import freemind.modes.FreeMindJFileDialog
import freemind.modes.FreeMindAwtFileDialog
import tests.freemind.FreeMindMainMock
import java.net.ServerSocket
import freemind.main.XMLElement
import java.lang.ClassCastException
import freemind.main.FixedHTMLWriter
import freemind.main.XHTMLWriter.XHTMLFilterWriter
import javax.swing.text.html.HTMLEditorKit
import kotlin.jvm.JvmStatic
import javax.swing.JLayeredPane
import freemind.main.StdFormatter.StdOutErrLevel
import javax.swing.JApplet
import freemind.main.FreeMindApplet
import freemind.main.FreeMindCommon.FreeMindResourceBundle
import java.net.URLClassLoader
import javax.swing.text.html.HTMLWriter
import javax.swing.text.MutableAttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.html.HTML
import javax.swing.text.StyleConstants
import javax.swing.text.html.CSS
import java.net.PasswordAuthentication
import freemind.main.LogFileLogHandler.LogReceiver
import freemind.main.FreeMindSplashModern.FeedBackImpl
import javax.swing.JProgressBar
import freemind.view.ImageFactory
import javax.swing.JRootPane
import java.awt.Graphics
import java.awt.Rectangle
import java.io.*
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

/**
 * XMLElement is a representation of an XML object. The object is able to parse
 * XML code.
 * <P>
</P> * <DL>
 * <DT><B>Parsing XML Data</B></DT>
 * <DD>
 * You can parse XML data using the following code:
 * <UL>
 * <CODE>
 * XMLElement xml = new XMLElement();<BR></BR>
 * FileReader reader = new FileReader("filename.xml");<BR></BR>
 * xml.parseFromReader(reader);
</CODE> *
</UL> *
</DD> *
</DL> *
 * <DL>
 * <DT><B>Retrieving Attributes</B></DT>
 * <DD>
 * You can enumerate the attributes of an element using the method
 * [enumerateAttributeNames][.enumerateAttributeNames]. The attribute
 * values can be retrieved using the method
 * [getStringAttribute][.getStringAttribute]. The
 * following example shows how to list the attributes of an element:
 * <UL>
 * <CODE>
 * XMLElement element = ...;<BR></BR>
 * Enumeration enumerator = element.getAttributeNames();<BR></BR>
 * while (enumerator.hasMoreElements()) {<BR></BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;String key = (String) enumerator.nextElement();<BR></BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;String value = element.getStringAttribute(key);<BR></BR>
 * &nbsp;&nbsp;&nbsp;&nbsp;System.out.println(key + " = " + value);<BR></BR>
 * }
</CODE> *
</UL> *
</DD> *
</DL> *
 * <DL>
 * <DT><B>Retrieving Child Elements</B></DT>
 * <DD>
 * You can enumerate the children of an element using
 * [enumerateChildren][.enumerateChildren]. The number of child elements
 * can be retrieved using [countChildren][.countChildren].</DD>
</DL> *
 * <DL>
 * <DT><B>Elements Containing Character Data</B></DT>
 * <DD>
 * If an elements contains character data, like in the following example:
 * <UL>
 * <CODE>
 * &lt;title&gt;The Title&lt;/title&gt;
</CODE> *
</UL> *
 * you can retrieve that data using the method [getContent][.getContent].
</DD> *
</DL> *
 * <DL>
 * <DT><B>Subclassing XMLElement</B></DT>
 * <DD>
 * When subclassing XMLElement, you need to override the method
 * [createAnotherElement][.createAnotherElement] which has to return a
 * new copy of the receiver.</DD>
</DL> *
 * <P>
 *
 *
 *
 * @author Marc De Scheemaecker &lt;<A href="mailto:cyberelf@mac.com">cyberelf@mac.com</A>&gt;
 * @version $Name: fm_060405_integration $, $Revision: 1.7.18.4.2.8 $
</P> */
open class XMLElement protected constructor(entities: Hashtable<String?, CharArray?>,
                                            /**
                                             * `true` if the leading and trailing whitespace of #PCDATA
                                             * sections have to be ignored.
                                             */
                                            private val ignoreWhitespace: Boolean,
                                            fillBasicConversionTable: Boolean,
                                            /**
                                             * `true` if the case of the element and attribute names are case
                                             * insensitive.
                                             */
                                            protected var ignoreCase: Boolean) {
    /**
     * The attributes given to the element.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * The field can be empty.
     *  * The field is never `null`.
     *  * The keys and the values are strings.
     *
    </dd> *
    </dl> *
     */
    private val attributes: TreeMap<String, String>

    /**
     * Child elements of the element.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * The field can be empty.
     *  * The field is never `null`.
     *  * The elements are instances of `XMLElement` or a subclass
     * of `XMLElement`.
     *
    </dd> *
    </dl> *
     */
    private val children: Vector<XMLElement>
    /**
     * Returns the name of the element.
     *
     * @see freemind.main.XMLElement.setName
     */
    /**
     * Changes the name of the element.
     *
     * The new name.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     *
     * @see freemind.main.XMLElement.getName
     */
    /**
     * The name of the element.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * The field is `null` iff the element is not initialized by
     * either parse or setName.
     *  * If the field is not `null`, it's not empty.
     *  * If the field is not `null`, it contains a valid XML
     * identifier.
     *
    </dd> *
    </dl> *
     */
    open var name: String? = null

    /**
     * The #PCDATA content of the object.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * The field is `null` iff the element is not a #PCDATA
     * element.
     *  * The field can be any string, including the empty string.
     *
    </dd> *
    </dl> *
     */
    private var contents: String? = ""

    /**
     * fc, 17.5.06: enable buld xml writing.
     */
    private var dontEncodeContents = false

    /**
     * Conversion table for &amp;...; entities. The keys are the entity names
     * without the &amp; and ; delimiters.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * The field is never `null`.
     *  * The field always contains the following associations:
     * "lt"&nbsp;=&gt;&nbsp;"&lt;", "gt"&nbsp;=&gt;&nbsp;"&gt;",
     * "quot"&nbsp;=&gt;&nbsp;"\"", "apos"&nbsp;=&gt;&nbsp;"'",
     * "amp"&nbsp;=&gt;&nbsp;"&amp;"
     *  * The keys are strings
     *  * The values are char arrays
     *
    </dd> *
    </dl> *
     */
    private val entities: Hashtable<String?, CharArray?>
    /**
     * Returns the line nr in the source data on which the element is found.
     * This method returns `0` there is no associated source data.
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * `result >= 0`
     *
    </dd> *
    </dl> *
     */
    /**
     * The line number where the element starts.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * `lineNr &gt= 0`
     *
    </dd> *
    </dl> *
     */
    val lineNr: Int

    /**
     * Character read too much. This character provides push-back functionality
     * to the input reader without having to use a PushbackReader. If there is
     * no such character, this field is '\0'.
     */
    private var charReadTooMuch = 0.toChar()

    /**
     * The reader provided by the caller of the parse method.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * The field is not `null` while the parse method is running.
     *
    </dd> *
    </dl> *
     */
    private var reader: Reader? = null

    /**
     * The current line number in the source content.
     *
     * <dl>
     * <dt>**Invariants:**</dt>
     * <dd>
     *
     *  * parserLineNr &gt; 0 while the parse method is running.
     *
    </dd> *
    </dl> *
     */
    private var parserLineNr = 0

    /**
     * Creates and initializes a new XML element. Calling the construction is
     * equivalent to:
     *
     * `new XMLElement(new Hashtable(), false, true)
    ` *
     *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => 0
     *  * enumerateChildren() => empty enumeration
     *  * enumeratePropertyNames() => empty enumeration
     *  * getChildren() => empty vector
     *  * getContent() => ""
     *  * getLineNr() => 0
     *  * getName() => null
     *
    </dd> *
    </dl> *
     *
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     */
    constructor() : this(Hashtable<String?, CharArray?>(), false, true, true) {}

    /**
     * Creates and initializes a new XML element. Calling the construction is
     * equivalent to:
     *
     * `new XMLElement(entities, false, true)
    ` *
     *
     *
     * The entity conversion table.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `entities != null`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => 0
     *  * enumerateChildren() => empty enumeration
     *  * enumeratePropertyNames() => empty enumeration
     *  * getChildren() => empty vector
     *  * getContent() => ""
     *  * getLineNr() => 0
     *  * getName() => null
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
    </dl> */
    constructor(entities: Hashtable<String?, CharArray?>) : this(entities, false, true, true) {}

    /**
     * Creates and initializes a new XML element. Calling the construction is
     * equivalent to:
     *
     * `new XMLElement(new Hashtable(), skipLeadingWhitespace, true)
    ` *
     *
     *
     * `true` if leading and trailing whitespace in PCDATA content
     * has to be removed.
     *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => 0
     *  * enumerateChildren() => empty enumeration
     *  * enumeratePropertyNames() => empty enumeration
     *  * getChildren() => empty vector
     *  * getContent() => ""
     *  * getLineNr() => 0
     *  * getName() => null
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
    </dl> */
    constructor(skipLeadingWhitespace: Boolean) : this(Hashtable<String?, CharArray?>(), skipLeadingWhitespace, true, true) {}

    /**
     * Creates and initializes a new XML element. Calling the construction is
     * equivalent to:
     *
     * `new XMLElement(entities, skipLeadingWhitespace, true)
    ` *
     *
     *
     * The entity conversion table. `true` if leading and trailing
     * whitespace in PCDATA content has to be removed.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `entities != null`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => 0
     *  * enumerateChildren() => empty enumeration
     *  * enumeratePropertyNames() => empty enumeration
     *  * getChildren() => empty vector
     *  * getContent() => ""
     *  * getLineNr() => 0
     *  * getName() => null
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
    </dl> */
    constructor(entities: Hashtable<String?, CharArray?>, skipLeadingWhitespace: Boolean) : this(entities, skipLeadingWhitespace, true, true) {}

    /**
     * Creates and initializes a new XML element.
     *
     * The entity conversion table. `true` if leading and trailing
     * whitespace in PCDATA content has to be removed. `true` if the
     * case of element and attribute names have to be ignored.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `entities != null`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => 0
     *  * enumerateChildren() => empty enumeration
     *  * enumeratePropertyNames() => empty enumeration
     *  * getChildren() => empty vector
     *  * getContent() => ""
     *  * getLineNr() => 0
     *  * getName() => null
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
     * @see freemind.main.XMLElement.XMLElement
    </dl> */
    constructor(entities: Hashtable<String?, CharArray?>, skipLeadingWhitespace: Boolean,
                ignoreCase: Boolean) : this(entities, skipLeadingWhitespace, true, ignoreCase) {
    }

    /**
     * Creates and initializes a new XML element.
     * <P>
     * This constructor should <I>only</I> be called from
     * [createAnotherElement][.createAnotherElement] to create child
     * elements.
     *
     * The entity conversion table. `true` if leading and trailing
     * whitespace in PCDATA content has to be removed. `true` if the
     * basic entities need to be added to the entity list. `true` if
     * the case of element and attribute names have to be ignored.
     *
     *
    </P> * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `entities != null`
     *  * if `fillBasicConversionTable == false` then
     * `entities` contains at least the following entries:
     * `amp`, `lt`, `gt`, `apos` and
     * `quot`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => 0
     *  * enumerateChildren() => empty enumeration
     *  * enumeratePropertyNames() => empty enumeration
     *  * getChildren() => empty vector
     *  * getContent() => ""
     *  * getLineNr() => 0
     *  * getName() => null
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.createAnotherElement
    </dl> */
    init {
        attributes = TreeMap()
        children = Vector()
        this.entities = entities
        lineNr = 0
        val enumerator = this.entities.keys()
        while (enumerator.hasMoreElements()) {
            val key = enumerator.nextElement()
            val value: Any? = this.entities[key]
            if (value is String) {
                this.entities[key] = value.toCharArray()
            }
        }
        if (fillBasicConversionTable) {
            this.entities["amp"] = charArrayOf('&')
            this.entities["quot"] = charArrayOf('"')
            this.entities["apos"] = charArrayOf('\'')
            this.entities["lt"] = charArrayOf('<')
            this.entities["gt"] = charArrayOf('>')
        }
    }

    // We may expect that some subclass would like to have its own object
    open val userObject: Any?
        get() = null

    /**
     * Adds a child element.
     *
     * The child element to add.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `child != null`
     *  * `child.getName() != null`
     *  * `child` does not have a parent element
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => old.countChildren() + 1
     *  * enumerateChildren() => old.enumerateChildren() + child
     *  * getChildren() => old.enumerateChildren() + child
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.countChildren
     * @see freemind.main.XMLElement.enumerateChildren
     * @see freemind.main.XMLElement.getChildren
     * @see freemind.main.XMLElement.removeChild
    </dl> */
    open fun addChild(child: XMLElement) {
        children.addElement(child)
    }

    /**
     * Adds or modifies an attribute.
     *
     * The name of the attribute. The value of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *  * `value != null`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * enumerateAttributeNames() => old.enumerateAttributeNames() + name
     *  * getAttribute(name) => value
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
    </dl> */
    open fun setAttribute(name: String, value: Any) {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        attributes[name] = value.toString()
    }

    /**
     * Adds or modifies an attribute.
     *
     * The name of the attribute. The value of the attribute.
     *
     */
    @Deprecated("""Use {@link #setAttribute(java.lang.String, java.lang.Object)
	 *             setAttribute} instead.""")
    fun addProperty(name: String, value: Any) {
        setAttribute(name, value)
    }

    /**
     * Adds or modifies an attribute.
     *
     * The name of the attribute. The value of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * enumerateAttributeNames() => old.enumerateAttributeNames() + name
     *  * getIntAttribute(name) => value
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
    </dl> */
    fun setIntAttribute(name: String, value: Int) {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        attributes[name] = Integer.toString(value)
    }

    /**
     * Adds or modifies an attribute.
     *
     * The name of the attribute. The value of the attribute.
     *
     */
    @Deprecated("""Use {@link #setIntAttribute(java.lang.String, int)
	 *             setIntAttribute} instead.""")
    fun addProperty(key: String, value: Int) {
        setIntAttribute(key, value)
    }

    /**
     * Adds or modifies an attribute.
     *
     * The name of the attribute. The value of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * enumerateAttributeNames() => old.enumerateAttributeNames() + name
     *  * getDoubleAttribute(name) => value
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
    </dl> */
    fun setDoubleAttribute(name: String, value: Double) {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        attributes[name] = java.lang.Double.toString(value)
    }

    /**
     * Adds or modifies an attribute.
     *
     * The name of the attribute. The value of the attribute.
     *
     */
    @Deprecated("""Use {@link #setDoubleAttribute(java.lang.String, double)
	 *             setDoubleAttribute} instead.""")
    fun addProperty(name: String, value: Double) {
        setDoubleAttribute(name, value)
    }

    /**
     * Returns the number of child elements of the element.
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * `result >= 0`
     *
    </dd> *
    </dl> *
     *
     * @see freemind.main.XMLElement.addChild
     * @see freemind.main.XMLElement.enumerateChildren
     * @see freemind.main.XMLElement.getChildren
     * @see freemind.main.XMLElement.removeChild
     */
    fun countChildren(): Int {
        return children.size
    }

    /**
     * Enumerates the attribute names.
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * `result != null`
     *
    </dd> *
    </dl> *
     *
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getBooleanAttribute
     */
    fun enumerateAttributeNames(): Iterator<String> {
        return attributes.keys.iterator()
    }

    /**
     * Enumerates the child elements.
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * `result != null`
     *
    </dd> *
    </dl> *
     *
     * @see freemind.main.XMLElement.addChild
     * @see freemind.main.XMLElement.countChildren
     * @see freemind.main.XMLElement.getChildren
     * @see freemind.main.XMLElement.removeChild
     */
    fun enumerateChildren(): Enumeration<*> {
        return children.elements()
    }

    /**
     * Returns the child elements as a Vector. It is safe to modify this Vector.
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * `result != null`
     *
    </dd> *
    </dl> *
     *
     * @see freemind.main.XMLElement.addChild
     * @see freemind.main.XMLElement.countChildren
     * @see freemind.main.XMLElement.enumerateChildren
     * @see freemind.main.XMLElement.removeChild
     */
    fun getChildren(): Vector<XMLElement>? {
        return try {
            children.clone() as Vector<XMLElement>
        } catch (e: Exception) {
            // this never happens, however, some Java compilers are so
            // braindead that they require this exception clause
            null
        }
    }

    /**
     * Returns the PCDATA content of the object. If there is no such content,
     * <CODE>null</CODE> is returned.
     *
     */
    @Deprecated("Use {@link #getContent() getContent} instead.")
    fun getContents(): String? {
        return content
    }
    /**
     * Returns the PCDATA content of the object. If there is no such content,
     * <CODE>null</CODE> is returned.
     *
     * @see freemind.main.XMLElement.setContent
     */
    /**
     * Changes the content string.
     *
     * The new content string.
     */
    var content: String?
        get() = contents
        set(content) {
            contents = content
            dontEncodeContents = false
        }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `null` is returned.
     *
     * @param name
     * The name of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
    </dl> */
    fun getAttribute(name: String): Any? {
        return this.getAttribute(name, null)
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `defaultValue` is returned.
     *
     * @param name
     * The name of the attribute.
     * @param defaultValue
     * Key to use if the attribute is missing.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
    </dl> */
    fun getAttribute(name: String, defaultValue: Any?): Any? {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        var value: Any? = attributes[name]
        if (value == null) {
            value = defaultValue
        }
        return value
    }

    /**
     * Returns an attribute by looking up a key in a hashtable. If the attribute
     * doesn't exist, the value corresponding to defaultKey is returned.
     * <P>
     * As an example, if valueSet contains the mapping `"one" =>
     * "1"` and the element contains the attribute `attr="one"`
     * , then `getAttribute("attr", mapping, defaultKey, false)`
     * returns `"1"`.
     *
     * The name of the attribute. Hashtable mapping keys to values. Key to use
     * if the attribute is missing. `true` if literals are valid.
     *
     *
    </P> * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *  * `valueSet` != null
     *  * the keys of `valueSet` are strings
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
    </dl> */
    fun getAttribute(name: String, valueSet: Hashtable<*, *>,
                     defaultKey: String?, allowLiterals: Boolean): Any? {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        var key: Any? = attributes[name]
        var result: Any?
        if (key == null) {
            key = defaultKey
        }
        result = valueSet[key]
        if (result == null) {
            result = if (allowLiterals) {
                key
            } else {
                throw invalidValue(name, key as String?)
            }
        }
        return result
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `null` is returned.
     *
     * @param name
     * The name of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
    </dl> */
    fun getStringAttribute(name: String): String? {
        return this.getStringAttribute(name, null)
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `defaultValue` is returned.
     *
     * @param name
     * The name of the attribute.
     * @param defaultValue
     * Key to use if the attribute is missing.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
    </dl> */
    fun getStringAttribute(name: String, defaultValue: String?): String? {
        return this.getAttribute(name, defaultValue) as String?
    }

    /**
     * Returns an attribute by looking up a key in a hashtable. If the attribute
     * doesn't exist, the value corresponding to defaultKey is returned.
     * <P>
     * As an example, if valueSet contains the mapping `"one" =>
     * "1"` and the element contains the attribute `attr="one"`
     * , then `getAttribute("attr", mapping, defaultKey, false)`
     * returns `"1"`.
     *
     * The name of the attribute. Hashtable mapping keys to values. Key to use
     * if the attribute is missing. `true` if literals are valid.
     *
     *
    </P> * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *  * `valueSet` != null
     *  * the keys of `valueSet` are strings
     *  * the values of `valueSet` are strings
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
    </dl> */
    fun getStringAttribute(name: String, valueSet: Hashtable<Any?, String?>,
                           defaultKey: String?, allowLiterals: Boolean): String? {
        return this.getAttribute(name, valueSet, defaultKey,
                allowLiterals) as String?
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `0` is returned.
     *
     * @param name
     * The name of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
    </dl> */
    fun getIntAttribute(name: String): Int {
        return this.getIntAttribute(name, 0)
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `defaultValue` is returned.
     *
     * @param name
     * The name of the attribute.
     * @param defaultValue
     * Key to use if the attribute is missing.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
    </dl> */
    fun getIntAttribute(name: String, defaultValue: Int): Int {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        val value = attributes[name]
        return if (value == null) {
            defaultValue
        } else {
            try {
                value.toInt()
            } catch (e: NumberFormatException) {
                throw invalidValue(name, value)
            }
        }
    }

    /**
     * Returns an attribute by looking up a key in a hashtable. If the attribute
     * doesn't exist, the value corresponding to defaultKey is returned.
     * <P>
     * As an example, if valueSet contains the mapping `"one" => 1`
     * and the element contains the attribute `attr="one"`, then
     * `getIntAttribute("attr", mapping, defaultKey, false)` returns
     * `1`.
     *
     * The name of the attribute. Hashtable mapping keys to values. Key to use
     * if the attribute is missing. `true` if literal numbers are
     * valid.
     *
     *
    </P> * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *  * `valueSet` != null
     *  * the keys of `valueSet` are strings
     *  * the values of `valueSet` are Integer objects
     *  * `defaultKey` is either `null`, a key in
     * `valueSet` or an integer.
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
    </dl> */
    fun getIntAttribute(name: String, valueSet: Hashtable<Any?, Int?>,
                        defaultKey: String?, allowLiteralNumbers: Boolean): Int {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        var key: Any? = attributes[name]
        var result: Int?
        if (key == null) {
            key = defaultKey
        }
        result = try {
            valueSet[key]
        } catch (e: ClassCastException) {
            throw invalidValueSet(name)
        }
        if (result == null) {
            if (!allowLiteralNumbers) {
                throw invalidValue(name, key as String?)
            }
            result = try {
                Integer.valueOf(key as String?)
            } catch (e: NumberFormatException) {
                throw invalidValue(name, key as String?)
            }
        }
        return result!!.toInt()
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `0.0` is returned.
     *
     * @param name
     * The name of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
    </dl> */
    fun getDoubleAttribute(name: String): Double {
        return this.getDoubleAttribute(name, 0.0)
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `defaultValue` is returned.
     *
     * @param name
     * The name of the attribute.
     * @param defaultValue
     * Key to use if the attribute is missing.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
    </dl> */
    fun getDoubleAttribute(name: String, defaultValue: Double): Double {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        val value = attributes[name]
        return if (value == null) {
            defaultValue
        } else {
            try {
                java.lang.Double.valueOf(value).toDouble()
            } catch (e: NumberFormatException) {
                throw invalidValue(name, value)
            }
        }
    }

    /**
     * Returns an attribute by looking up a key in a hashtable. If the attribute
     * doesn't exist, the value corresponding to defaultKey is returned.
     * <P>
     * As an example, if valueSet contains the mapping `"one" =>
     * 1.0` and the element contains the attribute `attr="one"`
     * , then
     * `getDoubleAttribute("attr", mapping, defaultKey, false)`
     * returns `1.0`.
     *
     * The name of the attribute. Hashtable mapping keys to values. Key to use
     * if the attribute is missing. `true` if literal numbers are
     * valid.
     *
     *
    </P> * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *  * `valueSet != null`
     *  * the keys of `valueSet` are strings
     *  * the values of `valueSet` are Double objects
     *  * `defaultKey` is either `null`, a key in
     * `valueSet` or a double.
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
    </dl> */
    fun getDoubleAttribute(name: String, valueSet: Hashtable<Any?, Double?>,
                           defaultKey: String?, allowLiteralNumbers: Boolean): Double {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        var key: Any? = attributes[name]
        var result: Double?
        if (key == null) {
            key = defaultKey
        }
        result = try {
            valueSet[key]
        } catch (e: ClassCastException) {
            throw invalidValueSet(name)
        }
        if (result == null) {
            if (!allowLiteralNumbers) {
                throw invalidValue(name, key as String?)
            }
            result = try {
                java.lang.Double.valueOf(key as String?)
            } catch (e: NumberFormatException) {
                throw invalidValue(name, key as String?)
            }
        }
        return result!!.toDouble()
    }

    /**
     * Returns an attribute of the element. If the attribute doesn't exist,
     * `defaultValue` is returned. If the value of the attribute is
     * equal to `trueValue`, `true` is returned. If the
     * value of the attribute is equal to `falseValue`,
     * `false` is returned. If the value doesn't match
     * `trueValue` or `falseValue`, an exception is
     * thrown.
     *
     * @param name
     * The name of the attribute.
     * @param trueValue
     * The value associated with `true`.
     * @param falseValue
     * The value associated with `true`.
     * @param defaultValue
     * Value to use if the attribute is missing.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *  * `trueValue` and `falseValue` are
     * different strings.
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.removeAttribute
     * @see freemind.main.XMLElement.enumerateAttributeNames
    </dl> */
    fun getBooleanAttribute(name: String, trueValue: String,
                            falseValue: String, defaultValue: Boolean): Boolean {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        val value: Any? = attributes[name]
        return if (value == null) {
            defaultValue
        } else if (value == trueValue) {
            true
        } else if (value == falseValue) {
            false
        } else {
            throw invalidValue(name, value as String?)
        }
    }
    /**
     * Reads one XML element from a java.io.Reader and parses it.
     *
     * The reader from which to retrieve the XML data. The line number of the
     * first line in the data.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `reader != null`
     *  * `reader` is not closed
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *  * the reader points to the first character following the last '&gt;'
     * character of the XML element
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws java.io.IOException
     * If an error occured while reading the input.
     * @throws XMLParseException
     * If an error occured while parsing the read data.
    </dl> */
    /**
     * Reads one XML element from a java.io.Reader and parses it.
     *
     * The reader from which to retrieve the XML data.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `reader != null`
     *  * `reader` is not closed
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *  * the reader points to the first character following the last '&gt;'
     * character of the XML element
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws java.io.IOException
     * If an error occured while reading the input.
     * @throws XMLParseException
     * If an error occured while parsing the read data.
    </dl> */
    @JvmOverloads
    @Throws(IOException::class, XMLParseException::class)
    fun parseFromReader(reader: Reader?, startingLineNr: Int =  /* startingLineNr */1) {
        charReadTooMuch = '\u0000'
        this.reader = reader
        parserLineNr = startingLineNr
        while (true) {
            var ch = this.scanWhitespace()
            if (ch != '<') {
                throw expectedInput("<")
            }
            ch = readChar()
            if (ch == '!' || ch == '?') {
                skipSpecialTag(0)
            } else {
                unreadChar(ch)
                scanElement(this)
                return
            }
        }
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * The reader from which to retrieve the XML data.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `string != null`
     *  * `string.length() > 0`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws XMLParseException
     * If an error occured while parsing the string.
    </dl> */
    @Throws(XMLParseException::class)
    fun parseString(string: String?) {
        try {
            parseFromReader(StringReader(string),  /* startingLineNr */
                    1)
        } catch (e: IOException) {
            // Java exception handling suxx
        }
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * The reader from which to retrieve the XML data. The first character in
     * `string` to scan.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `string != null`
     *  * `offset < string.length()`
     *  * `offset >= 0`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws XMLParseException
     * If an error occured while parsing the string.
    </dl> */
    @Throws(XMLParseException::class)
    fun parseString(string: String, offset: Int) {
        this.parseString(string.substring(offset))
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * The reader from which to retrieve the XML data. The first character in
     * `string` to scan. The character where to stop scanning. This
     * character is not scanned.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `string != null`
     *  * `end <= string.length()`
     *  * `offset < end`
     *  * `offset >= 0`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws XMLParseException
     * If an error occured while parsing the string.
    </dl> */
    @Throws(XMLParseException::class)
    fun parseString(string: String, offset: Int, end: Int) {
        this.parseString(string.substring(offset, end))
    }

    /**
     * Reads one XML element from a String and parses it.
     *
     * The reader from which to retrieve the XML data. The first character in
     * `string` to scan. The character where to stop scanning. This
     * character is not scanned. The line number of the first line in the data.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `string != null`
     *  * `end <= string.length()`
     *  * `offset < end`
     *  * `offset >= 0`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws XMLParseException
     * If an error occured while parsing the string.
    </dl> */
    @Throws(XMLParseException::class)
    fun parseString(string: String, offset: Int, end: Int,
                    startingLineNr: Int) {
        var string = string
        string = string.substring(offset, end)
        try {
            parseFromReader(StringReader(string), startingLineNr)
        } catch (e: IOException) {
            // Java exception handling suxx
        }
    }
    /**
     * Reads one XML element from a char array and parses it.
     *
     * The reader from which to retrieve the XML data. The first character in
     * `string` to scan. The character where to stop scanning. This
     * character is not scanned. The line number of the first line in the data.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `input != null`
     *  * `end <= input.length`
     *  * `offset < end`
     *  * `offset >= 0`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws XMLParseException
     * If an error occured while parsing the string.
    </dl> */
    /**
     * Reads one XML element from a char array and parses it.
     *
     * The reader from which to retrieve the XML data. The first character in
     * `string` to scan. The character where to stop scanning. This
     * character is not scanned.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `input != null`
     *  * `end <= input.length`
     *  * `offset < end`
     *  * `offset >= 0`
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * the state of the receiver is updated to reflect the XML element
     * parsed from the reader
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @throws XMLParseException
     * If an error occured while parsing the string.
    </dl> */
    @JvmOverloads
    @Throws(XMLParseException::class)
    fun parseCharArray(input: CharArray?, offset: Int, end: Int,
                       startingLineNr: Int =  /* startingLineNr */1) {
        try {
            val reader: Reader = CharArrayReader(input, offset, end)
            parseFromReader(reader, startingLineNr)
        } catch (e: IOException) {
            // This exception will never happen.
        }
    }

    /**
     * Removes a child element.
     *
     * The child element to remove.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `child != null`
     *  * `child` is a child element of the receiver
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * countChildren() => old.countChildren() - 1
     *  * enumerateChildren() => old.enumerateChildren() - child
     *  * getChildren() => old.enumerateChildren() - child
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.addChild
     * @see freemind.main.XMLElement.countChildren
     * @see freemind.main.XMLElement.enumerateChildren
     * @see freemind.main.XMLElement.getChildren
    </dl> */
    fun removeChild(child: XMLElement?) {
        children.removeElement(child)
    }

    /**
     * Removes an attribute.
     *
     * The name of the attribute.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name` is a valid XML identifier
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * enumerateAttributeNames() => old.enumerateAttributeNames() - name
     *  * getAttribute(name) => `null`
     *
    </dd> *
    </dl> *
     * <dl>
     *
     * @see freemind.main.XMLElement.enumerateAttributeNames
     * @see freemind.main.XMLElement.setDoubleAttribute
     * @see freemind.main.XMLElement.setIntAttribute
     * @see freemind.main.XMLElement.setAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getStringAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getIntAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getDoubleAttribute
     * @see freemind.main.XMLElement.getBooleanAttribute
    </dl> */
    fun removeAttribute(name: String) {
        var name = name
        if (ignoreCase) {
            name = name.uppercase()
        }
        attributes.remove(name)
    }

    /**
     * Removes an attribute.
     *
     * The name of the attribute.
     *
     */
    @Deprecated("""Use {@link #removeAttribute(java.lang.String)
	 *             removeAttribute} instead.""")
    fun removeProperty(name: String) {
        removeAttribute(name)
    }

    /**
     * Removes an attribute.
     *
     * The name of the attribute.
     *
     */
    @Deprecated("""Use {@link #removeAttribute(java.lang.String)
	 *             removeAttribute} instead.""")
    fun removeChild(name: String) {
        removeAttribute(name)
    }

    /**
     * Creates a new similar XML element.
     * <P>
     * You should override this method when subclassing XMLElement.
    </P> */
    protected open fun createAnotherElement(): XMLElement {
        return XMLElement(entities, ignoreWhitespace, false,
                ignoreCase)
    }

    // You should override this method when subclassing XMLElement.
    protected open fun completeElement() {}

    /**
     * Changes the content string.
     *
     * The new content string.
     */
    fun setEncodedContent(content: String?) {
        contents = content
        dontEncodeContents = true
    }

    /**
     * Changes the name of the element.
     *
     * The new name.
     *
     */
    @Deprecated("Use {@link #setName(java.lang.String) setName} instead.")
    fun setTagName(name: String?) {
        this.name = name
    }

    /**
     * Writes the XML element to a string.
     *
     * @see freemind.main.XMLElement.write
     */
    override fun toString(): String {
        return try {
            val out = ByteArrayOutputStream()
            val writer = OutputStreamWriter(out)
            write(writer)
            writer.flush()
            String(out.toByteArray())
        } catch (e: IOException) {
            // Java exception handling suxx
            super.toString()
        }
    }

    @Throws(IOException::class)
    fun writeWithoutClosingTag(writer: Writer) {
        write(writer, false)
    }

    @Throws(IOException::class)
    fun writeClosingTag(writer: Writer) {
        writer.write('<'.code)
        writer.write('/'.code)
        writer.write(name)
        writer.write('>'.code)
        writer.write('\n'.code)
    }

    /**
     * Writes the XML element to a writer.
     *
     * The writer to write the XML data to.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `writer != null`
     *  * `writer` is not closed
     *
    </dd> *
    </dl> *
     *
     * @throws java.io.IOException
     * If the data could not be written to the writer.
     *
     * @see freemind.main.XMLElement.toString
     */
    @JvmOverloads
    @Throws(IOException::class)
    fun write(writer: Writer, withClosingTag: Boolean = true) {
        if (name == null) {
            // fc, 17.5.06: support encoded contents
            if (dontEncodeContents) {
                writer.write(contents)
            } else {
                writeEncoded(writer, contents)
            }
            return
        }
        writer.write('<'.code)
        writer.write(name)
        if (!attributes.isEmpty()) {
            for (key in attributes.keys) {
                writer.write(' '.code)
                writer.write(key)
                writer.write('='.code)
                writer.write('"'.code)
                writeEncoded(writer, attributes[key])
                writer.write('"'.code)
            }
        }
        if (contents != null && contents!!.length > 0) {
            writer.write('>'.code)
            // writer.write('\n');
            // fc, 17.5.06: support encoded contents
            if (dontEncodeContents) {
                writer.write(contents)
            } else {
                writeEncoded(writer, contents)
            }
            if (withClosingTag) {
                writer.write('<'.code)
                writer.write('/'.code)
                writer.write(name)
                writer.write('>'.code)
                writer.write('\n'.code)
            }
        } else if (children.isEmpty()) {
            if (withClosingTag) {
                writer.write('/'.code)
            }
            writer.write('>'.code)
            writer.write('\n'.code)
        } else {
            writer.write('>'.code)
            writer.write('\n'.code)
            val enumerator = enumerateChildren()
            while (enumerator.hasMoreElements()) {
                val child = enumerator.nextElement() as XMLElement
                child.write(writer)
            }
            if (withClosingTag) {
                writer.write('<'.code)
                writer.write('/'.code)
                writer.write(name)
                writer.write('>'.code)
                writer.write('\n'.code)
            }
        }
    }

    /**
     * Writes a string encoded to a writer.
     *
     * The writer to write the XML data to. The string to write encoded.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `writer != null`
     *  * `writer` is not closed
     *  * `str != null`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun writeEncoded(writer: Writer, str: String?) {
        var i = 0
        while (i < str!!.length) {
            val ch = str[i]
            when (ch) {
                '<' -> {
                    writer.write('&'.code)
                    writer.write('l'.code)
                    writer.write('t'.code)
                    writer.write(';'.code)
                }
                '>' -> {
                    writer.write('&'.code)
                    writer.write('g'.code)
                    writer.write('t'.code)
                    writer.write(';'.code)
                }
                '&' -> {
                    writer.write('&'.code)
                    writer.write('a'.code)
                    writer.write('m'.code)
                    writer.write('p'.code)
                    writer.write(';'.code)
                }
                '"' -> {
                    writer.write('&'.code)
                    writer.write('q'.code)
                    writer.write('u'.code)
                    writer.write('o'.code)
                    writer.write('t'.code)
                    writer.write(';'.code)
                }
                '\'' -> {
                    writer.write('&'.code)
                    writer.write('a'.code)
                    writer.write('p'.code)
                    writer.write('o'.code)
                    writer.write('s'.code)
                    writer.write(';'.code)
                }
                else -> {
                    val unicode = ch.code
                    if (unicode < 32 || unicode > 126) {
                        writer.write('&'.code)
                        writer.write('#'.code)
                        writer.write('x'.code)
                        writer.write(Integer.toString(unicode, 16))
                        writer.write(';'.code)
                    } else {
                        writer.write(ch.code)
                    }
                }
            }
            i += 1
        }
    }

    /**
     * Scans an identifier from the current reader. The scanned identifier is
     * appended to `result`.
     *
     * The buffer in which the scanned identifier will be put.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `result != null`
     *  * The next character read from the reader is a valid first character of
     * an XML identifier.
     *
    </dd> *
    </dl> *
     *
     * <dl>
     * <dt>**Postconditions:**</dt>
     * <dd>
     *
     *  * The next character read from the reader won't be an identifier
     * character.
     *
    </dd> *
    </dl> *
     * <dl>
    </dl> */
    @Throws(IOException::class)
    protected fun scanIdentifier(result: StringBuffer) {
        while (true) {
            val ch = readChar()
            if ((ch < 'A' || ch > 'Z') && (ch < 'a' || ch > 'z')
                    && (ch < '0' || ch > '9') && ch != '_' && ch != '.'
                    && ch != ':' && ch != '-' && ch <= '\u007E') {
                unreadChar(ch)
                return
            }
            result.append(ch)
        }
    }

    /**
     * This method scans an identifier from the current reader.
     *
     * @return the next character following the whitespace.
     */
    @Throws(IOException::class)
    protected fun scanWhitespace(): Char {
        while (true) {
            val ch = readChar()
            when (ch) {
                ' ', '\t', '\n', '\r' -> {}
                else -> return ch
            }
        }
    }

    /**
     * This method scans an identifier from the current reader. The scanned
     * whitespace is appended to `result`.
     *
     * @return the next character following the whitespace.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `result != null`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun scanWhitespace(result: StringBuffer): Char {
        while (true) {
            val ch = readChar()
            when (ch) {
                ' ', '\t', '\n' -> result.append(ch)
                '\r' -> {}
                else -> return ch
            }
        }
    }

    /**
     * This method scans a delimited string from the current reader. The scanned
     * string without delimiters is appended to `string`.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `string != null`
     *  * the next char read is the string delimiter
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun scanString(string: StringBuffer) {
        val delimiter = readChar()
        if (delimiter != '\'' && delimiter != '"') {
            throw expectedInput("' or \"")
        }
        while (true) {
            val ch = readChar()
            if (ch == delimiter) {
                return
            } else if (ch == '&') {
                resolveEntity(string)
            } else {
                string.append(ch)
            }
        }
    }

    /**
     * Scans a #PCDATA element. CDATA sections and entities are resolved. The
     * next &lt; char is skipped. The scanned data is appended to
     * `data`.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `data != null`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun scanPCData(data: StringBuffer) {
        while (true) {
            var ch = readChar()
            if (ch == '<') {
                ch = readChar()
                if (ch == '!') {
                    checkCDATA(data)
                } else {
                    unreadChar(ch)
                    return
                }
            } else if (ch == '&') {
                resolveEntity(data)
            } else {
                data.append(ch)
            }
        }
    }

    /**
     * Scans a special tag and if the tag is a CDATA section, append its content
     * to `buf`.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `buf != null`
     *  * The first &lt; has already been read.
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun checkCDATA(buf: StringBuffer): Boolean {
        var ch = readChar()
        return if (ch != '[') {
            unreadChar(ch)
            skipSpecialTag(0)
            false
        } else if (!checkLiteral("CDATA[")) {
            skipSpecialTag(1) // one [ has already been read
            false
        } else {
            var delimiterCharsSkipped = 0
            while (delimiterCharsSkipped < 3) {
                ch = readChar()
                when (ch) {
                    ']' -> if (delimiterCharsSkipped < 2) {
                        delimiterCharsSkipped += 1
                    } else {
                        buf.append(']')
                        buf.append(']')
                        delimiterCharsSkipped = 0
                    }
                    '>' -> if (delimiterCharsSkipped < 2) {
                        var i = 0
                        while (i < delimiterCharsSkipped) {
                            buf.append(']')
                            i++
                        }
                        delimiterCharsSkipped = 0
                        buf.append('>')
                    } else {
                        delimiterCharsSkipped = 3
                    }
                    else -> {
                        var i = 0
                        while (i < delimiterCharsSkipped) {
                            buf.append(']')
                            i += 1
                        }
                        buf.append(ch)
                        delimiterCharsSkipped = 0
                    }
                }
            }
            true
        }
    }

    /**
     * Skips a comment.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * The first &lt;!-- has already been read.
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun skipComment() {
        var dashesToRead = 2
        while (dashesToRead > 0) {
            val ch = readChar()
            if (ch == '-') {
                dashesToRead -= 1
            } else {
                dashesToRead = 2
            }
        }
        if (readChar() != '>') {
            throw expectedInput(">")
        }
    }

    /**
     * Skips a special tag or comment.
     *
     * @param bracketLevel
     * The number of open square brackets ([) that have already been
     * read.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * The first &lt;! has already been read.
     *  * `bracketLevel >= 0`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun skipSpecialTag(bracketLevel: Int) {
        var bracketLevel = bracketLevel
        var tagLevel = 1 // <
        var stringDelimiter = '\u0000'
        if (bracketLevel == 0) {
            var ch = readChar()
            if (ch == '[') {
                bracketLevel += 1
            } else if (ch == '-') {
                ch = readChar()
                if (ch == '[') {
                    bracketLevel += 1
                } else if (ch == ']') {
                    bracketLevel -= 1
                } else if (ch == '-') {
                    skipComment()
                    return
                }
            }
        }
        while (tagLevel > 0) {
            val ch = readChar()
            if (stringDelimiter == '\u0000') {
                if (ch == '"' || ch == '\'') {
                    stringDelimiter = ch
                } else if (bracketLevel <= 0) {
                    if (ch == '<') {
                        tagLevel += 1
                    } else if (ch == '>') {
                        tagLevel -= 1
                    }
                }
                if (ch == '[') {
                    bracketLevel += 1
                } else if (ch == ']') {
                    bracketLevel -= 1
                }
            } else {
                if (ch == stringDelimiter) {
                    stringDelimiter = '\u0000'
                }
            }
        }
    }

    /**
     * Scans the data for literal text. Scanning stops when a character does not
     * match or after the complete text has been checked, whichever comes first.
     *
     * @param literal
     * the literal to check.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `literal != null`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun checkLiteral(literal: String): Boolean {
        val length = literal.length
        var i = 0
        while (i < length) {
            if (readChar() != literal[i]) {
                return false
            }
            i += 1
        }
        return true
    }

    /**
     * Reads a character from a reader.
     */
    @Throws(IOException::class)
    protected fun readChar(): Char {
        return if (charReadTooMuch != '\u0000') {
            val ch = charReadTooMuch
            charReadTooMuch = '\u0000'
            ch
        } else {
            val i = reader!!.read()
            if (i < 0) {
                throw unexpectedEndOfData()
            } else if (i == 10) {
                parserLineNr += 1
                '\n'
            } else {
                i.toChar()
            }
        }
    }

    /**
     * Scans an XML element.
     *
     * @param elt
     * The element that will contain the result.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * The first &lt; has already been read.
     *  * `elt != null`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun scanElement(elt: XMLElement) {
        var isCollectionMode = false
        val buf = StringBuffer()
        scanIdentifier(buf)
        val name = buf.toString()
        elt.name = name
        if (XML_NODE_XHTML_CONTENT_TAG == name) {
            /*
			 * special case of html content tag: collect chars until </...>
			 * occurs.
			 */
            isCollectionMode = true
        }
        var ch = this.scanWhitespace()

        // Scan the attributes of opening tag
        while (ch != '>' && ch != '/') { // Not the end of the tag
            buf.setLength(0)
            unreadChar(ch)
            scanIdentifier(buf)
            val key = buf.toString()
            ch = this.scanWhitespace()
            if (ch != '=') {
                throw expectedInput("=")
            }
            unreadChar(this.scanWhitespace())
            buf.setLength(0)
            scanString(buf)
            elt.setAttribute(key, buf)
            ch = this.scanWhitespace()
        }
        if (ch == '/') { // Case of self ending tag
            ch = readChar()
            if (ch != '>') {
                throw expectedInput(">")
            }
            elt.completeElement()
            return
        }

        // special collection mode:
        if (isCollectionMode) {
            val waitingBuf = StringBuffer()
            var lastOpeningBreak = -1
            while (true) {
                ch = readChar()
                waitingBuf.append(ch)
                if (ch == '<') {
                    lastOpeningBreak = waitingBuf.length - 1
                }
                if (ch == '>' && lastOpeningBreak >= 0) {
                    var content = waitingBuf.toString()
                    if (sContentEndTagPattern == null) {
                        sContentEndTagPattern = Pattern
                                .compile(XML_NODE_XHTML_CONTENT_END_TAG_REGEXP)
                    }
                    val substring = content.substring(lastOpeningBreak)
                    val matcher = sContentEndTagPattern!!.matcher(substring)
                    if (matcher.matches()) {
                        // end found, remove the end tag:
                        content = content.substring(0, lastOpeningBreak)
                        // Dimitry: begin
                        // // PCDATA
                        // if (this.ignoreWhitespace) {
                        // elt.setContent(content.trim());
                        // } else {
                        // elt.setContent(content);
                        // }
                        // Dimitry: always remove spaces around the rich content
                        // block because it should be valid xml.
                        elt.content = content.trim { it <= ' ' }
                        // Dimitry: end
                        elt.completeElement()
                        return
                    }
                }
            }
        }

        // This part is unclear - probing for PCDATA
        buf.setLength(0)
        ch = this.scanWhitespace(buf)
        if (ch != '<') { // Either: PCDATA
            unreadChar(ch)
            scanPCData(buf)
        } else { // Or: Maybe sequence of children tags
            while (true) {
                // This is a loop, after all
                ch = readChar()
                if (ch == '!') {
                    if (checkCDATA(buf)) {
                        scanPCData(buf)
                        break
                    } else {
                        ch = this.scanWhitespace(buf)
                        if (ch != '<') {
                            unreadChar(ch)
                            scanPCData(buf)
                            break
                        }
                    }
                } else {
                    buf.setLength(0)
                    break
                }
            }
        }
        if (buf.length == 0) {

            // Not PCDATA, '<' already read
            while (ch != '/') {
                if (ch == '!') { // Comment
                    ch = readChar()
                    if (ch != '-') {
                        throw expectedInput("Comment or Element")
                    }
                    ch = readChar()
                    if (ch != '-') {
                        throw expectedInput("Comment or Element")
                    }
                    skipComment()
                } else { // Not Comment
                    unreadChar(ch)
                    val child = createAnotherElement()
                    // Here goes the recursion.
                    scanElement(child)
                    elt.addChild(child)
                }
                ch = this.scanWhitespace()
                if (ch != '<') {
                    throw expectedInput("<")
                }
                ch = readChar()
            }
            unreadChar(ch)
        } else {

            // PCDATA
            if (ignoreWhitespace) {
                elt.content = buf.toString().trim { it <= ' ' }
            } else {
                elt.content = buf.toString()
            }
        }

        //
        ch = readChar()
        if (ch != '/') {
            throw expectedInput("/")
        }
        unreadChar(this.scanWhitespace())
        if (!checkLiteral(name)) {
            throw expectedInput(name)
        }
        if (this.scanWhitespace() != '>') {
            throw expectedInput(">")
        }
        elt.completeElement()
    }

    /**
     * Resolves an entity. The name of the entity is read from the reader. The
     * value of the entity is appended to `buf`.
     *
     * @param buf
     * Where to put the entity value.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * The first &amp; has already been read.
     *  * `buf != null`
     *
    </dd> *
    </dl> *
     */
    @Throws(IOException::class)
    protected fun resolveEntity(buf: StringBuffer) {
        var ch = '\u0000'
        val keyBuf = StringBuffer()
        while (true) {
            ch = readChar()
            if (ch == ';') {
                break
            }
            keyBuf.append(ch)
        }
        val key = keyBuf.toString()
        if (key[0] == '#') {
            ch = try {
                if (key[1] == 'x') {
                    key.substring(2).toInt(16).toChar()
                } else {
                    key.substring(1).toInt(10).toChar()
                }
            } catch (e: NumberFormatException) {
                throw unknownEntity(key)
            }
            buf.append(ch)
        } else {
            val value = entities[key] ?: throw unknownEntity(key)
            buf.append(value)
        }
    }

    /**
     * Pushes a character back to the read-back buffer.
     *
     * @param ch
     * The character to push back.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * The read-back buffer is empty.
     *  * `ch != '\0'`
     *
    </dd> *
    </dl> *
     */
    protected fun unreadChar(ch: Char) {
        charReadTooMuch = ch
    }

    /**
     * Creates a parse exception for when an invalid valueset is given to a
     * method.
     *
     * @param name
     * The name of the entity.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *
    </dd> *
    </dl> *
     */
    protected fun invalidValueSet(name: String): XMLParseException {
        val msg = "Invalid value set (entity name = \"$name\")"
        return XMLParseException(this.name, parserLineNr, msg)
    }

    /**
     * Creates a parse exception for when an invalid value is given to a method.
     *
     * @param name
     * The name of the entity.
     * @param value
     * The value of the entity.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `value != null`
     *
    </dd> *
    </dl> *
     */
    protected fun invalidValue(name: String, value: String?): XMLParseException {
        val msg = ("Attribute \"" + name + "\" does not contain a valid "
                + "value (\"" + value + "\")")
        return XMLParseException(this.name, parserLineNr, msg)
    }

    /**
     * Creates a parse exception for when the end of the data input has been
     * reached.
     */
    protected fun unexpectedEndOfData(): XMLParseException {
        val msg = "Unexpected end of data reached"
        return XMLParseException(name, parserLineNr, msg)
    }

    /**
     * Creates a parse exception for when a syntax error occured.
     *
     * @param context
     * The context in which the error occured.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `context != null`
     *  * `context.length() > 0`
     *
    </dd> *
    </dl> *
     */
    protected fun syntaxError(context: String): XMLParseException {
        val msg = "Syntax error while parsing $context"
        return XMLParseException(name, parserLineNr, msg)
    }

    /**
     * Creates a parse exception for when the next character read is not the
     * character that was expected.
     *
     * @param charSet
     * The set of characters (in human readable form) that was
     * expected.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `charSet != null`
     *  * `charSet.length() > 0`
     *
    </dd> *
    </dl> *
     */
    protected fun expectedInput(charSet: String): XMLParseException {
        val msg = "Expected: $charSet"
        return XMLParseException(name, parserLineNr, msg)
    }

    /**
     * Creates a parse exception for when an entity could not be resolved.
     *
     * @param name
     * The name of the entity.
     *
     *
     * <dl>
     * <dt>**Preconditions:**</dt>
     * <dd>
     *
     *  * `name != null`
     *  * `name.length() > 0`
     *
    </dd> *
    </dl> *
     */
    protected fun unknownEntity(name: String): XMLParseException {
        val msg = "Unknown or invalid entity: &$name;"
        return XMLParseException(this.name, parserLineNr, msg)
    }

    companion object {
        const val XML_NODE_XHTML_CONTENT_TAG = "richcontent"
        const val XML_NODE_XHTML_CONTENT_END_TAG_REGEXP = ("<\\s*/\\s*"
                + XML_NODE_XHTML_CONTENT_TAG + "\\s*>")
        private var sContentEndTagPattern: Pattern? = null

        /**
         * Serialization serial version ID.
         */
        const val serialVersionUID = 6685035139346394777L

        /**
         * Major version of NanoXML. Classes with the same major and minor version
         * are binary compatible. Classes with the same major version are source
         * compatible. If the major version is different, you may need to modify the
         * client source code.
         *
         * @see freemind.main.XMLElement.NANOXML_MINOR_VERSION
         */
        const val NANOXML_MAJOR_VERSION = 2

        /**
         * Minor version of NanoXML. Classes with the same major and minor version
         * are binary compatible. Classes with the same major version are source
         * compatible. If the major version is different, you may need to modify the
         * client source code.
         *
         * @see freemind.main.XMLElement.NANOXML_MAJOR_VERSION
         */
        const val NANOXML_MINOR_VERSION = 2
    }
}