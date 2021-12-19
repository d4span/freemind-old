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
 * Created on 23.06.2004
 */
/*$Id: XmlBindingTools.java,v 1.1.2.2.2.5 2009/05/20 19:19:11 christianfoltin Exp $*/
package freemind.common

import freemind.controller.Controller
import freemind.controller.actions.generated.instance.WindowConfigurationStorage
import freemind.controller.actions.generated.instance.XmlAction
import freemind.main.Resources
import org.jibx.runtime.BindingDirectory
import org.jibx.runtime.IBindingFactory
import org.jibx.runtime.IMarshallingContext
import org.jibx.runtime.IUnmarshallingContext
import org.jibx.runtime.JiBXException
import java.awt.Dimension
import java.awt.Toolkit
import java.io.Reader
import java.io.StringReader
import java.io.StringWriter
import javax.swing.JDialog
import javax.swing.JOptionPane

/**
 * @author foltin Singleton
 */
class XmlBindingTools private constructor() {
    fun createMarshaller(): IMarshallingContext? {
        return try {
            mBindingFactory!!.createMarshallingContext()
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            null
        }
    }

    fun createUnmarshaller(): IUnmarshallingContext? {
        return try {
            mBindingFactory!!.createUnmarshallingContext()
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            null
        }
    }

    fun storeDialogPositions(
        controller: Controller,
        dialog: JDialog,
        storage: WindowConfigurationStorage,
        window_preference_storage_property: String?
    ) {
        val result = storeDialogPositions(storage, dialog)
        controller.setProperty(window_preference_storage_property, result)
    }

    fun storeDialogPositions(
        storage: WindowConfigurationStorage,
        dialog: JDialog
    ): String? {
        storage.x = dialog.x
        storage.y = dialog.y
        storage.width = dialog.width
        storage.height = dialog.height
        return marshall(storage)
    }

    fun decorateDialog(
        controller: Controller,
        dialog: JDialog,
        window_preference_storage_property: String?
    ): WindowConfigurationStorage? {
        val marshalled = controller
            .getProperty(window_preference_storage_property)
        return decorateDialog(marshalled, dialog)
    }

    fun decorateDialog(
        marshalled: String?,
        dialog: JDialog
    ): WindowConfigurationStorage? {
        // String unmarshalled = controller.getProperty(
        // propertyName);
        if (marshalled != null) {
            val storage = unMarshall(marshalled) as WindowConfigurationStorage?
            if (storage != null) {
                // Check that location is on current screen.
                val screenSize: Dimension
                if (Resources.getInstance().getBoolProperty(
                        "place_dialogs_on_first_screen"
                    )
                ) {
                    val defaultToolkit = Toolkit.getDefaultToolkit()
                    screenSize = defaultToolkit.screenSize
                } else {
                    screenSize = Dimension()
                    screenSize.height = Int.MAX_VALUE
                    screenSize.width = Int.MAX_VALUE
                }
                val delta = 20
                dialog.setLocation(
                    Math.min(storage.x, screenSize.width - delta),
                    Math.min(storage.y, screenSize.height - delta)
                )
                dialog.size = Dimension(
                    storage.width,
                    storage
                        .height
                )
                return storage
            }
        }

        // set standard dialog size of no size is stored
        val rootFrame = JOptionPane.getFrameForComponent(dialog)
        val prefSize = rootFrame.size
        prefSize.width = prefSize.width * 3 / 4
        prefSize.height = prefSize.height * 3 / 4
        dialog.size = prefSize
        return null
    }

    fun marshall(action: XmlAction?): String? {
        // marshall:
        // marshal to StringBuffer:
        val writer = StringWriter()
        val m = instance?.createMarshaller()
        try {
            m!!.marshalDocument(action, "UTF-8", null, writer)
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            return null
        }
        return writer.toString()
    }

    fun unMarshall(inputString: String?): XmlAction? {
        return unMarshall(StringReader(inputString))
    }

    /**
     */
    fun unMarshall(reader: Reader?): XmlAction? {
        return try {
            // unmarshall:
            val u = instance?.createUnmarshaller()
            u!!.unmarshalDocument(reader, null) as XmlAction
        } catch (e: JiBXException) {
            Resources.getInstance().logException(e)
            null
        }
    }

    companion object {
        @JvmStatic
        var instance: XmlBindingTools? = null
            get() {
                if (field == null) {
                    field = XmlBindingTools()
                    try {
                        mBindingFactory = BindingDirectory.getFactory(XmlAction::class.java)
                    } catch (e: JiBXException) {
                        Resources.getInstance().logException(e)
                    }
                }
                return field
            }
            private set
        private var mBindingFactory: IBindingFactory? = null
    }
}
