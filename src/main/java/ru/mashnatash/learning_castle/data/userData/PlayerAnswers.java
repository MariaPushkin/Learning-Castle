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
        this.userId = 6;
        this.answers = new String[]{"3","1","3","1"};
        this.idOrder = new int[]{2,3,6,8};
    }

    public int getUserId() {
        return userId;
    }
}