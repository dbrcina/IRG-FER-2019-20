package hr.fer.zemris.irg.vjezba11.math;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model of complex number.
 * 
 * @author dbrcina
 *
 */
public class Complex {

	/**
	 * Constant that is used to check whether two doubles are equal.
	 */
	private static final double DELTA = 1e-9;

	/**
	 * Constant representing complex number <code>0+0i</code>.
	 */
	public static final Complex ZERO = new Complex(0, 0);

	/**
	 * Constant representing complex number <code>1+0i</code>.
	 */
	public static final Complex ONE = new Complex(1, 0);

	/**
	 * Constant representing complex number <code>-1+0i</code>.
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/**
	 * Constant representing complex number <code>0+1i</code>.
	 */
	public static final Complex IM = new Complex(0, 1);

	/**
	 * Constant representing complex number <code>0-1i</code>.
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Real part.
	 */
	private double real;

	/**
	 * Imaginary part.
	 */
	private double imag;

	/**
	 * Complex number's magnitude.
	 */
	private double magnitude;

	/**
	 * Complex number's angle.
	 */
	private double angle;

	/**
	 * Constructor used for initializing new Complex number.
	 * 
	 * @param real real part.
	 * @param imag imag part.
	 */
	public Complex(double real, double imag) {
		this.real = real;
		this.imag = imag;
		this.magnitude = sqrt(real * real + imag * imag);
		this.angle = atan2(imag, real);
		this.angle = angle >= 0 ? angle : angle + 2 * PI;
	}

	/**
	 * Default constructor used for creating complex number <code>0+0i</code>.
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Calculates module of complex number.
	 * 
	 * @return module of complex number.
	 */
	public double module() {
		return magnitude;
	}

	/**
	 * Performs multiply operation on two complex numbers.
	 * 
	 * @param c other complex number.
	 * @return new instance of {@link Complex} as a result.
	 * @throws NullPointerException if {@code c} is {@code null}.
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c, "Complex number cannot be null!");
		return new Complex(real * c.real - imag * c.imag, imag * c.real + real * c.imag);
	}

	/**
	 * Performs division operation on two complex numbers.
	 * 
	 * @param c other complex number.
	 * @return new instance of {@link Complex} as a result.
	 * @throws NullPointerException     if {@code c} is {@code null}.
	 * @throws IllegalArgumentException if division with zero occures.
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c, "Complex number cannot be null!");
		double divider = c.magnitude * c.magnitude;
		if (divider == 0) {
			throw new IllegalArgumentException("Cannot divide with zero!");
		}
		return new Complex((real * c.real + imag * c.imag) / divider, (imag * c.real - real * c.imag) / divider);
	}

	/**
	 * Performs add operation on two complex numbers.
	 * 
	 * @param c other complex number.
	 * @return new instance of {@link Complex} as a result.
	 * @throws NullPointerException if {@code c} is {@code null}.
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c, "Complex number cannot be null!");
		return new Complex(real + c.real, imag + c.imag);
	}

	/**
	 * Performs substract operation on two complex numbers.
	 * 
	 * @param c other complex number.
	 * @return new instance of {@link Complex} as a result.
	 * @throws NullPointerException if {@code c} is {@code null}.
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c, "Complex number cannot be null!");
		return new Complex(real - c.real, imag - c.imag);
	}

	/**
	 * Negates current complex number.
	 * 
	 * @return new instance of {@link Complex} as a result.
	 */
	public Complex negate() {
		return new Complex(-real, -imag);
	}

	/**
	 * Calculates potency of complex number by <code>n</code>.
	 * 
	 * @param n a non-negative integer.
	 * @return new instance of {@link Complex} as a result.
	 * @throws IllegalArgumentException if provided exponent is a negative integer.
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Exponent needs to be a non-negative integer!");
		}
		if (this.equals(Complex.ZERO)) {
			return this;
		}
		double powMagnitude = pow(magnitude, n);
		double mulAngle = angle * n;
		return new Complex(powMagnitude * cos(mulAngle), powMagnitude * sin(mulAngle));
	}

	/**
	 * Calculates n-th root of this complex number. Root exponent <code>n</code>
	 * needs to be positive integer.
	 * 
	 * @param n a positive integer.
	 * @return list of {@link Complex} as a result.
	 * @throws IllegalArgumentException if provided exponent is not positive
	 *                                  integer.
	 */
	public List<Complex> root(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Root exponent needs to be > 1!");
		}
		List<Complex> results = new ArrayList<>();
		double rootMagnitude = pow(magnitude, 1.0 / n);
		for (int i = 0; i < n; i++) {
			results.add(new Complex(rootMagnitude * cos((angle + 2 * i * PI) / n),
					rootMagnitude * sin((angle + 2 * i * PI) / n)));
		}
		return results;
	}

	/**
	 * Getter for real part of complex number.
	 * 
	 * @return {@link #real}.
	 */
	public double getReal() {
		return real;
	}

	/**
	 * Getter for imag part of complex number.
	 * 
	 * @return {@link #imag}.
	 */
	public double getImag() {
		return imag;
	}

	@Override
	public String toString() {
		char operation = imag >= 0 ? '+' : '-';
		return String.format("(%s%ci%s)", real, operation, abs(imag));
	}

	@Override
	public int hashCode() {
		return Objects.hash(real, imag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Complex))
			return false;
		Complex other = (Complex) obj;
		return abs(real - other.real) <= DELTA && abs(imag - other.imag) <= DELTA;
	}

	public Complex copy() {
		return new Complex(real, imag);
	}

}
