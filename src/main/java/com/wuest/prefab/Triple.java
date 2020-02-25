package com.wuest.prefab;

public class Triple<A, B, C> {
	private A first;
	private B second;
	private C third;

	public Triple(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public A getFirst() {
		return this.first;
	}

	public B getSecond() {
		return this.second;
	}

	public C getThird() {
		return this.third;
	}
}
