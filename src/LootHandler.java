import java.util.ArrayList;


public class LootHandler {
	
	EnemyWaveHandler waveHandler = new EnemyWaveHandler();
	
	private double rand;
	
	private ArrayList<Loot> lootArray = new ArrayList<Loot>();
	private int size = 0;
	
	public int dropLoot()
	{
		rand = Math.random();
		
		if(rand < 0.1 && rand > 0)
		{
			return Loot.GUN;
		}
		else if(rand > 0.48 && rand < 0.51)
		{
			return Loot.HP;
		}
		
		return -1;
	}
	
	public void collect()
	{}
	
	public int getSize()
	{
		return size;
	}
	
}
