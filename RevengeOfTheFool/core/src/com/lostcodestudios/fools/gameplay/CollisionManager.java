package com.lostcodestudios.fools.gameplay;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.gameplay.entities.Human;

public class CollisionManager implements ContactListener {

	private static final float KNOCKBACK_TIME = 0.4f;
	
	private GameWorld world;
	
	public CollisionManager(GameWorld world, World physicsWorld) {
		this.world = world;
		
		physicsWorld.setContactListener(this);
	}
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();
		
		// if one of the bodies belongs to a sensor with an event script, and an entity has walked into it, run the script
		if ((bodyA.getUserData() instanceof String || bodyB.getUserData() instanceof String)
				&& (bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human)) {
			
			String eventScript = (bodyA.getUserData() instanceof String ? (String) bodyA.getUserData() : (String) bodyB.getUserData());
			Human e = (bodyA.getUserData() instanceof Human ? (Human) bodyA.getUserData() : (Human) bodyB.getUserData());
			
			ObjectMap<String, Object> args = new ObjectMap<String, Object>();
			args.put("e", e);
			world.scripts.runScript(eventScript, args);
			
		}
		
		// if two humans collide with each other, they each inflict weapon damage and take knockback
		if (bodyA.getUserData() instanceof Human && bodyB.getUserData() instanceof Human) {
			Human humanA = (Human) bodyA.getUserData();
			Human humanB = (Human) bodyB.getUserData();
			
			if (!humanA.group.equals(humanB.group)) { // entities of same group don't hurt 
			
				float damageA = 0f;
				float damageB = 0f;
				
				if (humanA.weapon != null) {
					damageA = humanA.weapon.meleeDamage;
				}
				if (humanB.weapon != null) {
					damageB = humanB.weapon.meleeDamage;
				}
				
				humanA.damage(damageB, humanB);
				humanB.damage(damageA, humanA);
				
				float knockbackA = Math.max(damageB - damageA, 0) * 2;
				float knockbackB = Math.max(damageA - damageB, 0) * 2;
				
				humanA.body.setLinearVelocity(humanB.getVelocity().cpy().nor().scl(knockbackA));
				humanB.body.setLinearVelocity(humanA.getVelocity().cpy().nor().scl(knockbackB));
				
				humanA.knockbackTime = KNOCKBACK_TIME;
				humanB.knockbackTime = KNOCKBACK_TIME;
			}
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
