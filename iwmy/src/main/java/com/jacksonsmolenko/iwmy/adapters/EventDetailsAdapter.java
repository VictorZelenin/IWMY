package com.jacksonsmolenko.iwmy.adapters;

import android.view.View;

import com.jacksonsmolenko.iwmy.data_binders.DataBinder;
import com.jacksonsmolenko.iwmy.data_binders.EventAboutInfoBinder;
import com.jacksonsmolenko.iwmy.data_binders.MainEventBinder;
import com.jacksonsmolenko.iwmy.data_binders.OthersEventsBinder;
import com.jacksonsmolenko.iwmy.data_binders.PhotosBinder;
import com.jacksonsmolenko.iwmy.data_binders.UpperIconBinder;
import com.oleksiykovtun.iwmy.speeddating.data.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 15.07.16.
 */
public class EventDetailsAdapter extends DataBindAdapter {

    private List<DataBinder> dataBinderList = new ArrayList<>();

    // will replace null's to real objects
    public EventDetailsAdapter() {
        setupBinders(/* params to binders */);

    }

    private void setupBinders() {
        dataBinderList.add(new UpperIconBinder(this)); // 0
        dataBinderList.add(new MainEventBinder(this, null)); // 1
        dataBinderList.add(new EventAboutInfoBinder(this)); // 2
        dataBinderList.add(new PhotosBinder(this, null)); // 3
//        dataBinderList.add(new OthersEventsBinder(this, debugFillList())); // others

    }

    private List<Event> debugFillList() {
        List<Event> list = new ArrayList<>();
        list.add(null);

        return list;
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;

        for (int i = 0; i < dataBinderList.size(); i++) {
            itemCount += dataBinderList.get(i).getItemCount();
            System.out.println(dataBinderList.get(4).getItemCount());
        }
        System.out.println(itemCount);

        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public <T extends DataBinder> T getDataBinder(int viewType) {
        return (T) dataBinderList.get(viewType);
    }

    @Override
    public int getPosition(DataBinder binder, int binderPosition) {
        int viewType = dataBinderList.indexOf(binderPosition);

        if (viewType < 0) {
            throw new IllegalStateException("binder doesn't exists in adapter");
        }

        int position = binderPosition;
        for (int i = 0; i < viewType; i++) {
            position += dataBinderList.get(i).getItemCount();
        }

        return position;
    }

    @Override
    public int getBinderPosition(int position) {
        int binderItemCount;
        for (int i = 0; i < dataBinderList.size(); i++) {
            binderItemCount = dataBinderList.get(i).getItemCount();
            if (position - binderItemCount < 0) {
                break;
            }
            position -= binderItemCount;
        }
        return position;
    }
}
