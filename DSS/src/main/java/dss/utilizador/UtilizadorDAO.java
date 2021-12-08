package dss.utilizador;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jdk.jshell.execution.Util;

import java.util.HashMap;
import java.util.List;

public class UtilizadorDAO {
    public HashMap<String, Utilizador> utilizadoresPorID;

    public UtilizadorDAO() {
        //hard coded database
        this.utilizadoresPorID = new HashMap<>();
        adicionaUtilizador(new Tecnico("Matilde", "0","matildeopbravo"));
        adicionaUtilizador(new Tecnico("Pedro", "1", "pta2002"));
        adicionaUtilizador(new Funcionario("Alex", "2", "SugaryLump"));
        adicionaUtilizador(new Gestor("Mariana", "3", "Mariii_01"));
    }

    /**
     * Adiciona um utilizador ao mapa de utilizadores deste objeto
     * @return Verdadeiro se utilizador foi adicionado, falso se já existia
     */
    public boolean adicionaUtilizador(Utilizador utilizador) {
        if (utilizadoresPorID.containsKey(utilizador.getId()))
            return false;
        this.utilizadoresPorID.put(utilizador.getId(), utilizador);
        return true;
    }

    /**
     * Remove um utilizador do mapa de utilizadores deste objeto
     * @return Utilizador que foi removido, ou nulo se não existia
     */
    public Utilizador removeUtilizador(String idUtilizador) {
        return this.utilizadoresPorID.remove(idUtilizador);
    }

    /**
     * Valida as credenciais fornecidas, devolvendo o utilizador ao qual
     * correspondem.
     * @return Utilizador cujas credenciais foram fornecidas, null se forem
     * inválidas.
     */
    public Utilizador validaCredenciais (String id, String password) {
        Utilizador utilizador = this.utilizadoresPorID.get(id);
        String passwordHash = encriptaPassword(password);
        if (utilizador != null) {
            if (utilizador.getPasswordHash().equals(passwordHash))
                return utilizador;
        }
        return null;
    }

    public static String encriptaPassword(String password) {
        return BCrypt.withDefaults().hashToString(4, password.toCharArray());
    }

    public List<Utilizador> getUtilizadores() {
        return this.utilizadoresPorID.values().stream().toList();
    }

    public Utilizador getUtilizador(String idUtilizador) {
        return this.utilizadoresPorID.get(idUtilizador);
    }
}
