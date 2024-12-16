    package com.example.papb.data.user

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch

    class UserViewModel(private val repository: UserRepository = UserRepository()) : ViewModel() {
        private val _registerResult = MutableStateFlow<Boolean?>(null)
        val registerResult: StateFlow<Boolean?> = _registerResult

        private val _currentUser = MutableStateFlow<User?>(null)
        val currentUser: StateFlow<User?> = _currentUser

        fun registerUser(user: User, password: String) {
            viewModelScope.launch {
                try {
                    val result = repository.registerUser(user, password)
                    _registerResult.value = result != null // true jika registrasi berhasil
                } catch (e: Exception) {
                    e.printStackTrace()
                    _registerResult.value = false // false jika gagal
                }
            }
        }

        fun fetchUser(userId: String) {
            viewModelScope.launch {
                try {
                    val user = repository.getUser(userId)
                    _currentUser.value = user
                } catch (e: Exception) {
                    e.printStackTrace()
                    _currentUser.value = null
                }
            }
        }

        fun resetRegisterResult() {
            _registerResult.value = null
        }
    }
