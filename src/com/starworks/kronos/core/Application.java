package com.starworks.kronos.core;

import org.lwjgl.opengl.GL11;

import com.starworks.kronos.Configuration;
import com.starworks.kronos.core.imgui.ImGuiLayer;
import com.starworks.kronos.event.Event;
import com.starworks.kronos.event.EventCallback;
import com.starworks.kronos.event.EventManager;
import com.starworks.kronos.exception.KronosRuntimeException;
import com.starworks.kronos.files.FileSystem;
import com.starworks.kronos.input.InputManager;
import com.starworks.kronos.jobs.Job;
import com.starworks.kronos.jobs.JobManager;
import com.starworks.kronos.locale.StringTable;
import com.starworks.kronos.logging.Logger;
import com.starworks.kronos.scene.Scene;
import com.starworks.kronos.scene.SceneManager;
import com.starworks.kronos.toolkit.concurrent.ArrivalGate;

public abstract class Application implements AutoCloseable {
	private final Logger LOGGER = Logger.getLogger(Application.class);

	private static final ArrivalGate s_gate = new ArrivalGate(1);
	private static volatile Application s_instance = null;

	private static final double NANOS_PER_SECOND = 1e9;

	protected final EventManager m_eventManager;
	protected final Window m_window;
	protected final LayerStack m_layerStack;
	protected final ImGuiLayer m_imGuiLayer;
	protected final JobManager m_jobManager;
	protected final InputManager m_inputManager;
	protected final Scene m_scene;
	private final TimeStep m_timeStep;
	private final TimeStep m_fixedTimeStep;
	private final double m_fixedUpdateRate;
	private long m_currentTime;
	private double m_deltaTime;
	private long m_lastTime;
	protected boolean m_running;

	protected Application() {
		try {
			s_gate.arrive();
		} catch (InterruptedException e) {
			throw new KronosRuntimeException("Application failed to initialize", e);
		}
		Application.s_instance = this;

		int width = Configuration.window.width();
		int height = Configuration.window.height();
		String title = Configuration.window.title();
		double updateRate = Configuration.runtime.updateRate();
		double fixedUpdateRate = Configuration.runtime.fixedUpdateRate();
		this.m_eventManager = new EventManager();
		this.m_window = Window.create(width, height, title).onEvent(this::onEvent);
		this.m_layerStack = new LayerStack();
		this.m_imGuiLayer = new ImGuiLayer();
		this.m_jobManager = JobManager.create();
		this.m_inputManager = new InputManager(m_eventManager);
		this.m_scene = SceneManager.INSTANCE.getCurrentScene();
		this.m_timeStep = new TimeStep(updateRate);
		this.m_fixedTimeStep = new TimeStep(fixedUpdateRate);
		this.m_fixedUpdateRate = fixedUpdateRate;
		this.m_lastTime = 0;
		this.m_running = false;

		registerEvents();
		addOverlay(m_imGuiLayer);
		m_jobManager.start();
		initialize();
	}
	
	protected final synchronized void start() {
		LOGGER.info("Application starting");
		m_running = true;
		m_lastTime = System.nanoTime();
		loop();
	}
	
	protected abstract void initialize();

	protected final synchronized boolean stop() {
		LOGGER.info("Application stopping");
		m_running = false;
		return false;
	}

	private final boolean onEvent(final Event event) {
		if (m_layerStack.onEvent(event)) {
			return true;
		}
		return m_eventManager.post(event);
	}

	protected void registerEvents() {
		registerEventListener(Event.WindowClosed.class, e -> stop());
	}

	public final <E extends Event> void registerEventListener(final Class<E> eventType, final EventCallback<E> callback) {
		m_eventManager.register(eventType, callback);
	}

	public final <E extends Event> void unregisterEventListener(final Class<E> eventType, final EventCallback<E> callback) {
		m_eventManager.unregister(eventType, callback);
	}

	public final void addLayer(final Layer layer) {
		m_layerStack.pushLayer(layer);
	}

	public final void addOverlay(final Layer overlay) {
		m_layerStack.pushOverlay(overlay);
	}

	public final void submitJob(final Job<?> job) {
		m_jobManager.submit(job);
	}
	
	public final void loop() {
		try {
			for (; m_running || !m_window.shouldClose();) {
				time();

				GL11.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

				render();
				fixedUpdate(m_fixedTimeStep.getDeltaTime());
				update(m_timeStep.getDeltaTime());

				m_layerStack.update(m_timeStep);

				m_imGuiLayer.begin();
				m_layerStack.imGuiRender();
				m_imGuiLayer.end();

				m_window.update();
			}
		} catch (Exception e) {
			LOGGER.error("Unhandeled exception propagated to main loop: ", e);
			stop();
		}
	}

	private final void time() {
		m_currentTime = System.nanoTime();
		m_deltaTime = (m_currentTime - m_lastTime) / NANOS_PER_SECOND;
		m_lastTime = m_currentTime;
		m_timeStep.update(m_deltaTime);
		m_fixedTimeStep.update(m_fixedUpdateRate);
	}

	protected void update(double dt) {
		m_scene.onUpdate(m_timeStep);
	}

	protected void fixedUpdate(double dt) {
		m_scene.onFixedUpdate(m_fixedTimeStep);
	}

	protected void render() {
		m_scene.onRender();
	}

	@Override
	public void close() {
		m_window.close();
		m_jobManager.shutdown();
		StringTable.INSTANCE.shutdown();
		LOGGER.close();
		FileSystem.INSTANCE.shutdown();
	}

	public static final Application get() {
		if (s_instance == null) {
			throw new IllegalStateException("Application referenced before initialization");
		}
		return s_instance;
	}

	public Window getWindow() {
		return m_window;
	}

	public InputManager getInputManager() {
		return m_inputManager;
	}
}
