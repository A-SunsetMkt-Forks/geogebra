package org.geogebra.web.full.gui.toolbar.mow.popupcomponents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.geogebra.common.awt.GColor;
import org.geogebra.common.gui.dialog.handler.ColorChangeHandler;
import org.geogebra.web.full.css.MaterialDesignResources;
import org.geogebra.web.full.gui.dialog.DialogManagerW;
import org.geogebra.web.html5.gui.util.Dom;
import org.geogebra.web.html5.gui.util.NoDragImage;
import org.geogebra.web.html5.gui.view.IconSpec;
import org.geogebra.web.html5.gui.view.ImageIconSpec;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.html5.main.toolbox.ToolboxIcon;
import org.gwtproject.dom.client.Element;
import org.gwtproject.user.client.ui.FlowPanel;
import org.gwtproject.user.client.ui.SimplePanel;

public class ColorChooserPanel extends FlowPanel {
	private final Consumer<GColor> callback;
	private GColor activeColor;
	private FlowPanel activeButton;
	private final AppW appW;
	private Map<GColor, FlowPanel> colorButtons;

	/**
	 * constructor
	 * @param appW - application
	 * @param colorValues - color values
	 * @param callback - callback for setting the color for object
	 */
	public ColorChooserPanel(AppW appW, List<GColor> colorValues, Consumer<GColor> callback) {
		super();
		addStyleName("colorPalette");
		this.callback = callback;
		this.appW = appW;
		buildGUI(colorValues);
	}

	private void buildGUI(List<GColor> colorValues) {
		colorButtons = new HashMap<>();
		colorValues.forEach(this::addColorButton);
	}

	 private void addColorButton(GColor color) {
		if (color == null) {
			addCustomColorButton();
			return;
		}

		FlowPanel colorButton = new FlowPanel("button");
		colorButton.addStyleName("colorButton");
		if (GColor.WHITE.equals(color)) {
			colorButton.addStyleName("white");
		}

		SimplePanel colorHolder = new SimplePanel();
		colorHolder.addStyleName("colorBg");
		colorHolder.getElement().getStyle().setBackgroundColor(color.toString());

		NoDragImage checkmark = new NoDragImage(
				MaterialDesignResources.INSTANCE.check_border(), 18);
		checkmark.addStyleName("checkmark");

		colorButton.add(colorHolder);
		colorButton.add(checkmark);

		Dom.addEventListener(colorButton.getElement(), "click", (event) -> {
			if (isDisabled()) {
				return;
			}
			updateColor(colorButton, color);
		});

		colorButtons.put(color, colorButton);
		add(colorButton);
	 }

	 private void addCustomColorButton() {
		FlowPanel customColorButton = new FlowPanel("button");
		customColorButton.addStyleName("colorButton customColor");

		SimplePanel imageHolder = new SimplePanel();
		imageHolder.addStyleName("imageHolder");
		IconSpec iconPlus = appW.getToolboxIconResource().getImageResource(ToolboxIcon.PLUS);

		if (iconPlus instanceof ImageIconSpec) {
			customColorButton.add(imageHolder);
			NoDragImage img = new NoDragImage(((ImageIconSpec) iconPlus).getImage(), 18);
			img.addStyleName("plus");
			customColorButton.add(img);
		} else {
			Element iconElement = iconPlus.toElement();
			iconElement.addClassName("plus");
			customColorButton.getElement().insertFirst(iconElement);
			customColorButton.insert(imageHolder, 0);
		}

		Dom.addEventListener(customColorButton.getElement(), "click", (event) -> {
				if (!isDisabled()) {
					((DialogManagerW) appW.getDialogManager()).showColorChooserDialog(activeColor,
							new ColorChangeHandler() {
								@Override
								public void onColorChange(GColor color) {
									updateColor(null, color);
								}

								@Override
								public void onAlphaChange() {
									// nothing to do here
								}

								@Override
								public void onClearBackground() {
									// nothing to do here
								}

								@Override
								public void onForegroundSelected() {
									// nothing to do here
								}

								@Override
								public void onBackgroundSelected() {
									// nothing to do here
								}

								@Override
								public void onBarSelected() {
									// nothing to do here
								}
							});
				}
		});

		add(customColorButton);
	 }

	 private void updateActiveColorButton(FlowPanel newActiveButton, GColor newActiveColor) {
		if (activeButton != null) {
			activeButton.removeStyleName("selected");
		}
		activeButton = newActiveButton;
		if (activeButton != null) {
			activeButton.addStyleName("selected");
		}
		activeColor = newActiveColor;
	 }

	 private void runCallback(GColor color) {
		if (callback != null) {
			callback.accept(color);
		}
	 }

	 private void updateColor(FlowPanel colorButton, GColor color) {
		updateActiveColorButton(colorButton, color);
		runCallback(color);
	}

	/**
	 * disable/enable color chooser
	 * @param disabled - whether is disabled or not
	 */
	public void setDisabled(boolean disabled) {
		Dom.toggleClass(this, "disabled", disabled);
	}

	public boolean isDisabled() {
		return getElement().hasClassName("disabled");
	}

	private FlowPanel getColorButton(GColor color) {
		return colorButtons.get(color);
	}

	/**
	 * Update color palette selection (UI only, does not update the model).
	 * @param selectedColor - last selected color
	 */
	public void updateColorSelection(GColor selectedColor) {
		FlowPanel buttonToSelect = getColorButton(selectedColor);
		if (buttonToSelect != null) {
			updateActiveColorButton(buttonToSelect, selectedColor);
		} else {
			if (activeButton != null) {
				activeButton.removeStyleName("selected");
			}
			activeColor = selectedColor;
		}
	}
}
