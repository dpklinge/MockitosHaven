package com.fdmgroup.mockitoshaven.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import com.fdmgroup.mockitoshaven.game.character.NonPlayerCharacter;
import com.fdmgroup.mockitoshaven.game.character.PlayerCharacter;

@Aspect
public class LoggingAspect {
	private static Logger logger= LogManager.getLogger();
	
	@Pointcut("execution(* com.fdmgroup.mockitoshanven.game.character.NonPlayerCharacter.aggress(..))")
	public void aggress() {}

	@Before("aggress()")
	public void beforeAggress(JoinPoint jp) {
		NonPlayerCharacter npc = (NonPlayerCharacter) jp.getTarget();
		logger.trace("Doing aggress in "+npc.getIdentifier());
	}
	
	@Before("execution(* com.fdmgroup.mockitoshaven.game.logic.SpaceLogic.checkVisible(..)) && args(npc, target)")
	public void beforeCheckVisible(NonPlayerCharacter npc, PlayerCharacter target) {
		logger.trace(npc.getIdentifier()+" checking distance from "+target.getIdentifier());
	}
	

}
