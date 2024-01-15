package com.onemb.unitconverter

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onemb.unitconverter.ui.theme.UnitConverterTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UnitConverterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(red = 250, green = 250, blue = 250)
                ) {
                    UnitConverter()
                }
            }
        }
    }
}

@Composable
fun UnitConverter() {
    val inputDropDownListItems : List<String> = listOf(
        "meters",
        "kilometers",
        "centimeters",
        "millimeters",
        "miles",
        "yards",
        "feet",
        "inches"
    );
    val iDropOpen  = remember{mutableStateOf(false)};
    val oDropOpen =  remember{mutableStateOf(false)};
    val inputDropDownselectItem = remember{mutableStateOf("Select")};
    val outputDropDownselectItem = remember{mutableStateOf("Select")};
    val inputBoxValue = remember{ mutableStateOf("") };
    val result = remember {
        mutableStateOf("");
    };
    val displayConvertBtn = remember {
        mutableStateOf(false)
    }
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier
            .offset(x = 0.dp, y = (-56).dp)
            .scale(scaleX = 2F, scaleY = 2.5F), text = "Basic Unit Converter");
        heightSpace();
        OutlinedTextField(
            value = inputBoxValue.value,
            onValueChange = {
                System.out.println(it)
                try {
                    displayConvertBtn.value = true;
                    System.out.println(it.toDouble())
                } catch (err: Throwable) {
                    displayConvertBtn.value = false;
                    System.out.println(err)
                }

                inputBoxValue.value = it;
            },
            label = { Text(text = "Enter Value", color = Color.Black)},
            colors =  TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
        heightSpace();
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            inputBox(iDropOpen, inputDropDownListItems, inputDropDownselectItem);
            OutPutBox(oDropOpen, inputDropDownListItems, outputDropDownselectItem);
        }
        heightSpace();
        if(
            inputDropDownselectItem.value == outputDropDownselectItem.value &&
            inputDropDownselectItem.value != "Select" &&
            outputDropDownselectItem.value != "Select"
        ) {
            ErrorUi()
        } 
        heightSpace();
        if(!inputDropDownselectItem.value.equals(outputDropDownselectItem.value) && displayConvertBtn.value) {
            Button(onClick = {
                doConversion(
                    inputDropDownselectItem,
                    outputDropDownselectItem,
                    result,
                    inputBoxValue
                )
            }) {
                Text(text = "Convert")
            }
        }
        heightSpace();
        if(!result.value.equals("") && displayConvertBtn.value) {
            Text(text = "There are " + result.value + " " + outputDropDownselectItem.value + " in " + inputBoxValue.value + " " + inputDropDownselectItem.value);
        }
        
    }
}

@Composable
fun inputBox(iDropOpen: MutableState<Boolean>, inputDropDownListItems: List<String>, inputDropDownselectItem: MutableState<String>) {
    Box {
        Column {
            Text(text = "Convert From:")
            Button(onClick = {iDropOpen.value = iDropOpen.value.not()}) {
                Text(text = inputDropDownselectItem.value.uppercase())
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Drop down for input unit")
            }
            DropdownMenu(expanded = iDropOpen.value, onDismissRequest = {
                iDropOpen.value = iDropOpen.value.not();
            }) {
                for(item in inputDropDownListItems) {
                    menuitem(item, inputDropDownselectItem, iDropOpen);
                }
            }
        }
    }
}

@Composable
fun OutPutBox(oDropOpen: MutableState<Boolean>, inputDropDownListItems: List<String>, outputDropDownselectItem: MutableState<String>) {
    Box {
        Column {
            Text(text = "Convert To:")
            Button(onClick = {oDropOpen.value = oDropOpen.value.not()}) {
                Text(text = outputDropDownselectItem.value.uppercase())
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Drop down for output unit")
            }
            DropdownMenu(expanded = oDropOpen.value, onDismissRequest = {
                oDropOpen.value = oDropOpen.value.not();
            }) {
                        for (item in inputDropDownListItems) {
                            menuitem(item, outputDropDownselectItem, oDropOpen);
                        }
            }
        }
    }
}

@Composable
fun menuitem(itemText: String, inputValue: MutableState<String>, dropOpen: MutableState<Boolean>) {
    DropdownMenuItem(
        text = {Text(itemText.uppercase(locale = Locale.US), color = Color.Black)},
        onClick =  {
            inputValue.value = itemText;
            dropOpen.value = !dropOpen.value;
        },
        modifier = Modifier.background(Color.White)
    )
}

@Composable
fun ErrorUi() {
    Text(text = "Both Select values cannot be same!", color = Color.Red)
}

@Composable
fun heightSpace() {
    Spacer(modifier = Modifier.height(16.dp))
}

fun doConversion(inputDropDownselectItem:MutableState<String>, outputDropDownselectItem:MutableState<String>, result:MutableState<String>, inputValue: MutableState<String>) {
    val metersInKilometers = 1000.0
    val metersInCentimeters = 0.01
    val metersInMillimeters = 0.001
    val metersInMiles = 1609.34
    val metersInYards = 0.9144
    val metersInFeet = 0.3048
    val metersInInches = 0.0254

    val fromUnitLower = inputDropDownselectItem.value.lowercase()
    val toUnitLower = outputDropDownselectItem.value.lowercase()
    val numInputValue = inputValue.value.toDouble();
    val metersValue = when (fromUnitLower) {
        "meters" -> numInputValue
        "kilometers" -> numInputValue * metersInKilometers
        "centimeters" -> numInputValue * metersInCentimeters
        "millimeters" -> numInputValue * metersInMillimeters
        "miles" -> numInputValue * metersInMiles
        "yards" -> numInputValue * metersInYards
        "feet" -> numInputValue * metersInFeet
        "inches" -> numInputValue * metersInInches
        else -> {
            return  // Or throw an exception
        }
    }

    result.value =  when (toUnitLower) {
        "meters" -> metersValue
        "kilometers" ->  metersValue / metersInKilometers
        "centimeters" -> metersValue / metersInCentimeters
        "millimeters" -> metersValue / metersInMillimeters
        "miles" -> metersValue / metersInMiles
        "yards" -> metersValue / metersInYards
        "feet" -> metersValue / metersInFeet
        "inches" -> metersValue / metersInInches
        else -> {
            println("Invalid to unit")
            "0.0" // Or throw an exception
        }
    }.toString()

}

@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverter();
}
