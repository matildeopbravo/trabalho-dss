package dss.business.estatisticas;

public class EstatisticasFuncionario {
    String idFuncionario;
    int rececoes;
    int entregas;

    public EstatisticasFuncionario(String id, int rececoes, int entregas) {
        this.idFuncionario = id;
        this.rececoes = rececoes;
        this.entregas = entregas;
    }

    public String getIdFuncionario() {
        return idFuncionario;
    }

    public int getRececoes() {
        return rececoes;
    }

    public int getEntregas() {
        return entregas;
    }
}
