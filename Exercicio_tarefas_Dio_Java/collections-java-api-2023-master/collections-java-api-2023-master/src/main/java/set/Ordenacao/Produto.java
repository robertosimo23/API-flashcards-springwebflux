package main.java.set.Ordenacao;

import java.util.Comparator;
import java.util.Objects;

/**
 * @param codigo atributos
 */
public record Produto(long codigo, String nome, double preco, int quantidade) implements Comparable<Produto> {

  @Override
  public int compareTo(Produto p) {
    return nome.compareToIgnoreCase(p.nome());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Produto produto)) return false;
    return codigo() == produto.codigo();
  }

  @Override
  public int hashCode() {
    return Objects.hash(codigo());
  }

  @Override
  public String toString() {
    return "Produto{" +
            "codigo=" + codigo +
            ", nome='" + nome + '\'' +
            ", preco=" + preco +
            ", quantidade=" + quantidade +
            '}';
  }
}

class ComparatorPorPreco implements Comparator<Produto> {
  @Override
  public int compare(Produto p1, Produto p2)
  {
    return Double.compare(p1.preco(), p2.preco());
  }
}








