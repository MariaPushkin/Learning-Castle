package ru.mashnatash.learning_castle.data.userData;

public class PlayerAnswers
{
    int code;
    int userId;
    public String[] answers;
    public int[] idOrder;

    public PlayerAnswers() {}

    public PlayerAnswers(int a) {
        this.code = a;
        this.userId = 10;
        this.answers = new String[]{"4","1","18","2","2","1,2,4,9,16,25,36,49,64,81,100,121,144,169,196"};
        this.idOrder = new int[]{1,11,13,8,18,20};
    }

    public int getUserId() {
        return userId;
    }
}