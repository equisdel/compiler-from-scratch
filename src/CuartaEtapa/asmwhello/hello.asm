.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
.data
; volcar TS (clean, quitar cte o cosas sin scope)
; las single podemos dejarlas
; la .data hacerla despues de generar el .code, asi sabemos bien todas las variables que van (con las aux)
; CREO se limpia la TS, quitando cosas sin scope como cte y  quitando el scope a todo, pero diferenciando todo lexema
; con variables dif en el assembler (ej: main:f1:x es _f1x y main:x es _x  la logica del scope no se usa mas, se accede directo)
; en assembler no se chequea scope, y se asume se chequeó bien en semantica, asiq podemos ponerle cualq nombre a las variables, mientras sean distinto

;Las cadenas de texto son simplemente una secuencia de bytes en memoria. 
;Sin una marca de finalización, el programa no sabría dónde termina la cadena, ya que no hay un "tamaño" explícito asociado.
;El carácter 0 (nulo o NULL) sirve como una señal que indica que la cadena ha llegado a su fin.



 HelloWorld db "Hola mundo!", 0
.code
start:
 invoke StdOut, addr HelloWorld
 invoke ExitProcess, 0
end start