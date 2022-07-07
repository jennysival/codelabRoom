package br.com.zup.roomwordsample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class

// Use @Database para anotar a classe como um banco de dados da Room e usar os parâmetros
// de anotação para declarar as entidades que pertencem ao banco de dados e definir o número
// da versão. Cada entidade corresponde a uma tabela que será criada no banco de dados.
// As migrações de banco de dados estão além do escopo deste codelab, portanto, exportSchema
// foi definido como falso para evitar um aviso de compilação. Em um app real, considere configurar
// um diretório para o Room usar e exportar o esquema para que você possa verificar o esquema atual
// no seu sistema de controle de versões.

@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDAO

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDAO) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        // Você definiu um singleton, WordRoomDatabase, para evitar que várias
        // instâncias do banco de dados sejam abertas ao mesmo tempo.
        // getDatabase retorna o Singleton. Ele criará o banco de dados na primeira vez que for
        // acessado, usando o builder do banco de dados da Room para criar um objeto RoomDatabase
        // no contexto do aplicativo da classe WordRoomDatabase e o nomeará como "word_database"

        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope)).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    //O preenchimento do banco de dados não está relacionado ao ciclo de vida da IU. Portanto,
    // não use um CoroutineScope como o viewModelScope, porque ele está relacionado ao ciclo de vida
    // do app. Você atualizará o WordsApplication para conter um applicationScope e, depois,
    // transmitir esse valor para o WordRoomDatabase.getDatabase
    }
}