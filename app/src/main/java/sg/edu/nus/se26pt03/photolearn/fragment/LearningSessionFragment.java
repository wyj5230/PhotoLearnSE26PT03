package sg.edu.nus.se26pt03.photolearn.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import sg.edu.nus.se26pt03.photolearn.BAL.LearningSession;
import sg.edu.nus.se26pt03.photolearn.R;
import sg.edu.nus.se26pt03.photolearn.application.UserActionCallback;

/**
 * Created by part time team 3  on 08/3/18.
 */
public class LearningSessionFragment extends BaseFragment {
    private LearningSession learningSession;

    public static LearningSessionFragment newInstance(LearningSession learningSession) {
        LearningSessionFragment learningSessionFragment = new LearningSessionFragment();
        Bundle args = new Bundle();
        args.putSerializable("learningSession", learningSession);
        learningSessionFragment.setArguments(args);
        return learningSessionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setupData();
        return inflater.inflate(R.layout.fragment_learning_session, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addFragment(R.id.vp_learningsession_pager, LearningTitleListFragment.newInstance(learningSession), "Titles");
        addFragment(R.id.vp_learningsession_pager, QuizTitleListFragment.newInstance(learningSession), "Quizzes");
        setupTabLayout(R.id.tab_learningsession_layout, R.id.vp_learningsession_pager);
        ((sg.edu.nus.se26pt03.photolearn.view.ViewPager) getView().findViewById(R.id.vp_learningsession_pager)).setSwipeEnabled(false);
    }

    private void setupData() {
        learningSession = (LearningSession) getArguments().getSerializable("learningSession");
    }

    @Override
    public void onAccessModeChange(int mode, UserActionCallback callback) {
        super.onAccessModeChange(mode, new UserActionCallback() {
            @Override
            public void onPass() {
                refresh(LearningSessionFragment.class.getName());
            }
        });
    }
}
