package core.ui;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import org.lwjgl.util.vector.Vector2f;

import core.Theater;
import core.ui.event.KeyEvent;
import core.ui.event.MouseEvent;
import core.ui.event.UIEvent;
import core.ui.utils.Accessible;
import core.utilities.MathUtils;
import core.utilities.keyboard.Keybinds;
import core.utilities.mouse.MouseInput;

public class ElementGroup<T extends UIElement> extends UIElement {
	
	private ArrayList<T> elements = new ArrayList<T>();
	
	private boolean singleSelection = true;
	
	protected int selection = -1;
	private SelectionPointer pointer;
	
	private CancelListener listener;

	public void update() {
		if(pointer != null) {
			pointer.update();
		}
		
		// TODO Change to mouselistener event
		if(selection != -1 && (frame != null ? !getBounds().contains(MouseInput.getMouse()) : true)) {
			get(selection).setSelected(true);
			if(Keybinds.UP.clicked() && get(selection).getSurroundings()[0] != null) {
				changeSelection(0);
			} else if(Keybinds.RIGHT.clicked() && get(selection).getSurroundings()[1] != null) {
				changeSelection(1);
			} else if(Keybinds.LEFT.clicked() && get(selection).getSurroundings()[2] != null) {
				changeSelection(2);
			} else if(Keybinds.DOWN.clicked() && get(selection).getSurroundings()[3] != null) {
				changeSelection(3);
			}
		} else if(selection != -1 && getBounds().contains(MouseInput.getMouse())) {
			get(selection).setSelected(false);
			// TODO Get mouse coordinates and update pointer position
		}
		
		if(listener != null && Keybinds.CANCEL.clicked()) {
			listener.cancel();
		}
	}
	
	public void draw() {
		super.draw();
		
		for(UIElement e : elements) {
			e.draw();
		}
		
		if(pointer != null) {
			pointer.draw();
		}
	}
	
	public void setEnabledAll(boolean enabled) {
		for(UIElement e : elements) {
			e.setState(enabled ? ENABLED : DISABLED);
		}
	}
	
	public void setEnabledAllExcept(boolean enabled, UIElement except) {
		for(UIElement e : elements) {
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
		for(UIElement e : elements) {
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
		this.pointer = new SelectionPointer(pointerName, (!isEmpty() ? get(0).still : false));
		if(selection != -1) {
			pointer.setPosition(get(selection));
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
	
	public void setCancelListener(CancelListener listener) {
		this.listener = listener;
	}
	
	private void changeSelection(int direction) {
		get(selection).setSelected(false);
		selection = indexOf(get(selection).getSurroundings()[direction]);
		get(selection).setSelected(true);
		
		if(pointer != null) {
			pointer.setPosition(get(selection));
		}
	}
	
	public void setBounds() {
		if(isEmpty()) {
			this.bounds = new Rectangle2D.Double(0, 0, 1, 1);
		} else {
			Rectangle2D tempBounds = (Rectangle2D) get(0).getBounds().clone();
			for(UIElement e : elements) {
				Rectangle2D.union(tempBounds, e.getBounds(), tempBounds);
			}
			
			this.bounds = tempBounds;
		}
	}
	
	/*public EmptyFrame getFrame() {
		return frame;
	}
	
	public void addFrame(String image) {
		frame = new EmptyFrame(getBounds(), image);
		frame.setStill(true);
	}
	
	public void setFrame(String image, float xBorder, float yBorder) {
		frame = new EmptyFrame(getBounds(), image);
		frame.setStill(true);
		frame.setXBorder(xBorder);
		frame.setYBorder(yBorder);
	}*/
	
	@Override
	public void fireEvent(UIEvent e) {
		if(e instanceof MouseEvent) {
			if(mouseListener != null) {
				processMouseEvent((MouseEvent) e);
			} else if(!isEmpty()) {
				for(UIElement ui : elements) {
					if(ui.getBounds().contains(((MouseEvent) e).getPosition()) 
							|| ui.getBounds().contains(((MouseEvent) e).getPrevPosition())) {
						ui.fireEvent(e);
					}
				}
			}
		} else if(e instanceof KeyEvent) {
			if(!isEmpty()) {
				for(UIElement ui : elements) {
					if(ui.getState() == ENABLED) {
						ui.fireEvent(e);
					}
				}
			}
		}
	}
	
	public interface CancelListener {
		
		public void cancel();
		
	}
	
	public T get(int index) {
		return elements.get(index);
	}
	
	public int size() {
		return elements.size();
	}
	
	public boolean isEmpty() {
		return elements.isEmpty();
	}
	
	public int indexOf(UIElement element) {
		return elements.indexOf(element);
	}

	public void add(T element) {
		elements.add(element);
		setBounds();
	}
	
	public void addAll(Collection<T> elements) {
		this.elements.addAll(elements);
		setBounds();
	}

}

class SelectionPointer {
	
	private String texture;
	private Vector2f position = new Vector2f(0, 0);
	private Vector2f offset = new Vector2f(0, 0);
	private Dimension2D bounds = new Dimension();
	private boolean still;
	
	/** Tween variables */
	private float time;
	private float duration;
	private float distance;
	private boolean tweenOut;
	
	public SelectionPointer(String texture, boolean still) {
		this.texture = texture;
		this.still = still;
		//this.bounds.setSize(SpriteIndex.getSprite(texture).getWidth() * Camera.ASPECT_RATIO,
			//	SpriteIndex.getSprite(texture).getHeight() * Camera.ASPECT_RATIO);
		
		this.time = 0f;
		this.duration = 1.5f;
		this.distance = 15f;
		this.tweenOut = true;
	}
	
	public void update() {
		time = MathUtils.clamp(time + Theater.getDeltaSpeed(0.025f), 0, duration);
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
	
	public void draw() {
		/*SpriteIndex.getSprite(texture).setStill(still);
		SpriteIndex.getSprite(texture).set2DScale(Camera.ASPECT_RATIO);
		SpriteIndex.getSprite(texture).draw(position.x + offset.x, position.y + offset.y);*/
	}
	
	public void setPosition(float x, float y) {
		position.set(x, y);
	}
	
	public void setPosition(UIElement element) {
		position.setX((float) (element.getBounds().getX() - (bounds.getWidth() * 0.8f)));
		position.setY((float) (element.getBounds().getCenterY() - (bounds.getHeight() * 0.5f)));
	}
	
}
