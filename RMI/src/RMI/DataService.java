/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ngtukien
 */
public interface DataService extends Remote {                               
    public Object requestData(String studentCode, String qCode) throws RemoteException;              
    public void submitData(String studentCode, String qCode, Object data) throws RemoteException;    
}