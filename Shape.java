package org.radek.polycube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.radek.polycube.Expansion.X_NEG;
import static org.radek.polycube.Expansion.X_POS;
import static org.radek.polycube.Expansion.Y_NEG;
import static org.radek.polycube.Expansion.Y_POS;
import static org.radek.polycube.Expansion.Z_NEG;
import static org.radek.polycube.Expansion.Z_POS;

/**
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Shape implements Comparable<Shape> {
	public static final int MAX_SIZE = 20;
	
	private static final List<Expansion> DIRECTIONS = List.of(X_POS, Y_POS, Z_POS, Z_NEG, Y_NEG, X_NEG);

	private BoundingBox bounds;
	private Set<Coordinate> cubeSet = new HashSet<>(MAX_SIZE);
	private List<Coordinate> cubeList = new ArrayList<>(MAX_SIZE);
	private List<Integer> canonical = new ArrayList<>(2 * MAX_SIZE);
	private Integer hash = 0;
	
	public Shape() {
		bounds = new BoundingBox();
		Coordinate base = new Coordinate();
		cubeSet.add(base);
		cubeList.add(base);
	}

	public Shape(Shape original) {
		bounds = new BoundingBox(original.bounds);
		cubeSet.addAll(original.cubeSet);
		cubeList.addAll(original.cubeList);
	}

	public List<Coordinate> getCubeList() {
		return cubeList;
	}

	public boolean addCube(Coordinate cube, Expansion expansion) {
		if (cubeSet.add(cube)) {
			bounds.expand(cube, expansion);
			cubeList.add(cube);
			return true;
		}
		return false;
	}

	public void removeLastCube(Coordinate cube) {
		cubeSet.remove(cube);
		bounds.contract();
		cubeList.remove(cubeList.size() - 1);
	}

	private void buildOrientedArray(List<Integer> array, int volume, int dimHash, Rotation orientation) {
		array.clear();
		int count = 0;
		Coordinate loc = bounds.initStep(orientation);
		for (int v = 0; v < volume; v++, loc = bounds.step()) {
			if (cubeSet.contains(loc)) {
				if (count >= 0) {
					count++;
					continue;
				}
			} else {
				if (count <= 0) {
					count--;
					continue;
				}
			}
			array.add(count);
			if (canonical.size() > array.size()) {
				int index = array.size() - 1;
				if (canonical.get(index) > count) {
					return;
				} else if (canonical.get(index) < count) {
					canonical.clear();
				}
			}
			count = 0;
		}
		if (count > 0) {
			array.add(count);
		}
		int index = array.size();
		array.add(bounds.dimensionHash());
		if (canonical.size() == index + 1 && canonical.get(index) >= array.get(index)) {
			return;
		}
		canonical.clear();
		canonical.addAll(array);
		hash = canonical.hashCode();
	}

	public void calculateCanonical(boolean includeReflection) {
		canonical.clear();
		List<Integer> array = new ArrayList<>(2 * MAX_SIZE);
		int volume = bounds.volume();
		int dimHash = bounds.dimensionHash();
		for (Rotation orientation : includeReflection ? Rotation.values() : Rotation.ROTATIONS_ONLY) {
			buildOrientedArray(array, volume, dimHash, orientation);
		}
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Shape &&
				canonical.equals(((Shape) other).canonical);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public int compareTo(Shape o) {
		return hash.compareTo(o.hash);
	}

	public static void expandShape(Shape shape, Set<Shape> shapeSet, boolean includeReflection) {
		Shape expanded = new Shape(shape);
		Coordinate newCube = new Coordinate();
		for (Coordinate cube : shape.getCubeList()) {
			newCube.reset(cube);
			for (Expansion direction : DIRECTIONS) {
				newCube.migrate(direction);
				if (expanded.addCube(newCube, direction)) {
					expanded.calculateCanonical(includeReflection);
					if (shapeSet.add(expanded)) {
						expanded = new Shape(shape);
						newCube = new Coordinate(cube);
						continue;
					}
					expanded.removeLastCube(newCube);
				}
				newCube.unmigrate(direction);
			}
		}
	}

	public static void displayShapes(long ms, int size, Set<Shape> shapes) {
		System.out.println("Took " + ms + " ms to find " + shapes.size() + " shapes of size " + size + ":");
		if (size != 5) return;
		for (Shape shape : shapes) {
			System.out.print("  " + shape.hash + ": ");
			for (Coordinate cube : shape.cubeList) {
				System.out.print("(" + cube.x + "," + cube.y + "," + cube.z + ") ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) throws IOException {
		int size = 1;
    	Set<Shape> shapes = Set.of(new Shape());
    	Set<Shape> largerShapes = new ConcurrentSkipListSet<>();
    	long ms = 0;
    	for (; size < 10; size++) {
    		displayShapes(ms, size, shapes);
    		ms = System.currentTimeMillis();
    		for (Shape shape : shapes) {
        		expandShape(shape, largerShapes, false);    			
    		}
    		ms = System.currentTimeMillis() - ms;
        	shapes = largerShapes;
        	largerShapes = new ConcurrentSkipListSet<>();
    	}
    	displayShapes(ms, size, shapes);
    }
}
