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
 * Created on 07.10.2004
 */

package freemind.modes.mindmapmode.actions;

import freemind.dependencies.view.swing.NodeRepresentation;
import freemind.main.Tools;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.Action;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * @author foltin
 * 
 */
@SuppressWarnings("serial")
public class AddLocalLinkAction extends MindmapAction {

	private final MindMapController modeController;

	/**
     */
	public AddLocalLinkAction(MindMapController modeController) {
		super("paste_as_local_link", "images/stock_right.png", modeController);
		this.modeController = modeController;
	}

	public void actionPerformed(ActionEvent e) {
		NodeRepresentation source = modeController.getSelected();
		Vector<NodeRepresentation> nodesFromClipboard = Tools
				.getMindMapNodesFromClipboard(modeController);
		if (nodesFromClipboard.size() == 0) {
			modeController.getController().errorMessage(
					modeController.getText("no_copied_nodes"));
			return;
		}
		boolean first = true;
		for (NodeRepresentation destination : nodesFromClipboard) {
			if(!first) {
				logger.warning("Can't link the node '"+source+"' to more than one destination. Only the last is used.");
			}
			if(source != destination) {
				modeController.setLink(source, "#" + modeController.getNodeID(destination));
			} else {
				// hmm, give an error?
				logger.warning("Can't link the node '"+source+"' onto itself. Skipped.");
			}
			first = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.modes.FreemindAction#isEnabled(javax.swing.JMenuItem,
	 * javax.swing.Action)
	 */
	@Override
	public boolean isEnabled(JMenuItem pItem, Action pAction) {
		return super.isEnabled(pItem, pAction)
				&& (modeController != null)
				&& Tools.getMindMapNodesFromClipboard(modeController).size() == 1;
	}

}
