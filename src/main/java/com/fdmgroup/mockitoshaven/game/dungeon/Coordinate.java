package com.fdmgroup.mockitoshaven.game.dungeon;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Embeddable
public class Coordinate {
	@Column(nullable=true)
	private final int x;
	@Column(nullable=true)
	private final int y;
	@Column(name="mapid", nullable=true)
	private String mapId;

	@JsonCreator
	public Coordinate(@JsonProperty("x") int x,@JsonProperty("y") int y, @JsonProperty("mapId") String mapId) {
		super();
		this.x = x;
		this.y = y;
		this.mapId = mapId;
	}
	public Coordinate(Coordinate co) {
		this(co.x, co.y, co.mapId);
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public String getMapId() {
		return mapId;
	}
	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	@Override
	public String toString() {
		return "Coordinate [x=" + x + ", y=" + y + ", mapId=" + mapId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mapId == null) ? 0 : mapId.hashCode());
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (mapId == null) {
			if (other.mapId != null)
				return false;
		} else if (!mapId.equals(other.mapId))
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	

	
	
	
   
}
