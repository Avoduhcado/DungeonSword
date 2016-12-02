package core.scripts;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import core.entities.Entity;
import core.ui.TextBox;
import core.ui.UIElement;

public class Script implements Scriptable {
	private static final long serialVersionUID = 1L;
	
	private static final String START_EVENT = "EVENT";
	private static final String OPTIONS = "OPTIONS";
	
	private Entity source;
	private Entity reader;
	
	private JsonObject parsedData;
	private JsonElement currentEvent;
	
	private boolean reading;
	
	private UIElement screenElement;
	
	public Script(Entity source, String data) {
		parseData(data);
		this.source = source;
	}

	private void parseData(String data) {
		parsedData = new JsonParser().parse(data).getAsJsonObject();
	}

	@Override
	public void startReading(Entity reader) {
		setReading(true);
		setReader(reader);
		resetEvent();
		
		read();
	}

	@Override
	public void read() {
		// TODO process currentEvent through Interpreter, load up next currentEvent?
		// How do I handle conditionals?
		
		// Conditionals?
		if(currentEvent.isJsonArray()) {
			/*if(Interpreter.interpretConditional(currentEvent.getAsJsonArray().get(0).getAsJsonObject())) {
				currentEvent = 
			}*/
		}
		if(currentEvent.isJsonObject()) {
			Interpreter.interpret(currentEvent.getAsJsonObject());
		} else {
			// XXX Shouldn't happen
			resetEvent();
			return;
		}
	}

	@Override
	public void endReading() {
		setReading(false);
	}

	@Override
	public void interrupt() {
		if(screenElement != null) {
			//((GameSetup) source.getContainer()).getUI().remove(screenElement);
		}
		//screenElement = new TextBox(reader.getZBody().getX(), reader.getZBody().getY(), null, "See ya later faaaag", false);
		((TextBox) screenElement).setKillTimer(2.5f);
		//((GameSetup) source.getContainer()).addUI(screenElement);
		
		setReading(false);
		setReader(null);
	}

	@Override
	public boolean isBusyReading() {
		return reading;
	}
	
	private void resetEvent() {
		currentEvent = parsedData.get(START_EVENT);
	}
	
	private void setReading(boolean reading) {
		this.reading = reading;
	}

	private void setReader(Entity reader) {
		this.reader = reader;
	}

}
