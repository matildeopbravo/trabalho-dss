package dss.data;

import dss.business.equipamento.Componente;
import dss.business.equipamento.Equipamento;
import dss.business.reparacao.Reparacao;
import dss.exceptions.ReparacaoDesteClienteJaExisteException;
import dss.exceptions.EquipamentoNaoExisteException;
import dss.exceptions.JaExisteException;
import dss.exceptions.NaoExisteException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EquipamentosDAO implements IEquipamentos, Serializable{
    private final Map<Integer, Equipamento> equipamentoById;
    private final Map<Integer, Equipamento> equipamentoAbandonado;
    private final Map<Integer, Componente> componenteById;

    public EquipamentosDAO() {
        this.equipamentoById = new HashMap<>();
        this.componenteById = new HashMap<>();

        adicionaComponente(new Componente("Parafuso pequeno", 0.01f, List.of("Parafusos")));
        adicionaComponente(new Componente("Parafuso médio", 0.01f, List.of("Parafusos")));
        adicionaComponente(new Componente("Parafuso grande", 0.02f, List.of("Parafusos")));
        adicionaComponente(new Componente("Ecrã iPhone 11", 50.00f, List.of("Ecrã", "iPhone")));
        adicionaComponente(new Componente("Ecrã iPhone 12", 50.00f, List.of("Ecrã", "iPhone")));
        adicionaComponente(new Componente("Bateria iPhone 11", 25.00f, List.of("Bateria", "iPhone")));
        adicionaComponente(new Componente("Bateria iPhone 12", 25.00f, List.of("Bateria", "iPhone")));

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

    public void adicionaEquipamento(Equipamento equipamento) throws ReparacaoDesteClienteJaExisteException {
        System.out.println("Tentando adicioanar equipamento com id"+ equipamento.getIdEquipamento());
        if (!equipamentoById.containsKey(equipamento.getIdEquipamento())
            && !equipamentoAbandonado.containsKey(equipamento.getIdEquipamento())) {
            equipamentoById.put(equipamento.getIdEquipamento(), equipamento);
            return;
        }
        throw new ReparacaoDesteClienteJaExisteException();
    }

    public void adicionaComponente(Componente componente) {
        componenteById.put(componente.getId(), componente);
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

    public Equipamento getEquipamnetoByIdCliente(String id) {
        return equipamentoById.values().stream().filter(e -> e.getIdCliente().equals(id)).findAny().orElse(null);
    }

    @Override
    public void updateLastID() {
            int max = equipamentoById.keySet()
                    .stream()
                    .max(Comparator.naturalOrder())
                    .orElse(-1);
            Equipamento.updateLastID(max);
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
    public List<Equipamento> atualizaEquipamentoAbandonado() {
        List<Equipamento> l = new ArrayList<>();
        Iterator<Map.Entry<Integer, Equipamento>> it = equipamentoById.entrySet().iterator();
        LocalDateTime today = LocalDateTime.now();

        while (it.hasNext()) {
            Equipamento equipamento = it.next().getValue();
            if (today.isAfter(equipamento.getDataEntrega().plusDays(90))) {
                l.add(equipamento);
                //reparacoes.arquivaReparacoesDeEquipamento(equipamento.getIdEquipamento());
                it.remove();
                this.equipamentoAbandonado.put(equipamento.getIdEquipamento(), equipamento);
            }
        }
        return l;
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
