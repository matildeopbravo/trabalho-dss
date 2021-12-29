package dss;

import dss.clientes.Cliente;
import dss.clientes.ClientesDAO;
import dss.equipamentos.*;
import dss.estatisticas.EstatisticasFuncionario;
import dss.estatisticas.EstatisticasReparacoesTecnico;
import dss.exceptions.*;
import dss.reparacoes.*;
import dss.utilizador.*;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SGRModel implements Serializable {
    private UtilizadoresDAO utilizadoresDAO;
    private ReparacoesDAO reparacoesDAO;
    private EquipamentosDAO equipamentosDAO;
    private ClientesDAO clientesDAO;

    //############
    //#UTILIZADOR#
    //############
}