using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class BubbleGame : MonoBehaviour {

	public GameObject bubblePrefab;
	public GameObject ex; 
	public int BubbleNum = 3;
	public static int score = 0;
	public static int round = 0;

	private int answer;

	private int[] xArr = new int[8];

	private float[] posArr = new float[8];

	// Use this for initialization
	void Start () {

	}
	
	// Update is called once per frame
	void Update () {
		if (!FindObjectOfType<Bubble> ()) {
			BubbleSpawn ();
			round++;
		}
	}

	void Rand(){

		for (int i = 0; i < BubbleNum; i++) {
			int x;
			do {
				x = Random.Range (0, 20); 
			} while (System.Array.IndexOf (xArr, x) > -1);
			xArr [i] = x;
			answer = x;
		}

		for (int i = 0; i < BubbleNum; i++) {
			float pos;
			do {
				pos = Random.Range (-7.5f, 7.5f);
			} while (System.Array.IndexOf (posArr, pos) > -1);
			posArr [i] = pos;
		}
	}

	void BubbleSpawn(){
		Rand ();

		ex.GetComponent<Text> ().text = (answer * answer).ToString ();

		for (int i = 0; i < BubbleNum; i++) {
			BubbleAct (posArr[i], xArr[i]);
		}
	}

	void BubbleAct(float pos, int value){
		Vector2 frict = new Vector2 (0, Random.Range(-10, -40));

		GameObject bubble = Instantiate(bubblePrefab, new Vector3(pos, 7, 0), Quaternion.identity) as GameObject;
		bubble.GetComponent<Rigidbody2D> ().AddForce (frict);
		bubble.transform.parent = this.transform;
		bubble.GetComponent<Bubble> ().x = value;

		if (answer == value)
			bubble.GetComponent<Bubble> ().isCorrect = true;
		else
			bubble.tag = "WrongBubble";
	}
}
