package sg.edu.nus.se26pt03.photolearn.BAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chen ping on 7/3/2018.
 */

public class QuizItem extends Item{

    private String explaination ;
    private int position;
    private List<QuizOption> options;

    public QuizItem() {
        options = new ArrayList<QuizOption>();
    }

    public boolean checkAnswer(int selectedOptionID) {
        return true;
    }

    public  void createQuizeOption(QuizOption option){

    }
    public  void updateQuizeOption(QuizOption option){

    }
    public  void deleteQuizeOption(int optionId){

    }
    public List<QuizOption> getOptions(){
        return  null;
    }

    public void createQuizAnswer(QuizAnswer answer, int userID) {
    }

}