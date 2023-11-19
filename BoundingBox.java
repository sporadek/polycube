package org.radek.polycube;

import java.util.ArrayList;
import java.util.List;

public class BoundingBox {
	private Coordinate minCorner;
	private Coordinate maxCorner;
	private Expansion lastExpansion = Expansion.NO_CHANGE;
	private List<Expansion> stepOrder = new ArrayList<>(3);
	private Coordinate current;
	private Coordinate dimensions = new Coordinate();
	
	public BoundingBox() {
		minCorner = new Coordinate();
		maxCorner = new Coordinate();
		current = new Coordinate(minCorner);
	}
	
	public BoundingBox(BoundingBox original) {
		minCorner = new Coordinate(original.minCorner);
		maxCorner = new Coordinate(original.maxCorner);
		current = new Coordinate(minCorner);
	}

	public int volume() {
		return (maxCorner.x - minCorner.x + 1) * (maxCorner.y - minCorner.y + 1) * (maxCorner.z - minCorner.z + 1);
	}

	public int dimensionHash() {
//		int first = maxCorner.x - minCorner.x;
//		int second = maxCorner.y - minCorner.y;
//		int third = maxCorner.z - minCorner.z;
//		int max = Math.max(first, second);
//		int middle = Math.min(first, second);
//		dimensions.x = Math.min(middle, third);
//		middle = Math.max(middle, third);
//		dimensions.y = Math.min(max, middle);
//		dimensions.z = Math.max(max, middle);
		return dimensions.hashCode();
	}

	private int resetDimension(Expansion axis) {
		switch (axis) {
		case X_NEG:
			current.x = maxCorner.x;
			return maxCorner.x - minCorner.x;
		case X_POS:
			current.x = minCorner.x;
			return maxCorner.x - minCorner.x;
		case Y_NEG:
			current.y = maxCorner.y;
			return maxCorner.y - minCorner.y;
		case Y_POS:
			current.y = minCorner.y;
			return maxCorner.y - minCorner.y;
		case Z_NEG:
			current.z = maxCorner.z;
			return maxCorner.z - minCorner.z;
		case Z_POS:
			current.z = minCorner.z;
			return maxCorner.z - minCorner.z;
		default:
			return 0;
		}
	}

	public Coordinate initStep(Rotation orientation) {
		stepOrder.clear();
		stepOrder.add(orientation.innerOrder);
		dimensions.x = resetDimension(orientation.innerOrder);
		stepOrder.add(orientation.middleOrder);
		dimensions.y = resetDimension(orientation.middleOrder);
		stepOrder.add(orientation.outerOrder);
		dimensions.z = resetDimension(orientation.outerOrder);
		return current;
	}

	public Coordinate step() {
		for (Expansion axisDirection : stepOrder) {
			switch (axisDirection) {
			case X_NEG:
				if (current.x-- > minCorner.x) return current;
				break;
			case X_POS:
				if (current.x++ < maxCorner.x) return current;
				break;
			case Y_NEG:
				if (current.y-- > minCorner.y) return current;
				break;
			case Y_POS:
				if (current.y++ < maxCorner.y) return current;
				break;
			case Z_NEG:
				if (current.z-- > minCorner.z) return current;
				break;
			case Z_POS:
				if (current.z++ < maxCorner.z) return current;
				break;
			default:
				break;
			}
			resetDimension(axisDirection);
		}
		return current;
	}

	public void expand(Coordinate coordinate, Expansion expansion) {
		switch (expansion) {
		case X_NEG:
			lastExpansion = coordinate.x < minCorner.x ? expansion : Expansion.NO_CHANGE;
			minCorner.x = Math.min(minCorner.x, coordinate.x);
			break;
		case X_POS:
			lastExpansion = coordinate.x > maxCorner.x ? expansion : Expansion.NO_CHANGE;
			maxCorner.x = Math.max(maxCorner.x, coordinate.x);
			break;
		case Y_NEG:
			lastExpansion = coordinate.y < minCorner.y ? expansion : Expansion.NO_CHANGE;
			minCorner.y = Math.min(minCorner.y, coordinate.y);
			break;
		case Y_POS:
			lastExpansion = coordinate.y > maxCorner.y ? expansion : Expansion.NO_CHANGE;
			maxCorner.y = Math.max(maxCorner.y, coordinate.y);
			break;
		case Z_NEG:
			lastExpansion = coordinate.z < minCorner.z ? expansion : Expansion.NO_CHANGE;
			minCorner.z = Math.min(minCorner.z, coordinate.z);
			break;
		case Z_POS:
			lastExpansion = coordinate.z > maxCorner.z ? expansion : Expansion.NO_CHANGE;
			maxCorner.z = Math.max(maxCorner.z, coordinate.z);
			break;
		default:
			break;		
		}
	}

	public void contract() {
		switch (lastExpansion) {
		case X_NEG:
			minCorner.x++;
			break;
		case X_POS:
			maxCorner.x--;
			break;
		case Y_NEG:
			minCorner.y++;
			break;
		case Y_POS:
			maxCorner.y--;
			break;
		case Z_NEG:
			minCorner.z++;
			break;
		case Z_POS:
			maxCorner.z--;
			break;
		default:
			break;		
		}
	}
}
