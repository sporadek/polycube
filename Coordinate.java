package org.radek.polycube;

public class Coordinate {
	public int x;
	public int y;
	public int z;
	
	public Coordinate() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Coordinate(Coordinate original) {
		this.x = original.x;
		this.y = original.y;
		this.z = original.z;
	}

	public void reset(Coordinate original) {
		this.x = original.x;
		this.y = original.y;
		this.z = original.z;
	}

	public void migrate(Expansion expansion) {
		switch (expansion) {
		case X_NEG:
			this.x--;
			break;
		case X_POS:
			this.x++;
			break;
		case Y_NEG:
			this.y--;
			break;
		case Y_POS:
			this.y++;
			break;
		case Z_NEG:
			this.z--;
			break;
		case Z_POS:
			this.z++;
			break;
		default:
			break;
		}
	}

	public void unmigrate(Expansion expansion) {
		switch (expansion) {
		case X_NEG:
			this.x++;
			break;
		case X_POS:
			this.x--;
			break;
		case Y_NEG:
			this.y++;
			break;
		case Y_POS:
			this.y--;
			break;
		case Z_NEG:
			this.z++;
			break;
		case Z_POS:
			this.z--;
			break;
		default:
			break;
		}
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Coordinate &&
				((Coordinate) other).x == this.x &&
				((Coordinate) other).y == this.y &&
				((Coordinate) other).z == this.z;
	}

	@Override
	public int hashCode() {
		return 1024 * x + 32 * y + z;
	}
}
