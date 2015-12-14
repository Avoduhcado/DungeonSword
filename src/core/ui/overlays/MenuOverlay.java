package core.ui.overlays;

import java.util.ArrayList;

import core.ui.ElementGroup;
import core.ui.UIElement;
import core.ui.utils.UIContainer;
import core.utilities.keyboard.Keybind;

public abstract class MenuOverlay extends ElementGroup<UIElement> implements UIContainer {

	protected boolean toClose;
	
	public boolean isCloseRequest() {
		return toClose || Keybind.EXIT.clicked();
	}

	@Override
	public void drawUI() {
		for(UIElement ui : uiElements) {
			ui.draw();
		}
	}

	@Override
	public ArrayList<UIElement> getUI() {
		return uiElements;
	}

	@Override
	public UIElement getElement(int index) {
		return uiElements.get(index);
	}

	@Override
	public boolean removeElement(UIElement element) {
		return uiElements.remove(element);
	}

	@Override
	public void addUI(UIElement element) {
		element.setContainer(this);
		uiElements.add(element);
	}

	@Override
	public void addUI(UIElement element, int index) {
		element.setContainer(this);
		uiElements.add(index, element);
	}

}
