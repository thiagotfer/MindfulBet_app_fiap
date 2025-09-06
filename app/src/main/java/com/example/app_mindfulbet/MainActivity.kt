package com.example.mindfulbet_app// MindfulBet.kt
// Versão atualizada do código do aplicativo "Mindful Bet" com um dashboard aprimorado.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Definição das rotas de navegação para as telas
sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object Limits : Screen("limits", "Limites")
    object Journal : Screen("journal", "Diário")
    object PanicButton : Screen("panicButton", "Pânico")
    object Support : Screen("support", "Suporte")
}

// ----------------------------------------------------
// MainActivity
// Ponto de entrada do aplicativo.
// ----------------------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                MainApp()
            }
        }
    }
}

// ----------------------------------------------------
// Componente principal do aplicativo
// Gerencia a navegação entre as telas
// ----------------------------------------------------
@Composable
fun MainApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.Limits.route) { LimitsScreen() }
            composable(Screen.Journal.route) { JournalScreen() }
            composable(Screen.PanicButton.route) { PanicButtonScreen() }
            composable(Screen.Support.route) { SupportScreen() }
        }
    }
}


// Barra de navegação inferior

@Composable
fun AppBottomBar(navController: NavController) {
    NavigationBar {
        val navItems = listOf(
            Screen.Dashboard,
            Screen.Limits,
            Screen.Journal,
            Screen.PanicButton,
            Screen.Support
        )
        navItems.forEach { screen ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = {
                    when (screen) {
                        Screen.Dashboard -> Icon(Icons.Default.Home, contentDescription = screen.title)
                        Screen.Limits -> Icon(Icons.Default.Settings, contentDescription = screen.title)
                        Screen.Journal -> Icon(Icons.AutoMirrored.Filled.List, contentDescription = screen.title)
                        Screen.PanicButton -> Icon(Icons.Default.Refresh, contentDescription = screen.title)
                        Screen.Support -> Icon(Icons.Default.Place, contentDescription = screen.title)
                    }
                },
                label = { Text(screen.title) }
            )
        }
    }
}


// Tela 1: Dashboard Financeiro e de Tempo

@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Dashboard",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DashboardCard(title = "Dias Sem Jogar", value = "15")
            DashboardCard(title = "Tempo de Jogo (Semana)", value = "2h 45min")
            DashboardCard(title = "Perdas Acumuladas (Mês)", value = "R$ 520,00")
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


// Tela 2: Configuração de Limites

@Composable
fun LimitsScreen() {
    var timeLimit by remember { mutableStateOf("") }
    var spendingLimit by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Definir Limites", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 24.dp))
        OutlinedTextField(
            value = timeLimit,
            onValueChange = { timeLimit = it },
            label = { Text("Limite de Tempo") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        OutlinedTextField(
            value = spendingLimit,
            onValueChange = { spendingLimit = it },
            label = { Text("Limite de Gasto") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Lógica para salvar */ }) {
            Text("Salvar Limites")
        }
    }
}


// Tela 3: Diário de Sentimentos e Gatilhos

@Composable
fun JournalScreen() {
    var journalEntry by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Diário de Sentimentos", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        Text("Como me sinto agora?", modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = { /* Clicou em Feliz */ }) { Text("Feliz") }
            Button(onClick = { /* Clicou em Triste */ }) { Text("Triste") }
            Button(onClick = { /* Clicou em Ansioso */ }) { Text("Ansioso") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("O que me fez pensar em apostar?", modifier = Modifier.padding(8.dp))

        OutlinedTextField(
            value = journalEntry,
            onValueChange = { journalEntry = it },
            label = { Text("Descreva seu gatilho...") },
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Lógica para salvar */ }) {
            Text("Salvar Entrada")
        }
    }
}


// Tela 4: "Botão de Pânico" e Atividades de Desvio

@Composable
fun PanicButtonScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { /* Lógica do botão */ },
            modifier = Modifier.size(200.dp)
        ) {
            Text("Preciso de Ajuda Agora", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Sugestões de Atividades:", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        Text("Faça 10 minutos de meditação", modifier = Modifier.padding(4.dp))
        Text("Caminhe pelo quarteirão", modifier = Modifier.padding(4.dp))
        Text("Assista a um vídeo engraçado", modifier = Modifier.padding(4.dp))
    }
}


// Tela 5: Recursos de Suporte Profissional

@Composable
fun SupportScreen() {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Recursos de Suporte", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        Button(onClick = { uriHandler.openUri("https://www.gamblersanonymous.org") }) {
            Text("Jogadores Anônimos")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { uriHandler.openUri("https://www.cvv.org.br") }) {
            Text("Centro de Valorização da Vida (CVV)")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { uriHandler.openUri("https://www.psicologiaviva.com.br") }) {
            Text("Psicólogos Online")
        }
    }
}


// Tema simplificado

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFFF0F0F0),
            surface = Color(0xFFFFFFFF),
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black,
            onSurface = Color.Black
        ),
        content = content
    )
}
