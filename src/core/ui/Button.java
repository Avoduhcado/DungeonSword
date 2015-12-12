package core.ui;

import core.ui.event.ActionEvent;
import core.ui.event.ActionListener;
import core.ui.event.MouseEvent;
import core.ui.event.MouseListener;
import core.ui.event.UIEvent;
import core.ui.utils.Align;
import core.utilities.text.Text;

public class Button extends UIElement {
	
	private String text;
	private String textColor = "gray";
	private String icon;
	
	private ActionListener actionListener;
			
	public Button(String text) {		
		this.text = text;
		setBounds(0, 0,
				text != null ? Text.getDefault().getWidth(text) : 1,
				text != null ? Text.getDefault().getHeight(text) : 1);
		
		addMouseListener(new DefaultButtonAdapter());
	}
	
	public Button(String text, float x, float y, float width, String image) {		
		this.text = text;
		setBounds(x, y,
				width == 0 ? Text.getDefault().getWidth(text) : width, 
				Text.getDefault().getHeight(text));
		setFrame(image);
		
		addMouseListener(new DefaultButtonAdapter());
	}
	
	@Override
	public void draw() {
		super.draw();

		if(text != null) {
			if(textColor == null) {
				Text.drawString(text, getX(), getY());
			} else {
				Text.drawString(text, getX(), getY(), "c" + textColor);
			}
		}
	}

	@Override
	public void setAlign(Align border) {
		switch(border) {
		case RIGHT:
			setBounds((float) bounds.getMaxX(), (float) bounds.getY(), (float) bounds.getWidth(), (float) bounds.getHeight());
			break;
		case LEFT:
			setBounds((float) (bounds.getX() - bounds.getWidth()), (float) bounds.getY(), (float) bounds.getWidth(), (float) bounds.getHeight());
			break;
		case CENTER:
			bounds.setFrameFromCenter(bounds.getX(), bounds.getCenterY(), 
					bounds.getX() - (bounds.getWidth() / 2f), bounds.getY());
			break;
		default:
			break;
		}
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * Not recommended to override default <code>Button</code> behavior.
	 */
	@Override
	public void addMouseListener(MouseListener l) {
		this.mouseListener = l;
	}
	
	public void removeActionListener(ActionListener l) {
		if(l == null) {
			return;
		}
		
		actionListener = null;
	}
	
	public void addActionListener(ActionListener l) {
		this.actionListener = l;
	}
	
	@Override
	public void fireEvent(UIEvent e) {
		if(e instanceof MouseEvent) {
			processMouseEvent((MouseEvent) e);
		} else if(e instanceof ActionEvent) {
			processActionEvent((ActionEvent) e);
		}
	}
	
	protected void processActionEvent(ActionEvent e) {
		if(actionListener != null) {
			actionListener.actionPerformed(e);
		}
	}

	/**
	 * Handle the default actions for mouse events on a button
	 */
	class DefaultButtonAdapter implements MouseListener {

		public void mouseClicked(MouseEvent e) {
			Button.this.fireEvent(new ActionEvent());
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
		public void mouseEntered(MouseEvent e) {
			textColor = "white";
		}
		
		public void mouseExited(MouseEvent e) {
			textColor = "gray";
		}
	}
}

