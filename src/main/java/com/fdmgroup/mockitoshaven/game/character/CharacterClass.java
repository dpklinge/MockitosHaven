package com.fdmgroup.mockitoshaven.game.character;

public enum CharacterClass {
	TESTER("Tester", "tester.png", "Testers are the bane of bugs. Quick and observant, they do best dealing with a single clear target."),
	DEVELOPER("Developer","developer.png", "Developers are the brains of the operation. Keen intellects make them wizards of code, adept at feats that seem like magic to mere mortals."), 
	DEVOPS("DevOps","devops.png","Development Operations specialists are among the most flexible workers in the coding workplace. Adroit at switching up their style based on the circumstances, they rely on a fair bit of luck to ensure things run smoothly."),
	MANAGEMENT("Management","manager.png", "Managers are the driving force of an operation. With strong personalities and goals, they push forward and smash their way through the subtleties of code.");
	private String name, imageName, description;
	
	private CharacterClass(String name,String imageName, String description) {
		this.name=name;
		this.description=description;
		this.imageName=imageName;
	}
	
	

	public String getImageName() {
		return imageName;
	}



	public void setImageName(String imageName) {
		this.imageName = imageName;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
