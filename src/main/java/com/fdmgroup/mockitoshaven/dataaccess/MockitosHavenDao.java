package com.fdmgroup.mockitoshaven.dataaccess;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;

@Component
public interface MockitosHavenDao  {
	<T> boolean writeObject(T object);
	<T, ID> T readObject(Class<T> type, ID id);
	<T, ID> boolean deleteObject(Class<T> type, ID id);
	<T> List<T> findAllObjects(Class<T> type);
	<T, ID> T updateObject(Class<T> type, T object, ID id);
	<T, FIELD> List<T> findAllObjectsByField(Class<T> type, String fieldName, FIELD fieldValue);
	<T, U, FIELD> List<T> findAllFieldValuesByObject(Class<U> class1, String fieldName);
}