package sg.edu.nus.se26pt03.photolearn.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import sg.edu.nus.se26pt03.photolearn.BAL.Coordinate;
import sg.edu.nus.se26pt03.photolearn.BAL.Item;
import sg.edu.nus.se26pt03.photolearn.BAL.LearningItem;
import sg.edu.nus.se26pt03.photolearn.BAL.QuizItem;
import sg.edu.nus.se26pt03.photolearn.BAL.QuizOption;
import sg.edu.nus.se26pt03.photolearn.BAL.Title;
import sg.edu.nus.se26pt03.photolearn.R;
import sg.edu.nus.se26pt03.photolearn.application.UserActionCallback;
import sg.edu.nus.se26pt03.photolearn.enums.AccessMode;
import sg.edu.nus.se26pt03.photolearn.enums.EventType;
import sg.edu.nus.se26pt03.photolearn.enums.UserRole;
import sg.edu.nus.se26pt03.photolearn.service.ServiceCallback;
import sg.edu.nus.se26pt03.photolearn.utility.AsyncLoadImageHelper;
import sg.edu.nus.se26pt03.photolearn.utility.ConstHelper;
import sg.edu.nus.se26pt03.photolearn.utility.FileStorageHelper;
import sg.edu.nus.se26pt03.photolearn.utility.GPSTracker;

/**
 * Created by MyatMin on 08/3/18.
 */
public class QuizItemDetailFragment extends BaseFragment {
    private QuizItem src = null;
    private List<QuizOption> quizOptions = null;
    private Title title = null;
    private ImageView popupimagebutton;
    private EditText editContentTxtView, editOption1TxtView, editOption2TxtView, editOption3TxtView, editOption4TxtView;
    private CheckBox chkOption1, chkOption2, chkOption3, chkOption4;
    private int role;
    private int mode;
    private String sessionId;
    private String userId;
    private GPSTracker gpsTracker;
    private ImageButton CaptureImageButton;

    private ImageView imgPhotView;
    FileStorageHelper mStorageHelper;
    ProgressBar progressBar;

    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;
    private String imageFilePath = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        gpsTracker = new GPSTracker(getActivity());
        if (!gpsTracker.canGetLocation()) {
            gpsTracker.requestpermission();
        }
        src = (QuizItem) this.getArguments().getSerializable(ConstHelper.REF_QUIZ_ITEMS);
        title = src.getTitle();
        quizOptions = src.getQuizOptions();
        mode = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(ConstHelper.SharedPreferences_Access_Mode, AccessMode.EDIT);
        role = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(ConstHelper.SharedPreferences_User_Id, UserRole.toInt(UserRole.PARTICIPENT));
        userId = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(ConstHelper.SharedPreferences_User_Id, "0");

        return inflater.inflate(R.layout.fragment_quiz_item_detail, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupControls();
        setupViews();
    }

    private void setupViews() {
    }


    private void setupControls() {


        progressBar = (ProgressBar) this.getView().findViewById(R.id.pb_imgloadprogressBar);
        progressBar.setVisibility(View.INVISIBLE);
        imgPhotView = this.getView().findViewById(R.id.img_photo);
        if (src.getPhotoURL() != null) {
            imgPhotView.setAlpha(0.8f);
            try {
                AsyncLoadImageHelper loader = new AsyncLoadImageHelper(imgPhotView, getContext(), progressBar);
                loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, src.getPhotoURL());

            } catch (Exception ex) {
                Log.w("adapter", "error");
            }
        }
        //Question

        editContentTxtView = (EditText) this.getView().findViewById(R.id.et_content);
        editContentTxtView.setText(src.getContent());


        editOption1TxtView = (EditText) this.getView().findViewById(R.id.et_option_content1);


        editOption2TxtView = (EditText) this.getView().findViewById(R.id.et_option_content2);
        editOption3TxtView = (EditText) this.getView().findViewById(R.id.et_option_content3);
        editOption4TxtView = (EditText) this.getView().findViewById(R.id.et_option_content4);

        //checkboxs
        chkOption1 = (CheckBox) this.getView().findViewById(R.id.chk_option1);
        chkOption2 = (CheckBox) this.getView().findViewById(R.id.chk_option2);
        chkOption3 = (CheckBox) this.getView().findViewById(R.id.chk_option3);
        chkOption4 = (CheckBox) this.getView().findViewById(R.id.chk_option4);
        if (quizOptions != null) {
            if (quizOptions.size() > 0 && quizOptions.size() <= 4) {
                //set option values
                QuizOption quizOption1 = quizOptions.get(1);
                editOption1TxtView.setText(quizOption1.getContent());
                chkOption1.setChecked(quizOption1.isAnswer());
                QuizOption quizOption2 = quizOptions.get(2);
                editOption2TxtView.setText(quizOption2.getContent());
                chkOption2.setChecked(quizOption2.isAnswer());
                QuizOption quizOption3 = quizOptions.get(2);
                editOption3TxtView.setText(quizOption3.getContent());
                chkOption3.setChecked(quizOption3.isAnswer());
                QuizOption quizOption4 = quizOptions.get(2);
                editOption4TxtView.setText(quizOption4.getContent());
                chkOption4.setChecked(quizOption4.isAnswer());
            }
        }


        popupimagebutton = (ImageView) getView().findViewById(R.id.img_popupmenu);
        //save button
        Button btnSave = this.getView().findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Save clicked", Toast.LENGTH_LONG).show();
                QuizItemDetailFragment.this.src.setContent(editContentTxtView.getText().toString());

                ServiceCallback<Item> itemServiceCallback = new SaveServiceCallback();
                ServiceCallback<Boolean> booleanServiceCallback = new UpdateServiceCallback();
                if (src.getId() == null || src.getId().equals("")) {
                    title.createItem(src, itemServiceCallback);
                } else {
                    title.updateItem(src, booleanServiceCallback);
                }
            }
        });

        //File Storage setup
        mStorageHelper = new FileStorageHelper();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }

        //capture image
        CaptureImageButton = (ImageButton) this.getView().findViewById(R.id.imgbtn_CaptureImage);
        CaptureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCameraIntent();
            }
        });

    }

    public class SaveServiceCallback implements ServiceCallback<Item> {
        @Override
        public void onComplete(Item data) {
            QuizItem source = (QuizItem) data;
            QuizOption option1 = new QuizOption(source.getId());
            option1.setContent(editOption1TxtView.getText().toString());
            option1.setAnswer(chkOption1.isChecked());
            source.Add(option1);

            QuizOption option2 = new QuizOption(source.getId());
            option2.setContent(editOption2TxtView.getText().toString());
            option2.setAnswer(chkOption2.isChecked());
            source.Add(option2);
            // source.createQuizOption(option2,SaveOptionServiceCallback);

            QuizOption option3 = new QuizOption(source.getId());
            option3.setContent(editOption3TxtView.getText().toString());
            option3.setAnswer(chkOption3.isChecked());
            source.Add(option3);
            //source.createQuizOption(option3,SaveOptionServiceCallback);

            QuizOption option4 = new QuizOption(source.getId());
            option4.setContent(editOption4TxtView.getText().toString());
            option4.setAnswer(chkOption4.isChecked());
            source.Add(option4);

            title.updateItem(source,new UpdateServiceCallback());


            //Toast.makeText(getContext(),"Save succesfull "+source.getId(),Toast.LENGTH_LONG).show();
           // getFragmentManager().popBackStackImmediate("QuizFragment", 0);
        }

        @Override
        public void onError(int code, String message, String details) {
            Log.w("ERROR", code + "-" + message + "-" + details);

        }
    }


    public class UpdateServiceCallback implements ServiceCallback<Boolean> {
        @Override
        public void onComplete(Boolean data) {
            //LearningItem source= (LearningItem) data;
            //Toast.makeText(getContext(),"Save succesfull "+source.getId(),Toast.LENGTH_LONG).show();
            if (data) {
                getFragmentManager().popBackStackImmediate("QuizFragment", 0);
            } else {
                Toast.makeText(getContext(), "Udpdate unsuccessful Please try later", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onError(int code, String message, String details) {
            Log.w("ERROR", code + "-" + message + "-" + details);

        }
    }


    @Override
    public void onBackstack(Object object, UserActionCallback callback) {
        getFragmentManager().popBackStackImmediate("TitleFragment", 0);
        //super.onBackstack(object, callback);
    }

    @Override
    public void onBefore(@EventType.Event int event, final UserActionCallback callback) {
        new AlertDialog.Builder(getContext())
                .setTitle("Title")
                .setMessage("Your unsaved data will be lost.\n Are you sure you wanted to continue?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getFragmentManager().popBackStackImmediate("TitleFragment", 0);
                        // callback.onPass();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onReject();
                    }
                }).show();
    }


    private void OpenCameraIntent() {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {

            File photoFile, storageDir = null;
            try {
                storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = mStorageHelper.createImageFile(storageDir);
                imageFilePath = photoFile.getAbsolutePath();
                // PackageManager packageManager=this.getActivity().getPackageManager();
                Toast.makeText(getContext(), getContext().getPackageName(), Toast.LENGTH_LONG);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(pictureIntent, REQUEST_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Thanks for granting Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imgPhotView.setImageURI(Uri.parse(imageFilePath));
                Toast.makeText(getContext(), "Uploading..", Toast.LENGTH_SHORT).show();

                if (gpsTracker.canGetLocation()) {
                    Location location = gpsTracker.getLocation();
                    Coordinate coordinate = new Coordinate(location.getLatitude(), location.getLongitude());

                    src.setCoordinate(coordinate);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
            Uri file = Uri.fromFile(new File(imageFilePath));
            String fN = mStorageHelper.uploadFile(file, new ServiceCallback<String>() {

                @Override
                public void onComplete(String data) {
                    src.setPhotoURL(data);
                }

                @Override
                public void onError(int code, String message, String details) {
                    Log.e("", "");
                }
            });
            // src.setPhotoURL(fN);
            /*try {
                Uri URl=mStorageHelper.GetdownloadFileUrl(fN).getResult();
                src.setPhotoURL(URl.toString());
            }
            catch (Exception ex){

            }*/

            /* download test
            File sDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            try {
                String tmpImgPath= mStorageHelper.downloadFile(sDir,fN).getAbsolutePath();
                imageView.setImageURI(Uri.parse(tmpImgPath));
                Toast.makeText(this, "downloaded..", Toast.LENGTH_SHORT).show();

            }
            catch(IOException ie){}*/

        }
    }
}