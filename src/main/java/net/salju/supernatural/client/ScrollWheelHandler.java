package net.salju.supernatural.client;

import org.joml.Vector2i;

public class ScrollWheelHandler {
	private double sX;
	private double sY;

	public Vector2i onMouseScroll(double dX, double dY) {
		if (this.sX != 0.0 && Math.signum(dX) != Math.signum(this.sX)) {
			this.sX = 0.0;
		}
		if (this.sY != 0.0 && Math.signum(dY) != Math.signum(this.sY)) {
			this.sY = 0.0;
		}
		this.sX += dX;
		this.sY += dY;
		int i = (int) this.sX;
		int j = (int) this.sY;
		if (i == 0 && j == 0) {
			return new Vector2i(0, 0);
		} else {
			this.sX -= i;
			this.sY -= j;
			return new Vector2i(i, j);
		}
	}

	public static int getNextScrollWheelSelection(double d, int s, int e) {
		int i = (int) Math.signum(d);
		s -= i;
		s = Math.max(-1, s);
		while (s < 0) {
			s += e;
		}
		while (s >= e) {
			s -= e;
		}
		return s;
	}
}