using UnityEngine;
using System.Collections;
using UnityEngine.SceneManagement;

public class LevelManager : MonoBehaviour {

	void Update(){
		if (BubbleGame.round > 5) {
			LoadLevel ("Menu");		
		}
	}
	
	public void LoadLevel(string name){		
		SceneManager.LoadScene(name);
		BubbleGame.score = 0;
		BubbleGame.round = 0;
	}

	public void QuitRequest(){
		Application.Quit();
	}
	
	public void LoadNextLevel(){
		SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex + 1);
	}
}
