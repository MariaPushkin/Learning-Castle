package ru.mashnatash.learning_castle.tools;

public class MarkCounter {
    public static int countMark(int type, String question, String answer) {
        switch (type) {
            case 1:
                return testTypeOne(question, answer);
            case 2:
                return testTypeTwo(question, answer);
            default:
                return 0;
        }
    }

    public static int countMark(int score, int maxscore, int topic) {
        switch (topic) {
            case 1: return bubbleMark(score);
            case 2: return cloudeMark(score);
            default: return otherMark(score, maxscore);
        }
    }

    private static int bubbleMark(int score){
        if(score > -41 && score <= -10) return 2;
        if(score > -10 && score <= 5) return 3;
        if(score > 5 && score <=8) return 4;
        return 5;
    }

    private static int cloudeMark(int score){
        if(score > -15 && score <= -5) return 2;
        if(score > -5 && score <= 1) return 3;
        if(score > 1 && score <=3) return 4;
        return 5;
    }

    private static int otherMark(int score, int maxscore) {
        return Math.round(score/maxscore * 5);
    }

    private static int testTypeOne(String question, String answer){
        String rightAnswer = JSONManager.getRightAnswer(question);
        if(answer.equals(rightAnswer)) return 1;
        return 0;
    }

    private static int testTypeTwo(String question, String answer){
        //TODO сделать проверку и подсчет баллов для вопроса с множеством ответов
        return -1;
    }
}
