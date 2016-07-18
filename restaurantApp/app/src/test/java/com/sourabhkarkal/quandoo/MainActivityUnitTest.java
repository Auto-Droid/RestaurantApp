package com.sourabhkarkal.quandoo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourabhkarkal.quandoo.activity.MainActivity;
import com.sourabhkarkal.quandoo.adapter.CustomerAdapter;
import com.sourabhkarkal.quandoo.fragment.CustomerListFragment;
import com.sourabhkarkal.quandoo.fragment.CustomerTableFragment;
import com.sourabhkarkal.quandoo.realm.modal.RCustomerDTO;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

import static org.mockito.Mockito.doNothing;

/**
 * @author sourabhkarkal
 */
public class MainActivityUnitTest extends ActivityInstrumentationTestCase2<MainActivity>{

    MainActivity mainActivity;
    CustomerListFragment customerListFragment;
    CustomerTableFragment customerTableFragment;
    CustomerAdapter customerAdapter;
    RecyclerView.Adapter adapter;
    FragmentManager fragmentManager;

    Realm realm;
    RealmResults<RCustomerDTO> data;

    public MainActivityUnitTest() {
        super(MainActivity.class);
    }

    public MainActivityUnitTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Before
    public void init(){
        mainActivity = new MainActivity();
        customerListFragment = new CustomerListFragment();
        customerTableFragment = new CustomerTableFragment();

        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                MainActivity.class);
        getActivity().startActivity(intent);

        add_fragment(customerListFragment);
        customerAdapter = new CustomerAdapter(getActivity(),data);
    }

    public void test_check_for_null_objects(){
        assertNotNull("MainActivity is Null",mainActivity);

    }

    public void test_fragment(){
        getInstrumentation().callActivityOnStart(getActivity());
        getInstrumentation().callActivityOnResume(getActivity());

        CustomerListFragment customerListFragment = (CustomerListFragment) fragmentManager.findFragmentById(R.id.mainfragment);

        assertNotNull(customerListFragment);

    }

    public void test_on_adapter(){
        assertNotNull("Adapter is null",customerAdapter);

    }

    public void test_table_fragment(){
        add_fragment(customerTableFragment);

        CustomerTableFragment customerTableFragment = (CustomerTableFragment) fragmentManager.findFragmentById(R.id.mainfragment);

        assertNotNull(customerTableFragment);

    }

    public void add_fragment(Fragment fragment){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.mainfragment, fragment);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }


}