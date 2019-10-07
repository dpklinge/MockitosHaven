package com.fdmgroup.mockitoshaven.game.dungeon;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fdmgroup.mockitoshaven.game.character.GameCharacter;
import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.items.Consumable;
import com.fdmgroup.mockitoshaven.game.items.EquipableItem;
import com.fdmgroup.mockitoshaven.game.items.Item;

public class LocatableDeserializer extends StdDeserializer<Locatable> {
	/**
	 * 
	 */
	private static Logger logger = LogManager.getLogger();
	private static final long serialVersionUID = 206405387841818866L;

	public LocatableDeserializer() {
		this(null);
	}
	protected LocatableDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Locatable deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = parser.getCodec().readTree(parser);
		ObjectMapper mapper = (ObjectMapper) parser.getCodec();
		if (node.has("type")) {
			logger.trace("Deserialized npc");
			return mapper.treeToValue(node, NonPlayerCharacter.class);
		} else if (node.has("readyToAttack")) {
			logger.trace("Deserialized pc");
			return mapper.treeToValue(node, GameCharacter.class);
		} else if (node.has("slot")) {
			logger.trace("Deserialized equipable");
			return mapper.treeToValue(node, EquipableItem.class);
		} else if (node.has("duration")) {
			logger.trace("Deserialized consumable");
			return mapper.treeToValue(node, Consumable.class);
		} else if (node.has("amount")) {
			logger.trace("deserialized untyped item");
			node.forEach(subnode -> logger.trace(subnode.toString()));
			return mapper.treeToValue(node, Item.class);
		} else {
			return null;
		}
	}

}
