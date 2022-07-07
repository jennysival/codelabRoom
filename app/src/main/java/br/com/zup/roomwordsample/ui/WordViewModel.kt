package br.com.zup.roomwordsample.ui

import androidx.lifecycle.*
import br.com.zup.roomwordsample.data.Word
import br.com.zup.roomwordsample.domain.WordRepository
import kotlinx.coroutines.launch

// criou uma classe com o nome WordViewModel que recebe o WordRepository como parâmetro e estende
// ViewModel. O repositório é a única dependência necessária para o ViewModel. Se outras classes
// fossem necessárias, elas também seriam transmitidas no construtor;

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    //adicionou uma variável de membro LiveData pública para armazenar a lista de palavras em cache;
    //inicializou o LiveData com o fluxo allWords do repositório. Em seguida, converteu o fluxo
    // para LiveData, chamando asLiveData().
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    //criou um método wrapper insert() que chama o método insert() do repositório. Dessa forma,
    // a implementação de insert() é encapsulada na IU. Estamos inicializando uma nova corrotina
    // e chamando a inserção do repositório, que é uma função de suspensão. Como mencionado, os
    // ViewModels têm um escopo de corrotina baseado no ciclo de vida conhecido como viewModelScope,
    // que você usará aqui;
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

//criou o ViewModel e implementou uma ViewModelProvider.Factory que tem como um parâmetro as
// dependências necessárias para criar WordViewModel: o WordRepository.
//Ao usar viewModels e ViewModelProvider.Factory, o framework cuidará do ciclo de vida do ViewModel.
// Ele sobreviverá a mudanças de configuração e, mesmo que a atividade seja recriada, você sempre
// receberá a instância correta da classe WordViewModel
class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}