package com.googlecode.bpmn_simulator.animation.element.visual;

import java.util.HashMap;
import java.util.Map;

public final class VisualElements {

	private static final Map<Class<? extends VisualElement>, Info> infos = new HashMap<>();

	private VisualElements() {
		super();
	}

	public static void register(final Class<? extends VisualElement> element, final Info info) {
		synchronized (infos) {
			if (infos.containsKey(element)) {
				throw new IllegalArgumentException("element " + element.getName() + " already exists");
			}
			if (info == null) {
				throw new NullPointerException();
			}
			infos.put(element, info);
		}
	}

	public static void register(final Class<? extends VisualElement> element, final int defaultBackgroundColor) {
		register(element, defaultBackgroundColor, Info.DEFAULT_FOREGROUND_COLOR);
	}

	public static void register(final Class<? extends VisualElement> element, final int defaultBackgroundColor, final int defaultForegroundColor) {
		register(element, new DefaultInfo(defaultBackgroundColor, defaultForegroundColor));
	}

	public static Info getInfo(final Class<? extends VisualElement> element) {
		synchronized (infos) {
			return infos.get(element);
		}
	}

	public static int getDefaultBackgroundColor(final Class<? extends VisualElement> element) {
		final Info info = getInfo(element);
		if (info != null) {
			return info.getDefaultBackgroundColor();
		}
		return Info.DEFAULT_BACKGROUND_COLOR;
	}

	public static int getDefaultForegroundColor(final Class<? extends VisualElement> element) {
		final Info info = getInfo(element);
		if (info != null) {
			return info.getDefaultForegroundColor();
		}
		return Info.DEFAULT_FOREGROUND_COLOR;
	}

	public interface Info {

		int BLACK = 0x000000;
		int WHITE = 0xffffff;
		int GRAY = 0xEEEEEE;

		int YELLOW = 0xFFFFB5;
		int ORANGE = 0xFFD062;
		int RED = 0xFFA4A4;
		int GREEN = 0xA4F0B7;
		int BLUE = 0xDBF0F7;

		int DEFAULT_FOREGROUND_COLOR = BLACK;
		int DEFAULT_BACKGROUND_COLOR = WHITE;

		int getDefaultForegroundColor();

		int getDefaultBackgroundColor();

	}

	private static class DefaultInfo
			implements Info {

		private final int defaultForegroundColor;
		private final int defaultBackgroundColor;

		public DefaultInfo(final int defaultBackgroundColor, final int defaultForegroundColor) {
			super();
			this.defaultForegroundColor = defaultForegroundColor;
			this.defaultBackgroundColor = defaultBackgroundColor;
		}

		@Override
		public int getDefaultForegroundColor() {
			return defaultForegroundColor;
		}

		@Override
		public int getDefaultBackgroundColor() {
			return defaultBackgroundColor;
		}

	}

}
