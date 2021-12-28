package dss.reparacoes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReparacoesDAO {
    public static ReparacoesFacade lerReparacoes(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ReparacoesFacade)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ReparacoesFacade();
    }

    public static void escreverReparacoes(String ficheiro, ReparacoesFacade reparacoes) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(reparacoes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
