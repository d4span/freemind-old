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
/*$Id: MindMapMode.java,v 1.17.18.2 2006-01-12 23:10:13 christianfoltin Exp $*/

package freemind.modes.mindmapmode;

import freemind.controller.Controller;
import freemind.controller.StructuredMenuHolder;
import freemind.modes.Mode;
import freemind.modes.ModeController;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.JMenu;
import javax.swing.JToolBar;

public class MindMapMode implements Mode {

    private Controller c;
    private MindMapController modecontroller;
    private final String MODENAME = "MindMap";
	private boolean isRunning = false;
	private static Logger logger = null;

    public MindMapMode() {
    }

    public void init (Controller c) {
    		this.c = c;
		if (logger == null) {
			logger = c.getFrame().getLogger(this.getClass().getName());
		}
		modecontroller =  (MindMapController) createModeController();
    }

	public ModeController createModeController() {
		logger.finest("Creating new MindMapController..." );
		MindMapController mindMapController = new MindMapController(this);
		logger.finest("Creating new MindMapController. Done:"+mindMapController );
		return mindMapController;
	}

    public String toString() {
	return MODENAME;
    }

    /**
     * Called whenever this mode is chosen in the program.
     * (updates Actions etc.)
     */
    public void activate() {
        if(isRunning) {
            c.getMapModuleManager().changeToMapOfMode(this);
        } else {
            isRunning = true;
        }
    }

    public void restore(String restoreable) {
	try {
	    getDefaultModeController().load(new File(restoreable).toURL());
	} catch (Exception e) {
	    c.errorMessage("An error occured on opening the file: "+restoreable + ".");
        e.printStackTrace();
	}
    }
    
    public Controller getController() {
	return c;
    }

    public ModeController getDefaultModeController() {
	return modecontroller;
    }


}