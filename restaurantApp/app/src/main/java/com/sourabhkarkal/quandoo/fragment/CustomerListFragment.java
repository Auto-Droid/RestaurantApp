package com.sourabhkarkal.quandoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sourabhkarkal.quandoo.ApplicationController;
import com.sourabhkarkal.quandoo.R;
import com.sourabhkarkal.quandoo.adapter.CustomerAdapter;
import com.sourabhkarkal.quandoo.realm.modal.RCustomerDTO;
import com.sourabhkarkal.quandoo.service.RestTemplateExecutor;
import com.sourabhkarkal.quandoo.service.TagRequest;
import com.sourabhkarkal.quandoo.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.internal.Util;

/**
 * This fragment shows the list of customer and if the DB has data available it will show from DB
 * the object class for customer DB is {@link RCustomerDTO} , selection of customer will call {@link CustomerTableFragment}
 *
 * Created by sourabhkarkal on 13/07/16.
 */
public class CustomerListFragment extends BaseFragment {

    View rootView;
    RecyclerView rvCustomerList;
    public CustomerAdapter customerAdapter;
    RealmResults<RCustomerDTO> customerDTOs;
    EditText edtSearchCustomer;
    public final int TASK_CUSTOMER = 101;
    public Realm realm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.customer_list_fragment, container, false);
        rvCustomerList = (RecyclerView) rootView.findViewById(R.id.rvCustomerList);
        edtSearchCustomer = (EditText) rootView.findViewById(R.id.edtSearchCustomer);

        //initalized realm
        realm = Realm.getInstance(ApplicationController.getInstance().getRealmConfig());

        customerDTOs = realm.allObjectsSorted(RCustomerDTO.class, "customerFirstName",Sort.ASCENDING);
        rvCustomerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(customerDTOs!=null){
            customerAdapter = new CustomerAdapter(getActivity() , customerDTOs);
            rvCustomerList.setAdapter(customerAdapter);
        }

        if(Utils.isNetworkAvailable(getActivity())) {
            TagRequest tagRequest = new TagRequest(TASK_CUSTOMER, customerUrl, getActivity(), this);
            new RestTemplateExecutor().callServerApi(tagRequest);
        }else{
            Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

        /**
         * search for customer is done here
         */
        edtSearchCustomer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable query) {
            }

            @Override
            public void beforeTextChanged(CharSequence query, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start,
                                      int before, int count) {
                    if (query.length() != 0 && customerDTOs!=null) {
                        String strSearchValue = query.toString();
                        customerDTOs = customerDTOs.where().contains("customerFirstName", strSearchValue, Case.INSENSITIVE).or()
                                .contains("customerLastName", strSearchValue, Case.INSENSITIVE)
                                .findAllSorted("customerFirstName", Sort.ASCENDING);
                    } else {
                        customerDTOs = realm.allObjectsSorted(RCustomerDTO.class, "customerFirstName",Sort.ASCENDING);

                    }
                if(customerDTOs!=null) {
                    customerAdapter = new CustomerAdapter(getActivity(), customerDTOs);
                    rvCustomerList.setAdapter(customerAdapter);
                }
                }

        });

        return rootView;

    }


    @Override
    public void onTaskComplete(int taskId, Object object, boolean isError) {
        super.onTaskComplete(taskId, object, isError);
        if(taskId==TASK_CUSTOMER){
            if(isError){

            }else{
                try {
                    // Uncomment this if you want to map objects directy into Pojo object and also uncomment the dependency for jackson
                    /*customerDTOs = new ObjectMapper().readValue(object.toString(), new TypeReference<List<CustomerDTO>>(){});
                    if(customerDTOs!=null && customerDTOs.size()>0){
                        customerAdapter = new CustomerAdapter(customerDTOs);
                        rvCustomerList.setAdapter(customerAdapter);
                    }*/
                    realm.beginTransaction();
                    realm.createOrUpdateAllFromJson(RCustomerDTO.class,object.toString());
                    realm.commitTransaction();

                    customerDTOs = realm.allObjectsSorted(RCustomerDTO.class, "customerFirstName",Sort.ASCENDING);
                    customerAdapter = new CustomerAdapter(getActivity(),customerDTOs);
                    rvCustomerList.setAdapter(customerAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customerDTOs = null;
    }
}
