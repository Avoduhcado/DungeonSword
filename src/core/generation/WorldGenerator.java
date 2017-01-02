package core.generation;

import java.util.Random;

public abstract class WorldGenerator {
	
	public enum GeneratorState {
		FRESH,
		GENERATING,
		SEPARATING,
		CULLING,
		TRIANGULATING,
		PATHING,
		CONNECTING,
		WING_DING_WILDCARD,
		FINISHED;
	}

	/** Debug commands */
	public static boolean hideInactiveRooms;
	public static boolean hideConnections;
	public static boolean showGrid;
	public static boolean showRoomNumber;
	
	public static final int TILE_SIZE = 4;
	protected static final int WORLD_WIDTH = 300;
	protected static final int WORLD_HEIGHT = 100;
	protected static final int ROOM_WIDTH = 48;
	protected static final int ROOM_HEIGHT = 34;
	protected static final double MEAN_SIZE_LIMIT = 1.25;
	protected static final int TOTAL_ROOM_LOWER_BOUND = 150;
	protected static final int TOTAL_ROOM_UPPER_BOUND = 50;
	protected static final int BONUS_ROOMS = 2;
	protected static final int BONUS_ROOMS_MIN = 2;
	
	protected final Long seed;
	
	protected Random globalRandom;
	protected Random roomRandom;
	protected Random effectsRandom;
	
	protected int totalRoomsLeftToGenerate;
	
	protected GeneratorState state = GeneratorState.FRESH;
	
	public WorldGenerator(Long seed) {
		if(seed == null) {
			this.seed = System.currentTimeMillis();
		} else {
			this.seed = seed;
		}
	}
	
	protected void initRandoms() {
		globalRandom = new Random(seed);
		roomRandom = new Random(globalRandom.nextLong());
		effectsRandom = new Random(roomRandom.nextLong());
	}

	public abstract void generate();
	public abstract void draw();

	protected abstract void freshGeneration();
	protected abstract void generateRooms();
	protected abstract void separateRooms();
	protected abstract void findMainRooms();
	protected abstract void triangulateMainRooms();
	protected abstract void limitTriangulation();
	protected abstract void buildHallways();
	
	public static int roundM(double n, int m) {
		return (int) (Math.floor(((n + m - 1) / m)) * m);
	}
	
	public static int clamp(int value, int min, int max) {
		if(value < min) {
			return min;
		} else if(value > max) {
			return max;
		}
		return value;
	}

}
