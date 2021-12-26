package dss.equipamentos;

import dss.reparacoes.ReparacaoFacade;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EquipamentoDAO {
    public static EquipamentoFacade lerEquipamento(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (EquipamentoFacade)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new EquipamentoFacade();
    }

    public static void escreverEquipamento(String ficheiro, EquipamentoFacade equipamento) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(equipamento);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
