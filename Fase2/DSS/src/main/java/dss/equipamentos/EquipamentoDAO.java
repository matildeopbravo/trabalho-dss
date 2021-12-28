package dss.equipamentos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EquipamentoDAO {
    public static EquipamentosFacade lerEquipamento(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (EquipamentosFacade)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new EquipamentosFacade();
    }

    public static void escreverEquipamento(String ficheiro, EquipamentosFacade equipamento) {
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
