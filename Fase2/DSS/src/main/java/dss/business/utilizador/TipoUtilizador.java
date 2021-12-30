package dss.business.utilizador;

public enum TipoUtilizador {
    Funcionario(Funcionario.class),
    Gestor(Gestor.class),
    Tecnico(Tecnico.class);

    Class<? extends Utilizador> clazz;

    TipoUtilizador(Class<? extends Utilizador> clazz) {
        this.clazz = clazz;
    }

    public String getSimplename(){
        return this.clazz.getSimpleName();
    }
}
