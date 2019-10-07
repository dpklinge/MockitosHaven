package com.fdmgroup.mockitoshaven.game.generators;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.EnvironmentType;
import com.fdmgroup.mockitoshaven.game.dungeon.MapStyle;
import com.fdmgroup.mockitoshaven.game.dungeon.Tile;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.dungeon.TileType;
import com.fdmgroup.mockitoshaven.game.items.Item;

@Component
public class TileMapGenerator {
	private int tileMapId = 0;
	private int xSize = 50;
	private int ySize = 50;
	private int birthLimit = 4;
	private int deathLimit = 3;
	private double initialLifeChance = 0.45;
	private int initialNeighborLifeChance = 30;
	private int initialOtherLifeChance = 12;
	private int antiNeighborChance = 7;
	private int evolutionSteps = 5;
	private double enemyGenerationChance = 0;
	private double itemGenerationChance = 1;
	private CharacterGenerator monsterGenerator;
	private ItemGenerator itemGenerator;
	private List<TileType> passableTiles = new ArrayList<>();
	private List<TileType> impassableTiles = new ArrayList<>();
	private Logger logger = LogManager.getLogger();

	{
		passableTiles.add(TileType.OPEN);
		impassableTiles.add(TileType.WALL);
		loadProperties();
	}
	private Random random = new Random();

	@Autowired
	public TileMapGenerator(CharacterGenerator monsterGenerator, ItemGenerator itemGenerator) {
		super();
		this.itemGenerator = itemGenerator;
		this.monsterGenerator = monsterGenerator;
	}

	public void loadProperties() {
		Properties props = new Properties();
		try {
			File file = new File("src/main/resources/mapGeneration.properties");

			logger.info("------------------------------");
			logger.info("Path:" + file.getAbsolutePath() + " " + file.exists());
			props.load(new FileReader(file));
			logger.info(props.getProperty("xSize"));
			xSize = Integer.parseInt(props.getProperty("xSize"));
			ySize = Integer.parseInt(props.getProperty("ySize"));
			birthLimit = Integer.parseInt(props.getProperty("birthLimit"));
			deathLimit = Integer.parseInt(props.getProperty("deathLimit"));
			initialLifeChance = Double.parseDouble(props.getProperty("initialLifeChance"));
			initialNeighborLifeChance = Integer.parseInt(props.getProperty("initialNeighborLifeChance"));
			initialOtherLifeChance = Integer.parseInt(props.getProperty("initialOtherLifeChance"));
			antiNeighborChance = Integer.parseInt(props.getProperty("antiNeighborChance"));
			evolutionSteps = Integer.parseInt(props.getProperty("evolutionSteps"));
			enemyGenerationChance = Double.parseDouble(props.getProperty("enemyGenerationChance"));
			itemGenerationChance = Double.parseDouble(props.getProperty("itemGenerationChance"));
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public TileMap generateFromJsonFile(File file) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			TileMap map = mapper.readValue(file, TileMap.class);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	public TileMap generateRandomLevel(int depth) {
		TileMap map = new TileMap();
		MapStyle style = MapStyle.values()[getRandom().nextInt(MapStyle.values().length)];
		map.setStyle(style);
		map.setId("" + tileMapId++);
		map.setLevel(depth);
		for (int x = 0; x < xSize; x++) {
			map.getTileMap().add(new ArrayList<>());
			for (int y = 0; y < ySize; y++) {

				EnvironmentType etype = EnvironmentType.values()[random.nextInt(2)];
				TileType ttype = null;
				if (random.nextDouble() < initialLifeChance) {
					ttype = getPassable(etype);
				} else {
					ttype = getImpassable(etype);
				}
				// TileType ttype = randomPassabilityTileType(map, x, y);
				map.getTileMap().get(x).add(new Tile(new Coordinate(x, y, map.getId()), etype, ttype));

			}
		}

		for (int i = 0; i < evolutionSteps; i++) {
			map = evolve(map);
		}
		generateEntryPoint(map);
		generateExitPoint(map);

		map = populate(map, depth);
		return map;
	}

	protected TileMap generateEntryPoint(TileMap map) {
		while (map.getEntryPoint() == null || !map.getTile(map.getEntryPoint()).isNavigable()) {
			map.setEntryPoint(new Coordinate(random.nextInt(xSize), random.nextInt(ySize), map.getId()));
		}
		map.setTile(map.getEntryPoint(), new Tile(map.getEntryPoint(),
				map.getStyle().getTileTypeMapping().get(TileType.UPSTAIRS), TileType.UPSTAIRS));
		return map;
	}

	protected TileMap generateExitPoint(TileMap map) {
		while (map.getExitPoint() == null || !map.getTile(map.getExitPoint()).isNavigable()) {
			map.setExitPoint(new Coordinate(random.nextInt(xSize), random.nextInt(ySize), map.getId()));
		}
		map.setTile(map.getExitPoint(), new Tile(map.getExitPoint(),
				map.getStyle().getTileTypeMapping().get(TileType.DOWNSTAIRS), TileType.DOWNSTAIRS));
		return map;
	}

	protected TileMap populate(TileMap map, int level) {
		for (List<Tile> row : map.getTileMap()) {
			for (Tile tile : row) {
				if (tile.isNavigable()) {
					double randRoll = random.nextDouble() * 100;

					if (randRoll < enemyGenerationChance) {
						NonPlayerCharacter npc = monsterGenerator.generateRandomCharacter(map, level);
						npc.setCoordinate(tile.getCoordinate());
						map.addLocatable(npc);
					}
					randRoll = random.nextDouble() * 100;
					if (randRoll < itemGenerationChance) {
						Item item = itemGenerator.generateRandomItem(map, level);
						item.setCoordinate(tile.getCoordinate());
						map.addLocatable(item);
					}
				}
			}
		}
		return map;
	}

	protected TileMap evolve(TileMap map) {
		List<List<Tile>> newMap = new ArrayList<>();
		for (int x = 0; x < map.getTileMap().size(); x++) {
			newMap.add(new ArrayList<>());
			for (int y = 0; y < map.getTileMap().get(0).size(); y++) {
				int neighborCount = countPassableNeighbours(map, x, y);
				if (passableTiles.contains(map.getTileMap().get(x).get(y).getTileType())) {
					if (neighborCount < deathLimit) {
						newMap.get(x)
								.add(new Tile(new Coordinate(x, y, map.getId()),
										map.getTileMap().get(x).get(y).getEnvType(),
										getImpassable(map.getTileMap().get(x).get(y).getEnvType())));
					} else {
						newMap.get(x)
								.add(new Tile(new Coordinate(x, y, map.getId()),
										map.getTileMap().get(x).get(y).getEnvType(),
										getPassable(map.getTileMap().get(x).get(y).getEnvType())));
					}
				} else {
					if (neighborCount > birthLimit) {
						newMap.get(x)
								.add(new Tile(new Coordinate(x, y, map.getId()),
										map.getTileMap().get(x).get(y).getEnvType(),
										getPassable(map.getTileMap().get(x).get(y).getEnvType())));
					} else {
						newMap.get(x)
								.add(new Tile(new Coordinate(x, y, map.getId()),
										map.getTileMap().get(x).get(y).getEnvType(),
										getImpassable(map.getTileMap().get(x).get(y).getEnvType())));
					}
				}
			}
		}
		map.setTileMap(newMap);
		return map;

	}

	private TileType getImpassable(EnvironmentType envType) {
		return impassableTiles.get(random.nextInt(impassableTiles.size()));
	}

	private TileType getPassable(EnvironmentType etype) {
		return passableTiles.get(random.nextInt(passableTiles.size()));
	}

	private int countPassableNeighbours(TileMap map, int x, int y) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int neighbour_x = x + i;
				int neighbour_y = y + j;
				if (i == 0 && j == 0) {
					continue;
				}
				// In case the index we're looking at it off the edge of the map
				else if (neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= map.getTileMap().size()
						|| neighbour_y >= map.getTileMap().get(0).size()) {
					count = count + 1;
				}
				// Otherwise, a normal check of the neighbour
				else if (passableTiles.contains(map.getTileMap().get(neighbour_x).get(neighbour_y).getTileType())) {
					count = count + 1;
				}
			}
		}
		return count;
	}

	protected TileType randomPassabilityTileType(TileMap map, int x, int y) {
		double passabilityChance = 0;
		if ((x == 0 || x == 1) && (y == 0 || y == 1)) {
			return passableTiles.get(random.nextInt(passableTiles.size()));
		}
		if (x - 1 >= 0) {
			if (passableTiles.contains(map.getTileMap().get(x - 1).get(y).getTileType())) {
				passabilityChance += initialNeighborLifeChance;
			} else {
				passabilityChance += antiNeighborChance;
			}
		} else {
			passabilityChance += initialOtherLifeChance;
		}

		if (y - 1 >= 0) {
			if (passableTiles.contains(map.getTileMap().get(x).get(y - 1).getTileType())) {
				passabilityChance += initialNeighborLifeChance;
			} else {
				passabilityChance += antiNeighborChance;
			}

		} else

		{
			passabilityChance += initialOtherLifeChance;
		}

		if (random.nextInt(100) < passabilityChance) {
			return passableTiles.get(random.nextInt(passableTiles.size()));
		} else {
			return impassableTiles.get(random.nextInt(passableTiles.size()));
		}
	}

	public int getTileMapId() {

		return tileMapId++;
	}

	public void setTileMapId(int tileMapId) {
		this.tileMapId = tileMapId;
	}

	public int getxSize() {
		return xSize;
	}

	public void setxSize(int xSize) {
		this.xSize = xSize;
	}

	public int getySize() {
		return ySize;
	}

	public void setySize(int ySize) {
		this.ySize = ySize;
	}

	public int getBirthLimit() {
		return birthLimit;
	}

	public void setBirthLimit(int birthLimit) {
		this.birthLimit = birthLimit;
	}

	public int getDeathLimit() {
		return deathLimit;
	}

	public void setDeathLimit(int deathLimit) {
		this.deathLimit = deathLimit;
	}

	public double getInitialLifeChance() {
		return initialLifeChance;
	}

	public void setInitialLifeChance(double initialLifeChance) {
		this.initialLifeChance = initialLifeChance;
	}

	public int getInitialNeighborLifeChance() {
		return initialNeighborLifeChance;
	}

	public void setInitialNeighborLifeChance(int initialNeighborLifeChance) {
		this.initialNeighborLifeChance = initialNeighborLifeChance;
	}

	public int getInitialOtherLifeChance() {
		return initialOtherLifeChance;
	}

	public void setInitialOtherLifeChance(int initialOtherLifeChance) {
		this.initialOtherLifeChance = initialOtherLifeChance;
	}

	public int getAntiNeighborChance() {
		return antiNeighborChance;
	}

	public void setAntiNeighborChance(int antiNeighborChance) {
		this.antiNeighborChance = antiNeighborChance;
	}

	public int getEvolutionSteps() {
		return evolutionSteps;
	}

	public void setEvolutionSteps(int evolutionSteps) {
		this.evolutionSteps = evolutionSteps;
	}

	public double getEnemyGenerationChance() {
		return enemyGenerationChance;
	}

	public void setEnemyGenerationChance(double enemyGenerationChance) {
		this.enemyGenerationChance = enemyGenerationChance;
	}

	public double getItemGenerationChance() {
		return itemGenerationChance;
	}

	public void setItemGenerationChance(double itemGenerationChance) {
		this.itemGenerationChance = itemGenerationChance;
	}

	public CharacterGenerator getMonsterGenerator() {
		return monsterGenerator;
	}

	public void setMonsterGenerator(CharacterGenerator monsterGenerator) {
		this.monsterGenerator = monsterGenerator;
	}

	public ItemGenerator getItemGenerator() {
		return itemGenerator;
	}

	public void setItemGenerator(ItemGenerator itemGenerator) {
		this.itemGenerator = itemGenerator;
	}

	public List<TileType> getPassableTiles() {
		return passableTiles;
	}

	public void setPassableTiles(List<TileType> passableTiles) {
		this.passableTiles = passableTiles;
	}

	public List<TileType> getImpassableTiles() {
		return impassableTiles;
	}

	public void setImpassableTiles(List<TileType> impassableTiles) {
		this.impassableTiles = impassableTiles;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

}
