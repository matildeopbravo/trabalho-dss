package dss.clientes;

import dss.equipamentos.EquipamentoFacade;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClienteDAO {
    public static ClienteFacade lerClientes(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ClienteFacade) ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ClienteFacade();
    }

    public static void escreverEquipamento(String ficheiro, ClienteFacade clientes) {
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
