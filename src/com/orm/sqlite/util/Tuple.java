package com.orm.sqlite.util;

public class Tuple<P1, P2> {
	private P1 first;
	private P2 second;

	public Tuple(P1 first, P2 second) {
		super();
		this.first = first;
		this.second = second;
	}

	public P1 getFirst() {
		return first;
	}

	public void setFirst(P1 first) {
		this.first = first;
	}

	public P2 getSecond() {
		return second;
	}

	public void setSecond(P2 second) {
		this.second = second;
	}

}
