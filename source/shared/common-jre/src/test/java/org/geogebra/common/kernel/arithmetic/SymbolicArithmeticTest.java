package org.geogebra.common.kernel.arithmetic;

import static org.junit.Assert.assertEquals;

import org.geogebra.common.BaseUnitTest;
import org.geogebra.common.kernel.StringTemplate;
import org.geogebra.common.kernel.commands.EvalInfo;
import org.geogebra.common.kernel.kernelND.GeoElementND;
import org.geogebra.test.TestErrorHandler;

public class SymbolicArithmeticTest extends BaseUnitTest {

	/**
	 * Evaluate inputs using symbolic flag
	 * @param string input
	 * @param string2 expected output
	 */
	protected void t(String string, String string2) {
		GeoElementND[] geos = getKernel().getAlgebraProcessor()
				.processAlgebraCommandNoExceptionHandling(string, false,
						TestErrorHandler.INSTANCE,
						new EvalInfo(true).withSymbolic(true), null);
		assertEquals(string2,
				geos[0].toValueString(StringTemplate.algebraTemplate));
	}
}
