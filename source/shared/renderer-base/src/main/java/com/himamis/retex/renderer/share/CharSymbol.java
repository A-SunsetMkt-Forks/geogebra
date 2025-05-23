/* CharSymbol.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */

package com.himamis.retex.renderer.share;

/**
 * An common superclass for atoms that represent one single character and access
 * the font information.
 */
public abstract class CharSymbol extends Atom {

	/**
	 * RowAtom will mark certain CharSymbol atoms as text symbols. Msubsup will use
	 * this property for a certain spacing rule.
	 */
	protected boolean textSymbol = false;
	protected boolean italic = true;

	/**
	 * Mark as text symbol (used by Dummy)
	 */
	public void markAsTextSymbol() {
		textSymbol = true;
	}

	/**
	 * Remove the mark so the atom remains unchanged (used by Dummy)
	 */
	public void removeMark() {
		textSymbol = false;
	}

	/**
	 * Tests if this atom is marked as a text symbol (used by Msubsup)
	 *
	 * @return whether this CharSymbol is marked as a text symbol
	 */
	public boolean isMarkedAsTextSymbol() {
		return textSymbol;
	}

	/**
	 * Get the CharFont-object that uniquely identifies the character that is
	 * represented by this atom.
	 *
	 * @param tf
	 *            the TeXFont containing all font related information
	 * @return a CharFont
	 */
	public abstract CharFont getCharFont(TeXFont tf);

	/**
	 * Get the Char-object that uniquely identifies the character that is
	 * represented by this atom.
	 *
	 * @param env
	 *            the TeXEnvironment
	 * @return a Char
	 */
	public abstract Char getChar(TeXEnvironment env);

	@Override
	public double getItalic(TeXEnvironment env) {
		return getChar(env).getItalic();
	}

	@Override
	public double getXHeight(TeXEnvironment env) {
		final TeXFont tf = env.getTeXFont();
		return tf.getXHeight(env.getStyle(), getCharFont(tf).getFontInfo());
	}

	public boolean isCharSymbol() {
		return true;
	}

	@Override
	public boolean mustAddItalicCorrection() {
		return italic;
	}

	@Override
	public boolean setAddItalicCorrection(boolean b) {
		final boolean bb = italic;
		italic = b;
		return bb;
	}
}
