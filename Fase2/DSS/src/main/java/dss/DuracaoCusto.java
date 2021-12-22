package dss;

import java.time.Duration;

public class DuracaoCusto {
  private Duration duracaoReal;
  private Duration duracaoPrevista;
  private float  custoReal;
  private float  custoPrevisto;

    public DuracaoCusto(Duration duracaoPrevista,  float custoPrevisto) {
        this.duracaoReal = null;
        this.custoReal = -1;
        this.duracaoPrevista = duracaoPrevista;
        this.custoPrevisto = custoPrevisto;
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

    public float getCustoReal() {
        return custoReal;
    }

    public void setCustoReal(float custoReal) {
        this.custoReal = custoReal;
    }

    public float getCustoPrevisto() {
        return custoPrevisto;
    }

    public void setCustoPrevisto(float custoPrevisto) {
        this.custoPrevisto = custoPrevisto;
    }

    public void aumentaCustoPrevisto(float custo) {
        this.custoPrevisto += custo;
    }

    public void aumentaCustoReal(float custo) {
        this.custoReal += this.custoReal == -1 ? custo + 1: custo;
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
}
