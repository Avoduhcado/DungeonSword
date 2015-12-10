package core.setups;

import java.util.ArrayList;

import core.ui.UIElement;

public interface GameSetup {

	ArrayList<UIElement> uiElements = new ArrayList<UIElement>();
	
	/** Update the current game state */
	public void update();
	/** Draw the current game state */
	public void draw();
	/** Draw the current state UI */
	public void drawUI();
	
	public default ArrayList<UIElement> getUI() {
		return uiElements;
	}
	
	public default UIElement getElement(int index) {
		return uiElements.get(index);
	}
	
	public default void addUI(UIElement element) {
		uiElements.add(element);
	}
	
}
