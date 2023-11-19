package org.radek.polycube;

import static org.radek.polycube.Expansion.X_NEG;
import static org.radek.polycube.Expansion.X_POS;
import static org.radek.polycube.Expansion.Y_NEG;
import static org.radek.polycube.Expansion.Y_POS;
import static org.radek.polycube.Expansion.Z_NEG;
import static org.radek.polycube.Expansion.Z_POS;

public enum Rotation {
	NORMAL(X_POS, Y_POS, Z_POS),
	X_90(X_POS, Z_NEG, Y_POS),
	X_180(X_POS, Y_NEG, Z_NEG),
	X_270(X_POS, Z_POS, Y_NEG),
	Y_90(Z_POS, Y_POS, X_NEG),
	Y_180(X_NEG, Y_POS, Z_NEG),
	Y_270(Z_NEG, Y_POS, X_POS),
	Z_90(Y_NEG, X_POS, Z_POS),
	Z_180(X_NEG, Y_NEG, Z_POS),
	Z_270(Y_POS, X_NEG, Z_POS),
	X_90_Y_90(Y_POS, Z_NEG, X_NEG),
	X_180_Y_90(Z_NEG, Y_NEG, X_NEG),
	X_270_Y_90(Y_NEG, Z_POS, X_NEG),
	X_90_Y_180(X_NEG, Z_NEG, Y_NEG),
//	X_180_Y_180(X_NEG, Y_NEG, Z_POS),	// dupe of Z_180!
	X_270_Y_180(X_NEG, Z_POS, Y_POS),
	X_90_Y_270(Y_NEG, Z_NEG, X_POS),
	X_180_Y_270(Z_POS, Y_NEG, X_POS),
	X_270_Y_270(Y_POS, Z_POS, X_POS),
	X_90_Z_90(Z_POS, X_POS, Y_POS),
	X_180_Z_90(Y_POS, X_POS, Z_NEG),
	X_270_Z_90(Z_NEG, X_POS, Y_NEG),
//	X_90_Z_180(X_NEG, Z_POS, Y_POS),	// dupe of X_270_Y_180!
//	X_180_Z_180(X_NEG, Y_POS, Z_NEG),	// dupe of Y_180!
//	X_270_Z_180(X_NEG, Z_NEG, Y_NEG),	// dupe of X_90_Y_180!
	X_90_Z_270(Z_NEG, X_NEG, Y_POS),
	X_180_Z_270(Y_NEG, X_NEG, Z_NEG),
	X_270_Z_270(Z_POS, X_NEG, Y_NEG),
	NORMAL_MIRROR_X(X_NEG, Y_POS, Z_POS),
	NORMAL_MIRROR_Y(X_POS, Y_NEG, Z_POS),
	NORMAL_MIRROR_Z(X_POS, Y_POS, Z_NEG);

	public static final Rotation[] ROTATIONS_ONLY = { NORMAL, X_90, X_180, X_270, Y_90, Y_180, Y_270, Z_90, Z_180, Z_270,
			X_90_Y_90, X_180_Y_90, X_270_Y_90, X_90_Y_180, X_270_Y_180, X_90_Y_270, X_180_Y_270, X_270_Y_270,
			X_90_Z_90, X_180_Z_90, X_270_Z_90, X_90_Z_270, X_180_Z_270, X_270_Z_270 };

	public Expansion innerOrder;
	public Expansion middleOrder;
	public Expansion outerOrder;

	Rotation(Expansion inner, Expansion middle, Expansion outer) {
		innerOrder = inner;
		middleOrder = middle;
		outerOrder = outer;
	}
}
