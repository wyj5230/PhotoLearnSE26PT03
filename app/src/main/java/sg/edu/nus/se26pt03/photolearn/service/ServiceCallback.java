package sg.edu.nus.se26pt03.photolearn.service;

/**
 * Created by part time team 3  on 21/3/18.
 */

public interface ServiceCallback<T> {
    void onComplete(T data);
    void onError(int code, String message, String details);
}
