package hr.fer.zemris.irg.vjezba11.math;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class models polynomial with complex roots.
 * 
 * @author dbrcina
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Polynomial constant.
	 */
	private Complex constant;

	/**
	 * List of complex roots.
	 */
	private List<Complex> roots;

	/**
	 * Constructor used for initializing this polynom.
	 * 
	 * @param constant polynomial constant.
	 * @param roots    complex roots.
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
	public ComplexRootedPolynomial(Complex constant, Complex... roots) {
		this.constant = Objects.requireNonNull(constant, "Constant cannot be null!");
		this.roots = Arrays.asList(Objects.requireNonNull(roots, "List of roots cannot be null!"));
	}

	/**
	 * Computes polynomial value az given point <code>z</code>.
	 * 
	 * @param z complex number.
	 * @return new instance of {@link Complex} as a result.
	 * @throws NullPointerException if {@code z} is {@code null}.
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z, "Root point cannot be null!");
		if (roots.isEmpty()) {
			return constant;
		}

		Complex result = constant.add(Complex.ZERO);
		for (int i = 0; i < roots.size(); i++) {
			result = result.multiply(z.sub(roots.get(i)));
		}

		return result;
	}

	/**
	 * Converts this representation to {@link ComplexPolynomial} type.
	 * 
	 * @return new instance of {@link ComplexPolynomial}
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial result = new ComplexPolynomial(constant);
		for (Complex root : roots) {
			result = result.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(constant + "*");
		for (Complex root : roots) {
			sb.append("(z-");
			sb.append(root);
			sb.append(")*");
		}
		sb.replace(sb.length() - 1, sb.length(), "");
		return sb.toString();
	}

	/**
	 * Finds index of closest root for given complex number <code>z</code> that is
	 * within <code>treshold</code>. If there is no such root, returns
	 * <code>-1</code>.
	 * 
	 * @param z        complex number.
	 * @param treshold treshold.
	 * @return index of closest root if it exists, otherwise {@code -1}.
	 * @throws NullPointerException if {@code z} is {@code null}.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		Objects.requireNonNull(z, "Complex number cannot be null!");

		double minDistance = treshold + 1;
		int index = -1;

		for (int i = 0; i < roots.size(); i++) {
			Complex root = roots.get(i);
			double distance = z.sub(root).module();
			if (distance < minDistance && distance< treshold) {
				index = i;
				minDistance = distance;
			}
		}

		return index;
	}
}
