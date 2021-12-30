package dss.data;

import dss.business.cliente.Cliente;
import dss.exceptions.ClienteJaExisteException;
import dss.exceptions.ClienteNaoExisteException;
import java.util.Collection;
public interface IClientes extends IDAO<Cliente,String> {

    public void adicionaCliente(Cliente cliente) throws ClienteJaExisteException;

    public Cliente getCliente(String idCliente) throws ClienteNaoExisteException;

    public Collection<Cliente> getClientes() ;

    public void removeCliente(String idCliente) throws ClienteNaoExisteException ;
}
