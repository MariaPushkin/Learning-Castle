using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Bubble : MonoBehaviour {

	public AudioClip pop;
	public int x;


	public bool isCorrect = false;

	// Use this for initialization
	void Start () {
		this.GetComponentInChildren<Text> ().text = x.ToString ();
	}
	
	// Update is called once per frame
	void Update () {
		if (this.isCorrect && this.transform.position.y < -5) {
			BubbleGame.score--;
			Destroying ();
			Destroy (gameObject);
		}
	}

	void OnMouseDown(){
		AudioSource.PlayClipAtPoint (pop, Camera.main.transform.position);
		if (isCorrect) {
			BubbleGame.score++;
			Destroying ();
		} else {
			BubbleGame.score--;
		}
		Destroy (gameObject);
	}

	void Destroying(){
		GameObject[] other = GameObject.FindGameObjectsWithTag ("WrongBubble");
		for (int i = 0; i < other.Length; i++)
			Destroy (other[i]);
	}
}
