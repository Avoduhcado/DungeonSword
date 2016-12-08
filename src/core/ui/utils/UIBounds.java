package core.ui.utils;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import core.utilities.ValueSupplier;

public class UIBounds {

	private ValueSupplier<Double> x;
	private ValueSupplier<Double> y;
	private ValueSupplier<Double> width;
	private ValueSupplier<Double> height;
	
	private ValueSupplier<Double> xBorder;
	private ValueSupplier<Double> yBorder;

	private VerticalAlign verticalAlignment = VerticalAlign.TOP;
	private HorizontalAlign horizontalAlignment = HorizontalAlign.LEFT;
	
	public UIBounds() {
		this(0, 0);
	}
	
	public UIBounds(double x, double y) {
		this(x, y, 0, 0);
	}
	
	public UIBounds(ValueSupplier<Double> x, ValueSupplier<Double> y) {
		this(x, y, () -> 0d, () -> 0d);
	}
	
	public UIBounds(double x, double y, double width, double height) {
		this(() -> x, () -> y, () -> width, () -> height);
	}

	public UIBounds(ValueSupplier<Double> x, ValueSupplier<Double> y, ValueSupplier<Double> width, ValueSupplier<Double> height) {
		setFrame(x, y, width, height);
		
		setXBorder(() -> 0d);
		setYBorder(() -> 0d);
	}
	
	public boolean contains(Point2D point2d) {
		return contains(point2d.getX(), point2d.getY());
	}
	
	public boolean contains(double x, double y) {
		return x >= getX() && x <= getMaxX() && y >= getY() && y <= getMaxY(); 
	}
	
	public void setFrame(double x, double y, double width, double height) {
		setFrame(() -> x, () -> y, () -> width, () -> height);
	}

	public void setFrame(ValueSupplier<Double> x, ValueSupplier<Double> y, ValueSupplier<Double> width, ValueSupplier<Double> height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	public void setVerticalAlign(VerticalAlign vertical) {
		this.verticalAlignment = vertical;
	}
	
	public void setHorizontalAlign(HorizontalAlign horizontal) {
		this.horizontalAlignment = horizontal;
	}
	
	/**
	 * @return Default <b>x</b> value modified by the <b>alignment</b> and offset by the <b>xBorder</b>.
	 */
	public double getX() {
		return x.getValue() + horizontalAlignment.getX(this) - getXBorder();
	}
	
	public ValueSupplier<Double> getXSupplier() {
		return x;
	}
	
	public ValueSupplier<Double> getXAsSupplier() {
		return () -> x.getValue() + horizontalAlignment.getX(this) - getXBorder();
	}

	public void setX(double x) {
		this.x = () -> x;
	}
	
	public void setX(ValueSupplier<Double> x) {
		this.x = x;
	}
	
	public double getMaxX() {
		return getX() + getWidth();
	}

	/**
	 * @return Default <b>y</b> value modified by the <b>alignment</b> and offset by the <b>yBorder</b>.
	 */
	public double getY() {
		return y.getValue() + verticalAlignment.getY(this) - getYBorder();
	}

	public ValueSupplier<Double> getYSupplier() {
		return y;
	}

	public ValueSupplier<Double> getYAsSupplier() {
		return () -> y.getValue() + verticalAlignment.getY(this) - getYBorder();
	}

	public void setY(double y) {
		this.y = () -> y;
	}

	public void setY(ValueSupplier<Double> y) {
		this.y = y;
	}

	public double getMaxY() {
		return getY() + getHeight();
	}

	/**
	 * @return Default <b>width</b> value modified by the <b>alignment</b> and widened by the <b>xBorder</b>.
	 */
	public double getWidth() {
		return width.getValue() + (getXBorder() * 2);
	}

	public ValueSupplier<Double> getWidthSupplier() {
		return width;
	}

	public void setWidth(double width) {
		this.width = () -> width;
	}

	public void setWidth(ValueSupplier<Double> width) {
		this.width = width;
	}
	
	/**
	 * @return Default <b>height</b> value modified by the <b>alignment</b> and heightened by the <b>yBorder</b>.
	 */
	public double getHeight() {
		return height.getValue() + (getYBorder() * 2);
	}

	public ValueSupplier<Double> getHeightSupplier() {
		return height;
	}

	public void setHeight(double height) {
		this.height = () -> height;
	}

	public void setHeight(ValueSupplier<Double> height) {
		this.height = height;
	}
	
	protected double getXBorder() {
		return xBorder.getValue();
	}

	public void setXBorder(double xBorder) {
		this.xBorder = () -> xBorder;
	}
	
	public void setXBorder(ValueSupplier<Double> xBorder) {
		this.xBorder = xBorder;
	}

	protected double getYBorder() {
		return yBorder.getValue();
	}
	
	public void setYBorder(double yBorder) {
		this.yBorder = () -> yBorder;
	}
	
	public void setYBorder(ValueSupplier<Double> yBorder) {
		this.yBorder = yBorder;
	}
	
	public Rectangle2D toRectangle() {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public String toString() {
		return "UI Bounds [" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + "]";
	}
	
	/** XXX Not really the best, works in upscale mode, isn't guaranteed to work in resize mode
	 * due to static definitions for width/height */
	public static UIBounds merge(UIBounds src1, UIBounds src2, UIBounds dest) {
		if(dest == null) {
			dest = new UIBounds();
		}
		dest.setX(src1.getX() <= src2.getX() ? src1.x : src2.x);
		dest.setY(src1.getY() <= src2.getY() ? src1.y : src2.y);
		
		double width = (src1.getMaxX() >= src2.getMaxX() ? src1.getMaxX() : src2.getMaxX()) - dest.getX();
		dest.setWidth(width);
		double height = (src1.getMaxY() >= src2.getMaxY() ? src1.getMaxY() : src2.getMaxY()) - dest.getY();
		dest.setHeight(height);
		
		return dest;
	}
}
