package com.fdmgroup.mockitoshaven.game.character;

import javax.persistence.Entity;

import com.fdmgroup.mockitoshaven.game.Stats;

@Entity
public class PlayerCharacter extends GameCharacter {
	private CharacterClass clazz;

	public PlayerCharacter() {

	}

	@Override
	protected void levelUp() {
		System.out.println("Player character levelup");
		getStats().setLevel(getStats().getLevel() + 1);
		switch (clazz) {

		default:
			getStats().addStats(new Stats(5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 50));
			break;

		}
	}

	public static PlayerCharacter generateClass(CharacterClass clazz) {
		PlayerCharacter character = new PlayerCharacter();
		character.clazz = clazz;
		switch (clazz) {
		case DEVELOPER:
			character.setStats(new Stats(10, 5, 15, 8, 3, 3, 6, 5, 10, 5, 4, 1, 0, 5, 100));
			character.setImageName("developer.png");
			break;
		case DEVOPS:
			character.setStats(new Stats(10, 15, 10, 6, 6, 6, 3, 10, 6, 10, 6, 1, 0, 5, 100));
			character.setImageName("devops.png");
			break;
		case MANAGEMENT:
			character.setStats(new Stats(12, 15, 5, 2, 8, 10, 5, 5, 4, 6, 5, 1, 0, 10, 100));
			character.setImageName("manager.png");
			break;
		case TESTER:
			character.setStats(new Stats(10, 13, 8, 5, 5, 4, 10, 5, 4, 6, 8, 1, 0, 5, 100));
			character.setImageName("tester.png");
			break;
		default:
			character.setStats(new Stats(10, 10, 10, 5, 5, 5, 5, 5, 5, 5, 5, 1, 0, 5, 100));
			character.setImageName("character.png");
			break;

		}
		return character;
	}

}
