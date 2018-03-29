package sg.edu.nus.se26pt03.photolearn.DAL;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.se26pt03.photolearn.utility.DateConversionHelper;

/**
 * Created by chen ping on 3/10/2018.
 * Restructured by MyatMin on 12/3/2018.
 */

public class QuizItemDAO extends  BaseDAO {
    private String QuizTitleId;
    private String PhotoURL;
    private String Content;
    private String Explaination ;
    private int Position;

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    private double Latitude;
    private double Longitude;
    public QuizItemDAO() {
        this.quizOptionDAOS= new ArrayList<>();
    }

    public List<QuizOptionDAO> getQuizOptionDAOS() {
        return quizOptionDAOS;
    }

    public void setQuizOptionDAOS(List<QuizOptionDAO> quizOptionDAOS) {
        this.quizOptionDAOS = quizOptionDAOS;
    }

    private List<QuizOptionDAO> quizOptionDAOS;

    public String getQuizTitleId() {
        return QuizTitleId;
    }

    public void setQuizTitleId(String quizTitleId) {
        QuizTitleId = quizTitleId;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getExplaination() {
        return Explaination;
    }

    public void setExplaination(String explaination) {
        Explaination = explaination;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

}
