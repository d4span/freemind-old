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
/*$Id: MapMouseMotionListener.java,v 1.3 2003-11-03 10:39:51 sviles Exp $*/

package freemind.controller;

import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.MapView;

import java.awt.Rectangle;
import java.awt.event.*;
import javax.swing.Timer;
import javax.swing.JViewport;
import javax.swing.JPanel;


/**
 * The MouseListener which belongs to MapView
 */
public class MapMouseMotionListener implements MouseMotionListener, MouseListener {

    private final Controller c;

    int originX = -1;
    int originY = -1;

    // |=   oldX >=0 iff we are in the drag

    public MapMouseMotionListener(Controller controller) {
       c = controller; }

    private void handlePopup( MouseEvent e) {
       if (e.isPopupTrigger()) {
          c.getFrame().getFreeMindMenuBar().getMapsPopupMenu().show
             (e.getComponent(),e.getX(),e.getY()); }}

    public void mouseMoved(MouseEvent e) { }
    public void mouseDragged(MouseEvent e) {
       // Always try to get mouse to the original position in the Map.
       if (originX >=0) {
          ((MapView)e.getComponent()).scrollBy(originX - e.getX(), originY - e.getY()); }
       else {
          originX = e.getX();
          originY = e.getY(); }} 

    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered( MouseEvent e ) { }
    public void mouseExited( MouseEvent e ) { }
    public void mousePressed( MouseEvent e ) {
       handlePopup(e);
       e.consume(); }
    public void mouseReleased( MouseEvent e ) {
       originX = -1;
       originY = -1;
       handlePopup(e);
       e.consume(); }
}
    
