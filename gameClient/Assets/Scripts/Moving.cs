using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Moving : MonoBehaviour {

	public float WalkSpeed = 60;

	private Animator CharAnimation;
	//private BoxCollider2D CharCollider;
	private Rigidbody2D CharBody;
	//private Transform CharTrans;

	// Use this for initialization
	void Start () {
		CharAnimation = GetComponent<Animator> ();	
		//CharCollider = GetComponent<BoxCollider2D> ();
		CharBody = GetComponent<Rigidbody2D> ();	
		CharAnimation.speed = 1.5f;
	}
	
	// Update is called once per frame
	void FixedUpdate () {
		
		if (Input.GetKey (KeyCode.W) || Input.GetKey (KeyCode.UpArrow)) {
			CharAnimation.Play ("WalkUp");
			Vector2 Movement = new Vector2 (0, 0.1f);
			CharBody.AddForce (Movement * WalkSpeed);
		} 
		else if (Input.GetKey (KeyCode.A) || Input.GetKey (KeyCode.LeftArrow)) {
			CharAnimation.Play ("WalkLeft");
			Vector2 Movement = new Vector2 (-0.1f, 0);
			CharBody.AddForce (Movement * WalkSpeed);
		} 
		else if (Input.GetKey (KeyCode.S) || Input.GetKey (KeyCode.DownArrow)) {
			CharAnimation.Play ("WalkDown");
			Vector2 Movement = new Vector2 (0, -0.1f);
			CharBody.AddForce (Movement * WalkSpeed);
		} 
		else if (Input.GetKey (KeyCode.D) || Input.GetKey (KeyCode.RightArrow)) {
			CharAnimation.Play ("WalkRight");
			Vector2 Movement = new Vector2 (0.1f, 0);
			CharBody.AddForce (Movement * WalkSpeed);
		} 
		else 
		{
			CharAnimation.Play ("Idle");
		}

	}
}
