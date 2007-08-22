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
/*$Id: SharpBezierEdgeView.java,v 1.5.34.4 2007-08-22 13:23:54 dpolivaev Exp $*/

package freemind.view.mindmapview;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;

/**
 * This class represents a sharp Edge of a MindMap.
 */
public class SharpBezierEdgeView extends EdgeView {

    CubicCurve2D.Float line1 = new CubicCurve2D.Float();
    CubicCurve2D.Float line2 = new CubicCurve2D.Float();
	GeneralPath graph= new GeneralPath();
    Point2D.Float one, two;
	
    private static final float XCTRL = 12;//the distance between endpoint and controlpoint
   
    public SharpBezierEdgeView() {
	super();
    }

    private void update() {
    	float zoom = getMap().getZoom();
    	float xctrlRelative =   XCTRL*zoom ;
    	//YCTRL could be implemented but then we had to check whether target is above or below source.
    	if(getTarget().isLeft()) {
    		one = new Point2D.Float(start.x-xctrlRelative, start.y);
    		two = new Point2D.Float(end.x+xctrlRelative, end.y);
    	} else {
    		one = new Point2D.Float(start.x+xctrlRelative, start.y);
    		two = new Point2D.Float(end.x-xctrlRelative, end.y);
    	}
    	float w = (getWidth()/2+1)*zoom;
    	float w2 = w/2;
    	line1.setCurve(start.x,start.y-w,one.x,one.y-w,two.x,two.y-w2,end.x,end.y);
    	line2.setCurve(end.x,end.y,two.x,two.y+w2,one.x,one.y+w,start.x,start.y+w);
    	graph.reset();
    	graph.append(line1,true);
    	graph.append(line2,true);
    	graph.closePath();
    }

    protected void paint(Graphics2D g) {
    update();
	g.setColor(getColor());
	g.setPaint(getColor());
	g.setStroke(DEF_STROKE);
	g.fill(graph);
	g.draw(graph);
    }

    public Color getColor() {
	return getModel().getColor();
    }
}
