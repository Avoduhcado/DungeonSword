package core.ui;

import core.ui.utils.Align;
import core.ui.utils.MouseEvent;
import core.utilities.text.Text;

public class Button extends UIElement {
	
	private String text;
	private String textColor = "gray";
	private String icon;
			
	public Button(String text) {		
		this.text = text;
		setBounds(0, 0, text != null ? Text.getDefault().getWidth(text) : 1, text != null ? Text.getDefault().getHeight(text) : 1);
	}
	
	public Button(String text, float x, float y, float width, String image) {		
		this.text = text;
		setBounds(x, y, width == 0 ? Text.getDefault().getWidth(text) : width, Text.getDefault().getHeight(text));
		setFrame(image);
	}
	
	@Override
	public void draw() {
		super.draw();

		if(text != null) {
			if(textColor == null) {
				Text.drawString(text, (float) bounds.getX(), (float) bounds.getY());
			} else {
				Text.drawString(text, (float) bounds.getX(), (float) bounds.getY(), "c" + textColor);
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
	
	@Override
	protected void processMouseEvent(MouseEvent e) {
		if(mouseListener != null) {
			if(e.getEvent() == MouseEvent.MOVED) {
				if(getBounds().contains(e.getPosition()) && !getBounds().contains(e.getPrevPosition())) {
					mouseListener.mouseEntered(e);
					textColor = "white";
					return;
				} else if(!getBounds().contains(e.getPosition()) && getBounds().contains(e.getPrevPosition())) {
					mouseListener.mouseExited(e);
					textColor = "gray";
					return;
				}
			}
			
			switch(e.getEvent()) {
			case MouseEvent.CLICKED:
				mouseListener.mouseClicked(e);
				break;
			case MouseEvent.PRESSED:
				mouseListener.mousePressed(e);
				break;
			case MouseEvent.RELEASED:
				mouseListener.mouseReleased(e);
				break;
			}
		}
	}
	
}
