using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Menu : MonoBehaviour {

	public static int castle = 0;

	public void LoadGame(){		
		if (castle == 1)
			SceneManager.LoadScene("Bubbles");
		else
			SceneManager.LoadScene("Map");
	}



}
