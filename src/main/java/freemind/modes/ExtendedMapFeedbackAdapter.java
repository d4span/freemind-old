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

import freemind.controller.actions.generated.instance.Pattern;
import freemind.dependencies.view.swing.NodeRepresentation;
import freemind.extensions.HookFactory;
import freemind.main.XMLParseException;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.MindMapController.MindMapControllerPlugin;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActionRegistry;
import freemind.modes.mindmapmode.actions.xml.DefaultActionHandler;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;

import java.awt.Color;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

/**
 * @author foltin
 * @date 16.03.2014
 */
public abstract class ExtendedMapFeedbackAdapter extends MapFeedbackAdapter
		implements ExtendedMapFeedback {


	/**
	 * @author foltin
	 * @date 11.04.2014
	 */
	private final class DummyTransferable implements Transferable {
		@Override
		public boolean isDataFlavorSupported(DataFlavor pFlavor) {
			return false;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] {};
		}

		@Override
		public Object getTransferData(DataFlavor pFlavor)
				throws UnsupportedFlavorException, IOException {
			throw new UnsupportedFlavorException(pFlavor);
		}
	}

	protected ActionRegistry mActionRegistry;
	private NodeRepresentation mSelectedNode;
	protected XmlActorFactory mActorFactory;
	private MindMapHookFactory mNodeHookFactory;

	/**
	 * 
	 */
	public ExtendedMapFeedbackAdapter() {
		super();
		mActionRegistry = new ActionRegistry();
		mActorFactory = new XmlActorFactory(this);
		getActionRegistry().registerHandler(
				new DefaultActionHandler(getActionRegistry()));
	}

	/**
	 * @throws {@link IllegalArgumentException} when node isn't found.
	 */
	@Override
	public NodeAdapter getNodeFromID(String nodeID) {
		NodeAdapter node = (NodeAdapter) getMap().getLinkRegistry()
				.getTargetForId(nodeID);
		if (node == null) {
			throw new IllegalArgumentException("Node belonging to the node id "
					+ nodeID + " not found in map " + getMap().getFile());
		}
		return node;
	}

	@Override
	public String getNodeID(NodeRepresentation selected) {
		return getMap().getLinkRegistry().registerLinkTarget(selected);
	}

	@Override
	public void insertNodeInto(NodeRepresentation pNewNode, NodeRepresentation pParent,
                               int pIndex) {
		getMap().insertNodeInto(pNewNode, pParent, pIndex);
	}

	@Override
	public NodeRepresentation newNode(String pUserObject, MindMap pMap) {
		return null;
	}

	@Override
	public void removeNodeFromParent(NodeRepresentation pSelectedNode) {
		getMap().removeNodeFromParent(pSelectedNode);
	}

	/* (non-Javadoc)
	 * @see freemind.modes.ExtendedMapFeedback#setWaitingCursor(boolean)
	 */
	@Override
	public void setWaitingCursor(boolean pWaiting) {
	}
	
	@Override
	public void nodeStyleChanged(NodeRepresentation node) {
		nodeChanged(node);
		final ListIterator<NodeRepresentation> childrenFolded = node.childrenFolded();
		while (childrenFolded.hasNext()) {
			NodeRepresentation child = (NodeRepresentation) childrenFolded.next();
			if (!(child.hasStyle() && child.getEdge().hasStyle())) {
				nodeStyleChanged(child);
			}
		}
	}

	@Override
	public ActionRegistry getActionRegistry() {
		return mActionRegistry;
	}

	@Override
	public boolean doTransaction(String pName, ActionPair pPair) {
		return mActionRegistry.doTransaction(pName, pPair);
	}

	@Override
	public NodeRepresentation getSelected() {
		return mSelectedNode;
	}

	@Override
	public XmlActorFactory getActorFactory() {
		return mActorFactory;
	}

	public Transferable copy(NodeRepresentation node, boolean saveInvisible) {
		return new DummyTransferable();
	}
	
	/* (non-Javadoc)
	 * @see freemind.modes.ExtendedMapFeedback#copy(java.util.List, boolean)
	 */
	@Override
	public Transferable copy(List<NodeRepresentation> pNodeList, boolean pSaveInvisible) {
		return new DummyTransferable();
	}

	@Override
	public HookFactory getHookFactory() {
		// lazy creation.
		if (mNodeHookFactory == null) {
			mNodeHookFactory = new MindMapHookFactory();
			// initialization
			mNodeHookFactory.getPossibleNodeHooks();
		}
		return mNodeHookFactory;
	}

	@Override
	public void select(NodeRepresentation pFocussed, List<NodeRepresentation> pSelecteds) {
		mSelectedNode = pFocussed;
	}

	/* (non-Javadoc)
	 * @see freemind.modes.ExtendedMapFeedback#load(java.io.File)
	 */
	@Override
	public MapFeedback load(File pFile) throws FileNotFoundException,
			IOException, XMLParseException, URISyntaxException {
		return this;
	}

	@Override
	public void close(boolean pForce) {
		
	}

	@Override
	public void edit(KeyEvent pE, boolean pAddNew, boolean pEditLong) {
	}

	@Override
	public void setNodeText(NodeRepresentation pSelected, String pNewText) {
		getActorFactory().getEditActor().setNodeText(pSelected, pNewText);
	}

	@Override
	public void setNoteText(NodeRepresentation pSelected, String pNewText) {
		getActorFactory().getChangeNoteTextActor().setNoteText(pSelected, pNewText);
	}

	@Override
	public NodeRepresentation addNewNode(NodeRepresentation pParent, int pIndex,
                                         boolean pNewNodeIsLeft) {
		return mActorFactory.getNewChildActor().addNewNode(pParent, pIndex, pNewNodeIsLeft);
	}

	@Override
	public void deleteNode(NodeRepresentation pSelectedNode) {
		mActorFactory.getDeleteChildActor().deleteNode(pSelectedNode);
	}

	@Override
	public Transferable cut() {
		return cut(getViewAbstraction().getSelectedNodesSortedByY());
	}

	@Override
	public Transferable cut(List<NodeRepresentation> pNodeList) {
		return getActorFactory().getCutActor().cut(pNodeList);
	}

	@Override
	public void moveNodes(NodeRepresentation pSelected, List<NodeRepresentation> pSelecteds, int pDirection) {
		getActorFactory().getNodeUpActor().moveNodes(pSelected, pSelecteds, pDirection);
	}

	@Override
	public void toggleFolded() {
		getActorFactory().getToggleFoldedActor().toggleFolded(getViewAbstraction().getSelecteds().listIterator());		
	}

	@Override
	public void setFolded(NodeRepresentation pModel, boolean pFold) {
		getActorFactory().getToggleFoldedActor().setFolded(pModel, pFold);
		
	}

	@Override
	public void setBold(NodeRepresentation pNode, boolean pBolded) {
		mActorFactory.getBoldActor().setBold(pNode, pBolded);
	}

	@Override
	public void setStrikethrough(NodeRepresentation pNode, boolean pStrikethroughed) {
		mActorFactory.getStrikethroughActor().setStrikethrough(pNode, pStrikethroughed);
	}
	
	@Override
	public void setItalic(NodeRepresentation pNode, boolean pIsItalic) {
		mActorFactory.getItalicActor().setItalic(pNode, pIsItalic);
	}

	@Override
	public void setNodeColor(NodeRepresentation pNode, Color pColor) {
		getActorFactory().getNodeColorActor().setNodeColor(pNode, pColor);
	}

	@Override
	public void setNodeBackgroundColor(NodeRepresentation pNode, Color pColor) {
		getActorFactory().getNodeBackgroundColorActor().setNodeBackgroundColor(pNode, pColor);
	}

	@Override
	public void blendNodeColor(NodeRepresentation pNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFontFamily(NodeRepresentation pNode, String pFontFamily) {
		getActorFactory().getFontFamilyActor().setFontFamily(pNode, pFontFamily);
	}

	@Override
	public void setFontSize(NodeRepresentation pNode, String pFontSizeValue) {
		getActorFactory().getFontSizeActor().setFontSize(pNode, pFontSizeValue);
		
	}

	@Override
	public void addIcon(NodeRepresentation pNode, MindIcon pIcon) {
		mActorFactory.getAddIconActor().addIcon(pNode, pIcon);		
	}

	@Override
	public int removeLastIcon(NodeRepresentation pNode) {
		return mActorFactory.getRemoveIconActor().removeLastIcon(pNode);
	}

	@Override
	public void removeAllIcons(NodeRepresentation pNode) {
		mActorFactory.getRemoveAllIconsActor().removeAllIcons(pNode);
	}

	@Override
	public void applyPattern(NodeRepresentation pNode, Pattern pPattern) {
		StylePatternFactory.applyPattern(pNode, pPattern, Collections.<Pattern>emptyList(), Collections.<MindMapControllerPlugin>emptySet(), this);
	}

	@Override
	public void setNodeStyle(NodeRepresentation pNode, String pStyle) {
		getActorFactory().getNodeStyleActor().setStyle(pNode, pStyle);
	}

	@Override
	public void setEdgeColor(NodeRepresentation pNode, Color pColor) {
		getActorFactory().getEdgeColorActor().setEdgeColor(pNode, pColor);		
	}

	@Override
	public void setEdgeWidth(NodeRepresentation pNode, int pWidth) {
		getActorFactory().getEdgeWidthActor().setEdgeWidth(pNode, pWidth);
	}

	@Override
	public void setEdgeStyle(NodeRepresentation pNode, String pStyle) {
		getActorFactory().getEdgeStyleActor().setEdgeStyle(pNode, pStyle);
	}

	@Override
	public void setCloud(NodeRepresentation pNode, boolean pEnable) {
		mActorFactory.getCloudActor().setCloud(pNode, pEnable);
	}

	@Override
	public void setCloudColor(NodeRepresentation pNode, Color pColor) {
		mActorFactory.getCloudColorActor().setCloudColor(pNode, pColor);
	}

	@Override
	public void addLink(NodeRepresentation pSource, NodeRepresentation pTarget) {
		getActorFactory().getAddArrowLinkActor().addLink(pSource, pTarget);
	}

	@Override
	public void removeReference(MindMapLink pArrowLink) {
		getActorFactory().getRemoveArrowLinkActor().removeReference(pArrowLink);
	}

	@Override
	public void changeArrowsOfArrowLink(MindMapArrowLink pArrowLink,
			boolean pHasStartArrow, boolean pHasEndArrow) {
		getActorFactory().getChangeArrowsInArrowLinkActor().changeArrowsOfArrowLink(pArrowLink,
				pHasStartArrow, pHasEndArrow);
	}

	@Override
	public void setArrowLinkColor(MindMapLink pArrowLink, Color pColor) {
		getActorFactory().getColorArrowLinkActor().setArrowLinkColor(pArrowLink, pColor);
	}

	@Override
	public void setArrowLinkEndPoints(MindMapArrowLink pLink,
			Point pStartPoint, Point pEndPoint) {
		getActorFactory().getChangeArrowLinkEndPointsActor().setArrowLinkEndPoints(pLink, pStartPoint,
				pEndPoint);
	}

	@Override
	public void setLink(NodeRepresentation pNode, String pLink) {
		getActorFactory().getSetLinkActor().setLink(pNode, pLink);
	}

	@Override
	public boolean paste(Transferable pT, NodeRepresentation pTarget,
			boolean pAsSibling, boolean pIsLeft) {
		return mActorFactory.getPasteActor().paste(pT, pTarget, pAsSibling, pIsLeft);
	}

	@Override
	public void addHook(NodeRepresentation pFocussed, List<NodeRepresentation> pSelecteds,
                        String pHookName, Properties pHookProperties) {
		getActorFactory().getAddHookActor().addHook(pFocussed, pSelecteds, pHookName, pHookProperties);
	}

	@Override
	public void removeHook(NodeRepresentation pFocussed, List<NodeRepresentation> pSelecteds,
                           String pHookName) {
		getActorFactory().getAddHookActor().removeHook(pFocussed, pSelecteds, pHookName);
	}

	@Override
	public void moveNodePosition(NodeRepresentation pNode, int pVGap, int pHGap,
                                 int pShiftY) {
		getActorFactory().getMoveNodeActor().moveNodeTo(pNode, pVGap, pHGap, pShiftY);

	}

	@Override
	public void setAttribute(NodeRepresentation pNode, int pPosition,
                             Attribute pAttribute) {
		getActorFactory().getSetAttributeActor().setAttribute(pNode, pPosition, pAttribute);
		
	}

	@Override
	public void insertAttribute(NodeRepresentation pNode, int pPosition,
                                Attribute pAttribute) {
		getActorFactory().getInsertAttributeActor().insertAttribute(pNode, pPosition, pAttribute);
		
	}

	@Override
	public int addAttribute(NodeRepresentation pNode, Attribute pAttribute) {
		return getActorFactory().getAddAttributeActor().addAttribute(pNode, pAttribute);
	}

	@Override
	public void removeAttribute(NodeRepresentation pNode, int pPosition) {
		getActorFactory().getRemoveAttributeActor().removeAttribute(pNode, pPosition);
	}

}
