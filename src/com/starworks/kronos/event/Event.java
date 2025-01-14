package com.starworks.kronos.event;

/*
 * Represents an event. GLFW
 */
public sealed abstract class Event permits Event.KeyPressed, Event.KeyReleased, Event.KeyRepeated, Event.KeyTyped, Event.MouseButtonPressed,
										   Event.MouseButtonReleased, Event.MouseMoved, Event.MouseScrolled, Event.WindowResized, Event.WindowClosed,
										   Event.WindowMoved, Event.WindowFocusGained, Event.WindowFocusLost, Event.WindowMinimized, Event.WindowRestored,
										   Event.WindowMaximized, Event.WindowUnmaximized, Event.WindowRefreshed {

	public static final int CATEGORY_NONE = 0;
	public static final int CATEGORY_INPUT = 1;
	public static final int CATEGORY_KEYBOARD = 2;
	public static final int CATEGORY_MOUSE = 4;
	public static final int CATEGORY_WINDOW = 8;

	protected final EventType m_type;
	protected final long m_timestamp;
	protected boolean m_handled;

	protected Event(EventType type, long timestamp) {
		this.m_type = type;
		this.m_timestamp = timestamp;
	}

	public static int getCategory(Class<? extends Event> eventType) {
		if (eventType == KeyPressed.class) return CATEGORY_INPUT | CATEGORY_KEYBOARD;
		else if (eventType == KeyReleased.class) return CATEGORY_INPUT | CATEGORY_KEYBOARD;
		else if (eventType == KeyRepeated.class) return CATEGORY_INPUT | CATEGORY_KEYBOARD;
		else if (eventType == KeyTyped.class) return CATEGORY_INPUT | CATEGORY_KEYBOARD;
		else if (eventType == MouseButtonPressed.class) return CATEGORY_INPUT | CATEGORY_MOUSE;
		else if (eventType == MouseButtonReleased.class) return CATEGORY_INPUT | CATEGORY_MOUSE;
		else if (eventType == MouseMoved.class) return CATEGORY_INPUT | CATEGORY_MOUSE;
		else if (eventType == MouseScrolled.class) return CATEGORY_INPUT | CATEGORY_MOUSE;
		else if (eventType == WindowResized.class) return CATEGORY_WINDOW;
		else if (eventType == WindowClosed.class) return CATEGORY_WINDOW;
		else if (eventType == WindowMoved.class) return CATEGORY_WINDOW;
		else if (eventType == WindowFocusGained.class) return CATEGORY_WINDOW;
		else if (eventType == WindowFocusLost.class) return CATEGORY_WINDOW;
		else if (eventType == WindowMinimized.class) return CATEGORY_WINDOW;
		else if (eventType == WindowRestored.class) return CATEGORY_WINDOW;
		else if (eventType == WindowMaximized.class) return CATEGORY_WINDOW;
		else if (eventType == WindowUnmaximized.class) return CATEGORY_WINDOW;
		else if (eventType == WindowRefreshed.class) return CATEGORY_WINDOW;
		return CATEGORY_NONE;
	}
	
	public final boolean isInCategory(int category) {
		return (getCategory() & category) > 0;
	}
	
	public final EventType getType() {
		return m_type;
	}

	public final int getCategory() {
		return getCategory(this.getClass());
	}

	public final long getTimestamp() {
		return m_timestamp;
	}

	public final boolean wasHandled() {
		return m_handled;
	}

	public final boolean setHandled(boolean handled) {
		return m_handled = handled;
	}

	@Override
	public abstract Event clone();

	public static final class KeyPressed extends Event {

		private final int m_keyCode;
		private final int m_scancode;
		private final int m_mods;

		public KeyPressed(int keyCode, int scancode, int mods, long timestamp) {
			super(EventType.KEY_PRESSED, timestamp);
			this.m_keyCode = keyCode;
			this.m_scancode = scancode;
			this.m_mods = mods;
		}

		public int getKeyCode() {
			return m_keyCode;
		}

		public int getScancode() {
			return m_scancode;
		}

		public int getMods() {
			return m_mods;
		}

		@Override
		public KeyPressed clone() {
			return new KeyPressed(m_keyCode, m_scancode, m_mods, m_timestamp);
		}
	}

	public static final class KeyReleased extends Event {

		private final int m_keyCode;
		private final int m_scancode;
		private final int m_mods;

		public KeyReleased(int keyCode, int scancode, int mods, long timestamp) {
			super(EventType.KEY_RELEASED, timestamp);
			this.m_keyCode = keyCode;
			this.m_scancode = scancode;
			this.m_mods = mods;
		}

		public int getKeyCode() {
			return m_keyCode;
		}

		public int getScancode() {
			return m_scancode;
		}

		public int getMods() {
			return m_mods;
		}

		@Override
		public KeyReleased clone() {
			return new KeyReleased(m_keyCode, m_scancode, m_mods, m_timestamp);
		}
	}

	public static final class KeyRepeated extends Event {

		private final int m_keyCode;
		private final int m_scancode;
		private final int m_mods;

		public KeyRepeated(int keyCode, int scancode, int mods, long timestamp) {
			super(EventType.KEY_REPEATED, timestamp);
			this.m_keyCode = keyCode;
			this.m_scancode = scancode;
			this.m_mods = mods;
		}

		public int getKeyCode() {
			return m_keyCode;
		}

		public int getScancode() {
			return m_scancode;
		}

		public int getMods() {
			return m_mods;
		}

		@Override
		public KeyRepeated clone() {
			return new KeyRepeated(m_keyCode, m_scancode, m_mods, m_timestamp);
		}
	}

	public static final class KeyTyped extends Event {

		private final int m_keyCode;
		private final int m_mods;

		public KeyTyped(int keyCode, int mods, long timestamp) {
			super(EventType.KEY_TYPED, timestamp);
			this.m_keyCode = keyCode;
			this.m_mods = mods;
		}

		public int getKeyCode() {
			return m_keyCode;
		}

		public int getMods() {
			return m_mods;
		}

		@Override
		public KeyTyped clone() {
			return new KeyTyped(m_keyCode, m_mods, m_timestamp);
		}
	}

	public static final class MouseButtonPressed extends Event {

		private final int m_button;
		private final int m_mods;

		public MouseButtonPressed(int button, int mods, long timestamp) {
			super(EventType.MOUSE_PRESSED, timestamp);
			this.m_button = button;
			this.m_mods = mods;
		}

		public int getButton() {
			return m_button;
		}

		public int getMods() {
			return m_mods;
		}

		@Override
		public MouseButtonPressed clone() {
			return new MouseButtonPressed(m_button, m_mods, m_timestamp);
		}
	}

	public static final class MouseButtonReleased extends Event {

		private final int m_button;
		private final int m_mods;

		public MouseButtonReleased(int button, int mods, long timestamp) {
			super(EventType.MOUSE_RELEASED, timestamp);
			this.m_button = button;
			this.m_mods = mods;
		}

		public int getButton() {
			return m_button;
		}

		public int getMods() {
			return m_mods;
		}

		@Override
		public MouseButtonReleased clone() {
			return new MouseButtonReleased(m_button, m_mods, m_timestamp);
		}
	}

	public static final class MouseMoved extends Event {

		private final double m_xPosition;
		private final double m_yPosition;

		public MouseMoved(double xPosition, double yPosition, long timestamp) {
			super(EventType.MOUSE_MOVED, timestamp);
			this.m_xPosition = xPosition;
			this.m_yPosition = yPosition;
		}

		public double getXPosition() {
			return m_xPosition;
		}

		public double getYPosition() {
			return m_yPosition;
		}

		@Override
		public MouseMoved clone() {
			return new MouseMoved(m_xPosition, m_yPosition, m_timestamp);
		}
	}

	public static final class MouseScrolled extends Event {

		private final double m_xOffset;
		private final double m_yOffset;

		public MouseScrolled(double xOffset, double yOffset, long timestamp) {
			super(EventType.MOUSE_SCROLLED, timestamp);
			this.m_xOffset = xOffset;
			this.m_yOffset = yOffset;
		}

		public double getXOffset() {
			return m_xOffset;
		}

		public double getYOffset() {
			return m_yOffset;
		}

		@Override
		public MouseScrolled clone() {
			return new MouseScrolled(m_xOffset, m_yOffset, m_timestamp);
		}
	}

	public static final class WindowResized extends Event {

		private final int m_width;
		private final int m_height;

		public WindowResized(int width, int height, long timestamp) {
			super(EventType.WINDOW_RESIZED, timestamp);
			this.m_width = width;
			this.m_height = height;
		}

		public int getWidth() {
			return m_width;
		}

		public int getHeight() {
			return m_height;
		}

		@Override
		public WindowResized clone() {
			return new WindowResized(m_width, m_height, m_timestamp);
		}
	}

	public static final class WindowClosed extends Event {

		public WindowClosed(long timestamp) {
			super(EventType.WINDOW_CLOSED, timestamp);
		}

		@Override
		public WindowClosed clone() {
			return new WindowClosed(m_timestamp);
		}
	}

	public static final class WindowMoved extends Event {

		private final int m_xPosition;
		private final int m_yPosition;

		public WindowMoved(int xPosition, int yPosition, long timestamp) {
			super(EventType.WINDOW_MOVED, timestamp);
			this.m_xPosition = xPosition;
			this.m_yPosition = yPosition;
		}

		public int getXPosition() {
			return m_xPosition;
		}

		public int getYPosition() {
			return m_yPosition;
		}

		@Override
		public WindowMoved clone() {
			return new WindowMoved(m_xPosition, m_yPosition, m_timestamp);
		}
	}

	public static final class WindowFocusGained extends Event {

		public WindowFocusGained(long timestamp) {
			super(EventType.WINDOW_FOCUS_GAINED, timestamp);
		}

		@Override
		public WindowFocusGained clone() {
			return new WindowFocusGained(m_timestamp);
		}
	}

	public static final class WindowFocusLost extends Event {

		public WindowFocusLost(long timestamp) {
			super(EventType.WINDOW_FOCUS_LOST, timestamp);
		}

		@Override
		public WindowFocusLost clone() {
			return new WindowFocusLost(m_timestamp);
		}
	}

	public static final class WindowMinimized extends Event {

		public WindowMinimized(long timestamp) {
			super(EventType.WINDOW_MINIMIZED, timestamp);
		}

		@Override
		public WindowMinimized clone() {
			return new WindowMinimized(m_timestamp);
		}
	}

	public static final class WindowRestored extends Event {

		public WindowRestored(long timestamp) {
			super(EventType.WINDOW_RESTORED, timestamp);
		}

		@Override
		public WindowRestored clone() {
			return new WindowRestored(m_timestamp);
		}
	}

	public static final class WindowMaximized extends Event {

		public WindowMaximized(long timestamp) {
			super(EventType.WINDOW_MAXIMIZED, timestamp);
		}

		@Override
		public WindowMaximized clone() {
			return new WindowMaximized(m_timestamp);
		}
	}

	public static final class WindowUnmaximized extends Event {

		public WindowUnmaximized(long timestamp) {
			super(EventType.WINDOW_UNMAXIMIZED, timestamp);
		}

		@Override
		public WindowUnmaximized clone() {
			return new WindowUnmaximized(m_timestamp);
		}
	}

	public static final class WindowRefreshed extends Event {

		public WindowRefreshed(long timestamp) {
			super(EventType.WINDOW_REFRESHED, timestamp);
		}

		@Override
		public WindowRefreshed clone() {
			return new WindowRefreshed(m_timestamp);
		}
	}
}
