package br.com.aula;

import java.util.ArrayList;
import java.util.List;

import br.com.aula.exception.*;

public class Banco {

	private List<Conta> contas = new ArrayList<Conta>();

	public Banco() {
	}

	public Banco(List<Conta> contas) {
		this.contas = contas;
	}


	public void cadastrarConta(Conta conta) throws ContaJaExistenteException, ContaComNumeroInvalido, ContaComNomeJaExistenteException {

		for (Conta c : contas) {
			boolean isNomeClienteIgual = c.getCliente().getNome().equals(conta.getCliente().getNome());
			boolean isNumeroContaIgual = c.getNumeroConta() == conta.getNumeroConta();
			boolean isNumeroInvalido = c.getNumeroConta() != 0;

			if (isNumeroContaIgual) {
				throw new ContaJaExistenteException();
			}

			if (isNumeroInvalido) {
				throw new ContaComNumeroInvalido();
			}

			if (isNomeClienteIgual) {
				throw new ContaComNomeJaExistenteException();
			}
		}
		
		this.contas.add(conta);

	}

	public void efetuarTransferencia(int numeroContaOrigem, int numeroContaDestino, int valor)
			throws ContaNaoExistenteException, ContaSemSaldoException, TransferirValorNegativo {

		Conta contaOrigem = this.obterContaPorNumero(numeroContaOrigem);
		Conta contaDestino = this.obterContaPorNumero(numeroContaDestino);

		boolean isContaOrigemExistente = contaOrigem != null;
		boolean isContaDestinoExistente = contaDestino != null;

		if (isContaOrigemExistente && isContaDestinoExistente) {

			boolean isContaOrigemPoupança = contaOrigem.getTipoConta().equals(TipoConta.POUPANCA);
			boolean isSaldoContaOrigemNegativo = contaOrigem.getSaldo() - valor < 0;

			if (isContaOrigemPoupança && isSaldoContaOrigemNegativo) {
				throw new ContaSemSaldoException();
			}

			if (valor <= 0){
				throw new TransferirValorNegativo();
			}

			contaOrigem.debitar(valor);
			contaDestino.creditar(valor);

		} else {
			throw new ContaNaoExistenteException();
		}
	}

	public Conta obterContaPorNumero(int numeroConta) {

		for (Conta c : contas) {
			if (c.getNumeroConta() == numeroConta) {
				return c;
			}
		}

		return null;
	}

	public List<Conta> obterContas() {
		return this.contas;
	}

	public boolean validarNumConta(int numeroConta) throws ContaComNumeroInvalido{

		if (numeroConta == 0){
			throw new ContaComNumeroInvalido();
		}
		return true;

	}
}
