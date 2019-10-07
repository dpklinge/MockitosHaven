package com.fdmgroup.mockitoshaven.game.logic;

import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.character.PlayerCharacter;

@Component
public class DetectionLogic {

	public static boolean checkVisible(NonPlayerCharacter npc, PlayerCharacter locatable,
			double distanceToTarget) {
		return distanceToTarget<=npc.getStats().getObservation();
	}



}
