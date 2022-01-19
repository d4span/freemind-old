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
package freemind.modes.mindmapmode.actions.xml.actors

import freemind.modes.ExtendedMapFeedback
import freemind.modes.mindmapmode.actions.NodeActorXml

/**
 * @author foltin
 * @date 16.03.2014
 */
abstract class NodeXmlActorAdapter
/**
 * @param pMapFeedback
 */
(pMapFeedback: ExtendedMapFeedback?) : XmlActorAdapter(pMapFeedback), NodeActorXml
