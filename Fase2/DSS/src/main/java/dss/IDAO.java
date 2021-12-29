package dss;

import dss.exceptions.*;

import java.util.Collection;

public interface IDAO <E,T> {
    E get(T id) throws  NaoExisteException;
    void add(E item) throws JaExisteException;
    void remove(T id) throws NaoExisteException;
    public <C> Collection<C> getByClass(Class<C> classe);
    Collection<E> getAll();
}
