package hr.fer.zemris.irg.vjezba11.math;

import static java.lang.Math.*;

import java.util.Objects;

/**
 * Model of 3D vector.
 * 
 * @author dbrcina
 *
 */
public final class Vector3 {

	/**
	 * Constant that is used to check whether two doubles are equal.
	 */
	private static final double DELTA = 1e-9;

	/**
	 * x - component.
	 */
	private double x;

	/**
	 * y - component.
	 */
	private double y;

	/**
	 * z - component.
	 */
	private double z;

	/**
	 * Constructor used for initialization.
	 * 
	 * @param x x component.
	 * @param y y component.
	 * @param z z component.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculates vector's norm.
	 * 
	 * @return vector's norm.
	 */
	public double norm() {
		return sqrt(x * x + y * y + z * z);
	}

	/**
	 * Returns normalized vector.
	 * 
	 * @return new instance of {@link Vector3}.
	 */
	public Vector3 normalized() {
		return scale(1 / norm());
	}

	/**
	 * Add operation between two instances of {@link Vector3}.
	 * 
	 * @param other other vector.
	 * @return new instance of {@link Vector3} as a result.
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other, "Vector cannot be null!");
		return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	/**
	 * Substract operation between two instances of {@link Vector3}.
	 * 
	 * @param other other vector.
	 * @return new instance of {@link Vector3} as a result.
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other, "Vector cannot be null!");
		return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	/**
	 * Performs scalar product between two vectors.
	 * 
	 * @param other other vector.
	 * @return result of scalar product.
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other, "Vector cannot be null!");
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Performs vectors product between two vectors.
	 * 
	 * @param other other vector.
	 * @return result of vector product.
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other, "Vector cannot be null!");
		return new Vector3(
				this.y * other.z - this.z * other.y, 
				this.x * other.z - this.z * other.x,
				this.x * other.y - this.y * other.x);
	}

	/**
	 * Scales vector by scaler <code>s</code>.
	 * 
	 * @param s scaler.
	 * @return new instance of {@link Vector3} as a result.
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Calculates cos angle between two vectors.
	 * 
	 * @param other other vector.
	 * @return cos angle value.
	 */
	public double cosAngle(Vector3 other) {
		Objects.requireNonNull(other, "Vector cannot be null!");
		return this.dot(other) / (this.norm() * other.norm());
	}

	/**
	 * Getter for x component.
	 * 
	 * @return {@link #x}.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter for y component.
	 * 
	 * @return {@link #y}.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Getter for z component.
	 * 
	 * @return {@link #z}.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Creates an array of vector components.
	 * 
	 * @return new array of vector components.
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	@Override
	public String toString() {
		return String.format("(%.6f, %.6f, %.6f)", x, y, z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector3))
			return false;
		Vector3 other = (Vector3) obj;
		return abs(x - other.x) <= DELTA && abs(y - other.y) <= DELTA && abs(z - other.z) <= DELTA;
	}
}
