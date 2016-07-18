package com.sourabhkarkal.quandoo.realm.modal;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sourabhkarkal on 15/07/16.
 */
public class RReservationDTO extends RealmObject{

    @PrimaryKey
    private int tableNo;
    // Not used yet but can be used to send response to server again with customer and table relation
    private RCustomerDTO rCustomerDTO;
    private boolean isReserved;


    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public RCustomerDTO getrCustomerDTO() {
        return rCustomerDTO;
    }

    public void setrCustomerDTO(RCustomerDTO rCustomerDTO) {
        this.rCustomerDTO = rCustomerDTO;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }
}
