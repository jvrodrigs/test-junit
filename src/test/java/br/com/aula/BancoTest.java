package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import br.com.aula.exception.*;
import org.junit.Assert;
import org.junit.Test;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, ContaComNumeroInvalido, ContaComNomeJaExistenteException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, ContaComNumeroInvalido, ContaComNomeJaExistenteException {

		// Cenario
		Cliente cliente = new Cliente("Felipe");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Nicole");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaComNumeroInvalido.class)
	public void naoDeveCadastrarContaComNumInvalido() throws ContaComNumeroInvalido, ContaJaExistenteException, ContaComNomeJaExistenteException {
		//Cen
		Cliente cliente = new Cliente("Akira");
		Conta contaAkira = new Conta(cliente, 0, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		if (banco.validarNumConta(contaAkira.getNumeroConta())){
			banco.cadastrarConta(contaAkira);
		}

		assertEquals(2, banco.obterContas().size());
	}


	@Test(expected = ContaComNomeJaExistenteException.class)
	public void naoDeveCadastrarContaComNome() throws ContaComNomeJaExistenteException, ContaComNumeroInvalido, ContaJaExistenteException {
		Cliente cliente = new Cliente("João");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("João");
		Conta conta2 = new Conta(cliente2, 222, 0, TipoConta.CORRENTE);

		Banco banco = new Banco();

		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

		assertEquals(1, banco.obterContas().size());
	}


	@Test
	public void deveEfetuarTransferenciaContasCorrentesPoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, TransferirValorNegativo {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Ana");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(0, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}

	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarExistenciaDaContaOrigemBanco() throws ContaSemSaldoException, ContaNaoExistenteException, TransferirValorNegativo {

		Cliente cliente = new Cliente("Gabriel");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Alexandre");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		banco.efetuarTransferencia(333, 456, 100);
	}

	@Test(expected = ContaSemSaldoException.class)
	public void naoPodeContaPoupancaComSaldoNegativo() throws ContaSemSaldoException, ContaNaoExistenteException, TransferirValorNegativo {
		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Ana");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		// Ação
		banco.efetuarTransferencia(123, 456, 100);

		// Verificação
		assertEquals(0, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}

	@Test
	public void verificarContaDeDestino() throws ContaSemSaldoException, ContaNaoExistenteException, TransferirValorNegativo {
		Cliente cliente = new Cliente("Gabriel");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Alexandre");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		banco.efetuarTransferencia(123, 456, 100);
	}

	@Test(expected = TransferirValorNegativo.class)
	public void naoPodeTransferirValorNegativo() throws ContaSemSaldoException, ContaNaoExistenteException, TransferirValorNegativo {
		Cliente cliente = new Cliente("Teixeira");
		Conta contaOrigem = new Conta(cliente, 123, 100, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Rodrigues");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

		banco.efetuarTransferencia(123, 456, -1);
	}

}
