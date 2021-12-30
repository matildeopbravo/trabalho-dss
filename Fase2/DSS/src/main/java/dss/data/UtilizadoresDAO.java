package dss.data;

import dss.business.BCrypt.BCrypt;
import dss.exceptions.CredenciasInvalidasException;
import dss.exceptions.UtilizadorJaExisteException;
import dss.exceptions.UtilizadorNaoExisteException;
import dss.business.utilizador.Funcionario;
import dss.business.utilizador.Tecnico;
import dss.business.utilizador.Utilizador;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class UtilizadoresDAO implements IDAO<Utilizador,String>, IUtilizadores, Serializable  {
    private Map<String, Utilizador> utilizadoresPorID;

    public static UtilizadoresDAO lerUtilizadores(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (UtilizadoresDAO)ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new UtilizadoresDAO();
    }

    public static void escreverUtilizadores(String ficheiro, UtilizadoresDAO utilizadores) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(utilizadores);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public UtilizadoresDAO() {
        this.utilizadoresPorID = new HashMap<String,Utilizador>();
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
            throw new CredenciasInvalidasException("NIF ou password incorretos");
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
                .filter(u -> u instanceof  Funcionario)
                .map(f -> (Funcionario) f)
                .collect(Collectors.toList());
    }

    @Override
    public Utilizador get(String id) throws UtilizadorNaoExisteException {
        return getUtilizador(id);
    }

    @Override
    public void add(Utilizador item) throws UtilizadorJaExisteException {
        adicionaUtilizador(item);
    }

    @Override
    public void remove(String id) throws UtilizadorNaoExisteException {
        removeUtilizador(id);
    }

    public <C> Collection<C> getByClass(Class<C> classe) {
        return utilizadoresPorID.values().stream().filter(classe::isInstance).map(classe::cast).collect(Collectors.toList());
    }

    @Override
    public Collection<Utilizador> getAll() {
        return utilizadoresPorID.values();
    }
}
