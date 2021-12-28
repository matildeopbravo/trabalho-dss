package dss.clientes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClienteDAO {
    public static ClientesFacade lerClientes(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ClientesFacade) ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ClientesFacade();
    }

    public static void escreverEquipamento(String ficheiro, ClientesFacade clientes) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(clientes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
