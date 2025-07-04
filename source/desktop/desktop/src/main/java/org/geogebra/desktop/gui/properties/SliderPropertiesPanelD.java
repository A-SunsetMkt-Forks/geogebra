package org.geogebra.desktop.gui.properties;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.gui.SetLabels;
import org.geogebra.common.gui.UpdateFonts;
import org.geogebra.common.gui.dialog.options.model.AnimationStepModel;
import org.geogebra.common.gui.dialog.options.model.SliderModel;
import org.geogebra.common.gui.dialog.options.model.SliderModel.ISliderOptionsListener;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.MyDouble;
import org.geogebra.common.kernel.arithmetic.NumberValue;
import org.geogebra.common.main.Localization;
import org.geogebra.common.util.DoubleUtil;
import org.geogebra.desktop.awt.GColorD;
import org.geogebra.desktop.gui.AngleTextField;
import org.geogebra.desktop.gui.GuiManagerD;
import org.geogebra.desktop.gui.dialog.PropertiesPanelD;
import org.geogebra.desktop.gui.inputfield.MyTextFieldD;
import org.geogebra.desktop.gui.util.LayoutUtil;
import org.geogebra.desktop.gui.util.SliderUtil;
import org.geogebra.desktop.main.AppD;

/**
 * panel for numeric slider
 * 
 * @author Markus Hohenwarter
 */
public class SliderPropertiesPanelD extends JPanel
		implements ActionListener, FocusListener, UpdateablePropertiesPanel,
		SetLabels, UpdateFonts, ISliderOptionsListener, ChangeListener {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private final SliderModel model;
	private final AngleTextField tfMin;
	private final AngleTextField tfMax;
	private final JTextField tfWidth;
	private final JTextField tfBlobSize;
	private final JTextField tfLineThickness;
	private final JTextField[] tfields;
	private final JLabel[] tLabels;
	private final JLabel lbWidthUnit;
	private final JCheckBox cbSliderFixed;
	private final JCheckBox cbRandom;
	private final JComboBox<String> coSliderHorizontal;
	private final JLabel lblLineColor;
	private final JButton btnLineColor;
	private final JLabel lblBlobColor;
	private final JButton btnBlobColor;
	private final JSlider sliderLineOpacity;
	private final AppD app;
	private final AnimationStepPanel stepPanel;
	private final AnimationSpeedPanel speedPanel;
	private final Kernel kernel;
	private final PropertiesPanelD propPanel;
	private final JPanel intervalPanel;
	private final JPanel sliderPanel;
	private final JPanel animationPanel;
	private final JPanel pointSliderStylePanel;
	private final JPanel lineSliderStylePanel;
	private final boolean useTabbedPane;
	private boolean actionPerforming;

	private final Localization loc;

	/**
	 * @param app app
	 * @param propPanel properties panel
	 * @param useTabbedPane whether to use tabs
	 * @param includeRandom whether to add checkbox for random
	 */
	public SliderPropertiesPanelD(AppD app, PropertiesPanelD propPanel,
			boolean useTabbedPane, boolean includeRandom) {
		this.app = app;
		this.loc = app.getLocalization();
		kernel = app.getKernel();
		model = new SliderModel(app, this);
		this.propPanel = propPanel;
		this.useTabbedPane = useTabbedPane;
		model.setIncludeRandom(includeRandom);

		intervalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		sliderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		animationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		pointSliderStylePanel = new JPanel(
				new GridLayout(2, 1));
		lineSliderStylePanel = new JPanel(new GridLayout(4, 1));
		sliderLineOpacity = new JSlider(0, 100);
		sliderLineOpacity.setMajorTickSpacing(25);
		sliderLineOpacity.setMinorTickSpacing(5);
		sliderLineOpacity.setValue(40);
		sliderLineOpacity.setPaintTicks(true);
		sliderLineOpacity.setPaintLabels(true);
		sliderLineOpacity.setSnapToTicks(true);
		SliderUtil.addValueChangeListener(sliderLineOpacity, val -> model.storeUndoInfo());
		sliderLineOpacity.addChangeListener(this);

		cbSliderFixed = new JCheckBox("", true);
		cbSliderFixed.addActionListener(this);
		sliderPanel.add(cbSliderFixed);

		cbRandom = new JCheckBox();
		cbRandom.addActionListener(this);
		sliderPanel.add(cbRandom);

		coSliderHorizontal = new JComboBox<>();
		coSliderHorizontal.addActionListener(this);
		sliderPanel.add(coSliderHorizontal);

		tfMin = new AngleTextField(6, app);
		tfMax = new AngleTextField(6, app);
		tfWidth = new MyTextFieldD(app, 4);
		tfBlobSize = new MyTextFieldD(app, 4);
		tfLineThickness = new MyTextFieldD(app, 4);
		lbWidthUnit = new JLabel("");
		tfields = new MyTextFieldD[5];
		tLabels = new JLabel[5];
		tfields[0] = tfMin;
		tfields[1] = tfMax;
		tfields[2] = tfWidth;
		tfields[3] = tfBlobSize;
		tfields[4] = tfLineThickness;
		JLabel lblBlobSizeUnit = new JLabel("px");
		JLabel lblLineThicknessUnit = new JLabel("px");

		int numPairs = tLabels.length;

		// add textfields
		for (int i = 0; i < numPairs; i++) {
			JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
			tLabels[i] = new JLabel("", SwingConstants.LEADING);
			p.add(tLabels[i]);
			JTextField textField = tfields[i];
			tLabels[i].setLabelFor(textField);
			textField.addActionListener(this);
			textField.addFocusListener(this);
			p.add(textField);
			switch (i) {
			case 2:
				p.add(lbWidthUnit);
				break;
			case 3:
				p.add(lblBlobSizeUnit);
				break;
			case 4:
				p.add(lblLineThicknessUnit);
				break;
			default:
				break;
			}
			p.setAlignmentX(Component.LEFT_ALIGNMENT);

			if (i < 2) {
				intervalPanel.add(p);
			} else if (i == 3) {
				pointSliderStylePanel.add(p);
			} else {
				lineSliderStylePanel.add(p);
			}

		}
		lblBlobColor = new JLabel(loc.getMenu("Color") + ":");
		btnBlobColor = new JButton("\u2588");
		lblBlobColor.setLabelFor(btnBlobColor);
		btnBlobColor.addActionListener(this);
		lblLineColor = new JLabel(loc.getMenu("Color") + ":");
		btnLineColor = new JButton("\u2588");
		lblLineColor.setLabelFor(btnLineColor);
		btnLineColor.addActionListener(this);
		btnLineColor.setForeground(
				GColorD.getAwtColor(getColorWithOpacity(model.getLineColor())));
		pointSliderStylePanel
				.add(LayoutUtil.flowPanel(lblBlobColor, btnBlobColor));
		lineSliderStylePanel
				.add(LayoutUtil.flowPanel(lblLineColor, btnLineColor));
		JLabel lblLineOpacity = new JLabel(loc.getMenu("LineOpacity") + ":");
		JPanel opacityPanel = LayoutUtil.flowPanel(lblLineOpacity, sliderLineOpacity);
		lineSliderStylePanel.add(opacityPanel);
		// add increment to intervalPanel
		AnimationStepModel model = new AnimationStepModel(app);
		stepPanel = new AnimationStepPanel(model, app);
		model.setPartOfSlider(true);
		intervalPanel.add(stepPanel);
		speedPanel = new AnimationSpeedPanel(app);
		speedPanel.setPartOfSliderPanel();
		animationPanel.add(speedPanel);
		setLabels();
	}

	private void initPanels() {
		removeAll();
		// put together interval, slider options, animation panels
		if (useTabbedPane) {
			setLayout(new FlowLayout());
			JTabbedPane tabbedPane = new JTabbedPane();
			tabbedPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			tabbedPane.addTab(loc.getMenu("Interval"), intervalPanel);
			tabbedPane.addTab(loc.getMenu("Slider"), sliderPanel);
			tabbedPane.addTab(loc.getMenu("Animation"), animationPanel);
			add(tabbedPane);
		} else { // no tabs
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			intervalPanel.setBorder(
					BorderFactory.createTitledBorder(loc.getMenu("Interval")));
			sliderPanel.setBorder(
					BorderFactory.createTitledBorder(loc.getMenu("Slider")));
			animationPanel.setBorder(BorderFactory
					.createTitledBorder(loc.getMenu("Animation")));
			add(intervalPanel);
			add(Box.createVerticalStrut(5));
			add(sliderPanel);
			add(Box.createVerticalStrut(5));
			add(animationPanel);

			pointSliderStylePanel.setBorder(BorderFactory
					.createTitledBorder(loc.getMenu("PointStyle")));
			lineSliderStylePanel.setBorder(
					BorderFactory.createTitledBorder(loc.getMenu("LineStyle")));
			add(Box.createVerticalStrut(5));
			add(pointSliderStylePanel);
			add(Box.createVerticalStrut(5));
			add(lineSliderStylePanel);

		}
	}

	@Override
	public void setLabels() {
		initPanels();

		cbSliderFixed.setText(loc.getMenu("fixed"));
		cbRandom.setText(loc.getMenu("Random"));

		String[] comboStr = { loc.getMenu("horizontal"),
				loc.getMenu("vertical") };

		int selectedIndex = coSliderHorizontal.getSelectedIndex();
		coSliderHorizontal.removeActionListener(this);
		coSliderHorizontal.removeAllItems();

		for (String s : comboStr) {
			coSliderHorizontal.addItem(s);
		}

		coSliderHorizontal.setSelectedIndex(selectedIndex);
		coSliderHorizontal.addActionListener(this);

		tLabels[0].setText(loc.getMenu("min") + ":");
		tLabels[1].setText(loc.getMenu("max") + ":");
		tLabels[2].setText(loc.getMenu("Width") + ":");
		if (tLabels.length > 3) {
			tLabels[3].setText(loc.getMenu("Size") + ":");
			tLabels[4].setText(loc.getMenu("Thickness") + ":");
		}

		lblBlobColor.setText(loc.getMenu("Color") + ":");
		lblLineColor.setText(loc.getMenu("Color") + ":");

		model.setLabelForWidthUnit();

		stepPanel.setLabels();
		speedPanel.setLabels();
	}

	@Override
	public JPanel updatePanel(Object[] geos) {
		stepPanel.updatePanel(geos);
		speedPanel.updatePanel(geos);

		model.setGeos(geos);
		if (!model.checkGeos()) {
			return null;
		}

		for (JTextField tfield : tfields) {
			tfield.removeActionListener(this);
		}

		coSliderHorizontal.removeActionListener(this);
		cbSliderFixed.removeActionListener(this);
		cbRandom.removeActionListener(this);
		model.updateProperties();

		for (JTextField tfield : tfields) {
			tfield.addActionListener(this);
		}

		coSliderHorizontal.addActionListener(this);
		cbSliderFixed.addActionListener(this);
		cbRandom.addActionListener(this);

		return this;
	}

	private void doBlobColorActionPerformed() {
		model.applyBlobColor(
				GColorD.newColor(((GuiManagerD) app.getGuiManager())
						.showColorChooser(model.getBlobColor())));
	}

	/**
	 * @param color
	 *            basic color
	 * @return basic color with opacity
	 */
	public GColor getColorWithOpacity(GColor color) {
		GColor lineCol = color == null ? GColor.BLACK : color;
		return GColor.newColor(lineCol.getRed(), lineCol.getGreen(),
				lineCol.getBlue(), sliderLineOpacity.getValue() * 255 / 100);
	}

	private void doLineColorActionPerformed() {
		model.applyLineColor(
				GColorD.newColor(((GuiManagerD) app.getGuiManager())
						.showColorChooser(model.getLineColor())));
	}

	/**
	 * handle textfield changes
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == cbSliderFixed) {
			doCheckBoxActionPerformed((JCheckBox) source);
		} else if (source == cbRandom) {
			doRandomActionPerformed((JCheckBox) source);
		} else if (source == coSliderHorizontal) {
			doComboBoxActionPerformed();
		} else if (source == btnBlobColor) {
			doBlobColorActionPerformed();
		} else if (source == btnLineColor) {
			doLineColorActionPerformed();
		} else {
			doTextFieldActionPerformed((JTextField) e.getSource());
		}
	}

	private void doCheckBoxActionPerformed(JCheckBox source) {
		model.applyFixed(source.isSelected());
		updatePanel(model.getGeos());
	}

	private void doRandomActionPerformed(JCheckBox source) {
		model.applyRandom(source.isSelected());
		updatePanel(model.getGeos());
	}

	private void doComboBoxActionPerformed() {
		model.applyDirection(coSliderHorizontal.getSelectedIndex());
		updatePanel(model.getGeos());
	}

	private void doTextFieldActionPerformed(JTextField source) {
		actionPerforming = true;
		String inputText = source.getText().trim();
		boolean emptyString = "".equals(inputText);
		NumberValue value = new MyDouble(kernel, Double.NaN);
		if (!emptyString) {
			value = kernel.getAlgebraProcessor().evaluateToNumeric(inputText,
					false);
		}
		if (source == tfMin) {
			model.applyMin(value);
		} else if (source == tfMax) {
			model.applyMax(value);
		} else if (source == tfWidth) {
			model.applyWidth(value.getDouble());
		} else if (source == tfBlobSize) {
			double blob = value.getDouble() <= 0 ? 1 : value.getDouble();
			model.applyBlobSize(blob);
			if (DoubleUtil.isEqual(blob, 1)) {
				tfBlobSize.setText(String.valueOf(1));
			}
		} else if (source == tfLineThickness) {
			double thickness = value.getDouble() * 2 <= 0 ? 2
					: value.getDouble() * 2;
			model.applyLineThickness(thickness);
			if (DoubleUtil.isEqual(thickness, 2)) {
				tfLineThickness.setText(String.valueOf(1));
			}
		}
		if (propPanel != null) {
			propPanel.updateSelection(model.getGeos());
		} else {
			updatePanel(model.getGeos());
		}
		actionPerforming = false;
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// only handle focus lost
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (!actionPerforming) {
			doTextFieldActionPerformed((JTextField) e.getSource());
		}
	}

	@Override
	public void updateFonts() {
		Font font = app.getPlainFont();

		cbSliderFixed.setFont(font);
		cbRandom.setFont(font);
		coSliderHorizontal.setFont(font);

		for (JLabel tLabel : tLabels) {
			tLabel.setFont(font);
		}

		tfMin.setFont(font);
		tfMax.setFont(font);
		tfWidth.setFont(font);

		for (JTextField tfield : tfields) {
			tfield.setFont(font);
		}

		stepPanel.updateFonts();
		speedPanel.updateFonts();
	}

	@Override
	public void setMinText(String text) {
		tfMin.setText(text);
	}

	@Override
	public void setMaxText(String text) {
		tfMax.setText(text);
	}

	@Override
	public void setWidthText(String text) {
		tfWidth.setText(text);
	}

	@Override
	public void selectFixed(boolean value) {
		cbSliderFixed.setSelected(value);
	}

	@Override
	public void selectRandom(boolean value) {
		cbRandom.setSelected(value);
	}

	@Override
	public void setRandomVisible(boolean value) {
		cbRandom.setVisible(value);
	}

	@Override
	public void setSliderDirection(int index) {
		coSliderHorizontal.setSelectedIndex(index);
	}

	@Override
	public void setWidthUnitText(String text) {
		lbWidthUnit.setText(text);
	}

	@Override
	public void setBlobSizeText(String text) {
		tfBlobSize.setText(text);
	}

	@Override
	public void setBlobColor(GColor color) {
		btnBlobColor.setForeground(GColorD.getAwtColor(color));
		btnBlobColor.repaint();
	}

	@Override
	public void setLineColor(GColor color) {
		btnLineColor.setForeground(GColorD.getAwtColor(color));
		btnLineColor.repaint();
	}

	@Override
	public void setLineThicknessSizeText(String text) {
		tfLineThickness.setText(text);
	}

	/**
	 * change listener implementation for slider
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == sliderLineOpacity) {
			model.applyTransparency(sliderLineOpacity.getValue());
			btnLineColor.setForeground(
					GColorD.getAwtColor(
							getColorWithOpacity(model.getLineColor())));
		}
	}
}