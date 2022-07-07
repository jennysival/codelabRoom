package br.com.zup.roomwordsample.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDAO {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getAlphabetizedWords(): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE) //a estratégia onConflict selecionada ignora uma nova palavra se ela for exatamente igual à que já está na lista.
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
    //declara uma função de suspensão para excluir todas as palavras.
    //Não há uma anotação de conveniência para excluir várias entidades.
    // Por isso, a anotação genérica @Query é usada.
}