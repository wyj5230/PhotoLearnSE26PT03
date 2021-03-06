package sg.edu.nus.se26pt03.photolearn.BAL;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sg.edu.nus.se26pt03.photolearn.service.QuizAnswerService;
import sg.edu.nus.se26pt03.photolearn.service.QuizItemService;
import sg.edu.nus.se26pt03.photolearn.service.ServiceCallback;

/**
 * Created by part time team 3 on 7/3/2018.
 */

public class QuizTitle extends Title implements Serializable {
    private List<QuizItem> quizItems;
    transient QuizItemService  quizItemService;
    transient QuizAnswerService quizAnswerService;

    public QuizTitle() {
        quizItemService = new QuizItemService(this);
        quizAnswerService = new QuizAnswerService();
        setLearningSession(new LearningSession());
        quizItems = new ArrayList<QuizItem>();
    }

    public void copy(QuizTitle value) {
        //set learning session Id
        this.setId(value.getId());
        this.setTitle(value.getTitle());
        this.setCreatedBy(value.getCreatedBy());
    }

    public int getQuizScore(int userId) {
        return 0;
    }

    public void removeAllAnswers(List<QuizAnswer> quizAnswers, ServiceCallback<List<Boolean>> callback) {
        List<Boolean> result = new ArrayList<Boolean>();
        for (QuizAnswer quizAnswer : quizAnswers) {
            quizAnswerService.delete(quizAnswer, new ServiceCallback<Boolean>() {
                @Override
                public void onComplete(Boolean data) {
                    result.add(data);
                    if (result.size() == quizAnswers.size()) {
                        callback.onComplete(result);
                    }
                }

                @Override
                public void onError(int code, String message, String details) {
                    callback.onError(code,message,details);
                }
            });
        }
    }

    public void getQuizSubmissionProgress(String createdBy, ServiceCallback<AbstractMap.SimpleEntry<List<Item>, List<QuizAnswer>>> callback) {
        getQuizItems(new ServiceCallback<List<Item>>() {
            @Override
            public void onComplete(List<Item> data) {
                List<QuizAnswer> result = new ArrayList<QuizAnswer>();
                for (Item item: data) {
                    ((QuizItem) item).getQuizAnswers(createdBy, new ServiceCallback<List<QuizAnswer>>() {
                        @Override
                        public void onComplete(List<QuizAnswer> childData) {
                            if (childData.size() > 0) {
                                result.add(childData.get(0));
                            }
                            if (data.indexOf(item) == data.size()-1) {
                                callback.onComplete(new AbstractMap.SimpleEntry<List<Item>, List<QuizAnswer>>(data, result));
                            }
                        }
                        @Override
                        public void onError(int code, String message, String details) {
                            callback.onError(code, message, details);
                        }
                    });
                }
            }

            @Override
            public void onError(int code, String message, String details) {

            }
        });
    }


    @Override
    public void createItem(Item item, ServiceCallback<Item> callback) {
        try {
            quizItemService.save((QuizItem) item, callback);

        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void updateItem(Item item, ServiceCallback<Boolean> callback) {
        try {
            quizItemService.update((QuizItem) item, callback);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void deleteItem(String itemId, ServiceCallback<Boolean> callback) {
        try {

            quizItemService.deleteById(itemId, callback);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public void getItems(ServiceCallback<List<Item>> callback) {
        try {
            quizItemService.getAllByQuizTitleId(this.getId(), callback);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void getQuizItems(ServiceCallback<List<Item>> callback) {
        try {
            quizItemService.getAllByKeyValue("quizTitleId", this.getId(), callback);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
