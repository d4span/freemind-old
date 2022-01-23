/*FreeMind - A Program for creating and viewing Mindmaps
*Copyright (C) 2000-2014 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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

package freemind.modes;

import freemind.dependencies.view.swing.NodeRepresentation;
import freemind.extensions.HookFactory;
import freemind.main.XMLParseException;
import freemind.modes.mindmapmode.actions.MindMapActions;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActionRegistry;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * MapFeedback extended by the xml based node change management.
 * 
 * @author foltin
 * @date 16.03.2014
 */
public interface ExtendedMapFeedback extends MapFeedback, MindMapActions {
	/**
	 * @return the action factory that contains the actors definitions.
	 */
	ActionRegistry getActionRegistry();

	boolean doTransaction(String pName, ActionPair pPair);

	
	/**
	 * Given a node identifier, this method returns the corresponding node.
	 * 
	 * @throws IllegalArgumentException
	 *             if the id is unknown.
	 */
	NodeAdapter getNodeFromID(String nodeID);

	/**
	 * Calling this method the map-unique identifier of the node is returned
	 * (and created before, if not present)
	 */
	String getNodeID(NodeRepresentation selected);

	/**
	 * @return
	 */
	NodeRepresentation getSelected();
	
	void select(NodeRepresentation pFocussed, List<NodeRepresentation> pSelecteds);

	/**
	 * @param pNewNode
	 * @param pParent
	 * @param pIndex
	 */
	void insertNodeInto(NodeRepresentation pNewNode, NodeRepresentation pParent, int pIndex);

	/**
	 * @param pUserObject is the string/html of the new node
	 * @param pMap
	 * @return the new node.
	 */
	NodeRepresentation newNode(Object pUserObject, MindMap pMap);

	/**
	 * @param pSelectedNode
	 */
	void removeNodeFromParent(NodeRepresentation pSelectedNode);

	/**
	 * @return the factory used to create all xml actors.
	 */
	XmlActorFactory getActorFactory();

	Transferable copy(NodeRepresentation node, boolean saveInvisible);

	/**
	 * @param pNodeList
	 * @param pSaveInvisible
	 * @return a MindMapNode list as Transferable (special FM flavor).
	 */
	Transferable copy(List<NodeRepresentation> pNodeList, boolean pSaveInvisible);

	void setWaitingCursor(boolean waiting);
	
	void nodeStyleChanged(NodeRepresentation pNode);
	
	HookFactory getHookFactory();

	/**
	 * @param pFile loads a file into a new map.
	 */
	MapFeedback load(File pFile) throws FileNotFoundException,
	IOException, XMLParseException, URISyntaxException;

	/**
	 * Closes the actual map.
	 * 
	 * @param pForce
	 *            true= without save.
	 */
	void close(boolean pForce);


	
}
