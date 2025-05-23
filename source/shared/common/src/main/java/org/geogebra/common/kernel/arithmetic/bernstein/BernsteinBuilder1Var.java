package org.geogebra.common.kernel.arithmetic.bernstein;

import org.geogebra.common.util.MyMath;

public class BernsteinBuilder1Var {

	private double[] powerBasisCoeffs;

	BernsteinPolynomial1D build(double[] powerCoeffs,
			int degree, char variable, double min, double max) {
		powerBasisCoeffs = powerCoeffs;
		double[] bernsteinCoeffs = createBernsteinCoeffs(degree, min, max);
		return new BernsteinPolynomial1D(bernsteinCoeffs, variable, min, max);
	}

	private double[] createBernsteinCoeffs(int degree, double min, double max) {
		double[] partialBernsteinCoeffs = new double[degree + 1];
		double[] lastValues = new double[degree + 1];
		for (int i = 0; i <= degree; i++) {
			for (int j = 0; j <= i; j++) {
				double b_ij = bernsteinCoefficient(i, j, degree, min, max, lastValues);
				partialBernsteinCoeffs[j] = b_ij;
			}

			System.arraycopy(partialBernsteinCoeffs, 0, lastValues, 0, lastValues.length);
		}
		return partialBernsteinCoeffs;
	}

	private double bernsteinCoefficient(int i, int j, int degree, double min, double max,
			double[] lastValues) {
		if (i == 0 && j == 0) {
			return powerBasisCoeffs[degree];
		}

		double a_nMinusI = powerBasisCoeffs[degree - i];

		if (j == 0) {
			return a_nMinusI + min * lastValues[0];
		}

		if (j == i) {
			return a_nMinusI + max * lastValues[i - 1];
		}

		double binomial = MyMath.binomial(i, j);
		return binomial * a_nMinusI
				+ min * lastValues[j]
				+ max * lastValues[j - 1];
	}
}
