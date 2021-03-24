package com.wuest.prefab;

public class Tuple<A, B> {

	private final A first;
	private final B second;

	public Tuple(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		return this.first;
	}

	public B getSecond() {
		return this.second;
	}
}