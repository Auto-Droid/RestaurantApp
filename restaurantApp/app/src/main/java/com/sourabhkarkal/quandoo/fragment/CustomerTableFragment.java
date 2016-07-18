package com.sourabhkarkal.quandoo.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sourabhkarkal.quandoo.ApplicationController;
import com.sourabhkarkal.quandoo.R;
import com.sourabhkarkal.quandoo.realm.modal.RCustomerDTO;
import com.sourabhkarkal.quandoo.realm.modal.RReservationDTO;
import com.sourabhkarkal.quandoo.service.RestTemplateExecutor;
import com.sourabhkarkal.quandoo.service.TagRequest;
import com.sourabhkarkal.quandoo.utils.Utils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * This fragment will work as a table selection screen where waiter can select multiple table for
 * customer and confirm the table for them this in done in {@link #onClick(View)} for btnConfirm
 *
 * Created by sourabhkarkal on 14/07/16.
 */
public class CustomerTableFragment extends BaseFragment {

    View rootView;
    RecyclerView rvCustomerTableList;
    int spanCount = 3; // 3 columns
    int spacing = 10;
    boolean includeEdge = true;
    TableAdapter tableAdapter;
    int cellHeight;
    ArrayList<Integer> lastSelectedPosition;
    Realm realm;
    ArrayList<RReservationDTO> rReservationDTOs;
    Button btnConfirm;
    RCustomerDTO selectedCustomer;
    TextView tvTableTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.customer_table_list, container, false);
        realm = Realm.getInstance(ApplicationController.getInstance().getRealmConfig());
        rvCustomerTableList = (RecyclerView) rootView.findViewById(R.id.rvCustomerTableList);
        btnConfirm = (Button) rootView.findViewById(R.id.btnConfirm);
        tvTableTitle = (TextView) rootView.findViewById(R.id.tvTableTitle);

        btnConfirm.setOnClickListener(this);
        rReservationDTOs = new ArrayList<>();
        lastSelectedPosition = new ArrayList<>();

        int custId = getArguments().getInt("custId");
        selectedCustomer = realm.where(RCustomerDTO.class).equalTo("id",custId).findFirst();


        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        float totalWidth = displayMetrics.widthPixels / displayMetrics.density;
        cellHeight = (int) (totalWidth/spanCount) + 30;

        rvCustomerTableList.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        rvCustomerTableList.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rvCustomerTableList.setHasFixedSize(true);

        RealmResults<RReservationDTO> realmResults = realm.allObjectsSorted(RReservationDTO.class,"tableNo",Sort.ASCENDING);
        for(RReservationDTO rReservationDTO:realmResults){
            rReservationDTOs.add(rReservationDTO);
        }
        if(rReservationDTOs!=null){
            tableAdapter = new TableAdapter(rReservationDTOs);
            rvCustomerTableList.setAdapter(tableAdapter);
        }

        /*If the array is empty then it will call the service but if it is not empty it will
         not call and the ReservationTableDB will get update from {@link ReservationUpdateService}*/
        if(rReservationDTOs.isEmpty()) {
            if(Utils.isNetworkAvailable(getActivity())) {
                TagRequest tagRequest = new TagRequest(102, tableUrl, getActivity(), this);
                new RestTemplateExecutor().callServerApi(tagRequest);
            }else{
                Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }

        setTitle();

        return rootView;
    }


    /**
     * Product list Recycler View Adapter
     */
    class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
        private ArrayList<RReservationDTO> itemsData;

        public TableAdapter(ArrayList<RReservationDTO> itemsData) {
            this.itemsData = itemsData;
        }

        @Override
        public TableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.table_list_item, parent ,false);


            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,cellHeight);
            viewHolder.llTableLayout.setLayoutParams(parms);

            int tableNo = position+1;
            viewHolder.tvTableNo.setText(""+tableNo);
            if(itemsData.get(position).isReserved()){
                if(lastSelectedPosition.contains(position)){//set selected position
                    changeBGColor(viewHolder.llTableLayout, R.color.light_green);
                    setTableImage(viewHolder.ivTableImage, R.drawable.table_white);
                }else {
                    //setting empty position
                    changeBGColor(viewHolder.llTableLayout, R.color.white);
                    setTableImage(viewHolder.ivTableImage, R.drawable.table_grey);
                }
            }else{
                //setting reserved position
                changeBGColor(viewHolder.llTableLayout,R.color.darker_gray);
                setTableImage(viewHolder.ivTableImage,R.drawable.table_white);
            }

            viewHolder.llTableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(lastSelectedPosition.contains(position)){
                        if(itemsData.get(position).isReserved()) {
                            changeBGColor(viewHolder.llTableLayout, R.color.white);
                            setTableImage(viewHolder.ivTableImage,R.drawable.table_grey);

                            lastSelectedPosition.remove(new Integer(position));
                        }
                    }else {
                        if (itemsData.get(position).isReserved()) {
                            changeBGColor(viewHolder.llTableLayout, R.color.light_green);
                            setTableImage(viewHolder.ivTableImage, R.drawable.table_white);

                            lastSelectedPosition.add(position);


                        }
                    }
                    setTitle();
                }
            });


        }

        @Override
        public int getItemCount() {
            return itemsData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            TextView tvTableNo;
            FrameLayout llTableLayout;
            ImageView ivTableImage;
            public ViewHolder(View itemLayoutView) {
                super(itemLayoutView);
                tvTableNo = (TextView) itemLayoutView.findViewById(R.id.tvTableNo);
                llTableLayout = (FrameLayout) itemLayoutView.findViewById(R.id.llTableLayout);
                ivTableImage = (ImageView) itemLayoutView.findViewById(R.id.ivTableImage);
            }
        }
    }

    public void setTitle(){
        tvTableTitle.setText("Customers Name : "+selectedCustomer.getCustomerFirstName()+" "
                +selectedCustomer.getCustomerLastName()+"\n"+"Tables Selected : "+lastSelectedPosition.size());
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btnConfirm:
                if(lastSelectedPosition.size()==0) {
                    Toast.makeText(getActivity(),"Please select a table for reservation",Toast.LENGTH_SHORT).show();
                    return;
                }

                //reserving the table
                for(int table : lastSelectedPosition) {
                    realm.beginTransaction();
                    rReservationDTOs.get(table).setReserved(false);
                    realm.copyToRealmOrUpdate(rReservationDTOs.get(table));
                    realm.commitTransaction();
                }

                showDialog(getActivity(),"","Reservation for "+
                        selectedCustomer.getCustomerFirstName()+" "+selectedCustomer.getCustomerLastName()+
                        " is confirmed \nTables Selected : "+lastSelectedPosition.size()
                       , "OK",null,new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int option) {
                                        dialog.dismiss();
                                        getFragmentManager().popBackStack();
                            }
                        },null);
                break;
        }
    }

    public void changeBGColor(View view, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackgroundColor(getResources().getColor(color,getActivity().getTheme()));
        }else{
            view.setBackgroundColor(getResources().getColor(color));
        }
    }

    public void setTableImage(ImageView imageView,int resId){
        imageView.setImageResource(resId);

    }


    @Override
    public void onTaskComplete(int taskId, Object object, boolean isError) {
        super.onTaskComplete(taskId, object, isError);
        if(taskId==102){
            if(isError){

            }else{
                try {
                    String[] strings = object.toString().replace("[","").replace("]","").split(",");

                    rReservationDTOs.clear();

                    for (int i=0;i<strings.length;i++){
                        RReservationDTO rReservationDTO =  new RReservationDTO();
                        rReservationDTO.setTableNo(i+1);
                        rReservationDTO.setReserved(Boolean.parseBoolean(strings[i]));
                        rReservationDTOs.add(rReservationDTO);
                    }

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(rReservationDTOs);
                    realm.commitTransaction();


                    if(rReservationDTOs!=null){
                        tableAdapter = new TableAdapter(rReservationDTOs);
                        rvCustomerTableList.setAdapter(tableAdapter);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


        public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

}
