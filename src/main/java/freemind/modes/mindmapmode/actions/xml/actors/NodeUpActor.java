/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
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

package freemind.modes.mindmapmode.actions.xml.actors;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import freemind.controller.actions.generated.instance.MoveNodesAction;
import freemind.controller.actions.generated.instance.NodeListMember;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindMap;
import freemind.modes.NodeRepresentation;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

/**
 * @author foltin
 * @date 08.04.2014
 */
public class NodeUpActor extends XmlActorAdapter {

	/**
	 * @param pMapFeedback
	 */
	public NodeUpActor(ExtendedMapFeedback pMapFeedback) {
		super(pMapFeedback);
	}

	/**
     */
	public void moveNodes(NodeRepresentation selected, List<NodeRepresentation> selecteds, int direction) {
		MoveNodesAction doAction = createMoveNodesAction(selected, selecteds,
				direction);
		MoveNodesAction undoAction = createMoveNodesAction(selected, selecteds,
				-direction);
		execute(new ActionPair(
				doAction, undoAction));
	}

	private void _moveNodes(NodeRepresentation selected, List<NodeRepresentation> selecteds, int direction) {
		Comparator<Integer> comparator = (direction == -1) ? null : new Comparator<Integer>() {
			public int compare(Integer i1, Integer i2) {
				return i2 - i1;
			}
		};
		if (!selected.isRoot()) {
			NodeRepresentation parent = selected.getParentNode();
			// multiple move:
			Vector<NodeRepresentation> sortedChildren = getSortedSiblings(parent);
			TreeSet<Integer> range = new TreeSet<Integer>(comparator);
			for (NodeRepresentation node : selecteds) {
				if (node.getParent() != parent) {
					logger.warning("Not all selected nodes (here: "
							+ node.getText() + ") have the same parent "
							+ parent.getText() + ".");
					return;
				}
				range.add(new Integer(sortedChildren.indexOf(node)));
			}
			// test range for adjacent nodes:
			Integer last = (Integer) range.iterator().next();
			for (Integer newInt : range) {
				if (Math.abs(newInt.intValue() - last.intValue()) > 1) {
					logger.warning("Not adjacent nodes. Skipped. ");
					return;
				}
				last = newInt;
			}
			for (Integer position : range) {
				// from above:
				NodeRepresentation node = (NodeRepresentation) sortedChildren.get(position.intValue());
				moveNodeTo(node, parent, direction);
			}
		}
	}

	/**
	 * The direction is used if side left and right are present. then the next
	 * suitable place on the same side# is searched. if there is no such place,
	 * then the side is changed.
	 * 
	 * @return returns the new index.
	 */
	private int moveNodeTo(NodeRepresentation newChild, NodeRepresentation parent,
                           int direction) {
		MindMap model = getExMapFeedback().getMap();
		int index = model.getIndexOfChild(parent, newChild);
		int newIndex = index;
		int maxIndex = parent.getChildCount();
		Vector<NodeRepresentation> sortedNodesIndices = getSortedSiblings(parent);
		int newPositionInVector = sortedNodesIndices.indexOf(newChild)
				+ direction;
		if (newPositionInVector < 0) {
			newPositionInVector = maxIndex - 1;
		}
		if (newPositionInVector >= maxIndex) {
			newPositionInVector = 0;
		}
		NodeRepresentation destinationNode = (NodeRepresentation) sortedNodesIndices
				.get(newPositionInVector);
		newIndex = model.getIndexOfChild(parent, destinationNode);
		getExMapFeedback().removeNodeFromParent(newChild);
		getExMapFeedback().insertNodeInto(newChild, parent, newIndex);
		getExMapFeedback().nodeChanged(newChild);
		return newIndex;
	}

	/**
	 * Sorts nodes by their left/right status. The left are first.
	 */
	private Vector<NodeRepresentation> getSortedSiblings(NodeRepresentation node) {
		Vector<NodeRepresentation> nodes = new Vector<>();
		for (Iterator<NodeRepresentation> i = node.childrenUnfolded(); i.hasNext();) {
			nodes.add(i.next());
		}
		Collections.sort(nodes, new Comparator<NodeRepresentation>() {

			public int compare(NodeRepresentation n1, NodeRepresentation n2) {
				int b1 = n1.isLeft() ? 0 : 1;
				int b2 = n2.isLeft() ? 0 : 1;
				return b1 - b2;
			}
		});
		// logger.finest("Sorted nodes "+ nodes);
		return nodes;
	}

	public void act(XmlAction action) {
		if (action instanceof MoveNodesAction) {
			MoveNodesAction moveAction = (MoveNodesAction) action;
			NodeRepresentation selected = getNodeFromID(moveAction
					.getNode());
			Vector<NodeRepresentation> selecteds = new Vector<>();
			for (Iterator<NodeListMember> i = moveAction.getListNodeListMemberList().iterator(); i.hasNext();) {
				NodeListMember node = i.next();
				selecteds.add(getNodeFromID(node.getNode()));
			}
			_moveNodes(selected, selecteds, moveAction.getDirection());
		}
	}

	public Class<MoveNodesAction> getDoActionClass() {
		return MoveNodesAction.class;
	}

	private MoveNodesAction createMoveNodesAction(NodeRepresentation selected,
                                                  List<NodeRepresentation> selecteds, int direction) {
		MoveNodesAction moveAction = new MoveNodesAction();
		moveAction.setDirection(direction);
		moveAction.setNode(getNodeID(selected));
		// selectedNodes list
		for (NodeRepresentation node : selecteds) {

			NodeListMember nodeListMember = new NodeListMember();
			nodeListMember.setNode(getNodeID(node));
			moveAction.addNodeListMember(nodeListMember);
		}
		return moveAction;

	}

}
