package org.geogebra.common.euclidian;

import org.geogebra.common.awt.GGraphics2D;
import org.geogebra.common.awt.GPaint;
import org.geogebra.common.main.App;

/**
 * Pattern for SVG painting
 *
 */
public class VectorPatternPaint implements GPaint {

	public final App.ExportType type;
	private final GGraphics2D patternGraphics;

	private final int startX;
	private final int startY;
	private final int width;
	private final int height;

	/**
	 * @param path0 fill path
	 * @param width width
	 * @param height height
	 * @param startX horizontal position of sub-image
	 * @param startY vertical position of sub-image
	 */
	public VectorPatternPaint(GGraphics2D path0, int width, int height, int startX, int startY,
			App.ExportType type) {
		this.patternGraphics = path0;
		this.width = width;
		this.height = height;
		this.startX = startX;
		this.startY = startY;
		this.type = type;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return horizontal offset of the view box
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * @return vertical offset of the view box
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * @return graphics object with drawing of the pattern
	 */
	public GGraphics2D getPatternGraphics() {
		return patternGraphics;
	}

}
