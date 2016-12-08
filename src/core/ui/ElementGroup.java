package core.ui;

import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.util.vector.Vector2f;

import core.ui.event.ActionEvent;
import core.ui.event.KeyEvent;
import core.ui.event.KeybindEvent;
import core.ui.event.KeybindListener;
import core.ui.event.MouseEvent;
import core.ui.event.MouseListener;
import core.ui.event.MouseMotionListener;
import core.ui.event.TimeEvent;
import core.ui.event.TimeListener;
import core.ui.event.UIEvent;
import core.ui.utils.Accessible;
import core.ui.utils.UIBounds;
import core.utilities.MathUtils;

public class ElementGroup<T extends UIElement> extends UIElement {
	
	protected ArrayList<T> uiElements = new ArrayList<T>();
	
	private boolean singleSelection = true;
	protected int selection = -1;
	
	private SelectionPointer pointer;
	
	private KeybindListener keybindListener;
	
	public ElementGroup() {
		addMouseListener(new DefaultMouseAdapter());
		addMouseMotionListener(new DefaultMouseMotionAdapter());
		addKeybindListener(new DefaultKeybindAdapter());
	}

	public void draw() {
		super.draw();
		
		for(UIElement e : uiElements) {
			e.draw();
		}
		
		if(pointer != null) {
			pointer.draw();
		}
	}
	
	public void setEnabledAll(boolean enabled) {
		for(UIElement e : uiElements) {
			e.setState(enabled ? ENABLED : DISABLED);
		}
	}
	
	public void setEnabledAllExcept(boolean enabled, UIElement except) {
		for(UIElement e : uiElements) {
			if(e != except) {
				e.setState(enabled ? ENABLED : DISABLED);
			}
		}
	}
	
	public void setEnabledAllExcept(boolean enabled, int index) {
		for(int i = 0; i<size(); i++) {
			if(i != index) {
				get(i).setState(enabled ? ENABLED : DISABLED);
			}
		}
	}
	
	public void setFocus(Accessible focus) {
		for(UIElement e : uiElements) {
			if(e instanceof Accessible && e != focus) {
				e.setState(DISABLED);
			}
		}
	}
	
	public boolean isSingleSelection() {
		return singleSelection;
	}

	public void setSingleSelection(boolean singleSelection) {
		this.singleSelection = singleSelection;
	}
	
	public void setSelectionPointer(String pointerName) {
		this.pointer = new SelectionPointer(pointerName);
		
		if(selection != -1) {
			pointer.setPositionAt(get(selection));
		}
	}
	
	public void setKeyboardNavigable(boolean enabled, UIElement startIndex) {
		if(selection != -1) {
			get(selection).setSelected(false);
		}
		
		selection = (enabled ? indexOf(startIndex) : -1);
		if(selection != -1 && get(selection) != null) {
			get(selection).setSelected(true);
		} else {
			if(pointer != null) {
				pointer = null;
			}
		}
	}
	
	public void setSelection(int index) {
		if(get(index) != null) {
			if(selection != -1) {
				get(selection).setSelected(false);
			}
			selection = index;
			get(selection).setSelected(true);
			
			if(pointer != null) {
				pointer.setPositionAt(get(selection));
			}
		}
	}
	
	public void setSelection(UIElement element) {
		if(!this.uiElements.contains(element)) {
			return;
		}
		
		setSelection(this.uiElements.indexOf(element));
	}
	
	private void changeSelection(int direction) {
		if(get(selection).getSurroundings()[direction] != null) {
			get(selection).setSelected(false);
			selection = indexOf(get(selection).getSurroundings()[direction]);
			get(selection).setSelected(true);
			
			if(pointer != null) {
				pointer.setPositionAt(get(selection));
			}
		}
	}
	
	public void setBounds() {
		if(isEmpty()) {
			setBounds(0, 0, 1, 1);
		} else if(size() == 1) {
			UIBounds initBounds = get(0).getBounds();
			setBounds(initBounds.getXAsSupplier(), initBounds.getYAsSupplier(), initBounds.getWidthSupplier(), initBounds.getHeightSupplier());
		} else {
			for(UIElement e : uiElements) {
				UIBounds.merge(getBounds(), e.getBounds(), uiBounds);
			}
		}
	}
	
	public void removeKeybindListener(KeybindListener l) {
		if(l == null) {
			return;
		}
		keybindListener = null;
	}
	
	public void addKeybindListener(KeybindListener l) {
		keybindListener = l;
	}
	
	// TODO Likely can use stream operations to clean this up later
	@Override
	public void fireEvent(UIEvent e) {
		if(e instanceof MouseEvent) {
			processMouseEvent((MouseEvent) e);
		} else if(e instanceof TimeEvent) {
			processTimeEvent((TimeEvent) e);
			
			if(!isEmpty()) {
				for(UIElement ui : uiElements) {
					if(ui.getState() == ENABLED) {
						ui.fireEvent(e);
					}
				}
			}
		} else if(e instanceof KeyEvent) {
			if(!isEmpty()) {
				for(UIElement ui : uiElements) {
					if(ui.getState() == ENABLED) {
						ui.fireEvent(e);
					}
				}
			}
		} else if(e instanceof KeybindEvent) {
			processKeybindEvent((KeybindEvent) e);
			
			if(!isEmpty()) {
				for(UIElement ui : uiElements) {
					if(ui.getState() == ENABLED) {
						ui.fireEvent(e);
					}
				}
			}
		}
	}
	
	protected void processKeybindEvent(KeybindEvent e) {
		if(keybindListener != null) {
			keybindListener.KeybindTouched(e);
		}
	}
	
	class DefaultMouseAdapter implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if(isEmpty()) {
				return;
			}
			for(UIElement ui : uiElements) {
				if(e.isConsumed()) {
					return;
				}
				if(ui.getBoundsAsRect().contains(((MouseEvent) e).getPosition())) {
					ui.fireEvent(e);
				}
			}
			e.consume();
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if(isEmpty()) {
				return;
			}
			for(UIElement ui : uiElements) {
				if(ui.getBoundsAsRect().contains(e.getPosition())) {
					setSelection(ui);
				}
			}
			e.consume();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(selection != -1) {
				setSelection(selection);
			}
			e.consume();
		}
	}
	
	class DefaultMouseMotionAdapter implements MouseMotionListener {
		@Override
		public void mouseMoved(MouseEvent e) {
			if(isEmpty() || !ElementGroup.this.getBoundsAsRect().contains(e.getPosition())) {
				return;
			}
			
			for(UIElement ui : uiElements) {
				if(ui.getBoundsAsRect().contains(e.getPosition())) {
					setSelection(ui);
					e.consume();
					return;
				}
			}
			
			e.consume();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}
	
	class DefaultKeybindAdapter implements KeybindListener {

		@Override
		public void KeybindTouched(KeybindEvent e) {
			if(selection != -1) {
				get(selection).setSelected(true);
				if(e.getKeybind().clicked()) {
					switch(e.getKeybind()) {
					case UP:
						changeSelection(0);
						break;
					case RIGHT:
						changeSelection(1);
						break;
					case LEFT:
						changeSelection(2);
						break;
					case DOWN:
						changeSelection(3);
						break;
					case CONFIRM:
						get(selection).fireEvent(new ActionEvent());
						break;
					default:
					}
				}
			}
		}
		
	}
	
	public T get(int index) {
		return uiElements.get(index);
	}
	
	public int size() {
		return uiElements.size();
	}
	
	public boolean isEmpty() {
		return uiElements.isEmpty();
	}
	
	public int indexOf(UIElement element) {
		return uiElements.indexOf(element);
	}

	public void add(T element) {
		uiElements.add(element);
		setBounds();
	}
	
	public void addAll(Collection<T> elements) {
		this.uiElements.addAll(elements);
		setBounds();
	}

}

class SelectionPointer extends Icon {
	
	private Vector2f offset = new Vector2f(0, 0);
	
	/** Tween variables */
	private float time;
	private float duration;
	private float distance;
	private boolean tweenOut;
	
	public SelectionPointer(String icon) {
		super(icon);
		
		this.time = 0f;
		this.duration = 1.5f;
		this.distance = 15f;
		this.tweenOut = true;
		
		addTimeListener(new DefaultPointerTimeAdapter());
	}
	
	public void setPositionAt(UIElement element) {
		setPosition(element.getBounds().getX() - (getBounds().getWidth() * 0.8f), 
				element.getBoundsAsRect().getCenterY() - (getBounds().getHeight() * 0.5f));
	}
	
	class DefaultPointerTimeAdapter implements TimeListener {

		@Override
		public void timeStep(TimeEvent e) {
			time = MathUtils.clamp(time + e.getDelta(), 0, duration);
			if(tweenOut) {
				offset.setX(MathUtils.easeOut(time, 0, -distance, duration));
			} else {
				offset.setX(MathUtils.easeOut(time, -distance, distance, duration));
			}
			if(time == duration) {
				time = 0;
				tweenOut = !tweenOut;
			}
		}
		
	}
	
}
