.386
.model flat, stdcall
option casemap :none

includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib

.data
HelloWorld db "hola puta!!!", 0
printf PROTO C : VARARG
format db "Valor modificado: %f", 0
.code
start:
fld qword ptr [resultado]          ; Cargar 'resultado' en la FPU para mostrar
    sub esp, 8                         ; Reservar espacio en la pila para un double
    fstp qword ptr [esp]               ; Almacena en la pila
    push offset format                 ; Pasa el formato para mostrar
    call printf                        ; Llama a printf
    add esp, 12                        ; Limpia la pila
invoke MessageBox, NULL, addr HelloWorld, addr HelloWorld, MB_OK
invoke ExitProcess, 0
end start






