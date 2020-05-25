package sim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class SimWorker implements Runnable {

	private ExecutorService service = null;
	private boolean running = false;
	private String name;

	public SimWorker(ExecutorService service, String name) {
		this.service = service;
		this.name = name;
	}

	abstract protected boolean work();

	abstract protected void shutdown();

	public void start() {
		service.submit(this);
	}

	public String name() {
		return name;
	}

	public void stop() {
		running = false;
		try {
			service.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			System.out.println("Worker interupted");
			service.shutdownNow();
		}
	}

	@Override
	public void run() {
		System.out.println("Starting " + this);
		running = true;

			while (running && work());

		running = false;
		System.out.println("Stopping " + this);
		shutdown();
	}

	public boolean isRunning(){
		return running;
	}

	public String toString() {
		return "Worker:" + name;
	}
}