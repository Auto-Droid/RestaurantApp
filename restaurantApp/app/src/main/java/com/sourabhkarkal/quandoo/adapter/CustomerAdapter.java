package com.sourabhkarkal.quandoo.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourabhkarkal.quandoo.R;
import com.sourabhkarkal.quandoo.fragment.CustomerTableFragment;
import com.sourabhkarkal.quandoo.realm.modal.RCustomerDTO;
import com.sourabhkarkal.quandoo.utils.Utils;

import io.realm.RealmResults;

/**
 * Kept the adapter seperate class so that it could be used to display customer name in future
 * adaper used in {@link com.sourabhkarkal.quandoo.fragment.CustomerListFragment}
 * Created by sourabhkarkal on 13/07/16.
 */
public  class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private RealmResults<RCustomerDTO> itemsData;
    FragmentActivity activity;

    public CustomerAdapter(FragmentActivity activity ,RealmResults<RCustomerDTO> itemsData) {
        this.itemsData = itemsData;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_item, parent ,false);

        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.tvCustomerName.setText(itemsData.get(position).getCustomerFirstName()+ " " +itemsData.get(position).getCustomerLastName());
        viewHolder.tvCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CustomerTableFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("custId",itemsData.get(position).getId());
                fragment.setArguments(bundle);
                Utils.replaceFragment(activity,fragment);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCustomerName;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvCustomerName = (TextView) itemLayoutView.findViewById(R.id.tvCustomerName);

        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }

}