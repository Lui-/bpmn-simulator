/*
 * Copyright (C) 2012 Stefan Schweitzer
 *
 * This software was created by Stefan Schweitzer as a student's project at
 * Fachhochschule Kaiserslautern (University of Applied Sciences).
 * Supervisor: Professor Dr. Thomas Allweyer. For more information please see
 * http://www.fh-kl.de/~allweyer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this Software except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.bpmn_simulator.framework.element.visual.geometry;

import java.awt.Polygon;
import java.awt.geom.GeneralPath;


public final class GeometryUtil {

	public static final double RAD_FULL = 2. * Math.PI;
	public static final int PENTAGON_CORNERS = 5;

	private GeometryUtil() {
		super();
	}

	public static Waypoint polarToCartesian(final Waypoint orgin,
			final double radius, final double angle) {
		final int x = (int)Math.round(radius * Math.sin(angle));
		final int y = (int)Math.round(radius * Math.cos(angle));
		return new Waypoint(orgin.x + x, orgin.y + y);
	}

	public static double getAngle(final Waypoint from, final Waypoint to) {
		return Math.atan2(to.x - from.x, to.y - from.y);
	}

	public static Polygon createPentagon(final Bounds size) {
		final Polygon polygon = new Polygon();
		final Waypoint center = new Waypoint(size.x + size.width / 2, size.y + size.height / 2);
		final double r = size.width / 2.;
		Waypoint point = null;
		for (int i = 0; i < 5; ++i) {
			point = polarToCartesian(
					center, r,
					(RAD_FULL / GeometryUtil.PENTAGON_CORNERS) * i
							- (RAD_FULL / GeometryUtil.PENTAGON_CORNERS) / 2.);
			polygon.addPoint(point.x, point.y);
		}
		return polygon;
	}

	public static Polygon createStar(final Bounds size, final int corners) {
		final Polygon polygon = new Polygon();
		final Waypoint center = new Waypoint(size.x + size.width / 2, size.y + size.height / 2);
		final double r = size.width / 2.;
		Waypoint point = null;
		for (int i = 0; i < corners; ++i) {
			point = polarToCartesian(center, r, (RAD_FULL / corners) * i - (RAD_FULL / corners) / 2.);
			polygon.addPoint(point.x, point.y);
			point = polarToCartesian(center, r * 0.5, (RAD_FULL / corners) * i);
			polygon.addPoint(point.x, point.y);
		}
		return polygon;
	}

	public static Polygon createDiamond(final Bounds size) {
		final Polygon polygon = new Polygon();
		polygon.addPoint((int)size.getMinX(), (int)size.getCenterY());
		polygon.addPoint((int)size.getCenterX(), (int)size.getMinY());
		polygon.addPoint((int)size.getMaxX(), (int)size.getCenterY());
		polygon.addPoint((int)size.getCenterX(), (int)size.getMaxY());
		return polygon;
	}

	public static GeneralPath createArrowPath(final Waypoint from, final Waypoint to,
			final double d, final double length) {
		final GeneralPath path = new GeneralPath();
		final double angle = getAngle(to, from);
		final Waypoint point1 = polarToCartesian(to, length, angle - d);
		path.moveTo(point1.x, point1.y);
		path.lineTo(to.x, to.y);
		final Waypoint point2 = polarToCartesian(to, length, angle + d);
		path.lineTo(point2.x, point2.y);
		return path;
	}

}
