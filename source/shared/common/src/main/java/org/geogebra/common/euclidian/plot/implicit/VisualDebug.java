package org.geogebra.common.euclidian.plot.implicit;

import org.geogebra.common.awt.GGraphics2D;

/**
 * Visual debug aid for Bernstein poly plotter.
 */
public interface VisualDebug {
	/**
	 * Draw this in graphics.
	 * @param g2 graphics
	 */
	void draw(GGraphics2D g2);
}
