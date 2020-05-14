package hr.fer.zemris.irg.vjezba9.linearna;

public class IRG {

    public static IMatrix translate3D(float dx, float dy, float dz) {
        IMatrix translation = new Matrix(4, 4);
        translation.set(0, 0, 1);
        translation.set(1, 1, 1);
        translation.set(2, 2, 1);
        translation.set(3, 0, dx);
        translation.set(3, 1, dy);
        translation.set(3, 2, dz);
        translation.set(3, 3, 1);
        return translation;
    }

    public static IMatrix scale3D(float sx, float sy, float sz) {
        IMatrix scaling = new Matrix(4, 4);
        scaling.set(0, 0, sx);
        scaling.set(1, 1, sy);
        scaling.set(2, 2, sz);
        scaling.set(3, 3, 1);
        return scaling;
    }

    public static IMatrix lookAtMatrix(IVector eye, IVector center, IVector viewUp) {
        IVector forward = center.nSub(eye).normalize();
        IVector side = forward.nVectorProduct(viewUp.nNormalize()).normalize();
        IVector up = side.nVectorProduct(forward).normalize();
        IMatrix lookAt = new Matrix(4, 4);
        // U
        lookAt.set(0, 0, side.get(0));
        lookAt.set(1, 0, side.get(1));
        lookAt.set(2, 0, side.get(2));
        lookAt.set(3, 0, 0.0);
        // V
        lookAt.set(0, 1, up.get(0));
        lookAt.set(1, 1, up.get(1));
        lookAt.set(2, 1, up.get(2));
        lookAt.set(3, 1, 0.0);
        // N
        lookAt.set(0, 2, -forward.get(0));
        lookAt.set(1, 2, -forward.get(1));
        lookAt.set(2, 2, -forward.get(2));
        lookAt.set(3, 2, 0.0);
        // ----------------------------
        lookAt.set(3, 3, 1.0);
        IMatrix translation = translate3D((float) -eye.get(0), (float) -eye.get(1), (float) -eye.get(2));
        return translation.nMultiply(lookAt);
    }

    public static IMatrix buildFrustumMatrix(double l, double r, double b, double t, int n, int f) {
        IMatrix frustum = new Matrix(4, 4);
        frustum.set(0, 0, 2 * n / (r - l));
        frustum.set(1, 1, 2 * n / (t - b));
        frustum.set(2, 0, (r + l) / (r - l));
        frustum.set(2, 1, (t + b) / (t - b));
        frustum.set(2, 2, -((double) f + n) / (f - n));
        frustum.set(2, 3, -1);
        frustum.set(3, 2, -2.0 * f * n / (f - n));
        return frustum;
    }

    public static boolean isAntiClockwise(IVector v1, IVector v2, IVector v3) {
        IMatrix m = new Matrix(3, 3);
        // row 1
        m.set(0, 0, v1.get(0));
        m.set(0, 1, v1.get(1));
        m.set(0, 2, 1);
        // row 2
        m.set(1, 0, v2.get(0));
        m.set(1, 1, v2.get(1));
        m.set(1, 2, 1);
        // row 3
        m.set(2, 0, v3.get(0));
        m.set(2, 1, v3.get(1));
        m.set(2, 2, 1);

        return m.determinant() > 0;
    }

}
