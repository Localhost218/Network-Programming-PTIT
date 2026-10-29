package RMI;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ngtukien
 */
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObjectService extends Remote {
    Serializable requestObject(String studentCode, String qCode)
            throws RemoteException;

    void submitObject(String studentCode, String qCode, Serializable object)
            throws RemoteException;
}
