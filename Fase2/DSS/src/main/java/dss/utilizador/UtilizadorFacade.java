package dss.utilizador;

import
        dss.BCrypt.BCrypt;
import dss.exceptions.CredenciasInvalidasException;
import dss.exceptions.UtilizadorJaExisteException;
import dss.exceptions.UtilizadorNaoExisteException;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UtilizadorFacade {
    public HashMap<String, Utilizador> utilizadoresPorID;

    /*public UtilizadorFacade() {
        //hard coded database
        this.utilizadoresPorID = new HashMap<>();
        adicionaUtilizador(new Tecnico("Matilde", "0","matildeopbravo"));
        adicionaUtilizador(new Tecnico("Pedro", "1", "pta2002"));
        adicionaUtilizador(new Funcionario("Alex", "2", "SugaryLump"));
        adicionaUtilizador(new Gestor("Mariana", "3", "Mariii_01"));
    }*/

    public UtilizadorFacade() {
        this.utilizadoresPorID = new HashMap<>();
    }

    /**
     * Adiciona um utilizador ao mapa de utilizadores deste objeto
     */
    public void adicionaUtilizador(Utilizador utilizador) throws UtilizadorJaExisteException {
        if (utilizadoresPorID.containsKey(utilizador.getId()))
            throw new UtilizadorJaExisteException();
        else
            this.utilizadoresPorID.put(utilizador.getId(), utilizador);
    }

    /**
     * Remove um utilizador do mapa de utilizadores deste objeto
     * @return Utilizador que foi removido, ou nulo se não existia
     */
    public void removeUtilizador(String idUtilizador) throws UtilizadorNaoExisteException {
        if (utilizadoresPorID.remove(idUtilizador) == null)
            throw new UtilizadorNaoExisteException();
    }

    /**
     * Valida as credenciais fornecidas, devolvendo o utilizador ao qual
     * correspondem.
     * @return Utilizador cujas credenciais foram fornecidas, null se forem
     * inválidas.
     */
    public Utilizador validaCredenciais (String id, String password) throws CredenciasInvalidasException {
        Utilizador utilizador = this.utilizadoresPorID.get(id);
        if(utilizador != null && BCrypt.checkpw(password,utilizador.getPasswordHash())) {
            return utilizador;
        }
        else {
            throw new CredenciasInvalidasException();

        }
    }

    public static String encriptaPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Collection<Utilizador> getUtilizadores() {
        return this.utilizadoresPorID.values().stream().toList();
    }

    public Utilizador getUtilizador(String idUtilizador) throws UtilizadorNaoExisteException{
        Utilizador utilizador = utilizadoresPorID.get(idUtilizador);
        if (utilizador != null)
            return utilizador;
        throw new UtilizadorNaoExisteException();
    }

    public Collection<Tecnico> getTecnicos() {
        return utilizadoresPorID.values().stream()
                .filter(u -> u instanceof  Tecnico)
                .map(t -> (Tecnico) t)
                .collect(Collectors.toList());
    }

    public Collection<Funcionario> getFuncionarios() {
        return utilizadoresPorID.values().stream()
                .filter(u -> u instanceof  Tecnico)
                .map(f -> (Funcionario) f)
                .collect(Collectors.toList());
    }
}
