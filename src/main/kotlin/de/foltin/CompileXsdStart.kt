/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2009 Christian Foltin and others.
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
 * Created on 16.06.2009
 */
/*$Id: CompileXsdStart.java,v 1.1.2.1 2009/07/17 19:17:41 christianfoltin Exp $*/
package de.foltin

import java.lang.StringBuilder
import de.foltin.CompileXsdStart.XsdHandler
import java.util.TreeSet
import java.lang.StringBuffer
import de.foltin.CompileXsdStart.ElementTypes
import kotlin.Throws
import de.foltin.CompileXsdStart
import de.foltin.CompileXsdStart.ComplexTypeHandler
import de.foltin.CompileXsdStart.ComplexContentHandler
import de.foltin.CompileXsdStart.SchemaHandler
import de.foltin.CompileXsdStart.SequenceHandler
import de.foltin.CompileXsdStart.ChoiceHandler
import de.foltin.CompileXsdStart.AttributeHandler
import de.foltin.CompileXsdStart.EnumerationHandler
import de.foltin.CompileXsdStart.ChoiceElementHandler
import java.util.Locale
import de.foltin.CompileXsdStart.SequenceElementHandler
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.helpers.DefaultHandler
import java.io.*
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.HashMap
import java.util.StringTokenizer
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParserFactory
import kotlin.jvm.JvmStatic

/**
 * @author foltin
 */
class CompileXsdStart(private val mInputStream: InputStream) : DefaultHandler() {
    private var mCurrentHandler: XsdHandler? = null
    private val mKeyOrder = TreeSet<String>()
    private val mClassMap = HashMap<String?, HashMap<String, String>>()
    private val mBindingXml = StringBuffer()
    private val mElementMap = HashMap<String, ElementTypes>()
    private val mTypeMap = HashMap<String?, String>()

    inner class ElementTypes(pEnumerationId: Int) {
        val id: Int

        init {
            mEnumerationId = pEnumerationId
        }
    }

    val Schema_Id = 0
    val ComplexType_Id = 1
    val Sequence_Id = 2
    val Choice_Id = 3
    val Attribute_Id = 4
    val ComplexContent_Id = 5
    val Element_Id = 6
    val Extension_Id = 7
    val SimpleType_Id = 8
    val Restriction_Id = 9
    val Enumeration_Id = 10
    val Group_Id = 11
    var Schema = ElementTypes(Schema_Id)
    var ComplexType = ElementTypes(ComplexType_Id)
    var Sequence = ElementTypes(Sequence_Id)
    var Choice = ElementTypes(Choice_Id)
    var Attribute = ElementTypes(Attribute_Id)
    var ComplexContent = ElementTypes(ComplexContent_Id)
    var Element = ElementTypes(Element_Id)
    var Extension = ElementTypes(Extension_Id)
    var SimpleType = ElementTypes(SimpleType_Id)
    var Restriction = ElementTypes(Restriction_Id)
    var Enumeration = ElementTypes(Enumeration_Id)
    var Group = ElementTypes(Group_Id)

    init {
        mElementMap["xs:schema"] = Schema
        mElementMap["xs:complexType"] = ComplexType
        mElementMap["xs:complexContent"] = ComplexContent
        mElementMap["xs:element"] = Element
        mElementMap["xs:extension"] = Extension
        mElementMap["xs:choice"] = Choice
        mElementMap["xs:sequence"] = Sequence
        mElementMap["xs:attribute"] = Attribute
        mElementMap["xs:simpleType"] = SimpleType
        mElementMap["xs:restriction"] = Restriction
        mElementMap["xs:enumeration"] = Enumeration
        mElementMap["xs:group"] = Group
        mTypeMap["xs:long"] = "long"
        mTypeMap["xs:int"] = "int"
        mTypeMap["xs:string"] = "String"
        mTypeMap["xs:boolean"] = "boolean"
        mTypeMap["xs:float"] = "float"
        mTypeMap["xs:double"] = "double"
    }

    @Throws(Exception::class)
    private fun print() {
        val dir = File(DESTINATION_DIR)
        dir.mkdirs()
        for (className in mClassMap.keys) {

            // special handling for strange group tag.
            if (className == null) continue
            val classMap = mClassMap[className]!!
            // System.out.println("\nClass:" + keys);
            val fs = FileOutputStream(DESTINATION_DIR + "/" + className + ".java")
            for (orderString in mKeyOrder) {
                if (classMap.containsKey(orderString)) {
                    fs.write(classMap[orderString]!!.toByteArray())
                    // System.out.print(string);
                }
            }
            fs.close()
        }
        // write binding to disk
        if (true) {
            val fs = FileOutputStream(DESTINATION_DIR + "/binding.xml")
            fs.write(mBindingXml.toString().toByteArray())
            fs.close()
        }
    }

    @Throws(ParserConfigurationException::class, SAXException::class, IOException::class)
    fun generate() {
        val factory = SAXParserFactory.newInstance()
        val saxParser = factory.newSAXParser()
        mCurrentHandler = XsdHandler(null)
        mBindingXml.setLength(0)
        mBindingXml
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><binding>\n")
        // introduce correct marshaling for newlines in strings:
        mBindingXml
                .append("<format type=\"java.lang.String\" serializer=\"de.foltin.StringEncoder.encode\" deserializer=\"de.foltin.StringEncoder.decode\"/>\n")
        saxParser.parse(mInputStream, this)
        mBindingXml.append("</binding>\n")
        // System.out.println(mBindingXml.toString());
    }

    private open inner class XsdHandler(var mParent: XsdHandler?) : DefaultHandler() {
        var mClassName: String? = null
        var mExtendsClassName: String? = null
        val className: String?
            get() {
                if (mClassName != null) {
                    return mClassName
                }
                return if (mParent != null) mParent.getClassName() else null
            }
        val classMap: HashMap<String, String>
            get() {
                val className: String = getClassName()
                return createClass(className)
            }

        protected fun appendToClassMap(key: String, value: String) {
            mKeyOrder.add(key)
            val classMap: HashMap<String, String> = getClassMap()
            if (classMap.containsKey(key)) {
                classMap[key] = classMap[key] + value
            } else {
                classMap[key] = value
            }
        }

        protected fun addArrayListImport() {
            appendToClassMap(KEY_IMPORT_ARRAY_LIST,
                    "import java.util.ArrayList;\n")
        }

        val extendsClassName: String?
            get() {
                if (mExtendsClassName != null) {
                    return mExtendsClassName
                }
                return if (mParent == null) {
                    null
                } else mParent.getExtendsClassName()
            }

        open fun startElement(pName: String?, pAttributes: Attributes) {}
        @Throws(SAXException::class)
        override fun startElement(pUri: String, pLocalName: String, pName: String,
                                  pAttributes: Attributes) {
            super.startElement(pUri, pLocalName, pName, pAttributes)
            // System.out.print("[ " + pName + ", ");
            // for (int i = 0; i < pAttributes.getLength(); ++i) {
            // System.out.print(pAttributes.getLocalName(i) + "="
            // + pAttributes.getValue(i));
            // }
            // System.out.println("]");
            val defaultHandlerType: ElementTypes?
            defaultHandlerType = if (mElementMap.containsKey(pName)) {
                mElementMap[pName]
            } else {
                throw IllegalArgumentException("Element " + pName
                        + " is not matched.")
            }
            var nextHandler: XsdHandler? = null
            when (defaultHandlerType.getId()) {
                Element_Id -> nextHandler = createElementHandler()
                ComplexType_Id -> nextHandler = ComplexTypeHandler(this)
                ComplexContent_Id -> nextHandler = ComplexContentHandler(this)
                Schema_Id -> nextHandler = SchemaHandler(this)
                Sequence_Id -> nextHandler = SequenceHandler(this)
                Choice_Id -> nextHandler = ChoiceHandler(this)
                Extension_Id -> nextHandler = ExtensionHandler(this)
                Attribute_Id -> nextHandler = AttributeHandler(this)
                Enumeration_Id -> nextHandler = EnumerationHandler(this)
                Group_Id -> nextHandler = GroupHandler(this)
                else -> nextHandler = XsdHandler(this)
            }
            mCurrentHandler = nextHandler
            nextHandler!!.startElement(pName, pAttributes)
        }

        protected open fun createElementHandler(): XsdHandler? {
            return ComplexTypeHandler(this)
        }

        @Throws(SAXException::class)
        override fun endElement(pUri: String, pLocalName: String, pName: String) {
            super.endElement(pUri, pLocalName, pName)
            mCurrentHandler = mParent
        }
    }

    private inner class ExtensionHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            val base = arg1.getValue("base")
            mExtendsClassName = getNameFromXml(base)
            mKeyOrder.add(KEY_CLASS_EXTENSION)
            getClassMap().put(KEY_CLASS_EXTENSION,
                    " extends $mExtendsClassName")
            mBindingXml.append("""    <structure map-as="${base}_type"/>
""")
            // inform parents:
            var xsdHandlerHierarchy: XsdHandler? = this
            do {
                if (xsdHandlerHierarchy is ComplexTypeHandler) {
                    xsdHandlerHierarchy.mExtendsClassName = mExtendsClassName
                }
                xsdHandlerHierarchy = xsdHandlerHierarchy!!.mParent
            } while (xsdHandlerHierarchy != null)
        }
    }

    private inner class SchemaHandler(pParent: XsdHandler?) : XsdHandler(pParent)
    private inner class ChoiceHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        var isSingleChoice = false
            private set

        override fun createElementHandler(): XsdHandler? {
            return ChoiceElementHandler(this)
        }

        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            if (arg1.getValue("maxOccurs") != null) {
                // single array list:
                isSingleChoice = true
                appendToClassMap(
                        KEY_CLASS_SINGLE_CHOICE,
                        """  public void addChoice(Object choice) {
    choiceList.add(choice);
  }

  public void addAtChoice(int position, Object choice) {
    choiceList.add(position, choice);
  }

  public void setAtChoice(int position, Object choice) {
    choiceList.set(position, choice);
  }
  public Object getChoice(int index) {
    return (Object)choiceList.get( index );
  }

  public int sizeChoiceList() {
    return choiceList.size();
  }

  public void clearChoiceList() {
    choiceList.clear();
  }

  public java.util.List getListChoiceList() {
    return java.util.Collections.unmodifiableList(choiceList);
  }

  protected ArrayList choiceList = new ArrayList();

""")
                addArrayListImport()
                mBindingXml
                        .append("    <collection field='choiceList' ordered='false'>\n")
            }
        }

        @Throws(SAXException::class)
        override fun endElement(arg0: String, arg1: String, arg2: String) {
            if (isSingleChoice) {
                mBindingXml.append("    </collection>\n")
            }
            super.endElement(arg0, arg1, arg2)
        }
    }

    private inner class ChoiceElementHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        private var mIsSingle = false

        init {
            if (pParent is ChoiceHandler) {
                mIsSingle = pParent.isSingleChoice()
            } else {
                throw IllegalArgumentException(
                        "Hmm, parent is not a choice.")
            }
        }

        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            val rawName = arg1.getValue("ref")
            val name = getNameFromXml(rawName)
            val memberName = (name!!.substring(0, 1).lowercase(Locale.getDefault())
                    + name.substring(1))
            if (mIsSingle) {
                mBindingXml
                        .append("""      <structure usage="optional" map-as="$FREEMIND_PACKAGE.$name"/>
""")
                return
            }
            // do multiple choices.
            appendToClassMap(KEY_CLASS_MULTIPLE_CHOICES_MEMBERS, """  protected $name $memberName;

""")
            appendToClassMap(KEY_CLASS_MULTIPLE_CHOICES_SETGET, """  public $name get$name() {
    return this.$memberName;
  }

""")
            appendToClassMap(KEY_CLASS_MULTIPLE_CHOICES_SETGET,
                    """  public void set$name($name value){
    this.$memberName = value;
  }

""")
            mBindingXml.append("""    <structure field="$memberName" usage="optional" map-as="$FREEMIND_PACKAGE.$name"/>
""")
        }
    }

    private inner class GroupHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        @Throws(SAXException::class)
        override fun startElement(arg0: String, arg1: String, arg2: String,
                                  arg3: Attributes) {
            // super.startElement(arg0, arg1, arg2, arg3);
            // omit the output.
            mCurrentHandler = GroupHandler(this)
        }
    }

    private inner class SequenceHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        override fun createElementHandler(): XsdHandler? {
            return SequenceElementHandler(this)
        }
    }

    private inner class SequenceElementHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            var rawName = arg1.getValue("name")
            var type = arg1.getValue("type")
            var isRef = false
            if (rawName == null) {
                rawName = arg1.getValue("ref")
                isRef = true
            }
            val name = getNameFromXml(rawName)
            val memberName = (name!!.substring(0, 1).lowercase(Locale.getDefault())
                    + name.substring(1))
            type = if (isRef) {
                name
            } else {
                getType(type)
            }
            val maxOccurs = arg1.getValue("maxOccurs")
            val minOccurs = arg1.getValue("minOccurs")
            if (maxOccurs != null && maxOccurs.trim { it <= ' ' } == "1") {
                // single ref:
                appendToClassMap(KEY_CLASS_MULTIPLE_CHOICES_MEMBERS,
                        "  protected $type $memberName;\n\n")
                appendToClassMap(KEY_CLASS_MULTIPLE_CHOICES_SETGET, """  public $type get$name() {
    return this.$memberName;
  }

""")
                appendToClassMap(KEY_CLASS_MULTIPLE_CHOICES_SETGET,
                        """  public void set$name($type value){
    this.$memberName = value;
  }

""")
                var optReq = "optional"
                if (minOccurs != null && minOccurs.trim { it <= ' ' } == "1") {
                    optReq = "required"
                }
                if (isRef) {
                    mBindingXml.append("""      <structure field="$memberName" usage="$optReq" map-as="$FREEMIND_PACKAGE.$type"/>
""")
                } else {
                    mBindingXml.append("""      <value name="$rawName" field="$memberName" usage="$optReq"/>
""")
                    // whitespace='preserve' doesn't work
                }
            } else {
                // list ref:
                appendToClassMap(KEY_CLASS_SEQUENCE, """  public void add$name($name $memberName) {
    ${memberName}List.add($memberName);
  }

  public void addAt$name(int position, $name $memberName) {
    ${memberName}List.add(position, $memberName);
  }

  public $name get$name(int index) {
    return ($name)${memberName}List.get( index );
  }

  public void removeFrom${name}ElementAt(int index) {
    ${memberName}List.remove( index );
  }

  public int size${name}List() {
    return ${memberName}List.size();
  }

  public void clear${name}List() {
    ${memberName}List.clear();
  }

  public java.util.List getList${name}List() {
    return java.util.Collections.unmodifiableList(${memberName}List);
  }
    protected ArrayList ${memberName}List = new ArrayList();

""")
                addArrayListImport()
                mBindingXml.append("""    <collection field="${memberName}List">
      <structure map-as="$FREEMIND_PACKAGE.$name"/>
    </collection>
""")
            }
        }
    }

    private inner class ComplexTypeHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        private var mIsClassDefinedHere = false
        private var mRawName: String? = null
        private var mMixed = false
        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            val mixed = arg1.getValue("mixed")
            if ("true" == mixed) {
                // in case of mixed content (those with additional cdata
                // content), we add a "content" field to the class
                mMixed = true
            }
            if (getClassName() == null) {
                mRawName = startClass(arg1)
                // make binding:
                mBindingXml.append("""  <mapping class='$FREEMIND_PACKAGE.$mClassName' type-name='${mRawName}_type' abstract='true'>
""")
                mIsClassDefinedHere = true
            }
        }

        /**
         * @param arg1
         * @return the class name
         */
        protected fun startClass(arg1: Attributes): String {
            mKeyOrder.add(FILE_START)
            mKeyOrder.add(KEY_PACKAGE)
            mKeyOrder.add(KEY_CLASS_START)
            mKeyOrder.add(KEY_CLASS_END)
            val rawName = arg1.getValue("name")
            val name = getNameFromXml(rawName)
            val class1 = createClass(name)
            mClassName = name
            class1[FILE_START] = "/* $name...*/\n"
            class1[KEY_PACKAGE] = """
                 package $FREEMIND_PACKAGE;
                 
                 """.trimIndent()
            class1[KEY_CLASS_START] = "public class $name"
            mKeyOrder.add(KEY_CLASS_START2)
            class1[KEY_CLASS_START2] = " {\n"
            mKeyOrder.add(KEY_CLASS_CONSTANTS)
            class1[KEY_CLASS_CONSTANTS] = "  /* constants from enums*/\n"
            if (mMixed) {
                mKeyOrder.add(KEY_CLASS_MIXED)
                class1[KEY_CLASS_MIXED] = " public String content; public String getContent(){return content;} public void setContent(String content){this.content = content;}\n"
            }
            class1[KEY_CLASS_END] = "} /* $name*/\n"
            return rawName
        }

        @Throws(SAXException::class)
        override fun endElement(arg0: String, arg1: String, arg2: String) {
            if (mIsClassDefinedHere) {
                var extendString = ""
                if (getExtendsClassName() != null) {
                    extendString = (" extends=\"" + FREEMIND_PACKAGE + "."
                            + getExtendsClassName() + "\"")
                }
                if (mMixed) {
                    mBindingXml
                            .append("     <value field='content' style='text'/>\n")
                }
                mBindingXml.append("""  </mapping>
  <mapping name="$mRawName"$extendString class="$FREEMIND_PACKAGE.$mClassName"><structure map-as="${mRawName}_type"/></mapping>

""")
            }
            super.endElement(arg0, arg1, arg2)
        }
    }

    private inner class ComplexContentHandler(pParent: XsdHandler?) : XsdHandler(pParent)
    private inner class AttributeHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            var type = arg1.getValue("type")
            type = getType(type)
            val rawName = arg1.getValue("name")
            val usage = arg1.getValue("use")
            val minOccurs = arg1.getValue("minOccurs")
            var name = arg1.getValue("id")
            if (name == null) {
                name = getNameFromXml(rawName)
            }
            val memberName = decapitalizeFirstLetter(name)
            appendToClassMap(KEY_CLASS_PRIVATE_MEMBERS, """  protected $type $memberName;
""")
            appendToClassMap(KEY_CLASS_GETTERS, """  public $type get$name(){
    return $memberName;
  }
""")
            appendToClassMap(KEY_CLASS_SETTERS, """  public void set$name($type value){
    this.$memberName = value;
  }
""")
            mBindingXml.append("""    <value name='$rawName' field='$memberName' usage='${if ("required" == usage) "required" else "optional"}' ${if ("0" == minOccurs) "" else "style='attribute'"}/>
""")
            // whitespace='preserve' doesn't work
        }

        fun decapitalizeFirstLetter(name: String?): String {
            return name!!.substring(0, 1).lowercase(Locale.getDefault()) + name.substring(1)
        }
    }

    private inner class EnumerationHandler(pParent: XsdHandler?) : XsdHandler(pParent) {
        override fun startElement(arg0: String?, arg1: Attributes) {
            super.startElement(arg0, arg1)
            val `val` = arg1.getValue("value")
            appendToClassMap(KEY_CLASS_CONSTANTS, """  public static final String ${`val`.uppercase(Locale.getDefault())} = "$`val`";
""")
        }
    }

    @Throws(SAXException::class)
    override fun endElement(pUri: String, pLocalName: String, pName: String) {
        mCurrentHandler!!.endElement(pUri, pLocalName, pName)
    }

    fun createClass(pName: String?): HashMap<String, String> {
        if (mClassMap.containsKey(pName)) {
            return mClassMap[pName]!!
        }
        val newValue = HashMap<String, String>()
        mClassMap[pName] = newValue
        return newValue
    }

    @Throws(SAXException::class)
    override fun startElement(pUri: String, pLocalName: String, pName: String,
                              pAttributes: Attributes) {
        mCurrentHandler!!.startElement(pUri, pLocalName, pName, pAttributes)
    }

    fun firstLetterCapitalized(text: String?): String? {
        return if (text == null || text.length == 0) {
            text
        } else text.substring(0, 1).uppercase(Locale.getDefault())
                + text.substring(1, text.length)
    }

    private fun getNameFromXml(pXmlString: String?): String? {
        val st = StringTokenizer(pXmlString, "_")
        var result: String? = ""
        while (st.hasMoreTokens()) {
            result += firstLetterCapitalized(st.nextToken())
        }
        return result
    }

    private fun getType(type: String?): String? {
        var type = type
        type = if (mTypeMap.containsKey(type)) {
            mTypeMap[type]
        } else {
            // FIXME: Bad hack for tokens:
            "String"
            // throw new IllegalArgumentException("Unknown type " + type);
        }
        return type
    }

    companion object {
        const val FREEMIND_PACKAGE = "freemind.controller.actions.generated.instance"
        private val DESTINATION_DIR = ("binding/src/"
                + FREEMIND_PACKAGE.replace('.', File.separatorChar))
        private const val FREEMIND_ACTIONS_XSD = "freemind_actions.xsd"
        private const val KEY_PACKAGE = "000_KEY_PACKAGE"
        private const val FILE_START = "010_start"
        private const val KEY_IMPORT_ARRAY_LIST = "020_import_array_list"
        private const val KEY_CLASS_START = "030_CLASS_START"
        private const val KEY_CLASS_EXTENSION = "040_CLASS_EXTENSION"
        private const val KEY_CLASS_START2 = "050_CLASS_START2"
        private const val KEY_CLASS_CONSTANTS = "051_CONSTANTS"
        private const val KEY_CLASS_MIXED = "055_CLASS_MIXED"
        private const val KEY_CLASS_PRIVATE_MEMBERS = "060_PRIVATE_MEMBERS"
        private const val KEY_CLASS_GETTERS = "070_Getters"
        private const val KEY_CLASS_SETTERS = "080_setters"
        private const val KEY_CLASS_SINGLE_CHOICE = "090_single_choice"
        private const val KEY_CLASS_MULTIPLE_CHOICES_MEMBERS = "100_choice_members"
        private const val KEY_CLASS_MULTIPLE_CHOICES_SETGET = "110_choice_setget"
        private const val KEY_CLASS_SEQUENCE = "120_sequence"
        private const val KEY_CLASS_END = "500_CLASS_END"
        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val cXS = CompileXsdStart(BufferedInputStream(
                    FileInputStream(FREEMIND_ACTIONS_XSD)))
            cXS.generate()
            cXS.print()
        }
    }
}