package org.geogebra.common.kernel.arithmetic.filter;

import javax.annotation.Nonnull;

import org.geogebra.common.kernel.arithmetic.ExpressionValue;
import org.geogebra.common.kernel.arithmetic.ValueType;

/**
 * An {@link ExpressionFilter} based on complex values in expressions.
 */
public class ComplexExpressionFilter implements ExpressionFilter {

    @Override
    public boolean isAllowed(@Nonnull ExpressionValue expression) {
        boolean containsComplexValues = expression
                .any(expressionValue -> expressionValue.getValueType() == ValueType.COMPLEX);
        return !containsComplexValues;
    }
}
