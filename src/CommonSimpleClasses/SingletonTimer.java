package CommonSimpleClasses;

import java.util.Timer;

public class SingletonTimer extends Timer {
	
	static SingletonTimer instance = null;
	
	private SingletonTimer(){super();}
	
	static synchronized public SingletonTimer getInstance() {
		if (instance == null) instance = new SingletonTimer();
		return instance;
	}
}
