package rockcrabs;
	import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;
		@ScriptManifest(category = Category.MISC,name="Sand Crab bot farm",description ="",author="scape2code",version=1.0)

		public class RockCrab extends AbstractScript {
		    private static final Area SANDCRABAREA = new Area(1858,3541,1864,3535);//(3253,3255,3265,3295);
		    private static final String SANDCRAB="Sand Crab";
		    private static final Tile sandCrabBoatRide = new Tile(3058,3193);
		    private static final Tile pcBoatRide = new Tile(2659,2677);
		    private static final Tile bestTile = new Tile(1862,3536);
		    private static final Tile pcBankTile = new Tile(2667,2653);
		    private static final Area MAKEAGGRESSIVEAREA = new Area(1831,3543,1833,3537);// 1855 3569 1861 3560
		    private static Filter<NPC>SQUIREFILTER;
		    private static final Filter<NPC>SANDCRABFILTER = npc -> npc != null && npc.getName().equals(SANDCRAB) && SANDCRABAREA.contains(npc) && !npc.isHealthBarVisible();
		    private int attackType;
		    private int strLevel;
		    private int attackLevel;
		    private int oldAttackLevel=-1;
		    private Timer timeRunning;
		    private Timer unAggressiveTimer;

		    public void onStart() {
		    	//attackLevel = 10000;
		        
		        attackType = getPlayerSettings().getConfig(43);// tells  us what attack style is active
		        attackLevel = getSkills().getRealLevel(Skill.ATTACK);
		        log("Attack Level: "+attackLevel);
		        log("Old AttackLevl:"+ oldAttackLevel);
		        timeRunning = new Timer();
		        unAggressiveTimer = new Timer(60*1000*10);
		    }

		    @Override
		    public int onLoop() {
		    	//takeBoatRideToPortSarim();
		    	//log("Player is in make agressive area" + MAKEAGGRESSIVEAREA.contains(getLocalPlayer()));
		    	//checkIfSpotIsClear();
		    	if(unAggressiveTimer.finished())
		    	{
		    		if(!getLocalPlayer().isInCombat())
		    		{
		    		// walk to makeaggressive area
			    		while(!MAKEAGGRESSIVEAREA.contains(getLocalPlayer()))
			    		{
			    		getWalking().walk(MAKEAGGRESSIVEAREA.getRandomTile());
			    			//sleep(Calculations.random(25,100));
			    		sleepUntil(()->getWalking().getDestinationDistance()<Calculations.random(3,6),15000);
			    		}
			    		log("in makeagreesive area");
		    		}
		    		
		    	}
		        locationCheck();
		        eat();
		       // if(!getLocalPlayer().isInCombat())// dont thing i need this kill sandcrab should do this on its own
		      //  {
		            killSandCrab();

		       // }
		      checkLevels(getSkills().getRealLevel(Skill.STRENGTH),getSkills().getRealLevel(Skill.ATTACK));
		      checkStats();

		        return Calculations.random(300,3000);
		    }
		    private void locationCheck(){
		        while (!SANDCRABAREA.contains(getLocalPlayer())) { // what if the sandcrab is not in the area
		            getWalking().walk(bestTile);
		            sleepUntil(()->getWalking().getDestinationDistance()<Calculations.random(3,6),25000);
		        }
		        
		    }
		    private void killSandCrab(){
		      if(getLocalPlayer().getTile().equals(bestTile))
		      {
		    	  
		    	  log(" Time till unagressive "+unAggressiveTimer.remaining());
		      }
		      else { getWalking().walk(bestTile);
		      	unAggressiveTimer.reset();
		      }
		        
		    }
		    public void checkLevels(int strLevel,int attackLevel)
		    {
		    attackType = getPlayerSettings().getConfig(43);
		    String attackStyle;
		     log("oldAttackLevl " + oldAttackLevel +"new attack levl"+ attackLevel);
		    attackLevel = getSkills().getRealLevel(Skill.ATTACK);
	         strLevel = getSkills().getRealLevel(Skill.STRENGTH);
	         log(getEquipment().getNameForSlot(3));
	        if(oldAttackLevel< attackLevel) {
	         wieldWeapon(attackLevel,getEquipment().getNameForSlot(3));
	         oldAttackLevel = attackLevel;
	        }
	    	if(strLevel >= 8 && attackLevel < 5 )
	    	{	
	    		if(attackType != 0)
	    		{
	    		attackStyle = "Attack";
				switchAttackStyles(attackStyle);
	    		}
	    	
				
	    	}
	    	
				else if(strLevel>= 25 && attackLevel < 20)
				{
					if(attackType != 0) {
					attackStyle = "Attack";
					switchAttackStyles(attackStyle);
					}
				}
				
				else if(strLevel >= 30 && attackLevel <30)
				{
					if(attackType != 0) {
					attackStyle = "Attack";
					switchAttackStyles(attackStyle);
					}
				}
				else if (strLevel >= 40 && attackLevel < 40) {
					if(attackType !=0) {
						attackStyle ="Attack";
						switchAttackStyles(attackStyle);
					}
				}
				else if (strLevel >= 50 && attackLevel < 50) {
					if(attackType !=0) {
						attackStyle ="Attack";
						switchAttackStyles(attackStyle);
					}
				}
	    		
				else {
					if(attackType != 1) {
					attackStyle = "Strength";
					switchAttackStyles(attackStyle);
					}
				}
	    	
		    }
		    public void switchAttackStyles(String attackStyle) {
		
		   log("in switch attack styles method");
		    	if(attackStyle.equals("Attack"))
		    	{
		    		getTabs().open(Tab.COMBAT);
		    		sleepUntil(()->getTabs().isOpen(Tab.COMBAT),6000);
		    		WidgetChild attackTab = getWidgets().getWidgetChild(593,4);// need to add arguments
		    		attackTab.interact("Chop");
		    		sleep(Calculations.random(350,680));
		    		getTabs().open(Tab.INVENTORY);
		    		sleep(150);
		    	}
		    	if(attackStyle.equals("Strength"))
		    	{
		    		getTabs().open(Tab.COMBAT);
		    		sleepUntil(()->getTabs().isOpen(Tab.COMBAT),6000);
		    		WidgetChild strengthTab = getWidgets().getWidgetChild(593,8);// need to add arguments
		    		//maybe if i  can just click it
		    		strengthTab.interact("Slash");
		    		sleep(Calculations.random(350,680));	
		    		getTabs().open(Tab.INVENTORY);
		    		sleep(150);
		    	}
		    	
		    }
		    public void telePestControl()
		    {
		    	if(getInventory().contains("Pest control teleport")) {
            		getInventory().get("Pest control teleport").interact();
            	}
		    }
		    private void eat(){// if it has a eat option at it lol
		        if(getCombat().getHealthPercent()< 55) {
		            if (getInventory().contains("Shark")) {
		                log("Should be eating");
		                getInventory().get("Shark").interact("Eat");
		         

		            }
		            else {
		            	if(getInventory().contains("Pest control teleport"))
		            		telePestControl();
		            	else stop();	
		            }
		            }
		        }
		    public void bank()
		    {
		    	while(!getBank().isOpen())
		    	{//2667,2653
		    	
		    	getWalking().walk(pcBankTile);// need to walk to bank area then open bank and with sharks and potions
		    	sleepUntil(()->getWalking().getDestinationDistance()==0,12000);
		    	getBank().open();
		    	sleepUntil(()->getBank().isOpen(),5000);
		    	}
		    	if(getBank().isOpen())
		    	{
		    		getBank().depositAllItems();
		    		sleepUntil(()->getInventory().isEmpty(),13000);
		    		if(getBank().contains("Super strength(4)"))
		    			getBank().withdraw("Super strength(4)",2);
		    			sleepUntil(()->getInventory().contains("Super strength(4)"),14000);
		    		if (getBank().contains("Super attack(4)"))
		    			getBank().withdraw("Super attack(4)",2);
		    			sleepUntil(()->getInventory().contains("Super attack(4)"),14000);
		    		if(getBank().contains("Pest control teleport"))
		    				getBank().withdrawAll("Pest control teleport");
		    		if (getBank().contains("Shark"))
		    		{
		    			getBank().withdraw("Shark",25);
		    			sleepUntil(()->getInventory().contains("Shark"),15000);
		    		}
		    		else stop();
		    	}
		    	getBank().close();
		    	sleep(Calculations.random(250,4000));
		    	takeBoatRideToPortSarim();//probablymove this into the loop later
		    }
		    public void takeBoatRideToSandCrabs() {
		    	// after takingboat ride to port sarim
		    	getWalking().walk(sandCrabBoatRide);
		    	while(getWalking().getDestinationDistance()>4)
		    	{
			    	getWalking().walk(sandCrabBoatRide);
			    	sleepUntil(()->getWalking().getDestinationDistance()<4,35000);
		    	}
		    }
		    public void takeBoatRideToPortSarim() {
		    	getWalking().walk(pcBoatRide);
		    	while(getWalking().getDestinationDistance()<3)
		    	{
		    	getWalking().walk(pcBoatRide);
		    	sleepUntil(()->getWalking().getDestinationDistance()<4,15000);
		    	}
		    	SQUIREFILTER = npc->npc.hasAction("Travel");
		    	NPC squirenpc = getNpcs().closest(SQUIREFILTER);
		    	log("should be interacting with squire npc to travel");
		    	squirenpc.interact("Travel");
		    	sleep(5000);
		    	if(getDialogues() == null)
		    	{
		    	sleepUntil(()->getDialogues() != null,15000);
		    	}
		    	GameObject gangPlank=getGameObjects().closest(14303);
		    	log("Gangplank"+ gangPlank.getName());
		    	sleepUntil(()->gangPlank.interact("Cross"),15000);
		    	takeBoatRideToSandCrabs();
		    	//getNpcs().closest(npc->npc.hasAction("Travel"),14000);
		    	//getNpcs().all(npc->npc.hasAction("Travel"),15000);//(npc->npc.hasAction("Talk"),15000);
		    }
		    public void wieldWeapon(int attLevel,String type)
		    {	//1323 1325 1329 1331
		    	//make sure ironscim is weilded
		    	//Item(int itemId, int stack, org.dreambot.core.Instance instance) 
		    	if(attackLevel >= 40)
		    	{
		    		if(!type.equals("Rune scimitar"))
		    		{
			    		if(getInventory().contains("Rune scimitar"))
			    		{
			    			log("should be wearing a rune scim");
			    			Item runescim= getInventory().get("Rune scimitar");
			    			runescim.interact();
			    			// should make sure im in inventory
			    		}
		    		}	
		    	}
		    	else if(attackLevel >= 30)
		    	{
		    		log("attack level is greater than 5");
		    		if(!type.equals("Adamant scimitar"))
		    		{
			    		if(getInventory().contains("Adamant scimitar"))
			    		{
			    			log("should be wearing an adamant scim");
			    			Item adamantscim= getInventory().get("Adamant scimitar");
			    			adamantscim.interact();
			    			// should make sure im in inventory
			    		}
		    		}
		    		}
		    		else if (attackLevel>=20)
		    		{
		    			if(!type.equals("Mithril scimitar"))
		    			{
			    			if(getInventory().contains("Mithril scimitar"))
			    			{
			    				Item mithrilscim = getInventory().get("Mithril scimitar");
			    				mithrilscim.interact("Wield");
			    			}
		    			}
		    		}
		    		else if (attackLevel>=5) { //for testing purpose
		    			if(!type.equals("Steel scimitar"))
		    			{
			    			if(getInventory().contains("Steel scimitar"))
			    		    {	log("inventory contains steel scimitar");
			    		    	Item steelscim= getInventory().get("Steel scimitar");
			    				steelscim.interact("Wield");
			    		    }
		    			}
		    		}
		    	/*	else if(attackLevel>=40)
		    			if(getInventory().contains("Rune scimitar"))
		    		    {
		    				Item runescim = new Item();
		    				runescim.interact("weild");
		    		    } */
		    	
		    }
		    public void moveMouse() {
		    	int randomNumber = Calculations.random(-1,5);
		    	if(randomNumber >= 4)
		    	getMouse().move(new Point(Calculations.random(0,800),Calculations.random(0,600)));
		    	sleep(30,300);
		    }
		    public void checkStats(){
		    	int randomNumber = Calculations.random(-1,5);
		    	if(randomNumber >4)
		    	{
		        getTabs().openWithMouse(Tab.STATS);
		        if (getTabs().isOpen(Tab.STATS)){
		            if(getSkills().hoverSkill(Skill.STRENGTH))
		            {
		                sleep(Calculations.random(3000,5500));
		                getTabs().openWithMouse(Tab.INVENTORY);
		                sleepUntil(()->getTabs().isOpen(Tab.INVENTORY),7000);
		                sleep(Calculations.random(300,500));
		            }
		            else{
		                onLoop();
		            }
		        }
		    	}
		    }
		    public void moveCamera() {
		    	if(Calculations.random(-2,5)>=4)
		    	{
			    	int currentYaw =getCamera().getYaw();
			    	int randomNumber = Calculations.random(-200,200);
			    	if(randomNumber < 0)
			    	{
			    	int minYaw = currentYaw -randomNumber;
			    	getCamera().rotateToYaw(Calculations.random(minYaw,0));
			    	}
			    	else
			    	{
			    		int maxYaw = currentYaw + randomNumber;
			    		getCamera().rotateToYaw(Calculations.random(0,maxYaw));
			    		
			    	}
		    	}
		    }
		    public void hopWorlds(boolean isClear)
		    {
		    	List<World> worlds = getWorlds().all(f->f.isMembers());// client.getWorld()
		    	if(!getLocalPlayer().isInCombat()) {
		    	while(!isClear) { // hopefully while false dot his
		    		for(World world : worlds)
		    		{
		    			getWorldHopper().hopWorld(world);
		    			sleep(Calculations.random(5000,10000));
		    		}
		    	}
		    	}
		    
		    }
		    public void checkIfSpotIsClear()
		    {	List<Player> players = getPlayers().all(player -> SANDCRABAREA.contains(player));
		    	//Filter<Player> playerfilter = player -> SANDCRABAREA.contains(player);
		    	Player me = getLocalPlayer();
		    	if (players.contains(me))
		    		players.remove(me);
		    	if(!players.isEmpty()) {
		    	log("contains a player need to hop");
		    	hopWorlds(false);
		    	
		    	}
		    
		    	
		    }
		   
		    @Override
		    public void onPaint(Graphics graphics) {
		        super.onPaint(graphics);
		        int strLevel = getSkills().getRealLevel(Skill.STRENGTH);
		        int attackLevel = getSkills().getRealLevel(Skill.ATTACK);
		        int healthPercent = getCombat().getHealthPercent();
		        
		        graphics.drawString("Time Running: " + timeRunning.formatTime(),20, 20);
		        graphics.drawString("Current Strength level "+strLevel,20,40);
		        graphics.drawString("Current Attack Level: " + attackLevel, 20, 55);
		        graphics.drawString("Current Health Percentage" + healthPercent,20,70);
		        graphics.drawString("Current Attack Type " + attackType, 20, 85);
		    }
		}






