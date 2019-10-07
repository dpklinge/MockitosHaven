package com.fdmgroup.mockitoshaven.game.items;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Entity
public class Inventory {
	private static Logger logger=LogManager.getLogger();
	@Id
	private String identifier;
	@Column(name = "inventory_size")
	private int size = 0;
	private int remainingSize = 20;
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, Item> items = new HashMap<>();

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void setRemainingSize(int remainingSize) {
		this.remainingSize = remainingSize;
	}

	public Inventory() {
		super();
	}

	public Inventory(int size) {
		this.size = size;
		remainingSize = size;
		items = new HashMap<>();
	}

	public boolean addItem(Item item) {
		if (remainingSize != 0) {
			if (items.containsKey(item.getIdentifier())) {
				items.get(item.getIdentifier()).setAmount(items.get(item.getIdentifier()).getAmount() + 1);
			} else {
				items.put(item.getIdentifier(), item);
				remainingSize--;
				size++;
			}
			return true;

		}
		return false;
	}

	public Item removeItem(Item item, int quantityToRemove) {
		logger.info("Removing item "+item+" in amount "+quantityToRemove);
		Item itemToRemove = items.get(item.getIdentifier());
		int quantityOfItem = itemToRemove.getAmount();

		if (quantityOfItem > quantityToRemove) {
			itemToRemove.setAmount(quantityOfItem - quantityToRemove);
		}
		if (quantityOfItem == quantityToRemove) {
			items.remove(item.getIdentifier());
			remainingSize++;
			size--;
		}

		return item;
	}

	public int getSize() {
		return items.size();
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<String, Item> getItems() {
		return items;
	}

	public void setItems(Map<String, Item> items) {
		this.items = items;
	}

	public int getRemainingSize() {
		return remainingSize;
	}

	public boolean containsItem(String identifier) {
		if (items.containsKey(identifier) && items.get(identifier).getAmount() > 0) {
			return true;
		}
		return false;
	}

	public Item getItemWithoutRemove(String identifier) {
		return items.get(identifier);
	}

	@Override
	public String toString() {
		return "Inventory [id=" + identifier + ", size=" + size + ", remainingSize=" + remainingSize + ", items=" + items + "]";
	}

}
