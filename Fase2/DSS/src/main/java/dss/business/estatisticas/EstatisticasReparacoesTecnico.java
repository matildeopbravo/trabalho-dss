package dss.business.estatisticas;

public class EstatisticasReparacoesTecnico {
    private String idTecnico;
    private int numReparacoesExpresso;
    private int numReparacoesProgramadas;
    private double duracaoMedia;
    private double mediaDesvioDuracao;

    public EstatisticasReparacoesTecnico(String tecnico, int numReparacoesExpresso, int numReparacoesProgramadas, double duracaoMedia, double mediaDesvioDuracao) {
        this.idTecnico = tecnico;
        this.numReparacoesExpresso = numReparacoesExpresso;
        this.numReparacoesProgramadas = numReparacoesProgramadas;
        this.duracaoMedia = duracaoMedia;
        this.mediaDesvioDuracao = mediaDesvioDuracao;
    }

    public String getIdTecnico() {
        return idTecnico;
    }

    public int getNumReparacoesExpresso() {
        return numReparacoesExpresso;
    }

    public int getNumReparacoesProgramadas() {
        return numReparacoesProgramadas;
    }

    public double getDuracaoMedia() {
        return duracaoMedia;
    }

    public double getMediaDesvioDuracao() {
        return mediaDesvioDuracao;
    }
}
