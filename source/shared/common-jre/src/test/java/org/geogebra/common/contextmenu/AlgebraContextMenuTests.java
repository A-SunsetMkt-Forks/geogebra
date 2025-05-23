package org.geogebra.common.contextmenu;

import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.AddLabel;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.CreateSlider;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.CreateTableValues;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.Delete;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.DuplicateInput;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.DuplicateOutput;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.RemoveLabel;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.RemoveSlider;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.Settings;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.Solve;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.SpecialPoints;
import static org.geogebra.common.contextmenu.AlgebraContextMenuItem.Statistics;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.geogebra.common.AppCommonFactory;
import org.geogebra.common.GeoGebraConstants;
import org.geogebra.common.cas.MockedCasGiac;
import org.geogebra.common.gui.view.algebra.contextmenu.impl.CreateSlider;
import org.geogebra.common.jre.headless.AppCommon;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.commands.AlgebraProcessor;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.common.main.AppConfig;
import org.geogebra.common.main.settings.AlgebraSettings;
import org.geogebra.common.main.settings.config.AppConfigCas;
import org.geogebra.common.main.settings.config.AppConfigGeometry;
import org.geogebra.common.main.settings.config.AppConfigGraphing;
import org.geogebra.common.main.settings.config.AppConfigGraphing3D;
import org.geogebra.common.main.settings.config.AppConfigScientific;
import org.geogebra.common.scientific.LabelController;
import org.geogebra.common.util.MockedCasValues;
import org.geogebra.common.util.MockedCasValuesExtension;
import org.geogebra.test.TestErrorHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SuppressWarnings("checkstyle:RegexpSinglelineCheck") // Tabs in MockedCasValues
@ExtendWith(MockedCasValuesExtension.class)
public class AlgebraContextMenuTests {
	private final ContextMenuFactory contextMenuFactory = new ContextMenuFactory();

	private AlgebraProcessor algebraProcessor;
	private AlgebraSettings algebraSettings;
	private String appCode;
	private final MockedCasGiac mockedCasGiac = new MockedCasGiac();
	private Kernel kernel;

	@Test
	public void testAlgebraContextMenuWithInvalidGeoElement() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(Delete),
				contextMenuFactory.makeAlgebraContextMenu(
						null, algebraProcessor, appCode, algebraSettings)
		);
	}

	// Geometry app

	@Test
	public void testForDefaultAlgebraInputInGeometryApp() {
		setupApp(GeoGebraConstants.GEOMETRY_APPCODE);
		assertEquals(
				List.of(SpecialPoints,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("x"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithStatisticsInGeometryApp() {
		setupApp(GeoGebraConstants.GEOMETRY_APPCODE);
		assertEquals(
				List.of(Statistics,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("{1, 2, 3}"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithOutputInGeometryApp() {
		setupApp(GeoGebraConstants.GEOMETRY_APPCODE);
		assertEquals(
				List.of(DuplicateInput,
						DuplicateOutput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("1 + 2"), algebraProcessor, appCode, algebraSettings)
		);
	}

	// Scientific app

	@Test
	public void testForInputWithNoLabelInScientificApp() {
		setupApp(GeoGebraConstants.SCIENTIFIC_APPCODE);
		GeoElement geoElement = add("5");
		geoElement.setAlgebraLabelVisible(false);
		assertEquals(
				List.of(AddLabel,
						DuplicateInput,
						Delete),
				contextMenuFactory.makeAlgebraContextMenu(
						geoElement, algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithLabelInScientificApp() {
		setupApp(GeoGebraConstants.SCIENTIFIC_APPCODE);
		GeoElement geoElement = add("5");
		geoElement.setAlgebraLabelVisible(true);
		assertEquals(
				List.of(RemoveLabel,
						DuplicateInput,
						Delete),
				contextMenuFactory.makeAlgebraContextMenu(
						geoElement, algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithDuplicateOutputInScientificApp() {
		setupApp(GeoGebraConstants.SCIENTIFIC_APPCODE);
		GeoElement geoElement = add("1 + 2");
		assertEquals(
				List.of(RemoveLabel,
						DuplicateInput,
						DuplicateOutput,
						Delete),
				contextMenuFactory.makeAlgebraContextMenu(
						geoElement, algebraProcessor, appCode, algebraSettings)
		);
	}

	// Graphing app

	@Test
	public void testForInputWithSpecialPointsAndTableValuesInGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(CreateTableValues,
						SpecialPoints,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("x"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithSliderInGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(CreateSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("1"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForSliderCommandInGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(RemoveSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("Slider(-5,5,1)"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithStatisticsInGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(CreateTableValues,
						Statistics,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("{1, 2, 3}"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForPotentialSliderGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(CreateSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("1"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForSliderGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		assertEquals(
				List.of(RemoveSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("Slider(0, 5, 1)"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithOnlyEngineeringNotationOutputInGraphingApp() {
		setupApp(GeoGebraConstants.GRAPHING_APPCODE);
		algebraSettings.setEngineeringNotationEnabled(true);
		assertEquals(
				List.of(CreateSlider,
						DuplicateInput,
						DuplicateOutput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("1234567"), algebraProcessor, appCode, algebraSettings)
		);
	}

	// Graphing 3D app

	@Test
	public void testForInputWithSolutionInGraphing3DApp() {
		setupApp(GeoGebraConstants.G3D_APPCODE);
		assertEquals(
				List.of(Solve,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("x^(2) - 5x + 6 = 0"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithSpecialPointsInGraphing3DApp() {
		setupApp(GeoGebraConstants.G3D_APPCODE);
		assertEquals(
				List.of(SpecialPoints,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("x"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	public void testForInputWithStatisticsInGraphing3DApp() {
		setupApp(GeoGebraConstants.G3D_APPCODE);
		assertEquals(
				List.of(Statistics,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("{1, 2, 3}"), algebraProcessor, appCode, algebraSettings)
		);
	}

	// CAS app

	@Test
	@MockedCasValues({
			"Evaluate(5) 	-> 5",
			"Round(5, 13) 	-> 5.0",
	})
	public void testForSimpleInputWithSliderInCasApp() {
		setupApp(GeoGebraConstants.CAS_APPCODE);
		GeoElement number = add("slider=5");
		new CreateSlider(algebraProcessor, new LabelController()).execute(number);
		assertEquals(
				List.of(RemoveSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						kernel.lookupLabel("slider"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	@MockedCasValues({
			"Evaluate(5) 	-> 5",
			"Round(5, 13) 	-> 5.0",
	})
	public void testForSimpleInputWithoutLabelInCasApp() {
		setupApp(GeoGebraConstants.CAS_APPCODE);
		GeoElement geoElement = add("5");
		new LabelController().hideLabel(geoElement);
		assertEquals(
				List.of(AddLabel,
						CreateSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						geoElement, algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	@MockedCasValues({
			"Evaluate(5) 	-> 5",
			"Round(5, 13) 	-> 5.0",
	})
	public void testForSimpleInputWithoutSliderInCasApp() {
		setupApp(GeoGebraConstants.CAS_APPCODE);
		GeoElement geoElement = add("5");
		assertEquals(
				List.of(RemoveLabel,
						CreateSlider,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						geoElement, algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	@MockedCasValues({"Evaluate({1, 2, 3}) -> {1,2,3}"})
	public void testForInputWithStatisticsInCasApp() {
		setupApp(GeoGebraConstants.CAS_APPCODE);
		assertEquals(
				List.of(CreateTableValues,
						RemoveLabel,
						Statistics,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("{1, 2, 3}"), algebraProcessor, appCode, algebraSettings)
		);
	}

	@Test
	@MockedCasValues({"Evaluate(x) -> x"})
	public void testForInputWithSpecialPointsInCasApp() {
		setupApp(GeoGebraConstants.CAS_APPCODE);
		assertEquals(
				List.of(CreateTableValues,
						RemoveLabel,
						SpecialPoints,
						DuplicateInput,
						Delete,
						Settings),
				contextMenuFactory.makeAlgebraContextMenu(
						add("x"), algebraProcessor, appCode, algebraSettings)
		);
	}

	private void setupApp(String appCode) {
		this.appCode = appCode;
		AppCommon app = AppCommonFactory.create(makeAppConfig(appCode));
		mockedCasGiac.applyTo(app);
		algebraProcessor = app.getKernel().getAlgebraProcessor();
		algebraSettings = app.getSettings().getAlgebra();
		kernel = app.getKernel();
		app.getSettingsUpdater().resetSettingsOnAppStart();
	}

	private AppConfig makeAppConfig(String appCode) {
		switch (appCode) {
		case GeoGebraConstants.CAS_APPCODE: return new AppConfigCas();
		case GeoGebraConstants.G3D_APPCODE: return new AppConfigGraphing3D();
		case GeoGebraConstants.GEOMETRY_APPCODE: return new AppConfigGeometry();
		case GeoGebraConstants.SCIENTIFIC_APPCODE: return new AppConfigScientific();
		default: return new AppConfigGraphing();
		}
	}

	private GeoElement add(String command) {
		GeoElementND[] geoElements = algebraProcessor.processAlgebraCommandNoExceptionHandling(
				command, false, TestErrorHandler.INSTANCE, false, null);
		return (GeoElement) geoElements[0];
	}
}
