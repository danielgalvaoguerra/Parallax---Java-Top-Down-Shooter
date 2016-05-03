
public class EnemyWaveHandler {
	
	private int currentWave;
	private int numberOfEnemies;
	
	public EnemyWaveHandler()
	{
		currentWave = 1;
		numberOfEnemies = 1;
	}
	
	public void goToNextWave()
	{
		currentWave++;
		
		if(currentWave == 2)
		{
			numberOfEnemies = 2;
		}
		else if(currentWave == 3)
		{
			numberOfEnemies = 4;
		}
		else if(currentWave >= 4 && currentWave <= 8)
		{
			numberOfEnemies = currentWave*2;
		}
		else if(currentWave >= 9 && currentWave <= 13)
		{
			numberOfEnemies = currentWave*3;
		}
		else if(currentWave > 13)
		{
			numberOfEnemies = currentWave*4;
		}
	}
	
	public int getCurrentWave()
	{
		return currentWave;
	}
	
	public int getNumberOfEnemies()
	{
		return numberOfEnemies;
	}
}
