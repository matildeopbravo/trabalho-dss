package dss;

import java.time.Duration;

public interface Intervencao {
    String getDescricao();
    Duration getDuracaoReal();
    Duration getDuracaoPrevista();
    float getCustoTotalPrevisto();
    float getCustoTotalReal();
}
