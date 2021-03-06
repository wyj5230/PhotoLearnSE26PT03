package sg.edu.nus.se26pt03.photolearn.adapter;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sg.edu.nus.se26pt03.photolearn.BAL.Coordinate;
import sg.edu.nus.se26pt03.photolearn.BAL.Item;
import sg.edu.nus.se26pt03.photolearn.BAL.LearningTitle;
import sg.edu.nus.se26pt03.photolearn.BAL.Title;
import sg.edu.nus.se26pt03.photolearn.fragment.LearningItemFragment;

import sg.edu.nus.se26pt03.photolearn.BAL.LearningItem;

import sg.edu.nus.se26pt03.photolearn.service.LearningTitleService;
import sg.edu.nus.se26pt03.photolearn.service.ServiceCallback;

/**
 * Created by part time team 3 on 3/14/2018.
 */

public class ItemFragmentPageAdapter extends FragmentItemIdStatePagerAdapter {
    private Fragment mCurrentPrimaryItem = null;

    public List<Item> getLearningItemList() {
        return learningItemList;
    }

    public void setLearningItemList(List<Item> learningItemList) {
        this.learningItemList = learningItemList;
    }

    private List<Item> learningItemList = null;
    private LearningTitle title;



    public ItemFragmentPageAdapter(FragmentManager fm, LearningTitle title, List<Item> learningItemList) {
        super(fm);
        this.learningItemList = learningItemList != null ? learningItemList : new ArrayList<Item>();
        this.title = title;

    }


    @Override
    public Fragment getItem(int position) {
        return LearningItemFragment.create(position, (LearningItem) this.learningItemList.get(position));
    }

    public LearningItem getLearningItemByPosition(int position) {
        if (position <= getCount()) {
            return (LearningItem) this.learningItemList.get(position);
        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
        LearningItemFragment item = (LearningItemFragment) object;
        LearningItem itemValue = item.getLearningItem();
        for (int i = 0; i < learningItemList.size(); i++) {
            if (learningItemList.get(i).getId().equals(itemValue.getId())) {
                return i;
            }
        }
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return this.learningItemList.size();
    }
}
