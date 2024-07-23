package anzhy.dizi.mviarchitecture

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import anzhy.dizi.mviarchitecture.api.AnimalService
import anzhy.dizi.mviarchitecture.model.Animal
import anzhy.dizi.mviarchitecture.ui.theme.MVIArchitectureTheme
import anzhy.dizi.mviarchitecture.view.MainIntent
import anzhy.dizi.mviarchitecture.view.MainState
import anzhy.dizi.mviarchitecture.view.MainViewModel
import anzhy.dizi.mviarchitecture.view.MainViewModelFactory
import coil.compose.rememberImagePainter
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(AnimalService.api)
        )[MainViewModel::class.java]

        val onButtonClick: () -> Unit = {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchAnimals)
            }
        }

        setContent {
            MVIArchitectureTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(vm = mainViewModel, onButtonClick = onButtonClick)
                }
            }
        }
    }
}

@Composable
fun MainScreen(vm: MainViewModel, onButtonClick: () -> Unit) {
    val state = vm.state.value
    when (state) {
        is MainState.Idle -> {
            IdleScreen(onButtonClick)
        }

        is MainState.Loading -> {
            LoadingScreen()
        }

        is MainState.Success -> {
            AnimalList(animals = state.animalList)
        }

        is MainState.Error -> {
            IdleScreen(onButtonClick)
            Toast.makeText(LocalContext.current, state.error, Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun IdleScreen(onButtonClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onButtonClick) {
            Text(text = "Fetch Animals")

        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun AnimalList(animals: List<Animal>) {
    LazyColumn {
        items(animals.size) { index ->
            AnimalItem(animal = animals[index])
            Divider(
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun AnimalItem(animal: Animal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val url = AnimalService.BASE_URL + animal.image
        val painter = rememberImagePainter(data = url)
        Image(
            modifier = Modifier.size(100.dp),
            painter = painter, contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 4.dp)
        ) {
            Text(
                text = animal.name,
                fontWeight = FontWeight.Bold
            )
            Text(text = animal.location)
        }
    }
}