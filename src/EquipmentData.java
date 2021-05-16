
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nguyen Hai Dang
 */
public class EquipmentData implements Serializable
{
    String equipmentID;
    String status;
    String detailID;

    public EquipmentData(String equipmentID, String status, String detailID) {
        this.equipmentID = equipmentID;
        this.status = status;
        this.detailID = detailID;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public String getStatus() {
        return status;
    }

    public String getDetailID() {
        return detailID;
    }

    public void setEquipmentID(String equipmentID) {
        this.equipmentID = equipmentID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }
}
