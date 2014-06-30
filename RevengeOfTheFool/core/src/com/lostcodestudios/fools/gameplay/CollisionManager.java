package com.lostcodestudios.fools.gameplay;

import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.lostcodestudios.fools.SoundManager;
import com.lostcodestudios.fools.gameplay.entities.Door;
import com.lostcodestudios.fools.gameplay.entities.Human;
import com.lostcodestudios.fools.gameplay.entities.Item;
import com.lostcodestudios.fools.gameplay.entities.Switch;
import com.lostcodestudios.fools.gameplay.entities.Weapon;
import com.lostcodestudios.fools.scripts.Script;
import com.lostcodestudios.fools.scripts.ai.AI;


public class CollisionManager implements ContactListener {

	private class Spotting {
		public Spotting (Human viewer, Human viewed) {
			this.viewer = viewer;
			this.viewed = viewed;
		}
		
		public Human viewer;
		public Human viewed;
	}
	
	private static final float KNOCKBACK_TIME = 0.4f;
	private static final float KNOCKBACK = 5f;
	
	private GameWorld world;
	
	// this is not a map because an entity could THEORETICALLY see two other entities at once, in which case both spottings would need to be stored,
	// but a traditional map would overwrite the first one
	private Array<Spotting> spottingsToCheck = new Array<Spotting>();
	private Array<Spotting> spottings = new Array<Spotting>();
	
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
		
		if (bodyA.getUserData() == null || bodyB.getUserData() == null) return;
		
		if (fixtureA.isSensor() ^ fixtureB.isSensor()) {
		
			// one of the fixtures is a sensor
			
			// if one of the bodies belongs to a sensor with an event script, and an entity has walked into it, run the script
			if ((bodyA.getUserData() instanceof String || bodyB.getUserData() instanceof String)
					&& (bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human)) {
				
				String eventScript = (bodyA.getUserData() instanceof String ? (String) bodyA.getUserData() : (String) bodyB.getUserData());
				Human e = (bodyA.getUserData() instanceof Human ? (Human) bodyA.getUserData() : (Human) bodyB.getUserData());
				
				ObjectMap<String, Object> args = new ObjectMap<String, Object>();
				args.put("e", e);
				world.scripts.runScript(eventScript, args);
				
			}
			
			// human -> item collision
			
			if ((bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human) 
					&& (bodyA.getUserData() instanceof Item || bodyB.getUserData() instanceof Item)) {
				// item selected now
				
				Item i = null;
				Human h = null;
				
				if (bodyA.getUserData() instanceof Item) {
					i = (Item) bodyA.getUserData();
					h = (Human) bodyB.getUserData();
				}
				
				if (bodyB.getUserData() instanceof Item) {
					i = (Item) bodyB.getUserData();
					h = (Human) bodyA.getUserData();
				}
				
				if (h.tag.equals("Fool") && (!(i instanceof Weapon) || i.holder == null)) {
					i.selected = true;
				}
			}
			
			// human -> switch
			
			if ((bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human) 
					&& (bodyA.getUserData() instanceof Switch || bodyB.getUserData() instanceof Switch)) {
				// switch selected or triggered
				
				Switch s = null;
				Human h = null;
				
				if (bodyA.getUserData() instanceof Switch) {
					s = (Switch) bodyA.getUserData();
					h = (Human) bodyB.getUserData();
				}
				
				if (bodyB.getUserData() instanceof Switch) {
					s = (Switch) bodyB.getUserData();
					h = (Human) bodyA.getUserData();
				}
				
				if (h.tag.equals("Fool")) {
					s.selected = true;
				} else {
					s.trigger(h);
				}
			}
			
			// human sees human
			
			if (bodyA.getUserData() instanceof Human && bodyB.getUserData() instanceof Human) {
				Human viewer = null;
				Human viewed = null;
				
				if (fixtureA.isSensor()) {
					viewer = (Human) bodyA.getUserData();
					viewed = (Human) bodyB.getUserData();
				} else {
					viewer = (Human) bodyB.getUserData();
					viewed = (Human) bodyA.getUserData();
				}
				
				spottingsToCheck.add(new Spotting(viewer, viewed)); // store the two entities to make this check later, outside of the contact event
				
			}
			
		} else if (!(fixtureA.isSensor() || fixtureB.isSensor())) {
		
			// both bodies are physical
			
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
					
					// check for a backstab
					
					final float BACKSTAB_ROTATION_DIFF = (float) (Math.PI / 4);
					
					if (Math.abs(bodyA.getAngle() - bodyB.getAngle()) < BACKSTAB_ROTATION_DIFF) {
						Human backstabber = null;
						Human backstabbee = null;
						
						switch (humanA.getDirection()) {
							case Down:
								
								backstabber = humanA.getPosition().y > humanB.getPosition().y ? humanA : humanB;
								backstabbee = humanA.getPosition().y > humanB.getPosition().y ? humanB : humanA;
								
								break;
								
							case Up:
								
								backstabber = humanA.getPosition().y < humanB.getPosition().y ? humanA : humanB;
								backstabbee = humanA.getPosition().y < humanB.getPosition().y ? humanB : humanA;
								
								break;
								
							case Left:
								
								backstabber = humanA.getPosition().x > humanB.getPosition().x ? humanA : humanB;
								backstabbee = humanA.getPosition().x > humanB.getPosition().x ? humanB : humanA;
								
								break;
								
							case Right:
								
								backstabber = humanA.getPosition().x < humanB.getPosition().x ? humanA : humanB;
								backstabbee = humanA.getPosition().x < humanB.getPosition().x ? humanB : humanA;
								
								break;
						}
						
						backstabbee.damage(100, backstabber);
					} else {
						
						// damage normally
						humanA.damage(damageB, humanB);
						humanB.damage(damageA, humanA);
						
					}
					
					// play the appropriate sound effects
					// positioning and panning don't matter because the player will be involved in ALL collisions
					if (humanA.isDead() || humanB.isDead()) {
						// play a death sound
						SoundManager.playSoundGroup("snd_death");
					} else if (damageA == 0 ^ damageB == 0) {
						// play a hurt sound
						SoundManager.playSoundGroup("snd_sword_damage");
					} else if (damageA > 0 && damageB > 0) {
						// play a clash sound
						SoundManager.playSoundGroup("snd_sword_clash");
					} else {
						// play no sound... this should be rare
					}
					
					Vector2 dirA = bodyA.getPosition().cpy().sub(bodyB.getPosition());
					Vector2 dirB = bodyB.getPosition().cpy().sub(bodyA.getPosition());
					
					humanA.body.setLinearVelocity(dirA.nor().scl(KNOCKBACK));
					humanB.body.setLinearVelocity(dirB.nor().scl(KNOCKBACK));
					
					humanA.knockbackTime = KNOCKBACK_TIME;
					humanB.knockbackTime = KNOCKBACK_TIME;
				}
			}
		
			// human -> door collision
			if ((bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human)
					&& (bodyA.getUserData() instanceof Door || bodyB.getUserData() instanceof Door)) {
				// NPCs automatically open doors if they can
				
				Door d = null;
				Human h = null;
				
				if (bodyA.getUserData() instanceof Door) {
					d = (Door) bodyA.getUserData();
					h = (Human) bodyB.getUserData();
				}
				
				if (bodyB.getUserData() instanceof Door) {
					d = (Door) bodyB.getUserData();
					h = (Human) bodyA.getUserData();
				}
				
				if (d.key.isEmpty() || h.hasItem(d.key)) {
					d.toggleOpen(world);
				}
			}
		}
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		
		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();
		
		if (bodyA.getUserData() == null || bodyB.getUserData() == null) return;
		
		if ((bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human) 
				&& (bodyA.getUserData() instanceof Item || bodyB.getUserData() instanceof Item)) {
			// item not selected anymore
			
			Item i = null;
			Human h = null;
			
			if (bodyA.getUserData() instanceof Item) {
				i = (Item) bodyA.getUserData();
				h = (Human) bodyB.getUserData();
			}
			
			if (bodyB.getUserData() instanceof Item) {
				i = (Item) bodyB.getUserData();
				h = (Human) bodyA.getUserData();
			}
			
			if (h.tag.equals("Fool")) {
				i.selected = false;
			}
		}
		
		// human -> switch
		
		if ((bodyA.getUserData() instanceof Human || bodyB.getUserData() instanceof Human) 
				&& (bodyA.getUserData() instanceof Switch || bodyB.getUserData() instanceof Switch)) {
			// switch selected or triggered
			
			Switch s = null;
			Human h = null;
			
			if (bodyA.getUserData() instanceof Switch) {
				s = (Switch) bodyA.getUserData();
				h = (Human) bodyB.getUserData();
			}
			
			if (bodyB.getUserData() instanceof Switch) {
				s = (Switch) bodyB.getUserData();
				h = (Human) bodyA.getUserData();
			}
			
			if (h.tag.equals("Fool")) {
				s.selected = false;
			}
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
	private static boolean sightBlocked = false;
	public void update(float delta) {
		
	}

}