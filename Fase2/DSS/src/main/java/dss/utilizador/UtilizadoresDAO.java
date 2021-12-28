package dss.utilizador;

import java.io.*;

public class UtilizadoresDAO {
    public static UtilizadoresFacade lerUtilizadores(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (UtilizadoresFacade)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new UtilizadoresFacade();
    }

    public static void escreverUtilizadores(String ficheiro, UtilizadoresFacade utilizadores) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(utilizadores);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
