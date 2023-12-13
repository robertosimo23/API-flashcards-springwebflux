package main.java.set.OperacoesBasicas;

import java.util.Objects;

/**
 * @param nome atributos
 */
public record Convidado(String nome, int codigoConvite) {

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Convidado convidado)) return false;
    return codigoConvite() == convidado.codigoConvite();
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(codigoConvite());
  }

  @Override
  public String toString() {
    return "Convidado{" +
            "nome='" + nome + '\'' +
            ", codigoConvite=" + codigoConvite +
            '}';
  }
}
