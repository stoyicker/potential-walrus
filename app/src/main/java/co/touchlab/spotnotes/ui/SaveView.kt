package co.touchlab.spotnotes.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import co.touchlab.spotnotes.MainActivity.Screen
import co.touchlab.spotnotes.MainViewModel

@Composable
fun SaveView(onSaveClicked: (text:String)-> Unit){
    var text by rememberSaveable { mutableStateOf("") }
    Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    , verticalArrangement = Arrangement.Center) {
        Text(text = "Save your location with a note!",
                modifier = Modifier.padding(vertical = 8.dp))

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Note") },
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(onClick = {
            onSaveClicked(text)
         },
            modifier = Modifier.padding(vertical = 8.dp)) {
            Text(text = "Save")
        }
    }
}

@Composable
@Preview
fun Preview_SaveView(){
    SaveView({ })
}