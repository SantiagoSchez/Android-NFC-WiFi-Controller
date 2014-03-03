package sanchezsobrino.multimedia.anwc.business;

import java.awt.AWTException;
import java.awt.Robot;

public class KeySimulator {
	private Robot robot;
	
	public KeySimulator() throws AWTException {
		this.robot = new Robot();
	}
	
	public void simulateKeyPress(int key) {
		robot.keyPress(key);
		robot.keyRelease(key);
	}
}
