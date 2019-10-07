package com.fdmgroup.mockitoshaven.game.generators;

import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.dataaccess.MockitosHavenDao;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.items.Consumable;
import com.fdmgroup.mockitoshaven.game.items.EquipableItem;
import com.fdmgroup.mockitoshaven.game.items.Item;
import com.fdmgroup.mockitoshaven.game.items.ItemClassification;

@Component
public class ItemGenerator {
	private static long id =0;
	private Logger logger = LogManager.getLogger();

	private Random random = new Random();
	private List<Item> items;
	@Autowired
	public ItemGenerator (MockitosHavenDao dao) {
		items = dao.findAllObjects(Item.class);
		logger.trace("Initialized items: " + items);
		for(Item item : items) {
			logger.info("Item "+item+" is equippable? "+(item instanceof EquipableItem));
			logger.info("Item "+item+" is consumable? "+(item instanceof Consumable));
		}
	}

	public Item generateRandomItem(TileMap map, int level) {
		Item item = null;
		while (item == null) {
			Item potentialItem = items.get(random.nextInt(items.size())).copy();
			if (!map.getUniqueItemsGenerated().contains(potentialItem.getIdentifier())) {
				if(potentialItem.getClassification().contains(ItemClassification.UNIQUE)) {
					logger.trace("Unique item generated : "+potentialItem);
					map.addUniqueItemGenerated(potentialItem.getIdentifier());
				}
				item = potentialItem;
			}
		}
		item.setIdentifier(item.getIdentifier()+""+id);
		id++;
		return item;
	}
	


}
