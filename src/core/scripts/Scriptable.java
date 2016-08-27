package core.scripts;

import java.io.Serializable;

import core.entities.Entity;

public interface Scriptable extends Serializable {

	public void startReading(Entity reader);
	public void read();
	public void endReading();
	public void interrupt();
	
	public boolean isBusyReading();
	
}
