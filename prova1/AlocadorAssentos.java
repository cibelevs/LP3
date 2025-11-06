import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class AlocadorAssentos implements Runnable {
    private final PriorityBlockingQueue<RequisicaoReserva> filaRequisicoes;
    private final MapaAssentos mapaAssentos;
    private final Semaphore limitadorTaxa;
    private volatile boolean executando = true;
    
    public AlocadorAssentos(PriorityBlockingQueue<RequisicaoReserva> filaRequisicoes, MapaAssentos mapaAssentos, Semaphore limitadorTaxa) {
        this.filaRequisicoes = filaRequisicoes;
        this.mapaAssentos = mapaAssentos;
        this.limitadorTaxa = limitadorTaxa;
    }
    
    @Override
    public void run() {
        System.out.println("[AlocadorAssentos] Iniciado");
        
        while (executando) {
            try {
                RequisicaoReserva requisicao = filaRequisicoes.poll(1, TimeUnit.SECONDS);
                if (requisicao == null){
                     continue;
                }
                processarRequisicao(requisicao);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[AlocadorAssentos] Encerrado");
    }
    
    private void processarRequisicao(RequisicaoReserva requisicao) {
        /** */
        throw new UnsupportedOperationException("Método processarRequisicao() não implementado - ATIVIDADE 2-II");
        /** *
        REQUISITOS:
            1. Tentar adquirir permissão do Semaphore (limitadorTaxa) com timeout de 10 segundos
            2. Se timeout, enviar resposta de falha "TIMEOUT" com mensagem "Sistema sobrecarregado"
            3. Verificar se o código do assento é "ANY" (case-insensitive)
            4. Se "ANY": chamar alocarQualquerAssento()
            5. Se específico: chamar alocarAssentoEspecifico()
            6. Enviar a resposta usando enviarResposta()
            7. SEMPRE liberar o Semaphore no bloco finally (mesmo em caso de exceção)
            8. Tratar InterruptedException:
                - Restaurar flag de interrupção: Thread.currentThread().interrupt()
                 Enviar resposta de falha "ERRO_SERVIDOR" com mensagem "Operação interrompida"
        DICAS:
            - Use try-catch-finally para garantir liberação do semáforo
            - tryAcquire() retorna boolean indicando sucesso/falha
            - O finally SEMPRE executa, mesmo com return ou exception
            - Use equalsIgnoreCase() para comparação case-insensitive
        /** */

        boolean permissaoAdquirida = false;

        try {
            // 1. Tentar adquirir permissão com timeout de 10 segundos
            permissaoAdquirida = limitadorTaxa.tryAcquire(10, TimeUnit.SECONDS);

            if (!permissaoAdquirida) {
                // 2. Se timeout, enviar resposta de falha "TIMEOUT"
                enviarResposta(requisicao, new RespostaReserva(permissaoAdquirida, requisicao.getCodigoAssento(), requisicao.getCodigoReserva(), "Erro no codigo", "ERRO"));
                return; // Sai do método; o 'finally' ainda será executado
            }

            // Permissão adquirida. Processar a lógica de negócio.
            RespostaReserva resposta;

            // 3. Verificar se o código do assento é "ANY"
            if (requisicao.getCodigoAssento().equalsIgnoreCase("ANY")) {
                // 4. Se "ANY": chamar alocarQualquerAssento()
                resposta = alocarQualquerAssento(requisicao);
            } else {
                // 5. Se específico: chamar alocarAssentoEspecifico()
                resposta = alocarAssentoEspecifico(requisicao, requisicao.getCodigoAssento());
            }

            // 6. Enviar a resposta (de sucesso ou falha da alocação)
            enviarResposta(requisicao, resposta);

        } catch (InterruptedException e) {
            // 8. Tratar InterruptedException
            // Restaurar flag de interrupção
            Thread.currentThread().interrupt();
            // Enviar resposta de falha "ERRO_SERVIDOR"
            enviarResposta(requisicao, RespostaReserva.falha("TIMEOUT", "Sistema sobrecarregado"));

        } catch (Exception e) {
            // Bloco de captura genérico (boa prática) para qualquer outra exceção
            // que possa ocorrer durante a lógica de alocação.
            System.err.println("Erro inesperado ao processar requisição " + requisicao.getId() + ": " + e.getMessage());
            enviarResposta(requisicao, RespostaReserva.falha("ERRO_SERVIDOR", e.getMessage()));
        
        } finally {
            // 7. SEMPRE liberar o Semaphore no bloco finally (SE foi adquirido)
            if (permissaoAdquirida) {
                limitadorTaxa.release();
            }
        }
    }
    
    private RespostaReserva alocarQualquerAssento(RequisicaoReserva requisicao) {
        /** */
        throw new UnsupportedOperationException("Método alocarQualquerAssento() não implementado - ATIVIDADE 2-I");
        /** *
        REQUISITOS:
            1. Iterar por todos os assentos disponíveis usando mapaAssentos.getTodosCodigosAssentos()
            2. Para cada assento, verificar se está ocupado (pular se estiver)
            3. Tentar adquirir o lock do assento com timeout de 5 segundos usando tryLock()
            4. Fazer verificação dupla (double-check) se o assento ainda está livre
            5. Usar compareAndSet() do AtomicBoolean para garantir atomicidade
            6. Se conseguir alocar:
                - Gerar código de reserva usando gerarCodigoReserva()
                - Ocupar o assento com assento.ocupar()
                - Decrementar contador de assentos livres
                - Registrar a reserva no log
                - Retornar RespostaReserva.sucesso()
            7. SEMPRE liberar o lock no bloco finally
            8. Se nenhum assento disponível, retornar RespostaReserva.falha("VOO_LOTADO", "Voo lotado")
            9. Tratar InterruptedException adequadamente
        DICAS:
            - Use try-finally para garantir que o lock seja liberado
            - A verificação dupla evita race conditions
            - compareAndSet garante que apenas uma thread aloque o assento
            - Lembre-se de decrementar o contador atômico de assentos livres
        /** */


        // 1. Iterar por todos os assentos
        for (String codigoAssento : mapaAssentos.getTodosCodigosAssentos()) {
            Assento assento = mapaAssentos.getAssento(codigoAssento);
            if (assento == null) continue;

            // 2. Pré-verificação (otimização): se já está ocupado, nem tenta pegar o lock
            if (assento.isOcupado()) {
                continue;
            }

            ReentrantLock lockAssento = assento.getLock();
            boolean lockAdquirido = false;

            try {
                // 3. Tentar adquirir o lock do assento com timeout de 5 segundos
                lockAdquirido = lockAssento.tryLock(5, TimeUnit.SECONDS);

                if (lockAdquirido) {
                    
                    // 4. Verificação dupla (double-check) + 5. compareAndSet (Atomicidade)
                    // Esta é a operação atômica que verifica se o assento está livre (false)
                    // e, se estiver, o ocupa (true), tudo em um passo.
                    if (assento.getOcupadoAtomic().compareAndSet(false, true)) {
                        
                        // 6. Conseguiu alocar!
                        
                        // Gerar código de reserva
                        String codigoReserva = gerarCodigoReserva(requisicao, codigoAssento);
                        
                        // Ocupar o assento (salvar o código da reserva)
                        assento.ocupar(requisicao.getNomePassageiro(), requisicao.getCategoria(), codigoReserva);
                        
                        // Decrementar contador de assentos livres
                        mapaAssentos.decrementarAssentosLivres();
                        
                        // Registrar a reserva no log
                        registrarLog("RESERVA", "Assento " + codigoAssento + " alocado para req " + requisicao.getId());
                        
                        // Retornar RespostaReserva.sucesso()
                        return RespostaReserva.sucesso(codigoAssento, codigoReserva);
                    }
                    // Se o compareAndSet falhou, significa que outra thread
                    // alocou este assento no pequeno intervalo entre o tryLock e o compareAndSet.
                    // O loop continua para o próximo assento.
                }
                // Se o lock não foi adquirido (timeout), o loop continua para o próximo assento.

            } catch (InterruptedException e) {
                // 9. Tratar InterruptedException
                Thread.currentThread().interrupt(); // Restaura a flag de interrupção
                registrarLog("ERRO", "Thread interrompida ao tentar alocar assento " + codigoAssento);
                // Retorna falha da operação atual, pois a thread foi interrompida
                return RespostaReserva.falha("OPERACAO_INTERROMPIDA", "A operação foi interrompida");
            
            } finally {
                // 7. SEMPRE liberar o lock (SE foi adquirido)
                if (lockAdquirido) {
                    lockAssento.unlock();
                }
            }
        }

        // 8. Se nenhum assento disponível (loop terminou sem retornar)
        registrarLog("INFO", "Nenhum assento livre encontrado para req " + requisicao.getId());
        return RespostaReserva.falha("VOO_LOTADO", "Voo lotado");
    }
    
    private RespostaReserva alocarAssentoEspecifico(RequisicaoReserva requisicao, String codigoAssento) {
        
        Assento assento = mapaAssentos.getAssento(codigoAssento);
        
        if (assento == null) {
            return RespostaReserva.falha("ASSENTO_INVALIDO", "Assento " + codigoAssento + " não existe");
        }
        
        try {
            if (!assento.getTrava().tryLock(5, TimeUnit.SECONDS)) {
                return RespostaReserva.falha("TIMEOUT", "Timeout ao acessar assento");
            }
            
            try {
                if (assento.estaOcupado()) {
                    return RespostaReserva.falha("ASSENTO_OCUPADO", "Assento " + codigoAssento + " já está reservado");
                }
                
                if (assento.getFlagOcupado().compareAndSet(false, true)) {
                    String codigoReserva = gerarCodigoReserva();
                    assento.ocupar(requisicao.getNomePassageiro(), requisicao.getCategoria(), codigoReserva);
                    mapaAssentos.decrementarAssentosLivres();
                    registrarReserva(requisicao, codigoAssento, codigoReserva);
                    return RespostaReserva.sucesso(codigoAssento, codigoReserva);
                } else {
                    return RespostaReserva.falha("ASSENTO_OCUPADO", "Assento " + codigoAssento + " foi reservado por outro cliente");
                }
            } finally {
                assento.getTrava().unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return RespostaReserva.falha("TIMEOUT", "Operação interrompida");
        }
    }
    
    private void enviarResposta(RequisicaoReserva requisicao, RespostaReserva resposta) {
        try {
            requisicao.getFilaResposta().put(resposta);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[AlocadorAssentos] Erro ao enviar resposta: " + e.getMessage());
        }
    }
    
    private String gerarCodigoReserva() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
    
    private void registrarReserva(RequisicaoReserva requisicao, String codigoAssento, String codigoReserva) {
        System.out.printf("[RESERVA] %s %s → %s (%s)%n", requisicao.getNomePassageiro(), requisicao.getCategoria(), codigoAssento, codigoReserva);
    }
    
    public void encerrar() {
        executando = false;
    }
}