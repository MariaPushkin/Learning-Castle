using System.Collections;
using System.Collections.Generic;
using WebSocketSharp;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;


public class WebSockets : MonoBehaviour {

    public WebSocket socket;
    public bool isSocket = false;
    User user = new User();

    public class User
    {
        public int status;
        public int id;

        public void Clear()
        {
           
        }
    }

    private class Message
    {
        public string login;
        public string password;
    }


    private void Awake()
    {
            
    }

    private void Update()
    {
        
    }

    public void auth()
    {
        socket = new WebSocket("ws://127.0.0.1:16000");
        isSocket = true;
        socket.Connect();
        string login = GameObject.Find("Login").GetComponent<InputField>().text;
        string password = GameObject.Find("Password").GetComponent<InputField>().text;
        Message message = new Message();
        message.login = login;
        message.password = password;
        string jsonmessage = JsonUtility.ToJson(message);
        socket.Send(jsonmessage);

        socket.OnMessage += (sender, e) =>
        {
            Debug.Log(e.Data);
            JsonUtility.FromJsonOverwrite(e.Data, user);
        };

        System.Threading.Thread.Sleep(500);
        if (user.status == 1)
        {
            Debug.Log("OK");
            SceneManager.LoadScene("Map");
        }
        else
        {
            Debug.Log("NOT OK");
                socket.Close();
            //Debug.Log(user.status);
            //Debug.Log(user.id);
        }
    }

}
