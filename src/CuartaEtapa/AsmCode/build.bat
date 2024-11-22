@echo off
:: Verifica si se proporcionó un archivo como argumento
if "%~1"=="" (
    echo Por favor, proporciona un archivo .asm.
    echo Ejemplo: build.bat archivo.asm
    pause
    exit /b 1
)

:: Extrae el nombre del archivo sin extensión

:: Compila el archivo .asm
ml /c /coff "%filename%.asm" /Fo"%filename%.obj"
if errorlevel 1 (
    echo Error al compilar %filename%.asm
    pause
    exit /b 1
)

:: Enlaza el archivo compilado
link /SUBSYSTEM:CONSOLE "%filename%.obj" /OUT:"%filename%.exe"
if errorlevel 1 (
    echo Error al enlazar %filename%.obj
    pause
    exit /b 1
)

echo Compilacion y enlace exitosos: %filename%.exe
pause
