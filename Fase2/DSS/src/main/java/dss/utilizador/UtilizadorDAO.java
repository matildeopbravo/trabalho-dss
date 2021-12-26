package dss.utilizador;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class UtilizadorDAO {
    public static UtilizadorFacade lerUtilizadores(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (UtilizadorFacade)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new UtilizadorFacade();
    }

    public static void escreverUtilizadores(String ficheiro, UtilizadorFacade utilizadores) {
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
