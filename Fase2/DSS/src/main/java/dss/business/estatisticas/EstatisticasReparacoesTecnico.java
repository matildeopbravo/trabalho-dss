package dss.business.estatisticas;

public class EstatisticasReparacoesTecnico {
    private int numReparacoesExpresso;
    private int numReparacoesProgramadas;
    private double duracaoMedia;
    private double mediaDesvioDuracao;

    public EstatisticasReparacoesTecnico(int numReparacoesExpresso, int numReparacoesProgramadas, double duracaoMedia, double mediaDesvioDuracao) {
        this.numReparacoesExpresso = numReparacoesExpresso;
        this.numReparacoesProgramadas = numReparacoesProgramadas;
        this.duracaoMedia = duracaoMedia;
        this.mediaDesvioDuracao = mediaDesvioDuracao;
    }
}
