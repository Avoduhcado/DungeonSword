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
	public default void drawUI() {
		for(UIElement ui : uiElements) {
			ui.draw();
		}
	}
	
	public default ArrayList<UIElement> getUI() {
		return uiElements;
	}
	
	public default UIElement getElement(int index) {
		return uiElements.get(index);
	}
	
	public default boolean removeElement(UIElement element) {
		return uiElements.remove(element);
	}
	
	public default void addUI(UIElement element) {
		uiElements.add(element);
	}
	
}
