package br.com.zup.roomwordsample.domain

import androidx.annotation.WorkerThread
import br.com.zup.roomwordsample.data.Word
import br.com.zup.roomwordsample.data.WordDAO
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO

// O DAO é transmitido ao construtor do repositório, e não ao banco de dados inteiro.
// Isso ocorre porque só é necessário acessar o DAO, já que ele contém todos os métodos
// de leitura/gravação do banco de dados. Não é necessário expor tod.o o banco de dados
// ao repositório.

class WordRepository(private val wordDao: WordDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.

    // A lista de palavras é uma propriedade pública. Ela é inicializada com a lista de palavras
    // do Flow do Room. É possível fazer isso devido à forma como você definiu o método
    // getAlphabetizedWords para retornar um Flow na etapa "Como observar mudanças no banco de dados".
    // O Room executa todas as consultas em uma linha de execução separada.

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.

    //O modificador suspend informa ao compilador que ele precisa ser chamado por uma corrotina
    // ou outra função de suspensão.

    //A Room executa consultas de suspensão fora da linha de execução principal.

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}