﻿/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2005  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 * Created on 25.04.2005
 */
import visorFreeMind.*;
/**
* Take into account all the KEY and Mouse events.
*/
class visorFreeMind.KeyControler{

	private var browser:Browser;
	private var node_selectable:Node=null;

	//All the key  inputs are controles from here
	function KeyControler(browser:Browser){
		this.browser=browser;
		Key.addListener(this);
	}

	public function onKeyUp(){
			if(Key.getCode()==Key.SHIFT && node_selectable!=null){
				node_selectable.ref_mc.node_txt.node_txt.selectable=false;
				node_selectable.activateEvents();
				node_selectable=null;
				browser.floor.makeDraggable();
			}
	}

	public function onKeyDown(){
			if(Key.isDown(Key.CONTROL) and Key.getCode() == 17 ){ //c
				if(Node.currentOver!=null)
					System.setClipboard(Node.currentOver.text);
			}if(Key.isDown(Key.CONTROL) and Key.getCode() == 187){ //+
				this.browser.mc_floor._xscale+=20;
				this.browser.mc_floor._yscale+=20;
			}else if(Key.isDown(Key.CONTROL) and Key.getCode() == 189){ //-
				this.browser.mc_floor._xscale-=20;
				this.browser.mc_floor._yscale-=20;
			}
			/// HISTORY
			else if(Key.isDown(Key.CONTROL) && Key.isDown(Key.LEFT) && browser.posXmls>0){
				browser.posXmls--;
				browser.fileName=browser.visitedMM[browser.posXmls];
				browser.genMindMap(3);
			}else if(Key.isDown(Key.CONTROL) && Key.isDown(Key.RIGHT)){
				if(browser.posXmls<(browser.visitedMM.length-1)){
				browser.posXmls++;
				browser.fileName=browser.visitedMM[browser.posXmls];
				browser.genMindMap(3);
				}
			}else if(Key.isDown(Key.LEFT)){
				this.browser.mc_floor._x-=10;
			}else if(Key.isDown(Key.RIGHT)){
				this.browser.mc_floor._x+=10;
			}else if(Key.isDown(Key.UP)){
				this.browser.mc_floor._y-=10;
			}else if(Key.isDown(Key.DOWN)){
				this.browser.mc_floor._y+=10;
			}else if(Key.isDown(Key.SHIFT) && Node.currentOver!=null && node_selectable==null){
				node_selectable=Node.currentOver;
				node_selectable.deactivateEvents();
				browser.floor.notDraggable();
				Logger.trace("txt:"+Node.currentOver.text);
				node_selectable.ref_mc.node_txt.node_txt.selectable=true;
			}
		}
}
