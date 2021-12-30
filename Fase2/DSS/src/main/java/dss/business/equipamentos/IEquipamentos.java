package dss.business.equipamentos;

import dss.data.IDAO;
import dss.exceptions.EquipamentoJaExisteException;
import dss.exceptions.EquipamentoNaoExisteException;
import java.util.Collection;

import dss.business.reparacoes.IReparacoes;

public interface IEquipamentos extends IDAO<Equipamento,Integer> {

    public void adicionaEquipamento(Equipamento equipamento) throws EquipamentoJaExisteException;

    public Collection<Equipamento> getEquipamentos();

    public Collection<Equipamento> getEquipamentosAbandonados();

    public Equipamento getEquipamento(Integer id) throws EquipamentoNaoExisteException;

    public Collection<Componente> getComponentes();

    public Componente getComponente(Integer componenteID) throws EquipamentoNaoExisteException;

    //TODO Mudar
    public void atualizaEquipamentoAbandonado(IReparacoes reparacoes);
}
