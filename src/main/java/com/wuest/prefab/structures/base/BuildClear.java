package com.wuest.prefab.structures.base;

import com.google.gson.annotations.Expose;

/**
 * Defines a shape which should have all of it's blocks replaced with air.
 *
 * @author WuestMan
 */
public class BuildClear {
	@Expose
	private BuildShape shape;

	@Expose
	private PositionOffset startingPosition;

	public BuildClear() {
		this.Initialize();
	}

	public BuildShape getShape() {
		return this.shape;
	}

	public void setShape(BuildShape value) {
		this.shape = value;
	}

	public PositionOffset getStartingPosition() {
		return this.startingPosition;
	}

	public void setStartingPosition(PositionOffset value) {
		this.startingPosition = value;
	}

	public void Initialize() {
		this.shape = new BuildShape();
		this.startingPosition = new PositionOffset();
	}
}
