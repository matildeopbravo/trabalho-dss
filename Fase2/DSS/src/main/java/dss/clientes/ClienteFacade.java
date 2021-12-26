package dss.clientes;

import dss.exceptions.UtilizadorJaExisteException;
import dss.exceptions.UtilizadorNaoExisteException;

import java.util.Collection;
import java.util.HashMap;

public class ClienteFacade {
    private final HashMap<String, Cliente> clientesById;

    public ClienteFacade() {
        this.clientesById = new HashMap<>();
    }

    public void adicionaCliente(Cliente cliente) throws UtilizadorJaExisteException {
        if (clientesById.containsKey(cliente.getNIF()))
            throw new UtilizadorJaExisteException();
        else
            clientesById.put(cliente.getNIF(), cliente);
    }

    public Cliente getCliente(String idCliente) throws UtilizadorNaoExisteException {
        Cliente cliente = clientesById.get(idCliente);
        if (cliente != null)
            return cliente;
        throw new UtilizadorNaoExisteException();
    }

    public Collection<Cliente> getClientes() {
        return clientesById.values();
    }
}
