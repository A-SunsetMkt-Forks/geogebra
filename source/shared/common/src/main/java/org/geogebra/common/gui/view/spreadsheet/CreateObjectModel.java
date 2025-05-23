package org.geogebra.common.gui.view.spreadsheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geogebra.common.annotation.MissingDoc;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.algos.AlgoPolyLine;
import org.geogebra.common.kernel.geos.GeoList;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.main.App;
import org.geogebra.common.main.Localization;
import org.geogebra.common.spreadsheet.core.TabularRange;
import org.geogebra.common.util.IndexHTMLBuilder;
import org.geogebra.common.util.debug.Log;

/**
 * Dialog to create GeoElements (lists, matrices, tabletext, etc.) from
 * spreadsheet cell selections
 * 
 * @author G. Sturr
 * 
 */
public class CreateObjectModel {
	private CellRangeProcessor cp;
	private ArrayList<TabularRange> selectedRanges;

	public static final int TYPE_LIST = 0;
	public static final int TYPE_MATRIX = 2;
	public static final int TYPE_LISTOFPOINTS = 1;
	public static final int TYPE_TABLETEXT = 3;
	public static final int TYPE_POLYLINE = 4;
	private int objectType = TYPE_LIST;

	public static final int OPTION_ORDER = 0;
	public static final int OPTION_XY = 1;
	public static final int OPTION_TRANSPOSE = 2;

	private GeoElementND newGeo;
	private boolean keepNewGeo = false;
	private App app;
	private ICreateObjectListener listener;
	private Localization loc;

	/**
	 * UI delegate for the model.
	 */
	public interface ICreateObjectListener {
		/**
		 * @param name name
		 */
		void setName(String name);

		@MissingDoc
		boolean isVisible();

		/**
		 * Show or hide the dialog.
		 * @param value visibility
		 */
		void setVisible(boolean value);

		/**
		 * Show or hide sort options.
		 * @param isVisible visibility
		 */
		void setSortVisible(boolean isVisible);

		@MissingDoc
		boolean isCopiedByValue();

		@MissingDoc
		boolean isScannedByColumn();

		@MissingDoc
		boolean isLeftToRight();

		@MissingDoc
		boolean isTranspose();

		/**
		 * Update the preview.
		 * @param latexStr latex formula
		 * @param isLatexDrawable whether to use LaTeX rendering
		 */
		void updatePreview(String latexStr, boolean isLatexDrawable);
	}

	/**
	 * @param app
	 *            application
	 * @param objectType
	 *            object type, see TYPE_ constants
	 * @param listener
	 *            listener
	 */
	public CreateObjectModel(App app, int objectType,
			ICreateObjectListener listener) {
		this.app = app;
		this.loc = app.getLocalization();
		this.objectType = objectType;
		this.listener = listener;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return loc.getMenu(getTitle(getObjectType()));
	}

	/**
	 * @param type dialog type
	 * @return translation key for title
	 */
	public static String getTitle(int type) {
		switch (type) {
			default:
			return null;
			case TYPE_LIST: return "List.Tool";

			case TYPE_LISTOFPOINTS: return "ListOfPoints";

			case TYPE_TABLETEXT: return "Table.Tool";

			case TYPE_POLYLINE: return "CreatePolyLine";

			case TYPE_MATRIX: return "Matrix.Tool";
		}
	}

	/**
	 * @return localized object types that can be created
	 */
	public List<String> getObjectTypeNames() {
		return Arrays.asList(loc.getMenu("List.Create"), loc.getMenu("Matrix"),
				loc.getMenu("ListOfPoints"), loc.getMenu("Table"),
				loc.getMenu("PolyLine"));
	}

	/**
	 * Update UI
	 */
	public void update() {
		if (newGeo == null) {
			listener.setName("");
		} else {
			listener.setName(newGeo.getLabel(StringTemplate.defaultTemplate));
		}

		listener.setSortVisible(getObjectType() == TYPE_POLYLINE);
	}

	/**
	 * Remove all created objects and close.
	 */
	public void cancel() {
		if (newGeo != null) {
			newGeo.remove();
			if (newGeo instanceof GeoList
					&& getObjectType() == TYPE_LISTOFPOINTS) {
				for (int i = 0; i < ((GeoList) newGeo).size(); i++) {
					((GeoList) newGeo).get(i).remove();
				}
			}
		}
		listener.setVisible(false);
	}

	/**
	 * Mark as success and close
	 */
	public void ok() {
		keepNewGeo = true;
		listener.setVisible(false);
	}

	private void addNewGeoToConstruction() {

		if (getObjectType() == TYPE_LISTOFPOINTS
				|| getObjectType() == TYPE_POLYLINE) {
			app.getKernel().getConstruction()
					.addToConstructionList(newGeo.getParentAlgorithm(), true);
		}

		newGeo.setEuclidianVisible(true);
		if (!newGeo.isGeoText()) {
			newGeo.setAuxiliaryObject(false);
		}

		if (getObjectType() == TYPE_LISTOFPOINTS) {
			GeoList gl = (GeoList) newGeo;
			for (int i = 0; i < gl.size(); i++) {
				gl.get(i).setEuclidianVisible(true);
				gl.get(i).setAuxiliaryObject(false);
			}
		}

		if (getObjectType() == TYPE_POLYLINE) {
			GeoPointND[] pts = ((AlgoPolyLine) newGeo.getParentAlgorithm())
					.getPoints();
			for (int i = 0; i < pts.length; i++) {
				pts[i].setEuclidianVisible(true);
				pts[i].setAuxiliaryObject(false);
				pts[i].updateRepaint();
			}
		}

		newGeo.updateRepaint();
		app.storeUndoInfo();
	}

	/**
	 * @param name
	 *            label
	 */
	public void createNewGeo(String name) {
		boolean nullGeo = newGeo == null;

		if (!nullGeo) {
			if (getObjectType() == TYPE_LISTOFPOINTS) {
				GeoList gl = (GeoList) newGeo;
				for (int i = 0; i < gl.size(); i++) {
					gl.get(i).remove();
				}
			}

			if (getObjectType() == TYPE_POLYLINE) {
				GeoPointND[] pts = ((AlgoPolyLine) newGeo.getParentAlgorithm())
						.getPoints();
				for (int i = 0; i < pts.length; i++) {
					pts[i].remove();
				}
			}
			newGeo.remove();
		}

		int column1 = selectedRanges.get(0).getMinColumn();
		int column2 = selectedRanges.get(0).getMaxColumn();
		int row1 = selectedRanges.get(0).getMinRow();
		int row2 = selectedRanges.get(0).getMaxRow();

		boolean copyByValue = listener.isCopiedByValue();
		boolean scanByColumn = listener.isScannedByColumn();
		boolean leftToRight = listener.isLeftToRight();
		boolean transpose = listener.isTranspose();
		boolean doCreateFreePoints = true;
		boolean doStoreUndo = true;

		try {
			switch (getObjectType()) {

			default:
				// do nothing
				break;
			case TYPE_LIST:
				newGeo = cp.createList(getSelectedRanges(), scanByColumn,
						copyByValue);
				break;

			case TYPE_LISTOFPOINTS:
				newGeo = cp.createPointGeoList(getSelectedRanges(),
						copyByValue, leftToRight, doStoreUndo,
						doCreateFreePoints);
				newGeo.setLabel(null);
				for (int i = 0; i < ((GeoList) newGeo).size(); i++) {
					((GeoList) newGeo).get(i).setAuxiliaryObject(true);
					((GeoList) newGeo).get(i).setEuclidianVisible(false);
				}
				newGeo.updateRepaint();
				break;

			case TYPE_MATRIX:
				newGeo = cp.createMatrix(column1, column2, row1, row2,
						copyByValue, transpose);
				break;

			case TYPE_TABLETEXT:
				newGeo = cp.createTableText(column1, column2, row1, row2,
						copyByValue, transpose);
				newGeo.setEuclidianVisible(false);
				newGeo.updateRepaint();
				break;

			case TYPE_POLYLINE:
				newGeo = cp.createPolyLine(getSelectedRanges(), copyByValue,
						leftToRight);
				newGeo.setLabel(null);
				GeoPointND[] pts = ((AlgoPolyLine) newGeo.getParentAlgorithm())
						.getPoints();
				for (int i = 0; i < pts.length; i++) {
					pts[i].setAuxiliaryObject(true);
					pts[i].setEuclidianVisible(false);
					pts[i].updateRepaint();
				}
				newGeo.updateRepaint();
				break;

			}

			// String latexStr = newGeo.getLaTeXAlgebraDescription(true);

			listener.updatePreview(
					newGeo.getFormulaString(StringTemplate.latexTemplate, true),
					newGeo.isLaTeXDrawableGeo());

			if (!nullGeo) {
				newGeo.setLabel(name);
				newGeo.setAuxiliaryObject(true);
				newGeo.setEuclidianVisible(false);
			}

		} catch (Exception e) {
			Log.debug(e);
		}

	}

	/**
	 * Remove temporary geo if not in construction
	 */
	public void cleanUp() {
		if (newGeo == null) {
			return;
		}
		if (keepNewGeo) {
			addNewGeoToConstruction();
		} else {
			newGeo.remove();
		}
	}

	/**
	 * Gets non-latex description of the created element.
	 * @param sb builder
	 * @return text
	 */
	public String getNonLatexText(IndexHTMLBuilder sb) {
		return newGeo.toGeoElement().getAlgebraDescriptionTextOrHTMLDefault(sb);
	}

	public CellRangeProcessor getCellRangeProcessor() {
		return cp;
	}

	public void setCellRangeProcessor(CellRangeProcessor cp) {
		this.cp = cp;
	}

	public ArrayList<TabularRange> getSelectedRanges() {
		return selectedRanges;
	}

	public void setSelectedRanges(ArrayList<TabularRange> selectedRanges) {
		this.selectedRanges = selectedRanges;
	}

	public int getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType
	 *            object type
	 */
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}

	/**
	 * Set type to list.
	 */
	public void setListType() {
		objectType = TYPE_LIST;
	}

	/**
	 * @return index of options tab
	 */
	public int getOptionType() {
		int idx = 0;

		switch (getObjectType()) {
		default:
		case CreateObjectModel.TYPE_LIST:
			idx = OPTION_ORDER;
			break;
		case CreateObjectModel.TYPE_POLYLINE:
		case CreateObjectModel.TYPE_LISTOFPOINTS:
			idx = OPTION_XY;
			break;
		case CreateObjectModel.TYPE_MATRIX:
		case CreateObjectModel.TYPE_TABLETEXT:
			idx = OPTION_TRANSPOSE;
			break;
		}

		return idx;
	}

	public GeoElementND getGeo() {
		return newGeo;
	}

}
