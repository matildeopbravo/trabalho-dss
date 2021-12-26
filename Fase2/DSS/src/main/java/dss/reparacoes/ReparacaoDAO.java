package dss.reparacoes;

import dss.utilizador.UtilizadorFacade;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ReparacaoDAO {
    public static ReparacaoFacade lerReparacoes(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ReparacaoFacade)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ReparacaoFacade();
    }

    public static void escreverReparacoes(String ficheiro, ReparacaoFacade reparacoes) {
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
