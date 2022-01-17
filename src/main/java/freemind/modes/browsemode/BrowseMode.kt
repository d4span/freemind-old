/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
package freemind.modes.browsemode

import freemind.controller.Controller
import freemind.main.Resources
import freemind.modes.Mode
import freemind.modes.ModeController
import java.io.File

class BrowseMode : Mode() {
    private var c: Controller? = null
    private var modecontroller: BrowseController? = null
    private var isRunning = false
    override fun init(c: Controller) {
        this.c = c
        modecontroller = BrowseController(this)
    }

    override fun toString(): String {
        return MODENAME
    }

    /**
     * Called whenever this mode is chosen in the program. (updates Actions
     * etc.)
     */
    override fun activate() {
        if (isRunning) {
            c!!.mapModuleManager!!.changeToMapOfMode(this)
        } else {
            isRunning = true
        }
    }

    override fun restore(restoreable: String) {
        try {
            defaultModeController.load(File(restoreable))
        } catch (e: Exception) {
            c!!.errorMessage(
                "An error occured on opening the file: " +
                    restoreable + "."
            )
            Resources.getInstance().logException(e)
        }
    }

    override fun getController(): Controller {
        return c!!
    }

    override fun getDefaultModeController(): ModeController {
        // no url should be visible for the empty controller.
        modecontroller!!.toolBar.setURLField("")
        return modecontroller!!
    }

    override fun createModeController(): ModeController {
        return BrowseController(this)
    }

    companion object {
        const val MODENAME = "Browse"
    }
}
