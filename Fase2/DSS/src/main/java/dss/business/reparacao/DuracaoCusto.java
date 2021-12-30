package dss.business.reparacao;

import java.time.Duration;

public class DuracaoCusto {
  private Duration duracaoReal;
  private Duration duracaoPrevista;
  private float custoMaoDeObraReal;
  private float custoMaoDeObraPrevisto;

    public DuracaoCusto(Duration duracaoPrevista,  float custoPrevisto) {
        this.duracaoReal = null;
        this.custoMaoDeObraReal = -1;
        this.duracaoPrevista = duracaoPrevista;
        this.custoMaoDeObraPrevisto = custoPrevisto;
    }

    public Duration getDuracaoReal() {
        return duracaoReal;
    }

    public void setDuracaoReal(Duration duracaoReal) {
        this.duracaoReal = duracaoReal;
    }

    public Duration getDuracaoPrevista() {
        return duracaoPrevista;
    }

    public void setDuracaoPrevista(Duration duracaoPrevista) {
        this.duracaoPrevista = duracaoPrevista;
    }

    public float getCustoMaoDeObraReal() {
        return custoMaoDeObraReal;
    }

    public void setCustoMaoDeObraReal(float custoMaoDeObraReal) {
        this.custoMaoDeObraReal = custoMaoDeObraReal;
    }

    public float getCustoMaoDeObraPrevisto() {
        return custoMaoDeObraPrevisto;
    }

    public void setCustoMaoDeObraPrevisto(float custoMaoDeObraPrevisto) {
        this.custoMaoDeObraPrevisto = custoMaoDeObraPrevisto;
    }

    public void aumentaCustoPrevisto(float custo) {
        this.custoMaoDeObraPrevisto += custo;
    }

    public void aumentaCustoRealMaoDeObra(float custo) {
        this.custoMaoDeObraReal += this.custoMaoDeObraReal == -1 ? custo + 1: custo;
    }

    public void aumentaDuracaoPrevista(Duration duracaoPrevista) {
        this.duracaoPrevista = this.duracaoPrevista.plus(duracaoPrevista);
    }

    public void aumentaDuracaoReal(Duration duracaoReal) {
        if(this.duracaoReal == null) {
            this.duracaoReal = Duration.ZERO;
        }
        this.duracaoReal = this.duracaoReal.plus(duracaoReal);
    }

    @Override
    public String toString() {
        return "DuracaoCusto{" +
                "duracaoReal=" + duracaoReal +
                ", duracaoPrevista=" + duracaoPrevista +
                ", custoMaoDeObraReal=" + custoMaoDeObraReal +
                ", custoMaoDeObraPrevisto=" + custoMaoDeObraPrevisto +
                '}';
    }
}
