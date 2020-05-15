package hr.fer.zemris.irg.vjezba11.math;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ComplexPolynomial {

	/**
	 * List of complex numbers.
	 */
	private List<Complex> complexNumbers;

	/**
	 * Constructor used for initializing this polynom.
	 * 
	 * @param factors list of factors.
	 * @throws NullPointerException if {@code factors} is {@code null}.
	 */
	public ComplexPolynomial(Complex... factors) {
		complexNumbers = Arrays.asList(Objects.requireNonNull(factors, "List of factors cannot be null!"));
	}

	/**
	 * Returns order of this polynom;
	 * 
	 * <pre>
	 * eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	 * </pre>
	 * 
	 * @return order of polynom.
	 */
	public short order() {
		return (short) (complexNumbers.size() - 1);
	}

	/**
	 * Performs multiply operation on two polynoms.
	 * 
	 * @param p other polynom.
	 * @return new instance of {@link ComplexPolynomial} as a result.
	 * @throws NullPointerException if {@code p} is {@code null}.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p, "Polynom cannot be null!");
		Complex[] resultFactors = new Complex[order() + p.order() + 1];
		for (int i = 0; i < resultFactors.length; i++) {
			resultFactors[i] = Complex.ZERO;
		}

		for (int i = 0; i < complexNumbers.size(); i++) {
			for (int j = 0; j < p.complexNumbers.size(); j++) {
				resultFactors[i + j] = resultFactors[i + j]
						.add(complexNumbers.get(i).multiply(p.complexNumbers.get(j)));
			}
		}

		return new ComplexPolynomial(resultFactors);
	}

	/**
	 * Computes first derivate of this polynomial; for example
	 * 
	 * <pre>
	 * for (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	 * </pre>
	 * 
	 * @return new instance of {@link ComplexPolynomial} as a result.
	 */
	public ComplexPolynomial derive() {
		Complex[] resultFactors = new Complex[order()];

		for (int i = 0, len = order(); i < len; i++) {
			resultFactors[i] = complexNumbers.get(i + 1).multiply(new Complex(i + 1, 0));
		}

		return new ComplexPolynomial(resultFactors);
	}

	/**
	 * Computes polynomial value at given point <code>z</code>.
	 * 
	 * @param z complex number.
	 * @return new instance of {@link Complex} as a result.
	 * @throws NullPointerException if {@code z} is {@code null}.
	 */
	public Complex apply(Complex z) {
		Complex result = Complex.ZERO;

		for (int i = 0; i < complexNumbers.size(); i++) {
			result = result.add(z.power(i).multiply(complexNumbers.get(i)));
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = complexNumbers.size() - 1; i > 0; i--) {
			sb.append(complexNumbers.get(i));
			sb.append("*z^" + i + "+");
		}
		sb.append(complexNumbers.get(0));
		return sb.toString();
	}
}
