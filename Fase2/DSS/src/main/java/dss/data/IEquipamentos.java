package dss.data;

import dss.business.equipamento.Componente;
import dss.business.equipamento.Equipamento;
import dss.exceptions.FichaDesteClienteJaExisteException;
import dss.exceptions.EquipamentoNaoExisteException;

import java.util.Collection;
import java.util.List;

public interface IEquipamentos extends IDAO<Equipamento,Integer> {

    public void adicionaEquipamento(Equipamento equipamento) throws FichaDesteClienteJaExisteException;

    public Collection<Equipamento> getEquipamentos();

    public Collection<Equipamento> getEquipamentosAbandonados();

    public Equipamento getEquipamento(Integer id) throws EquipamentoNaoExisteException;

    public Collection<Componente> getComponentes();

    public Componente getComponente(Integer componenteID) throws EquipamentoNaoExisteException;

    public List<Equipamento> atualizaEquipamentoAbandonado();

    public Equipamento getEquipamnetoByIdCliente(String id) ;

    }
