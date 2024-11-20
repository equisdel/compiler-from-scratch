@echo off
:: Verifica si se proporcionó un archivo como argumento
if "%~1"=="" (
    echo Por favor, proporciona un archivo .asm.
    echo Ejemplo: build.bat archivo.asm
    pause
    exit /b 1
)

:: Extrae el nombre del archivo sin extensión
set "filename=%~n1"

:: Compila el archivo .asm
ml /c /coff "%filename%.asm"
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

echo Compilación y enlace exitosos: %filename%.exe
pause
