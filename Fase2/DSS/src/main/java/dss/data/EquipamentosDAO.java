package dss.data;

import dss.equipamentos.Componente;
import dss.equipamentos.Equipamento;
import dss.equipamentos.IEquipamentos;
import dss.exceptions.EquipamentoJaExisteException;
import dss.exceptions.EquipamentoNaoExisteException;
import dss.exceptions.JaExisteException;
import dss.exceptions.NaoExisteException;
import dss.reparacoes.IReparacoes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;

public class EquipamentosDAO implements IEquipamentos, Serializable{
    private final Map<Integer, Equipamento> equipamentoById;
    private final Map<Integer, Equipamento> equipamentoAbandonado;
    private final Map<Integer, Componente> componenteById;

    public EquipamentosDAO() {
        this.equipamentoById = new HashMap<>();
        this.componenteById = new HashMap<>();
        this.equipamentoAbandonado = new HashMap<>();
    }

    public static EquipamentosDAO lerEquipamento(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (EquipamentosDAO)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new EquipamentosDAO();
    }

    public static void escreverEquipamento(String ficheiro, EquipamentosDAO equipamento) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(equipamento);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException {
        if (!equipamentoById.containsKey(equipamento.getIdEquipamento())
            && !equipamentoAbandonado.containsKey(equipamento.getIdEquipamento())) {
            equipamentoById.put(equipamento.getIdEquipamento(), equipamento);
        }
        throw new EquipamentoJaExisteException();
    }

    public Collection<Equipamento> getEquipamentos() {
        return equipamentoById.values();
    }

    public Collection<Equipamento> getEquipamentosAbandonados() {
        return equipamentoAbandonado.values();
    }

    public Equipamento getEquipamento(Integer id) throws EquipamentoNaoExisteException {
        Equipamento equipamento = equipamentoById.get(id);
        if (equipamento!=null)
            return equipamento;
        throw new EquipamentoNaoExisteException();
    }

    public Collection<Componente> getComponentes() {
        return componenteById.values();
    }

    public Componente getComponente(Integer componenteID) throws EquipamentoNaoExisteException {
        Componente componente = componenteById.get(componenteID);
        if (componente != null)
            return componente;
        throw new EquipamentoNaoExisteException();
    }

    @Override
    public void atualizaEquipamentoAbandonado(IReparacoes reparacoes) {
        Iterator<Map.Entry<Integer, Equipamento>> it = equipamentoById.entrySet().iterator();
        LocalDateTime today = LocalDateTime.now();

        while (it.hasNext()) {
            Equipamento equipamento = it.next().getValue();
            if (today.isAfter(equipamento.getDataEntrega().plusDays(90))) {
                reparacoes.arquivaReparacoesDeEquipamento(equipamento.getIdEquipamento());
                it.remove();
                this.equipamentoAbandonado.put(equipamento.getIdEquipamento(), equipamento);
            }
        }
    }


    @Override
    public Equipamento get(Integer id) throws NaoExisteException {
        Equipamento equipamento;
        equipamento = equipamentoById.get(id);
        if( equipamento == null)
            equipamento = this.equipamentoAbandonado.get(id);
        if (equipamento == null)
            throw new NaoExisteException();
        return equipamento;
    }

    @Override
    public void add(Equipamento item) throws JaExisteException {
        adicionaEquipamento(item);
    }

    @Override
    public void remove(Integer id) throws NaoExisteException {
        Equipamento equipamento = equipamentoById.remove(id);
        if(equipamento == null)
            equipamento = equipamentoAbandonado.remove(id);

        if(equipamento == null)
            throw new NaoExisteException();
    }

    @Override
    public <C> Collection<C> getByClass(Class<C> classe) {
        return getAll().stream().filter(classe::isInstance).map(classe::cast).collect(Collectors.toList());
    }

    @Override
    public Collection<Equipamento> getAll() {
        ArrayList<Equipamento> r = new ArrayList<> ();
        r.addAll(equipamentoById.values());
        r.addAll(equipamentoAbandonado.values());
        return r;
    }
}
