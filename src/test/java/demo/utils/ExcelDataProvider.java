package demo.utils;

import org.testng.annotations.DataProvider;

public class ExcelDataProvider {

@DataProvider(name = "excelData")
public static Object[][] excelData() {
    return new Object[][]{
            {"Movies"},
            {"Music"},
            {"Games"},
            {"India"},
            {"UK"}
    };
}
public static void main(String args[]){
        excelData();
    }
}