package com.fdmgroup.mockitoshaven.game.character;

import java.util.Random;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fdmgroup.mockitoshaven.game.Game;
import com.fdmgroup.mockitoshaven.game.MovementValidator;
import com.fdmgroup.mockitoshaven.game.dungeon.Coordinate;
import com.fdmgroup.mockitoshaven.game.dungeon.Locatable;
import com.fdmgroup.mockitoshaven.game.dungeon.TileMap;
import com.fdmgroup.mockitoshaven.game.logic.DetectionLogic;
import com.fdmgroup.mockitoshaven.game.logic.SpaceLogic;

@Entity
public class NonPlayerCharacter extends GameCharacter {
	private static Random random = new Random();
	@Enumerated(EnumType.STRING)
	private Behaviour behaviour = Behaviour.AGGRESSIVE;

	public Behaviour getBehaviour() {
		return behaviour;
	}

	public void setBehaviour(Behaviour behaviour) {
		this.behaviour = behaviour;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	public void executeBehaviour(Game game) {
		switch (behaviour) {
		case AGGRESSIVE:
			aggress(game);
			break;
		case PASSIVE:
			break;
		default:
			break;

		}

	}

	private void aggress(Game game) {
		// System.out.println("Aggressing");
		TileMap map = game.getDungeon().getTileMapById(getCoordinate().getMapId());
		double closestEnemyDistance = getStats().getObservation() + 1;
		PlayerCharacter pc = null;
		Set<Locatable> locatables = map.getAllLocatables();

		for (Locatable locatable : locatables) {

			if (locatable instanceof PlayerCharacter) {
				PlayerCharacter chara = (PlayerCharacter) locatable;
				if (chara.getStats().getHealth() > 0) {
					double distanceToTarget = SpaceLogic.getDistanceBetween(this, locatable);
					if (DetectionLogic.checkVisible(this, (PlayerCharacter) locatable, distanceToTarget)
							&& distanceToTarget < closestEnemyDistance) {
						pc = chara;
						closestEnemyDistance = distanceToTarget;
					}
				}
			}

		}

		if (pc != null) {
			Coordinate nextMove = SpaceLogic.nextStepBetweenPoints(getCoordinate(), pc.getCoordinate(), map);

			if (nextMove != null) {
				MovementValidator.attemptMove(game, map, nextMove, this);
			}
		} else {
			if (random.nextDouble() < .1) {
				MovementValidator.attemptMove(game, map, SpaceLogic.randomAdjacentTile(this.getCoordinate(), map).getCoordinate(), this);
			}
		}

	}

}
