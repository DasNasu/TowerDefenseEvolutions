package dev.bitbite.illustratio;

public class InputHandler {
	
	public void handleKeyboardInput(int key) {
		//System.out.println(key+" got pressed");
	}
	
	public void handleMouseMovement(double x, double y) {
		//System.out.println("moved to x: "+x+", y: "+y);
	}
	
	public void handleMouseInput(int button) {
		//System.out.println(button+" got pressed");
	}
	
	public void handleMouseScroll(double x, double y) {
		if(y > 0) {
			//System.out.println("scrolled up");
		} else {
			//System.out.println("scrolled down");
		}
	}
}
