package dev.bitbite.illustratio;

public class InputHandler {
	
	public void handleKeyboardInput(int key) {
		/*if(key == 81) Illustratio.model.setPosition(Illustratio.model.getPosition().x, Illustratio.model.getPosition().y, Illustratio.model.getPosition().z+0.1f);
		if(key == 87) Illustratio.model.setPosition(Illustratio.model.getPosition().x, Illustratio.model.getPosition().y+0.1f, Illustratio.model.getPosition().z);
		if(key == 69) Illustratio.model.setPosition(Illustratio.model.getPosition().x, Illustratio.model.getPosition().y, Illustratio.model.getPosition().z-0.1f);
		if(key == 65) Illustratio.model.setPosition(Illustratio.model.getPosition().x-0.1f, Illustratio.model.getPosition().y, Illustratio.model.getPosition().z);
		if(key == 83) Illustratio.model.setPosition(Illustratio.model.getPosition().x, Illustratio.model.getPosition().y-0.1f, Illustratio.model.getPosition().z);
		if(key == 68) Illustratio.model.setPosition(Illustratio.model.getPosition().x+0.1f, Illustratio.model.getPosition().y, Illustratio.model.getPosition().z);*/
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
