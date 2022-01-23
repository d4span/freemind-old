/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 by Christian Foltin
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


/*
 * Created on 19.02.2006
 *
 */

package accessories.plugins;

import java.awt.datatransfer.Transferable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import freemind.main.Tools;
import freemind.dependencies.view.swing.NodeRepresentation;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

/**
 * @author foltin
 */
public class ChangeNodeLevelAction extends MindMapNodeHookAdapter {

	/**
	 *
	 */
	public ChangeNodeLevelAction() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see freemind.extensions.NodeHook#invoke(freemind.modes.MindMapNode,
	 * java.util.List)
	 */
	public void invoke(NodeRepresentation rootNode) {
		// we dont need node.
		NodeRepresentation selectedNode;
		List<NodeRepresentation> selectedNodes;
		{
			NodeRepresentation focussed = getMindMapController().getSelected();
			List<NodeRepresentation> selecteds = getMindMapController().getSelecteds();
			selectedNode = focussed;
			selectedNodes = selecteds;
		}

		// bug fix: sort to make independent by user's selection:
		getMindMapController().sortNodesByDepth(selectedNodes);

		if (selectedNode.isRoot()) {
			getMindMapController().getController().errorMessage(
					getResourceString("cannot_add_parent_to_root"));
			return;
		}

		boolean upwards = Tools.safeEquals("left",
				getResourceString("action_type")) != selectedNode.isLeft();
		// Make sure the selected nodes all have the same parent
		// (this restriction is to simplify the action, and could
		// possibly be removed in the future, when we have undo)
		// Also make sure that none of the selected nodes are the root node
		NodeRepresentation selectedParent = selectedNode.getParentNode();
		for (NodeRepresentation node : selectedNodes) {
			if (node.getParentNode() != selectedParent) {
				getMindMapController().getController().errorMessage(
						getResourceString("cannot_add_parent_diff_parents"));
				return;
			}
			if (node == rootNode) {
				getMindMapController().getController().errorMessage(
						getResourceString("cannot_add_parent_to_root"));
				return;
			}
		}

		// collect node ids:
		String selectedNodeId = selectedNode.getObjectId(getController());
		// WORKAROUND: Make target of local hyperlinks for the case, that ids
		// are not stored persistently.
		getMap().getLinkRegistry().registerLocalHyperlinkId(selectedNodeId);
		Vector<String> selectedNodesId = new Vector<>();
		for (NodeRepresentation node : selectedNodes) {
			String nodeId = node.getObjectId(getController());
			// WORKAROUND: Make target of local hyperlinks for the case, that
			// ids are not stored persistently.
			getMap().getLinkRegistry().registerLocalHyperlinkId(nodeId);
			selectedNodesId.add(nodeId);
		}

		if (upwards) {
			if (selectedParent.isRoot()) {
				// change side of the items:
				boolean isLeft = selectedNode.isLeft();
				Transferable copy = getMindMapController().cut(selectedNodes);
				getMindMapController().paste(copy, selectedParent, false,
						!isLeft);
				select(selectedNodeId, selectedNodesId);
				return;
			}
			// determine child pos of parent
			NodeRepresentation grandParent = selectedParent.getParentNode();
			int parentPosition = grandParent.getChildPosition(selectedParent);
			boolean isLeft = selectedParent.isLeft();
			Transferable copy = getMindMapController().cut(selectedNodes);
			if (parentPosition == grandParent.getChildCount() - 1) {
				getMindMapController().paste(copy, grandParent, false, isLeft);
			} else {
				getMindMapController().paste(
						copy,
						(NodeRepresentation) grandParent
								.getChildAt(parentPosition + 1), true, isLeft);
			}
			select(selectedNodeId, selectedNodesId);

		} else {
			int ownPosition = selectedParent.getChildPosition(selectedNode);
			// find node above the own nodes:
			NodeRepresentation directSibling = null;
			for (int i = ownPosition - 1; i >= 0; --i) {
				NodeRepresentation sibling = (NodeRepresentation) selectedParent
						.getChildAt(i);
				if ((!selectedNodes.contains(sibling))
						&& selectedNode.isLeft() == sibling.isLeft()) {
					directSibling = sibling;
					break;
				}
			}
			if (directSibling == null) {
				// start searching for a sibling after the selected block:
				for (int i = ownPosition + 1; i < selectedParent
						.getChildCount(); ++i) {
					NodeRepresentation sibling = (NodeRepresentation) selectedParent
							.getChildAt(i);
					if ((!selectedNodes.contains(sibling))
							&& selectedNode.isLeft() == sibling.isLeft()) {
						directSibling = sibling;
						break;
					}
				}
			}
			if (directSibling != null) {
				// sibling on the same side found:
				Transferable copy = getMindMapController().cut(selectedNodes);
				getMindMapController().paste(copy, directSibling, false,
						directSibling.isLeft());
				select(selectedNodeId, selectedNodesId);
			}
		}
		obtainFocusForSelected();
	}

	private void select(String selectedNodeId, List<String> selectedNodesIds) {
		// get new nodes by object id:
		NodeRepresentation newInstanceOfSelectedNode = getMindMapController()
				.getNodeFromID(selectedNodeId);
		List<NodeRepresentation> newSelecteds = new LinkedList<>();
		for (String nodeId : selectedNodesIds) {
			newSelecteds.add(getMindMapController().getNodeFromID(nodeId));
		}
		getMindMapController().select(newInstanceOfSelectedNode, newSelecteds);
	}

}
