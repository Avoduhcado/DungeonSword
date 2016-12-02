package core.setups;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.lwjgl.util.vector.Vector4f;

import core.Camera;
import core.Theater;
import core.render.effects.TintEffect;
import core.render.effects.Tween;
import core.ui.Icon;
import core.ui.utils.HorizontalAlign;
import core.ui.utils.VerticalAlign;
import core.utilities.keyboard.Keybind;

public class SplashScreen extends GameSetup {

	/** Queued list of images to display */
	private Queue<Icon> splashImages = new LinkedList<Icon>();
	/** Time to display each image */
	private float timer = 5f;
	/** True if audio ding has played */
	private boolean dinged;
	/** To draw sorted, or unsorted splashes (read: useless) */
	private boolean loadRandom = false;
	
	/**
	 * Splash Screen
	 * Displays once game has been opened.
	 */
	public SplashScreen() {
		Camera.get().addScreenEffect(new TintEffect(new Vector4f(0f, 0f, 0f, 0f), 1f, true, Tween.IN));
		if(loadRandom) {
			loadRandomSplashes();
		} else {
			loadSplashes();
		}
	}
	
	/**
	 * Read splash screen text file to load as many splash screens as detected.
	 */
	private void loadSplashes() {
		try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/splash/splash text"))) {
			Icon splashIcon;
			
			String line = null;
			while((line = reader.readLine()) != null) {
				// Load each screen
				splashIcon = new Icon(line);
				splashIcon.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.5f));
				splashIcon.setAlignments(VerticalAlign.CENTER, HorizontalAlign.CENTER);
				splashImages.add(splashIcon);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load a random assortment of splash screens
	 */
	private void loadRandomSplashes() {
		try {
			// Total number of screens to display
			int totalScreens = 1;
			// Used in display screens randomly
			// All of this is mostly unused
			List<Integer> usedInts = new LinkedList<Integer>();
			// Text file containing names of images for splash screens
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/splash/splash text"));
			
			// Loads each screen
			String line = null;
			for(int x = 0; x<totalScreens; x++) {
				int tempNumber = 1;
				if(!usedInts.contains(tempNumber)) {
					usedInts.add(tempNumber);
					Icon splashIcon;
					
					while(tempNumber > 0) {
						line = reader.readLine();
						tempNumber--;
					}

					splashIcon = new Icon(line);
					splashIcon.setPosition(() -> Camera.get().getDisplayWidth(0.5f), () -> Camera.get().getDisplayHeight(0.5f));
					splashIcon.setAlignments(VerticalAlign.CENTER, HorizontalAlign.CENTER);
					splashImages.add(splashIcon);
					
					reader.close();
					reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/splash/splash text"));
				} else {
					tempNumber = 1;
					x--;
				}
					
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if splash screen should be skipped.
	 * Play ding effect.
	 * Fade image in and out.
	 */
	@Override
	public void update() {
		// If player wishes to skip screen
		if(Keybind.CONFIRM.clicked()) {
			// Remove current screen
			splashImages.poll();
			// Stop any sound effects playing
			
			// Restart screen if a new one exists
			if(!splashImages.isEmpty()) {
				timer = 5f;
				Camera.get().addScreenEffect(new TintEffect(new Vector4f(0f, 0f, 0f, 0f), 1f, true, Tween.IN));
			// Proceed with setup swap
			} else {
				Camera.get().cancelAllEffects();
				Theater.get().setSetup(new TitleMenu());
			}
		}
		
		// Adjust fading and reduce timer
		if(timer > 0f) {
			timer -= Theater.getDeltaSpeed(0.025f);
			
			// If a sound effect is to be played
			if(timer < 4f && !dinged) {
				//Ensemble.get().playSoundEffect(new SoundEffect("Just Like Make Game", 0.75f, false));
				dinged = true;
			}

			// Start fading out
			if(timer <= 1f && Camera.get().getTint().w == 0f) {
				Camera.get().addScreenEffect(new TintEffect(new Vector4f(0f, 0f, 0f, 1f), timer, true, Tween.OUT));
			}
		} else {
			// Remove current screen
			splashImages.poll();
			// Restart screen if a new one exists
			if(!splashImages.isEmpty()) {
				timer = 5f;
				Camera.get().addScreenEffect(new TintEffect(new Vector4f(0f, 0f, 0f, 0f), 1f, true, Tween.IN));
			// Proceed with setup swap
			} else {
				Theater.get().setSetup(new TitleMenu());
			}
		}
	}

	/**
	 * Draw current splash screen in center of screen.
	 */
	@Override
	public void draw() {
		if(splashImages.peek() != null) {
			splashImages.peek().draw();
		}
	}

	@Override
	public void drawUI() {}
	
}
