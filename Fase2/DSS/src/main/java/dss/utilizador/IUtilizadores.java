package dss.utilizador;

import dss.data.IDAO;
import dss.exceptions.CredenciasInvalidasException;
import dss.exceptions.UtilizadorJaExisteException;
import dss.exceptions.UtilizadorNaoExisteException;
import java.util.Collection;

public interface IUtilizadores  extends IDAO<Utilizador,String>{

    public void adicionaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException;

    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException;

    public Utilizador validaCredenciais (String id, String password) throws CredenciasInvalidasException;

    public Utilizador getUtilizador(String idUtilizador) throws UtilizadorNaoExisteException;

    public Collection<Utilizador> getUtilizadores();

    public Collection<Tecnico> getTecnicos();

    public Collection<Funcionario> getFuncionarios();
}
