package dss.equipamentos;

import dss.exceptions.EquipamentoJaExisteException;
import dss.exceptions.EquipamentoNaoExisteException;
import dss.reparacoes.ReparacoesFacade;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EquipamentosFacade implements Serializable {
    private final Map<Integer, Equipamento> equipamentoById;
    private final Map<Integer, Equipamento> equipamentoAbandonado;
    private final Map<Integer, Componente> componenteById;

    public EquipamentosFacade() {
        this.equipamentoById = new HashMap<>();
        this.componenteById = new HashMap<>();
        this.equipamentoAbandonado = new HashMap<>();
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

    public Equipamento getEquipamento(String id) throws EquipamentoNaoExisteException {
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

    public void atualizaEquipamentoAbandonado(ReparacoesFacade reparacoesFacade) {
        Iterator<Map.Entry<Integer, Equipamento>> it = equipamentoById.entrySet().iterator();
        LocalDateTime today = LocalDateTime.now();

        while (it.hasNext()) {
            Equipamento equipamento = it.next().getValue();
            if (today.isAfter(equipamento.getDataEntrega().plusDays(90))) {
                reparacoesFacade.arquivaReparacoesDeEquipamento(equipamento.getIdEquipamento());
                it.remove();
                this.equipamentoAbandonado.put(equipamento.getIdEquipamento(), equipamento);
            }
        }
    }
}
