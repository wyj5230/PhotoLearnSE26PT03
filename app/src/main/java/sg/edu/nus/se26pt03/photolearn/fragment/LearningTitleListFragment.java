package sg.edu.nus.se26pt03.photolearn.fragment;

import android.app.Dialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import sg.edu.nus.se26pt03.photolearn.BAL.LearningTitle;
import sg.edu.nus.se26pt03.photolearn.R;
import sg.edu.nus.se26pt03.photolearn.adapter.LearningTitleListAdapter;
import sg.edu.nus.se26pt03.photolearn.application.App;
import sg.edu.nus.se26pt03.photolearn.controller.SwipeController;
import sg.edu.nus.se26pt03.photolearn.database.ICallback;
import sg.edu.nus.se26pt03.photolearn.database.IListCallback;
import sg.edu.nus.se26pt03.photolearn.enums.AccessMode;
import sg.edu.nus.se26pt03.photolearn.enums.UserRole;
import sg.edu.nus.se26pt03.photolearn.utility.ConstHelper;

public class LearningTitleListFragment extends BaseFragment {
    private SearchView svSearchView;
    private TextView tvEmpty;
    private LearningTitleListAdapter learningTitleListAdapter;
    private Dialog dialog;
    private int role;
    private int mode;
    private String sessionId;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sessionId = "1";

        mode = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(ConstHelper.SharedPreferences_Access_Mode, AccessMode.toInt(AccessMode.EDIT));
        role = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(ConstHelper.SharedPreferences_User_Id, UserRole.toInt(UserRole.TRAINER));
        userId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(ConstHelper.SharedPreferences_User_Id, "0");

        return inflater.inflate(R.layout.fragment_learning_title_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLearningTitleList();
    }

    private void loadLearningTitleList() {
        App.session.getLearningTitles(sessionId, userId, mode, "", new IListCallback<LearningTitle>() {
            @Override
            public void onCallback(List<LearningTitle> itemList) {
                final List<LearningTitle> learningTitleList = itemList;
                learningTitleListAdapter = new LearningTitleListAdapter(learningTitleList, new LearningTitleListAdapter.LearningTitleViewHolderClick() {
                    @Override
                    public void onItemClick(LearningTitleListAdapter.LearningTitleViewHolder viewHolder) {
                        onLoad(learningTitleList.get(viewHolder.getAdapterPosition()), null);
                    }
                });

                setupViews();
                setupControls();
            }
        });
    }

    private void setupViews() {
        // View fragmentView = inflater.inflate(R.layout.fragment_learning_title_list, container, false);
        RecyclerView rvLearningTitle = getView().findViewById(R.id.rv_learning_title);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvLearningTitle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvLearningTitle.setAdapter(learningTitleListAdapter);

        tvEmpty = getView().findViewById(R.id.tv_empty_value);
        tvEmpty.setVisibility(learningTitleListAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        svSearchView = getView().findViewById(R.id.sv_learningtitle);
        svSearchView.setVisibility(mode == AccessMode.toInt(AccessMode.VIEW) ? View.VISIBLE : View.GONE);
        svSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // your text view here
                callSearch(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String text) {
                callSearch(text);
                return true;
            }

            private void callSearch(String text) {
                // learningTitleListAdapter.refreshLearningTitles(text);
            }
        });

        final SwipeController swipeController = new SwipeController(0, (App.currentAccessMode == AccessMode.EDIT ? R.layout.partial_swipe_item : 0)) {
            @Override
            public void onRevealInflated(View view, int position) {
                if (view instanceof LinearLayout) {
                    LinearLayout linearLayout = (LinearLayout) ((LinearLayout) view).getChildAt(0);
                    // linearLayout.findViewWithTag("edit").setVisibility(View.GONE);
                    // inearLayout.findViewWithTag("delete").setVisibility(View.GONE);
                    linearLayout.findViewWithTag("edit").setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // showDialogue(title);
                        }
                    });
                }
            }

            @Override
            public void onClicked(Object tag, final int position) {
                switch (tag.toString()) {
                    case "delete":
                        App.session.deleteLearningTitle(learningTitleListAdapter.learningTitleList.get(position), new ICallback<Boolean>() {
                            @Override
                            public void onCallback(Boolean item) {
//                                if (item) {
//                                    learningTitleListAdapter.learningTitleList.remove(position);
//                                    learningTitleListAdapter.notifyItemRemoved(position);
//                                    learningTitleListAdapter.notifyItemRangeChanged(position, learningTitleListAdapter.getItemCount());
//                                }
                                loadLearningTitleList();
                            }
                        });

                        //loadLearningTitleList();
                        break;
                    case "edit":
                        //onEdit(learningSessionListAdapter.learningSessionList.get(position), null);
                        break;
                }
            }
        };
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvLearningTitle);
        rvLearningTitle.addItemDecoration(new RecyclerView.ItemDecoration()

        {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    private void setupControls() {
        FloatingActionButton floatingActionButton = getView().findViewById(R.id.fab_learningtitlelist);
        // floatingActionButton.setVisibility((mode == AccessMode.toInt(AccessMode.EDIT) && role == UserRole.toInt(UserRole.PARTICIPENT)) ? View.VISIBLE : View.GONE);
        floatingActionButton.setVisibility(View.VISIBLE);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue(null);
            }
        });
    }

    private void showDialogue(LearningTitle title) {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_title);
        dialog.show();

        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.learning_title);

        final EditText etContent = (EditText) dialog.findViewById(R.id.et_content);
        etContent.setHint(R.string.enter_title);
        if (title != null) {
            etContent.setText(title.title);
        }

        Button btnSave = (Button) dialog.findViewById(R.id.btn_save);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LearningTitle newTitle = new LearningTitle();
                newTitle.sessionId = sessionId;
                newTitle.title = etContent.getText().toString();
                newTitle.createdBy = userId;
                newTitle.timestamp = new Date();

                App.session.createLearningTitle(newTitle);
                loadLearningTitleList();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
