.386
.model flat, stdcall
option casemap :none
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib


.data
x@main@funcion1 DW 15
y@main@funcion1@funcion2 DW ?
Resultado DB "El resultado es: ", 0
buffer DB 10 DUP(0)

.code
start:
MOV y@main@funcion1@funcion2, 20
invoke StdOut, addr Resultado
INVOKE dwtoa, x@main@funcion1, ADDR buffer
invoke StdOut, addr buffer
INVOKE dwtoa, y@main@funcion1@funcion2, ADDR buffer
invoke StdOut, addr buffer
end start