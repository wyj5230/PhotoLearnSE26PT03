package sg.edu.nus.se26pt03.photolearn.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.nus.se26pt03.photolearn.BAL.QuizAnswer;
import sg.edu.nus.se26pt03.photolearn.BAL.QuizItem;
import sg.edu.nus.se26pt03.photolearn.BAL.QuizOption;
import sg.edu.nus.se26pt03.photolearn.BAL.QuizTitle;
import sg.edu.nus.se26pt03.photolearn.R;
import sg.edu.nus.se26pt03.photolearn.application.App;
import sg.edu.nus.se26pt03.photolearn.enums.AppMode;
import sg.edu.nus.se26pt03.photolearn.enums.UserRole;
import sg.edu.nus.se26pt03.photolearn.service.QuizAnswerService;
import sg.edu.nus.se26pt03.photolearn.service.ServiceCallback;
import sg.edu.nus.se26pt03.photolearn.utility.AsyncLoadImageHelper;
import sg.edu.nus.se26pt03.photolearn.utility.GPSHelper;
import sg.edu.nus.se26pt03.photolearn.utility.TTSHelper;

/**
 * Created by MyatMin on 08/3/18.
 */
public class QuizItemFragment extends BaseFragment {


    public static final String ARG_ITEM_COUNT = "ITEM_COUNT";
    public static final String ARG_ITEM = "ITEM";

    private int itemPosition;
    private QuizItem quizItem;
    private QuizAnswerService quizAnswerService;
    private QuizAnswer quizAnswer;
    private GPSHelper gpsHelper;
    private QuizTitle quizTitle;
    private CheckBox chk_opt1, chk_opt2, chk_opt3, chk_opt4;
    private ViewPager mPager;
    private Button btnPrev;
    private Button btnNext;


    public static QuizItemFragment create(int itemNumber, QuizItem quizItem) {
        QuizItemFragment quizItemFragment = new QuizItemFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemNumber);
        args.putSerializable(ARG_ITEM, quizItem);
        quizItemFragment.setArguments(args);
        return quizItemFragment;
    }

    public QuizItemFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemPosition = getArguments().getInt(ARG_ITEM_COUNT);
        quizItem = (QuizItem) getArguments().getSerializable(ARG_ITEM);
        gpsHelper = new GPSHelper(getContext());
        quizAnswerService = new QuizAnswerService();
        // this.getActivity().setTitle("Item :"+itemPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_quiz_item, container, false);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupControl();
        setupView();
    }

    ImageView imgPhotView;
    TextView txtContentView;
    TextView txtViewLocation;
    ProgressBar progressBar;


    private void setupControl() {

        imgPhotView = getView().findViewById(R.id.imgItemPhotoUrl);
        txtContentView = getView().findViewById(R.id.txt_content);
        txtContentView.setMovementMethod(new ScrollingMovementMethod());
        txtViewLocation = getView().findViewById(R.id.tv_location);

        btnPrev = (Button) getView().findViewById(R.id.btn_prev);
        btnNext = (Button) getView().findViewById(R.id.btn_next);
        mPager = (ViewPager) getParentFragment().getView().findViewById(R.id.vp_quizitem);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizAnswer = new QuizAnswer();
                quizAnswer.setQuizItemId(quizItem.getId());
                quizAnswer.setSelectedOptionIds(getCheckedIds());
                quizAnswerService.save(quizAnswer, new ServiceCallback<QuizAnswer>() {
                    @Override
                    public void onComplete(QuizAnswer data) {
                        displayInfoMessage("Quiz Answer saved successfully!");
                        mPager.arrowScroll(1);
                    }

                    @Override
                    public void onError(int code, String message, String details) {
                        displayErrorMessage(message);
                    }
                });

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizAnswer = new QuizAnswer();
                quizAnswer.setQuizItemId(quizItem.getId());
                quizAnswer.setSelectedOptionIds(getCheckedIds());
                quizAnswerService.save(quizAnswer, new ServiceCallback<QuizAnswer>() {
                    @Override
                    public void onComplete(QuizAnswer data) {
                        displayInfoMessage("Quiz Answer saved successfully!");
                        displayInfoMessage(data.getId());
                        mPager.arrowScroll(2);
                    }

                    @Override
                    public void onError(int code, String message, String details) {
                        displayErrorMessage(message);
                    }
                });

            }
        });
        progressBar = getView().findViewById(R.id.quizitemListprogressBarSmall);


        if (quizItem.getCoordinate() != null) {
            String location = gpsHelper.GetLocationByLatandLongitudeAsString(Double.valueOf(quizItem.getCoordinate().getLatitude()), Double.valueOf(quizItem.getCoordinate().getLongitude()));
            txtViewLocation.setText(location);
        }

        try {
            AsyncLoadImageHelper loader = new AsyncLoadImageHelper(imgPhotView, getContext(), progressBar);
            loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, quizItem.getPhotoURL());

        } catch (Exception ex) {
            Log.w("adapter", "error");
        }

        txtContentView.setText(quizItem.getContent());
        chk_opt1 = getView().findViewById(R.id.chk_option1);
        chk_opt2 = getView().findViewById(R.id.chk_option2);
        chk_opt3 = getView().findViewById(R.id.chk_option3);
        chk_opt4 = getView().findViewById(R.id.chk_option4);
        if (quizItem.getQuizOptions() != null && quizItem.getQuizOptions().size() > 0) {
            QuizOption quizOption1 = quizItem.getQuizOptions().get(0);
            chk_opt1.setText(quizOption1.getContent());
            QuizOption quizOption2 = quizItem.getQuizOptions().get(1);
            chk_opt2.setText(quizOption2.getContent());
            QuizOption quizOption3 = quizItem.getQuizOptions().get(2);
            chk_opt3.setText(quizOption3.getContent());
            QuizOption quizOption4 = quizItem.getQuizOptions().get(3);
            chk_opt4.setText(quizOption4.getContent());
        }

    }

    void setupView() {
        if (App.getCurrentAppMode() == AppMode.TRAINER) {
            chk_opt1.setEnabled(false);
            chk_opt2.setEnabled(false);
            chk_opt3.setEnabled(false);
            chk_opt4.setEnabled(false);
        }
        else {
            chk_opt1.setEnabled(true);
            chk_opt2.setEnabled(true);
            chk_opt3.setEnabled(true);
            chk_opt4.setEnabled(true);
        }
    }

    List<String> getCheckedIds() {
        List<String> selectedOptionIds = new ArrayList<>();
        if (chk_opt1.isChecked()) selectedOptionIds.add("1");
        if (chk_opt2.isChecked()) selectedOptionIds.add("2");
        if (chk_opt3.isChecked()) selectedOptionIds.add("3");
        if (chk_opt4.isChecked()) selectedOptionIds.add("4");
        return selectedOptionIds;
    }
}
