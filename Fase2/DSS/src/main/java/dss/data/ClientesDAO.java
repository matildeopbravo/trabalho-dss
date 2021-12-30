package dss.data;

import dss.clientes.Cliente;
import dss.clientes.IClientes;
import dss.exceptions.ClienteJaExisteException;
import dss.exceptions.ClienteNaoExisteException;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ClientesDAO implements IClientes, Serializable  {
    private final HashMap<String, Cliente> clientesById;

    public ClientesDAO() {
        this.clientesById = new HashMap<>();
    }

    public void adicionaCliente(Cliente cliente) throws ClienteJaExisteException {
        if (clientesById.containsKey(cliente.getNIF()))
            throw new ClienteJaExisteException();
        else
            clientesById.put(cliente.getNIF(), cliente);
    }

    public Cliente getCliente(String idCliente) throws ClienteNaoExisteException{
        Cliente cliente = clientesById.get(idCliente);
        if (cliente != null)
            return cliente;
        throw new ClienteNaoExisteException();
    }

    public Collection<Cliente> getClientes() {
        return clientesById.values();
    }
    public static ClientesDAO lerClientes(String ficheiro) {
        try {
            FileInputStream fis = new FileInputStream(ficheiro);
            ObjectInputStream ois = new ObjectInputStream(fis);

            return (ClientesDAO) ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ClientesDAO();
    }

    public static void escreverClientes(String ficheiro, ClientesDAO clientes) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(clientes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCliente(String idCliente) throws ClienteNaoExisteException {
            if (clientesById.remove(idCliente) == null)
                throw new ClienteNaoExisteException();
        }

    @Override
    public Cliente get(String id) throws ClienteNaoExisteException {
        return getCliente(id);
    }

    @Override
    public void add(Cliente item) throws ClienteJaExisteException {
        adicionaCliente(item);
    }

    @Override
    public void remove(String id) throws ClienteNaoExisteException {
        removeCliente(id);
    }

    @Override
    public <C> Collection<C> getByClass(Class<C> classe) {
        return clientesById.values().stream().filter(classe::isInstance).map(classe::cast).collect(Collectors.toList());
    }

    @Override
    public Collection<Cliente> getAll() {
        return clientesById.values();
    }
}
