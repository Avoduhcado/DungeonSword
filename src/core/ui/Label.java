package core.ui;

import core.utilities.text.Text;

public class Label extends UIElement {

	private String text;
	
	public Label(String text) {
		this.text = text;
		setBounds(0, 0, Text.getDefault().getWidth(text), Text.getDefault().getHeight(text));
	}
	
	@Override
	public void draw() {
		super.draw();
		
		Text.drawString(text, getBounds().getX(), getBounds().getY());
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
}
