package dss.clientes;

import dss.exceptions.UtilizadorJaExisteException;
import dss.exceptions.UtilizadorNaoExisteException;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

public class ClientesDAO implements Serializable  {
    private final HashMap<String, Cliente> clientesById;

    public ClientesDAO() {
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

    public static void escreverEquipamento(String ficheiro, ClientesDAO clientes) {
        try {
            FileOutputStream fos = new FileOutputStream(ficheiro);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(clientes);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
